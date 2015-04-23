function LineChart(name, dataSource, symbol, indicator) {
	BaseChart.call(this, name, dataSource, symbol, indicator);

	this.valueColor = 0x0099FF;
}

LineChart.constructor = LineChart;
LineChart.prototype = Object.create(BaseChart.prototype);


LineChart.prototype.draw = function() {
	BaseChart.prototype.draw.call(this);

	if(this.data.length == 0) return;

	var firstTime = this.timeAxis.min;
	while(!this.data[firstTime] && firstTime <= this.timeAxis.max) firstTime += this.timeAxis.period;

	for(var f in this.fields) {
		this.lineStyle(2, this[this.fields[f] + "Color"], 0.5);

		if(this.data[firstTime])
			this.moveTo(this.timeAxis.getPosition(firstTime), this.valueAxis.getPosition(this.data[firstTime][this.fields[f]]));

		for(var t = firstTime + this.timeAxis.period; t <= this.timeAxis.max; t += this.timeAxis.period) {
			if(!this.data[t]) continue;

			this.lineTo(this.timeAxis.getPosition(t), this.valueAxis.getPosition(this.data[t][this.fields[f]]));
		}
	}
}