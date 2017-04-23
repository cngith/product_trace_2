/**
 * 
 */

	function bodyLoad(){
		var cmId = document.getElementById("cmId").innerHTML;
		if(undefined == cmId || "" == cmId){
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
		xhr.open("GET", baseURL + "/complete/getcpdjsonbyid?cmId=" + cmId, true);
		xhr.send();
	}
	
	function getDetail(jsonArray){
		
		document.getElementById("tdetail").innerHTML = "";
		document.getElementById("message").innerText = "";
		var dL = eval("(" + jsonArray + ")");
		var row = document.createElement("tr");
		//addTd(row,"选择");
		addTd(row,"序号");
		addTd(row,"所在部门");
		addTd(row,"收管人");
		addTd(row,"商品条码");
		document.getElementById("tdetail").appendChild(row);
		for(var i=0;i<dL.length;i++){ //循环将其中每个对象的属性取出
			row = document.createElement("tr");
			var dr = dL[i];
			var td = document.createElement("td");
			
			addTd(row,dr["cdId"]);
			addTd(row,dr["fromWsName"]);
			addTd(row,dr["fromEpName"]);
			addTd(row,dr["barCode"]);
			document.getElementById("tdetail").appendChild(row);
		}
		
	}

	
	function addTd(row, str){
		var tnode = document.createTextNode(str);
		var td = document.createElement("td");
		td.appendChild(tnode);
		row.appendChild(td);
	}
	
	
	