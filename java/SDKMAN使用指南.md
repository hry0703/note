# SDKMAN 使用指南

## 📖 什么是 SDKMAN？

SDKMAN（Software Development Kit Manager）是 Java 生态中最流行的版本管理工具，类似于 Node.js 的 `nvm`。它可以帮你：

- 管理多个 Java 版本（JDK）
- 管理其他开发工具（Maven, Gradle, Spring Boot CLI 等）
- 轻松切换不同版本
- 自动配置环境变量

---

## 🚀 安装 SDKMAN

### macOS / Linux

```bash
# 安装 SDKMAN
curl -s "https://get.sdkman.io" | bash

# 重新加载 shell 配置
source "$HOME/.sdkman/bin/sdkman-init.sh"

# 验证安装
sdk version
```

**安装后会自动添加到 shell 配置文件：**
- Bash: `~/.bashrc`
- Zsh: `~/.zshrc`

### Windows

SDKMAN 在 Windows 上需要以下环境之一：

1. **Git Bash**（推荐）
   - 下载安装 Git for Windows
   - 在 Git Bash 中运行安装命令

2. **WSL（Windows Subsystem for Linux）**
   - 在 WSL 中按照 Linux 方式安装

3. **Cygwin**
   - 在 Cygwin 终端中安装

---

## 📦 管理 Java 版本

### 查看可用的 Java 版本

```bash
# 列出所有可用的 Java 版本（包括不同发行版）
sdk list java

# 只查看已安装的版本
sdk list java | grep installed

# 查看特定发行版（如 Temurin）
sdk list java | grep tem
```

### 安装 Java

```bash
# 安装最新稳定版 Java 17（Temurin 发行版）
sdk install java 17.0.2-tem

# 安装特定版本
sdk install java 11.0.19-tem

# 安装 Java 21
sdk install java 21.0.1-tem

# 安装时指定默认使用
sdk install java 17.0.2-tem
sdk default java 17.0.2-tem
```

**常见的 Java 发行版：**
- `tem` - Eclipse Temurin（推荐，开源免费）
- `zulu` - Azul Zulu
- `open` - OpenJDK
- `oracle` - Oracle JDK（商业使用需授权）

### 切换 Java 版本

```bash
# 切换到指定版本（临时，仅当前终端会话）
sdk use java 17.0.2-tem

# 设置默认版本（全局）
sdk default java 17.0.2-tem

# 查看当前使用的版本
sdk current java

# 查看所有已安装的版本
sdk list java | grep installed
```

### 卸载 Java 版本

```bash
# 卸载指定版本
sdk uninstall java 11.0.19-tem
```

### SDKMAN 自动配置环境变量详解

**是的！SDKMAN 会自动配置环境变量，这是它的核心功能之一。**

#### 自动配置机制

当你安装或切换 Java 版本时，SDKMAN 会自动：

1. **设置 JAVA_HOME**
   - 自动指向当前使用的 JDK 安装目录
   - 例如：`JAVA_HOME=$HOME/.sdkman/candidates/java/current`

2. **更新 PATH**
   - 自动将 JDK 的 `bin` 目录添加到 PATH 最前面
   - 例如：`PATH=$JAVA_HOME/bin:$PATH`

3. **创建符号链接**
   - 在 `~/.sdkman/candidates/java/current` 创建指向当前版本的软链接
   - 切换版本时自动更新这个链接

#### 验证自动配置

安装 Java 后，可以验证环境变量：

```bash
# 安装 Java 17
sdk install java 17.0.2-tem

# 查看 JAVA_HOME（自动设置）
echo $JAVA_HOME
# 输出：/Users/你的用户名/.sdkman/candidates/java/current

# 查看 Java 版本（PATH 已自动配置）
java -version
# 输出：openjdk version "17.0.2" ...

# 查看 javac 版本
javac -version
# 输出：javac 17.0.2

# 查看完整路径
which java
# 输出：/Users/你的用户名/.sdkman/candidates/java/current/bin/java
```

