# 05 - RAG 系统开发

> **前置条件**：完成 LangChain 框架学习  
> **学习时长**：2-3 周  
> **学习目标**：深入理解 RAG 原理，能够构建生产级 RAG 系统

---

## 🎯 什么是 RAG？

### RAG（Retrieval-Augmented Generation）检索增强生成

**核心思想**：
```
用户问题 → 检索相关文档 → 将文档作为上下文 → LLM 生成回答
```

**为什么需要 RAG？**
- ✅ **解决 LLM 知识过时问题**：实时获取最新信息
- ✅ **减少幻觉**：基于真实文档生成答案
- ✅ **支持私有数据**：企业内部文档、个人笔记
- ✅ **降低成本**：无需重新训练模型

---

## 📚 RAG 系统架构

### 基础架构

```
┌─────────────┐
│  文档加载    │ → PDF、Word、网页、数据库
└──────┬──────┘
       ↓
┌─────────────┐
│  文本分块    │ → 智能分割，保持语义完整
└──────┬──────┘
       ↓
┌─────────────┐
│  向量化      │ → Embeddings 模型
└──────┬──────┘
       ↓
┌─────────────┐
│  向量存储    │ → Chroma、Pinecone、Weaviate
└──────┬──────┘
       ↓
┌─────────────┐     ┌─────────────┐
│  用户问题    │ ──→ │  向量检索    │
└─────────────┘     └──────┬──────┘
                           ↓
                    ┌─────────────┐
                    │  生成回答    │ → GPT-4
                    └─────────────┘
```

---

## 📚 学习内容

### 1. 文档分块策略

#### 1.1 基础分块

```python
from langchain.text_splitter import RecursiveCharacterTextSplitter

text_splitter = RecursiveCharacterTextSplitter(
    chunk_size=1000,        # 块大小
    chunk_overlap=200,      # 重叠部分
    length_function=len,    # 长度计算函数
    separators=["\n\n", "\n", "。", "！", "？", " ", ""]
)

chunks = text_splitter.split_text(long_text)
```

---

#### 1.2 智能分块（根据内容结构）

```python
from langchain.text_splitter import MarkdownHeaderTextSplitter

# Markdown 结构化分块
markdown_splitter = MarkdownHeaderTextSplitter(
    headers_to_split_on=[
        ("#", "Header 1"),
        ("##", "Header 2"),
        ("###", "Header 3"),
    ]
)

# 保留文档结构
md_header_splits = markdown_splitter.split_text(markdown_text)
```

---

#### 1.3 语义分块

```python
from langchain.text_splitter import SemanticChunker
from langchain_openai import OpenAIEmbeddings

# 基于语义相似度分块
semantic_chunker = SemanticChunker(
    OpenAIEmbeddings(),
    breakpoint_threshold_type="percentile",  # 或 "standard_deviation"
    breakpoint_threshold_amount=0.95
)

semantic_chunks = semantic_chunker.split_text(text)
```

---

### 2. 向量数据库对比

| 数据库 | 类型 | 特点 | 适用场景 |
|--------|------|------|---------|
| **Chroma** | 本地/云 | 轻量级、易用 | 开发测试 |
| **FAISS** | 本地 | 高性能、内存 | 高性能检索 |
| **Pinecone** | 云服务 | 托管、可扩展 | 生产环境 |
| **Weaviate** | 本地/云 | GraphQL、混合搜索 | 复杂查询 |
| **Milvus** | 本地/云 | 高性能、分布式 | 大规模应用 |

---

### 3. 向量检索优化

#### 3.1 混合检索（Hybrid Search）

```python
from langchain.retrievers import EnsembleRetriever
from langchain.retrievers import BM25Retriever
from langchain.vectorstores import Chroma

# 向量检索器
vectorstore = Chroma.from_documents(documents, embeddings)
vector_retriever = vectorstore.as_retriever(search_kwargs={"k": 5})

# 关键词检索器（BM25）
bm25_retriever = BM25Retriever.from_documents(documents)
bm25_retriever.k = 5

# 集成检索器（混合）
ensemble_retriever = EnsembleRetriever(
    retrievers=[vector_retriever, bm25_retriever],
    weights=[0.5, 0.5]  # 权重分配
)

# 检索
results = ensemble_retriever.get_relevant_documents("查询内容")
```

---

#### 3.2 重排序（Re-ranking）

