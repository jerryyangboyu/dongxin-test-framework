package pro.boyu.dongxin.framework.infobean;

import pro.boyu.dongxin.framework.constenum.*;

public class ExecutionInfo {
    private long time;
    private TestCaseState state;
    private String message = "成功";
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

	public ExecutionInfo() {

	}

	public ExecutionInfo(long time, TestCaseState state) {
		this.time=time;
		this.state=state;
	}
	
	public ExecutionInfo(long time, TestCaseState state, String messageString) {
		this.time=time;
		this.state=state;
		this.message=messageString;
	}

	@Override
	public String toString() {
		return "ExecutionInfo{" +
				"time=" + time +
				", state=" + state +
				", message='" + message + '\'' +
				'}';
	}
}
