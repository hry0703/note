# JavaScript 基础与进阶 面试题

---

## 一、JavaScript 基础

### 1. 数据类型（原始类型 vs 引用类型）

**问：JavaScript 有哪些数据类型？原始类型和引用类型的区别？**

**答：**

- **原始类型（7种）**：`undefined`、`null`、`boolean`、`number`、`string`、`symbol`、`bigint`
- **引用类型**：`Object`（包括 Array、Function、Date、RegExp 等）

**区别：**

| 对比项 | 原始类型 | 引用类型 |
|--------|----------|----------|
| 存储 | 栈内存，存值本身 | 栈存引用，堆存实际数据 |
| 复制 | 复制值 | 复制引用，指向同一对象 |
| 比较 | 比较值 | 比较引用地址 |
| 可变性 | 不可变 | 可变 |

```javascript
let a = 1;
let b = a;
b = 2;
console.log(a); // 1，互不影响

let obj1 = { x: 1 };
let obj2 = obj1;
obj2.x = 2;
console.log(obj1.x); // 2，指向同一对象
```

---

### 2. `==` 与 `===` 的区别

**问：`==` 和 `===` 有什么区别？**

**答：**

- **`==`（宽松相等）**：会进行类型转换后再比较
- **`===`（严格相等）**：不转换类型，类型不同直接返回 false

```javascript
1 == '1'    // true，'1' 转为数字 1
1 === '1'   // false，类型不同

null == undefined   // true
null === undefined  // false

NaN == NaN   // false（NaN 与任何值都不相等，包括自己）
NaN === NaN  // false
```

**建议**：开发中优先使用 `===`，避免隐式转换带来的问题。

---

### 3. 闭包原理与应用场景

**问：什么是闭包？有什么应用场景？**

**答：**

---

#### 一、定义

**闭包**：函数可以访问并「记住」其**词法作用域**，即使该函数在其词法作用域**之外**执行。

换句话说：**内层函数引用外层函数的变量，且内层函数被导出（return、赋值给全局等）时，就形成了闭包**。

---

#### 二、词法作用域（静态作用域）

JavaScript 采用**词法作用域**：函数的作用域在**定义时**就确定了，由写代码时的位置决定，与调用位置无关。

```javascript
let a = 1;
function outer() {
  let b = 2;
  function inner() {
    console.log(a, b);  // inner 定义时「看到」了 outer 和全局
  }
  return inner;
}
const fn = outer();
fn();  // 1 2 —— 在全局执行，但仍能访问 outer 的 b，这就是闭包
```

---

#### 三、原理：作用域链

每个函数在**创建时**都会生成一条**作用域链**，记录自己能访问的所有外层作用域。

执行 `inner` 时，查找变量 `b` 的过程：
1. 先在 `inner` 自己的作用域找 → 没有
2. 沿作用域链到 `outer` 的作用域找 → 找到 `b`
3. 返回 `b` 的值

**关键**：`outer` 执行完后，其执行上下文会出栈，但 `inner` 仍持有对 `outer` 作用域中变量的**引用**，所以这些变量不会被 GC 回收，形成闭包。

---

#### 四、经典示例

**1. 计数器（数据私有化）**

```javascript
function createCounter() {
  let count = 0;  // 外部无法直接访问
  return function() {
    count++;
    return count;
  };
}
const counter = createCounter();
counter(); // 1
counter(); // 2
// count 被「封闭」在闭包内，只能通过返回的函数修改
```

**2. 循环 + 异步（经典坑）**

```javascript
for (var i = 0; i < 3; i++) {
  setTimeout(() => console.log(i), 100);
}
// 输出：3 3 3 —— 因为 var 是函数作用域，i 共享，setTimeout 执行时 i 已是 3

// 解决：用 let（块级作用域，每次循环新的 i）
for (let i = 0; i < 3; i++) {
  setTimeout(() => console.log(i), 100);
}
// 输出：0 1 2

// 或用 IIFE 创建闭包，保存每次的 i
for (var i = 0; i < 3; i++) {
  (function(j) {
    setTimeout(() => console.log(j), 100);
  })(i);
}
// 输出：0 1 2
```

**3. IIFE 模拟模块**

```javascript
const module = (function() {
  let privateVar = 0;  // 私有变量
  function privateFn() {
    return privateVar;
  }
  return {
    get: () => privateVar,
    add: () => { privateVar++; }
  };
})();
module.add();
module.get();  // 1
// module.privateVar 无法访问
```

---

#### 五、应用场景

