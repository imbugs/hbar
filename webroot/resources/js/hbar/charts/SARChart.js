function SARChart(name, datasource, symbol, indicator) {
	PointChart.call(this, name, datasource, symbol, indicator);

	this.options.acceleration = 0.02;
	this.options.maximum = 0.2;
}

SARChart.constructor = SARChart;
SARChart.prototype = Object.create(PointChart.prototype);