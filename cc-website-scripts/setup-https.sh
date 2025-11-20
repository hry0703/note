#!/bin/bash

# ========================================
# Docker Nginx HTTPS 自动配置脚本
# ========================================

set -e  # 遇到错误立即退出

# ========================================
# 配置变量（请根据实际情况修改）
# ========================================
DOMAIN="yourdomain.com"              # 👈 修改为您的主域名
WWW_DOMAIN="www.yourdomain.com"      # 👈 修改为www域名
EMAIL="your-email@example.com"       # 👈 修改为您的邮箱
CONTAINER_NAME="website-front"       # 容器名称
HTML_PATH="/data/website/front/html" # 网站文件路径
NGINX_CONF="/data/website/front/nginx-https.conf"  # Nginx配置文件路径

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# ========================================
# 函数定义
# ========================================

# 打印信息
print_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查命令是否存在
check_command() {
    if ! command -v $1 &> /dev/null; then
        print_error "$1 未安装"
        return 1
    fi
    return 0
}

# 检查端口是否被占用
check_port() {
    if lsof -Pi :$1 -sTCP:LISTEN -t >/dev/null 2>&1 ; then
        print_warning "端口 $1 已被占用"
        lsof -i :$1
        return 1
    fi
    return 0
}

# ========================================
# 主流程
# ========================================

echo ""
echo "=========================================="
echo "  🔐 Docker Nginx HTTPS 配置工具"
echo "=========================================="
echo ""

# 1. 检查必要工具
print_info "检查必要工具..."
check_command docker || exit 1
check_command certbot || {
    print_error "Certbot 未安装，开始安装..."
    if [[ -f /etc/debian_version ]]; then
        sudo apt update && sudo apt install -y certbot
    elif [[ -f /etc/redhat-release ]]; then
        sudo yum install -y certbot
    else
        print_error "不支持的系统，请手动安装 certbot"
        exit 1
    fi
}

# 2. 检查域名解析
print_info "检查域名解析..."
if ! host $DOMAIN &> /dev/null; then
    print_warning "域名 $DOMAIN 解析可能有问题，请确认DNS已配置"
    read -p "是否继续？(y/n) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

# 3. 停止现有容器
print_info "停止并删除现有容器..."
if docker ps -a --format '{{.Names}}' | grep -q "^${CONTAINER_NAME}$"; then
    docker stop $CONTAINER_NAME 2>/dev/null || true
    docker rm $CONTAINER_NAME 2>/dev/null || true
    print_info "容器已停止并删除"
else
    print_info "没有发现现有容器"
fi

# 4. 检查80端口
print_info "检查端口占用情况..."
if ! check_port 80; then
    print_error "端口80被占用，需要先释放才能获取证书"
    read -p "是否尝试停止占用进程？(y/n) " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        sudo fuser -k 80/tcp 2>/dev/null || true
        sleep 2
    else
        exit 1
    fi
fi

# 5. 获取SSL证书
print_info "获取 Let's Encrypt SSL 证书..."
if [ -d "/etc/letsencrypt/live/$DOMAIN" ]; then
    print_warning "证书已存在，跳过获取步骤"
else
    sudo certbot certonly --standalone \
        -d $DOMAIN \
        -d $WWW_DOMAIN \
        --email $EMAIL \
        --agree-tos \
        --non-interactive \
        --preferred-challenges http
    
    if [ $? -eq 0 ]; then
        print_info "证书获取成功！"
    else
        print_error "证书获取失败，请检查域名解析和网络连接"
        exit 1
    fi
fi

# 6. 验证配置文件和证书
print_info "验证配置文件..."
if [ ! -f "$NGINX_CONF" ]; then
    print_error "Nginx 配置文件不存在: $NGINX_CONF"
    exit 1
fi

if [ ! -f "/etc/letsencrypt/live/$DOMAIN/fullchain.pem" ]; then
    print_error "证书文件不存在"
    exit 1
fi

