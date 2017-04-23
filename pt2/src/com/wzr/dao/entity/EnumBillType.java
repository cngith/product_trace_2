package com.wzr.dao.entity;

/**
 * 表示单据类型[1:调拨;2:完工]
 * @author wzr
 *
 */
public enum EnumBillType {
	
	/**
	 * 调拨
	 */
	TRANSFER(1),
	
	
	/**
	 * 完工
	 */
	COMPLETE(2); 
	
	private int value;
	
	public int getValue() {
		return value;
	}

	public void setName(int value) {
		this.value = value;
	}

	private EnumBillType(int value){
		this.value = value;
	}

}
