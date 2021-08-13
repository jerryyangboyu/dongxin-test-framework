package pro.boyu.dongxin.framework;

public class TestClassInitFailedException extends Exception{
    public TestClassInitFailedException(String className,String message){
        super("Test Case "+className+" init Failed ,Because of "+message);
    }
}
