import ProtoSock from './data/ProtoSock';
import ChartStackManager from './ChartStackManager';

var ProtoBuf = require('protobufjs');

function HBAR(container, onReady)
{
	this.speed = 10;

	this.dragging = false;
	
	this.builders = 
	{
		"OHLCV" : ProtoBuf.loadProtoFile("./resources/protobuf/OHLCVSeries.proto").build("OHLCVSeries"),
		"SMA" : ProtoBuf.loadProtoFile("./resources/protobuf/SMASeries.proto").build("SMASeries"),
		"EMA" : ProtoBuf.loadProtoFile("./resources/protobuf/EMASeries.proto").build("EMASeries"),
		"BBands" : ProtoBuf.loadProtoFile("./resources/protobuf/BBandsSeries.proto").build("BBandsSeries"),
		"MACD" : ProtoBuf.loadProtoFile("./resources/protobuf/MACDSeries.proto").build("MACDSeries"),
		"RSI" : ProtoBuf.loadProtoFile("./resources/protobuf/RSISeries.proto").build("RSISeries"),
		"LinearReg" : ProtoBuf.loadProtoFile("./resources/protobuf/LinearRegSeries.proto").build("LinearRegSeries"),
		"SAR" : ProtoBuf.loadProtoFile("./resources/protobuf/SARSeries.proto").build("SARSeries"),
		"HilbertDominantCyclePeriod" : ProtoBuf.loadProtoFile("./resources/protobuf/HilbertDominantCyclePeriodSeries.proto").build("HilbertDominantCyclePeriodSeries"),
		"HilbertDominantCyclePhase" : ProtoBuf.loadProtoFile("./resources/protobuf/HilbertDominantCyclePhaseSeries.proto").build("HilbertDominantCyclePhaseSeries"),
		"HilbertTrendline" : ProtoBuf.loadProtoFile("./resources/protobuf/HilbertTrendlineSeries.proto").build("HilbertTrendlineSeries"),
		"HilbertTrendMode" : ProtoBuf.loadProtoFile("./resources/protobuf/HilbertTrendModeSeries.proto").build("HilbertTrendModeSeries"),
		"SarStrategy" : ProtoBuf.loadProtoFile("./resources/protobuf/StrategyStatsSeries.proto").build("StrategyStatsSeries"),
	}

	this.protoSock = new ProtoSock('http://localhost:8888/api', this.builders, function() {
		this.chartStackManager = new ChartStackManager(container, this.protoSock);

		container.addEventListener('mousewheel',
		function(event) {
			event.preventDefault();

			this.chartStackManager.scroll(event.wheelDeltaX, event.wheelDeltaY);
		    
		    return false;
		}.bind(this), false);

	container.addEventListener("touchmove",
		function(event) {
		    event.preventDefault();

		    return false;
		}.bind(this), false);

	container.addEventListener("mousemove",
		function(event) {
			this.chartStackManager.drawCrosshair(event.layerX, event.layerY);
			
			if(this.dragging) {
				if(this.lastDragX && this.lastDragY) {
					this.chartStackManager.scroll(event.screenX - this.lastDragX, 2*(event.screenY - this.lastDragY));
				}

				this.lastDragX = event.screenX;
				this.lastDragY = event.screenY;
			}
		}.bind(this), false);

	container.addEventListener("mousedown",
		function(event) {
			if(event.target.className.indexOf('ui-resizable-handle') < 0) {
				this.dragging = true;
				this.lastDragX = event.screenX;
				this.lastDragY = event.screenY;
			}
		}.bind(this), false);

	container.addEventListener("mouseup",
		function(event) {
			this.dragging = false;
		}.bind(this), false);


	window.addEventListener("resize",
		function(event) {
			this.chartStackManager.resize();
		}.bind(this), false);

		onReady();
	}.bind(this));
	
}

HBAR.prototype.addChart = function(stack, chart) 
{
	this.chartStackManager.addChart(stack, chart);
}

HBAR.prototype.setPeriod = function(period)
{
	this.chartStackManager.setPeriod(period);
}

HBAR.prototype.setSpeed = function(speed)
{
	this.speed = speed;
}

HBAR.prototype.simulate = function()
{
	this.play();
}

HBAR.prototype.play = function() {
	this.intervalId = setTimeout(function() {
		this.tick();
		this.play();
	}.bind(this), 250);
}

HBAR.prototype.stopSimulation = function()
{
	clearTimeout(this.intervalId);
}

HBAR.prototype.tick = function()
{
	this.protoSock.tick(this.speed, function() {
		this.chartStackManager.refreshLastTick();
	}.bind(this));
}

export default HBAR;