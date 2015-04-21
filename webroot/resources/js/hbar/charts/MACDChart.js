function MACDChart(name, datasource, symbol, indicator) {
	LineChart.call(this, name, datasource, symbol, indicator);

	this.fields = ["macd", "signal", "histogram"];
	this.maFields = ["macd", "signal" ];

	this.macdColor = 0x0099FF;
	this.signalColor = 0xFF6600;
	this.histogramColor = 0x9999FF;

	this.options.fastPeriod = 12;
	this.options.slowPeriod = 26;
	this.options.signalPeriod = 9;
}

MACDChart.constructor = MACDChart;
MACDChart.prototype = Object.create(LineChart.prototype);

MACDChart.prototype.draw = function(data) {
	BaseChart.prototype.draw.call(this, data);

	this.drawMAs();

	this.drawHistogram();
}

MACDChart.prototype.drawMAs = function() {
	var firstTime = this.timeAxis.min;
	while(!this.data[firstTime] && firstTime <= this.timeAxis.max) firstTime += this.timeAxis.period;

	for(var f in this.maFields) {
		this.lineStyle(2, this[this.maFields[f] + "Color"], 0.5);

		if(this.data[firstTime])
			this.moveTo(this.timeAxis.getPosition(firstTime), this.valueAxis.getPosition(this.data[firstTime][this.maFields[f]]));

		for(var t = firstTime + this.timeAxis.period; t <= this.timeAxis.max; t += this.timeAxis.period) {
			if(!this.data[t]) continue;

			this.lineTo(this.timeAxis.getPosition(t), this.valueAxis.getPosition(this.data[t][this.maFields[f]]));
		}
	}
}

MACDChart.prototype.drawHistogram = function() {
	this.lineStyle(1, this.histogramColor, 0.5);

	for(var t = this.timeAxis.min; t <= this.timeAxis.max; t += this.timeAxis.period) {
		if(!this.data[t]) continue;

		var delta = this.valueAxis.getDelta(0, this.data[t].histogram);
		var low = this.data[t].histogram < 0 ? this.data[t].histogram : 0;

		this.drawRect(this.timeAxis.getMinPosition(t), this.valueAxis.getPosition(low), this.timeAxis.barSize - 1, delta);
	}
}