package com.wzr.dao.dao;

import java.time.LocalDateTime;
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
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.wzr.dao.interf.IGeneral;
import com.wzr.dao.entity.CompleteDetail;
import com.wzr.dao.entity.TransferOrderDetail;

@Repository
public class CompleteDetailDao implements IGeneral{

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
    	this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

	@Override
	public void add(Object obj) {
		String sql = "INSERT INTO complete_detail (cdId,fromWs,fromEp,toWs,toEp,barCode,fromTime,toTime,addTime,fromUserId,toUserId) "
				+ "VALUES(:cmId,:cdId,:fromWs,:fromEp,:toWs,:toEp,:barCode,:fromTime,:toTime,:addTime,:fromUserId,:toUserId) ";
		SqlParameterSource sps = new BeanPropertySqlParameterSource(obj);
		namedParameterJdbcTemplate.update(sql, sps);
	}

	public void addFromTodByBarCode(String barCode) {
		String sql = "INSERT INTO complete_detail (fromWs,fromEp,toWs,toEp,barCode,fromTime,toTime,addTime,fromUserId,toUserId) "
				+ " (SELECT fromWs,fromEp,toWs,toEp,barCode,fromTime,toTime,addTime,fromUserId,toUserId "
		+ " FROM transfer_order_detail WHERE barCode=:barCode)";
		SqlParameterSource sps = new MapSqlParameterSource().addValue("barCode", barCode);
		namedParameterJdbcTemplate.update(sql, sps);
	}
	
	/**
	 * 批量添加
	 * @param objList : 待加入数据库的数据对象
	 * @return : 返回影响的行数
	 */
	public int[] addBatch(List<?> objList) {
		String sql = "INSERT INTO complete_detail (cdId,fromWs,fromEp,toWs,toEp,barCode,fromTime,toTime,addTime,fromUserId,toUserId)"
				+ " VALUES(:cdId,:fromWs,:fromEp,:toWs,:toEp,:barCode,:fromTime,:toTime,:addTime,:fromUserId,:toUserId )";
		
		return batchUpdate(sql, objList);
	}
	
	public int[] batchUpdate(String sql, final List<?> objList ) {
		   SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(objList.toArray());
		   int[] insertCounts = namedParameterJdbcTemplate.batchUpdate(sql, batch);
		   return insertCounts;
	}
	
