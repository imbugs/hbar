function ValueAxis(width, height, padding) {
	BaseAxis.call(this, width, height);

	this.parentValueType = "price";

	this.padding = padding;
}

ValueAxis.constructor = ValueAxis;
ValueAxis.prototype = Object.create(BaseAxis.prototype);

ValueAxis.prototype.getPosition = function(value) {
	return this.h - (value - this.min) * this.getScale() - this.padding;
}

ValueAxis.prototype.getDelta = function(value1, value2, preserveSign) {
	return -Math.max(0.1, Math.abs(value1 - value2) * this.getScale());
}

ValueAxis.prototype.getScale = function() {
	return (this.h - 2 * this.padding) / (this.max - this.min);
}