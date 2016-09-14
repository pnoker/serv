var initIsEditConfig = function(){
    if(!hasConfigId){
        return;
    }
    //取数据
    $.ajaxExtend({
        url : serviceHost+"/configer/v1/queryconfiger.json",
        async : false,
        type : "get",
        dataType : "json",
        data : {
            configerId : hasConfigId
        },
        success : function(data) {
            if (data != null && data.resultCode==0) {
                var conf = data.config;
                 $("#serverMenu").val(conf.parentId);
                 $("#dataSource").val(conf.dataSourceId); 
                 $("#serviceName").val(conf.serviceName);
                 $("#serviceCode").val(conf.serviceCode);
                 $("#configRemark").val(conf.configRemark);
                 //选中请求方式
                 var rMethod = conf.requestMethod.split(',');
                 for(var i=0;i<rMethod.length;i++){
                     if(rMethod[i]=="post"){
                         $("#ckPOST").attr("checked",'true')
                     }else{
                         $("#ckGET").attr("checked",'true')
                     }
                 }
                 //选中返回格式
                var rFormat = conf.resultFormat.split(',');
                for(var i=0;i<rFormat.length;i++){
                    if(rFormat[i]=="json"){
                        $("#ckJSON").attr("checked",'true')
                    }else{
                        $("#ckJXML").attr("checked",'true')
                    }
                }
                initDataTable(function(){ 
                    $("#datatableName").val(conf.dataTableName);
                    initDataTableField(function(){
                         
                        $("#querySql").val(conf.querySql);
                        generalSQL = conf.querySql;
                        //动态列表添加
                        var inputArgsNavs = conf.inputArgsNavs;
                        var dateRangeArgs = conf.dateRangeArgs;
                        var outputArgsNav = conf.outputArgsNav;
                        
                        var inputArgs = conf.inputArgs;
                        var segments = conf.segments;
                        var outputArgs = conf.outputArgs;
                        //向导输入参数还原
                        for(var i = 0;i < inputArgsNavs.length;i++){
                            var row = inputArgsNavs[i];
                            var newTableFieldOption = selectedTableFields
                            newTableFieldOption = newTableFieldOption.replace("'"+row.columnCode+"'","'"+row.columnCode+"' selected = 'selected '");
                           
                            var contHtml="<tr>" +
                            "<td><select onchange='filedChanged(this,this.value)' name='columnCode' class='form-control'>"+newTableFieldOption+"</select></td>" +
                            "<td><input class='form-control input-sm' disabled=true onchange='filedDescChanged(this,this.value)' value='"+(selectedTableFieldsDesc[row.columnCode]?selectedTableFieldsDesc[row.columnCode]:"")+"'></td>" +
                            "<td><input name='paramCode' class='form-control input-sm validate[required,custom[onlyLetterNumSp]]' value ='"+row.paramCode+"'></td>" +
                            "<td><input name='isRequired' type='checkbox' class='form-control-checkbox' "+(row.isRequired==1?" checked='checked'":"")+"></td>" +
                            "<td><select name='operator' class='form-control'>" +
                            "<option value='>' "+(row.operator=='>'?" selected='selected'":"")+">大于</option>" +
                            "<option value='<' "+(row.operator=='<'?" selected='selected'":"")+">小于</option>" +
                            "<option value='=' "+(row.operator=='='?" selected='selected'":"")+">等于</option></select></td>" +
                            "<td><input name='columnDesc' class='form-control input-sm' value='"+row.columnDesc +"'></td>" +
                            "<td><button class='delete-row-btn' type='button'><i class='glyphicon glyphicon-trash'></i></button></td>" +
                            "</tr>";
                            Table.addRowContent("config-request-param1",contHtml); 
                        }
                        //向导区间参数
                        for(var i = 0;i < dateRangeArgs.length;i++){
                            var row = dateRangeArgs[i];
                            var newTableFieldOption = selectedTableFields
                            newTableFieldOption = newTableFieldOption.replace("'"+row.columnCode+"'","'"+row.columnCode+"' selected = 'selected '");
                           
                            var contHtml ="<tr>" +
                                "<td><select name='columnCode' class='form-control'>"+selectedTableFieldsDateType+"</select></td>" +
                                "<td>近<input name='dataRange' class='form-control-interval validate[required,custom[onlyNumberSp]]' value='"+row.dataRange +"'>天（不包括今天）</td>" +
                                "<td><button class='delete-row-btn' type='button'><i class='glyphicon glyphicon-trash'></i></button></td>" +
                                "</tr>";
                            Table.addRowContent("config-interval-param1",contHtml);
                        }
                        //向导输出参数
                        for(var i = 0;i < outputArgsNav.length;i++){
                            var row = outputArgsNav[i];
                            var newTableFieldOption = selectedTableFields
                            newTableFieldOption = newTableFieldOption.replace("'"+row.columnCode+"'","'"+row.columnCode+"' selected = 'selected '");
                            var returnTableId = 'config-return-param1';
                            var contHtml ="<tr>" +
                                "<td><select name='columnCode' class='form-control'>"+newTableFieldOption+"</select></td>" +
                                "<td><input name='columnLias' class='form-control input-sm validate[required,custom[onlyLetterNumSp]]' value='"+row.columnLias+"'></td>" +
                                "<td><input name='columnDesc' class='form-control input-sm' value='"+row.columnDesc+"'></td>" +
                                "<td><input type='hidden' value='desc'><a index ='1' onclick='orderBy(this,\""+returnTableId+"\")' href='javascript:;' class='orderBy "+(row.sortOrder=="desc"?"active":"") +" '><i class='glyphicon glyphicon-arrow-down'></i></a><a index ='2' onclick='orderBy(this,\""+returnTableId+"\")' class='orderBy "+(row.sortOrder=="asc"?"active":"") +" ' href='javascript:;'><i class='glyphicon glyphicon-arrow-up'></i></a></td>" +
                                "<td><button class='delete-row-btn' type='button'><i class='glyphicon glyphicon-trash'></i></button></td>" +
                                "</tr>";
                            Table.addRowContent(returnTableId,contHtml);
                        }
                        // 实际输入参数
                        for(var i = 0;i < inputArgs.length;i++){
                            var row = inputArgs[i];
                            var contHtml ="<tr>" +
                                "<td><input name='paramCode' class='form-control input-sm validate[required,custom[onlyLetterNumSp]]' value='"+row.paramCode+"'></td>" +
                                "<td><select name='paramType' class='form-control'>" +
                                "<option value='0' "+(row.paramType=='0'?" selected='selected'":"")+">int</option>" +
                                "<option value='1' "+(row.paramType=='1'?" selected='selected'":"")+">string</option>" +
                                "<option value='2' "+(row.paramType=='2'?" selected='selected'":"")+">float</option>" +
                                "<option value='3' "+(row.paramType=='3'?" selected='selected'":"")+">date</option>" +
                                "<option value='4' "+(row.paramType=='4'?" selected='selected'":"")+">datetime(yyyy-MM-dd HH:mm:ss)</option></select></td>" +
                                "<td><input name='isRequired' "+(row.isRequired==1?" checked='checked'":"")+" type='checkbox' class='form-control-checkbox'></td>" +
                                "<td><input name='paramDesc' class='form-control input-sm' value='"+row.paramDesc+"'></td>" +
                                "<td><button class='delete-row-btn' type='button'><i class='glyphicon glyphicon-trash'></i></button></td>" +
                                "</tr>";
                            Table.addRowContent("config-request-param2",contHtml);
                        }
                        //实际SQL片段
                        for(var i = 0;i < segments.length;i++){
                            var row = segments[i];
                            var contHtml ="<tr>" +
                                "<td><input name='segmentCode' value='"+row.segmentCode+"' class='form-control input-sm validate[required,custom[onlyLetterNumSp]]'></td>" +
                                "<td><input name='paramCode' value='"+row.paramCode+"' class='form-control input-sm validate[required,custom[onlyLetterNumSp]]'></td>" +
                                "<td><input name='replaceSql' value='"+row.replaceSql+"' class='form-control input-sm validate[required]'></td>" +
                                "<td><button class='delete-row-btn' type='button'><i class='glyphicon glyphicon-trash'></i></button></td>" +
                                "</tr>";
                            Table.addRowContent("config-interval-param2",contHtml);
                        }
                        //实际输出参数
                        for(var i = 0;i < outputArgs.length;i++){
                            var row = outputArgs[i];
                            var contHtml ="<tr>" +
                                "<td><input name='paramCode'  class='form-control input-sm validate[required,custom[onlyLetterNumSp]]' value='"+row.paramCode+"'></td>" +
                                "<td><select name='paramType' class='form-control'>" +
                                "<option value='0' "+(row.paramType=='0'?" selected='selected'":"")+">int</option>" +
                                "<option value='1' "+(row.paramType=='1'?" selected='selected'":"")+">string</option>" +
                                "<option value='2' "+(row.paramType=='2'?" selected='selected'":"")+">float</option>" +
                                "<option value='3' "+(row.paramType=='3'?" selected='selected'":"")+">date</option>" +
                                "<option value='4' "+(row.paramType=='4'?" selected='selected'":"")+">datetime(yyyy-MM-dd HH:mm:ss)</option></select></td>" +
                                "<td><input name='paramDesc' class='form-control input-sm' value='"+row.paramDesc+"'></td>" +
                                "<td><button class='delete-row-btn' type='button'><i class='glyphicon glyphicon-trash'></i></button></td>" +
                                "</tr>";
                            Table.addRowContent("config-return-param2",contHtml);
                        }
                        if(readOnly){
                            disableForm("dismissEventForm");
                            $("#inner_update_log_row").css("display","none");
                        }
                    });
                });  
            } else {
                layer.msg("读取服务出错"+data.resultInfo);
            }
        },
        error:function(s,e,k){
            layer.msg("读取服务出错");
        }
    });
}
//初始化数据源
var initDataSource = function(afterFunction) {
	$.ajaxExtend({
		url : serviceHost+"/configer/v1/querydatasource",
		async : true,
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
					//数据源下拉框字符串
					var option = "<option value='" + dataRow.id + "'>"
							+ dataRow.sourceName + "</option>";
					obj.append(option);
				}
				if(afterFunction){
				    afterFunction();
				}
			} else {
				layer.msg("初始化数据源失败");
			}
		}
	});
}
//初始化选中的数据源下面的数据表
var initDataTable = function(afterFunction){
	var dataSourceKey  =$("#dataSource").val();
	if(dataSourceKey==""){
		 layer.msg("数据源列表为空");
		return;
	}
    $.ajaxExtend({
        url : serviceHost+"/configer/v1/queryAllTables.json",
        async : true,
        type : "get",
        dataType : "json",
        data : {
            dataSource : $("#dataSource").val()
        },
        success : function(obj) {
            var data = eval(obj).datas;
            if (data != null) {
                var obj = $("#datatableName");
                obj.empty();
                //下拉框的候选字段
                var optionTags=[];
                for ( var index in data) {
                    var dataRow = data[index];
                    //数据表下拉框面的字符串
                    
                    var option = "<option value='" + dataRow + "'>"
                            + dataRow + "</option>";
                    //obj.append(option);
                    optionTags.push(dataRow);
                }
                triggerAutoComplete(optionTags,"#datatableName","#autoCompleteSelect",function(ui){
                    $("#datatableName").val(ui.item.value);
                    clearNavTable();
                    initDataTableField(); 
                });
                if(afterFunction){
                    afterFunction();
                } 
            } else {
                layer.msg("初始化数据源中的数据表失败");
            }
        }
    });
}
//初始化数据表的字段
var initDataTableField = function(afterFunction){//alert($("#datatableName").val());
    $.ajaxExtend({
        url : serviceHost+"/configer/v1/queryTableColumn.json",
        async : true,
        global:false,
        type : "get",
        dataType : "json",
        data : {
            dataSource : $("#dataSource").val(),
            tableName : $("#datatableName").val()
        },
        success : function(obj) {
            var data = eval(obj).datas;
            if (data != null && obj.resultCode==0) {
               var newTableFieldOption="";
               var newSelectedTableFieldsDateType="";
               //表字段描述的JSON MAP
               selectedTableFieldsDesc={};
             //表字段类型的JSON MAP
               selectedTableFieldsType={};
                for ( var i=0;i<data.length;i++) {
                    var dataRow = data[i];
                     //字段的下拉框
                    newTableFieldOption += "<option value='" + dataRow.columnName + "'>"
                            + dataRow.columnName + "</option>";
                    selectedTableFieldsDesc[dataRow.columnName]=dataRow.columnDesc;
                    selectedTableFieldsType[dataRow.columnName]=dataRow.columnType;
                   //时间区间的字段下拉框
                    var fType = dataRow.columnType.toLocaleLowerCase();
                    if(fType.indexOf("date")>=0||fType.indexOf("datetime")>=0||fType.indexOf("timestamp")>=0||fType.indexOf("time")>=0){
                        newSelectedTableFieldsDateType += "<option value='" + dataRow.columnName + "'>"
                        + dataRow.columnName + "</option>";
                    }
                }
                selectedTableFields = newTableFieldOption;
                selectedTableFieldsDateType = newSelectedTableFieldsDateType;
                initMutilTableList();
                if(afterFunction){
                    afterFunction();
                } 
            } else {
                selectedTableFields = "";
                selectedTableFieldsDateType="";
                //页面被跳转了之后就不提示
                if($("#dataSource").val()){
                    layer.msg("数据源连接失败导致获取表字段出错");
                }
                
            }
        },
        error:function(){
          //页面被跳转了之后就不提示
            if($("#dataSource").val()){
                layer.msg("数据源连接失败导致获取表字段出错");
            }
        }
    }); 
};
var postConfigerSave = function(args, confId){
    //新增的服务接口地址
    var url = serviceHost+"/configer/v1/addconfiger.json";
    //有服务ID　就表示是编辑
    if(confId){
        args.configId =  confId;
        //修改的服务接口地址
        url = serviceHost+"/configer/v1/modifyconfiger.json";
    }
    $.ajaxExtend({
        url : url,
        async : true,
        type : "post",
        dataType : "json",
        data : args,
        success : function(data) { 
            if (data != null && data.resultCode==0) {
                layer.msg("保存成功！");
                var url="./pages/serviceManage/inner/list.html";
                $("#digitalChinaCurrentUrl").val(url);
                $("#tab-content-body").load(url);
            } else {
                layer.msg("保存服务出错："+data.resultInfo);
            }
        },
        error:function(s){
            layer.msg("保存服务出错");
        }
    }); 
}

