# 📦 Nginx Docker HTTPS 配置工具包

> 为 Docker 部署的 Nginx 网站快速配置免费 HTTPS 证书

---

## 📁 文件说明

本目录包含以下文件：

### 1. `nginx-https.conf`
**Nginx HTTPS 配置文件**
- 支持 HTTP 自动跳转 HTTPS
- 配置了安全的 SSL/TLS 参数
- 支持 HTTP/2
- 包含移动端和PC端自适应路由
- 已优化性能和安全头部

### 2. `setup-https.sh`
**自动化配置脚本**
- 一键获取 Let's Encrypt SSL 证书
- 自动停止旧容器并启动新的 HTTPS 容器
- 自动配置证书续期定时任务
- 自动配置防火墙规则
- 包含完整的错误检查和日志输出

### 3. `HTTPS配置操作步骤.md`
**详细操作文档**
- 完整的前置准备说明
- 快速开始指南
- 手动配置详细步骤
- 故障排查方案
- 日常维护指南
- 常见问题解答

### 4. `1.md`
**Docker 运行命令参考**
- 包含常用的 Docker 运行命令示例

---

## 🚀 快速开始

### 方式一：自动化脚本（推荐）

```bash
# 1. 进入脚本目录
cd /Users/edenhuang/Desktop/脚本/nginx-scripts/

# 2. 编辑配置脚本，修改域名等信息
vim setup-https.sh
# 修改这些变量：
#   DOMAIN="yourdomain.com"
#   EMAIL="your-email@example.com"
#   HTML_PATH="/data/website/front/html"
#   NGINX_CONF="/data/website/front/nginx-https.conf"

# 3. 编辑 Nginx 配置，修改域名
vim nginx-https.conf
# 修改 server_name 为您的域名

# 4. 上传文件到服务器
scp nginx-https.conf root@your-server:/data/website/front/
scp setup-https.sh root@your-server:/data/website/front/

# 5. 在服务器上执行脚本
ssh root@your-server
cd /data/website/front/
chmod +x setup-https.sh
sudo ./setup-https.sh

# 6. 完成！访问 https://yourdomain.com
```

### 方式二：手动配置

详细步骤请查看 `HTTPS配置操作步骤.md` 中的 **手动配置步骤** 章节。

---

## ⚙️ 配置说明

### 需要修改的配置项

#### 在 `setup-https.sh` 中修改：

```bash
DOMAIN="yourdomain.com"              # 👈 您的主域名
WWW_DOMAIN="www.yourdomain.com"      # 👈 www域名
EMAIL="your-email@example.com"       # 👈 您的邮箱（用于证书通知）
CONTAINER_NAME="website-front"       # 容器名称
HTML_PATH="/data/website/front/html" # 网站文件存放路径
NGINX_CONF="/data/website/front/nginx-https.conf"  # Nginx配置文件路径
```

#### 在 `nginx-https.conf` 中修改：

```nginx
# 第3行和第13行，修改为您的域名
server_name yourdomain.com www.yourdomain.com;
```

---

## 📋 使用前提

### 必须满足的条件：

- ✅ 已有域名（如：example.com）
- ✅ 域名已解析到服务器IP（配置 A 记录）
- ✅ 服务器已安装 Docker
- ✅ 服务器开放 80 和 443 端口
- ✅ 具有服务器 root 或 sudo 权限

### 域名解析配置示例：

在您的域名服务商（阿里云/腾讯云/Cloudflare等）添加：

```
类型: A
主机记录: @
记录值: 您的服务器IP
TTL: 600

类型: A
主机记录: www
记录值: 您的服务器IP
TTL: 600
```

---

## 🔍 验证配置

### 1. 检查域名解析

```bash
ping yourdomain.com
nslookup yourdomain.com
```

### 2. 检查容器运行

```bash
docker ps | grep website-front
docker logs website-front
```

### 3. 测试 HTTPS 访问

```bash
curl -I https://yourdomain.com
```

### 4. 浏览器测试

访问：`https://yourdomain.com`
- 应该显示 🔒 锁图标
- 证书应该有效

### 5. 在线 SSL 测试

访问：`https://www.ssllabs.com/ssltest/analyze.html?d=yourdomain.com`
- 目标评分：A 或 A+

---

## 🔧 常用操作

### 查看容器状态
```bash
docker ps | grep website-front
```

### 查看日志
```bash
docker logs website-front
docker logs -f website-front  # 实时查看
```

### 重启容器
```bash
docker restart website-front
```

### 测试 Nginx 配置
```bash
docker exec website-front nginx -t
```

### 重载 Nginx 配置（不中断服务）
```bash
docker exec website-front nginx -s reload
```

### 查看证书信息
```bash
sudo certbot certificates
```

### 手动续期证书
```bash
sudo certbot renew
docker restart website-front
```

### 测试证书续期
```bash
sudo certbot renew --dry-run
```

---

## ❓ 常见问题

### 1. 证书获取失败

**原因：**
- 域名未正确解析
- 80端口被占用
- 防火墙阻止

**解决：**
```bash
# 检查域名解析
ping yourdomain.com

# 检查80端口
sudo lsof -i :80

# 停止占用80端口的服务
sudo fuser -k 80/tcp
```

### 2. 容器启动失败

**查看错误日志：**
```bash
docker logs website-front
```

**常见原因：**
- 配置文件路径错误
- 证书文件不存在
- 端口被占用

### 3. HTTPS 无法访问

