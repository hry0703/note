# 06 - Agent 开发实战

> **前置条件**：完成 LangChain 和 RAG 系统学习  
> **学习时长**：3-4 周  
> **学习目标**：掌握 AI Agent 设计与开发，构建自主决策系统

---

## 🎯 什么是 Agent？

### Agent（智能代理）核心概念

**定义**：能够感知环境、自主决策、使用工具、完成任务的 AI 系统

```
┌─────────────────────────────────────┐
│           AI Agent                  │
│                                     │
│  观察 → 思考 → 决策 → 行动 → 观察   │
│    ↑                           ↓    │
│    └───────── 循环 ─────────────┘   │
└─────────────────────────────────────┘
```

**Agent vs 普通 LLM 应用**：

| 特性 | 普通 LLM | Agent |
|------|----------|-------|
| 决策 | 单次调用 | 多步推理 |
| 工具使用 | 固定流程 | 动态选择 |
| 自主性 | 无 | 有 |
| 适应性 | 低 | 高 |

---

## 📚 学习内容

### 1. Agent 核心组件

#### 1.1 Agent 架构

```python
from langchain.agents import AgentExecutor, create_openai_functions_agent
from langchain_openai import ChatOpenAI
from langchain.prompts import ChatPromptTemplate, MessagesPlaceholder
from langchain.tools import Tool

# 1. 定义工具
def calculator(expression: str) -> str:
    """计算数学表达式"""
    try:
        return str(eval(expression))
    except:
        return "计算错误"

tools = [
    Tool(
        name="Calculator",
        func=calculator,
        description="用于计算数学表达式，输入应该是有效的 Python 表达式，如 '2+2' 或 '10*5'"
    )
]

# 2. 创建 LLM
llm = ChatOpenAI(model="gpt-4", temperature=0)

# 3. 创建提示词
prompt = ChatPromptTemplate.from_messages([
    ("system", "你是一个有帮助的助手。"),
    ("human", "{input}"),
    MessagesPlaceholder(variable_name="agent_scratchpad"),
])

# 4. 创建 Agent
agent = create_openai_functions_agent(llm, tools, prompt)

# 5. 创建 Agent 执行器
agent_executor = AgentExecutor(agent=agent, tools=tools, verbose=True)

# 6. 运行
result = agent_executor.invoke({"input": "25 乘以 4 是多少？"})
print(result['output'])
```

---

### 2. Agent 类型

#### 2.1 Zero-shot ReAct Agent

```python
from langchain.agents import create_react_agent
from langchain.agents import AgentExecutor

# ReAct = Reasoning + Acting
prompt = """
回答以下问题，你可以使用这些工具：

{tools}

使用以下格式：

Question: 需要回答的问题
Thought: 你应该思考要做什么
Action: 使用的工具 [{tool_names}]
Action Input: 工具的输入
Observation: 工具的输出
... (这个 Thought/Action/Action Input/Observation 可以重复 N 次)
Thought: 我现在知道最终答案了
Final Answer: 原始问题的最终答案

开始！

Question: {input}
Thought: {agent_scratchpad}
"""

agent = create_react_agent(llm, tools, prompt)
agent_executor = AgentExecutor(agent=agent, tools=tools, verbose=True)
```

**工作流程**：
```
1. Thought: "我需要计算 25*4"
2. Action: Calculator
3. Action Input: "25*4"
4. Observation: 100
5. Thought: "我现在知道答案了"
6. Final Answer: "25 乘以 4 等于 100"
```

---

#### 2.2 Structured Tool Agent

```python
from langchain.tools import StructuredTool
from pydantic import BaseModel, Field

# 定义工具的输入结构
class SearchInput(BaseModel):
    query: str = Field(description="搜索查询")
    num_results: int = Field(default=5, description="返回结果数量")

def search_web(query: str, num_results: int = 5) -> str:
    """搜索网络"""
    return f"找到 {num_results} 个关于 '{query}' 的结果"

# 创建结构化工具
search_tool = StructuredTool.from_function(
    func=search_web,
    name="WebSearch",
    description="在网络上搜索信息",
    args_schema=SearchInput
)

tools = [search_tool]
agent = create_openai_functions_agent(llm, tools, prompt)
agent_executor = AgentExecutor(agent=agent, tools=tools)
```

---

#### 2.3 Plan-and-Execute Agent

