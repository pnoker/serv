
jQuery(document).ready(function() {
   // var headUrl = 'http://localhost:8088/serv_configuration/sqloperation/v1/excute.json?dataSource=3&sql=select * from serve_data_source';
	 var headUrl = '';
	var tableId = 'example3';
    
    var columnsAdded = [{"name":"删除","width":"10%"}];
    var columnParams = createTableHead(headUrl,tableId,columnsAdded,[],false,'get','json');
    Table.createParamsTable(tableId);

    var rowContent = "<tr><td><input></td><td><input></td><td><input></td><td><input></td><td><a href='javascript:;'><i class='glyphicon glyphicon-minus delete-row'></i></a></td></tr>";
    $(".add-row-btn").on("click",{"rowContent":rowContent,"tableId":tableId},Table.addRow);
});
hideLeftMenu();