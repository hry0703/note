package com.hry.firstjava;

import java.lang.reflect.*;
import java.util.Arrays;

/**
 * Java 反射机制演示
 */
public class ReflectionDemo {
    public static void main(String[] args) {
        System.out.println("=== Java 反射机制演示 ===\n");
        
        // 1. 获取 Class 对象
        demonstrateGetClass();
        
        // 2. 获取类的信息
        demonstrateClassInfo();
        
        // 3. 动态创建对象
        demonstrateCreateObject();
        
        // 4. 动态调用方法
        demonstrateInvokeMethod();
        
        // 5. 访问字段
        demonstrateAccessField();
        
        // 6. 动态加载
        demonstrateDynamicLoading();
        
        // 7. 实际应用场景
        demonstrateRealWorldScenarios();
    }
    
    /**
     * 获取 Class 对象演示
     */
    private static void demonstrateGetClass() {
        System.out.println("1. 获取 Class 对象演示：\n");
        
        // 方式 1：通过类名获取（推荐）
        Class<String> clazz1 = String.class;
        System.out.println("方式 1（类名）：" + clazz1.getName());
        
        // 方式 2：通过对象获取
        String str = "hello";
        Class<?> clazz2 = str.getClass();
        System.out.println("方式 2（对象）：" + clazz2.getName());
        
        // 方式 3：通过类名字符串获取（动态加载）
        try {
            Class<?> clazz3 = Class.forName("java.lang.String");
            System.out.println("方式 3（字符串）：" + clazz3.getName());
        } catch (ClassNotFoundException e) {
            System.out.println("类不存在：" + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * 获取类的信息演示
     */
    private static void demonstrateClassInfo() {
        System.out.println("2. 获取类的信息演示：\n");
        
        Class<?> clazz = ReflectionPerson.class;
        
        // 类名
        System.out.println("类名：" + clazz.getName());
        System.out.println("简单类名：" + clazz.getSimpleName());
        
        // 包名
        Package pkg = clazz.getPackage();
        if (pkg != null) {
            System.out.println("包名：" + pkg.getName());
        }
        
        // 父类
        Class<?> superClass = clazz.getSuperclass();
        System.out.println("父类：" + (superClass != null ? superClass.getSimpleName() : "无"));
        
        // 实现的接口
        Class<?>[] interfaces = clazz.getInterfaces();
        System.out.println("实现的接口：" + Arrays.toString(interfaces));
        
        // 修饰符
        int modifiers = clazz.getModifiers();
        System.out.println("是否公共：" + Modifier.isPublic(modifiers));
        System.out.println("是否抽象：" + Modifier.isAbstract(modifiers));
        
        // 构造方法
        System.out.println("\n构造方法：");
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            System.out.println("  " + constructor);
        }
        
        // 方法
        System.out.println("\n方法：");
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println("  " + method.getName() + "()");
        }
        
        // 字段
        System.out.println("\n字段：");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println("  " + field.getType().getSimpleName() + " " + field.getName());
        }
        
        System.out.println();
    }
    
