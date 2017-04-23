/**
 * 导入调拨单2015-12-10
 * 关键控件:
 * 	
 * 对于"判断当前条码是否处于生产过程或已完工",
 * 要求前台添加每条明细时校验,同时提交整个表单后,后台完整校验.
 */

	function bodyLoad(){
//		var todJson = document.getElementById("todJson").value;
//		alert("OK");	
//		alert(typeof(todJson));	
		filTable(todJson);
		getWsJSONList();
	}
	
	
	function filTable(jsonArray){
		
		document.getElementById("message").innerText = "";
		document.getElementById("tdetail").innerHTML = "";
		var dL = eval("(" + jsonArray + ")");
//		var dL = jsonArray;
		var row = document.createElement("tr");
		addTd(row,"商品条码");
		addTd(row,"款号");
		addTd(row,"颜色");
		addTd(row,"长度");
		addTd(row,"尺码");
		addTd(row,"类别");
		addTd(row,"单号");
		addTd(row,"备注");
		document.getElementById("tdetail").appendChild(row);
		for(var i=0;i<dL.length;i++){ //循环将其中每个对象的属性取出
			row = document.createElement("tr");
			var dr = dL[i];
			var td = document.createElement("td");
			
			addTd(row,dr["barCode"]);
			addTd(row,dr["styleCode"]);
			addTd(row,dr["pdName"]);
			addTd(row,dr["length"]);
			addTd(row,dr["size"]);
			addTd(row,dr["cusCode"]);
			addTd(row,dr["billNo"]);
			addTd(row,dr["remark"]);
			document.getElementById("tdetail").appendChild(row);
		}
		
	}

	function addTd(row, str){
		var tnode = document.createTextNode(str);
		var td = document.createElement("td");
		td.appendChild(tnode);
		row.appendChild(td);
	}
	
	/**
	 * 取JSON格式的车间对象列表
	 */
	function getWsJSONList(){
		var xhr;
		if(window.XMLHttpRequest){
			xhr = new XMLHttpRequest();
		}else{
			xhr = new ActiveXObject("Microsoft.XMLHTTP");
		}
		xhr.onreadystatechange = function putWsOption(){
			if(xhr.readyState == 4){
				if(xhr.status == 200){
					var fromWsSel = document.getElementById("fromWs");
					var fromEpSel = document.getElementById("fromEp");
					var wsJSONList = xhr.responseText;
					addWsOption(fromWsSel, wsJSONList);
					wsChanged(fromWsSel, fromEpSel);
					
					var toWsSel = document.getElementById("toWs");
					var toEpSel = document.getElementById("toEp");
					addWsOption(toWsSel,wsJSONList);
					wsChanged(toWsSel, toEpSel);
				}
			}
		}
		// TODO 同步方式提交
		xhr.open("GET", baseURL + "/workshop/getwsjsonlist", false);
		xhr.send();
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
	
	function fromWsChanged(){
		var fromWsSel = document.getElementById("fromWs");
		var fromEpSel = document.getElementById("fromEp");
		wsChanged(fromWsSel, fromEpSel);
	} 

	function toWsChanged(){
		var toWsSel = document.getElementById("toWs");
		var toEpSel = document.getElementById("toEp");
		wsChanged(toWsSel, toEpSel);
	}
	
	/**
	 * 根据选中的车间填充员工列表
	 * @param wsSel: 车间select控件
	 * @param epSel: 员工select控件
	 */
	function wsChanged(wsSel, epSel){
		
		var index = wsSel.selectedIndex;
		if(undefined == wsSel.options[index]){
			return;
		}
		var value = wsSel.options[index].value;
		var xmlhttp;
		if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera,Safari
			xmlhttp = new XMLHttpRequest();
		} else {// code for IE6, IE5
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4) { // 4: 请求已完成，且响应已就绪
				if (xmlhttp.status == 200) {
					// 200: "OK"
					addEpOption(epSel, xmlhttp.responseText);
				}
			}
		}
		xmlhttp.open("GET", baseURL + "/employee/getepbywsid?wsId=" + value, false);
		xmlhttp.send();
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
	 * 判断发送部门和接收部门是否合法(二者相同就不合法)
	 * @returns {Boolean}合法返回true
	 */
	function fromAndToWsIsValid(){
		var fromWsSel = document.getElementById("fromWs");
		var fromIndex = fromWsSel.selectedIndex;
		var fromValue = fromWsSel.options[fromIndex].value;
		var toWsSel = document.getElementById("toWs");
		var toIndex = toWsSel.selectedIndex;
		var toValue = toWsSel.options[toIndex].value;
		if(fromValue == toValue){
			alert("[发送部门]和[接收部门]不能相同.");
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 判断发送人和接收人是否合法(二者相同就不合法)
	 * @returns {Boolean}合法返回true
	 */
	function fromAndToEpIsValid(){
		var fromEpSel = document.getElementById("fromEp");
		var toEpSel = document.getElementById("toEp");
		if(fromEpSel.value == toEpSel.value && fromEpSel.value != 0){
			alert("[发送人]和[接收人]不能相同.");
			return false;
		}else{
			return true;
		}
	}
	
	function importAddSubmit(){
		if(false == fromAndToWsIsValid()){
			return false;
		}
		if(false == fromAndToEpIsValid()){
			return false;
		}
		if(confirm('确认导入此调拨单吗?')){
			
			var xhr;
			if(window.XMLHttpRequest){
				xhr = new XMLHttpRequest();
			}else{
				xhr = new ActiveXObject("Microsoft.XMLHTTP");
			}
			xhr.onreadystatechange = function putWsOption(){
				if(xhr.readyState == 4){
					if(xhr.status == 200){
						
						var reJson = xhr.responseText;
						if(null == reJson || "" == reJson){
							alert("未知错误");
						}else {
							var reJsonObj = eval("(" +reJson + ")");
							if("" == reJsonObj["err"]){
								alert("添加成功");
								var ioTag = document.getElementById("ioTag");
								ioTag.style.display = "none";
							}else{
								alert(reJsonObj["err"]);
							}
						}
						
					}
				}
			}
			var fromWsSel = document.getElementById("fromWs");
			var toWsSel = document.getElementById("toWs");

			var fromEpSel = document.getElementById("fromEp");
			var toEpSel = document.getElementById("toEp");
			var postStr = "todJson=" + escEncode(todJson)
			+ "&fromWs=" + fromWsSel.value + "&toWs=" + toWsSel.value
			+ "&fromEp=" + fromEpSel.value + "&toEp=" + toEpSel.value;
			
			xhr.open("POST", baseURL + "/transferorder/toimportadd", true);
			xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');

			xhr.send(postStr);
			
		}else{
			return false;
		}
	}
	
	function escEncode(strSendToServer){
//		strSendToServer = strSendToServer.replace(/\%/g,'%25');	// 由于%是转义字符，所以必第一个调用，先将值中的%转义出来
		strSendToServer = strSendToServer.replace(/\\/g,'%5C').replace(/\^/g,'%5E'); // //
		strSendToServer = strSendToServer.replace(/\ /g,'%20').replace(/\,/g,'%2C');
		strSendToServer = strSendToServer.replace(/\"/g,'%22').replace(/\'/g,'%27').replace(/\&/g,'%26');
		strSendToServer = strSendToServer.replace(/\./g,'%2E');
//		strSendToServer = strSendToServer.replace(/\}/g,'%7D');
//		strSendToServer = strSendToServer.replace(/\{/g,'%7B');
		strSendToServer = strSendToServer.replace(/\=/g,'%3D').replace(/\?/g,'%3F').replace(/\//g,'%2F');
		strSendToServer = strSendToServer.replace(/\[/g,'%5B').replace(/\]/g,'%5D');
		strSendToServer = strSendToServer.replace(/\#/g,'%23').replace(/\:/g,'%3A').replace(/\+/g,'%2B');
		return strSendToServer;
	}
