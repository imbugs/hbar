import LineChart from "./LineChart"

function BBandsChart(name, dataSource, symbol, indicator) {
	LineChart.call(this, name, dataSource, symbol, indicator);

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

export default BBandsChart;