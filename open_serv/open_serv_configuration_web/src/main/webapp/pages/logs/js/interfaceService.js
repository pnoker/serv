$(function() {
    var date = new Date();
    var beginDate = FormatDate(AddDays(date, -30), "yyyy-MM-dd");
    var endDate = FormatDate(new Date(), "yyyy-MM-dd");

    $.ajaxExtend({
        url : serviceHost + "/log/v1/allstatbyservice.json",
        async : true,
        type : "get",
        dataType : "json",
        data : {
            beginDate : beginDate,
            endDate : endDate
        },
        success : function(data) {
            if (data && data.resultCode == "0") {
                var jsonData = data;
                var opt = {};
                opt.title = {
                    text : "服务接口近一月调用次数"
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
                    text : 'TOP12'
                };
                opt.series = [ sSeries ];
                opt.divId = "chartInterfaceByService"
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
hideLeftMenu();