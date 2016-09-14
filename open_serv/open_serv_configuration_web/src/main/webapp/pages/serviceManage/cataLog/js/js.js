var initCataLogTree = function() {
	var cataLogTree = [];
	$('#cataLogTree').empty();
	$.ajaxExtend({
		url : serviceHost + "/catalog/v1/querycatalogtree",
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
			var tempTree = [];
			if (data != null && data.length > 0) {

				for(var i = 0; i < data.length; i++){
					var dataRow = data[i];
					var level = dataRow.treeLevel;

					var option = '<option value="' + dataRow.id + '">'
							+ getLevelPrefix(level) + dataRow.catalogName
							+ '</option>';
					$("#cataLogSelect").append(option);
				}

				for (var i = 0; i < data.length; i++) {
					var dataRow = data[i];
					var obj = {};
					obj["level"] = dataRow.treeLevel;
					obj["id"] = dataRow.id;
					obj["text"] = dataRow.catalogName;
					obj["checkboxes"] = [ {
						"name" : "调用",
						"checked" : false,
						"id":"access_"+dataRow.id,
						"dataId":dataRow.id,
						"index" : 1
					},
					{
						"name" : "查看",
						"checked" : false,
						"id":"view_"+dataRow.id,
						"dataId":dataRow.id,
						"index" : 0
					}];
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
			}

			// 获取接口目录树
			$('#cataLogTree').treeview({
				data : cataLogTree,
				nodeIcon : "",
				expandIcon : 'glyphicon glyphicon-chevron-right',
				collapseIcon : 'glyphicon glyphicon-chevron-down',
				onNodeSelected : function(event, node) {
					console.log(node);
					$("#pid").val(node.id);
					if(node.id == 0){
						$("#editBtn").fadeOut();
						$("#addBtn").fadeOut();
					}else{
						$("#editBtn").fadeIn();
						$("#addBtn").fadeIn();
					}
					if(node.id> 10){
						$("#delBtn").fadeIn();
					}else{
						$("#delBtn").fadeOut();
					}
				}
			});
		}
	});
};

var openAddCataLog=function(){
	$("#parentId").val($("#pid").val());
	$("#cataLogSelect").val($("#pid").val());
	$("#catalogName").val("");
	$("#catalogRemark").val("");
	$("#catalogId").val("");
	$("#cataLogModal").modal("show");
}

var openEditCataLog=function(){
	var pid=$("#pid").val();
	$("#catalogId").val(pid);
	$.ajaxExtend({
		url : serviceHost + "/catalog/v1/querycatalogbyid",
		async : false,
		type : "get",
		dataType : "json",
		data : {
			catalogId : pid
		},
		success : function(obj) {
			var data=eval(obj);
			console.log(data);
			if(data.resultCode =="0"){
				var catalog = data.datas[0];
				if(catalog == null){
					layer.msg("系统错误，请联系管理员");
					return;
				}
				
				$("#parentId").val(catalog.pid);
				$("#cataLogSelect").val(catalog.pid);
				$("#catalogName").val(catalog.catalogName);
				$("#catalogRemark").val(catalog.catalogRemark);
				$("#cataLogModal").modal("show");
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

var delCataLog=function(){
	if(!confirm("是否删除当前目录节点")){
		return;
	}
	$.ajaxExtend({
		url : serviceHost + "/catalog/v1/delcatalog",
		async : false,
		type : "post",
		dataType : "json",
		data : {
			catalogId:$("#pid").val()
		},
		success : function(obj) {
			var data=eval(obj);
			console.log(data);
			if(data.resultCode =="0"){
				initCataLogTree();
				$("#cataLogModal").modal("hide");
			}
			layer.msg(data.resultInfo);
		}
	});
}
var submitCataLog=function(){
	var url =serviceHost + "/catalog/v1/"+($("#catalogId").val()>0?"updatecatalog":"addcatalog");
	$.ajaxExtend({
		url : url,
		async : false,
		type : "post",
		dataType : "json",
		data : $("#cataLogForm").serialize(),
		success : function(obj) {
			var data=eval(obj);
			console.log(data);
			if(data.resultCode =="0"){
				initCataLogTree();
				$("#cataLogModal").modal("hide");
				//动态刷新左侧目录树
				getPopMenu();
			}
			layer.msg(data.resultInfo);
		}
	});
}

$(function() {
	initCataLogTree();
	hideLeftMenu();
})
