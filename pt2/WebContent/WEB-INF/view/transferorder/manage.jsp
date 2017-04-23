<%@page import="com.wzr.dao.entity.Workshop"%>
<%@page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>生产单管理</title>
</head>
<body>
<%@ include file="../titlemenu.jsp" %>
<hr>
<%
	String contextPath = request.getContextPath();
	pageContext.setAttribute("baseURL", contextPath);
%>
<a href="${baseURL }/transferorder/upload" >导入生产单</a>
<a href="${baseURL }/transferorder/addtobystockview" >车间生产调拨</a>
<a href="${baseURL }/transferorder/accept" >接收商品</a>

<!-- 
<div>
起始时间:<input type="datetime-local" id="dateTime1"/>
</div>
 -->
 
<div>
	<table border=1>

	</table>
</div>

</body>
</html>