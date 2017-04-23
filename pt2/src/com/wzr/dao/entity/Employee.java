package com.wzr.dao.entity;

public final class Employee {

	private Integer epId;
	
	private String epName;

	private Workshop workshop;


	public Workshop getWorkshop() {
		return workshop;
	}

	public void setWorkshop(Workshop workshop) {
		this.workshop = workshop;
	}

	public Integer getEpId() {
		return epId;
	}

	public void setEpId(Integer epId) {
			this.epId = epId;
	}

	public String getEpName() {
		return epName;
	}

	public void setEpName(String epName) {
		this.epName = epName;
	}

//	private int wsId;

//	public int getWsId() {
//		return wsId;
//	}
//
//	public void setWsId(int wsId) {
//		this.wsId = wsId;
//	}

	
}