```python
from langchain_experimental.plan_and_execute import PlanAndExecute, load_agent_executor, load_chat_planner

# 先规划，再执行
planner = load_chat_planner(llm)
executor = load_agent_executor(llm, tools, verbose=True)

agent = PlanAndExecute(planner=planner, executor=executor, verbose=True)

# 复杂任务会被分解成多个步骤
result = agent.run("研究 Python 和 JavaScript 的区别，然后写一份对比报告")
```

**工作流程**：
```
Plan:
1. 搜索 Python 的特点
2. 搜索 JavaScript 的特点
3. 对比两者
4. 生成报告

Execute:
Step 1: [执行搜索 Python]
Step 2: [执行搜索 JavaScript]
Step 3: [执行对比]
Step 4: [生成报告]
```

---

### 3. 工具开发

#### 3.1 基础工具

```python
from langchain.tools import BaseTool
from typing import Optional

class CurrentTimeTool(BaseTool):
    name = "current_time"
    description = "获取当前时间"
    
    def _run(self, query: str = "") -> str:
        """同步执行"""
        from datetime import datetime
        return datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    
    async def _arun(self, query: str = "") -> str:
        """异步执行"""
        return self._run(query)

# 使用
time_tool = CurrentTimeTool()
print(time_tool.run(""))
```

---

#### 3.2 API 工具

```python
import requests
from langchain.tools import Tool

def search_github(query: str) -> str:
    """搜索 GitHub 仓库"""
    url = f"https://api.github.com/search/repositories?q={query}&sort=stars&order=desc"
    response = requests.get(url)
    data = response.json()
    
    if 'items' in data and len(data['items']) > 0:
        top_repo = data['items'][0]
        return f"最热门的仓库：{top_repo['full_name']} ({top_repo['stargazers_count']} stars)"
    return "未找到相关仓库"

github_tool = Tool(
    name="GitHubSearch",
    func=search_github,
    description="搜索 GitHub 仓库"
)
```

---

#### 3.3 数据库工具

```python
from langchain.agents import create_sql_agent
from langchain.sql_database import SQLDatabase
from langchain_openai import ChatOpenAI

# 连接数据库
db = SQLDatabase.from_uri("sqlite:///example.db")

# 创建 SQL Agent
agent = create_sql_agent(
    llm=ChatOpenAI(model="gpt-4", temperature=0),
    db=db,
    verbose=True
)

# 自然语言查询数据库
result = agent.run("有多少用户？")
result = agent.run("显示年龄大于 25 岁的用户")
```

---

### 4. Agent 记忆系统

#### 4.1 短期记忆（对话历史）

```python
from langchain.memory import ConversationBufferMemory
from langchain.agents import initialize_agent, AgentType

memory = ConversationBufferMemory(
    memory_key="chat_history",
    return_messages=True
)

agent_executor = initialize_agent(
    tools=tools,
    llm=llm,
    agent=AgentType.OPENAI_FUNCTIONS,
    memory=memory,
    verbose=True
)

# 多轮对话
agent_executor.run("我叫 Alice")
agent_executor.run("我的名字是什么？")  # Agent 会记住
```

---

#### 4.2 长期记忆（向量存储）

```python
from langchain.vectorstores import Chroma
from langchain_openai import OpenAIEmbeddings
from langchain.tools import Tool

class MemoryTool(BaseTool):
    name = "long_term_memory"
    description = "从长期记忆中检索信息"
    
    def __init__(self):
        super().__init__()
        self.vectorstore = Chroma(
            embedding_function=OpenAIEmbeddings(),
            persist_directory="./agent_memory"
        )
    
    def _run(self, query: str) -> str:
        docs = self.vectorstore.similarity_search(query, k=3)
        return "\n".join([doc.page_content for doc in docs])
    
    def save_memory(self, content: str):
        """保存新记忆"""
        self.vectorstore.add_texts([content])

memory_tool = MemoryTool()
```

---

### 5. Multi-Agent 系统

#### 5.1 多 Agent 协作

```python
from langchain.agents import initialize_agent
from langchain_openai import ChatOpenAI

# Agent 1: 研究员
researcher_llm = ChatOpenAI(model="gpt-4", temperature=0)
researcher = initialize_agent(
    tools=[search_tool],
    llm=researcher_llm,
    agent=AgentType.OPENAI_FUNCTIONS,
    verbose=True
)

# Agent 2: 作家
writer_llm = ChatOpenAI(model="gpt-4", temperature=0.7)
writer = initialize_agent(
    tools=[],
    llm=writer_llm,
    agent=AgentType.OPENAI_FUNCTIONS,
    verbose=True
)

# 协作流程
def research_and_write(topic: str):
    # Step 1: 研究员收集信息
    research_result = researcher.run(f"研究关于 {topic} 的信息")
    
    # Step 2: 作家根据研究结果写文章
    article = writer.run(f"根据以下信息写一篇文章：\n{research_result}")
    
    return article

result = research_and_write("AI Agent 的未来发展")
print(result)
```

