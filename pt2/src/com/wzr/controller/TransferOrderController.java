package com.wzr.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.wzr.dao.dao.TransferOrderDetailDao;
import com.wzr.dao.service.CompleteDetailService;
import com.wzr.dao.service.EmployeeService;
import com.wzr.dao.service.ProductService;
import com.wzr.dao.service.TransferOrderDetailService;
import com.wzr.dao.service.TransferOrderMainService;
import com.wzr.dao.service.UserService;
import com.wzr.dao.service.WorkshopService;
import com.wzr.exception.ProductStatusException;
import com.wzr.exception.UploadException;
import com.wzr.dao.entity.singlexmo.*;
import com.wzr.dao.entity.CompleteDetail;
import com.wzr.dao.entity.Employee;
import com.wzr.dao.entity.EnumProductState;
import com.wzr.dao.entity.Product;
import com.wzr.dao.entity.TransferOrderDetail;
import com.wzr.dao.entity.TransferOrderMain;
import com.wzr.dao.entity.User;
import com.wzr.dao.entity.Workshop;
import com.wzr.dao.entity.singlexmo.SingleSheet;
import com.wzr.dao.entity.singlexmo.SingleXlsxManuOrder;


@Controller
@RequestMapping("/transferorder")
public class TransferOrderController {

	private static final String ADDIMPORT = "/addimport";
	
	private static final String TRANSFERORDER = "/transferorder";
	
	private static final String TRANSFERORDER_ADDIMPORT = TRANSFERORDER + ADDIMPORT;

	private static final String TRANSFERORDER_MANAGE = "/transferorder/manage";
	
	private static final String LOGIN_USERNAME = "loginUsername";
	
	@Autowired
	private TransferOrderMainService transferOrderMainService;
	
	@Autowired
	private TransferOrderDetailService transferOrderDetailService;
	
	@Autowired
	private WorkshopService workshopService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private CompleteDetailService completeDetailService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;
	
	
	@RequestMapping(value = "/manage", method = RequestMethod.GET)
	public String transferOrderMainManage(Model model){
//		LocalDateTime dateTime2 = LocalDateTime.now();
//		LocalDateTime dateTime1 = dateTime2.plusDays(-5);
//		List<TransferOrderMain> transferOrderMainList = transferOrderMainService.getAll();
//		for(TransferOrderMain tom : transferOrderMainList){
//			User user = userService.getById(tom.getUser().getUserid());
//			tom.setUser(user);
//		}
//		model.addAttribute("dateTime1", dateTime1);
//		model.addAttribute("transferOrderMainList", transferOrderMainList);
		return TRANSFERORDER_MANAGE;
	}
	