```python
from langchain.retrievers import ContextualCompressionRetriever
from langchain.retrievers.document_compressors import LLMChainExtractor

# 基础检索器
base_retriever = vectorstore.as_retriever(search_kwargs={"k": 10})

# LLM 压缩器（重排序）
compressor = LLMChainExtractor.from_llm(llm)

# 压缩检索器
compression_retriever = ContextualCompressionRetriever(
    base_compressor=compressor,
    base_retriever=base_retriever
)

# 只返回最相关的内容
compressed_docs = compression_retriever.get_relevant_documents("查询内容")
```

---

#### 3.3 多查询检索

```python
from langchain.retrievers.multi_query import MultiQueryRetriever

# 自动生成多个相关查询
multi_query_retriever = MultiQueryRetriever.from_llm(
    retriever=vectorstore.as_retriever(),
    llm=llm
)

# 一个问题，多个角度检索
results = multi_query_retriever.get_relevant_documents(
    "Python 的优势是什么？"
)
# LLM 会自动生成：
# - "Python 的好处有哪些？"
# - "为什么选择 Python？"
# - "Python 相比其他语言的优点"
```

---

### 4. RAG 链类型

#### 4.1 Stuff Chain（填充链）

```python
from langchain.chains import RetrievalQA

# 最简单：将所有文档填充到提示词中
qa_chain = RetrievalQA.from_chain_type(
    llm=llm,
    chain_type="stuff",  # 适合文档数量少
    retriever=retriever
)

result = qa_chain.run("问题")
```

**优点**：简单、快速  
**缺点**：文档多时超出上下文窗口

---

#### 4.2 Map-Reduce Chain

```python
# 分别处理每个文档，然后合并结果
qa_chain = RetrievalQA.from_chain_type(
    llm=llm,
    chain_type="map_reduce",  # 适合大量文档
    retriever=retriever
)
```

**工作流程**：
```
文档1 → LLM → 答案1 ┐
文档2 → LLM → 答案2 ├→ LLM → 最终答案
文档3 → LLM → 答案3 ┘
```

**优点**：可处理大量文档  
**缺点**：多次 LLM 调用，成本高

---

#### 4.3 Refine Chain

```python
# 迭代优化答案
qa_chain = RetrievalQA.from_chain_type(
    llm=llm,
    chain_type="refine",  # 逐步优化答案
    retriever=retriever
)
```

**工作流程**：
```
初始答案 + 文档1 → 改进答案1
改进答案1 + 文档2 → 改进答案2
改进答案2 + 文档3 → 最终答案
```

---

### 5. 高级 RAG 技术

#### 5.1 Parent Document Retriever

```python
from langchain.retrievers import ParentDocumentRetriever
from langchain.storage import InMemoryStore

# 小块检索，大块返回
parent_splitter = RecursiveCharacterTextSplitter(chunk_size=2000)
child_splitter = RecursiveCharacterTextSplitter(chunk_size=400)

vectorstore = Chroma(embedding_function=embeddings)
store = InMemoryStore()

retriever = ParentDocumentRetriever(
    vectorstore=vectorstore,
    docstore=store,
    child_splitter=child_splitter,
    parent_splitter=parent_splitter,
)

retriever.add_documents(documents)
```

**优点**：检索精确（小块），上下文完整（大块）

---

#### 5.2 Self-Query Retriever

```python
from langchain.retrievers.self_query.base import SelfQueryRetriever
from langchain.chains.query_constructor.base import AttributeInfo

# 元数据定义
metadata_field_info = [
    AttributeInfo(
        name="author",
        description="文档作者",
        type="string"
    ),
    AttributeInfo(
        name="year",
        description="发布年份",
        type="integer"
    ),
]

document_content_description = "技术文档集合"

# 自查询检索器
retriever = SelfQueryRetriever.from_llm(
    llm=llm,
    vectorstore=vectorstore,
    document_contents=document_content_description,
    metadata_field_info=metadata_field_info,
    verbose=True
)

# 自动从问题中提取过滤条件
results = retriever.get_relevant_documents(
    "2023 年关于 Python 的文档"
)
```

---

### 6. RAG 评估

#### 6.1 评估指标

```python
from ragas import evaluate
from ragas.metrics import (
    faithfulness,          # 忠实度（答案是否基于文档）
    answer_relevancy,      # 答案相关性
    context_precision,     # 上下文精确度
    context_recall,        # 上下文召回率
)

# 评估数据集
eval_dataset = {
    "question": ["问题1", "问题2"],
    "answer": ["答案1", "答案2"],
    "contexts": [["文档1", "文档2"], ["文档3"]],
    "ground_truths": [["标准答案1"], ["标准答案2"]]
}

# 运行评估
result = evaluate(
    dataset=eval_dataset,
    metrics=[
        faithfulness,
        answer_relevancy,
        context_precision,
        context_recall,
    ],
)

print(result)
```

