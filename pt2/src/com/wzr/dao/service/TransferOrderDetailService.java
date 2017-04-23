package com.wzr.dao.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wzr.dao.dao.TransferOrderDetailDao;
import com.wzr.dao.entity.TransferOrderDetail;

/**
 * @author Adm
 *
 */
@Service
public class TransferOrderDetailService {

	@Autowired
	private TransferOrderDetailDao transferOrderDetailDao;

	/**
	 * 取某车间未接收的记录
	 * @param toWsId
	 * @return
	 */
	public List<TransferOrderDetail> getUnAcceptedListByToWsId(int toWsId) {
		return transferOrderDetailDao.getListByToWsId(toWsId, false);
	}
	
	/**
	 * 取某车间已接收的记录
	 * @param toWsId
	 * @return
	 */
	public List<TransferOrderDetail> getAcceptedListByToWsId(int toWsId) {
		return transferOrderDetailDao.getListByToWsId(toWsId, true);
	}
	
	/**
	 * 根据条码取最后一条[调拨明细](按发送时间排序)
	 * @param barCode：条码
	 * @return
	 */
	public TransferOrderDetail getLastByBarCode(String barCode) {
		return transferOrderDetailDao.getLastByBarCode(barCode);
	}
	
	/**
	 * 根据客户代码查找待接收[调拨明细](按发送时间排序)
	 * @param barCode
	 * @return
	 */
	public List<TransferOrderDetail> getWfaListByCusCode(String cusCode) {
		return transferOrderDetailDao.getWfaListByCusCode(cusCode);
	}
		
	/**
	 * 通过toEpId(接收人Id)取未接收的调拨明细{@code List<TransferOrderDetail>}
	 * @param toEpId : 接收人Id
	 * @return : 成功返回{@code List<TransferOrderDetail>}, 否则返回null
	 */
	public List<TransferOrderDetail> getWfaListByToEpId(int toEpId) {
		return transferOrderDetailDao.getWfaListByToEpId(toEpId);
	}
	
	/**
	 * 取发送给某车间的所有记录(未接收的,已接收的)
	 * @param toWsId
	 * @return
	 */
	public List<TransferOrderDetail> getListByToWsId(int toWsId) {
		return transferOrderDetailDao.getListByToWsId(toWsId);
	}
	

	/**
	 * 根据品名查找待接收[调拨明细](按发送时间排序)
	 * @param pdName : 品名
	 * @return
	 */
	public List<TransferOrderDetail> getWfaListByPdName(String pdName) {
		return transferOrderDetailDao.getWfaListByPdName(pdName);
	}

	/**
	 * 根据条码取调拨明细,已按发送时间排序
	 * @param barCode
	 * @return
	 */
	public List<TransferOrderDetail> getListByBarCode(String barCode) {
		return transferOrderDetailDao.getListByBarCode(barCode);
	}
	
	public void add(Object obj) {
		transferOrderDetailDao.add(obj);
	}
	
	/**
	 * 批量添加调拨记录
	 * @param objList
	 * @return 返回主键数组
	 */
	public int[] addBatch(List<?> objList) {
		
		return transferOrderDetailDao.addBatch(objList);
	}
	
	public void delete(Object obj) {
		transferOrderDetailDao.delete(obj);
	}

	/**
	 * 根据条码删除此商品的所有调拨明细记录
	 * @param barCode
	 */
	public void deleteByBarCode(String barCode) {
		transferOrderDetailDao.deleteByBarCode(barCode);
	}
	/**
	 * 批量更新商品接收时间(若toTime原来为null, 则此更改为接收商品行为)
	 * @param objList
	 * @return
	 */
	public int[] updateToTimeBatch(List<?> objList) {
		return transferOrderDetailDao.updateToTimeBatch(objList);
	}
	
	/**
	 * 批量接收调拨单明细
	 * @param objList : 待接收的调拨单明细对象(必须设置其中的tdId,toTime,toUserId)
	 * @return 
	 */
	public int[] acceptBatch(List<?> objList) {
		return transferOrderDetailDao.acceptBatch(objList);
	}

	/**
	 * 查询某部门某时间段内发送的商品(未完工)
	 * @param wsId 部门Id
	 * @param dateTime1
	 * @param dateTime2
	 * @return
	 */
	public List<TransferOrderDetail> getListByFromWsId(int wsId, LocalDateTime dateTime1, LocalDateTime dateTime2) {
		return transferOrderDetailDao.getListByFromWsId(wsId,dateTime1,dateTime2);
	}
	
	/**
	 * 查询某员工某时间段内发送的商品
	 * @param epId 员工Id
	 * @param dateTime1
	 * @param dateTime2
	 * @return
	 */
	public List<TransferOrderDetail> getListByFromEpId(int epId, LocalDateTime dateTime1, LocalDateTime dateTime2) {
		return transferOrderDetailDao.getListByFromEpId(epId,dateTime1,dateTime2);
	}
	
	/**
	 * 指定部门,员工,接收日期之前的tod
	 * @param wsId
	 * @param epId
	 * @param dateTimeAgo
	 * @return
	 */
	public List<TransferOrderDetail> getListByDoing(int wsId, int epId, LocalDateTime dateTimeAgo) {
		return transferOrderDetailDao.getListByDoing(wsId, epId, dateTimeAgo);
	}
}
