function VolumeChart(name, dataSource, symbol, indicator) {
	HistogramChart.call(this, name, dataSource, symbol, indicator);

	this.type = "volume";

	this.fields = ["volume"];

	this.valueColor = 0x0099FF;
}

VolumeChart.constructor = VolumeChart;
VolumeChart.prototype = Object.create(HistogramChart.prototype);


VolumeChart.prototype.setValueAxis = function(valueAxis) {
	HistogramChart.prototype.setValueAxis.call(this, valueAxis);
	this.valueAxis.padding = 0;
}

VolumeChart.prototype.getLow = function() {
	return 0;
}

VolumeChart.prototype.getHigh = function() {
	return 10 * HistogramChart.prototype.getHigh.call(this);
}