var Table = function(){
    var oTable = new Object();//查询表格对象
    var currQueryTableId;//当前查询表格id

    var editable = new Object();//可编辑表格对象
    var curreditableTableId;//当前可编辑表格id
    var noDataRow;

    var oLanguage={
        "oAria": {
            "sSortAscending": ": 升序排列",
            "sSortDescending": ": 降序排列"
        },
        "oPaginate": {
            "sFirst": "首页",
            "sLast": "末页",
            "sNext": "下一页>",
            "sPrevious": "<上一页"
        },
        "sEmptyTable": "没有相关记录",
        "sInfo": "第 _START_ 到 _END_ 条记录，共 _TOTAL_ 条",
        "sInfoEmpty": "第 0 到 0 条记录，共 0 条",
        "sInfoFiltered": "(从 _MAX_ 条记录中检索)",
        "sInfoPostFix": "",
        "sDecimal": "",
        "sThousands": ",",
        "sLengthMenu": "每页显示 _MENU_条",
        "sLoadingRecords": "正在载入...",
        "sProcessing": "正在载入...",
        "sSearch": "搜索:",
        "sSearchPlaceholder": "",
        "sUrl": "",
        "sZeroRecords": "没有相关记录",
        /*select: {
            rows: {
                _: '%d 行被选中',
                0: '单击选中某行',
                1: '选中了一行'
            }
        },*/
        buttons: {
            selectAll: "全选",
            selectNone: "全不选"
        }
    };
    return {
        /**
         * 绘制可编辑的表格
         * @param tableId 表格id，在相应html中已定义好
         * @param contentUrl 获取表格数据的后台请求地址
         * @param headUrl    获取表格表头的后台请求地址
         * @param columnsAdded 添加的自定义列的列名
         * @param hasCheckbox  是否添加首列复选框 true:是  false:否
         * @param columnDefs   添加操作按钮列时，定义操作列需展示的内容，为数组
         * @param queryType    后台请求方式  post/get
         * @param dataType     后台返回查询结果数据类型   json/xml
         */
          init:function(tableObj){
            var tableId = tableObj.tableId;
            var contentUrl = tableObj.contentUrl;
            var headUrl = tableObj.headUrl;
            var columnsAdded = tableObj.columnsAdded;
            var hasCheckbox = tableObj.hasCheckbox;
            var columnDefs = tableObj.columnDefs;
            var queryType = tableObj.queryType;
            var dataType = tableObj.dataType;
            var editorFields = tableObj.editorFields;
            var tableColumns = tableObj.tableColumns;


            $.fn.dataTable.defaults.oLanguage=oLanguage;
            $.fn.dataTable.Editor.defaults.i18n={
                create: {
                    button: "新建",
                    title: "新建",
                    submit: "提交"
                },
                edit: {
                    button: "编辑",
                    title: "修改",
                    submit: "更新"
                },
                remove: {
                    button: "删除",
                    title: "删除",
                    submit: "删除",
                    confirm: "确定删除选中的 %d 行数据?"
                },
                "error": {
                    "system": "遇到一个系统错误 (请联系我们)"
                },

                "multi": {
                    "title": "多个数据",
                    "info": "所选择的项目包含多个不同的值。要将所选的多行数据修改为相同的值请单击此处，否则它们将保留其原来的值。",
                    "restore": "取消修改"
                },

                "datetime": {
                    "previous": '<<',
                    "next":     '>>',
                    "months":   [ '一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月' ],
                    "weekdays": [ '日', '一', '二', '三', '四', '五', '六' ],
                    "amPm":     [ '上午', '下午' ],
                    "unknown":  '-'
                }
            };

            /**
             * 从后台获取表格的表头信息
             * headUrl:后台请求url
             * hasCheckbox：是否添加第一列复选框
             */
            var columns = createEditorColumn(headUrl,columnsAdded,tableColumns,hasCheckbox,editorFields,queryType,dataType);
            var editor; // use a global for the submit and return data rendering in the examples
            editor = new $.fn.dataTable.Editor( {
                ajax: {
                    "url":contentUrl,
                    "dataSrc": "data",
                    "type":queryType,
                    "dataType":dataType,
                    "complete":function(obj){
                        if(typeof obj.responseText!="undefined"){
                            var result = JSON.parse(obj.responseText);
                            if(result!=null&&result.resultCode!="undefined"){
                                if(result.resultCode=="203"){//没权限，弹出提示框
                                    layer.msg(result.resultInfo);
                                    return false;
                                }else if(result.resultCode=="302"){//没登录，跳到登录页面
                                    location.href ='./NewLogin.html';
                                    return false;
                                }else if(result.resultCode=="404"){//权限系统数据库未配置相应功能模块
                                    alert(result.resultInfo);
                                    return false;
                                }
                            }
                            //alert("ajax:"+JSON.stringify(obj.responseText));
                        }
                        
                    }
                    /*,
                     "global":false*/
                },
                table: "#"+tableId,
                fields:columns.editorFields
            } );

            $('#'+tableId).on( 'dblclick', 'tbody td.editable', function (e) {
                /*$(".DTE_Field").each(function(){
                 var $this = $(this).find("div[data-dte-e='input-control']").children();
                 var currClass = $this.attr("class");
                 if(currClass != null && currClass != undefined && currClass != '' &&currClass.indexOf("validate") != -1){
                 var currValidator = currClass.substring(currClass.indexOf("validate"),currClass.length);
                 $this.removeClass(currValidator);
                 }
                 });*/
                editor.inline( this, {
                    buttons: {
                        fn: function () {
                            this.submit();
                        },
                        className:"glyphicon glyphicon-ok edit-ok"
                    }
                } );
            } );

            	
            columnDefs = Table.createColumnDefs(tableId,columnDefs,headUrl);
            if(tableId != curreditableTableId){
                editable = $('#'+tableId).DataTable( {
                    dom: "Bfrtip",
                    ajax: {
                        "url":contentUrl,
                        "dataSrc": "data",
                        "type":queryType,
                        "dataType":dataType,
                        "dom": '<"top"f>rt<"bottom"ilp><"clear">',
                        "complete":function(obj){
                            if(typeof obj.responseText!="undefined"){
                                var result = JSON.parse(obj.responseText);
                                if(result!=null&&result.resultCode!="undefined"){
                                    if(result.resultCode=="203"){//没权限，弹出提示框
                                        layer.msg(result.resultInfo);
                                        return false;
                                    }else if(result.resultCode=="302"){//没登录，跳到登录页面
                                        location.href ='./login.html';
                                        return false;
                                    }else if(result.resultCode=="404"){//权限系统数据库未配置相应功能模块
                                        alert(result.resultInfo);
                                        return false;
                                    }
                                }
                                //alert("ajax:"+JSON.stringify(obj.responseText));
                            }
                            
                        }
                        /*,
                         "global":false*/
                    },
                    initComplete:function(){

                    },
                    autoWidth:false,
                    columns: columns.tableColumns,
                    order: [ 1, 'asc' ],
                    iDisplayLength:10,
                    bProcessing:true,
                    bServerSide:false,
                    select: {
                        style:    'multi',
                        selector: 'td:first-child'
                    },
                    buttons: [
                        { extend: "create", editor: editor,className:"btn-green",formButtons:[{label:"提交",className:"submit"}]},
                        { extend: "edit",   editor: editor,className:"btn-green"},
                        { extend: "remove", editor: editor,className:"btn-green"},
                        { extend: 'selectAll',className:"btn-green"},
                        { extend: 'selectNone',className:"btn-green"}
                    ],
                   // "bPaginate": true, //翻页功能
                    "bLengthChange": true, //改变每页显示数据数量
                    "columnDefs" : columnDefs,
                    "rowCallback": function(row, data) {
                    },
                    "preDrawCallback":function(settings, json){
                    }

                } );
            }else{
                editable.ajax.url(contentUrl).load();
            }
            this.conditionQuery(tableId,contentUrl,editable);
            this.addFormItemValidator();
            currTableId = tableId;
        },

        /**
         * 绘制查询表格
         * @param tableId  表格id，在相应html中已定义好
         * @param contentUrl  获取表格数据的后台请求地址
         * @param headUrl     获取表格表头的后台请求地址
         * @param columnsAmount 添加的自定义列的数量
         * @param columnsAdded  添加的自定义列的内容
         * @param columnParams 自定义表头变量名
         * @param columnDefs   新增自定义列
         * @param queryType    后台请求方式  post/get
         * @param dataType     后台返回查询结果数据类型   json/xml
         */
        tableQuery:function(tableObj){
        	//if(columnsAdded.length == 0 && headUrl != '') columnDefs = [];
        	//columnDefs = Table.createColumnDefs(tableId,columnDefs,headUrl);
            if(currQueryTableId != tableObj.tableId){
                tableObj.columnDefs.push({
                    "createdCell" : function(td, data, row) {
                        if(data.length > 10){
                            $(td).attr('title',data);
                        }
                    },
                    "targets" :"_all"
                });
                var tableColumns = createTableHead(tableObj.headUrl,tableObj.tableId,[],tableObj.columnParams,false,tableObj.queryType,tableObj.dataType);
                isTableInitialised = true;
                $.fn.dataTable.defaults.oLanguage=oLanguage;
                $.fn.dataTable.ext.errMode = 'none';
                oTable = $('#'+tableObj.tableId).on('error.dt', function(e, settings, techNote, message) {
                    $(this).parent().replaceWith(message);
                }).DataTable( {
                	"ajax": {
                        "url":tableObj.contentUrl,
                        "dataSrc": "datas",
                        "type":tableObj.queryType,
                        "dataType":tableObj.dataType,
                        "data":tableObj.ajaxData,
                        "ordering":false,
                        "complete":function(obj){
                            if(typeof obj.responseText!="undefined"){
                                var result = JSON.parse(obj.responseText);
                                if(result!=null&&result.resultCode!="undefined"){
                                    if(result.resultCode=="203"){//没权限，弹出提示框
                                        layer.msg(result.resultInfo);
                                        return false;
                                    }else if(result.resultCode=="302"){//没登录，跳到登录页面
                                        location.href ='./login.html';
                                        return false;
                                    }else if(result.resultCode=="404"){//权限系统数据库未配置相应功能模块
                                        alert(result.resultInfo);
                                        return false;
                                    }
                                }
                                //alert("ajax:"+JSON.stringify(obj.responseText));
                            }
                            
                        }
                        /*,
                         "global":false*/
                    },
                    "dom": '<"top"f>rt<"bottom"ilp><"clear">',
                    "bLengthChange": true, //改变每页显示数据数量
                    "bServerSide":true,//使用服务端
                    "bFilter": false, //过滤功能
                    "bProcessing":false,
                    "paginationType":'simple_numbers',
                    "bDestroy":true,
                    "lengthMenu": [[10, 15, 20, 50], [10, 15, 20, 50]],
                    "initComplete": function(settings, json) {
                    	//setCookie('columnLength', json.columns.length,'10000');
                        var tableHeight = $(".sql-result-container").height();
                        $(".sql-result .dataTables_wrapper").css("height",tableHeight+"px").css("overflow-y","scroll");
                    },
                    columns: tableColumns,
                    order: [ 1, 'asc' ],
                    iDisplayLength:10,
                    columnDefs : tableObj.columnDefs,
                    rowCallback: function(row, data, iDisplayIndex) {
                    	tableObj.callBackFn(row, data, iDisplayIndex);
                    }
                } );
            }else{
            	oTable.ajax.url( tableObj.contentUrl).load();
            }
            this.conditionQuery(tableObj.tableId,tableObj.contentUrl,oTable);
            currQueryTableId = tableObj.tableId;
        },
        refresh:function(){
        	currQueryTableId ="";
        },
        
        createParamsTable:function(tableId){
        	var columnsAmount = $('#'+tableId+' thead tr').find('th').length;
            if($('#'+tableId).find('tbody').length != 0){
                var tr = "<tr class='no-data'><td colspan='"+columnsAmount+"' style='text-align:center;'>暂无数据</td></tr>";
                $("#"+tableId).find('tbody').append(tr);
            }else{
                var tbody = "<tbody><tr class='no-data'><td colspan='"+columnsAmount+"' style='text-align:center;'>暂无数据</td></tr></tbody>";
                $(tbody).appendTo("#"+tableId);
            }

        },

        /**
         * 修改列表
         * @param tableId
         * @param editableContent
         */
        createEditableTable:function(tableId,editableContent){
            var tbody = "<tbody>"+editableContent+"</tbody>";
            $(tbody).appendTo("#"+tableId);

            Table.deleteRow(tableId);
        },

        /**
         * 添加行
         */
        addRow:function(){
            var tableId,rowContent,callbackFun;
            if(arguments.length == 1){
                var event = arguments[0];
                tableId = event.data.tableId;//表格id
                rowContent = event.data.rowContent;//待添加的行内容
                callbackFun  = arguments[0].data.callFun;//加载完后的回调函数
            }else{
                tableId = arguments[0];//表格id
                rowContent = arguments[1];//待添加的行内容
            } 
            //没有数据时，需填充的表格内容
            $("#"+tableId).find(".no-data").remove();
            $("#"+tableId).find("tbody").append(rowContent);

            Table.deleteRow(tableId);
            
            if(typeof(callbackFun) ==="function" ){
                
                callbackFun();
            }
        },
        /**
         * 添加行
         */
        addRowContent:function(id,content){      
            //没有数据时，需填充的表格内容
            $("#"+id).find(".no-data").remove();
            $("#"+id).find("tbody").append(content);

            Table.deleteRow(id);
        },
        /**
         * 添加行
         */
        clearContent:function(id,content){      
            //没有数据时，需填充的表格内容
            $("#"+id).find(".no-data").remove();
            $("#"+id).find("tbody").empty();
 
        },

        deleteRow:function(tableId){
            var thisObj = this;
            //删除事件绑定
            $("#"+tableId).find(".delete-row-btn").each(function(){
                var _this = $(this);
                var isBindClick = _this.data("events");
                //判断当前删除按钮是否绑定了点击事件
                if(isBindClick == undefined){
                    _this.on("click",function(){
                        /*$(this).parent().parent().hide();
                        $(this).parent().parent().find('td').each(function(){
                            if(!$(this).attr('state')){
                                Table.updateParamState($(this).children(),'d');
                            }
                        });
                         if($(this).parent().parent().next().length == 0){
                         thisObj.createParamsTable(tableId);
                         }*/
                        $(this).parent().parent().remove();
                        if($("#"+tableId).find('tbody').find('tr').length == 0){
                            thisObj.createParamsTable(tableId);
                        }

                    });
                }
            });
        },

        /**
         * 更新当前表单数据的状态
         * @param param 表单对象
         * @param state  表单状态 a:添加  u:修改  d:删除 s:保持不变
         */
        updateParamState:function(param,state){
            $(param).attr('state',state);
        },

        /**
         * 条件查询
         * @param tableId
         * @param contentUrl
         */
        conditionQuery:function(tableId,contentUrl,tableObj){
            $("#"+tableId+"QueryBtn").on("click",function(){
                //editable.fnMultiFilter({"userName":$("input[queryParam='userName']").val()});
                // editable.column( 1 ).search( '用户名0' ).draw();
            	var contentUrlArray  = contentUrl.split("?");
            	contentUrl = contentUrlArray[0];
                var queryParams = new String();
                $(".query-panel li").children().each(function(){
                    var name = $(this).attr("name");
                    var value = $(this).val();

                    if(value != undefined && value != ""){
                        queryParams = queryParams + name + "=" + value + "&";
                    }
                });
                if(queryParams != undefined){
                    var tokenId=getCookie("tokenId");
                    if(tokenId===undefined){
                        tokenId="";
                    }
                    queryParams = queryParams.substring(0,queryParams.length-1);
                    contentUrl = contentUrl + "?" + queryParams+"&tokenId="+tokenId;
                    tableObj.ajax.url(contentUrl).load();
                }
            });
        },
        
        /**
         * 组装自定义列内容
         * @param tableId 表格id
         * @param oldColumnDefs  待组装的自定义列内容
         * @param headUrl    表头请求url
         */
        createColumnDefs:function(tableId,oldColumnDefs,headUrl){
        	
        	var qColumnLength = 0;
        	
        	if(qColumnLength == 0 && headUrl == ''){
        		
        		qColumnLength = $('#'+tableId+' thead').find('th').length;
        		
        	}else if(qColumnLength == 0 && headUrl != ''){
        		
        		qColumnLength = parseInt(getCookie('columnLength'));
        		
        	}
        	
        	for(i=0;i<oldColumnDefs.length;i++){
        		
        		if(headUrl == ''){
        			
        			oldColumnDefs[i].targets = qColumnLength-i-1;
        			
        		}else{
        			
        			oldColumnDefs[i].targets = qColumnLength+i;
        			
        		}
        		
        		
        	}
        	
        	return oldColumnDefs;
        },

        /**
         * 获取表单列表数据
         * @param tableId
         */
        getTableValues:function(event){
            var tableId ;
            if((typeof event) == 'string'){
                tableId = event;
            }else{
                tableId = event.data.tableId;
            }
            var tableObj = new Array();
            $('#'+tableId+' tbody').find('tr').each(function(){
            	//如果没有数据，就直接返回
            	if($(this).hasClass("no-data")){
            		return false;
            	}
                var trObj = new Object();
                $(this).find('td').each(function(i,n){
                    var tdObj = $(this).children();
                    var obj = tdObj[0];
                    var name = $(obj).attr('name');
                    if($(this).find('input').length != 0 || $(this).find('select').length != 0){
                        if(name != undefined){
                            if($(obj).attr('type') == 'checkbox'){
                                trObj[name] = $(obj).is(':checked');
                            }else{
                                trObj[name] = $(obj).val();
                            }

                        }
                    }else{
                        if(name != undefined){
                            trObj[name] = $(obj).text();
                        }
                    }
                });
                trObj['state'] = $(this).attr('state');
                tableObj.push(trObj);
            });
            //alert(JSON.stringify(tableObj));
            return tableObj;
        },
        
        /**
         * 表格“新建”按钮点击事件，为各表单元素添加验证属性
         */
        addFormItemValidator:function(){
            $(document).on("click","a.buttons-create,a.buttons-edit",function () {
            //$("a.buttons-create").on("click",function(){
                $(".DTE_Field").each(function(){
                    var $this = $(this);
                    var currClass = $this.attr("class");
                    if(currClass.indexOf("validate") != -1){
                        var currValidator = currClass.substring(currClass.indexOf("validate"),currClass.length);
                        $this.removeClass(currValidator);
                        $this.find("div[data-dte-e='input-control']").children().addClass(currValidator);
                    }
                });

                /*if(!flag){
                    flag = true;

                    $(".DTE_Footer").find("button.submit").attr("type","submit");
                    //移除原有form表单
                    $('div[data-dte-e="form_content"]').unwrap();

                    //新建form表单并插入
                    var form = document.createElement("form");
                    $(form).attr('data-dte-e','form');
                    $('div[data-dte-e="body"]').before(form);

                    //将body及其子元素放入form中
                    var bodyHtml = $('div[data-dte-e="body"]').prop("outerHTML");
                    $('div[data-dte-e="body"]').remove();
                    $('form[data-dte-e="form"]').wrapInner(bodyHtml);

                    //将foot及其子元素放入form表单
                    $('div[data-dte-e="foot"]').appendTo('form[data-dte-e="form"]');
                }*/


                $('form[data-dte-e="form"]').validationEngine({
                    validationEventTrigger:"blur",  //触发的事件  :"keyup blur",
                    inlineValidation: true,//是否即时验证，false为提交表单时验证,默认true
                    success :  false,//为true时即使有不符合的也提交表单,false表示只有全部通过验证了才能提交表单,默认false
                    promptPosition: "centerRight"//提示所在的位置，topLeft, topRight, bottomLeft,  centerRight, bottomRight
                    //failure : function() { alert("验证失败，请检查。");  }//验证失败时调用的函数
                    //success : function() { callSuccessFunction() },//验证通过时调用的函数,
                    ,addPromptClass:'formError-text',//给提示信息增加 class
                    maxErrorsPerField:1,//单个元素显示错误提示的最大数量，值设为数值。默认 false 表示不限制。
                    addFailureCssClassToField:'common-form-error'//验证失败时，给控件增加 class，当再次验证通过时，会去除。
                });

                $(".DTE_Footer").find("button.submit").on("click",function(){
                    $('form[data-dte-e="form"]').submit();
                });

               /* $('form[data-dte-e="form"]').bind('jqv.field.result', function(event, errorFound){
                    showValidationResult();
                });*/

                $(".DTED_Lightbox_Close").on("click", function () {
                    $(".DTE_Field").each(function(){
                        var $this = $(this).find("div[data-dte-e='input-control']").children();
                        var currClass = $this.attr("class");
                        //if(currClass != null && currClass != undefined && currClass != '' &&currClass.indexOf("validate") != -1){
                        if(currClass != null && currClass != undefined && currClass != ''){
                            if($this.hasClass("formError")){
                                var currValidator = currClass.substring(currClass.indexOf("validate"),currClass.length);
                                $this.removeClass(currValidator);
                                $this.find(".formErrorContent").parent().remove();
                            }
                            var currValidator = currClass.substring(currClass.indexOf("validate"),currClass.length);
                            $this.removeClass(currValidator);
                        }
                    });

                });

            });

        }
    };
}();

