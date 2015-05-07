package io.hbar.fx.data;

import io.hbar.fx.data.series.FieldSeries;
import io.hbar.fx.data.series.OHLCVSeries;
import io.hbar.fx.ta.TaLib;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vertx.java.core.json.JsonObject;

public class DataManager {
	final static Logger logger = LogManager.getLogger(DataManager.class.getName());
	
	public List<Trade> tradeData;
	
	protected Map<String, Map<String, Map<Integer, Map<Integer, FieldSeries<?>>>>> cache = new HashMap<String, Map<String, Map<Integer, Map<Integer, FieldSeries<?>>>>>();
	

	public DataManager(String dataFile) {
		loadData(dataFile);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Enum<T>> FieldSeries<T> getSeries(String symbol, Class<T> indicator, int period, JsonObject options) {
		return (FieldSeries<T>) getSeries(symbol, indicator.getSimpleName(), period, options);
	}
	
	public FieldSeries<?> getSeries(String symbol, String indicator, int period, JsonObject options) {
		if(!cache.containsKey(symbol)) 
			cache.put(symbol, new HashMap<String, Map<Integer, Map<Integer, FieldSeries<?>>>>());
		
		if(!cache.get(symbol).containsKey(indicator))
			cache.get(symbol).put(indicator, new HashMap<Integer, Map<Integer, FieldSeries<?>>>());
		
		if(!cache.get(symbol).get(indicator).containsKey(period)) {
			cache.get(symbol).get(indicator).put(period, new HashMap<Integer, FieldSeries<?>>());
		}
		
		int optionsHash = options.toString().hashCode();
		
		if(!cache.get(symbol).get(indicator).get(period).containsKey(optionsHash)) {
			if(indicator.equals("OHLCV")) {
				cache.get(symbol).get(indicator).get(period).put(optionsHash, createOHLCVSeries(period));
			} else {
				try {
					cache.get(symbol).get(indicator).get(period).put(optionsHash, (FieldSeries<?>) TaLib.class.getMethod(indicator.toLowerCase(), OHLCVSeries.class, JsonObject.class).invoke(null, (OHLCVSeries) getSeries(symbol, "OHLCV", period, new JsonObject()), options));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
			}
		}
		
		return cache.get(symbol).get(indicator).get(period).get(optionsHash);
	}
	
	public byte[] getSerialized(String symbol, String indicator, int period, int startTime, int endTime, JsonObject options) {
		return getSeries(symbol, indicator, period, options).serialize(startTime, endTime);
	}
	
	public OHLCVSeries createOHLCVSeries(int period) {
		OHLCVSeries ohlcv = new OHLCVSeries(period);
		
		try {
			for(Trade trade : tradeData) {
				ohlcv.addTrade(trade);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ohlcv;
	}
	
	public int getMaxTime() {
		return tradeData.get(tradeData.size() - 1).getTimestamp();
	}
	
	protected void loadData(String file) {
		tradeData = new ArrayList<Trade>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	String[] tpv = line.split(",");
		    	int timestamp = Integer.parseInt(tpv[0]);
		    	double price = Double.parseDouble(tpv[1]);
		    	double volume = Double.parseDouble(tpv[2]);
		    	Trade trade = new Trade(timestamp, price, volume); 
		    	tradeData.add(trade);
		    }
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

}
