var rowCallBack = function(row, data, iDisplayIndex) {
};

var columsAdded = [];

var columnParams = [ {
	data : 'serviceStatus'
}, {
	data : 'serviceName'
}, {
	data : 'configRemark'
}, {
	data : 'serviceCode'
}, {
	data : 'updatetime'
}, {
	data : 'serviceStatus'
} ];

var contentUrl = serviceHost + "/configer/v1/servicelist/v1/querylist";

var cellCallBack = [
		{
			"render" : function(status, type, row) {
				if (status == 3) {
					return "<img src='./theme/images/status_3.png'>";
				} else if(status==2){
					return "<img src='./theme/images/status_2.png'>";
				}else if(status == 1){
					return "<img src='./theme/images/status_1.png'>";
				}else {
					return "<img src='./theme/images/status_1.png'>";
				}
			},
			"targets" : 0
		},{
            "render" : function(data, type, row) {
                var html = "";
                if (data.length > 15) {
                    html = data.substr(0, 12) + "...";
                } else {
                    html = data;
                }
                var contx='<a href="javascript:moreInfoConfig(' + row.id+ ')">'+html+'</a>';
                return contx;

            },
            "targets" : 1
        },
		{
			"render" : function(data, type, row) {
				var html = "";
				if (data.length > 15) {
					html = data.substr(0, 12) + "...";
				} else {
					html = data;
				}
				var contx='<a href="javascript:moreInfoConfig(' + row.id+ ')">'+html+'</a>';
                return contx;

			},
			"targets" : 2
		},
		{
			"render" : function(status, type, row) {
				var html = '';
				html += '&nbsp;<a href="javascript:deleteConfig('+row.id+')">删除</a>&nbsp;&nbsp;';
				if(status==3){
					html += '&nbsp;<a href="javascript:cancelPublish('+ row.id+ ');" >撤销</a>&nbsp;';
					 html += '&nbsp;<a href="" onclick="viewPublishPage('+ row.id + ');" data-backdrop="static" data-toggle="modal" data-target="#publishService" >详情</a>&nbsp;';
				}else{
					html += '&nbsp;<a href="javascript:loadConfig('+row.id+')">修改</a>&nbsp;&nbsp;';
					html += '&nbsp;<a href="" onclick="openPublishPage('+ row.id+ ');" data-backdrop="static" data-toggle="modal" data-target="#publishService" >发布</a>&nbsp;';
				}
				return html;
			},
			"targets" : 5
		} ];

var getQueryArgs = function() {
	var param = {
		isService : 1,
		isDeleted : 0,
		parentId : parentIdValue,
		serviceType:2
	};
	return param;
}

var tableObj = new queryTableObj("serviceList", contentUrl, "", columnParams,
		cellCallBack, 'get', 'json', rowCallBack, getQueryArgs());
// 初始化查询
var initQuery = function() {
	Table.refresh();
	Table.tableQuery(tableObj);
};

var initCatalog = function() {
	$("#parentId").empty();
	$.ajaxExtend({
		url : serviceHost + "/catalog/v1/querycatalogtree",
		async : false,
		type : "get",
		dataType : "json",
		data : {
			parentId : 2,
			isDeleted : 0
		},
		success : function(obj) {
			var data = eval(obj).datas;
			if (data != null && data.length > 0) {

				for (var i = 0; i < data.length; i++) {
					var dataRow = data[i];
					var level = dataRow.treeLevel;

					var option = '<option value="' + dataRow.id + '">'
							+ getLevelPrefix(level) + dataRow.catalogName
							+ '</option>';
					$("#parentId").append(option);
					
				}
			}
			if (parentIdValue > -1) {
				$("#parentId").val(parentIdValue);
			}

		}
	});
};

var getLevelPrefix = function(level) {
	var prefix = "";
	for (var i = 1; i <= level; i++) {
		prefix += "&nbsp;&nbsp;&nbsp;&nbsp;";
		if (i > 5) {
			prefix = +"。。。";
			break;
		}
	}
	return prefix;

};

var toggleFilterTable = function(checkboxObj) {
	var checked = $(checkboxObj).attr('checked');
	if (checked) {
		$(checkboxObj).parent().parent().next().removeClass('hide');
	} else {
		$(checkboxObj).parent().parent().next().addClass('hide');
	}
};

var hideFilterTable=function(checkboxObj){
	$(checkboxObj).removeAttr("checked"); 
	$(checkboxObj).parent().parent().next().addClass('hide');
	
}

