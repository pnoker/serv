function getTree(){
    var tree =[
        {
            "level":0,
            "text": "Parent 1",
            "selectable":true,
            "checkboxes": [
                {
                    "name": "查看",
                    "checked": false,
                    "index": 0
                },
                {
                    "name": "编辑",
                    "checked": false,
                    "index": 1
                }
            ],
            "nodes": [
                {
                    "level":1,
                    "text": "Child 1",
                    "selectable":false,
                    "checkboxes": [
                        {
                            "name": "查看",
                            "checked": true,
                            "index": 0
                        },
                        {
                            "name": "编辑",
                            "checked": true,
                            "index": 1
                        }
                    ],
                    "nodes": [
                        {
                            "level":2,
                            "text": "Grandchild 1",
                            "checkboxes": [
                                {
                                    "name": "查看",
                                    "checked": false,
                                    "index": 0
                                },
                                {
                                    "name": "编辑",
                                    "checked": false,
                                    "index": 1
                                }
                            ],
                            "tags": [
                                "2"
                            ]
                        },
                        {
                            "level":2,
                            "text": "Grandchild 2",
                            "checkboxes": [
                                {
                                    "name": "查看",
                                    "checked": false,
                                    "index": 0
                                },
                                {
                                    "name": "编辑",
                                    "checked": false,
                                    "index": 1
                                }
                            ],
                            "tags": [
                                "3"
                            ]
                        }
                    ]
                },
                {
                    "level":1,
                    "text": "Child 2",
                    "checkboxes": [
                        {
                            "name": "查看",
                            "checked": false,
                            "index": 0
                        },
                        {
                            "name": "编辑",
                            "checked": false,
                            "index": 1
                        }
                    ]
                }
            ]
        },
        {
            "level":0,
            "text": "Parent 2",
            "checkboxes": [
                {
                    "name": "查看",
                    "checked": false,
                    "index": 0
                },
                {
                    "name": "编辑",
                    "checked": false,
                    "index": 1
                }
            ]
        },
        {
            "level":0,
            "text": "Parent 3",
            "checkboxes": [
                {
                    "name": "查看",
                    "checked": false,
                    "index": 0
                },
                {
                    "name": "编辑",
                    "checked": false,
                    "index": 1
                }
            ]
        },
        {
            "level":0,
            "text": "Parent 4",
            "checkboxes": [
                {
                    "name": "查看",
                    "checked": false,
                    "index": 0
                },
                {
                    "name": "编辑",
                    "checked": false,
                    "index": 1
                }
            ]
        },
        {
            "level":0,
            "text": "Parent 5",
            "checkboxes": [
                {
                    "name": "查看",
                    "checked": false,
                    "index": 0
                },
                {
                    "name": "编辑",
                    "checked": false,
                    "index": 1
                }
            ]
        }
    ];

    return tree;
}

$('#tree').treeview({
    data: getTree(),
    nodeIcon:"",
    expandIcon: 'glyphicon glyphicon-chevron-right',
    collapseIcon: 'glyphicon glyphicon-chevron-down'
});


var checkableTree = $('#treeCheckable').treeview(
    {
        data: getTree(),
        nodeIcon:"",
        expandIcon: 'glyphicon glyphicon-chevron-right',
        collapseIcon: 'glyphicon glyphicon-chevron-down',
        showBorder:false,
        hasCheckbox:true,
        highlightSelected:true,
        clickable:false,
        onNodeSelected:function(event,node){
        }
    }
);

hideLeftMenu();






