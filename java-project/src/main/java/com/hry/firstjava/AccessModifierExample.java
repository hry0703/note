package com.hry.firstjava;

/**
 * 访问修饰符详解：演示同类、同包、子类、不同包的概念
 */
public class AccessModifierExample {
    public static void main(String[] args) {
        System.out.println("=== Java 访问修饰符概念详解 ===\n");
        
        // 演示同类访问
        System.out.println("1. 同类访问演示：");
        Parent parent = new Parent();
        parent.testSameClass(); // 同类内部可以访问所有修饰符
        
        System.out.println("\n2. 同包非子类访问演示：");
        SamePackageClass samePackage = new SamePackageClass();
        samePackage.testAccess();
        
        System.out.println("\n3. 同包子类访问演示：");
        SamePackageSubclass samePackageSub = new SamePackageSubclass();
        samePackageSub.testAccess();
    }
}

/**
 * 父类：演示不同访问修饰符
 */
class Parent {
    // 不同访问修饰符的字段
    private String privateField = "private 字段";
    String defaultField = "default 字段";  // 包访问权限（没有修饰符）
    protected String protectedField = "protected 字段";
    public String publicField = "public 字段";
    
    // 不同访问修饰符的方法
    private void privateMethod() {
        System.out.println("  ✅ 这是 private 方法");
    }
    
    void defaultMethod() {
        System.out.println("  ✅ 这是 default 方法");
    }
    
    protected void protectedMethod() {
        System.out.println("  ✅ 这是 protected 方法");
    }
    
    public void publicMethod() {
        System.out.println("  ✅ 这是 public 方法");
    }
    
    /**
     * 同类访问：在同一个类内部访问
     * 同类可以访问所有修饰符的成员（包括 private）
     */
    public void testSameClass() {
        System.out.println("在 Parent 类内部（同类）：");
        System.out.println("  ✅ private 字段：" + privateField);
        System.out.println("  ✅ default 字段：" + defaultField);
        System.out.println("  ✅ protected 字段：" + protectedField);
        System.out.println("  ✅ public 字段：" + publicField);
        privateMethod();      // ✅ 可以访问
        defaultMethod();      // ✅ 可以访问
        protectedMethod();    // ✅ 可以访问
        publicMethod();       // ✅ 可以访问
    }
}

/**
 * 同包非子类：SamePackageClass 和 Parent 在同一个包中，但不是子类
 * 
 * 同包 = 同一个包（package）中的类
 * 例如：com.hry.firstjava 包中的所有类都是"同包"
 */
class SamePackageClass {
    public void testAccess() {
        System.out.println("SamePackageClass 和 Parent 在同一个包中（同包非子类）：");
        Parent parent = new Parent();
        
        // ❌ private：不能访问
        // System.out.println(parent.privateField);  // 编译错误
        // parent.privateMethod();  // 编译错误
        
        // ✅ default：可以访问（因为同包）
        System.out.println("  ✅ default 字段：" + parent.defaultField);
        parent.defaultMethod();
        
        // ✅ protected：可以访问（因为同包）
        // 注意：protected 在同包中，即使不是子类也可以访问！
        System.out.println("  ✅ protected 字段：" + parent.protectedField);
        parent.protectedMethod();
        
        // ✅ public：可以访问
        System.out.println("  ✅ public 字段：" + parent.publicField);
        parent.publicMethod();
    }
}

/**
 * 同包子类：SamePackageSubclass 和 Parent 在同一个包中，并且是子类
 * 
 * 同包子类 = 既是子类（继承关系），又在同一个包中
 */
class SamePackageSubclass extends Parent {
    public void testAccess() {
        System.out.println("SamePackageSubclass 是 Parent 的子类，且在同一个包中（同包子类）：");
        
        // ❌ private：不能访问（即使是子类也不能访问父类的 private）
        // System.out.println(privateField);  // 编译错误
        // privateMethod();  // 编译错误
        
        // ✅ default：可以访问（因为同包）
        System.out.println("  ✅ default 字段：" + defaultField);
        defaultMethod();
        
        // ✅ protected：可以访问（因为既是同包，又是子类）
        System.out.println("  ✅ protected 字段：" + protectedField);
        protectedMethod();
        
        // ✅ public：可以访问
        System.out.println("  ✅ public 字段：" + publicField);
        publicMethod();
    }
}
