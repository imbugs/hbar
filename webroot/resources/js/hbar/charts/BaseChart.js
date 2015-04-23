function BaseChart(name, dataSource, symbol, indicator) {
	PIXI.Graphics.call(this);

	this.type = "price";

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
}

BaseChart.prototype.getRequest = function() {
	return new DataRequest(this.symbol, this.indicator, this.timeAxis.period, this.timeAxis.min, this.timeAxis.max, this.options);
}

BaseChart.prototype.draw = function() { 
	this.clear();
	this.data = this.getData();

	if(this.type != this.valueAxis.parentType)
		this.valueAxis.setMinMax(this.getLow(), this.getHigh());
}


BaseChart.prototype.setRedraw = function(redraw) {
	this.redraw = redraw;
}

BaseChart.prototype.setTimeAxis = function(timeAxis) {
	this.timeAxis = timeAxis;
}

BaseChart.prototype.setValueAxis = function(valueAxis) {
	if(this.type != valueAxis.parentType)
	{
		this.valueAxis = new ValueAxis(valueAxis.y, valueAxis.h, valueAxis.padding);
		this.valueAxis.parentType = valueAxis.parentType;
	} 
	else
	{
		this.valueAxis = valueAxis;
	}
}

BaseChart.prototype.getLow = function() {
	return this.dataSource.getMin(this.getRequest(), this.fields);
}

BaseChart.prototype.getHigh = function() {
	return this.dataSource.getMax(this.getRequest(), this.fields);
}