/**
 * 2016-12-26新建
 * author:王忠瑞
 */

var STR_MINK_TYPE_INVALID="皮号类型已存在，不可添加";
var STR_MINK_TYPE_VALID="皮号类型合法，可以添加";
var STR_MINK_TYPE_CANNT_EMPTY="皮号类型不能为空";

/**
 * 添加一条皮号类型信息
 */
function addMinkType() {
	
	validateByMinkType(false);

	if(STR_MINK_TYPE_VALID == $("#spanValidateResult").html()){
		if(confirm('确认添加此皮号类型信息吗?')){
			
			var url = baseURL + "/product_attribute/add_mink_type";
			$.ajax({
				type : 'POST',
				url : url,
				data : "minkType=" + $("#minkType").val(),
				success : function(msg){
					window.location.href = baseURL + "/product_attribute/mink_type_manage"
				}
			});
		}
	}
	
}

/**
 * 点击了验证按钮
 * @returns
 */
function validateClick(){
	validateByMinkType(true);
}
/**
 * 验证皮号类型称是否可以添加，并显示结果
 */
function validateByMinkType(isAsync){
	var minkType = $("#minkType").val();	// 取文本框数据
	var mtie = minkTypeIsEmpty(minkType);
	if(mtie){
		$("#spanValidateResult").html(STR_MINK_TYPE_CANNT_EMPTY);
		return ;
	}
	else{ // 不为空
		minkTypeExist(minkType,isAsync);
	}
}


function minkTypeIsEmpty(minkType){
	if (undefined == minkType || minkType == null || "" == minkType) {
		return true;
	}
	else{
		return false;
	}
}


/**
 * 按皮号类型验证皮号类型是否存在
 */
function minkTypeExist(minkType,isAsync) {
	
	var urlGet;
	urlGet = baseURL + "/product_attribute/seek_minkType_by_minkType?minkType=" + minkType;
	$.ajax({
		type : "GET",
		url : urlGet,
		async : isAsync,
		success : function(data) {
			var reData = eval( "(" + data + ")" );
			if("true" == reData.minkTypeExist){
				// 存在,不合法
				$("#spanValidateResult").html(STR_MINK_TYPE_INVALID);
			}
			else{ // 不存在
				$("#spanValidateResult").html(STR_MINK_TYPE_VALID);
			}
		}
	});
}

function cancelClick(){
	window.location.href = baseURL + "/product_attribute/mink_type_manage";
}