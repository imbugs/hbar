function HBAR(container, onReady)
{
	this.chartStackManager = new ChartStackManager(container);

	this.builders = 
	{
		"OHLCV" : dcodeIO.ProtoBuf.loadProtoFile("./protobuf/OHLCVSeries.proto").build("OHLCVSeries"),
		"SMA" : dcodeIO.ProtoBuf.loadProtoFile("./protobuf/SMASeries.proto").build("SMASeries"),
		"EMA" : dcodeIO.ProtoBuf.loadProtoFile("./protobuf/EMASeries.proto").build("EMASeries"),
		"BBands" : dcodeIO.ProtoBuf.loadProtoFile("./protobuf/BBandsSeries.proto").build("BBandsSeries"),
		"MACD" : dcodeIO.ProtoBuf.loadProtoFile("./protobuf/MACDSeries.proto").build("MACDSeries"),
		"RSI" : dcodeIO.ProtoBuf.loadProtoFile("./protobuf/RSISeries.proto").build("RSISeries"),
		"LinearReg" : dcodeIO.ProtoBuf.loadProtoFile("./protobuf/LinearRegSeries.proto").build("LinearRegSeries"),
		"SAR" : dcodeIO.ProtoBuf.loadProtoFile("./protobuf/SARSeries.proto").build("SARSeries")
	}

	this.protoSock = new ProtoSock('http://localhost:8080/api', this.builders, onReady);

	container.addEventListener('mousewheel',
		function(event) {
			event.preventDefault();

			this.chartStackManager.scroll(event.wheelDeltaX, event.wheelDeltaY)
		    
		    return false;
		}.bind(this), false);

	container.addEventListener("touchmove",
		function(event) {
		    event.preventDefault();

		    return false;
		}.bind(this), false);

	window.addEventListener("resize",
		function(event) {
			this.chartStackManager.resize();
		}.bind(this), false);
}

HBAR.prototype.addChart = function(stack, chart) 
{
	this.chartStackManager.addChart(stack, chart);
}