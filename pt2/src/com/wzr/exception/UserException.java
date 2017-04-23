package com.wzr.exception;

public class UserException extends RuntimeException {
	
	public static final String EXCEP_USERNAME = "用户名错误!";
	
	public static final String EXCEP_PASSWORD = "密码错误!";
	
	public UserException(String message) {
		super(message);
	}
}
