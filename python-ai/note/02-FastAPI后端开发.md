# 02 - FastAPI 后端开发

> **前置条件**：完成 Python 基础学习  
> **学习时长**：2-3 周  
> **学习目标**：能够使用 FastAPI 构建现代化的 RESTful API

---

## 🎯 为什么选择 FastAPI？

### 对比其他框架

| 特性 | FastAPI | Flask | Django | Express.js |
|------|---------|-------|--------|------------|
| 性能 | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐ |
| 异步支持 | ⭐⭐⭐⭐⭐ | ⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| 类型提示 | ⭐⭐⭐⭐⭐ | ⭐ | ⭐⭐ | ⭐⭐⭐⭐ |
| 自动文档 | ⭐⭐⭐⭐⭐ | ⭐ | ⭐⭐⭐ | ⭐⭐ |
| 学习曲线 | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐ | ⭐⭐⭐⭐ |

**FastAPI 的优势**：
- 🚀 极快的性能（媲美 Node.js 和 Go）
- 💡 基于类型提示，自动生成文档（Swagger UI）
- 🔄 原生异步支持（async/await）
- ✅ 自动请求验证（基于 Pydantic）
- 🎨 现代化设计，对前端友好

---

## 📚 学习内容

### 1. FastAPI 快速开始

#### 1.1 安装依赖

```bash
# 创建虚拟环境
python -m venv venv
source venv/bin/activate  # Mac/Linux
# venv\Scripts\activate  # Windows

# 安装 FastAPI 和 Uvicorn（ASGI 服务器）
pip install fastapi uvicorn[standard]
pip install python-multipart  # 处理表单和文件上传
```

---

#### 1.2 第一个 API（Hello World）

```python
# main.py
from fastapi import FastAPI

app = FastAPI()

@app.get("/")
def read_root():
    return {"message": "Hello, World!"}

@app.get("/items/{item_id}")
def read_item(item_id: int, q: str = None):
    return {"item_id": item_id, "q": q}
```

**运行服务器**：
```bash
uvicorn main:app --reload
# 访问 http://localhost:8000
# 自动文档：http://localhost:8000/docs
```

**对比 Express.js**：
```javascript
const express = require('express');
const app = express();

app.get('/', (req, res) => {
    res.json({ message: 'Hello, World!' });
});

app.get('/items/:item_id', (req, res) => {
    res.json({ item_id: req.params.item_id, q: req.query.q });
});

app.listen(8000);
```

---

### 2. 请求与响应

#### 2.1 路径参数与查询参数

```python
from typing import Optional
from fastapi import FastAPI

app = FastAPI()

# 路径参数
@app.get("/users/{user_id}")
def get_user(user_id: int):  # 自动类型转换和验证
    return {"user_id": user_id}

# 查询参数
@app.get("/search")
def search(q: str, limit: int = 10, offset: int = 0):
    return {"q": q, "limit": limit, "offset": offset}

# 可选参数
@app.get("/items/{item_id}")
def get_item(item_id: int, details: Optional[bool] = None):
    if details:
        return {"item_id": item_id, "name": "Item", "price": 100}
    return {"item_id": item_id}
```

---

#### 2.2 请求体（Pydantic 模型）

```python
from pydantic import BaseModel, Field
from typing import Optional

# 定义数据模型
class User(BaseModel):
    name: str = Field(..., min_length=1, max_length=50)
    email: str
    age: Optional[int] = Field(None, ge=0, le=150)
    is_active: bool = True

# POST 请求
@app.post("/users")
def create_user(user: User):
    # 自动验证和解析请求体
    return {"message": "User created", "user": user}

# PUT 请求
@app.put("/users/{user_id}")
def update_user(user_id: int, user: User):
    return {"user_id": user_id, "user": user}
```

**请求示例**（前端）：
```javascript
// JavaScript fetch
const response = await fetch('http://localhost:8000/users', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
        name: 'Alice',
        email: 'alice@example.com',
        age: 25
    })
});
const data = await response.json();
```

---

#### 2.3 响应模型

```python
from pydantic import BaseModel
from typing import List

class UserResponse(BaseModel):
    id: int
    name: str
    email: str
    # 不包含敏感信息（如密码）

class UsersListResponse(BaseModel):
    total: int
    users: List[UserResponse]

@app.get("/users", response_model=List[UserResponse])
def list_users():
    # 自动过滤和序列化响应
    return [
        {"id": 1, "name": "Alice", "email": "alice@example.com", "password": "secret"},
        {"id": 2, "name": "Bob", "email": "bob@example.com", "password": "secret123"}
    ]
```

---

### 3. 数据库操作（SQLAlchemy）

#### 3.1 安装依赖

```bash
pip install sqlalchemy databases asyncpg
```

---

