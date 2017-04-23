/**
 * 按客户查询商品 查询给定客户Id订货的商品当前所在部门(人员)
 */

//$(document).ready(function(){
//	// 这些代码在页面加载后将执行
//	$("#cusCode").onKeyPress(function(event){
//		// 绑定代码到cusCode的onKeyPress事件
//		if(event.keyCode == 13){
//			alert("kp");
//			seek();
//		}
//	});
//});

function bodyLoad() {
	$("#divSum").css("display", "none");
}
/**
 * 按客户查询商品
 */
function seek() {
	if ("" == $("#cusCode").val()) {
		// 应弹出软提示
		alert("请输入客户代码");
		return;
	}
	var urlGet;
	var cusCode = $("#cusCode").val();
	urlGet = baseURL + "/count/seekpdbycuscode?cusCode=" + cusCode;
	$("#spanFilenameSaveAs").html("客户" + $("#cusCode").val() + "订货商品状态");
	$.ajax({
		type : "GET",
		url : urlGet,
		success : function(data) {
			showPdInfo(data);
		}
	});
}

/**
 * 根据商品JSON格式数据输出此商品进度信息，
 * 
 * @param ptJSONStr:
 *            从服务器返回的JSON数据
 */
function showPdInfo(pdJsonArray) {
	// 结果信息对象
	var joRe = eval("(" + pdJsonArray + ")");
	var exportContent = joRe.exportContent;
	$("#exportContent").val(JSON.stringify(exportContent)); // JSON对象转换成字符串
	$("#divSum").css("display", "");
	// 根据返回数据统计结果合计数
	$("#spanPdNumber").html(exportContent.length);
	if (0 == exportContent.length) {
		$("#DivPdInfo").css("display", "none");
		return;
	}
	$("#DivPdInfo").css("display", "");
	$("#tDetail").html("");
	$("#exportField").val(JSON.stringify(joRe.exportField));
	filTable(joRe.exportField, exportContent);

}

/*
 * 填充表格 exportField 字段对象(保存代码,名称(数组)) exportContent 表格内容对象(数组)
 */
function filTable(exportField, exportContent) {
	var tdetail = document.getElementById("tDetail");
	tdetail.innerHTML = "";

	var row = document.createElement("tr");
	var fieldName;
	for (var i = 0; i < exportField.length; ++i) {
		// 添加表头
		fieldName = exportField[i][1];
		addTh(row, fieldName);
	}

	tdetail.appendChild(row);

	for (var i = 0; i < exportContent.length; i++) {
		// 循环添加行
		var jsonObj = exportContent[i];
		row = document.createElement("tr");
		for (var col = 0; col < exportField.length; ++col) {
			// 将其中每个对象的属性取出添加列
			addTd(row, jsonObj[exportField[col][0]]); // 按字段名取内容
		}

		tdetail.appendChild(row);
	}

}

function addTh(row, str) {
	var tnode = document.createTextNode(str);
	var th = document.createElement("th");
	th.setAttribute("class", "tableTitle");
	th.appendChild(tnode);
	row.appendChild(th);
}
function addTd(row, str) {
	var tnode = document.createTextNode(str);
	var td = document.createElement("td");
	td.appendChild(tnode);
	row.appendChild(td);
}

/**
 * 导出查询结果
 */
function saveDataAs() {
	var exportContent = eval("(" + $("#exportContent").val() + ")");
	if(exportContent.length == 0){
		alert("没有可导出的数据");
		return;
	}
	var js={};	// 待传向后台的JSON对象
	// 字段信息
	js.exportField = eval("(" + $("#exportField").val() + ")");
	// 内容信息
	js.exportContent = exportContent;
	// 文件名
	js.filenameSaveAs=$("#spanFilenameSaveAs").html();
	
	var postData = JSON.stringify(js);
	var urlGo = baseURL + "/index/saveAsXlsx";
	download(urlGo,postData);
}

/**
 * 代码生成表单，提交下载请求
 * 
 * @param url
 * @param postData
 */
function download(url, postData) {
	var form = $("<form>"); // 定义一个form表单
	form.attr('style', 'display:none'); // 在form表单中添加查询参数
	form.attr('target', '');
	form.attr('method', 'post');// 使用POST方式提交
	form.attr('action', url);

	var input1 = $('<input>');
	input1.attr('type', 'hidden');
	input1.attr('name', 'postInfo');
	input1.attr('value', postData);
	$('body').append(form); // 将表单放置在web中
	form.append(input1); // 将查询参数控件添加到表单上
	form.submit();

}