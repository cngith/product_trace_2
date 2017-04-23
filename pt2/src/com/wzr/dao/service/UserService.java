package com.wzr.dao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wzr.dao.dao.UserDao;
import com.wzr.dao.entity.User;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	
	public void add(Object obj) {
		userDao.add(obj);
	}
	
	public List<User> getAll(){
		return userDao.getAll();
	}
	public User getById(int id) {
		return (User)userDao.getById(id);
	}
	public User getByName(String name) {

		return (User) userDao.getByName(name);
	}

	public boolean UserExistByName(String name) {

		User user = getByName(name);

		return (user == null) ? false : true;
	}

	public boolean UserPasswordValid(User user) {
		
		User userP = getByName(user.getUsername());
		if( userP == null){
			return false;
		}
		else { // 用户名相同，继续校验密码
			if(userP.getPassword().equals(user.getPassword())){
				return true;
			}
			else{
				return false;
			}
		}
	}
	
}
