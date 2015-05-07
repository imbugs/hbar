function StrategyChart(name, dataSource, symbol, indicator) {
	BaseChart.call(this, name, dataSource, symbol, indicator);

	this.fields = ["price"];
}

StrategyChart.constructor = StrategyChart;
StrategyChart.prototype = Object.create(BaseChart.prototype);

StrategyChart.prototype.draw = function() {
	BaseChart.prototype.draw.call(this);

	if(this.data.length == 0) return;

	var volume = 0;
	var price = 0;
	var pnl = 0;

	for(var f in this.fields) {

		for(t in this.data) {

			if(!this.data[t]) continue;

			this.lineStyle(1, this.getLineColor(t, f), 1);

			var x = this.timeAxis.getPosition(t);
			var y = this.valueAxis.getPosition(this.data[t][this.fields[f]]);
			var s = this.getRadius(t, f);
			var xs = s * 0.75;

			this.beginFill(0xFFFFFF, 1);
			this.drawPolygon([ x-xs,y, x,y+(this.data[t].volume > 0 ? -s : s), x+xs,y ]);

			this.beginFill(this.getFillColor(t, f), this.getFillAlpha(t, f));
			this.drawPolygon([ x-xs,y, x,y+(this.data[t].volume > 0 ? -s : s), x+xs,y ]);

			
			price = (price * volume + this.data[t].price * this.data[t].volume) / (volume + this.data[t].volume);
			
			volume += this.data[t].volume;
		}

	}
	if(Math.abs(volume) > 1e-5)
		console.log("Volume:", volume, "Price:", price);
}

StrategyChart.prototype.getRadius = function(timestamp, field) {
	return Math.max(3, (6 + Math.sqrt(Math.abs(this.data[timestamp].volume))) / 2)
}

BaseChart.prototype.getLineColor = function(timestamp, field) {
	return 0X666666;
}

StrategyChart.prototype.getFillColor = function(timestamp, field) {
	return this.data[timestamp].volume > 0 ? 0X066800 : 0Xce0000;
}

StrategyChart.prototype.getFillAlpha = function(timestamp, field) {
	return Math.min(0.75, Math.abs(this.data[timestamp].volume) / 20);
}