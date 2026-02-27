# 03 - LLM 基础与应用

> **前置条件**：完成 Python 基础和 FastAPI 学习  
> **学习时长**：2-3 周  
> **学习目标**：掌握大语言模型的基本原理和 OpenAI API 使用

---

## 🎯 学习目标

- 理解大语言模型（LLM）的基本原理
- 掌握 OpenAI API 的使用
- 学习 Prompt Engineering（提示词工程）
- 理解 Token、Embeddings、Function Calling
- 能够构建基础的 AI 应用

---

## 📚 学习内容

### 1. LLM 基础概念

#### 1.1 什么是大语言模型？

**核心概念**：
- **Transformer 架构**：基于注意力机制的神经网络
- **预训练**：在海量文本数据上学习语言模式
- **生成式 AI**：根据输入生成文本输出
- **上下文窗口**：模型能够"记住"的文本长度

**主流 LLM 对比**：

| 模型 | 上下文窗口 | 特点 | 适用场景 |
|------|-----------|------|---------|
| GPT-4 | 8K / 32K / 128K | 最强能力 | 复杂推理、代码生成 |
| GPT-3.5-turbo | 16K | 性价比高 | 聊天、简单任务 |
| Claude 3 | 200K | 超长上下文 | 文档分析 |
| Gemini | 1M | 多模态 | 图像+文本 |

---

#### 1.2 Token 概念

**什么是 Token？**
- Token 是 LLM 处理文本的基本单位
- 1 个 Token ≈ 0.75 个英文单词
- 1 个中文字符 ≈ 2-3 个 Token

**示例**：
```
"Hello, World!" → 4 tokens: ["Hello", ",", " World", "!"]
"你好世界" → 6-8 tokens（中文 token 化较复杂）
```

**成本计算**：
```python
# GPT-4 价格（2024年）
# Input: $0.03 / 1K tokens
# Output: $0.06 / 1K tokens

# 示例：1000 字文章分析
input_tokens = 1500  # 输入 token
output_tokens = 500  # 输出 token

cost = (input_tokens / 1000 * 0.03) + (output_tokens / 1000 * 0.06)
print(f"成本：${cost:.4f}")  # $0.0750
```

---

### 2. OpenAI API 使用

#### 2.1 安装与配置

```bash
pip install openai
```

```python
import os
from openai import OpenAI

# 设置 API Key
os.environ["OPENAI_API_KEY"] = "sk-your-api-key"
client = OpenAI()
```

---

#### 2.2 基础对话

```python
def chat_with_gpt(user_message):
    response = client.chat.completions.create(
        model="gpt-4",  # 或 "gpt-3.5-turbo"
        messages=[
            {"role": "system", "content": "你是一个有帮助的助手。"},
            {"role": "user", "content": user_message}
        ],
        temperature=0.7,  # 控制随机性 (0-2)
        max_tokens=500,   # 最大输出 token 数
    )
    return response.choices[0].message.content

# 使用
result = chat_with_gpt("解释什么是 Python 装饰器")
print(result)
```

---

#### 2.3 多轮对话（带记忆）

```python
class ChatBot:
    def __init__(self, system_prompt="你是一个有帮助的助手。"):
        self.messages = [
            {"role": "system", "content": system_prompt}
        ]
    
    def chat(self, user_message):
        # 添加用户消息
        self.messages.append({"role": "user", "content": user_message})
        
        # 调用 API
        response = client.chat.completions.create(
            model="gpt-3.5-turbo",
            messages=self.messages
        )
        
        # 添加助手回复
        assistant_message = response.choices[0].message.content
        self.messages.append({"role": "assistant", "content": assistant_message})
        
        return assistant_message
    
    def clear_history(self):
        self.messages = self.messages[:1]  # 保留系统提示

# 使用
bot = ChatBot()
print(bot.chat("我的名字是 Alice"))
print(bot.chat("我叫什么名字？"))  # AI 会记住之前的对话
```

---