var checkboxEvent = function() {
	$('#viewUser').on('change', function() {
		toggleFilterTable(this);
		if ($('#viewUser').attr("checked") == "checked") {
			initChooseList("viewChooseList", "", "viewUserTable");
		}

	});
	$('#callUser').on('change', function() {
		toggleFilterTable(this);
		if ($('#callUser').attr("checked") == "checked") {
			initChooseList("accessChooseList", "", "callUserTable");
		}

	});
	$('#IPLimit').on('change', function() {
		toggleFilterTable(this);
	});
};

var removeUser = function(thisObj) {
	if(isReadOnly){
		return;
	}
	$(thisObj).parent().remove();
};

var selectUser = function(id, thisObj) {
	if(isReadOnly){
		return;
	}
	var selectedUser = $(thisObj)
			.clone(false)
			.append(
					'<a href="javascript:;" class="fr" onclick="removeUser(this)">x</a>');
	// 去重
	var selectedArray = new Array();
	$('#' + id).find('.user-selected').find('li').each(function() {
		selectedArray.push($(this).attr('userid'));
	});
	var index = $.inArray(selectedUser.attr('userid'), selectedArray);
	if (index == -1) {
		selectedUser.removeAttr('onclick');
		$('#' + id).find('.user-selected').append(selectedUser);
	}
};

// 只有没有列表数据的时候，才重新查一次
var initChooseList = function(divId, name, tableId) {
	if (!$("#" + divId).find("li").html()) {
		loadChooseList(divId, name, tableId);
	}
};

var loadChooseList = function(divId, name, tableId) {
	$("#" + divId).empty();
	$.ajaxExtend({
		url : serviceHost + "/usermanage/v1/queryuser",
		async : false,
		async : false,
		type : "get",
		dataType : "json",
		data : {
			userName : name,
			pageSize : 10
		},
		success : function(obj) {
			var data = eval(obj).datas;
			if (data != null) {
				for (var i = 0; i < data.length; i++) {
					var html = "<li userid='" + data[i].id
							+ "' onclick='selectUser(\"" + tableId
							+ "\",this)'>" + data[i].userName + "</li>";
					$("#" + divId).append(html);
				}
			} else {
				 layer.msg("加载服务配置失败，请联系管理员");
			}
		}
	});
};

//初始化是否需要权限,需要异步的去查一次数据库，查出哪些有权限的用户
var initAuthUser = function(verifyView, verifyAccess) {
	
var viewObj = document.getElementById("viewUser");
	
	if (verifyView == 1) {
		viewObj.checked=true;
		initChooseList("viewChooseList", "", "viewUserTable");
	}else{
		viewObj.checked=false;
	}
	toggleFilterTable(viewObj);
	
	$("#viewUserSelect").empty();
	
	if(verifyAccess == 0){
		return;
	}

	

	// 动态获取授权的用户信息,set到对应选中框里面
	// 异步ajax提交
	$.ajaxExtend({
				url : serviceHost + "/configer/v1/queryconfigerAuthUsers",
				async : false,
				type : "get",
				dataType : "json",
				data : {
					configerId : $("#configerId").val()
				},
				success : function(obj) {
					var data = eval(obj);
					if (data.resultCode == "0") {
						var accessList = data.access;
						var viewList = data.views;
						for (var i = 0; i < viewList.length; i++) {
							var dataRow = viewList[i];
							//userName为空的说明用户已删除不显示
							if(dataRow.userName==""){
								continue;
							}
							var viewUserHtml = '<li userid="'
									+ dataRow.userId
									+ '">'
									+ dataRow.userName
									+ '<a href="javascript:;" class="fr" onclick="removeUser(this)">x</a></li>';
							$("#viewUserSelect").append(viewUserHtml);
						}
					}
				}
			});

};



var setReadOnly=function(){
	$("#saveBtn").hide();
	$("#publishBtn").hide();
	$("#requestExampleUrl").attr("readonly","readonly");
	$("#resultExampleJson").attr("readonly","readonly");
	$("#resultExampleXml").attr("readonly","readonly");
	$("#buildJsonHref").hide();
	$("#viewUser").attr("disabled","disabled");
	$("#IPLimit").attr("disabled","disabled");
	$("#publishRemark").attr("readonly","readonly");
	$("#editMarkDiv").hide();
};

var removeReadonly=function(){
	$("#saveBtn").show();
	$("#publishBtn").show();
	$("#requestExampleUrl").removeAttr("readonly");
	$("#resultExampleJson").removeAttr("readonly");
	$("#resultExampleXml").removeAttr("readonly");
	$("#buildJsonHref").show();
	$("#viewUser").removeAttr("disabled");
	$("#IPLimit").removeAttr("disabled");
	$("#publishRemark").removeAttr("readonly");
	$("#editMarkDiv").show();
}
var isReadOnly=false;
var viewPublishPage=function(id){
	isReadOnly=true;
	initPublishPage(id);
	setReadOnly();
};