**检查防火墙：**
```bash
sudo ufw allow 443/tcp
```

**检查云服务器安全组：**
- 确保开放 443 端口入站规则

### 4. 证书自动续期失败

**查看续期日志：**
```bash
sudo tail -f /var/log/certbot-renew.log
```

**手动测试续期：**
```bash
sudo certbot renew --dry-run
```

---

## 📖 详细文档

更多详细信息，请查看：
- **完整操作指南**: `HTTPS配置操作步骤.md`
- **故障排查**: 查看文档中的"常见问题"章节
- **日常维护**: 查看文档中的"维护管理"章节

---

## 📞 获取帮助

遇到问题时：

1. **查看日志**
   ```bash
   docker logs website-front
   sudo tail -f /var/log/certbot-renew.log
   ```

2. **测试配置**
   ```bash
   docker exec website-front nginx -t
   ```

3. **参考文档**
   - 查看 `HTTPS配置操作步骤.md`
   - 在线搜索错误信息

---

## 📝 文件结构

```
nginx-scripts/
├── README.md                    # 本文件
├── HTTPS配置操作步骤.md          # 详细操作文档（必读）
├── nginx-https.conf             # Nginx HTTPS 配置文件
├── setup-https.sh               # 自动化配置脚本
└── 1.md                         # Docker 命令参考
```

---

## 🎯 特性说明

### nginx-https.conf 配置特性

- ✅ **HTTP 自动跳转 HTTPS**
- ✅ **支持 HTTP/2** - 更快的页面加载
- ✅ **移动端/PC端自适应** - 根据设备类型自动路由
- ✅ **前端路由支持** - 支持 Vue/React 等 SPA 应用
- ✅ **安全头部** - HSTS, X-Frame-Options, CSP 等
- ✅ **SSL 优化** - 使用现代加密套件，禁用过时协议
- ✅ **Gzip 压缩** - 减小传输大小
- ✅ **静态资源缓存** - 优化加载速度
- ✅ **OCSP Stapling** - 提升 SSL 握手性能

### setup-https.sh 脚本特性

- ✅ **自动检查依赖** - 检测并提示安装必要工具
- ✅ **域名解析验证** - 确保 DNS 配置正确
- ✅ **端口占用检测** - 自动发现并处理端口冲突
- ✅ **证书自动获取** - Let's Encrypt 免费证书
- ✅ **容器自动启动** - 一键配置完整的 HTTPS 环境
- ✅ **自动续期配置** - 证书到期前自动续期
- ✅ **防火墙自动配置** - 自动开放必要端口
- ✅ **完整错误处理** - 详细的错误提示和建议
- ✅ **彩色输出** - 清晰的信息展示

---

## 🔒 安全建议

1. **定期更新**
   ```bash
   # 更新 Nginx 镜像
   docker pull nginx:latest
   
   # 重新创建容器
   docker stop website-front
   docker rm website-front
   # 然后使用新镜像启动
   ```

2. **备份证书**
   ```bash
   sudo tar -czf ssl-backup-$(date +%Y%m%d).tar.gz /etc/letsencrypt/
   ```

3. **监控证书有效期**
   ```bash
   # 添加到监控系统
   sudo certbot certificates
   ```

4. **定期检查 SSL 评级**
   - 访问：https://www.ssllabs.com/ssltest/
   - 目标保持 A 或 A+ 评级

---

## 📊 性能优化建议

1. **启用 CDN**
   - 使用阿里云 CDN / 腾讯云 CDN
   - 或 Cloudflare（国际用户）

2. **图片优化**
   - 使用 WebP 格式
   - 启用图片懒加载

3. **资源压缩**
   - 已在配置中启用 Gzip
   - 可考虑使用 Brotli

4. **HTTP/2 推送**
   - 配置中已启用 HTTP/2
   - 可添加 Server Push 优化关键资源

---

## 🌟 最佳实践

### 1. 使用 Docker Compose（推荐）

如果管理多个服务，建议使用 Docker Compose：

```yaml
version: '3.8'
services:
  website-front:
    image: nginx:latest
    container_name: website-front
    restart: always
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - /data/website/front/html:/usr/share/nginx/html
      - /data/website/front/nginx-https.conf:/etc/nginx/conf.d/default.conf:ro
      - /etc/letsencrypt/live/yourdomain.com/fullchain.pem:/etc/nginx/ssl/fullchain.pem:ro
      - /etc/letsencrypt/live/yourdomain.com/privkey.pem:/etc/nginx/ssl/privkey.pem:ro
```

### 2. 日志轮转

防止日志文件过大：

```bash
# 在 Docker Compose 中配置
logging:
  driver: "json-file"
  options:
    max-size: "10m"
    max-file: "3"
```

### 3. 健康检查

添加健康检查确保服务可用：

```yaml
healthcheck:
  test: ["CMD", "curl", "-f", "http://localhost"]
  interval: 30s
  timeout: 10s
  retries: 3
```

---

## 📈 升级计划

### 未来可能的增强功能：

- [ ] 支持 Docker Compose 一键部署
- [ ] 支持多域名配置
- [ ] 集成监控告警
- [ ] 支持自定义 SSL 证书
- [ ] 添加流量分析工具
- [ ] 支持灰度发布

---

## 📄 许可证

本工具包免费使用，欢迎修改和分享。

---

**✨ 快速、安全、易用的 HTTPS 配置方案！**

开始使用：`./setup-https.sh` 🚀

