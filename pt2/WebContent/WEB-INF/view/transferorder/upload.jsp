<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- <%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %> --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>导入文件</title>
</head>
<body>
<%@ include file="../titlemenu.jsp" %>
<hr>
<form action="uploadimport" method="post" enctype="multipart/form-data">
	<div>
		<span>导入文件:</span>	
		<input type="file" name="importfile" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"/>
	</div>
	
	<input type="submit" value="上传文件" />
</form>
<div>${excep.message }</div>
</body>
</html>