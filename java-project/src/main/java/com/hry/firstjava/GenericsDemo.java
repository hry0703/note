package com.hry.firstjava;

import java.util.*;

/**
 * Java 泛型演示
 */
public class GenericsDemo {
    public static void main(String[] args) {
        System.out.println("=== Java 泛型演示 ===\n");
        
        // 1. 泛型类演示
        demonstrateGenericClass();
        
        // 2. 泛型方法演示
        demonstrateGenericMethod();
        
        // 3. 泛型接口演示
        demonstrateGenericInterface();
        
        // 4. 通配符演示
        demonstrateWildcards();
        
        // 5. 实际应用场景
        demonstrateRealWorldScenarios();
    }
    
    /**
     * 泛型类演示
     */
    private static void demonstrateGenericClass() {
        System.out.println("1. 泛型类演示：\n");
        
        // 单个类型参数
        Box<String> stringBox = new Box<>();
        stringBox.setContent("Hello, World!");
        System.out.println("字符串盒子：" + stringBox.getContent());
        
        Box<Integer> intBox = new Box<>();
        intBox.setContent(100);
        System.out.println("整数盒子：" + intBox.getContent());
        
        // 多个类型参数
        Pair<String, Integer> pair = new Pair<>("年龄", 25);
        System.out.println("键值对：" + pair.getKey() + " = " + pair.getValue());
        
        // 泛型类继承
        StringBox stringBox2 = new StringBox();
        stringBox2.setData("测试");
        stringBox2.print();
        
        System.out.println();
    }
    
    /**
     * 泛型方法演示
     */
    private static void demonstrateGenericMethod() {
        System.out.println("2. 泛型方法演示：\n");
        
        // 交换数组元素
        String[] strings = {"apple", "banana", "cherry"};
        System.out.println("交换前：" + Arrays.toString(strings));
        GenericUtils.swap(strings, 0, 2);
        System.out.println("交换后：" + Arrays.toString(strings));
        
        // 查找最大值
        Integer[] numbers = {3, 1, 4, 1, 5, 9, 2, 6};
        Integer max = GenericUtils.max(numbers);
        System.out.println("最大值：" + max);
        
        // 获取第一个元素
        List<String> list = Arrays.asList("a", "b", "c");
        String first = GenericUtils.getFirst(list);
        System.out.println("第一个元素：" + first);
        
        // 数组转列表
        String[] array = {"x", "y", "z"};
        List<String> converted = GenericUtils.arrayToList(array);
        System.out.println("转换后的列表：" + converted);
        
        System.out.println();
    }
    
    /**
     * 泛型接口演示
     */
    private static void demonstrateGenericInterface() {
        System.out.println("3. 泛型接口演示：\n");
        
        // 实现泛型接口（指定具体类型）
        StringContainer stringContainer = new StringContainer();
        stringContainer.add("第一");
        stringContainer.add("第二");
        System.out.println("字符串容器大小：" + stringContainer.size());
        System.out.println("第一个元素：" + stringContainer.get(0));
        
        // 实现泛型接口（保持泛型）
        GenericContainer<Integer> intContainer = new GenericContainer<>();
        intContainer.add(1);
        intContainer.add(2);
        System.out.println("整数容器大小：" + intContainer.size());
        System.out.println("第一个元素：" + intContainer.get(0));
        
        System.out.println();
    }
    
    /**
     * 通配符演示
     */
    private static void demonstrateWildcards() {
        System.out.println("4. 通配符演示：\n");
        
        // 无界通配符
        System.out.println("无界通配符（?）：");
        List<String> strings = Arrays.asList("a", "b", "c");
        List<Integer> numbers = Arrays.asList(1, 2, 3);
        WildcardDemo.printList(strings);   // ✅ 可以
        WildcardDemo.printList(numbers);   // ✅ 可以
        
        // 上界通配符
        System.out.println("\n上界通配符（? extends Number）：");
        List<Integer> integers = Arrays.asList(1, 2, 3);
        List<Double> doubles = Arrays.asList(1.1, 2.2, 3.3);
        WildcardDemo.printNumbers(integers);  // ✅ 可以
        WildcardDemo.printNumbers(doubles);   // ✅ 可以
        
        // 下界通配符
        System.out.println("\n下界通配符（? super Integer）：");
        List<Number> numberList = new ArrayList<>();
        List<Object> objectList = new ArrayList<>();
        WildcardDemo.addNumbers(numberList);  // ✅ 可以
        WildcardDemo.addNumbers(objectList);  // ✅ 可以
        System.out.println("Number 列表：" + numberList);
        System.out.println("Object 列表：" + objectList);
        
        System.out.println();
    }
    
