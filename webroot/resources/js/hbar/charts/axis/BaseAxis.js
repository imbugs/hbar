var PIXI = require('pixi.js');

function BaseAxis(width, height) {
	PIXI.Graphics.call(this);
	
	this.w = width;
	this.h = height;

	this.min = 0;
	this.max = 0;
}

BaseAxis.constructor = BaseAxis;
BaseAxis.prototype = Object.create(PIXI.Graphics.prototype);

BaseAxis.prototype.resize = function(width, height) {
	this.w = width;
	this.h = height;
}

BaseAxis.prototype.getPosition = function(value) {
	return 0;
}

BaseAxis.prototype.getDelta = function(value1, value2) {
	return 0;
}

BaseAxis.prototype.setMinMax = function(min, max) {
	this.min = min; 
	this.max = max;
}

export default BaseAxis;