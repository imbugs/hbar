function DataRequest(symbol, indicator, period, startTime, endTime, options) {
	this.symbol 	= symbol;
	this.indicator 	= indicator;
	this.period 	= period;
	this.startTime 	= startTime;
	this.endTime 	= endTime;
	this.options 	= options;
}