    /**
     * 实际应用场景演示
     */
    private static void demonstrateRealWorldScenarios() {
        System.out.println("5. 实际应用场景演示：\n");
        
        // 场景 1：缓存系统
        System.out.println("场景 1：缓存系统");
        GenericCache<String, String> cache = new GenericCache<>();
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        System.out.println("key1 的值：" + cache.get("key1"));
        System.out.println("缓存大小：" + cache.size());
        
        // 场景 2：工具类
        System.out.println("\n场景 2：工具类");
        List<String> list1 = Arrays.asList("a", "b", "c");
        List<String> list2 = Arrays.asList("x", "y", "z");
        List<String> merged = GenericUtils.merge(list1, list2);
        System.out.println("合并后的列表：" + merged);
        
        // 场景 3：比较器
        System.out.println("\n场景 3：比较器");
        List<Person> people = Arrays.asList(
            new Person("张三", 25),
            new Person("李四", 30),
            new Person("王五", 20)
        );
        System.out.println("排序前：" + people);
        Collections.sort(people, new AgeComparator());
        System.out.println("按年龄排序后：" + people);
        
        System.out.println();
    }
}

/**
 * 泛型类：盒子（单个类型参数）
 */
class Box<T> {
    private T content;
    
    public void setContent(T content) {
        this.content = content;
    }
    
    public T getContent() {
        return content;
    }
}

/**
 * 泛型类：键值对（多个类型参数）
 */
class Pair<K, V> {
    private K key;
    private V value;
    
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
    
    public K getKey() {
        return key;
    }
    
    public V getValue() {
        return value;
    }
}

/**
 * 泛型类继承示例
 */
class GenericBase<T> {
    protected T data;
    
    public void setData(T data) {
        this.data = data;
    }
}

class StringBox extends GenericBase<String> {
    public void print() {
        System.out.println("字符串：" + data);
    }
}

/**
 * 泛型工具类
 */
class GenericUtils {
    /**
     * 泛型方法：交换数组中的两个元素
     */
    public static <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    
    /**
     * 泛型方法：查找数组中的最大值（有界类型参数）
     */
    public static <T extends Comparable<T>> T max(T[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        T max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i].compareTo(max) > 0) {
                max = array[i];
            }
        }
        return max;
    }
    
    /**
     * 泛型方法：获取列表的第一个元素
     */
    public static <T> T getFirst(List<T> list) {
        return list.isEmpty() ? null : list.get(0);
    }
    
    /**
     * 泛型方法：数组转列表
     */
    public static <T> List<T> arrayToList(T[] array) {
        List<T> list = new ArrayList<>();
        for (T item : array) {
            list.add(item);
        }
        return list;
    }
    
    /**
     * 泛型方法：合并两个列表
     */
    public static <T> List<T> merge(List<T> list1, List<T> list2) {
        List<T> result = new ArrayList<>(list1);
        result.addAll(list2);
        return result;
    }
}

/**
 * 泛型接口
 */
interface Container<T> {
    void add(T item);
    T get(int index);
    int size();
}

/**
 * 实现泛型接口（指定具体类型）
 */
class StringContainer implements Container<String> {
    private List<String> list = new ArrayList<>();
    
    @Override
    public void add(String item) {
        list.add(item);
    }
    
    @Override
    public String get(int index) {
        return list.get(index);
    }
    
    @Override
    public int size() {
        return list.size();
    }
}

/**
 * 实现泛型接口（保持泛型）
 */
class GenericContainer<T> implements Container<T> {
    private List<T> list = new ArrayList<>();
    
    @Override
    public void add(T item) {
        list.add(item);
    }
    
    @Override
    public T get(int index) {
        return list.get(index);
    }
    
    @Override
    public int size() {
        return list.size();
    }
}

/**
 * 通配符演示类
 */
class WildcardDemo {
    /**
     * 无界通配符：可以接受任何类型的 List
     */
    public static void printList(List<?> list) {
        for (Object item : list) {
            System.out.println("  元素：" + item);
        }
    }
    
    /**
     * 上界通配符：可以接受 Number 及其子类型的 List
     */
    public static void printNumbers(List<? extends Number> list) {
        for (Number num : list) {
            System.out.println("  数字：" + num);
        }
    }
    
    /**
     * 下界通配符：可以接受 Integer 及其父类型的 List
     */
    public static void addNumbers(List<? super Integer> list) {
        list.add(1);
        list.add(2);
        list.add(3);
    }
}

/**
 * 实际应用：缓存系统
 */
class GenericCache<K, V> {
    private Map<K, V> map = new HashMap<>();
    
    public void put(K key, V value) {
        map.put(key, value);
    }
    
    public V get(K key) {
        return map.get(key);
    }
    
    public int size() {
        return map.size();
    }
}

/**
 * 实际应用：人员类
 */
class Person {
    private String name;
    private int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public String getName() {
        return name;
    }
    
    public int getAge() {
        return age;
    }
    
    @Override
    public String toString() {
        return name + "(" + age + ")";
    }
}

/**
 * 实际应用：年龄比较器
 */
class AgeComparator implements Comparator<Person> {
    @Override
    public int compare(Person p1, Person p2) {
        return Integer.compare(p1.getAge(), p2.getAge());
    }
}
