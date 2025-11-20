#!/bin/bash
set -e

# GitLab Docker Compose 启动脚本
# 作者：自动生成
# 版本：1.0
# 更新时间：2025年10月

echo "🚀 启动 GitLab Docker Compose 部署..."

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 检查 Docker 和 Docker Compose
echo "🔍 检查依赖..."
if ! command -v docker &> /dev/null; then
    echo -e "${RED}❌ Docker 未安装，请先安装 Docker${NC}"
    echo "安装指南：https://docs.docker.com/get-docker/"
    exit 1
fi

if ! command -v docker-compose &> /dev/null && ! docker compose version &> /dev/null; then
    echo -e "${RED}❌ Docker Compose 未安装，请先安装 Docker Compose${NC}"
    echo "安装指南：https://docs.docker.com/compose/install/"
    exit 1
fi

# 获取脚本目录的绝对路径
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# 使用标准配置文件
COMPOSE_FILE="gitlab-docker-compose.yml"
echo "⚙️  使用 GitLab 标准配置"

# 检查配置文件是否存在
if [ ! -f "$SCRIPT_DIR/$COMPOSE_FILE" ]; then
    echo -e "${RED}❌ 未找到配置文件: $SCRIPT_DIR/$COMPOSE_FILE${NC}"
    exit 1
fi

echo "📄 配置文件: $COMPOSE_FILE"

# 设置可配置的数据目录路径
# 优先级：环境变量 > 配置文件 > 默认路径
DEFAULT_DATA_DIR="/opt/gitlab-data"  # 推荐的服务器部署路径
CONFIG_FILE="$SCRIPT_DIR/gitlab-config.conf"

# 从配置文件读取数据目录路径
if [ -f "$CONFIG_FILE" ]; then
    source "$CONFIG_FILE"
fi

# 确定最终的数据目录路径
if [ -n "$GITLAB_DATA_DIR" ]; then
    DATA_BASE_DIR="$GITLAB_DATA_DIR"
    echo "🔧 使用环境变量指定的数据目录: $DATA_BASE_DIR"
elif [ -n "$DATA_DIR" ]; then
    DATA_BASE_DIR="$DATA_DIR"
    echo "📄 使用配置文件指定的数据目录: $DATA_BASE_DIR"
else
    DATA_BASE_DIR="$DEFAULT_DATA_DIR"
    echo "⚙️  使用默认数据目录: $DATA_BASE_DIR"
fi

echo "🔍 数据目录: $DATA_BASE_DIR"
echo "📁 脚本位置: $SCRIPT_DIR"

# 检查数据目录是否存在，不存在则创建
if [ ! -d "$DATA_BASE_DIR" ]; then
    echo "📁 创建数据根目录: $DATA_BASE_DIR"
    sudo mkdir -p "$DATA_BASE_DIR" || {
        echo -e "${RED}❌ 无法创建数据目录: $DATA_BASE_DIR${NC}"
        echo "请检查权限或手动创建目录"
        exit 1
    }
    sudo chown -R $USER:$USER "$DATA_BASE_DIR" 2>/dev/null || true
fi

# 创建必要目录
echo "📁 创建数据目录..."
mkdir -p "$DATA_BASE_DIR/gitlab"/{config,logs,data,ssl,backups}

# 设置权限
echo "🔐 设置目录权限..."
if command -v sudo &> /dev/null; then
    sudo chown -R 998:998 "$DATA_BASE_DIR/gitlab/" 2>/dev/null || {
        echo -e "${YELLOW}⚠️  无法设置 gitlab 目录权限，请手动执行：sudo chown -R 998:998 $DATA_BASE_DIR/gitlab/${NC}"
    }
else
    echo -e "${YELLOW}⚠️  sudo 不可用，跳过权限设置${NC}"
fi

