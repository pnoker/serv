$(function() {
    var date = new Date();
    var beginDate = FormatDate(AddDays(date, -7), "yyyy-MM-dd");
    var endDate = FormatDate(new Date(), "yyyy-MM-dd");
     
    $.ajaxExtend({
        url : serviceHost + "/log/v1/servicestatbyimpl.json",
        async : true,
        type : "get",
        dataType : "json",
        data : {
            beginDate : beginDate,
            endDate : endDate,
            serviceId : serviceId
        },
        success : function(data) {
            if (data && data.resultCode == "0") {
                var jsonData = data;
                var opt = {};
                opt.title = {
                    text :"用户访问"+ serviceName+"接口访问统计"
                };
                opt.chart = {
                    type : 'column'
                };
                // X 轴
                opt.xAxis = {
                    categories : []
                }
               
                for (var i = 0; i < jsonData.datas.length; i++) {
                    opt.xAxis.categories.push(jsonData.datas[i].statKey);
                }
                opt.yAxis = hChart.yyAxis;
                opt.yAxis.title.text = "调用次数（次）";
                // 图表数据
                var sSeries = {
                    name : '接口调用次数',
                    data : []
                };
                for (var i = 0; i < jsonData.datas.length; i++) {
                    sSeries.data.push(jsonData.datas[i].totalNum);
                }
                opt.subtitle = {
                    text : ''
                };
                opt.series = [ sSeries ];
                opt.divId = "chartServiceStatisUser"
                hChart.initChart(opt);

            } else {
                alert("获取图表数据错误," + data.resultInfo);
            }
        },
        error : function(d) {
            alert("获取图表数据错误" + d);
        }
    });
});