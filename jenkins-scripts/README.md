# Jenkins Docker 部署脚本

简单易用的 Jenkins Docker 部署解决方案。

## 📁 文件说明

- `start-jenkins.sh` - Jenkins 启动脚本
- `jenkins-manage.sh` - Jenkins 管理脚本
- `init-config.sh` - 配置初始化向导
- `jenkins-docker-compose.yml` - Jenkins 配置文件
- `jenkins-config.conf.example` - 配置模板

## 🚀 快速开始

### 1. 初始化配置（推荐）
```bash
# 进入 jenkins-scripts 文件夹
cd jenkins-scripts

# 运行配置向导
./init-config.sh
```

### 2. 启动 Jenkins
```bash
# 启动 Jenkins
./start-jenkins.sh
```

## 📂 数据目录配置

### 🎯 **可配置数据目录**
脚本支持灵活的数据目录配置，优先级如下：
1. **环境变量** `JENKINS_DATA_DIR`
2. **配置文件** `jenkins-config.conf` 中的 `DATA_DIR`
3. **默认路径** `/opt/jenkins-data`（推荐的服务器部署路径）

### 📍 **推荐路径**
- **服务器部署**: `/opt/jenkins-data` (推荐)
- **用户目录 (macOS)**: `/Users/$USER/jenkins-data`
- **用户目录 (Linux)**: `/home/$USER/jenkins-data`

### 📁 **目录结构**
无论使用哪个数据目录，都会创建以下结构：

```
配置的数据目录/                    # 例如：/opt/jenkins-data
└── jenkins/                        # Jenkins 数据
    ├── home/                      # Jenkins 主目录
    ├── docker-certs-ca/           # Docker CA 证书
    ├── docker-certs-client/       # Docker 客户端证书
    └── agent-workdir/             # 代理工作目录
```

### ⚙️ **配置方法**

#### 方法一：使用配置向导（推荐）
```bash
./init-config.sh
```

#### 方法二：手动创建配置文件
```bash
# 复制配置模板
cp jenkins-config.conf.example jenkins-config.conf

# 编辑配置文件
vim jenkins-config.conf
```

#### 方法三：使用环境变量
```bash
export JENKINS_DATA_DIR="/your/custom/path"
./start-jenkins.sh
```

## 🛠️ 管理脚本

新增了便捷的管理脚本 `jenkins-manage.sh`，可以在任何目录下管理 Jenkins 服务：

```bash
# 在任何目录下都可以使用
/path/to/jenkins-scripts/jenkins-manage.sh start
/path/to/jenkins-scripts/jenkins-manage.sh logs
/path/to/jenkins-scripts/jenkins-manage.sh password

# 查看帮助
./jenkins-manage.sh help
```

## 📝 访问信息

启动完成后：
- **Web 访问**：http://localhost:19080
- **管理员密码**：`./jenkins-manage.sh password`

## 🔧 常用命令

```bash
# 启动服务
./start-jenkins.sh

# 查看状态
./jenkins-manage.sh status

# 查看日志
./jenkins-manage.sh logs

# 获取管理员密码
./jenkins-manage.sh password

# 停止服务
./jenkins-manage.sh stop

# 重启服务
./jenkins-manage.sh restart
```

## 🔗 与 GitLab 集成

### 1. 安装插件
在 Jenkins 中安装以下插件：
- GitLab Plugin
- Git Plugin
- Pipeline Plugin

### 2. 配置 GitLab 连接
```
Jenkins → Manage Jenkins → Configure System → GitLab
```

### 3. 创建构建任务
- 选择 "Pipeline" 或 "Freestyle project"
- 配置 Git 仓库地址
- 设置构建触发器（Webhook 或轮询）

### 4. GitLab Webhook 配置
```
GitLab → Project → Settings → Webhooks
URL: http://your-jenkins:19080/project/your-job
```

## 💡 最佳实践

### 🔒 **安全配置**
- 定期更新 Jenkins 版本
- 配置用户权限和角色
- 启用 CSRF 保护
- 使用强密码

### 📊 **性能优化**
- 根据需要调整 JVM 内存设置
- 定期清理旧的构建记录
- 使用代理节点分散构建负载

### 🔄 **备份策略**
- 定期备份 Jenkins 主目录
- 备份重要的任务配置
- 使用版本控制管理 Pipeline 脚本

## 🆘 故障排除

### 常见问题

1. **端口被占用**
   - 检查端口 19080 和 19000 是否被占用
   - 修改 docker-compose.yml 中的端口映射

2. **权限问题**
   ```bash
   sudo chown -R 1000:1000 /path/to/jenkins/home
   ```

3. **内存不足**
   - 确保系统有至少 2GB 可用内存
   - 调整 JAVA_OPTS 参数

4. **无法获取初始密码**
   ```bash
   docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
   ```

## 📚 相关资源

- [Jenkins 官方文档](https://www.jenkins.io/doc/)
- [Jenkins Pipeline 语法](https://www.jenkins.io/doc/book/pipeline/syntax/)
- [GitLab Integration](https://docs.gitlab.com/ee/integration/jenkins.html)
