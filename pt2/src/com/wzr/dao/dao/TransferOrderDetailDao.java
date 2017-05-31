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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.wzr.dao.interf.ITransferOrderDetailDao;
import com.wzr.dao.entity.Employee;
import com.wzr.dao.entity.TransferOrderDetail;
import com.wzr.dao.entity.Workshop;

@Repository
public class TransferOrderDetailDao implements ITransferOrderDetailDao {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
    	this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

	@Override
	public void add(Object obj) {
		String sql = "INSERT INTO transfer_order_detail (tdId,fromWs,fromEp,toWs,toEp,barCode,fromTime,toTime,addTime,fromUserId,toUserId) "
				+ " VALUES(:tdId,:fromWs.wsId,:fromEp.epId,:toWs.wsId,:toEp.epId,:barCode,:fromTime,:toTime,:addTime,:fromUserId,:toUserId)";
		SqlParameterSource sps = new BeanPropertySqlParameterSource(obj);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update(sql, sps, keyHolder);
		int tdId = keyHolder.getKey().intValue();
		((TransferOrderDetail)obj).setTdId(tdId);
	}

	@Override
	public void delete(Object obj) {
		String sql = "DELETE FROM transfer_order_detail WHERE tmId=:tmId ";
		SqlParameterSource sps = new BeanPropertySqlParameterSource(obj);
		namedParameterJdbcTemplate.update(sql, sps);
	}
	
