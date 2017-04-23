package com.wzr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 此类处理商品跟踪2015-12-10
 * @author wzr
 *
 */
@Controller
@RequestMapping("/pt")
public class ProductTraceController {
	
	private static final String PRODUCTTRACE = "/pt/producttrace";

	
	//private static final String LOGIN_USERNAME = "loginUsername";
	/**
	 * 返回"商品跟踪"页面地址
	 * @return
	 */
	@RequestMapping(value = "/ptview", method = RequestMethod.GET)
	public String productTrace(Model model){
		
		return PRODUCTTRACE;
	}
	
	
	



	
}