	@Override
	public void delete(Object obj) {
		String sql = "DELETE FROM complete_detail WHERE cdId=:cdId ";
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
					, new BeanPropertyRowMapper<CompleteDetail>(CompleteDetail.class));
			
		} catch (EmptyResultDataAccessException e) {
			// 当查询的result为空时，抛出EmptyResultDataAccessException异常
			return null;
		}
		return (CompleteDetail)reObj;
	}
	
	/**
	 * 用SQL语句及参数查询
	 * @param sql
	 * @param namedParameters
	 * @return
	 */
	public CompleteDetail get(String sql, SqlParameterSource namedParameters) {

		CompleteDetail reObj = null;
		try {
			
			reObj = this.namedParameterJdbcTemplate.queryForObject(sql, namedParameters
					, new BeanPropertyRowMapper<CompleteDetail>(CompleteDetail.class));
			
		} catch (EmptyResultDataAccessException e) {
			// 当查询的result为空时，抛出EmptyResultDataAccessException异常
			return null;
		}
		return reObj;
	}
	
	public Object getById(int cdId) {
		String sql = "SELECT * FROM complete_detail "
				+ "WHERE cdId=:cdId";
		CompleteDetail obj = new CompleteDetail();
		obj.setCdId(cdId);
		return this.get(sql,obj);
	}
	
	/**
	 * 根据条码取最后一条[完工明细](按发送时间排序)
	 * @param barCode
	 * @return
	 */
	public CompleteDetail getLastByBarCode(String barCode) {
		String sql = "SELECT cdId,fromWs,fromEp,toWs,toEp,barCode,fromTime,toTime,fromUserId,toUserId FROM complete_detail "
				+ "WHERE barCode=:barCode ORDER BY fromTime DESC LIMIT 0,1";
		SqlParameterSource sps = new MapSqlParameterSource().addValue("barCode", barCode);
		return this.get(sql,sps);
	}

	
	/**
	 * 取所有已完工商品的完工记录
	 * @return
	 */
	public List<CompleteDetail> getAllLastList() {
		String sql = "SELECT * FROM complete_detail WHERE toTime IS null";
		CompleteDetail obj = new CompleteDetail();
		return this.getList(sql,null,obj);
	}
	
	/**
	 * 取某条码的所有完工明细
	 * @param barCode
	 * @return:结果已按fromTime排序
	 */
	public List<CompleteDetail> getListByBarCode(String barCode) {
		// 
		String sql = "SELECT * FROM complete_detail "
				+ "WHERE barCode=:barCode ORDER BY fromTime";
		SqlParameterSource sps = new MapSqlParameterSource().addValue("barCode", barCode);
		return this.getList(sql,sps,null);
	}
	
	
	@Override
	public List<CompleteDetail> getList(String sql,SqlParameterSource sqlParameterSource,Object obj) {
		if(null == sqlParameterSource){
			sqlParameterSource = new BeanPropertySqlParameterSource(obj);
		}
		
		List<CompleteDetail> reObjList = new ArrayList<CompleteDetail>();
		
		try {
				SqlRowSet rs = namedParameterJdbcTemplate.queryForRowSet(sql, sqlParameterSource);
				while(rs.next()){
					CompleteDetail reObj = new CompleteDetail();
					reObj.setCdId(rs.getInt("cdId"));
					reObj.setFromWs(rs.getInt("fromWs"));
					reObj.setFromEp(rs.getInt("fromEp"));
					reObj.setToWs(rs.getInt("toWs"));
					reObj.setToEp(rs.getInt("toEp"));
					reObj.setBarCode(rs.getString("barCode"));
					reObj.setFromTime(rs.getTimestamp("fromTime"));
					reObj.setToTime(rs.getTimestamp("toTime"));
					reObj.setAddTime(rs.getTimestamp("addTime"));
					reObj.setFromUserId(rs.getInt("fromUserId"));
					reObj.setToUserId(rs.getInt("toUserId"));
					reObjList.add(reObj);
				}
			
		} catch (EmptyResultDataAccessException e) {
			// 当查询的result为空时，抛出EmptyResultDataAccessException异常
			return null;
		}
		return reObjList;
	}


	/**
	 * 查询某部门某时间段内发送的商品记录
	 * @param fromWsId 部门Id
	 * @param dateTime1
	 * @param dateTime2
	 * @return 此方法返回生产过程中的所有生产记录，包含指定部门最后一条完工时的记录
	 */
	public List<CompleteDetail> getListByFromWsId(int fromWsId, LocalDateTime dateTime1, LocalDateTime dateTime2) {
		String sql = "(SELECT * FROM complete_detail "
				+ "WHERE (fromWs=:fromWs)  AND toTime IS NOT NULL "
				+ "AND ((toTime>='" + dateTime1.toString() + "') AND (toTime<='" + dateTime2.toString() + "')))"
				+ " UNION (SELECT * FROM complete_detail WHERE (fromWs=:fromWs) AND toTime IS NULL "
				+ " AND ((fromTime>='" + dateTime1.toString() + "') AND (fromTime<='" + dateTime2.toString() + "')))";
		SqlParameterSource sps = new MapSqlParameterSource().addValue("fromWs", fromWsId);
		return this.getList(sql, sps, new CompleteDetail());
	}
	
	/**
	 * 查询某员工某时间段内发送的商品记录
	 * @param fromEpId 部门Id
	 * @param dateTime1
	 * @param dateTime2
	 * @return
	 */
	public List<CompleteDetail> getListByFromEpId(int fromEpId, LocalDateTime dateTime1, LocalDateTime dateTime2) {
		String sql = "SELECT * FROM complete_detail "
				+ "WHERE (fromEp=:fromEp) "
		+ "AND ((toTime>='" + dateTime1.toString() + "') AND (toTime<='" + dateTime2.toString() + "'))";
		SqlParameterSource sps = new MapSqlParameterSource().addValue("fromEp", fromEpId);
		return this.getList(sql, sps, new CompleteDetail());
	}
	
	/*
	public int addBatch(List<?> objList) {
		String sql = "INSERT INTO complete_detail (cmId,cdId,fromWs,fromEp,toWs,toEp,barCode,fromTime,toTime)"
				+ " VALUES(:cmId,:cdId,:fromWs.wsId,:fromEp.epId,:toWs.wsId,:toEp.epId,:barCode,:fromTime,:toTime)";
		for(Object obj : objList){
			this.add(obj);
		}
		return objList.size();
	}
	
	public List<CompleteDetail> getListByToWsId(int toWsId, Boolean accepted) {
		// 
		String sql = "SELECT cmId,cdId,fromWs,fromEp,toWs,toEp,barCode,fromTime,toTime FROM complete_detail "
				+ "WHERE toWs=:toWs ";
		SqlParameterSource sps = new MapSqlParameterSource().addValue("toWs", toWsId);
		if(null != accepted){
			// 查询接收 或 未接收的
			if(!accepted){
				sql += " AND toTime IS NULL";
			}else{
				sql += " AND toTime IS NOT NULL";
			}
		} // 为null表示不考虑是否接收
		
		return this.getList(sql, sps, new CompleteDetail());
	}
	

	public List<CompleteDetail> getListByDateTime(LocalDateTime dateTime1, LocalDateTime dateTime2) {
		// 
		String sql = "SELECT cmId,userid,tmDatetime FROM complete_detail WHERE tmDatetime>=:tmDatetime1"
				+ " AND tmDatetime<=:tmDatetime2";
		SqlParameterSource sps = new MapSqlParameterSource().addValue("tmDatetime1", dateTime1)
				.addValue("tmDatetime2", dateTime2);
		CompleteDetail obj = new CompleteDetail();
		return this.getList(sql,sps,obj);
	}
	
	
	
	
	
	
		 
	*/
	

}
