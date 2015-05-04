package io.hbar.fx.strategy;

import io.hbar.fx.data.Trade;
import io.hbar.fx.data.series.FieldSeries;
import io.hbar.fx.data.series.types.OHLCV;
import io.hbar.fx.data.series.types.Order;
import io.hbar.fx.data.series.types.SAR;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vertx.java.core.json.JsonObject;


public class SarStrategy extends Strategy {
	final static Logger logger = LogManager.getLogger(SarStrategy.class.getName());
	
	JsonObject OPTIONS = new JsonObject().putNumber("acceleration", 0.02).putNumber("maximum", 0.2);
	double BTC = 0;
	double PPBTC = 0;
	double USD = 1000;
	int PERIOD = 3600;
	
	double BUY_THRESH = 0.01;
	double LIQ_THRESH = 0.02;
	
	List<Leg> legs = new ArrayList<Leg>();
	
	@Override
	public void tick() {
		FieldSeries<OHLCV> ohlcvSeries = (FieldSeries<OHLCV>) dataManager.getSeries("BTCUSD:Bitfinex", OHLCV.class, PERIOD, new JsonObject());
		int[] timestamps = ohlcvSeries.getTimestamps();
		int time = timestamps[timestamps.length - 1];
		
		FieldSeries<SAR> sarSeries = (FieldSeries<SAR>) dataManager.getSeries("BTCUSD:Bitfinex", SAR.class, PERIOD, OPTIONS);
		
		Map<OHLCV, Double> ohlcv = ohlcvSeries.getRow(time);
		Map<SAR, Double> sar = sarSeries.getRow(time);
		
		double currentPrice = ohlcv.get(OHLCV.Close);
		double currentDelta = (currentPrice - sar.get(SAR.Value)) / currentPrice;
		double absCurrentDelta = Math.abs(currentDelta);
		
		Leg currentLeg = legs.size() > 0 ? legs.get(legs.size() - 1) : null;
		
		logger.info("position: " + getPosition() + "price: " + currentPrice + " sar: " + sar.get(SAR.Value) + " delta: " + currentDelta);
		
		/* start new leg */
		if(currentLeg == null || currentLeg.liquidated) {
			if(absCurrentDelta < BUY_THRESH) {
				currentLeg = new Leg(currentDelta > 0 ? Leg.LONG : Leg.SHORT);
				
				legs.add(currentLeg);
				
				logger.info("new leg: " + currentLeg.direction);
			}
		}
		
		/* manage current leg */
		if(currentLeg != null) {
			Trade trade = null;
			
			if(currentLeg.direction.equals(Leg.LONG)) {
				if(absCurrentDelta < BUY_THRESH && currentDelta > 0) {
					/* buy more!!! */
					trade = new Trade(time, currentPrice, 0.1);
					currentLeg.addTrade(trade);
					
					addTrade(trade);
					
					logger.info("long " + trade);
				} else if(currentDelta < 0 && !currentLeg.liquidated && (absCurrentDelta > LIQ_THRESH || absCurrentDelta < BUY_THRESH)) {
					/* liquidate!!! */
					trade = new Trade(time + 1, currentPrice, -currentLeg.getLongVolume());
					currentLeg.addTrade(trade);
					currentLeg.liquidated = true;
					
					addTrade(trade);
					
					logger.info("liquidate long " + trade);
					
					if(absCurrentDelta < BUY_THRESH) {
						/* swing it!!! */
						currentLeg = new Leg(Leg.SHORT);
						trade = new Trade(time, currentPrice, trade.getVolume() / 2);
						currentLeg.addTrade(trade);
						
						legs.add(currentLeg);
						
						addTrade(trade);
						
						logger.info("swing short " + trade);
					}
				}
			} else {
				if(absCurrentDelta < BUY_THRESH && currentDelta < 0) {
					/* short more!!! */
					trade = new Trade(time + 1, currentPrice, -0.1);
					currentLeg.addTrade(trade);
					
					addTrade(trade);
					
					logger.info("short " + trade);
				} else if(currentDelta > 0 && !currentLeg.liquidated && (absCurrentDelta > LIQ_THRESH || absCurrentDelta < BUY_THRESH)) {
					/* liquidate!!! */
					trade = new Trade(time, currentPrice, -currentLeg.getShortVolume());
					currentLeg.addTrade(trade);
					currentLeg.liquidated = true;
					
					addTrade(trade);
					
					logger.info("liquidate short " + trade);
					
					if(absCurrentDelta < BUY_THRESH) {
						/* swing it!!! */
						currentLeg = new Leg(Leg.LONG);
						trade = new Trade(time, currentPrice, trade.getVolume() / 2);
						currentLeg.addTrade(trade);
						
						legs.add(currentLeg);
						
						addTrade(trade);
						
						logger.info("swing long " + trade);
					}
				}
			}
		
		}
		
		if(currentLeg != null) logger.info("legPos: " + currentLeg.getVolume());
	}
	
	private Trade getPosition() {
		double volume = 0;
		
		for(Integer row : series.table.rowKeySet()) {
			volume += series.table.get(row, Order.Volume);
		}
		return new Trade(0, 0, volume);
	}
	
	private class Leg {
		public final static String LONG = "long";
		public final static String SHORT = "short";
		
		public boolean liquidated = false;
		
		public String direction;
		
		private TradeList tradeList = new TradeList();
		
		public Leg(String direction) {
			this.direction = direction;
		}
		
		public void addTrade(Trade trade) {
			tradeList.addTrade(trade);
		}
		
		public double getLongVolume() {
			double v = 0;
			
			for(Trade trade : tradeList.trades) {
				if(trade.getVolume() > 0) v += trade.getVolume();
			}
			
			return v;
		}
		
		public double getShortVolume() {
			double v = 0;
			
			for(Trade trade : tradeList.trades) {
				if(trade.getVolume() < 0) v += trade.getVolume();
			}
			
			return v;
		}
		
		public double getVolume() {
			double v = 0;
			
			for(Trade trade : tradeList.trades) v += trade.getVolume();
			
			return v;
		}
	}
	
	private class TradeList {
		List<Trade> trades = new ArrayList<Trade>();
		
		public void addTrade(Trade trade) {
			trades.add(trade);
		}
	}
}
