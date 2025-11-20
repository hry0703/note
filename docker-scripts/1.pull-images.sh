#!/bin/bash

echo "开始拉取镜像..."

# 拉取 Nginx
echo "拉取 Nginx..."
docker pull nginx:latest

# 拉取 GitLab
echo "拉取 GitLab..."
docker pull gitlab/gitlab-ce:latest

# 拉取 MySQL
echo "拉取 MySQL..."
docker pull mysql:8.0

# 拉取 Jenkins
echo "拉取 Jenkins..."
docker pull jenkins/jenkins:lts

echo "所有镜像拉取完成！"
docker images



# # 给脚本执行权限
# chmod +x pull-images.sh

# # 运行脚本
# ./pull-images.sh