//快捷回车查询类型 1:首页列表 2:授权页面列表，其他不处理
var quickBtnType=1; 

var rowCallBack = function(row, data, iDisplayIndex) {};
//日志统计标志
var statis = getQueryString("s_type")=="statis";
var columsAdded = [];

var columnParams = [ {
	data : 'id'
}, {
	data : 'userName'
}, {
	data : 'userChannelName'
}, {
	data : 'appKey'
}, {
	data : 'createTime'
} ];

var contentUrl = serviceHost + "/usermanage/v1/queryuser";
//日志统计的时候路径
if(statis){
    contentUrl = serviceHost + "/log/v1/queryuser";
}

var cellCallBack = [ {
	"render" : function(data, type, row) {
	    //统计时操作按钮
	    if(statis){
	        return "<a href='javascript:statisUser(\""
            + row.userName + "\")' class='add-row-btn'>统计</a>&nbsp;";
	    }else{ 
    		var html = "<a href='javascript:void()' onclick='deleteUser(" + row.id + ",\""
    				+ row.appKey + "\")' class='add-row-btn'>删除</a>&nbsp;";
    
    		html += "<a href='javascript:void()' onclick='openEdit("
    				+ row.id
    				+ ")' class='add-row-btn' data-backdrop='static' data-toggle='modal' data-target='#userModal'>编辑</a>&nbsp;";
    
    		html += "<a href='javascript:void()' onclick='initModalAuthority("
    				+ row.id
    				+ ",\""
    				+ row.appKey
    				+ "\")' class='add-row-btn' data-backdrop='static' data-toggle='modal' data-target='#authorityModal'>授权</a>";
    		return html;
	    }
	},
	"targets" : 5
} ];

//全局变量
var viewPermission = "";
var accessPermission = "";
var userId = "";
var appKey = "";

// 重置弹出框信息
var initModal = function() {
	quickBtnType = -1;
	$(".modal-title").html("新增用户");
	$("#id").val("0");
	$("#userNameEdit").removeAttr("readonly");
	$("#userNameEdit").val("");
	$("#nickName").val("");
	$("#userChannel").val(1);
	$("#appKey").val("");
	$("#userMobile").val("");
	$("#userEmail").val("");
	$("#userRemark").val("");
	$("#userPass").val("");
	$("#rePassword").val("");
}
var statisUser = function(userName){
	gotoDiv('pannelStatisUserChart');
    var sUrl="./pages/logs/loguser.html?userName="+userName;
    $("#digitalChinaCurrentUrl").val(sUrl); 
    $("#pannelStatisUserChart").load(sUrl)
};

var addNew = function() {
	initModal();
	//隐藏appkey
	$("#appKeyDiv").hide();
	
	$("#reloadBtn").hide();
	$("#resetBtn").show();
};
//刷新数据
var reloadData=function(){
	openEdit($("#id").val());
};

// 编辑弹出窗口
var openEdit = function(id) {
	initModal();
	//切换清除重置按钮	
	$("#resetBtn").hide();
	$("#reloadBtn").show();
	
	//绑定弹出页面列表回车
	
	$("#userListQueryBtn").click();
	//编辑时可以修改appkey
	$("#appKeyDiv").show();
	$("#id").val(id);
	$.ajaxExtend({
		url : serviceHost + "/usermanage/v1/queryuser",
		async : false,
		type : "get",
		dataType : "json",
		data : {
			id : id
		},
		success : function(obj) {
			var data = eval(obj).datas[0];
			if (data != null) {
				$(".modal-title").html("编辑用户：" + (data.nickName==undefined?data.userName:data.nickName));
				$("#userNameEdit").attr("readonly", "true");
				$("#userNameEdit").val(data.userName);
				$("#nickName").val(data.nickName);
				$("#userChannel").val(data.userChannel);
				$("#appKey").val(data.appKey);
				$("#userMobile").val(data.userMobile);
				$("#userEmail").val(data.userEmail);
				$("#userRemark").val(data.userRemark);
				$("#userPass").val("******");
				$("#rePassword").val("******");
			}
		}
	});
};

//删除用户
var deleteUser = function(id, appKey) {
	if (!confirm("请确认删除用户")) {
		return;
	}
	$.ajaxExtend({
		url : serviceHost + "/usermanage/v1/removeuser",
		async : false,
		type : "post",
		dataType : "json",
		data : {
			id : id,
			appKey : appKey
		},
		success : function(obj) {
			var data = eval(obj);
			if (data.resultCode == 0) {
				layer.msg("删除用户成功");
				initQuery();
			} else {
				layer.msg(data.resultInfo);
			}
		}
	});
};

