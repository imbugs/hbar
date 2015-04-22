function MAChart(name, dataSource, symbol, indicator) {
	LineChart.call(this, name, dataSource, symbol, indicator);

	this.options.period = 9;
}

MAChart.constructor = MAChart;
MAChart.prototype = Object.create(LineChart.prototype);