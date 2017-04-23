/**
 * 
 */

	function bodyLoad(){
		var tmId = document.getElementById("tmId").innerHTML;
		if(undefined == tmId || "" == tmId){
			document.getElementById("message").innerText = "没有查询到数据。";
			return;
		}
		var xhr;
		if(window.XMLHttpRequest){
			xhr = new XMLHttpRequest();
		}else{
			xhr = new ActiveXObject("Microsoft.XMLHTTP");
		}
		xhr.onreadystatechange = function putWsOption(){
			if(xhr.readyState == 4){
				if(xhr.status == 200){
					getDetail(xhr.responseText);
				}
			}
		}
		xhr.open("GET", baseURL + "/transferorder/getodjsonbyid?tmId=" + tmId, true);
		xhr.send();
	}
	
	function getDetail(jsonArray){
		
		document.getElementById("tdetail").innerHTML = "";
		document.getElementById("message").innerText = "";
		var dL = eval("(" + jsonArray + ")");
		var row = document.createElement("tr");
		//addTd(row,"选择");
		addTd(row,"序号");
		addTd(row,"调出部门");
		addTd(row,"发送人");
		addTd(row,"调入部门");
		addTd(row,"接收人");
		addTd(row,"商品条码");
		addTd(row,"发送时间");
		addTd(row,"接收时间");
		document.getElementById("tdetail").appendChild(row);
		for(var i=0;i<dL.length;i++){ //循环将其中每个对象的属性取出
			row = document.createElement("tr");
			var dr = dL[i];
			var td = document.createElement("td");
			
			addTd(row,dr["tdId"]);
			addTd(row,dr["fromWs.wsName"]);
			addTd(row,dr["fromEp.epName"]);
			addTd(row,dr["toWs.wsName"]);
			addTd(row,dr["toEp.epName"]);
			addTd(row,dr["barCode"]);
			addTd(row,dr["fromTime"]);
			
			if(typeof(dr["toTime"]) != "undefined"){
				addTd(row,formatDateTime(dr["toTime"]));
			}
			else{
				addTd(row,"");
			}
			document.getElementById("tdetail").appendChild(row);
		}
		
	}

	
	function addTd(row, str){
		var tnode = document.createTextNode(str);
		var td = document.createElement("td");
		td.appendChild(tnode);
		row.appendChild(td);
	}
	
	
	