var openPublishPage=function(id){
	isReadOnly=false;
	initPublishPage(id);
	removeReadonly();
}

// 发布前先查一次，然后在设置发布页面进行数据加载
var initPublishPage = function(id) {
	initParentCataLog(id);
	//开启按钮
	$("#saveBtn").removeAttr("disabled");
	$("#publishBtn").removeAttr("disabled");
	
	//关闭所有验证提示框
	$("#dismissEventForm").validationEngine("hideAll");
	
	$("#editMark").val("");
	$("#configerId").val(id);
	initUpdateRecord(id);
	$.ajaxExtend({
		url : serviceHost + "/configer/v1/queryconfiger",
		async : false,
		type : "get",
		dataType : "json",
		data : {
			configerId : id
		},
		success : function(obj) {
			var data = eval(obj).config;
			if (data != null) {
				//设置入参
				setInputArgs(data.inputArgs);
				//设置出餐
				setOutputArgs(data.outputArgs);
				$("#mServiceName").html(data.serviceName);
				$("#mConfigRemark").html(data.configRemark);
				$("#mServiceCode").html(data.serviceCode);
				$("#requestExampleUrl").val(data.requestExampleUrl);
				$("#mRequestMethod").html(data.requestMethod);			
				$("#mResultFormat").html(data.resultFormat);		
				$("#publishRemark").val(data.publishRemark);
				initExampleDiv(data.resultFormat);	
				initResultExample(data.exampleList);
				initAuthUser(data.verifyView, 0);
			} else {
				 layer.msg("加载服务配置失败，请联系管理员");
				$("#publishService").modal('hide');
			}
		}
	});
};

//根据返回类型 显示隐藏示例文本框
var initExampleDiv=function(resultType){
	$("#jsonDiv").hide();
	$("#xmlDiv").hide();
	if(resultType.indexOf("json")>-1){
		$("#jsonDiv").show();
	}else if(resultType.indexOf("xml")>-1){
		$("#xmlDiv").show();
	}
};

var initUpdateRecord = function(id) {
	$.ajaxExtend({
		url : serviceHost + "/configer/v1/servicelist/v1/querylog",
		async : false,
		type : "get",
		dataType : "json",
		data : {
			configerId : id
		},
		success : function(obj) {

			var rs = eval(obj).datas;
			Table.clearContent("p-inner-config-update-recode");
			for (var i = 0; i < rs.length; i++) {
				var row = rs[i];

				var contHtml = "<tr>" + "<td>" + row.operatorName + "</td>"
						+ "<td>" + row.modifyRemark + "</td>" + "<td>"
						+ row.updatetime + "</td> </tr>";

				Table.addRowContent("p-inner-config-update-recode", contHtml);
			}
		}
	});
}


//初始化json返回示例
var initResultExample=function(exampleList){
	$("#resultExampleXml").val("");
	$("#resultExampleJson").val("");
	for(var i=0;i<exampleList.length;i++){
		var dataRow = exampleList[i];
		if(dataRow.resultType=="xml"){
			$("#resultExampleXml").val(dataRow.resultExample);
		}else if(dataRow.resultType=="json"){
			$("#resultExampleJson").val(dataRow.resultExample);
		}
	 }
}

//设置入参table数据
var setInputArgs = function(inputArgs) {
	Table.clearContent("config-params-state");
	for (var i = 0; i < inputArgs.length; i++) {
		var data = inputArgs[i];
		var html = '<td>'+data.paramCode+'</td><td>'+getParamType(data.paramType)+'</td><td>'+(data.isRequired==1?'<i class="glyphicon glyphicon-ok"></i>':'')+'</td><td>'+data.paramDesc+'</td>';
		Table.createEditableTable('config-params-state', html);
	}

};
//设置出参table数据
var setOutputArgs = function(outputArgs) {
	Table.clearContent("return-params-state");
	for (var i = 0; i < outputArgs.length; i++) {
		var data = outputArgs[i];
		var html = '<td>'+data.paramCode+'</td><td>'+getParamType(data.paramType)+'</td><td>'+(data.isRequired==1?'<i class="glyphicon glyphicon-ok"></i>':'')+'</td><td>'+data.paramDesc+'</td>';
		Table.createEditableTable('return-params-state', html);
	}
};
//参数类型翻译
var getParamType=function(type){
	switch (type) {
	case 0:
		return "INT";;
	case 2:
		return "FLOAT";
	case 3:
		return "DATE";
	case 4:
		return "DATETIME";
	default:
		return "STRING";
	}
}

