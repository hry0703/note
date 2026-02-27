## 本地开发与运行指南

### 环境信息
- **项目路径**: `/Users/edenhuang/Desktop/脚本/frontend-to-ai-agent/demo/fast`
- **Python 版本**: 3.14.0
- **虚拟环境路径**: `.venv`（即 `/Users/edenhuang/Desktop/脚本/frontend-to-ai-agent/demo/fast/.venv`）
- **主要依赖**:
  - `fastapi 0.128.6`
  - `uvicorn 0.40.0`

---

### 一、激活虚拟环境

在终端中执行以下命令（建议使用 zsh / bash）：

```bash
cd "/Users/edenhuang/Desktop/脚本/frontend-to-ai-agent/demo/fast"
source .venv/bin/activate
```

激活成功后，命令行前面会出现类似：

```bash
(fastp-py3.14) ➜  fast git:(main) ✗
```

表示后续所有操作都会在本项目的虚拟环境中进行。

> **退出虚拟环境**：在终端输入 `deactivate` 或直接关闭终端标签页。

---

### 二、安装依赖（首次运行或依赖变动时）

如果是第一次在本机运行项目，或者 `pyproject.toml` 有更新，建议先安装依赖：

```bash
cd "/Users/edenhuang/Desktop/脚本/frontend-to-ai-agent/demo/fast"
source .venv/bin/activate

# 安装当前项目依赖
pip install -e .
# 或者使用 poetry（如果你更习惯它）
# poetry install
```

> 依赖定义在 `pyproject.toml` 中，包含 `fastapi`、`uvicorn[standard]` 等。

---

### 三、启动 FastAPI 服务

确保已经激活虚拟环境后，在项目根目录执行：

```bash
cd "/Users/edenhuang/Desktop/脚本/frontend-to-ai-agent/demo/fast"
source .venv/bin/activate

uvicorn src.fastp.main:app --reload --host 0.0.0.0 --port 8000
```

参数说明：
- **`src.fastp.main:app`**: 指向 `src/fastp/main.py` 中的 `app = FastAPI()` 实例
- **`--reload`**: 代码变更自动重启（开发环境强烈推荐开启）
- **`--host 0.0.0.0`**: 监听所有网卡（本机访问用 `localhost` 即可）
- **`--port 8000`**: 端口号，默认访问地址为 `http://localhost:8000`

看到类似输出即为启动成功：

```text
INFO:     Uvicorn running on http://0.0.0.0:8000 (Press CTRL+C to quit)
INFO:     Started reloader process [xxxx] using StatReload
```

> 如需停止服务：在运行服务器的终端按 `Ctrl + C`。

---

### 四、验证服务是否正常

服务器启动后，可以通过浏览器或命令行进行验证。

- **根接口**：
  - 浏览器访问：`http://localhost:8000/`
  - 期望返回：`{"message": "Hello World"}`

- **调试示例接口**：
  - `http://localhost:8000/debug-example`

- **调试测试接口**（用于断点调试）：
  - `http://localhost:8000/test-debug?name=测试用户`

FastAPI 自带文档页面：
- Swagger UI: `http://localhost:8000/docs`
- ReDoc: `http://localhost:8000/redoc`

---

### 五、使用 VS Code / Cursor 调试运行（可选）

项目已内置 `.vscode/launch.json` 调试配置，可直接使用 F5 调试运行。

1. 打开项目根目录（当前文件所在目录）
2. 左侧选择“运行和调试”面板
3. 在顶部配置下拉中选择：**Python: FastAPI**
4. 按 `F5`（Mac 上为 `Fn + F5`）或点击绿色 ▶️ 按钮启动

此时会自动等价于执行：

```bash
uvicorn src.fastp.main:app --reload --host 0.0.0.0 --port 8000
```

你可以在 `src/fastp/main.py` 中设置断点（例如 `/test-debug` 端点内部）并进行单步调试。

---

### 六、常见问题

- **端口被占用（[Errno 48] Address already in use）**
  - 说明本机已有其他进程占用了 8000 端口
  - 解决方法：
    - 找到并关闭占用端口的进程，或
    - 换一个端口，例如：

      ```bash
      uvicorn src.fastp.main:app --reload --host 0.0.0.0 --port 8001
      ```

- **提示找不到 fastapi / uvicorn 模块**
  - 一般是因为没有激活虚拟环境或依赖未安装
  - 请确认：
    1. 终端前缀有 `(fastp-py3.14)`
    2. 运行过：

       ```bash
       pip install -e .
       ```

- **浏览器访问不到接口**
  - 确认终端中服务器仍在运行，没有报错退出
  - 确认访问地址为：`http://localhost:8000/`

---

如果你只想“快速度过环境阶段”，最低限度只需要记住：

```bash
cd "/Users/edenhuang/Desktop/脚本/frontend-to-ai-agent/demo/fast"
source .venv/bin/activate
uvicorn src.fastp.main:app --reload --host 0.0.0.0 --port 8000
```

激活虚拟环境 + 启动服务，这三行就能跑起来整个项目。

