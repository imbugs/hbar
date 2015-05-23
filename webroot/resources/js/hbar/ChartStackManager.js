import TimeAxis from './charts/axis/TimeAxis';
import ChartStack from './ChartStack';


function ChartStackManager(container, protoSock) 
{
	this.container = container;

	this.protoSock = protoSock;

	this.timeAxis = new TimeAxis(this.container.clientWidth, 20);

	this.stacks = [];

	this.protoSock.sendMaxTimeRequest(function(timestamp) {
		this.setPeriod(this.timeAxis.period, timestamp);
	}.bind(this));
}

ChartStackManager.prototype.addChart = function(stack, chart)
{
	if(!this.stacks[stack]) 
	{
		var div = document.createElement("div");
		div.setAttribute("id", stack);
		div.className += "hbar-chart-container";

		var stackKeys = Object.keys(this.stacks);

		var height = (100 / (stackKeys.length + 1)) + "%";

		div.style.height = height;

		for(var s in this.stacks)
		{
			this.stacks[s].container.style.height = height;
			this.stacks[s].resize();
		}

		this.container.appendChild(div);

		this.stacks[stack] = new ChartStack(div, this.timeAxis);

		if(stackKeys.length) 
		{
			$("#" + stackKeys[stackKeys.length - 1]).resizable(
			{
				handles: 's',
				alsoResizeReverse: "#" + stack,
				resize: this.resize.bind(this)
			});
		}


		div.addEventListener("mousemove",
			function(event) {
				this.stacks[stack].setHorizontalCrosshair(event.layerY);
			}.bind(this), false);

		div.addEventListener("mouseout",
			function(event) {
				this.stacks[stack].setHorizontalCrosshair(-1);
			}.bind(this), false);
	}

	this.stacks[stack].addChart(chart);
}

ChartStackManager.prototype.setPeriod = function(period, timestamp)
{
	this.timeAxis.setPeriod(period, timestamp);
	
	for(var stack in this.stacks)
	{
		this.stacks[stack].draw();
	}
}

ChartStackManager.prototype.refreshLastTick = function()
{
	for(var stack in this.stacks)
	{
		this.stacks[stack].refreshLastTick();
	}
}

ChartStackManager.prototype.scroll = function(scrollX, scrollY)
{
	this.timeAxis.scroll(scrollX, scrollY);
		    
	for(var stack in this.stacks)
	{
		this.stacks[stack].draw();
	}
}

ChartStackManager.prototype.resize = function()
{
	this.timeAxis.resize(this.container.clientWidth, this.timeAxis.h);
		    
	for(var stack in this.stacks)
	{
		this.stacks[stack].resize();
	}
}

ChartStackManager.prototype.drawCrosshair = function(x, y) 
{
	for(var stack in this.stacks)
	{
		this.stacks[stack].setVerticalCrosshair(x);
		this.stacks[stack].render();
	}
}

export default ChartStackManager;