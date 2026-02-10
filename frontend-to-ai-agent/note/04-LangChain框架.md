# 04 - LangChain 框架

> **前置条件**：完成 Python 基础、FastAPI 和 LLM 基础学习  
> **学习时长**：3-4 周  
> **学习目标**：掌握 LangChain 框架，能够构建复杂的 AI 应用

---

## 🎯 为什么学习 LangChain？

### LangChain 解决的问题

**不使用 LangChain**：
```python
# 复杂、重复的代码
response = openai.chat.completions.create(...)
# 需要手动管理对话历史
# 需要手动处理文档加载和分块
# 需要手动实现向量检索
# 需要手动串联多个 LLM 调用
```

**使用 LangChain**：
```python
# 简洁、可复用的代码
from langchain.chains import LLMChain
chain = LLMChain(llm=llm, prompt=prompt)
result = chain.run("用户输入")
```

**LangChain 的核心价值**：
- 🔗 **链式调用**：轻松组合多个步骤
- 💾 **记忆管理**：自动处理对话历史
- 📚 **文档处理**：内置文档加载和分块
- 🔍 **向量检索**：集成向量数据库
- 🤖 **Agent 框架**：构建自主决策的 AI Agent

---

## 📚 学习内容

### 1. LangChain 核心概念

#### 1.1 安装

```bash
pip install langchain langchain-openai langchain-community
pip install chromadb  # 向量数据库
pip install pypdf  # PDF 文档处理
pip install faiss-cpu  # 向量检索（可选）
```

---

#### 1.2 核心组件

```
LangChain 核心架构：

Models（模型）
   ↓
Prompts（提示词模板）
   ↓
Chains（链式调用）
   ↓
Memory（记忆）
   ↓
Agents（智能代理）
```

---

### 2. Models（模型封装）

#### 2.1 LLM 和 Chat Models

```python
from langchain_openai import ChatOpenAI, OpenAI

# Chat Models（用于对话）
chat_model = ChatOpenAI(
    model="gpt-3.5-turbo",
    temperature=0.7,
    max_tokens=500
)

# LLM（用于文本生成）
llm = OpenAI(
    model="gpt-3.5-turbo-instruct",
    temperature=0.7
)

# 直接调用
response = chat_model.invoke("Hello!")
print(response.content)
```

---

#### 2.2 消息类型

```python
from langchain.schema import HumanMessage, SystemMessage, AIMessage

messages = [
    SystemMessage(content="你是一个有帮助的助手。"),
    HumanMessage(content="Python 是什么？"),
]

response = chat_model.invoke(messages)
print(response.content)
```

---

### 3. Prompts（提示词模板）

#### 3.1 PromptTemplate

```python
from langchain.prompts import PromptTemplate

# 创建模板
template = """
你是一个{role}。
请回答以下问题：{question}
"""

prompt = PromptTemplate(
    input_variables=["role", "question"],
    template=template
)

# 格式化
formatted_prompt = prompt.format(
    role="Python 专家",
    question="什么是装饰器？"
)
print(formatted_prompt)
```

---

#### 3.2 ChatPromptTemplate

```python
from langchain.prompts import ChatPromptTemplate, HumanMessagePromptTemplate, SystemMessagePromptTemplate

# 创建聊天模板
system_template = "你是一个{role}。"
human_template = "{question}"

chat_prompt = ChatPromptTemplate.from_messages([
    SystemMessagePromptTemplate.from_template(system_template),
    HumanMessagePromptTemplate.from_template(human_template)
])

# 格式化
messages = chat_prompt.format_messages(
    role="Python 专家",
    question="什么是装饰器？"
)

response = chat_model.invoke(messages)
print(response.content)
```

---

#### 3.3 Few-shot Prompts

