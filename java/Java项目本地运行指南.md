# Java 项目本地运行指南

## 📋 目录

- [方式 1：使用 Maven 直接运行（最简单）](#方式-1使用-maven-直接运行最简单)
- [方式 2：编译后运行](#方式-2编译后运行)
- [方式 3：打包后运行 JAR](#方式-3打包后运行-jar)
- [方式 4：使用 IDE 运行](#方式-4使用-ide-运行)
- [常见问题](#常见问题)

---

## 🚀 方式 1：使用 Maven 直接运行（最简单）

### 前置条件

确保已安装 Maven 并配置好 Java 环境：

```bash
# 检查 Java
java -version

# 检查 Maven（如果使用 SDKMAN）
source "$HOME/.sdkman/bin/sdkman-init.sh"
mvn -version
```

### 运行步骤

```bash
# 1. 进入项目目录
cd /Users/edenhuang/Desktop/脚本/java-project

# 2. 直接运行（Maven 会自动编译并运行）
mvn exec:java

# 或指定主类
mvn exec:java -Dexec.mainClass="com.hry.firstjava.App"
```

**优点：**
- ✅ 最简单，一条命令搞定
- ✅ 自动编译（如果代码有变化）
- ✅ 不需要手动管理 classpath

**输出示例：**
```
[INFO] Scanning for projects...
[INFO] 
[INFO] --- exec:java (default-cli) @ firstjava ---
Hello, World!
欢迎使用 Java 项目！
```

---

## 🔨 方式 2：编译后运行

### 步骤 1：编译项目

```bash
# 进入项目目录
cd /Users/edenhuang/Desktop/脚本/java-project

# 编译项目
mvn compile

# 或清理后编译
mvn clean compile
```

**编译后的文件位置：**
```
target/classes/
└── com/
    └── hry/
        └── firstjava/
            └── App.class
```

### 步骤 2：运行编译后的类

```bash
# 方式 A：使用 classpath 运行
java -cp target/classes com.hry.firstjava.App

# 方式 B：进入 classes 目录运行
cd target/classes
java com.hry.firstjava.App
cd ../..
```

**优点：**
- ✅ 快速，不需要打包
- ✅ 适合开发调试
- ✅ 可以看到编译过程

**缺点：**
- ❌ 需要手动指定 classpath
- ❌ 如果有依赖，需要手动添加

---

## 📦 方式 3：打包后运行 JAR

### 步骤 1：打包项目

```bash
# 进入项目目录
cd /Users/edenhuang/Desktop/脚本/java-project

# 打包（跳过测试）
mvn clean package -DskipTests

# 或完整打包（包含测试）
mvn clean package
```

**打包后的文件：**
```
target/firstjava-1.0-SNAPSHOT.jar
```

### 步骤 2：运行 JAR 文件

```bash
# 方式 A：使用 -jar（需要配置 Main-Class）
java -jar target/firstjava-1.0-SNAPSHOT.jar

# 方式 B：使用 -cp（不需要 Main-Class）
java -cp target/firstjava-1.0-SNAPSHOT.jar com.hry.firstjava.App
```

**优点：**
- ✅ 最接近生产环境
- ✅ 一个文件包含所有内容
- ✅ 方便分发和部署

**缺点：**
- ❌ 需要先打包（稍慢）

---

## 💻 方式 4：使用 IDE 运行

### IntelliJ IDEA

1. **打开项目**
   - File → Open → 选择项目目录

2. **运行主类**
   - 右键点击 `App.java`
   - 选择 "Run 'App.main()'"
   - 或点击类名旁边的绿色运行按钮 ▶️

3. **配置运行参数**
   - Run → Edit Configurations
   - 可以设置程序参数、JVM 参数等

### VS Code

1. **安装扩展**
   - Java Extension Pack
   - 包括：Language Support for Java、Debugger for Java 等

2. **运行项目**
   - 打开 `App.java`
   - 点击 `main` 方法上方的 "Run" 按钮
   - 或按 `F5` 调试运行

3. **使用终端**
   - 打开集成终端（Ctrl+`）
   - 使用 Maven 命令运行

### Eclipse

1. **导入项目**
   - File → Import → Existing Maven Projects

2. **运行项目**
   - 右键点击 `App.java`
   - Run As → Java Application

---

## 🎯 完整运行示例

### 示例 1：快速运行（开发时）

```bash
cd /Users/edenhuang/Desktop/脚本/java-project

# 最简单的方式
mvn exec:java
```

### 示例 2：完整流程（学习用）

```bash
cd /Users/edenhuang/Desktop/脚本/java-project

# 1. 清理
mvn clean

# 2. 编译
mvn compile

# 3. 运行
java -cp target/classes com.hry.firstjava.App
```

### 示例 3：打包运行（生产模拟）

```bash
cd /Users/edenhuang/Desktop/脚本/java-project

# 1. 打包
mvn clean package -DskipTests

# 2. 运行
java -jar target/firstjava-1.0-SNAPSHOT.jar
```

---

## 🔧 带参数运行

### 传递命令行参数

```java
// App.java
public static void main(String[] args) {
    if (args.length > 0) {
        System.out.println("参数: " + args[0]);
    }
}
```

**运行方式：**

```bash
# Maven 方式
mvn exec:java -Dexec.args="参数1 参数2"

# 直接运行
java -cp target/classes com.hry.firstjava.App 参数1 参数2

# JAR 方式
java -jar target/firstjava-1.0-SNAPSHOT.jar 参数1 参数2
```

---

## 📊 运行方式对比

| 方式 | 命令 | 优点 | 缺点 | 适用场景 |
|------|------|------|------|---------|
| **Maven exec** | `mvn exec:java` | 最简单，自动编译 | 需要 Maven | 开发调试 |
| **编译后运行** | `java -cp target/classes ...` | 快速，不需要打包 | 需要手动管理 classpath | 快速测试 |
| **JAR 运行** | `java -jar app.jar` | 接近生产环境 | 需要先打包 | 生产模拟 |
| **IDE 运行** | 点击运行按钮 | 可视化，调试方便 | 需要 IDE | 开发调试 |

---

## ⚙️ 配置项目 Java 版本

### 使用 SDKMAN（推荐）

```bash
# 进入项目目录
cd /Users/edenhuang/Desktop/脚本/java-project

# 激活项目配置（如果项目有 .sdkmanrc）
sdk env

# 验证 Java 版本
java -version
```

### 检查项目配置

```bash
# 查看 pom.xml 中的 Java 版本
grep "maven.compiler" pom.xml

# 应该看到：
# <maven.compiler.source>17</maven.compiler.source>
# <maven.compiler.target>17</maven.compiler.target>
```

---

## 🐛 常见问题

### Q1: 找不到主类

**错误信息：**
```
错误: 找不到或无法加载主类 com.hry.firstjava.App
```

**解决方法：**
```bash
# 1. 检查类文件是否存在
ls -la target/classes/com/hry/firstjava/App.class

# 2. 重新编译
mvn clean compile

# 3. 检查包名是否正确
# 确保 App.java 中的 package 声明正确
```

### Q2: Maven 命令不存在

**错误信息：**
```
command not found: mvn
```

**解决方法：**
```bash
# 使用 SDKMAN 加载 Maven
source "$HOME/.sdkman/bin/sdkman-init.sh"

# 或安装 Maven
sdk install maven
```

### Q3: Java 版本不匹配

**错误信息：**
```
错误: 无法访问 App (错误的类文件: ... 需要 class file version 61.0)
```

**解决方法：**
```bash
# 检查 Java 版本
java -version

# 应该使用 Java 17（对应 class file version 61.0）
# 如果版本不对，使用 SDKMAN 切换
sdk use java 17.0.2-tem
```

### Q4: 依赖找不到

**错误信息：**
```
Exception in thread "main" java.lang.NoClassDefFoundError: ...
```

**解决方法：**
```bash
# 如果项目有依赖，需要：
# 1. 下载依赖
mvn dependency:resolve

# 2. 或使用 Fat JAR（包含所有依赖）
# 配置 maven-shade-plugin
```

---

## 💡 最佳实践

### 开发阶段

```bash
# 推荐：使用 Maven exec
mvn exec:java

# 或使用 IDE 运行（更方便调试）
```

### 测试阶段

```bash
# 编译并运行测试
mvn test

# 或运行特定测试
mvn test -Dtest=AppTest
```

### 生产模拟

```bash
# 打包并运行
mvn clean package -DskipTests
java -jar target/firstjava-1.0-SNAPSHOT.jar
```

---

## 📝 快速参考

### 常用命令

```bash
# 编译
mvn compile

# 运行
mvn exec:java

# 打包
mvn package

# 运行 JAR
java -jar target/firstjava-1.0-SNAPSHOT.jar

# 清理
mvn clean
```

### 当前项目运行

```bash
# 进入项目
cd /Users/edenhuang/Desktop/脚本/java-project

# 最简单的方式
mvn exec:java

# 或打包后运行
mvn clean package -DskipTests && java -jar target/firstjava-1.0-SNAPSHOT.jar
```

---

## 🎓 总结

### 推荐运行方式

1. **开发调试**：`mvn exec:java` 或使用 IDE
2. **快速测试**：`mvn compile && java -cp target/classes ...`
3. **生产模拟**：`mvn package && java -jar app.jar`

### 学习路径

1. 先学会 `mvn exec:java`（最简单）
2. 理解编译和运行的过程
3. 掌握打包和 JAR 运行
4. 熟悉 IDE 的使用

---

**祝开发顺利！💪**

*最后更新：2024年*
