# ==========================================
# 新项目
# ==========================================
python3 -m venv venv              # 创建虚拟环境
source venv/bin/activate          # 激活（Mac/Linux）
venv\Scripts\activate             # 激活（Windows）
pip install <package>             # 安装包
pip freeze > requirements.txt    # 保存依赖

# ==========================================
# 克隆项目
# ==========================================
python3 -m venv venv              # 创建虚拟环境
source venv/bin/activate          # 激活
pip install -r requirements.txt  # 安装所有依赖

# ==========================================
# 日常使用
# ==========================================
source venv/bin/activate          # 每天开始工作前激活
python app.py                     # 运行项目
deactivate                        # 结束工作（可选）

# ==========================================
# 检查状态
# ==========================================
which python                      # 查看当前 Python 路径
pip list                          # 查看已安装的包