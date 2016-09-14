var editOption = function(){alert("添加按钮事件绑定测试！！");};
var columnLength = 0;

var editorFields = [
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
];
var tableColumns = [
    { data: 'id',className:"editable"},
    { data: 'userName',className:"editable"},
    { data: 'userAddress',className:"editable"},
    { data: 'userAge',className:"editable", render: $.fn.dataTable.render.number( ',', '.', 0, '$' )}
];
var columnDefs = [ {
    "render" : function(data, type, row) {
            return "<a href='javascript:;' onclick='editOption();'><i class='glyphicon glyphicon-pencil'></i></a>&nbsp;&nbsp;&nbsp;<a href='javascript:;' onclick='editOption();'><i class='glyphicon glyphicon-remove'></i></a>";
        },
        "targets" : 0
    },
    {
        "render" : function(data, type, row) {
            return "<a href='javascript:;' onclick='editOption();'><i class='glyphicon glyphicon-remove'></i></a>";
        },
        "targets" : 1
    }];

var columsAdded = [{"name":"操作"},{"name":"操作"}];

jQuery(document).ready(function() {
    var contentUrl = serviceHost_cityoutput+"/cxfDemo/demo.json";

    var tableObj = new editableObj("example",contentUrl,"",columsAdded,true,columnDefs,'post','json',editorFields,tableColumns);
    Table.init(tableObj);
});
hideLeftMenu();