package com.wzr.dao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wzr.dao.dao.EmployeeDao;
import com.wzr.dao.entity.Employee;
import com.wzr.dao.entity.Workshop;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private WorkshopService workshopService;
	
	
	public Employee getById(int id) {
		return (Employee)employeeDao.getById(id);
	}
	public List<Employee> getListByWsId(int wsId){
		List<Employee> emList = employeeDao.getListByWsId(wsId);
		return emList; 
	}
	public Employee getByName(String name) {
		return (Employee) employeeDao.getByName(name);
	}

	public List<Employee> getAll(){
		List<Employee> emList = employeeDao.getAll();
		for(Employee em : emList){
			if(em != null){
				em.setWorkshop((Workshop)(workshopService.getById(em.getWorkshop().getWsId())));
			}
		}
		return emList;
	}
	
	public void add(Object obj) {
		employeeDao.add(obj);
	}
	
	public void delete(Object obj) {
		employeeDao.delete(obj);
	}
	
}
