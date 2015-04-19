function OHLCChart(name, datasource, symbol, indicator, width, height) {
	BaseChart.call(this, name, datasource, symbol, indicator, width, height);

	this.fields = ["open", "high", "low", "close"];

	this.colorUp = 0X009900;
	this.colorDown = 0xFF0000;
}

OHLCChart.constructor = OHLCChart;
OHLCChart.prototype = Object.create(BaseChart.prototype);


OHLCChart.prototype.draw = function(data) {
	this.clear();

	this.data = data != undefined ? data : this.getData();

	if(this.data.length == 0) return;


	for(var t = this.startTime; t <= this.endTime; t += this.period) {
		if(!this.data[t]) continue;

		var lineColor = this.data[t].close >= this.data[t].open ? this.colorUp : this.colorDown;
		var fillColor = this.data[t].close >= this.data[t].open ? 0XFFFFFF : lineColor;

		var x = (t - this.startTime) / this.period;

		/*** High / Low Line ***/
		this.lineStyle(1, lineColor, 1);
		this.moveTo(x * this.delta + this.barSize / 2, this.valueAxis.getPosition(this.data[t].low));
		this.lineTo(x * this.delta + this.barSize / 2, this.valueAxis.getPosition(this.data[t].high));


		/*** Open / Close Rect ***/
		this.beginFill(fillColor);

		var low = Math.min(this.data[t].open, this.data[t].close);

		this.drawRect(x * this.delta, this.valueAxis.getPosition(low), this.barSize, this.valueAxis.getDelta(this.data[t].open, this.data[t].close));
	}

}

