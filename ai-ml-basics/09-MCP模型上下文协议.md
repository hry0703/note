# MCP（模型上下文协议）基础

## 📋 什么是 MCP？

**MCP（Model Context Protocol，模型上下文协议）**是一种用于 AI 应用与外部数据源、工具和服务交互的标准化协议。

## 🎯 核心概念

### 目的

- 标准化 AI 应用与外部系统的交互
- 提供统一的接口访问各种资源
- 支持工具调用和函数执行
- 管理上下文信息

### 关键特性

- **标准化接口**：统一的 API 设计
- **工具集成**：支持外部工具调用
- **上下文管理**：管理对话和任务上下文
- **资源访问**：访问外部数据源

## 🏗️ MCP 架构

### 基本结构

```
AI 应用
    ↓
MCP 客户端
    ↓
MCP 服务器
    ↓
外部资源（工具、数据、服务）
```

### 核心组件

1. **MCP 客户端**
   - AI 应用
   - 发起请求
   - 处理响应

2. **MCP 服务器**
   - 提供工具和服务
   - 处理请求
   - 返回结果

3. **协议层**
   - 消息格式
   - 通信协议
   - 错误处理

## 💡 主要功能

### 1. 工具调用（Tool Calling）

```json
{
  "type": "tool_call",
  "tool": "search",
  "parameters": {
    "query": "Python tutorial"
  }
}
```

### 2. 资源访问（Resource Access）

```json
{
  "type": "resource_request",
  "resource": "file://data.txt",
  "action": "read"
}
```

### 3. 上下文管理（Context Management）

```json
{
  "type": "context_update",
  "context": {
    "session_id": "123",
    "messages": [...]
  }
}
```

## 🔧 使用场景

### 1. 函数调用

```python
# AI 应用通过 MCP 调用外部函数
response = mcp_client.call_tool(
    tool="calculate",
    parameters={"expression": "2 + 2"}
)
```

### 2. 数据访问

```python
# 通过 MCP 访问外部数据
data = mcp_client.get_resource(
    resource="database://users",
    query="SELECT * FROM users"
)
```

### 3. 服务集成

```python
# 集成外部服务
result = mcp_client.invoke_service(
    service="email",
    method="send",
    params={"to": "user@example.com", "subject": "Hello"}
)
```

## 📝 协议格式

### 请求格式

```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "method": "tools/call",
  "params": {
    "name": "tool_name",
    "arguments": {}
  }
}
```

### 响应格式

```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "result": {
    "content": "result_data"
  }
}
```

## 🎯 实现示例

### MCP 客户端

```python
class MCPClient:
    def __init__(self, server_url):
        self.server_url = server_url
    
    def call_tool(self, tool_name, parameters):
        request = {
            "method": "tools/call",
            "params": {
                "name": tool_name,
                "arguments": parameters
            }
        }
        response = self.send_request(request)
        return response["result"]
    
    def get_resource(self, resource_uri):
        request = {
            "method": "resources/read",
            "params": {
                "uri": resource_uri
            }
        }
        response = self.send_request(request)
        return response["result"]
```

### MCP 服务器

```python
class MCPServer:
    def __init__(self):
        self.tools = {}
        self.resources = {}
    
    def register_tool(self, name, handler):
        self.tools[name] = handler
    
    def handle_request(self, request):
        method = request["method"]
        if method == "tools/call":
            return self.handle_tool_call(request["params"])
        elif method == "resources/read":
            return self.handle_resource_read(request["params"])
    
    def handle_tool_call(self, params):
        tool_name = params["name"]
        arguments = params["arguments"]
        handler = self.tools[tool_name]
        result = handler(**arguments)
        return {"content": result}
```

## ⚡ 优势

- ✅ **标准化**：统一的接口和协议
- ✅ **可扩展**：易于添加新工具和服务
- ✅ **解耦**：AI 应用与外部系统解耦
- ✅ **灵活性**：支持多种使用场景

## 🔗 相关概念

- **Function Calling**：函数调用
- **Tool Use**：工具使用
- **API Integration**：API 集成
- **Context Management**：上下文管理
- **Resource Protocol**：资源协议

---

*最后更新：2024年*