// 重新生成appkey
var rebuildAppKey = function() {
	if ($("#id").val() == "0") {
		layer.msg("新增用户不能操作");
		return;
	}

	$.ajaxExtend({
		url : serviceHost + "/usermanage/v1/modifyuserkey",
		async : false,
		type : "post",
		dataType : "json",
		data : {
			id : $("#id").val()
		},
		success : function(obj) {
			var data = eval(obj);
			if (data.resultCode == 0) {
				layer.msg("更新key成功");
				$("#appKey").val(data.resultInfo);
			} else {
				layer.msg(data.resultInfo);
			}
		}
	});

};

//保存用户信息
var saveUserInfo = function() {
	var url = "";
	if ($("#id").val() != "0") {
		url = serviceHost + "/usermanage/v1/modifyuser";
	} else {
		url = serviceHost + "/usermanage/v1/createuser";
	}
	$.ajaxExtend({
		url : url,
		async : false,
		type : "post",
		dataType : "json",
		data : $("#userForm").serialize(),
		success : function(obj) {
			var data = eval(obj);
			if (data.resultCode == 0) {
				layer.msg("编辑用户成功");
				initQuery();
				$("#userModal").modal('hide');
			} else {
				layer.msg(data.resultInfo);
			}
		}
	});
};

var tableObj = new queryTableObj("userList", contentUrl, "", columnParams,
		cellCallBack, 'get', 'json', rowCallBack, {});
// 初始化查询
var initQuery = function() {
	quickBtnType = 1;
	Table.refresh();
	Table.tableQuery(tableObj);
};

var cataLogTree = [];
var tempTree = [];
//初始化服务目录树
var initCataLogTree = function() {
    
    var urlText = serviceHost + "/usermanage/v1/querycatalogtree";
  //日志统计时使用
    if(statis){
        urlText = serviceHost + "/log/v1/querycatalogtree";;
    }
	$.ajaxExtend({
		url : urlText,
		async : false,
		type : "get",
		dataType : "json",
		data : {
			parentId : 0,
			isService : 0,
			isDeleted : 0
		},
		success : function(obj) {
			var data = eval(obj).datas;
			if (data != null && data.length > 0) {

				for (var i = 0; i < data.length; i++) {
					var dataRow = data[i];
					var obj = {};
					obj["level"] = dataRow.treeLevel;
					obj["id"] = dataRow.id;
					obj["text"] = dataRow.catalogName;
					obj["pid"] = dataRow.pid;
					//根目录不能选中进行全部授权
					if(dataRow.id > 0){
						obj["checkboxes"] = [ {
							"name" : "调用",
							"checked" : false,
							"id" : "access_" + dataRow.id,
							"dataId" : dataRow.id,
							"index" : 0
						}, {
							"name" : "查看",
							"checked" : false,
							"id" : "view_" + dataRow.id,
							"dataId" : dataRow.id,
							"index" : 1
						} ];
					}
					tempTree[dataRow.id] = obj;
				}

				for (var j = data.length - 1; j >= 0; j--) {
					var dataRow = data[j];
					var obj = tempTree[dataRow.id];
					if (dataRow.pid >= 0 && tempTree[dataRow.pid] != null) {
						var tempObj = tempTree[dataRow.pid];
						if(tempObj["nodes"]==null){
							tempObj["nodes"]=[];
						}
						tempTree[dataRow.pid] = tempObj;
						tempTree[dataRow.pid]["nodes"].unshift(obj);
					} else {
						cataLogTree.unshift(obj);
					}
				}

				$('#apiAuthorityTree').find('.expand-collapse').on('click',function(){
					setApiTableHeight();
				});
			}
		}
	});

}
/**
 * 授权弹窗初始化
 */
var initModalAuthority = function(id, key) {
	$(".modal-title").html("服务授权");
	//首先清空用户服务列表权限
	 viewPermission = "";
	 accessPermission = "";
	// 初始化一次权限，存放全局
	loadUserAuths(key, id);
	// 将树的高度设置为与右侧表格一样高
	var tableHeight = $('#apiAuthorityTable').outerHeight();
	loadCataLogTree();
	// 初始化目录
	loadServiceByCataLog(0,"");
	$('#apiAuthorityTree').height(tableHeight);
	$('#apiAuthorityTree').addClass('dataTables_wrapper');
	// 初始化用户目录权限
	initCataLogAuth(0);
	quickBtnType = 2;
};