var app={}; app.cookiepath = 'open_serv_configuration_web';

/**
 * 设置cookie
 * @param name
 * @param value
 * @param day 天数
 */
function setCookie(name, value, iDay) {
    /*var expiration = new Date((new Date()).getTime() + min * 60 * 1000);
    document.cookie = name
    + "="
    + escape(value)
    + ";path=" + app.cookiepath + "; expires=" + expiration
        .toGMTString();*/
    var oDate = new Date();
    var cookieTime = oDate.getTime();
    if (iDay < 1||iDay>=0) {
        cookieTime += 30 * 60 * 1000;
    }else if(iDay>=1){
        cookieTime += iDay * 24 * 60 * 60 * 1000;
    }
    oDate.setTime(cookieTime);
    document.cookie = name + '=' + encodeURIComponent(value) + ';expires=' + oDate + ";path=/";
}

/**
 * 获取Cookie
 *
 * @param {} Name
 * @return {String}
 */
function getCookie(Name) {
    var search = Name + "="
    if (document.cookie.length > 0) {
        offset = document.cookie.indexOf(search)
        if (offset != -1) {
            offset += search.length
            end = document.cookie.indexOf(";", offset)
            if (end == -1)
                end = document.cookie.length
            return unescape(document.cookie.substring(offset, end))
        } else
            return ""
    }
}

/**
 * 从缓存中清除Cookie
 *
 * @param {} name
 */
function clearCookie(name) {
    /*var expdate = new Date();
    expdate.setTime(expdate.getTime() - (86400 * 1000 * 1));
    this.setCookie(name, "", expdate);*/
    this.setCookie(name, "", -1);
}
//清空Cookie
function clearAllCookie() {
    var keys = document.cookie.match(/[^ =;]+(?=\=)/g);
    if (keys) {
        for (var i = keys.length; i--;)
            document.cookie = keys[i] + "=0;path=" + app.cookiepath + ";expires=" + new Date(0).toUTCString()
    }
}