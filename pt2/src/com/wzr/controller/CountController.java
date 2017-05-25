package com.wzr.controller;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.wzr.dao.entity.CompleteDetail;
import com.wzr.dao.entity.Employee;
import com.wzr.dao.entity.EnumProductState;
import com.wzr.dao.entity.Product;
import com.wzr.dao.entity.TransferOrderDetail;
import com.wzr.dao.entity.Workshop;
import com.wzr.dao.service.CompleteDetailService;
import com.wzr.dao.service.EmployeeService;
import com.wzr.dao.service.ProductService;
import com.wzr.dao.service.TransferOrderDetailService;
import com.wzr.dao.service.UtilService;
import com.wzr.dao.service.WorkshopService;

/**
 * 此类处理查询统计
 * (建立:2016-1-19)
 * (修改:2016-4-1)
 * @author wzr
 *
 */
@Controller
@RequestMapping("/count")
public class CountController {
	
	@Autowired
	private TransferOrderDetailService transferOrderDetailService;

	@Autowired
	private CompleteDetailService completeDetailService;

	@Autowired
	private ProductService productService;	

	@Autowired
	private WorkshopService workshopService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@RequestMapping(value = "/seek-complete-product-view", method = RequestMethod.GET)
	public String getCpView(){
		// 查询已完工商品
		return "/count/seek-complete-product-view";
	}
	
	@RequestMapping(value = "/seektodbydoingview", method = RequestMethod.GET)
	public String getWfsByTodView(){
		// 待发送商品查询页面
		return "count/seektodbydoing";
	}

	@RequestMapping(value = "/countnumberproduce", method = RequestMethod.GET)
	public String countNumberProduceView(){
		// 产量统计
		return "count/countnumberproduce";
	}
	
	@RequestMapping(value = "/seekpdbycusview", method = RequestMethod.GET)
	public String seekProductbyCustomerView(){
		// 接收统计
		return "count/seekproductbycustomer";
	}

	@RequestMapping(value = "/countitems", method = RequestMethod.GET)
	public String countItemsView(){
		// 查询统计页面
		return "count/countitems";
	}
	
	
	/**
	 * 查询某部门某时间段内发送的商品(包括未完工和已完工)
	 * 发送的商品必须已被接收部门接收
	 * @param pw
	 */
	@RequestMapping(value = "/sendpbywsid", method = RequestMethod.GET)
	public void getSendpByWsId(@RequestParam("wsId") int wsId,
			@RequestParam("dateTime1") String dateTime1,
			@RequestParam("dateTime2") String dateTime2,
			PrintWriter pw){
		// 用于返回前台的JSON对象
		JSONObject joRe = new JSONObject();
		// 按显示顺序填入字段名
		JSONArray jaField = jaSeekProductByWsField();
		joRe.put("exportField", jaField);
		JSONArray jaPdInfo = new JSONArray();
		LocalDateTime dt1 = LocalDateTime.parse(dateTime1);
		LocalDateTime dt2 = LocalDateTime.parse(dateTime2);
		List<TransferOrderDetail> todList = transferOrderDetailService.getListByFromWsId(wsId, dt1, dt2);
		UtilService us = new UtilService();
		us.setProductService(productService);
		for(TransferOrderDetail tod : todList){
			String barCode = tod.getBarCode();
			JSONObject joPd = new JSONObject();
			us.putPdJsonByBarCode(barCode, joPd);
			
			jaPdInfo.put(joPd);
		}
		List<CompleteDetail> cdList = completeDetailService.getListByFromWsId(wsId, dt1, dt2);
		for(CompleteDetail cd : cdList){
			String barCode = cd.getBarCode();
			EnumProductState epsu =  us.getProductStatus(barCode);
			if(EnumProductState.DONE == epsu){
				// 如果商品处于已完工状态，则加入结果集，否则（处于取消等其它状态）不添加
				JSONObject joPd = new JSONObject();
				us.putPdJsonByBarCode(barCode, joPd);
				jaPdInfo.put(joPd);
			}
		}
		joRe.put("exportContent", jaPdInfo);
		pw.write(joRe.toString());
		pw.flush();
		pw.close();
	}
	
