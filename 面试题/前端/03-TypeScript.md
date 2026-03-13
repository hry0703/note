# TypeScript 面试题

---

## 1. 基础类型与接口

**问：TypeScript 有哪些基础类型？interface 和 type 的区别？**

**答：**

### 基础类型

```typescript
let a: number = 1;
let b: string = 'hello';
let c: boolean = true;
let d: null = null;
let e: undefined = undefined;
let f: symbol = Symbol('a');
let g: bigint = 100n;

let arr: number[] = [1, 2, 3];
let tuple: [string, number] = ['a', 1];
let fn: (a: number) => string = (a) => String(a);
```

### interface vs type

| 对比项 | interface | type |
|--------|-----------|------|
| 扩展 | `extends` | 交叉类型 `&` |
| 声明合并 | 支持同名合并 | 不支持 |
| 适用场景 | 对象形状 | 联合、交叉、基础类型等 |

```typescript
interface User {
  name: string;
}
interface User {
  age: number;  // 合并为 { name: string; age: number }
}

type ID = string | number;
type Point = { x: number; y: number };
```

---

## 2. 泛型

**问：什么是泛型？常见用法？**

**答：**

泛型用于在定义函数、类或接口时**延迟指定类型**，由调用时传入。

```typescript
function identity<T>(arg: T): T {
  return arg;
}
identity<number>(1);
identity('hello');  // 自动推断为 string

interface ApiResponse<T> {
  code: number;
  data: T;
}
const res: ApiResponse<User> = { code: 0, data: user };

// 泛型约束
function getProp<T, K extends keyof T>(obj: T, key: K): T[K] {
  return obj[key];
}
```

**常见场景**：API 响应、工具函数、组件 Props 等。

---

## 3. 高级类型（联合、交叉、映射等）

**问：联合类型、交叉类型、映射类型是什么？**

**答：**

### 联合类型（Union）

```typescript
type Status = 'pending' | 'success' | 'error';
type ID = string | number;

function printId(id: ID) {
  if (typeof id === 'string') {
    console.log(id.toUpperCase());
  } else {
    console.log(id.toFixed(2));
  }
}
```

### 交叉类型（Intersection）

```typescript
type A = { a: number };
type B = { b: string };
type C = A & B;  // { a: number; b: string }
```

### 映射类型（Mapped）

```typescript
type Readonly<T> = {
  readonly [P in keyof T]: T[P];
};

type Partial<T> = {
  [P in keyof T]?: T[P];
};

type Pick<T, K extends keyof T> = {
  [P in K]: T[P];
};
```

### 条件类型

```typescript
type IsString<T> = T extends string ? true : false;
type A = IsString<'a'>;  // true
type B = IsString<123>;  // false
```

---

## 4. 类型体操常见题

**问：实现 Pick、Omit、Partial、Required？**

**答：**

```typescript
// Pick：选取部分属性
type MyPick<T, K extends keyof T> = {
  [P in K]: T[P];
};

// Omit：排除部分属性
type MyOmit<T, K extends keyof any> = Pick<T, Exclude<keyof T, K>>;

// Partial：所有属性可选
type MyPartial<T> = {
  [P in keyof T]?: T[P];
};

// Required：所有属性必选
type MyRequired<T> = {
  [P in keyof T]-?: T[P];
};

// 递归 Partial（深度可选）
type DeepPartial<T> = {
  [P in keyof T]?: T[P] extends object ? DeepPartial<T[P]> : T[P];
};
```