---

#### 6.2 自定义评估

```python
class RAGEvaluator:
    def __init__(self, qa_chain, llm):
        self.qa_chain = qa_chain
        self.llm = llm
    
    def evaluate_answer_quality(self, question, answer, ground_truth):
        """评估答案质量"""
        prompt = f"""
        问题：{question}
        生成答案：{answer}
        标准答案：{ground_truth}
        
        请评估生成答案的质量（0-10分），并说明理由。
        """
        
        response = self.llm.invoke(prompt)
        return response.content
    
    def evaluate_retrieval(self, question, retrieved_docs, relevant_docs):
        """评估检索质量"""
        retrieved_ids = set([doc.metadata.get('id') for doc in retrieved_docs])
        relevant_ids = set(relevant_docs)
        
        precision = len(retrieved_ids & relevant_ids) / len(retrieved_ids)
        recall = len(retrieved_ids & relevant_ids) / len(relevant_ids)
        f1 = 2 * (precision * recall) / (precision + recall) if (precision + recall) > 0 else 0
        
        return {
            "precision": precision,
            "recall": recall,
            "f1_score": f1
        }
```

---

### 7. 生产级 RAG 系统

#### 7.1 完整实现

```python
from langchain_openai import ChatOpenAI, OpenAIEmbeddings
from langchain.text_splitter import RecursiveCharacterTextSplitter
from langchain.vectorstores import Chroma
from langchain.chains import ConversationalRetrievalChain
from langchain.memory import ConversationBufferMemory
from langchain.document_loaders import DirectoryLoader, PyPDFLoader
import os

class ProductionRAG:
    def __init__(
        self,
        model="gpt-4",
        chunk_size=1000,
        chunk_overlap=200,
        k_documents=5
    ):
        self.llm = ChatOpenAI(model=model, temperature=0)
        self.embeddings = OpenAIEmbeddings()
        self.chunk_size = chunk_size
        self.chunk_overlap = chunk_overlap
        self.k_documents = k_documents
        
        self.vectorstore = None
        self.qa_chain = None
        self.memory = ConversationBufferMemory(
            memory_key="chat_history",
            return_messages=True,
            output_key="answer"
        )
    
    def load_directory(self, directory_path):
        """加载目录下的所有文档"""
        # PDF 文档
        pdf_loader = DirectoryLoader(
            directory_path,
            glob="**/*.pdf",
            loader_cls=PyPDFLoader
        )
        
        # 文本文档
        txt_loader = DirectoryLoader(
            directory_path,
            glob="**/*.txt",
            loader_cls=lambda path: TextLoader(path, encoding='utf-8')
        )
        
        documents = []
        documents.extend(pdf_loader.load())
        documents.extend(txt_loader.load())
        
        # 分块
        text_splitter = RecursiveCharacterTextSplitter(
            chunk_size=self.chunk_size,
            chunk_overlap=self.chunk_overlap,
            separators=["\n\n", "\n", "。", "！", "？", " ", ""]
        )
        
        splits = text_splitter.split_documents(documents)
        
        # 创建向量存储
        self.vectorstore = Chroma.from_documents(
            documents=splits,
            embedding=self.embeddings,
            persist_directory="./production_rag_db"
        )
        
        # 创建问答链
        self.qa_chain = ConversationalRetrievalChain.from_llm(
            llm=self.llm,
            retriever=self.vectorstore.as_retriever(
                search_type="mmr",  # Maximum Marginal Relevance
                search_kwargs={"k": self.k_documents, "fetch_k": 20}
            ),
            memory=self.memory,
            return_source_documents=True,
            verbose=True
        )
        
        return f"成功加载 {len(documents)} 个文档，分割成 {len(splits)} 个块"
    
    def ask(self, question):
        """提问"""
        if not self.qa_chain:
            return {"error": "请先加载文档"}
        
        result = self.qa_chain({"question": question})
        
        return {
            "answer": result['answer'],
            "sources": [
                {
                    "content": doc.page_content[:200],
                    "metadata": doc.metadata
                }
                for doc in result['source_documents']
            ]
        }
    
    def clear_memory(self):
        """清空对话历史"""
        self.memory.clear()
    
    def add_documents(self, new_documents):
        """动态添加文档"""
        text_splitter = RecursiveCharacterTextSplitter(
            chunk_size=self.chunk_size,
            chunk_overlap=self.chunk_overlap
        )
        
        splits = text_splitter.split_documents(new_documents)
        self.vectorstore.add_documents(splits)
        
        return f"添加了 {len(splits)} 个新文档块"

# 使用示例
rag_system = ProductionRAG(model="gpt-4", k_documents=5)

# 加载文档
print(rag_system.load_directory("./documents"))

# 提问
response = rag_system.ask("这些文档的主要内容是什么？")
print(f"答案：{response['answer']}")
print(f"来源：{response['sources']}")

# 继续对话
response = rag_system.ask("能详细解释一下吗？")
print(f"答案：{response['answer']}")
```

