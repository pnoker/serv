var generalColumnType = function(optionType,columnName){
    
      var  columnType = selectedTableFieldsType[columnName].toLocaleLowerCase();
        var selectStr=" selected='selected' ";
        if(optionType=="int"){
            if(columnType=="number"||(columnType.indexOf("number(")>=0&&columnType.indexOf(",")<0)){//
                return selectStr;
            }
            //SQL Server 类型
            if(columnType=="bit"||columnType=="tinyint"||columnType=="smallint"||columnType=="int"){
                return selectStr;
            }
            
        }else if(optionType=="string"){
            if(columnType.indexOf("char")>=0||columnType.indexOf("text")>=0 ||columnType.indexOf("clob")>=0   ){
                return selectStr;
            }
        }
        else if(optionType=="float"){
            if(columnType.indexOf("number(")>=0&&columnType.indexOf(",")>0){
                return selectStr;
             }
            //SQL Server 类型
            if(columnType=="real"||columnType=="decimal"||columnType=="numeric"||columnType=="money"||columnType=="float"){
                return selectStr;
            }
        }
        else if(optionType=="date"){
            if(columnType.indexOf("date")>=0){
                return selectStr;
            }
        }
        else if(optionType=="datetime"){
            if(columnType.indexOf("datetime")>=0||columnType.indexOf("timestamp")>=0||columnType.indexOf("time")>=0){
                return selectStr;
            }
        }
        return "";     
    }

var filedChanged = function(thisCtl,val){      
    var desc = selectedTableFieldsDesc[val];
    $(thisCtl).parent().next().find("input").val(desc);
    $(thisCtl).parent().parent().find("input[name=columnDesc]").val(desc);
    $(thisCtl).parent().next().next().find("input").val(val);
    //运算规则的过滤字符只能为等号
    var olyEqule = "<option  value='=' selected='selected'>等于</option>";
    var allOperator = "<option value='>'>大于</option><option value='<'>小于</option><option  value='=' selected='selected'>等于</option>";
    var opSelet = $(thisCtl).parent().parent().find("select[name=operator]");
    opSelet.empty();
    if(generalColumnType("string",val).length>0){
        //是字符类型
        opSelet.append(olyEqule);
    }else{
        opSelet.append(allOperator);
    }
}
var outFiledChanged = function(thisCtl,val){ 
   // debugger;
    $(thisCtl).parent().parent().find("input[name=columnDesc]").val(selectedTableFieldsDesc[val]);
    $(thisCtl).parent().next().find("input").val(val);
}
var filedDescChanged = function(thisCtl,val){ 
   // $(thisCtl).parent().parent().find("input[name=columnDesc]").val(val);
}

//请求参数配置1

var orderBy = function(thisObj,tableId){
    if(!$(thisObj).hasClass('active')){
        $('#'+tableId).find('.orderBy').removeClass('active');
        $('#'+tableId).find('.orderBy').parent().find("input").removeAttr('name');
        $(thisObj).addClass('active');
        $(thisObj).parent().find("input").attr("name","sortOrder");
        if($(thisObj).attr('index')=='1'){
            $(thisObj).parent().find("input").val('desc');
        }else{
            $(thisObj).parent().find("input").val('asc');
        }
    }
};