#### 环境变量工作原理

**SDKMAN 初始化脚本会：**

1. **加载到 shell 配置**
   ```bash
   # 在 ~/.zshrc 或 ~/.bashrc 中自动添加：
   export SDKMAN_DIR="$HOME/.sdkman"
   [[ -s "$HOME/.sdkman/bin/sdkman-init.sh" ]] && source "$HOME/.sdkman/bin/sdkman-init.sh"
   ```

2. **每次打开终端时自动执行**
   - SDKMAN 初始化脚本会检查当前默认版本
   - 自动设置相应的环境变量

3. **切换版本时动态更新**
   ```bash
   # 切换版本时，SDKMAN 会立即更新环境变量
   sdk use java 11.0.19-tem
   # JAVA_HOME 和 PATH 立即更新
   ```

#### 查看 SDKMAN 管理的环境变量

```bash
# 查看所有 SDKMAN 相关的环境变量
env | grep -i java

# 或者查看 SDKMAN 的配置
cat ~/.sdkman/etc/config

# 查看当前 Java 的完整路径
sdk home java
# 输出：/Users/你的用户名/.sdkman/candidates/java/17.0.2-tem
```

#### 手动检查环境变量

```bash
# 检查 JAVA_HOME
echo $JAVA_HOME
# 应该指向：~/.sdkman/candidates/java/current

# 检查 PATH 中的 Java
echo $PATH | tr ':' '\n' | grep java
# 应该包含：~/.sdkman/candidates/java/current/bin

# 检查 Java 可执行文件
ls -la $(which java)
# 应该指向 SDKMAN 管理的 Java
```

#### 与手动配置的对比

**手动配置（不使用 SDKMAN）：**
```bash
# 需要手动编辑 ~/.zshrc
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home
export PATH=$JAVA_HOME/bin:$PATH

# 切换版本需要手动修改
# 容易出错，需要记住路径
```

**使用 SDKMAN（自动配置）：**
```bash
# 安装后自动配置
sdk install java 17.0.2-tem

# 切换版本自动更新
sdk use java 11.0.19-tem
# 环境变量自动更新，无需手动操作
```

#### 多版本管理

SDKMAN 可以同时安装多个版本，但环境变量只指向当前使用的版本：

```bash
# 安装多个版本
sdk install java 11.0.19-tem
sdk install java 17.0.2-tem
sdk install java 21.0.1-tem

# 查看所有已安装版本
sdk list java | grep installed

# 切换版本（环境变量自动更新）
sdk use java 17.0.2-tem
echo $JAVA_HOME  # 指向 17.0.2

sdk use java 11.0.19-tem
echo $JAVA_HOME  # 自动更新为 11.0.19
```

#### 项目级别的环境变量

SDKMAN 还支持项目级别的版本管理：

```bash
# 在项目根目录创建 .sdkmanrc 文件
echo "java=17.0.2-tem" > .sdkmanrc
echo "maven=3.9.4" >> .sdkmanrc

# 进入项目时自动切换
cd /path/to/project
sdk env
# 自动读取 .sdkmanrc 并切换版本
# 环境变量自动更新为项目指定的版本
```

---

## 🎯 每个项目使用不同的 Java 版本

### 为什么需要项目级别的 Java 版本管理？

在实际开发中，你可能需要：
- 项目 A 使用 Java 11（旧项目）
- 项目 B 使用 Java 17（新项目）
- 项目 C 使用 Java 21（最新项目）

**全局切换的问题：**
- ❌ 每次切换项目都要手动切换 Java 版本
- ❌ 容易忘记切换，导致编译错误
- ❌ 多个项目同时开发时容易混乱

**项目级别管理的优势：**
- ✅ 每个项目自动使用指定的 Java 版本
- ✅ 进入项目目录时自动切换
- ✅ 不同项目互不干扰

---

### 方法 1：使用 .sdkmanrc 文件（推荐）

