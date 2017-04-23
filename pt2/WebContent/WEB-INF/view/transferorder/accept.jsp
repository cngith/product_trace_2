<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>接收商品</title>

<%
	pageContext.setAttribute("baseURL", request.getContextPath());
%>
<script type="text/javascript">  
	var baseURL = "${baseURL}";  
</script> 
<script type="text/javascript" src="${baseURL}/resource/js/jquery.min.js" ></script>
<script type="text/javascript" src="${baseURL}/resource/js/accept.js" ></script>

</head>
<body onload="bodyLoad()">
<%@ include file="../titlemenu.jsp" %>
<hr>
	<div>
		<div id="to" class="inputTag" >
			<span>调入部门:</span>	
 			<select id="toWs" name="toWs" onchange="toWsChanged()">
			</select>
			
			<span>接收人:</span><select id="toEp" name="toEp"></select>
			
			<input type="button" value="查询" onclick="btnQClick()" />
			
			<div>
				<span>条码:</span>
				<input type="text" id="barCode" onkeypress="if (event.keyCode == 13) barCodeKeyPress()" />
				<span>类别:</span>
				<input type="text" id="cusCode" name="cusCode" onkeypress="if (event.keyCode == 13) cusCodeKeyPress()" />
				<span>颜色:</span>
				<input type="text" id="pdName" name="pdName" onkeypress="if (event.keyCode == 13) pdNameKeyPress()" />
			</div>
		</div>	
	</div>
	<div id="resultDiv">
		<table id="tdetail" border="1" cellpadding="5">

		</table>
	
		<div><span id="message"></span></div>
		<div id="btnDiv">
		 	<div id="cAll" class="inputTag" ><input type="checkbox" id="chooseAll" checked="checked" onchange="chooseAllClick()" />全选 </div>
			<div><input type="button" id="submitBtn" class="inputTag" value="接收商品" onclick="acceptBtnClick()"/></div> 
		</div> 
	</div>

	<div><button onclick="saveDataAs()">导出…</button></div>
</body>
</html>