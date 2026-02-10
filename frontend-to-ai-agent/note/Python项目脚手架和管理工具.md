# Python 项目脚手架和管理工具

> **适合对象**：从前端转 Python 开发的工程师  
> **对比**：Python 工具 vs JavaScript/Node.js 工具

---

## 🎯 工具对比总览

| 功能 | JavaScript/Node.js | Python |
|------|-------------------|--------|
| **脚手架** | create-react-app, Vite | Cookiecutter, copier |
| **包管理** | npm, yarn, pnpm | pip, Poetry, Pipenv, PDM |
| **虚拟环境** | node_modules | venv, virtualenv, conda |
| **运行脚本** | npm run | poetry run, make |
| **版本管理** | nvm | pyenv |
| **Monorepo** | Turborepo, Nx | Poetry workspaces |

---

## 📦 包管理工具

### 1. Poetry（⭐ 最推荐）

**简介**：Python 的现代化包管理工具，类似 npm/yarn

**特点**：
- 📦 依赖管理 + 虚拟环境 + 打包 一体化
- 🔒 自动生成锁文件（poetry.lock）
- 🚀 使用简单，体验好
- 📝 使用 pyproject.toml（现代标准）

#### 安装

```bash
# Mac/Linux
curl -sSL https://install.python-poetry.org | python3 -

# Windows（PowerShell）
(Invoke-WebRequest -Uri https://install.python-poetry.org -UseBasicParsing).Content | python -

# 或使用 pip
pip install poetry
```

#### 基本使用

```bash
# 创建新项目
poetry new my-project
# 生成结构：
# my-project/
# ├── my_project/
# │   └── __init__.py
# ├── tests/
# │   └── __init__.py
# ├── pyproject.toml
# └── README.md

# 在现有项目初始化
cd existing-project
poetry init

# 添加依赖
poetry add fastapi
poetry add --group dev pytest  # 开发依赖

# 安装所有依赖
poetry install

# 运行命令
poetry run python main.py
poetry run pytest

# 激活虚拟环境
poetry shell

# 更新依赖
poetry update

# 显示依赖树
poetry show --tree

# 构建和发布
poetry build
poetry publish
```

#### pyproject.toml 示例

```toml
[tool.poetry]
name = "my-project"
version = "0.1.0"
description = "My awesome project"
authors = ["Your Name <you@example.com>"]
readme = "README.md"

[tool.poetry.dependencies]
python = "^3.11"
fastapi = "^0.104.1"
uvicorn = {extras = ["standard"], version = "^0.24.0"}
langchain = "^0.1.0"

[tool.poetry.group.dev.dependencies]
pytest = "^7.4.3"
black = "^23.11.0"
flake8 = "^6.1.0"
mypy = "^1.7.1"

[build-system]
requires = ["poetry-core"]
build-backend = "poetry.core.masonry.api"
```

#### 对比 npm/yarn

| 功能 | npm/yarn | Poetry |
|------|----------|--------|
| 初始化 | `npm init` | `poetry init` |
| 安装依赖 | `npm install` | `poetry install` |
| 添加依赖 | `npm install pkg` | `poetry add pkg` |
| 删除依赖 | `npm uninstall pkg` | `poetry remove pkg` |
| 运行脚本 | `npm run script` | `poetry run script` |
| 更新依赖 | `npm update` | `poetry update` |
| 锁文件 | `package-lock.json` | `poetry.lock` |
| 配置文件 | `package.json` | `pyproject.toml` |

---

### 2. Pipenv

**简介**：结合 pip 和 virtualenv 的工具

**特点**：
- 📦 自动管理虚拟环境
- 🔒 生成 Pipfile.lock
- 🛡️ 安全检查

#### 使用

```bash
# 安装
pip install pipenv

# 创建项目
pipenv install

# 添加依赖
pipenv install fastapi
pipenv install --dev pytest

# 激活环境
pipenv shell

# 运行命令
pipenv run python main.py
```

---

### 3. PDM

