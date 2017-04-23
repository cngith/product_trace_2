<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>按客户查询商品</title>
<%
	pageContext.setAttribute("baseURL", request.getContextPath());
%>
<script type="text/javascript">  
	var baseURL = "${baseURL}";  
</script> 
<script type="text/javascript" src="${baseURL}/resource/js/jquery.min.js"></script>
<script type="text/javascript" src="${baseURL}/resource/js/count/seekproductbycustomer.js"></script>

</head>
<body onload="bodyLoad()">
<%@ include file="../titlemenu.jsp" %>
<hr>
	<div>
		<div class="inputTag">
			<div id="from">
				<span>客户代码:</span>	
				<input type="text" id="cusCode" />
			</div>
			<div>
				<button onclick="seek()">查询</button>
				<button onclick="saveDataAs()">导出…</button>
			</div>
		</div>
		<div id="divSum">
			<span id="spanFilenameSaveAs"></span>
			<span>共订货:</span>	
			<span id="spanPdNumber"></span>
			<span>件</span>	
		</div>
	</div>
	
	<div id="DivPdInfo">
		<table id="tDetail" border="1" cellpadding="5">

		</table>
	</div>
 	<input type="hidden" id="exportField"/> <!-- 按顺序存放字段代码 -->
 	<input type="hidden" id="exportContent"/> <!-- 存放查询结果用于导出 -->
		
</body>
</html>