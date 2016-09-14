var rowCallBack = function(row, data, iDisplayIndex) {
};

var columsAdded = [];

var columnParams = [ {
	data : 'id'
}, {
	data : 'sourceName'
}, {
	data : 'sourceUser'
}, {
	data : 'connectionStatus'
}, {
	data : 'isDeleted'
}, {
	data : 'sourceUrl'
}, {
	data : 'updatetime'
} ];

var contentUrl = serviceHost+"/datasource/v1/query";

//列表td 数据回调
var cellCallBack = [
		{
			"render" : function(data, type, row) {
				var html = "";
				if (data == "1"&& row.isDeleted=='0') {
						return "<b>成功</b>&nbsp";
				}else if(row.isDeleted=='1'){
					return "";
				}else if(data=="0"){
					return "<b>正在初始化</b>&nbsp";
				}else {
					return "失败&nbsp;&nbsp;<a href='javascript:checkStatus("+row.id+")'  class='add-row-btn'><font color='#279af0'>手动加载</font></a>";
				}

			},
			"targets" : 3
		},
		{
			"render" : function(data, type, row) {
				var html = "";
				if (row.isDeleted == "0") {
					return "<b>已启用</b>";
				} else {
					return "已删除";
				}
			},
			"targets" : 4
		},
		{
			"render" : function(data, type, row) {
				var html = "";
				if (row.isDeleted == "0") {
					html += "<a href='javascript:void()' onclick='openEdit("
							+ row.id
							+ ")' class='add-row-btn' data-backdrop='static' data-toggle='modal' data-target='#dataSourceModal'>编辑</a>";
					html += "&nbsp;"
					html += "<a href='javascript:deleteSource("+ row.id + ")' class='add-row-btn' >删除</a>";
				}
				return html;
			},
			"targets" : 7
		}
	];

// 重置弹出框信息
var initModal = function() {
	$("#id").val(0);
	$("#sourceName").val("");
	$("#sourceType").val("");
	$("#sourceUrl").val("");
	$("#sourceUser").val("");
	$("#sourcePass").val("");
	$("#sourceRemark").val("");
	$("#isDeleted").val(0);
	$(".modal-title").html("新增数据源");
}

var addNew = function() {
	initModal();
	$("#reloadBtn").hide();
	$("#resetBtn").show();
};

//刷新数据
var reloadData=function(){
	openEdit($("#id").val());
}
// 编辑弹出窗口
var openEdit = function(id) {
	$("#resetBtn").hide();
	$("#reloadBtn").show();
	$("#id").val(id);
	$.ajaxExtend({
		url : serviceHost+"/datasource/v1/query",
		async : false,
		type : "get",
		dataType : "json",
		data : {
			id : id
		},
		success : function(obj) {
			var data = eval(obj).datas[0];
			console.log(data);
			if (data != null) {
				$("#sourceName").val(data.sourceName);
				$("#sourceType").val(data.sourceType);
				$("#sourceUrl").val(data.sourceUrl);
				$("#sourceUser").val(data.sourceUser);
				$("#sourcePass").val(data.sourcePass);
				$("#sourceRemark").val(data.sourceRemark);
				$("#isDeleted").val(data.isDeleted);
				$(".modal-title").html("编辑数据源：" + data.sourceName);
			}
		}
	});
};

//数据源保存提交
var submitSourceData = function() {
	// 验证表达，成功才执行ajax请求
	if (!$("#dismissEventForm").validationEngine("validate")) {
		return;
	}
	var id = $("#id").val();
	var postUrl = serviceHost+"/datasource/v1/create";
	if (id > 0) {
		postUrl = serviceHost+"/datasource/v1/modify";
	}
 

	$.ajaxExtend({
		url : postUrl,
		async : false,
		type : "post",
		dataType : "json",
		data : $("#dismissEventForm").serialize(),
		success : function(obj) {
			var data = eval(obj);
			if (data.resultCode == "0") {
				layer.msg((id > 0 ? "编辑" : "新增") + "数据源成功,稍后生效");
				initQuery();
				$("#dataSourceModal").modal('hide');
			} else {
				layer.msg(data.resultInfo);
			}
		}
	});
};

// 删除数据库
var deleteSource = function(sourceId) {
	if (!confirm("是否删除数据源")) {
		return;
	}
	$.ajaxExtend({
		url : serviceHost+"/datasource/v1/remove",
		async : false,
		type : "post",
		dataType : "json",
		data : {
			id : sourceId
		},
		success : function(obj) {
			var data = eval(obj);
			if (data.resultCode == "0") {
				layer.msg("删除数据源成功,稍后生效");
				initQuery();
			} else {
				layer.msg(data.resultInfo);
			}
		}
	});
};

//选择数据源类型后，添加地址指引
var loadUrlGuide=function(type){
	var id =$("#id").val();

	if(id > 0 && $("#sourceUrl").val()!=""){
		return;
	}
	if(type=="1"){
		url='jdbc:oracle:thin:@{IP:PORT}:{SCHEMA}';
	}else if(type=="2"){
		url ='jdbc:sqlserver://{IP}\{SCHEMA}; DatabaseName={dataBaseName}';
	}else if(type=="3"){
		url='jdbc:mysql://{IP:PORT}/{SCHEMA}?useUnicode=true&characterEncoding=utf-8'
	}
	$("#sourceUrl").val(url);
};
 
//检查失败数据源状态
var checkStatus=function(id){
	$.ajaxExtend({
		url : serviceHost+"/datasource/v1/recheck",
		async : false,
		type : "post",
		dataType : "json",
		data : {
			dataSourceId : id
		},
		success : function(obj) {
			var data = eval(obj);
			if (data.resultCode == "0") {
				layer.msg("数据源启用成功");
				initQuery();
			} else {
				layer.msg(data.resultInfo);
			}
		}
	});
};

var tableObj = new queryTableObj("tableList", contentUrl, "", columnParams,
		cellCallBack, 'get', 'json', rowCallBack,{});
// 初始化查询
var initQuery = function() {
	Table.refresh();
	Table.tableQuery(tableObj);
}

$(function() {
	initQuery();
	hideLeftMenu();
})
