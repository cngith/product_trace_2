<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商品跟踪v2.5用户登录</title>
</head>
<body>
	<sf:form modelAttribute="loginUser" method="post">
		<div><span>商品跟踪v2.5</span></div>
		<table>
		    <tr>
		        <td colspan="2">用户登录</td>
		    </tr>
		    <tr>
		        <td>用户名</td>
		        <td><sf:input path="username" size="15" /></td>
		    </tr>
		    <tr>
		        <td>密&nbsp&nbsp码</td>
		        <td><sf:password path="password" size="15" /></td>
		    </tr>
		    <tr>
		        <td colspan="2"><input type="submit" name="submit" value="登录"></td>
		    </tr>
		</table>
	</sf:form>
	<font color="red">${excep.message }</font><br>
</body>
</html>