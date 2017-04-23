package com.wzr.controller;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.wzr.dao.entity.CompleteDetail;
import com.wzr.dao.entity.Employee;
import com.wzr.dao.entity.EnumProductState;
import com.wzr.dao.entity.EnumProductStatus;
import com.wzr.dao.entity.Product;
import com.wzr.dao.entity.TransferOrderDetail;
import com.wzr.dao.entity.User;
import com.wzr.dao.entity.Workshop;
import com.wzr.dao.service.CompleteDetailService;
import com.wzr.dao.service.EmployeeService;
import com.wzr.dao.service.ProductService;
import com.wzr.dao.service.TransferOrderDetailService;
import com.wzr.dao.service.UserService;
import com.wzr.dao.service.WorkshopService;
import com.wzr.dao.service.UtilService;

/**
 * 此类处理商品完工2015-12-6
 * @author wzr
 *
 */
@Controller
@RequestMapping("/complete")
public class CompleteController {
	
	private static final String COMPLETE_MANAGE = "/complete/manage";

	private static final String COMPLETE_ADDCPBYSTOCK = "/complete/addcpbystock";

	private static final String COMPLETE_CPDVIEW = "/complete/cpdview";

	private static final String LOGIN_USERNAME = "loginUsername";

	@Autowired
	private UserService userService;
	
	@Autowired
	private CompleteDetailService completeDetailService;

	@Autowired
	private TransferOrderDetailService transferOrderDetailService;

	@Autowired
	private WorkshopService workshopService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private ProductService productService;
	

	@RequestMapping(value = "/manage", method = RequestMethod.GET)
	public String completeMainManage(){
		
		return COMPLETE_MANAGE;
	}
	
	@RequestMapping(value = "/addcpbystockview", method = RequestMethod.GET)
	public String addCpByStockView(){
		
		return COMPLETE_ADDCPBYSTOCK;
	}
	
	@RequestMapping(value = "/cancelcpbystockview", method = RequestMethod.GET)
	public String addCnByStockView(Model model){
		
		return "/complete/pdcancel";
	}
	
	/**
	 * 打开显示完工明细页面
	 * @return
	 */
	@RequestMapping(value = "/cpdview", method = RequestMethod.GET)
	public String cpdView(@RequestParam("cmId") String cmId,Model model){
		if(null != cmId){
			
			model.addAttribute("cmId", cmId);
		}
		return COMPLETE_CPDVIEW;
	}

	/**
	 * 根据条码判断商品是否处于可接收状态
	 * @param pw
	 */
	@RequestMapping(value = "/wfabybarcode", method = RequestMethod.GET)
	public void wfaBybarCode(@RequestParam("barCode") String barCode,PrintWriter pw){
		UtilService us = createUtilService();
		JSONObject jo = us.whereIsTheProduct(barCode);
		if(jo.getString("pdState").equals(EnumProductState.ACCEPTED.getName())){
			jo.put("err", "商品已处于接收状态，不能接收。");
		}
		if(jo.getString("pdState").equals("UNKNOWN")){
			jo.put("err", "查询出现未知错误.");
		}
		if(jo.getString("pdState").equals(EnumProductState.NONE.getName())){
			jo.put("err", "系统中不存在此商品, 不能调拨。");
		}
		if(jo.getString("pdState").equals(EnumProductState.PRODUCT_INFO.getName())){
			jo.put("err", "商品目录中已存在, 但未产生业务数据, 不能调拨。");
		}
		if(jo.getString("pdState").equals(EnumProductState.CANCEL.getName())){
			jo.put("err", "商品已取消生产, 不能调拨。");
		}
		if(jo.getString("pdState").equals(EnumProductState.DONE.getName())){
			// 无此商品，或商品已出厂
			jo.put("err", "商品已完工, 不能调拨。");
		}
		pw.write(jo.toString());
		pw.flush();
		pw.close();
	}

