# Poetry 常用命令速查表

> Poetry - Python 现代依赖管理和打包工具

## 📦 安装 Poetry

### macOS / Linux
```bash
# 官方安装脚本
curl -sSL https://install.python-poetry.org | python3 -

# macOS 使用 Homebrew
brew install poetry

# 验证安装
poetry --version
```

### Windows
```powershell
# PowerShell
(Invoke-WebRequest -Uri https://install.python-poetry.org -UseBasicParsing).Content | py -
```

---

## ⚙️ 初始配置（推荐）

```bash
# 在项目目录内创建虚拟环境（推荐）
poetry config virtualenvs.in-project true

# 查看所有配置
poetry config --list

# 使用国内镜像源（清华源）
poetry source add --priority=primary tsinghua https://pypi.tuna.tsinghua.edu.cn/simple/

# 或使用阿里云镜像
poetry source add --priority=primary aliyun https://mirrors.aliyun.com/pypi/simple/
```

---

## 🚀 项目管理

### 创建新项目

```bash
# 创建完整的项目结构
poetry new my-project

# 创建项目时指定名称（与文件夹名不同）
poetry new my-project --name my_package

# 在现有目录初始化（交互式）
poetry init

# 在现有目录初始化（非交互式）
poetry init -n
```

生成的项目结构：
```
my-project/
├── src/
│   └── my_package/    # 源代码目录
│       ├── __init__.py
│       ├── module1.py
│       └── module2.py
├── tests/             # 测试目录
│   ├── __init__.py
│   └── test_module1.py
├── docs/
├── pyproject.toml      # 项目配置文件
├── README.md
└── LICENSE
```

---

## 📚 依赖管理

### 安装依赖

```bash
# 安装 pyproject.toml 中的所有依赖
poetry install

# 仅安装生产依赖（不安装开发依赖）
poetry install --only main

# 仅安装开发依赖
poetry install --only dev

# 同步依赖（移除未在 pyproject.toml 中的包）
poetry install --sync
```

### 添加依赖

```bash
# 添加生产依赖
poetry add requests
poetry add numpy pandas

# 添加开发依赖
poetry add pytest --group dev
poetry add black flake8 mypy --group dev

# 添加指定版本
poetry add "django>=4.0,<5.0"
poetry add flask==2.3.0
poetry add "fastapi^0.100.0"  # ^表示兼容版本

# 添加 Git 仓库的包
poetry add git+https://github.com/user/repo.git

# 添加本地包
poetry add ./my-package
```

### 移除依赖

```bash
# 移除包
poetry remove requests

# 移除开发依赖
poetry remove pytest --group dev
```

### 更新依赖

```bash
# 更新所有依赖到最新兼容版本
poetry update

# 更新指定包
poetry update requests

# 更新开发依赖
poetry update --only dev

# 显示哪些包可以更新
poetry show --outdated
```

### 查看依赖

```bash
# 列出所有已安装的包
poetry show

# 树状显示依赖关系
poetry show --tree

# 只显示顶层依赖
poetry show --top-level

# 查看特定包的信息
poetry show requests

# 查看最新版本
poetry show --latest
```

---

## 🔧 虚拟环境管理

### 基本操作

```bash
# 方式1：激活虚拟环境后运行（推荐 ⭐）
poetry shell            # 激活环境
python main.py          # 直接运行，无需前缀
pytest                  # 运行测试
exit                    # 退出环境（或 Ctrl+D）

# 方式2：不激活环境，使用 poetry run（适合一次性命令）
poetry run python main.py
poetry run pytest
poetry run python -m flask run
poetry run uvicorn main:app --reload

# 推荐使用方式1，因为：
# ✅ 命令更简洁
# ✅ 更接近传统 Python 开发体验
# ✅ IDE 更容易识别环境
```

### 自定义脚本命令（最佳实践 ⭐⭐⭐）

通过在 `pyproject.toml` 中配置 `[tool.poetry.scripts]`，可以创建自定义命令别名：

#### 1. 配置 pyproject.toml

```toml
[tool.poetry.scripts]
# 格式：命令名 = "模块路径:函数名"
start = "my_project.main:main"           # 启动主程序
dev = "my_project.main:dev_server"       # 开发服务器
test = "pytest:main"                      # 运行测试
serve = "my_project.cli:serve"           # 启动服务
migrate = "my_project.db:migrate"        # 数据库迁移

# Web 框架示例
web = "my_project.app:run"               # Flask/FastAPI 应用
worker = "my_project.tasks:start_worker" # Celery Worker
```

