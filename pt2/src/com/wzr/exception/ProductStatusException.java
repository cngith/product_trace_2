package com.wzr.exception;

@SuppressWarnings("serial")
public class ProductStatusException extends RuntimeException {
	
	public static final String EXCEP_PRODUCT_UNKOWN = "此商品处于未知状态";
	
	public static final String EXCEP_PRODUCT_NONE = "系统中不存在此商品";
	
	public static final String EXCEP_PRODUCT_INFO = "商品目录中已添加, 但未产生业务数据";
	
	public static final String EXCEP_PRODUCT_ACCEPTED= "商品处于已接收状态";
	
	public static final String EXCEP_PRODUCT_NOT_ACCEPT= "商品处于未接收状态";
	
	public static final String EXCEP_PRODUCT_CANCEL = "取消生产状态";
	
	public static final String EXCEP_PRODUCT_COMPLETED = "商品已下线";
	
	public ProductStatusException(String message) {
		super(message);
	}
}