```python
from langchain.prompts import FewShotPromptTemplate

# 示例数据
examples = [
    {"input": "开心", "output": "😊"},
    {"input": "悲伤", "output": "😢"},
    {"input": "愤怒", "output": "😠"}
]

# 示例模板
example_template = """
输入：{input}
输出：{output}
"""

example_prompt = PromptTemplate(
    input_variables=["input", "output"],
    template=example_template
)

# Few-shot 模板
few_shot_prompt = FewShotPromptTemplate(
    examples=examples,
    example_prompt=example_prompt,
    prefix="将情绪转换为 emoji：",
    suffix="输入：{input}\n输出：",
    input_variables=["input"]
)

# 使用
prompt = few_shot_prompt.format(input="惊讶")
print(prompt)
```

---

### 4. Chains（链式调用）

#### 4.1 LLMChain

```python
from langchain.chains import LLMChain

# 创建链
prompt = PromptTemplate(
    input_variables=["topic"],
    template="写一首关于{topic}的诗。"
)

chain = LLMChain(llm=chat_model, prompt=prompt)

# 执行
result = chain.run("春天")
print(result)
```

---

#### 4.2 Sequential Chain（顺序链）

```python
from langchain.chains import SimpleSequentialChain

# 第一个链：生成剧本大纲
synopsis_chain = LLMChain(
    llm=chat_model,
    prompt=PromptTemplate(
        input_variables=["title"],
        template="为电影《{title}》写一个简短的剧情大纲。"
    )
)

# 第二个链：写评论
review_chain = LLMChain(
    llm=chat_model,
    prompt=PromptTemplate(
        input_variables=["synopsis"],
        template="为以下电影剧情写一段评论：\n\n{synopsis}"
    )
)

# 组合链
overall_chain = SimpleSequentialChain(
    chains=[synopsis_chain, review_chain],
    verbose=True  # 显示中间步骤
)

# 执行
result = overall_chain.run("时空旅行者")
print(result)
```

---

#### 4.3 LangChain Expression Language (LCEL)

```python
# 现代化的链式语法
from langchain_core.output_parsers import StrOutputParser
from langchain_core.runnables import RunnablePassthrough

# 创建链（使用管道操作符 |）
chain = (
    {"topic": RunnablePassthrough()}
    | PromptTemplate.from_template("写一首关于{topic}的诗")
    | chat_model
    | StrOutputParser()
)

# 执行
result = chain.invoke("夏天")
print(result)
```

---

### 5. Memory（记忆管理）

#### 5.1 ConversationBufferMemory

```python
from langchain.memory import ConversationBufferMemory
from langchain.chains import ConversationChain

# 创建记忆
memory = ConversationBufferMemory()

# 创建对话链
conversation = ConversationChain(
    llm=chat_model,
    memory=memory,
    verbose=True
)

# 多轮对话
print(conversation.predict(input="我叫 Alice"))
print(conversation.predict(input="我的名字是什么？"))

# 查看记忆
print(memory.load_memory_variables({}))
```

---

#### 5.2 ConversationBufferWindowMemory

```python
from langchain.memory import ConversationBufferWindowMemory

# 只保留最近 k 轮对话
memory = ConversationBufferWindowMemory(k=2)

conversation = ConversationChain(
    llm=chat_model,
    memory=memory
)

# 测试
conversation.predict(input="第一句话")
conversation.predict(input="第二句话")
conversation.predict(input="第三句话")
conversation.predict(input="第四句话")
conversation.predict(input="你还记得第一句话吗？")  # 应该不记得
```

---

#### 5.3 ConversationSummaryMemory

```python
from langchain.memory import ConversationSummaryMemory

# 自动总结历史对话
memory = ConversationSummaryMemory(llm=chat_model)

conversation = ConversationChain(
    llm=chat_model,
    memory=memory,
    verbose=True
)

# 长对话会被自动总结
conversation.predict(input="我今年 25 岁，是一名前端工程师。")
conversation.predict(input="我正在学习 Python 和 AI。")
conversation.predict(input="我关于我的信息你记得多少？")

# 查看总结
print(memory.load_memory_variables({}))
```

---

### 6. Document Loaders（文档加载）

#### 6.1 文本文件加载

```python
from langchain.document_loaders import TextLoader

loader = TextLoader("data.txt", encoding="utf-8")
documents = loader.load()

print(f"加载了 {len(documents)} 个文档")
print(documents[0].page_content[:200])  # 前 200 字符
```

