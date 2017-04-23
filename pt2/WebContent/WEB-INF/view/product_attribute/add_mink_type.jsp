<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>添加原料</title>
	<%
		// 取应用URL
		pageContext.setAttribute("baseURL", request.getContextPath());
	%>
	<script type="text/javascript">  
		var baseURL = "${baseURL}";  
	</script> 
	<script type="text/javascript" src="${baseURL}/resource/js/jquery.min.js"></script>
	<script type="text/javascript" src="${baseURL}/resource/js/product_attribute/add_mink_type.js"></script>
</head>
<body>
	<%@ include file="../titlemenu.jsp" %>
	<hr>
		<div id="DivEditor">
		<span>原料名称:</span>	
		<input type="text" name="mtName" id="mtName"/>
		<button onclick="validateClick()">验证</button> <span id="spanValidateResult"></span>
		</div>
		<div>
		<button onclick="addMaterial()">添加</button>
		<button onclick="cancelClick()">取消</button>
	</div>

	
</body>
</html>