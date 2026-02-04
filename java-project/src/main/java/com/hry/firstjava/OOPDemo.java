package com.hry.firstjava;

/**
 * Java 面向对象编程三大特性演示
 * 1. 封装（Encapsulation）
 * 2. 继承（Inheritance）
 * 3. 多态（Polymorphism）
 */
public class OOPDemo {
    public static void main(String[] args) {
        System.out.println("=== Java 面向对象编程三大特性演示 ===\n");
        
        // ========== 1. 封装演示 ==========
        System.out.println("1. 封装（Encapsulation）演示：");
        Student student = new Student("张三", 20);
        // 使用公共方法访问私有属性
        System.out.println("姓名：" + student.getName());
        System.out.println("年龄：" + student.getAge());
        // 通过方法修改属性，可以添加验证逻辑
        student.setAge(25);
        System.out.println("修改后年龄：" + student.getAge());
        System.out.println();
        
        // ========== 2. 继承演示 ==========
        System.out.println("2. 继承（Inheritance）演示：");
        Dog dog = new Dog("旺财", 3);
        Cat cat = new Cat("小花", 2);
        
        // 子类可以使用父类的方法
        dog.eat();  // 继承自父类 Animal
        dog.bark(); // 子类特有的方法
        
        cat.eat();  // 继承自父类 Animal
        cat.meow(); // 子类特有的方法
        System.out.println();
        
        // ========== 3. 多态演示 ==========
        System.out.println("3. 多态（Polymorphism）演示：");
        
        // 多态：父类引用指向子类对象
        Animal animal1 = new Dog("小黑", 4);
        Animal animal2 = new Cat("小白", 1);
        
        // 运行时多态：调用的是子类重写的方法
        animal1.makeSound(); // 输出：汪汪汪！
        animal2.makeSound(); // 输出：喵喵喵！
        
        // 多态数组
        Animal[] animals = {
            new Dog("金毛", 5),
            new Cat("波斯猫", 3),
            new Dog("哈士奇", 2)
        };
        
        System.out.println("\n遍历动物数组（多态）：");
        for (Animal animal : animals) {
            animal.makeSound(); // 每个对象调用自己的实现
        }
    }
}

// ========== 1. 封装（Encapsulation）==========

/**
 * 封装示例：Student 类
 * 
 * 封装的核心思想：
 * 1. 将数据（属性）和方法（行为）包装在类中
 * 2. 隐藏内部实现细节，只暴露必要的接口
 * 3. 通过访问修饰符控制访问权限
 */
class Student {
    // 私有属性：外部不能直接访问
    private String name;
    private int age;
    
    // 构造方法
    public Student(String name, int age) {
        this.name = name;
        // 在构造方法中也可以进行验证
        if (age > 0 && age < 150) {
            this.age = age;
        } else {
            this.age = 0;
            System.out.println("年龄无效，设置为 0");
        }
    }
    
    // 公共的 getter 方法：提供只读访问
    public String getName() {
        return name;
    }
    
    public int getAge() {
        return age;
    }
    
    // 公共的 setter 方法：提供受控的修改
    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
    }
    
    public void setAge(int age) {
        // 封装的好处：可以在修改时添加验证逻辑
        if (age > 0 && age < 150) {
            this.age = age;
        } else {
            System.out.println("年龄必须在 0-150 之间");
        }
    }
    
    // 公共方法：提供功能接口
    public void study() {
        System.out.println(name + " 正在学习...");
    }
}

// ========== 2. 继承（Inheritance）==========

/**
 * 父类：Animal（动物）
 * 
 * 继承的核心思想：
 * 1. 子类继承父类的属性和方法
 * 2. 子类可以添加新的属性和方法
 * 3. 子类可以重写（override）父类的方法
 */
class Animal {
    protected String name;  // protected：子类可以访问
    protected int age;
    
    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    // 父类的通用方法
    public void eat() {
        System.out.println(name + " 正在吃东西...");
    }
    
    public void sleep() {
        System.out.println(name + " 正在睡觉...");
    }
    
    // 虚方法：子类可以重写
    public void makeSound() {
        System.out.println(name + " 发出声音...");
    }
    
    public void displayInfo() {
        System.out.println("动物名称：" + name + "，年龄：" + age);
    }
}

/**
 * 子类：Dog（狗）
 * 继承自 Animal 类
 */
class Dog extends Animal {
    // 子类可以添加新的属性
    private String breed; // 品种
    
    public Dog(String name, int age) {
        super(name, age); // 调用父类构造方法
        this.breed = "未知";
    }
    
    public Dog(String name, int age, String breed) {
        super(name, age);
        this.breed = breed;
    }
    
    // 子类特有的方法
    public void bark() {
        System.out.println(name + " 汪汪汪！");
    }
    
    // 重写父类方法：实现多态
    @Override
    public void makeSound() {
        bark(); // 调用自己的方法
    }
    
    // 子类可以添加新方法
    public void fetch() {
        System.out.println(name + " 正在捡球...");
    }
    
    // 使用子类特有的属性
    public String getBreed() {
        return breed;
    }
}

/**
 * 子类：Cat（猫）
 * 继承自 Animal 类
 */
class Cat extends Animal {
    public Cat(String name, int age) {
        super(name, age);
    }
    
    // 子类特有的方法
    public void meow() {
        System.out.println(name + " 喵喵喵！");
    }
    
    // 重写父类方法：实现多态
    @Override
    public void makeSound() {
        meow(); // 调用自己的方法
    }
    
    // 子类可以添加新方法
    public void climb() {
        System.out.println(name + " 正在爬树...");
    }
}

// ========== 3. 多态（Polymorphism）==========

/**
 * 多态的核心思想：
 * 1. 同一个接口，不同的实现
 * 2. 父类引用可以指向子类对象
 * 3. 运行时根据实际对象类型调用相应的方法
 * 
 * 多态的实现方式：
 * - 方法重写（Override）：子类重写父类方法
 * - 方法重载（Overload）：同一个类中方法名相同，参数不同
 */

/**
 * 形状接口：演示接口多态
 */
interface Shape {
    double calculateArea();  // 计算面积
    double calculatePerimeter(); // 计算周长
}

/**
 * 圆形类：实现 Shape 接口
 */
class Circle implements Shape {
    private double radius;
    
    public Circle(double radius) {
        this.radius = radius;
    }
    
    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }
    
    @Override
    public double calculatePerimeter() {
        return 2 * Math.PI * radius;
    }
}

/**
 * 矩形类：实现 Shape 接口
 */
class Rectangle implements Shape {
    private double width;
    private double height;
    
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double calculateArea() {
        return width * height;
    }
    
    @Override
    public double calculatePerimeter() {
        return 2 * (width + height);
    }
}
