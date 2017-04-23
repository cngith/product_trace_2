<%@page import="com.wzr.dao.entity.Employee"%>
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
<a href="${baseURL }/employee/add" >添加员工</a>

<div>
	<table border=1>
		<c:choose>
		<c:when test="${empty employeeList}">
			<c:out value="无员工记录"></c:out>
		</c:when>
		<c:otherwise>
			<tr>
				<td>编号</td>
				<td>姓名</td>
				<td>所属部门</td>
				<td colspan="2">管理</td>
			</tr>
			<c:forEach items="${employeeList}" var="employee">
			<tr>
				<td><c:out value="${employee.epId }" /> </td>
				<td><c:out value="${employee.epName }" /> </td>
				<td><c:out value="${employee.workshop.wsName }" /> </td>
				<td>编辑</td>
<%-- 				<td><a href="${baseURL }/employee/edit?epId=${employee.epId}">编辑</a></td> --%>
				<td><a href= "${baseURL }/employee/delete?epId=${employee.epId}">删除</a></td>
			</tr>
			</c:forEach>
		</c:otherwise>
		</c:choose>
	</table>
</div>

</body>
</html>