package com.myretail.pricingservice.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {
	
	public static String stackTraceToString(Throwable t) {
		StringWriter errors = new StringWriter();
		t.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}
}
