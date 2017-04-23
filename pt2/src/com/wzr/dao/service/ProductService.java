package com.wzr.dao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wzr.dao.dao.ProductDao;
import com.wzr.dao.entity.EnumProductStatus;
import com.wzr.dao.entity.Product;

@Service
public class ProductService {

	@Autowired
	private ProductDao productDao;

	
	/**
	 * 根据商品状态码取商品状态说明
	 * @param status 状态的数值表示
	 * @return 返回状态的汉字表示
	 */
	public String getProductStatusName(int status){
		String statusName = null;
		if(EnumProductStatus.DOING.getStatus() == status){
			statusName = "生产中";
		}
		if(EnumProductStatus.CANCEL.getStatus() == status){
			statusName = "取消生产";
		}
		if(EnumProductStatus.DONE.getStatus() == status){
			statusName = "已完工";
		}
		return statusName;
	}
	
	public void add(Object obj) {
		productDao.add(obj);
	}
	
	/**
	 * 批量添加商品记录
	 * @param objList
	 */
	public void addBatch(List<?> objList) {
		
		productDao.addBatch(objList);
	}
	
	public void updateStatus(String barCode, int status){
		productDao.updateStatus(barCode, status);
	}
	public void delete(Object obj) {
		productDao.delete(obj);
	}
	
	public Product getByBarCode(String barCode) {
		return productDao.getByBarCode(barCode);
	}
	
	/**
	 * 根据客户代码取商品信息 
	 * @param cusCode 客户代码
	 * @return 此客户订货的所有商品
	 */
	public List<Product> getListByCusCode(String cusCode) {
		return productDao.getListByCusCode(cusCode);
	}
}
