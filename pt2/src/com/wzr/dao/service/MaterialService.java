package com.wzr.dao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wzr.dao.dao.MaterialDao;
import com.wzr.dao.entity.Material;

@Service
public class MaterialService {

	@Autowired
	private MaterialDao materialDao;

	public Material getById(int id) {
		return (Material)materialDao.getById(id);
	}
	
	public Material getByName(String name) {
		return (Material) materialDao.getByName(name);
	}

	public List<Material> getAll(){
		List<Material> emList = materialDao.getAll();
		return emList;
	}
	
	public void add(Object obj) {
		materialDao.add(obj);
	}
	
	public void delete(Object obj) {
		materialDao.delete(obj);
	}
	
}
