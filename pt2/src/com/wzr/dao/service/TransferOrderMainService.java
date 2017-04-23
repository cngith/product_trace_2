package com.wzr.dao.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wzr.dao.dao.TransferOrderMainDao;
import com.wzr.dao.entity.TransferOrderMain;

@Service
public class TransferOrderMainService {

	@Autowired
	private TransferOrderMainDao transferOrderMainDao;

	
	public List<TransferOrderMain> getListByDateTime(LocalDateTime dateTime1, LocalDateTime dateTime2) {
		return transferOrderMainDao.getListByDateTime(Timestamp.valueOf(dateTime1), Timestamp.valueOf(dateTime2));
	}

	public List<TransferOrderMain> getAll(){
		List<TransferOrderMain> emList = transferOrderMainDao.getAll();
		
		return emList;
	}
	
	public void add(Object obj) {
		transferOrderMainDao.add(obj);
	}
	
	public void delete(Object obj) {
		transferOrderMainDao.delete(obj);
	}
	
}
