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

import com.wzr.dao.interf.IEmployeeDao;
import com.wzr.dao.entity.Employee;
import com.wzr.dao.entity.Workshop;

@Repository
public class EmployeeDao implements IEmployeeDao {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
    	this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

	@Override
	public void add(Object obj) {
		String sql = "INSERT INTO employee (wsId,epName) VALUES(:wsId,:epName) ";
		Employee ep = (Employee)obj;
		SqlParameterSource sps = new MapSqlParameterSource().addValue("epName", ep.getEpName())
				.addValue("wsId", ep.getWorkshop().getWsId());
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update(sql, sps, keyHolder);
		int id = keyHolder.getKey().intValue();
		ep.getWorkshop().setWsId(id);
	}

	@Override
	public void delete(Object obj) {
		String sql = "DELETE FROM employee WHERE epId=:epId ";
		SqlParameterSource sps = new BeanPropertySqlParameterSource(obj);
		namedParameterJdbcTemplate.update(sql, sps);
	}

	@Override
	public void update(Object obj) {
		String sql = "UPDATE employee SET epName=:epName WHERE epId=:epId ";
		SqlParameterSource sps = new BeanPropertySqlParameterSource(obj);
		namedParameterJdbcTemplate.update(sql, sps);

	}
	
	@Override
	public Object get(String sql, Object obj) {

		SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(obj);
		Object reObj = null;
		try {
			reObj = this.namedParameterJdbcTemplate.queryForObject(sql, namedParameters
					, new BeanPropertyRowMapper<Employee>(Employee.class));
			
		} catch (EmptyResultDataAccessException e) {
			// 当查询的result为空时，抛出EmptyResultDataAccessException异常
			return null;
		}
		return (Employee)reObj;
	}

	public Object getById(int id) {
		// 
		String sql = "SELECT epId,wsId,epName FROM employee WHERE epId=:epId";
		Employee obj = new Employee();
		obj.setEpId(id);
		return this.get(sql,obj);
	}


	public Object getByName(String name) {
		// 
		String sql = "SELECT epId,wsId,epName FROM employee WHERE epName=:epName";
		Employee obj = new Employee();
		obj.setEpName(name);
		return this.get(sql,obj);
	}
	
	public List<Employee> getAll(){
		String sql = "SELECT epId,wsId,epName FROM employee";
		Employee obj = new Employee();
		return this.getList(sql,null,obj);
	}
	
	public List<Employee> getListByWsId(int wsId){
		String sql = "SELECT epId,wsId,epName FROM employee WHERE wsId=:wsId";
		SqlParameterSource sps = new MapSqlParameterSource().addValue("wsId", wsId);
		return this.getList(sql,sps,new Employee());
	}
	
	@Override
	public List<Employee> getList(String sql,SqlParameterSource sqlParameterSource,Object obj) {
		if(null == sqlParameterSource){
			sqlParameterSource = new BeanPropertySqlParameterSource(obj);
		}
		List<Employee> reObjList = new ArrayList<Employee>();
		
		try {
				SqlRowSet rs = this.namedParameterJdbcTemplate.queryForRowSet(sql, sqlParameterSource);
				while(rs.next()){
					Employee ep = new Employee();
	                ep.setEpId(rs.getInt("epId"));
	                ep.setEpName(rs.getString("epName"));
	                ep.setWorkshop(new Workshop());
	                ep.getWorkshop().setWsId(rs.getInt("wsId"));
	                reObjList.add(ep);
				}
			
		} catch (EmptyResultDataAccessException e) {
			// 当查询的result为空时，抛出EmptyResultDataAccessException异常
			return null;
		}
		return reObjList;
	}

}
