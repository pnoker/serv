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
	data : 'sourceClass'
}, {
	data : 'updatetime'
} ];

var contentUrl = serviceHost+"/datasource/v1/query";

var cellCallBack = [
		{
			"render" : function(data, type, row) {
				var html = "";
				if (row.isDeleted == "0") {
					if (row.connectionStatus == 1) {
						return "连接正常";
					} else {
						return "<b>连接失败</b>";
					}
				} else {
					return "";
				}

			},
			"targets" : 3
		},
		{
			"render" : function(data, type, row) {
				var html = "";
				if (row.isDeleted == "0") {
					return "<b>正常</b>";
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
					html += "<a href='javascript:void()' onclick='deleteSource("
							+ row.id + ")' class='add-row-btn' >删除</a>";
				}
				return html;
			},
			"targets" : 7
		} ];

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
}
// 编辑弹出窗口
var openEdit = function(id) {
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
				alert((id > 0 ? "编辑" : "新增") + "数据源成功,下次重启时生效");
				initQuery();
				$("#dataSourceModal").modal('hide');
			} else {
				alert(data.resultInfo);
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
				alert("删除数据源成功,下次重启时生效");
				initQuery();
			} else {
				alert(data.resultInfo);
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
};



$(function() {
	initQuery();
	showLeftMenu();
});