**简介**：符合 PEP 标准的现代包管理器

**特点**：
- 🚀 快速
- 📦 不创建虚拟环境（使用 PEP 582）
- 🎯 严格遵循标准

#### 使用

```bash
# 安装
pip install pdm

# 初始化
pdm init

# 添加依赖
pdm add fastapi

# 运行
pdm run python main.py
```

---

### 4. 传统方式（pip + venv）

```bash
# 创建虚拟环境
python -m venv venv

# 激活
source venv/bin/activate  # Mac/Linux
venv\Scripts\activate     # Windows

# 安装依赖
pip install -r requirements.txt

# 保存依赖
pip freeze > requirements.txt
```

---

## 🏗️ 项目脚手架工具

### 1. Cookiecutter（⭐ 最推荐）

**简介**：基于模板的项目生成器，类似 create-react-app

**特点**：
- 🎨 丰富的模板库
- 🔧 可自定义模板
- 🌍 支持所有语言

#### 安装

```bash
pip install cookiecutter
# 或
pipx install cookiecutter  # 推荐使用 pipx
```

#### 使用

```bash
# 使用在线模板
cookiecutter https://github.com/audreyfeldroy/cookiecutter-pypackage

# 使用 GitHub 短链接
cookiecutter gh:audreyfeldroy/cookiecutter-pypackage

# 使用本地模板
cookiecutter path/to/template

# FastAPI 模板
cookiecutter gh:tiangolo/full-stack-fastapi-postgresql
```

#### 热门模板

| 模板 | 用途 | GitHub |
|------|------|--------|
| **cookiecutter-pypackage** | Python 包 | audreyfeldroy/cookiecutter-pypackage |
| **full-stack-fastapi** | FastAPI 全栈 | tiangolo/full-stack-fastapi-postgresql |
| **cookiecutter-django** | Django 项目 | pydanny/cookiecutter-django |
| **cookiecutter-flask** | Flask 项目 | cookiecutter-flask/cookiecutter-flask |
| **cookiecutter-data-science** | 数据科学 | drivendata/cookiecutter-data-science |

#### 创建自定义模板

```bash
# 模板结构
my-template/
├── {{cookiecutter.project_slug}}/
│   ├── __init__.py
│   └── main.py
├── tests/
├── cookiecutter.json
└── README.md
```

**cookiecutter.json**:
```json
{
  "project_name": "My Project",
  "project_slug": "{{ cookiecutter.project_name.lower().replace(' ', '_') }}",
  "author_name": "Your Name",
  "python_version": "3.11"
}
```

---

### 2. Copier

**简介**：另一个项目模板生成器，支持模板更新

**特点**：
- 🔄 可更新模板（重要）
- 🎯 简单易用
- 📝 使用 Jinja2 模板

#### 使用

```bash
# 安装
pipx install copier

# 生成项目
copier copy https://github.com/username/template my-project

# 更新项目（保留修改）
copier update
```

---

### 3. FastAPI 官方脚手架

```bash
# 安装
pip install fastapi-cli

# 创建项目
fastapi create my-app

# 或使用 tiangolo 的全栈模板
cookiecutter gh:tiangolo/full-stack-fastapi-postgresql
```

---

## 🛠️ 项目管理工具

### 1. Make（跨平台脚本管理）

**Makefile 示例**:

```makefile
.PHONY: install dev test lint format clean

# 安装依赖
install:
	poetry install

# 开发模式运行
dev:
	poetry run uvicorn app.main:app --reload

# 运行测试
test:
	poetry run pytest tests/ -v

# 代码检查
lint:
	poetry run flake8 app/ tests/
	poetry run mypy app/

# 代码格式化
format:
	poetry run black app/ tests/
	poetry run isort app/ tests/

# 清理缓存
clean:
	find . -type d -name "__pycache__" -exec rm -rf {} +
	find . -type f -name "*.pyc" -delete
	rm -rf .pytest_cache

# 构建
build:
	poetry build

# 运行所有检查
check: lint test
```

