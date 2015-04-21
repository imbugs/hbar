function MAChart(name, datasource, symbol, indicator) {
	LineChart.call(this, name, datasource, symbol, indicator);

	this.options.period = 9;
}

MAChart.constructor = MAChart;
MAChart.prototype = Object.create(LineChart.prototype);