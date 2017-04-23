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
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.wzr.dao.service.WorkshopService;
import com.wzr.dao.entity.Workshop;

@Controller
@RequestMapping("/workshop")
public class WorkShopController {

	private static final String WORKSHOP_MANAGE = "/workshop/manage";
	@Autowired
	private WorkshopService workshopService;
	
	@RequestMapping(value = "/manage", method = RequestMethod.GET)
	public String workshopManage(Model model){
		List<Workshop> workshopList = workshopService.getAll();
		model.addAttribute("workshopList", workshopList);
		return WORKSHOP_MANAGE;
	}
	
	/**
	 * 生成JSON格式的车间对象列表
	 * @param pw
	 */
	@RequestMapping(value = "/getwsjsonlist", method = RequestMethod.GET)
	public void getWsJSONList(PrintWriter pw){
		List<Workshop> workshopList = workshopService.getAll();
		JSONArray ja = new JSONArray();
		for(Workshop ws : workshopList){
			JSONObject jo = new JSONObject();
			jo.put("wsId", ws.getWsId());
			jo.put("wsName", ws.getWsName());
			ja.put(jo);
		}
		
		pw.write(ja.toString());
		pw.flush();
		pw.close();
	}
	
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String workshopAddView(Model model){
		model.addAttribute("workShop",new Workshop());
		return "/workshop/add";
	}
	
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String workshopAddSubmit(Workshop workShop){
		workshopService.add(workShop);
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX + WORKSHOP_MANAGE;
	}
	
	@RequestMapping(value = "delete", method = RequestMethod.GET)
	public String workshopDelete(Workshop workShop){
		workshopService.delete(workShop);
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX + WORKSHOP_MANAGE;
	}
}
