package pro.boyu.dongxin.framework.executor;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;


public class TestCaseInfoUtil {
	
	static String executeExceptionInfoUtil(String methodNameString ,int repeatTime,String exceptionString) {
		return String.format("test case %s appears Exeption in %s, after executed %d time ,because of %s", methodNameString,new Date(System.currentTimeMillis()).toLocaleString(),repeatTime,exceptionString);
	}
	
	static String getLocalDate(long ms) {
		return new Date(ms).toLocaleString();
	}

}
