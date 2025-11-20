# 🔐 Docker Nginx HTTPS 配置完整指南

> 本文档详细说明如何为 Docker 部署的 Nginx 网站配置免费的 Let's Encrypt SSL 证书

---

## 📋 目录

1. [前置准备](#前置准备)
2. [快速开始（推荐）](#快速开始推荐)
3. [手动配置步骤](#手动配置步骤)
4. [验证和测试](#验证和测试)
5. [常见问题](#常见问题)
6. [维护管理](#维护管理)

---

## 前置准备

### ✅ 检查清单

- [ ] 已有域名（如：example.com）
- [ ] 域名已解析到服务器IP（A记录）
- [ ] 服务器已安装 Docker
- [ ] 服务器开放 80 和 443 端口
- [ ] 具有 sudo 权限

### 📌 域名解析配置

在您的域名服务商（阿里云/腾讯云/Cloudflare等）配置DNS：

```
类型: A
主机记录: @
记录值: 您的服务器公网IP
TTL: 600

类型: A
主机记录: www
记录值: 您的服务器公网IP
TTL: 600
```

**验证DNS解析：**
```bash
# 方法1：使用 ping
ping yourdomain.com

# 方法2：使用 nslookup
nslookup yourdomain.com

# 方法3：使用 dig
dig yourdomain.com +short
```

### 🔧 安装必要工具

**Ubuntu/Debian：**
```bash
# 更新软件包
sudo apt update

# 安装 Docker（如果未安装）
sudo apt install -y docker.io
sudo systemctl start docker
sudo systemctl enable docker

# 安装 Certbot
sudo apt install -y certbot

# 安装其他工具
sudo apt install -y curl lsof
```

**CentOS/RHEL：**
```bash
# 安装 Docker
sudo yum install -y docker
sudo systemctl start docker
sudo systemctl enable docker

# 安装 Certbot
sudo yum install -y certbot

# 安装其他工具
sudo yum install -y curl lsof
```

---

## 快速开始（推荐）

### 🚀 方式一：使用自动化脚本（最简单）

#### 步骤 1：下载并配置脚本

```bash
# 进入脚本目录
cd /data/website/front/

# 如果脚本不在当前目录，请复制过来
# cp /path/to/setup-https.sh .
# cp /path/to/nginx-https.conf .

# 给脚本添加执行权限
chmod +x setup-https.sh
```

#### 步骤 2：编辑配置变量

编辑 `setup-https.sh`，修改以下变量：

```bash
vim setup-https.sh
```

修改这些行：
```bash
DOMAIN="yourdomain.com"              # 改成您的域名
WWW_DOMAIN="www.yourdomain.com"      # 改成您的www域名
EMAIL="your-email@example.com"       # 改成您的邮箱
CONTAINER_NAME="website-front"       # 容器名称
HTML_PATH="/data/website/front/html" # 网站文件路径
NGINX_CONF="/data/website/front/nginx-https.conf"  # 配置文件路径
```

#### 步骤 3：编辑 Nginx 配置

编辑 `nginx-https.conf`，修改域名：

```bash
vim nginx-https.conf
```

找到并修改这两处（第3行和第13行）：
```nginx
server_name yourdomain.com www.yourdomain.com;  # 改成您的域名
```

#### 步骤 4：运行脚本

```bash
# 执行脚本（需要 sudo 权限）
sudo ./setup-https.sh
```

脚本会自动完成：
- ✅ 检查必要工具
- ✅ 停止旧容器
- ✅ 获取SSL证书
- ✅ 启动HTTPS容器
- ✅ 配置自动续期
- ✅ 配置防火墙

#### 步骤 5：验证

```bash
# 访问您的网站
curl -I https://yourdomain.com

# 应该看到类似输出：
# HTTP/2 200
# server: nginx
# ...
```

**🎉 完成！现在访问：https://yourdomain.com**

---

## 手动配置步骤

如果您想手动执行每一步，请按以下流程操作：

### 第一步：准备配置文件

#### 1.1 创建目录结构

```bash
# 创建必要目录
sudo mkdir -p /data/website/front/html
sudo mkdir -p /data/website/front/
```

#### 1.2 创建或复制 nginx-https.conf

将提供的 `nginx-https.conf` 文件复制到服务器：

```bash
# 上传到服务器
scp nginx-https.conf root@your-server:/data/website/front/
```

或者直接在服务器上创建：

```bash
vim /data/website/front/nginx-https.conf
# 复制 nginx-https.conf 的内容并粘贴
```

**⚠️ 重要：修改配置文件中的域名**

```bash
# 编辑配置文件
vim /data/website/front/nginx-https.conf

# 找到这两处并修改：
server_name yourdomain.com www.yourdomain.com;
```

### 第二步：停止现有容器

```bash
# 查看运行中的容器
docker ps

# 停止并删除旧容器
docker stop website-front
docker rm website-front

# 验证容器已删除
docker ps -a | grep website-front
```

### 第三步：获取 SSL 证书

```bash
# 使用 Certbot 获取证书
sudo certbot certonly --standalone \
  -d yourdomain.com \
  -d www.yourdomain.com \
  --email your-email@example.com \
  --agree-tos \
  --non-interactive

# 等待几秒钟...
# 看到 "Successfully received certificate" 表示成功
```

**证书文件位置：**
```
/etc/letsencrypt/live/yourdomain.com/
  ├── fullchain.pem   (完整证书链)
  ├── privkey.pem     (私钥)
  ├── cert.pem        (服务器证书)
  └── chain.pem       (中间证书)
```

**验证证书：**
```bash
# 查看证书信息
sudo certbot certificates

# 查看证书文件
ls -la /etc/letsencrypt/live/yourdomain.com/
```

### 第四步：启动 HTTPS 容器

```bash
# 运行支持 HTTPS 的容器
docker run -d \
  --name website-front \
  -p 80:80 \
  -p 443:443 \
  -v /data/website/front/html:/usr/share/nginx/html \
  -v /data/website/front/nginx-https.conf:/etc/nginx/conf.d/default.conf:ro \
  -v /etc/letsencrypt/live/yourdomain.com/fullchain.pem:/etc/nginx/ssl/fullchain.pem:ro \
  -v /etc/letsencrypt/live/yourdomain.com/privkey.pem:/etc/nginx/ssl/privkey.pem:ro \
  --restart always \
  nginx:latest
```

**参数说明：**
- `-p 80:80` - HTTP端口（用于重定向到HTTPS）
- `-p 443:443` - HTTPS端口
- `--restart always` - 容器自动重启
- `:ro` - 只读挂载（安全性更好）

### 第五步：验证容器运行

```bash
# 1. 查看容器状态
docker ps | grep website-front

# 2. 查看容器日志
docker logs website-front

# 3. 测试 Nginx 配置
docker exec website-front nginx -t

# 应该看到：
# nginx: the configuration file /etc/nginx/nginx.conf syntax is ok
# nginx: configuration file /etc/nginx/nginx.conf test is successful
```

### 第六步：配置防火墙

```bash
# Ubuntu/Debian (UFW)
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw status

# CentOS/RHEL (Firewalld)
sudo firewall-cmd --permanent --add-service=http
sudo firewall-cmd --permanent --add-service=https
sudo firewall-cmd --reload

# 直接使用 iptables
sudo iptables -A INPUT -p tcp --dport 80 -j ACCEPT
sudo iptables -A INPUT -p tcp --dport 443 -j ACCEPT
```

**云服务器安全组：**
如果使用阿里云/腾讯云/AWS等，还需要在控制台配置安全组：

```
规则方向: 入站
协议端口: TCP:80
授权对象: 0.0.0.0/0

规则方向: 入站
协议端口: TCP:443
授权对象: 0.0.0.0/0
```

### 第七步：配置自动续期

Let's Encrypt 证书有效期为 90 天，需要自动续期。

```bash
# 测试续期命令（不会真正续期）
sudo certbot renew --dry-run

# 如果测试成功，添加定时任务
sudo crontab -e

# 添加以下行（每天凌晨2点检查并续期）
0 2 * * * certbot renew --quiet --post-hook "docker restart website-front" >> /var/log/certbot-renew.log 2>&1
```

**验证定时任务：**
```bash
# 查看当前的定时任务
sudo crontab -l

# 查看续期日志
sudo tail -f /var/log/certbot-renew.log
```

---

## 验证和测试

### 🧪 基础测试

```bash
# 1. 测试 HTTP 重定向
curl -I http://yourdomain.com
# 应该返回 301 或 302 重定向到 HTTPS

# 2. 测试 HTTPS 访问
curl -I https://yourdomain.com
# 应该返回 200 OK

# 3. 测试证书
curl -v https://yourdomain.com 2>&1 | grep "SSL certificate verify"
# 应该看到 "SSL certificate verify ok"

# 4. 查看证书详情
openssl s_client -connect yourdomain.com:443 -servername yourdomain.com < /dev/null 2>/dev/null | openssl x509 -noout -text
```

### 🌐 浏览器测试

1. **访问 HTTP 地址**
   ```
   http://yourdomain.com
   ```
   应该自动跳转到：
   ```
   https://yourdomain.com
   ```

2. **检查浏览器地址栏**
   - ✅ 应该显示 🔒 锁图标
   - ✅ 点击锁图标，查看证书详情
   - ✅ 证书应该由 "Let's Encrypt" 签发

3. **测试不同路径**
   ```
   https://yourdomain.com/
   https://yourdomain.com/pc/
   https://yourdomain.com/mobile/
   ```

### 🔍 在线 SSL 测试工具

**SSL Labs 测试（推荐）：**
```
https://www.ssllabs.com/ssltest/analyze.html?d=yourdomain.com
```
目标评分：A 或 A+

**其他测试工具：**
- SSL Checker: https://www.sslshopper.com/ssl-checker.html
- SSL Test: https://www.ssllabs.com/ssltest/

### 📊 性能测试

```bash
# 使用 curl 测试响应时间
curl -w "\n\nTime Connect: %{time_connect}s\nTime Start Transfer: %{time_starttransfer}s\nTime Total: %{time_total}s\n" -o /dev/null -s https://yourdomain.com

# 使用 ab 进行压力测试
ab -n 1000 -c 10 https://yourdomain.com/
```

---

## 常见问题

### ❓ 问题 1：证书获取失败

**症状：**
```
Certbot failed to authenticate some domains
```

**解决方案：**

1. **检查域名解析**
   ```bash
   ping yourdomain.com
   # 确保解析到正确的服务器IP
   ```

2. **检查80端口是否被占用**
   ```bash
   sudo lsof -i :80
   # 或
   sudo netstat -tulnp | grep :80
   ```

3. **停止占用80端口的服务**
   ```bash
   # 停止旧的 Nginx/Apache
   sudo systemctl stop nginx
   sudo systemctl stop apache2
   
   # 或强制杀死进程
   sudo fuser -k 80/tcp
   ```

4. **检查防火墙**
   ```bash
   # UFW
   sudo ufw status
   sudo ufw allow 80/tcp
   
   # Firewalld
   sudo firewall-cmd --list-all
   ```

5. **使用 DNS 验证模式（备选方案）**
   ```bash
   sudo certbot certonly --manual --preferred-challenges dns \
     -d yourdomain.com -d www.yourdomain.com
   ```

### ❓ 问题 2：容器启动失败

**症状：**
```bash
docker ps | grep website-front
# 没有输出，容器不在运行
```

**解决方案：**

1. **查看容器日志**
   ```bash
   docker logs website-front
   ```

2. **常见错误及解决：**

   **错误：nginx: [emerg] cannot load certificate**
   ```bash
   # 检查证书文件是否存在
   ls -la /etc/letsencrypt/live/yourdomain.com/
   
   # 检查文件权限
   sudo chmod 644 /etc/letsencrypt/live/yourdomain.com/*.pem
   ```

   **错误：bind() to 0.0.0.0:443 failed (98: Address already in use)**
   ```bash
   # 查看占用443端口的进程
   sudo lsof -i :443
   
   # 停止其他容器
   docker ps
   docker stop <container_name>
   ```

   **错误：nginx: [emerg] host not found in upstream**
   ```bash
   # 检查 nginx-https.conf 配置
   vim /data/website/front/nginx-https.conf
   
   # 确保没有配置错误的 upstream 或 proxy_pass
   ```

3. **测试 Nginx 配置**
   ```bash
   # 临时启动容器测试配置
   docker run --rm -v /data/website/front/nginx-https.conf:/etc/nginx/conf.d/default.conf nginx nginx -t
   ```

### ❓ 问题 3：浏览器显示证书不安全

**症状：**
- 浏览器显示 "您的连接不是私密连接"
- ERR_CERT_COMMON_NAME_INVALID

**解决方案：**

1. **检查域名是否匹配**
   ```bash
   # 查看证书中的域名
   openssl x509 -in /etc/letsencrypt/live/yourdomain.com/cert.pem -noout -text | grep "DNS:"
   
   # 确保访问的域名在证书中
   ```

2. **检查证书是否过期**
   ```bash
   # 查看证书有效期
   openssl x509 -in /etc/letsencrypt/live/yourdomain.com/cert.pem -noout -dates
   ```

3. **强制刷新浏览器缓存**
   - Chrome: Ctrl + Shift + Delete，清除缓存
   - 或使用隐身模式测试

4. **检查证书链是否完整**
   ```bash
   # 应该使用 fullchain.pem 而不是 cert.pem
   # 检查 nginx-https.conf：
   grep ssl_certificate /data/website/front/nginx-https.conf
   # 应该是：ssl_certificate /etc/nginx/ssl/fullchain.pem;
   ```

### ❓ 问题 4：HTTP 没有自动跳转到 HTTPS

**解决方案：**

1. **检查 Nginx 配置**
   ```bash
   # 确保有 HTTP 到 HTTPS 的重定向配置
   docker exec website-front cat /etc/nginx/conf.d/default.conf | grep "return 301"
   ```

2. **手动添加重定向（如果缺失）**
   ```nginx
   server {
       listen 80;
       server_name yourdomain.com www.yourdomain.com;
       return 301 https://$server_name$request_uri;
   }
   ```

3. **重启容器**
   ```bash
   docker restart website-front
   ```

### ❓ 问题 5：自动续期失败

**症状：**
```bash
# 查看续期日志
sudo tail -f /var/log/certbot-renew.log
# 看到续期失败的错误
```

**解决方案：**

1. **手动测试续期**
   ```bash
   sudo certbot renew --dry-run
   ```

2. **检查容器是否运行**
   ```bash
   docker ps | grep website-front
   ```

3. **修改续期钩子**
   ```bash
   sudo crontab -e
   
   # 修改为（先停止容器，续期后再启动）：
   0 2 * * * docker stop website-front && certbot renew --quiet && docker start website-front >> /var/log/certbot-renew.log 2>&1
   ```

4. **使用 webroot 模式（推荐）**
   ```bash
   # 重新获取证书，使用 webroot 模式
   sudo certbot certonly --webroot \
     -w /data/website/front/html \
     -d yourdomain.com \
     -d www.yourdomain.com
   ```

### ❓ 问题 6：网站内容显示404

**解决方案：**

1. **检查文件是否存在**
   ```bash
   ls -la /data/website/front/html/
   # 应该包含 index.html 或 pc/index.html, mobile/index.html
   ```

2. **检查文件权限**
   ```bash
   sudo chmod -R 755 /data/website/front/html/
   sudo chown -R $USER:$USER /data/website/front/html/
   ```

3. **进入容器检查**
   ```bash
   docker exec -it website-front bash
   ls -la /usr/share/nginx/html/
   exit
   ```

### ❓ 问题 7：中国大陆访问慢或无法访问

**原因：**
- Let's Encrypt 在中国大陆访问较慢
- 需要ICP备案

**解决方案：**

1. **使用CDN加速**
   - 阿里云CDN
   - 腾讯云CDN
   - Cloudflare（国际）

2. **完成ICP备案**
   - 在云服务商控制台提交备案申请
   - 备案期间使用IP+端口访问

3. **使用国内证书（可选）**
   - 阿里云免费SSL证书
   - 腾讯云免费SSL证书

---

## 维护管理

### 📅 日常维护

#### 1. 查看容器状态

```bash
# 查看运行状态
docker ps | grep website-front

# 查看容器资源使用
docker stats website-front

# 查看容器详细信息
docker inspect website-front
```

#### 2. 查看日志

```bash
# 实时查看日志
docker logs -f website-front

# 查看最近100行日志
docker logs --tail 100 website-front

# 查看特定时间的日志
docker logs --since "2024-01-01T00:00:00" website-front

# 进入容器查看 Nginx 日志
docker exec website-front tail -f /var/log/nginx/access.log
docker exec website-front tail -f /var/log/nginx/error.log
```

#### 3. 更新网站内容

```bash
# 方式1：直接上传文件到宿主机
scp -r ./dist/* root@your-server:/data/website/front/html/

# 方式2：在服务器上直接编辑
vim /data/website/front/html/index.html

# 无需重启容器，刷新浏览器即可看到更新
```

#### 4. 更新配置文件

```bash
# 修改配置
vim /data/website/front/nginx-https.conf

# 测试配置
docker exec website-front nginx -t

# 重载配置（不中断服务）
docker exec website-front nginx -s reload

# 或者重启容器
docker restart website-front
```

### 🔄 证书管理

#### 查看证书信息

```bash
# 查看所有证书
sudo certbot certificates

# 查看证书有效期
openssl x509 -in /etc/letsencrypt/live/yourdomain.com/cert.pem -noout -dates

# 输出示例：
# notBefore=Jan  1 00:00:00 2024 GMT
# notAfter=Apr  1 00:00:00 2024 GMT
```

#### 手动续期证书

```bash
# 续期所有证书
sudo certbot renew

# 续期特定证书
sudo certbot renew --cert-name yourdomain.com

# 测试续期（不会真正续期）
sudo certbot renew --dry-run

# 强制续期（即使未到期）
sudo certbot renew --force-renewal

# 续期后重启容器
docker restart website-front
```

#### 吊销证书

```bash
# 吊销证书
sudo certbot revoke --cert-path /etc/letsencrypt/live/yourdomain.com/cert.pem

# 吊销并删除
sudo certbot revoke --cert-path /etc/letsencrypt/live/yourdomain.com/cert.pem --delete-after-revoke
```

#### 添加新域名到现有证书

```bash
# 扩展证书（添加新域名）
sudo certbot certonly --standalone \
  --expand \
  -d yourdomain.com \
  -d www.yourdomain.com \
  -d api.yourdomain.com \
  -d admin.yourdomain.com

# 更新 Nginx 配置
vim /data/website/front/nginx-https.conf
# 添加新域名到 server_name

# 重启容器
docker restart website-front
```

### 🔧 容器管理

#### 重启容器

```bash
# 平滑重启
docker restart website-front

# 强制停止并启动
docker stop website-front
docker start website-front
```

#### 更新容器

```bash
# 拉取最新镜像
docker pull nginx:latest

# 停止并删除旧容器
docker stop website-front
docker rm website-front

# 使用新镜像启动容器（使用之前的运行命令）
docker run -d \
  --name website-front \
  -p 80:80 \
  -p 443:443 \
  -v /data/website/front/html:/usr/share/nginx/html \
  -v /data/website/front/nginx-https.conf:/etc/nginx/conf.d/default.conf:ro \
  -v /etc/letsencrypt/live/yourdomain.com/fullchain.pem:/etc/nginx/ssl/fullchain.pem:ro \
  -v /etc/letsencrypt/live/yourdomain.com/privkey.pem:/etc/nginx/ssl/privkey.pem:ro \
  --restart always \
  nginx:latest
```

#### 备份容器配置

```bash
# 备份整个网站目录
tar -czf website-backup-$(date +%Y%m%d).tar.gz /data/website/

# 只备份配置和证书
tar -czf config-backup-$(date +%Y%m%d).tar.gz \
  /data/website/front/nginx-https.conf \
  /etc/letsencrypt/

# 恢复备份
tar -xzf website-backup-20240101.tar.gz -C /
```

### 📊 监控和优化

#### 性能监控

```bash
# 实时监控资源使用
docker stats website-front

# 查看网络连接
docker exec website-front netstat -an | grep :443

# 查看进程
docker exec website-front ps aux
```

#### 日志轮转

创建日志轮转配置：

```bash
sudo vim /etc/logrotate.d/docker-nginx

# 添加以下内容：
/var/lib/docker/containers/*/*.log {
    rotate 7
    daily
    compress
    missingok
    delaycompress
    copytruncate
}
```

#### 访问日志分析

```bash
# 统计访问最多的IP
docker exec website-front awk '{print $1}' /var/log/nginx/access.log | sort | uniq -c | sort -rn | head -10

# 统计访问最多的页面
docker exec website-front awk '{print $7}' /var/log/nginx/access.log | sort | uniq -c | sort -rn | head -10

# 统计HTTP状态码
docker exec website-front awk '{print $9}' /var/log/nginx/access.log | sort | uniq -c | sort -rn
```

### 🗑️ 清理和卸载

#### 删除容器和数据

```bash
# 停止并删除容器
docker stop website-front
docker rm website-front

# 删除镜像（可选）
docker rmi nginx:latest

# 删除网站文件（谨慎！）
sudo rm -rf /data/website/
```

#### 删除证书

```bash
# 删除特定证书
sudo certbot delete --cert-name yourdomain.com

# 删除所有 Certbot 数据
sudo rm -rf /etc/letsencrypt/
sudo rm -rf /var/log/letsencrypt/
```

#### 删除定时任务

```bash
# 编辑 crontab
sudo crontab -e

# 删除包含 certbot 的行

# 查看确认
sudo crontab -l
```

---

## 📚 附录

### 常用命令速查

```bash
# ========== 容器管理 ==========
docker ps                          # 查看运行中的容器
docker ps -a                       # 查看所有容器
docker logs website-front          # 查看日志
docker restart website-front       # 重启容器
docker stop website-front          # 停止容器
docker start website-front         # 启动容器
docker rm website-front            # 删除容器

# ========== Nginx 操作 ==========
docker exec website-front nginx -t               # 测试配置
docker exec website-front nginx -s reload        # 重载配置
docker exec website-front nginx -V               # 查看版本和编译参数
docker exec -it website-front bash               # 进入容器

# ========== 证书管理 ==========
sudo certbot certificates                        # 查看所有证书
sudo certbot renew                               # 续期证书
sudo certbot renew --dry-run                     # 测试续期
sudo certbot delete --cert-name yourdomain.com   # 删除证书

# ========== 系统检查 ==========
sudo lsof -i :80                   # 查看80端口占用
sudo lsof -i :443                  # 查看443端口占用
curl -I https://yourdomain.com     # 测试HTTPS
ping yourdomain.com                # 测试域名解析
```

### 配置文件模板

**最小化 nginx-https.conf：**
```nginx
server {
    listen 80;
    server_name yourdomain.com;
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name yourdomain.com;
    
    ssl_certificate /etc/nginx/ssl/fullchain.pem;
    ssl_certificate_key /etc/nginx/ssl/privkey.pem;
    
    root /usr/share/nginx/html;
    index index.html;
    
    location / {
        try_files $uri $uri/ /index.html;
    }
}
```

### 相关文档链接

- **Let's Encrypt 官网**: https://letsencrypt.org/
- **Certbot 文档**: https://certbot.eff.org/
- **Nginx 官方文档**: https://nginx.org/en/docs/
- **Docker 官方文档**: https://docs.docker.com/
- **SSL Labs 测试**: https://www.ssllabs.com/ssltest/

---

## 📞 技术支持

### 获取帮助

如果遇到问题，请按以下步骤排查：

1. **查看日志**
   ```bash
   docker logs website-front
   sudo tail -f /var/log/certbot-renew.log
   ```

2. **测试配置**
   ```bash
   docker exec website-front nginx -t
   sudo certbot renew --dry-run
   ```

3. **检查系统状态**
   ```bash
   docker ps
   sudo systemctl status docker
   sudo ufw status
   ```

4. **在线搜索错误信息**
   - 复制错误信息到 Google
   - 查看 Stack Overflow
   - 查看 GitHub Issues

---

## 📝 更新日志

- **2024-01-01**: 初始版本
- 添加了自动化脚本
- 完善了故障排查章节
- 增加了维护管理指南

---

**✅ 恭喜！您已成功配置 HTTPS！**

现在您的网站：
- 🔒 使用加密连接
- 🌍 被浏览器信任
- 📈 SEO 更友好
- ⚡ 支持 HTTP/2
- 🔄 证书自动续期

**祝您使用愉快！** 🎉