| 场景 | 说明 |
|------|------|
| **数据私有化** | 变量只在闭包内可访问，外部无法直接修改 |
| **防抖 / 节流** | 闭包保存 timer 等状态 |
| **柯里化** | 闭包保存已传入的参数 |
| **模块化** | IIFE + 闭包实现私有变量 |
| **回调函数** | 回调中访问外部变量，形成闭包 |

---

#### 六、注意事项

**1. 内存泄漏**

闭包会持有外部变量的引用，若闭包长期存在（如挂在全局、DOM 事件），外部变量不会被回收。

```javascript
function leak() {
  const bigData = new Array(1000000).fill('x');
  return function() {
    console.log(bigData.length);  // bigData 一直被引用
  };
}
const fn = leak();  // bigData 无法被 GC
// 解决：用完后 fn = null，或避免在闭包中持有大对象
```

**2. 循环中的闭包**

用 `var` 时，循环变量共享，需用 `let` 或 IIFE 为每次循环创建独立闭包。

---

#### 七、面试精简版

**定义**：内层函数引用外层变量，且内层函数被导出，形成闭包。  
**原理**：作用域链在定义时确定，闭包持有外层变量引用，使其不被回收。  
**场景**：私有变量、防抖节流、柯里化、模块化。  
**注意**：避免在闭包中持有大对象，用完后解除引用。

---

### 4. 原型与原型链

**问：解释原型和原型链？**

**答：**

- **原型（prototype）**：每个函数都有一个 `prototype` 属性，指向一个对象，该对象是实例的「原型」
- **原型链**：实例通过 `__proto__` 指向其构造函数的 `prototype`，`prototype` 又有自己的 `__proto__`，层层向上直到 `Object.prototype`，形成原型链

```javascript
function Person(name) {
  this.name = name;
}
Person.prototype.sayHi = function() {
  console.log('Hi, ' + this.name);
};

const p = new Person('Tom');
p.sayHi(); // 先在 p 上找，没有则沿 __proto__ 到 Person.prototype
p.__proto__ === Person.prototype; // true
Person.prototype.__proto__ === Object.prototype; // true
```

**查找规则**：访问属性时，先在自身找，没有则沿 `__proto__` 向上查找，直到 `Object.prototype`，再往上为 `null`。

---

### 5. this 指向（call/apply/bind）

**问：this 的指向规则？call、apply、bind 的区别？**

**答：**

**this 指向规则：**

1. **默认绑定**：独立函数调用 → `window`（严格模式为 `undefined`）
2. **隐式绑定**：`obj.fn()` → `obj`
3. **显式绑定**：`call/apply/bind` 指定 this
4. **new 绑定**：`new Fn()` → 新创建的对象
5. **箭头函数**：继承外层 this，无法被修改

```javascript
const obj = {
  name: 'obj',
  fn: function() { console.log(this.name); }
};
obj.fn(); // 'obj'

const fn2 = obj.fn;
fn2(); // undefined（默认绑定，this 为 window）
```

**call / apply / bind：**

| 方法 | 用法 | 区别 |
|------|------|------|
| call | `fn.call(ctx, a, b, c)` | 立即执行，参数逐个传 |
| apply | `fn.apply(ctx, [a, b, c])` | 立即执行，参数为数组 |
| bind | `fn.bind(ctx)(a, b, c)` | 返回新函数，不立即执行 |

---

### 6. 作用域与作用域链

**问：什么是作用域和作用域链？**

**答：**

- **作用域**：变量和函数的可访问范围。分为全局作用域、函数作用域、块级作用域（let/const）
- **作用域链**：函数在定义时确定作用域链，查找变量时从当前作用域向外层逐级查找，直到全局

```javascript
let a = 1;
function outer() {
  let b = 2;
  function inner() {
    let c = 3;
    console.log(a, b, c); // 1 2 3，沿作用域链查找
  }
  inner();
}
```

**注意**：作用域在函数**定义时**确定，与调用位置无关（静态作用域）。

---

### 7. var 与 let 的区别

**问：var 和 let 有什么区别？**

**答：**

| 对比项 | var | let |
|--------|-----|-----|
| **作用域** | 函数作用域 | 块级作用域 |
| **提升** | 会提升，值为 undefined | 会提升，但存在暂时性死区（TDZ） |
| **重复声明** | 允许 | 不允许 |
| **全局挂载** | 会挂到 window | 不会 |

**1. 作用域：函数作用域 vs 块级作用域**

| 对比项 | 函数作用域 | 块级作用域 |
|--------|------------|------------|
| **形成方式** | `function` 声明 | `{}` 代码块 |
| **适用变量** | `var` | `let`、`const` |
| **if/for 内部** | 变量泄漏到整个函数 | 变量只在块内 |
| **典型问题** | 循环变量共享、污染 | 更可控 |

