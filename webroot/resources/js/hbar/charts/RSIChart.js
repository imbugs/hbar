function RSIChart(name, datasource, symbol, indicator, width, height) {
	LineChart.call(this, name, datasource, symbol, indicator, width, height);

	this.valueColor = 0xBB00FF;

	this.options.period = 14;

	this.lowValue = 20;
	this.highValue = 80;
}

RSIChart.constructor = RSIChart;
RSIChart.prototype = Object.create(LineChart.prototype);

RSIChart.prototype.draw = function(data) {
	LineChart.prototype.draw.call(this, data);


	var lowLine = this.valueAxis.getPosition(this.lowValue);
	var highLine = this.valueAxis.getPosition(this.highValue);

	this.lineStyle(1, this.valueColor, 1);
	this.beginFill(this.valueColor, 0.1);

	this.drawRect(-1, lowLine, this.w + 1, this.valueAxis.getDelta(this.lowValue, this.highValue));
}

RSIChart.prototype.getLow = function() {
	return 0;
}

RSIChart.prototype.getHigh = function() {
	return 100;
}