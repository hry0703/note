package com.hry.firstjava;

import java.io.IOException;

/**
 * Java 异常处理演示
 */
public class ExceptionHandlingDemo {
    public static void main(String[] args) {
        System.out.println("=== Java 异常处理演示 ===\n");
        
        // 1. try-catch 基本用法
        demonstrateTryCatch();
        
        // 2. 多个 catch 块
        demonstrateMultipleCatch();
        
        // 3. finally 块
        demonstrateFinally();
        
        // 4. try-with-resources
        demonstrateTryWithResources();
        
        // 5. throws 和 throw
        demonstrateThrowsAndThrow();
        
        // 6. 自定义异常
        demonstrateCustomException();
        
        // 7. 常见异常处理
        demonstrateCommonExceptions();
    }
    
    /**
     * try-catch 基本用法
     */
    private static void demonstrateTryCatch() {
        System.out.println("1. try-catch 基本用法：\n");
        
        // 捕获算术异常
        try {
            int result = 10 / 0;  // 抛出 ArithmeticException
            System.out.println("结果：" + result);
        } catch (ArithmeticException e) {
            System.out.println("捕获异常：" + e.getMessage());
            System.out.println("异常类型：" + e.getClass().getSimpleName());
        }
        
        System.out.println("程序继续执行\n");
    }
    
    /**
     * 多个 catch 块
     */
    private static void demonstrateMultipleCatch() {
        System.out.println("2. 多个 catch 块：\n");
        
        try {
            String str = null;
            // 可能抛出 NullPointerException
            if (str != null) {
                int length = str.length();
            } else {
                throw new NullPointerException("字符串为空");
            }
            int[] array = {1, 2, 3};
            // 可能抛出 ArrayIndexOutOfBoundsException
            int value = array[10];
        } catch (NullPointerException e) {
            System.out.println("捕获空指针异常：" + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("捕获数组越界异常：" + e.getMessage());
        } catch (Exception e) {
            System.out.println("捕获其他异常：" + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * finally 块演示
     */
    private static void demonstrateFinally() {
        System.out.println("3. finally 块演示：\n");
        
        // 无论是否发生异常，finally 都会执行
        try {
            System.out.println("  try 块执行");
            int result = 10 / 2;
            System.out.println("  结果：" + result);
        } catch (ArithmeticException e) {
            System.out.println("  catch 块执行");
        } finally {
            System.out.println("  finally 块执行（总是执行）");
        }
        
        System.out.println("\n发生异常时：");
        try {
            System.out.println("  try 块执行");
            int result = 10 / 0;  // 抛出异常
        } catch (ArithmeticException e) {
            System.out.println("  catch 块执行");
        } finally {
            System.out.println("  finally 块执行（总是执行）");
        }
        
        System.out.println();
    }
    
    /**
     * try-with-resources 演示
     */
    private static void demonstrateTryWithResources() {
        System.out.println("4. try-with-resources 演示：\n");
        
        // ✅ 推荐：自动管理资源
        try (AutoCloseableResource resource = new AutoCloseableResource()) {
            System.out.println("  使用资源");
            resource.doSomething();
        } catch (Exception e) {
            System.out.println("  处理异常：" + e.getMessage());
        }
        // 资源自动关闭，不需要 finally
        
        System.out.println();
    }
    
    /**
     * throws 和 throw 演示
     */
    private static void demonstrateThrowsAndThrow() {
        System.out.println("5. throws 和 throw 演示：\n");
        
        // 调用声明了 throws 的方法
        try {
            methodThatThrowsException();
        } catch (IOException e) {
            System.out.println("捕获异常：" + e.getMessage());
        }
        
        // 调用主动抛出异常的方法
        try {
            validateAge(-5);
        } catch (IllegalArgumentException e) {
            System.out.println("验证失败：" + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * 声明可能抛出异常的方法
     */
    private static void methodThatThrowsException() throws IOException {
        // 模拟可能抛出 IOException 的操作
        throw new IOException("文件读取失败");
    }
    
    /**
     * 主动抛出异常的方法
     */
    private static void validateAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("年龄不能为负数，当前值：" + age);
        }
        if (age > 150) {
            throw new IllegalArgumentException("年龄不能超过 150，当前值：" + age);
        }
        System.out.println("年龄验证通过：" + age);
    }
    
    /**
     * 自定义异常演示
     */
    private static void demonstrateCustomException() {
        System.out.println("6. 自定义异常演示：\n");
        
        BankAccount account = new BankAccount(1000);
        
        try {
            account.withdraw(500);
            System.out.println("取款成功，余额：" + account.getBalance());
            
            account.withdraw(600);  // 余额不足
        } catch (InsufficientBalanceException e) {
            System.out.println("取款失败：" + e.getMessage());
            System.out.println("当前余额：" + e.getBalance());
            System.out.println("请求金额：" + e.getRequestedAmount());
        }
        
        System.out.println();
    }
    
    /**
     * 常见异常处理演示
     */
    private static void demonstrateCommonExceptions() {
        System.out.println("7. 常见异常处理演示：\n");
        
        // NullPointerException
        System.out.println("处理 NullPointerException：");
        String str = null;
        try {
            int length = str.length();  // 抛出异常
            System.out.println("长度：" + length);
        } catch (NullPointerException e) {
            System.out.println("  字符串为空，使用默认值");
            str = "";
        }
        
        // ArrayIndexOutOfBoundsException
        System.out.println("\n处理 ArrayIndexOutOfBoundsException：");
        int[] array = {1, 2, 3};
        try {
            int value = array[5];  // 抛出异常
            System.out.println("值：" + value);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("  数组索引越界，使用默认值");
        }
        
        // ClassCastException
        System.out.println("\n处理 ClassCastException：");
        Object obj = "字符串";
        try {
            Integer num = (Integer) obj;  // 抛出异常
            System.out.println("数字：" + num);
        } catch (ClassCastException e) {
            System.out.println("  类型转换失败");
        }
        
        // 预防性检查
        System.out.println("\n预防性检查：");
        if (obj instanceof Integer) {
            Integer num = (Integer) obj;
            System.out.println("  数字：" + num);
        } else {
            System.out.println("  对象不是 Integer 类型");
        }
        
        System.out.println();
    }
}

/**
 * 自动关闭资源示例（实现 AutoCloseable 接口）
 */
class AutoCloseableResource implements AutoCloseable {
    public void doSomething() {
        System.out.println("  执行操作");
    }
    
    @Override
    public void close() throws Exception {
        System.out.println("  资源自动关闭");
    }
}

/**
 * 银行账户类（演示自定义异常）
 */
class BankAccount {
    private double balance;
    
    public BankAccount(double balance) {
        this.balance = balance;
    }
    
    public double getBalance() {
        return balance;
    }
    
    /**
     * 取款方法（抛出自定义异常）
     */
    public void withdraw(double amount) throws InsufficientBalanceException {
        if (amount > balance) {
            throw new InsufficientBalanceException(balance, amount);
        }
        balance -= amount;
    }
}

/**
 * 自定义异常：余额不足异常（检查异常）
 */
class InsufficientBalanceException extends Exception {
    private double balance;
    private double requestedAmount;
    
    public InsufficientBalanceException(double balance, double requestedAmount) {
        super(String.format("余额不足：当前余额 %.2f，请求金额 %.2f", 
              balance, requestedAmount));
        this.balance = balance;
        this.requestedAmount = requestedAmount;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public double getRequestedAmount() {
        return requestedAmount;
    }
}

/**
 * 自定义异常：无效输入异常（非检查异常）
 */
class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }
    
    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
