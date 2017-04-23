/**
 * 用户管理
 * 		添加用户
 */
//
//	function bodyLoad(){
//		getWsJSONList();
//		$("#divSum").css("display","none");
//	}
//	
	layer.config({
	extend: 'extend/layer.ext.js'
	});

	/**
	 * 取JSON格式的车间对象列表
	 */
	function addButtonClicked(){
//		layer.config({
//			  extend: baseURL + '/resource/js/plugin/layer/extend/layer.ext.js'
//		});
		layer.prompt({
			title: "用户名:",
			formType: 1
		},function(val){
			  layer.msg('得到了'+val);
			  alert("add:" + val);
			});
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
		var urlGet;
		var epId = $("#ep").val();
		var dt1 = $("#date1").val() + "T00:00:00";
		var dt2 = $("#date2").val() + "T23:59:59";
		if(0 == epId){
			// 针对部门统计
			urlGet = baseURL + "/count/acceptpbywsid?wsId=" + $("#ws").val()
			+ "&dateTime1=" + dt1 + "&dateTime2=" + dt2 ;
		}else{
			// 针对员工统计
			urlGet = baseURL + "/count/sendpbyepid?epId=" + $("#ep").val()
			+ "&dateTime1=" + dt1 + "&dateTime2=" + dt2 ;
			
		}
		$.ajax({
			type : "GET",
			url : urlGet,
			success : function(data){
				getProduceNumber(data);
				showPdInfo(data);
			}
		});
	}
	
	
	/**
	 * 根据客户代码取待接收商品明细
	 */
	function getProduceNumber(pdJsonArray){
		var pds = eval( "(" + pdJsonArray + ")");
		$("#divSum").css("display","");
		$("#spanPdNumber").html(pds.length);
	}
	
	/**
	 * 根据商品JSON格式数据输出此商品进度信息，
	 * @param ptJSONStr: 从服务器返回的JSON数据
	 */
	function showPdInfo(pdJsonArray){
		// 商品信息
		var pdsInfo = eval( "(" + pdJsonArray + ")" );
		if(0 == pdsInfo.length){
			$("#DivPdInfo").css("display","none");
			return;
		}
		
		$("#tDetail").html("");
		filTable(pdsInfo);
	
	}

	function filTable(jsonAry){
		var tdetail = document.getElementById("tDetail");
		tdetail.innerHTML = "";
		
		var row = document.createElement("tr");
		addTd(row,"条码");
		addTd(row,"款号");
		addTd(row,"颜色");
		addTd(row,"客户代码");
		addTd(row,"单号");
		addTd(row,"商品状态");
		tdetail.appendChild(row);
		
		for(var i=0;i<jsonAry.length;i++){ //循环将其中每个对象的属性取出
			var jsonObj = jsonAry[i];
			row = document.createElement("tr");
			addTd(row,jsonObj["barCode"]);	// 条码
			addTd(row,jsonObj["styleCode"]);	// 款号
			addTd(row,jsonObj["pdName"]);		// 颜色
			addTd(row,jsonObj["cusCode"]);	// 客户代码
			addTd(row,jsonObj["billNo"]);	// 单号
			addTd(row,jsonObj["status"]);	// 商品状态
			
			tdetail.appendChild(row);
		}
		
	}

	
	function addTd(row, str){
		var tnode = document.createTextNode(str);
		var td = document.createElement("td");
		td.appendChild(tnode);
		row.appendChild(td);
	}