#### 2. 使用自定义命令

```bash
# 安装项目（会注册脚本命令）
poetry install

# 使用自定义命令
poetry run start        # 启动程序
poetry run dev          # 开发模式
poetry run test         # 运行测试
poetry run serve        # 启动服务

# 在激活的环境中可以直接使用
poetry shell
start                   # 直接运行，更简洁！
dev
test
```

#### 3. 完整示例

**项目结构：**
```
my_project/
├── src/
│   └── my_project/
│       ├── __init__.py
│       ├── main.py         # 主入口
│       ├── cli.py          # CLI 命令
│       └── app.py          # Web 应用
├── tests/
├── pyproject.toml
└── README.md
```

**pyproject.toml 配置：**
```toml
[tool.poetry]
name = "my-project"
version = "0.1.0"
description = "My awesome project"
authors = ["Your Name <you@example.com>"]

[tool.poetry.dependencies]
python = "^3.11"
fastapi = "^0.104.0"
uvicorn = "^0.24.0"

[tool.poetry.group.dev.dependencies]
pytest = "^7.4.0"
black = "^23.10.0"

[tool.poetry.scripts]
start = "my_project.main:main"
dev = "my_project.main:dev"
serve = "my_project.app:serve"
cli = "my_project.cli:main"

[build-system]
requires = ["poetry-core"]
build-backend = "poetry.core.masonry.api"
```

**src/my_project/main.py：**
```python
def main():
    """生产环境入口"""
    print("Starting production server...")
    from my_project.app import app
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)

def dev():
    """开发环境入口"""
    print("Starting development server...")
    from my_project.app import app
    import uvicorn
    uvicorn.run(app, host="127.0.0.1", port=8000, reload=True)

if __name__ == "__main__":
    main()
```

**src/my_project/app.py：**
```python
from fastapi import FastAPI

app = FastAPI()

@app.get("/")
def read_root():
    return {"Hello": "World"}

def serve():
    """供 poetry scripts 调用"""
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
```

**使用方式：**
```bash
# 安装依赖并注册命令
poetry install

# 方式1：使用 poetry run
poetry run start        # 生产模式
poetry run dev          # 开发模式（支持热重载）
poetry run serve        # 启动服务

# 方式2：激活环境后直接使用（推荐）
poetry shell
start                   # 简洁！
dev                     # 简洁！
```

#### 4. 优势对比

| 方式 | 命令 | 优点 | 适用场景 |
|------|------|------|---------|
| **直接运行** | `python src/my_project/main.py` | 简单直接 | 临时测试 |
| **poetry run** | `poetry run python -m my_project.main` | 无需激活环境 | CI/CD |
| **poetry shell** | `poetry shell && python main.py` | 类似传统开发 | 长时间开发 |
| **scripts（推荐）** | `poetry run start` 或 `start` | ✅ 最简洁<br>✅ 统一团队命令<br>✅ 易于维护 | **生产项目** |

#### 5. 为什么推荐 scripts？

✅ **命令简洁统一**
```bash
# ❌ 之前：团队成员可能用不同命令
python src/main.py
python -m my_project.main
poetry run python src/main.py

# ✅ 使用 scripts：统一的命令
poetry run start
```

✅ **易于维护**
```toml
# 修改入口点只需改配置文件，不需要改文档和脚本
[tool.poetry.scripts]
start = "my_project.main:main"  # 改这里即可
```

✅ **支持打包分发**
```bash
# 安装后可在系统中全局使用
pip install my-project
my-project-start  # 自动生成的全局命令
```

✅ **适合多环境**
```toml
[tool.poetry.scripts]
prod = "my_project.main:prod"      # 生产环境
dev = "my_project.main:dev"        # 开发环境
staging = "my_project.main:staging" # 预发布环境
```

#### 6. 实际项目示例

**FastAPI 项目：**
```toml
[tool.poetry.scripts]
dev = "app.main:dev"              # 开发服务器（热重载）
start = "app.main:start"          # 生产服务器
migrate = "app.db:migrate"        # 数据库迁移
seed = "app.db:seed"              # 填充测试数据
worker = "app.tasks:start_worker" # 后台任务
```

