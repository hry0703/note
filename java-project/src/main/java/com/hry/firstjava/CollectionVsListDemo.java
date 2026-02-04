package com.hry.firstjava;

import java.util.*;

/**
 * Collection 和 List 的区别演示
 */
public class CollectionVsListDemo {
    public static void main(String[] args) {
        System.out.println("=== Collection 和 List 的区别演示 ===\n");
        
        // 1. 基本区别演示
        demonstrateBasicDifference();
        
        // 2. 方法对比演示
        demonstrateMethodDifference();
        
        // 3. 多态使用演示
        demonstratePolymorphism();
        
        // 4. 使用场景演示
        demonstrateUseCases();
    }
    
    /**
     * 基本区别演示
     */
    private static void demonstrateBasicDifference() {
        System.out.println("1. 基本区别演示：\n");
        
        // Collection 接口
        Collection<String> collection = new ArrayList<>();
        collection.add("苹果");
        collection.add("香蕉");
        collection.add("橙子");
        
        System.out.println("Collection 内容：" + collection);
        System.out.println("Collection 大小：" + collection.size());
        System.out.println("Collection 是否包含'苹果'：" + collection.contains("苹果"));
        
        // ❌ Collection 没有索引相关的方法
        // String first = collection.get(0);  // 编译错误！
        // collection.add(0, "葡萄");         // 编译错误！
        
        System.out.println("\nCollection 只能遍历，不能通过索引访问：");
        for (String item : collection) {
            System.out.println("  " + item);
        }
        
        // List 接口
        List<String> list = new ArrayList<>();
        list.add("苹果");
        list.add("香蕉");
        list.add("橙子");
        
        System.out.println("\nList 内容：" + list);
        System.out.println("List 大小：" + list.size());
        System.out.println("List 是否包含'苹果'：" + list.contains("苹果"));
        
        // ✅ List 有索引相关的方法
        String first = list.get(0);
        System.out.println("List 第一个元素（通过索引）：" + first);
        System.out.println("'苹果'的索引：" + list.indexOf("苹果"));
        
        list.add(0, "葡萄");  // 在开头插入
        System.out.println("在开头插入'葡萄'后：" + list);
        
        list.set(1, "替换");
        System.out.println("替换索引1的元素后：" + list);
        
        System.out.println("\nList 可以通过索引访问：");
        for (int i = 0; i < list.size(); i++) {
            System.out.println("  " + i + ": " + list.get(i));
        }
        
        System.out.println();
    }
    
    /**
     * 方法对比演示
     */
    private static void demonstrateMethodDifference() {
        System.out.println("2. 方法对比演示：\n");
        
        // 共同的方法（都继承自 Collection）
        System.out.println("共同的方法（Collection 和 List 都有）：");
        System.out.println("  - add(E e)");
        System.out.println("  - remove(Object o)");
        System.out.println("  - size()");
        System.out.println("  - isEmpty()");
        System.out.println("  - contains(Object o)");
        System.out.println("  - clear()");
        
        // List 特有的方法
        System.out.println("\nList 特有的方法（Collection 没有）：");
        System.out.println("  - add(int index, E element)  // 在指定位置插入");
        System.out.println("  - get(int index)             // 获取指定位置的元素");
        System.out.println("  - set(int index, E element) // 替换指定位置的元素");
        System.out.println("  - remove(int index)          // 删除指定位置的元素");
        System.out.println("  - indexOf(Object o)         // 查找元素的索引");
        System.out.println("  - lastIndexOf(Object o)     // 查找元素最后出现的索引");
        System.out.println("  - subList(int from, int to) // 获取子列表");
        
        System.out.println();
    }
    
    /**
     * 多态使用演示
     */
    private static void demonstratePolymorphism() {
        System.out.println("3. 多态使用演示：\n");
        
        // List 是 Collection 的子接口，可以自动向上转型
        List<String> list = new ArrayList<>();
        Collection<String> collection = list;  // ✅ 自动向上转型
        
        collection.add("苹果");
        collection.add("香蕉");
        
        System.out.println("Collection 引用指向 List 对象：" + collection);
        
        // 但 Collection 引用不能调用 List 特有的方法
        // collection.get(0);  // 编译错误！需要强制转换
        
        // 需要强制转换回 List 才能使用 List 的方法
        if (collection instanceof List) {
            List<String> list2 = (List<String>) collection;
            System.out.println("转换回 List 后可以使用 get 方法：" + list2.get(0));
        }
        
        System.out.println();
    }
    
    /**
     * 使用场景演示
     */
    private static void demonstrateUseCases() {
        System.out.println("4. 使用场景演示：\n");
        
        // 场景 1：方法参数使用 Collection（可以接受 List 或 Set）
        System.out.println("场景 1：方法参数使用 Collection（通用）");
        List<String> list = new ArrayList<>();
        list.add("苹果");
        list.add("香蕉");
        
        Set<String> set = new HashSet<>();
        set.add("苹果");
        set.add("香蕉");
        
        printCollection(list);  // ✅ 可以传入 List
        printCollection(set);   // ✅ 可以传入 Set
        
        // 场景 2：方法参数使用 List（只能接受 List）
        System.out.println("\n场景 2：方法参数使用 List（只能接受 List）");
        printList(list);        // ✅ 可以传入 List
        // printList(set);      // ❌ 编译错误！Set 不是 List
        
        System.out.println();
    }
    
    /**
     * 使用 Collection 作为参数（可以接受 List 或 Set）
     */
    private static void printCollection(Collection<String> collection) {
        System.out.println("  打印 Collection（通用方法）：");
        for (String item : collection) {
            System.out.println("    " + item);
        }
    }
    
    /**
     * 使用 List 作为参数（只能接受 List）
     */
    private static void printList(List<String> list) {
        System.out.println("  打印 List（可以使用索引）：");
        for (int i = 0; i < list.size(); i++) {
            System.out.println("    " + i + ": " + list.get(i));
        }
    }
}
