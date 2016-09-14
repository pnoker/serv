var editOption = function() {
	alert("添加按钮事件绑定测试！！");
};

var formatData = function(row,data,iDisplayIndex){
	if(data.SOURCE_USER == 'devuser'){
		$('td:eq(3)', row).html("aaa");
	}
};

var columsAdded = [ {
	"name" : "操作",
	"width" : "20%"
} ];

var columnParams = [ {
	data : 'id'
}, {
	data : 'sourceName'
},{
	data : 'sourceUser'
}, {
	data : 'isDeleted'
},{
	data : 'sourceClass'
},  {
	data : 'updatetime'
} ];

var contentUrl = serviceHost+"/datasource/v1/query";

var columnDefs = [
		{
			"render" : function(data, type, row) {
				return "<a href='javascript:;' onclick='editOption();'><i class='glyphicon glyphicon-pencil'></i></a>";

			},
			"targets" : 6
		} ];

var tableObj = new queryTableObj("tableModal", contentUrl, "", columnParams, columnDefs, 'get', 'json',formatData,{});
Table.tableQuery(tableObj);
hideLeftMenu();

