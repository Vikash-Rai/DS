package com.equabli.utils;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.equabli.domain.Response;
import com.equabli.domain.helpers.ErrorUtils;

@Component
public class Utils implements Constants{

	public static Boolean isStringNullOrBlank(String str) {
			return 	str == null || str.equals(BLANK);
	}
	
	public static Boolean isIntegerNull(Integer i) {
		return i==null;
	}
	
	public static Boolean isLongNull(Long l) {
		return l == null;
	}
	
	public static void errorHandler(Exception exp, Response<?> response, Logger logger) {
		ErrorUtils.buildErrorResponse(response, exp, logger);
	}
}
