package pro.boyu.dongxin.framework.infobean;

import pro.boyu.dongxin.framework.constenum.*;

public class TestExecuteInfo {
    private long time;
    private TestCaseState state;
    private String message;
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public TestCaseState getState() {
		return state;
	}
	public void setState(TestCaseState state) {
		this.state = state;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public TestExecuteInfo() {
		
	}
	
	public TestExecuteInfo(long time,TestCaseState state,String messageString) {
		// TODO Auto-generated constructor stub
		this.time=time;
		this.state=state;
		this.message=messageString;
	}
    
}
