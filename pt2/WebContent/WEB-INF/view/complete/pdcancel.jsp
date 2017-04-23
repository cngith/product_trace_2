<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商品取消</title>
<%
	pageContext.setAttribute("baseURL", request.getContextPath());
%>
<script type="text/javascript">  
	var baseURL = "${baseURL}";  
</script> 
<script type="text/javascript" src="${baseURL}/resource/js/jquery.min.js"></script>
<script type="text/javascript" src="${baseURL}/resource/js/pdcancel.js"></script>

</head>
<body onload="bodyLoad()">
<%@ include file="../titlemenu.jsp" %>
<hr>
	<div class="inputTag">
		<div><span>条码:</span>
			<input type="text" id="barCode" />
			<button onclick="getPtByBarCode()">查询</button>
			<span id="infoBarCode"></span>
		</div>	
		<div id="divStatus">
			<span>商品进度:</span>	
			<span id="spanPdStatus"></span><br/>
			<span>相关部门员工:</span>	
			<span id="spanWsEp"></span>
		</div>
		<!-- <div id="from">
			<span>所在部门:</span>	
			<select id="fromWs" onchange="fromWsChanged()" disabled="disabled" >
				
			</select>
			<span>收管人:</span>	
			<select id="fromEp" disabled="disabled">
				
			</select>	
		</div> -->
		<input type="hidden" id="fromWsName" />
		<input type="hidden" id="fromEpName" />
		<input type="hidden" id="styleCode" />
		<input type="hidden" id="pdName" />
		<input type="hidden" id="cusCode" />
		<input type="hidden" id="billNo" />
		<div><input	type="button" id="btnAddOneRow" value="添加" onclick="addOneRowBtnClicked()" /></div>
	</div>
	
	<div>
		<table id="tdetail" border="1" cellpadding="5">
			<thead id="ttitle">
				<tr>
					<td>所在部门</td>
					<td>收管人</td>
					<td>商品条码</td>
					<td>款号</td>
					<td>颜色</td>
					<td>客户</td>
					<td>单号</td>
					<td>删除</td>
				</tr>
			</thead>
			<tbody>
			
			</tbody>
		</table>
		<div><span id="spanMsg" ></span></div>
	</div>
	<div class="inputTag"><input type="button" id="submitBtn" value="提交" onclick="submitClick()"/></div> 
	
	<%-- <div>${excep.message }</div> --%>
</body>
</html>