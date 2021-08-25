package pro.boyu.dongxin.utils.logger;

import pro.boyu.dongxin.framework.constenum.TestCaseState;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;


public class TestCaseInfoUtil {
	
	public static String executeExceptionInfoUtil(String testClass, String methodNameString, int repeatTime, String message) {
		return String.format("Test Class: %s Test Method: %s execute finish in %sms with state %s message %s", testClass, methodNameString, new Date(System.currentTimeMillis()), TestCaseState.SUCCESSFIN, message);
	}


	
	public static String getLocalDate(long ms) {
		return new Date(ms).toString();
	}

}