	/**
	 * 查询某员工某时间段内发送的商品(包括未完工和已完工)
	 * 发送的商品必须已被接收部门接收
	 * @param pw
	 */
	@RequestMapping(value = "/sendpbyepid", method = RequestMethod.GET)
	public void getSendpByEpId(@RequestParam("epId") int epId,
			@RequestParam("dateTime1") String dateTime1,
			@RequestParam("dateTime2") String dateTime2,
			PrintWriter pw){
		// 用于返回前台的JSON对象
		JSONObject joRe = new JSONObject();
		// 按显示顺序填入字段名
		JSONArray jaField = jaSeekProductByWsField();
		joRe.put("exportField", jaField);
		JSONArray jaPdInfo = new JSONArray();
		if(epId > 0){
			//"legal employee Id." + epId;
			LocalDateTime dt1 = LocalDateTime.parse(dateTime1);
			LocalDateTime dt2 = LocalDateTime.parse(dateTime2);
			List<TransferOrderDetail> todList = transferOrderDetailService.getListByFromEpId(epId, dt1, dt2);
			UtilService us = new UtilService();
			us.setProductService(productService);
			for(TransferOrderDetail tod : todList){
				String barCode = tod.getBarCode();
				JSONObject joPd = new JSONObject();
				us.putPdJsonByBarCode(barCode, joPd);
				jaPdInfo.put(joPd);
			}
			List<CompleteDetail> cdList = completeDetailService.getListByFromEpId(epId, dt1, dt2);
			for(CompleteDetail cd : cdList){
				String barCode = cd.getBarCode();
				EnumProductState epsu = us.getProductStatus(barCode);
				if(EnumProductState.DONE == epsu){
					// 如果商品处于已完工状态，则加入结果集，否则（处于取消等其它状态）不添加
					JSONObject joPd = new JSONObject();
					us.putPdJsonByBarCode(barCode, joPd);
					jaPdInfo.put(joPd);
				}
			}
		}
		joRe.put("exportContent", jaPdInfo);
		pw.write(joRe.toString());
		pw.flush();
		pw.close();
	}
	

	/**
	 * 查询某客户订货的商品目前所在位置(部门.员工)
	 * @param pw
	 */
	@RequestMapping(value = "/seekpdbycuscode", method = RequestMethod.GET)
	public void seekProductbyCusCode(@RequestParam("cusCode") String cusCode,	PrintWriter pw){
		// 用于返回前台的JSON对象
		JSONObject joRe = new JSONObject();
		// 按显示顺序填入字段名
		JSONArray jaField = jaSeekProductByCusField();
		joRe.put("exportField", jaField);
		JSONArray jaPdInfo = new JSONArray();
		List<Product> pdList = productService.getListByCusCode(cusCode);
		UtilService us = createUtilService();
		for(Product pd : pdList){
			JSONObject joPd = new JSONObject();
			joPd = us.whereIsTheProduct(pd.getBarCode());
			
			jaPdInfo.put(joPd);
		}
		
		joRe.put("exportContent", jaPdInfo);
		pw.write(joRe.toString());
		pw.flush();
		pw.close();
	}
	
