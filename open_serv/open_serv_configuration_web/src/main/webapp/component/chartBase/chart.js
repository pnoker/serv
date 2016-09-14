var hChart = function() {
    var divId = "";
    var jsonData = null;
    var chart = null;
    var colors = [ "#FFB53A", '#FD6D6D', '#1198FF', '#00D364', '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4' ];
    var credits = {
        enabled : false
    };
    var data = null;
    var drilldown = null;
    var exporting = {
        enabled : true
    };
    var labels = null;
    var legend = {
        align : 'right',
        x : 0,
        verticalAlign : 'top',
        y : 10,
        floating : true,
        borderWidth : 0,
        symbolWidth : 12,
        symbolHeight : 9,
        shadow : false
    };
    var loading = null;
    var navigation = null;
    var noData = null;
    var pane = null;
    var plotOptions = null;
    var series = [];
    var subtitle = null;
    var title = null;
    var tooltip = {
        shared : true,
        formatter : function() {
            var curPointList = this.points;
            var tooltipStr = "";
            // 逐个返回单位进行数据拼接然后返回提示信息
            for (var i = 0; i < curPointList.length; i++) {
                if (i === 0) {
                    tooltipStr += "<b>" + curPointList[i].x + "</b><br/>";
                }
                tooltipStr += curPointList[i].series.name + "：" + curPointList[i].y + "<br>";
            }
            return tooltipStr;
        }
    };
    var xAxis = null;
    var yAxis = {
        title : {
            text : 'Y轴标题',
            align : 'middle',
            offset : 50,
            rotation : 270,
            y : -10
        },
        stackLabels : {
            enabled : true,
            style : {
                fontWeight : 'bold',
                color : 'black'
            }
        }
    };
    var chartCallback = function(chart) {

    }
    return {
        llegend : legend,
        ttooltip : tooltip,
        yyAxis : yAxis,
        initChart : function(args) {
            if (!args.divId) {
                alert("缺少必要参数，divId");
                return;
            }
            this.replaceArgs(args);
            var option = this.getChartOptions(); 
            $('#' + args.divId).highcharts(option, chartCallback);
        },
        getChartOptions : function() {
            var chartOptions = {};
            if (chart) {
                chart.style = {
                    fontFamily : "Microsoft YaHei"
                };
                chart.backgroundColor = "#F6F6F6";
                chartOptions.chart = chart;
            }
            if (colors) {
                chartOptions.colors = colors;
            }
            if (credits) {
                chartOptions.credits = credits;
            }
            if (data) {
                chartOptions.data = data;
            }
            if (drilldown) {
                chartOptions.drilldown = drilldown;
            }
            if (exporting) {
                chartOptions.exporting = exporting;
            }
            if (labels) {
                chartOptions.labels = labels;
            }
            if (legend) {
                legend.itemStyle = {
                    fontFamily : "Microsoft YaHei",
                    cursor : 'pointer',
                    color : '#3E576F'
                };
                legend.x = 0;
                chartOptions.legend = legend;
            }
            if (loading) {
                chartOptions.loading = loading;
            }
            if (navigation) {
                chartOptions.navigation = navigation;
            }
            if (noData) {
                chartOptions.noData = noData;
            }
            if (pane) {
                chartOptions.pane = pane;
            }
            if (plotOptions) {
                chartOptions.plotOptions = plotOptions;
            }
            if (series) {
                var style = {
                    "color" : "#335333",
                    "fontSize" : "11px",
                    "fontWeight" : "bold",
                    "textShadow" : "",
                    "fontFamily" : "Microsoft YaHei"
                };
                for (var i = 0; i < series.length; i++) {
                    if (series[i].dataLabels) {
                        series[i].dataLabels.style = style
                    } else {
                        series[i].dataLabels = {
                            style : style
                        }
                    }
                }
                chartOptions.series = series;
            }
            if (subtitle) {
                chartOptions.subtitle = subtitle;
            }
            if (title) {
                title.style = {
                    "color" : "#333333",
                    "fontSize" : "16px",
                    "fontWight" : "bold"
                };
                chartOptions.title = title;
            }
            if (tooltip) {
                tooltip.style = {
                    color : '#333333',
                    fontSize : '12px',
                    padding : '8px',
                    fontFamily : "Microsoft YaHei"
                };
                chartOptions.tooltip = tooltip;
            }
            if (xAxis) {
                xAxis.gridLineWidth = 1;
                chartOptions.xAxis = xAxis;
            }
            if (yAxis) {
                var ys2 = { // Secondary yAxis
                    title : {
                        text : '(时)',
                        align : 'high',
                        offset : 35,
                        rotation : 0,
                        y : -7
                    },
                    labels : {
                        format : '{value}'
                    },
                    opposite : true,
                    min : 0
                };
                if (yAxis.length > 1) {
                    if (yAxis[1].title && yAxis[1].title.text && yAxis[1].title.text.indexOf(ys2.title.text) >= 0) {
                        yAxis.pop();
                    }
                }
                chartOptions.yAxis = yAxis;
            }
            return chartOptions;
        },
        replaceArgs : function(args) {
            if (args.chart) {
                chart = args.chart
            }
            if (args.colors) {
                colors = args.colors
            }
            if (args.credits) {
                credits = args.credits
            }
            if (args.data) {
                data = args.data
            }
            if (args.drilldown) {
                drilldown = args.drilldown
            }
            if (args.exporting) {
                exporting = args.exporting
            }
            if (args.labels) {
                labels = args.labels
            }
            if (args.legend) {
                legend = args.legend
            }
            if (args.loading) {
                loading = args.loading
            }
            if (args.navigation) {
                navigation = args.navigation
            }
            if (args.noData) {
                noData = args.noData
            }
            if (args.pane) {
                pane = args.pane
            }
            if (args.plotOptions) {
                plotOptions = args.plotOptions
            }
            if (args.series) {
                series = args.series
            }
            if (args.subtitle) {
                subtitle = args.subtitle
            }
            if (args.title) {
                title = args.title
            }
            if (args.tooltip) {
                tooltip = args.tooltip
            }
            if (args.xAxis) {
                xAxis = args.xAxis
            }
            if (args.yAxis) {
                yAxis = args.yAxis
            }
            if (args.chartCallback) {
                chartCallback = args.chartCallback;
            }
        }
    };
}();