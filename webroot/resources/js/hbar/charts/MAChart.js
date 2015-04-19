function MAChart(name, datasource, symbol, indicator, width, height) {
	LineChart.call(this, name, datasource, symbol, indicator, width, height);

	this.options.period = 9;
}

MAChart.constructor = MAChart;
MAChart.prototype = Object.create(LineChart.prototype);