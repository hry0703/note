# Java 文件 I/O 操作详解

## 📚 目录

1. [文件 I/O 基础概念](#文件-io-基础概念)
2. [字符流操作](#字符流操作)
3. [字节流操作](#字节流操作)
4. [文件操作工具类](#文件操作工具类)
5. [文件读写最佳实践](#文件读写最佳实践)
6. [常见文件操作场景](#常见文件操作场景)

---

## 文件 I/O 基础概念

### 什么是文件 I/O？

**文件 I/O（Input/Output）**是指程序与文件系统之间的数据交换操作，包括：
- **读取（Read）**：从文件读取数据到程序
- **写入（Write）**：将程序中的数据写入文件

### 流的分类

#### 1. 按数据类型分类

- **字符流（Character Stream）**：处理文本文件（如 .txt、.java）
  - `Reader`、`Writer` 及其子类
- **字节流（Byte Stream）**：处理二进制文件（如 .jpg、.mp4、.class）
  - `InputStream`、`OutputStream` 及其子类

#### 2. 按功能分类

- **输入流（Input Stream）**：从文件读取数据
- **输出流（Output Stream）**：向文件写入数据

### 流的层次结构

```
字符流：
Reader
├── FileReader（文件字符输入流）
├── BufferedReader（缓冲字符输入流）
└── InputStreamReader（字符输入流转换器）

Writer
├── FileWriter（文件字符输出流）
├── BufferedWriter（缓冲字符输出流）
└── OutputStreamWriter（字符输出流转换器）

字节流：
InputStream
├── FileInputStream（文件字节输入流）
├── BufferedInputStream（缓冲字节输入流）
└── ObjectInputStream（对象输入流）

OutputStream
├── FileOutputStream（文件字节输出流）
├── BufferedOutputStream（缓冲字节输出流）
└── ObjectOutputStream（对象输出流）
```

---

## 字符流操作

### 1. FileReader（文件字符输入流）

用于读取文本文件。

#### 基本用法

```java
import java.io.FileReader;
import java.io.IOException;

// 方式 1：逐个字符读取
FileReader reader = new FileReader("file.txt");
int ch;
while ((ch = reader.read()) != -1) {
    System.out.print((char) ch);
}
reader.close();

// 方式 2：读取到字符数组
FileReader reader = new FileReader("file.txt");
char[] buffer = new char[1024];
int length;
while ((length = reader.read(buffer)) != -1) {
    System.out.print(new String(buffer, 0, length));
}
reader.close();
```

#### 使用 try-with-resources（推荐）

```java
// ✅ 推荐：自动关闭资源
try (FileReader reader = new FileReader("file.txt")) {
    int ch;
    while ((ch = reader.read()) != -1) {
        System.out.print((char) ch);
    }
} catch (IOException e) {
    System.out.println("文件读取失败：" + e.getMessage());
}
```

### 2. FileWriter（文件字符输出流）

用于写入文本文件。

#### 基本用法

```java
import java.io.FileWriter;
import java.io.IOException;

// 方式 1：写入字符串
try (FileWriter writer = new FileWriter("output.txt")) {
    writer.write("Hello, World!");
    writer.write("\n这是第二行");
} catch (IOException e) {
    System.out.println("文件写入失败：" + e.getMessage());
}

// 方式 2：追加模式
try (FileWriter writer = new FileWriter("output.txt", true)) {
    writer.write("\n追加的内容");
} catch (IOException e) {
    System.out.println("文件写入失败：" + e.getMessage());
}
```

### 3. BufferedReader（缓冲字符输入流）

提供缓冲功能，提高读取效率。

```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// ✅ 推荐：使用 BufferedReader 提高效率
try (BufferedReader reader = new BufferedReader(new FileReader("file.txt"))) {
    String line;
    while ((line = reader.readLine()) != null) {
        System.out.println(line);
    }
} catch (IOException e) {
    System.out.println("文件读取失败：" + e.getMessage());
}
```

**优势**：
- 提高读取效率（减少系统调用）
- 提供 `readLine()` 方法，方便按行读取

### 4. BufferedWriter（缓冲字符输出流）

提供缓冲功能，提高写入效率。

```java
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

// ✅ 推荐：使用 BufferedWriter 提高效率
try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
    writer.write("第一行");
    writer.newLine();  // 换行
    writer.write("第二行");
    writer.flush();  // 刷新缓冲区（可选）
} catch (IOException e) {
    System.out.println("文件写入失败：" + e.getMessage());
}
```

---

## 字节流操作

### 1. FileInputStream（文件字节输入流）

用于读取二进制文件。

```java
import java.io.FileInputStream;
import java.io.IOException;

try (FileInputStream input = new FileInputStream("image.jpg")) {
    byte[] buffer = new byte[1024];
    int length;
    while ((length = input.read(buffer)) != -1) {
        // 处理字节数据
        System.out.println("读取了 " + length + " 字节");
    }
} catch (IOException e) {
    System.out.println("文件读取失败：" + e.getMessage());
}
```

### 2. FileOutputStream（文件字节输出流）

用于写入二进制文件。

```java
import java.io.FileOutputStream;
import java.io.IOException;

try (FileOutputStream output = new FileOutputStream("output.dat")) {
    byte[] data = "Hello, World!".getBytes();
    output.write(data);
} catch (IOException e) {
    System.out.println("文件写入失败：" + e.getMessage());
}
```

### 3. BufferedInputStream 和 BufferedOutputStream

提供缓冲功能，提高效率。

```java
import java.io.*;

try (
    BufferedInputStream input = new BufferedInputStream(
        new FileInputStream("input.dat"));
    BufferedOutputStream output = new BufferedOutputStream(
        new FileOutputStream("output.dat"))
) {
    byte[] buffer = new byte[1024];
    int length;
    while ((length = input.read(buffer)) != -1) {
        output.write(buffer, 0, length);
    }
} catch (IOException e) {
    System.out.println("文件操作失败：" + e.getMessage());
}
```

---

## 文件操作工具类

### 1. Files 类（Java 7+，推荐）

`java.nio.file.Files` 提供了便捷的文件操作方法。

#### 读取文件

```java
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.IOException;
import java.util.List;

// 读取所有行
Path path = Paths.get("file.txt");
List<String> lines = Files.readAllLines(path);
for (String line : lines) {
    System.out.println(line);
}

// 读取所有字节
byte[] bytes = Files.readAllBytes(path);

// 读取为字符串
String content = Files.readString(path);  // Java 11+
```

#### 写入文件

```java
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.IOException;
import java.util.Arrays;

Path path = Paths.get("output.txt");

// 写入字符串
Files.writeString(path, "Hello, World!");  // Java 11+

// 写入多行
List<String> lines = Arrays.asList("第一行", "第二行", "第三行");
Files.write(path, lines);

// 写入字节
byte[] data = "Hello".getBytes();
Files.write(path, data);
```

#### 其他常用方法

```java
// 检查文件是否存在
boolean exists = Files.exists(path);

// 创建文件
if (!Files.exists(path)) {
    Files.createFile(path);
}

// 创建目录
Path dir = Paths.get("mydir");
if (!Files.exists(dir)) {
    Files.createDirectory(dir);
}

// 复制文件
Path source = Paths.get("source.txt");
Path target = Paths.get("target.txt");
Files.copy(source, target);

// 移动/重命名文件
Files.move(source, target);

// 删除文件
Files.delete(path);

// 获取文件大小
long size = Files.size(path);
```

### 2. File 类（传统方式）

```java
import java.io.File;
import java.io.IOException;

File file = new File("file.txt");

// 检查文件是否存在
if (file.exists()) {
    System.out.println("文件存在");
}

// 获取文件信息
System.out.println("文件名：" + file.getName());
System.out.println("路径：" + file.getPath());
System.out.println("绝对路径：" + file.getAbsolutePath());
System.out.println("大小：" + file.length() + " 字节");
System.out.println("是否可读：" + file.canRead());
System.out.println("是否可写：" + file.canWrite());

// 创建文件
if (!file.exists()) {
    file.createNewFile();
}

// 创建目录
File dir = new File("mydir");
if (!dir.exists()) {
    dir.mkdir();  // 创建单层目录
    dir.mkdirs(); // 创建多层目录
}

// 删除文件
if (file.exists()) {
    file.delete();
}

// 列出目录中的文件
File directory = new File(".");
String[] files = directory.list();
for (String fileName : files) {
    System.out.println(fileName);
}
```

---

## 文件读写最佳实践

### 1. 使用 try-with-resources

```java
// ✅ 推荐：自动关闭资源
try (FileReader reader = new FileReader("file.txt")) {
    // 读取文件
} catch (IOException e) {
    // 处理异常
}

// ❌ 不推荐：手动关闭资源
FileReader reader = null;
try {
    reader = new FileReader("file.txt");
    // 读取文件
} catch (IOException e) {
    // 处理异常
} finally {
    if (reader != null) {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### 2. 使用缓冲流提高效率

```java
// ✅ 推荐：使用缓冲流
try (BufferedReader reader = new BufferedReader(new FileReader("file.txt"))) {
    String line;
    while ((line = reader.readLine()) != null) {
        System.out.println(line);
    }
}

// ⚠️ 不推荐：直接使用 FileReader（效率较低）
try (FileReader reader = new FileReader("file.txt")) {
    int ch;
    while ((ch = reader.read()) != -1) {
        System.out.print((char) ch);
    }
}
```

### 3. 使用 Files 类（Java 7+）

```java
// ✅ 推荐：使用 Files 类（简洁）
Path path = Paths.get("file.txt");
List<String> lines = Files.readAllLines(path);

// ⚠️ 传统方式（代码较多）
try (BufferedReader reader = new BufferedReader(new FileReader("file.txt"))) {
    List<String> lines = new ArrayList<>();
    String line;
    while ((line = reader.readLine()) != null) {
        lines.add(line);
    }
}
```

### 4. 处理文件不存在的情况

```java
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

Path path = Paths.get("file.txt");

// 方式 1：检查文件是否存在
if (Files.exists(path)) {
    List<String> lines = Files.readAllLines(path);
} else {
    System.out.println("文件不存在");
}

// 方式 2：使用 try-catch
try {
    List<String> lines = Files.readAllLines(path);
} catch (java.nio.file.NoSuchFileException e) {
    System.out.println("文件不存在：" + e.getMessage());
} catch (IOException e) {
    System.out.println("读取失败：" + e.getMessage());
}
```

### 5. 选择合适的字符编码

```java
import java.io.*;
import java.nio.charset.StandardCharsets;

// 指定字符编码（UTF-8）
try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(
            new FileInputStream("file.txt"), 
            StandardCharsets.UTF_8))) {
    String line;
    while ((line = reader.readLine()) != null) {
        System.out.println(line);
    }
}

// 使用 Files 类（默认 UTF-8）
Path path = Paths.get("file.txt");
List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
```

---

## 常见文件操作场景

### 场景 1：读取配置文件

```java
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.io.InputStream;

// 使用 Properties 读取配置文件
Properties props = new Properties();
try (InputStream input = Files.newInputStream(Paths.get("config.properties"))) {
    props.load(input);
    String value = props.getProperty("key");
    System.out.println("配置值：" + value);
} catch (IOException e) {
    System.out.println("读取配置失败：" + e.getMessage());
}
```

### 场景 2：写入日志文件

```java
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String LOG_FILE = "app.log";
    
    public static void log(String message) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(LOG_FILE, true))) {  // 追加模式
            String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            writer.write(timestamp + " - " + message);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("写入日志失败：" + e.getMessage());
        }
    }
}
```

### 场景 3：复制文件

```java
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

