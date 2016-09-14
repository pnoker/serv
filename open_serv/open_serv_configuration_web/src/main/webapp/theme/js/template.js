//全局变量，用于区分服务目录目前属于服务管理的那个功能界面下：“新增服务”(true)或“服务列表”(false)
var addService=true;
var Template = function () {

    //首页url
    var defaultPageContentUrl = "./pages/dataBaseManage/addService.html";

    /**
     * 页面动态加载，页面需要刷新或跳转时调用
     * @param pageContentUrl  待加载页面的url
     */
    var loadPageContent = function(pageContentUrl){
        $("#digitalChinaCurrentUrl").val(pageContentUrl);
        $("#tab-content-body").load(pageContentUrl);

    };

    /**
     * 动态生成菜单,首页页面初始化时调用
     */

    var createMenu = function(data){

        var menuData = {
            //一级菜单数据
            "menuLevel1":[
                {"id":"0","name":"数据源管理","url":"","imgSrc":"./theme/images/icon/nav-menubar-1.png"},
                {"id":"111","name":"服务管理","url":"","imgSrc":"./theme/images/icon/nav-menubar-2.png"},
                {"id":"4","name":"安全管理","url":"","imgSrc":"./theme/images/icon/nav-menubar-3.png"},
                {"id":"5","name":"日志管理","url":"","imgSrc":"./theme/images/icon/nav-menubar-4.png"}
            ],
            //二级菜单数据
            "menuLevel2":[
                {"id":"1020","name":"新增服务","parentId":"111","url":"./pages/serviceManage/inner/list.html","imgSrc":"./theme/images/icon/nav-menubar-dropdown-1.png"},
                {"id":"6","name":"服务列表","parentId":"111","url":"./pages/serviceManage/inner/list.html?hidden=true","imgSrc":"./theme/images/icon/nav-menubar-dropdown-3.png"},
                {"id":"7","name":"服务目录","parentId":"111","url":"./pages/serviceManage/cataLog/addCataLog.html","imgSrc":"./theme/images/icon/nav-menubar-dropdown-5.png"},
                {"id":"9","name":"用户管理","parentId":"4","url":"./pages/security/user/list.html","imgSrc":"./theme/images/icon/nav-menubar-dropdown-1.png"},
                {"id":"8","name":"黑名单","parentId":"4","url":"./pages/security/blackList/list.html","imgSrc":"./theme/images/icon/nav-menubar-dropdown-1.png"},
                {"id":"18","name":"数据源配置","parentId":"0","url":"./pages/dataBase/dataSource/list.html","imgSrc":"./theme/images/icon/nav-menubar-dropdown-2.png"},
                {"id":"14","name":"数据库操作","parentId":"0","url":"./pages/dataBase/operate/index.html","imgSrc":"./theme/images/icon/nav-menubar-dropdown-4.png"},
                {"id":"17","name":"日志查询","parentId":"5","url":"./pages/logs/log_query.html","imgSrc":"./theme/images/icon/nav-menubar-dropdown-1.png"},
                {"id":"15","name":"日志统计","parentId":"5","url":"./pages/logs/logtotal.html","imgSrc":"./theme/images/icon/nav-menubar-dropdown-1.png"}
            ]
        };

        //一级菜单页面元素组装
        for(i=0;i<menuData.menuLevel1.length;i++){
            var menuLevel1 = menuData.menuLevel1[i];
            var menuObj = '<li id="'+menuLevel1.id+'"><a href="javascript:;" url="'+menuLevel1.url+'"><img src="'+menuLevel1.imgSrc+'"/><span class="title">'+menuLevel1.name+'</span></a></li>';

            $(".navbar-nav").append(menuObj);
            if(i == 1){
                $("#"+menuLevel1.id).addClass('active');
            }
            /*if(i == 0){
                $("#"+menuLevel1.id).addClass("start");
            }*/
        }

        //二级菜单页面元素组装
        for(i=0;i<menuData.menuLevel2.length;i++){
            var menuLevel2 = menuData.menuLevel2[i];
            var $parent = $("#"+menuLevel2.parentId);
            /*$parent.find("a").attr("href","javascript:;");*/
            $parent.find("a").attr("data-toggle","dropdown");
            $parent.find("a").attr("class","dropdown-toggle");
            if($parent.find("a").children(".arrow").length == 0){
                $parent.find("a").append("<img src='./theme/images/icon/arrow-down.png' class='arrow'/>");
            }
            if(!$parent.find("ul").hasClass("dropdown-menu")){
                var subMenu = '<ul class="dropdown-menu"><li><a href="javascript:;" id="'+menuLevel2.id+'" url="'+menuLevel2.url+'"><img src="'+menuLevel2.imgSrc+'"/>'+menuLevel2.name+'</a></li></ul>';
                $parent.append(subMenu);
            }else{
                var subMenu = '<li><a href="javascript:;" id="'+menuLevel2.id+'" url="'+menuLevel2.url+'"><img src="'+menuLevel2.imgSrc+'"/>'+menuLevel2.name+'</a></li>';
                $parent.find(".dropdown-menu").append(subMenu);
            }
        }

        //退出按钮添加
        var exitIcon = '<li class="exit"><a href="javascript:"><img src="./theme/images/icon/nav-menubar-exit.png"/></a></li>';
        $(".navbar-nav").append(exitIcon);
        //$("li.exit > a").attr("href",serviceHost+"/logout");
        $("li.exit > a").click(function () {
            $.ajaxExtend({
                url : serviceHost+"/logout",
                async : false,
                type :'get',
                dataType : 'json',
                complete: function (obj) {
                    if(obj.responseText!=null&&obj.responseText!=""){
                        var data=JSON.parse(obj.responseText);
                        if(data.resultCode=="302"){
                            clearCookie("tokenId");
                            layer.msg("退出成功");
                        }
                    }
                }
            })
        });

    };

    //点击顶部菜单，加载相应内容页。首页初始化时调用
    var loadMenuContent = function(){
        initLeftMenu();//左侧菜单初始化
    	var lastUrl ="";
        $('.hor-menu').on('click','ul ul a',function(){
            $('.hor-menu').find('a').parent().removeClass('active');
            $(this).parent().addClass('active');
            if($(this).parent().parent().hasClass('dropdown-menu')){
                $(this).parent().parent().parent().addClass('active');
            }
            var current = $(this).attr('url');
            $("#digitalChinaCurrentUrl").val(current);


            $('#tab-content-body').load(current,function(){
            	lastUrl = current;
            	/*点击“服务管理”下的“服务配置”模块时，隐藏新增按钮*/
            	if(current.match("hidden=true")!=null){
            	    addService=false;
                    $("button.add-btn").css("display","none");
                }else{
                    addService=true;
                }
            });
            
        });

        $('.hor-menu').find('.dropdown-menu').find('a').hover(
            function(){
                $(this).parent().parent().parent().css('background-color','#279af0');
            },
            function(){
                $(this).parent().parent().parent().css('background-color','#365bb3');
            });

        $('.hor-menu').find('li').hover(function(){
            var dropdownWidth = $(this).width();
            $(this).find('.dropdown-menu').width(dropdownWidth);
        });

    };

    return{
        init:function(){
            loadPageContent(defaultPageContentUrl);

            createMenu();
            loadMenuContent();
        }
    };
}();


