package com.hry.firstjava;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 如何判断多线程环境演示
 */
public class MultiThreadDetectionDemo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 如何判断多线程环境 ===\n");
        
        // 1. 单线程环境示例
        demonstrateSingleThread();
        
        // 2. 多线程环境示例（显式创建线程）
        demonstrateMultiThread();
        
        // 3. 多线程环境示例（线程池）
        demonstrateThreadPool();
        
        // 4. 多线程环境示例（共享资源）
        demonstrateSharedResource();
        
        // 5. 实际应用场景
        demonstrateRealWorldScenarios();
    }
    
    /**
     * 单线程环境示例
     */
    private static void demonstrateSingleThread() {
        System.out.println("1. 单线程环境示例：\n");
        
        List<String> list = new ArrayList<>();
        
        // 只有一个主线程在执行
        list.add("元素1");
        list.add("元素2");
        list.add("元素3");
        
        System.out.println("判断要点：");
        System.out.println("  ✅ 只有一个主线程");
        System.out.println("  ✅ 没有创建其他线程");
        System.out.println("  ✅ 可以使用 ArrayList（不需要线程安全）");
        System.out.println("列表内容：" + list);
        
        System.out.println();
    }
    
    /**
     * 多线程环境示例（显式创建线程）
     */
    private static void demonstrateMultiThread() throws InterruptedException {
        System.out.println("2. 多线程环境示例（显式创建线程）：\n");
        
        // ⚠️ 共享资源
        List<String> unsafeList = new ArrayList<>();
        List<String> safeList = new CopyOnWriteArrayList<>();
        
        System.out.println("判断要点：");
        System.out.println("  ✅ 创建了多个 Thread 对象");
        System.out.println("  ✅ 多个线程共享同一个 list");
        System.out.println("  ⚠️ 需要使用线程安全的集合");
        
        // 创建多个线程
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                unsafeList.add("线程1-元素" + i);
                safeList.add("线程1-元素" + i);
            }
        });
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                unsafeList.add("线程2-元素" + i);
                safeList.add("线程2-元素" + i);
            }
        });
        
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        
        System.out.println("\n线程不安全的 ArrayList 大小：" + unsafeList.size() + 
                          "（可能少于 10，数据丢失）");
        System.out.println("线程安全的 CopyOnWriteArrayList 大小：" + safeList.size() + 
                          "（正确：10）");
        
        System.out.println();
    }
    
    /**
     * 多线程环境示例（线程池）
     */
    private static void demonstrateThreadPool() throws InterruptedException {
        System.out.println("3. 多线程环境示例（线程池）：\n");
        
        // ⚠️ 共享资源
        List<String> results = new CopyOnWriteArrayList<>();
        
        System.out.println("判断要点：");
        System.out.println("  ✅ 使用了 ExecutorService（线程池）");
        System.out.println("  ✅ 多个任务可能同时执行");
        System.out.println("  ⚠️ 需要使用线程安全的集合");
        
        ExecutorService executor = Executors.newFixedThreadPool(5);
        
        // 提交多个任务
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            executor.submit(() -> {
                String result = "任务" + taskId + "完成";
                results.add(result);
                System.out.println("  执行：" + result);
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        System.out.println("\n完成的任务数：" + results.size());
        System.out.println();
    }
    
    /**
     * 多线程环境示例（共享资源）
     */
    private static void demonstrateSharedResource() throws InterruptedException {
        System.out.println("4. 多线程环境示例（共享资源）：\n");
        
        // ⚠️ 共享资源（类成员变量或单例对象）
        SharedCounter counter = new SharedCounter();
        
        System.out.println("判断要点：");
        System.out.println("  ✅ 多个线程访问同一个对象（共享资源）");
        System.out.println("  ✅ 多个线程可能同时修改同一个变量");
        System.out.println("  ⚠️ 需要使用线程安全的实现");
        
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        });
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        });
        
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        
        System.out.println("计数器值：" + counter.getCount() + "（期望：2000）");
        System.out.println();
    }
    
    /**
     * 实际应用场景演示
     */
    private static void demonstrateRealWorldScenarios() {
        System.out.println("5. 实际应用场景：\n");
        
        System.out.println("场景 1：Web 应用");
        System.out.println("  ✅ 每个 HTTP 请求一个线程");
        System.out.println("  ✅ 多个请求可能同时访问同一个 Controller");
        System.out.println("  ⚠️ Controller 中的成员变量需要使用线程安全的集合");
        System.out.println("  示例：");
        System.out.println("    @RestController");
        System.out.println("    public class UserController {");
        System.out.println("        private List<String> users = new CopyOnWriteArrayList<>();");
        System.out.println("        // 需要使用线程安全的集合");
        System.out.println("    }");
        
        System.out.println("\n场景 2：缓存系统");
        System.out.println("  ✅ 缓存通常是单例对象");
        System.out.println("  ✅ 多个线程可能同时访问缓存");
        System.out.println("  ⚠️ 需要使用 ConcurrentHashMap");
        System.out.println("  示例：");
        System.out.println("    public class Cache {");
        System.out.println("        private Map<String, Object> cache = new ConcurrentHashMap<>();");
        System.out.println("        // 需要使用线程安全的 Map");
        System.out.println("    }");
        
        System.out.println("\n场景 3：事件监听器");
        System.out.println("  ✅ 事件可能在多个线程中触发");
        System.out.println("  ✅ 监听器列表可能被多个线程修改");
        System.out.println("  ⚠️ 需要使用 CopyOnWriteArrayList");
        System.out.println("  示例：");
        System.out.println("    public class EventManager {");
        System.out.println("        private List<EventListener> listeners = new CopyOnWriteArrayList<>();");
        System.out.println("        // 需要使用线程安全的 List");
        System.out.println("    }");
        
        System.out.println();
    }
}