function trim(str) { // 删除左右两端的空格
	return str.replace(/(^\s*)|(\s*$)/g, "");
}
//初始化服务目录
var initCatalog = function() {
	$("#serverMenu").empty();
	$.ajaxExtend({
		url : serviceHost + "/configer/v1/querycatalogtree",
		async : true,
		type : "get",
		dataType : "json",
		data : {
			parentId : 1,
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
					$("#serverMenu").append(option);
					;
				}
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
var clearNavTable = function(){
    $("#config-request-param1").find("tbody").empty();
    Table.createParamsTable("config-request-param1");
    $("#config-interval-param1").find("tbody").empty();
    Table.createParamsTable("config-interval-param1");
    $("#config-return-param1").find("tbody").empty(); 
    Table.createParamsTable("config-return-param1");
};
$(function() {
    $("#inner_update_log_row").css("display",(hasConfigId?"":"none"));
	initDataSource(function(){ 
	    initCatalog();
	    initDataTable(function(){
	      //如果是编辑服务就要先把已有信息填充到界面上
	        initDataTableField(initIsEditConfig);    
	    }); 
	});

	
	//数据源改变
	$("#dataSource").change(function(){
	    clearNavTable();
	    initDataTable(initDataTableField); 
	});
	//数据表改变事件
	/*$("#datatableName").change(function(){
	    
	    clearNavTable();
	    initDataTableField(); 
	});*/
	
	//生成  按钮
	$("#generoyBtn").on('click',function(){
        // 验证表达，成功才执行ajax请求
	    if (!$("#argdismissEventForm").validationEngine("validate")) {
           // return;
        }
	    var navInputArgs = Table.getTableValues("config-request-param1");
	    var navDateRangeArgs = Table.getTableValues("config-interval-param1");
	    var navOutputArgs = Table.getTableValues("config-return-param1");
	    
	    var inputArgsHtml = "";
	    
	    for(var i=0;i<navInputArgs.length;i++){
	        //空数据
	        if(!navInputArgs[i].paramCode){
	            continue;
	        }
	        inputArgsHtml += "<tr>" +
                "<td><input name='paramCode' class='form-control input-sm validate[required,custom[onlyLetterNumSp]]' value='"+navInputArgs[i].paramCode+"'></td>" +
                "<td><select name='paramType' class='form-control'>" +
                "<option value='0' "+generalColumnType("int",navInputArgs[i].columnCode)+">int</option>" +
                "<option value='1' "+generalColumnType("string",navInputArgs[i].columnCode)+">string</option>" +
                "<option value='2' "+generalColumnType("float",navInputArgs[i].columnCode)+">float</option>" +
                "<option value='3' "+generalColumnType("date",navInputArgs[i].columnCode)+">date</option>" +
                "<option value='4' "+generalColumnType("datetime",navInputArgs[i].columnCode)+">datetime(yyyy-MM-dd HH:mm:ss)</option></select></td>" +
                "<td><input name='isRequired' type='checkbox' class='form-control-checkbox' "+(navInputArgs[i].isRequired?" checked='checked'":"")+"></td>" +
                "<td><input name='paramDesc' class='form-control input-sm' value='"+navInputArgs[i].columnDesc+"'></td>" +
                "<td><button class='delete-row-btn' type='button'><i class='glyphicon glyphicon-trash'></i></button></td>" +
                "</tr>";
	    }
	    var outputArgsHtml ="";
	    var jsonLias={};
	    for(var i=0;i<navOutputArgs.length;i++){
	        //空数据
            if(!navOutputArgs[i].columnLias){
                layer.msg("输出参数的JSON别名为空："+navOutputArgs[i].columnCode);
                continue;
            }
            if(jsonLias[navOutputArgs[i].columnLias]){
                layer.msg("返回参数的别名应不能相同："+navOutputArgs[i].columnLias);
                return;
            }else{
                jsonLias[navOutputArgs[i].columnLias]=navOutputArgs[i].columnLias;
            }
	        outputArgsHtml += "<tr>" +
                "<td><input name='paramCode' class='form-control input-sm validate[required,custom[onlyLetterNumSp]]' value='"+navOutputArgs[i].columnLias+"'></td>" +
                "<td><select name='paramType' class='form-control'>" +
                "<option value='0' "+generalColumnType("int",navOutputArgs[i].columnCode)+">int</option>" +
                "<option value='1' "+generalColumnType("string",navOutputArgs[i].columnCode)+">string</option>" +
                "<option value='2' "+generalColumnType("float",navOutputArgs[i].columnCode)+">float</option>" +
                "<option value='3' "+generalColumnType("date",navOutputArgs[i].columnCode)+">date</option>" +
                "<option value='4' "+generalColumnType("datetime",navOutputArgs[i].columnCode)+">datetime(yyyy-MM-dd HH:mm:ss)</option></select></td>" +
                "<td><input name='paramDesc' class='form-control input-sm' value='"+navOutputArgs[i].columnDesc+"'></td>" +
                "<td><button class='delete-row-btn' type='button'><i class='glyphicon glyphicon-trash'></i></button></td>" +
                "</tr>";
        }
	    
	    //输入参数
	    $("#config-request-param2").find("tbody").empty();
        Table.createParamsTable("config-request-param2");
	    Table.addRowContent("config-request-param2",inputArgsHtml);
	    //返回参数
	    $("#config-return-param2").find("tbody").empty();
        Table.createParamsTable("config-return-param2");
        Table.addRowContent("config-return-param2",outputArgsHtml);
        
        //可选参数   就是SQL片段
        var segmentHtml="";
        var segmentSql = "";
        var whereSql="";
        for(var i=0;i<navInputArgs.length;i++){
          //空数据
            if(!navInputArgs[i].paramCode){
                layer.msg("输入参数的名字为空："+navInputArgs[i].columnCode);
                continue;
            }
            if(!navInputArgs[i].isRequired){
                segmentHtml+="<tr>" +
                "<td><input name='segmentCode' class='form-control input-sm validate[required,custom[onlyLetterNumSp]]' value='segment"+i+"'></td>" +
                "<td><input name='paramCode' class='form-control input-sm validate[required,custom[onlyLetterNumSp]]' value='"+navInputArgs[i].paramCode+"'></td>" +
                "<td><input name='replaceSql' class='form-control input-sm' value='and "+
                navInputArgs[i].columnCode +" " + navInputArgs[i].operator + " :"+navInputArgs[i].paramCode+" " + "'></td>" +
                "<td><button class='delete-row-btn' type='button'><i class='glyphicon glyphicon-trash'></i></button></td>" +
                "</tr>";
                segmentSql+=" {segment"+i+"}";
            }else{
                whereSql+= " and "+ navInputArgs[i].columnCode +" " + navInputArgs[i].operator + " :"+navInputArgs[i].paramCode+" " ;
            }
        }
        $("#config-interval-param2").find("tbody").empty();
        Table.createParamsTable("config-interval-param2");
        Table.addRowContent("config-interval-param2",segmentHtml);
        
        //最后生成SQL
        var sql = "";
        var orderBySql="";
        for(var i=0;i<navOutputArgs.length;i++){
            sql +="," + navOutputArgs[i].columnCode + (navOutputArgs[i].columnLias?(" as " + navOutputArgs[i].columnLias):"") ;
            if(navOutputArgs[i].sortOrder){
                orderBySql +=" order by "+ navOutputArgs[i].columnCode + " " + navOutputArgs[i].sortOrder;
            }
        }
        if(navOutputArgs.length == 0){
            layer.msg("输出参数个数不能为0 ！");
            return;
        }
        sql = "select "+sql.substring(1)+" from " + $("#datatableName").val() 
              +" where 1=1 {range} "+ whereSql + segmentSql + orderBySql;
         if(sql.indexOf("undefined")>=0){
             layer.msg("数据内容不完整，生成的SQL有错误！！")
         }
         $("#querySql").val(sql);
         generalSQL=sql;
	});
	$("#querySql").blur(function(){
	    if(generalSQL!=$("#querySql").val()){
	        layer.msg("SQL语句已经改变，请查看返回参数与输入参数！");
	        generalSQL=$("#querySql").val();
	    }
	});
});
