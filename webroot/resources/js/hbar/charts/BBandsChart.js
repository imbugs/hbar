function BBandsChart(name, datasource, symbol, indicator, width, height) {
	LineChart.call(this, name, datasource, symbol, indicator, width, height);

	this.upperColor = 0x0099FF;
	this.middleColor = 0x6699FF;
	this.lowerColor = 0x0099FF;

	this.fields = ["upper", "lower", "middle"];

	this.options.period = 20;
	this.options.deviationUp = this.options.deviationDown = 2;
	this.options.maType = "SMA";
}

BBandsChart.constructor = BBandsChart;
BBandsChart.prototype = Object.create(LineChart.prototype);