**Django 项目：**
```toml
[tool.poetry.scripts]
runserver = "manage:runserver"
migrate = "manage:migrate"
shell = "manage:shell"
createsuperuser = "manage:createsuperuser"
```

**CLI 工具项目：**
```toml
[tool.poetry.scripts]
mytool = "mytool.cli:main"
mytool-config = "mytool.cli:configure"
mytool-update = "mytool.cli:update"
```

### 环境信息

```bash
# 查看虚拟环境信息
poetry env info

# 显示虚拟环境路径
poetry env info --path

# 列出所有虚拟环境
poetry env list

# 查看 Python 解释器路径
poetry run which python
```

### 环境管理

```bash
# 使用特定 Python 版本创建环境
poetry env use python3.11
poetry env use 3.10
poetry env use /usr/local/bin/python3.9

# 删除虚拟环境
poetry env remove python3.11
poetry env remove 3.10

# 删除所有虚拟环境
poetry env remove --all
```

---

## 🏗️ 项目构建与发布

### 构建项目

```bash
# 构建 wheel 和 tar.gz 包
poetry build

# 只构建 wheel
poetry build --format wheel

# 只构建 sdist
poetry build --format sdist
```

### 发布到 PyPI

```bash
# 配置 PyPI 凭证
poetry config pypi-token.pypi your-token-here

# 发布到 PyPI
poetry publish

# 构建并发布
poetry publish --build

# 发布到测试 PyPI
poetry publish -r testpypi
```

### 版本管理

```bash
# 查看当前版本
poetry version

# 升级版本
poetry version patch      # 0.1.0 -> 0.1.1
poetry version minor      # 0.1.0 -> 0.2.0
poetry version major      # 0.1.0 -> 1.0.0

# 设置特定版本
poetry version 1.2.3

# 预发布版本
poetry version prepatch   # 0.1.0 -> 0.1.1-alpha.0
poetry version preminor   # 0.1.0 -> 0.2.0-alpha.0
poetry version premajor   # 0.1.0 -> 1.0.0-alpha.0
```

---

## 🔍 依赖锁定

```bash
# 更新 poetry.lock（不安装）
poetry lock

# 重新解析所有依赖
poetry lock --no-update

# 检查 pyproject.toml 和 poetry.lock 是否同步
poetry check

# 检查配置文件语法
poetry check --lock
```

---

## 📤 导出依赖文件

```bash
# 导出为 requirements.txt
poetry export -f requirements.txt --output requirements.txt

# 不包含哈希值（更简洁）
poetry export -f requirements.txt --output requirements.txt --without-hashes

# 只导出生产依赖
poetry export -f requirements.txt --output requirements.txt --only main --without-hashes

# 导出开发依赖
poetry export -f requirements.txt --output requirements-dev.txt --only dev --without-hashes

# 导出所有依赖（包括开发）
poetry export -f requirements.txt --output requirements-all.txt --with dev --without-hashes
```

---

## 🔐 凭证管理

```bash
# 配置 PyPI token
poetry config pypi-token.pypi your-token

# 配置私有仓库
poetry config repositories.private https://pypi.example.com
poetry config http-basic.private username password

# 列出所有凭证
poetry config --list

# 删除凭证
poetry config pypi-token.pypi --unset
```

---

## 🧪 实用技巧

### 1. 在 CI/CD 中使用

```bash
# 安装依赖（跳过根包安装）
poetry install --no-root

# 不创建虚拟环境（使用系统环境）
poetry config virtualenvs.create false
poetry install
```

### 2. 调试依赖问题

```bash
# 详细输出
poetry add requests -vvv

# 清除缓存
poetry cache clear pypi --all

# 显示依赖解析器详情
poetry lock -vvv
```

### 3. 多 Python 版本支持

在 `pyproject.toml` 中指定：
```toml
[tool.poetry.dependencies]
python = "^3.8"  # 支持 Python 3.8 及以上
```

### 4. 依赖分组

```toml
[tool.poetry.group.dev.dependencies]
pytest = "^7.0"
black = "^23.0"

[tool.poetry.group.docs.dependencies]
sphinx = "^5.0"
```

```bash
# 安装特定组
poetry install --with docs
poetry install --without dev
poetry install --only dev
```

