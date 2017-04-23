<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>调拨单明细</title>
	<style type="text/css">
		td[align]{ text-align:right;}
	</style>
</head>

<body onload="bodyLoad()">
<%@ include file="../titlemenu.jsp" %>
<hr>
<%
	pageContext.setAttribute("baseURL", request.getContextPath());
%>
<script type="text/javascript">  
	var baseURL = "${baseURL}";  
</script> 
<script type="text/javascript" src="${baseURL}/resource/js/todetail.js"></script>

<form id="myForm" action="" method="post" >
	<input type="hidden" value="${tmId}" name="tmId"/>
	当前调拨单号为：<span id="tmId" >${tmId}</span>
	<div>
		<table id="tdetail" border="1" cellpadding="5">

		</table>
	</div>
	
 	<div><input type="checkbox" id="chooseAll" checked="checked" onclick="chooseGo()" >全选 </input></div>
	<div><button id="submitBtn" onclick="return submitClick()">接收选中商品</button> </div>
	<div>${excep.message }</div>
</form>
</body>
</html>