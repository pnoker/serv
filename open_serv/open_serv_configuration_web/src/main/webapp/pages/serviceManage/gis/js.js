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
					html += '&nbsp;<a href="" onclick ="openPublishPage('+ row.id+',10110);" data-backdrop="static" data-toggle="modal" data-target="#publishService" >详情</a>&nbsp;';
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
		serviceType:3
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
		url : serviceHost + "/configer/v1/servicelist/v1/querycatalogtree",
		async : false,
		type : "get",
		dataType : "json",
		data : {
			parentId : 3,
			isService : 0,
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
					;
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

var checkboxEvent = function() {
	$('#viewUser').on('change', function() {
		toggleFilterTable(this);		
		if($('#viewUser').attr("checked")=="checked"){
			initChooseList("viewChooseList","","viewUserTable");
		}
	
	});
	$('#callUser').on('change', function() {
		toggleFilterTable(this);
		if($('#callUser').attr("checked")=="checked"){
			initChooseList("accessChooseList","","callUserTable");
		}

	});
	$('#IPLimit').on('change', function() {
		toggleFilterTable(this);
	});
};

var removeUser = function(thisObj){
	$(thisObj).parent().remove();
};

var selectUser = function(id,thisObj){
	var selectedUser = $(thisObj).clone(false).append('<a href="javascript:;" class="fr" onclick="removeUser(this)">x</a>');
	//去重
	var selectedArray = new Array();
	$('#'+id).find('.user-selected').find('li').each(function(){
		selectedArray.push($(this).attr('userid'));
	});
	var index = $.inArray(selectedUser.attr('userid'), selectedArray);
	if(index == -1){
		selectedUser.removeAttr('onclick');
		$('#'+id).find('.user-selected').append(selectedUser);
	}
};

//只有没有列表数据的时候，才重新查一次
var initChooseList=function(divId,name,tableId){
	if(!$("#"+divId).find("li").html()){
		loadChooseList(divId, name, tableId);
	}
};

var loadChooseList=function(divId,name,tableId){
	$("#"+divId).empty();
	$.ajaxExtend({
		url:serviceHost+"/configer/v1/servicelist/v1/queryuser",
		async:false,
		async : false,
		type : "get",
		dataType : "json",
		data:{
			userName:name,
			pageSize:10
		},
		success : function(obj) {
			var data = eval(obj).datas;
			if (data != null ) {
				for(var i =0;i< data.length;i++){
					var html ="<li userid='"+data[i].id+"' onclick='selectUser(\""+tableId+"\",this)'>"+data[i].userName+"</li>";
					$("#"+divId).append(html);
				}
			} else {
				layer.msg("加载服务配置失败，请联系管理员");
			}
		}
	});
};

// 发布前先查一次，然后在设置发布页面进行数据加载
var openPublishPage = function(id,isMoreInfo) {
	//获取父节点名称	
	initParentCataLog(id);
	//释放提交按钮
	$("#saveBtn").removeAttr("disabled");
	$("#publishBtn").removeAttr("disabled");
	
	$("#configerId").val(id);
	
    //详情页面 隐藏两个按钮 发布就显示
    $("#saveBtnn").css("display",isMoreInfo?"none":"");
    $("#publishBtn").css("display",isMoreInfo?"none":"");
    $("#update_log_row").css("display",isMoreInfo?"none":"");
	$.ajaxExtend({
		url : serviceHost + "/configer/v1/servicelist/v1/querygisconfig",
		async : false,
		type : "get",
		dataType : "json",
		data : {
		    gisConfigId : id
		},
		 success : function(data) {
	            if (data != null && data.resultCode==0) {
	                var conf = data;
	                 //$("#mServerMenu").val(conf.config.parentId);
	                $("#configerId").val(id); 
	                 $("#mServiceName").text(conf.config.serviceName); 
	                 $("#mConfigRemark").text(conf.config.configRemark);
	                 $("#mUrlTest").text(conf.serveExtendSpace.urlTest);
	                 $("#mUrlFormal").text(conf.serveExtendSpace.urlFormal);

					$("#mServiceName").attr('title',conf.config.serviceName);
					$("#mConfigRemark").attr('title',conf.config.configRemark);
					$("#mUrlTest").attr('title',conf.serveExtendSpace.urlTest);
					$("#mUrlFormal").attr('title',conf.serveExtendSpace.urlFormal);
	                 //地图预览
	                 var mapUrl = conf.serveExtendSpace.urlFormal;
	                 featureLayer = generalLayer(mapUrl);
	                 map.removeAllLayers();
	                 map.addLayer(featureLayer);
	                 
	                 var geoType={
	                         "1":"点",
	                         "2":"线",
	                         "3":"面",
	                         "4":"NULL"      
	                 };
	                 $("#mGeoFeature").text(geoType[conf.serveExtendSpace.ge0Features]);
	                 $("#mUpdateTime").text(conf.config.updatetime);

					$("#mGeoFeature").attr('title',geoType[conf.serveExtendSpace.ge0Features]);
					$("#mUpdateTime").attr('title',conf.config.updatetime);
	                  
	                 $("#p-layerType").text(conf.serveExtendSpace.layerType == 1 ? "FeatureLayer":"RasterLayer");
	                 $("#p-isVisible").text(conf.serveExtendSpace.isVisible == 1 ? "可见":"不可见");
	                 $("#p-resultFormat").text(conf.config.resultFormat);
	                 var gisServer={
	                         "1" :"Map Server + KML", 
	                         "2":" Map Server + KML + WMS",
	                         "3":" Map Server + KML + Feature Access",
	                         "4":"Map Server + KML + WCS",
	                        "5":"Map Server + KML + WFS",
	                        "6":" Map Server + KML + Schematics",
	                        "7":"Map Server + KML + Mobile Data Access",
	                        "8":" Map Server + KML + Network Analysis"
	                 }
	                 $("#p-gisServer").text(gisServer[conf.serveExtendSpace.gisServer]);
	                 $("#p-displayScaleMin").text(conf.serveExtendSpace.displayScaleMin);
	                 $("#p-displayScaleMax").text(conf.serveExtendSpace.displayScaleMax);
	                 $("#p-isLabel").text(conf.serveExtendSpace.isLabel==1?"有":"没有");
	                 $("#p-coordinateCode").text(conf.serveExtendSpace.coordinateCode);
	                 $("#p-coordinateSystem").text(conf.serveExtendSpace.coordinateSystem);
	                 $("#p-maxLength").text(conf.serveExtendSpace.maxLength);
	                 //数据字典
	                 var dic = conf.serveExtendSpace.gisDictionaries;

                     Table.clearContent("p-gis-config-data-dictionary");
	                 for(var i = 0;i < dic.length;i++){
	                     var row = dic[i];
	                      
	                     var contHtml="<tr>" 
	                            + "<td>"+row.filedName+"</td>" 
	                            + "<td>"+row.filedType+"</td>"
	                            +"<td>"+row.filedLength+"</td>"
	                            + "<td>"+row.decimalLength+"</td>"
	                            + "<td>"+row.isNull+"</td>"
	                            + "<td>"+row.filedDesc+"</td>" 
	                            + "<td>"+row.fieldRemark+"</td></tr>"; 
	                     Table.addRowContent("p-gis-config-data-dictionary",contHtml); 
	                 }
	                 //更新记录
	                var rs =  conf.datas;
	                Table.clearContent("p-gis-config-update-recode");
	                for(var i = 0;i < rs.length;i++){
                        var row = rs[i];
                         
                        var contHtml="<tr>" 
                               + "<td>"+row.operatorName+"</td>" 
                               + "<td>"+row.modifyRemark+"</td>"
                               +"<td>"+row.updatetime+"</td> </tr>"; 
                        
                        Table.addRowContent("p-gis-config-update-recode",contHtml); 
                    }

	            }else{
	                layer.msg("读取服务出错"+data.resultInfo);
	            }
	        },
	        error:function(s,e,k){
	            layer.msg("读取服务出错");
	        }
		
	});
};

var initIpAddress=function(verifyIp,ipList){
	if(verifyIp == 1){
		var obj = document.getElementById("IPLimit");
		$("#IPLimit").click();
		toggleFilterTable(obj);
	}
	var html=""
	for(var i=0;i<ipList.length;i++){
		 html +=ipList[i].ipAddress+"\n";
	}
	$(".IP-list").text(html);
};

//初始化是否需要权限,需要异步的去查一次数据库，查出哪些有权限的用户
var initAuthUser=function(verifyView,verifyAccess){
	if(verifyView == 1){
		var obj = document.getElementById("viewUser");
		$("#viewUser").click();
		toggleFilterTable(obj);
	}
	
	if(verifyAccess == 1){
		var obj = document.getElementById("callUser");
		$("#callUser").click();
		toggleFilterTable(obj);
	}
	//动态获取授权的用户信息,set到对应选中框里面
	//异步ajax提交
	$.ajaxExtend({
		url : serviceHost + "/configer/v1/servicelist/v1/queryconfigerAuthUsers",
		async : false,
		type : "get",
		dataType : "json",
		data :{
			configerId:$("#configerId").val()
		},
		success : function(obj) {
			var data = eval(obj);
			if(data.resultCode=="0"){
				var accessList = data.access;
				var viewList = data.views;
				for(var i = 0;i< viewList.length;i++){
					var dataRow=viewList[i];
					var viewUserHtml='<li userid="'+dataRow.userId+'">'+dataRow.userName+'<a href="javascript:;" class="fr" onclick="removeUser(this)">x</a></li>';
					$("#viewUserSelect").append(viewUserHtml);
				}
				for(var j=0;j< accessList.length;j++){
					var dataRow=accessList[j];
					var accessUserHtml='<li userid="'+dataRow.userId+'">'+dataRow.userName+'<a href="javascript:;" class="fr" onclick="removeUser(this)">x</a></li>';
					$("#accessUserSelect").append(accessUserHtml);
				}
			}
		}
	});

	
};

//初始化json返回示例
var initResultExample=function(exampleList){
	var jsonResult = exampleList[0];
	if(jsonResult == null){
		return;
	}
	 $("#resultExample").val(jsonResult.resultExample);
}

//设置入参table数据
var setInputArgs = function(inputArgs) {
	for (var i = 0; i < inputArgs.length; i++) {
		var data = inputArgs[i];
		var html = '<td>'+data.paramCode+'</td><td>'+getParamType(data.paramType)+'</td><td>'+(data.isRequired==1?'<i class="glyphicon glyphicon-ok"></i>':'')+'</td><td>'+data.paramDesc+'</td>';
		Table.createEditableTable('config-params-state', html);
	}

};
//设置出参table数据
var setOutputArgs = function(outputArgs) {
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

var configParamContent = '<td>姓名</td><td>string</td><td><i class="glyphicon glyphicon-ok"></i></td><td>操作人姓名</td>';
var returnParamContent = '<td>姓名</td><td>string</td><td><i class="glyphicon glyphicon-ok"></i></td><td>操作人姓名</td>';
var initForm = function(){
	checkboxEvent();
	$('#publishService').on('shown.bs.modal', function () {
		var titleHeight = $('#titleList').height();
		$('#mapViewDiv').find('.container').height(titleHeight);
	})
};

var buildIpAddress=function(){
	if(!$("#IPLimit").attr('checked')){
		$("#ipAddresses").val("");
		$("#verifyIp").val(0);
		return;
	}
	var ips = ($(".IP-list").val()).split("\n");
	var ipAddress="";
	for(var i in ips){
		ipAddress +=ips[i]+",";
	}
	$("#ipAddresses").val(ipAddress);
	$("#verifyIp").val(1);
};
var buildAccessUsers=function(){
	if(!$("#callUser").attr('checked')){
		$("#verifyAccess").val(0);
		$("#accessUsers").val("");
		return;
	}

	var accessUsers="";
	$("#accessUserSelect").find("li").each(function(){
		accessUsers +=$(this).attr("userid")+",";
	});
	
	$("#verifyAccess").val(1);
	$("#accessUsers").val(accessUsers);

};
var buildVierUsers=function(){
	if(!$("#viewUser").attr('checked')){
		$("#viewUsers").val("");
		$("#verifyView").val(0)
		return;
	}

	var viewUsers="";
	$("#viewUserSelect").find("li").each(function(){
		viewUsers +=$(this).attr("userid")+",";
	});
	$("#verifyView").val(1);
	$("#viewUsers").val(viewUsers);
	
};
//为了兼容后续同时添加xml json类型，做次兼容
var buildExample=function(){
	var serviceId = $("#configerId").val();
	var type="JSON";
	var example = $("#resultExample").val();
	var jsonExample='[{"resultType":"'+type+'","serviceId":'+serviceId+',"resultExample":"'+example+'"}]';
	$("#resultExampleStr").val(jsonExample);
};

/**
 * 发布信息保存
 */
var submitPublishInfo=function(status){
	
	// 验证表达，成功才执行ajax请求
	if (!$("#dismissEventForm").validationEngine("validate")) {
		return;
	}

	//禁用提交按钮
	$("#saveBtn").attr("disabled","disabled");
	$("#publishBtn").attr("disabled","disabled");

	//异步ajax提交
	$.ajaxExtend({
		url : serviceHost + "/configer/v1/gisconfigerpublish",
		async : false,
		type : "POST",
		dataType : "json",
		data :{
		    configerId:$("#configerId").val(),
		    serviceStatus:status,
		    editRemark:$("#editRemark").val()
		},
		success : function(obj) {
			var data = eval(obj);
			if(data.resultCode=="0"){
				layer.msg("服务发布成功");
				var url = "./pages/serviceManage/gis/list.html";
			    $("#digitalChinaCurrentUrl").val(url);
			    $("#tab-content-body").load(url);
			    
			    $("#publishService").modal("hide");
			   // alert($(".modal-backdrop fade in"));
			    $(".modal-backdrop").css({display:"none"})
				
			}else{
				layer.msg(data.resultInfo);
			}
			//释放提交按钮
			$("#saveBtn").removeAttr("disabled");
			$("#publishBtn").removeAttr("disabled");
		}
	});	
};
	
var loadPageContent = function(pageContentUrl){
    $("#digitalChinaCurrentUrl").val(pageContentUrl);
    $("#tab-content-body").load(pageContentUrl);
    hideLeftMenu();
};

var loadConfig=function(id){
	var url = "./pages/dataBaseManage/gisconfig.html?configId="+id;
	loadPageContent(url);
}
var moreInfoConfig=function(id){
    var url = "./pages/dataBaseManage/gisconfig.html?configId="+id+"&readOnly=true";
    loadPageContent(url);
}
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

var deleteConfig=function(id){  
    if(!confirm("是否删除当前服务？")){
        return;
    }
    $.ajaxExtend({
        url : serviceHost + "/configer/v1/servicelist/v1/deletegisconfig.json",
        async : false,
        type : "post",
        dataType : "json",
        data : {
            gisConfigId : id
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
  var url =  "./pages/dataBaseManage/gisconfig.html"; 
  loadPageContent(url);
}
$(function() {
	showLeftMenu();
	initForm();
	//initCatalog();
	initQuery();
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