/**
 * 共享计数器（演示共享资源）
 */
class SharedCounter {
    // ✅ 线程安全的方法（使用 AtomicInteger）
    private AtomicInteger atomicCount = new AtomicInteger(0);
    
    public void increment() {
        atomicCount.incrementAndGet();  // 原子操作，线程安全
    }
    
    public int getCount() {
        return atomicCount.get();
    }
}

/**
 * 模拟 Web Controller（演示实际应用场景）
 */
class UserController {
    // ⚠️ 共享资源（所有请求共享）
    private List<String> users = new CopyOnWriteArrayList<>();  // ✅ 使用线程安全的集合
    
    public List<String> getUsers() {
        // 多个请求可能同时调用这个方法
        return new ArrayList<>(users);  // 返回副本
    }
    
    public void addUser(String user) {
        // 多个请求可能同时调用这个方法
        users.add(user);  // 线程安全
    }
}

/**
 * 模拟缓存系统（演示实际应用场景）
 */
class Cache {
    // ⚠️ 共享资源（单例对象）
    private Map<String, Object> cache = new ConcurrentHashMap<>();  // ✅ 使用线程安全的 Map
    
    public Object get(String key) {
        // 多个线程可能同时读取
        return cache.get(key);
    }
    
    public void put(String key, Object value) {
        // 多个线程可能同时写入
        cache.put(key, value);
    }
}

/**
 * 模拟事件管理器（演示实际应用场景）
 * 注意：EventManager 和 EventListener 在 ThreadSafeCollectionDemo 中已定义
 */
class EventManagerExample {
    // ⚠️ 共享资源（可能被多个线程修改）
    private List<EventListenerExample> listeners = new CopyOnWriteArrayList<>();  // ✅ 使用线程安全的 List
    
    public void addListener(EventListenerExample listener) {
        // 可能被多个线程调用
        listeners.add(listener);
    }
    
    public void fireEvent(String event) {
        // 可能在不同线程中触发
        for (EventListenerExample listener : listeners) {
            listener.onEvent(event);
        }
    }
}

interface EventListenerExample {
    void onEvent(String event);
}