# 检查端口占用
echo "🔍 检查端口占用..."
check_port() {
    local port=$1
    if command -v netstat &> /dev/null; then
        if netstat -tlnp 2>/dev/null | grep -q ":${port} "; then
            echo -e "${YELLOW}⚠️  警告：端口 ${port} 已被占用${NC}"
            return 1
        fi
    elif command -v ss &> /dev/null; then
        if ss -tlnp 2>/dev/null | grep -q ":${port} "; then
            echo -e "${YELLOW}⚠️  警告：端口 ${port} 已被占用${NC}"
            return 1
        fi
    else
        echo -e "${YELLOW}⚠️  无法检查端口占用（netstat 和 ss 都不可用）${NC}"
    fi
    return 0
}

# 检查 GitLab 使用的端口
check_port 18080
check_port 18443
check_port 12222

# 检查系统资源
echo "💾 检查系统资源..."
if command -v free &> /dev/null; then
    total_mem=$(free -m | awk 'NR==2{printf "%.0f", $2}')
    if [ "$total_mem" -lt 2048 ]; then
        echo -e "${YELLOW}⚠️  警告：系统内存少于 2GB (${total_mem}MB)，GitLab 可能运行缓慢${NC}"
        echo "建议增加 swap 空间或升级内存"
    else
        echo -e "${GREEN}✅ 内存检查通过 (${total_mem}MB)${NC}"
    fi
fi

if command -v df &> /dev/null; then
    available_space=$(df -BG . | awk 'NR==2 {print $4}' | sed 's/G//')
    if [ "$available_space" -lt 10 ]; then
        echo -e "${YELLOW}⚠️  警告：可用磁盘空间少于 10GB (${available_space}GB)${NC}"
        echo "建议清理磁盘空间或扩容"
    else
        echo -e "${GREEN}✅ 磁盘空间检查通过 (${available_space}GB 可用)${NC}"
    fi
fi

# 拉取最新镜像（可选）
read -p "是否拉取最新的 GitLab 镜像？(y/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "📥 拉取最新镜像..."
    if command -v docker-compose &> /dev/null; then
        cd "$DATA_BASE_DIR" && docker-compose -f "$SCRIPT_DIR/$COMPOSE_FILE" pull
    else
        cd "$DATA_BASE_DIR" && docker compose -f "$SCRIPT_DIR/$COMPOSE_FILE" pull
    fi
fi

# 启动服务
echo "🎯 启动 GitLab 服务..."
echo "📂 切换到数据目录: $DATA_BASE_DIR"
if command -v docker-compose &> /dev/null; then
    cd "$DATA_BASE_DIR" && docker-compose -f "$SCRIPT_DIR/$COMPOSE_FILE" up -d
else
    cd "$DATA_BASE_DIR" && docker compose -f "$SCRIPT_DIR/$COMPOSE_FILE" up -d
fi

# 等待服务启动
echo "⏳ 等待 GitLab 启动（这可能需要 3-10 分钟）..."
echo "您可以按 Ctrl+C 取消等待，服务将在后台继续启动"

# 显示启动进度
for i in {1..30}; do
    sleep 10
    if docker ps --format "table {{.Names}}\t{{.Status}}" | grep -q "gitlab.*healthy\|gitlab.*Up"; then
        echo -e "${GREEN}✅ GitLab 容器启动成功！${NC}"
        break
    fi
    echo "⏳ 等待中... (${i}0秒)"
done

# 显示状态
echo "📊 服务状态："
if command -v docker-compose &> /dev/null; then
    cd "$DATA_BASE_DIR" && docker-compose -f "$SCRIPT_DIR/$COMPOSE_FILE" ps
else
    cd "$DATA_BASE_DIR" && docker compose -f "$SCRIPT_DIR/$COMPOSE_FILE" ps
fi

# 检查 GitLab 健康状态
echo "🏥 检查 GitLab 健康状态..."
sleep 5
if docker exec gitlab curl -f http://localhost/-/health &>/dev/null; then
    echo -e "${GREEN}✅ GitLab 健康检查通过${NC}"
