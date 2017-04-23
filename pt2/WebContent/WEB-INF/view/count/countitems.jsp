<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>查询统计</title>
	<%
		pageContext.setAttribute("baseURL", request.getContextPath());
	%>
	<script type="text/javascript">  
		var baseURL = "${baseURL}";  
	</script> 
</head>
<body>
<%@ include file="../titlemenu.jsp" %>
<hr>
	<a href="${baseURL }/count/countnumberproduce" >产量统计</a>&nbsp
	<a href="${baseURL }/count/seekpdbycusview" >按客户查询商品</a>&nbsp
	<a href="${baseURL }/count/seektodbydoingview" >查询待发送商品</a>&nbsp
<%-- 	<a href="${baseURL }/count/countnumberaccept" >查询待发送商品</a>&nbsp --%>

</body>
</html>