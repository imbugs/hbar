function VolumeChart(name, datasource, symbol, indicator) {
	BaseChart.call(this, name, datasource, symbol, indicator);

	this.type = "volume";

	this.fields = ["volume"];

	this.valueColor = 0x0099FF;
}

VolumeChart.constructor = VolumeChart;
VolumeChart.prototype = Object.create(HistogramChart.prototype);


VolumeChart.prototype.draw = function(data) {
	this.valueAxis.setMinMax(0, this.getHigh());

	HistogramChart.prototype.draw.call(this, data);
}

VolumeChart.prototype.setValueAxis = function(_valueAxis) {
	var valueAxis = new ValueAxis(_valueAxis.y, _valueAxis.h, 0);

	BaseChart.prototype.setValueAxis.call(this, valueAxis);
}

VolumeChart.prototype.getLow = function() {
	return 0;
}

VolumeChart.prototype.getHigh = function() {
	return 10 * BaseChart.prototype.getHigh.call(this);
}