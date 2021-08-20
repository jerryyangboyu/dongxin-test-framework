package pro.boyu.dongxin.framework.constenum;

public enum TestCaseState {
    START("start"),RUNNING("running"),ERROR("error"),SUCCESSFIN("successFin"),ERRORFIN("errorFin");
    
    private TestCaseState(String intro) {
    	this.introString=intro;
    }
    
    String introString;
}
