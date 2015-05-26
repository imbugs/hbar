import DataRequest from '../data/dao/DataRequest';
import ValueAxis from './axis/ValueAxis';

var PIXI = require('pixi.js');

function BaseChart(name, dataSource, symbol, indicator) {
	PIXI.Graphics.call(this);

	this.valueType = "price";

	this.name = name;
	this.dataSource = dataSource;
	this.symbol = symbol;
	this.indicator = indicator;

	this.fields = ["value"];

	this.data = [];
	this.options = {};
}

BaseChart.constructor = BaseChart;
BaseChart.prototype = Object.create(PIXI.Graphics.prototype);

BaseChart.prototype.getData = function() {
	var request = this.getRequest();

	return this.dataSource.getData(request, this.redraw.bind(this));
};

BaseChart.prototype.getRequest = function() {
	return new DataRequest(this.symbol, this.indicator, this.timeAxis.period, this.timeAxis.min, this.timeAxis.max, this.options);
};

BaseChart.prototype.draw = function() { 
	this.clear();
	this.data = this.getData();

	if(this.valueType != this.valueAxis.parentValueType)
		this.valueAxis.setMinMax(this.getLow(), this.getHigh());
};

BaseChart.prototype.refreshLastTick = function() 
{ 
	var request = this.getRequest();

	return this.dataSource.refreshLastTick(request, this.redraw.bind(this));
};


BaseChart.prototype.setRedraw = function(redraw) {
	this.redraw = redraw;
};

BaseChart.prototype.setTimeAxis = function(timeAxis) {
	this.timeAxis = timeAxis;
};

BaseChart.prototype.setValueAxis = function(valueAxis) {
	if(this.valueType != valueAxis.parentValueType)
	{
		this.valueAxis = new ValueAxis(valueAxis.y, valueAxis.h, valueAxis.padding);
		this.valueAxis.parentValueType = valueAxis.parentValueType;
	} 
	else
	{
		this.valueAxis = valueAxis;
	}
};

BaseChart.prototype.getLow = function() {
	return this.dataSource.getMin(this.getRequest(), this.fields);
};

BaseChart.prototype.getHigh = function() {
	return this.dataSource.getMax(this.getRequest(), this.fields);
};

export default BaseChart;