package com.hry.firstjava;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程安全和并发集合演示
 */
public class ThreadSafeCollectionDemo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 线程安全和并发集合演示 ===\n");

        // 1. 演示线程不安全的问题
        demonstrateThreadUnsafe();

        // 2. 使用 Collections.synchronizedXXX
        demonstrateSynchronizedCollections();

        // 3. 使用 ConcurrentHashMap
        demonstrateConcurrentHashMap();

        // 4. 使用 CopyOnWriteArrayList
        demonstrateCopyOnWriteArrayList();

        // 5. 使用 BlockingQueue
        demonstrateBlockingQueue();
    }

    /**
     * 演示线程不安全的问题
     */
    private static void demonstrateThreadUnsafe() throws InterruptedException {
        System.out.println("1. 线程不安全问题演示：\n");

        // ❌ 线程不安全的 ArrayList
        List<Integer> unsafeList = new ArrayList<>();

        // 创建多个线程同时添加元素
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                unsafeList.add(i);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                unsafeList.add(i);
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // 期望：2000 个元素
        // 实际：可能少于 2000（数据丢失）
        System.out.println("线程不安全的 ArrayList 大小：" + unsafeList.size() +
                "（期望：2000，可能更少）");

        // ❌ 线程不安全的 HashMap
        Map<String, Integer> unsafeMap = new HashMap<>();

        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                unsafeMap.put("key" + i, i);
            }
        });

        Thread t4 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                unsafeMap.put("key" + i, i);
            }
        });

        t3.start();
        t4.start();
        t3.join();
        t4.join();

        System.out.println("线程不安全的 HashMap 大小：" + unsafeMap.size() +
                "（期望：1000，可能更少）");

        System.out.println();
    }

    /**
     * 演示 Collections.synchronizedXXX
     */
    private static void demonstrateSynchronizedCollections() throws InterruptedException {
        System.out.println("2. Collections.synchronizedXXX 演示：\n");

        // ✅ 使用 synchronizedList
        List<Integer> syncList = Collections.synchronizedList(new ArrayList<>());

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                syncList.add(i);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                syncList.add(i);
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("synchronizedList 大小：" + syncList.size() + "（正确：2000）");

        // ⚠️ 遍历时需要手动同步
        System.out.println("\n遍历 synchronizedList（需要手动同步）：");
        synchronized (syncList) {
            int count = 0;
            for (Integer item : syncList) {
                count++;
                if (count <= 5) {
                    System.out.println("  元素 " + count + ": " + item);
                }
            }
            System.out.println("  遍历完成（已同步），共 " + count + " 个元素");
        }

        // ✅ 使用 synchronizedMap
        Map<String, Integer> syncMap = Collections.synchronizedMap(new HashMap<>());

        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                syncMap.put("key" + i, i);
            }
        });

        Thread t4 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                syncMap.put("key" + i, i);
            }
        });

        t3.start();
        t4.start();
        t3.join();
        t4.join();

        System.out.println("synchronizedMap 大小：" + syncMap.size() + "（正确：1000）");

        System.out.println();
    }

    /**
     * 演示 ConcurrentHashMap
     */
    private static void demonstrateConcurrentHashMap() throws InterruptedException {
        System.out.println("3. ConcurrentHashMap 演示：\n");

        // ✅ 使用 ConcurrentHashMap
        Map<String, Integer> concurrentMap = new ConcurrentHashMap<>();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                concurrentMap.put("key" + i, i);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                concurrentMap.put("key" + i, i);
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("ConcurrentHashMap 大小：" + concurrentMap.size() + "（正确：1000）");

        // ✅ 遍历不需要手动同步
        System.out.println("\n遍历 ConcurrentHashMap（不需要手动同步）：");
        int count = 0;
        for (Map.Entry<String, Integer> entry : concurrentMap.entrySet()) {
            count++;
            if (count <= 5) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue());
            }
        }
        System.out.println("  遍历完成（自动安全）");

        // ✅ 原子操作
        concurrentMap.putIfAbsent("newKey", 999); // 如果不存在则添加
        concurrentMap.replace("key0", 0, 100); // 如果值等于 0 则替换为 100

        System.out.println("原子操作后，key0 的值：" + concurrentMap.get("key0"));
        System.out.println("newKey 的值：" + concurrentMap.get("newKey"));

        System.out.println();
    }

    /**
     * 演示 CopyOnWriteArrayList
     */
    private static void demonstrateCopyOnWriteArrayList() throws InterruptedException {
        System.out.println("4. CopyOnWriteArrayList 演示：\n");

        // ✅ 使用 CopyOnWriteArrayList（读多写少场景）
        List<String> copyOnWriteList = new CopyOnWriteArrayList<>();
        copyOnWriteList.add("元素1");
        copyOnWriteList.add("元素2");
        copyOnWriteList.add("元素3");

        // 一个线程遍历，另一个线程修改，不会抛出异常
        Thread reader = new Thread(() -> {
            System.out.println("  读取线程开始遍历：");
            for (String item : copyOnWriteList) {
                System.out.println("    读取：" + item);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("  读取线程完成");
        });

        Thread writer = new Thread(() -> {
            try {
                Thread.sleep(50); // 等待读取线程开始
                System.out.println("  写入线程添加元素");
                copyOnWriteList.add("元素4");
                copyOnWriteList.add("元素5");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        reader.start();
        writer.start();
        reader.join();
        writer.join();

        System.out.println("\n最终列表：" + copyOnWriteList);
        System.out.println("注意：读取线程看到的是旧版本，不会抛出异常");

        System.out.println();
    }

    /**
     * 演示 BlockingQueue
     */
    private static void demonstrateBlockingQueue() throws InterruptedException {
        System.out.println("5. BlockingQueue 演示：\n");

        // ✅ 使用 ArrayBlockingQueue（生产者-消费者模式）
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);

        // 生产者线程
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    String product = "产品" + i;
                    queue.put(product); // 如果队列满，会阻塞
                    System.out.println("  生产：" + product);
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // 消费者线程
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    String product = queue.take(); // 如果队列空，会阻塞
                    System.out.println("  消费：" + product);
                    Thread.sleep(300);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();

        System.out.println("\n生产者-消费者模式演示完成");
        System.out.println("注意：队列满时生产会阻塞，队列空时消费会阻塞");

        System.out.println();
    }
}

/**
 * 实际应用示例：线程安全的计数器
 */
class ThreadSafeCounter {
    private Map<String, AtomicInteger> counter = new ConcurrentHashMap<>();

    public void increment(String key) {
        counter.computeIfAbsent(key, k -> new AtomicInteger(0)).incrementAndGet();
    }

    public int getCount(String key) {
        return counter.getOrDefault(key, new AtomicInteger(0)).get();
    }

    public void printAll() {
        counter.forEach((key, value) -> System.out.println(key + ": " + value.get()));
    }
}

/**
 * 实际应用示例：事件管理器（读多写少）
 */
class EventManager {
    private List<EventListener> listeners = new CopyOnWriteArrayList<>();

    public void addListener(EventListener listener) {
        listeners.add(listener);
    }

    public void removeListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void fireEvent(String event) {
        // 遍历不需要加锁，自动安全
        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}

interface EventListener {
    void onEvent(String event);
}
