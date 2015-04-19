function TimeAxis(width, height) {
	BaseAxis.call(this, width, height);
}

TimeAxis.constructor = TimeAxis;
TimeAxis.prototype = Object.create(BaseAxis.prototype);