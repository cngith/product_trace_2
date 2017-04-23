package com.wzr.dao.entity;

/**
 * 表示商品目录中商品的生产状态
 * @author Wzr
 *
 */
public enum EnumProductStatus {
	

	/**
	 * 生产中(包括待接收和已接收状态)
	 */
	DOING(1),
	
	/**
	 *  商品已取消生产
	 */
	CANCEL(0),
	
	/**
	 * 已经完成生产
	 */
	DONE(5); 
	
	private int status;
	

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	private EnumProductStatus(int status){
		this.status = status;
		
	}

}
