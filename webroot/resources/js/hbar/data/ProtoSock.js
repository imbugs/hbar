import vertx from '../../lib/vertxbus';

function ProtoSock(url, builders, onReady)
{

	this.ProtoBuf = require('protobufjs');
	this.builders = builders;

	this.cache = {};

	this.eventBus = new vertx.EventBus(url);

	this.eventBus.onopen = function() {
		console.log("connected to websocket!");

		onReady();
	};

}

ProtoSock.prototype.getData = function(request, cb)
{
	var seriesData = this.getSeriesData(request.symbol, request.indicator, request.period, this.hashCode(request.options));

	var periods =  Math.floor((request.endTime - request.startTime) / request.period);
	var buffer = Math.floor(periods / 2) * request.period;
	request.startTime -= buffer;
	request.endTime += buffer;

	if(request.startTime < seriesData.min || request.endTime > seriesData.max)
	{
		if(request.startTime < seriesData.min) request.startTime -= buffer;
		else if(request.startTime > seriesData.max) request.startTime = seriesData.max + request.period;

		if(request.endTime > seriesData.max) request.endTime += buffer;
		else if(request.endTime < seriesData.min) request.endTime = seriesData.min - request.period;


		seriesData.min = Math.min(seriesData.min, request.startTime);
		seriesData.max = Math.max(seriesData.max, request.endTime);

		this.sendDataRequest(request, seriesData, cb);
	}

	return seriesData.data;
};

ProtoSock.prototype.tick = function(steps, cb)
{
	this.eventBus.send('tick', { steps : steps }, function(data)
	{
		if(cb) cb();
	}.bind(this));
};

ProtoSock.prototype.refreshLastTick = function(request, cb)
{
	var seriesData = this.getSeriesData(request.symbol, request.indicator, request.period, this.hashCode(request.options));

	var timestamps = Object.keys(seriesData.data);

	if(timestamps.length > 0) {
		request.startTime = parseInt(timestamps[timestamps.length - 1]) - request.period;
	} else {
		request.startTime = seriesData.min;
	}

	request.endTime = seriesData.max;

	this.sendDataRequest(request, seriesData, cb);
};

ProtoSock.prototype.sendDataRequest = function(request, seriesData, cb)
{
	this.eventBus.send('data', request, function(data64)
		{
			var data = this.builders[request.indicator].decode64(data64.replace(/\n/gm, "")).series;

			for(var i = 0; i < data.length; i++)
			{
				seriesData.data[data[i].timestamp] = data[i];
				seriesData.min = Math.min(seriesData.min, data[i].timestamp);
				seriesData.max = Math.max(seriesData.max, data[i].timestamp);
			}

			if(cb) cb();
		}.bind(this));
};

ProtoSock.prototype.sendMaxTimeRequest = function(cb) {
	this.eventBus.send('maxTime', {}, function(data)
		{
			if(cb) cb(data.timestamp);
		}.bind(this));
};

ProtoSock.prototype.getMin = function(request, fields)
{
	var seriesData = this.getSeriesData(request.symbol, request.indicator, request.period, this.hashCode(request.options)).data;

	var low = Number.MAX_VALUE;

	for(var f in fields)
		for(var t = request.startTime; t <= request.endTime; t += request.period)
			if(seriesData[t]) low = Math.min(low, seriesData[t][fields[f]]);

	return low;
};

ProtoSock.prototype.getMax = function(request, fields)
{
	var seriesData = this.getSeriesData(request.symbol, request.indicator, request.period, this.hashCode(request.options)).data;

	var high = Number.MIN_VALUE;

	for(var f in fields)
		for(var t = request.startTime; t <= request.endTime; t += request.period)
			if(seriesData[t]) high = Math.max(high, seriesData[t][fields[f]]);

	return high;
};

ProtoSock.prototype.getSymbolData = function(symbol)
{
	if(!this.cache[symbol]) this.cache[symbol] = {};

	return this.cache[symbol];
};

ProtoSock.prototype.getIndicatorData = function(symbol, indicator)
{
	var symbolData = this.getSymbolData(symbol);

	if(!symbolData[indicator]) symbolData[indicator] = {};

	return symbolData[indicator];
};

ProtoSock.prototype.getPeriodData = function(symbol, indicator, period)
{
	var indicatorData = this.getIndicatorData(symbol, indicator);

	if(!indicatorData[period]) indicatorData[period] = {};

	return indicatorData[period];
};

ProtoSock.prototype.getSeriesData = function(symbol, indicator, period, optionsHash)
{
	var periodData = this.getPeriodData(symbol, indicator, period);

	if(!periodData[optionsHash]) periodData[optionsHash] = { 
		data : [], 
		min : Number.MAX_VALUE, 
		max : Number.MIN_VALUE
	};

	return periodData[optionsHash];
};

ProtoSock.prototype.hashCode = function(obj)
{
	var s = JSON.stringify(obj);
	return s.split("").reduce(function(a,b){a=((a<<5)-a)+b.charCodeAt(0);return a&a;},0);
};

export default ProtoSock;