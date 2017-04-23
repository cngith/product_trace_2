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

import com.wzr.dao.interf.IMinkSizeDao;
import com.wzr.dao.entity.MinkSize;

@Repository
public class MinkSizeDao implements IMinkSizeDao {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
    	this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

	@Override
	public void add(Object obj) {
		String sql = "INSERT INTO ms_size (msSize) VALUES(:msSize) ";
		MinkSize ms = (MinkSize)obj;
		SqlParameterSource sps = new MapSqlParameterSource().addValue("msSize", ms.getMsSize());
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update(sql, sps, keyHolder);
		int id = keyHolder.getKey().intValue();
		ms.setMsId(id);
	}

	@Override
	public void delete(Object obj) {
		String sql = "DELETE FROM ms_size WHERE msId=:msId ";
		SqlParameterSource sps = new BeanPropertySqlParameterSource(obj);
		namedParameterJdbcTemplate.update(sql, sps);
	}

	@Override
	public void update(Object obj) {
		String sql = "UPDATE ms_size SET msSize=:msSize WHERE msId=:msId ";
		SqlParameterSource sps = new BeanPropertySqlParameterSource(obj);
		namedParameterJdbcTemplate.update(sql, sps);

	}
	
	@Override
	public Object get(String sql, Object obj) {

		SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(obj);
		Object reObj = null;
		try {
			reObj = this.namedParameterJdbcTemplate.queryForObject(sql, namedParameters
					, new BeanPropertyRowMapper<MinkSize>(MinkSize.class));
			
		} catch (EmptyResultDataAccessException e) {
			// 当查询的result为空时，抛出EmptyResultDataAccessException异常
			return null;
		}
		return (MinkSize)reObj;
	}

	public Object getById(int id) {
		// 
		String sql = "SELECT msId,msSize FROM ms_size WHERE msId=:msId";
		MinkSize obj = new MinkSize();
		obj.setMsId(id);
		return this.get(sql,obj);
	}


	public Object getByMinkSize(String name) {
		// 
		String sql = "SELECT msId,msSize FROM ms_size WHERE msSize=:msSize";
		MinkSize obj = new MinkSize();
		obj.setMsSize(name);
		return this.get(sql,obj);
	}
	
	public List<MinkSize> getAll(){
		String sql = "SELECT msId,msSize FROM ms_size";
		MinkSize obj = new MinkSize();
		return this.getList(sql,null,obj);
	}
	
	
	@Override
	public List<MinkSize> getList(String sql,SqlParameterSource sqlParameterSource,Object obj) {
		if(null == sqlParameterSource){
			sqlParameterSource = new BeanPropertySqlParameterSource(obj);
		}
		List<MinkSize> reObjList = new ArrayList<MinkSize>();
		
		try {
				SqlRowSet rs = this.namedParameterJdbcTemplate.queryForRowSet(sql, sqlParameterSource);
				while(rs.next()){
					MinkSize ms = new MinkSize();
	                ms.setMsId(rs.getInt("msId"));
	                ms.setMsSize(rs.getString("msSize"));
	                reObjList.add(ms);
				}
			
		} catch (EmptyResultDataAccessException e) {
			// 当查询的result为空时，抛出EmptyResultDataAccessException异常
			return null;
		}
		return reObjList;
	}

}
