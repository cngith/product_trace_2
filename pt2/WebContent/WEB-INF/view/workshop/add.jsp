<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%@ include file="../titlemenu.jsp" %>
<sf:form modelAttribute="workShop" method="post">
	<div>
	部门名:
	<sf:input path="wsName" />
	<input type="submit" value="提交" />
	</div>
</sf:form>
</body>
</html>