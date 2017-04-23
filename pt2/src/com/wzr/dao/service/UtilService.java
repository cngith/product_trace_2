package com.wzr.dao.service;

import org.json.JSONObject;

import com.wzr.dao.entity.CompleteDetail;
import com.wzr.dao.entity.Employee;
import com.wzr.dao.entity.EnumProductState;
import com.wzr.dao.entity.Product;
import com.wzr.dao.entity.TransferOrderDetail;
import com.wzr.dao.entity.Workshop;

/**
 * 工具类
 * @author root
 *
 */
public class UtilService {
	
	
	private CompleteDetailService completeDetailService;

	private TransferOrderDetailService transferOrderDetailService;

	private WorkshopService workshopService;
	
	private EmployeeService employeeService;

	private ProductService productService;
	
	
	public void setCompleteDetailService(CompleteDetailService completeDetailService) {
		this.completeDetailService = completeDetailService;
	}


	public void setTransferOrderDetailService(TransferOrderDetailService transferOrderDetailService) {
		this.transferOrderDetailService = transferOrderDetailService;
	}


	public void setWorkshopService(WorkshopService workshopService) {
		this.workshopService = workshopService;
	}


	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}


	


	public void setProductService(ProductService productService) {
		this.productService = productService;
	}


	/**
	 * 判断商品当前状态，返回状态,位置信息,商品信息(Json格式)
	 * 
	 * @return : 	"pdState"元素代表商品状态,
	 * 				"wsId","epId"代表位置信息(包括部门.员工的Id及名称),
	 * 				"styleCode","pdName"等代表商品信息.
	 */
	public JSONObject whereIsTheProduct(String barCode){
		JSONObject jo = new JSONObject();

		EnumProductState enumPs = getProductStatus(barCode);
		if(null == enumPs){
			// 此商品处于未知状态
			jo.put("pdState", "UNKNOWN");
			jo.put("statusName", "未知");
		}else{
			jo.put("pdState", enumPs.getName());
			if(EnumProductState.PRODUCT_INFO == enumPs){
				putPdJsonByBarCode(barCode, jo);
				jo.put("statusName", "无业务");
			}
			
			if(EnumProductState.ACCEPTED == enumPs){
				// 已接收，在接收人手中
				TransferOrderDetail tod = transferOrderDetailService.getLastByBarCode(barCode);
				jo.put("wsId", tod.getToWs().getWsId());
				jo.put("epId", tod.getToEp().getEpId());
				jo.put("wsName", getWorkshopName(tod.getToWs().getWsId()));
				jo.put("epName", getEmployeeName(tod.getToEp().getEpId()));
				putPdJsonByBarCode(barCode, jo);
				jo.put("statusName", "已接收");
			}
			
			if(EnumProductState.WAIT_FOR_ACCEPT == enumPs){
				// 待接收，在发送人手中
				TransferOrderDetail tod = transferOrderDetailService.getLastByBarCode(barCode);
				jo.put("wsId", tod.getFromWs().getWsId());
				jo.put("epId", tod.getFromEp().getEpId());
				jo.put("wsName", getWorkshopName(tod.getFromWs().getWsId()));
				jo.put("epName", getEmployeeName(tod.getFromEp().getEpId()));
				putPdJsonByBarCode(barCode, jo);
				jo.put("statusName", "已发送");
			}
			
			if(EnumProductState.CANCEL == enumPs){
				putPdJsonByBarCode(barCode, jo);
			}
			if(EnumProductState.DONE == enumPs){
				putPdJsonByBarCode(barCode, jo);
			}
		}
		
		return jo;
	}


	/**
	 * 根据部门Id查询部门名称
	 * @param wsId : 部门Id
	 * @return
	 */
	public String getWorkshopName(Integer wsId){
		if(null == wsId || 0 == wsId){
			return "";
		}
		Workshop ws = workshopService.getById(wsId);
		if(null == ws){
			return "";
		}else{
			return ws.getWsName();
		}
	}
	
	
	/**
	 * 根据员工Id查询员工名称
	 * @param epId : 员工Id
	 * @return
	 */
	public String getEmployeeName(Integer epId){
		if(null == epId || 0 == epId){
			return "";
		}
		Employee ep = employeeService.getById(epId);
		if(null == ep){
			return "";
		}else{
			return ep.getEpName();
		}
	}
	
	
	/**
	 * 根据条码查出商品信息, 写入到JSONObject中
	 * @param barCode 
	 * @param jo : 指定的JSONObject
	 */
	public void putPdJsonByBarCode(String barCode, JSONObject jo) {
		jo.put("barCode", barCode);
		Product pd = productService.getByBarCode(barCode);
		jo.put("styleCode", pd.getStyleCode());
		jo.put("pdName", pd.getPdName());
		jo.put("size", pd.getSize());
		jo.put("length", String.valueOf(pd.getLength()));
		jo.put("cusCode", pd.getCusCode());
		jo.put("billNo", pd.getBillNo());
		jo.put("statusName", productService.getProductStatusName(pd.getStatus()));
	}
	

	/**
	 * 根据条码取商品当前状态
	 * @param barCode
	 * @return : 成功返回EnumProductState枚举常量, 否则(未知的状态)返回null
	 */
	public EnumProductState getProductStatus(String barCode) {
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
	
}