/**
 * 左侧菜单滑动效果实现
 */
function popoverNew() {

    $("[data-toggle='popover']").off('popover');
    $("[data-toggle='popover']").off('shown.bs.popover');
    $("[data-toggle='popover']").off('mouseleave');
    $("[data-toggle='popover']").off('mouseenter');
    $(".popover").off('mouseleave');

    $("[data-toggle='popover']").popover({
        html : true
    });

    $("[data-toggle='popover']").on("shown.bs.popover", function() {
        var top = $('.page-sidebar-menu').find('li.active').outerHeight();
        var index = $(this).parent().attr('index') - 1;
        top = top*index;
        $(".popover").find(".arrow").remove();
        $(".popover").css("border-width", "0 0 0 1px").css("border-color", "#fff").css("border-style", "solid").css("border-radius", "0").css("left","0").css("top",top);
    }).on("mouseenter", function(event) {
        $(".popover").off('mouseleave');
        var _this = this;
        var _event = event;
        $(this).popover("show");

        $(".popover").on("mouseleave", function(event) {
            var flag = isCursorIn(_this,event);
            if(!flag){
                $(_this).popover('hide');
            }
        });

    }).on("mouseleave", function() {
        var _this = this;
        setTimeout(function() {

            if (!$(".popover:hover").length) {
                $(_this).popover("hide");
            }
        }, 100);
    });

}

