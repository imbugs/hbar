import BaseAxis from './BaseAxis';
import ValueAxis from './ValueAxis';

function TimeAxis(width, height) {
	BaseAxis.call(this, width, height);

	this.barSize = 3;
	this.barSpacing = 1;
	this.delta = this.barSize + this.barSpacing;

	this.setPeriod(TimeAxis.DEFAULT_PERIOD);
}

TimeAxis.constructor = TimeAxis;
TimeAxis.prototype = Object.create(BaseAxis.prototype);

TimeAxis.SCROLL_SPEED = 0.001;
TimeAxis.MAX_SCROLL_SPEED = 0.5;
TimeAxis.ZOOM_SPEED = 0.001;
TimeAxis.DEFAULT_PERIOD = 3600;


TimeAxis.prototype.setPeriod = function(period, maxTime) {
	this.period = period;
	this.bars = Math.floor(this.parentW / this.delta);

	if(!this.maxTime) {
		if(maxTime) this.maxTime = maxTime = this.periodize(maxTime);
		else maxTime = this.periodize(new Date().getTime() / 1000);
	} else maxTime = this.periodize(this.maxTime);
	
	var offset = this.periodize(period * this.bars * ValueAxis.WIDTH / this.parentW);
	this.min = maxTime - this.bars * this.period + offset;
	this.max = this.min + this.period * this.bars + offset;
};

TimeAxis.prototype.getPeriod = function() {
	return this.period;
};

TimeAxis.prototype.getPosition = function(value) {
	return this.getMinPosition(value) + Math.floor(this.barSize / 2);
};

TimeAxis.prototype.getMinPosition = function(value) {
	return this.getPeriodOffset(value) * this.delta;
};

TimeAxis.prototype.getMaxPosition = function(value) {
	return this.getMinPosition(value) + this.barSize;
};


TimeAxis.prototype.getPeriodOffset = function(value) {
	return (value - this.min) / this.period;
};

TimeAxis.prototype.resize = function(width, height) {
	this.parentW = width;
	this.parentH = height;
	this.delta = this.barSize + this.barSpacing;
	this.bars = Math.round(this.parentW / this.delta);
};

TimeAxis.prototype.scroll = function(scrollX, scrollY) {
	if(Math.abs(scrollX) > Math.abs(scrollY)) {
		var barDelta = this.bars * scrollX * TimeAxis.SCROLL_SPEED;

		if(scrollX > 0) {
			barDelta = Math.floor(Math.max(1, Math.min(this.bars * TimeAxis.MAX_SCROLL_SPEED, barDelta)));	
		} else {
			barDelta = Math.floor(Math.min(-1, Math.max(-this.bars * TimeAxis.MAX_SCROLL_SPEED, barDelta)));
		}
		
		this.min -= barDelta * this.period;
		this.max = this.min + this.period * this.bars;
	} else {
		this.barSize = Math.max(1, this.barSize * (1 + scrollY * TimeAxis.ZOOM_SPEED));

		this.delta = this.barSize + this.barSpacing;
		this.bars = Math.round(this.parentW / this.delta);

		var halfBars = Math.round(this.bars / 2);
		var mid = this.periodize(this.min + (this.max - this.min) / 2);

		this.min = mid - halfBars * this.period;
		this.max = this.min + this.period * this.bars;
	}
};

TimeAxis.prototype.periodize = function(time) {
	return Math.floor(time / this.period + 0.5) * this.period;
};

export default TimeAxis;