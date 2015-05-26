require('jquery');
require('jquery-ui');
require('./lib/extend/jquery-ui-reversible-resizable');
require('bootstrap');

import HBAR from './hbar/HBAR';
import OHLCChart from './hbar/charts/OHLCChart';
import VolumeChart from './hbar/charts/VolumeChart';
import LineChart from './hbar/charts/LineChart';
import MAChart from './hbar/charts/MAChart';
import RSIChart from './hbar/charts/RSIChart';
import BBandsChart from './hbar/charts/BBandsChart';
import SARChart from './hbar/charts/SARChart';
import MACDChart from './hbar/charts/MACDChart';


var hbar = new HBAR(document.getElementById("hbar"), function() 
{
	var ohlcChart = new OHLCChart("OHLCChart", hbar.protoSock, "BTCUSD:Bitfinex", "OHLCV");

	var volumeChart = new VolumeChart("VolumeChart", hbar.protoSock, "BTCUSD:Bitfinex", "OHLCV");

	var smaChart = new MAChart("SMAChart", hbar.protoSock, "BTCUSD:Bitfinex", "SMA");

	var emaChart = new MAChart("EMAChart", hbar.protoSock, "BTCUSD:Bitfinex", "EMA");
	emaChart.valueColor = 0xFF6600;
	emaChart.options.period = 26;

	var linearRegChart = new LineChart("LinearRegChart", hbar.protoSock, "BTCUSD:Bitfinex", "LinearReg");
	linearRegChart.valueColor = 0x000000;
	linearRegChart.options.period = 14;

	var bbandsChart = new BBandsChart("BBandsChart", hbar.protoSock, "BTCUSD:Bitfinex", "BBands");

	var sarChart = new SARChart("SARChart", hbar.protoSock, "BTCUSD:Bitfinex", "SAR");

	var hilbertCycleChart = new LineChart("HilbertCycleChart", hbar.protoSock, "BTCUSD:Bitfinex", "HilbertDominantCyclePeriod");
	hilbertCycleChart.valueType = "hilbertCycle";

	var hilbertPhaseChart = new LineChart("HilbertPhaseChart", hbar.protoSock, "BTCUSD:Bitfinex", "HilbertDominantCyclePhase");
	hilbertPhaseChart.valueType = "hilbertPhase";
	hilbertPhaseChart.valueColor = 0xFF6600;

	var hilbertTrendline = new LineChart("HilbertTrendlineChart", hbar.protoSock, "BTCUSD:Bitfinex", "HilbertTrendline");
	hilbertTrendline.valueType = "hilbertTrendline";
	hilbertTrendline.valueColor = 0x00DBD7;


	// var strategyChart = new StrategyChart("StrategyChart", hbar.protoSock, "BTCUSD:Bitfinex", "SarStrategy");



	hbar.addChart("ohlc-chart", ohlcChart);
	hbar.addChart("ohlc-chart", volumeChart);
	// hbar.addChart("ohlc-chart", strategyChart);
	hbar.addChart("ohlc-chart", smaChart);
	hbar.addChart("ohlc-chart", emaChart);
	hbar.addChart("ohlc-chart", linearRegChart);
	hbar.addChart("ohlc-chart", bbandsChart);
	hbar.addChart("ohlc-chart", sarChart);

	hbar.addChart("hilbert-chart", hilbertCycleChart);
	hbar.addChart("hilbert-chart", hilbertPhaseChart);
	hbar.addChart("hilbert-chart", hilbertTrendline);


	var rsiChart = new RSIChart("RSIChart", hbar.protoSock, "BTCUSD:Bitfinex", "RSI");

	hbar.addChart("rsi-chart", rsiChart);


	var macdChart = new MACDChart("MACDChart", hbar.protoSock, "BTCUSD:Bitfinex", "MACD");

	hbar.addChart("macd-chart", macdChart);


	$("#hbar-ui .period-btn-group .btn").click(function(event) {
		$("#hbar-ui .period-btn-group .btn").removeClass('disabled');
		$(event.target).toggleClass('disabled');
		hbar.setPeriod(parseInt($(event.target).attr('data-period')));
	});

	var intervalId;

	$("#hbar-ui #play-btn").click(function(event) {
		hbar.simulate();

		$("#hbar-ui #stop-btn").removeClass('disabled');
		$("#hbar-ui #play-btn").addClass('disabled');
	});

	$("#hbar-ui #stop-btn").click(function(event) {
		hbar.stopSimulation();

		$("#hbar-ui #stop-btn").addClass('disabled');
		$("#hbar-ui #play-btn").removeClass('disabled');
	});

	$("#hbar-ui .speed-btn-group .btn").click(function(event) {
		$("#hbar-ui .speed-btn-group .btn").removeClass('disabled');
		$(event.target).toggleClass('disabled');
		hbar.setSpeed(parseInt($(event.target).attr('data-speed')));
	});
	
	

});