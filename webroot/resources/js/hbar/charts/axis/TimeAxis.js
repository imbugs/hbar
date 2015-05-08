function TimeAxis(width, height) {
	BaseAxis.call(this, width, height);

	this.barSize = 3;
	this.barSpacing = 1;
	this.delta = this.barSize + this.barSpacing;

	this.scrollSpeed = 0.001;
	this.maxScrollSpeed = 0.5;

	this.zoomSpeed = 0.001;

	this.setPeriod(3600);
}

TimeAxis.constructor = TimeAxis;
TimeAxis.prototype = Object.create(BaseAxis.prototype);

TimeAxis.prototype.setPeriod = function(period, maxTime) {
	this.period = period;

	if(!this.maxTime) {
		if(maxTime) {
			this.maxTime = maxTime = this.periodize(maxTime);
		} else {
			maxTime = this.periodize(new Date().getTime() / 1000);
		}
	} else maxTime = this.periodize(this.maxTime);
	
	this.bars = Math.floor(this.w / this.delta);
	this.min = maxTime - this.bars * this.period;
	this.max = this.min + this.period * this.bars;

	console.log("period", this.min, this.max);
}

TimeAxis.prototype.getPosition = function(value) {
	return this.getMinPosition(value) + Math.floor(this.barSize / 2);
}

TimeAxis.prototype.getMinPosition = function(value) {
	return this.getPeriod(value) * this.delta
}

TimeAxis.prototype.getMaxPosition = function(value) {
	return this.getMinPosition(value) + this.barSize;
}


TimeAxis.prototype.getPeriod = function(value) {
	return (value - this.min) / this.period;
}

TimeAxis.prototype.resize = function(width, height) {
	this.w = width;
	this.h = height;
	this.delta = this.barSize + this.barSpacing;
	this.bars = Math.round(this.w / this.delta);
}

TimeAxis.prototype.scroll = function(scrollX, scrollY) {
	if(Math.abs(scrollX) > Math.abs(scrollY)) {
		var barDelta = this.bars * scrollX * this.scrollSpeed;

		if(scrollX > 0) {
			barDelta = Math.floor(Math.max(1, Math.min(this.bars * this.maxScrollSpeed, barDelta)));	
		} else {
			barDelta = Math.floor(Math.min(-1, Math.max(-this.bars * this.maxScrollSpeed, barDelta)));
		}
		
		this.min -= barDelta * this.period;
		this.max = this.min + this.period * this.bars;
	} else {
		this.barSize = Math.max(1, this.barSize * (1 + scrollY * this.zoomSpeed));

		this.delta = this.barSize + this.barSpacing;
		this.bars = Math.round(this.w / this.delta);

		var halfBars = Math.round(this.bars / 2);
		var mid = this.periodize(this.min + (this.max - this.min) / 2);

		this.min = mid - halfBars * this.period;
		this.max = this.min + this.period * this.bars;
	}
}

TimeAxis.prototype.periodize = function(time) {
	return Math.floor(time / this.period + 0.5) * this.period;
}