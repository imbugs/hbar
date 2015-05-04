package io.hbar.fx.ta;

import io.hbar.fx.data.series.FieldSeries;
import io.hbar.fx.data.series.OHLCVSeries;
import io.hbar.fx.data.series.types.BBands;
import io.hbar.fx.data.series.types.EMA;
import io.hbar.fx.data.series.types.HilbertDominantCyclePeriod;
import io.hbar.fx.data.series.types.HilbertDominantCyclePhase;
import io.hbar.fx.data.series.types.HilbertTrendMode;
import io.hbar.fx.data.series.types.HilbertTrendline;
import io.hbar.fx.data.series.types.LinearReg;
import io.hbar.fx.data.series.types.MACD;
import io.hbar.fx.data.series.types.OHLCV;
import io.hbar.fx.data.series.types.RSI;
import io.hbar.fx.data.series.types.SAR;
import io.hbar.fx.data.series.types.SMA;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.vertx.java.core.json.JsonObject;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

public class TaLib {
	public static Core core = new Core();
	
	public static FieldSeries<SMA> sma(OHLCVSeries ohlcv, JsonObject options) {
		return ma(SMA.class, ohlcv, options, OHLCV.Close);
	}
	public static FieldSeries<SMA> sma(OHLCVSeries ohlcv, JsonObject options, OHLCV field) {
		return ma(SMA.class, ohlcv, options, field);
	}
	
	
	public static FieldSeries<EMA> ema(OHLCVSeries ohlcv, JsonObject options) {
		return ma(EMA.class, ohlcv, options, OHLCV.Close);
	}
	public static FieldSeries<EMA> ema(OHLCVSeries ohlcv, JsonObject options, OHLCV field) {
		return ma(EMA.class, ohlcv, options, field);
	}
	
	
//	private static <T extends Enum<T>> FieldSeries<T> ma2(FieldSeries<T> series, JsonObject options, T field) {
//		return null;
//	}
	
