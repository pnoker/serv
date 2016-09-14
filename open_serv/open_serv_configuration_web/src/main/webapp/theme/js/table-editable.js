var TableEditable = function () {

    return {

        //main function to initiate the module
        init: function (tableObj) {
            function restoreRow(oTable, nRow) {
                var aData = oTable.fnGetData(nRow);
                var jqTds = $('>td', nRow);

                for (var i = 0, iLen = jqTds.length; i < iLen; i++) {
                    oTable.fnUpdate(aData[i], nRow, i, false);
                }

                oTable.fnDraw();
            }

            function editRow(oTable, nRow) {
                var aData = oTable.fnGetData(nRow);
                var jqTds = $('>td', nRow);
                jqTds[0].innerHTML = '<input type="text" class="m-wrap small" value="' + aData[0] + '">';
                jqTds[1].innerHTML = '<input type="text" class="m-wrap small" value="' + aData[1] + '">';
                jqTds[2].innerHTML = '<input type="text" class="m-wrap small" value="' + aData[2] + '">';
                jqTds[3].innerHTML = '<input type="text" class="m-wrap small" value="' + aData[3] + '">';
                jqTds[4].innerHTML = '<a href="javascript:;" class="option edit"><i class="glyphicon glyphicon-saved"></i></a><a href="javascript:;" class="option cancel"><i class="glyphicon glyphicon-remove"></i></a>';
            }

            function saveRow(oTable, nRow) {
                var jqInputs = $('input', nRow);
                oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
                oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
                oTable.fnUpdate(jqInputs[2].value, nRow, 2, false);
                oTable.fnUpdate(jqInputs[3].value, nRow, 3, false);
                oTable.fnUpdate('<a href="javascript:;" class="option edit"><i class="glyphicon glyphicon-pencil"></i></a><a href="javascript:;" class="option delete"><i class="glyphicon glyphicon-trash"></i></a>', nRow, 4, false);
                oTable.fnDraw();
            }

            function cancelEditRow(oTable, nRow) {
                var jqInputs = $('input', nRow);
                oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
                oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
                oTable.fnUpdate(jqInputs[2].value, nRow, 2, false);
                oTable.fnUpdate(jqInputs[3].value, nRow, 3, false);
                oTable.fnUpdate('<a href="javascript:;" class="option edit"><i class="glyphicon glyphicon-pencil"></i></a><a href="javascript:;" class="option delete"><i class="glyphicon glyphicon-trash"></i></a>', nRow, 4, false);
                oTable.fnDraw();
            }

            var oTable = $('#'+tableObj.tableId).dataTable({
                "bProcessing": true,                    //加载数据时显示正在加载信息
                "bServerSide": false,                    //指定从服务器端获取数据
                "bFilter": true,                       //不使用过滤功能
                "bLengthChange": false,                 //用户不可改变每页显示数量
                //"iDisplayLength": 8,                    //每页显示8条数据
                //"sAjaxSource": "customerInfo/search.do",//获取数据的url
                //"fnServerData": retrieveData,           //获取数据的处理函数
                "sPaginationType": "full_numbers",      //翻页界面类型
                "sDom": "<'row-fluid'<'span6'l><'span6'f>r>t<'row-fluid'<'span6'i><'span6'p>>",
                "oLanguage": {
                    //"sLengthMenu": "_MENU_ records per page",
                    "oPaginate": {
                        "sPrevious": "上页",
                        "sNext": "下页"
                    },
                    "sZeroRecords": "抱歉， 没有找到",
                    "sInfoEmpty": "没有数据",
                    "sZeroRecords": "没有检索到数据",
                    "sInfo": "第 _PAGE_ 页 ( 总共 _PAGES_ 页 )"
                },
                "aoColumnDefs": [
                        {
                            'bSortable': false,
                            'aTargets': [0]
                        },
                        {
                            "targets": -1,
                            "data": null,
                            "defaultContent": "<a href='javascript:;' class='option edit'><i class='glyphicon glyphicon-pencil'></i></a><a href='javascript:;' class='option delete'><i class='glyphicon glyphicon-trash'></i></a>"
                        }
                ]

            });


            var nEditing = null;

            $('#'+tableObj.newTableId).click(function (e) {
                e.preventDefault();
                var aiNew = oTable.fnAddData(['', '', '', '',
                        '<a href="javascript:;" class="option edit"><i class="glyphicon glyphicon-pencil"></i></a><a href="javascript:;" class="option cancel"><i class="glyphicon glyphicon-remove"></i></a>'
                ]);
                var nRow = oTable.fnGetNodes(aiNew[0]);
                editRow(oTable, nRow);
                nEditing = nRow;
            });

            $('#'+tableObj.tableId+' a.delete').live('click', function (e) {
                e.preventDefault();

                if (confirm("Are you sure to delete this row ?") == false) {
                    return;
                }

                var nRow = $(this).parents('tr')[0];
                oTable.fnDeleteRow(nRow);
                alert("Deleted! Do not forget to do some ajax to sync with backend :)");
            });

            $('#'+tableObj.tableId+' a.cancel').live('click', function (e) {
                e.preventDefault();
                if ($(this).attr("data-mode") == "new") {
                    var nRow = $(this).parents('tr')[0];
                    oTable.fnDeleteRow(nRow);
                } else {
                    restoreRow(oTable, nEditing);
                    nEditing = null;
                }
            });

            $('#'+tableObj.tableId+' a.edit').live('click', function (e) {
                e.preventDefault();

                /* Get the row as a parent of the link that was clicked on */
                var nRow = $(this).parents('tr')[0];

                if (nEditing !== null && nEditing != nRow) {
                    /* Currently editing - but not this row - restore the old before continuing to edit mode */
                    restoreRow(oTable, nEditing);
                    editRow(oTable, nRow);
                    nEditing = nRow;
                } else if (nEditing == nRow && $(this).find("i").hasClass("glyphicon-saved")) {
                    /* Editing this row and want to save it */
                    saveRow(oTable, nEditing);
                    nEditing = null;
                    alert("Updated! Do not forget to do some ajax to sync with backend :)");
                } else {
                    /* No edit in progress - let's start one */
                    editRow(oTable, nRow);
                    nEditing = nRow;
                }
            });
        }

    };

}();