---

#### 6.2 PDF 文档加载

```python
from langchain.document_loaders import PyPDFLoader

loader = PyPDFLoader("document.pdf")
pages = loader.load_and_split()

print(f"PDF 有 {len(pages)} 页")
print(f"第一页内容：{pages[0].page_content[:200]}")
```

---

#### 6.3 网页加载

```python
from langchain.document_loaders import WebBaseLoader

loader = WebBaseLoader("https://example.com")
documents = loader.load()

print(documents[0].page_content[:200])
```

---

### 7. Text Splitters（文本分块）

#### 7.1 CharacterTextSplitter

```python
from langchain.text_splitter import CharacterTextSplitter

text_splitter = CharacterTextSplitter(
    chunk_size=1000,      # 每块最大字符数
    chunk_overlap=200,    # 重叠字符数
    separator="\n\n"      # 分隔符
)

# 分割文本
texts = text_splitter.split_text(long_text)
print(f"分割成 {len(texts)} 块")
```

---

#### 7.2 RecursiveCharacterTextSplitter

```python
from langchain.text_splitter import RecursiveCharacterTextSplitter

# 递归分割（更智能）
text_splitter = RecursiveCharacterTextSplitter(
    chunk_size=1000,
    chunk_overlap=200,
    separators=["\n\n", "\n", "。", " ", ""]  # 优先级从高到低
)

# 分割文档
documents = text_splitter.split_documents(loaded_documents)
```

---

### 8. Vector Stores（向量存储）

#### 8.1 Chroma（本地向量数据库）

```python
from langchain.vectorstores import Chroma
from langchain_openai import OpenAIEmbeddings

# 创建 embeddings
embeddings = OpenAIEmbeddings()

# 创建向量存储
texts = [
    "LangChain 是一个 LLM 应用框架",
    "Python 是一门编程语言",
    "FastAPI 是一个 Web 框架"
]

vectorstore = Chroma.from_texts(
    texts=texts,
    embedding=embeddings,
    persist_directory="./chroma_db"  # 持久化存储
)

# 相似度搜索
results = vectorstore.similarity_search("什么是 LangChain？", k=2)
for doc in results:
    print(doc.page_content)
```

---

#### 8.2 FAISS（高性能向量检索）

```python
from langchain.vectorstores import FAISS

# 创建 FAISS 索引
vectorstore = FAISS.from_texts(texts, embeddings)

# 保存
vectorstore.save_local("faiss_index")

# 加载
loaded_vectorstore = FAISS.load_local(
    "faiss_index",
    embeddings,
    allow_dangerous_deserialization=True
)

# 检索
results = loaded_vectorstore.similarity_search("Python", k=1)
```

---

### 9. Retrievers（检索器）

```python
from langchain.chains import RetrievalQA

# 创建检索器
retriever = vectorstore.as_retriever(
    search_type="similarity",
    search_kwargs={"k": 3}  # 返回 3 个最相似的文档
)

# 创建问答链
qa_chain = RetrievalQA.from_chain_type(
    llm=chat_model,
    chain_type="stuff",  # 或 "map_reduce", "refine"
    retriever=retriever,
    return_source_documents=True  # 返回来源文档
)

# 提问
result = qa_chain({"query": "LangChain 是什么？"})
print(f"答案：{result['result']}")
print(f"来源：{result['source_documents']}")
```

---

### 10. Output Parsers（输出解析）

#### 10.1 结构化输出

```python
from langchain.output_parsers import PydanticOutputParser
from pydantic import BaseModel, Field

# 定义输出结构
class Person(BaseModel):
    name: str = Field(description="人名")
    age: int = Field(description="年龄")
    occupation: str = Field(description="职业")

# 创建解析器
parser = PydanticOutputParser(pydantic_object=Person)

# 创建提示词（包含格式说明）
prompt = PromptTemplate(
    template="提取以下文本中的人物信息。\n{format_instructions}\n\n文本：{text}",
    input_variables=["text"],
    partial_variables={"format_instructions": parser.get_format_instructions()}
)

# 创建链
chain = prompt | chat_model | parser

# 执行
result = chain.invoke({"text": "Alice 今年 25 岁，是一名软件工程师。"})
print(type(result))  # <class 'Person'>
print(result.name, result.age, result.occupation)
```

