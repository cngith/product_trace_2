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

import com.wzr.dao.entity.Material;
import com.wzr.dao.entity.MinkSize;
import com.wzr.dao.entity.MinkType;
import com.wzr.dao.service.MaterialService;
import com.wzr.dao.service.MinkSizeService;
import com.wzr.dao.service.MinkTypeService;

@Controller
@RequestMapping("/product_attribute")
public class ProductAttributeController {

	private static final String PRODUCT_ATTRIBUTE_MANAGE = "/product_attribute/manage";
	
	@Autowired
	private MaterialService materialSerivce;
	
	@Autowired
	private MinkTypeService minkTypeSerivce;
	
	@Autowired
	private MinkSizeService minkSizeSerivce;
	
	@RequestMapping(value = "/manage", method = RequestMethod.GET)
	public String productAttributeManage(Model model){
		return PRODUCT_ATTRIBUTE_MANAGE;
	}
	
	
	@RequestMapping(value = "/material_manage", method = RequestMethod.GET)
	public String materialManage(Model model){
		return "/product_attribute/material_manage";
	}

	@RequestMapping(value = "/get_all_material", method = RequestMethod.GET)
	public void getAllMaterial(PrintWriter pw){
		List<Material> mtList = materialSerivce.getAll();
		// 用于返回前台的JSON对象
		JSONObject joRe = new JSONObject();
		// 从数据表查询到的内容
		JSONArray jaContent = new JSONArray();
		// 按显示顺序填入字段名
		JSONArray jaField = resultFieldArrayMaterial();
		joRe.put("exportField", jaField);
		
		for(Material mt : mtList){
			JSONObject jo = new JSONObject();
			jo.put("mtId", mt.getMtId());
			jo.put("mtName", mt.getMtName());
			jaContent.put(jo);
		}
		joRe.put("exportContent", jaContent);
		pw.write(joRe.toString());
		pw.flush();
		pw.close();
		
	}
	
	
	/**
	 * 生成查询结果的字段信息用于前端取数据(作为JSON的KEY)
	 * @return 返回字段信息(代码,名称)(有顺序)
	 */
	private JSONArray resultFieldArrayMaterial() {
		JSONArray ja = new JSONArray();
		putOneField(ja, "mtId", "Id");
		putOneField(ja, "mtName", "原料名称");
		return ja;
	}
	
	
	@RequestMapping(value = "/add_material", method = RequestMethod.GET)
	public String addMaterialView(Model model){
		model.addAttribute("material",new Material());
		return "/product_attribute/add_material";
	}
	
	@RequestMapping(value = "/add_material", method = RequestMethod.POST)
	public String addMaterialSubmit(@RequestParam(value="mtName") String mtName){
		Material mt = new Material();
		mt.setMtName(mtName);
		materialSerivce.add(mt);
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX + PRODUCT_ATTRIBUTE_MANAGE;
	}

	@RequestMapping(value = "/seek_material_by_mtName", method = RequestMethod.GET)
	public void seekMaterialByMtName(@RequestParam(value="mtName") String mtName,PrintWriter pw){
		Material mt = materialSerivce.getByName(mtName);
		JSONObject jo = new JSONObject();
		if(null == mt){
			jo.append("materialExist", "false");
		}
		else{
			jo.append("materialExist", "true");
		}
		pw.write(jo.toString());
		pw.flush();
		pw.close();
	}
	
	@RequestMapping(value = "/delete_material", method = RequestMethod.GET)
	public String materialDelete(Material material){
		materialSerivce.delete(material);
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX + PRODUCT_ATTRIBUTE_MANAGE;
	}
	
	/**
	 * 皮号管理
	 */
	@RequestMapping(value = "/mink_manage", method = RequestMethod.GET)
	public String minkManageView(){
		return "/product_attribute/mink_manage";
	}
	
	/**
	 * 皮号类型管理
	 */
	@RequestMapping(value = "/mink_type_manage", method = RequestMethod.GET)
	public String minkTypeManageView(){
		return "/product_attribute/mink_type_manage";
	}
	
	

//	@RequestMapping(value = "/add_mink_type", method = RequestMethod.GET)
//	public String addMinkTypeView(){
//		return "/product_attribute/add_mink_type";
//	}
	
	@RequestMapping(value = "/get_all_mink_type", method = RequestMethod.GET)
	public void getAllMinkType(PrintWriter pw){
		List<MinkType> mtList = minkTypeSerivce.getAll();
		// 用于返回前台的JSON对象
		JSONObject joRe = new JSONObject();
		// 从数据表查询到的内容
		JSONArray jaContent = new JSONArray();
		// 按显示顺序填入字段名
		JSONArray jaField = resultFieldArrayMinkType();
		joRe.put("exportField", jaField);
		
		for(MinkType mt : mtList){
			JSONObject jo = new JSONObject();
			jo.put("minkTypeId", mt.getMinkTypeId());
			jo.put("minkType", mt.getMinkType());
			jaContent.put(jo);
		}
		joRe.put("exportContent", jaContent);
		pw.write(joRe.toString());
		pw.flush();
		pw.close();
		
	}
	
	
	/**
	 * 生成查询结果的字段信息用于前端取数据(作为JSON的KEY)
	 * @return 返回字段信息(代码,名称)(有顺序)
	 */
	private JSONArray resultFieldArrayMinkType() {
		JSONArray ja = new JSONArray();
		putOneField(ja, "msSizeId", "Id");
		putOneField(ja, "minkType", "原料名称");
		return ja;
	}
	