var loadCataLogTree=function(){
	$('#apiAuthorityTree').empty();
	// 获取接口目录树
	$('#apiAuthorityTree').treeview({
		data : cataLogTree,
		nodeIcon : "",
		expandIcon : 'glyphicon glyphicon-chevron-right',
		collapseIcon : 'glyphicon glyphicon-chevron-down',
		onNodeSelected : function(event, node) {
			loadServiceByCataLog(node.id,"");
		},
		onNodeExpanded:function(){
			setApiTableHeight();
		}
	});

	// 生成按目录授权的菜单
};

var setApiTableHeight = function(){
	var treeHeight = $('#apiAuthorityTree').find('ul').outerHeight()+20;
	var tableHeight = $('#apiAuthorityTableContainer').outerHeight();
	if(treeHeight > tableHeight){
		$('#apiAuthorityTree').parent().height(treeHeight);
	}else{
		$('#apiAuthorityTree').parent().height(tableHeight);
	}

};

//var getTreeNodeById = function(data,id)
//{
//    var Deep,T,F;
//    for (F = data.length;F;)
//    {
//        T = data[--F]
//        if (id === T.id) return T
//        if (T.nodes)
//        {
//            Deep = getTreeNodeById(T.nodes,id)
//            if (Deep) return Deep
//        }
//    }
//}
////获取所有子节点
//var getChildsByPid = function(pid) {
//	var childrens = [];
//	childrens.push(pid);
//	var node = getTreeNodeById(cataLogTree, pid);
//	getSonIds(node, 0, childrens);
//	return childrens;
//}
//
//var getSonIds = function(node, i, result) {
//	var children = node.nodes;
//	if (children != null && children.length > 0) {
//		result.push(children[i].id);
//		if (i == children.length - 1) {
//			getSonIds(children[0], 0, result);
//		} else {
//			getSonIds(node, i + 1, result);
//		}
//	}
//}

// 初始化目录树权限
var initCataLogAuth = function(parentId) {
	var tempAuthTree=[];
	$.ajax({
		url : serviceHost + "/usermanage/v1/querycatalogauth",
		async : false,
		type : "get",
		dataType : "json",
		data : {
			userId : userId,
			parentId:parentId
		},
		success : function(obj) {
			var data = eval(obj).datas;
			var tempAuthTree=[];
			var authTree=[];
			for (var i = 0; i < data.length; i++) {
				var dataRow = data[i];
				var obj = {};
				obj["level"] = dataRow.treeLevel;
				obj["id"] = dataRow.id;
				obj["text"] = dataRow.catalogName;
				obj["pid"] = dataRow.pid;
				//根目录不能选中进行全部授权
				if(dataRow.id > 0){
					obj["checkboxes"] = [ {
						"name" : "调用",
						"checked" : dataRow.accessPermission,
						"id" : "access_" + dataRow.id,
						"dataId" : dataRow.id,
						"pid":dataRow.pid,
						"index" : 0
					}, {
						"name" : "查看",
						"checked" : dataRow.viewPermission,
						"id" : "view_" + dataRow.id,
						"dataId" : dataRow.id,
						"pid":dataRow.pid,
						"index" : 1
					} ];
				}
				tempAuthTree[dataRow.id] = obj;
			}

			for (var j = data.length - 1; j >= 0; j--) {
				var dataRow = data[j];
				var obj = tempAuthTree[dataRow.id];
				if (dataRow.pid >= 0 && tempAuthTree[dataRow.pid] != null) {
					var tempObj = tempAuthTree[dataRow.pid];
					if(tempObj["nodes"]==null){
						tempObj["nodes"]=[];
					}
					tempAuthTree[dataRow.pid] = tempObj;
					tempAuthTree[dataRow.pid]["nodes"].unshift(obj);
				} else {
					authTree.unshift(obj);
				}
			}

			$('#catalogAuthorityTree').empty();			
			// 生成按目录授权的菜单
			$('#catalogAuthorityTree').treeview({
				levels:100,
				data : authTree,
				nodeIcon : "",
				expandIcon : 'glyphicon glyphicon-chevron-right',
				collapseIcon : 'glyphicon glyphicon-chevron-down',
				showBorder : false,
				hasCheckbox : true,
				highlightSelected : true,
				clickable : false,
				onNodeSelected : function(event, node) {
				},
				onNodeCheckedCallback : function(event, node) {
					var checkBox = node["checkboxes"][0];
					var type = checkBox.id.replace("_" + checkBox.dataId, "");
					var obj = document.getElementById(checkBox.id);
					submitServiceAuth(type, obj);
				}
			});
			
		}
	});
	
	
	
};

