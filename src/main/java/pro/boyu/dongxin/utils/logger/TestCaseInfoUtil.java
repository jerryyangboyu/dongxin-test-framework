package pro.boyu.dongxin.utils.logger;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;


public class TestCaseInfoUtil {
	
	public static String executeExceptionInfoUtil(String testClass, String methodNameString, int repeatTime, String exceptionString) {
		return String.format("test class %s test case %s appears Exeption in %s, after executed %d time ,because of %s",testClass,  methodNameString, new Date(System.currentTimeMillis()),repeatTime,exceptionString);
	}


	
	public static String getLocalDate(long ms) {
		return new Date(ms).toString();
	}

}
