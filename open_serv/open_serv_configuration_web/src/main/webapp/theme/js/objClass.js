var serviceHost="http://localhost:8080/open_serv_configuration";
//var serviceHost="http://10.0.2.31:8096/serv_configuration";
//var authRootPath = "https://localhost:8080/auth_authority";
//var authRootPath = "https://10.0.2.31:8443/auth_authority";
var path=window.location.protocol + "//" + window.location.host + "/open_serv_configuration_web/";



/**
 * 可编辑的表格对象
 * @param tableId 表格id，在相应html中已定义好
 * @param contentUrl 获取表格数据的后台请求地址
 * @param headUrl    获取表格表头的后台请求地址
 * @param hasCheckbox  是否添加首列复选框 true:是  false:否
 * @param columnDefs   添加操作按钮列时，定义操作列需展示的内容，为数组
 * @param queryType    请求的方法：post/get
 * @param dataType     指定后台返回参数类型:json/xml
 */
function editableObj(tableId,contentUrl,headUrl,columnsAdded,hasCheckbox,columnDefs,queryType,dataType,editorFields,tableColumns){
    this.tableId = tableId;
    this.contentUrl = contentUrl;
    this.headUrl = headUrl;
    this.columnsAdded = columnsAdded;
    this.hasCheckbox = hasCheckbox;
    this.columnDefs = columnDefs;
    this.queryType = queryType;
    this.dataType = dataType;
    this.editorFields = editorFields;
    this.tableColumns = tableColumns;
}
var getQueryString = function(name) {
    var url=$("#digitalChinaCurrentUrl").val();
    if(url =="" || url.indexOf("?")< 0){
        return "";
    }
    var url = url.substr(url.indexOf("?"));    
    var theRequest = new Object();
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for(var i = 0; i < strs.length; i ++) {
            theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
        }
    }
    return theRequest[name];
};

//禁用form表单中所有的input[文本框、复选框、单选框],select[下拉选],多行文本框[textarea]

function disableForm(formId) {
  var isDisabled = true;
  var attr="disable";
  if(!isDisabled){
     attr="enable";
  }
  $("form[id='"+formId+"'] :text").attr("disabled",isDisabled);
  $("form[id='"+formId+"'] textarea").attr("disabled",isDisabled);
  $("form[id='"+formId+"'] select").attr("disabled",isDisabled);
  $("form[id='"+formId+"'] :radio").attr("disabled",isDisabled);
  $("form[id='"+formId+"'] :checkbox").attr("disabled",isDisabled);
  $("form[id='"+formId+"'] :button").attr("disabled",isDisabled);
  $("form[id='"+formId+"'] :button").addClass("btn font-disabled");
  $("#toggleConfigPanel").removeClass("btn font-disabled");
  $("#generoyBtn").removeClass("btn font-disabled");
  $("#generoyBtn").addClass("btn disabled");
  
  //禁用jquery easyui中的下拉选（使用input生成的combox）

  $("#" + formId + " input[class='combobox-f combo-f']").each(function () {
      if (this.id) {alert("input"+this.id);
          $("#" + this.id).combobox(attr);
      }
  });
  
  //禁用jquery easyui中的下拉选（使用select生成的combox）
  $("#" + formId + " select[class='combobox-f combo-f']").each(function () {
      if (this.id) {
      alert(this.id);
          $("#" + this.id).combobox(attr);
      }
  });
  
  //禁用jquery easyui中的日期组件dataBox
  $("#" + formId + " input[class='datebox-f combo-f']").each(function () {
      if (this.id) {
      alert(this.id)
          $("#" + this.id).datebox(attr);
      }
  });
}
/**
 * 对象查询表格
 * @param tableId  表格id，在相应html中已定义好
 * @param contentUrl  获取表格数据的后台请求地址
 * @param headUrl     获取表格表头的后台请求地址
 * @param columnsAmount 添加的自定义列的数量
 * @param columnsAdded  添加的自定义列的内容
 * @param columnParams 自定义表头变量名
 * @param columnDefs   新增自定义列
 * @param queryType    后台请求方式  post/get
 * @param dataType     后台返回查询结果数据类型   json/xml
 * @param callBackFn   行回调函数，包括参数row,data,iDisplayIndex
 */
function queryTableObj(tableId,contentUrl,headUrl,columnParams,columnDefs,queryType,dataType,callBackFn,ajaxData){
	this.tableId = tableId;
    this.contentUrl = contentUrl;
    this.headUrl = headUrl;
    this.columnParams = columnParams;
    this.columnDefs = columnDefs;
    this.queryType = queryType;
    this.dataType = dataType;
    this.callBackFn = callBackFn;
    this.ajaxData = ajaxData;
}

/**
 *
 * @param headUrl   获取表格表头的后台请求地址
 * @param id        表格id，在相应html中已定义好
 * @param columnsAdded   添加的自定义列的表头内容
 * @param hasCheckbox    是否添加首列复选框 true:是  false:否
 */
function editableTableHead(headUrl,id,columnsAdded,hasCheckbox){
    this.id = id;
    this.headUrl = headUrl;
    this.columnsAdded = columnsAdded;
    this.hasCheckbox = hasCheckbox;
}