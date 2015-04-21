function BaseChart(name, datasource, symbol, indicator) {
	PIXI.Graphics.call(this);

	this.name = name;
	this.datasource = datasource;
	this.symbol = symbol;
	this.indicator = indicator;

	this.fields = ["value"];

	this.data = [];
	this.options = {};
}

BaseChart.constructor = BaseChart;
BaseChart.prototype = Object.create(PIXI.Graphics.prototype);

BaseChart.prototype.getData = function() {
	var request = new DataRequest(this.symbol, this.indicator, this.timeAxis.period, this.timeAxis.min, this.timeAxis.max, this.options);

	return this.datasource.getData(request, this.redraw.bind(this));
}

BaseChart.prototype.draw = function(data) { 
	this.clear();
	this.data = data != undefined ? data : this.getData();
}


BaseChart.prototype.setRedraw = function(redraw) {
	this.redraw = redraw;
}

BaseChart.prototype.setTimeAxis = function(timeAxis) {
	this.timeAxis = timeAxis;
}

BaseChart.prototype.setValueAxis = function(valueAxis) {
	this.valueAxis = valueAxis;
}

BaseChart.prototype.getLow = function() {
	var low = Number.MAX_VALUE;

	for(var f in this.fields)
		for(var t = this.timeAxis.min; t <= this.timeAxis.max; t += this.timeAxis.period)
			if(this.data[t]) low = Math.min(low, this.data[t][this.fields[f]]);

	return low;
}

BaseChart.prototype.getHigh = function() {
	var high = Number.MIN_VALUE;

	for(var f in this.fields)
		for(var t = this.timeAxis.min; t <= this.timeAxis.max; t += this.timeAxis.period)
			if(this.data[t]) high = Math.max(high, this.data[t][this.fields[f]]);

	return high;
}