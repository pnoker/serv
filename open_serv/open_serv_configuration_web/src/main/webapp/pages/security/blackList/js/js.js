var rowCallBack = function(row, data, iDisplayIndex) {
};

var columsAdded = [];

var columnParams = [ {
	data : 'id'
}, {
	data : 'ipAddress'
}, {
	data : 'banReason'
}, {
	data : 'updatetime'
} ];

var contentUrl = serviceHost+"/blacklistmanage/v1/queryblacklist";

var cellCallBack = [ {
	"render" : function(data, type, row) {
		return "<a href='javascript:void()' onclick='deleteBlackList("+row.id+")' class='add-row-btn'>删除</a>";
	},
	"targets" : 4}
];

// 重置弹出框信息
var initModal = function() {
	$("#id").val(0);
	$("input[name='ipAddress']").val("");
	$("#banReason").val("");
	$(".modal-title").html("新增黑名单");
}

var addNew = function() {
	initModal();
}
// 编辑弹出窗口
var openEdit = function(id) {
	$("#id").val(id);
	$.ajaxExtend({
		url : serviceHost+"/blacklistmanage/v1/queryblacklist",
		async : false,
		type : "get",
		dataType : "json",
		data : {
			id : id
		},
		success : function(obj) {
			var data = eval(obj).datas[0];

			if (data != null) {
				$("input[name='ipAddress']").val(data.ipAddress);
				$("#banReason").val(data.banReason);
				$(".modal-title").html("编辑黑名单");
			}
		}
	});
};

//保存黑名单
var submitBlackList = function() {
	// 验证表达，成功才执行ajax请求
	if (!$("#dataForm").validationEngine("validate")) {
		return;
	}
	var id = $("#id").val();
	var postUrl = serviceHost+"/blacklistmanage/v1/createblacklist";

	$.ajaxExtend({
		url : postUrl,
		async : false,
		type : "post",
		dataType : "json",
		data : $("#dataForm").serialize(),
		success : function(obj) {
			var data = eval(obj);
			if (data.resultCode == "0") {
				layer.msg("新增黑名单成功，稍后生效");
				initQuery();
				$("#blackModal").modal('hide');
			} else {
				layer.msg(data.resultInfo);
			}
		}
	});
};

// 删除数据库
var deleteBlackList = function(id) {
	if (!confirm("是否删除黑名单")) {
		return;
	}
	$.ajaxExtend({
		url : serviceHost+"/blacklistmanage/v1/removeblacklist",
		async : false,
		type : "post",
		dataType : "json",
		data : {
			blackListId : id
		},
		success : function(obj) {
			var data = eval(obj);
			if (data.resultCode == "0") {
				layer.msg("删除黑名单成功");
				initQuery();
			} else {
				layer.msg(data.resultInfo);
			}
		}
	});
};

var tableObj = new queryTableObj("blackList", contentUrl, "", columnParams,
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