//为了兼容后续同时添加xml json类型，做次兼容
var buildExample=function(){
	var example = $("#resultExample").val();
	$("#resultExampleStr").val(example);
};
var buildVierUsers = function() {
	if (!$("#viewUser").attr('checked')) {
		$("#viewUsers").val("");
		$("#verifyView").val(0)
		return;
	}

	var viewUsers = "";
	$("#viewUserSelect").find("li").each(function() {
		viewUsers += $(this).attr("userid") + ",";
	});
	$("#verifyView").val(1);
	$("#viewUsers").val(viewUsers);

};

/**
 * 发布信息保存
 */
var submitPublishOuterInfo=function(status){
	
	// 验证表达，成功才执行ajax请求
	if (!$("#dismissEventForm").validationEngine("validate")) {
		return;
	}
	

	
	$("#dismissEventForm").find("input[name=serviceStatus]").val(status);
	$("#dismissEventForm").find("input[name=verifyAccess]").val($("#viewUser").attr('checked')?"1":"0");
	$("#dismissEventForm").find("input[name=verifyIp]").val(0);
	buildExample();
	buildVierUsers();
		

	//禁用提交按钮
	$("#saveBtn").attr("disabled","disabled");
	$("#publishBtn").attr("disabled","disabled");
	//debugger;
	//异步ajax提交
	$.ajaxExtend({
		url : serviceHost + "/configer/v1/configerpublish",
		async : false,
		type : "POST",
		dataType : "json",
		data :$("#dismissEventForm").serialize(),
		success : function(obj) {//debugger;
			var data = eval(obj);
			if(data.resultCode=="0"){
				 layer.msg("服务发布成功");
				initQuery();
				$("#publishService").modal("hide");
			}else{
				 layer.msg(data.resultInfo);
			}
			//释放提交按钮
			$("#saveBtn").removeAttr("disabled");
			$("#publishBtn").removeAttr("disabled");
		},
		error:function(k){
		    layer.msg("发布失败"+k);
		}
	});	
};
	
var loadPageContent = function(pageContentUrl){
    $("#digitalChinaCurrentUrl").val(pageContentUrl);
    $("#tab-content-body").load(pageContentUrl);

};

var loadConfig=function(id){
	var url = "./pages/dataBaseManage/outconfig.html?configId="+id;
	loadPageContent(url);
}
var moreInfoConfig=function(id){
    var url = "./pages/dataBaseManage/outconfig.html?configId="+id+"&readOnly=true";
    loadPageContent(url);
}
var deleteConfig=function(id){  
    if(!confirm("是否删除当前服务？")){
        return;
    }
    $.ajaxExtend({
        url : serviceHost + "/configer/v1/deleteoutconfiger.json",
        async : false,
        type : "post",
        dataType : "json",
        data : {
            outConfigId : id
        },
        success : function(data) {
            if (data != null && data.resultCode==0) {
            	 layer.msg("删除成功！");
                initQuery();
            }
        },
        error:function(s,e,k){
        	 layer.msg("删除失败");
        }
    });
};
var cancelPublish = function(id) {
	if(!confirm("是否撤销当前发布")){
		return;
	}
	$.ajaxExtend({
		url : serviceHost + "/configer/v1/servicelist/v1/cancelpublish",
		async : false,
		type : "post",
		dataType : "json",
		data : {
			configId : id
		},
		success : function(obj) {
			var data=eval(obj);
			
			if(data.resultCode=='0'){
				initQuery();
				 layer.msg("服务撤销发布成功")
			}else{
				 layer.msg(data.resultInfo);
			}

		}
	});
};

var initParentCataLog=function(childId){
	$("#parentCataLogName").html("");
	$.ajaxExtend({
		url : serviceHost + "/catalog/v1/queryparentcatalogbyid",
		async : false,
		type : "get",
		dataType : "json",
		data : {
			childId : childId
		},
		success : function(obj) {
			var datas = obj.datas;
			var parentName = "";
			for(var i = 0; i< datas.length;i++){
				parentName += datas[i].catalogName+"\\";
			}
			$("#parentCataLogName").html(parentName);
		}
	});
};

function initModal(){ 
    var url =  "./pages/dataBaseManage/outconfig.html"; 
    loadPageContent(url);
  }
$(function() {
	showLeftMenu();
	//initCatalog();
	initQuery();
	checkboxEvent();
	$("#parentId").val(parentIdValue);
});

document.onkeydown = function(event) {
	var e = event || window.event || arguments.callee.caller.arguments[0];
	if (e && e.keyCode == 27) { // 按 Esc
		// 要做的事情
	}
	if (e && e.keyCode == 113) { // 按 F2
		// 要做的事情
	}
	if (e && e.keyCode == 13) { // enter 键
		// 要做的事情
		$("#serviceListQueryBtn").click();
	}
	if (e && e.keyCode == 119) { // f8 键
		// 要做的事情
	}
};
