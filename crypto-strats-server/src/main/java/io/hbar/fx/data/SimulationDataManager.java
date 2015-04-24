package io.hbar.fx.data;

import io.hbar.fx.data.series.FieldSeries;
import io.hbar.fx.data.series.OHLCVSeries;
import io.hbar.fx.ta.TaLib;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vertx.java.core.json.JsonObject;


public class SimulationDataManager extends DataManager {
	
	final static Logger logger = LogManager.getLogger(SimulationDataManager.class.getName());
	
	protected int startTime;
	protected int endTime;
	
	protected Trade lastTrade = null;
	
	protected Iterator<Trade> tradeIterator;

	public SimulationDataManager(String dataFile) {
		super(dataFile);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_YEAR, -7);
		
		endTime = (int) (cal.getTimeInMillis() / 1000);
		
		cal.add(Calendar.DAY_OF_YEAR, -30);
		
		startTime = (int) (cal.getTimeInMillis() / 1000);
		
		tradeIterator = tradeData.iterator();
	}
	
	public void tick() {
		if(tradeIterator.hasNext()) {
			lastTrade = tradeIterator.next();
			
			for(String symbol : cache.keySet()) {
				HashMap<String, HashMap<Integer, HashMap<Integer, FieldSeries<?>>>> indicatorCache = cache.get(symbol);
				for(String indicator : indicatorCache.keySet()) {
					HashMap<Integer, HashMap<Integer, FieldSeries<?>>> periodCache = indicatorCache.get(indicator);
					for(Integer period : periodCache.keySet()) {
						HashMap<Integer, FieldSeries<?>> hashCache = periodCache.get(period);
						for(Integer hash : hashCache.keySet()) {
							FieldSeries<?> series = hashCache.get(hash);

							if(indicator.equals("OHLCV")) {
								try {
									((OHLCVSeries) series).addTrade(lastTrade);
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								try {
									cache.get(symbol).get(indicator).get(period).put(hash, (FieldSeries<?>) TaLib.class.getMethod(indicator.toLowerCase(), OHLCVSeries.class, JsonObject.class).invoke(null, (OHLCVSeries) getSeries(symbol, "OHLCV", period, new JsonObject()), series.getOptions()));
								} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	protected OHLCVSeries getOHLCVSeries(int period) {
		OHLCVSeries ohlcv = new OHLCVSeries(period);
		
		try {
			if(lastTrade == null) {
				Trade trade = null;
				
				while(tradeIterator.hasNext() && (trade = tradeIterator.next()).getTimestamp() <= endTime) {
					if(trade.getTimestamp() < startTime) continue;
					ohlcv.addTrade(trade);
				}
				
				lastTrade = trade;
			} else {
				Trade trade = null;
				Iterator<Trade> tIt = tradeData.iterator();
				
				do {
					trade = tIt.next();
					if(trade.getTimestamp() < startTime) continue;
					ohlcv.addTrade(trade);
				} while(trade != lastTrade);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ohlcv;
	}

}