	@RequestMapping(value = "/getbydatetime", method = RequestMethod.GET)
	public String transferOrderMainGetByDateTime(Model model, HttpServletRequest req){
		LocalDateTime dateTime1 = LocalDateTime.parse((String)req.getAttribute("dateTime1"),
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		LocalDateTime dateTime2 = LocalDateTime.parse((String)req.getAttribute("dateTime2"),
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		
		List<TransferOrderMain> transferOrderMainList = transferOrderMainService.getListByDateTime(dateTime1, dateTime2);
		model.addAttribute("transferOrderMainList", transferOrderMainList);
		return TRANSFERORDER_MANAGE;
	}
	
	
	@RequestMapping(value = ADDIMPORT, method = RequestMethod.GET)
	public String transferOrderMainAddView(Model model){
		return TRANSFERORDER_ADDIMPORT;
	}
	
	
	
	
	/**
	 * 返回"新建调拨单[现有库存]"页面地址
	 * @return
	 */
	@RequestMapping(value = "/addtobystockview", method = RequestMethod.GET)
	public String addToByStockView(){
		return TRANSFERORDER + "/addtobystock";
	}
	
		
	@RequestMapping(value = "/pdbybarcode", method = RequestMethod.GET)
	public void getProductTrace(@RequestParam("barCode") String barCode, PrintWriter pw){
		JSONArray ja = new JSONArray();

		List<TransferOrderDetail> todL = transferOrderDetailService.getListByBarCode(barCode);
		if(null != todL){ // 在调拨明细表中存在, 此商品处于生产过程中
			for(TransferOrderDetail tod : todL){
				JSONObject js = new JSONObject();
				//js.put("tdId", tod.getTdId());
				Workshop fromWs = workshopService.getById(tod.getFromWs().getWsId());
				js.put("fromWs.wsName", fromWs.getWsName());
				Employee fromEp = employeeService.getById(tod.getFromEp().getEpId());
				js.put("fromEp.epName", fromEp.getEpName());
				Workshop toWs = workshopService.getById(tod.getToWs().getWsId());
				js.put("toWs.wsName", toWs.getWsName());
				Employee toEp = employeeService.getById(tod.getToEp().getEpId());
				js.put("toEp.epName", toEp.getEpName());
				//js.put("barCode", tod.getBarCode());
				js.put("fromTime", tod.getFromTime());
				js.put("toTime", tod.getToTime());
				ja.put(js);
			}
		}else{ // 在调拨明细表中不存在, 此商品未处于生产过程中
			
		}
		pw.write(ja.toString());
		pw.flush();
		pw.close();
	}
	
	/**
	 * 打开上传excel文件页面
	 * @param part
	 * @param req
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "upload", method = RequestMethod.GET)
	public String transferOrderUploadBill() {
		return "/transferorder/upload";
	}
	
	/**
	 * 上传excel文件,读取解析,生成明细字符串,传回页面
	 * @param upLoadFile : 上传文件名(不含路径)
	 * @param req
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "uploadimport", method = RequestMethod.POST)
	public String transferOrderImportBill(@RequestParam("importfile") Part upLoadFile
			, Model model, HttpServletRequest req, HttpServletResponse rep) throws IOException{
		
		// 取上传目标路径(绝对路径)
		String upLoadFolder = req.getServletContext().getRealPath("/WEB-INF/upload/");

		String ulJson = getTodJsonByUploadXlsxFile(upLoadFile, upLoadFolder);
		ulJson = ulJson.replace("\\u", "\\\\u"); 	// JSON在生成JSON数组时会自动把某些全角符号转为Unicode编码，为了在页面正确解码必须加\\
//		System.out.println(ulJson);
		rep.setCharacterEncoding("utf-8");
		model.addAttribute("transferOrderDetailJson", ulJson);
		return TRANSFERORDER_ADDIMPORT;
	}

	
	/**
	 * 读取Excel文件，生成JSONArray格式的字符串
	 * @param upLoadFile  上传文件名
	 * @param upLoadFolder  上传目标路径(绝对路径)
	 * @return : 成功返回JSONArray格式的字符串
	 * @throws IOException
	 */
	/*
	private String getTodJsonByUploadFile(Part upLoadFile, String upLoadFolder) throws IOException {
		List<List<String>> rowsList = null;
		// filename为上传文件的全路径名
		String filename = null;
		JSONArray ja = new JSONArray();
		if(null != upLoadFile){
			String uploadfile = upLoadFile.getSubmittedFileName();
			String ext = uploadfile.substring(uploadfile.lastIndexOf("."),uploadfile.length());
			// filename为上传文件的全路径名
			filename = upLoadFolder + uploadfile + UUID.randomUUID().toString() + ext;
			upLoadFile.write(filename);
			
			rowsList = getExcelRows(filename);
			
			
			if(rowsList.get(0).get(0).trim().isEmpty()){
				throw new UploadException("A1单元格内容为空");
			}
			int rowIndex = 0;
			if("编号".equals(rowsList.get(0).get(0).trim())){
				// 第一行为标题行, 跳过
				++rowIndex;
			}
			for(;rowIndex < rowsList.size() ; ++rowIndex){
				List<String> row = rowsList.get(rowIndex);
				JSONObject jo = new JSONObject();
				int ci = 0;
				jo.put("barCode", row.get(ci++).trim());
				jo.put("styleCode", row.get(ci++).trim());
				jo.put("pdName", row.get(ci++).trim());
				jo.put("length", row.get(ci++).trim());
				jo.put("size", row.get(ci++).trim());
				jo.put("cusCode", row.get(ci++).trim());
				jo.put("billNo", row.get(ci++).trim());
				jo.put("remark", row.get(ci++).trim());
								
				ja.put(jo);
			}
		}
		// 删除上传的文件
		deleteFile(filename);
		return ja.toString();
	}
*/
	
	/**
	 * 读取Excel文件，生成JSONArray格式的字符串
	 * @param upLoadFile  上传文件名
	 * @param upLoadFolder  上传目标路径(绝对路径)
	 * @return : 成功返回JSONArray格式的字符串
	 * @throws IOException
	 */
	private String getTodJsonByUploadXlsxFile(Part upLoadFile, String upLoadFolder) throws IOException {
		// filename为上传文件的全路径名
		String filename = null;
		JSONArray ja = new JSONArray();
		if(null != upLoadFile){
			String uploadfile = upLoadFile.getSubmittedFileName();
			String ext = uploadfile.substring(uploadfile.lastIndexOf("."),uploadfile.length());
			// filename为上传文件的全路径名
			filename = upLoadFolder + uploadfile + UUID.randomUUID().toString() + ext;
			upLoadFile.write(filename);
			
			SingleXlsxManuOrder sxmo = new SingleXlsxManuOrder(filename);
			SingleSheet ss = sxmo.getSingleSheetList().get(0);
			
			if(ss.getDetailRows().get(0).getBarCode().trim().isEmpty()){
				throw new UploadException("A1单元格内容为空");
			}
			
			for(DetailRow dr : ss.getDetailRows()){
				
				JSONObject jo = new JSONObject();
				jo.put("barCode", dr.getBarCode().trim());
				jo.put("styleCode", dr.getStyleCode().trim());
				jo.put("pdName", dr.getPdName().trim());
				jo.put("length", dr.getLength().trim());
				jo.put("size", dr.getSize().trim());
				jo.put("cusCode", dr.getCusCode().trim());
				jo.put("billNo", dr.getBillNo().trim());
				String s1 = string2Json(dr.getRemark().trim());
//				System.out.println(s1);
				jo.put("remark", s1);
				ja.put(jo);
			}
		}
		// 删除上传的文件
		deleteFile(filename);
		return ja.toString();
	}
	
	/**
	 * 从服务器端向前端页面发送JSON对象时，处理JSON值中包含的特殊符号
	 * <p>replace效率不高，需要用StringBuilder重写
	 */
	public String json2string(String input) {       
		if(input.isEmpty()){
            return input;
        }
		input = input.replace("\\\\", "\\u005C");	// 由于/是Java中的转义符，所以要先转
        input = input.replace("\\{", "\\u007B");
        input = input.replace("\\}", "\\u007D");
        input = input.replace("\\'", "\\u0027");   //单引号可能会在JSON标识键值时使用,所以如果值的内容中如果包含了单引号，则需转义成实体编号
        input = input.replace("\\\"", "\\u0022"); //双引号转义的原因与单引号相同，由于双引号本身对于JAVA字符串来说即为特殊字符，所以前面加一个斜线对其进行转义
        return input;
	 } 
	/**
	 * 从服务器端向前端页面发送JSON对象时，处理JSON值中包含的特殊符号
	 * <p>replace效率不高，需要用StringBuilder重写
	 */
	public String string2Json(String input) {       
		if(input.isEmpty()){
            return input;
        }
		input = input.replace("\\", "\\u005C");	// 由于/是Java中的转义符，所以要先转
//		System.out.println(input);
        input = input.replace("{", "\\u007B");
        input = input.replace("}", "\\u007D");
        input = input.replace("'", "\\u0027");   //单引号可能会在JSON标识键值时使用,所以如果值的内容中如果包含了单引号，则需转义成实体编号
        input = input.replace("\"", "\\u0022"); //双引号转义的原因与单引号相同，由于双引号本身对于JAVA字符串来说即为特殊字符，所以前面加一个斜线对其进行转义
//        System.out.println(input);
//        input = input.replace(" ", "&nbsp;");
//        input = input.replace("\n", "<br/>");  //不能把\n的过滤放在前面，因为还要对<和>过滤，这样就会导致<br/>失效了
        return input;
	 } 
	    
    /** 
     * 删除单个文件 
     * @param  sPath 被删除文件的文件名 
     * @return 单个文件删除成功返回true，否则返回false 
     */  
    public boolean deleteFile(String sPath) {  
	    boolean flag = false;  
	    File file = new File(sPath);  
	     // 路径为文件且不为空则进行删除  
	    if (file.isFile() && file.exists()) {  
	        file.delete();  
	        flag = true;  
	    }  
	    return flag;  
    }  

		
	/**
	 * 导入调拨单,提交事件
	 * @param req
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/toimportadd", method = RequestMethod.POST)
	@Transactional(rollbackFor = Exception.class)
	public void transferOrderImportSubmit(HttpServletRequest req, PrintWriter printWriter) throws IOException{
		
		JSONObject rejo = new JSONObject();
		List<Product> pdList = new ArrayList<Product>();
		// todsJson待导入的明细数据
		String todsJson = req.getParameter("todJson");
		String s1 = json2string(todsJson);
		if(null == todsJson){
			rejo.put("err", "服务器没有取到生产单明细数据");
			printWriter.write(rejo.toString());
			printWriter.flush();
			printWriter.close();
			return;
		}
		
		String username = req.getSession().getAttribute(LOGIN_USERNAME).toString();
		User user = userService.getByName(username);
		int userId = user.getUserid();

		int fromWsId = Integer.parseInt(req.getParameter("fromWs"));
		int fromEpId = Integer.parseInt(req.getParameter("fromEp"));
		int toWsId = Integer.parseInt(req.getParameter("toWs"));
		int toEpId = Integer.parseInt(req.getParameter("toEp"));
		
		JSONArray ja = new JSONArray(s1);
		
		// 添加商品表及调拨明细表记录
		List<TransferOrderDetail> todList = new ArrayList<TransferOrderDetail>();
		
		for(int i=0; i < ja.length(); ++i){
			// jo 为一行商品记录
			JSONObject jo = ja.getJSONObject(i);
			
			String barCode = jo.getString("barCode");
			
			if(null == productService.getByBarCode(barCode)){
				// 商品状态:1为正常生产状态
				int status = 1;
				Product pd = newProductByJson(jo, status);
				pdList.add(pd);
			}
			String exStr = invalidWftStatus(barCode);
			if(!exStr.isEmpty()){
				throw new ProductStatusException(exStr + ", 商品条码: " + barCode);
			}
			TransferOrderDetail tod = addTransferOrderDetail( fromWsId, fromEpId, toWsId, toEpId, barCode, userId);
			
			todList.add(tod);
		}
		
		if(0 < pdList.size()){
			productService.addBatch(pdList);
		}
		
		if(0 < todList.size()){
			transferOrderDetailService.addBatch(todList);
		}
		rejo.put("err", "");
		printWriter.write(rejo.toString());
		printWriter.flush();
		printWriter.close();
		//return InternalResourceViewResolver.REDIRECT_URL_PREFIX + TRANSFERORDER_MANAGE;
	}

	/**
	 * 验证商品状态是否处于可调拨状态, 若不能进行调拨则返回异常信息
	 * <br/>Wft=Wait for Transfer
	 * @param barCode
	 * @return : 合法(能进行调拨)返回空字符串, 否则返回异常信息(返回主程序后用于抛出ProductStatusException异常)
	 */
	private String invalidWftStatus(String barCode) {
		EnumProductState enumPs = getProductStatus(barCode);
		if(null == enumPs){
			// 此商品处于未知状态
			return ProductStatusException.EXCEP_PRODUCT_UNKOWN;
		}
		if(EnumProductState.PRODUCT_INFO == enumPs){
			// 商品目录中已添加, 但未产生业务数据
			return ProductStatusException.EXCEP_PRODUCT_INFO;
		}
		if(EnumProductState.WAIT_FOR_ACCEPT == enumPs){
			// 此商品处于调拨且未接收状态, 不能进行调拨
			return ProductStatusException.EXCEP_PRODUCT_NOT_ACCEPT;
		}
		if(EnumProductState.CANCEL == enumPs){
			// 此商品处于取消生产状态
			return ProductStatusException.EXCEP_PRODUCT_CANCEL;
		}
		if(EnumProductState.DONE == enumPs){
			// 此商品已完工下线, 不能进行调拨
			return ProductStatusException.EXCEP_PRODUCT_COMPLETED;
		}
		
		return "";
	}

	/**
	 * 根据条码取商品当前状态
	 * @param barCode
	 * @return : 成功返回EnumProductState枚举常量, 否则(未知的状态)返回null
	 */
	private EnumProductState getProductStatus(String barCode) {
		Product pd = productService.getByBarCode(barCode);
		if(null == pd){
			// 商品目录中不存在, 商品处于"系统中不存在"状态
			return EnumProductState.NONE;
		}
		if(null == pd.getStatus()){
			// 商品目录中已添加, 但未产生业务数据
			return EnumProductState.PRODUCT_INFO;
		}
		if(null != pd.getStatus()){
			if(0 == pd.getStatus()){
				// 取消生产
				return EnumProductState.CANCEL;
			}
			
			if(5 == pd.getStatus()){
				// 已经完成生产
				return EnumProductState.DONE;
			}
			
			if(1 == pd.getStatus()){
				// 处于生产过程中(待接收或已接收)
				TransferOrderDetail tod = transferOrderDetailService.getLastByBarCode(barCode);
				if(null != tod){
					if(null == tod.getToTime()){
						// 没有"接收时间",商品处于"待接收状态"
						return EnumProductState.WAIT_FOR_ACCEPT;
					}else{
						// 有"接收时间",商品处于"已接收状态"
						return EnumProductState.ACCEPTED;
					}
				}else{
					CompleteDetail cpd = completeDetailService.getLastByBarCode(barCode);
					if(null != cpd){
						// 有完工明细, 商品处于"已经完成生产"状态
						return EnumProductState.DONE;
					}else{
						// 没有完工明细, 商品处于"系统中不存在"状态
						return EnumProductState.NONE;
					}
				}
			}
		}
		
		return null;
	}
	/**
	 * 根据JSON格式的商品信息生成一个商品对象
	 * @param jo : 商品基本信息
	 * @param status : 商品状态:0-取消,1-生产中,5-完工
	 * @return
	 */
	private Product newProductByJson(JSONObject jo, int status) {
		Product pd = new Product();
		pd.setBarCode(jo.getString("barCode"));
		pd.setStyleCode(jo.getString("styleCode"));
		pd.setPdName(jo.getString("pdName"));
		pd.setLength(jo.getInt("length"));
		pd.setSize(jo.getString("size"));
		pd.setCusCode(jo.getString("cusCode"));
		pd.setBillNo(jo.getString("billNo"));
		pd.setRemark(jo.getString("remark"));
		pd.setStatus(status);
		return pd;
	}

	/**
	 * 根据参数产生一个TransferOrderDetail对象
	 * @param fromWsId
	 * @param fromEpId
	 * @param toWsId
	 * @param toEpId
	 * @param barCode
	 * @param userId
	 * @return
	 */
	private TransferOrderDetail addTransferOrderDetail(int fromWsId, int fromEpId, int toWsId,
			int toEpId, String barCode, int userId) {
		TransferOrderDetail tod = new TransferOrderDetail();
		
		Workshop fromWs = new Workshop();
		fromWs.setWsId(fromWsId);
		tod.setFromWs(fromWs);
		
		Employee fromEp = new Employee();
		if(0 == fromEpId){
			fromEp.setEpId(null);
		}else{
			fromEp.setEpId(fromEpId);
		}
		tod.setFromEp(fromEp);
		
		Workshop toWs = new Workshop();
		toWs.setWsId(toWsId);
		tod.setToWs(toWs);
		
		Employee toEp = new Employee();
		if(0 == toEpId){
			toEp.setEpId(null);
		}else{
			toEp.setEpId(toEpId);
		}
		tod.setToEp(toEp);
		
		tod.setBarCode(barCode);
		tod.setFromTime(new Timestamp(System.currentTimeMillis()));
		tod.setToTime(null);
		tod.setAddTime(new Timestamp(System.currentTimeMillis()));
		tod.setEditTime(null);
		tod.setFromUserId(userId);
		return tod;
	}

	
	/**
	 * 读取.xlsx文件的第一个工作表, 右边和下边的空单元格将被去除.
	 * @param filePath: .xlsx文件的全路径名.
	 * @return
	 * @throws IOException
	 */
	/*
	private List<List<String>> getExcelRows(String filePath) throws IOException{
		// 构造 XSSFWorkbook 对象，strPath 传入文件路径  
		XSSFWorkbook xwb = new XSSFWorkbook(filePath);  
		// 读取第一张表格内容  
		XSSFSheet sheet = xwb.getSheetAt(0);  
		// 定义 row、cell  
		XSSFRow row; 
		List<List<String>> rowsList = new ArrayList<List<String>>(); 
		
		String cell = null;  
		// 循环输出表格中的内容  
		for (int i = sheet.getFirstRowNum(); i < sheet.getPhysicalNumberOfRows(); i++) {  
		    row = sheet.getRow(i);  
		    List<String> rowList = new ArrayList<String>(); 
		    if(null == row.getCell(0) || row.getCell(0).toString().isEmpty()){
		    	cell = null;
		    	break;
		    }
		    for (int j = row.getFirstCellNum(); j < row.getPhysicalNumberOfCells(); j++) {  
		    	// 在当前行遍历每个单元格
		        // 通过 row.getCell(j).toString() 获取单元格内容，  
		    	if(null == row.getCell(j)){
		    		break;
		    	}
		    	row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
		        cell = row.getCell(j).getStringCellValue();
				rowList.add(cell);
		    } 
		    rowsList.add(rowList);
		    //System.out.println("");  
		} 
		xwb.close();
		return rowsList;
	}
	*/
	
	/*
	@RequestMapping(value = "delete", method = RequestMethod.GET)
	public String transferOrderMainDelete(TransferOrderMain transferOrderMain){
		transferOrderMainService.delete(transferOrderMain);
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX + TRANSFERORDER_MANAGE;
	}
	*/
	
	/**
	 * 验证商品状态是否处于可接收状态, 若不能进行接收则返回异常信息
	 * <br/>Wfa=Wait for accept
	 * @param barCode
	 * @return : 可接收状态返回空字符串, 否则返回异常信息(返回主程序后用于抛出ProductStatusException异常)
	 */
	private String invalidWfaStatus(String barCode) {
		EnumProductState enumPs = getProductStatus(barCode);
		if(null == enumPs){
			// 此商品处于未知状态
			return ProductStatusException.EXCEP_PRODUCT_UNKOWN + ", 商品条码: " + barCode;
		}
		if(EnumProductState.NONE == enumPs){
			// 系统中不存在此商品
			return ProductStatusException.EXCEP_PRODUCT_NONE;
		}
		if(EnumProductState.PRODUCT_INFO == enumPs){
			// 商品目录中已添加, 但未产生业务数据
			return ProductStatusException.EXCEP_PRODUCT_INFO;
		}
		if(EnumProductState.WAIT_FOR_ACCEPT == enumPs){
			// 此商品处于调拨且未接收状态
			return "";
		}
		if(EnumProductState.ACCEPTED == enumPs){
			// 此商品已接收状态, 不能进行接收
			return ProductStatusException.EXCEP_PRODUCT_ACCEPTED;
		}
		if(EnumProductState.CANCEL == enumPs){
			// 此商品处于取消生产状态
			return ProductStatusException.EXCEP_PRODUCT_CANCEL;
		}
		if(EnumProductState.DONE == enumPs){
			// 此商品已完工下线, 不能进行调拨
			return ProductStatusException.EXCEP_PRODUCT_COMPLETED;
		}

		return ProductStatusException.EXCEP_PRODUCT_UNKOWN + ", 商品条码: " + barCode;
		
	}
	
	/**
	 * 根据条码,取待接收商品的明细,生成JSON对象,传回页面
	 * @return : 成功返回JSON对象数组(只有一个元素),失败返回JSON对象数组(只有一个err键值对元素)
	 * @throws IOException
	 */
	@RequestMapping(value = "getwfatodbybarcode", method = RequestMethod.GET)
	public void getWfaTodByBarcode(@RequestParam(value="barCode")String barCode, PrintWriter printWriter) {
		String exStr = invalidWfaStatus(barCode);
		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();
		if(exStr.isEmpty()){ 
			TransferOrderDetail tod = transferOrderDetailService.getLastByBarCode(barCode);
			jo = getJsonTod(tod);
		}else{ // 不可接收状态
			jo.put("err", exStr);
		}
		ja.put(jo);
		printWriter.write(ja.toString());
		printWriter.flush();
		printWriter.close();
	}
	

	/**
	 * 根据条码,取待接收商品的明细,生成JSON对象,传回页面
	 * @return : 成功返回JSON对象数组(只有一个元素),失败返回JSON对象数组(只有一个err键值对元素)
	 * @throws IOException
	 */
	@RequestMapping(value = "getwfatodbytoepid", method = RequestMethod.GET)
	public void getWfaTodByToEpId(@RequestParam(value="toEpId")int toEpId, PrintWriter printWriter) {
		String exStr;
		JSONArray ja = new JSONArray();
		List<TransferOrderDetail> tods = transferOrderDetailService.getWfaListByToEpId(toEpId);
		for(TransferOrderDetail tod : tods){
			exStr = invalidWfaStatus(tod.getBarCode());
			if(exStr.isEmpty()){ 
				JSONObject jo = getJsonTod(tod);
				ja.put(jo);
			}
		}
		if(0 == ja.length()){ // 不可接收状态
			JSONObject jo = new JSONObject();
			jo.put("err", "没有可接收的商品");
			ja.put(jo);
		}
			
		printWriter.write(ja.toString());
		printWriter.flush();
		printWriter.close();
	}
	
	/**
	 * 根据条码,取待接收商品的明细,生成JSON对象,传回页面
	 * @return : 成功返回JSON对象数组(只有一个元素),失败返回JSON对象数组(只有一个err键值对元素)
	 * @throws IOException
	 */
	@RequestMapping(value = "getwfatodbycuscode", method = RequestMethod.GET)
	public void getWfaTodByCusCode(@RequestParam(value="cusCode")String cusCode, PrintWriter printWriter) {
		
		String exStr;
		JSONArray ja = new JSONArray();
		List<TransferOrderDetail> tods = transferOrderDetailService.getWfaListByCusCode(cusCode);
		for(TransferOrderDetail tod : tods){
			exStr = invalidWfaStatus(tod.getBarCode());
			if(exStr.isEmpty()){ 
				JSONObject jo = getJsonTod(tod);
				ja.put(jo);
			}
		}
		if(0 == ja.length()){ // 不可接收状态
			JSONObject jo = new JSONObject();
			jo.put("err", "没有可接收的商品");
			ja.put(jo);
		}
			
		printWriter.write(ja.toString());
		printWriter.flush();
		printWriter.close();
	}
	
	/**
	 * 根据品名,取待接收商品的明细,生成JSON对象,传回页面
	 * @return : 成功返回JSON对象数组(只有一个元素),失败返回JSON对象数组(只有一个err键值对元素)
	 * @throws IOException
	 */
	@RequestMapping(value = "getwfatodbypdname", method = RequestMethod.GET)
	public void getWfaTodByPdName(@RequestParam(value="pdName")String pdName, PrintWriter printWriter) {
		
		String exStr;
		JSONArray ja = new JSONArray();
		List<TransferOrderDetail> tods = transferOrderDetailService.getWfaListByPdName(pdName);
		for(TransferOrderDetail tod : tods){
			exStr = invalidWfaStatus(tod.getBarCode());
			if(exStr.isEmpty()){ 
				JSONObject jo = getJsonTod(tod);
				ja.put(jo);
			}
		}
		if(0 == ja.length()){ // 不可接收状态
			JSONObject jo = new JSONObject();
			jo.put("err", "没有可接收的商品");
			ja.put(jo);
		}
		
		printWriter.write(ja.toString());
		printWriter.flush();
		printWriter.close();
	}
	
	/**
	 * 将调拨单号发送给页面todview.jsp
	 * @return
	 */
	@RequestMapping(value = "gotodviewbyid", method = RequestMethod.GET)
	public String goTodViewById(Model model, HttpServletRequest req) {
		if(null != req.getParameter("tmId")){
			int tmId = Integer.parseInt(req.getParameter("tmId"));
			model.addAttribute("tmId", tmId);
		}
		return "/transferorder/todview";
	}
	
	
	/**
	 * 将[调拨单明细]对象内容填充入一个JSON对象
	 * @param tod
	 * @return : 返回一个带有调拨明细信息的JSONObject对象, 
	 * 其中包含条码连接的商品信息(款号,品名...等)
	 */
	private JSONObject getJsonTod(TransferOrderDetail tod) {
		JSONObject jo = new JSONObject();
		jo.put("tdId", tod.getTdId());
		Workshop fromWs = workshopService.getById(tod.getFromWs().getWsId());
		jo.put("fromWs.wsName", fromWs.getWsName());
		Employee fromEp = employeeService.getById(tod.getFromEp().getEpId());
		if(null == fromEp){
			jo.put("fromEp.epName", "");
		}else{
			jo.put("fromEp.epName", fromEp.getEpName());
		}
		Workshop toWs = workshopService.getById(tod.getToWs().getWsId());
		jo.put("toWs.wsName", toWs.getWsName());
		Employee toEp = employeeService.getById(tod.getToEp().getEpId());
		if(null == toEp){
			jo.put("toEp.epName", "");
		}else{
			jo.put("toEp.epName", toEp.getEpName());
		}
		jo.put("barCode", tod.getBarCode());
		
		Product pd = productService.getByBarCode(tod.getBarCode());
		jo.put("styleCode", pd.getStyleCode());
		jo.put("pdName", pd.getPdName());
		jo.put("length", pd.getLength());
		jo.put("size", pd.getSize());
		jo.put("cusCode", pd.getCusCode());
		jo.put("billNo", pd.getBillNo());
		jo.put("fromTime", tod.getFromTime());
		jo.put("toTime", tod.getToTime());
		jo.put("addTime", tod.getAddTime());
		String fromUserName = userService.getById(tod.getFromUserId()).getUsername();
		jo.put("fromUserName", fromUserName);
		User toUser = userService.getById(tod.getToUserId());
		if(null == toUser){
			jo.put("toUserName", "");
		}else{
			jo.put("toUserName", toUser.getUsername());
		}
		return jo;
	}
	
	
	/**
	 * 根据调入部门Id号,取待接收明细,生成JSON对象数组,传回
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "getwfatodbytowsid", method = RequestMethod.GET)
	public void getWfaTodByWsId(@RequestParam("toWsId") int toWsId, PrintWriter printWriter) {
		List<TransferOrderDetail> todList = transferOrderDetailService.getUnAcceptedListByToWsId(toWsId);
		JSONArray ja = new JSONArray();
		for(TransferOrderDetail tod: todList){
			JSONObject jo = getJsonTod(tod);
			ja.put(jo);
		}

		printWriter.write(ja.toString());
		printWriter.flush();
		printWriter.close();
	}
	
	
	/**
	 * 根据JSON格式的{tdId}接收多个商品
	 * @param printWriter
	 * @param req
	 */
	@RequestMapping(value = "accepttodbyids", method = RequestMethod.POST)
	public void acceptTodByIds(PrintWriter printWriter, HttpServletRequest req){
		String err = "";
		if(null != req.getParameter("tods")){
			String tods = req.getParameter("tods");
			JSONArray ja = new JSONArray(tods);
			String username = req.getSession().getAttribute(LOGIN_USERNAME).toString();
			User user = userService.getByName(username);
			int userid = user.getUserid();
			int todListLen = ja.length();
			// tdId数组
			int[] arrTdIds = new int[todListLen];
			for(int i=0; i < todListLen; ++i){
				JSONObject jo = ja.getJSONObject(i);
				arrTdIds[i] = jo.getInt("tdId");
			}
			acceptTodByArrTdIds(userid, arrTdIds);
			
		}else{
			err = "服务器没有接收到数据";
		}
		printWriter.write(err);
		printWriter.flush();
		printWriter.close();
	}

	/**
	 * 按tdId数组, 接收调拨单
	 * @param userid 操作员Id
	 * @param arrTdIds tdId数组
	 */
	private void acceptTodByArrTdIds(int userid, int[] arrTdIds) {
		List<TransferOrderDetail> todList = new ArrayList<TransferOrderDetail>();
		for(int i=0; i < arrTdIds.length; ++i){
			TransferOrderDetail tod = new TransferOrderDetail();
			tod.setTdId(arrTdIds[i]);
			tod.setToTime(new Timestamp(System.currentTimeMillis()));
			tod.setToUserId(userid);
			todList.add(tod);
		}
		transferOrderDetailService.acceptBatch(todList);
	}
	
	/**
	 * 根据JSON格式的调拨单明细信息， 添加调拨单（明细表）
	 * JSON格式的调拨单明细信息:{tdId,fromWs,fromEp,toWs,toEp,barCode}
	 * @param printWriter
	 * @param req
	 */
	@RequestMapping(value = "addtobytodjson", method = RequestMethod.POST)
	@Transactional(rollbackFor = Exception.class)
	public void addToByTodJSON(PrintWriter printWriter, HttpServletRequest req){
		String err = "";
		if(null != req.getParameter("tods")){
			String tods = req.getParameter("tods");
			String username = req.getSession().getAttribute(LOGIN_USERNAME).toString();
			User user = userService.getByName(username);
			// 添加调拨单明细,并返回明细Id数组
			int[] arrTdId = addTransferOrderDetail(tods, user.getUserid());
			// FIXME 自动接收标志, 暂时硬编码, 应该由用户设置后程序从数据库中读取
			boolean autoAccept = true;
			if(autoAccept){
				acceptTodByArrTdIds(user.getUserid(),arrTdId);
			}
		}else{
			err = "服务器没有接收到数据";
		}
//		if(0==tmId){
//			// FIXME 应该转到出错页面
//			url = "";
//		}else{
//			url = "/transferorder/gotodviewbyid?tmId=" + tmId;
//		}
		
		printWriter.write(err);
		printWriter.flush();
		printWriter.close();
	}
	
	/**
	 * 添加调拨明细表记录
	 * @param tmId: 调拨单号
	 * @param tods: 调拨明细数据JSON格式
	 *  @return 返回主键数组
	 */
	private int[] addTransferOrderDetail(String todsJson, int fromUserId){
		// 添加调拨明细表记录
		JSONArray ja = new JSONArray(todsJson);
		List<TransferOrderDetail> todList = new ArrayList<TransferOrderDetail>();
		for(int i=0; i < ja.length(); ++i){
			JSONObject jo = ja.getJSONObject(i);
			TransferOrderDetail tod = new TransferOrderDetail();
			
			Workshop fromWs = new Workshop();
			fromWs.setWsId(jo.getInt("fromWs"));
			tod.setFromWs(fromWs);
			
			Employee fromEp = new Employee();
			if(0 == jo.getInt("fromEp")){
				fromEp.setEpId(null);
			}else{
				fromEp.setEpId(jo.getInt("fromEp"));
			}
			tod.setFromEp(fromEp);
			
			Workshop toWs = new Workshop();
			toWs.setWsId(jo.getInt("toWs"));
			tod.setToWs(toWs);
			
			Employee toEp = new Employee();
			if(0 == jo.getInt("toEp")){
				toEp.setEpId(null);
			}else{
				toEp.setEpId(jo.getInt("toEp"));
			}
			tod.setToEp(toEp);
			
			tod.setBarCode(jo.getString("barCode"));
			tod.setFromTime(new Timestamp(System.currentTimeMillis()));
			tod.setToTime(null);
			tod.setAddTime(new Timestamp(System.currentTimeMillis()));
			tod.setFromUserId(fromUserId);
			
			todList.add(tod);
		}
		int[] reKeys;
		if(0 < todList.size()){
			reKeys = transferOrderDetailService.addBatch(todList);
			return reKeys;
		}
		return null;
	}

	/**
	 * 打开接收商品页面
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "accept", method = RequestMethod.GET)
	public String acceptProductView(Model model) {
		List<Workshop> wsList = workshopService.getAll();
		JSONArray ja = new JSONArray();
		for(Workshop ws : wsList){
			JSONObject js = new JSONObject();
			js.put("wsId",Integer.toString(ws.getWsId()));
			js.put("wsName", ws.getWsName());
			ja.put(js);
		}
		model.addAttribute("wsList",ja.toString());

		return "/transferorder/accept";
	}
	
	
	// 局部异常处理, 发生异常时返回登录界面
		@ExceptionHandler(UploadException.class)
		public String handlerUploadException(Exception excep,HttpServletRequest req){
			req.setAttribute("excep", excep);
			//req.setAttribute(LOGIN_USER,new User());
			return TRANSFERORDER_ADDIMPORT;
		}
		
		@ExceptionHandler(ProductStatusException.class)
		public String handlerTransferOrderException(Exception excep,HttpServletRequest req){
			req.setAttribute("excep", excep);
			//req.setAttribute(LOGIN_USER,new User());
			return TRANSFERORDER_ADDIMPORT;
		}
		
}