**使用**:
```bash
make install  # 安装依赖
make dev      # 启动开发服务器
make test     # 运行测试
make format   # 格式化代码
```

---

### 2. Task（现代化的 Make 替代品）

**taskfile.yml**:

```yaml
version: '3'

tasks:
  install:
    desc: Install dependencies
    cmds:
      - poetry install

  dev:
    desc: Run development server
    cmds:
      - poetry run uvicorn app.main:app --reload

  test:
    desc: Run tests
    cmds:
      - poetry run pytest tests/ -v

  lint:
    desc: Lint code
    cmds:
      - poetry run flake8 app/ tests/
      - poetry run mypy app/

  format:
    desc: Format code
    cmds:
      - poetry run black app/ tests/
      - poetry run isort app/ tests/
```

---

### 3. Pre-commit（Git 钩子管理）

**特点**：
- 🔍 提交前自动检查代码
- 🎯 统一团队代码风格
- 🚀 支持多种语言

**.pre-commit-config.yaml**:

```yaml
repos:
  - repo: https://github.com/pre-commit/pre-commit-hooks
    rev: v4.5.0
    hooks:
      - id: trailing-whitespace
      - id: end-of-file-fixer
      - id: check-yaml
      - id: check-json
      - id: check-added-large-files

  - repo: https://github.com/psf/black
    rev: 23.11.0
    hooks:
      - id: black

  - repo: https://github.com/pycqa/isort
    rev: 5.12.0
    hooks:
      - id: isort

  - repo: https://github.com/pycqa/flake8
    rev: 6.1.0
    hooks:
      - id: flake8
```

**安装和使用**:

```bash
# 安装
pip install pre-commit

# 安装 git 钩子
pre-commit install

# 手动运行所有钩子
pre-commit run --all-files
```

---

## 🐍 Python 版本管理

### 1. pyenv（⭐ 推荐）

**简介**：类似 nvm，管理多个 Python 版本

```bash
# Mac 安装
brew install pyenv

# Linux 安装
curl https://pyenv.run | bash

# 列出可用版本
pyenv install --list

# 安装特定版本
pyenv install 3.11.5

# 设置全局版本
pyenv global 3.11.5

# 设置项目版本
pyenv local 3.11.5  # 生成 .python-version 文件

# 查看已安装版本
pyenv versions
```

---

### 2. Conda

**简介**：科学计算常用，功能强大

```bash
# 创建环境
conda create -n myenv python=3.11

# 激活环境
conda activate myenv

# 安装包
conda install numpy pandas

# 导出环境
conda env export > environment.yml

# 从文件创建环境
conda env create -f environment.yml
```

---

## 🚀 完整工作流推荐

### 推荐组合 1：现代化工作流（⭐ 最推荐）

```bash
# 工具链
- Poetry: 包管理
- Cookiecutter: 项目生成
- pyenv: Python 版本管理
- Make/Task: 脚本管理
- Pre-commit: 代码检查

# 工作流
1. 使用 pyenv 安装 Python 版本
2. 使用 Cookiecutter 生成项目
3. 使用 Poetry 管理依赖
4. 使用 Make 管理任务
5. 使用 Pre-commit 自动检查
```

### 推荐组合 2：传统工作流

```bash
# 工具链
- pip + venv: 包管理
- 手动创建项目结构
- requirements.txt: 依赖管理
- Shell 脚本: 任务管理
```

---

## 📋 项目初始化完整示例

### 使用 Poetry + Cookiecutter

```bash
# 1. 安装工具
pip install poetry cookiecutter

# 2. 使用 Cookiecutter 生成项目
cookiecutter gh:tiangolo/full-stack-fastapi-postgresql

# 3. 进入项目
cd my-project

# 4. 使用 Poetry 管理依赖
poetry install

# 5. 添加项目依赖
poetry add langchain langchain-openai chromadb

# 6. 添加开发依赖
poetry add --group dev pytest black flake8 mypy

# 7. 创建 Makefile
cat > Makefile << 'EOF'
.PHONY: install dev test

install:
	poetry install

dev:
	poetry run uvicorn app.main:app --reload

test:
	poetry run pytest
EOF

# 8. 安装 pre-commit
poetry add --group dev pre-commit
poetry run pre-commit install

# 9. 运行项目
make dev
```