//判断鼠标是否在指定html元素范围内
function isCursorIn(HtmlObjSelector,event){
    var divObj = $(HtmlObjSelector).parent();
    var div = divObj[0];
    var x=event.clientX;
    var y=event.clientY;
    var divx1 = getElementLeft(div);
    var divy1 = getElementTop(div);
    var divx2 = divx1 + div.offsetWidth;
    var divy2 = divy1 + div.offsetHeight;
    if( x < divx1 || x > divx2 || y < divy1 || y > divy2){
        return false;
    }else{
        return true;
    }
}

var menuLevel1 = [
    {
        "id": 5,
        "name": "基础设施",
        "url": "",
        "parentId": 1,
        "parentName": "",
        "level": 1,
        "requestDemoUrl": "",
        "methods": "",
        "remark": "",
        "description": "基础设施",
        "isVisible": 0,
        "updateContent": "",
        "state": 0,
        "updateTime": "",
        "params": "",
        "fields": "",
        "picture": "",
        "picStr": ""
    },
    {
        "id": 6,
        "name": "交通出行",
        "url": "",
        "parentId": 2,
        "parentName": "",
        "level": 1,
        "requestDemoUrl": "",
        "methods": "",
        "remark": "",
        "description": "交通出行",
        "isVisible": 0,
        "updateContent": "",
        "state": 0,
        "updateTime": "",
        "params": "",
        "fields": "",
        "picture": "",
        "picStr": ""
    }];
var menuLevel2 = [
    {
        "id": 7,
        "name": "管网管线",
        "url": "",
        "parentId": 5,
        "parentName": "",
        "level": 2,
        "requestDemoUrl": "",
        "methods": "",
        "remark": "",
        "description": "管网管线",
        "isVisible": 0,
        "updateContent": "",
        "state": 0,
        "updateTime": "",
        "params": "",
        "fields": "",
        "picture": "",
        "picStr": ""
    },
    {
        "id": 8,
        "name": "重点部件",
        "url": "",
        "parentId": 5,
        "parentName": "",
        "level": 2,
        "requestDemoUrl": "",
        "methods": "",
        "remark": "",
        "description": "重点部件",
        "isVisible": 0,
        "updateContent": "",
        "state": 0,
        "updateTime": "",
        "params": "",
        "fields": "",
        "picture": "",
        "picStr": ""
    },
    {
        "id": 9,
        "name": "重点区域",
        "url": "",
        "parentId": 5,
        "parentName": "",
        "level": 2,
        "requestDemoUrl": "",
        "methods": "",
        "remark": "",
        "description": "重点区域",
        "isVisible": 0,
        "updateContent": "",
        "state": 0,
        "updateTime": "",
        "params": "",
        "fields": "",
        "picture": "",
        "picStr": ""
    },
    {
        "id": 10,
        "name": "实时路况",
        "url": "",
        "parentId": 6,
        "parentName": "",
        "level": 2,
        "requestDemoUrl": "",
        "methods": "",
        "remark": "",
        "description": "实时路况",
        "isVisible": 0,
        "updateContent": "",
        "state": 0,
        "updateTime": "",
        "params": "",
        "fields": "",
        "picture": "",
        "picStr": ""
    },
    {
        "id": 35,
        "name": "公交",
        "url": "",
        "parentId": 6,
        "parentName": "",
        "level": 2,
        "requestDemoUrl": "",
        "methods": "",
        "remark": "",
        "description": "公交",
        "isVisible": 0,
        "updateContent": "",
        "state": 0,
        "updateTime": "",
        "params": "",
        "fields": "",
        "picture": "",
        "picStr": ""
    },
    {
        "id": 41,
        "name": "出租车",
        "url": "",
        "parentId":6,
        "parentName": "",
        "level": 2,
        "requestDemoUrl": "",
        "methods": "",
        "remark": "",
        "description": "出租车",
        "isVisible": 0,
        "updateContent": "",
        "state": 0,
        "updateTime": "",
        "params": "",
        "fields": "",
        "picture": "",
        "picStr": ""
    },
    {
        "id": 47,
        "name": "地铁",
        "url": "",
        "parentId": 6,
        "parentName": "",
        "level": 2,
        "requestDemoUrl": "",
        "methods": "",
        "remark": "",
        "description": "地铁",
        "isVisible": 0,
        "updateContent": "",
        "state": 0,
        "updateTime": "",
        "params": "",
        "fields": "",
        "picture": "",
        "picStr": ""
    }];

