# Dongxin Test Framework

## Features
- Write a simple test case using only @TestMethod Annotation
- Multithread test case support
- Asynchronous and Synchronous execution of test cases
- Simulation of concurrent environment support
- Component management & Dependency injection

## Usage
### Basic Test Class and Method
```Java
@TestClass
class Test {
    private SharedJedisFactory factory;
    @InitMethod
    void init() {
        factory = LocalJedisFactory.getJedisFactory();
    }

    @TestMethod
    void testBasic() {
        SharedJedis jedis = factory.getJedis();
        System.out.println(jedis.get("Hello"));
        jedis.close();
    }
}
```
### Concurrent Environment Simulation

```Java
import pro.boyu.dongxin.framework.annotations.TestClass;
import pro.boyu.dongxin.framework.annotations.TestMethod;

@TestClass
class TestConcurrent {
    @TestMethod(threadsNum = 10, repeatTime = 9000, maxTime = 3)
    void testConcurrent(RedisCenter center) {
        Assert.assertEquals("World", center.get("Hello"));
    }
}
```
### Run Integration Test

```java
import pro.boyu.dongxin.framework.TestApplication;

public class App {
    public static void main(String[] args) {
        TestApplication.run(App.class);
    }
}
```

## Update
- impl by zhang ying

## Authors
- [Zhang Ying](https://github.com/shadoowz97)
- [Jerry Yang](https://boyu.pro)
- [Li Dongxin](#)

