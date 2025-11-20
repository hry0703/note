# Docker 常用命令

## 容器管理

### 基本操作
```bash
# 运行容器
docker run [选项] 镜像名

# 列出运行中的容器
docker ps

# 列出所有容器（包括停止的）
docker ps -a

# 停止容器
docker stop 容器ID/名称

# 启动已停止的容器
docker start 容器ID/名称

# 重启容器
docker restart 容器ID/名称

# 删除容器
docker rm 容器ID/名称

# 强制删除运行中的容器
docker rm -f 容器ID/名称
```

### 容器交互
```bash
# 进入运行中的容器
docker exec -it 容器ID/名称 /bin/bash

# 查看容器日志
docker logs 容器ID/名称

# 实时查看日志
docker logs -f 容器ID/名称

# 查看容器详细信息
docker inspect 容器ID/名称
```

## 镜像管理

### 基本操作
```bash
# 列出本地镜像
docker images

# 搜索镜像
docker search 镜像名

# 拉取镜像
docker pull 镜像名:标签

# 删除镜像
docker rmi 镜像ID/名称

# 强制删除镜像
docker rmi -f 镜像ID/名称

# 查看镜像详细信息
docker inspect 镜像ID/名称
```

### 构建镜像
```bash
# 构建镜像
docker build -t 镜像名:标签 .

# 从Dockerfile构建
docker build -f Dockerfile -t 镜像名:标签 .

# 查看构建历史
docker history 镜像名:标签
```

## Docker Compose

### 基本操作
```bash

# 启动服务
docker-compose up

# 在包含 docker-compose.yml 的目录中执行 会自动执行、
# 在当前目录下有标准命名的文件，Compose 会自动识别：
# 默认查找：docker-compose.yml 或 docker-compose.yaml
# 自动合并：同时存在 docker-compose.override.yml 会自动与主文件合并
# 需要指定其他文件或多文件时再加参数：

# 指定单个文件
docker-compose -f ./compose.dev.yml up -d

# 合并多个文件（后面的覆盖前面的）
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d

# Compose V2 等价用法
docker compose -f compose.dev.yml up -d

# 后台启动服务 -常用
docker-compose up -d

# 停止服务
docker-compose down

# 查看服务状态
docker-compose ps

# 查看服务日志
docker-compose logs

# 实时查看日志
docker-compose logs -f

# 重新构建并启动
docker-compose up --build

# 启动特定服务
docker-compose up 服务名
```

## 网络管理

```bash
# 列出网络
docker network ls

# 创建网络
docker network create 网络名

# 删除网络
docker network rm 网络名

# 查看网络详细信息
docker network inspect 网络名
```

## 数据卷管理

```bash
# 列出数据卷
docker volume ls

# 创建数据卷
docker volume create 数据卷名

# 删除数据卷
docker volume rm 数据卷名

# 查看数据卷详细信息
docker volume inspect 数据卷名
```

## 系统管理

```bash
# 查看Docker系统信息
docker system info

# 查看磁盘使用情况
docker system df

# 清理未使用的资源
docker system prune

# 清理所有未使用的资源（包括镜像）
docker system prune -a

# 查看Docker版本
docker version
```

## 常用选项说明

### run 命令常用选项
```bash
-d          # 后台运行
-it         # 交互式终端
-p 主机端口:容器端口  # 端口映射
-v 主机路径:容器路径  # 数据卷挂载
--name 名称  # 指定容器名称
--rm        # 容器停止后自动删除
-e 环境变量=值  # 设置环境变量
```

## 实用示例

### 基础服务
```bash
# 运行Nginx容器
docker run -d -p 80:80 --name nginx nginx

# 运行MySQL容器
docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password --name mysql mysql

# 运行Redis容器
docker run -d -p 6379:6379 --name redis redis

# 运行PostgreSQL容器
docker run -d -p 5432:5432 -e POSTGRES_PASSWORD=password --name postgres postgres
```

### 容器操作
```bash
# 进入容器执行命令
docker exec -it nginx /bin/bash

# 复制文件到容器
docker cp 本地文件 容器名:容器路径

# 从容器复制文件
docker cp 容器名:容器路径 本地路径

# 查看容器资源使用情况
docker stats 容器名

# 查看容器进程
docker top 容器名
```

### 开发环境
```bash
# 运行Node.js开发环境
docker run -it -v $(pwd):/app -p 3000:3000 --name node-dev node:latest

# 运行Python开发环境
docker run -it -v $(pwd):/app -p 8000:8000 --name python-dev python:latest

# 运行Go开发环境
docker run -it -v $(pwd):/app -p 8080:8080 --name go-dev golang:latest
```

## 故障排除

### 常用调试命令
```bash
# 查看容器详细信息
docker inspect 容器名

# 查看容器日志
docker logs 容器名

# 查看容器资源使用
docker stats 容器名

# 查看Docker事件
docker events

# 查看容器文件系统变化
docker diff 容器名
```

### 清理命令
```bash
# 清理停止的容器
docker container prune

# 清理未使用的镜像
docker image prune

# 清理未使用的网络
docker network prune

# 清理未使用的数据卷
docker volume prune

# 清理所有未使用的资源
docker system prune -a
```

## 最佳实践

1. **使用标签管理镜像版本**
   ```bash
   docker build -t myapp:v1.0 .
   docker build -t myapp:latest .
   ```

2. **使用数据卷持久化数据**
   ```bash
   docker run -v /host/data:/container/data 镜像名
   ```

3. **使用环境变量配置**
   ```bash
   docker run -e NODE_ENV=production 镜像名
   ```

4. **使用Docker Compose管理多容器应用**
   ```yaml
   version: '3.8'
   services:
     web:
       build: .
       ports:
         - "3000:3000"
     db:
       image: postgres
       environment:
         POSTGRES_PASSWORD: password
   ```

5. **定期清理未使用的资源**
   ```bash
   docker system prune -a --volumes
   ```

## 常用别名

可以将以下别名添加到你的 `~/.bashrc` 或 `~/.zshrc` 文件中：

```bash
# Docker 别名
alias dps='docker ps'
alias dpsa='docker ps -a'
alias di='docker images'
alias dex='docker exec -it'
alias dlog='docker logs -f'
alias dstop='docker stop'
alias dstart='docker start'
alias drm='docker rm'
alias drmi='docker rmi'
alias dcp='docker-compose'
alias dcpup='docker-compose up -d'
alias dcpdown='docker-compose down'
```

---

*最后更新：2024年*
