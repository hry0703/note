# Poetry 常用命令速查（面向前端工程师）

> **定位**：Poetry = Python 的 npm + 虚拟环境管理 + 构建工具  
> **场景**：FastAPI、AI Agent、RAG 等 Python 项目的依赖与环境管理

---

## 🌱 0. 安装与版本

```bash
# 安装（官方推荐）
curl -sSL https://install.python-poetry.org | python3 -

# 查看版本
poetry --version
```

---

## 📂 1. 项目创建与初始化

### 1.1 新建项目

```bash
# 创建一个新的 Python 项目（推荐）
poetry new my-project

# 生成结构：
# my-project/
#   ├── pyproject.toml      # 类似 package.json
#   ├── my_project/         # 源码目录
#   └── tests/              # 测试目录
```

### 1.2 在已有项目中初始化

```bash
cd existing-project
poetry init
# 按提示填写：项目名、版本、依赖等
```

---

## 📦 2. 依赖管理（最常用）

### 2.1 安装依赖

```bash
# 根据 pyproject.toml 安装所有依赖（首次或换新机器）
poetry install
```

### 2.2 添加依赖

```bash
# 添加运行时依赖（生产 + 开发都会用到）
poetry add fastapi uvicorn[standard]
poetry add langchain chromadb

# 添加开发依赖（只在开发/测试环境用）
poetry add --group dev pytest black flake8 mypy
```

### 2.3 移除依赖

```bash
# 移除运行时依赖
poetry remove fastapi

# 移除开发依赖
poetry remove --group dev pytest
```

### 2.4 更新依赖

```bash
# 更新所有依赖
poetry update

# 只更新某个包
poetry update fastapi
```

---

## 🐍 3. 虚拟环境与运行

> Poetry 2.0 之后推荐多用 `poetry run`，少用（或通过插件恢复）`poetry shell`。

### 3.1 查看虚拟环境

```bash
# 查看当前项目的虚拟环境信息
poetry env info

# 只看虚拟环境路径
poetry env info --path
```

### 3.2 指定 Python 版本 & 创建环境

```bash
# 为当前项目指定 Python 解释器
poetry env use python3.11
# 或用绝对路径
poetry env use /usr/local/bin/python3.11
```

> 一般不需要手动 `create`，`poetry install` 会自动创建虚拟环境。

### 3.3 直接运行命令（⭐ 最推荐）

```bash
# 不显式激活虚拟环境，直接在 Poetry 环境中运行
poetry run python main.py
poetry run uvicorn main:app --reload
poetry run pytest
poetry run python -m fastp  # 运行包入口
```

### 3.4 激活 / 退出虚拟环境（Poetry 2.0+ 推荐方式）

```bash
# 激活虚拟环境（Poetry 2.0+）
poetry env activate

# 然后可以直接使用 python / pytest / uvicorn
python main.py
pytest

# 退出虚拟环境
deactivate
```

> 如果你安装了 `poetry-plugin-shell`，也可以用老命令：
>
> ```bash
> poetry shell   # 进入虚拟环境
> exit           # 退出
> ```

---

## 🔍 4. 查看与导出依赖

### 4.1 查看依赖

```bash
# 简单列表
poetry show

# 查看依赖树（哪个包依赖了谁）
poetry show --tree
```

### 4.2 导出为 requirements.txt（给 Docker / 服务器用）

```bash
# 导出所有运行时依赖
poetry export \
  -f requirements.txt \
  --output requirements.txt \
  --only main \
  --without-hashes

# 导出开发依赖
poetry export \
  -f requirements.txt \
  --output requirements-dev.txt \
  --only dev \
  --without-hashes
```

---

## 🏗 5. 构建与发布（以后做库时会用）

### 5.1 构建发行包

```bash
poetry build
# 生成 dist/*.whl 和 dist/*.tar.gz
```

### 5.2 发布到 PyPI（需要先配置账号）

```bash
poetry publish

# 或显式分步
poetry build
poetry publish
```

---

## ⚙️ 6. 常用配置

### 6.1 虚拟环境放在项目内（推荐）

```bash
# 设置后，新项目会在项目根目录创建 .venv/
poetry config virtualenvs.in-project true
```

项目结构示例：

```text
my-project/
├── .venv/              # Poetry 创建的虚拟环境（不提交到 Git）
├── pyproject.toml      # 项目 & 依赖配置
├── poetry.lock         # 锁定依赖版本
├── my_project/
└── tests/
```

### 6.2 查看配置

```bash
poetry config --list
```

---

## 🧪 7. 搭配 FastAPI / AI Agent 的常见命令组合

在你的 AI Agent / FastAPI 项目里，最常用的大概就是这些：

```bash
# 初始化/安装（第一次或换新机器）
poetry install

# 添加依赖
poetry add fastapi uvicorn[standard]
poetry add langchain langchain-openai chromadb

# 启动开发服务器
poetry run uvicorn main:app --reload

# 运行测试
poetry run pytest

# 查看依赖树
poetry show --tree

# 导出 requirements.txt（给 Docker 用）
poetry export -f requirements.txt --output requirements.txt --only main --without-hashes
```

---

## 📝 对前端工程师的记忆小抄

| 前端命令 | Poetry 对应 |
|----------|-------------|
| `npm init` | `poetry init` / `poetry new` |
| `npm install` | `poetry install` |
| `npm install axios` | `poetry add requests` |
| `npm install -D jest` | `poetry add --group dev pytest` |
| `npm run dev` | `poetry run uvicorn main:app --reload` |
| `npm run test` | `poetry run pytest` |
| `package.json` | `pyproject.toml` |
| `package-lock.json` | `poetry.lock` |

只要把 Poetry 当成“Python 世界里的 npm + venv + 构建工具”，配合这张表来对照，很快就会顺手。🚀

