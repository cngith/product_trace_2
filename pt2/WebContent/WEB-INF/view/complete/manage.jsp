<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>商品完工</title>
	<%
		pageContext.setAttribute("baseURL", request.getContextPath());
	%>
	<script type="text/javascript">  
		var baseURL = "${baseURL}";  
	</script> 
	<script type="text/javascript" src="${baseURL}/resource/js/cm_manage.js"></script>
</head>
<body onload="bodyLoad()">
<%@ include file="../titlemenu.jsp" %>
<hr>

<!-- 
	<a href="${baseURL }/transferorder/upload" >新建调拨单[导入]</a> 
	<a href="${baseURL }/transferorder/accept" >接收商品</a>
-->
	<a href="${baseURL }/complete/addcpbystockview" >商品完工</a>
	<a href="${baseURL }/complete/cancelcpbystockview" >商品取消</a>

<!-- 
<div>
	<table border=1 id="table_cm">
		

	</table>
</div> -->
<div><span id="message" ></span></div>
</body>
</html>