### 3. Prompt Engineering（提示词工程）

#### 3.1 基本原则

**1. 清晰明确**
```python
# ❌ 不好的提示
"写点关于 AI 的东西"

# ✅ 好的提示
"写一篇 500 字的文章，介绍 AI 在医疗领域的 3 个应用案例，包括诊断、药物研发和个性化治疗。"
```

**2. 提供上下文**
```python
# ✅ 提供角色和背景
prompt = """
你是一位经验丰富的 Python 教师。
学生刚学完基础语法，正在学习面向对象编程。
请用简单的语言解释什么是类和对象，并给出一个生活中的例子。
"""
```

**3. 使用示例（Few-shot Learning）**
```python
prompt = """
将以下文本分类为：正面、负面、中性

示例：
文本："这个产品太棒了！" → 正面
文本："质量一般，价格偏高。" → 负面
文本："这是一个蓝色的杯子。" → 中性

文本："服务态度很好，但等待时间有点长。" → ?
"""
```

---

#### 3.2 高级技巧

**Chain of Thought（思维链）**
```python
prompt = """
问题：一个班级有 30 名学生，其中 60% 是女生。女生中有 40% 戴眼镜。请问有多少女生戴眼镜？

请一步步思考：
1. 首先计算女生人数
2. 然后计算戴眼镜的女生人数
3. 给出最终答案
"""
```

**角色扮演**
```python
system_prompt = """
你是一位资深的前端面试官，有 10 年经验。
你需要：
- 提出有深度的技术问题
- 根据回答给出建设性反馈
- 评估候选人的技术水平
"""
```

---

### 4. 流式响应（Streaming）

```python
def stream_chat(user_message):
    stream = client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[{"role": "user", "content": user_message}],
        stream=True  # 启用流式输出
    )
    
    for chunk in stream:
        if chunk.choices[0].delta.content is not None:
            content = chunk.choices[0].delta.content
            print(content, end="", flush=True)

stream_chat("写一首关于春天的诗")
```

**在 FastAPI 中使用流式响应**：
```python
from fastapi import FastAPI
from fastapi.responses import StreamingResponse

app = FastAPI()

@app.get("/stream-chat")
async def stream_chat(message: str):
    def generate():
        stream = client.chat.completions.create(
            model="gpt-3.5-turbo",
            messages=[{"role": "user", "content": message}],
            stream=True
        )
        for chunk in stream:
            if chunk.choices[0].delta.content:
                yield chunk.choices[0].delta.content
    
    return StreamingResponse(generate(), media_type="text/plain")
```

---

### 5. Function Calling（函数调用）

#### 5.1 基本概念

Function Calling 让 LLM 能够：
- 识别何时需要调用外部函数
- 生成正确的函数参数
- 将结果反馈给用户

**使用场景**：
- 查询数据库
- 调用天气 API
- 执行计算
- 与外部系统交互

---

#### 5.2 实战示例

```python
import json

# 定义可用的函数
def get_weather(location, unit="celsius"):
    """获取天气信息（模拟）"""
    return {
        "location": location,
        "temperature": 22,
        "unit": unit,
        "condition": "晴天"
    }

# 定义函数描述
functions = [
    {
        "name": "get_weather",
        "description": "获取指定地点的天气信息",
        "parameters": {
            "type": "object",
            "properties": {
                "location": {
                    "type": "string",
                    "description": "城市名称，例如：北京"
                },
                "unit": {
                    "type": "string",
                    "enum": ["celsius", "fahrenheit"],
                    "description": "温度单位"
                }
            },
            "required": ["location"]
        }
    }
]

def chat_with_function_calling(user_message):
    # 第一次调用：让 GPT 决定是否需要调用函数
    response = client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[{"role": "user", "content": user_message}],
        functions=functions,
        function_call="auto"
    )
    
    message = response.choices[0].message
    
    # 检查是否需要调用函数
    if message.function_call:
        function_name = message.function_call.name
        function_args = json.loads(message.function_call.arguments)
        
        # 调用实际函数
        if function_name == "get_weather":
            function_response = get_weather(**function_args)
        
        # 第二次调用：将函数结果返回给 GPT
        second_response = client.chat.completions.create(
            model="gpt-3.5-turbo",
            messages=[
                {"role": "user", "content": user_message},
                message,
                {
                    "role": "function",
                    "name": function_name,
                    "content": json.dumps(function_response)
                }
            ]
        )
        return second_response.choices[0].message.content
    
    return message.content

# 使用
result = chat_with_function_calling("北京今天天气怎么样？")
print(result)  # GPT 会调用 get_weather 函数并生成自然语言回复
```

