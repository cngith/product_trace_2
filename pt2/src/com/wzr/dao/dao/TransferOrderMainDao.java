package com.wzr.dao.dao;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

import com.wzr.dao.interf.ITransferOrderMainDao;
import com.wzr.dao.entity.TransferOrderMain;
import com.wzr.dao.entity.User;

@Repository
public class TransferOrderMainDao implements ITransferOrderMainDao {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
    	this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

	@Override
	public void add(Object obj) {
		String sql = "INSERT INTO transfer_order_main (tmId,userid,tmDatetime) "
				+ "VALUES(:tmId,:user.userid,:tmDatetime) ";
		SqlParameterSource sps = new BeanPropertySqlParameterSource(obj);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update(sql, sps, keyHolder);
		int id = keyHolder.getKey().intValue();
		((TransferOrderMain)obj).setTmId(id);
	}

	@Override
	public void delete(Object obj) {
		String sql = "DELETE FROM transfer_order_main WHERE tmId=:tmId ";
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
					, new BeanPropertyRowMapper<TransferOrderMain>(TransferOrderMain.class));
			
		} catch (EmptyResultDataAccessException e) {
			// 当查询的result为空时，抛出EmptyResultDataAccessException异常
			return null;
		}
		return (TransferOrderMain)reObj;
	}

	public Object getById(int id) {
		String sql = "SELECT tmId,userid,tmDatetime FROM transfer_order_main WHERE tmId=:tmId";
		TransferOrderMain obj = new TransferOrderMain();
		obj.setTmId(id);
		return this.get(sql,obj);
	}


//	public List<TransferOrderMain> getListByDateTime(Timestamp dateTime1, Timestamp dateTime2) {
	public List<TransferOrderMain> getListByDateTime(Timestamp dateTime1, Timestamp dateTime2) {
		// 
		String sql = "SELECT tmId,userid,tmDatetime FROM transfer_order_main WHERE tmDatetime>=:tmDatetime1"
				+ " AND tmDatetime<=:tmDatetime2";
		SqlParameterSource sps = new MapSqlParameterSource().addValue("tmDatetime1", dateTime1)
				.addValue("tmDatetime2", dateTime2);
		TransferOrderMain obj = new TransferOrderMain();
		return this.getList(sql,sps,obj);
	}
	
	public List<TransferOrderMain> getAll(){
		String sql = "SELECT tmId,userid,tmDatetime FROM transfer_order_main";
		TransferOrderMain obj = new TransferOrderMain();
		return this.getList(sql,null,obj);
	}
	
	@Override
	public List<TransferOrderMain> getList(String sql,SqlParameterSource sqlParameterSource,Object obj) {
		if(null == sqlParameterSource){
			sqlParameterSource = new BeanPropertySqlParameterSource(obj);
		}
		
		List<TransferOrderMain> reObjList = new ArrayList<TransferOrderMain>();
		
		try {
				SqlRowSet rs = namedParameterJdbcTemplate.queryForRowSet(sql, sqlParameterSource);
				while(rs.next()){
					TransferOrderMain tom = new TransferOrderMain();
					tom.setTmId(rs.getInt("tmId"));
					User user = new User();
					user.setUserid(rs.getInt("userid"));
					tom.setUser(user);
					tom.setTmDatetime(rs.getTimestamp("tmDatetime"));
					reObjList.add(tom);
				}
//			reObjList = this.namedParameterJdbcTemplate.queryForList(sql, sqlParameterSource
//					, TransferOrderMain.class);
			
		} catch (EmptyResultDataAccessException e) {
			// 当查询的result为空时，抛出EmptyResultDataAccessException异常
			return null;
		}
		return reObjList;
	}

}
