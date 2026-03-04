"""
文档问答 RAG Agent - FastAPI 入口
"""
from pathlib import Path

from dotenv import load_dotenv
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel

from rag_core import RAGAgent

load_dotenv()

# 项目根目录
BASE_DIR = Path(__file__).resolve().parent
DOCS_DIR = BASE_DIR / "documents"

app = FastAPI(title="文档问答 RAG Agent", description="基于本地文档的智能问答")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:5173", "http://127.0.0.1:5173"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 启动时构建索引（可改为懒加载）
agent = None
_init_error = "初始化失败"
try:
    agent = RAGAgent(docs_dir=str(DOCS_DIR))
except Exception as e:
    _init_error = str(e)
    import traceback
    print(f"[RAG] 初始化失败: {e}")
    traceback.print_exc()


class AskRequest(BaseModel):
    question: str


class AskResponse(BaseModel):
    answer: str
    sources: list[dict]


@app.get("/")
def root():
    return {
        "message": "文档问答 RAG Agent",
        "docs": "http://localhost:8002/docs",
        "ask": "POST /ask",
    }


@app.get("/health")
def health():
    return {"status": "ok", "agent_ready": agent is not None}


@app.post("/ask", response_model=AskResponse)
def ask(req: AskRequest):
    if agent is None:
        raise HTTPException(status_code=503, detail=_init_error)
    if not req.question.strip():
        raise HTTPException(status_code=400, detail="问题不能为空")

    try:
        answer, sources = agent.ask(req.question)
        return AskResponse(answer=answer, sources=sources)
    except Exception as e:
        import traceback
        print(f"[ask] 500 错误: {e}")
        traceback.print_exc()
        raise HTTPException(status_code=500, detail=str(e))


if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8002)
