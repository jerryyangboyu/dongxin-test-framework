package pro.boyu.test;

import pro.boyu.dongxin.framework.annotations.InitMethod;
import pro.boyu.dongxin.framework.annotations.TestClass;
import pro.boyu.dongxin.framework.annotations.TestInit;
import pro.boyu.dongxin.framework.annotations.TestMethod;

@TestClass
public class GeneralTestClass {

    void init() {
        System.out.println("Hello");
    }

    @TestMethod(threadsNum = 1, repeatTime = 10)
    void testPlain() {
        System.out.println("Test plain");
    }
}