    /**
     * 动态创建对象演示
     */
    private static void demonstrateCreateObject() {
        System.out.println("3. 动态创建对象演示：\n");
        
        try {
            Class<?> clazz = ReflectionPerson.class;
            
            // 方式 1：使用无参构造方法
            System.out.println("方式 1：无参构造方法");
            ReflectionPerson person1 = (ReflectionPerson) clazz.getDeclaredConstructor().newInstance();
            System.out.println("创建的对象：" + person1);
            
            // 方式 2：使用有参构造方法
            System.out.println("\n方式 2：有参构造方法");
            Constructor<?> constructor = clazz.getConstructor(String.class, int.class);
            ReflectionPerson person2 = (ReflectionPerson) constructor.newInstance("张三", 25);
            System.out.println("创建的对象：" + person2);
            
        } catch (Exception e) {
            System.out.println("创建对象失败：" + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println();
    }
    
    /**
     * 动态调用方法演示
     */
    private static void demonstrateInvokeMethod() {
        System.out.println("4. 动态调用方法演示：\n");
        
        try {
            ReflectionPerson person = new ReflectionPerson("李四", 30);
            Class<?> clazz = person.getClass();
            
            // 调用公共方法
            System.out.println("调用公共方法：");
            Method getNameMethod = clazz.getMethod("getName");
            String name = (String) getNameMethod.invoke(person);
            System.out.println("姓名：" + name);
            
            // 调用有参数的方法
            System.out.println("\n调用有参数的方法：");
            Method setAgeMethod = clazz.getMethod("setAge", int.class);
            setAgeMethod.invoke(person, 35);
            System.out.println("修改后的年龄：" + person.getAge());
            
            // 调用私有方法
            System.out.println("\n调用私有方法：");
            Method privateMethod = clazz.getDeclaredMethod("privateMethod");
            privateMethod.setAccessible(true);  // 绕过访问控制
            privateMethod.invoke(person);
            
            // 调用静态方法
            System.out.println("\n调用静态方法：");
            Method staticMethod = clazz.getMethod("staticMethod");
            staticMethod.invoke(null);  // 静态方法，第一个参数为 null
            
        } catch (Exception e) {
            System.out.println("调用方法失败：" + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println();
    }
    
    /**
     * 访问字段演示
     */
    private static void demonstrateAccessField() {
        System.out.println("5. 访问字段演示：\n");
        
        try {
            ReflectionPerson person = new ReflectionPerson("王五", 40);
            Class<?> clazz = person.getClass();
            
            // 读取公共字段
            System.out.println("读取公共字段：");
            Field publicField = clazz.getField("publicField");
            String publicValue = (String) publicField.get(person);
            System.out.println("公共字段值：" + publicValue);
            
            // 修改公共字段
            System.out.println("\n修改公共字段：");
            publicField.set(person, "新值");
            System.out.println("修改后的值：" + publicField.get(person));
            
            // 读取私有字段
            System.out.println("\n读取私有字段：");
            Field nameField = clazz.getDeclaredField("name");
            nameField.setAccessible(true);  // 绕过访问控制
            String name = (String) nameField.get(person);
            System.out.println("私有字段 name：" + name);
            
            // 修改私有字段
            System.out.println("\n修改私有字段：");
            nameField.set(person, "赵六");
            System.out.println("修改后的 name：" + nameField.get(person));
            
        } catch (Exception e) {
            System.out.println("访问字段失败：" + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println();
    }
    
    /**
     * 动态加载演示
     */
    private static void demonstrateDynamicLoading() {
        System.out.println("6. 动态加载演示：\n");
        
        // 动态加载不同的类
        String[] classNames = {
            "java.lang.String",
            "java.util.ArrayList",
            "com.hry.firstjava.ReflectionPerson"
        };
        
        for (String className : classNames) {
            try {
                Class<?> clazz = Class.forName(className);
                System.out.println("成功加载类：" + clazz.getName());
                System.out.println("  简单类名：" + clazz.getSimpleName());
            } catch (ClassNotFoundException e) {
                System.out.println("加载类失败：" + className);
            }
        }
        
        System.out.println();
    }
    
    /**
     * 实际应用场景演示
     */
    private static void demonstrateRealWorldScenarios() {
        System.out.println("7. 实际应用场景演示：\n");
        
        // 场景 1：JSON 序列化
        System.out.println("场景 1：JSON 序列化");
        ReflectionPerson person = new ReflectionPerson("测试", 20);
        String json = JsonSerializer.toJson(person);
        System.out.println("JSON：" + json);
        
        // 场景 2：注解处理
        System.out.println("\n场景 2：注解处理");
        Class<?> clazz = ReflectionPerson.class;
        if (clazz.isAnnotationPresent(Entity.class)) {
            Entity entity = clazz.getAnnotation(Entity.class);
            System.out.println("实体类，表名：" + entity.tableName());
        }
        
        // 场景 3：方法调用统计
        System.out.println("\n场景 3：方法调用统计");
        try {
            ReflectionPerson testPerson = new ReflectionPerson("统计", 25);
            Method method = clazz.getMethod("getName");
            
            long start = System.currentTimeMillis();
            for (int i = 0; i < 10000; i++) {
                method.invoke(testPerson);
            }
            long end = System.currentTimeMillis();
            System.out.println("反射调用 10000 次耗时：" + (end - start) + " 毫秒");
            
            start = System.currentTimeMillis();
            for (int i = 0; i < 10000; i++) {
                testPerson.getName();
            }
            end = System.currentTimeMillis();
            System.out.println("直接调用 10000 次耗时：" + (end - start) + " 毫秒");
            
        } catch (Exception e) {
            System.out.println("统计失败：" + e.getMessage());
        }
        
        System.out.println();
    }
}

/**
 * 测试类：ReflectionPerson（用于反射演示）
 */
@Entity(tableName = "person")
class ReflectionPerson {
    private String name;
    private int age;
    public String publicField = "公共字段";
    
    public ReflectionPerson() {
        this.name = "默认";
        this.age = 0;
    }
    
    public ReflectionPerson(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    private void privateMethod() {
        System.out.println("这是私有方法");
    }
    
    public static void staticMethod() {
        System.out.println("这是静态方法");
    }
    
    @Override
    public String toString() {
        return "ReflectionPerson{name='" + name + "', age=" + age + "}";
    }
}

/**
 * 实体注解
 */
@interface Entity {
    String tableName();
}

/**
 * JSON 序列化器（使用反射）
 */
class JsonSerializer {
    public static String toJson(Object obj) {
        if (obj == null) {
            return "null";
        }
        
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        
        for (Field field : fields) {
            // 跳过静态字段
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                if (!first) {
                    json.append(",");
                }
                json.append("\"").append(field.getName()).append("\":");
                
                if (value == null) {
                    json.append("null");
                } else if (value instanceof String) {
                    json.append("\"").append(value).append("\"");
                } else {
                    json.append(value);
                }
                first = false;
            } catch (IllegalAccessException e) {
                // 忽略无法访问的字段
            }
        }
        
        json.append("}");
        return json.toString();
    }
}
