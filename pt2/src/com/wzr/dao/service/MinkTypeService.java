package com.wzr.dao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wzr.dao.dao.MinkTypeDao;
import com.wzr.dao.entity.MinkType;

@Service
public class MinkTypeService {

	@Autowired
	private MinkTypeDao minkTypeDao;

	public MinkType getById(int id) {
		return (MinkType)minkTypeDao.getById(id);
	}
	
	public MinkType getByName(String name) {
		return (MinkType) minkTypeDao.getByName(name);
	}

	public List<MinkType> getAll(){
		List<MinkType> reList = minkTypeDao.getAll();
		return reList;
	}
	
	public void add(Object obj) {
		minkTypeDao.add(obj);
	}
	
	public void delete(Object obj) {
		minkTypeDao.delete(obj);
	}
	
}
