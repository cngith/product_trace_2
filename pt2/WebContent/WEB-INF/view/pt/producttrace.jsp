<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商品跟踪</title>
<%
	pageContext.setAttribute("baseURL", request.getContextPath());
%>
<script type="text/javascript">  
	var baseURL = "${baseURL}";  
</script> 
<script type="text/javascript" src="${baseURL}/resource/js/jquery.min.js"></script>
<script type="text/javascript" src="${baseURL}/resource/js/producttrace.js"></script>

</head>
<body onload="bodyLoad()">
<%@ include file="../titlemenu.jsp" %>
<hr>
	<div>
		<div id="product">
			<span>商品条码:</span>	
			<input type="text" id="barCode" name="barCode" onkeypress="if (event.keyCode == 13) barCodeKeyPress();" />
			<button onclick="getPtByBarCode()">查询</button>
			<span id="infoBarCode"></span>
		</div>	
		<div id="divStatus">
			<span>商品进度:</span>	
			<span id="spanPdStatus"></span><br/>
			<span>相关部门员工:</span>	
			<span id="spanWsEp"></span>
		</div>
	</div>
	
	<div id="ptInfo"></div>
	<div>
		<table id="tDetail" border="1" cellpadding="5">

		</table>
	</div>
	
</body>
</html>