#### 3.2 数据库连接配置

```python
# database.py
from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker

DATABASE_URL = "sqlite:///./test.db"  # 使用 SQLite
# DATABASE_URL = "postgresql://user:password@localhost/dbname"  # PostgreSQL

engine = create_engine(DATABASE_URL, connect_args={"check_same_thread": False})
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
Base = declarative_base()

# 依赖注入：获取数据库会话
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()
```

---

#### 3.3 定义数据库模型

```python
# models.py
from sqlalchemy import Column, Integer, String, Boolean
from database import Base

class User(Base):
    __tablename__ = "users"
    
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, index=True)
    email = Column(String, unique=True, index=True)
    hashed_password = Column(String)
    is_active = Column(Boolean, default=True)
```

---

#### 3.4 CRUD 操作

```python
# crud.py
from sqlalchemy.orm import Session
import models, schemas

def get_user(db: Session, user_id: int):
    return db.query(models.User).filter(models.User.id == user_id).first()

def get_users(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.User).offset(skip).limit(limit).all()

def create_user(db: Session, user: schemas.UserCreate):
    fake_hashed_password = user.password + "_hashed"
    db_user = models.User(
        name=user.name,
        email=user.email,
        hashed_password=fake_hashed_password
    )
    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    return db_user

def update_user(db: Session, user_id: int, user: schemas.UserUpdate):
    db_user = get_user(db, user_id)
    if db_user:
        for key, value in user.dict(exclude_unset=True).items():
            setattr(db_user, key, value)
        db.commit()
        db.refresh(db_user)
    return db_user

def delete_user(db: Session, user_id: int):
    db_user = get_user(db, user_id)
    if db_user:
        db.delete(db_user)
        db.commit()
    return db_user
```

---

#### 3.5 API 路由

```python
# main.py
from fastapi import FastAPI, Depends, HTTPException
from sqlalchemy.orm import Session
import crud, models, schemas
from database import engine, get_db

models.Base.metadata.create_all(bind=engine)
app = FastAPI()

@app.post("/users", response_model=schemas.User)
def create_user(user: schemas.UserCreate, db: Session = Depends(get_db)):
    db_user = crud.get_user_by_email(db, email=user.email)
    if db_user:
        raise HTTPException(status_code=400, detail="Email already registered")
    return crud.create_user(db=db, user=user)

@app.get("/users", response_model=list[schemas.User])
def read_users(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    users = crud.get_users(db, skip=skip, limit=limit)
    return users

@app.get("/users/{user_id}", response_model=schemas.User)
def read_user(user_id: int, db: Session = Depends(get_db)):
    db_user = crud.get_user(db, user_id=user_id)
    if db_user is None:
        raise HTTPException(status_code=404, detail="User not found")
    return db_user
```

---

### 4. 身份认证与授权（JWT）

#### 4.1 安装依赖

```bash
pip install python-jose[cryptography] passlib[bcrypt] python-multipart
```

---

#### 4.2 密码加密

```python
# auth.py
from passlib.context import CryptContext
from jose import JWTError, jwt
from datetime import datetime, timedelta

pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")

SECRET_KEY = "your-secret-key-keep-it-secret"
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES = 30

def verify_password(plain_password, hashed_password):
    return pwd_context.verify(plain_password, hashed_password)

def get_password_hash(password):
    return pwd_context.hash(password)

def create_access_token(data: dict, expires_delta: timedelta = None):
    to_encode = data.copy()
    if expires_delta:
        expire = datetime.utcnow() + expires_delta
    else:
        expire = datetime.utcnow() + timedelta(minutes=15)
    to_encode.update({"exp": expire})
    encoded_jwt = jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)
    return encoded_jwt
```

---

#### 4.3 登录端点

```python
from fastapi import Depends, HTTPException, status
from fastapi.security import OAuth2PasswordBearer, OAuth2PasswordRequestForm

oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")

@app.post("/token")
def login(form_data: OAuth2PasswordRequestForm = Depends(), db: Session = Depends(get_db)):
    user = authenticate_user(db, form_data.username, form_data.password)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect username or password",
            headers={"WWW-Authenticate": "Bearer"},
        )
    access_token_expires = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = create_access_token(
        data={"sub": user.email}, expires_delta=access_token_expires
    )
    return {"access_token": access_token, "token_type": "bearer"}

# 受保护的路由
@app.get("/users/me")
def read_users_me(token: str = Depends(oauth2_scheme), db: Session = Depends(get_db)):
    user = get_current_user(token, db)
    return user
```

---

### 5. 中间件与 CORS

