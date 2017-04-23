package com.wzr.controller;

import java.util.List;

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
import com.wzr.dao.entity.Employee;
import com.wzr.dao.entity.User;

@Controller
@RequestMapping("/user")
public class UserController {

	private static final String LOGIN_USER = "loginUser";

	private static final String LOGIN_USERNAME = "loginUsername";
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/manage", method = RequestMethod.GET)
	public String EmployeeManage(Model model){
		List<User> userList = userService.getAll();
		model.addAttribute("userList", userList);
		return "user/manage";
	}
	

	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String userAddView(Model model){
		model.addAttribute("user",new User());
//		model.addAttribute("wsList", workshopService.getAll());
		return "/user/add";
	}
	
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String employeeAddSubmit(User user){
		userService.add(user);
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "user/manage";
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
