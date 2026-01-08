# Java 基础命令说明

## 📝 java 和 javac 的区别

作为前端程序员，你可能熟悉 JavaScript 的运行方式。Java 的编译和运行过程与 JavaScript 不同，需要理解 `java` 和 `javac` 这两个核心命令。

---

## 🔑 核心概念

### javac（Java Compiler - Java 编译器）

**作用：** 将 `.java` 源文件编译成 `.class` 字节码文件

**类比前端：**
- 类似于 TypeScript 的 `tsc`（TypeScript Compiler）
- 类似于 Babel 的编译过程
- 将源代码转换为可执行的字节码

### java（Java Runtime - Java 运行时）

**作用：** 运行编译后的 `.class` 字节码文件

**类比前端：**
- 类似于 Node.js 运行 JavaScript 文件：`node app.js`
- 类似于浏览器执行 JavaScript 代码
- 执行已编译的字节码

---

## 📊 对比表

| 命令 | 作用 | 输入 | 输出 | 前端类比 |
|------|------|------|------|----------|
| **javac** | 编译源代码 | `.java` 文件 | `.class` 字节码文件 | `tsc` 编译 TypeScript |
| **java** | 运行程序 | `.class` 文件 | 程序执行结果 | `node` 运行 JavaScript |

---

## 🔄 Java 程序执行流程

```
.java 源文件  →  javac 编译  →  .class 字节码  →  java 运行  →  程序输出
```

**详细步骤：**

1. **编写源代码**：创建 `.java` 文件
   ```java
   // HelloWorld.java
   public class HelloWorld {
       public static void main(String[] args) {
           System.out.println("Hello, World!");
       }
   }
   ```

2. **编译源代码**：使用 `javac` 编译
   ```bash
   javac HelloWorld.java
   ```
   - 生成：`HelloWorld.class`（字节码文件）

3. **运行程序**：使用 `java` 运行
   ```bash
   java HelloWorld
   ```
   - 注意：不需要 `.class` 扩展名
   - 输出：`Hello, World!`

---

## 💻 详细使用说明

### javac 命令

#### 基本用法

```bash
# 编译单个文件
javac HelloWorld.java

# 编译多个文件
javac File1.java File2.java File3.java

# 编译当前目录所有 .java 文件
javac *.java

# 指定输出目录
javac -d ./out HelloWorld.java
```

#### 常用选项

```bash
# -d：指定输出目录
javac -d ./classes HelloWorld.java

# -cp 或 -classpath：指定类路径
javac -cp ./lib/*.jar HelloWorld.java

# -encoding：指定源文件编码
javac -encoding UTF-8 HelloWorld.java

# -version：显示编译器版本
javac -version

# -help：显示帮助信息
javac -help
```

#### 实际示例

```bash
# 编译并指定输出目录
javac -d ./target HelloWorld.java

# 编译时包含外部库
javac -cp ./lib/gson-2.8.9.jar Main.java

# 编译多个文件（有依赖关系）
javac -d ./out src/com/example/*.java
```

### java 命令

#### 基本用法

```bash
# 运行主类（不需要 .class 扩展名）
java HelloWorld

# 运行带包名的类
java com.example.HelloWorld

# 传递命令行参数
java HelloWorld arg1 arg2 arg3
```

#### 常用选项

```bash
# -cp 或 -classpath：指定类路径
java -cp ./lib/*.jar HelloWorld

# -jar：运行 JAR 文件
java -jar myapp.jar

# -version：显示 Java 版本
java -version

# -Xmx：设置最大堆内存
java -Xmx512m HelloWorld

# -Xms：设置初始堆内存
java -Xms256m HelloWorld

# -D：设置系统属性
java -Dkey=value HelloWorld
```

#### 实际示例

```bash
# 运行主类
java HelloWorld

# 运行带包名的类
java -cp ./out com.example.Main

# 运行 JAR 文件
java -jar target/myapp.jar

# 设置内存和系统属性
java -Xmx1g -Xms512m -Dconfig.file=app.properties Main
```

---

## 🆚 前端 vs Java 执行流程对比

### JavaScript（前端）

```bash
# 直接运行（无需编译）
node app.js

# 或者浏览器直接执行
# <script src="app.js"></script>
```

**流程：**
```
app.js → 直接执行 → 输出
```

### TypeScript（需要编译）

```bash
# 1. 编译
tsc app.ts

# 2. 运行编译后的 JavaScript
node app.js
```

**流程：**
```
app.ts → tsc 编译 → app.js → node 运行 → 输出
```

### Java（必须编译）

```bash
# 1. 编译
javac App.java

# 2. 运行
java App
```

**流程：**
```
App.java → javac 编译 → App.class → java 运行 → 输出
```

