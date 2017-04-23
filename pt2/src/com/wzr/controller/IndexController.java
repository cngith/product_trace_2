package com.wzr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("loginUsername")
public class IndexController {
	
	@RequestMapping ("/index")
	public String hello(){
		return "index";
	}
	
	
	/**
	 * 根据JSON格式的字符串生成xlsx文件
	 */
	@RequestMapping(value = "index/saveAsXlsx", method = RequestMethod.POST)
	public ModelAndView saveAsXlsx(ModelMap model){
		
		MyXlsxView ve = new MyXlsxView();     
		return new ModelAndView(ve, model); 
	}
	
}
