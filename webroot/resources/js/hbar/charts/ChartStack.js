
function ChartStack(canvas, DEBUG) 
{
	this.stage = new PIXI.Stage(0xFFFFFF);

	this.paddingY = 20;


	var rendererOptions = {
		antialiasing : false,
		transparent : false,
		resolution : 1,
		view : canvas
	}

	this.renderer = PIXI.autoDetectRenderer(800, 600, rendererOptions);

	this.valueAxis = new ValueAxis(this.renderer.width, this.renderer.height, this.paddingY);

	this.charts = [];

	requestAnimFrame(this.frame.bind(this));

	this.resize();


	var self = this;

	canvas.addEventListener('mousewheel',
		function(event) {
		    self.scroll(event.wheelDeltaX, event.wheelDeltaY);
		    event.preventDefault();
		    return false;
		}, false);

	canvas.addEventListener("touchmove",
		function(event) {
		    event.preventDefault();
		    return false;
		}, false);
}

ChartStack.prototype.addChart = function(chart)
{
	chart.setValueAxis(this.valueAxis);
	chart.setRedraw(this.draw.bind(this));

	this.charts.push(chart);
	this.stage.addChild(chart);
	this.resize();
}


ChartStack.prototype.frame = function()
{
	this.renderer.render(this.stage);
	
	requestAnimFrame(this.frame.bind(this));
}

ChartStack.prototype.draw = function()
{
	this.valueAxis.setMinMax(this.getLow(), this.getHigh());

	for(var i = 0; i < this.charts.length; i++)
	{
		this.charts[i].draw();
	}
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
	var width = this.renderer.view.parentElement.clientWidth,
		height = this.renderer.view.parentElement.clientHeight;

	this.renderer.resize(width, height);

	for(var i = 0; i < this.charts.length; i++)
	{
		this.charts[i].resize(width, height);
	}

	this.valueAxis.resize(width, height);

	this.draw();
}


ChartStack.prototype.getWidth = function()
{
	return this.renderer.width;
}

ChartStack.prototype.getHeight = function()
{
	return this.renderer.height;
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
		// console.log(this.charts[i].name, this.charts[i].getHigh());
		high = Math.max(high, this.charts[i].getHigh());
	}

	return high;
}