```javascript
// 函数作用域：var 只在 function 内有效，if/for 不形成新作用域
function fn() {
  var a = 1;
  if (true) {
    var b = 2;   // 仍在函数作用域内
  }
  for (var i = 0; i < 3; i++) {
    var c = 3;   // 仍在函数作用域内
  }
  console.log(a, b, c, i);  // 1 2 3 3 —— 都能访问
}

// 块级作用域：let/const 只在块内有效
function fn() {
  let a = 1;
  if (true) {
    let b = 2;
    console.log(a, b);  // 1 2
  }
  console.log(a);   // 1
  console.log(b);   // ReferenceError: b is not defined
}

// 单独的 {} 也是块
{
  let x = 1;
  const y = 2;
}
console.log(x);  // ReferenceError
```

**2. 变量提升与暂时性死区**

```javascript
// var：提升后为 undefined
console.log(a);  // undefined
var a = 1;

// let：提升但存在 TDZ，访问会报错
console.log(b);  // ReferenceError: Cannot access 'b' before initialization
let b = 1;
```

**3. 循环中的表现（常考）**

```javascript
// var：循环变量共享
for (var i = 0; i < 3; i++) {
  setTimeout(() => console.log(i), 100);
}
// 输出：3 3 3

// let：每次循环都是新的块级作用域
for (let i = 0; i < 3; i++) {
  setTimeout(() => console.log(i), 100);
}
// 输出：0 1 2
```

**4. 全局对象**

```javascript
var g1 = 1;
let g2 = 2;
console.log(window.g1);  // 1
console.log(window.g2);   // undefined
```

**总结**：`let` 有块级作用域、TDZ、禁止重复声明，推荐用 `let`/`const` 替代 `var`。

---

### 8. 深拷贝 vs 浅拷贝

**问：深拷贝和浅拷贝的区别？如何实现深拷贝？**

**答：**

- **浅拷贝**：只复制第一层，嵌套对象仍是引用
- **深拷贝**：递归复制所有层级，得到完全独立的对象

```javascript
// 浅拷贝
const obj = { a: 1, b: { c: 2 } };
const shallow = { ...obj };
shallow.b.c = 3;
console.log(obj.b.c); // 3，被影响

// 深拷贝
const deep = JSON.parse(JSON.stringify(obj)); // 简单方式，但有局限
```

**深拷贝实现注意点：**

- `JSON.parse(JSON.stringify())` 无法处理函数、undefined、循环引用、Date、RegExp 等
- 完整实现需要递归 + 类型判断 + 循环引用处理（用 WeakMap）

```javascript
function deepClone(obj, map = new WeakMap()) {
  if (obj === null || typeof obj !== 'object') return obj;
  if (map.has(obj)) return map.get(obj);
  
  const clone = Array.isArray(obj) ? [] : {};
  map.set(obj, clone);
  
  for (let key in obj) {
    if (obj.hasOwnProperty(key)) {
      clone[key] = deepClone(obj[key], map);
    }
  }
  return clone;
}
```

---

## 二、JavaScript 进阶

### 1. 事件循环（Event Loop）

**问：解释 JavaScript 的事件循环机制？**

**答：**

JavaScript 是单线程的，通过事件循环实现异步。

**执行顺序：**

1. 执行同步代码（调用栈）
2. 调用栈清空后，检查微任务队列，全部执行完
3. 执行一个宏任务
4. 再检查微任务队列，全部执行完
5. 重复 3、4

**宏任务**：`setTimeout`、`setInterval`、I/O、`setImmediate`（Node）、UI 渲染  
**微任务**：`Promise.then`、`queueMicrotask`、`MutationObserver`

```javascript
console.log(1);
setTimeout(() => console.log(2), 0);
Promise.resolve().then(() => console.log(3));
console.log(4);
// 输出：1 4 3 2
```

---

### 2. 宏任务与微任务

**问：宏任务和微任务的区别？执行顺序？**

**答：**

- **宏任务**：由宿主环境（浏览器/Node）发起的任务，每次事件循环执行一个
- **微任务**：由 JS 引擎发起的任务，在每个宏任务结束后、下一个宏任务前，会清空当前所有微任务

**顺序**：同步代码 → 微任务 → 宏任务 → 微任务 → 宏任务 → ...

---

### 3. Promise 原理与手写实现

**问：手写一个简易 Promise？**

**答：**

