<%@page import="com.wzr.dao.entity.Workshop"%>
<%@page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%@ include file="../titlemenu.jsp" %>
<hr>
<%
	String contextPath = request.getContextPath();
	pageContext.setAttribute("baseURL", contextPath);
%>
<a href="${baseURL }/workshop/add" >新建部门</a>

<div>
	<table border=1>
		<c:choose>
		<c:when test="${empty workshopList}">
			<c:out value="无部门记录"></c:out>
		</c:when>
		<c:otherwise>
			<tr>
				<td>编号</td>
				<td>部门名称</td>
				<td colspan="2">管理</td>
			</tr>
			<c:forEach items="${workshopList}" var="workshop">
			<tr>
				<td><c:out value="${workshop.wsId }" /> </td>
				<td><c:out value="${workshop.wsName }" /> </td>
				<td>编辑</td>
<%-- 				<td><a href="${baseURL }/workshop/edit?wsId=${workshop.wsId}">编辑</a></td> --%>
				<td><a href= "${baseURL }/workshop/delete?wsId=${workshop.wsId}">删除</a></td>
			</tr>
			</c:forEach>
		</c:otherwise>
		</c:choose>
	</table>
</div>

</body>
</html>