// 方式 1：使用 Files.copy（推荐）
Path source = Paths.get("source.txt");
Path target = Paths.get("target.txt");
Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

// 方式 2：使用字节流
try (
    FileInputStream input = new FileInputStream("source.txt");
    FileOutputStream output = new FileOutputStream("target.txt")
) {
    byte[] buffer = new byte[1024];
    int length;
    while ((length = input.read(buffer)) != -1) {
        output.write(buffer, 0, length);
    }
} catch (IOException e) {
    System.out.println("复制失败：" + e.getMessage());
}
```

### 场景 4：读取 CSV 文件

```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    public static List<String[]> readCSV(String filename) {
        List<String[]> records = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(
                new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                records.add(fields);
            }
        } catch (IOException e) {
            System.out.println("读取 CSV 失败：" + e.getMessage());
        }
        
        return records;
    }
}
```

### 场景 5：文件搜索

```java
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileSearcher {
    public static List<File> searchFiles(File directory, String extension) {
        List<File> result = new ArrayList<>();
        
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        result.addAll(searchFiles(file, extension));
                    } else if (file.getName().endsWith(extension)) {
                        result.add(file);
                    }
                }
            }
        }
        
        return result;
    }
}
```

---

## 📝 总结

### 选择指南

| 场景 | 推荐方式 |
|------|---------|
| **读取文本文件** | `BufferedReader` + `FileReader` 或 `Files.readAllLines()` |
| **写入文本文件** | `BufferedWriter` + `FileWriter` 或 `Files.write()` |
| **读取二进制文件** | `BufferedInputStream` + `FileInputStream` |
| **写入二进制文件** | `BufferedOutputStream` + `FileOutputStream` |
| **简单文件操作** | `Files` 类（Java 7+） |
| **文件信息查询** | `File` 类或 `Files` 类 |

### 核心要点

1. **使用 try-with-resources**：自动管理资源
2. **使用缓冲流**：提高 I/O 效率
3. **使用 Files 类**：代码更简洁（Java 7+）
4. **处理异常**：文件操作可能失败
5. **指定字符编码**：避免乱码问题

### 最佳实践

- ✅ 使用 try-with-resources 自动关闭资源
- ✅ 使用缓冲流提高效率
- ✅ 使用 Files 类简化代码（Java 7+）
- ✅ 处理文件不存在的情况
- ✅ 指定字符编码（UTF-8）

---

*最后更新：2024年*
