package io.hbar.fx.strategy;

import java.util.ArrayList;
import java.util.List;

import io.hbar.fx.data.Trade;
import io.hbar.fx.data.series.FieldSeries;
import io.hbar.fx.data.series.types.OHLCV;
import io.hbar.fx.data.series.types.SAR;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vertx.java.core.json.JsonObject;

public class TestStrategy extends Strategy {
	final static String BULLISH = "bullish";
	final static String BEARISH = "bearish";

	final Logger logger = LogManager.getLogger(TestStrategy.class.getName());

	final JsonObject NO_OPTIONS = new JsonObject();
	final JsonObject SAR_OPTIONS = new JsonObject().putNumber("acceleration", 0.02).putNumber("maximum", 0.2);
	final int PERIOD_SHORT = 3600;
	final int PERIOD_LONG = 4 * PERIOD_SHORT;

	double BUY_THRESH = 0.01;

	FieldSeries<OHLCV> ohlcvSeries1H, ohlcvSeries4H;
	FieldSeries<SAR> sarSeries1H, sarSeries4H;
	
	List<Leg> legs = new ArrayList<Leg>();

	@Override
	public void tick() {
		loadData();
		
		Leg currentLeg = getCurrentLeg();
		int currentTimestamp = ohlcvSeries1H.getLastTimestamp();
		double currentPrice = ohlcvSeries1H.getValue(currentTimestamp, OHLCV.Close);
		
		/*** liquidate ***/
		if (currentLeg != null && !currentLeg.liquidated && (longSarCross() 
				|| (currentLeg.direction.equals(Leg.LONG) && currentPrice < currentLeg.getPrice() * 0.995)
				|| (currentLeg.direction.equals(Leg.SHORT) && currentPrice > currentLeg.getPrice() * 1.005))) {
			
			
			currentLeg.liquidated = true;
			logger.info("liquidate @ " + currentPrice + "(position was: " + currentLeg.getVolume() + " @ " + currentLeg.getPrice() + ")");
			
			addTrade(new Trade(currentTimestamp, currentPrice, -currentLeg.getVolume()));
		}
	}

	@Override
	public void candleEnd(int period) {
		loadData();
		
		if (period == PERIOD_SHORT) {
			// if within buy thresh, buy!
			int currentTimestamp = ohlcvSeries1H.getLastTimestamp();
			double currentPrice = ohlcvSeries1H.getValue(currentTimestamp, OHLCV.Close);
			double currentSar = sarSeries1H.getValue(currentTimestamp, SAR.Value);
			double delta = (currentPrice - currentSar) / currentPrice;

			if (Math.abs(delta) < BUY_THRESH) {
				Leg currentLeg = getCurrentLeg();
				
				/* start new leg */
				if(currentLeg == null || currentLeg.liquidated) {
					currentLeg = new Leg(getLongTrend(currentTimestamp).equals(TestStrategy.BULLISH) ? Leg.LONG : Leg.SHORT);
					
					legs.add(currentLeg);
					logger.info("new leg: " + currentLeg.direction);
				}
				
				if(currentLeg.getVolume() == 0
						|| (currentLeg.direction.equals(Leg.LONG) && currentPrice > currentLeg.previousTradePrice()) 
						|| (currentLeg.direction.equals(Leg.SHORT) && currentPrice < currentLeg.previousTradePrice())) {
					
					Trade trade = new Trade(currentTimestamp, currentPrice, currentLeg.direction.equals(Leg.SHORT) ? -0.1 : 0.1);
					currentLeg.addTrade(trade);
					
					addTrade(trade);
					
					logger.info("buy @ " + currentPrice + "(current position: " + currentLeg.getVolume() + " @ " + currentLeg.getPrice() + ")");
				}
			}

		} else if (period == PERIOD_LONG) {
			
		}
	}

	@Override
	public void loadData() {
		ohlcvSeries1H = getOHLCVSeries1H();
		ohlcvSeries4H = getOHLCVSeries4H();

		sarSeries1H = getSARSeries1H();
		sarSeries4H = getSARSeries4H();
	}
	
	private FieldSeries<OHLCV> getOHLCVSeries1H() {
		return (FieldSeries<OHLCV>) dataManager.getSeries("BTCUSD:Bitfinex", OHLCV.class, PERIOD_SHORT, NO_OPTIONS);
	}
	
	private FieldSeries<OHLCV> getOHLCVSeries4H() {
		return (FieldSeries<OHLCV>) dataManager.getSeries("BTCUSD:Bitfinex", OHLCV.class, PERIOD_LONG, NO_OPTIONS);
	}
	
	private FieldSeries<SAR> getSARSeries1H() {
		return (FieldSeries<SAR>) dataManager.getSeries("BTCUSD:Bitfinex", SAR.class, PERIOD_SHORT, SAR_OPTIONS);
	}
	
	private FieldSeries<SAR> getSARSeries4H() {
		return (FieldSeries<SAR>) dataManager.getSeries("BTCUSD:Bitfinex", SAR.class, PERIOD_LONG, SAR_OPTIONS);
	}

	private boolean longSarCross() {
		int currentTimestamp = ohlcvSeries4H.getLastTimestamp();
		int previousTimestamp = ohlcvSeries4H.getPreviousTimestamp();

		String previousTrend = getLongTrend(previousTimestamp);
		String currentTrend = getLongTrend(currentTimestamp);

		return !currentTrend.equals(previousTrend);
	}

	private String getLongTrend(int timestamp) {
		int t = ((int)(timestamp / PERIOD_LONG)) * PERIOD_LONG;
		
		double price = ohlcvSeries4H.getValue(t, OHLCV.Close);
		double sar = sarSeries4H.getValue(t, SAR.Value);
		return price < sar ? TestStrategy.BEARISH : TestStrategy.BULLISH;
	}
	
	private Leg getCurrentLeg() {
		return legs.size() > 0 ? legs.get(legs.size() - 1) : null;
	}
	
	
	
	private class Leg {
		public final static String LONG = "long";
		public final static String SHORT = "short";
		
		public boolean liquidated = false;
		
		public String direction;
		
		private Trade lastTrade;
		
		private TradeList tradeList = new TradeList();
		
		public Leg(String direction) {
			this.direction = direction;
		}
		
		public void addTrade(Trade trade) {
			tradeList.addTrade(trade);
			lastTrade = trade;
		}
		
		public double getVolume() {
			double v = 0;
			for(Trade trade : tradeList.trades) v += trade.getVolume();
			return v;
		}
		
		public double getPrice() {
			double p = 0;
			double v = 0;
			for(Trade trade : tradeList.trades) {
				p = (p * v + trade.getPrice() * trade.getVolume()) / (v + trade.getVolume());
				v += trade.getVolume();
			}
			return p;
		}
		
		public double previousTradePrice() {
			return lastTrade.getPrice();
		}
	}
	
	private class TradeList {
		List<Trade> trades = new ArrayList<Trade>();
		
		public void addTrade(Trade trade) {
			trades.add(trade);
		}
	}
}
