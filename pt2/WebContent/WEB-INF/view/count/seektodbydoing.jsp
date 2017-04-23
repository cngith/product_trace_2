<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>按生产天数查询</title>
<%
	pageContext.setAttribute("baseURL", request.getContextPath());
%>
<script type="text/javascript">  
	var baseURL = "${baseURL}";  
</script> 
<script type="text/javascript" src="${baseURL}/resource/js/jquery.min.js"></script>
<script type="text/javascript" src="${baseURL}/resource/js/plugin/laydate/laydate.js"></script>
<script type="text/javascript" src="${baseURL}/resource/js/count/seektodbydoing.js"></script>

</head>
<body onload="bodyLoad()">
<%@ include file="../titlemenu.jsp" %>
<hr>
	<div>
		<div class="inputTag">
		<div id="from">
			<span>部门:</span>	
			<select id="ws" onchange="wsChanged()" >
			</select>
				
			<span>员工:</span>	
			<select id="ep" >
			</select>	
			<div class="divDate">
	        	已经生产
	        	<select id="doDay">
	        	</select>
	        	天以上
	        	<span id="seekDate"></span>
	    	</div>
		</div>
		<div><button onclick="seekBtnClick()">查询</button></div>
	</div>
		<div id="divSum">
			<span>共查到结果:</span>	
			<span id="spanResultNumber"></span>
			<span>条</span>	
		</div>
	</div>
	
	<div id="divResultSpace">
		<table id="tDetail" border="1" cellpadding="5">

		</table>
	</div>
	
</body>
</html>