	/**
	 * 构造一个UtilService
	 * @return
	 */
	private UtilService createUtilService() {
		UtilService us = new UtilService();
		us.setProductService(productService);
		us.setTransferOrderDetailService(transferOrderDetailService);
		us.setCompleteDetailService(completeDetailService);
		us.setWorkshopService(workshopService);
		us.setEmployeeService(employeeService);
		return us;
	}
	
	/**
	 * 根据条码判断商品是否处于可调拨状态
	 * @param pw
	 */
	@RequestMapping(value = "/wftbybarcode", method = RequestMethod.GET)
	public void wftBybarCode(@RequestParam("barCode") String barCode,PrintWriter pw){
		UtilService us = createUtilService();
		JSONObject jo = us.whereIsTheProduct(barCode);
		if(jo.getString("pdState").equals(EnumProductState.WAIT_FOR_ACCEPT.getName())){
			// 商品未接收，在发送人手中
			jo.put("err", "商品处于未接收状态，不能调拨。");
		}
		if(jo.getString("pdState").equals("UNKNOWN")){
			jo.put("err", "查询出现未知错误.");
		}
		if(jo.getString("pdState").equals(EnumProductState.NONE.getName())){
			jo.put("err", "系统中不存在此商品, 不能调拨。");
		}
		if(jo.getString("pdState").equals(EnumProductState.PRODUCT_INFO.getName())){
			jo.put("err", "商品目录中已存在, 但未产生业务数据, 不能调拨。");
		}
		if(jo.getString("pdState").equals(EnumProductState.CANCEL.getName())){
			jo.put("err", "商品已取消生产, 不能调拨。");
		}
		if(jo.getString("pdState").equals(EnumProductState.DONE.getName())){
			// 无此商品，或商品已出厂
			jo.put("err", "商品已完工, 不能调拨。");
		}
		pw.write(jo.toString());
		pw.flush();
		pw.close();
	}
	/**
	 * 根据条码判断商品是否处于可完工状态
	 * @param pw
	 */
	@RequestMapping(value = "/wffbybarcode", method = RequestMethod.GET)
	public void wffBybarCode(@RequestParam("barCode") String barCode,PrintWriter pw){
		UtilService us = createUtilService();
		JSONObject jo = us.whereIsTheProduct(barCode);
		validateWff(jo);
		pw.write(jo.toString());
		pw.flush();
		pw.close();
	}
	
	/**
	 * 根据条码取商品进度明细列表
	 * @param pw
	 */
	@RequestMapping(value = "/getptbybarcode", method = RequestMethod.GET)
	public void getPtJSONListByBarCode(@RequestParam("barCode") String barCode, PrintWriter pw){
		UtilService us = createUtilService();
		JSONObject jo = us.whereIsTheProduct(barCode);
		jo.put("barCode", barCode);
		inputPtInfo(jo);
		pw.write(jo.toString());
		pw.flush();
		pw.close();
	}
	
