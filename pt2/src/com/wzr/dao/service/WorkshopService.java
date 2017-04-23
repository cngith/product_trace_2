package com.wzr.dao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wzr.dao.dao.WorkshopDao;
import com.wzr.dao.entity.Workshop;;

@Service
public class WorkshopService {

	@Autowired
	private WorkshopDao workshopDao;

	public Workshop getById(int id) {
		return (Workshop)workshopDao.getById(id);
	}
	
	public Workshop getByName(String name) {

		return (Workshop) workshopDao.getByName(name);
	}

	public List<Workshop> getAll(){
		return workshopDao.getAll();
	}
	
	public void add(Object obj) {
		workshopDao.add(obj);
	}
	
	public void delete(Object obj) {
		workshopDao.delete(obj);
	}
	
}