#### 步骤 1：在项目根目录创建 .sdkmanrc 文件

```bash
# 进入项目目录
cd /path/to/your-project

# 创建 .sdkmanrc 文件
cat > .sdkmanrc << EOF
java=17.0.2-tem
maven=3.9.4
EOF
```

**文件内容示例：**
```
java=17.0.2-tem
maven=3.9.4
gradle=8.4
```

#### 步骤 2：手动激活项目配置

```bash
# 进入项目目录
cd /path/to/your-project

# 激活项目配置（切换到项目指定的版本）
sdk env

# 验证当前版本
java -version
mvn -version
```

#### 步骤 3：配置自动激活（可选）

**方法 A：使用 direnv（推荐）**

```bash
# 安装 direnv
brew install direnv  # macOS

# 配置 shell（添加到 ~/.zshrc 或 ~/.bashrc）
eval "$(direnv hook zsh)"

# 在项目根目录创建 .envrc 文件
echo 'eval "$(sdk env)"' > .envrc

# 允许 direnv
direnv allow
```

**效果：**
- 进入项目目录时自动切换 Java 版本
- 离开项目目录时自动恢复默认版本

**方法 B：使用 shell 别名**

```bash
# 添加到 ~/.zshrc 或 ~/.bashrc
alias cd='cd_with_sdk() { builtin cd "$@" && [ -f .sdkmanrc ] && sdk env; }; cd_with_sdk'

# 重新加载配置
source ~/.zshrc
```

---

### 方法 2：使用 .java-version 文件（兼容其他工具）

除了 `.sdkmanrc`，SDKMAN 也支持 `.java-version` 文件（兼容 jenv、asdf 等工具）：

```bash
# 在项目根目录创建 .java-version 文件
echo "17.0.2-tem" > .java-version

# 激活
sdk env
```

**文件内容：**
```
17.0.2-tem
```

---

### 方法 3：Maven 项目中的 Java 版本配置

在 Maven 项目中，可以在 `pom.xml` 中指定 Java 版本：

```xml
<properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    <java.version>17</java.version>
</properties>
```

**配合 SDKMAN 使用：**

```bash
# 1. 在项目根目录创建 .sdkmanrc
echo "java=17.0.2-tem" > .sdkmanrc

# 2. 激活项目配置
sdk env

# 3. 验证
java -version  # 应该显示 Java 17
mvn compile    # Maven 会使用 Java 17 编译
```

---

### 方法 4：Gradle 项目中的 Java 版本配置

在 Gradle 项目中，可以在 `build.gradle` 中指定 Java 版本：

```groovy
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
```

**配合 SDKMAN 使用：**

```bash
# 1. 在项目根目录创建 .sdkmanrc
echo "java=17.0.2-tem" > .sdkmanrc
echo "gradle=8.4" >> .sdkmanrc

# 2. 激活项目配置
sdk env

# 3. 验证
java -version
gradle -version
```

---

### 实际使用示例

#### 场景：同时开发多个项目

```bash
# 项目 A：使用 Java 11
cd ~/projects/old-project
echo "java=11.0.19-tem" > .sdkmanrc
sdk env
java -version  # 显示 Java 11

# 项目 B：使用 Java 17
cd ~/projects/new-project
echo "java=17.0.2-tem" > .sdkmanrc
sdk env
java -version  # 显示 Java 17

# 项目 C：使用 Java 21
cd ~/projects/latest-project
echo "java=21.0.1-tem" > .sdkmanrc
sdk env
java -version  # 显示 Java 21
```

#### 场景：团队协作

```bash
# 1. 在项目根目录创建 .sdkmanrc
cat > .sdkmanrc << EOF
java=17.0.2-tem
maven=3.9.4
EOF

# 2. 将 .sdkmanrc 提交到 Git（团队成员共享配置）
git add .sdkmanrc
git commit -m "Add SDKMAN configuration"
git push

# 3. 团队成员克隆项目后
git clone https://github.com/team/project.git
cd project
sdk env  # 自动切换到项目指定的 Java 版本
```

