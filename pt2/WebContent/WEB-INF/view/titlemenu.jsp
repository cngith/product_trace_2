<%@ page language="java" pageEncoding="utf-8"%>
<!-- 此页不能加 contentType="text/html; charset=UTF-8" -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Insert title here</title>
</head>
<body>
	<div id="titleMenu">
		<a href=<%=request.getContextPath() + "/transferorder/manage" %> >生产单管理</a>
		<a href=<%=request.getContextPath() + "/complete/manage" %> >结束生产 </a>
		<a href=<%=request.getContextPath() + "/pt/ptview" %> >商品跟踪</a>
 		<a href=<%=request.getContextPath() + "/count/countitems" %> >查询统计</a> 
		<a href=<%=request.getContextPath() + "/product_attribute/manage" %> >商品属性管理</a>
		<a href=<%=request.getContextPath() + "/workshop/manage" %> >部门管理</a>
		<a href=<%=request.getContextPath() + "/employee/manage" %> >员工管理</a>
		<a href=<%=request.getContextPath() + "/user/manage" %> >用户管理</a>
		<a href=<%=request.getContextPath() + "/logout" %> >退出登录</a>|
		<span>当前用户:<%= session.getAttribute("loginUsername")%></span>
	
	</div>
</body>
</html>