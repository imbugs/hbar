
function ChartStack(container, timeAxis) 
{
	this.container = container;

	this.stage = new PIXI.Stage(0xFFFFFF);

	this.paddingY = 20;

	var rendererOptions = {
		antialiasing : false,
		transparent : false,
		resolution : 1
	}

	this.renderer = PIXI.autoDetectRenderer(this.container.clientWidth, this.container.clientHeight, rendererOptions);
	this.renderer.view.className += "hbar-chart";

	this.container.appendChild(this.renderer.view);

	this.timeAxis = timeAxis;

	this.valueAxis = new ValueAxis(this.renderer.width, this.renderer.height, this.paddingY);

	this.charts = [];
}

ChartStack.prototype.addChart = function(chart)
{
	chart.setValueAxis(this.valueAxis);
	chart.setTimeAxis(this.timeAxis);
	chart.setRedraw(this.draw.bind(this));

	this.charts.push(chart);
	this.stage.addChild(chart);

	this.draw();
}

ChartStack.prototype.draw = function()
{
	this.valueAxis.setMinMax(this.getLow(), this.getHigh());

	for(var i = 0; i < this.charts.length; i++)
	{
		this.charts[i].draw();
	}

	this.renderer.render(this.stage);
}


ChartStack.prototype.scroll = function(scrollX, scrollY)
{
	for(var i = 0; i < this.charts.length; i++)
	{
		this.charts[i].scroll(scrollX, scrollY);
	}

	this.draw();
}

ChartStack.prototype.resize = function()
{
	width = this.container.clientWidth;
	height = this.container.clientHeight;

	this.renderer.resize(width, height);

	this.valueAxis.resize(width, height);

	this.draw();
}


ChartStack.prototype.getLow = function() 
{
	var low = Number.MAX_VALUE;
	for(var i = 0; i < this.charts.length; i++)
	{
		low = Math.min(low, this.charts[i].getLow());
	}

	return low;
}

ChartStack.prototype.getHigh = function() 
{
	var high = Number.MIN_VALUE;

	for(var i = 0; i < this.charts.length; i++)
	{
		high = Math.max(high, this.charts[i].getHigh());
	}

	return high;
}

