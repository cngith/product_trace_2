package com.wzr.dao.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TransferOrderDetail {


	private int tdId;
	
	private Workshop fromWs;
	
	private Employee fromEp;
	
	private Workshop toWs;
	
	private Employee toEp;
	
	private String barCode;
	
	private Timestamp fromTime;
	
	private Timestamp toTime;
	
	private Timestamp addTime;
	
	private Integer fromUserId;
	
	private Integer toUserId;
	

	public Integer getToUserId() {
		return toUserId;
	}

	public void setToUserId(Integer toUserId) {
		this.toUserId = toUserId;
	}

	public Integer getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(Integer fromUserId) {
		this.fromUserId = fromUserId;
	}

	public Timestamp getAddTime() {
		return addTime;
	}

	public void setAddTime(Timestamp addTime) {
		this.addTime = addTime;
	}

	public Timestamp getEditTime() {
		return editTime;
	}

	public void setEditTime(Timestamp editTime) {
		this.editTime = editTime;
	}

	private Timestamp editTime;

	public Timestamp getFromTime() {
		return fromTime;
	}

	public void setFromTime(Timestamp fromTime) {
		this.fromTime = fromTime;
	}

	public Timestamp getToTime() {
		return toTime;
	}

	public void setToTime(Timestamp toTime) {
		this.toTime = toTime;
	}

//	public int getTmId() {
//		return tmId;
//	}
//
//	public void setTmId(int tmId) {
//		this.tmId = tmId;
//	}

	public int getTdId() {
		return tdId;
	}

	public void setTdId(int tdId) {
		this.tdId = tdId;
	}


	public Workshop getFromWs() {
		return fromWs;
	}

	public void setFromWs(Workshop fromWs) {
		this.fromWs = fromWs;
	}

	public Employee getFromEp() {
		return fromEp;
	}

	public void setFromEp(Employee fromEp) {
		this.fromEp = fromEp;
	}

	public Workshop getToWs() {
		return toWs;
	}

	public void setToWs(Workshop toWs) {
		this.toWs = toWs;
	}

	public Employee getToEp() {
		return toEp;
	}

	public void setToEp(Employee toEp) {
		this.toEp = toEp;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}


	
}
