<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>皮号管理</title>
	</head>
	<body>
		<%@ include file="../titlemenu.jsp" %>
		<hr>
		<%
			String contextPath = request.getContextPath();
			pageContext.setAttribute("baseURL", contextPath);
		%>
		<a href="${baseURL }/product_attribute/mink_type_manage" >皮号类型管理</a>
		<a href="${baseURL }/product_attribute/mink_size_manage" >皮号码数管理</a>
	
	</body>
</html>