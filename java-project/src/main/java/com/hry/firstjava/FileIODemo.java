package com.hry.firstjava;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Java 文件 I/O 操作演示
 */
public class FileIODemo {
    public static void main(String[] args) {
        System.out.println("=== Java 文件 I/O 操作演示 ===\n");
        
        // 1. 字符流读取文件
        demonstrateCharacterRead();
        
        // 2. 字符流写入文件
        demonstrateCharacterWrite();
        
        // 3. 字节流操作
        demonstrateByteStream();
        
        // 4. Files 类操作（推荐）
        demonstrateFilesClass();
        
        // 5. 实际应用场景
        demonstrateRealWorldScenarios();
    }
    
    /**
     * 字符流读取文件演示
     */
    private static void demonstrateCharacterRead() {
        System.out.println("1. 字符流读取文件演示：\n");
        
        // 创建测试文件
        createTestFile("test.txt", "第一行\n第二行\n第三行");
        
        // 方式 1：FileReader（逐个字符读取）
        System.out.println("方式 1：FileReader（逐个字符读取）");
        try (FileReader reader = new FileReader("test.txt")) {
            int ch;
            while ((ch = reader.read()) != -1) {
                System.out.print((char) ch);
            }
            System.out.println();
        } catch (IOException e) {
            System.out.println("读取失败：" + e.getMessage());
        }
        
        // 方式 2：BufferedReader（按行读取，推荐）
        System.out.println("\n方式 2：BufferedReader（按行读取，推荐）");
        try (BufferedReader reader = new BufferedReader(new FileReader("test.txt"))) {
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                System.out.println("第 " + lineNumber + " 行：" + line);
                lineNumber++;
            }
        } catch (IOException e) {
            System.out.println("读取失败：" + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * 字符流写入文件演示
     */
    private static void demonstrateCharacterWrite() {
        System.out.println("2. 字符流写入文件演示：\n");
        
        // 方式 1：FileWriter
        System.out.println("方式 1：FileWriter");
        try (FileWriter writer = new FileWriter("output1.txt")) {
            writer.write("这是第一行\n");
            writer.write("这是第二行\n");
            writer.write("这是第三行");
        } catch (IOException e) {
            System.out.println("写入失败：" + e.getMessage());
        }
        System.out.println("已写入 output1.txt");
        
        // 方式 2：BufferedWriter（推荐）
        System.out.println("\n方式 2：BufferedWriter（推荐）");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output2.txt"))) {
            writer.write("使用 BufferedWriter");
            writer.newLine();
            writer.write("这是第二行");
            writer.newLine();
            writer.write("这是第三行");
        } catch (IOException e) {
            System.out.println("写入失败：" + e.getMessage());
        }
        System.out.println("已写入 output2.txt");
        
        // 方式 3：追加模式
        System.out.println("\n方式 3：追加模式");
        try (FileWriter writer = new FileWriter("output2.txt", true)) {
            writer.write("\n这是追加的内容");
        } catch (IOException e) {
            System.out.println("追加失败：" + e.getMessage());
        }
        System.out.println("已追加到 output2.txt");
        
        System.out.println();
    }
    
    /**
     * 字节流操作演示
     */
    private static void demonstrateByteStream() {
        System.out.println("3. 字节流操作演示：\n");
        
        // 写入字节数据
        System.out.println("写入字节数据：");
        try (FileOutputStream output = new FileOutputStream("binary.dat")) {
            String data = "Hello, World!";
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            output.write(bytes);
            System.out.println("已写入 " + bytes.length + " 字节到 binary.dat");
        } catch (IOException e) {
            System.out.println("写入失败：" + e.getMessage());
        }
        
        // 读取字节数据
        System.out.println("\n读取字节数据：");
        try (FileInputStream input = new FileInputStream("binary.dat")) {
            byte[] buffer = new byte[1024];
            int length = input.read(buffer);
            String content = new String(buffer, 0, length, StandardCharsets.UTF_8);
            System.out.println("读取内容：" + content);
        } catch (IOException e) {
            System.out.println("读取失败：" + e.getMessage());
        }
        
        // 使用缓冲流复制文件
        System.out.println("\n使用缓冲流复制文件：");
        try (
            BufferedInputStream input = new BufferedInputStream(
                new FileInputStream("binary.dat"));
            BufferedOutputStream output = new BufferedOutputStream(
                new FileOutputStream("binary_copy.dat"))
        ) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) != -1) {
                output.write(buffer, 0, length);
            }
            System.out.println("已复制 binary.dat 到 binary_copy.dat");
        } catch (IOException e) {
            System.out.println("复制失败：" + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Files 类操作演示（推荐）
     */
    private static void demonstrateFilesClass() {
        System.out.println("4. Files 类操作演示（推荐）：\n");
        
        Path path = Paths.get("files_demo.txt");
        
        // 写入文件
        System.out.println("写入文件：");
        try {
            List<String> lines = List.of("第一行", "第二行", "第三行");
            Files.write(path, lines, StandardCharsets.UTF_8);
            System.out.println("已写入 files_demo.txt");
        } catch (IOException e) {
            System.out.println("写入失败：" + e.getMessage());
        }
        
        // 读取文件
        System.out.println("\n读取文件：");
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (int i = 0; i < lines.size(); i++) {
                System.out.println("第 " + (i + 1) + " 行：" + lines.get(i));
            }
        } catch (IOException e) {
            System.out.println("读取失败：" + e.getMessage());
        }
        
        // 读取为字符串（Java 11+）
        System.out.println("\n读取为字符串：");
        try {
            String content = Files.readString(path, StandardCharsets.UTF_8);
            System.out.println("文件内容：\n" + content);
        } catch (IOException e) {
            System.out.println("读取失败：" + e.getMessage());
        }
        
        // 文件信息
        System.out.println("\n文件信息：");
        try {
            System.out.println("文件是否存在：" + Files.exists(path));
            System.out.println("文件大小：" + Files.size(path) + " 字节");
        } catch (IOException e) {
            System.out.println("获取信息失败：" + e.getMessage());
        }
        
        // 复制文件
        System.out.println("\n复制文件：");
        try {
            Path target = Paths.get("files_demo_copy.txt");
            Files.copy(path, target, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("已复制到 files_demo_copy.txt");
        } catch (IOException e) {
            System.out.println("复制失败：" + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * 实际应用场景演示
     */
    private static void demonstrateRealWorldScenarios() {
        System.out.println("5. 实际应用场景演示：\n");
        
        // 场景 1：日志记录
        System.out.println("场景 1：日志记录");
        Logger.log("这是一条日志信息");
        Logger.log("这是另一条日志信息");
        System.out.println("日志已写入 app.log");
        
        // 场景 2：读取配置文件
        System.out.println("\n场景 2：读取配置文件");
        createPropertiesFile("config.properties", "name=张三\nage=25\ncity=北京");
        Properties props = ConfigReader.readConfig("config.properties");
        if (props != null) {
            System.out.println("配置信息：");
            props.forEach((key, value) -> 
                System.out.println("  " + key + " = " + value));
        }
        
        // 场景 3：CSV 文件读取
        System.out.println("\n场景 3：CSV 文件读取");
        createCSVFile("data.csv", "姓名,年龄,城市\n张三,25,北京\n李四,30,上海");
        List<String[]> csvData = CSVReader.readCSV("data.csv");
        System.out.println("CSV 数据：");
        for (String[] row : csvData) {
            System.out.println("  " + String.join(" | ", row));
        }
        
        System.out.println();
    }
    
    /**
     * 创建测试文件
     */
    private static void createTestFile(String filename, String content) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(content);
        } catch (IOException e) {
            System.err.println("创建测试文件失败：" + e.getMessage());
        }
    }
    
    /**
     * 创建 Properties 文件
     */
    private static void createPropertiesFile(String filename, String content) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(content);
        } catch (IOException e) {
            System.err.println("创建配置文件失败：" + e.getMessage());
        }
    }
    
    /**
     * 创建 CSV 文件
     */
    private static void createCSVFile(String filename, String content) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(content);
        } catch (IOException e) {
            System.err.println("创建 CSV 文件失败：" + e.getMessage());
        }
    }
}

/**
 * 日志记录器
 */
class Logger {
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

/**
 * 配置文件读取器
 */
class ConfigReader {
    public static Properties readConfig(String filename) {
        Properties props = new Properties();
        try (InputStream input = Files.newInputStream(Paths.get(filename))) {
            props.load(input);
            return props;
        } catch (IOException e) {
            System.err.println("读取配置失败：" + e.getMessage());
            return null;
        }
    }
}

/**
 * CSV 文件读取器
 */
class CSVReader {
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
            System.err.println("读取 CSV 失败：" + e.getMessage());
        }
        
        return records;
    }
}
