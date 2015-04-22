function PointChart(name, dataSource, symbol, indicator) {
	BaseChart.call(this, name, dataSource, symbol, indicator);

	this.valueColor = 0x00CC33;
	this.valueRadius = 2;
}

PointChart.constructor = PointChart;
PointChart.prototype = Object.create(BaseChart.prototype);


PointChart.prototype.draw = function() {
	BaseChart.prototype.draw.call(this);

	if(this.data.length == 0) return;

	for(var f in this.fields) {
		this.lineStyle(1, this[this.fields[f] + "Color"], 0.5);

		for(var t = this.timeAxis.min + this.timeAxis.period; t <= this.timeAxis.max; t += this.timeAxis.period) {
			if(!this.data[t]) continue;

			this.drawCircle(this.timeAxis.getPosition(t), this.valueAxis.getPosition(this.data[t][this.fields[f]]), this[this.fields[f] + "Radius"]);
		}
	}
}