	/**
	 * 根据商品状态(Json对象中的"pdState"元素),查询商品进度("pdStatus")并加入Json对象中
	 * 初始条件 : joIO需含条码"barCode"元素
	 * @param joIO : 输入输出参数, 使用此参数的"pdState"元素, 并将结果写入"ptInfo"元素
	 */
	private void inputPtInfo(JSONObject joIO) {
		
		if(joIO.getString("pdState").equals("UNKNOWN")){
			joIO.put("pdStatus", "查询出现未知错误.");
		}
		if(joIO.getString("pdState").equals(EnumProductState.NONE.getName())){
			joIO.put("pdStatus", "系统中不存在此商品。");
		}
		if(joIO.getString("pdState").equals(EnumProductState.PRODUCT_INFO.getName())){
			joIO.put("pdStatus", "商品目录中已存在, 但未产生业务数据。");
		}
		JSONArray ja = new JSONArray();
		String barCode = joIO.getString("barCode");
		if(joIO.getString("pdState").equals(EnumProductState.WAIT_FOR_ACCEPT.getName())
				|| joIO.getString("pdState").equals(EnumProductState.ACCEPTED.getName())){
			// 商品处于生产过程中
			if(joIO.getString("pdState").equals(EnumProductState.WAIT_FOR_ACCEPT.getName())){
				joIO.put("pdStatus", "商品处于生产过程中的待接收状态。");
			}else{
				joIO.put("pdStatus", "商品处于生产过程中的已接收状态。");
			}
			List<TransferOrderDetail> todList = transferOrderDetailService.getListByBarCode(barCode);
			for(TransferOrderDetail tod : todList){
				JSONObject jo = convertTodToJson(tod);
				ja.put(jo);
			}
			joIO.put("ptInfo", ja);
		}
		if(joIO.getString("pdState").equals(EnumProductState.CANCEL.getName())
				|| joIO.getString("pdState").equals(EnumProductState.DONE.getName())){
			//	"商品已取消生产。"或"商品已完工。");
			if(joIO.getString("pdState").equals(EnumProductState.CANCEL.getName())){
				joIO.put("pdStatus", "商品已取消生产。");
			}else{
				joIO.put("pdStatus", "商品已完工。");
			}
			List<CompleteDetail> cpdList = completeDetailService.getListByBarCode(barCode);
			for(CompleteDetail cpd : cpdList){
				JSONObject jo = convertCpdToJson(cpd);
				ja.put(jo);
			}
			joIO.put("ptInfo", ja);
		}
	}
	
	/**
	 * 将完工明细记录转为JSON对象
	 * 初始条件 : cpd不为null
	 * @param cpd : 完工明细记录对象
	 * @return
	 */
	private JSONObject convertCpdToJson(CompleteDetail cpd) {
		JSONObject jo = new JSONObject();
		UtilService us = createUtilService();
		jo.put("fromWsName", us.getWorkshopName(cpd.getFromWs()));
		jo.put("fromEpName", us.getEmployeeName(cpd.getFromEp()));
		jo.put("toWsName", us.getWorkshopName(cpd.getToWs()));
		jo.put("toEpName", us.getEmployeeName(cpd.getToEp()));
		jo.put("barCode", cpd.getBarCode());
		jo.put("fromTime", cpd.getFromTime());
		if(null == cpd.getToTime()){
			jo.put("toTime", "");
		}else{
			jo.put("toTime", cpd.getToTime());
		}
		return jo;
	}
	
	/**
	 * 将调拨明细记录转为JSON对象
	 * 初始条件 : tod不为null
	 * @param tod : 完工明细记录对象
	 * @return
	 */
	private JSONObject convertTodToJson(TransferOrderDetail tod) {
		JSONObject jo = new JSONObject();
		Workshop ws = workshopService.getById(tod.getFromWs().getWsId());
		jo.put("fromWsName", ws.getWsName());
		if(null == tod.getFromEp()){
			jo.put("fromEpName", "");
		}else{
			Employee ep = employeeService.getById(tod.getFromEp().getEpId());
			if(null == ep){
				jo.put("fromEpName", "");
			}else{
				jo.put("fromEpName", ep.getEpName());
			}
		}
		
		ws = workshopService.getById(tod.getToWs().getWsId());
		jo.put("toWsName", ws.getWsName());
		
		if(null == tod.getToEp()){
			jo.put("toEpName", "");
		}else{
			Employee ep = employeeService.getById(tod.getToEp().getEpId());
			if(null == ep){
				jo.put("toEpName", "");
			}else{
				jo.put("toEpName", ep.getEpName());
			}
		}
		jo.put("barCode", tod.getBarCode());
		jo.put("fromTime", tod.getFromTime());
		if(null == tod.getToTime()){
			jo.put("toTime", "");
		}else{
			jo.put("toTime", tod.getToTime());
		}
		jo.put("fromUserId", tod.getFromUserId());
		return jo;
	}
	
