// base64加密开始
var keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv" + "wxyz0123456789+/" + "=";
/**
 * 加密算法 xutao
 * 
 * @param input
 * @returns {String}
 */
function encode64(input) {

    var output = "";
    var chr1, chr2, chr3 = "";
    var enc1, enc2, enc3, enc4 = "";
    var i = 0;
    do {
        chr1 = input.charCodeAt(i++);
        chr2 = input.charCodeAt(i++);
        chr3 = input.charCodeAt(i++);
        enc1 = chr1 >> 2;
        enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
        enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
        enc4 = chr3 & 63;
        if (isNaN(chr2)) {
            enc3 = enc4 = 64;
        } else if (isNaN(chr3)) {
            enc4 = 64;
        }
        output = output + keyStr.charAt(enc1) + keyStr.charAt(enc2) + keyStr.charAt(enc3) + keyStr.charAt(enc4);
        chr1 = chr2 = chr3 = "";
        enc1 = enc2 = enc3 = enc4 = "";
    } while (i < input.length);

    return output;
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