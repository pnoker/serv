var initArgsTable = function(){
    //输入参数
    var tableId = 'out-config-request-param'; 
    Table.createParamsTable(tableId);     
    var rowContent = "<tr>" +
    "<td><input name='paramCode' class='form-control input-sm validate[required,maxSize[50],custom[onlyLetterNumSp]]'></td>" +
    "<td><select name='paramType' class='form-control'><option value='0'>int</option><option value='1'>string</option><option value='2'>float</option>" +
    "<option value='3'>date</option><option value='4'>datetime(yyyy-MM-dd HH:mm:ss)</option></select></td>" +
    "<td><input name='isRequired' type='checkbox' class='form-control-checkbox'></td>" +
    "<td><input name='paramDesc' class='form-control input-sm'></td>" +
    "<td><button class='delete-row-btn' type='button'><i class='glyphicon glyphicon-trash'></i></button></td>" +
    "</tr>";
    $("#out-config-request-param-btn").off("click");
    $("#out-config-request-param-btn").on("click",{"rowContent":rowContent,"tableId":tableId},Table.addRow);
    //输出参数
    var returnTableId2 = 'out-config-return-param'; 
    Table.createParamsTable(returnTableId2);    
    var returnRowContent2 = "<tr>" +
        "<td><input name='paramCode' class='form-control input-sm validate[required,maxSize[50],custom[onlyLetterNumSp]]'></td>" +
        "<td><select name='paramType' class='form-control'><option value='0'>int</option><option value='1'>string</option><option value='2'>float</option>" +
        "<option value='3'>date</option><option value='4'>datetime(yyyy-MM-dd HH:mm:ss)</option></select></td>" +
        "<td><input name='paramDesc' class='form-control input-sm'></td>" +
        "<td><button class='delete-row-btn' type='button'><i class='glyphicon glyphicon-trash'></i></button></td>" +
        "</tr>";
    $("#out-config-return-param-btn").off("click");
    $("#out-config-return-param-btn").on("click",{"rowContent":returnRowContent2,"tableId":returnTableId2},Table.addRow);
    
};
var initCatalog = function() {
    $("#serverMenu").empty();
    $.ajaxExtend({
        url : serviceHost + "/configer/v1/querycatalogtree",
        async : false,
        type : "get",
        dataType : "json",
        data : {
            parentId : 2,
            isService : 0,
            isDeleted : 0
        },
        success : function(obj) {
            var data = eval(obj).datas;
            if (data != null && data.length > 0) {

                for (var i = 0; i < data.length; i++) {
                    var dataRow = data[i];
                    var level = dataRow.treeLevel;

                    var option = '<option value="' + dataRow.id + '">' + getLevelPrefix(level) + dataRow.catalogName + '</option>';
                    $("#serverMenu").append(option);
                    ;
                }
            }
        }
    });
};
var initIsEditOutConfig = function(){
    if(!hasOutConfigId){
        return;
    }
    //取数据
    $.ajaxExtend({
        url : serviceHost+"/configer/v1/queryoutconfig.json",
        async : false,
        type : "get",
        dataType : "json",
        data : {
            outConfigId : hasOutConfigId
        },
        success : function(data) {
            if (data != null && data.resultCode==0) {
                var conf = data.config;
                 $("#serverMenu").val(conf.parentId); 
                 $("#requestExampleUrl").val(conf.requestExampleUrl); 
                 $("#serviceName").val(conf.serviceName);
                 $("#serviceCode").val(conf.serviceCode);
                 $("#configRemark").val(conf.configRemark);
                 $("#otherInfo").val(conf.otherInfo);
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
                var inputArgs = conf.inputArgs; 
                var outputArgs = conf.outputArgs;
                //实际输出参数
                for(var i = 0;i < outputArgs.length;i++){
                    var row = outputArgs[i];
                    var contHtml ="<tr>" +
                        "<td><input name='paramCode' class='form-control input-sm validate[required,maxSize[50],custom[onlyLetterNumSp]]' value='"+row.paramCode+"'></td>" +
                        "<td><select name='paramType' class='form-control'>" +
                        "<option value='0' "+(row.paramType=='0'?" selected='selected'":"")+">int</option>" +
                        "<option value='1' "+(row.paramType=='1'?" selected='selected'":"")+">string</option>" +
                        "<option value='2' "+(row.paramType=='2'?" selected='selected'":"")+">float</option>" +
                        "<option value='3' "+(row.paramType=='3'?" selected='selected'":"")+">date</option>" +
                        "<option value='4' "+(row.paramType=='4'?" selected='selected'":"")+">datetime(yyyy-MM-dd HH:mm:ss)</option></select></td>" +
                        "<td><input name='paramDesc' class='form-control input-sm' value='"+row.paramDesc+"'></td>" +
                        "<td><button class='delete-row-btn' type='button'>-</button></td>" +
                        "</tr>";
                    Table.addRowContent("out-config-return-param",contHtml);
                }
                // 实际输入参数
                for(var i = 0;i < inputArgs.length;i++){
                    var row = inputArgs[i];
                    var contHtml ="<tr>" +
                        "<td><input name='paramCode' class='form-control input-sm validate[required,maxSize[50],custom[onlyLetterNumSp]]' value='"+row.paramCode+"'></td>" +
                        "<td><select name='paramType' class='form-control'>" +
                        "<option value='0' "+(row.paramType=='0'?" selected='selected'":"")+">int</option>" +
                        "<option value='1' "+(row.paramType=='1'?" selected='selected'":"")+">string</option>" +
                        "<option value='2' "+(row.paramType=='2'?" selected='selected'":"")+">float</option>" +
                        "<option value='3' "+(row.paramType=='3'?" selected='selected'":"")+">date</option>" +
                        "<option value='4' "+(row.paramType=='4'?" selected='selected'":"")+">datetime(yyyy-MM-dd HH:mm:ss)</option></select></td>" +
                        "<td><input name='isRequired' "+(row.isRequired==1?" checked='checked'":"")+" type='checkbox' class='form-control-checkbox'></td>" +
                        "<td><input name='paramDesc' class='form-control input-sm' value='"+row.paramDesc+"'></td>" +
                        "<td><button class='delete-row-btn' type='button'>-</button></td>" +
                        "</tr>";
                    Table.addRowContent("out-config-request-param",contHtml);
                }
                //JSON 示例
               var jsonExamp = conf.exampleList;
               if(jsonExamp && jsonExamp.length > 0){
                   for(var i=0;i<jsonExamp.length;i++){
                       if(jsonExamp[i].resultType=="json"){
                           $("#jsonExamp").val(jsonExamp[i].resultExample);
                           $("#JSONexampBox").css("display","");
                       }else if(jsonExamp[i].resultType=="xml"){
                           $("#xmlExamp").val(jsonExamp[i].resultExample);
                           $("#XMLexampBox").css("display","");
                       }
                       
                   }
               }

               if(readOnly){
                   disableForm("dismissEventForm");
                   $("#out_update_log_row").css("display","none");
               }
            }
        },       
        error:function(s,e,k){
            layer.msg("读取服务出错");
        }
    });
};
$(function() {
    initArgsTable();
    initCatalog();
    hideLeftMenu();
    $("#out_update_log_row").css("display",(hasOutConfigId?"":"none"));
    $('#saveBtn').on('click', function() {
        // 验证表达，成功才执行ajax请求
        if (!$("#dismissEventForm").validationEngine("validate")) {
            return;
        }
        var postArgs = {};
        postArgs.parentId = $("#serverMenu").val();
        postArgs.serviceName = $("#serviceName").val();
        postArgs.configRemark = $("#configRemark").val();
        postArgs.serviceCode = $("#serviceCode").val();
        postArgs.requestExampleUrl = $("#requestExampleUrl").val();
        postArgs.jsonExamp = $("#jsonExamp").val();
        postArgs.xmlExamp = $("#xmlExamp").val();
        postArgs.otherInfo = $("#otherInfo").val(); 
        postArgs.resultFormat = "";
        $("input[name=resultFormat]").each(function() {
            if ($(this).attr("checked")) {
                postArgs.resultFormat += "," + $(this).val();
            }
        });
        if (!postArgs.resultFormat || postArgs.resultFormat.length <= 0) {
            layer.msg("选择支持类型");
            return;
        } else {
            postArgs.resultFormat = postArgs.resultFormat.substring(1);
        }
        postArgs.requestMethod = "";
        $("input[name=requestMethod]").each(function() {
            if ($(this).attr("checked")) {
                postArgs.requestMethod += "," + $(this).val();
            }
        });
        if (!postArgs.requestMethod
                || postArgs.requestMethod.length <= 0) {
            layer.msg("选择请求方式");
            return;
        } else {
            postArgs.requestMethod = postArgs.requestMethod .substring(1);
        }
 
        postArgs.inputArgsJson = JSON.stringify(Table .getTableValues("out-config-request-param"));
        postArgs.outputArgsJson = JSON.stringify(Table .getTableValues("out-config-return-param"));
        if(postArgs.outputArgsJson=="[]"){
            layer.msg("要求输出参数不能为空！！");
            return;
        }
        //debugger;
        if(hasOutConfigId){
            postArgs.outConfigId = hasOutConfigId;
            postArgs.editMark = $("#editRemark").val();
        }
        $.ajaxExtend({
            url : serviceHost + "/configer/v1/editoutconfig.json",
            async : false,
            type : "post",
            dataType : "json",
            data : postArgs,
            success : function(data) { 
                if (data != null && data.resultCode==0) {
                    layer.msg("保存成功！");
                    var url = "./pages/serviceManage/outer/list.html";
                    $("#digitalChinaCurrentUrl").val(url);
                     $("#tab-content-body").load(url);
                } else {
                    layer.msg("保存服务出错---"+data.resultInfo);
                }
            },
            error:function(s){
                layer.msg("error-保存服务出错");
            }
        }); 
    });
  //如果是编辑服务就要先把已有信息填充到界面上
    initIsEditOutConfig();    
});

