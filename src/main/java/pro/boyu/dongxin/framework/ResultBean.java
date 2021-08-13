package pro.boyu.dongxin.framework;

import java.util.Stack;

public class ResultBean {
    private Stack<Exception> errStack=new Stack<Exception>();
    public void pushException(Exception e){
        this.errStack.push(e);
    }


}
