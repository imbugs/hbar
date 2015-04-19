package io.hbar.fx.data.series;

import io.hbar.fx.data.Trade;
import io.hbar.fx.data.series.types.OHLCV;

import java.util.Map;

public class OHLCVSeries extends FieldSeries<OHLCV> {

	private int period;

	public OHLCVSeries(int period) {
		super(OHLCV.class);
		this.period = period;
	}

	public void addTrade(Trade trade) throws Exception {
		int timestamp = ((int) (trade.getTimestamp() / period)) * period;
		double price = trade.getPrice();
		double volume = trade.getVolume();
		
		if(table.containsRow(timestamp)) {
			Map<OHLCV, Double> row = table.row(timestamp);

			double high = Math.max(row.get(OHLCV.High), price);
			double low = Math.min(row.get(OHLCV.Low), price);

			row.put(OHLCV.High, high);
			row.put(OHLCV.Low, low);
			row.put(OHLCV.Close, price);
			row.put(OHLCV.Volume, row.get(OHLCV.Volume) + volume);
		} else {
			addRow(timestamp, price, price, price, price, volume);
		}
	}

}