---

## 📝 完整示例

### 示例 1：Hello World

**1. 创建源文件 `HelloWorld.java`：**

```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        System.out.println("参数数量: " + args.length);
        
        for (int i = 0; i < args.length; i++) {
            System.out.println("参数 " + i + ": " + args[i]);
        }
    }
}
```

**2. 编译：**

```bash
javac HelloWorld.java
```

**3. 运行：**

```bash
java HelloWorld
# 输出: Hello, World!
# 输出: 参数数量: 0

java HelloWorld 张三 李四
# 输出: Hello, World!
# 输出: 参数数量: 2
# 输出: 参数 0: 张三
# 输出: 参数 1: 李四
```

### 示例 2：带包名的类

**1. 创建目录结构：**

```
com/
  example/
    Main.java
```

**2. 创建源文件 `com/example/Main.java`：**

```java
package com.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello from package!");
    }
}
```

**3. 编译：**

```bash
# 在项目根目录执行
javac com/example/Main.java
```

**4. 运行：**

```bash
# 在项目根目录执行
java com.example.Main
```

---

## ⚠️ 常见错误和解决方案

### 错误 1：找不到或无法加载主类

**错误信息：**
```
错误: 找不到或无法加载主类 HelloWorld
```

**原因：**
- 类名与文件名不匹配
- 包名问题
- 类路径问题

**解决方案：**
```bash
# 检查类名和文件名是否一致（区分大小写）
# HelloWorld.java 中的类名必须是 public class HelloWorld

# 如果有包名，需要从包外运行
java com.example.HelloWorld

# 或者指定类路径
java -cp . HelloWorld
```

### 错误 2：找不到符号

**错误信息：**
```
错误: 找不到符号
```

**原因：**
- 缺少依赖的类文件
- 类路径配置错误

**解决方案：**
```bash
# 编译所有相关文件
javac *.java

# 或者指定类路径
javac -cp ./lib/*.jar Main.java
```

### 错误 3：编码问题

**错误信息：**
```
错误: 编码 GBK 的不可映射字符
```

**解决方案：**
```bash
# 指定 UTF-8 编码编译
javac -encoding UTF-8 HelloWorld.java
```

### 错误 4：运行 java 时加了 .class 扩展名

**错误：**
```bash
java HelloWorld.class  # ❌ 错误
```

**正确：**
```bash
java HelloWorld  # ✅ 正确
```

---

## 🛠️ 其他相关命令

### javap（Java 反编译器）

查看 `.class` 文件的内容：

```bash
# 查看类的结构
javap HelloWorld

# 显示详细信息
javap -c HelloWorld

# 显示所有成员
javap -private HelloWorld
```

### jar（Java 归档工具）

打包 `.class` 文件为 JAR：

```bash
# 创建 JAR 文件
jar cvf myapp.jar *.class

# 查看 JAR 内容
jar tf myapp.jar

# 解压 JAR
jar xf myapp.jar
```

### jps（Java 进程查看工具）

查看正在运行的 Java 进程：

```bash
# 列出所有 Java 进程
jps

# 显示详细信息
jps -l -v
```

---

## 📚 快速参考

### javac 常用命令

```bash
javac [选项] <源文件>
  -d <目录>          指定输出目录
  -cp <路径>         指定类路径
  -encoding <编码>   指定源文件编码
  -version           显示版本
  -help              显示帮助
```

### java 常用命令

```bash
java [选项] <主类> [参数...]
  -cp <路径>         指定类路径
  -jar <jar文件>     运行 JAR 文件
  -Xmx<大小>         最大堆内存
  -Xms<大小>         初始堆内存
  -D<key>=<value>    系统属性
  -version           显示版本
```

---

## 💡 最佳实践

1. **使用构建工具**
   - 对于复杂项目，使用 Maven 或 Gradle
   - 自动处理编译、依赖和打包

2. **IDE 集成**
   - IntelliJ IDEA、Eclipse 等 IDE 会自动处理编译
   - 但理解底层命令有助于调试

3. **项目结构**
   ```
   project/
     src/
       main/
         java/
           com/
             example/
               Main.java
     target/
       classes/
         com/
           example/
             Main.class
   ```

4. **环境变量**
   - `JAVA_HOME`：JDK 安装路径
   - `PATH`：包含 `$JAVA_HOME/bin`

---

## 🎓 总结

- **javac**：编译器，将 `.java` 编译成 `.class`
- **java**：运行时，执行 `.class` 字节码
- **流程**：编写 → 编译 → 运行
- **类比**：类似 TypeScript 的 `tsc` + `node` 的组合

理解这两个命令是学习 Java 的基础，就像理解 `node` 对于 JavaScript 一样重要！

---