var loadServicePageByPopMenu=function(id,type){
	var  pageContentUrl="./pages/serviceManage/inner/list.html?parentId="+id;
	switch (type) {
	case 2:
		pageContentUrl="./pages/serviceManage/outer/list.html?parentId="+id;
		break;
	case 3:
		pageContentUrl="./pages/serviceManage/gis/list.html?parentId="+id;
		break;
	default:
		break;
	}

    //导航添加
    var navContent = $('#'+id).text();
    var parentMenu = $('#'+$('#'+id).attr('parentId'));
    if(parentMenu.length != 0){
        navContent = '<a href="javascript:;">'+parentMenu.text()+'</a>&nbsp;<span class="arrow">></span>&nbsp;<span>'+navContent+'</span>';
        var grandparentMenu = $('#'+parentMenu.attr('parentId'));
        if(grandparentMenu.length != 0){
            navContent = '<a href="javascript:;">'+grandparentMenu.text()+'</a>&nbsp;<span class="arrow">></span>&nbsp;'+navContent;
        }
    }


    $("#digitalChinaCurrentUrl").val(pageContentUrl);
    $("#tab-content-body").load(pageContentUrl,function(){
        $('.nav-menu-title').html(navContent);
        /*处于“服务配置”界面时，隐藏新增按钮*/
        if(addService==false){
            $("button.add-btn").css("display","none");
        }
    });
};