---

### IDE 中的项目级别 Java 版本配置

#### IntelliJ IDEA

1. **项目级别配置：**
   - File → Project Structure → Project
   - 设置 "SDK" 和 "Language level"

2. **模块级别配置：**
   - File → Project Structure → Modules
   - 为每个模块设置 "Language level"

3. **Maven 项目：**
   - IDEA 会自动读取 `pom.xml` 中的 Java 版本配置
   - 配合 SDKMAN 使用更顺畅

#### VS Code

1. **安装扩展：**
   - Java Extension Pack
   - Language Support for Java

2. **配置：**
   - 在项目根目录创建 `.vscode/settings.json`
   ```json
   {
     "java.configuration.runtimes": [
       {
         "name": "JavaSE-17",
         "path": "/Users/你的用户名/.sdkman/candidates/java/17.0.2-tem"
       }
     ],
     "java.jdt.ls.java.home": "/Users/你的用户名/.sdkman/candidates/java/17.0.2-tem"
   }
   ```

---

### 验证和调试

```bash
# 1. 查看当前项目配置
cat .sdkmanrc

# 2. 查看当前使用的 Java 版本
sdk current java

# 3. 查看 Java 路径
which java
echo $JAVA_HOME

# 4. 验证 Maven 使用的 Java 版本
mvn -version

# 5. 验证 Gradle 使用的 Java 版本
gradle -version
```

---

### 常见问题

**Q: 为什么 `sdk env` 后版本没有切换？**

**A:** 检查以下几点：
```bash
# 1. 确认 .sdkmanrc 文件存在
ls -la .sdkmanrc

# 2. 确认文件内容正确
cat .sdkmanrc

# 3. 确认指定的版本已安装
sdk list java | grep installed

# 4. 重新执行 sdk env
sdk env
```

**Q: 如何为不同项目设置不同的 Maven 版本？**

**A:** 在 `.sdkmanrc` 中同时指定：
```
java=17.0.2-tem
maven=3.9.4
```

**Q: 离开项目目录后如何恢复默认版本？**

**A:** 
```bash
# 方法 1：手动切换
sdk default java 17.0.2-tem

# 方法 2：使用 direnv（自动恢复）
# 配置 direnv 后，离开目录会自动恢复
```

**Q: 可以在 .sdkmanrc 中使用通配符吗？**

**A:** 可以，但建议使用完整版本号：
```
# 可以使用
java=17.0.2-tem

# 也可以使用（但可能不稳定）
java=17-tem
```

---

### 最佳实践

1. **为每个项目创建 .sdkmanrc**
   - 明确指定项目需要的 Java 版本
   - 提交到 Git，团队成员共享

2. **使用 direnv 自动切换**
   - 进入项目自动切换版本
   - 离开项目自动恢复默认

3. **在 pom.xml/build.gradle 中也指定版本**
   - 双重保障，确保版本一致
   - IDE 可以正确识别

4. **定期更新 .sdkmanrc**
   - 项目升级 Java 版本时同步更新
   - 保持配置一致性

#### 常见问题

**Q: 为什么安装后 `java -version` 还是显示旧版本？**

**A:** 需要重新加载 shell 或重启终端：
```bash
# 重新加载配置
source "$HOME/.sdkman/bin/sdkman-init.sh"

# 或者关闭并重新打开终端
```

**Q: 如何确认使用的是 SDKMAN 管理的 Java？**

**A:** 检查路径：
```bash
which java
# 应该显示：~/.sdkman/candidates/java/current/bin/java

# 如果显示系统路径（如 /usr/bin/java），说明没有使用 SDKMAN 的版本
```

**Q: 可以同时使用系统 Java 和 SDKMAN 的 Java 吗？**

**A:** 可以，但 SDKMAN 的 Java 优先级更高（在 PATH 前面）：
```bash
# 查看所有 Java
/usr/bin/java -version  # 系统 Java
java -version           # SDKMAN 的 Java（优先）
```

