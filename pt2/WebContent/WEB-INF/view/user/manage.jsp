<%@page import="com.wzr.dao.entity.User"%>
<%@page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>用户管理</title>
	<%
		pageContext.setAttribute("baseURL", request.getContextPath());
	%>
	<script type="text/javascript">
		var baseURL = "${baseURL}"; 
	</script> 
	<script type="text/javascript" src="${baseURL}/resource/js/jquery.min.js"> </script> 
	<script type="text/javascript" src="${baseURL}/resource/js/plugin/layer/layer.js"> </script> 
	<script type="text/javascript" src="${baseURL}/resource/js/user/user.manage.js"> </script> 
		
</head>
<body>
	<%@ include file="../titlemenu.jsp" %>
	<hr>
	
	<a href="${baseURL }/employee/add" >添加员工</a>
	<button id="" onclick="addButtonClicked()">添加员工</button>
	<div id="DivTable">
		<table id="tDetail" border="1" cellpadding="5">

		</table>
	</div>


</body>
</html>