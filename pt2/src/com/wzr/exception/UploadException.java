package com.wzr.exception;

public class UploadException extends RuntimeException {
	
	public static final String EXCEP_FILE_STRUCTURE = "文件结构错误";
	
	public static final String EXCEP_FILE_READ = "文件读取错误";
	
	public UploadException(String message) {
		super(message);
	}
}