---

#### 5.2 使用 CrewAI（Multi-Agent 框架）

```python
# pip install crewai
from crewai import Agent, Task, Crew

# 定义 Agent
researcher = Agent(
    role='研究员',
    goal='收集关于 {topic} 的详细信息',
    backstory='你是一个经验丰富的研究员',
    verbose=True
)

writer = Agent(
    role='作家',
    goal='根据研究结果写一篇高质量的文章',
    backstory='你是一个专业的技术作家',
    verbose=True
)

# 定义任务
research_task = Task(
    description='研究 {topic} 的最新发展',
    agent=researcher
)

write_task = Task(
    description='写一篇关于 {topic} 的文章',
    agent=writer
)

# 创建团队
crew = Crew(
    agents=[researcher, writer],
    tasks=[research_task, write_task],
    verbose=True
)

# 执行
result = crew.kickoff(inputs={'topic': 'Python异步编程'})
```

---

### 6. Agent 错误处理

#### 6.1 重试机制

```python
from langchain.agents import AgentExecutor

agent_executor = AgentExecutor(
    agent=agent,
    tools=tools,
    max_iterations=5,        # 最大迭代次数
    max_execution_time=60,   # 最大执行时间（秒）
    early_stopping_method="generate",  # 提前停止策略
    handle_parsing_errors=True,  # 处理解析错误
    verbose=True
)

# 自定义错误处理
def custom_error_handler(error):
    return f"发生错误：{error}。请重新思考并尝试其他方法。"

agent_executor = AgentExecutor(
    agent=agent,
    tools=tools,
    handle_parsing_errors=custom_error_handler
)
```

---

#### 6.2 工具验证

```python
from langchain.tools import Tool
from pydantic import ValidationError

def safe_calculator(expression: str) -> str:
    """安全的计算器"""
    # 验证输入
    allowed_chars = set("0123456789+-*/() .")
    if not all(c in allowed_chars for c in expression):
        return "错误：包含非法字符"
    
    # 限制长度
    if len(expression) > 100:
        return "错误：表达式过长"
    
    try:
        result = eval(expression)
        return str(result)
    except ZeroDivisionError:
        return "错误：除以零"
    except Exception as e:
        return f"错误：{str(e)}"

calculator_tool = Tool(
    name="Calculator",
    func=safe_calculator,
    description="安全的计算器"
)
```

---

### 7. 完整项目：智能助手 Agent

