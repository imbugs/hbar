function LineChart(name, datasource, symbol, indicator, width, height) {
	BaseChart.call(this, name, datasource, symbol, indicator, width, height);

	this.valueColor = 0x0099FF;
}

LineChart.constructor = LineChart;
LineChart.prototype = Object.create(BaseChart.prototype);


LineChart.prototype.draw = function(data) {
	this.clear();
	
	this.data = data != undefined ? data : this.getData();

	if(this.data.length == 0) return;


	var firstTime = this.startTime;
	while(!this.data[firstTime] && firstTime <= this.endTime) firstTime += this.period;

	for(var f in this.fields) {
		this.lineStyle(2, this[this.fields[f] + "Color"], 0.5);

		if(this.data[firstTime])
			this.moveTo(this.delta * (firstTime - this.startTime) / this.period + this.barSize / 2, this.valueAxis.getPosition(this.data[firstTime][this.fields[f]]));

		for(var t = firstTime + this.period; t <= this.endTime; t += this.period) {
			if(!this.data[t]) continue;

			var x = (t - this.startTime) / this.period;

			this.lineTo(x * this.delta + this.barSize / 2, Math.round(this.valueAxis.getPosition(this.data[t][this.fields[f]])));
		}
	}
}