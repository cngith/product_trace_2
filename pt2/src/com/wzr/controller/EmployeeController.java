package com.wzr.controller;

import java.io.PrintWriter;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.wzr.dao.entity.Employee;
import com.wzr.dao.service.EmployeeService;
import com.wzr.dao.service.WorkshopService;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

	private static final String EMPLOYEE_MANAGE = "/employee/manage";
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private WorkshopService workshopService;
	
	@RequestMapping(value = "/manage", method = RequestMethod.GET)
	public String EmployeeManage(Model model){
		List<Employee> employeeList = employeeService.getAll();
		model.addAttribute("employeeList", employeeList);
		return EMPLOYEE_MANAGE;
	}
	
	
	@RequestMapping(value = "/getepbywsid", method = RequestMethod.GET)
	public void getEmployeeByWsId(@RequestParam("wsId") int wsId, PrintWriter printWriter){
		List<Employee> toEpL = employeeService.getListByWsId(wsId);
		JSONArray ja = new JSONArray();
		for(Employee ep: toEpL){
			JSONObject js = new JSONObject();
			js.put("epId", ep.getEpId());
			js.put("epName", ep.getEpName());
			
//			Workshop ws = workshopService.getById(ep.getWorkshop().getWsId());
//			js.put("workshop.wsName", ws.getWsName());
			js.put("workshop.wsId", ep.getWorkshop().getWsId());
			ja.put(js);
		}
		
		printWriter.write(ja.toString());
		printWriter.flush();
		printWriter.close();
	}
	
	
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String employeeAddView(Model model){
		model.addAttribute("employee",new Employee());
		model.addAttribute("wsList", workshopService.getAll());
		return "/employee/add";
	}
	
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String employeeAddSubmit(Employee employee){
		employeeService.add(employee);
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX + EMPLOYEE_MANAGE;
	}
	
	@RequestMapping(value = "delete", method = RequestMethod.GET)
	public String employeeDelete(Employee employee){
		employeeService.delete(employee);
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX + EMPLOYEE_MANAGE;
	}
}
