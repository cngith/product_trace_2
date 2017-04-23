package com.wzr.dao.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.wzr.dao.interf.IWorkshopDao;
import com.wzr.dao.entity.Workshop;

@Repository
public class WorkshopDao implements IWorkshopDao {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
    	this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

	@Override
	public void add(Object obj) {
		String sql = "INSERT INTO workshop (wsId,wsName) VALUES(:wsId,:wsName) ";
		SqlParameterSource sps = new BeanPropertySqlParameterSource(obj);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update(sql, sps, keyHolder);
		int id = keyHolder.getKey().intValue();
		((Workshop)obj).setWsId(id);
	}

	@Override
	public void delete(Object obj) {
		String sql = "DELETE FROM workshop WHERE wsId=:wsId ";
		SqlParameterSource sps = new BeanPropertySqlParameterSource(obj);
		namedParameterJdbcTemplate.update(sql, sps);
	}

	@Override
	public void update(Object obj) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public Object get(String sql, Object obj) {

		SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(obj);
		Object reObj = null;
		try {
			
			reObj = this.namedParameterJdbcTemplate.queryForObject(sql, namedParameters
					, new BeanPropertyRowMapper<Workshop>(Workshop.class));
			
		} catch (EmptyResultDataAccessException e) {
			// 当查询的result为空时，抛出EmptyResultDataAccessException异常
			return null;
		}
		return (Workshop)reObj;
	}

	public Object getById(int id) {
		String sql = "SELECT wsId,wsName FROM workshop WHERE wsId=:wsId";
		Workshop obj = new Workshop();
		obj.setWsId(id);
		return this.get(sql,obj);
	}


	public Object getByName(String name) {
		// 
		String sql = "SELECT wsId,wsName FROM workshop WHERE wsName=:wsName";
		Workshop obj = new Workshop();
		obj.setWsName(name);
		return this.get(sql,obj);
	}
	
	public List<Workshop> getAll(){
		String sql = "SELECT wsId,wsName FROM workshop";
		Workshop obj = new Workshop();
		return this.getList(sql, null, obj);
	}
	
	@Override
	public List<Workshop> getList(String sql,SqlParameterSource sqlParameterSource,Object obj) {
		if(null == sqlParameterSource){
			sqlParameterSource = new BeanPropertySqlParameterSource(obj);
		}
		List<Workshop> reObjList = null;
		
		try {
			reObjList = this.namedParameterJdbcTemplate.query(sql, sqlParameterSource
					, new BeanPropertyRowMapper<Workshop>(Workshop.class));
			
		} catch (EmptyResultDataAccessException e) {
			// 当查询的result为空时，抛出EmptyResultDataAccessException异常
			return null;
		}
		return reObjList;
	}

}
