#!/bin/bash

# Jenkins 配置初始化脚本
# 用于快速生成配置文件

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_FILE="$SCRIPT_DIR/jenkins-config.conf"
EXAMPLE_FILE="$SCRIPT_DIR/jenkins-config.conf.example"

echo "🔧 Jenkins 配置初始化向导"
echo "================================"

# 检查是否已存在配置文件
if [ -f "$CONFIG_FILE" ]; then
    echo -e "${YELLOW}⚠️  配置文件已存在: $CONFIG_FILE${NC}"
    read -p "是否覆盖现有配置？(y/N): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        echo "取消配置初始化"
        exit 0
    fi
fi

echo ""
echo "请选择数据存储路径："
echo "1) 服务器标准路径: /opt/jenkins-data (推荐)"
echo "2) 用户目录 (macOS): /Users/$USER/jenkins-data"
echo "3) 用户目录 (Linux): /home/$USER/jenkins-data"
echo "4) 自定义路径"
echo ""

read -p "请选择 [1-4]: " -n 1 -r choice
echo ""

case $choice in
    1)
        DATA_DIR="/opt/jenkins-data"
        ENV_TYPE="服务器标准路径"
        ;;
    2)
        DATA_DIR="/Users/$USER/jenkins-data"
        ENV_TYPE="用户目录 (macOS)"
        ;;
    3)
        DATA_DIR="/home/$USER/jenkins-data"
        ENV_TYPE="用户目录 (Linux)"
        ;;
    4)
        echo ""
        read -p "请输入自定义数据目录路径: " DATA_DIR
        ENV_TYPE="自定义路径"
        ;;
    *)
        echo -e "${RED}❌ 无效选择${NC}"
        exit 1
        ;;
esac

echo ""
echo -e "${BLUE}📋 配置信息${NC}"
echo "环境类型: $ENV_TYPE"
echo "数据目录: $DATA_DIR"
echo ""

# 检查路径是否合理
if [[ "$DATA_DIR" != /* ]]; then
    echo -e "${RED}❌ 路径必须是绝对路径（以 / 开头）${NC}"
    exit 1
fi

# 检查磁盘空间
if command -v df &> /dev/null; then
    parent_dir=$(dirname "$DATA_DIR")
    if [ -d "$parent_dir" ]; then
        available_space=$(df -BG "$parent_dir" 2>/dev/null | awk 'NR==2 {print $4}' | sed 's/G//')
        if [ -n "$available_space" ] && [ "$available_space" -lt 10 ]; then
            echo -e "${YELLOW}⚠️  警告：可用磁盘空间少于 10GB，建议确保有足够空间${NC}"
        fi
    fi
fi

# 生成配置文件
cat > "$CONFIG_FILE" << EOF
# Jenkins Docker 部署配置文件
# 生成时间: $(date)
# 环境类型: $ENV_TYPE

# ===========================================
# 数据目录配置
# ===========================================

# Jenkins 数据存储根目录
DATA_DIR="$DATA_DIR"

# ===========================================
# 其他配置（可选）
# ===========================================

# 如果需要覆盖默认配置，可以在这里添加
# 例如：
# JENKINS_VERSION="lts"
# JAVA_OPTS="-Xmx2g -Xms1g"

EOF

echo -e "${GREEN}✅ 配置文件已生成: $CONFIG_FILE${NC}"
echo ""

# 询问是否创建数据目录
read -p "是否立即创建数据目录？(Y/n): " -n 1 -r
echo
if [[ $REPLY =~ ^[Nn]$ ]]; then
    echo "数据目录将在首次运行时自动创建"
else
    echo "📁 创建数据目录..."
    if [ ! -d "$DATA_DIR" ]; then
        if sudo mkdir -p "$DATA_DIR" 2>/dev/null; then
            sudo chown -R $USER:$USER "$DATA_DIR" 2>/dev/null || true
            echo -e "${GREEN}✅ 数据目录创建成功: $DATA_DIR${NC}"
        else
            echo -e "${YELLOW}⚠️  无法自动创建目录，请手动创建：${NC}"
            echo "sudo mkdir -p $DATA_DIR"
            echo "sudo chown -R $USER:$USER $DATA_DIR"
        fi
    else
        echo -e "${BLUE}ℹ️  数据目录已存在: $DATA_DIR${NC}"
    fi
fi

echo ""
echo -e "${GREEN}🎉 配置完成！${NC}"
echo ""
echo "现在可以使用以下命令启动 Jenkins："
echo "  ./start-jenkins.sh           # 启动 Jenkins"
echo "  ./jenkins-manage.sh start    # 启动服务"
echo "  ./jenkins-manage.sh password # 获取管理员密码"
echo ""
echo "数据将存储在: $DATA_DIR"
