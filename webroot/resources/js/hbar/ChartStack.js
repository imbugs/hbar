import ValueAxis from './charts/axis/ValueAxis';

var PIXI = require('pixi.js');

function ChartStack(container, timeAxis) 
{
	this.displayResolution = window.devicePixelRatio;

	this.container = container;

	this.stage = new PIXI.Container();
	this.chartContainer = new PIXI.Container();
	this.crosshair = new PIXI.Graphics();
	this.crosshairPoint = new PIXI.Point(0, 0);

	this.stage.addChild(this.chartContainer);
	this.stage.addChild(this.crosshair);

	this.paddingY = 20;

	var rendererOptions = {
		antialiasing : false,
		transparent : true,
		resolution : this.displayResolution
	};


	this.renderer = PIXI.autoDetectRenderer(this.container.clientWidth, this.container.clientHeight, rendererOptions);
	this.renderer.view.className += "hbar-chart";

	this.container.appendChild(this.renderer.view);

	this.timeAxis = timeAxis;

	this.valueAxis = new ValueAxis(this.renderer.width, this.renderer.height, this.paddingY);

	this.stage.addChild(this.valueAxis);

	this.charts = [];
}

ChartStack.prototype.addChart = function(chart)
{
	if(!this.charts.length)
		this.valueAxis.parentValueType = chart.valueType;
	
	chart.setValueAxis(this.valueAxis);
	chart.setTimeAxis(this.timeAxis);
	chart.setRedraw(this.draw.bind(this));

	this.charts.push(chart);
	this.chartContainer.addChild(chart);

	this.draw();
};

ChartStack.prototype.draw = function()
{
	this.valueAxis.setMinMax(this.getLow(), this.getHigh());
	this.valueAxis.draw();

	for(var i = 0; i < this.charts.length; i++)
	{
		this.charts[i].draw();
	}

	this.render();
};

ChartStack.prototype.refreshLastTick = function()
{
	for(var i = 0; i < this.charts.length; i++)
	{
		this.charts[i].refreshLastTick();
	}
};

ChartStack.prototype.render = function() 
{
	this.renderer.render(this.stage);
};


ChartStack.prototype.scroll = function(scrollX, scrollY)
{
	for(var i = 0; i < this.charts.length; i++)
	{
		this.charts[i].scroll(scrollX, scrollY);
	}

	this.draw();
};

ChartStack.prototype.resize = function()
{
	var width = this.container.clientWidth;
	var height = this.container.clientHeight;

	this.renderer.resize(width, height);

	this.valueAxis.resize(width, height);

	for(var i = 0; i < this.charts.length; i++)
	{
		if(this.charts[0].valueType != this.charts[i].valueType)
		{
			this.charts[i].valueAxis.resize(width, height);
		}
	}

	this.draw();
};

ChartStack.prototype.setVerticalCrosshair = function(x)
{
	this.crosshairPoint.x = x;

	this.drawCrosshair();
};

ChartStack.prototype.setHorizontalCrosshair = function(y)
{
	this.crosshairPoint.y = y;

	this.drawCrosshair();
};

ChartStack.prototype.drawCrosshair = function()
{
	this.crosshair.clear();

	this.crosshair.lineStyle(1, 0x000000, 0.1);

	this.crosshair.moveTo(this.crosshairPoint.x, 0);
	this.crosshair.lineTo(this.crosshairPoint.x, this.renderer.height);

	this.crosshair.moveTo(0, this.crosshairPoint.y);
	this.crosshair.lineTo(this.renderer.width, this.crosshairPoint.y);
};

ChartStack.prototype.getLow = function() 
{
	var low = Number.MAX_VALUE;
	for(var i = 0; i < this.charts.length; i++)
	{
		if(this.charts[0].valueType != this.charts[i].valueType) continue;
		low = Math.min(low, this.charts[i].getLow());
	}

	return low;
};

ChartStack.prototype.getHigh = function() 
{
	var high = Number.MIN_VALUE;

	for(var i = 0; i < this.charts.length; i++)
	{
		if(this.charts[0].valueType != this.charts[i].valueType) continue;
		high = Math.max(high, this.charts[i].getHigh());
	}

	return high;
};

export default ChartStack;

