/**
 * 页面加载后查询所有皮号类型并显示
 * @returns
 */
function bodyLoad() {
	$.ajax({
		type : "GET",
		url : baseURL + "/product_attribute/get_all_mink_type",
		success : function(data){
			showMinkTypes(data);
			
		}
	});
}

/**
 * 根据JSON格式数据输出此皮号类型信息，
 * 
 * @param mtJsonArray:
 *            从服务器返回的JSON数据
 */
function showMinkTypes(mtJsonArray) {
	// 结果信息对象
	var joRe = eval("(" + mtJsonArray + ")");
	var exportContent = joRe.exportContent;
	if(exportContent.length > 0){
		$("#spanResult").html("");
		filTable(joRe.exportField, exportContent);
	}
	else{
		$("#spanResult").html("没有查到任何皮号类型的信息。");
	}

}

/**
 * 
 * @param exportField : 多行2列的数组，用于保存显示表格的表头和字段名
 * @param exportContent : 数组，用于保存表格内容
 * @returns
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
	addTh(row,"删除");
	tdetail.appendChild(row);

	for (var i = 0; i < exportContent.length; i++) {
		// 循环添加行
		var jsonObj = exportContent[i];
		row = document.createElement("tr");
		for (var col = 0; col < exportField.length; ++col) {
			// 将其中每个对象的属性取出添加列
			addTd(row, jsonObj[exportField[col][0]]); // 按字段名取内容
		}
		
		// 为每条记录添加删除链接
		var delHref = baseURL + "/product_attribute/delete_mink_type?minkTypeId=" + jsonObj[exportField[0][0]];
		addTd(row,"<a href='" + delHref + "'>删除</a>");
		
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

/**
 * 添加一个单元格
 * @param row ：单元格所在的行对象
 * @param str ：单元格中的内容
 * @returns
 */
function addTd(row, str) {
		var td = document.createElement("td");
		td.innerHTML = str;
		row.appendChild(td);
	
}