	/**
	 * 根据商品状态(Json对象中的"pdState"元素),判断是否可以完工,并将判断结果写入"err"元素
	 * @param joIO : 输入输出参数, 使用此参数的"pdState"元素, 并将判断结果写入"err"元素
	 */
	private void validateWff(JSONObject joIO) {
		if(joIO.getString("pdState").equals(EnumProductState.WAIT_FOR_ACCEPT.getName())){
			// 商品未接收，在发送人手中
			joIO.put("err", "商品处于未接收状态，不能完工。");
		}
		if(joIO.getString("pdState").equals("UNKNOWN")){
			joIO.put("err", "查询出现未知错误.");
		}
		if(joIO.getString("pdState").equals(EnumProductState.NONE.getName())){
			joIO.put("err", "系统中不存在此商品, 不能完工。");
		}
		if(joIO.getString("pdState").equals(EnumProductState.PRODUCT_INFO.getName())){
			joIO.put("err", "商品目录中已存在, 但未产生业务数据, 不能完工。");
		}
		if(joIO.getString("pdState").equals(EnumProductState.CANCEL.getName())){
			joIO.put("err", "商品已取消生产, 不能完工。");
		}
		if(joIO.getString("pdState").equals(EnumProductState.DONE.getName())){
			// 无此商品，或商品已出厂
			joIO.put("err", "商品已完工, 不能重复完工。");
		}
	}
	
	/**
	 * 根据条码判断商品是否处于可取消状态, 
	 * 结果写入Json对象的"pdState"元素, 若商品存在则商品信息写入"styleCode"等元素,
	 * 若不可取消则添加"err"元素,
	 * @param pw
	 */
	@RequestMapping(value = "/wfcbybarcode", method = RequestMethod.GET)
	public void wfcBybarCode(@RequestParam("barCode") String barCode,PrintWriter pw){
		UtilService us = createUtilService();
		JSONObject jo = us.whereIsTheProduct(barCode);
		validateWfc(jo);
		jo.put("barCode", barCode);
		inputPtInfo(jo);
		pw.write(jo.toString());
		pw.flush();
		pw.close();
	}

	/**
	 * 根据商品状态(Json对象中的"pdState"元素), 将判断结果写入"err"元素
	 * @param joIO : 输入输出参数, 使用此参数的"pdState"元素, 并将判断结果写入"err"元素
	 */
	private void validateWfc(JSONObject joIO) {
		
		if(joIO.getString("pdState").equals("UNKNOWN")){
			joIO.put("err", "查询出现未知错误.");
		}
		if(joIO.getString("pdState").equals(EnumProductState.NONE.getName())){
			joIO.put("err", "系统中不存在此商品, 不能取消。");
		}
		if(joIO.getString("pdState").equals(EnumProductState.CANCEL.getName())){
			joIO.put("err", "商品已取消生产, 不能重复取消。");
		}
		if(joIO.getString("pdState").equals(EnumProductState.DONE.getName())){
			// 无此商品，或商品已出厂
			joIO.put("err", "商品已完工, 不能取消。");
		}
	}

	/**
	 * 根据完工商品列表,添加完工商品记录
	 */
	@RequestMapping(value = "/addcpbycpsjson", method = RequestMethod.POST)
	@Transactional(rollbackFor = Exception.class)
	public void addCpByCpsJson(HttpServletRequest req, PrintWriter printWriter, Model model){
		String err = "";
		if(null != req.getParameter("cpds") && !req.getParameter("cpds").isEmpty()){
			
			String cpds = req.getParameter("cpds");
			JSONArray ja = new JSONArray(cpds);
			// 1. 验证List<CompleteDetail>中的商品是否可以完工, 返回第一个不能完工商品的Json对象
			JSONObject jo = validateProductsCanFinish(ja);
			if(null == jo){
				int userId = getLoginUserId(req);
				addCp(ja,userId,EnumProductStatus.DONE);
				
			}else{
				assert null == jo : "不能处理的数据被传了进来";
			}
		}else{
			err = "服务器没有接收到数据";
		}
		
			printWriter.write(err);
			printWriter.flush();
			printWriter.close();
	}
	