---

## 🎨 AI Agent 项目专用脚手架

### 创建 AI Agent Cookiecutter 模板

**项目结构**:

```
cookiecutter-ai-agent/
├── {{cookiecutter.project_slug}}/
│   ├── app/
│   │   ├── agents/
│   │   ├── tools/
│   │   ├── rag/
│   │   └── main.py
│   ├── tests/
│   ├── pyproject.toml
│   └── .env.example
├── cookiecutter.json
└── hooks/
    └── post_gen_project.py
```

**cookiecutter.json**:

```json
{
  "project_name": "My AI Agent",
  "project_slug": "{{ cookiecutter.project_name.lower().replace(' ', '_').replace('-', '_') }}",
  "author_name": "Your Name",
  "author_email": "your.email@example.com",
  "python_version": ["3.11", "3.10", "3.9"],
  "use_poetry": ["yes", "no"],
  "use_langchain": ["yes", "no"],
  "use_fastapi": ["yes", "no"],
  "open_source_license": ["MIT", "BSD", "Apache", "None"]
}
```

---

## 📊 工具选择建议

### 小型项目（学习/原型）

```bash
✅ venv + pip + requirements.txt
✅ 手动创建项目结构
✅ 简单的 Makefile
```

### 中型项目（生产应用）

```bash
✅ Poetry
✅ Cookiecutter（可选）
✅ Make/Task
✅ Pre-commit
✅ pyenv
```

### 大型项目（企业级）

```bash
✅ Poetry + Workspaces
✅ Cookiecutter 自定义模板
✅ Task + CI/CD
✅ Pre-commit + 完整的代码质量工具链
✅ Docker + Docker Compose
```

---

## 🔧 实用工具集合

### 代码质量工具

```bash
# 格式化
poetry add --group dev black isort

# 检查
poetry add --group dev flake8 pylint mypy

# 测试
poetry add --group dev pytest pytest-cov pytest-asyncio

# 文档
poetry add --group dev sphinx mkdocs
```

### 开发工具

```bash
# 调试
poetry add --group dev ipython ipdb

# 热重载
poetry add --group dev watchdog

# 性能分析
poetry add --group dev py-spy memory-profiler
```

---

## 📖 学习资源

### 官方文档

- **Poetry**: https://python-poetry.org/
- **Cookiecutter**: https://cookiecutter.readthedocs.io/
- **Pipenv**: https://pipenv.pypa.io/
- **pyenv**: https://github.com/pyenv/pyenv

### 模板仓库

- **Awesome Cookiecutters**: https://github.com/cookiecutter/cookiecutter#data-science
- **FastAPI 模板**: https://github.com/tiangolo/full-stack-fastapi-postgresql

---

## ✅ 总结

### 核心推荐

1. **包管理**: 使用 **Poetry**（现代化，体验好）
2. **脚手架**: 使用 **Cookiecutter**（灵活，模板多）
3. **版本管理**: 使用 **pyenv**（类似 nvm）
4. **任务管理**: 使用 **Make** 或 **Task**
5. **代码检查**: 使用 **Pre-commit**

### 快速开始模板

```bash
# 1. 安装必要工具
pip install poetry cookiecutter

# 2. 生成项目
cookiecutter gh:your-template

# 3. 初始化
cd my-project
poetry install
poetry run pre-commit install

# 4. 开发
make dev  # 或 poetry run uvicorn app.main:app --reload

# 5. 测试
make test  # 或 poetry run pytest
```

**记住**：
- Poetry = npm/yarn（包管理）
- Cookiecutter = create-react-app（项目生成）
- pyenv = nvm（版本管理）
- Make = npm scripts（任务管理）

从前端转 Python，工具链思维是相通的！🎉
