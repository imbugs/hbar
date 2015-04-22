function RSIChart(name, dataSource, symbol, indicator) {
	LineChart.call(this, name, dataSource, symbol, indicator);

	this.type = "RSI";

	this.valueColor = 0xBB00FF;

	this.options.period = 14;

	this.lowValue = 20;
	this.highValue = 80;
}

RSIChart.constructor = RSIChart;
RSIChart.prototype = Object.create(LineChart.prototype);

RSIChart.prototype.draw = function() {
	LineChart.prototype.draw.call(this);

	var lowLine = this.valueAxis.getPosition(this.lowValue);
	var highLine = this.valueAxis.getPosition(this.highValue);

	this.lineStyle(1, this.valueColor, 1);
	this.beginFill(this.valueColor, 0.1);

	this.drawRect(-1, lowLine, this.timeAxis.w + 1, this.valueAxis.getDelta(this.lowValue, this.highValue));
}

RSIChart.prototype.getLow = function() {
	return 0;
}

RSIChart.prototype.getHigh = function() {
	return 100;
}

RSIChart.prototype.setValueAxis = function(valueAxis) {
	BaseChart.prototype.setValueAxis.call(this, valueAxis);
	this.valueAxis.padding = 0;
}