	private static <T extends Enum<T>> FieldSeries<T> ma(Class<T> maType, OHLCVSeries ohlcv, JsonObject options, OHLCV field) {
		double inReal[] = ohlcv.getSeries(field);
		
		int period = options.getInteger("period");
		
        double outReal[] = new double[inReal.length];
        
        MInteger outBegIdx = new MInteger();
        MInteger outNbElement = new MInteger();
        
        Method maMethod;
        RetCode code = null;
        
		try {
			maMethod = Core.class.getMethod(maType.getSimpleName().toLowerCase(), int.class, int.class, double[].class, int.class, MInteger.class, MInteger.class, double[].class);
			code = (RetCode) maMethod.invoke(core, 0, inReal.length-1, inReal, period, outBegIdx, outNbElement, outReal);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
        
        if(code != RetCode.Success) return null;
        
        FieldSeries<T> s = createSeries(maType, options, outBegIdx.value, ohlcv.getTimestamps(), outReal);
        
        return s;
	}
	
	public static FieldSeries<RSI> rsi(OHLCVSeries ohlcv, JsonObject options) {
		return TaLib.rsi(ohlcv, options, OHLCV.Close);
	}
	public static FieldSeries<RSI> rsi(OHLCVSeries ohlcv, JsonObject options, OHLCV field) {
		double inReal[] = ohlcv.getSeries(field);
		
		int period = options.getInteger("period");
		
        double outReal[] = new double[inReal.length];
        
        MInteger outBegIdx = new MInteger();
        MInteger outNbElement = new MInteger();
        
        RetCode code = core.rsi(0, inReal.length-1, inReal, period, outBegIdx, outNbElement, outReal);
        
        if(code != RetCode.Success) return null;
        
        FieldSeries<RSI> s = createSeries(RSI.class, options, outBegIdx.value, ohlcv.getTimestamps(), outReal);
        
        return s;
	}
	
	
	
	public static FieldSeries<MACD> macd(OHLCVSeries ohlcv, JsonObject options) {
		return macd(ohlcv, options, OHLCV.Close);
	}
	public static FieldSeries<MACD> macd(OHLCVSeries ohlcv, JsonObject options, OHLCV field) {
		double inReal[] = ohlcv.getSeries(field);
		
		int slowPeriod = options.getInteger("slowPeriod");
		int fastPeriod = options.getInteger("fastPeriod");
		int signalPeriod = options.getInteger("signalPeriod");
		
		double outMACD[] = new double[inReal.length];
		double outSignal[] = new double[inReal.length];
		double outHistogram[] = new double[inReal.length];
		
		MInteger outBegIdx = new MInteger();
        MInteger outNbElement = new MInteger();
        
		RetCode code = core.macd(0, inReal.length-1, inReal, slowPeriod, fastPeriod, signalPeriod, outBegIdx, outNbElement, outMACD, outSignal, outHistogram);
		if( code != RetCode.Success ) return null;
		
		return createSeries(MACD.class, options, outBegIdx.value, ohlcv.getTimestamps(), outMACD, outSignal, outHistogram);
	}
	
	public static FieldSeries<BBands> bbands(OHLCVSeries ohlcv, JsonObject options) {
		return bbands(ohlcv, options, OHLCV.Close);
	}
	public static FieldSeries<BBands> bbands(OHLCVSeries ohlcv, JsonObject options, OHLCV field) {
		double inReal[] = ohlcv.getSeries(field);
		
		int inPeriod = options.getInteger("period");
		double optInNbDevUp = options.getInteger("deviationUp");
		double optInNbDevDn = options.getInteger("deviationDown");
		MAType inMAType =  MAType.valueOf(capFirst(options.getString("maType")));
		
		double upper[] = new double[inReal.length];
		double middle[] = new double[inReal.length];
		double lower[] = new double[inReal.length];
		
		MInteger outBegIdx = new MInteger();
        MInteger outNbElement = new MInteger();
        
		RetCode code = core.bbands(0, inReal.length-1, inReal, inPeriod, optInNbDevUp, optInNbDevDn, inMAType, outBegIdx, outNbElement, upper, lower, middle);
		if( code != RetCode.Success ) return null;
		
		return createSeries(BBands.class, options, outBegIdx.value, ohlcv.getTimestamps(), upper, lower, middle);
	}
	
	
	public static FieldSeries<SAR> sar(OHLCVSeries ohlcv, JsonObject options) {
		double inHigh[] = ohlcv.getSeries(OHLCV.High);
		double inLow[] = ohlcv.getSeries(OHLCV.Low);
		
		double optInAcceleration = options.getNumber("acceleration").doubleValue();
		double optInMaximum = options.getNumber("maximum").doubleValue();
		
		double outReal[] = new double[inHigh.length];
		
		MInteger outBegIdx = new MInteger();
        MInteger outNbElement = new MInteger();
        
		RetCode code = core.sar(0, inHigh.length-1, inHigh, inLow, optInAcceleration, optInMaximum, outBegIdx, outNbElement, outReal);
		if( code != RetCode.Success ) return null;
		
		return createSeries(SAR.class, options, outBegIdx.value, ohlcv.getTimestamps(), outReal);
	}
	
	
	
	public static FieldSeries<LinearReg> linearreg(OHLCVSeries ohlcv, JsonObject options) {
		return linearreg(ohlcv, options, OHLCV.Close);
	}
	public static FieldSeries<LinearReg> linearreg(OHLCVSeries ohlcv, JsonObject options, OHLCV field) {
		double inReal[] = ohlcv.getSeries(field);
		
		int inPeriod = options.getInteger("period");
		
		double outReal[] = new double[inReal.length];
		
		MInteger outBegIdx = new MInteger();
        MInteger outNbElement = new MInteger();
        
        RetCode code = core.linearReg(0, inReal.length-1, inReal, inPeriod, outBegIdx, outNbElement, outReal);
		if( code != RetCode.Success ) return null;
		
		return createSeries(LinearReg.class, options, outBegIdx.value, ohlcv.getTimestamps(), outReal);
	}
	
	public static FieldSeries<HilbertDominantCyclePeriod> hilbertdominantcycleperiod(OHLCVSeries ohlcv, JsonObject options) {
		return hilbertdominantcycleperiod(ohlcv, options, OHLCV.Close);
	}
	public static FieldSeries<HilbertDominantCyclePeriod> hilbertdominantcycleperiod(OHLCVSeries ohlcv, JsonObject options, OHLCV field) {
		double inReal[] = ohlcv.getSeries(field);
		
		double outReal[] = new double[inReal.length];
		
		MInteger outBegIdx = new MInteger();
        MInteger outNbElement = new MInteger();
        
        RetCode code = core.htDcPeriod(0, inReal.length-1, inReal, outBegIdx, outNbElement, outReal);
		if( code != RetCode.Success ) return null;
		
		return createSeries(HilbertDominantCyclePeriod.class, options, outBegIdx.value, ohlcv.getTimestamps(), outReal);
	}
	
	
	public static FieldSeries<HilbertDominantCyclePhase> hilbertdominantcyclephase(OHLCVSeries ohlcv, JsonObject options) {
		return hilbertdominantcyclephase(ohlcv, options, OHLCV.Close);
	}
	public static FieldSeries<HilbertDominantCyclePhase> hilbertdominantcyclephase(OHLCVSeries ohlcv, JsonObject options, OHLCV field) {
		double inReal[] = ohlcv.getSeries(field);
		
		double outReal[] = new double[inReal.length];
		
		MInteger outBegIdx = new MInteger();
        MInteger outNbElement = new MInteger();
        
        RetCode code = core.htDcPhase(0, inReal.length-1, inReal, outBegIdx, outNbElement, outReal);
		if( code != RetCode.Success ) return null;
		
		return createSeries(HilbertDominantCyclePhase.class, options, outBegIdx.value, ohlcv.getTimestamps(), outReal);
	}
	
	public static FieldSeries<HilbertTrendline> hilberttrendline(OHLCVSeries ohlcv, JsonObject options) {
		return hilberttrendline(ohlcv, options, OHLCV.Close);
	}
	public static FieldSeries<HilbertTrendline> hilberttrendline(OHLCVSeries ohlcv, JsonObject options, OHLCV field) {
		double inReal[] = ohlcv.getSeries(field);
		
		double outReal[] = new double[inReal.length];
		
		MInteger outBegIdx = new MInteger();
        MInteger outNbElement = new MInteger();
        
        RetCode code = core.htTrendline(0, inReal.length-1, inReal, outBegIdx, outNbElement, outReal);
		if( code != RetCode.Success ) return null;
		
		return createSeries(HilbertTrendline.class, options, outBegIdx.value, ohlcv.getTimestamps(), outReal);
	}
	
	public static FieldSeries<HilbertTrendMode> hilberttrendmode(OHLCVSeries ohlcv, JsonObject options) {
		return hilberttrendmode(ohlcv, options, OHLCV.Close);
	}
	public static FieldSeries<HilbertTrendMode> hilberttrendmode(OHLCVSeries ohlcv, JsonObject options, OHLCV field) {
		double inReal[] = ohlcv.getSeries(field);
		
		double outReal[] = new double[inReal.length];
		
		MInteger outBegIdx = new MInteger();
        MInteger outNbElement = new MInteger();
        
        RetCode code = core.htTrendMode(0, inReal.length-1, inReal, outBegIdx, outNbElement, outReal);
		if( code != RetCode.Success ) return null;
		
		return createSeries(HilbertTrendMode.class, options, outBegIdx.value, ohlcv.getTimestamps(), outReal);
	}
	
	
	private static <T extends Enum<T>> FieldSeries<T> createSeries(Class<T> fields, JsonObject options, int begIndex, int[] timestamps, double[]... values) {
		FieldSeries<T> series = new FieldSeries<T>(fields, options);
		
		for(int i = begIndex; i < timestamps.length; i++) {
        	try {
        		double[] row = new double[values.length];
        		
        		for(int j = 0; j < row.length; j++)
        			row[j] = values[j][i - begIndex];
        		
        		series.addRowArray(timestamps[i], row);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
		
		return series;
	}
	
	private static String capFirst(String s) {
		String lower = s.toLowerCase();
		return lower.substring(0, 1).toUpperCase() + lower.substring(1);
	}
}
