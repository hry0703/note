# Function Calling（函数调用）基础

## 📋 什么是 Function Calling？

**Function Calling（函数调用）**是让 AI 模型能够调用外部函数或工具的能力，使 AI 能够执行实际的操作而不仅仅是生成文本。

## 🎯 核心概念

### 目的

- 扩展 AI 的能力边界
- 连接 AI 与外部系统
- 执行实际的操作
- 获取实时数据

### 工作流程

```
用户请求
    ↓
AI 分析需求
    ↓
识别需要调用的函数
    ↓
生成函数调用请求
    ↓
执行函数
    ↓
返回结果给 AI
    ↓
AI 生成最终回答
```

## 🏗️ Function Calling 架构

### 基本结构

```
AI 模型
    ↓
函数定义（Function Definitions）
    ↓
函数调用决策（Function Call Decision）
    ↓
函数执行器（Function Executor）
    ↓
外部函数/工具
    ↓
结果返回
```

### 关键组件

1. **函数定义**
   - 函数名称
   - 参数定义
   - 函数描述

2. **调用决策**
   - 分析用户意图
   - 选择合适函数
   - 生成调用参数

3. **函数执行**
   - 参数验证
   - 执行函数
   - 错误处理

## 💡 函数定义格式

### OpenAI 格式

```json
{
  "type": "function",
  "function": {
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
}
```

### 调用示例

```json
{
  "name": "get_weather",
  "arguments": "{\"location\": \"北京\", \"unit\": \"celsius\"}"
}
```

## 🔧 实现示例

### 1. 定义函数

```python
functions = [
    {
        "name": "get_weather",
        "description": "获取天气信息",
        "parameters": {
            "type": "object",
            "properties": {
                "location": {
                    "type": "string",
                    "description": "城市名称"
                }
            },
            "required": ["location"]
        }
    },
    {
        "name": "send_email",
        "description": "发送电子邮件",
        "parameters": {
            "type": "object",
            "properties": {
                "to": {"type": "string"},
                "subject": {"type": "string"},
                "body": {"type": "string"}
            },
            "required": ["to", "subject", "body"]
        }
    }
]
```

### 2. 实现函数

```python
def get_weather(location):
    """获取天气信息"""
    # 调用天气 API
    weather_data = weather_api.get(location)
    return {
        "temperature": weather_data.temp,
        "condition": weather_data.condition
    }

def send_email(to, subject, body):
    """发送邮件"""
    email_client.send(to, subject, body)
    return {"status": "sent"}
```

### 3. 处理函数调用

```python
def handle_function_call(function_name, arguments):
    """处理函数调用"""
    # 解析参数
    params = json.loads(arguments)
    
    # 执行函数
    if function_name == "get_weather":
        result = get_weather(**params)
    elif function_name == "send_email":
        result = send_email(**params)
    else:
        raise ValueError(f"Unknown function: {function_name}")
    
    return result
```

### 4. 完整流程

```python
import openai

# 1. 发送请求（包含函数定义）
response = openai.ChatCompletion.create(
    model="gpt-4",
    messages=[
        {"role": "user", "content": "北京今天天气怎么样？"}
    ],
    functions=functions,
    function_call="auto"
)

# 2. 检查是否需要调用函数
message = response.choices[0].message
if message.get("function_call"):
    # 3. 执行函数
    function_name = message["function_call"]["name"]
    arguments = message["function_call"]["arguments"]
    function_result = handle_function_call(function_name, arguments)
    
    # 4. 将结果返回给 AI
    response = openai.ChatCompletion.create(
        model="gpt-4",
        messages=[
            {"role": "user", "content": "北京今天天气怎么样？"},
            message,
            {
                "role": "function",
                "name": function_name,
                "content": json.dumps(function_result)
            }
        ],
        functions=functions
    )
    
    # 5. 获取最终回答
    final_answer = response.choices[0].message.content
```

## 🎯 应用场景

### 1. 工具调用

```python
# AI 可以调用各种工具
functions = [
    {"name": "search_web", ...},
    {"name": "read_file", ...},
    {"name": "execute_code", ...}
]
```

### 2. 数据获取

```python
# 获取实时数据
functions = [
    {"name": "get_stock_price", ...},
    {"name": "get_news", ...},
    {"name": "query_database", ...}
]
```

### 3. 操作执行

```python
# 执行实际操作
functions = [
    {"name": "send_email", ...},
    {"name": "create_calendar_event", ...},
    {"name": "place_order", ...}
]
```

## ⚡ 最佳实践

### 1. 清晰的函数描述

```python
{
    "name": "calculate",
    "description": "执行数学计算。支持加减乘除运算。",
    "parameters": {
        "properties": {
            "expression": {
                "description": "数学表达式，例如：'2 + 2' 或 '10 * 5'"
            }
        }
    }
}
```

### 2. 参数验证

```python
def validate_arguments(function_name, arguments, schema):
    """验证参数"""
    try:
        validate(instance=arguments, schema=schema)
        return True
    except ValidationError as e:
        raise ValueError(f"Invalid arguments: {e}")
```

### 3. 错误处理

```python
def safe_function_call(function_name, arguments):
    """安全的函数调用"""
    try:
        result = handle_function_call(function_name, arguments)
        return {"success": True, "result": result}
    except Exception as e:
        return {"success": False, "error": str(e)}
```

## 🔗 相关概念

- **Tool Use**：工具使用
- **API Calling**：API 调用
- **Plugin System**：插件系统
- **Skill**：技能
- **MCP**：模型上下文协议

---

*最后更新：2024年*
