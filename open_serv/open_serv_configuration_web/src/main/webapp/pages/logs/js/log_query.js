var rowCallBack = function(row, data, iDisplayIndex) {
};
var columsAdded = [];

var columnParams = [ {
	data : 'number'
}, {
	data : 'serviceName'
}, {
	data : 'serviceType'
}, {
	data : 'userName'
}, {
	data : 'userChannel'
}, {
	data : 'visitIpAddress'
},{
	data : 'retCode',
}, {
	data : 'retMsg',
}, {
	data : 'costTime'
}, {
	data : 'visitDate'
} ];

var contentUrl = serviceHost + "/logquery/v1/queryloglists.json";

var cellCallBack = [
		{
			"render" : function(data, type, row) {
				// 1:配置接口，2:外部接口，3:空间数据库
				return data == "1" ? "配置接口" : (data == "2" ? "外部接口"
						: (data == "3" ? "空间数据库" : "未知类型"));

			},
			"targets" : 2
		},
		{
			"render" : function(data, type, row) {
				// (1:内网，2:外网)
				return data == "1" ? "内网" : (data == "2" ? "外网" : "未知类型");

			},
			"targets" : 4
		},
		{
			"render" : function(data, type, row) {
				var str = JSON.stringify(row);
				var html = '<a href="#" onclick="showLogInfo(this);"> ' + data
						+ '</a> <label name = "ks" style="display: none;">'
						+ str + ' </label>';
				return html;

			},
			"targets" : 0
		},
		{
			"render" : function(msg, type, row) {
				var html =msg;
				if(msg.length>15){
					html = msg.substr(0,15)+"...";
					return "<span title='"+msg+"'>"+html+"</span>";
				}else{
					return html;
				}
			

			},
			"targets" : 7
		},];

var showLogInfo = function(obj) {
	var jsonstr = $(obj).next().text();
	var json = JSON.parse(jsonstr);
	alert(jsonstr);
}
var getInputArgs = function() {

	var args = {};
	args.serviceName = $("#serviceName").val();
	args.userName = $("#userName").val();
	args.userChannel = $("#userChannel").val();
	args.ip = $("#ip").val();
	args.beginTime = $("#beginTime").val();
	args.endTime = $("#endTime").val();
	args.servType = $("#servType").val();
	return args;
}

var tableObj = new queryTableObj("logList", contentUrl, "", columnParams,
		cellCallBack, 'get', 'json', rowCallBack, getInputArgs());
// 初始化查询
var initQuery = function() {
	Table.refresh();
	Table.tableQuery(tableObj);
}

$(function() {
	initQuery();
})
hideLeftMenu();
