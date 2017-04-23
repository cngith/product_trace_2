package com.wzr.dao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wzr.dao.dao.MinkSizeDao;
import com.wzr.dao.entity.MinkSize;

@Service
public class MinkSizeService {

	@Autowired
	private MinkSizeDao minkSizeDao;

	public MinkSize getById(int id) {
		return (MinkSize)minkSizeDao.getById(id);
	}
	
	public MinkSize getByMinkSize(String minkSize) {
		return (MinkSize) minkSizeDao.getByMinkSize(minkSize);
	}

	public List<MinkSize> getAll(){
		List<MinkSize> emList = minkSizeDao.getAll();
		return emList;
	}
	
	public void add(Object obj) {
		minkSizeDao.add(obj);
	}
	
	public void delete(Object obj) {
		minkSizeDao.delete(obj);
	}
	
}
