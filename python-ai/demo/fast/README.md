# FastAPI 项目

## 快速启动

```bash
cd /Users/edenhuang/Desktop/脚本/python-ai/demo/fast

# 首次运行：若报错 python 找不到，先执行
poetry env use $(which python3)

poetry install
poetry run python -m uvicorn fastp.main:app --reload --host 0.0.0.0 --port 8000
```

## 访问地址

- API: http://localhost:8000/
- 文档: http://localhost:8000/docs

## 常见问题

| 问题 | 解决 |
|------|------|
| `python` 找不到 | `poetry env use $(which python3)` 后重新 `poetry install` |
| 端口被占用 | 改用 `--port 8001` |
| 模块找不到 | 确认已执行 `poetry install` |

## 调试

- 按 F5 启动调试（需配置 `.vscode/launch.json`）
- 可在 `src/fastp/main.py` 设置断点