	/**
	 * 根据取消商品列表,添加取消商品记录
	 */
	@RequestMapping(value = "/addcnbycnsjson", method = RequestMethod.POST)
	@Transactional(rollbackFor = Exception.class)
	public void addCnByCnsJson(HttpServletRequest req, PrintWriter printWriter, Model model){
		String err = "";
		if(null != req.getParameter("cns") && !req.getParameter("cns").isEmpty()){
			
			String cpds = req.getParameter("cns");
			JSONArray ja = new JSONArray(cpds);
			// 1. 验证JSONArray中的商品是否可以取消, 返回第一个不能取消商品的Json对象
			JSONObject jo = validateProductsCanCancel(ja);
			if(null == jo){
				// 商品都可取消
				int userId = getLoginUserId(req);
				addCp(ja,userId,EnumProductStatus.CANCEL);
			}else{
				assert null == jo : "不能处理的数据被传了进来";
				err = jo.getString("err");
			}
		}else{
			err = "服务器没有接收到数据";
		}
		
			printWriter.write(err);
			printWriter.flush();
			printWriter.close();
	}

	/**
	 * 取当前登录的操作员Id
	 * @param req
	 * @return
	 */
	private int getLoginUserId(HttpServletRequest req) {
		String username = req.getSession().getAttribute(LOGIN_USERNAME).toString();
		User user = userService.getByName(username);
		int userId = user.getUserid();
		return userId;
	}
	/**
	 * 商品完工(或取消生产)操作
	 * @param ja : 保存条码列表的JSONArray对象
	 * @param userId : 操作员Id
	 * @param productStatus : 商品保存状态(完工或取消)
	 */
	private void addCp(JSONArray ja, int userId, EnumProductStatus productStatus) {
		// 商品都可完工(取消)
		List<CompleteDetail> cpdList = new ArrayList<CompleteDetail>();

		CompleteDetail cpd;
		for(int i=0; i < ja.length(); ++i){
			cpd = new CompleteDetail();
			UtilService us = createUtilService();
			JSONObject joWhere = us.whereIsTheProduct(ja.getJSONObject(i).getString("barCode"));
			cpd.setBarCode(ja.getJSONObject(i).getString("barCode"));
			cpd.setFromWs(joWhere.getInt("wsId"));
			cpd.setFromEp((joWhere.getInt("epId")==0)? null : joWhere.getInt("epId"));
			cpd.setAddTime(new Timestamp(System.currentTimeMillis()));
			cpd.setFromTime(new Timestamp(System.currentTimeMillis()));
			cpd.setFromUserId(userId);
			
			// 2. 添加此商品的所有调拨明细记录到完工明细表(complete_detail)
			copyTodToCpd(cpd.getBarCode());
			
			// 删除此商品的所有调拨明细记录
			deleteTodByBarCode(cpd.getBarCode());
			
			// 3. 修改商品目录中商品的状态字段
			productService.updateStatus(cpd.getBarCode(), productStatus.getStatus());
			
			cpdList.add(cpd);
		}
		
		// 4. 添加完工明细表记录
		addCompleteDetail(cpdList);
	}
	
	/**
	 * 根据条码删除此商品的所有调拨明细记录
	 * @param barCode
	 */
	private void deleteTodByBarCode(String barCode) {
		transferOrderDetailService.deleteByBarCode(barCode);
	}

	/**
	 * 根据条码将此商品调拨单明细添加到完工明细表
	 * @param cpdList
	 */
	private void copyTodToCpd(String barCode) {
		completeDetailService.addFromTod(barCode);
	}

