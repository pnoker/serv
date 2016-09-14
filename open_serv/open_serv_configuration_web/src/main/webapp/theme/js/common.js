//快捷键事件绑定ctrl+enter
function Hotkey(event, targetObj, ctrlKey, shiftKey, altKey, keycode)
{
    if (
        targetObj
        && event.ctrlKey == ctrlKey
        && event.shiftKey == shiftKey
        && event.altKey == altKey
        && event.keyCode == keycode
    )
        targetObj.click();
}

function fnKeyup(event)
{
    var b = document.getElementById("sqlExe");
    Hotkey(event, b, true, false, false, 13);
}

// 捕获系统的Keyup事件
// 如果是Mozilla系列浏览器
if (document.addEventListener)
    document.addEventListener("keyup",fnKeyup,true);
else
    document.attachEvent("onkeyup",fnKeyup);


var xmlhttp;
//统一请求接口，主要用于ajax错误信息抓取并处理
$(document).ajaxError(function(event,xhr,property,errorMsg){
    if(xhr.status == 404){
        $("#tab-content-body").find(".tab-pane.active").load("./pages/error/404.html");
    }else if(xhr.status == 500){
        $("#tab-content-body").find(".tab-pane.active").load("./pages/error/500.html");
    }
   /* else  if(xhr.status == 0){
       $(".tab-content").find(".tab-pane.active").load("./pages/error/error.html");
    }*/
});

/**
 * 发送ajax请求之前执行的操作
 */
$(document).ajaxSend(function(event,xhr,options){

    //console.log("----------发送ajax请求前-----------");
    var tokenId=getCookie("tokenId");
    if(tokenId===undefined){
        tokenId="";
    }
    $.ajaxSetup({
        data:{"tokenId" : tokenId}
    });

   $('#loadingModal').modal('show');


    //alert("tokenID=" + JSON.stringify(tokenId.substr(8)));
});

$(document).ajaxStop(function(event,xhr,options){
    setTimeout("$('#loadingModal').modal('hide');$('.modal').css('overflow-y','auto')",500);
});


//var flag=false;
//getMenuList("serv_configuration");

/**
 * 发送ajax请求之后执行的操作
 */
/*$(document).ajaxSuccess(function(obj){
    
    //console.log("----------发送ajax请求后-----------");
    var result
    if(xhr.responseText){
        result=JSON.parse(xhr.responseText);
    }
    if(result.resultCode){
      //alert("result=" + JSON.parse(xhr.responseText).resultCode);
        if(result.resultCode=="203"){//没权限，弹出提示框
            //alert(result.resultInfo);
            layer.msg(result.resultInfo);
            //$("body").load("./index.html");
            return false;
        }else if(result.resultCode=="302"){//没登录，跳到登录页面
            //flag=true;
            //alert(result.resultInfo);
            //$("body").load("./login.html");
            location.href ='./login.html';
            //layout.msg("没登录");
            //alert("没登录");
            return false;
        }
    }
    
});*/

(function($){
    $.ajaxExtend = function(options){
        $.ajax({
            url : options.url,
            async : options.async,
            type : options.type,
            dataType : options.dataType,
            data : options.data,
            error:function(data){
                if(typeof (options.error) == 'function'){
                    options.error(data);
                }
            },
            complete:function(data){
                if(typeof (options.complete) == 'function'){
                    options.complete(data);
                }
            },
            success:function(obj){
                //var result=JSON.parse(obj.responseText);
                if(obj.resultCode=="203"){//没权限，弹出提示框
                    layer.msg(obj.resultInfo,{time:100000});
                    return false;
                }else if(obj.resultCode=="302"){//没登录，跳到登录页面
                    location.href ='./NewLogin.html';
                    return false;
                }else if(obj.resultCode=="404"){//权限系统数据库未配置相应功能模块
                    alert(obj.resultInfo);
                    return false;
                }
                options.success(obj);
            }
        });
    }

})(jQuery);

function gotoTop() {
    $('html, body').animate({scrollTop:0}, 'slow');
}
function gotoDiv(div) {
    var a = $("#"+div).offset().top - 100;
    $("#tab-content-body").animate({scrollTop:a}, 'slow');
}
function gotoBottom() {
    window.scrollTo(0, document.documentElement.scrollHeight-document.documentElement.clientHeight);
}

