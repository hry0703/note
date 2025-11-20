 
## 将文件上传到服务器有多种方法，具体选择哪种方式取决于您的具体需求和服务器配置。以下是几种常用的方法：
### 1. 使用 SCP（推荐）
# 上传单个文件
scp /path/to/local/file username@server_ip:/path

# 上传目录（递归）
scp -r /path/to/local/directory username@server_ip:/path

###### : 只是用来分隔服务器地址和路径的分隔符，它后面的路径可以是任何有效的路径。
1. 绝对路径（以 / 开头）
    # 上传到根目录下的 tmp 文件夹
    username@server:/tmp/

    # 上传到用户家目录
    username@server:/home/username/

    # 上传到 web 服务器目录
    username@server:/var/www/html/

    # 上传到自定义目录
    username@server:/opt/myapp/data/
2. 相对路径（不以 / 开头）
    # 相对于用户家目录的路径
    username@server:documents/

    # 相对路径的子目录
    username@server:projects/myproject/

    # 当前目录（用户家目录）
    username@server: