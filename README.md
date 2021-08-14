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
@TestClass
class TestConcurrent {
    private SharedJedisFactory factory;
    @InitMethod
    void init() {
        factory = LocalJedisFactory.getJedisFactory();
    }

    @TestMethod
    void testConcurrent() {
        ParallelExecutors.exec(() -> {
            SharedJedis jedis = factory.getJedis();
            System.out.println(jedis.get("Hello"));
            jedis.close();
        })  
    }
}
```
### Run Integration Test
```java
public class App {
    public static void main(String[] args) {
        OrderedPackageLoader.run(App.class);
    }
}
```

## Update
- impl by zhang ying

## Authors
- [Zhang Ying](#)
- [Jerry Yang](#)
- [Li Dongxin](#)

