package io.hbar.fx.data;

import io.hbar.fx.data.series.FieldSeries;
import io.hbar.fx.data.series.OHLCVSeries;
import io.hbar.fx.strategy.Strategy;
import io.hbar.fx.ta.TaLib;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vertx.java.core.json.JsonObject;

public class SimulationDataManager extends DataManager {

	final static Logger logger = LogManager.getLogger(SimulationDataManager.class.getName());

	protected int startTime;
	protected int endTime;

	protected Strategy strategy;

	protected Trade lastTrade = null;

	protected Iterator<Trade> tradeIterator;

	public SimulationDataManager(String dataFile, Strategy strategy, int startTime, int endTime) {
		super(dataFile);
		
		tradeIterator = tradeData.iterator();

		this.startTime = startTime;
		this.endTime = endTime;
		
		this.strategy = strategy;
		this.strategy.setDataManager(this);
	}

	public boolean tick(int iterations) {
		for (int i = 0; i < iterations; i++) {
			if (tradeIterator.hasNext()) {
				lastTrade = tradeIterator.next();
				
				for (OHLCVSeries ohlcv : getAllOHLCVSeries()) {
					try {
						if(ohlcv.isNewCandle(lastTrade.getTimestamp())) {
							strategy.candleEnd(ohlcv.getPeriod());
						}
						ohlcv.addTrade(lastTrade);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				for (String symbol : cache.keySet()) {
					Map<String, Map<Integer, Map<Integer, FieldSeries<?>>>> indicatorCache = cache.get(symbol);
					for (String indicator : indicatorCache.keySet()) {
						Map<Integer, Map<Integer, FieldSeries<?>>> periodCache = indicatorCache.get(indicator);
						for (Integer period : periodCache.keySet()) {
							Map<Integer, FieldSeries<?>> hashCache = periodCache.get(period);
							for (Integer hash : hashCache.keySet()) {
								FieldSeries<?> series = hashCache.get(hash);
								if (indicator.equals("OHLCV") || series == null) continue;
								try {
									cache.get(symbol).get(indicator).get(period).put(hash, (FieldSeries<?>) TaLib.class.getMethod(indicator.toLowerCase(), OHLCVSeries.class, JsonObject.class).invoke(null, (OHLCVSeries) getSeries(symbol, "OHLCV", period, new JsonObject()), series.getOptions()));
								} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
										| SecurityException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
				
				strategy.tick();
				
			} else {
				return false;
			}
		}
		
		return true;
	}

	private List<OHLCVSeries> getAllOHLCVSeries() {
		List<OHLCVSeries> seriesList = new ArrayList<OHLCVSeries>();

		for (String symbol : cache.keySet()) {
			Map<Integer, Map<Integer, FieldSeries<?>>> periodCache = cache.get(symbol).get("OHLCV");
			for (Integer period : periodCache.keySet()) {
				Map<Integer, FieldSeries<?>> hashCache = periodCache.get(period);
				for (Integer hash : hashCache.keySet()) {
					seriesList.add((OHLCVSeries) hashCache.get(hash));
				}
			}
		}

		return seriesList;
	}

	@Override
	public FieldSeries<?> getSeries(String symbol, String indicator, int period, JsonObject options) {
		if (indicator.indexOf("Strategy") >= 0) {
			return strategy.getSeries();
		}

		return super.getSeries(symbol, indicator, period, options);
	}

	@Override
	public OHLCVSeries createOHLCVSeries(int period) {
		OHLCVSeries ohlcv = new OHLCVSeries(period);

		try {
			if (lastTrade == null) {
				Trade trade = null;

				while (tradeIterator.hasNext() && (trade = tradeIterator.next()).getTimestamp() <= endTime) {
					if (trade.getTimestamp() < startTime)
						continue;
					ohlcv.addTrade(trade);
				}

				lastTrade = trade;
			} else {
				Trade trade = null;
				Iterator<Trade> tIt = tradeData.iterator();

				do {
					trade = tIt.next();
					if (trade.getTimestamp() < startTime)
						continue;
					ohlcv.addTrade(trade);
				} while (trade != lastTrade);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ohlcv;
	}
	
	@Override
	public int getMaxTime() {
		if(lastTrade == null) {
			return this.endTime;
		} else {
			return lastTrade.getTimestamp();
		}
	}

}
