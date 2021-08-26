# Dongxin Test Framework

## Features
- Write a simple test case using only @TestMethod Annotation
- Multithreading test case support
- Asynchronous and Synchronous execution of test cases
- Simulation of concurrent environment support
- Component management & Dependency injection

## Usage
### Basic Test Class and Method
Use @TestInit annotation for initialization and @TestMethod for test case
```Java
import pro.boyu.dongxin.framework.annotations.TestClass;
import pro.boyu.dongxin.framework.annotations.TestInit;
import pro.boyu.dongxin.framework.annotations.TestMethod;

@TestClass
class Test {
    private SharedJedisFactory factory;

    @TestInit
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
### Log Message Inside the Test Case
> Note: You are not recommended to print output text in console inside test case method, you should inject a ExecutorSubject to log instead.

You should use @CaseSubject with ExecutorSubject type parameter declaration in each test method, and use `subject.updateData()` to print your log inside the test method.

```java
import pro.boyu.dongxin.framework.annotations.CaseSubject;
import pro.boyu.dongxin.framework.annotations.TestClass;
import pro.boyu.dongxin.framework.annotations.TestMethod;
import pro.boyu.dongxin.framework.constenum.TestCaseState;
import pro.boyu.dongxin.framework.infobean.ExecutionInfo;
import pro.boyu.dongxin.framework.subscription.ExecutorSubject;

@TestClass
class TestLog {
    @TestMethod
    void exportLogInsideMethod(@CaseSubject ExecutorSubject subject) {
        String message = "This is a custom log inside the method";
        subject.updateData(new ExecutionInfo(System.currentTimeMillis(), TestCaseState.RUNNING, message));
    }
}
```
### Service Injection
Sometimes, a test case may rely on other third party modules, like redis client. In practical, you may not initialize those modules inside a test method. Instead, you may manage the third party module separately.
Therefore, @Service annotation allows you to define you own service class, and @Bean annotation allows you to define your module client. You can use them by declaring parameters in your test method. 
The module client will be automatically injected into the parameter you declared, as the client instance will be automatically loaded into the ioc container.

```java
import pro.boyu.dongxin.framework.annotations.Bean;
import pro.boyu.dongxin.framework.annotations.Service;
import pro.boyu.dongxin.framework.annotations.TestClass;
import pro.boyu.dongxin.framework.annotations.TestMethod;

@Service
class MyServiceClass {
    @Bean
    RedisClient getRedisClient() {
        RedisCenter center = RedisCenter.getInstance();
        return center.getRedis();
    }
}

@TestClass
class TestServiceClass {
    @TestMethod
    void testService(RedisClient client) {
        client.doSomeThing();
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

### Use Log Export Module
Export Log into PDF or HTML format is pretty simple, just use @ExportLogs annotation on you main application class
```java
import pro.boyu.dongxin.framework.TestApplication;
import pro.boyu.dongxin.framework.annotations.ExportLogs;

@ExportLogs(path = "/Users/jerry/test_report.pdf", fileType = "pdf")
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
- [Li Dongxin](https://github.com/Thesmall943)