//用来获取绝对位置的横坐标和纵坐标
function getElementLeft(element){
    var actualLeft = element.offsetLeft;
    var current = element.offsetParent;
    while (current !== null) {
        actualLeft += current.offsetLeft;
        current = current.offsetParent;
    }
    return actualLeft;
}

function getElementTop(element){
    var actualTop = element.offsetTop;
    var current = element.offsetParent;
    while (current !== null){
        actualTop += current.offsetTop;
        current = current.offsetParent;
    }
    return actualTop;
}

//表单验证提示方式由悬浮提示框改为表单项后面显示
function showValidationResult(){
    $(".formError").addClass("common-form-error");
}

//获取用户有权限访问的模块
function getMenuList(systemCode){
    $.ajaxExtend({
        type : "post",
        url : "../../city_authority/login/v1/querymoduletree?systemCode="+systemCode,
        data : null,
        async : false,
        contentType : "application/json; charset=utf-8",
        success : function(data) {
            alert("module :　"+JSON.stringify(data));
        },
        error : function(data) {
            alert(JSON.stringify(data));
        }
    });
}

//div向上拖拽功能实现
function dragElementTop(containerSelector,dragSelector,otherPartSelector){
    var src_posi_Y = 0, dest_posi_Y = 0, move_Y = 0, is_mouse_down = false, destHeight = 30;
    $(dragSelector).mousedown(function(e){
            src_posi_Y = e.pageY;
            is_mouse_down = true;
    });

    $(document).bind("click mouseup",function(e){
        if(is_mouse_down){
            is_mouse_down = false;
        }
    }).mousemove(function(e){
            dest_posi_Y = e.pageY;
            move_Y = src_posi_Y - dest_posi_Y;
            src_posi_Y = dest_posi_Y;
            destHeight = $(containerSelector).height() + move_Y;
            if(is_mouse_down){
                destHeight = destHeight > 30 ? destHeight : 30;
                $(containerSelector).css("height",destHeight );
                if(otherPartSelector != undefined && otherPartSelector != ''){
                    executeHeight = $('#tab-content-body').height() - destHeight - 20;
                    $(".sql-execute").css("height",executeHeight);
                }
            }
    });
}

/**
 * 获取cookie的方法
 * @param name
 */
function getCookie(name) {
    var cookieVal = null;
    //获取所有的cookie数组
    var cookieArr = document.cookie.split(";");
    //循环解析获取cookie
    for (var i = 0; i < cookieArr.length; i++) {
        //当cookie的名称相等的时候取得需要的值,并跳出循环
        var cookieOnlyArr = cookieArr[i].split("=");
    	if ($.trim(cookieOnlyArr[0]) == name) {
            cookieVal = unescape(cookieOnlyArr[1]);
            break;
        }
    }
    return cookieVal;
}
//用户登录后，动态显示当前用户名
$(function(){
    var tokenStr=getCookie("tokenId");
    if(tokenStr==null&&tokenStr==undefined)return;
    var tokenArr=tokenStr.split(":");
    if(tokenArr.length>2){
        $(".current-user a").html(tokenArr[2]);
    }
})

//在鼠标点击下去时，关闭layer插件弹出框点击事件
$("*").mousedown(function(){
   // layer.closeAll();
});

//可输入下拉列表实现
function clearSelect(obj,e)
{
    opt = obj.options[0];
    opt.selected = "selected";
    if((e.keyCode== 8) ||(e.charCode==8))//使用退格（backspace）键实现逐字删除的编辑功能
    {
        opt.value = opt.value.substring(0, opt.value.length>0?opt.value.length-1:0);
        opt.text = opt.value;
    }
    if((e.keyCode== 46) ||(e.charCode==46))//使用删除（Delete）键实现逐字删除的编辑功能
    {
        opt.value = "";
        opt.text = opt.value;
    }
//还可以实现其他按键的响应
}