---

### 6. Embeddings（向量嵌入）

#### 6.1 什么是 Embeddings？

- 将文本转换为高维向量（数字数组）
- 语义相似的文本有相似的向量
- 用于语义搜索、推荐系统、聚类分析

```python
def get_embedding(text):
    response = client.embeddings.create(
        model="text-embedding-ada-002",
        input=text
    )
    return response.data[0].embedding

# 获取文本的向量表示
text = "Python 是一门编程语言"
embedding = get_embedding(text)
print(f"向量维度：{len(embedding)}")  # 1536
print(f"前 5 个值：{embedding[:5]}")
```

---

#### 6.2 语义相似度计算

```python
import numpy as np

def cosine_similarity(vec1, vec2):
    """计算余弦相似度"""
    return np.dot(vec1, vec2) / (np.linalg.norm(vec1) * np.linalg.norm(vec2))

# 比较两个文本的相似度
text1 = "Python 是一门编程语言"
text2 = "JavaScript 是一种编程语言"
text3 = "今天天气很好"

emb1 = get_embedding(text1)
emb2 = get_embedding(text2)
emb3 = get_embedding(text3)

print(f"文本1 vs 文本2：{cosine_similarity(emb1, emb2):.4f}")  # 高相似度
print(f"文本1 vs 文本3：{cosine_similarity(emb1, emb3):.4f}")  # 低相似度
```

---

### 7. 完整项目示例

#### 项目：AI 聊天机器人（带流式响应）

```python
# main.py
from fastapi import FastAPI, WebSocket
from fastapi.middleware.cors import CORSMiddleware
import json

app = FastAPI()
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

class ChatSession:
    def __init__(self):
        self.messages = [
            {"role": "system", "content": "你是一个友好的 AI 助手。"}
        ]
    
    def add_message(self, role, content):
        self.messages.append({"role": role, "content": content})
    
    def get_response_stream(self):
        stream = client.chat.completions.create(
            model="gpt-3.5-turbo",
            messages=self.messages,
            stream=True
        )
        
        full_response = ""
        for chunk in stream:
            if chunk.choices[0].delta.content:
                content = chunk.choices[0].delta.content
                full_response += content
                yield content
        
        self.add_message("assistant", full_response)

sessions = {}

@app.websocket("/ws/chat/{session_id}")
async def websocket_chat(websocket: WebSocket, session_id: str):
    await websocket.accept()
    
    # 创建或获取会话
    if session_id not in sessions:
        sessions[session_id] = ChatSession()
    
    session = sessions[session_id]
    
    try:
        while True:
            # 接收用户消息
            user_message = await websocket.receive_text()
            session.add_message("user", user_message)
            
            # 流式发送 AI 回复
            for chunk in session.get_response_stream():
                await websocket.send_text(json.dumps({
                    "type": "chunk",
                    "content": chunk
                }))
            
            # 发送完成信号
            await websocket.send_text(json.dumps({
                "type": "done"
            }))
    except Exception as e:
        print(f"WebSocket error: {e}")
```