```python
from langchain.agents import AgentExecutor, create_openai_functions_agent
from langchain_openai import ChatOpenAI, OpenAIEmbeddings
from langchain.tools import Tool, BaseTool
from langchain.prompts import ChatPromptTemplate, MessagesPlaceholder
from langchain.memory import ConversationBufferMemory
from langchain.vectorstores import Chroma
import requests
from datetime import datetime

class SmartAssistant:
    def __init__(self):
        self.llm = ChatOpenAI(model="gpt-4", temperature=0)
        self.memory = ConversationBufferMemory(
            memory_key="chat_history",
            return_messages=True
        )
        self.tools = self._create_tools()
        self.agent = self._create_agent()
    
    def _create_tools(self):
        """创建工具集"""
        
        # 工具1：获取当前时间
        def get_current_time(query: str = "") -> str:
            return datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        
        # 工具2：网络搜索（模拟）
        def web_search(query: str) -> str:
            # 实际应用中集成 Google Search API 或 SerpAPI
            return f"搜索结果：关于 '{query}' 的相关信息..."
        
        # 工具3：计算器
        def calculator(expression: str) -> str:
            try:
                return str(eval(expression))
            except:
                return "计算错误"
        
        # 工具4：记忆检索
        class MemoryTool(BaseTool):
            name = "memory_search"
            description = "从知识库中检索信息"
            
            def __init__(self):
                super().__init__()
                self.vectorstore = Chroma(
                    embedding_function=OpenAIEmbeddings(),
                    persist_directory="./assistant_memory"
                )
            
            def _run(self, query: str) -> str:
                docs = self.vectorstore.similarity_search(query, k=2)
                if docs:
                    return "\n".join([doc.page_content for doc in docs])
                return "未找到相关信息"
            
            async def _arun(self, query: str) -> str:
                return self._run(query)
        
        return [
            Tool(name="GetTime", func=get_current_time, description="获取当前时间"),
            Tool(name="WebSearch", func=web_search, description="在网络上搜索信息"),
            Tool(name="Calculator", func=calculator, description="计算数学表达式"),
            MemoryTool()
        ]
    
    def _create_agent(self):
        """创建 Agent"""
        prompt = ChatPromptTemplate.from_messages([
            ("system", """你是一个智能助手，可以使用多种工具帮助用户。
            
            能力：
            - 回答问题
            - 搜索信息
            - 计算数学问题
            - 查询时间
            - 记住对话内容
            
            注意：
            - 如果不确定，使用搜索工具
            - 对于数学问题，使用计算器
            - 需要时间信息时，使用时间工具
            """),
            MessagesPlaceholder(variable_name="chat_history"),
            ("human", "{input}"),
            MessagesPlaceholder(variable_name="agent_scratchpad"),
        ])
        
        agent = create_openai_functions_agent(self.llm, self.tools, prompt)
        
        return AgentExecutor(
            agent=agent,
            tools=self.tools,
            memory=self.memory,
            verbose=True,
            max_iterations=5,
            handle_parsing_errors=True
        )
    
    def chat(self, message: str) -> str:
        """与助手对话"""
        try:
            result = self.agent.invoke({"input": message})
            return result['output']
        except Exception as e:
            return f"抱歉，发生错误：{str(e)}"
    
    def clear_memory(self):
        """清空记忆"""
        self.memory.clear()

# 使用示例
assistant = SmartAssistant()

print(assistant.chat("现在几点？"))
print(assistant.chat("计算 123 * 456"))
print(assistant.chat("搜索 Python 最新版本"))
print(assistant.chat("我刚才问了什么问题？"))
```

---

### 8. FastAPI 集成

```python
from fastapi import FastAPI, WebSocket
from pydantic import BaseModel
import json

app = FastAPI()
assistant = SmartAssistant()

class ChatMessage(BaseModel):
    message: str

@app.post("/chat")
async def chat(msg: ChatMessage):
    """普通对话"""
    response = assistant.chat(msg.message)
    return {"response": response}

@app.websocket("/ws/chat")
async def websocket_chat(websocket: WebSocket):
    """WebSocket 实时对话"""
    await websocket.accept()
    
    try:
        while True:
            message = await websocket.receive_text()
            
            # 流式发送思考过程
            response = assistant.chat(message)
            await websocket.send_text(json.dumps({
                "type": "response",
                "content": response
            }))
    except Exception as e:
        print(f"WebSocket error: {e}")

@app.delete("/memory")
async def clear_memory():
    """清空对话历史"""
    assistant.clear_memory()
    return {"message": "记忆已清空"}
```

---

## 🎯 实战练习

### 练习 1：代码助手 Agent
- 能够解释代码
- 生成代码
- 查找文档
- 运行代码（沙箱环境）

### 练习 2：数据分析 Agent
- 读取 CSV/Excel 文件
- 数据清洗
- 生成图表
- 撰写分析报告

### 练习 3：客服 Agent
- 查询订单信息
- 处理常见问题
- 升级到人工客服
- 记录对话日志

---

## 📖 推荐资源

### 框架与工具
- **LangChain Agents**：https://python.langchain.com/docs/modules/agents
- **CrewAI**：https://github.com/joaomdmoura/crewAI
- **AutoGPT**：https://github.com/Significant-Gravitas/AutoGPT
- **BabyAGI**：https://github.com/yoheinakajima/babyagi

### 学习资源
- **LangChain Agents Tutorial**：官方教程
- **Building AI Agents**：YouTube 系列视频
- **Agent Papers**：arXiv 最新论文

---

## ✅ 学习检查清单

- [ ] 理解 Agent 的核心概念和架构
- [ ] 掌握不同类型的 Agent（ReAct、Plan-and-Execute）
- [ ] 能够开发自定义工具
- [ ] 实现 Agent 记忆系统
- [ ] 理解 Multi-Agent 协作
- [ ] 掌握 Agent 错误处理
- [ ] 完成至少 2 个实用的 Agent 项目

---

**下一步**：学习 [07-全栈项目实战](./07-全栈项目实战.md)
