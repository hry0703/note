"""
RAG 核心逻辑：文档加载、分块、向量化、检索、生成
"""
import os
from pathlib import Path
from typing import Optional

import numpy as np
from openai import OpenAI


# 分块配置
CHUNK_SIZE = 500
CHUNK_OVERLAP = 50
TOP_K = 5
EMBEDDING_MODEL = "text-embedding-3-small"
CHAT_MODEL = "qwen3.5-27b"


def load_documents(docs_dir: str) -> list[tuple[str, str]]:
    """加载 documents 目录下所有 .md 文件"""
    docs_dir = Path(docs_dir)
    if not docs_dir.exists():
        return []

    result = []
    for path in docs_dir.glob("*.md"):
        if path.name.startswith("README"):
            continue
        try:
            text = path.read_text(encoding="utf-8")
            result.append((path.name, text))
        except Exception:
            pass
    return result


def chunk_text(text: str, chunk_size: int = CHUNK_SIZE, overlap: int = CHUNK_OVERLAP) -> list[str]:
    """按字符数分块，保留一定重叠"""
    text = text.strip()
    if not text:
        return []

    chunks = []
    start = 0
    while start < len(text):
        end = start + chunk_size
        chunk = text[start:end]
        if chunk.strip():
            chunks.append(chunk.strip())
        start = end - overlap
    return chunks


def get_client() -> OpenAI:
    """获取 OpenAI 客户端"""
    api_key = os.getenv("OPENAI_API_KEY")
    base_url = os.getenv("OPENAI_BASE_URL")
    if not api_key:
        raise ValueError("请设置环境变量 OPENAI_API_KEY")
    kwargs = {"api_key": api_key}
    if base_url:
        # 代理 API 通常需要 /v1 后缀（如 dmxapi.cn）
        base_url = base_url.rstrip("/")
        if not base_url.endswith("/v1"):
            base_url = f"{base_url}/v1"
        kwargs["base_url"] = base_url
    return OpenAI(**kwargs)


class RAGAgent:
    """文档问答 RAG Agent"""

    def __init__(self, docs_dir: str = "documents"):
        self.docs_dir = Path(docs_dir)
        self.chunks: list[dict] = []  # [{"text": str, "source": str, "embedding": list}]
        self.client: Optional[OpenAI] = None
        self._build_index()

    def _build_index(self) -> None:
        """加载文档、分块、向量化"""
        docs = load_documents(str(self.docs_dir))
        if not docs:
            raise ValueError(f"未找到文档，请确保 {self.docs_dir} 目录下有 .md 文件")

        all_chunks = []
        for name, content in docs:
            for chunk in chunk_text(content):
                all_chunks.append({"text": chunk, "source": name})

        if not all_chunks:
            raise ValueError("文档内容为空")

        self.client = get_client()
        texts = [c["text"] for c in all_chunks]

        # 批量获取 embedding（OpenAI 单次最多 2048 个 input）
        batch_size = 100
        for i in range(0, len(texts), batch_size):
            batch = texts[i : i + batch_size]
            resp = self.client.embeddings.create(input=batch, model=EMBEDDING_MODEL)
            for j, emb in enumerate(resp.data):
                idx = i + j
                all_chunks[idx]["embedding"] = emb.embedding

        self.chunks = all_chunks

    def _cosine_similarity(self, a: list[float], b: list[float]) -> float:
        """余弦相似度"""
        va, vb = np.array(a), np.array(b)
        return float(np.dot(va, vb) / (np.linalg.norm(va) * np.linalg.norm(vb) + 1e-9))

    def search(self, query: str, top_k: int = TOP_K) -> list[dict]:
        """检索与问题最相关的文档块"""
        resp = self.client.embeddings.create(input=query, model=EMBEDDING_MODEL)
        q_emb = resp.data[0].embedding

        scored = [
            (self._cosine_similarity(q_emb, c["embedding"]), c)
            for c in self.chunks
        ]
        scored.sort(key=lambda x: x[0], reverse=True)
        return [item for _, item in scored[:top_k]]

    def ask(self, question: str, top_k: int = TOP_K) -> tuple[str, list[dict]]:
        """基于检索结果生成回答"""
        results = self.search(question, top_k=top_k)
        context = "\n\n---\n\n".join(
            f"【来源：{r['source']}】\n{r['text']}" for r in results
        )

        prompt = f"""你是一个文档问答助手。请根据以下文档片段回答用户问题。
                如果文档中没有相关信息，请如实说明。

                ## 参考文档
                {context}

                ## 用户问题
                {question}

                ## 回答要求
                - 基于文档内容回答，不要编造
                - 回答简洁清晰
                - 若文档中无相关信息，回复「文档中暂无相关信息」
                """

        resp = self.client.chat.completions.create(
            model=CHAT_MODEL,
            messages=[{"role": "user", "content": prompt}],
            temperature=0.3,
        )
        answer = resp.choices[0].message.content.strip()

        sources = [{"source": r["source"], "text": r["text"][:200]} for r in results]
        return answer, sources