**前端示例（React）**：
```javascript
import { useState, useEffect } from 'react';

function ChatApp() {
    const [messages, setMessages] = useState([]);
    const [input, setInput] = useState('');
    const [ws, setWs] = useState(null);
    const [currentResponse, setCurrentResponse] = useState('');

    useEffect(() => {
        const websocket = new WebSocket('ws://localhost:8000/ws/chat/user123');
        
        websocket.onmessage = (event) => {
            const data = JSON.parse(event.data);
            
            if (data.type === 'chunk') {
                setCurrentResponse(prev => prev + data.content);
            } else if (data.type === 'done') {
                setMessages(prev => [...prev, {
                    role: 'assistant',
                    content: currentResponse
                }]);
                setCurrentResponse('');
            }
        };
        
        setWs(websocket);
        return () => websocket.close();
    }, []);

    const sendMessage = () => {
        if (ws && input.trim()) {
            setMessages(prev => [...prev, { role: 'user', content: input }]);
            ws.send(input);
            setInput('');
        }
    };

    return (
        <div>
            <div className="messages">
                {messages.map((msg, i) => (
                    <div key={i} className={msg.role}>
                        {msg.content}
                    </div>
                ))}
                {currentResponse && (
                    <div className="assistant">{currentResponse}</div>
                )}
            </div>
            <input 
                value={input}
                onChange={(e) => setInput(e.target.value)}
                onKeyPress={(e) => e.key === 'Enter' && sendMessage()}
            />
            <button onClick={sendMessage}>发送</button>
        </div>
    );
}
```

---

## 🎯 实战练习

### 练习 1：智能文本摘要
```python
def summarize_text(long_text, max_words=100):
    prompt = f"""
    请将以下文本总结为不超过 {max_words} 字的摘要：
    
    {long_text}
    """
    
    response = client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[{"role": "user", "content": prompt}]
    )
    return response.choices[0].message.content
```

### 练习 2：代码解释器
```python
def explain_code(code, language="python"):
    prompt = f"""
    请解释以下 {language} 代码的功能，用简单的语言说明：
    
    ```{language}
    {code}
    ```
    """
    
    response = client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[{"role": "user", "content": prompt}]
    )
    return response.choices[0].message.content
```

### 练习 3：智能搜索（语义搜索）
```python
class SemanticSearch:
    def __init__(self):
        self.documents = []
        self.embeddings = []
    
    def add_document(self, text):
        self.documents.append(text)
        self.embeddings.append(get_embedding(text))
    
    def search(self, query, top_k=3):
        query_embedding = get_embedding(query)
        
        # 计算相似度
        similarities = [
            cosine_similarity(query_embedding, doc_emb)
            for doc_emb in self.embeddings
        ]
        
        # 返回最相似的文档
        top_indices = np.argsort(similarities)[-top_k:][::-1]
        return [(self.documents[i], similarities[i]) for i in top_indices]

# 使用
search_engine = SemanticSearch()
search_engine.add_document("Python 是一门编程语言")
search_engine.add_document("机器学习是 AI 的子领域")
search_engine.add_document("今天天气很好")

results = search_engine.search("什么是编程？")
for doc, score in results:
    print(f"{score:.4f}: {doc}")
```

---

## 📖 推荐资源

### 官方文档
- **OpenAI API 文档**：https://platform.openai.com/docs
- **Prompt Engineering 指南**：https://platform.openai.com/docs/guides/prompt-engineering

### 学习资源
- **OpenAI Cookbook**：https://github.com/openai/openai-cookbook
- **Learn Prompting**：https://learnprompting.org/
- **Prompt Engineering Guide**：https://www.promptingguide.ai/

### 实战项目
- 构建智能客服
- 开发代码助手
- 创建内容生成器

---

## ✅ 学习检查清单

- [ ] 理解 LLM 和 Token 的基本概念
- [ ] 能够使用 OpenAI API 进行对话
- [ ] 掌握 Prompt Engineering 技巧
- [ ] 实现流式响应
- [ ] 理解并使用 Function Calling
- [ ] 掌握 Embeddings 和语义搜索
- [ ] 完成至少 2 个完整的 AI 应用项目

---

**下一步**：学习 [04-LangChain框架](./04-LangChain框架.md)