function writeSelect(obj,e)
{
    opt = obj.options[0];
    opt.selected = "selected";
    opt.value += String.fromCharCode(e.charCode||e.keyCode);
    opt.text = opt.value;
}
function forbidBackSpace()//为了在IE中，避免backspace的返回上一页功能，和本下拉框的编辑功能冲突，需要禁掉backspace的功能。forbidBackSpace可以写在<body onkeydown="forbidBackSpace();">中。
{
    if((event.keyCode == 8) && (event.srcElement.type != "text" && event.srcElement.type != "textarea" && event.srcElement.type != "password"))
    {
        event.keyCode = 0;
        event.returnValue = false;
    }
}

/**
 * 下拉列表自动搜索过滤
 * source:下拉列表默认数据
 * inputSelector：输入框选择器
 * selectBtnSelector：输入框父级div选择器
 * ajaxUrl：远程获取下拉列表数据url
 * ajaxType:ajax请求方式  get/post
 * ajaxData:ajax请求参数
 * ajaxDataType:ajax参数返回类型
 * 本地过滤调用方式：triggerAutoComplete(source,inputSelector,selectBtnSelector)
 * 远程获取过滤调用方式：triggerAutoComplete(source,inputSelector,selectBtnSelector,ajaxUrl,ajaxType,ajaxData,ajaxDataType)
 */
function triggerAutoComplete(){

    var source,inputSelector,selectBtnSelector,ajaxUrl,ajaxType,ajaxData,ajaxDataType,ajaxCallbackFn;

    source = arguments[0];
    inputSelector = arguments[1];
    selectBtnSelector = arguments[2];
    myFun = arguments[3];
    if(arguments.length == 8){
        ajaxUrl = arguments[3];
        ajaxType = arguments[4];
        ajaxData = arguments[5];
        ajaxDataType = arguments[6];
        ajaxCallbackFn = arguments[7];
    }

    if(arguments.length == 8){

        $( inputSelector).autocomplete({
            minLength:0,
            // autoFocus: true,
            source: function(request, response){
                $.ajax({
                    url : ajaxUrl,
                    type:ajaxType,
                    dataType: ajaxDataType,
                    global:"false",
                    data : ajaxData,
                    success: function(data) {
                        var newSource = ajaxCallbackFn(data);
                        var matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex( request.term ), "i" );
                        response( $.grep( newSource, function( item ){
                            return matcher.test( item );
                        }) );
                    }
                });
            },
            appendTo: selectBtnSelector
        });
    }else{
        $( inputSelector).autocomplete({
            minLength:0,
            // autoFocus: true,
            source: source,
            select: function( event, ui ) {
                if(myFun){
                    myFun(ui);
                }
            },
            appendTo: selectBtnSelector
        });
    }



    //为点击下拉列表添加点击显示下拉列表事件
    $(selectBtnSelector+' img,'+selectBtnSelector+' input').on('click',function (){
        if ( $(inputSelector).autocomplete( "widget" ).is( ":visible" ) ){
            $(inputSelector).autocomplete( "close" );
            return;
        }
        $(this).blur();
        $(inputSelector).autocomplete('search',$(inputSelector).val());
        $(inputSelector).focus();
    });
}

cmp = function( x, y ) {
// If both x and y are null or undefined and exactly the same
    if ( x === y ) {
        return true;
    }

// If they are not strictly equal, they both need to be Objects
    if ( ! ( x instanceof Object ) || ! ( y instanceof Object ) ) {
        return false;
    }

//They must have the exact same prototype chain,the closest we can do is
//test the constructor.
    if ( x.constructor !== y.constructor ) {
        return false;
    }

    for ( var p in x ) {
        //Inherited properties were tested using x.constructor === y.constructor
        if ( x.hasOwnProperty( p ) ) {
            // Allows comparing x[ p ] and y[ p ] when set to undefined
            if ( ! y.hasOwnProperty( p ) ) {
                return false;
            }

            // If they have the same strict value or identity then they are equal
            if ( x[ p ] === y[ p ] ) {
                continue;
            }

            // Numbers, Strings, Functions, Booleans must be strictly equal
            if ( typeof( x[ p ] ) !== "object" ) {
                return false;
            }

            // Objects and Arrays must be tested recursively
            if ( ! Object.equals( x[ p ], y[ p ] ) ) {
                return false;
            }
        }
    }

    for ( p in y ) {
        // allows x[ p ] to be set to undefined
        if ( y.hasOwnProperty( p ) && ! x.hasOwnProperty( p ) ) {
            return false;
        }
    }
    return true;
};