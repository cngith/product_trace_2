/**
 * 产量统计
 * 		计算给定时间段内某部门发送（并被接收的商品）
 * 		起始时间<=发送时间<接收时间<=结束时间
 */

	function bodyLoad(){
		getWsJSONList();
		$("#divSum").css("display","none");
	}
	

	/**
	 * 取JSON格式的车间对象列表
	 */
	function getWsJSONList(){
		$.ajax({
			type : "GET",
			url : baseURL + "/workshop/getwsjsonlist",
			success : function(data){
				var wsJSONList = data;
				var wsSel = document.getElementById("ws");
				addWsOption(wsSel, wsJSONList);
				wsChanged();
			},
			async : false
		});
	}
	

	/**
	 * 向车间select控件中填充车间对象
	 * @param wsSel: 车间select控件
	 * @param wsJSONList: JSON格式的车间对象列表
	 */
	function addWsOption(wsSel, wsJSONList){
		var jsonArray = wsJSONList;
		var objList = eval(jsonArray);
		
		for(var i=0;i<objList.length;i++){ //循环将其中每个对象的属性取出
			var obj = objList[i];
			var itemV = obj["wsId"];
			var item = obj["wsName"];
			addOption(wsSel, item, itemV);
		}
		if(0 < wsSel.length)
			wsSel.options[0].selected = true;
	}
	
		
	/**
	 * 根据选中的车间填充员工列表
	 * @param wsSel: 车间select控件
	 * @param epSel: 员工select控件
	 */
	function wsChanged(){
		
		if(0 == $("#ws").get(0).length){
			return;
		}
		var value = $("#ws").val();
		$.ajax({
			type : "GET",
			url : baseURL + "/employee/getepbywsid?wsId=" + value,
			success : function(data){
				var epSel = document.getElementById("ep");
				addEpOption(epSel, data);
			}
		});
	}
	
	/**
	 * @param epSel: 员工select控件
	 * @param epList: 员工对象数组(JSON格式)
	 */
	function addEpOption(epSel, epJSONList){
		var jsonArray = epJSONList;
		var objList = eval(jsonArray);
		epSel.innerHTML = "";
		addOption(epSel, "", "0");	// 此项表示未选择具体员工
		for(var i=0;i<objList.length;i++){ //循环将其中每个对象的属性取出
			var obj = objList[i];
			var itemV = obj["epId"];
			var item = obj["epName"];
			addOption(epSel, item, itemV);
		}
		if(0 < epSel.length)
			epSel.options[0].selected = true;
	}
	
	function addOption(list, item, itemV){
		list.options.length = list.options.length+1;
		var option = new Option(item,itemV,false,true);
		list.options[list.options.length-1] = option;
	}
	
	
	/**
	 * 统计产量
	 */
	function countNumberProduce(){
		if("" == $("#date1").val() || "" == $("#date2").val()){
			// 应弹出软提示
			alert("请选择日期范围");
			return;
		}
		var urlGet;
		var epId = $("#ep").val();
		var dt1 = $("#date1").val() + "T00:00:00";
		var dt2 = $("#date2").val() + "T23:59:59";
		if(0 == epId){
			// 针对部门统计
			urlGet = baseURL + "/count/sendpbywsid?wsId=" + $("#ws").val()
			+ "&dateTime1=" + dt1 + "&dateTime2=" + dt2 ;
			$("#spanFilenameSaveAs").html($("#ws option:selected").text() 
					+ "(" + $("#date1").val()+ "至" + $("#date2").val() + ")" + "产量统计");
		}else{
			// 针对员工统计
			urlGet = baseURL + "/count/sendpbyepid?epId=" + $("#ep").val()
			+ "&dateTime1=" + dt1 + "&dateTime2=" + dt2 ;
			$("#spanFilenameSaveAs").html($("#ws option:selected").text() + "." + $("#ep option:selected").text() 
					+ "(" + $("#date1").val()+ "至" + $("#date2").val() + ")" + "产量统计");
			
		}
		$.ajax({
			type : "GET",
			url : urlGet,
			success : function(data){
				showPdInfo(data);
			}
		});
	}
	
	
	/**
	 * 根据商品JSON格式数据输出此商品进度信息，
	 * @param pdJsonArray: 从服务器返回的JSON数据
	 */
	function showPdInfo(pdJsonArray){
		// 结果信息对象
		var joRe = eval( "(" + pdJsonArray + ")" );
		var exportContent = joRe.exportContent;
		$("#exportContent").val(JSON.stringify(exportContent)); // JSON对象转换成字符串
		$("#divSum").css("display","");
		// 根据返回数据统计结果合计数
		$("#spanPdNumber").html(exportContent.length);
		if(0 == exportContent.length){
			$("#DivPdInfo").css("display","none");
			return;
		}
		$("#DivPdInfo").css("display","");
		$("#tDetail").html("");
		$("#exportField").val(JSON.stringify(joRe.exportField));
		filTable(joRe.exportField,exportContent);
	
	}

	/*
	 * 填充表格
	 * exportField 字段对象(保存代码,名称(数组))
	 * exportContent 表格内容对象(数组)
	 */
	function filTable(exportField,exportContent){
		var tdetail = document.getElementById("tDetail");
		tdetail.innerHTML = "";
		
		var row = document.createElement("tr");
		var fieldName;
		for(var i=0; i<exportField.length; ++i){
			// 添加表头
			fieldName = exportField[i][1];
			addTh(row, fieldName);
		}
		
		tdetail.appendChild(row);
		
		for(var i=0;i<exportContent.length;i++){ 
			//循环添加行
			var jsonObj = exportContent[i];
			row = document.createElement("tr");
			for(var col=0; col < exportField.length;++col){
				// 将其中每个对象的属性取出添加列
				addTd(row,jsonObj[exportField[col][0]]);	// 按字段名取内容
			}
			
			tdetail.appendChild(row);
		}
		
	}

	function addTh(row, str){
		var tnode = document.createTextNode(str);
		var th = document.createElement("th");
		th.setAttribute("class","tableTitle");
		th.appendChild(tnode);
		row.appendChild(th);
	}
	function addTd(row, str){
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
	 * @param url
	 * @param postData
	 */
	 function download(url,postData) {
        var form = $("<form>");   	// 定义一个form表单
        form.attr('style', 'display:none');   // 在form表单中添加查询参数
        form.attr('target', '');
        form.attr('method', 'post');// 使用POST方式提交
        form.attr('action', url);

        var input1 = $('<input>');
        input1.attr('type', 'hidden');
        input1.attr('name', 'postInfo');
        input1.attr('value', postData);
        $('body').append(form);  //将表单放置在web中
        form.append(input1);   	//将查询参数控件添加到表单上
        form.submit();

     }