```javascript
class MyPromise {
  constructor(executor) {
    this.state = 'pending';
    this.value = undefined;
    this.reason = undefined;
    this.onFulfilledCallbacks = [];
    this.onRejectedCallbacks = [];

    const resolve = (value) => {
      if (this.state === 'pending') {
        this.state = 'fulfilled';
        this.value = value;
        this.onFulfilledCallbacks.forEach(fn => fn());
      }
    };

    const reject = (reason) => {
      if (this.state === 'pending') {
        this.state = 'rejected';
        this.reason = reason;
        this.onRejectedCallbacks.forEach(fn => fn());
      }
    };

    try {
      executor(resolve, reject);
    } catch (e) {
      reject(e);
    }
  }

  then(onFulfilled, onRejected) {
    onFulfilled = typeof onFulfilled === 'function' ? onFulfilled : v => v;
    onRejected = typeof onRejected === 'function' ? onRejected : err => { throw err };

    const promise2 = new MyPromise((resolve, reject) => {
      if (this.state === 'fulfilled') {
        setTimeout(() => {
          try {
            const x = onFulfilled(this.value);
            resolve(x);
          } catch (e) {
            reject(e);
          }
        });
      }
      if (this.state === 'rejected') {
        setTimeout(() => {
          try {
            const x = onRejected(this.reason);
            resolve(x);
          } catch (e) {
            reject(e);
          }
        });
      }
      if (this.state === 'pending') {
        this.onFulfilledCallbacks.push(() => {
          setTimeout(() => {
            try {
              const x = onFulfilled(this.value);
              resolve(x);
            } catch (e) {
              reject(e);
            }
          });
        });
        this.onRejectedCallbacks.push(() => {
          setTimeout(() => {
            try {
              const x = onRejected(this.reason);
              resolve(x);
            } catch (e) {
              reject(e);
            }
          });
        });
      }
    });
    return promise2;
  }
}
```

**核心点**：状态不可逆、then 链式调用、异步执行（用 setTimeout 模拟微任务）。

---

### 4. async/await 原理

**问：async/await 的原理？和 Promise 的关系？**

**答：**

- `async` 函数返回一个 Promise，内部返回值会被包装成 `Promise.resolve`
- `await` 会暂停函数执行，等待后面的 Promise 完成，再继续执行
- `await` 后面的代码相当于放在 `Promise.then` 里

```javascript
async function foo() {
  const a = await Promise.resolve(1);
  console.log(a);
  return a + 1;
}
// 等价于
function foo() {
  return Promise.resolve(1).then(a => {
    console.log(a);
    return a + 1;
  });
}
```

**错误处理**：`await` 的 Promise 被 reject 时，会抛出异常，需要用 try/catch 或 `.catch()` 捕获。

---

### 5. 防抖与节流

**问：防抖和节流的区别？手写实现？**

**答：**

| 对比 | 防抖（debounce） | 节流（throttle） |
|------|------------------|------------------|
| 含义 | 多次触发只执行最后一次 | 多次触发按固定间隔执行 |
| 场景 | 搜索输入、窗口 resize | 滚动、按钮点击 |

```javascript
// 防抖：延迟执行，期间再次触发则重新计时
function debounce(fn, delay) {
  let timer = null;
  return function(...args) {
    clearTimeout(timer);
    timer = setTimeout(() => fn.apply(this, args), delay);
  };
}

// 节流：固定间隔内只执行一次
function throttle(fn, delay) {
  let last = 0;
  return function(...args) {
    const now = Date.now();
    if (now - last >= delay) {
      last = now;
      fn.apply(this, args);
    }
  };
}
```

---

### 6. 事件委托

**问：什么是事件委托？有什么好处？**

**答：**

**定义**：把子元素的事件绑定到父元素上，利用事件冒泡统一处理。

```javascript
// 不委托：每个 li 都绑一次
document.querySelectorAll('li').forEach(li => {
  li.addEventListener('click', handler);
});

// 委托：只在 ul 上绑一次
document.querySelector('ul').addEventListener('click', (e) => {
  if (e.target.tagName === 'LI') {
    handler(e);
  }
});
```

**好处**：

- 减少监听器数量，节省内存
- 动态添加的子元素无需重新绑定
- 代码更简洁

---

### 7. 模块化（CommonJS、ES Module）

**问：CommonJS 和 ES Module 的区别？**

**答：**

| 对比项 | CommonJS | ES Module |
|--------|----------|-----------|
| 加载时机 | 运行时加载 | 编译时静态分析 |
| 输出 | 值的拷贝 | 值的引用 |
| this | 指向当前模块 | 指向 undefined |
| 循环依赖 | 可能拿到未完成的值 | 有提升机制，相对安全 |

```javascript
// CommonJS
const { a } = require('./a');  // 同步
module.exports = { b };

// ES Module
import { a } from './a';  // 静态，可 tree-shaking
export { b };
```

**CommonJS 输出拷贝**：模块内部变化不会影响已导入的值；**ES Module 输出引用**：导入的是只读引用，模块内变化会反映到导入方。
