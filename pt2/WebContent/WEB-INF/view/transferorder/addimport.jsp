<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%-- <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增调拨单</title>
<%
	pageContext.setAttribute("baseURL", request.getContextPath());
%>
<script type="text/javascript">  
	var baseURL = "${baseURL}"; 
</script> 
<script type="text/javascript" src="${baseURL}/resource/js/addimport.js"></script>
<script type="text/javascript">  
	var todJson = '${transferOrderDetailJson}';
</script> 
</head>
<body onload="bodyLoad()">
<%@ include file="../titlemenu.jsp" %>



<input type="hidden" id="todJsonHidden"/>
<hr>
	<!-- <form id="todForm" action="" method="post"> -->
	<div id="ioTag">
		<div id="from" >
			<span>调出部门:</span>	
			<select name="fromWs" id="fromWs" onchange="fromWsChanged()">
				
			</select>
			<span>发送人:</span>	
			<select name="fromEp" id="fromEp" >
				
			</select>	
		</div>

	
		<div id="to">
			<span>调入部门:</span>	
			<select id="toWs" name="toWs" onchange="toWsChanged()">
				
			</select>
			<span>接收人:</span>	
			<select id="toEp" name="toEp">
				
			</select>	
		</div>	
		
		<button id="submitBtn" onclick="return importAddSubmit()">保存调拨单</button>
	</div>
	
	
	<div>
		<table border="1" id="tdetail">
		</table>
	</div>

	<div>${excep.message }<span id="message"></span></div>
<!-- </form> -->
</body>
</html>