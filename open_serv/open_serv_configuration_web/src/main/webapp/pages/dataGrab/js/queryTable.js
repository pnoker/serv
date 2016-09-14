var editOption = function(){alert("添加按钮事件绑定测试！！");};

var formatData = function(row,data,iDisplayIndex){
	if(data.SOURCE_USER == 'devuser'){
		$('td:eq(3)', row).html("aaa");
	}
};

var contentUrl = serviceHost+"/sqloperation/v1/excute.json?dataSource=3&sql=select * from serve_data_source";

jQuery('body').on('click', '.sql-execute .tools .config',{"contentUrl":contentUrl,"headUrl":''},function(e) {
	
	e.preventDefault();
	
	var tableObj = jQuery(this).closest(".sql-execute").next(".sql-result").find("table");
	
	var resultObj = jQuery(this).closest(".sql-execute").next(".sql-result");
	
	var columsAdded = [{"name":"操作","width":"20%"},{"name":"操作","width":"20%"}];
	
	var columnParams = [{ data: 'ID'},{ data: 'SOURCE_NAME'},{ data: 'SOURCE_CLASS'},{ data: 'SOURCE_USER'}];
	
	
	var columnDefs = [ 
	    {
	    	"render" : function(data, type, row) {
	    	
		        return "<a href='javascript:;' onclick='editOption();'><i class='glyphicon glyphicon-pencil'></i></a>";
		        
	        },
	        "targets" : 0
	    },
	    {
	        "render" : function(data, type, row) {
	        	
	            return "<a href='javascript:;' onclick='editOption();'><i class='glyphicon glyphicon-remove'></i></a>";
	            
	        },
	        
	        "targets" : 1
	    }];

	Table.tableQuery($(tableObj).attr("id"),e.data.contentUrl,e.data.headUrl,columsAdded,columnParams,columnDefs,'get','json',formatData,{});
	
	$(resultObj).removeClass("hide");
});

hideLeftMenu();