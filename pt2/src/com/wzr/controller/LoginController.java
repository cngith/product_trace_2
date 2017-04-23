package com.wzr.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.wzr.dao.service.UserService;
import com.wzr.exception.UserException;
import com.wzr.dao.entity.User;

@Controller
public class LoginController {

	private static final String LOGIN_USER = "loginUser";

	private static final String LOGIN_USERNAME = "loginUsername";
	
	@Autowired
	private UserService userService;
	
	//@Autowired
	//private RequestMappingHandlerAdapter  requestMappingHandlerAdapter ;
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String Login(Model model){
		model.addAttribute(LOGIN_USER,new User());
		return "login";
	}
	
	@RequestMapping(value="/logout", method=RequestMethod.GET)
	public String Logout(HttpSession session){
		if(null != session.getAttribute(LOGIN_USERNAME)){
			session.invalidate();
		}
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "login";
	}
	
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String Login(User user, HttpSession session){
		
		if(!userService.UserExistByName(user.getUsername())){
			throw new UserException(UserException.EXCEP_USERNAME);
		}
		else if(!userService.UserPasswordValid(user)){
			throw new UserException(UserException.EXCEP_PASSWORD);
		}
		
		session.setAttribute(LOGIN_USERNAME, user.getUsername());
//		return InternalResourceViewResolver.FORWARD_URL_PREFIX + "index";
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "index";
	}
	
	// 局部异常处理, 发生异常时返回登录界面
	@ExceptionHandler(UserException.class)
	public String handlerException(Exception excep,HttpServletRequest req){
		req.setAttribute("excep", excep);
		req.setAttribute(LOGIN_USER,new User());
		return "login";
	}
	
}
