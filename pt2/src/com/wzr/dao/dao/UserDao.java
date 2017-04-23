package com.wzr.dao.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.wzr.dao.interf.IUserDao;
import com.wzr.dao.entity.Employee;
import com.wzr.dao.entity.User;
import com.wzr.dao.entity.Workshop;

@Repository
public class UserDao implements IUserDao {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
    	this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

	@Override
	public void add(Object obj) {
		String sql = "INSERT INTO user (`userid`, `username`, `password`) "
				+ "VALUES(:userid,:username,:password) ";
		User user = (User)obj;
		SqlParameterSource sps = new MapSqlParameterSource().addValue("userid", null)
				.addValue("username", user.getUsername()).addValue("password", user.getPassword());
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update(sql, sps, keyHolder);
		int id = keyHolder.getKey().intValue();
		user.setUserid(id);

	}

	@Override
	public void delete(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Object obj) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public Object get(String sql, Object obj) {

		SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(obj);
		Object user = null;
		try {
			
			user = this.namedParameterJdbcTemplate.queryForObject(sql, namedParameters, new BeanPropertyRowMapper(User.class));
			
		} catch (EmptyResultDataAccessException e) {
			// 当查询的result为空时，抛出EmptyResultDataAccessException异常
			return null;
		}
		return (User)user;
	}

	public Object getById(int id) {
		// 
		String sql = "SELECT userid,username,password FROM user WHERE userid=:userid";
		User user = new User();
		user.setUserid(id);
		return this.get(sql,user);
	}

	public List<User> getAll(){
		String sql = "SELECT userid,username,password FROM user";
		User obj = new User();
		return this.getList(sql,null,obj);
	}
	
	public Object getByName(String name) {
		// 
		String sql = "SELECT userid,username,password FROM user WHERE username=:username";
		User user = new User();
		user.setUsername(name);
		return this.get(sql,user);
	}
	
	@Override
	public List<User> getList(String sql,SqlParameterSource sqlParameterSource,Object obj) {
		if(null == sqlParameterSource){
			sqlParameterSource = new BeanPropertySqlParameterSource(obj);
		}
		List<User> reObjList = new ArrayList<User>();
		
		try {
				SqlRowSet rs = this.namedParameterJdbcTemplate.queryForRowSet(sql, sqlParameterSource);
				while(rs.next()){
					User rsObj = new User();
	                rsObj.setUserid(rs.getInt("userid"));
	                rsObj.setUsername(rs.getString("username"));
	                rsObj.setPassword(rs.getString("password"));
	                reObjList.add(rsObj);
				}
			
		} catch (EmptyResultDataAccessException e) {
			// 当查询的result为空时，抛出EmptyResultDataAccessException异常
			return null;
		}
		return reObjList;
	}

}
