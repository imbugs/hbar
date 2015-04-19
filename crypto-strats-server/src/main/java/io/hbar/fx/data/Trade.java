package io.hbar.fx.data;

public class Trade {
	private int timesamp;
	private double price;
	private double volume;
	
	public Trade(int timesamp, double price, double volume) {
		super();
		this.timesamp = timesamp;
		this.price = price;
		this.volume = volume;
	}
	
	public int getTimestamp() {
		return timesamp;
	}

	public double getPrice() {
		return price;
	}

	public double getVolume() {
		return volume;
	}
	
	public String toString() {
		return "[ timestamp : " + getTimestamp() + ", price : " + getPrice() + ", volume: " + getVolume() + " ]";
	}
}
