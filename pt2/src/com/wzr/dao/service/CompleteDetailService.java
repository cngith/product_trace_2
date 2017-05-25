package com.wzr.dao.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wzr.dao.dao.CompleteDetailDao;
import com.wzr.dao.entity.CompleteDetail;
import com.wzr.dao.entity.TransferOrderDetail;

@Service
public class CompleteDetailService {
	@Autowired
	private CompleteDetailDao completeDetailDao;
	

	/**
	 * 取某条码的所有完工明细
	 * @param barCode
	 * @return : 结果已按fromTime排序
	 */
	public List<CompleteDetail> getListByBarCode(String barCode) {
		return completeDetailDao.getListByBarCode(barCode);
	}
	
	/**
	 * 根据条码取最后一条[完工明细](按发送时间排序)
	 * @param barCode
	 * @return
	 */
	public CompleteDetail getLastByBarCode(String barCode) {
		return completeDetailDao.getLastByBarCode(barCode);
	}
	
	/**
	 * 取所有已完工商品，每件商品取最后一条记录
	 * @return
	 */
	public List<CompleteDetail> getAllLastList() {
		return completeDetailDao.getAllLastList();
	}
	/**
	 * 根据条码将此商品调拨单明细添加到完工明细表
	 * @param barCode
	 */
	public void addFromTod(String barCode) {
		completeDetailDao.addFromTodByBarCode(barCode);
	}
	
	/**
	 * 批量添加
	 * @param objList : 待加入数据库的数据对象
	 * @return : 返回影响的行数
	 */
	public int[] addBatch(List<CompleteDetail> objList) {
		return completeDetailDao.addBatch(objList);
	}
	
	/**
	 * 查询某部门某时间段内发送的商品(完工)
	 * @param fromWsId 部门Id
	 * @param dateTime1
	 * @param dateTime2
	 * @return
	 */
	public List<CompleteDetail> getListByFromWsId(int fromWsId, LocalDateTime dateTime1, LocalDateTime dateTime2) {
		return completeDetailDao.getListByFromWsId(fromWsId, dateTime1, dateTime2);
	}
	
	/**
	 * 查询某员工某时间段内发送的商品(完工)
	 * @param fromEpId 部门Id
	 * @param dateTime1
	 * @param dateTime2
	 * @return
	 */
	public List<CompleteDetail> getListByFromEpId(int fromEpId, LocalDateTime dateTime1, LocalDateTime dateTime2) {
		return completeDetailDao.getListByFromEpId(fromEpId, dateTime1, dateTime2);
	}
}
