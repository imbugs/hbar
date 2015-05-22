import PointChart from './PointChart';

function SARChart(name, dataSource, symbol, indicator) {
	PointChart.call(this, name, dataSource, symbol, indicator);

	this.options.acceleration = 0.02;
	this.options.maximum = 0.2;
}

SARChart.constructor = SARChart;
SARChart.prototype = Object.create(PointChart.prototype);

export default SARChart;