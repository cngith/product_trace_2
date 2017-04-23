/**
 * 显示完工记录列表
 */

	function bodyLoad(){
		
		//getCmJSONList();
		
	}
	
	
	
	/**
	 * 取JSON格式的完工记录列表
	 */
	function getCmJSONList(){
		var xhr;
		if(window.XMLHttpRequest){
			xhr = new XMLHttpRequest();
		}else{
			xhr = new ActiveXObject("Microsoft.XMLHTTP");
		}
		xhr.onreadystatechange = function putWsOption(){
			if(xhr.readyState == 4){
				if(xhr.status == 200){
					filTable(xhr.responseText);
				}
			}
		}
		// TODO 异步方式提交
		xhr.open("GET", baseURL + "/complete/getcmListbydate", true);
		xhr.send();
	}
	

	function filTable(jsonArray){
		var tdetail = document.getElementById("table_cm")
		tdetail.innerHTML = "";
		var jsonAry = eval("(" + jsonArray + ")");
		
		var msg = document.getElementById("message");
		msg.innerHTML = "共找到 " + jsonAry.length + " 条记录.";
		if(0 == jsonAry.length){
			return;
		}
		var row = document.createElement("tr");
		addTd(row,"编号");
		addTd(row,"开单时间");
		addTd(row,"操作员");
		addTd(row,"明细");
		tdetail.appendChild(row);
		
		for(var i=0;i<jsonAry.length;i++){ //循环将其中每个对象的属性取出
			row = document.createElement("tr");
			var jsonObj = jsonAry[i];
			addTd(row,jsonObj["cmId"]);
			addTd(row,jsonObj["cmDatetime"]);
			addTd(row,jsonObj["userName"]);
			var td = document.createElement("td");
			td.innerHTML = "<a href=\"" + baseURL +"/complete/cpdview?cmId=" + jsonObj["cmId"] + "\">明细</a>";
			row.appendChild(td);
			tdetail.appendChild(row);
			//document.getElementById("table_cm").appendChild(row);
		}
		
	}

	
	function addTd(row, str){
		var tnode = document.createTextNode(str);
		var td = document.createElement("td");
		td.appendChild(tnode);
		row.appendChild(td);
	}
	