---

#### 7.2 FastAPI 集成

```python
from fastapi import FastAPI, HTTPException, UploadFile, File
from pydantic import BaseModel
import shutil

app = FastAPI()
rag_system = ProductionRAG()

class Question(BaseModel):
    question: str

@app.post("/upload")
async def upload_document(file: UploadFile = File(...)):
    """上传文档"""
    try:
        # 保存文件
        file_path = f"./uploads/{file.filename}"
        with open(file_path, "wb") as buffer:
            shutil.copyfileobj(file.file, buffer)
        
        # 加载文档
        loader = PyPDFLoader(file_path) if file.filename.endswith('.pdf') else TextLoader(file_path)
        documents = loader.load()
        result = rag_system.add_documents(documents)
        
        return {"message": result}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/ask")
async def ask_question(question: Question):
    """提问"""
    try:
        response = rag_system.ask(question.question)
        return response
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.delete("/memory")
async def clear_memory():
    """清空对话历史"""
    rag_system.clear_memory()
    return {"message": "对话历史已清空"}
```

---

### 8. RAG 优化技巧

#### 8.1 提示词优化

```python
# 自定义 RAG 提示词
from langchain.prompts import PromptTemplate

template = """
你是一个专业的文档分析助手。请基于以下文档内容回答问题。

规则：
1. 只使用提供的文档内容回答
2. 如果文档中没有相关信息，明确说明
3. 引用具体的文档内容
4. 保持回答简洁准确

文档内容：
{context}

问题：{question}

回答：
"""

PROMPT = PromptTemplate(
    template=template,
    input_variables=["context", "question"]
)

qa_chain = RetrievalQA.from_chain_type(
    llm=llm,
    chain_type="stuff",
    retriever=retriever,
    chain_type_kwargs={"prompt": PROMPT}
)
```

---

#### 8.2 元数据过滤

```python
# 添加元数据
documents = [
    Document(
        page_content="内容",
        metadata={
            "source": "document.pdf",
            "page": 1,
            "category": "技术",
            "date": "2024-01-01"
        }
    )
]

# 根据元数据过滤
retriever = vectorstore.as_retriever(
    search_kwargs={
        "k": 5,
        "filter": {"category": "技术"}
    }
)
```

---

#### 8.3 缓存优化

```python
from langchain.cache import InMemoryCache
from langchain.globals import set_llm_cache

# 启用缓存
set_llm_cache(InMemoryCache())

# 相同问题不会重复调用 LLM
result1 = qa_chain.run("什么是 Python？")
result2 = qa_chain.run("什么是 Python？")  # 使用缓存
```

---

## 🎯 实战项目

### 项目：企业知识库问答系统

**功能需求**：
- 支持多种文档格式（PDF、Word、TXT、Markdown）
- 实时文档上传和索引
- 多轮对话支持
- 来源追溯
- 搜索历史记录
- 权限管理

**技术栈**：
- 后端：FastAPI + LangChain
- 向量数据库：Chroma 或 Pinecone
- 前端：React + TypeScript
- 部署：Docker + AWS/Azure

---

## 📖 推荐资源

### 学习资源
- **LangChain RAG 文档**：https://python.langchain.com/docs/use_cases/question_answering
- **RAG Survey Paper**：最新 RAG 研究综述
- **Building RAG from Scratch**：YouTube 教程

### 工具与库
- **LlamaIndex**：专注于 RAG 的框架
- **Haystack**：另一个 RAG 框架
- **RAGAS**：RAG 评估工具

---

## ✅ 学习检查清单

- [ ] 理解 RAG 的基本原理和架构
- [ ] 掌握多种文档分块策略
- [ ] 能够选择合适的向量数据库
- [ ] 实现混合检索和重排序
- [ ] 理解不同 RAG 链类型的适用场景
- [ ] 掌握 RAG 评估方法
- [ ] 完成一个生产级 RAG 系统

---

**下一步**：学习 [06-Agent开发实战](./06-Agent开发实战.md)