```python
from fastapi.middleware.cors import CORSMiddleware

app = FastAPI()

# 配置 CORS（前端跨域）
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:3000"],  # React/Vue 开发服务器
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 自定义中间件
from fastapi import Request
import time

@app.middleware("http")
async def add_process_time_header(request: Request, call_next):
    start_time = time.time()
    response = await call_next(request)
    process_time = time.time() - start_time
    response.headers["X-Process-Time"] = str(process_time)
    return response
```

---

### 6. 异步操作

```python
import asyncio
import httpx
from fastapi import FastAPI

app = FastAPI()

# 异步路由
@app.get("/async-data")
async def get_async_data():
    # 模拟异步操作
    await asyncio.sleep(1)
    return {"message": "Async data"}

# 异步 HTTP 请求
@app.get("/fetch-external")
async def fetch_external():
    async with httpx.AsyncClient() as client:
        response = await client.get("https://api.github.com")
        return response.json()

# 并发请求
@app.get("/fetch-multiple")
async def fetch_multiple():
    async with httpx.AsyncClient() as client:
        tasks = [
            client.get("https://api.github.com"),
            client.get("https://api.example.com"),
        ]
        responses = await asyncio.gather(*tasks)
        return [r.json() for r in responses]
```

---

### 7. WebSocket 实时通信

```python
from fastapi import WebSocket
from typing import List

class ConnectionManager:
    def __init__(self):
        self.active_connections: List[WebSocket] = []
    
    async def connect(self, websocket: WebSocket):
        await websocket.accept()
        self.active_connections.append(websocket)
    
    def disconnect(self, websocket: WebSocket):
        self.active_connections.remove(websocket)
    
    async def broadcast(self, message: str):
        for connection in self.active_connections:
            await connection.send_text(message)

manager = ConnectionManager()

@app.websocket("/ws/{client_id}")
async def websocket_endpoint(websocket: WebSocket, client_id: int):
    await manager.connect(websocket)
    try:
        while True:
            data = await websocket.receive_text()
            await manager.broadcast(f"Client {client_id}: {data}")
    except Exception:
        manager.disconnect(websocket)
```

**前端连接**：
```javascript
const ws = new WebSocket('ws://localhost:8000/ws/1');

ws.onmessage = (event) => {
    console.log('Message:', event.data);
};

ws.send('Hello, Server!');
```

---

## 🎯 实战项目

### 项目 1：Todo API
```python
# 完整的 Todo CRUD API
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List, Optional

app = FastAPI()

class Todo(BaseModel):
    id: Optional[int] = None
    title: str
    description: Optional[str] = None
    completed: bool = False

todos_db = []
next_id = 1

@app.post("/todos", response_model=Todo)
def create_todo(todo: Todo):
    global next_id
    todo.id = next_id
    next_id += 1
    todos_db.append(todo)
    return todo

@app.get("/todos", response_model=List[Todo])
def list_todos():
    return todos_db

@app.get("/todos/{todo_id}", response_model=Todo)
def get_todo(todo_id: int):
    for todo in todos_db:
        if todo.id == todo_id:
            return todo
    raise HTTPException(status_code=404, detail="Todo not found")

@app.put("/todos/{todo_id}", response_model=Todo)
def update_todo(todo_id: int, updated_todo: Todo):
    for i, todo in enumerate(todos_db):
        if todo.id == todo_id:
            updated_todo.id = todo_id
            todos_db[i] = updated_todo
            return updated_todo
    raise HTTPException(status_code=404, detail="Todo not found")

@app.delete("/todos/{todo_id}")
def delete_todo(todo_id: int):
    for i, todo in enumerate(todos_db):
        if todo.id == todo_id:
            del todos_db[i]
            return {"message": "Todo deleted"}
    raise HTTPException(status_code=404, detail="Todo not found")
```

---

## 📖 推荐资源

### 官方文档
- **FastAPI 官方文档**：https://fastapi.tiangolo.com/
- **Pydantic 文档**：https://docs.pydantic.dev/
- **SQLAlchemy 文档**：https://docs.sqlalchemy.org/

### 视频教程
- **Traversy Media**：FastAPI Crash Course（YouTube）
- **Tech With Tim**：FastAPI Tutorial Series

### 实战项目
- GitHub 上搜索 "fastapi-example"
- 构建个人博客 API
- 构建实时聊天后端

---

## ✅ 学习检查清单

- [ ] 能够创建基础的 FastAPI 应用
- [ ] 理解路径参数、查询参数、请求体
- [ ] 掌握 Pydantic 数据验证
- [ ] 能够使用 SQLAlchemy 操作数据库
- [ ] 实现 JWT 身份认证
- [ ] 配置 CORS 跨域
- [ ] 编写异步路由
- [ ] 实现 WebSocket 实时通信
- [ ] 完成至少 1 个完整的 CRUD API 项目

---

**下一步**：学习 [03-LLM基础与应用](./03-LLM基础与应用.md)
