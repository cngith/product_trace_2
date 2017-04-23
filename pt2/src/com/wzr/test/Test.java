package com.wzr.test;

import java.io.PrintWriter;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.wzr.dao.entity.MinkType;

public class Test {

	private int userid;

	public int getUserid() {
		
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}
	

}