---

### 11. 完整项目示例

#### 项目：智能文档问答系统

```python
from langchain_openai import ChatOpenAI, OpenAIEmbeddings
from langchain.text_splitter import RecursiveCharacterTextSplitter
from langchain.vectorstores import Chroma
from langchain.chains import RetrievalQA
from langchain.document_loaders import PyPDFLoader, TextLoader
import os

class DocumentQA:
    def __init__(self, model="gpt-3.5-turbo"):
        self.llm = ChatOpenAI(model=model, temperature=0)
        self.embeddings = OpenAIEmbeddings()
        self.vectorstore = None
        self.qa_chain = None
    
    def load_documents(self, file_paths):
        """加载文档"""
        documents = []
        for file_path in file_paths:
            if file_path.endswith('.pdf'):
                loader = PyPDFLoader(file_path)
            elif file_path.endswith('.txt'):
                loader = TextLoader(file_path, encoding='utf-8')
            else:
                continue
            
            documents.extend(loader.load())
        
        # 分块
        text_splitter = RecursiveCharacterTextSplitter(
            chunk_size=1000,
            chunk_overlap=200
        )
        splits = text_splitter.split_documents(documents)
        
        # 创建向量存储
        self.vectorstore = Chroma.from_documents(
            documents=splits,
            embedding=self.embeddings,
            persist_directory="./doc_qa_db"
        )
        
        # 创建问答链
        self.qa_chain = RetrievalQA.from_chain_type(
            llm=self.llm,
            chain_type="stuff",
            retriever=self.vectorstore.as_retriever(search_kwargs={"k": 3}),
            return_source_documents=True
        )
        
        return f"成功加载 {len(documents)} 个文档，分割成 {len(splits)} 个块"
    
    def ask(self, question):
        """提问"""
        if not self.qa_chain:
            return "请先加载文档"
        
        result = self.qa_chain({"query": question})
        
        answer = result['result']
        sources = [doc.metadata.get('source', 'Unknown') for doc in result['source_documents']]
        
        return {
            "answer": answer,
            "sources": list(set(sources))  # 去重
        }

# 使用
doc_qa = DocumentQA()
print(doc_qa.load_documents(["document1.pdf", "document2.txt"]))

response = doc_qa.ask("这份文档的主要内容是什么？")
print(f"答案：{response['answer']}")
print(f"来源：{response['sources']}")
```

---

## 🎯 实战练习

### 练习 1：个人知识库助手
```python
# 加载个人笔记、文档
# 实现智能搜索和问答
# 支持对话式交互
```

### 练习 2：代码文档助手
```python
# 加载代码库文档
# 回答代码相关问题
# 生成代码示例
```

### 练习 3：多文档总结
```python
# 加载多个文档
# 生成综合总结
# 对比分析
```

---

## 📖 推荐资源

### 官方文档
- **LangChain 文档**：https://python.langchain.com/
- **LangChain GitHub**：https://github.com/langchain-ai/langchain

### 学习资源
- **LangChain Cookbook**：官方示例集合
- **LangChain Tutorial（YouTube）**：视频教程

### 社区
- **LangChain Discord**：官方社区
- **GitHub Discussions**：问题讨论

---

## ✅ 学习检查清单

- [ ] 理解 LangChain 的核心概念和架构
- [ ] 掌握 Models、Prompts、Chains 的使用
- [ ] 能够实现对话记忆管理
- [ ] 掌握文档加载和文本分块
- [ ] 能够使用向量数据库进行检索
- [ ] 理解并使用 Output Parsers
- [ ] 完成至少 1 个完整的文档问答项目

---

**下一步**：学习 [05-RAG系统开发](./05-RAG系统开发.md)