var initMutilTableList = function(first ){

    var tableId = 'config-request-param1';
    if(first){
        Table.createParamsTable(tableId);
    }
    var rowContent = "<tr>" +
        "<td><select onchange='filedChanged(this,this.value)' name='columnCode' class='form-control'>"+selectedTableFields+"</select></td>" +
        "<td><input class='form-control input-sm' disabled=true onchange='filedDescChanged(this,this.value)'></td>" +
        "<td><input name='paramCode' value='ID' class='form-control input-sm validate[required,maxSize[30],custom[onlyLetterNumSp]]'></td>" +
        "<td><input name='isRequired' type='checkbox' class='form-control-checkbox'></td>" +
        "<td><select name='operator' class='form-control'><option value='>'>大于</option><option value='<'>小于</option><option  value='=' selected='selected'>等于</option></select></td>" +
        "<td><input name='columnDesc' class='form-control input-sm'></td>" +
        "<td><button class='delete-row-btn' type='button'><i class='glyphicon glyphicon-trash'></i></button></td>" +
        "</tr>";
    $("#addRequestBtn1").off("click");
    $("#addRequestBtn1").on("click",{
        "rowContent":rowContent,
        "tableId":tableId,
        "callFun":function(){
            var filed = $("#"+tableId).find("tr:last-child").find("select[name=columnCode]").val();
            $("#"+tableId).find("tr:last-child").find("input[name=paramCode]").val(filed);
            $("#"+tableId).find("tr:last-child").find("input:first").val(selectedTableFieldsDesc[filed]);
            $("#"+tableId).find("tr:last-child").find("input[name=columnDesc]").val(selectedTableFieldsDesc[filed]);
        }
    },Table.addRow);
    
    //区间参数配置1
    var intervalTableId = 'config-interval-param1';
    if(first){
        Table.createParamsTable(intervalTableId);
    }
    var intervalRowContent = "<tr>" +
        "<td><select name='columnCode' class='form-control'>"+selectedTableFieldsDateType+"</select></td>" +
        "<td>近<input name='dataRange' class='form-control-interval validate[required,custom[onlyNumberSp]]'>天（不包括今天）</td>" +
        "<td><button class='delete-row-btn' type='button'><i class='glyphicon glyphicon-trash'></i></button></td>" +
        "</tr>";
    $("#addIntervelBtn1").off("click");
    $("#addIntervelBtn1").on("click",{"rowContent":intervalRowContent,"tableId":intervalTableId},Table.addRow);
    
    //返回参数配置1
    var returnTableId = 'config-return-param1';
    if(first){
        Table.createParamsTable(returnTableId);
    }
    var returnRowContent = "<tr>" +
        "<td><select name='columnCode' onchange='outFiledChanged(this,this.value)' class='form-control'>"+selectedTableFields+"</select></td>" +
        "<td><input name='columnLias' value='ID' class='form-control input-sm validate[required,maxSize[30],custom[onlyLetterNumSp]]'></td>" +
        "<td><input name='columnDesc' class='form-control input-sm'></td>" +
        "<td><input type='hidden' value='desc'><a index ='1' onclick='orderBy(this,\""+returnTableId+"\")' href='javascript:;' class='orderBy'><i class='glyphicon glyphicon-arrow-down'></i></a><a index ='2' onclick='orderBy(this,\""+returnTableId+"\")' class='orderBy' href='javascript:;'><i class='glyphicon glyphicon-arrow-up'></i></a></td>" +
        "<td><button class='delete-row-btn' type='button'><i class='glyphicon glyphicon-trash'></i></button></td>" +
        "</tr>";
    $("#addReturnBtn1").off("click");
    $("#addReturnBtn1").on("click",{
        "rowContent":returnRowContent,
        "tableId":returnTableId,
        "callFun":function(){
            
            var filed = $("#"+returnTableId).find("tr:last-child").find("select[name=columnCode]").val();
            $("#"+returnTableId).find("tr:last-child").find("input[name=columnLias]").val(filed);
            $("#"+returnTableId).find("tr:last-child").find("input[name=columnDesc]").val(selectedTableFieldsDesc[filed]);
        }
      },Table.addRow);
    
    //请求参数配置2
    var tableId2 = 'config-request-param2';
    if(first){
        Table.createParamsTable(tableId2);
    }
    //0:int,1:string,2:float,3:date,4:datetime, 时间格式为标准格式（yyyy-MM-dd HH:mm:ss)
    var rowContent2 = "<tr>" +
        "<td><input name='paramCode' class='form-control input-sm validate[required,maxSize[30],custom[onlyLetterNumSp]]'></td>" +
        "<td><select name='paramType' class='form-control'><option value='0'>int</option><option value='1'>string</option><option value='2'>float</option>" +
        "<option value='3'>date</option><option value='4'>datetime(yyyy-MM-dd HH:mm:ss)</option></select></td>" +
        "<td><input name='isRequired' type='checkbox' class='form-control-checkbox'></td>" +
        "<td><input name='paramDesc' class='form-control input-sm'></td>" +
        "<td><button class='delete-row-btn' type='button'><i class='glyphicon glyphicon-trash'></i></button></td>" +
        "</tr>";
    $("#addRequestBtn2").off("click");
    $("#addRequestBtn2").on("click",{"rowContent":rowContent2,"tableId":tableId2},Table.addRow);
    
    //区间参数配置2
    var intervalTableId2 = 'config-interval-param2';
    if(first){
        Table.createParamsTable(intervalTableId2);
    }
    var intervalRowContent2 = "<tr>" +
        "<td><input name='segmentCode' class='form-control input-sm validate[required,maxSize[30],custom[onlyLetterNumSp]]'></td>" +
        "<td><input name='paramCode' class='form-control input-sm validate[required,maxSize[30],custom[onlyLetterNumSp]]'></td>" +
        "<td><input name='replaceSql' class='form-control input-sm'></td>" +
        "<td><button class='delete-row-btn' type='button'><i class='glyphicon glyphicon-trash'></i></button></td>" +
        "</tr>";
    $("#addIntervelBtn2").off("click");
    $("#addIntervelBtn2").on("click",{"rowContent":intervalRowContent2,"tableId":intervalTableId2},Table.addRow);
    
    //返回参数配置2
    var returnTableId2 = 'config-return-param2'; 
    if(first){
        Table.createParamsTable(returnTableId2);
    }
    var returnRowContent2 = "<tr>" +
        "<td><input name='paramCode'  class=' form-control input-sm validate[required,maxSize[30],custom[onlyLetterNumSp]]'></td>" +
        "<td><select name='paramType' class='form-control'><option value='0'>int</option><option value='1'>string</option><option value='2'>float</option>" +
        "<option value='3'>date</option><option value='4'>datetime(yyyy-MM-dd HH:mm:ss)</option></select></td>" +
        "<td><input name='paramDesc' class='form-control input-sm'></td>" +
        "<td><button class='delete-row-btn' type='button'><i class='glyphicon glyphicon-trash'></i></button></td>" +
        "</tr>";
    $("#addReturnBtn2").off("click");
    $("#addReturnBtn2").on("click",{"rowContent":returnRowContent2,"tableId":returnTableId2},Table.addRow);
};


