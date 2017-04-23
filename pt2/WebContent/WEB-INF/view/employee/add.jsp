<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加员工</title>
</head>
<body>
<%@ include file="../titlemenu.jsp" %>
<sf:form modelAttribute="employee" method="post">
	<div>
	员工名:
	<sf:input path="epName" />
	所属部门:
	<sf:select path="workshop.wsId">
<%-- 		<sf:option value="-" label="-选择"/> --%>
		<sf:options items="${wsList }" itemLabel="wsName" itemValue="wsId" />
		
	</sf:select>
<%-- 	<sf:input path="workshop.wsId"/> --%>
	<input type="submit" value="提交" />
	</div>
</sf:form>
</body>
</html>