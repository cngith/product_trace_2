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

import com.wzr.dao.interf.IMaterialDao;
import com.wzr.dao.entity.Material;

@Repository
public class MaterialDao implements IMaterialDao {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
    	this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

	@Override
	public void add(Object obj) {
		String sql = "INSERT INTO material (mtName) VALUES(:mtName) ";
		Material mt = (Material)obj;
		SqlParameterSource sps = new MapSqlParameterSource().addValue("mtName", mt.getMtName());
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update(sql, sps, keyHolder);
		int id = keyHolder.getKey().intValue();
		mt.setMtId(id);
	}

	@Override
	public void delete(Object obj) {
		String sql = "DELETE FROM material WHERE mtId=:mtId ";
		SqlParameterSource sps = new BeanPropertySqlParameterSource(obj);
		namedParameterJdbcTemplate.update(sql, sps);
	}

	@Override
	public void update(Object obj) {
		String sql = "UPDATE material SET mtName=:mtName WHERE mtId=:mtId ";
		SqlParameterSource sps = new BeanPropertySqlParameterSource(obj);
		namedParameterJdbcTemplate.update(sql, sps);

	}
	
	@Override
	public Object get(String sql, Object obj) {

		SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(obj);
		Object reObj = null;
		try {
			reObj = this.namedParameterJdbcTemplate.queryForObject(sql, namedParameters
					, new BeanPropertyRowMapper<Material>(Material.class));
			
		} catch (EmptyResultDataAccessException e) {
			// 当查询的result为空时，抛出EmptyResultDataAccessException异常
			return null;
		}
		return (Material)reObj;
	}

	public Object getById(int id) {
		// 
		String sql = "SELECT mtId,mtName FROM material WHERE mtId=:mtId";
		Material obj = new Material();
		obj.setMtId(id);
		return this.get(sql,obj);
	}


	public Object getByName(String name) {
		// 
		String sql = "SELECT mtId,mtName FROM material WHERE mtName=:mtName";
		Material obj = new Material();
		obj.setMtName(name);
		return this.get(sql,obj);
	}
	
	public List<Material> getAll(){
		String sql = "SELECT mtId,mtName FROM material";
		Material obj = new Material();
		return this.getList(sql,null,obj);
	}
	
//	public List<Material> getListByWsId(int wsId){
//		String sql = "SELECT mtId,wsId,mtName FROM material WHERE wsId=:wsId";
//		SqlParameterSource sps = new MapSqlParameterSource().addValue("wsId", wsId);
//		return this.getList(sql,sps,new Material());
//	}
	
	@Override
	public List<Material> getList(String sql,SqlParameterSource sqlParameterSource,Object obj) {
		if(null == sqlParameterSource){
			sqlParameterSource = new BeanPropertySqlParameterSource(obj);
		}
		List<Material> reObjList = new ArrayList<Material>();
		
		try {
				SqlRowSet rs = this.namedParameterJdbcTemplate.queryForRowSet(sql, sqlParameterSource);
				while(rs.next()){
					Material mt = new Material();
	                mt.setMtId(rs.getInt("mtId"));
	                mt.setMtName(rs.getString("mtName"));
	                reObjList.add(mt);
				}
			
		} catch (EmptyResultDataAccessException e) {
			// 当查询的result为空时，抛出EmptyResultDataAccessException异常
			return null;
		}
		return reObjList;
	}

}
