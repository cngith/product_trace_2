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
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.wzr.dao.interf.IProductDao;
import com.wzr.dao.entity.CompleteDetail;
import com.wzr.dao.entity.Product;

@Repository
public class ProductDao implements IProductDao {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
    	this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

	@Override
	public void add(Object obj) {
		String sql = "INSERT INTO product (pdId,barCode,styleCode,pdName,length,size,cusCode,billNo,remark,status) "
				+ "VALUES(:pdId,:barCode,:styleCode,:pdName,:length,:size,:cusCode,:billNo,:remark,:status) ";
		SqlParameterSource sps = new BeanPropertySqlParameterSource(obj);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update(sql, sps, keyHolder);
		int id = keyHolder.getKey().intValue();
		Product pd = (Product)obj;
		pd.setPdId(id);
		
	}

	@Override
	public void delete(Object obj) {
		String sql = "DELETE FROM product WHERE pdId=:pdId ";
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
					, new BeanPropertyRowMapper<Product>(Product.class));
			
		} catch (EmptyResultDataAccessException e) {
			// 当查询的result为空时，抛出EmptyResultDataAccessException异常
			return null;
		}
		return (Product)reObj;
	}

	public Object getById(int id) {
		String sql = "SELECT pdId,barCode,styleCode,pdName,length,size,cusCode,billNo,remark,status FROM product WHERE pdId=:pdId";
		SqlParameterSource sps = new MapSqlParameterSource().addValue("pdId", id);

		return this.get(sql,sps);
	}


	public Product getByBarCode(String barCode) {
		String sql = "SELECT pdId,barCode,styleCode,pdName,length,size,cusCode,billNo,remark,status FROM product WHERE barCode=:barCode";
		SqlParameterSource sps = new MapSqlParameterSource().addValue("barCode", barCode);
		return this.get(sql,sps);
	}
	
	/**
	 * 更新指定条码商品的状态
	 * @param barCode
	 * @param status
	 */
	public void updateStatus(String barCode, int status){
		String sql = "UPDATE product SET status=:status WHERE barCode=:barCode";
		SqlParameterSource sps = new MapSqlParameterSource().addValue("barCode", barCode)
				.addValue("status", status);
		namedParameterJdbcTemplate.update(sql, sps);
	}
	
	/**
	 * 用SQL语句及参数查询
	 * @param sql
	 * @param namedParameters
	 * @return
	 */
	public Product get(String sql, SqlParameterSource namedParameters) {

		Product reObj = null;
		try {
			
			reObj = this.namedParameterJdbcTemplate.queryForObject(sql, namedParameters
					, new BeanPropertyRowMapper<Product>(Product.class));
			
		} catch (EmptyResultDataAccessException e) {
			// 当查询的result为空时，抛出EmptyResultDataAccessException异常
			return null;
		}
		return reObj;
	}
	
	public List<Product> getAll(){
		String sql = "SELECT * FROM product";
		Product obj = new Product();
		return this.getList(sql,null,obj);
	}
	
	/**
	 * 根据客户代码取商品信息 
	 * @param cusCode 客户代码
	 * @return 此客户订货的所有商品
	 */
	public List<Product> getListByCusCode(String cusCode) {
		String sql = "SELECT * FROM product "
				+ "WHERE cusCode=:cusCode";
		SqlParameterSource sps = new MapSqlParameterSource().addValue("cusCode", cusCode);
		return this.getList(sql,sps,null);
	}
	
	@Override
	public List<Product> getList(String sql,SqlParameterSource sqlParameterSource,Object obj) {
		if(null == sqlParameterSource){
			sqlParameterSource = new BeanPropertySqlParameterSource(obj);
		}
		
		List<Product> reObjList = new ArrayList<Product>();
		
		try {
			SqlRowSet rs = namedParameterJdbcTemplate.queryForRowSet(sql, sqlParameterSource);
			while(rs.next()){
				Product reObj = new Product();
				reObj.setPdId(rs.getInt("pdId"));
				reObj.setBarCode(rs.getString("barCode"));
				reObj.setStyleCode(rs.getString("styleCode"));
				reObj.setPdName(rs.getString("pdName"));
				reObj.setLength(rs.getInt("length"));
				reObj.setBarCode(rs.getString("barCode"));
				reObj.setSize(rs.getString("size"));
				reObj.setCusCode(rs.getString("cusCode"));
				reObj.setBillNo(rs.getString("billNo"));
				reObj.setRemark(rs.getString("remark"));
				reObj.setStatus(rs.getInt("status"));
				reObjList.add(reObj);
			}
			
		} catch (EmptyResultDataAccessException e) {
			// 当查询的result为空时，抛出EmptyResultDataAccessException异常
			return null;
		}
		return reObjList;
	}

	/**
	 * 批量添加商品记录
	 * @param objList
	 */
	public int[] addBatch(List<?> objList) {
		String sql = "INSERT INTO product (pdId,barCode,styleCode,pdName,length,size,cusCode,billNo,remark,status)"
				+ " VALUES(:pdId,:barCode,:styleCode,:pdName,:length,:size,:cusCode,:billNo,:remark,:status)";
		return batchUpdate(sql, objList );
	}
	
	public int[] batchUpdate(String sql, final List<?> objList ) {
		   SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(objList.toArray());
		   int[] insertCounts = namedParameterJdbcTemplate.batchUpdate(sql, batch);
		   return insertCounts;
		 }


}
