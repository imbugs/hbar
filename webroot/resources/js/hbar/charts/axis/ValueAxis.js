import BaseAxis from './BaseAxis';

function ValueAxis(width, height, padding) {
	BaseAxis.call(this, width, height);

	this.parentValueType = "price";

	this.padding = padding;

	this.w = 50;

	this.INCREMENTS = [ 100, 50, 25, 20, 10, 5, 2, 1, 0.5, 0.25, 0.1 ];
	this.FONT_SIZE = 10;
	this.TICK_SIZE = 2;
	this.PADDING_LEFT = 10;
}

ValueAxis.constructor = ValueAxis;
ValueAxis.prototype = Object.create(BaseAxis.prototype);

ValueAxis.prototype.draw = function() {
	BaseAxis.prototype.draw.call(this);

	while(this.children.length > 0) this.removeChild(this.getChildAt(0));

	this.lineStyle(1, 0x000000, 1);
	this.beginFill(0xFFFFFF, 0.5);
	this.drawRect(this.parentW - this.w, -1, this.w, this.parentH + 1);

	var delta = this.max - this.min;

	var bestInc = this.INCREMENTS[0];

	var maxIncs = this.parentH / 20;

	for(var i = 1; i < this.INCREMENTS.length; i++) {
		if(delta / this.INCREMENTS[i] < maxIncs) bestInc = this.INCREMENTS[i];
	}

	for(var value = Math.ceil((this.min + (this.padding === 0 ? 1 : 0)) / bestInc) * bestInc; value < this.max; value += bestInc) {
		var text = new PIXI.Text(value, {
			font : "10px Arial", 
			fill : "black", 
			dropShadow : true, 
			dropShadowDistance : 1, 
			dropShadowColor : 0xFFFFFF
		});
		text.y = this.getPosition(value) - (this.FONT_SIZE / 2 + 2);
		text.x = this.parentW - this.w + this.PADDING_LEFT;
		this.addChild(text);

		this.moveTo(this.parentW - this.w - this.TICK_SIZE, this.getPosition(value));
		this.lineTo(this.parentW - this.w + this.TICK_SIZE, this.getPosition(value));
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