	/**
	 * 将JSON格式字符串转换为CompleteDetail列表
	 * @param cpds:JSON格式字符串
	 * @return
	 */
	private List<CompleteDetail> convertCpdJsonToList(String cpds) {
		JSONArray ja = new JSONArray(cpds);
		List<CompleteDetail> cpdList = new ArrayList<CompleteDetail>();
		for(int i=0; i < ja.length(); ++i){
			JSONObject jo = ja.getJSONObject(i);
			CompleteDetail cpd = new CompleteDetail();
			//cpd.setCdId(i+1);
			
			cpd.setFromWs(jo.getInt("fromWs"));
			
			if(0 == jo.getInt("fromEp")){
				cpd.setFromEp(null);
			}else{
				cpd.setFromEp(jo.getInt("fromEp"));
			}
			
			if(jo.has("toWs")){
				cpd.setToWs(jo.getInt("toWs"));
			}else{
				cpd.setToWs(null);
			}
			
			if(jo.has("toEp")){
				if(0 == jo.getInt("toEp")){
					cpd.setToEp(null);
				}else{
					cpd.setToEp(jo.getInt("toEp"));
				}
			}else{
				cpd.setToEp(null);
			}
			
			cpd.setBarCode(jo.getString("barCode"));
			cpd.setFromTime(new Timestamp(System.currentTimeMillis()));
//			cpd.setToTime(null);
			
			cpdList.add(cpd);
		}
		return cpdList;
	}

	/**
	 * 验证List<CompleteDetail>中的商品是否可以完工
	 * @param cpds
	 * @return: 如果含有不能完工的商品则返回第一个不能完工的CompleteDetail
	 */
	private JSONObject validateProductsCanFinish(JSONArray ja) {
		for(int i=0; i < ja.length(); ++i){
			JSONObject jor = validateProductCanFinish(ja.getJSONObject(i).getString("barCode"));
			if(null != jor){
				return jor;
			}
		}
		return null;
	}

	
	/**
	 * 确定商品是否能完工
	 * @param barCode
	 * @return
	 */
	private JSONObject validateProductCanFinish(String barCode) {
		UtilService us = createUtilService();
		JSONObject jo = us.whereIsTheProduct(barCode);
		validateWff(jo);
		if(jo.getString("pdState").equals(EnumProductState.ACCEPTED.getName())){
			return null;
		}else{
			return jo;
		}
	}
	
	/**
	 * 验证JSONArray中的商品是否可以取消
	 * @param ja : 保存条码列表
	 * @return: 如果含有不能取消的商品则返回第一个不能完工的JSONObject
	 */
	private JSONObject validateProductsCanCancel(JSONArray ja) {
		for(int i=0; i < ja.length(); ++i){
			JSONObject jor = validateProductsCanCancel(ja.getJSONObject(i).getString("barCode"));
			if(null != jor){
				return jor;
			}
		}
		return null;
	}
	
	
	/**
	 * 确定商品是否能取消
	 * @param barCode
	 * @return
	 */
	private JSONObject validateProductsCanCancel(String barCode) {
		UtilService us = createUtilService();
		JSONObject jo = us.whereIsTheProduct(barCode);
		validateWfc(jo);
		if(jo.getString("pdState").equals(EnumProductState.ACCEPTED.getName())
				|| jo.getString("pdState").equals(EnumProductState.WAIT_FOR_ACCEPT.getName())
				|| jo.getString("pdState").equals(EnumProductState.PRODUCT_INFO.getName())){
			return null;
		}else{
			return jo;
		}
	}

		
	/**
	 * 添加完工明细表记录(要求已经通过数据合法性验证)
	 * @param cpdList: 明细数据对象列表
	 */
	private void addCompleteDetail(List<CompleteDetail> cpdList){

		completeDetailService.addBatch(cpdList);
		
	}
	
/*
	
	
	
	
	
	
	//*********************

	// 局部异常处理, 发生异常时返回登录界面
		@ExceptionHandler(UploadException.class)
		public String handlerUploadException(Exception excep,HttpServletRequest req){
			req.setAttribute("excep", excep);
			//req.setAttribute(LOGIN_USER,new User());
			return ADDIMPORT;
		}
		
		@ExceptionHandler(TransferOrderException.class)
		public String handlerTransferOrderException(Exception excep,HttpServletRequest req){
			req.setAttribute("excep", excep);
			//req.setAttribute(LOGIN_USER,new User());
			return ADDIMPORT;
		}
		
*/
	
}