	/**
	 * 查询某员工某时间段内发送的商品(包括未完工和已完工)
	 * 发送的商品必须已被接收部门接收
	 * @param pw
	 */
	@RequestMapping(value = "/seektodbydoing", method = RequestMethod.GET)
	public void seekTodByDoing(@RequestParam("wsId") int wsId, @RequestParam("epId") int epId,
			@RequestParam("doDay") int doDay, PrintWriter pw){
		// 用于返回前台的JSON对象
		JSONObject joRe = new JSONObject();
		// 按显示顺序填入字段名
		JSONArray jaField = jaSeekProductByCusField();
		joRe.put("exportField", jaField);
		JSONArray jaPdInfo = new JSONArray();
		if(doDay > 0){
			//"legal employee Id." + epId;
			LocalDateTime paramDate = LocalDateTime.now().plusDays(-1 * doDay);
			List<TransferOrderDetail> todList = transferOrderDetailService.getListByDoing(wsId,epId, paramDate);
			UtilService us = createUtilService();
			for(TransferOrderDetail tod : todList){
				String barCode = tod.getBarCode();
				JSONObject joPd = new JSONObject();
				joPd = us.whereIsTheProduct(barCode);

				jaPdInfo.put(joPd);
			}
			
		}
		joRe.put("exportContent", jaPdInfo);
		pw.write(joRe.toString());
		pw.flush();
		pw.close();
	}
	
	
	/**
	 * 查询某员工某时间段内完工的商品(当前只实现所有商品)
	 * @param pw
	 */
	@RequestMapping(value = "/seek-complete-product", method = RequestMethod.GET)
	public void seekCompleteProduct(PrintWriter pw){ // @RequestParam("wsId") int wsId, @RequestParam("epId") int epId,		@RequestParam("doDay") int doDay,
		// 用于返回前台的JSON对象
		JSONObject joRe = new JSONObject();
		// 按显示顺序填入字段名
		JSONArray jaField = jaSeekCompleteProductField();
		joRe.put("exportField", jaField);
		JSONArray jaPdInfo = new JSONArray();
		List<CompleteDetail> cdList = completeDetailService.getAllLastList();
		if(cdList != null && cdList.size() > 0){
//			LocalDateTime paramDate = LocalDateTime.now().plusDays(-1 * doDay);
//			List<TransferOrderDetail> todList = transferOrderDetailService.getListByDoing(wsId,epId, paramDate);
			UtilService us = createUtilService();
			for(CompleteDetail cd : cdList){
				String barCode = cd.getBarCode();
				JSONObject joPd = new JSONObject();
				us.putPdJsonByBarCode(barCode, joPd);
				if(null != cd.getFromWs() && 0 != cd.getFromWs()){
					// fromWs不为空
					Workshop ws = workshopService.getById(cd.getFromWs());
					joPd.put("wsName", ws.getWsName());
				}
				else{
					// 为空意味着出错了，应该抛异常
					joPd.put("wsName", "");
				}
				if(null != cd.getFromEp() && 0 != cd.getFromEp()){
					// fromEp不为空
					Employee ep = employeeService.getById(cd.getFromEp());
					joPd.put("epName", ep.getEpName());
				}
				else{
					joPd.put("epName", "");
				}
				
				jaPdInfo.put(joPd);
			}
			
		}
		joRe.put("exportContent", jaPdInfo);
		pw.write(joRe.toString());
		pw.flush();
		pw.close();
	}
	

	/**
	 * 生成查询结果的字段信息用于前端取数据(作为JSON的KEY)
	 * @return 返回字段信息(代码,名称)(有顺序)
	 */
	private JSONArray jaSeekCompleteProductField() {
		JSONArray ja = new JSONArray();
		putOneField(ja, "barCode", "条码");
		putOneField(ja, "styleCode", "款号");
		putOneField(ja, "pdName", "颜色");
		putOneField(ja, "length", "长度");
		putOneField(ja, "size", "尺码");
		putOneField(ja, "cusCode", "类别");
		putOneField(ja, "billNo", "单号");
		putOneField(ja, "wsName","所在部门");
		putOneField(ja, "epName","员工");
//		putOneField(ja, "statusName", "商品状态");
		return ja;
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
	 * 查询结果的字段名称用于前端取数据(作为JSON的KEY)
	 * 按客户代码查询时,加入2个位置属性
	 * @return
	 */
	private JSONArray jaSeekProductByCusField() {
		JSONArray ja = jaSeekProductByWsField();
		putOneField(ja, "wsName","所在部门");
		putOneField(ja, "epName","员工");
		return ja;
	}


	/**
	 * 生成查询结果的字段信息用于前端取数据(作为JSON的KEY)
	 * @return 返回字段信息(代码,名称)(有顺序)
	 */
	private JSONArray jaSeekProductByWsField() {
		JSONArray ja = new JSONArray();
		putOneField(ja, "barCode", "条码");
		putOneField(ja, "styleCode", "款号");
		putOneField(ja, "pdName", "颜色");
		putOneField(ja, "length", "长度");
		putOneField(ja, "size", "尺码");
		putOneField(ja, "cusCode", "类别");
		putOneField(ja, "billNo", "单号");
		putOneField(ja, "statusName", "商品状态");
		return ja;
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
