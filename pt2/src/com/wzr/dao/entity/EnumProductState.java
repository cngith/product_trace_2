package com.wzr.dao.entity;

/**
 * 表示商品的生产状态
 * @author Adm
 *
 */
public enum EnumProductState {
	
	/**
	 * 系统中不存在此商品
	 */
	NONE("NONE"),
	
	/**
	 * 商品目录中已存在, 但未产生业务数据
	 */
	PRODUCT_INFO("PRODUCT_INFO"),
	/**
	 * 待接收状态
	 */
	WAIT_FOR_ACCEPT("WAIT_FOR_ACCEPT"),
	
	/**
	 *  已接收状态
	 */
	ACCEPTED("ACCEPTED"),
	
	/**
	 *  商品已取消生产
	 */
	CANCEL("CANCEL"),
	
	/**
	 * 已经完成生产
	 */
	DONE("DONE"); 
	
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private EnumProductState(String name){
		this.name = name;
	}

	
}
