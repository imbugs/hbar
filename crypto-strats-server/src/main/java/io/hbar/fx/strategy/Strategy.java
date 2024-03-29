package io.hbar.fx.strategy;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vertx.java.core.json.JsonObject;

import io.hbar.fx.data.DataManager;
import io.hbar.fx.data.Trade;
import io.hbar.fx.data.series.FieldSeries;
import io.hbar.fx.data.series.types.StrategyStats;

public abstract class Strategy {
	final static Logger logger = LogManager.getLogger(Strategy.class.getName());
	
	protected DataManager dataManager;
	protected FieldSeries<StrategyStats> series;
	
	
	public void setDataManager(DataManager dataManager) {
		this.dataManager = dataManager;
		
		series = new FieldSeries<StrategyStats>(StrategyStats.class, new JsonObject());
		
		loadData();
	}
	
	public void tick() {
	}
	
	public void candleEnd(int period) {
	}
	
	protected void loadData() {}
	
	public FieldSeries<StrategyStats> getSeries() {
		return series;
	}
	
	protected void addTrade(Trade trade) {
		int timestamp = trade.getTimestamp();
		if(trade.getVolume() < 0) timestamp ++; // 1 second offset for sells so both show up on same tick
		
		try {
			Map<StrategyStats, Double> row = series.getRow(timestamp);
			if(!row.isEmpty()) {
				double currentPrice = row.get(StrategyStats.Price);
				double currentVolume = row.get(StrategyStats.Volume);
				
				double newVolume = currentVolume + trade.getVolume();
				double newPrice = (currentPrice * currentVolume + trade.getPrice() * trade.getVolume()) / newVolume;
				
				logger.info("updating position (" + currentPrice + ", " + currentVolume + ") -> (" + newPrice + ", " + newVolume + ")");
				
				series.addRow(timestamp, newPrice, newVolume);
			} else {
				logger.info("adding new trade " + trade);
				series.addRow(timestamp, trade.getPrice(), trade.getVolume());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
