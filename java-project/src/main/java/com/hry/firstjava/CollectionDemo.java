package com.hry.firstjava;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Java 集合框架演示
 * 包括 List、Set、Map 的常用操作
 */
public class CollectionDemo {
    public static void main(String[] args) {
        System.out.println("=== Java 集合框架演示 ===\n");

        // 1. List 演示
        demonstrateList();

        // 2. Set 演示
        demonstrateSet();

        // 3. Map 演示
        demonstrateMap();

        // 4. 集合转换演示
        demonstrateConversion();

        // 5. Java 8+ Stream 操作演示
        demonstrateStream();
    }

    /**
     * List（列表）演示
     */
    private static void demonstrateList() {
        System.out.println("1. List（列表）演示：");
        System.out.println("特点：有序、可重复、有索引\n");

        // ArrayList 示例
        List<String> arrayList = new ArrayList<>();
        arrayList.add("苹果");
        arrayList.add("香蕉");
        arrayList.add("橙子");
        arrayList.add("苹果"); // 可以重复

        System.out.println("ArrayList 内容：" + arrayList);
        System.out.println("第一个元素：" + arrayList.get(0));
        System.out.println("大小：" + arrayList.size());
        System.out.println("是否包含'苹果'：" + arrayList.contains("苹果"));
        System.out.println("'苹果'的索引：" + arrayList.indexOf("苹果"));

        // 修改元素
        arrayList.set(1, "葡萄");
        System.out.println("修改后：" + arrayList);

        // 删除元素
        arrayList.remove("苹果"); // 删除第一个匹配的
        System.out.println("删除'苹果'后：" + arrayList);

        // LinkedList 示例
        List<String> linkedList = new LinkedList<>();
        linkedList.add("第一");
        linkedList.add("第二");
        linkedList.add(0, "插入到开头"); // 在头部插入很快
        System.out.println("LinkedList 内容：" + linkedList);

        System.out.println();
    }

    /**
     * Set（集合）演示
     */
    private static void demonstrateSet() {
        System.out.println("2. Set（集合）演示：");
        System.out.println("特点：不重复、无序（部分有序）\n");

        // HashSet 示例（无序）
        Set<String> hashSet = new HashSet<>();
        hashSet.add("苹果");
        hashSet.add("香蕉");
        hashSet.add("橙子");
        hashSet.add("苹果"); // 重复，不会添加

        System.out.println("HashSet 内容：" + hashSet + "（顺序不确定）");
        System.out.println("大小：" + hashSet.size()); // 3，不是 4

        // TreeSet 示例（有序，自动排序）
        Set<String> treeSet = new TreeSet<>();
        treeSet.add("香蕉");
        treeSet.add("苹果");
        treeSet.add("橙子");
        System.out.println("TreeSet 内容：" + treeSet + "（自动排序）");

        // LinkedHashSet 示例（保持插入顺序）
        Set<String> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.add("苹果");
        linkedHashSet.add("香蕉");
        linkedHashSet.add("橙子");
        System.out.println("LinkedHashSet 内容：" + linkedHashSet + "（保持插入顺序）");

        System.out.println();
    }

    /**
     * Map（映射）演示
     */
    private static void demonstrateMap() {
        System.out.println("3. Map（映射）演示：");
        System.out.println("特点：键值对、键不重复\n");

        // HashMap 示例
        Map<String, Integer> hashMap = new HashMap<>();
        hashMap.put("苹果", 10);
        hashMap.put("香蕉", 20);
        hashMap.put("橙子", 15);
        hashMap.put("苹果", 25); // 键重复，会覆盖原值

        System.out.println("HashMap 内容：" + hashMap);
        System.out.println("'苹果'的数量：" + hashMap.get("苹果"));
        System.out.println("'葡萄'的数量：" + hashMap.getOrDefault("葡萄", 0));
        System.out.println("是否包含键'苹果'：" + hashMap.containsKey("苹果"));
        System.out.println("是否包含值 20：" + hashMap.containsValue(20));

        // 遍历 Map
        System.out.println("\n遍历 Map（方式 1：entrySet）：");
        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }

        System.out.println("\n遍历 Map（方式 2：keySet）：");
        for (String key : hashMap.keySet()) {
            System.out.println("  " + key + ": " + hashMap.get(key));
        }

        System.out.println("\n遍历 Map（方式 3：forEach Lambda）：");
        hashMap.forEach((key, value) -> System.out.println("  " + key + ": " + value));

        // TreeMap 示例（有序）
        Map<String, Integer> treeMap = new TreeMap<>();
        treeMap.put("香蕉", 20);
        treeMap.put("苹果", 10);
        treeMap.put("橙子", 15);
        System.out.println("\nTreeMap 内容：" + treeMap + "（按键排序）");

        // LinkedHashMap 示例（保持插入顺序）
        Map<String, Integer> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("苹果", 10);
        linkedHashMap.put("香蕉", 20);
        linkedHashMap.put("橙子", 15);
        System.out.println("LinkedHashMap 内容：" + linkedHashMap + "（保持插入顺序）");

        System.out.println();
    }

    /**
     * 集合转换演示
     */
    private static void demonstrateConversion() {
        System.out.println("4. 集合转换演示：\n");

        // List 转 Set（去重）
        List<String> list = Arrays.asList("苹果", "香蕉", "苹果", "橙子");
        Set<String> set = new HashSet<>(list);
        System.out.println("原 List：" + list);
        System.out.println("转 Set（去重）：" + set);

        // Set 转 List
        List<String> list2 = new ArrayList<>(set);
        System.out.println("转回 List：" + list2);

        // List 转数组
        String[] array = list.toArray(new String[0]);
        System.out.println("转数组：" + Arrays.toString(array));

        // 数组转 List
        List<String> list3 = Arrays.asList(array);
        System.out.println("数组转 List：" + list3);

        System.out.println();
    }

    /**
     * Java 8+ Stream 操作演示
     */
    private static void demonstrateStream() {
        System.out.println("5. Java 8+ Stream 操作演示：\n");

        List<String> fruits = Arrays.asList("苹果", "香蕉", "橙子", "葡萄", "西瓜");

        // 过滤
        List<String> filtered = fruits.stream()
                .filter(fruit -> fruit.length() == 2)
                .collect(Collectors.toList());
        System.out.println("过滤（长度为2）：" + filtered);

        // 转换（转大写）
        List<String> upper = fruits.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        System.out.println("转换（转大写）：" + upper);

        // 去重
        List<String> withDuplicates = Arrays.asList("苹果", "香蕉", "苹果", "橙子");
        List<String> distinct = withDuplicates.stream()
                .distinct()
                .collect(Collectors.toList());
        System.out.println("去重：" + distinct);

        // 排序
        List<String> sorted = fruits.stream()
                .sorted()
                .collect(Collectors.toList());
        System.out.println("排序：" + sorted);

        // 统计
        long count = fruits.stream()
                .filter(fruit -> fruit.length() == 2)
                .count();
        System.out.println("统计（长度为2的数量）：" + count);

        System.out.println();
    }
}
