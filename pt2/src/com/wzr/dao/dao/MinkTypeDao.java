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

import com.wzr.dao.interf.IMinkTypeDao;
import com.wzr.dao.entity.MinkType;

@Repository
public class MinkTypeDao implements IMinkTypeDao {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
    	this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

	@Override
	public void add(Object obj) {
		String sql = "INSERT INTO mink_type (minkType) VALUES(:minkType) ";
		MinkType mt = (MinkType)obj;
		SqlParameterSource sps = new MapSqlParameterSource().addValue("minkType", mt.getMinkType());
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update(sql, sps, keyHolder);
		int id = keyHolder.getKey().intValue();
		mt.setMinkTypeId(id);
	}

	@Override
	public void delete(Object obj) {
		String sql = "DELETE FROM mink_type WHERE minkTypeId=:minkTypeId ";
		SqlParameterSource sps = new BeanPropertySqlParameterSource(obj);
		namedParameterJdbcTemplate.update(sql, sps);
	}

	@Override
	public void update(Object obj) {
		String sql = "UPDATE mink_type SET minkType=:minkType WHERE minkTypeId=:minkTypeId ";
		SqlParameterSource sps = new BeanPropertySqlParameterSource(obj);
		namedParameterJdbcTemplate.update(sql, sps);

	}
	
	@Override
	public Object get(String sql, Object obj) {

		SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(obj);
		Object reObj = null;
		try {
			reObj = this.namedParameterJdbcTemplate.queryForObject(sql, namedParameters
					, new BeanPropertyRowMapper<MinkType>(MinkType.class));
			
		} catch (EmptyResultDataAccessException e) {
			// 当查询的result为空时，抛出EmptyResultDataAccessException异常
			return null;
		}
		return (MinkType)reObj;
	}

	public Object getById(int id) {
		// 
		String sql = "SELECT minkTypeId,minkType FROM mink_type WHERE minkTypeId=:minkTypeId";
		MinkType obj = new MinkType();
		obj.setMinkTypeId(id);
		return this.get(sql,obj);
	}


	public Object getByName(String name) {
		// 
		String sql = "SELECT minkTypeId,minkType FROM mink_type WHERE minkType=:minkType";
		MinkType obj = new MinkType();
		obj.setMinkType(name);
		return this.get(sql,obj);
	}
	
	public List<MinkType> getAll(){
		String sql = "SELECT minkTypeId,minkType FROM mink_type";
		MinkType obj = new MinkType();
		return this.getList(sql,null,obj);
	}
	
//	public List<MinkType> getListByWsId(int wsId){
//		String sql = "SELECT minkTypeId,wsId,minkType FROM mink_type WHERE wsId=:wsId";
//		SqlParameterSource sps = new MapSqlParameterSource().addValue("wsId", wsId);
//		return this.getList(sql,sps,new MinkType());
//	}
	
	@Override
	public List<MinkType> getList(String sql,SqlParameterSource sqlParameterSource,Object obj) {
		if(null == sqlParameterSource){
			sqlParameterSource = new BeanPropertySqlParameterSource(obj);
		}
		List<MinkType> reObjList = new ArrayList<MinkType>();
		
		try {
				SqlRowSet rs = this.namedParameterJdbcTemplate.queryForRowSet(sql, sqlParameterSource);
				while(rs.next()){
					MinkType mt = new MinkType();
	                mt.setMinkTypeId(rs.getInt("minkTypeId"));
	                mt.setMinkType(rs.getString("minkType"));
	                reObjList.add(mt);
				}
			
		} catch (EmptyResultDataAccessException e) {
			// 当查询的result为空时，抛出EmptyResultDataAccessException异常
			return null;
		}
		return reObjList;
	}

}
