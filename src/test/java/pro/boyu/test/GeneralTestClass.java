package pro.boyu.test;

import pro.boyu.dongxin.framework.annotations.CaseSubject;
import pro.boyu.dongxin.framework.annotations.TestClass;
import pro.boyu.dongxin.framework.annotations.TestMethod;
import pro.boyu.dongxin.framework.constenum.TestCaseState;
import pro.boyu.dongxin.framework.infobean.ExecutionInfo;
import pro.boyu.dongxin.framework.subscription.ExecutorSubject;
import pro.boyu.dongxin.framework.subscription.Subject;

@TestClass
public class GeneralTestClass {

    void init() {
        System.out.println("Hello");
    }

    @TestMethod(threadsNum = 10, repeatTime = 1, maxTime = 3)
    void testPlain() {
    }

    @TestMethod
    void testBean(String beanMessage) {
        System.out.println(beanMessage);
    }

    @TestMethod
    void testInstanceBean(TestService service) {
        System.out.println(service.getClient());
    }

    @TestMethod
    void testMessage(@CaseSubject ExecutorSubject subject) {
        subject.updateData(new ExecutionInfo(System.currentTimeMillis(), TestCaseState.ERROR, "Good"));
    }
}
