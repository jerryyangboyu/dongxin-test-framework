# Dongxin Test Framework

## Features
- Write a simple test case using only @TestMethod Annotation
- Multithread test case support
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

You should use @CaseSubject to indicate you want to inject a subject correspond to each test method, and use `subject.updateData()` to print your log side the test method.

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
Sometimes, a test case may relay on other third party modules, like redis client. Usually, we initialize those modules inside a test method. However, you may wish to manage the third party module by your self separately.
Therefore, @Service annotation allows you to define you own service class, and @Bean annotation allows you to define your module client managed by the framework. the module client will be automatically injected into the test method.

```java
import pro.boyu.dongxin.framework.annotations.Bean;
import pro.boyu.dongxin.framework.annotations.Service;

@Service
class MyServiceClass {
    @Bean
    RedisClient getRedisClient() {
        RedisCenter center = RedisCenter.getInstance();
        return center.getRedis();
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
- [Li Dongxin](#)