// 根据左边树形菜单，进行联动
var loadServiceByCataLog = function(parentId,key) {
	var columnParamsA = [ {
		data : 'serviceName'
	}, {
		data : 'serviceCode'
	}, {
		data : 'id'
	}, {
		data : 'id'
	} ];

	var contentUrl = serviceHost + "/configer/v1/servicelist/v1/querylist";

	var cellCallBackA = [
			{
				"render" : function(data, type, row) {
					var html = "<input type='checkbox' onclick='submitServiceAuth(\"view\",this);' dataId="
							+ row.id + " id='view_" + row.id + "'";
					if (row.verifyView == 0) {
						html += " checked='checked' disabled='disabled'";
					} else if (viewPermission.indexOf("," + row.id + ",") >= 0) {
						html += " checked='checked'";
					}
					html += ">"
					return html;
				},
				"orderable" : false,
				"targets" : 2
			},
			{
				"render" : function(data, type, row) {
					var html = "<input type='checkbox' onclick='submitServiceAuth(\"access\",this);' dataId="
							+ row.id + " id='access_" + row.id + "'";
					if (row.verifyAccess == 0) {
						html += " checked='checked' disabled='disabled'";
					} else if (accessPermission.indexOf("," + row.id + ",") >= 0) {
						html += " checked='checked'";
					}
					html += ">"
					return html;
				},
				"orderable" : false,
				"targets" : 3
			} ];

	var tableObj = new queryTableObj("apiAuthorityTable", contentUrl, "",
			columnParamsA, cellCallBackA, 'get', 'json', rowCallBack, {
				isService : 1,
				isDeleted : 0,
				parentId : parentId,
				key:key
			});
	Table.refresh();
	// 表格初始化查询
	Table.tableQuery(tableObj);
};

//查询用户接口权限信息
var loadUserAuths = function(key, id) {
	userId = id;
	appKey = key;
	$.ajaxExtend({
		url : serviceHost + "/usermanage/v1/queryuserbyappkey",
		async : false,
		type : "get",
		dataType : "json",
		data : {
			appKey : appKey
		},
		success : function(obj) {
			var result = eval(obj).object;
			viewPermission = "," + result.viewPermissions + ",";
			accessPermission = "," + result.accessPermissions + ",";
		}
	});
};
// 按单个服务授权提交
var submitServiceAuth = function(type, obj) {
	var accessObj;
	var viewObj;
	var serviceId = $(obj).attr("dataid");
	if (type == "access") {
		accessObj = $(obj);
		viewObj = $("#view_" + serviceId);
	} else {
		viewObj = $(obj);
		accessObj = $("#access_" + serviceId);
	}

	var authListJson = [];
	var auth = {};
	auth["userId"] = userId;
	auth["serviceId"] = serviceId;
	auth["viewPermission"] = viewObj.attr("checked") == "checked" ? "1" : "0";
	auth["accessPermission"] = accessObj.attr("checked") == "checked" ? "1": "0";
	authListJson.push(auth);
	$.ajaxExtend({
		url : serviceHost + "/usermanage/v1/createuserauthorization",
		async : false,
		type : "POST",
		dataType : "json",
		data : {
			id : userId,
			appKey : appKey,
			serviceId :serviceId,
			serveUserAuthorityVoListJson : JSON.stringify(authListJson)
		},
		success : function(obj) {
			var result = eval(obj);
		}
	});

};

//绑定快捷键
document.onkeydown = function(event) {
	var e = event || window.event || arguments.callee.caller.arguments[0];
	if (e && e.keyCode == 27) { // 按 Esc
		// 要做的事情
	}
	if (e && e.keyCode == 113) { // 按 F2
		// 要做的事情
	}
	if (e && e.keyCode == 13) { // enter 键
		if(quickBtnType==2){
			loadServiceByCataLog(0,$('#inputGroupSuccess1').val());
		}else if(quickBtnType==1){
			$("#userListQueryBtn").click();
		}
		return false;
	}
	if (e && e.keyCode == 119) { // f8 键
		// 要做的事情
	}
};

$(function() {
	initCataLogTree();
	hideLeftMenu();
	initQuery();
	if(statis){
	    //隐藏增加按钮
	    $("#addUserButtonWh").css("display","none")
	};
	
	//授权窗口关闭，快捷查询默认为首页列表
	$("#authorityModal").on("hide.bs.modal",function(){
		quickBtnType=1;
	});
	//用户资料窗口关闭，快捷查询默认为首页列表
	$("#userModal").on("hide.bs.modal",function(){
		quickBtnType=1;
	});
	
	
});


