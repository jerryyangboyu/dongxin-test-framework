package pro.boyu.test;

import pro.boyu.dongxin.framework.annotations.TestClass;
import pro.boyu.dongxin.framework.annotations.TestMethod;

@TestClass
public class GeneralTestClass {

    void init() {
        System.out.println("Hello");
    }

    @TestMethod(threadsNum = 10, repeatTime = 1, maxTime = 3)
    void testPlain() {
    }

//    @TestMethod
    void testBean(String beanMessage) {
        System.out.println(beanMessage);
    }

//    @TestMethod
    void testInstanceBean(TestService service) {
        System.out.println(service.getClient());
    }
}
