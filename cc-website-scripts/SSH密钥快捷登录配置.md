# SSH 密钥快捷登录配置指南

## 一、准备工作

确保您已经有了密钥文件（私钥文件），通常命名为 `id_rsa`、`id_ed25519` 或自定义名称。

## 二、配置步骤

### 1. 放置密钥文件

```bash
# 创建 .ssh 目录（如果不存在）
mkdir -p ~/.ssh

# 将您的密钥文件复制到 .ssh 目录（替换为您的实际路径）
cp /path/to/your/keyfile ~/.ssh/my_key

# 设置正确的权限（非常重要！）
chmod 600 ~/.ssh/my_key
chmod 700 ~/.ssh
```

### 2. 编辑 SSH 配置文件

```bash
# 编辑或创建配置文件
vim ~/.ssh/config
```

或者使用其他编辑器：
```bash
nano ~/.ssh/config
# 或
code ~/.ssh/config
```

### 3. 添加服务器配置

在 `~/.ssh/config` 文件中添加以下内容：

```bash
# 服务器 1 配置
Host server1
    HostName 192.168.1.100
    User root
    Port 22
    IdentityFile ~/.ssh/my_key

# 服务器 2 配置
Host server2
    HostName example.com
    User ubuntu
    Port 22
    IdentityFile ~/.ssh/my_key

# 服务器 3 配置（自定义端口）
Host server3
    HostName 192.168.1.200
    User admin
    Port 2222
    IdentityFile ~/.ssh/another_key
```

### 4. 设置配置文件权限

```bash
chmod 600 ~/.ssh/config
```

### 5. 使用快捷登录

配置完成后，登录服务器就非常简单了：

```bash
# 登录服务器 1
ssh server1

# 登录服务器 2
ssh server2

# 登录服务器 3
ssh server3
```

不需要再输入 IP、用户名、端口和密钥文件路径！

## 三、配置参数说明

| 参数 | 说明 | 示例 |
|------|------|------|
| Host | 别名（自定义，用于登录） | `server1` |
| HostName | 服务器 IP 或域名 | `192.168.1.100` 或 `example.com` |
| User | 登录用户名 | `root`、`ubuntu`、`admin` |
| Port | SSH 端口（默认 22） | `22`、`2222` |
| IdentityFile | 私钥文件路径 | `~/.ssh/my_key` |

## 四、其他常用配置选项

```bash
Host myserver
    HostName 192.168.1.100
    User root
    Port 22
    IdentityFile ~/.ssh/my_key
    # 保持连接活跃
    ServerAliveInterval 60
    ServerAliveCountMax 3
    # 禁用严格的主机密钥检查（仅开发环境）
    StrictHostKeyChecking no
    # 不保存主机密钥（仅开发环境）
    UserKnownHostsFile /dev/null
    # 连接超时时间
    ConnectTimeout 10
    # 启用压缩
    Compression yes
```

## 五、完整示例

```bash
# 生产服务器
Host prod-web
    HostName 192.168.1.10
    User deploy
    Port 22
    IdentityFile ~/.ssh/prod_key
    ServerAliveInterval 60

# 测试服务器
Host test-web
    HostName test.example.com
    User admin
    Port 2222
    IdentityFile ~/.ssh/test_key
    ServerAliveInterval 60

# 开发服务器
Host dev
    HostName 192.168.1.50
    User developer
    Port 22
    IdentityFile ~/.ssh/dev_key
    StrictHostKeyChecking no

# 使用通配符配置多个服务器
Host *.example.com
    User admin
    Port 22
    IdentityFile ~/.ssh/company_key
```

## 六、使用技巧

### 1. 查看所有已配置的主机

```bash
grep "^Host " ~/.ssh/config
```

### 2. 测试 SSH 连接

```bash
ssh -T server1
```

### 3. 使用 SCP 复制文件（也支持别名）

```bash
# 从本地复制到服务器
scp file.txt server1:/path/to/destination/

# 从服务器复制到本地
scp server1:/path/to/file.txt ./
```

### 4. 使用 rsync 同步文件

```bash
rsync -avz /local/path/ server1:/remote/path/
```

### 5. SSH 跳板机配置

```bash
# 通过跳板机连接目标服务器
Host target-server
    HostName 10.0.0.100
    User admin
    IdentityFile ~/.ssh/my_key
    ProxyJump jump-server

Host jump-server
    HostName 192.168.1.1
    User jump
    IdentityFile ~/.ssh/jump_key
```

## 七、常见问题

### 1. 权限错误

```bash
# 确保权限正确
chmod 700 ~/.ssh
chmod 600 ~/.ssh/config
chmod 600 ~/.ssh/my_key
```

### 2. 密钥已设置但仍要求密码

- 检查服务器上的 `~/.ssh/authorized_keys` 是否包含对应的公钥
- 检查服务器上的权限：`chmod 700 ~/.ssh && chmod 600 ~/.ssh/authorized_keys`

### 3. 查看详细的连接信息（调试）

```bash
ssh -v server1  # 普通详细信息
ssh -vv server1 # 更详细
ssh -vvv server1 # 最详细
```

## 八、安全建议

1. ✅ 始终为私钥文件设置 `600` 权限
2. ✅ 为私钥设置密码短语（passphrase）
3. ✅ 不同服务器使用不同的密钥
4. ✅ 定期更换密钥
5. ✅ 生产环境不要禁用 `StrictHostKeyChecking`
6. ✅ 妥善备份私钥文件
7. ❌ 不要将私钥文件提交到 Git 仓库
8. ❌ 不要将私钥文件共享给他人

## 九、快速参考

```bash
# 编辑配置
vim ~/.ssh/config

# 使用别名登录
ssh server1

# 复制文件
scp file.txt server1:~/

# 同步目录
rsync -avz ./local/ server1:~/remote/

# 查看配置的主机
grep "^Host " ~/.ssh/config

# 测试连接
ssh -T server1
```

---

**最后更新：** 2025-11-05




cc 官网服务器
密钥路径 ～/.ssh/cc/id_ed25519

# cat ~/.ssh.config
```
Host ccprod
    HostName 8.137.90.127
    User root
    Port 12020
    IdentityFile ~/.ssh/cc/id_ed25519
```

快捷登录 ssh ccprod