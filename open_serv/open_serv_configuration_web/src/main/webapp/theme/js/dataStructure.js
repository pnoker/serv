/**
 * 构造html页面表头数据
 * @param headUrl  表头请求地址
 * @param id       表格id，在html中已定义好
 * @param columnsAdded    添加的自定义列的表头内容，数组类型
 * @param columnParams    自定义列参数名
 * @param hasCheckbox     是否添加第一列复选框
 * @param queryType       后台请求方式 get/post
 * @param dataType        后台返回数据类型  json/xml
 */
function createTableHead(headUrl,id,columnsAdded,columnParams,hasCheckbox,queryType,dataType){
    var columnParams_;
    if(headUrl != ''){
        $.ajax({
            "url":headUrl,
            "type":queryType,
            "dataType":dataType,
            "async": false,
            "success":function(data){

                var tableHeadData  = data.columns;

                var headContainer = $("#"+id+" thead tr"); 
                headContainer.empty();
                

                if(hasCheckbox){
                    headContainer.append("<th></th>");
                }
                

                for(i=0;i<tableHeadData.length;i++){
                	if(tableHeadData[i].name != undefined){
                		headContainer.append("<th>"+tableHeadData[i].name+"</th>");
                	}else{
                		headContainer.append("<th>"+tableHeadData[i].data+"</th>");
                	}
                }

                for(i=0;i<columnsAdded.length;i++){
                	headContainer.append("<th width='"+columnsAdded[i].width+"'>"+columnsAdded[i].name+"</th>");
                }

                columnParams_ = data.columns;
            }
        });
    }else{
        columnParams_ = columnParams;
    }
    return columnParams_;

}

/**
 * 构造可编辑表格表头
 * @param url：表头请求地址
 * @param hasCheckbox：是否添加第一列复选框
 * @param editorFields：编辑列参数
 * @param tableColumns：表格列参数
 *
 * editorFields eg:
 * var editorFields = [
            {
                "label": "ID",
                "name": "id",
                "type":"datetime",
                "className": "validate[required,custom[onlyNumberSp]]"
            },
            {
                "label": "用户名",
                "name": "userName",
                "className": "validate[required,length[0,5]]"
            },
            {
                "label": "年龄",
                "name": "userAge",
                "type": "select",
                "className": "validate[required]",
                "options": [
                    {
                        "label": "10",
                        "value": "10"
                    },
                    {
                        "label": "20",
                        "value": "20"
                    },
                    {
                        "label": "30",
                        "value": "30"
                    },
                    {
                        "label": "40",
                        "value": "40"
                    },
                    {
                        "label": "50",
                        "value": "50"
                    }
                ]
            },
            {
                "label": "地址",
                "name": "userAddress",
                "type": "radio",
                "options": [
                    {
                        "label": "北京市",
                        "value": 0
                    },
                    {
                        "label": "成都市",
                        "value": 1
                    }
                ],
                "default": 0
            }
        ]

 * tableColumns eg:
 * var tableColumns = [
     { data: 'id',className:"editable"},
     { data: 'userName',className:"editable"},
     { data: 'userAddress',className:"editable"},
     { data: 'userAge',className:"editable", render: $.fn.dataTable.render.number( ',', '.', 0, '$' )}
     ];
 */
function createEditorColumn(url,columnsAdded,tableColumns,hasCheckbox,editorFields,queryType,dataType){

    var columns = new Object();

    //editor
    if(url != ''){
        $.ajax({
            "url":url,
            "type":queryType,
            "dataType":dataType,
            "async": false,
            "success":function(data){
                columns.editorFields = data.editorFields;
            }
        });
    }else{
        columns.editorFields = editorFields;
    }

    //tableColumns
    if(url != ''){
        $.ajax({
             "url":url,
             "type":queryType,
             "dataType":dataType,
             "success":function(data){
            	 
            	 var tableHeadData  = data.columns;

                 var headContainer = $("#"+id+" thead tr"); 
                 headContainer.empty();
                 

                 if(hasCheckbox){
                     headContainer.append("<th></th>");
                 }
                 

                 for(i=0;i<tableHeadData.length;i++){
                 	if(tableHeadData[i].name != undefined){
                 		headContainer.append("<th>"+tableHeadData[i].name+"</th>");
                 	}else{
                 		headContainer.append("<th>"+tableHeadData[i].data+"</th>");
                 	}
                 }

                 for(i=0;i<columnsAdded.length;i++){
                 	headContainer.append("<th width='"+columnsAdded[i].width+"'>"+columnsAdded[i].name+"</th>");
                 }

                 columns.tableColumns = data.columns;
                 columns.editorFields = data.editorFields;
             }
         });
    }else{
        columns.tableColumns = tableColumns;
        columns.editorFields = editorFields;
    }
    if(hasCheckbox){
        var checkboxArray = [{data: null,defaultContent: '',className: 'select-checkbox',orderable: false,width:'2%'}];
        columns.tableColumns = checkboxArray.concat(columns.tableColumns);
    }

    if(columnsAdded.length > 0){
        for(i=0;i<columnsAdded.length;i++){
            columns.tableColumns.push({ data: ""});
        }
    }

    return columns;
}