//
var toggleConfigAssist = function(){
    $('#toggleConfigPanel').on('click',function(){
        if($(this).find('.glyphicon-chevron-up').hasClass('hide')){
            $(this).find('.glyphicon-chevron-down').addClass('hide');
            $(this).find('.glyphicon-chevron-up').removeClass('hide');
            $(this).parent().parent().next('.first-form-group').addClass('hide');
        }else{
            $(this).find('.glyphicon-chevron-down').removeClass('hide');
            $(this).find('.glyphicon-chevron-up').addClass('hide');
            $(this).parent().parent().next('.first-form-group').removeClass('hide');
        }
    });
};


var tags = [ "c++", "java", "php", "coldfusion", "javascript", "asp", "ruby","ruby1","ruby2","ruby3","ruby4","ruby5","ruby6","ruby7","ruby8","ruby9","ruby10","ruby11","ruby12","ruby13","ruby14","ruby15","ruby16","ruby17","ruby18" ];
$(function() {
    initMutilTableList(true);
    toggleConfigAssist();
    hideLeftMenu();
    var data = {
        dataSource : $("#dataSource").val(),
        tableName : $("#datatableName").val()
    };
    var ajaxCallbackFn = function(data){
        return data;
    };
    //triggerAutoComplete(tags,"#datatableName","#autoCompleteSelect");
    //triggerAutoComplete(tags,"#datatableName","#autoCompleteSelect",serviceHost+"/configer/v1/queryTableColumn.json","GET",data,"json",ajaxCallbackFn);
});