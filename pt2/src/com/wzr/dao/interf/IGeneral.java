package com.wzr.dao.interf;

import java.util.List;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public interface IGeneral {

	public void add(Object obj);
	
	public void delete(Object obj);
	
	public void update(Object obj);
	
	public Object get(String sql, Object obj);
	
	
	public List<?> getList(String sql,SqlParameterSource sqlParameterSource,Object obj);

}
