function BaseChart(name, datasource, symbol, indicator, width, height) {
	PIXI.Graphics.call(this);

	this.name = name;
	this.datasource = datasource;
	this.symbol = symbol;
	this.indicator = indicator;
	this.w = width;
	this.h = height;

	this.fields = ["value"];

	this.data = [];
	this.options = {};

	this.barSize = 2;
	this.barSpacing = 1;
	this.paddingY = 20;
	this.scrollSpeed = 1000;

	this.period = 14400;
	this.delta = this.barSize + this.barSpacing * 2;
	this.bars = Math.floor(this.w / this.delta);
	this.startTime = this.periodize(new Date().getTime() / 1000) - this.bars * this.period;
	this.endTime = this.startTime + this.period * this.bars;
}

BaseChart.constructor = BaseChart;
BaseChart.prototype = Object.create(PIXI.Graphics.prototype);

BaseChart.prototype.resize = function(width, height) {
	this.w = width;
	this.h = height;

	this.delta = this.barSize + this.barSpacing * 2;
	this.bars = Math.floor(this.w / this.delta);
}

BaseChart.prototype.getData = function() {
	var request = new DataRequest(this.symbol, this.indicator, this.period, this.startTime, this.endTime, this.options);

	return this.datasource.getData(request, this.redraw.bind(this));
}

BaseChart.prototype.draw = function(data) { }

BaseChart.prototype.scroll = function(scrollX, scrollY) {
	this.startTime = this.periodize(this.startTime - scrollX * this.scrollSpeed) + (scrollX < 0 ? this.period : -this.period);
	this.endTime = this.startTime + this.period * this.bars;
}

BaseChart.prototype.periodize = function(time) {
	return Math.floor(time / this.period + 0.5) * this.period;
}

BaseChart.prototype.setRedraw = function(redraw) {
	this.redraw = redraw;
}

BaseChart.prototype.setValueAxis = function(valueAxis) {
	this.valueAxis = valueAxis;
}

BaseChart.prototype.getLow = function() {
	var low = Number.MAX_VALUE;

	for(var f in this.fields)
		for(var t = this.startTime; t <= this.endTime; t += this.period)
			if(this.data[t]) low = Math.min(low, this.data[t][this.fields[f]]);

	return low;
}

BaseChart.prototype.getHigh = function() {
	var high = Number.MIN_VALUE;

	for(var f in this.fields)
		for(var t = this.startTime; t <= this.endTime; t += this.period)
			if(this.data[t]) high = Math.max(high, this.data[t][this.fields[f]]);

	return high;
}