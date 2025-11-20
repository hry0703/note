#!/bin/bash

# Jenkins 管理脚本
# 用于在任何目录下管理 Jenkins 服务

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 获取脚本目录和数据目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# 设置可配置的数据目录路径（与 start-jenkins.sh 保持一致）
DEFAULT_DATA_DIR="/opt/jenkins-data"
CONFIG_FILE="$SCRIPT_DIR/jenkins-config.conf"

# 从配置文件读取数据目录路径
if [ -f "$CONFIG_FILE" ]; then
    source "$CONFIG_FILE"
fi

# 确定最终的数据目录路径
if [ -n "$JENKINS_DATA_DIR" ]; then
    DATA_BASE_DIR="$JENKINS_DATA_DIR"
elif [ -n "$DATA_DIR" ]; then
    DATA_BASE_DIR="$DATA_DIR"
else
    DATA_BASE_DIR="$DEFAULT_DATA_DIR"
fi

# 默认配置文件
DEFAULT_COMPOSE_FILE="jenkins-docker-compose.yml"

# 显示帮助信息
show_help() {
    echo "Jenkins Docker 管理脚本"
    echo ""
    echo "用法: $0 <命令>"
    echo ""
    echo "命令:"
    echo "  start    - 启动 Jenkins 服务"
    echo "  stop     - 停止 Jenkins 服务"
    echo "  restart  - 重启 Jenkins 服务"
    echo "  status   - 查看服务状态"
    echo "  logs     - 查看服务日志"
    echo "  ps       - 查看容器状态"
    echo "  down     - 停止并删除容器"
    echo "  pull     - 拉取最新镜像"
    echo "  password - 获取初始管理员密码"
    echo ""
    echo "示例:"
    echo "  $0 start     - 启动 Jenkins"
    echo "  $0 logs      - 查看日志"
    echo "  $0 password  - 获取管理员密码"
}

# 获取配置文件
get_compose_file() {
    echo "jenkins-docker-compose.yml"
}

# 执行 docker-compose 命令
run_compose() {
    local compose_file="$1"
    shift
    local cmd="$@"
    
    echo -e "${BLUE}📂 数据目录: $DATA_BASE_DIR${NC}"
    echo -e "${BLUE}📄 配置文件: $compose_file${NC}"
    echo -e "${BLUE}🔧 执行命令: $cmd${NC}"
    echo ""
    
    if command -v docker-compose &> /dev/null; then
        cd "$DATA_BASE_DIR" && docker-compose -f "$SCRIPT_DIR/$compose_file" $cmd
    else
        cd "$DATA_BASE_DIR" && docker compose -f "$SCRIPT_DIR/$compose_file" $cmd
    fi
}

# 获取初始管理员密码
get_admin_password() {
    echo -e "${BLUE}🔑 获取 Jenkins 初始管理员密码...${NC}"
    echo ""
    if docker ps | grep -q jenkins; then
        password=$(docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword 2>/dev/null)
        if [ -n "$password" ]; then
            echo -e "${GREEN}初始管理员密码：${NC}"
            echo -e "${YELLOW}$password${NC}"
            echo ""
            echo "请复制此密码，在首次访问 Jenkins 时使用"
        else
            echo -e "${YELLOW}⚠️  无法获取密码，可能 Jenkins 还未完全启动${NC}"
        fi
    else
        echo -e "${RED}❌ Jenkins 容器未运行${NC}"
    fi
}

# 主逻辑
main() {
    local command="$1"
    
    if [ -z "$command" ]; then
        show_help
        exit 1
    fi
    
    # 获取配置文件
    local compose_file=$(get_compose_file)
    
    # 检查配置文件是否存在
    if [ ! -f "$SCRIPT_DIR/$compose_file" ]; then
        echo -e "${RED}❌ 配置文件不存在: $SCRIPT_DIR/$compose_file${NC}"
        exit 1
    fi
    
    case "$command" in
        "start")
            echo -e "${GREEN}🚀 启动 Jenkins 服务...${NC}"
            "$SCRIPT_DIR/start-jenkins.sh"
            ;;
        "stop")
            echo -e "${YELLOW}⏹️  停止 Jenkins 服务...${NC}"
            run_compose "$compose_file" stop
            ;;
        "restart")
            echo -e "${YELLOW}🔄 重启 Jenkins 服务...${NC}"
            run_compose "$compose_file" restart
            ;;
        "status"|"ps")
            echo -e "${BLUE}📊 查看服务状态...${NC}"
            run_compose "$compose_file" ps
            ;;
        "logs")
            echo -e "${BLUE}📋 查看服务日志...${NC}"
            run_compose "$compose_file" logs -f
            ;;
        "down")
            echo -e "${RED}🗑️  停止并删除容器...${NC}"
            run_compose "$compose_file" down
            ;;
        "pull")
            echo -e "${BLUE}📥 拉取最新镜像...${NC}"
            run_compose "$compose_file" pull
            ;;
        "password")
            get_admin_password
            ;;
        "help"|"-h"|"--help")
            show_help
            ;;
        *)
            echo -e "${RED}❌ 未知命令: $command${NC}"
            echo ""
            show_help
            exit 1
            ;;
    esac
}

# 执行主函数
main "$@"