---

## 🛠️ 管理其他工具

SDKMAN 不仅可以管理 Java，还可以管理其他开发工具：

### Maven

```bash
# 查看可用版本
sdk list maven

# 安装 Maven
sdk install maven 3.9.4

# 切换版本
sdk use maven 3.9.4

# 设置默认
sdk default maven 3.9.4
```

### Gradle

```bash
# 查看可用版本
sdk list gradle

# 安装 Gradle
sdk install gradle 8.4

# 切换版本
sdk use gradle 8.4
```

### Spring Boot CLI

```bash
# 安装 Spring Boot CLI
sdk install springboot

# 查看版本
spring --version
```

### 其他可用工具

```bash
# 查看所有可用的工具
sdk list

# 常用工具包括：
# - Ant
# - Groovy
# - Kotlin
# - Scala
# - SBT
# - Vert.x
# - Micronaut
# - Quarkus
# 等等...
```

---

## 📋 常用命令速查

### 基础命令

| 命令 | 说明 | 示例 |
|------|------|------|
| `sdk version` | 查看 SDKMAN 版本 | `sdk version` |
| `sdk list` | 列出所有可用工具 | `sdk list` |
| `sdk list <candidate>` | 列出指定工具的版本 | `sdk list java` |
| `sdk install <candidate> <version>` | 安装指定版本 | `sdk install java 17.0.2-tem` |
| `sdk uninstall <candidate> <version>` | 卸载指定版本 | `sdk uninstall java 11.0.19-tem` |
| `sdk use <candidate> <version>` | 临时切换版本 | `sdk use java 17.0.2-tem` |
| `sdk default <candidate> <version>` | 设置默认版本 | `sdk default java 17.0.2-tem` |
| `sdk current <candidate>` | 查看当前版本 | `sdk current java` |
| `sdk upgrade <candidate>` | 升级到最新版本 | `sdk upgrade java` |
| `sdk upgrade` | 升级 SDKMAN 本身 | `sdk upgrade` |

### 高级命令

```bash
# 查看某个工具的详细信息
sdk info java

# 查看某个版本的详细信息
sdk info java 17.0.2-tem

# 刷新候选列表（从服务器获取最新信息）
sdk flush candidates

# 刷新广播消息
sdk flush broadcast

# 查看 SDKMAN 配置
sdk config

# 离线模式（不检查更新）
sdk offline enable
sdk offline disable
```

---

## 🎯 实际使用场景

### 场景 1：项目需要不同 Java 版本

```bash
# 项目 A 需要 Java 11
cd ~/project-a
sdk use java 11.0.19-tem

# 项目 B 需要 Java 17
cd ~/project-b
sdk use java 17.0.2-tem
```

### 场景 2：测试不同 Java 版本

```bash
# 测试 Java 11
sdk use java 11.0.19-tem
java -version
javac -version

# 测试 Java 17
sdk use java 17.0.2-tem
java -version
javac -version
```

### 场景 3：管理 Maven 和 Gradle

```bash
# 安装 Maven
sdk install maven 3.9.4
sdk default maven 3.9.4

# 安装 Gradle
sdk install gradle 8.4
sdk default gradle 8.4

# 切换使用
sdk use maven 3.9.4
mvn --version

sdk use gradle 8.4
gradle --version
```

---

## 🔧 配置和自定义

### 查看配置

```bash
# 查看 SDKMAN 配置
cat ~/.sdkman/etc/config
```

### 修改安装路径

默认安装路径是 `~/.sdkman`，可以通过环境变量修改：

```bash
# 在 ~/.zshrc 或 ~/.bashrc 中添加
export SDKMAN_DIR="/custom/path/to/.sdkman"

# 然后重新安装 SDKMAN
```

### 代理设置

如果网络访问受限，可以配置代理：