else
    echo -e "${YELLOW}⚠️  GitLab 可能还在启动中，请稍后再试${NC}"
fi

echo ""
echo -e "${GREEN}🎉 GitLab 启动完成！${NC}"
echo ""
echo "📋 访问信息："

# GitLab 访问信息
# 获取公网IP地址
PUBLIC_IP=$(curl -s --connect-timeout 5 ifconfig.me 2>/dev/null || curl -s --connect-timeout 5 ipinfo.io/ip 2>/dev/null || curl -s --connect-timeout 5 icanhazip.com 2>/dev/null || echo "获取失败")

# 获取内网IP地址作为备用
LOCAL_IP=$(ifconfig | grep -Eo 'inet (addr:)?([0-9]*\.){3}[0-9]*' | grep -Eo '([0-9]*\.){3}[0-9]*' | grep -v '127.0.0.1' | head -1 2>/dev/null || echo "localhost")

echo -e "🌐 Web 访问地址："
echo -e "  - 本地访问：${BLUE}http://localhost:18080${NC}"
echo -e "  - 内网访问：${BLUE}http://${LOCAL_IP}:18080${NC}"
if [ "$PUBLIC_IP" != "获取失败" ]; then
    echo -e "  - 公网访问：${BLUE}http://${PUBLIC_IP}:18080${NC}"
fi
echo -e "🔒 HTTPS 访问地址："
echo -e "  - 本地访问：${BLUE}https://localhost:18443${NC}"
echo -e "  - 内网访问：${BLUE}https://${LOCAL_IP}:18443${NC}"
if [ "$PUBLIC_IP" != "获取失败" ]; then
    echo -e "  - 公网访问：${BLUE}https://${PUBLIC_IP}:18443${NC}"
fi
echo -e "🔑 SSH 克隆端口：${BLUE}12222${NC}"
echo -e "  - 内网克隆：${BLUE}git clone ssh://git@${LOCAL_IP}:12222/username/repo.git${NC}"
if [ "$PUBLIC_IP" != "获取失败" ]; then
    echo -e "  - 公网克隆：${BLUE}git clone ssh://git@${PUBLIC_IP}:12222/username/repo.git${NC}"
fi

echo -e "👤 默认用户名：${BLUE}root${NC}"
echo ""
echo "🔑 获取初始密码："
echo "docker exec gitlab grep 'Password:' /etc/gitlab/initial_root_password 2>/dev/null || echo '密码文件不存在，请检查容器状态'"
echo ""

echo "📝 常用管理命令（需要在数据目录 $DATA_BASE_DIR 下执行）："
echo "  查看日志：cd $DATA_BASE_DIR && docker-compose -f $SCRIPT_DIR/$COMPOSE_FILE logs -f"
echo "  重启服务：cd $DATA_BASE_DIR && docker-compose -f $SCRIPT_DIR/$COMPOSE_FILE restart"
echo "  停止服务：cd $DATA_BASE_DIR && docker-compose -f $SCRIPT_DIR/$COMPOSE_FILE down"
echo "  进入容器：docker exec -it \$(cd $DATA_BASE_DIR && docker-compose -f $SCRIPT_DIR/$COMPOSE_FILE ps -q | head -1) bash"
echo "  备份数据：docker exec -t \$(cd $DATA_BASE_DIR && docker-compose -f $SCRIPT_DIR/$COMPOSE_FILE ps -q | head -1) gitlab-backup create"
echo "  查看状态：cd $DATA_BASE_DIR && docker-compose -f $SCRIPT_DIR/$COMPOSE_FILE ps"
echo ""

echo "📚 更多帮助："
echo "  GitLab 文档：https://docs.gitlab.com/"
echo "  Docker Compose 文档：https://docs.docker.com/compose/"
echo ""

echo -e "${GREEN}✨ 享受使用 GitLab！${NC}"
