<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>产量统计</title>
<%
	pageContext.setAttribute("baseURL", request.getContextPath());
%>
<script type="text/javascript">  
	var baseURL = "${baseURL}";  
</script> 
<script type="text/javascript" src="${baseURL}/resource/js/jquery.min.js"></script>
<script type="text/javascript" src="${baseURL}/resource/js/plugin/laydate/laydate.js"></script>
<script type="text/javascript" src="${baseURL}/resource/js/count/countnumberproduce.js"></script>

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
			</div>
			<div class="demo1">
	        	日期范围：
	        	<input type="text" id="date1" class="laydate-icon" />
	        	--
	        	<input type="text" id="date2" class="laydate-icon"/>
	    	</div>
			<div>
				<button onclick="countNumberProduce()">查询</button>
				<button onclick="saveDataAs()">导出…</button>
			</div>
		</div>
		<div id="divSum">
			<span id="spanFilenameSaveAs"></span>
			<span>共生产(发送)商品:</span>	
			<span id="spanPdNumber"></span>
			<span>件</span>	
		</div>
	</div>
	
	<div id="DivPdInfo">
		<table id="tDetail" border="1" cellpadding="5">

		</table>
	</div>
	
 	<input type="hidden" id="exportField"/> <!-- 按顺序存放表格字段代码,用于后台从JSON中取值时的KEY -->
 	<input type="hidden" id="exportContent"/> <!-- 存放查询结果用于导出 -->
		<script type="text/javascript">
       laydate({
          elem: '#date1'
        });
        laydate({
            elem: '#date2'
        });
        
    </script>
</body>
</html>