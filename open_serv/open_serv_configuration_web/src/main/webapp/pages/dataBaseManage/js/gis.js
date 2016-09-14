var initGisDataTable = function() {
    var tableId = 'gis-config-data-dictionary';
    Table.createParamsTable(tableId);
    var rowContent = "<tr>" 
        + "<td><input name='filedName' class='form-control input-sm'></td>" 
        + "<td><select name='filedType' class='form-control'>" 
        + "<option value='Float'>Float</option>"
                + "<option value='Double'>Double</option>" 
                + "<option value='Int'>Int</option>" 
                + "<option value='Long'>Long</option>" 
                + "<option value='String'>String</option>"
                + "<option value='Date'>Date</option>" 
                + "</select></td>" 
        +"<td><input name='filedLength' class='form-control input-sm validate[required,,custom[onlyNumberSp]]'></td>"
        + "<td><input name='decimalLength' class='form-control input-sm validate[required,,custom[onlyNumberSp]]'></td>"
        + "<td><select name='isNull' class='form-control'><option value='YES'>YES</option><option value='NO'>NO</option></select></td>"
        + "<td><input name='filedDesc' class='form-control input-sm'></td>" + "<td><input name='fieldRemark' class='form-control input-sm'></td>"
        + "<td><button class='delete-row-btn' type='button'><i class='glyphicon glyphicon-trash'></i></button></td>" + "</tr>";
    $("#addGisConfigBtn").off("click");
    $("#addGisConfigBtn").on("click", {
        "rowContent" : rowContent,
        "tableId" : tableId
    }, Table.addRow);

};
var initCatalog = function() {
    $("#serverMenu").empty();
    $.ajaxExtend({
        url : serviceHost + "/configer/v1/querycatalogtree",
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

                    var option = '<option value="' + dataRow.id + '">' + getLevelPrefix(level) + dataRow.catalogName + '</option>';
                    $("#serverMenu").append(option);
                    ;
                }
            }
        }
    });
};
var initIsEditGisConfig = function(){
    if(!hasGisConfigId){
        return;
    }
    //取数据
    $.ajaxExtend({
        url : serviceHost+"/configer/v1/querygisconfig.json",
        async : false,
        type : "get",
        dataType : "json",
        data : {
            gisConfigId : hasGisConfigId
        },
        success : function(data) {
            if (data != null && data.resultCode==0) {
                var conf = data;
                 $("#serverMenu").val(conf.config.parentId);
                 $("#serviceName").val(conf.config.serviceName); 
                 $("#configRemark").val(conf.config.configRemark);
                 $("#urlTest").val(conf.serveExtendSpace.urlTest);
                 $("#urlFormal").val(conf.serveExtendSpace.urlFormal);
                 //选中几何特征
                 $("input[name=geoFeature][value="+conf.serveExtendSpace.ge0Features+"]").attr("checked",'checked');
                 $("#maxLength").val(conf.serveExtendSpace.maxLength);
                 $("input[name=isVisible][value="+conf.serveExtendSpace.isVisible+"]").attr("checked",'checked');
                 $("input[name=layerType][value="+conf.serveExtendSpace.layerType+"]").attr("checked",'checked');
                 //支持格式多选框
                 var rMethod = conf.config.resultFormat.split(',');
                 for(var i=0;i<rMethod.length;i++){
                     if(rMethod[i]=="json"){
                         $("#resultJSON").attr("checked",'true')
                     }else{
                         $("#resultAMF").attr("checked",'true')
                     }
                 }
                 $("#displayScaleMax").val(conf.serveExtendSpace.displayScaleMax);
                 $("#displayScaleMin").val(conf.serveExtendSpace.displayScaleMin);
                 $("input[name=isLabel][value="+conf.serveExtendSpace.isLabel+"]").attr("checked",'checked');
                 $("input[name=gisServer][value="+conf.serveExtendSpace.gisServer+"]").attr("checked",'checked');
                 $("#coordinateSystem").val(conf.serveExtendSpace.coordinateSystem);
                 $("#coordinateCode").val(conf.serveExtendSpace.coordinateCode);
                 //数据字典
                 var dic = conf.serveExtendSpace.gisDictionaries;
                 for(var i = 0;i < dic.length;i++){
                     var row = dic[i];
                      
                     var contHtml="<tr>" 
                            + "<td><input name='filedName' class='form-control input-sm' value='"+row.filedName+"'></td>" 
                            + "<td><select name='filedType' class='form-control'>" 
                                    + "<option value='Float' "+(row.filedType=='Float'?" selected='selected'":"")+">Float</option>"
                                    + "<option value='Double' "+(row.filedType=='Double'?" selected='selected'":"")+">Double</option>" 
                                    + "<option value='Int' "+(row.filedType=='Int'?" selected='selected'":"")+">Int</option>" 
                                    + "<option value='Long' "+(row.filedType=='Long'?" selected='selected'":"")+">Long</option>" 
                                    + "<option value='String' "+(row.filedType=='String'?" selected='selected'":"")+">String</option>"
                                    + "<option value='Date' "+(row.filedType=='Date'?" selected='selected'":"")+">Date</option>" 
                                    + "</select></td>" 
                            +"<td><input name='filedLength' class='form-control input-sm validate[required,,custom[onlyNumberSp]]' value='"+row.filedLength+"'></td>"
                            + "<td><input name='decimalLength' class='form-control input-sm validate[required,,custom[onlyNumberSp]]' value='"+row.decimalLength+"'></td>"
                            + "<td><select name='isNull' class='form-control'>" +
                            		"<option value='YES' "+(row.isNull=='YES'?" selected='selected'":"")+">YES</option>" +
                            		"<option value='NO' "+(row.isNull=='NO'?" selected='selected'":"")+">NO</option></select></td>"
                            + "<td><input name='filedDesc' class='form-control input-sm' value='"+row.filedDesc+"'></td>" 
                            + "<td><input name='fieldRemark' class='form-control input-sm' value='"+row.fieldRemark+"'></td>"
                            + "<td><button class='delete-row-btn' type='button'><i class='glyphicon glyphicon-trash'></i></button></td>" + "</tr>";
                     Table.addRowContent("gis-config-data-dictionary",contHtml); 
                 }

                 if(readOnly){
                     disableForm("dismissEventForm");
                     $("#gis_update_log_row").css("display","none");
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

initGisDataTable();
$(function() {
    $("#gis_update_log_row").css("display",(hasGisConfigId?"":"none"));
    // 初始化目录
    initCatalog();
    
    $('#gisSaveBtn').on('click', function() {
     // 验证表达，成功才执行ajax请求de
     
        if (!$("#dismissEventForm").validationEngine("validate")) {
            return;
        }
        var postArgs = {};
        postArgs.parentId = $("#serverMenu").val();
        postArgs.serviceName = $("#serviceName").val();
        postArgs.configRemark = $("#configRemark").val();
        postArgs.urlTest = $("#urlTest").val();
        postArgs.urlFormal = $("#urlFormal").val();
        postArgs.geoFeature = $("input[name=geoFeature]:checked ").val();
        postArgs.maxLength = $("#maxLength").val();
        postArgs.isVisible = $("input[name=isVisible]:checked ").val();
        postArgs.layerType = $("input[name=layerType]:checked ").val();
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
        postArgs.displayScaleMax = $("#displayScaleMax").val();
        postArgs.displayScaleMin = $("#displayScaleMin").val();
        postArgs.isLabel = $("input[name=isLabel]:checked ").val();
        postArgs.gisServer = $("input[name=gisServer]:checked ").val();
        postArgs.coordinateSystem = $("#coordinateSystem").val();
        postArgs.coordinateCode = $("#coordinateCode").val();
        postArgs.inputArgsJson = JSON.stringify(Table .getTableValues("gis-config-data-dictionary"));
        if(hasGisConfigId){
            postArgs.gisConfigId = hasGisConfigId;
            postArgs.editMark = $("#editRemark").val();
        }
        $.ajaxExtend({
            url : serviceHost+"/configer/v1/editgisconfig.json",
            async : false,
            type : "post",
            dataType : "json",
            data : postArgs,
            success : function(data) {  
                if (data != null && data.resultCode==0) {
                    layer.msg("保存成功！");
                    var url = "./pages/serviceManage/gis/list.html";
                    $("#digitalChinaCurrentUrl").val(url);
                     $("#tab-content-body").load(url);
                } else {
                    layer.msg("保存服务出错"+data.resultInfo);
                }
            },
            error:function(s){
                layer.msg("error-保存服务出错");
            }
        }); 
    });
  //如果是编辑服务就要先把已有信息填充到界面上
    initIsEditGisConfig();
});