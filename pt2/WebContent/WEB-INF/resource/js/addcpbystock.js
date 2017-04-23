/**
 * 根据当前正在生产的商品添加完工单记录
 * 关键控件:
 * 	tr.id: 以本行[条码]值赋值,保证每行id唯一
 * 	
 * 对于"判断当前条码是否处于[发送部门]的生产过程中",
 * 要求前台添加每条明细时校验,同时提交整个表单后,后台完整校验.
 */

	function bodyLoad(){
		
		getWsJSONList();
		$("#btnAddOneRow").attr("disabled","disabled");
		
		var infoBarCode = document.getElementById("infoBarCode");
		infoBarCode.style.color = "red";
		
		var tdetail = document.getElementById("tdetail");
		tdetail.style.display = "none";
		
		var submitBtn = document.getElementById("submitBtn");
		submitBtn.disabled = true;
		
		barCodeChange();
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
				}
			}
		}
		// TODO 同步方式提交
		xhr.open("GET", baseURL + "/workshop/getwsjsonlist", false);
		xhr.send();
	}
	
	
	/**
	 * 条码框内容变化
	 */
	function barCodeChange(){
		var barCode = $("#barCode").val();
		// FIXME 当前条码位数为魔数, 未来版本将在页面加载时后台传入
		if(barCode.length != 9){
			// 条码位数不符, 直接返回.
			$("#btnAddOneRow").attr("disabled","disabled");
			return;
		}
		// 去后台验证[条码]并返回[发送部门][发送人员]
		$("#btnAddOneRow").removeAttr("disabled");
		$.ajax({
			type : 'GET',
			async : false,
			url : baseURL + "/complete/wffbybarcode?barCode=" + barCode,
			success : function(backdata){
				barCodeValidate(backdata);
			}
		});
	}

	/**
	 * 根据商品JSON格式数据判断此商品是否可以进行完工操作，
	 * 并填充[发送部门][发送人员]等信息
	 * @param jsonObject: 从服务器返回的JSON数据
	 */
	function barCodeValidate(jsonObject){
		var pdJSONStr = jsonObject;
		// 商品的状态信息
		var pdInfo = eval( "(" + pdJSONStr + ")" );
		var pdState = pdInfo["pdState"];
		var fromEpSel = document.getElementById("fromEp");
		var infoBarCode = document.getElementById("infoBarCode");
		
		if(pdInfo["pdState"] == "ACCEPTED"){
			// 商品已接收，在接收人手中,可以完工
			$("#fromWs").val(pdInfo["wsId"]);
//			alert($("#fromWs").get(0).selectedIndex);
			fromWsChanged();
			fromEpSel.value = pdInfo["epId"];
			$("#styleCode").val(pdInfo["styleCode"]);
			$("#pdName").val(pdInfo["pdName"]);
			$("#cusCode").val(pdInfo["cusCode"]);
			$("#billNo").val(pdInfo["billNo"]);
			$("#btnAddOneRow").removeAttr("disabled");
			infoBarCode.display = "none";
			infoBarCode.innerHTML = "";
		}
		else{
			$("#btnAddOneRow").attr("disabled","disabled");
			infoBarCode.display = "inline";
			infoBarCode.innerHTML = pdInfo["err"];
		}
	}
	
	function fromWsChanged(){
		var fromWsSel = document.getElementById("fromWs");
		var fromEpSel = document.getElementById("fromEp");
		wsChanged(fromWsSel, fromEpSel);
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
	 * 条码框中回车事件
	 */
	function barCodePressEnter(){
		$("#barCode").select();
		addOneRowBtnClicked();
	}
	
	
	/**
	 * "添加一行调拨单明细"按钮点击事件
	 */
	function addOneRowBtnClicked(){
		if("disabled" == $("#btnAddOneRowTod").attr("disabled")){
			$("#spanMsg").html("错误的条码, 不能添加."); // 设置spanMsg的内容
			return false;
		}
		var barCode = $("#barCode");
		(barCode).select();
		// FIXME 当前条码位数为魔数, 未来版本将在页面加载时后台传入
		if(barCode.val().length != 9){
			// 条码位数不符, 直接返回.
			$("#spanMsg").html("条码位数不符,请输入9位条码.");
			return false;
		}
		
		addOneRowTod();
	}

	

	function addOneRowTod(){
		// 判断当前[条码]是否已存在于页面列表中
		var barCode = $("#barCode").val();
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
		var row = document.createElement("tr");
		row.className = "tod";	// 表明此tr为明细
		row.id = barCode;
		row.fromWs = fromWsSel.value;
		row.fromEp = fromEpSel.value;
		addTd(row,fromWsSel.options[fromWsSel.selectedIndex].text);
		addTd(row,fromEpSel.options[fromEpSel.selectedIndex].text);
		addTd(row,barCode);
		addTd(row,$("#styleCode").val());
		addTd(row,$("#pdName").val());
		addTd(row,$("#cusCode").val());
		addTd(row,$("#billNo").val());
//		row.styleCode = $("#styleCode").val();
//		row.pdName = $("#pdName").val();
//		row.cusCode = $("#cusCode").val();
//		row.billNo = $("#billNo").val();		
		var td = document.createElement("td");
		td.innerHTML = "<a id='a" + barCode +"' href='#' class='inputTag' >删除</a>";
		row.appendChild(td);
		tdetail.appendChild(row);
		var btnSub = document.getElementById("submitBtn");
		btnSub.disabled = false;
		
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
		if(confirm("是否确定将以下商品进行完工操作?")){
			// 取所有商品tr
			var pds = document.getElementsByClassName("tod");
			if(0 >= pds.length){
				return;
			}
			var tds = [];
			for(var i=0;i<pds.length;++i){
				var td={
						fromWs:pds[i].fromWs,
						fromEp:pds[i].fromEp,
						barCode:pds[i].id
				};	// JSON对象
				tds.push(td);
			}
			$.ajax({
				type : 'POST',
				url : baseURL + "/complete/addcpbycpsjson",
				data : "cpds=" + JSON.stringify(tds),
				success : function(backdata){
					getCpByCpJsonResult(backdata);
				}
			});
		}
	}

	
	function getCpByCpJsonResult(repStr){
		if("" == repStr){
			alert("完工操作成功");
			$(".inputTag").each(function(){
				$(this).css("display","none");
			});
//			$("#spanMsg").html("");
		}else{
			alert("操作出错.\n错误信息:" + repStr);
//			location.href = baseURL + repStr;
		}
	}
	
