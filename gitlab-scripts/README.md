# GitLab Docker 部署脚本

简单易用的 GitLab Docker 部署解决方案。

## 📁 文件说明

- `start-gitlab.sh` - GitLab 启动脚本
- `gitlab-manage.sh` - GitLab 管理脚本
- `init-config.sh` - 配置初始化向导
- `gitlab-docker-compose.yml` - GitLab 配置文件
- `gitlab-config.conf.example` - 配置模板

## 🚀 快速开始

### 1. 初始化配置（推荐）
```bash
# 进入 gitlab-scripts 文件夹
cd gitlab-scripts

# 运行配置向导
./init-config.sh
```

### 2. 启动 GitLab
```bash
# 启动 GitLab
./start-gitlab.sh
```

## 📂 数据目录配置

### 🎯 **可配置数据目录**
脚本支持灵活的数据目录配置，优先级如下：
1. **环境变量** `GITLAB_DATA_DIR`
2. **配置文件** `gitlab-config.conf` 中的 `DATA_DIR`
3. **默认路径** `/opt/gitlab-data`（推荐的服务器部署路径）

### 📍 **推荐路径**
- **服务器部署**: `/opt/gitlab-data` (推荐)
- **用户目录 (macOS)**: `/Users/$USER/gitlab-data`
- **用户目录 (Linux)**: `/home/$USER/gitlab-data`

### 📁 **目录结构**
无论使用哪个数据目录，都会创建以下结构：

```
配置的数据目录/                    # 例如：/opt/gitlab-data
└── gitlab/                        # GitLab 数据
    ├── config/                   # 配置文件
    ├── logs/                     # 日志文件
    ├── data/                     # 仓库和数据库
    ├── ssl/                      # SSL 证书
    └── backups/                  # 备份文件
```

### ⚙️ **配置方法**

#### 方法一：使用配置向导（推荐）
```bash
./init-config.sh
```

#### 方法二：手动创建配置文件
```bash
# 复制配置模板
cp gitlab-config.conf.example gitlab-config.conf

# 编辑配置文件
vim gitlab-config.conf
```

#### 方法三：使用环境变量
```bash
export GITLAB_DATA_DIR="/your/custom/path"
./start-gitlab.sh
```

## 🛠️ 管理脚本

新增了便捷的管理脚本 `gitlab-manage.sh`，可以在任何目录下管理 GitLab 服务：

```bash
# 在任何目录下都可以使用
/path/to/gitlab-scripts/gitlab-manage.sh start
/path/to/gitlab-scripts/gitlab-manage.sh logs
/path/to/gitlab-scripts/gitlab-manage.sh status

# 查看帮助
./gitlab-manage.sh help
```

## 📝 访问信息

启动完成后：
- **Web 访问**：http://localhost:18080
- **HTTPS 访问**：https://localhost:18443
- **SSH 克隆端口**：12222
- **默认用户**：root
- **获取密码**：`docker exec gitlab grep 'Password:' /etc/gitlab/initial_root_password`

## 🔧 常用命令

```bash
# 启动服务
./start-gitlab.sh

# 查看状态
./gitlab-manage.sh status

# 查看日志
./gitlab-manage.sh logs

# 停止服务
./gitlab-manage.sh stop

# 重启服务
./gitlab-manage.sh restart
```
