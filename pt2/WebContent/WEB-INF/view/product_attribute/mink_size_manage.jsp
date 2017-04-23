<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>皮号类型管理</title>
	<%
		pageContext.setAttribute("baseURL", request.getContextPath());
	%>
	<script type="text/javascript">  
		var baseURL = "${baseURL}";  
	</script> 
	<!-- 
	
	 -->
	<script type="text/javascript" src="${baseURL}/resource/js/jquery.min.js"></script>
	<script type="text/javascript" src="${baseURL}/resource/js/product_attribute/mink_type_manage.js"></script>
</head>
<body onload="bodyLoad()">
	<%@ include file="../titlemenu.jsp" %>
	<hr>
	<a href="${baseURL }/product_attribute/add_mink_type" >添加皮号类型</a>
	<a href="${baseURL }/product_attribute/add_mink_size" >添加皮号码数</a>
	<div id="DivPdInfo">
		<table id="tDetail" border="1" cellpadding="5">

		</table>
	</div>
	
</body>
</html>