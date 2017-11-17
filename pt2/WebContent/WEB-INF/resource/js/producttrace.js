/**
 * 
 */
		
	function bodyLoad(){
		$("#divStatus").css("display","none");
	}
	function barCodeKeyPress(){
		getPtByBarCode();
	}
	
	function getPtByBarCode(){
		var barCode = $.trim($("#barCode").val());
		// FIXME 当前条码位数为魔数, 未来版本将在页面加载时后台传入
		if(barCode.length < 9 || barCode.length > 15){
			// 条码位数不符, 直接返回.
			$("#infoBarCode").html("条码位数不符, 请输入9-15位条码");
			return;
		}
		$("#infoBarCode").html("");
		// 去后台验证[条码]并返回[发送部门][发送人员]信息
		$.ajax({
			type : 'GET',
			url : baseURL + "/complete/getptbybarcode?barCode=" + barCode,
			success : function(backData){
				showPtInfo(backData);
			}
		})
		
	}
	/**
	 * 根据商品JSON格式数据输出此商品进度信息，
	 * @param ptJSONStr: 从服务器返回的JSON数据
	 */
	function showPtInfo(ptJSONStr){
		//spanPdStatus;
		// 商品的状态信息
		var ptsInfo = eval( "(" + ptJSONStr + ")" );
		if(0 == ptsInfo.length){
			$("#divStatus").css("display","none");
			$("#tDetail").html("");
			alert("服务端没有返回记录.");
			return;
		}
		if(!ptsInfo.hasOwnProperty("pdStatus")){
			alert("返回信息有误.");
			$("#divStatus").css("display","none");
			return;
		}
		$("#spanPdStatus").html(ptsInfo["pdStatus"]);
		$("#divStatus").css("display","");
		if("NONE" == ptsInfo["pdState"]){
			$("#spanWsEp").html("无");
			$("#tDetail").html("");
		}
		
		if("WAIT_FOR_ACCEPT" == ptsInfo["pdState"]
			|| "ACCEPTED" == ptsInfo["pdState"]
		|| "CANCEL" == ptsInfo["pdState"]
		|| "DONE" == ptsInfo["pdState"]
		){
			if(ptsInfo.hasOwnProperty("ptInfo")){
				var tdetail = document.getElementById("tDetail");
				$("#tDetail").html("");
				$("#spanWsEp").html(filTable(ptsInfo["ptInfo"],ptsInfo["pdState"]));
			}
			
		}
	}

	function filTable(jsonAry,pdState){
		var tdetail = document.getElementById("tDetail");
		tdetail.innerHTML = "";
		
		var row = document.createElement("tr");
		addTd(row,"日期时间");
		addTd(row,"相关部门及人员");
		addTd(row,"事件");
		addTd(row,"状态");
		tdetail.appendChild(row);
		
		for(var i=0;i<jsonAry.length;i++){ //循环将其中每个对象的属性取出
			var jsonObj = jsonAry[i];
			var lastInfo = "";
			if("" == jsonObj["toWsName"] && "" == jsonObj["toTime"]){
				// 完工记录
				row = document.createElement("tr");
				addTd(row,jsonObj["fromTime"]);	// 日期时间
				var depEp = jsonObj["fromWsName"] + "." + jsonObj["fromEpName"];
				if("DONE" == pdState){
					addTd(row,depEp);	// 相关部门及人员
					addTd(row,"完工");	// 事件
					addTd(row,"已完工");	// 状态
				}else if("CANCEL" == pdState){
					addTd(row,depEp);	// 相关部门及人员
					addTd(row,"取消");	// 事件
					addTd(row,"已取消");	// 状态
					
				}
				
				tdetail.appendChild(row);
			}else if("" != jsonObj["toWsName"]){
				// 调拨记录
				row = document.createElement("tr");
				addTd(row,jsonObj["fromTime"]);	// 日期时间
				var depEp = jsonObj["fromWsName"] + "." + jsonObj["fromEpName"];
				depEp += "--" + jsonObj["toWsName"] + "." + jsonObj["toEpName"];
				addTd(row,depEp);	// 相关部门及人员
				addTd(row,"生产");	// 事件
				addTd(row,"已发送");	// 状态
				tdetail.appendChild(row);
				
				row = document.createElement("tr");
				addTd(row,jsonObj["toTime"]);	// 日期时间
				addTd(row,depEp);	// 相关部门及人员
				addTd(row,"生产");	// 事件
				if("" == jsonObj["toTime"]){
					addTd(row,"待接收");	// 状态
				}else{
					addTd(row,"已接收");	// 状态
				}
				tdetail.appendChild(row);
			}
			if(i == jsonAry.length - 1){
				if("" == jsonObj["toTime"]){
					// 已发送未接收状态,已完工状态
					lastInfo = jsonObj["fromWsName"] + "." + jsonObj["fromEpName"];
				}else{// 已发送已接收状态
					lastInfo = jsonObj["toWsName"] + "." + jsonObj["toEpName"];
				}
			}
		}
		return lastInfo;
		
	}

	
	function addTd(row, str){
		var tnode = document.createTextNode(str);
		var td = document.createElement("td");
		td.appendChild(tnode);
		row.appendChild(td);
	}
	
	