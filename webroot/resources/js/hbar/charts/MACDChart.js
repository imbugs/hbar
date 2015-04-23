function MACDChart(name, dataSource, symbol, indicator) {
	CompositeChart.call(this, name, dataSource, symbol, indicator);

	this.fields = ["macd", "signal", "histogram"];

	this.type = "MACD";

	this.options.fastPeriod = 12;
	this.options.slowPeriod = 26;
	this.options.signalPeriod = 9;

	this.maChart = new LineChart("MACDMAChart", dataSource, symbol, indicator);
	this.maChart.fields = ["macd", "signal"];
	this.maChart.macdColor = 0x0099FF;
	this.maChart.signalColor = 0xFF6600;
	this.maChart.options = this.options;
	this.maChart.type = "MACD";

	this.histogramChart = new HistogramChart("MACDHistogramChart", dataSource, symbol, indicator);
	this.histogramChart.fields = ["histogram"];
	this.histogramChart.histogramColor = 0x9999FF;
	this.histogramChart.options = this.options;
	this.histogramChart.type = "MACD";

	this.addChart(this.maChart);
	this.addChart(this.histogramChart);
}

MACDChart.constructor = MACDChart;
MACDChart.prototype = Object.create(CompositeChart.prototype);