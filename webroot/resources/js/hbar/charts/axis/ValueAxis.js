import BaseAxis from './BaseAxis';

function ValueAxis(width, height, padding) {
	BaseAxis.call(this, width, height);

	this.parentValueType = "price";

	this.padding = padding;
}

ValueAxis.constructor = ValueAxis;
ValueAxis.prototype = Object.create(BaseAxis.prototype);

ValueAxis.WIDTH = 40;
ValueAxis.INCREMENTS = [ 500, 250, 200, 100, 50, 25, 20, 10, 5, 2, 1, 0.5, 0.25, 0.1 ];
ValueAxis.PRECISION  = [   0,   0,   0,   0,  0,  0,  0,  0, 0, 0, 0,   1,    2,   2 ];
ValueAxis.FONT_SIZE = 10;
ValueAxis.TICK_SIZE = 2;
ValueAxis.PADDING_LEFT = 10;

ValueAxis.prototype.draw = function() {
	BaseAxis.prototype.draw.call(this);

	while(this.children.length > 0) this.removeChild(this.getChildAt(0));

	this.lineStyle(1, 0x000000, 1);
	this.beginFill(0xFFFFFF, 0.5);
	this.drawRect(this.parentW - ValueAxis.WIDTH, -1, ValueAxis.WIDTH, this.parentH + 1);

	var delta = this.max - this.min;

	var bestInc = ValueAxis.INCREMENTS[0];

	var bestPrecision = ValueAxis.PRECISION[0];

	var maxIncs = this.parentH / 20;

	for(var i = 1; i < ValueAxis.INCREMENTS.length; i++) {
		if(delta / ValueAxis.INCREMENTS[i] < maxIncs) {
			bestInc = ValueAxis.INCREMENTS[i];
			bestPrecision = ValueAxis.PRECISION[i];
		}
	}

	for(var value = Math.ceil((this.min + (this.padding === 0 ? 1 : 0)) / bestInc) * bestInc; value < this.max; value += bestInc) {
		var text = new PIXI.Text(value.toFixed(bestPrecision), {
			font : "10px Arial", 
			fill : "black", 
			dropShadow : true, 
			dropShadowDistance : 1, 
			dropShadowColor : 0xFFFFFF
		});
		text.y = this.getPosition(value) - (ValueAxis.FONT_SIZE / 2 + 2);
		text.x = this.parentW - ValueAxis.WIDTH + ValueAxis.PADDING_LEFT;
		this.addChild(text);

		this.moveTo(this.parentW - ValueAxis.WIDTH - ValueAxis.TICK_SIZE, this.getPosition(value));
		this.lineTo(this.parentW - ValueAxis.WIDTH + ValueAxis.TICK_SIZE, this.getPosition(value));
	}

	
};

ValueAxis.prototype.getPosition = function(value) {
	return this.parentH - (value - this.min) * this.getScale() - this.padding;
};

ValueAxis.prototype.getDelta = function(value1, value2, preserveSign) {
	return -Math.max(0.1, Math.abs(value1 - value2) * this.getScale());
};

ValueAxis.prototype.getScale = function() {
	return (this.parentH - 2 * this.padding) / (this.max - this.min);
};

export default ValueAxis;