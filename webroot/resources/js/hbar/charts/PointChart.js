import BaseChart from './BaseChart';

function PointChart(name, dataSource, symbol, indicator) {
	BaseChart.call(this, name, dataSource, symbol, indicator);

	this.valueLineColor = 0x00AA33;
	this.valueFillColor = 0xCCCCCC;
	this.valueRadius = 1;
}

PointChart.constructor = PointChart;
PointChart.prototype = Object.create(BaseChart.prototype);


PointChart.prototype.draw = function() {
	BaseChart.prototype.draw.call(this);

	if(this.data.length === 0) return;

	for(var f in this.fields) {
		for(var t = this.timeAxis.min + this.timeAxis.period; t <= this.timeAxis.max; t += this.timeAxis.period) {
			if(!this.data[t]) continue;

			this.lineStyle(1, this.getLineColor(t, f), 1);

			this.beginFill(this.getFillColor(t, f), this.getFillAlpha(t, f));
			this.drawCircle(this.timeAxis.getPosition(t), this.valueAxis.getPosition(this.data[t][this.fields[f]]), this.getRadius(t, f));
		}
	}
};

PointChart.prototype.getRadius = function(timestamp, field) {
	return Math.min(2, this.timeAxis.barSize / 2);
};

PointChart.prototype.getLineColor = function(timestamp, field) {
	return this[this.fields[field] + "LineColor"];
};

PointChart.prototype.getFillColor = function(timestamp, field) {
	return this[this.fields[field] + "FillColor"];
};

PointChart.prototype.getFillAlpha = function(timestamp, field) {
	return 1;
};

export default PointChart;