// 获取弹出窗口菜单
//function getPopMenu(menuLevel1, menuLevel2) {
function getPopMenu() {
    var mainMenu = $("[data-toggle='popover']");

    $('.page-sidebar-menu').find('li').find('a').attr('data-content','');
    $('.secondary_menu').empty();

    // 遍历显示主菜单
    for (i = 0; i < mainMenu.length; i++) {
        var mainMenuEle = '<div class="menu_left_pop" id="levelZero_' + $(mainMenu[i]).attr("id") + '"></div>';
        $(mainMenuEle).appendTo(".secondary_menu");
    }
	$.ajax({
		url : serviceHost + "/catalog/v1/querycatalogtree",
		async : false,
		type : "get",
		dataType : "json",
		data : {
			isDeleted : 0
		},
		success : function(obj) {
			var data = eval(obj).datas;
			if (data != null && data.length > 0) {
				for (var i = 0; i < data.length; i++) {
					var dataRow = data[i];
					var level = dataRow.treeLevel;
					if(level!=2){
						continue;
						}
						 var menuLevel1Obj = '<dl class="dl-horizontal clearfix"><dt class="menu_level_1 fl"><a parentId="'+dataRow.pid+'" href="javascript:loadServicePageByPopMenu('+dataRow.id+','+dataRow.serviceType+');" id="' + dataRow.id + '">'
				            + dataRow.catalogName + '</a>></dt><dd class="menu_level_2 fl" id="levelTwoContainer_' + dataRow.id + '"></dd></dl>';
				        $(menuLevel1Obj).appendTo("#levelZero_" + dataRow.pid);
					
				}
			}
			
			if (data != null && data.length > 0) {
				for (var i = 0; i < data.length; i++) {
					var dataRow = data[i];
					var level = dataRow.treeLevel;
					if(level!=3){
						continue;
					}
						 var menuLevel2Link = '<a parentId="'+dataRow.pid+'" href="javascript:loadServicePageByPopMenu('+dataRow.id+','+dataRow.serviceType+');" id="' + dataRow.id + '" >' + dataRow.catalogName + '</a>';
					        $(menuLevel2Link).appendTo("#levelTwoContainer_" + dataRow.pid);
					
				}
			}
			
		    // 将一、二级菜单的内容放入滑过主菜单的弹出框内
		    for (i = 0; i < mainMenu.length; i++) {
		        var mainMenuId = $(mainMenu[i]).attr("id");
		        var dataContent = $("#levelZero_" + mainMenuId).html();
		        if ($("#levelZero_" + mainMenuId).html() == '') {
		            $("#" + mainMenuId).popover("destroy");
		        } else {
		            $("#" + mainMenuId).attr("data-content", dataContent);
		        }
		    }

		}
	});

}

//左侧菜单
var initLeftMenu = function(){
    getPopMenu();
    popoverNew();
};
//显示左侧菜单
var showLeftMenu = function(){
    $('.page-sidebar').removeClass('hide');
    $('.page-content').addClass('col-md-offset-2');
};
//隐藏左侧菜单
var hideLeftMenu = function(){
    $('.page-sidebar').addClass('hide');
    $('.page-content').removeClass('col-md-offset-2');
};
/* var findTopMenu = function(data){
                for(var i=0;i<data.length;i++){
                    var pid=data[i].parentId;
                    var hasPt=false;
                    for(var k=0;k<data.length;k++){
                        if(data[k].id==pid){
                            hasPt=true;
                            continue;
                        }
                    }
                    if(!hasPt){
                        return data[i];
                    }
                }
                return null;
            };
           var menuData = {
                "menuLevel1":[
                    //{"id":"3","name":"数据转换","url":"index.html"},
                    //{"id":"a4","name":"数据抓取","url":""},
                ],
                "menuLevel2":[
                   // {"id":"6","name":"文件","parentId":"a4","url":"./pages/dataGrab/table_editable3.html"},
                ]
            };
            $.ajax({
                type : "post",
                url : "http://localhost:8080/city_authority/login/v1/querymoduletree?systemCode=serv_conf",
                //dataType : 'json',
                data : null,
                async : false,
                contentType : "application/json; charset=utf-8",
                success : function(data) {
                    data=JSON.parse(data);
                    var topMenu=findTopMenu(data);
                    if(data&&data.length>1&&topMenu){
                        for (var i=0;i<data.length;i++){
                            if(data[i].parentId==topMenu.id){
                                menuData.menuLevel1.push({
                                    id:data[i].id,
                                    name:data[i].name,
                                    url:"./tree"+data[i].url.replace("/v1",".html")
                                });
                            }
                        }
                        for(var i=0;i<menuData.menuLevel1.length;i++ ){
                            var level1=menuData.menuLevel1[i];
                            for (var k=0;k<data.length;k++){
                                if(data[k].parentId==level1.id){
                                    menuData. menuLevel2.push({
                                            id: data[k].id,
                                            name:data[k].name,
                                            parentId:level1.id,
                                            url:"./tree"+data[k].url.replace("/v1",".html")
                                        }
                                    );
                                }
                            }
                        }
                        createMenu(menuData);
                    }else{
                        alert("获取菜单树失败！");
                    }
                },
                error : function(data,t,e) {
                    alert("error---"+data);
                }
            });*/

