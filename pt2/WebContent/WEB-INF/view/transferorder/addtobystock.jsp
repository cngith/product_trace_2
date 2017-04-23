<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>车间生产调拨</title>
<%
	pageContext.setAttribute("baseURL", request.getContextPath());
%>
<script type="text/javascript">  
	var baseURL = "${baseURL}";  
</script> 
<script type="text/javascript" src="${baseURL}/resource/js/jquery.min.js"></script>
<script type="text/javascript" src="${baseURL}/resource/js/addtobystock.js"></script>

</head>
<body onload="bodyLoad()">
<%@ include file="../titlemenu.jsp" %>
<hr>
	<div class="inputTag">
		<div><span>条码:</span>
			<input type="text" id="barCode" onkeypress="if(event.keyCode==13) barCodePressEnter()"/>
			<input type="button" value="查询" onclick="barCodeChange()">
			<span id="infoBarCode"></span>
		</div>
		<div id="from">
			<span>调出部门:</span>	
			<select name="fromWs" id="fromWs" onchange="fromWsChanged()" disabled="disabled" >
				
			</select>
			<span>发送人:</span>	
			<select name="fromEp" id="fromEp" disabled="disabled">
				
			</select>	
		</div>
		<div id="to">
			<span>调入部门:</span>	
			<select id="toWs" name="toWs" onchange="toWsChanged()">
				
			</select>
			
			<span>接收人:</span>	
			<select id="toEp" name="toEp">
				
			</select>	
		</div>	
		<div><input	type="button" id="btnAddOneRowTod" value="添加" onclick="addOneRowTodBtnClicked()" /></div>
	</div>

	<div id="resultDiv">
		<table id="tdetail" border="1" cellpadding="5">
			<thead id="ttitle">
				<tr>
					<td>调出部门</td>
					<td>发送人</td>
					<td>调入部门</td>
					<td>接收人</td>
					<td>商品条码</td>
					<td class="inputTag" >删除</td>
				</tr>
			</thead>
			<tbody>
			
			</tbody>
		</table>
<!-- 	<div><span id="spanMsg" ></span></div> -->
<!-- 	<div id="noResult"><span>没有找到符合条件的记录.</span></div> -->
	<div><input type="button" id="submitBtn" class="inputTag" value="提交" onclick="submitClick()"/></div> 
	</div>
<%-- 	<embed id="soundAlert" width="1" height="1" src="${baseURL}/resource/sound/ding.wav" /> --%>
	
	<%-- <div>${excep.message }</div> --%>
</body>
</html>