	public void deleteByBarCode(String barCode) {
		String sql = "DELETE FROM transfer_order_detail WHERE barCode=:barCode ";
		SqlParameterSource sps = new MapSqlParameterSource().addValue("barCode", barCode);
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
					, new BeanPropertyRowMapper<TransferOrderDetail>(TransferOrderDetail.class));
			
		} catch (EmptyResultDataAccessException e) {
			// 当查询的result为空时，抛出EmptyResultDataAccessException异常
			return null;
		}
		return (TransferOrderDetail)reObj;
	}
	
	public TransferOrderDetail get(String sql,SqlParameterSource sqlParameterSource, Object obj) {
		
		List<TransferOrderDetail> reObjList = getList(sql, sqlParameterSource, obj);
		if(null == reObjList || 0 == reObjList.size()){
			return null;
		}
		return reObjList.get(0);
	}

	/**
	 * 根据条码取最后一条[调拨明细](按发送时间排序)
	 * @param barCode
	 * @return
	 */
	public TransferOrderDetail getLastByBarCode(String barCode) {
		String sql = "SELECT tdId,fromWs,fromEp,toWs,toEp,barCode,fromTime,toTime,addTime,fromUserId,toUserId FROM transfer_order_detail "
				+ "WHERE barCode=:barCode ORDER BY fromTime DESC LIMIT 0,1";
		SqlParameterSource sps = new MapSqlParameterSource().addValue("barCode", barCode);
		return this.get(sql,sps,null);
	}
	
	/**
	 * 根据客户代码查找待接收[调拨明细](按发送时间排序)
	 * @param cusCode : 客户代码
	 * @return
	 */
	public List<TransferOrderDetail> getWfaListByCusCode(String cusCode) {
		String sql = "SELECT tdId,fromWs,fromEp,toWs,toEp,barCode,fromTime,toTime,addTime,fromUserId,toUserId FROM transfer_order_detail "
				+ " WHERE barCode IN (SELECT barCode FROM product WHERE cusCode=:cusCode AND status=1) AND toTime IS NULL "
				+ " ORDER BY fromTime ";
		SqlParameterSource sps = new MapSqlParameterSource().addValue("cusCode", cusCode);
		return this.getList(sql,sps,null);
	}
	
	/**
	 * 根据品名查找待接收[调拨明细](按发送时间排序)
	 * @param pdName : 品名
	 * @return
	 */
	public List<TransferOrderDetail> getWfaListByPdName(String pdName) {
		String sql = "SELECT tdId,fromWs,fromEp,toWs,toEp,barCode,fromTime,toTime,addTime,fromUserId,toUserId FROM transfer_order_detail "
				+ " WHERE barCode IN (SELECT barCode FROM product WHERE pdName LIKE CONCAT('%',:pdName,'%') AND status=1) AND toTime IS NULL "
				+ " ORDER BY fromTime ";
		SqlParameterSource sps = new MapSqlParameterSource().addValue("pdName", pdName);
		return this.getList(sql,sps,null);
	}

	/**
	 * 通过toEpId(接收人Id)取未接收的调拨明细{@code List<TransferOrderDetail>}
	 * @param toEpId : 接收人Id
	 * @return : 成功返回{@code List<TransferOrderDetail>}, 否则返回null
	 */
	public List<TransferOrderDetail> getWfaListByToEpId(int toEpId) {
		return this.getListByToEpId(toEpId, false);
	}
	
	/**
	 * 通过toEpId(接收人Id)取{@code List<TransferOrderDetail>}
	 * @param toEpId : 接收人Id
	 * @param accepted : 是否区分接收状态(true:已接收;false:未接收;null:不区分)
	 * @return : 成功返回{@code List<TransferOrderDetail>}, 否则返回null
	 */
	public List<TransferOrderDetail> getListByToEpId(int toEpId, Boolean accepted) {
		// 
		String sql = "SELECT tdId,fromWs,fromEp,toWs,toEp,barCode,fromTime,toTime,addTime,fromUserId,toUserId FROM transfer_order_detail "
				+ "WHERE toEp=:toEp ";
		SqlParameterSource sps = new MapSqlParameterSource().addValue("toEp", toEpId);
		if(null != accepted){
			// 查询接收 或 未接收的
			if(!accepted){ // 未接收
				sql += " AND toTime IS NULL";
			}else{ // 已接收
				sql += " AND toTime IS NOT NULL";
			}
		} // 为null表示不考虑是否接收
		
		return this.getList(sql, sps, new TransferOrderDetail());
	}
	
	/**
	 * 根据条码取调拨明细,已按发送时间排序
	 * @param barCode
	 * @return
	 */
	public List<TransferOrderDetail> getListByBarCode(String barCode) {
		// 
		String sql = "SELECT tdId,fromWs,fromEp,toWs,toEp,barCode,fromTime,toTime,addTime,fromUserId,toUserId FROM transfer_order_detail "
				+ "WHERE barCode=:barCode ORDER BY fromTime";
		SqlParameterSource sps = new MapSqlParameterSource().addValue("barCode", barCode);
		return this.getList(sql,sps,null);
	}
	
	public List<TransferOrderDetail> getListByToWsId(int toWsId) {
		// 
		return getListByToWsId(toWsId, null);
	}
	
	/**
	 * 通过toWsId(接收部门Id)取{@code List<TransferOrderDetail>}
	 * @param toWsId : 接收部门Id
	 * @param accepted : 是否区分接收状态(true:已接收;false:未接收;null:不区分)
	 * @return : 成功返回{@code List<TransferOrderDetail>}, 否则返回null
	 */
	public List<TransferOrderDetail> getListByToWsId(int toWsId, Boolean accepted) {
		// 
		String sql = "SELECT tdId,fromWs,fromEp,toWs,toEp,barCode,fromTime,toTime,addTime,fromUserId,toUserId FROM transfer_order_detail "
				+ "WHERE toWs=:toWs ";
		SqlParameterSource sps = new MapSqlParameterSource().addValue("toWs", toWsId);
		if(null != accepted){
			// 查询接收 或 未接收的
			if(!accepted){
				sql += " AND toTime IS NULL";
			}else{ // 已接收
				sql += " AND toTime IS NOT NULL";
			}
		} // 为null表示不考虑是否接收
		
		return this.getList(sql, sps, new TransferOrderDetail());
	}
	

	public List<TransferOrderDetail> getListByDateTime(LocalDateTime dateTime1, LocalDateTime dateTime2) {
		// 
		String sql = "SELECT userid,tmDatetime FROM transfer_order_detail WHERE tmDatetime>=:tmDatetime1"
				+ " AND tmDatetime<=:tmDatetime2";
		SqlParameterSource sps = new MapSqlParameterSource().addValue("tmDatetime1", dateTime1)
				.addValue("tmDatetime2", dateTime2);
		TransferOrderDetail obj = new TransferOrderDetail();
		return this.getList(sql,sps,obj);
	}
	
