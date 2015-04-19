$(document).ready(onReady);
$(window).resize(onResize);

var chartStack;
var chart;

function onReady()
{
	chartStack = new ChartStack(document.getElementById("hbar"), true);

	chartStack2 = new ChartStack(document.getElementById("macd"), true);

	builders = 
	{
		"OHLCV" : dcodeIO.ProtoBuf.loadProtoFile("./protobuf/OHLCVSeries.proto").build("OHLCVSeries"),
		"SMA" : dcodeIO.ProtoBuf.loadProtoFile("./protobuf/SMASeries.proto").build("SMASeries"),
		"EMA" : dcodeIO.ProtoBuf.loadProtoFile("./protobuf/EMASeries.proto").build("EMASeries"),
		"BBands" : dcodeIO.ProtoBuf.loadProtoFile("./protobuf/BBandsSeries.proto").build("BBandsSeries"),
		"MACD" : dcodeIO.ProtoBuf.loadProtoFile("./protobuf/MACDSeries.proto").build("MACDSeries"),
		"RSI" : dcodeIO.ProtoBuf.loadProtoFile("./protobuf/RSISeries.proto").build("RSISeries"),
		"LinearReg" : dcodeIO.ProtoBuf.loadProtoFile("./protobuf/LinearRegSeries.proto").build("LinearRegSeries")
	}

	protoSock = new ProtoSock('http://localhost:8080/api', builders,
		function() 
		{
			ohlcChart = new OHLCChart("OHLCChart", protoSock, "BTCUSD:Bitfinex", "OHLCV", chartStack.getWidth(), chartStack.getHeight());
			ohlcChart.period = 86400;
			chartStack.addChart(ohlcChart);

			smaChart = new MAChart("SMAChart", protoSock, "BTCUSD:Bitfinex", "SMA", chartStack.getWidth(), chartStack.getHeight());
			smaChart.period = 86400;
			chartStack.addChart(smaChart);

			emaChart = new MAChart("EMAChart", protoSock, "BTCUSD:Bitfinex", "EMA", chartStack.getWidth(), chartStack.getHeight());
			emaChart.period = 86400;
			emaChart.valueColor = 0XFF6600;
			emaChart.options.period = 26;
			chartStack.addChart(emaChart);

			linearRegChart = new LineChart("LinearRegChart", protoSock, "BTCUSD:Bitfinex", "LinearReg", chartStack.getWidth(), chartStack.getHeight());
			linearRegChart.period = 86400;
			linearRegChart.valueColor = 0x000000;
			linearRegChart.options.period = 14;
			chartStack.addChart(linearRegChart);


			emaChart2 = new MAChart("EMAChart2", protoSock, "BTCUSD:Bitfinex", "EMA", chartStack.getWidth(), chartStack.getHeight());
			emaChart2.period = 86400;
			emaChart2.valueColor = 0xF200FF;
			emaChart2.options.period = 120;
			chartStack.addChart(emaChart2);

			emaChart3 = new MAChart("EMAChart2", protoSock, "BTCUSD:Bitfinex", "EMA", chartStack.getWidth(), chartStack.getHeight());
			emaChart3.period = 86400;
			emaChart3.valueColor = 0xA200AA;
			emaChart3.options.period = 180;
			chartStack.addChart(emaChart3);



			// bbands = new BBandsChart("BBandsChart", protoSock, "BTCUSD:Bitfinex", "BBands", chartStack.getWidth(), chartStack.getHeight());
			// bbands.period = 86400;
			// chartStack.addChart(bbands);

			// linearRegChart2 = new LineChart("LinearRegChart", protoSock, "BTCUSD:Bitfinex", "LinearReg", chartStack.getWidth(), chartStack.getHeight());
			// linearRegChart2.period = 7200;
			// linearRegChart2.valueColor = 0x999999;
			// linearRegChart2.options.period = 20;
			// chartStack.addChart(linearRegChart2);

			// linearRegChart3 = new LineChart("LinearRegChart", protoSock, "BTCUSD:Bitfinex", "LinearReg", chartStack.getWidth(), chartStack.getHeight());
			// linearRegChart3.period = 7200;
			// linearRegChart3.valueColor = 0xCCCCCC;
			// linearRegChart3.options.period = 30;
			// chartStack.addChart(linearRegChart3);

			// bbandsChart = new BBandsChart("BBandsChart", protoSock, "BTCUSD:Bitfinex", "BBands", chartStack.getWidth(), chartStack.getHeight());
			// bbandsChart.period = 7200;
			// chartStack.addChart(bbandsChart);



			rsiChart = new RSIChart("RSIChart", protoSock, "BTCUSD:Bitfinex", "RSI", chartStack.getWidth(), chartStack.getHeight());
			rsiChart.period = 86400;
			chartStack2.addChart(rsiChart);
			rsiChart.valueAxis.padding = 0;
		});


}

function onResize()
{
	chartStack.resize();
	chartStack2.resize();
}
