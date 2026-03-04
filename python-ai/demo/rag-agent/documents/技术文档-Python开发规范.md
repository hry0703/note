# Python 开发规范

团队内部技术文档 | 最后更新：2024年1月

---

## 一、代码风格

### 1.1 遵循 PEP 8

- 使用 4 个空格缩进，禁止 Tab
- 每行不超过 88 字符（Black 默认）
- 函数、类之间空 2 行

### 1.2 命名规范

```python
# 变量、函数：小写 + 下划线
user_name = "张三"
def get_user_info():
    pass

# 类名：大驼峰
class UserService:
    pass

# 常量：全大写 + 下划线
MAX_RETRY_COUNT = 3
API_BASE_URL = "https://api.example.com"

# 私有：单下划线前缀
def _internal_helper():
    pass
```

### 1.3 类型注解

- 新代码必须添加类型注解
- 使用 `typing` 模块

```python
from typing import Optional, List, Dict

def get_user(user_id: int) -> Optional[dict]:
    ...

def process_items(items: List[str]) -> Dict[str, int]:
    ...
```

---

## 二、项目结构

### 2.1 推荐目录结构

```
project/
├── src/
│   └── app_name/
│       ├── __init__.py
│       ├── main.py          # 入口
│       ├── config.py        # 配置
│       ├── api/             # 接口层
│       ├── services/        # 业务逻辑
│       ├── models/          # 数据模型
│       └── utils/           # 工具函数
├── tests/
├── docs/
├── pyproject.toml
├── requirements.txt
└── README.md
```

### 2.2 模块划分原则

- **api**：只做参数校验、调用 service、返回结果
- **services**：业务逻辑，可调用多个 model
- **models**：数据访问，单表或简单关联
- **utils**：纯函数，无业务逻辑

---

## 三、依赖管理

### 3.1 使用 Poetry 或 pip-tools

```bash
# Poetry
poetry add fastapi uvicorn
poetry add --group dev pytest black

# 生成 lock 文件，提交到 Git
poetry lock
```

### 3.2 依赖版本

- 生产依赖：锁定主版本，如 `fastapi>=0.100,<0.110`
- 避免使用 `*` 或过于宽松的范围
- 定期更新依赖，修复安全漏洞

---

## 四、错误处理

### 4.1 异常处理

```python
# 捕获具体异常
try:
    result = fetch_data()
except requests.RequestException as e:
    logger.error(f"请求失败: {e}")
    raise ServiceError("数据获取失败") from e

# 不要裸 except
# except:  # 禁止
except Exception as e:  # 可接受，但要记录
    logger.exception("未预期错误")
    raise
```

### 4.2 自定义异常

```python
class AppError(Exception):
    """应用基础异常"""
    pass

class ValidationError(AppError):
    """参数校验错误"""
    pass

class NotFoundError(AppError):
    """资源不存在"""
    pass
```

---

## 五、日志规范

### 5.1 日志级别

- **DEBUG**：调试信息，生产环境关闭
- **INFO**：关键流程，如请求开始/结束
- **WARNING**：可恢复的异常，如重试成功
- **ERROR**：需要关注的错误
- **CRITICAL**：系统级故障

### 5.2 日志格式

```python
import logging

logging.basicConfig(
    format="%(asctime)s [%(levelname)s] %(name)s: %(message)s",
    datefmt="%Y-%m-%d %H:%M:%S",
    level=logging.INFO
)

logger = logging.getLogger(__name__)
logger.info("用户 %s 登录成功", user_id)
```

---

## 六、测试规范

### 6.1 测试覆盖率

- 核心业务逻辑覆盖率 ≥ 80%
- 使用 pytest，配合 pytest-cov

```bash
pytest tests/ -v --cov=src --cov-report=html
```

### 6.2 测试命名

```python
# 文件：test_模块名.py
# 函数：test_功能描述
def test_get_user_returns_404_when_not_found():
    ...
```

### 6.3 Mock 使用

- 外部 API、数据库用 Mock
- 优先使用 pytest fixtures

---

## 七、API 开发（FastAPI）

### 7.1 路由定义

```python
from fastapi import APIRouter, Depends

router = APIRouter(prefix="/api/v1", tags=["users"])

@router.get("/users/{user_id}")
async def get_user(user_id: int, db: Session = Depends(get_db)):
    ...
```

### 7.2 响应模型

- 使用 Pydantic 定义请求/响应模型
- 统一响应格式：`{ "code": 0, "data": {}, "message": "" }`

### 7.3 文档

- 为每个接口添加 `summary` 和 `description`
- 复杂参数用 `Body(..., example=...)` 提供示例

---

## 八、安全规范

- 敏感配置使用环境变量，不提交到 Git
- 密码使用 bcrypt 等加密存储
- SQL 使用参数化查询，防止注入
- 对外 API 需鉴权、限流

---

## 九、代码审查清单

- [ ] 符合 PEP 8 和团队规范
- [ ] 有类型注解
- [ ] 有必要的注释和文档
- [ ] 有单元测试
- [ ] 无敏感信息泄露
- [ ] 错误处理完善
