function HistogramChart(name, dataSource, symbol, indicator) {
	BaseChart.call(this, name, dataSource, symbol, indicator);

	this.valueColor = 0x0099FF;
}

HistogramChart.constructor = HistogramChart;
HistogramChart.prototype = Object.create(BaseChart.prototype);


HistogramChart.prototype.draw = function() {
	BaseChart.prototype.draw.call(this);

	if(this.data.length == 0) return;


	for(var f in this.fields) {
		this.lineStyle(1, this[this.fields[f] + "Color"], 0.5);

		for(var t = this.timeAxis.min; t <= this.timeAxis.max; t += this.timeAxis.period) {
			if(!this.data[t]) continue;

			this.drawRect(this.timeAxis.getMinPosition(t), this.valueAxis.getPosition(0), this.timeAxis.barSize - 1, this.valueAxis.getDelta(0, this.data[t][this.fields[f]]));
		}
	}
}