//	public List<TransferOrderDetail> getAll(){
//		String sql = "SELECT userid,tmDatetime FROM transfer_order_detail";
//		TransferOrderDetail obj = new TransferOrderDetail();
//		return this.getList(sql,null,obj);
//	}
//	
	@Override
	public List<TransferOrderDetail> getList(String sql,SqlParameterSource sqlParameterSource,Object obj) {
		if(null == sqlParameterSource){
			sqlParameterSource = new BeanPropertySqlParameterSource(obj);
		}
		
		List<TransferOrderDetail> reObjList = new ArrayList<TransferOrderDetail>();
		
		try {
				SqlRowSet rs = namedParameterJdbcTemplate.queryForRowSet(sql, sqlParameterSource);
				while(rs.next()){
					TransferOrderDetail reObj = new TransferOrderDetail();
					reObj.setTdId(rs.getInt("tdId"));
//					reObj.setTmId(rs.getInt("tmId"));
					Workshop fromWs = new Workshop();
					fromWs.setWsId(rs.getInt("fromWs"));
					reObj.setFromWs(fromWs);
					Employee fromEp = new Employee();
					fromEp.setEpId(rs.getInt("fromEp"));
					reObj.setFromEp(fromEp);
					Workshop toWs = new Workshop();
					toWs.setWsId(rs.getInt("toWs"));
					reObj.setToWs(toWs);
					Employee toEp = new Employee();
					toEp.setEpId(rs.getInt("toEp"));
					reObj.setToEp(toEp);
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
	 * 批量接收调拨单明细
	 * @param objList : 待接收的调拨单明细对象(必须设置其中的tdId,toTime,toUserId)
	 * @return 
	 */
	public int[] acceptBatch(List<?> objList) {
		String sql = "UPDATE transfer_order_detail SET toTime=:toTime , toUserId=:toUserId "
				+ " WHERE tdId=:tdId";
		return batchUpdate(sql, objList);
	}
	
	public int[] updateToTimeBatch(List<?> objList) {
		String sql = "UPDATE transfer_order_detail SET toTime=:toTime";
		
		return batchUpdate(sql, objList);
	}
	
	/**
	 * 批量添加调拨记录
	 * @param objList
	 * @return 返回主键数组
	 */
	public int[] addBatch(List<?> objList) {
//		String sql = "INSERT INTO transfer_order_detail (tdId,fromWs,fromEp,toWs,toEp,barCode,fromTime,toTime,addTime,fromUserId,toUserId)"
//			+ " VALUES(:tdId,:fromWs.wsId,:fromEp.epId,:toWs.wsId,:toEp.epId,:barCode,:fromTime,:toTime,:addTime,:fromUserId,:toUserId)";
		int[] keys = new int[objList.size()];
		for(int i=0; i<objList.size(); ++i){
			add(objList.get(i));
			keys[i] = ((TransferOrderDetail)(objList.get(i))).getTdId();
		}
		return keys;
	}
	
	public int[] batchUpdate(String sql, final List<?> objList ) {
		   SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(objList.toArray());
		   int[] insertCounts = namedParameterJdbcTemplate.batchUpdate(sql, batch);
		   return insertCounts;
		 }
	/*
	public int addBatch(List<?> objList) {
		String sql = "INSERT INTO transfer_order_detail (tdId,fromWs,fromEp,toWs,toEp,barCode,fromTime,toTime,addTime,fromUserId,toUserId)"
				+ " VALUES(:tdId,::fromWs.wsId,:fromEp.epId,:toWs.wsId,:toEp.epId,:barCode,:fromTime,:toTime)";
		for(Object obj : objList){
			this.add(obj);
		}
		return objList.size();
	}
	
	
	public Object getById(int id) {
		String sql = "SELECT tdId,fromWs,fromEp,toWs,toEp,barCode,fromTime,toTime,addTime,fromUserId,toUserId FROM transfer_order_detail "
				+ "WHERE barCode=:barCode";
		TransferOrderDetail obj = new TransferOrderDetail();
		obj.setTmId(id);
		return this.get(sql,obj);
	}
	*/

	/**
	 * 查询某部门某时间段内发送的商品
	 * @param wsId 部门Id
	 * @param dateTime1
	 * @param dateTime2
	 * @return
	 */
	public List<TransferOrderDetail> getListByFromWsId(int wsId, LocalDateTime dateTime1, LocalDateTime dateTime2) {
		// 实际上是查询下个接收部门接收商品的时间在指定的时间段内
		String sql = "SELECT * FROM transfer_order_detail "
				+ "WHERE (fromWs=:fromWs) "
		+ "AND ((toTime>='" + dateTime1.toString() + "') AND (toTime<='" + dateTime2.toString() + "'))";
		SqlParameterSource sps = new MapSqlParameterSource().addValue("fromWs", wsId);
		return this.getList(sql, sps, new TransferOrderDetail());
	}
	
	/**
	 * 查询某员工某时间段内发送的商品
	 * @param epId 员工Id
	 * @param dateTime1
	 * @param dateTime2
	 * @return
	 */
	public List<TransferOrderDetail> getListByFromEpId(int epId, LocalDateTime dateTime1, LocalDateTime dateTime2) {
		String sql = "SELECT * FROM transfer_order_detail "
				+ "WHERE (fromEp=:fromEp) "
				+ "AND ((toTime>='" + dateTime1.toString() + "') AND (toTime<='" + dateTime2.toString() + "'))";
		SqlParameterSource sps = new MapSqlParameterSource().addValue("fromEp", epId);
		return this.getList(sql, sps, new TransferOrderDetail());
	}
	
	/**
	 * 指定部门,员工,接收日期之前的tod
	 * @param wsId
	 * @param epId
	 * @param dateTimeAgo
	 * @return
	 */
	public List<TransferOrderDetail> getListByDoing(int wsId, int epId, LocalDateTime dateTimeAgo) {
		String sql = "";
		SqlParameterSource sps = null;
		StringBuffer sqlBuf = new StringBuffer("SELECT * FROM ");
		if(0 == wsId){
			sqlBuf.append(" transfer_order_detail t WHERE NOT EXISTS "
					+ "(SELECT 1 from transfer_order_detail WHERE barCode=t.barCode AND toTime>t.toTime )"
					+ " AND t.toTime<'" + dateTimeAgo.toString() + "' ");
		}
		MapSqlParameterSource mps = new MapSqlParameterSource();
		if(0 < wsId){
			mps = mps.addValue("toWs", wsId);
			if(0 == epId){
				sqlBuf.append(" transfer_order_detail t WHERE NOT EXISTS "
						+ "(SELECT 1 from transfer_order_detail WHERE barCode=t.barCode AND toTime>t.toTime )"
						+ " AND t.toTime<'" + dateTimeAgo.toString() + "' AND toWs=:toWs");
			}else{
				sqlBuf.append(" transfer_order_detail t WHERE NOT EXISTS "
						+ "(SELECT 1 from transfer_order_detail WHERE barCode=t.barCode AND toTime>t.toTime )"
						+ " AND t.toTime<'" + dateTimeAgo.toString() + "' AND toWs=:toWs AND toEp=:toEp");
				mps = mps.addValue("toEp", epId);
			}
		}
		sps = mps;
		sql = sqlBuf.toString(); 
		return this.getList(sql, sps, new TransferOrderDetail());
	}
	
}
