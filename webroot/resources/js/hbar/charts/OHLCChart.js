import BaseChart from './BaseChart';

function OHLCChart(name, dataSource, symbol, indicator) {
	BaseChart.call(this, name, dataSource, symbol, indicator);

	this.fields = ["open", "high", "low", "close"];

	this.colorUp = 0X009900;
	this.colorDown = 0xFF0000;
}

OHLCChart.constructor = OHLCChart;
OHLCChart.prototype = Object.create(BaseChart.prototype);


OHLCChart.prototype.draw = function() {
	BaseChart.prototype.draw.call(this);

	if(this.data.length == 0) return;


	for(var t = this.timeAxis.min; t <= this.timeAxis.max; t += this.timeAxis.period) {
		if(!this.data[t]) continue;

		var lineColor = this.data[t].close >= this.data[t].open ? this.colorUp : this.colorDown;
		var fillColor = this.data[t].close >= this.data[t].open ? 0XFFFFFF : lineColor;

		/*** High / Low Line ***/
		this.lineStyle(1, lineColor, 1);

		this.moveTo(this.timeAxis.getPosition(t), this.valueAxis.getPosition(this.data[t].low));
		this.lineTo(this.timeAxis.getPosition(t), this.valueAxis.getPosition(this.data[t].high));
		
		/*** Open / Close Rect ***/
		this.beginFill(fillColor);

		var low = Math.min(this.data[t].open, this.data[t].close);

		this.drawRect(this.timeAxis.getMinPosition(t), this.valueAxis.getPosition(low), this.timeAxis.barSize - 1, this.valueAxis.getDelta(this.data[t].open, this.data[t].close));
	}

}

export default OHLCChart;