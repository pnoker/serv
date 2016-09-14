//初始化数据源方法
var initDataSource = function() {
	dragElementTop('#sqlResult','.drag-div','#sqlExecute');
	
	//异步请求获取数据源列表
	$.ajaxExtend({
		url : serviceHost+"/sqloperation/v1/datasourcelist",
		async : false,
		type : "get",
		dataType : "json",
		data : {
			isDeleted : 0
		},
		success : function(obj) {
			var data = eval(obj).datas;
			if (data != null) {
				var obj = $("#dataSource");
				obj.empty();
				for ( var index in data) {
					var dataRow = data[index];
					var option = "<option value='" + dataRow.id + "'>"
							+ dataRow.sourceName + "</option>";
					obj.append(option);
				}
			} else {
				layer.msg("初始化数据源失败");
			}
		}
	});
}

function trim(str) { // 删除左右两端的空格
	return str.replace(/(^\s*)|(\s*$)/g, "");
}
// 执行sql方法
var excuteSql = function() {
	var dataSourceId = $("#dataSource").val();
	if (dataSourceId < 1) {
		layer.msg("请选择数据源");
		return;
	}

	var sql = $("#sqlStr").val();
	if (trim(sql) == "") {
		layer.msg("请输入SQL语句");
	}

	//sql语句提交到后台进行执行
	$("#thead tr").empty();
	$("#tbody").empty();
	$("#resultInfo").html("");
	$(".sql-result").removeClass("hide");
	$.ajaxExtend({
				url : serviceHost+"/sqloperation/v1/excute",
				async : false,
				type : "POST",
				dataType : "json",
				data : {
					dataSource : dataSourceId,
					sql : sql,
					fetchSize:$("#fetchSize").val()
				},
				success : function(obj) {
					var data = eval(obj);
					if (data.resultCode == "0") {
						$("#resultInfo").addClass('hide');
						//通过判断返回数据的columns来判断是否需要动态组装数据datagrid还是直接提示执行成功
						var columns = data.columns;
						if (columns.length == 0) {
							layer.msg("sql执行成功,耗时:" + data.costTime + "秒");
							$("#resultInfo").html(
									data.resultInfo + ",耗时:" + data.costTime
											+ "秒");
						} else {
							// 动态绘制表头，生成dataGrid
							for ( var i in columns) {
								var column = columns[i];
								$("#thead tr").append("<th>" + column.data + "</th>");
							}
							var resultSet = data.datas;
							if (resultSet != null && resultSet.length > 0) {
								for ( var j in resultSet) {
									var dataRow = resultSet[j];
									var html = "<tr>";
									for ( var k in columns) {
										//字段超长时，自动截取显示
										var data=dataRow[columns[k].data];
										if(data.length>60){
											html += "<td title='"+data+"'>"
												+ data.substr(0,55)+"..."
												+ "</td>";
										}else{
											html += "<td>"
													+ dataRow[columns[k].data]
													+ "</td>";
										}
									}
									html += "</tr>";
									$("#tbody").append(html);
								}
							}
						}
					} else {
						$("#resultInfo").html(data.resultInfo);
						$("#resultInfo").removeClass('hide');
					}
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
		// 要做的事情
	}
	if (e && e.keyCode == 119) { // f8 键
		// 要做的事情
		excuteSql();
	}
};

$(function() {
	hideLeftMenu();
	initDataSource();
})
