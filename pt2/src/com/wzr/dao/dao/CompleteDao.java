package com.wzr.dao.dao;

import java.sql.Timestamp;
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

import com.wzr.dao.interf.IGeneral;
import com.wzr.dao.entity.Complete;
import com.wzr.dao.entity.EnumBillType;

@Repository
public class CompleteDao implements IGeneral {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
    	this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

	@Override
	public void add(Object obj) {
		String sql = "INSERT INTO complete_main (cmId,userid,cmDatetime,billType) "
				+ "VALUES(:cmId,:userid,:cmDatetime,:billType) ";
		SqlParameterSource sps = new BeanPropertySqlParameterSource(obj);
		namedParameterJdbcTemplate.update(sql, sps);
	}

	@Override
	public void delete(Object obj) {
		String sql = "DELETE FROM complete_main WHERE cmId=:cmId ";
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
					, new BeanPropertyRowMapper<Complete>(Complete.class));
			
		} catch (EmptyResultDataAccessException e) {
			// 当查询的result为空时，抛出EmptyResultDataAccessException异常
			return null;
		}
		return (Complete)reObj;
	}




	public List<Complete> getListByDateTime(Timestamp dateTime1, Timestamp dateTime2) {
		// 
		String sql = "SELECT cmId,userid,cmDatetime FROM complete_main WHERE cmDatetime>=:Datetime1"
				+ " AND cmDatetime<=:Datetime2";
		SqlParameterSource sps = new MapSqlParameterSource().addValue("Datetime1", dateTime1)
				.addValue("Datetime2", dateTime2);
		Complete obj = new Complete();
		return this.getList(sql,sps,obj);
	}
	
	public List<Complete> getAll(){
		String sql = "SELECT cmId,userid,cmDatetime,billType FROM complete_main ";
		Complete obj = new Complete();
		return this.getList(sql,null,obj);
	}
	
	public List<Complete> getAll(EnumBillType billType){
		String sql = "SELECT cmId,userid,cmDatetime,billType FROM complete_main WHERE billType=:billType";
		SqlParameterSource sps = new MapSqlParameterSource().addValue("billType", billType.getValue());
		return this.getList(sql,sps,null);
	}
	
	@Override
	public List<Complete> getList(String sql,SqlParameterSource sqlParameterSource,Object obj) {
		if(null == sqlParameterSource){
			sqlParameterSource = new BeanPropertySqlParameterSource(obj);
		}
		
		List<Complete> reObjList = new ArrayList<Complete>();
		
		try {
				SqlRowSet rs = namedParameterJdbcTemplate.queryForRowSet(sql, sqlParameterSource);
				while(rs.next()){
					Complete cp = new Complete();
					cp.setCmId(rs.getInt("cmId"));
					cp.setUserid(rs.getInt("userid"));
					cp.setCmDatetime(rs.getTimestamp("cmDatetime"));
					cp.setBillType(rs.getInt("billType"));
					reObjList.add(cp);
				}
			
		} catch (EmptyResultDataAccessException e) {
			// 当查询的result为空时，抛出EmptyResultDataAccessException异常
			return null;
		}
		return reObjList;
	}
	
	/*
	public Object getById(int id) {
		String sql = "SELECT cmId,userid,cmDatetime,billType FROM complete_main WHERE cmId=:cmId";
		CompleteMain obj = new CompleteMain();
		obj.setCmId(id);
		return this.get(sql,obj);
	}
	*/
}