```bash
# 在 ~/.sdkman/etc/config 中设置
sdkman_proxy_host=proxy.example.com
sdkman_proxy_port=8080
```

### 自动完成（Auto-completion）

SDKMAN 支持 shell 自动完成：

```bash
# Zsh
echo '. "$HOME/.sdkman/bin/sdkman-init.sh"' >> ~/.zshrc
source ~/.zshrc

# Bash
echo '. "$HOME/.sdkman/bin/sdkman-init.sh"' >> ~/.bashrc
source ~/.bashrc
```

---

## 🆚 与 nvm 对比（前端程序员参考）

| 功能 | nvm (Node.js) | SDKMAN (Java) |
|------|---------------|---------------|
| **安装工具** | `curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh \| bash` | `curl -s "https://get.sdkman.io" \| bash` |
| **列出可用版本** | `nvm list-remote` | `sdk list java` |
| **列出已安装版本** | `nvm list` | `sdk list java \| grep installed` |
| **安装版本** | `nvm install 18` | `sdk install java 17.0.2-tem` |
| **切换版本** | `nvm use 18` | `sdk use java 17.0.2-tem` |
| **设置默认版本** | `nvm alias default 18` | `sdk default java 17.0.2-tem` |
| **查看当前版本** | `nvm current` | `sdk current java` |
| **卸载版本** | `nvm uninstall 16` | `sdk uninstall java 11.0.19-tem` |
| **升级工具本身** | `nvm install-latest-npm` | `sdk upgrade` |

---

## ❓ 常见问题

### Q1: 安装后提示 "command not found"

**解决方案：**
```bash
# 重新加载 shell 配置
source "$HOME/.sdkman/bin/sdkman-init.sh"

# 或者重启终端
# 检查 ~/.zshrc 或 ~/.bashrc 中是否有 SDKMAN 初始化代码
```

### Q2: 如何卸载 SDKMAN？

```bash
# 卸载 SDKMAN
rm -rf "$HOME/.sdkman"

# 从 shell 配置文件中删除相关代码
# 编辑 ~/.zshrc 或 ~/.bashrc，删除以下行：
# export SDKMAN_DIR="$HOME/.sdkman"
# [[ -s "$HOME/.sdkman/bin/sdkman-init.sh" ]] && source "$HOME/.sdkman/bin/sdkman-init.sh"
```

### Q3: 安装速度慢怎么办？

```bash
# 使用国内镜像（如果有）
# 或者配置代理
export SDKMAN_PROXY_HOST=proxy.example.com
export SDKMAN_PROXY_PORT=8080
```

### Q4: 如何查看已安装的所有工具？

```bash
# 查看所有已安装的工具和版本
sdk current

# 或者
ls ~/.sdkman/candidates/
```

### Q5: 如何在 CI/CD 中使用？

```bash
# 在 CI 脚本中安装和使用
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 17.0.2-tem
sdk use java 17.0.2-tem
java -version
```

---

## 📚 更多资源

- **官方网站**：https://sdkman.io/
- **GitHub**：https://github.com/sdkman/sdkman-cli
- **文档**：https://sdkman.io/usage
- **候选列表**：https://sdkman.io/sdks

---

## 💡 最佳实践

1. **使用 LTS 版本作为默认**
   ```bash
   sdk default java 17.0.2-tem  # Java 17 是当前推荐的 LTS
   ```

2. **项目级别切换**
   - 在项目根目录创建 `.sdkmanrc` 文件
   ```bash
   # .sdkmanrc
   java=17.0.2-tem
   maven=3.9.4
   ```
   - 进入项目时自动切换：`sdk env`

3. **定期更新**
   ```bash
   # 更新 SDKMAN 本身
   sdk upgrade
   
   # 更新所有工具到最新版本
   sdk upgrade java
   sdk upgrade maven
   ```

4. **清理不需要的版本**
   ```bash
   # 定期清理旧版本，节省磁盘空间
   sdk uninstall java 11.0.19-tem
   ```

---

