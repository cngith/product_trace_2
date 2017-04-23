/**
 * 2016-12-22新建
 * author:王忠瑞
 */

var STR_MATERIAL_INVALID="原料名已存在，不可添加";
var STR_MATERIAL_VALID="原料名合法，可以添加";
var STR_MATERIAL_CANNT_EMPTY="原料名不能为空";

/**
 * 添加一条原料信息
 */
function addMaterial() {
	
	validateMaterialByMtName(false);

	if(STR_MATERIAL_VALID == $("#spanMaterialInfo").html()){
		if(confirm('确认添加此原料信息吗?')){
			
			var url = baseURL + "/product_attribute/add_material";
			$.ajax({
				type : 'POST',
				url : url,
				data : "mtName=" + $("#mtName").val(),
				success : function(msg){
					window.location.href = baseURL + "/product_attribute/material_manage"
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
	validateMaterialByMtName(true);
}
/**
 * 验证原料名称是否可以添加，并显示结果
 */
function validateMaterialByMtName(isAsync){
	var mtName = $("#mtName").val();	// 取文本框数据
	var mtie = mtNameIsEmpty(mtName);
	if(mtie){
		$("#spanMaterialInfo").html(STR_MATERIAL_CANNT_EMPTY);
		return ;
	}
	else{ // 不为空
		materialExist(mtName,isAsync);
	}
}

function mtNameIsEmpty(mtName){
	if (undefined == mtName || mtName == null || "" == mtName) {
		// 软提示
		return true;
	}
	else{
		return false;
	}
}


/**
 * 按原料名验证原料名称是否存在
 */
function materialExist(mtName,isAsync) {
	
	var urlGet;
	urlGet = baseURL + "/product_attribute/seek_material_by_mtName?mtName=" + mtName;
	$.ajax({
		type : "GET",
		url : urlGet,
		async : isAsync,
		success : function(data) {
			var reData = eval( "(" + data + ")" );
			if("true" == reData.materialExist){
				// 存在
				$("#spanMaterialInfo").html(STR_MATERIAL_INVALID);
			}
			else{ // 不存在
				$("#spanMaterialInfo").html(STR_MATERIAL_VALID);
			}
		}
	});
}

function cancelClick(){
	window.location.href = baseURL + "/product_attribute/material_manage";
}