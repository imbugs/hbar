import BaseChart from './BaseChart';

function CompositeChart(name, dataSource, symbol, indicator) {
	BaseChart.call(this, name, dataSource, symbol, indicator);

	this.charts = [];
}

CompositeChart.constructor = CompositeChart;
CompositeChart.prototype = Object.create(BaseChart.prototype);

CompositeChart.prototype.addChart = function(chart) {
	this.charts.push(chart);

	this.addChild(chart);
}

CompositeChart.prototype.draw = function() {
	BaseChart.prototype.draw.call(this);

	for(var chart in this.charts)
		this.charts[chart].draw.bind(this.charts[chart])();
}

CompositeChart.prototype.setTimeAxis = function(timeAxis) {
	BaseChart.prototype.setTimeAxis.call(this, timeAxis);

	for(var chart in this.charts)
		this.charts[chart].setTimeAxis(timeAxis);
}

CompositeChart.prototype.setValueAxis = function(valueAxis) {
	BaseChart.prototype.setValueAxis.call(this, valueAxis);

	for(var chart in this.charts)
		this.charts[chart].setValueAxis(valueAxis);
}

CompositeChart.prototype.setRedraw = function(redraw) {
	BaseChart.prototype.setRedraw.call(this, redraw);

	for(var chart in this.charts)
		this.charts[chart].setRedraw(redraw);
}

export default CompositeChart;