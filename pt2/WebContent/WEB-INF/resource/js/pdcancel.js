/**
 * 根据当前正在生产的商品添加调拨单记录
 * 关键控件:
 * 	tr.id: 以本行[条码]值赋值,保证每行id唯一
 * 	
 * 对于"判断当前条码是否处于[发送部门]的生产过程中",
 * 要求前台添加每条明细时校验,同时提交整个表单后,后台完整校验.
 */

	function bodyLoad(){
		bindBarCodeKey();
		
		$("#btnAddOneRow").attr("disabled","disabled")
		
		$("#submitBtn").attr("disabled","disabled");
		
		$("#infoBarCode").css("color","red");
		
		$("#tdetail").css("display","none");
		
		// 当刷新页面时条码会留在条码框,而部门和员工会重置,为防止不一致情况要调用一次barCodeChange()
		barCodeChange(); 
	}
	
	function bindBarCodeKey(){
		$('#barCode').bind('keyup',function(event){ 
			if(event.keyCode == 13){
				barCodePressEnter(); 
			}
			if((event.keyCode>=48 && event.keyCode<=57) 
					|| (event.keyCode>=65 && event.keyCode<=90)
					|| (event.keyCode>=96 && event.keyCode<=105)) { 
				barCodeChange(); 
			}
			    return true;	
			}); 
	}

	/**
	 * 条码框内容变化
	 */
	function barCodeChange(){
		var barCode = $.trim($("#barCode").val());
		// FIXME 当前条码位数为魔数, 未来版本将在页面加载时后台传入
		if(barCode.length < 9 || barCode.length > 15){
			// 条码位数不符, 直接返回.
			$("#btnAddOneRow").attr("disabled","disabled");
			$("#spanPdStatus").html("");
			$("#spanWsEp").html("");
			return;
		}
		// 去后台验证[条码]并返回[发送部门][发送人员]
		$("#btnAddOneRow").removeAttr("disabled");
		$.ajax({
			type : 'GET',
			async : false,
			url : baseURL + "/complete/wfcbybarcode?barCode=" + barCode,
			success : function(backdata){
				barCodeValidate(backdata);
			}
		});
	}

	/**
	 * 根据商品JSON格式数据判断此商品是否可以进行取消操作，
	 * 并填充[发送部门][发送人员]等信息
	 * @param jsonObject: 从服务器返回的JSON数据
	 */
	function barCodeValidate(jsonObject){
		// 商品的状态信息
		var ptsInfo = eval( "(" + jsonObject + ")" );
		var infoBarCode = document.getElementById("infoBarCode");
		if(0 == ptsInfo.length){
			$("#divStatus").css("display","none");
			$("#tDetail").html("");
			alert("服务端没有返回记录.");
			return;
		}
		if(!ptsInfo.hasOwnProperty("pdStatus")){
			alert("返回信息有误.");
			//$("#divStatus").css("display","none");
			return;
		}
		$("#spanPdStatus").html(ptsInfo["pdStatus"]);
		var wsName = (ptsInfo["wsName"] == undefined) ? "" : ptsInfo["wsName"];
		var epName = (ptsInfo["epName"] == undefined) ? "" : ptsInfo["epName"];
		var wsep = (wsName == "" && epName == "") ? "无" : wsName + "." + epName;
		$("#spanWsEp").html(wsep);
		
		$("#divStatus").css("display","");
		if("NONE" == ptsInfo["pdState"]){
			$("#spanWsEp").html("无");
//			$("#tDetail").html("");
		}
		if(ptsInfo["pdState"] == "ACCEPTED"
			|| "WAIT_FOR_ACCEPT" == ptsInfo["pdState"]
			|| "PRODUCT_INFO" == ptsInfo["pdState"]	){
			// 商品已在商品目录或在生产中,可以取消
			
			$("#fromWsName").val(ptsInfo["wsName"]);
			$("#fromEpName").val(ptsInfo["epName"]);
			$("#styleCode").val(ptsInfo["styleCode"]);
			$("#pdName").val(ptsInfo["pdName"]);
			$("#cusCode").val(ptsInfo["cusCode"]);
			$("#billNo").val(ptsInfo["billNo"]);
			$("#btnAddOneRow").removeAttr("disabled");
			infoBarCode.display = "none";
			infoBarCode.innerHTML = "";
		}
		else{
			$("#btnAddOneRow").attr("disabled","disabled");
			infoBarCode.display = "inline";
			infoBarCode.innerHTML = ptsInfo["err"];
		}
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
		if(barCode.val().length < 9 || barCode.val().length > 15){
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
//		var fromWsSel = document.getElementById("fromWs");
//		var fromEpSel = document.getElementById("fromEp");
		var row = document.createElement("tr");
		row.className = "tod";	// 表明此tr为明细
		row.id = barCode;
//		row.fromWs = fromWsSel.value;
//		row.fromEp = fromEpSel.value;
		addTd(row,$("#fromWsName").val());
		addTd(row,$("#fromEpName").val());
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
		if(confirm("是否确定将表格中的商品进行取消生产?")){
			// 取所有商品tr
			var pds = document.getElementsByClassName("tod");
			if(0 >= pds.length){
				return;
			}
			var tds = [];
			for(var i=0;i<pds.length;++i){
				var td={
	//					fromWs:pds[i].fromWs,
	//					fromEp:pds[i].fromEp,
						barCode:pds[i].id
						};	// JSON对象
				tds.push(td);
			}
			$.ajax({
				type : 'POST',
				url : baseURL + "/complete/addcnbycnsjson",
				data : "cns=" + JSON.stringify(tds),
				success : function(backdata){
					getCnByCnsJsonResult(backdata);
				}
			});
		}
	}

	
	function getCnByCnsJsonResult(repStr){
		if("" == repStr){
			alert("完工操作成功");
			$(".inputTag").each(function(){
				$(this).css("display","none");
			});
		}else{
			alert("操作出错.\n错误信息:" + repStr);
//			location.href = baseURL + repStr;
		}
	}
	