# 7. 创建HTML目录（如果不存在）
if [ ! -d "$HTML_PATH" ]; then
    print_warning "HTML目录不存在，创建目录: $HTML_PATH"
    sudo mkdir -p $HTML_PATH
fi

# 8. 启动支持HTTPS的容器
print_info "启动支持 HTTPS 的 Docker 容器..."
docker run -d \
    --name $CONTAINER_NAME \
    -p 80:80 \
    -p 443:443 \
    -v $HTML_PATH:/usr/share/nginx/html \
    -v $NGINX_CONF:/etc/nginx/conf.d/default.conf:ro \
    -v /etc/letsencrypt/live/$DOMAIN/fullchain.pem:/etc/nginx/ssl/fullchain.pem:ro \
    -v /etc/letsencrypt/live/$DOMAIN/privkey.pem:/etc/nginx/ssl/privkey.pem:ro \
    --restart always \
    nginx:latest

if [ $? -eq 0 ]; then
    print_info "容器启动成功！"
else
    print_error "容器启动失败"
    exit 1
fi

# 9. 等待容器启动
sleep 3

# 10. 检查容器状态
print_info "检查容器运行状态..."
if docker ps --filter "name=$CONTAINER_NAME" --format '{{.Names}}' | grep -q "^${CONTAINER_NAME}$"; then
    print_info "容器运行正常 ✓"
else
    print_error "容器未正常运行"
    docker logs $CONTAINER_NAME
    exit 1
fi

# 11. 测试Nginx配置
print_info "测试 Nginx 配置..."
docker exec $CONTAINER_NAME nginx -t
if [ $? -eq 0 ]; then
    print_info "Nginx 配置测试通过 ✓"
else
    print_error "Nginx 配置有误"
    exit 1
fi

# 12. 配置自动续期
print_info "配置 SSL 证书自动续期..."
CRON_CMD="0 2 * * * certbot renew --quiet --post-hook 'docker restart $CONTAINER_NAME' >> /var/log/certbot-renew.log 2>&1"
(crontab -l 2>/dev/null | grep -v "certbot renew"; echo "$CRON_CMD") | crontab -
print_info "已添加自动续期任务（每天凌晨2点）"

# 13. 配置防火墙（如果有ufw）
if check_command ufw; then
    print_info "配置防火墙规则..."
    sudo ufw allow 80/tcp >/dev/null 2>&1 || true
    sudo ufw allow 443/tcp >/dev/null 2>&1 || true
    print_info "防火墙规则已配置"
fi

# 14. 显示容器信息
echo ""
echo "=========================================="
echo "  ✅ HTTPS 配置完成！"
echo "=========================================="
echo ""
echo "📊 容器信息："
docker ps --filter "name=$CONTAINER_NAME" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
echo ""
echo "🌐 访问地址："
echo "  - HTTPS: https://$DOMAIN"
echo "  - HTTPS: https://$WWW_DOMAIN"
echo ""
echo "📜 证书信息："
echo "  - 证书路径: /etc/letsencrypt/live/$DOMAIN/"
echo "  - 有效期: 90天"
echo "  - 自动续期: 已配置（每天凌晨2点检查）"
echo ""
echo "🔧 常用命令："
echo "  - 查看容器日志: docker logs $CONTAINER_NAME"
echo "  - 重启容器: docker restart $CONTAINER_NAME"
echo "  - 停止容器: docker stop $CONTAINER_NAME"
echo "  - 手动续期证书: sudo certbot renew"
echo "  - 测试续期: sudo certbot renew --dry-run"
echo ""
echo "🧪 测试 SSL 配置："
echo "  - 访问: https://www.ssllabs.com/ssltest/analyze.html?d=$DOMAIN"
echo ""

# 15. 测试HTTPS连接
print_info "测试 HTTPS 连接..."
sleep 2
if curl -sI https://$DOMAIN | head -n 1 | grep "200\|301\|302" > /dev/null; then
    print_info "HTTPS 连接测试成功 ✓"
else
    print_warning "HTTPS 连接测试失败，请检查防火墙和DNS配置"
fi

echo ""
print_info "脚本执行完成！"

