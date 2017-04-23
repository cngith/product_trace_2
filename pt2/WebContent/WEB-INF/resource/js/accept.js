/**
 * 接收商品
 * 关键控件:
 * 	tr.id: 以本行[条码]值赋值,保证每行id唯一
 * 	
 * 对于"判断当前条码是否处于[发送部门]的生产过程中",
 * 要求前台添加每条明细时校验,同时提交整个表单后,后台完整校验.
 */

	function bodyLoad(){
		getWsJSONList();
		var div = document.getElementById("btnDiv");
		div.style.display="none";
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
					var toWsSel = document.getElementById("toWs");
					var toEpSel = document.getElementById("toEp");
					var wsJSONList = xhr.responseText;
					addWsOption(toWsSel, wsJSONList);
					wsChanged(toWsSel, toEpSel);
					
				}
			}
		}
		// TODO 同步方式提交
		xhr.open("GET", baseURL + "/workshop/getwsjsonlist", false);
		xhr.send();
	}
	
	function toWsChanged(){
		var toWsSel = document.getElementById("toWs");
		var toEpSel = document.getElementById("toEp");
		wsChanged(toWsSel, toEpSel);
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
		xmlhttp.open("GET", baseURL + "/employee/getepbywsid?wsId=" + value, true);
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
	
	function barCodeKeyPress(){
		getWfadByBarCode();
	}
	
	/**
	 * 根据条码取待接收商品明细
	 */
	function getWfadByBarCode(){
		var barCode = document.getElementById("barCode").value;
		// FIXME 当前条码位数为魔数, 未来版本将在页面加载时后台传入
		if(barCode.length != 9){
			// 条码位数不符, 直接返回.
			var spanMsg = document.getElementById("message");
			spanMsg.innerHTML="条码位数不符,请输入9位条码.";
			return;
		}
		// 去后台验证[条码]并返回[发送部门][发送人员]
		
		var xhr;
		if(window.XMLHttpRequest){
			xhr = new XMLHttpRequest();
		}else{
			xhr = new ActiveXObject("Microsoft.XMLHTTP");
		}
		xhr.onreadystatechange = function(){
			if(xhr.readyState == 4){
				if(xhr.status == 200){
					// 判断返回字符串中是否有可接收的商品明细信息...
					var jsonArray = invalidWfaTodJson(xhr.responseText);
					  
					if("undefined" != typeof(jsonArray) && undefined != jsonArray
							  && null != jsonArray){
						filWaitForAcceptDetail(jsonArray);
					}
				}
			}
		}
		xhr.open("GET", baseURL + "/transferorder/getwfatodbybarcode?barCode=" + barCode, true);
		xhr.send();
	}
	

	/**
	 * 客户代码框中按下回车
	 */
	function cusCodeKeyPress(){
		getWfadByCusCode();
	}
	
	/**
	 * 根据客户代码取待接收商品明细
	 */
	function getWfadByCusCode(){
		var cusCode = $("#cusCode").val();
		// FIXME 当前客户代码位数为魔数, 未来版本将在页面加载时后台传入
		if(cusCode.length != 3){
			// 客户代码位数不符, 直接返回.
			var spanMsg = $("#message");
			spanMsg.innerHTML="客户代码位数不符,请输入3位客户代码.";
			return;
		}
		// 去后台验证[条码]并返回[发送部门][发送人员]
		var getUrl = baseURL + "/transferorder/getwfatodbycuscode?cusCode=" + cusCode
		$.get(getUrl, null, wfaBackDataPutInTable);
	}
	

	/**
	 * 品名框中按下回车
	 */
	function pdNameKeyPress(){
		getWfadByPdName();
	}
	
	/**
	 * 根据品名取待接收商品明细
	 */
	function getWfadByPdName(){
		var pdName = $("#pdName").val();
		if(pdName.length == 0 || pdName.length >= 30){
			// 品名位数不符, 直接返回.
			var spanMsg = $("#message");
			spanMsg.innerHTML="品名长度不符,请重新输入.";
			return;
		}
		// 去后台验证[条码]并返回[发送部门][发送人员]
		var getUrl = baseURL + "/transferorder/getwfatodbypdname?pdName=" + pdName
		$.get(getUrl, null, wfaBackDataPutInTable);
	}
	
	function wfaBackDataPutInTable(backData){
		// 判断返回字符串中是否有可接收的商品明细信息...
		var jsonArray = invalidWfaTodJson(backData);
		  
		if("undefined" != typeof(jsonArray) && undefined != jsonArray
				  && null != jsonArray){
			filWaitForAcceptDetail(jsonArray);
		}
	}
	
	function chooseAllClick(){
		var chksChoose = document.getElementsByClassName("chkChoose");
		var chkAll = document.getElementById("chooseAll");
		for(var i=0; i<chksChoose.length; ++i){
			chksChoose[i].checked = chkAll.checked;
		}
		chkboxChanged();
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
	
	function btnQClick(){
		getWfadByToWsOrEp();
	}
	
	/**
	 * 根据接收部门和员工查询待接收商品
	 * Wfad=WaitForAcceptDetail
	 */
	function getWfadByToWsOrEp(){
		var wsOp = document.getElementById("toWs");
		var index = wsOp.selectedIndex;
		var wsValue = wsOp.options[index].value;
		var epOp = document.getElementById("toEp");
		index = epOp.selectedIndex;
		var epValue = epOp.options[index].value;
		var xmlhttp;
		if (window.XMLHttpRequest)
		{// code for IE7+, Firefox, Chrome, Opera, Safari
		  xmlhttp=new XMLHttpRequest();
		  }
		else
		  {// code for IE6, IE5
		  	xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		  }
		xmlhttp.onreadystatechange = function filToEp()
		  {
			  if (xmlhttp.readyState == 4 )
			  { // 4: 请求已完成，且响应已就绪
				  if(xmlhttp.status == 200){
					  // 200: "OK"
					  var jsonArray = invalidWfaTodJson(xmlhttp.responseText);
					  
					  if("undefined" != typeof(jsonArray) && undefined != jsonArray
							  && null != jsonArray){
						  filWaitForAcceptDetail(jsonArray);
					  }
					  
				  }
			    }
			  }
		var uri = baseURL + "/transferorder/";
		if("undefined" == typeof(epValue) || 0 == epValue){
			// 没选接收人, 按接收部门查
			uri += "getwfatodbytowsid?toWsId=" + wsValue;
		}else{
			uri += "getwfatodbytoepid?toEpId=" + epValue;
		}
		xmlhttp.open("GET", uri, true);
		xmlhttp.send();
	}
	
	/**
	 * 校验Json格式的Tod数据, 在页面上显示校验信息
	 * @param todJson : 待校验的Json("JSONArray")格式的Tod数据
	 * @returns : 
	 */
	function invalidWfaTodJson(todJson){
		var divCA = document.getElementById("btnDiv");
		var spanMsg = document.getElementById("message");
		// 清空表格
		var tdetail = document.getElementById("tdetail");
		tdetail.innerHTML = "";
		var jsonArray = {};
		var dL = eval("(" + todJson + ")");
		if(dL == ""){// 没取到数据
			divCA.style.display="none";
			spanMsg.innerHTML="没有找到符合条件的记录.";
			return null;
			
		}else { // 传入了正常JSONArray的数据
			if(dL instanceof Array){ // 是数组
				if("undefined" == typeof(dL) || 0 == dL.length){
					// 没有找到明细
					divCA.style.display="none";
					spanMsg.innerHTML="没有找到符合条件的记录.";
					return null;
				}else if(1 == dL.length){ // 只有一条记录
					//alert(dL[0]["err"]);
					if(undefined == dL[0]["err"] || "" == todJson["err"]){
						// 如果没有出错
						divCA.style.display="";
						spanMsg.innerHTML="";
						return dL;
					}else if("" != dL[0]["err"]){
						// 出错了
						divCA.style.display="none";
						spanMsg.innerHTML= dL[0]["err"];
						return null;
					}
				}else if(1 < dL.length){ // 多条记录
					divCA.style.display="";
					spanMsg.innerHTML="";
					return dL;
				
				}
			}else{
				divCA.style.display="";
				spanMsg.innerHTML="返回了未知的数据,不能显示.";
				return dL;
			}
		}
	}
	
	/**
	 * 将可接收调拨明细填入表格中
	 * jsonArray : 已装配好的JSON对象数组
	 */
	function filWaitForAcceptDetail(jsonArray){
		$("#resultDiv").attr("disabled",""); // 设置显示区为有效
		var dL = jsonArray;
				
		var row = document.createElement("tr");
		addTd(row,"选择");
		addTd(row,"调出部门");
		addTd(row,"发送人");
		addTd(row,"调入部门");
		addTd(row,"接收人");
		addTd(row,"商品条码");
		addTd(row,"款号");
		addTd(row,"颜色");
		addTd(row,"类别");
		addTd(row,"单号");
		addTd(row,"发送时间");
		document.getElementById("tdetail").appendChild(row);
		
		for(var i=0;i<dL.length;i++){ //循环将其中每个对象的属性取出
			row = document.createElement("tr");
			var dr = dL[i];
			var td = document.createElement("td");
			if(typeof(dr["toTime"]) == "undefined"){ // 根据是否有接收时间, 决定是否添加复选框
				var echkbox = document.createElement("input");
				echkbox.setAttribute("type","checkbox");  
				echkbox.setAttribute("onclick","chkboxChanged()");  
				echkbox.setAttribute("tdId",dr["tdId"]);
				echkbox.setAttribute("class","chkChoose");  
				echkbox.setAttribute("checked","checked"); 

				td.appendChild(echkbox);
			}
			row.appendChild(td);
			addTd(row,dr["fromWs.wsName"]);
			addTd(row,dr["fromEp.epName"]);
			addTd(row,dr["toWs.wsName"]);
			addTd(row,dr["toEp.epName"]);
			addTd(row,dr["barCode"]);
			addTd(row,dr["styleCode"]);
			addTd(row,dr["pdName"]);
			addTd(row,dr["cusCode"]);	
			addTd(row,dr["billNo"]);	
			addTd(row,dr["fromTime"]);
			
			/* if(typeof(dr["toTime"]) == "undefined"){
				addTd(row,"");
			}
			else{
				addTd(row,formatDateTime(dr["toTime"]));
			} */
			tdetail.appendChild(row);
		}
		
		var chksChoose = document.getElementsByClassName("chkChoose");
		if(typeof(chksChoose) == "undefined"){ // 没有待接收的商品, 用于全选的复选框失效
			chooseAll.disabled = true;
		}else{
			chooseAll.disabled = false;
			chooseAll.checked = true;
		}
	
	}
	

	function addTd(row, str){
		var tnode = document.createTextNode(str);
		var td = document.createElement("td");
		td.appendChild(tnode);
		row.appendChild(td);
	}
	
	function acceptBtnClick(){
		// 得到所有选中的checkbox
		var ckds = $(".chkChoose");
		if(0 == ckds.length){
			return;
		}
		if(confirm('确认接收选中的商品吗?')){
			var tds = [];
			for(var i=0; i<ckds.length; ++i){
				if(ckds[i].checked){
					var td={tdId:ckds[i].getAttribute("tdId")};	// 构造JSON对象
					tds.push(td);
				}
			}
		
			var url = baseURL + "/transferorder/accepttodbyids";
			$.ajax({
				type : 'POST',
				url : url,
				data : "tods=" + JSON.stringify(tds),
				success : function(data){
					backdataAcceptTod(data);
					},
				error : function(data, textStatus, jqXHR){
					alert(data);
					alert(textStatus);
					alert(jqXHR);
					}
			});
		}
	}
	
	/**
	 * 提交返回后, 页面处理函数
	 */
	function backdataAcceptTod(data){
//		var errJson = $.parseJSON(data);
//		var errMsg = errJson["err"];
		var errMsg = data;
		if('' == errMsg){ // 接收成功
			alert("接收成功");
			$(".inputTag").each(function(){
				$(this).css("display","none");
			});
			$("#tdetail").attr("disabled","disabled");
		}else{
			var spanMsg = $("#message");
			spanMsg.innerHTML="服务器出错, 接收商品失败.\n错误信息:" + errMsg;
		}
	}