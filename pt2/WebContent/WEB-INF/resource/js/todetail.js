	
function bodyLoad(){
	var tmId = document.getElementById("tmId");
	var xhr;
	if(window.XMLHttpRequest){
		xhr = new XMLHttpRequest();
	}else{
		xhr = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xhr.onreadystatechange = function(){
		if(xhr.readyState == 4){
			if(xhr.status == 200){
				filTDetail(xhr.responseText);
			}
		}
	}
	xhr.open("GET", baseURL + "/transferorder/getodjsonbyid?tmId=" + tmId.innerHTML, true);
	xhr.send();
}
function filTDetail(jsonArray){
	var dL = eval(jsonArray);
	var submit = document.getElementById("submitBtn");
	if(0 == dL.length){
		chooseAll.disabled = true;
		chooseAll.checked = false;
		submit.disabled = true;
		return;
	}
	var row = document.createElement("tr");
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
		if(typeof(dr["toTime"]) == "undefined"){ // 根据是否有接收时间, 决定是否添加复选框
			var echkbox = document.createElement("input");
			echkbox.setAttribute("type","checkbox");  
			echkbox.setAttribute("onclick","chkboxChanged()");  
			echkbox.setAttribute("value",dr["tdId"]);  
			echkbox.setAttribute("class","chkChoose"); 
			echkbox.setAttribute("checked","checked"); 
			echkbox.setAttribute("name","chkTdId"); 	// 后台以此名取控件数据

			td.appendChild(echkbox);
		}
		td.appendChild(document.createTextNode(dr["tdId"]));
		td.align = "right"; // 加入align属性用于设置css, 使第一列右对齐
		row.appendChild(td);
		//addTd(row,dr["tdId"]);
		addTd(row,dr["fromWs.wsName"]);
		addTd(row,dr["fromEp.epName"]);
		addTd(row,dr["toWs.wsName"]);
		addTd(row,dr["toEp.epName"]);
		addTd(row,dr["barCode"]);
		addTd(row,formatDateTime(dr["fromTime"]));
		
		if(typeof(dr["toTime"]) != "undefined"){
			addTd(row,formatDateTime(dr["toTime"]));
		}
		else{
			addTd(row,"");
		}
		document.getElementById("tdetail").appendChild(row);
	}
	
	var chksChoose = document.getElementsByClassName("chkChoose");
	if(typeof(chksChoose) == "undefined" || 0 == chksChoose.length){ // 没有待接收的商品, 用于全选的复选框失效
		chooseAll.disabled = true;
		chooseAll.checked = false;
		submit.disabled = true;
	}else{
		chooseAll.disabled = false;
	}

}

	function formatDateTime(param){
		var index = param.indexOf(".");
		if(-1 == index){ // 没找到"."
			return param;
		}
		return param.slice(0,index);
	}
	
	function addTd(row, str){
		var tnode = document.createTextNode(str);
		var td = document.createElement("td");
		td.appendChild(tnode);
		row.appendChild(td);
	}
	
	function chkboxChanged(){ // 遍历复选框, 若存在选中项则提交按钮可用
		var chksChoose = document.getElementsByClassName("chkChoose");
		for(var i=0; i<chksChoose.length; ++i){
			if(chksChoose[i].checked == true || chksChoose[i].checked == "checked"){
				submitBtn.disabled = false;
				return;
			}
		}
		submitBtn.disabled = true;
	}
	
	function chooseGo(){
		var chksChoose = document.getElementsByClassName("chkChoose");
		var chkAll = document.getElementById("chooseAll");
		for(var i=0; i<chksChoose.length; ++i){
			chksChoose[i].checked = chkAll.checked;
		}
		chkboxChanged();
	}
	
	function submitClick(){
		if(confirm('确定接收选中的商品吗?')){
			// 遍历复选框, 若存在选中项则提交
			var chksChoose = document.getElementsByClassName("chkChoose");
			for(var i=0; i<chksChoose.length; ++i){
				if(chksChoose[i].checked == true || chksChoose[i].checked == "checked"){
					document.getElementById("myForm").action= baseURL + "/transferorder/accepttodbyid"; //设置处理程序
					document.getElementById("myForm").submit(); //提交表单
				}
			}
		}else{
			return false;
		}
	}