	/**
	 * 添加皮号类型页面
	 */
	/*
	@RequestMapping(value = "/add_mink_type", method = RequestMethod.GET)
	public String addMinkTypeView(Model model){
		model.addAttribute("minkType",new MinkType());
		return "/product_attribute/add_mink_type";
	}
	*/
	
	@RequestMapping(value = "/add_mink_type", method = RequestMethod.POST)
	public String addMinkTypeSubmit(@RequestParam(value="minkType") String minkType){
		MinkType newObj = new MinkType();
		newObj.setMinkType(minkType);
		minkTypeSerivce.add(newObj);
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX + PRODUCT_ATTRIBUTE_MANAGE;
	}
	
	@RequestMapping(value = "/seek_minkType_by_minkType", method = RequestMethod.GET)
	public void seekMinkTypeByMtName(@RequestParam(value="minkType") String minkType,PrintWriter pw){
		MinkType mt = minkTypeSerivce.getByName(minkType);
		JSONObject jo = new JSONObject();
		if(null == mt){
			jo.append("minkTypeExist", "false");
		}
		else{
			jo.append("minkTypeExist", "true");
		}
		pw.write(jo.toString());
		pw.flush();
		pw.close();
	}
	
	@RequestMapping(value = "/delete_mink_type", method = RequestMethod.GET)
	public String minkTypeDelete(MinkType minkType){
		minkTypeSerivce.delete(minkType);
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX + PRODUCT_ATTRIBUTE_MANAGE;
	}
	
	
	/**
	 * 添加皮号码数页面
	 */
//	@RequestMapping(value = "/add_mink_size", method = RequestMethod.GET)
//	public String addMinkSizeView(){
//		return "/product_attribute/add_mink_size";
//	}
	
	@RequestMapping(value = "/get_all_mink_size", method = RequestMethod.GET)
	public void getAllMinkSize(PrintWriter pw){
		List<MinkSize> mList = minkSizeSerivce.getAll();
		// 用于返回前台的JSON对象
		JSONObject joRe = new JSONObject();
		// 从数据表查询到的内容
		JSONArray jaContent = new JSONArray();
		// 按显示顺序填入字段名
		JSONArray jaField = resultFieldArrayMinkSize();
		joRe.put("exportField", jaField);
		
		for(MinkSize mObj : mList){
			JSONObject jo = new JSONObject();
			jo.put("minkSizeId", mObj.getMsId());
			jo.put("minkSize", mObj.getMsSize());
			jaContent.put(jo);
		}
		joRe.put("exportContent", jaContent);
		pw.write(joRe.toString());
		pw.flush();
		pw.close();
		
	}
	
	
	/**
	 * 生成查询结果的字段信息用于前端取数据(作为JSON的KEY)
	 * @return 返回字段信息(代码,名称)(有顺序)
	 */
	private JSONArray resultFieldArrayMinkSize() {
		JSONArray ja = new JSONArray();
		putOneField(ja, "msSizeId", "Id");
		putOneField(ja, "minkSize", "原料名称");
		return ja;
	}
	
	
//	@RequestMapping(value = "/add_mink_size", method = RequestMethod.GET)
//	public String addMinkSizeView(Model model){
////		model.addAttribute("minkSize",new MinkSize());
//		return "/product_attribute/add_mink_size";
//	}
	
	@RequestMapping(value = "/add_mink_size", method = RequestMethod.POST)
	public String addMinkSizeSubmit(@RequestParam(value="minkSize") String minkSize){
		MinkSize newObj = new MinkSize();
		newObj.setMsSize(minkSize);
		minkSizeSerivce.add(newObj);
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX + PRODUCT_ATTRIBUTE_MANAGE;
	}
	
	@RequestMapping(value = "/seek_minkSize_by_minkSize", method = RequestMethod.GET)
	public void seekMinkSizeByMtName(@RequestParam(value="minkSize") String minkSize,PrintWriter pw){
		MinkSize mt = minkSizeSerivce.getByMinkSize(minkSize);
		JSONObject jo = new JSONObject();
		if(null == mt){
			jo.append("minkSizeExist", "false");
		}
		else{
			jo.append("minkSizeExist", "true");
		}
		pw.write(jo.toString());
		pw.flush();
		pw.close();
	}
	
	@RequestMapping(value = "/delete_mink_size", method = RequestMethod.GET)
	public String minkSizeDelete(MinkSize minkSize){
		minkSizeSerivce.delete(minkSize);
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX + PRODUCT_ATTRIBUTE_MANAGE;
	}
	
	/**
	 * 将字段代码和名称加入JSONArray
	 * @param ja JSONArray
	 * @param fieldCode 字段代码
	 * @param fieldName 字段名称
	 */
	private void putOneField(JSONArray ja, String fieldCode, String fieldName) {
		JSONArray jone = new JSONArray();
		jone.put(fieldCode);
		jone.put(fieldName);
		ja.put(jone);
	}
	
	
}
