# 文档问答 RAG Agent

基于 RAG（检索增强生成）的文档问答 Agent，支持对本地 Markdown 文档进行智能问答。

## 创建项目步骤（Poetry）

### 1. 进入项目目录

```bash
cd /Users/edenhuang/Desktop/脚本/python-ai/demo/rag-agent
```

### 2. 创建虚拟环境并安装依赖

```bash

pip install poetry

# 若报错 python 找不到，先执行：
poetry env use $(which python3)

# 安装依赖（仅安装到本项目 .venv）
# 如果希望项目下有 .venv 目录，可以执行：
poetry config virtualenvs.in-project true
poetry env remove python
poetry install

#激活本地环境
source .venv/Scripts/activate

```

### 3. 配置环境变量

复制 `.env.example` 为 `.env`，填入 OpenAI API Key：

```bash
cp .env.example .env
# 编辑 .env，设置 OPENAI_API_KEY
```

### 4. 启动服务

```bash
# 双栈（IPv4+IPv6），任选其一：
poetry run python main.py
# 或
poetry run python -m uvicorn main:app --reload --host "" --port 8002
```

访问 http://localhost:8002/docs 进行问答测试。

### 5. 启动前端（Vue 3）

```bash
cd frontend
npm install
npm run dev
```

访问 http://localhost:5173 使用问答界面。需同时运行后端（端口 8002）。

### 6. 接口说明

| 接口 | 方法 | 说明 |
|------|------|------|
| `/` | GET | 健康检查 |
| `/health` | GET | 服务状态 |
| `/ask` | POST | 文档问答，请求体 `{"question": "..."}` |

---

## 注意点

- **依赖隔离**：所有依赖仅安装在 `rag-agent/.venv`，不影响系统和其他项目
- **Python 版本**：要求 Python 3.11+
- **API Key**：`.env` 不要提交到 Git
- **端口**：默认 8002，避免与 fast 项目 8000 冲突