---

## 📋 常用工作流

### 开始新项目

```bash
# 1. 创建项目
poetry new my-project
cd my-project

# 2. 添加依赖
poetry add fastapi uvicorn

# 3. 添加开发工具
poetry add pytest black mypy --group dev

# 4. 配置自定义命令（推荐 ⭐）
# 编辑 pyproject.toml，添加：
# [tool.poetry.scripts]
# start = "my_project.main:main"
# dev = "my_project.main:dev"

# 5. 安装项目（注册脚本命令）
poetry install

# 6. 运行项目（三种方式）
# 方式1：使用自定义命令（最推荐）
poetry run start

# 方式2：激活环境后运行
poetry shell
start  # 或 python my_project/main.py

# 方式3：直接运行（不推荐）
# poetry run python my_project/main.py
```

### 克隆现有项目

```bash
# 1. 克隆仓库
git clone https://github.com/user/project.git
cd project

# 2. 安装依赖
poetry install

# 3. 激活环境
poetry shell

# 4. 运行项目
python main.py
```

### 日常开发

```bash
# 激活环境
poetry shell

# 运行测试
pytest

# 代码格式化
black .

# 类型检查
mypy .

# 添加新依赖
poetry add new-package

# 更新依赖
poetry update

# 退出环境
exit
```

---

## 🆚 Poetry vs pip/venv

| 特性 | Poetry | pip + venv |
|------|--------|-----------|
| **依赖管理** | ✅ 自动解析冲突 | ❌ 手动处理 |
| **锁定版本** | ✅ poetry.lock | ❌ 需手动 freeze |
| **虚拟环境** | ✅ 自动管理 | ❌ 手动创建 |
| **打包发布** | ✅ 一键完成 | ❌ 需多步骤 |
| **配置文件** | ✅ pyproject.toml | ❌ 多个文件 |
| **依赖分组** | ✅ 支持 | ❌ 不支持 |

---

## 🐛 常见问题

### 1. 虚拟环境找不到

```bash
# 显示环境路径
poetry env info --path

# 重建环境
poetry env remove python3.11
poetry install
```

### 2. 依赖冲突

```bash
# 详细输出查看冲突
poetry add package-name -vvv

# 清除缓存重试
poetry cache clear pypi --all
poetry lock --no-cache
```

### 3. Poetry 本身更新

```bash
# Homebrew 安装的
brew upgrade poetry

# 官方脚本安装的
poetry self update
```

### 4. 速度慢

```bash
# 使用国内镜像
poetry source add --priority=primary tsinghua https://pypi.tuna.tsinghua.edu.cn/simple/

# 并行安装（实验性）
poetry config installer.parallel true
```

---

## 📖 相关文件

### pyproject.toml 基本结构

```toml
[tool.poetry]
name = "my-project"
version = "0.1.0"
description = "项目描述"
authors = ["Your Name <you@example.com>"]
readme = "README.md"

[tool.poetry.dependencies]
python = "^3.8"
requests = "^2.28.0"
fastapi = "^0.100.0"

[tool.poetry.group.dev.dependencies]
pytest = "^7.0"
black = "^23.0"
mypy = "^1.0"

[build-system]
requires = ["poetry-core"]
build-backend = "poetry.core.masonry.api"

[tool.poetry.scripts]
my-cli = "my_project.cli:main"
```

---

## 🔗 相关资源

- 官方文档：https://python-poetry.org/docs/
- GitHub：https://github.com/python-poetry/poetry
- PyPI：https://pypi.org/project/poetry/

---

## 💡 快速参考

```bash
# 项目管理
poetry new project-name        # 创建新项目
poetry init                    # 初始化现有项目
poetry add package-name        # 添加依赖
poetry install                 # 安装依赖并注册脚本命令

# 运行项目（推荐方式 ⭐）
# 1. 在 pyproject.toml 中配置：
#    [tool.poetry.scripts]
#    start = "my_project.main:main"
#    dev = "my_project.main:dev"

# 2. 使用自定义命令
poetry run start               # 方式1：使用 poetry run
poetry shell                   # 方式2：激活环境
start                          # 然后直接运行命令

# 其他常用命令
poetry update                  # 更新依赖
poetry show --tree             # 查看依赖树
poetry check                   # 检查配置
```

---

**最后更新：2025-11-18**

