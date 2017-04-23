package com.wzr.dao.entity;

import java.sql.Timestamp;

public class TransferOrderMain {

	private int tmId;
	
	//@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp tmDatetime;

	public Timestamp getTmDatetime() {
		return tmDatetime;
	}

	public void setTmDatetime(Timestamp tmDatetime) {
		this.tmDatetime = tmDatetime;
	}

	private User user;

	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getTmId() {
		return tmId;
	}

	public void setTmId(int tmId) {
		this.tmId = tmId;
	}

	
	
}
