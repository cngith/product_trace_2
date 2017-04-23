/**
 * 根据当前正在生产的商品添加调拨单记录
 * 关键控件:
 * 	tr.id: 以本行[条码]值赋值,保证每行id唯一
 * 	
 * 对于"判断当前条码是否处于[发送部门]的生产过程中",
 * 要求前台添加每条明细时校验,同时提交整个表单后,后台完整校验.
 */

	function bodyLoad(){
		
		getWsJSONList();
		var btnAddOneRow = document.getElementById("btnAddOneRowTod");
		btnAddOneRow.disabled = false;
		
		var infoBarCode = document.getElementById("infoBarCode");
		infoBarCode.style.color = "red";
		
		var tdetail = document.getElementById("tdetail");
		tdetail.style.display = "none";
		
		var submitBtn = document.getElementById("submitBtn");
		submitBtn.disabled = true;
		
		// 当刷新页面时条码会留在条码框,而部门和员工会重置,为防止不一致情况要调用一次barCodeChange()
		barCodeChange(); 
	}
	


	function barCodePressEnter(){
		barCodeChange(); 
		if("disabled" == $("#btnAddOneRowTod").attr("disabled")){
			// 添加按钮不可用
		}else{
			// 可用则添加一行
			$("#barCode").select();
			addOneRowTodBtnClicked();
		}
		
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
	
	function barCodeChange() {
		
		var barCode = document.getElementById("barCode").value;
		// FIXME 当前条码位数为魔数, 未来版本将在页面加载时后台传入
		if(barCode.length != 9){
			// 条码位数不符, 直接返回.
			if(barCode.length == 0){
				$("#infoBarCode").html("");
			}else{
				$("#infoBarCode").html("错误的条码, 不能添加.");
			}
			$("#btnAddOneRowTod").attr("disabled","disabled");
			return;
		}
		// 去后台验证[条码]并返回[发送部门][发送人员]

		$.ajax({
			type : 'GET',
			async : false,
			url : baseURL + "/complete/wftbybarcode?barCode=" + barCode,
//			url : baseURL + "/transferorder/whereispd?barCode=" + barCode,
			success : function (backdata){
				barCodeValidate(backdata);
			},
			error: function(xhr, textStatus, errorThrown) {
				alert('出错啦! ' + ( errorThrown ? errorThrown : xhr.status ));
			}
		});
		
	}
	
	
	/**
	 * 根据商品JSON格式数据判断此商品是否可以进行调拨，
	 * 并填充[发送部门][发送人员]等信息
	 * @param pdJSONStr: 从服务器返回的JSON数据
	 */
	function barCodeValidate(jsonObject){
		var pdJSONStr = jsonObject;
		// 商品的状态信息
		var pdInfo = eval( "(" + pdJSONStr + ")" );
		var pdState = pdInfo["pdState"];
		var fromWsSel = document.getElementById("fromWs");
		var fromEpSel = document.getElementById("fromEp");
		var btnAddOneRow = document.getElementById("btnAddOneRowTod");
		var infoBarCode = document.getElementById("infoBarCode");
		
		if(pdInfo["pdState"] == "ACCEPTED"){
			// 商品已接收，在接收人手中
			fromWsSel.value = pdInfo["wsId"];
			fromWsChanged();
			fromEpSel.value = pdInfo["epId"];
			btnAddOneRow.disabled = false;
			infoBarCode.display = "none";
			infoBarCode.innerHTML = "";
		}
		else{
			btnAddOneRow.disabled = true;
			infoBarCode.display = "inline";
			infoBarCode.innerHTML = pdInfo["err"];
		}
	}
	

	
	/**
	 * "添加一行调拨单明细"按钮点击事件
	 */
	function addOneRowTodBtnClicked(){
		if("disabled" == $("#btnAddOneRowTod").attr("disabled")){
			$("#infoBarCode").html("错误的条码, 不能添加.");
			return false;
		}
		// 判断条码是否为空
		var barCode = $("#barCode");
		(barCode).select();
		// FIXME 当前条码位数为魔数, 未来版本将在页面加载时后台传入
		if(barCode.val().length != 9){
			// 条码位数不符, 直接返回.
			$("#infoBarCode").html("条码位数不符,请输入9位条码.");
			return false;
		}
		
		// 判断发送部门和接收部门是否合法(二者相同就不合法)
		if(fromAndToWsIsValid() == false){
//			$("#soundAlert").play();
			alert("[发送部门]和[接收部门]不能相同.");
			return false;
		}
		// 判断发送人和接收人是否合法(二者相同就不合法)
		if(fromAndToEpIsValid() == false){
			alert("[发送人]和[接收人]不能相同.");
			return false;
		}
		
		addOneRowTod();
	}
	
//	function delOneRowTod(barCode){
//		// TODO 删除已添加的行
//		alert("功能尚未实现");
//		
//	}

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
			return false;
		}else{
			return true;
		}
	}
	
	
	function addOneRowTod(){
		// 判断当前[条码]是否已存在于页面列表中
		var barCode = document.getElementById("barCode").value;
		var bcRow = document.getElementById(barCode);
		if(bcRow){// 
			
			alert("[条码]"+ barCode + "已存在于页面列表中, 不能重复添加.");
			return;
		}
		
		// 判断表格当前是否处于可见状态
		var tdetail = document.getElementById("tdetail");
		if("none" == tdetail.style.display){
			// 表格当前处于不可见状态, 使其可见
			tdetail.style.display="";
		}
		var fromWsSel = document.getElementById("fromWs");
		var fromEpSel = document.getElementById("fromEp");
		var toWsSel = document.getElementById("toWs");
		var toEpSel = document.getElementById("toEp");
		var row = document.createElement("tr");
		row.className = "tod";	// 表明此tr为明细
		row.id = barCode;
		row.fromWs = fromWsSel.value;
		row.fromEp = fromEpSel.value;
		row.toWs = toWsSel.value;
		row.toEp = toEpSel.value;
		addTd(row,fromWsSel.options[fromWsSel.selectedIndex].text);
		addTd(row,fromEpSel.options[fromEpSel.selectedIndex].text);
		addTd(row,toWsSel.options[toWsSel.selectedIndex].text);
		addTd(row,toEpSel.options[toEpSel.selectedIndex].text);
		addTd(row,barCode);
		var td = document.createElement("td");
		td.innerHTML = "<a id='a" + barCode +"' href='#' class='inputTag' >删除</a>";
		row.appendChild(td);
		tdetail.appendChild(row);
		var btnSub = document.getElementById("submitBtn");		btnSub.disabled = false;
		
		// 删除一行代码
		var ab = "#a" + barCode;
		$(ab).click(function(){
		    $(this).parent().parent().remove();
		    if(0 == $(".tod").length){
		    	$("#tdetail").css("display","none");
				$("#submitBtn").attr("disabled","disabled");
		    }
		});
	}
	

	function addTd(row, str){
		var tnode = document.createTextNode(str);
		var td = document.createElement("td");
		td.appendChild(tnode);
		row.appendChild(td);
		return td;
	}
	
	function submitClick(){
		// 得到所有商品tr
		var pds = document.getElementsByClassName("tod");
		if(0 == pds.length){
			return;
		}
		if(confirm('确认发送选中的商品吗?')){
			var tds = [];
			for(var i=0;i<pds.length;++i){
				var td={
						fromWs:pds[i].fromWs,
						fromEp:pds[i].fromEp,
						toWs:pds[i].toWs,
						toEp:pds[i].toEp,
						barCode:pds[i].id
						};	// JSON对象
				tds.push(td);
			}
			var xhr;
			if(window.XMLHttpRequest){
				xhr=new XMLHttpRequest();
				if(xhr.overrideMimetype){
					xml.overrideMimeType("text/xml");
				}
			}else{
				xhr=new ActiveXObject("Microsoft.XMLHTTP");
			}
			xhr.onreadystatechange = function(){
				if(xhr.readyState == 4){
					if(xhr.status == 200){
						getAddToByTodJSONResult(xhr.responseText);
					}
				}
			}
			var uri = baseURL + "/transferorder/addtobytodjson";
			xhr.open("POST", uri, true);
	 		xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
			xhr.send("tods=" + JSON.stringify(tds)); 
		}
	}
	
	function getAddToByTodJSONResult(repStr){
		if("" == repStr){
			// 添加成功
			alert("添加成功");
			$(".inputTag").each(function(){
				$(this).css("display","none");
			});
//			$("#spanMsg").html("");
		}else{
			alert("添加出错.\n错误信息:" + repStr);
//			location.href = baseURL + repStr;
		}
	}
