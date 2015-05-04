/* TA-LIB Copyright (c) 1999-2007, Mario Fortier
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 *
 * - Neither name of author nor the names of its contributors
 *   may be used to endorse or promote products derived from this
 *   software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/* List of contributors:
 *
 *  Initial  Name/description
 *  -------------------------------------------------------------------
 *  MF       Mario Fortier
 *  BT       Barry Tsung
 *
 * Change history:
 *
 *  MMDDYY BY     Description
 *  -------------------------------------------------------------------
 *  121005 MF     First Version
 *  022206 BT     1. initialization of candleSettings
 *                2. add SetCompatibility and GetCompatibility
 *                3. add SetUnstablePeriod, GetUnstablePeriod
 */

package com.tictactec.ta.lib;

public class Core {
   
   private int[] unstablePeriod;
   
   private CandleSetting[] candleSettings;
   
   private Compatibility compatibility;
   
   /** Creates a new instance of Core */
   public Core() {
      unstablePeriod = new int[com.tictactec.ta.lib.FuncUnstId.All
         .ordinal()];
      compatibility = Compatibility.Default;
      candleSettings = new CandleSetting[com.tictactec.ta.lib.CandleSettingType.AllCandleSettings
         .ordinal()];
      for(int i=0;i<candleSettings.length;i++){
         candleSettings[i] = new CandleSetting(TA_CandleDefaultSettings[i]);
      }
   }
   
   public RetCode SetCandleSettings(CandleSettingType settingType,
      RangeType rangeType, int avgPeriod, double factor) {
      if (settingType.ordinal() >= CandleSettingType.AllCandleSettings
         .ordinal())
         return RetCode.BadParam;
      candleSettings[settingType.ordinal()].settingType = settingType;
      candleSettings[settingType.ordinal()].rangeType = rangeType;
      candleSettings[settingType.ordinal()].avgPeriod = avgPeriod;
      candleSettings[settingType.ordinal()].factor = factor;
      return RetCode.Success;
   }
   
   final private CandleSetting TA_CandleDefaultSettings[] = {
      /*
      * real body is long when it's longer than the average of the 10
      * previous candles' real body
      */
      new CandleSetting(CandleSettingType.BodyLong,
         RangeType.RealBody, 10, 1.0),
      /*
      * real body is very long when it's longer than 3 times the average
      * of the 10 previous candles' real body
      */
      new CandleSetting(CandleSettingType.BodyVeryLong,
         RangeType.RealBody, 10, 3.0),
      /*
      * real body is short when it's shorter than the average of the 10
      * previous candles' real bodies
      */
      new CandleSetting(CandleSettingType.BodyShort,
         RangeType.RealBody, 10, 1.0),
      /*
      * real body is like doji's body when it's shorter than 10% the
      * average of the 10 previous candles' high-low range
      */
      new CandleSetting(CandleSettingType.BodyDoji,
         RangeType.HighLow, 10, 0.1),
      /* shadow is long when it's longer than the real body */
      new CandleSetting(CandleSettingType.ShadowLong,
         RangeType.RealBody, 0, 1.0),
      /* shadow is very long when it's longer than 2 times the real body */
      new CandleSetting(CandleSettingType.ShadowVeryLong,
         RangeType.RealBody, 0, 2.0),
      /*
      * shadow is short when it's shorter than half the average of the 10
      * previous candles' sum of shadows
      */
      new CandleSetting(CandleSettingType.ShadowShort,
         RangeType.Shadows, 10, 1.0),
      /*
      * shadow is very short when it's shorter than 10% the average of
      * the 10 previous candles' high-low range
      */
      new CandleSetting(CandleSettingType.ShadowVeryShort,
         RangeType.HighLow, 10, 0.1),
      /* when measuring distance between parts of candles or width of gaps */
      /*
      * "near" means "<= 20% of the average of the 5 previous candles'
      * high-low range"
      */
      new CandleSetting(CandleSettingType.Near,
         RangeType.HighLow, 5, 0.2),
      /* when measuring distance between parts of candles or width of gaps */
      /*
      * "far" means ">= 60% of the average of the 5 previous candles'
      * high-low range"
      */
      new CandleSetting(CandleSettingType.Far,
         RangeType.HighLow, 5, 0.6),
      /* when measuring distance between parts of candles or width of gaps */
      /*
      * "equal" means "<= 5% of the average of the 5 previous candles'
      * high-low range"
      */
      new CandleSetting(CandleSettingType.Equal,
         RangeType.HighLow, 5, 0.05) };
   
   public RetCode RestoreCandleDefaultSettings(
      CandleSettingType settingType) {
      int i;
      if (settingType.ordinal() > CandleSettingType.AllCandleSettings
         .ordinal())
         return RetCode.BadParam;
      if (settingType == CandleSettingType.AllCandleSettings) {
         for (i = 0; i < CandleSettingType.AllCandleSettings.ordinal(); ++i) {
            candleSettings[i].CopyFrom(TA_CandleDefaultSettings[i]);
         }
      } else {
         candleSettings[settingType.ordinal()]
            .CopyFrom(TA_CandleDefaultSettings[settingType.ordinal()]);
      }
      return RetCode.Success;
   }
   
   public RetCode SetUnstablePeriod(FuncUnstId id, int period)
   {
      if (id.ordinal() >= FuncUnstId.All
         .ordinal())
         return RetCode.BadParam;
      unstablePeriod[id.ordinal()] = period;
      return RetCode.Success;
   }
   
   public int GetUnstablePeriod(FuncUnstId id)
   {
      return unstablePeriod[id.ordinal()];
   }
   
   public void SetCompatibility(Compatibility compatibility)
   {
      this.compatibility = compatibility;
   }
   
   public Compatibility getCompatibility()
   {
      return compatibility;
   }
   
   /**** START GENCODE SECTION 1 - DO NOT DELETE THIS LINE ****/
   public int acosLookback( )
   {
      return 0;
   }
   public RetCode acos( int startIdx,
      int endIdx,
      double inReal[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx;
      int i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      for( i=startIdx, outIdx=0; i <= endIdx; i++, outIdx++ )
      {
         outReal[outIdx] = Math.acos (inReal[i]);
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int adLookback( )
   {
      return 0;
   }
   public RetCode ad( int startIdx,
      int endIdx,
      double inHigh[],
      double inLow[],
      double inClose[],
      double inVolume[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int nbBar, currentBar, outIdx;
      double high, low, close, tmp;
      double ad;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      nbBar = endIdx-startIdx+1;
      outNBElement.value = nbBar;
      outBegIdx.value = startIdx;
      currentBar = startIdx;
      outIdx = 0;
      ad = 0.0;
      while( nbBar != 0 )
      {
         high = inHigh[currentBar];
         low = inLow[currentBar];
         tmp = high-low;
         close = inClose[currentBar];
         if( tmp > 0.0 )
            ad += (((close-low)-(high-close))/tmp)*((double)inVolume[currentBar]);
         outReal[outIdx++] = ad;
         currentBar++;
         nbBar--;
      }
      return RetCode.Success ;
   }
   
   /* Generated */
   public int addLookback( )
   {
      return 0;
   }
   public RetCode add( int startIdx,
      int endIdx,
      double inReal0[],
      double inReal1[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx;
      int i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      for( i=startIdx, outIdx=0; i <= endIdx; i++, outIdx++ )
      {
         outReal[outIdx] = inReal0[i]+inReal1[i];
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int adOscLookback( int optInFastPeriod,
      int optInSlowPeriod )
   {
      int slowestPeriod;
      if( (int)optInFastPeriod == ( Integer.MIN_VALUE ) )
         optInFastPeriod = 3;
      else if( ((int)optInFastPeriod < 2) || ((int)optInFastPeriod > 100000) )
         return -1;
      if( (int)optInSlowPeriod == ( Integer.MIN_VALUE ) )
         optInSlowPeriod = 10;
      else if( ((int)optInSlowPeriod < 2) || ((int)optInSlowPeriod > 100000) )
         return -1;
      if( optInFastPeriod < optInSlowPeriod )
         slowestPeriod = optInSlowPeriod;
      else
         slowestPeriod = optInFastPeriod;
      return emaLookback ( slowestPeriod );
   }
   public RetCode adOsc( int startIdx,
      int endIdx,
      double inHigh[],
      double inLow[],
      double inClose[],
      double inVolume[],
      int optInFastPeriod,
      int optInSlowPeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int today, outIdx, lookbackTotal;
      int slowestPeriod;
      double high, low, close, tmp;
      double slowEMA, slowk, one_minus_slowk;
      double fastEMA, fastk, one_minus_fastk;
      double ad;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInFastPeriod == ( Integer.MIN_VALUE ) )
         optInFastPeriod = 3;
      else if( ((int)optInFastPeriod < 2) || ((int)optInFastPeriod > 100000) )
         return RetCode.BadParam ;
      if( (int)optInSlowPeriod == ( Integer.MIN_VALUE ) )
         optInSlowPeriod = 10;
      else if( ((int)optInSlowPeriod < 2) || ((int)optInSlowPeriod > 100000) )
         return RetCode.BadParam ;
      if( optInFastPeriod < optInSlowPeriod )
         slowestPeriod = optInSlowPeriod;
      else
         slowestPeriod = optInFastPeriod;
      lookbackTotal = emaLookback ( slowestPeriod );
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outBegIdx.value = startIdx;
      today = startIdx-lookbackTotal;
      ad = 0.0;
      fastk = ((double)2.0 / ((double)(optInFastPeriod + 1))) ;
      one_minus_fastk = 1.0 - fastk;
      slowk = ((double)2.0 / ((double)(optInSlowPeriod + 1))) ;
      one_minus_slowk = 1.0 - slowk;
      { high = inHigh[today]; low = inLow[today]; tmp = high-low; close = inClose[today]; if( tmp > 0.0 ) ad += (((close-low)-(high-close))/tmp)*((double)inVolume[today]); today++; } ;
      fastEMA = ad;
      slowEMA = ad;
      while( today < startIdx )
      {
         { high = inHigh[today]; low = inLow[today]; tmp = high-low; close = inClose[today]; if( tmp > 0.0 ) ad += (((close-low)-(high-close))/tmp)*((double)inVolume[today]); today++; } ;
         fastEMA = (fastk*ad)+(one_minus_fastk*fastEMA);
         slowEMA = (slowk*ad)+(one_minus_slowk*slowEMA);
      }
      outIdx = 0;
      while( today <= endIdx )
      {
         { high = inHigh[today]; low = inLow[today]; tmp = high-low; close = inClose[today]; if( tmp > 0.0 ) ad += (((close-low)-(high-close))/tmp)*((double)inVolume[today]); today++; } ;
         fastEMA = (fastk*ad)+(one_minus_fastk*fastEMA);
         slowEMA = (slowk*ad)+(one_minus_slowk*slowEMA);
         outReal[outIdx++] = fastEMA - slowEMA;
      }
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int adxLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      return (2 * optInTimePeriod) + (this.unstablePeriod[FuncUnstId.Adx.ordinal()]) - 1;
   }
   public RetCode adx( int startIdx,
      int endIdx,
      double inHigh[],
      double inLow[],
      double inClose[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int today, lookbackTotal, outIdx;
      double prevHigh, prevLow, prevClose;
      double prevMinusDM, prevPlusDM, prevTR;
      double tempReal, tempReal2, diffP, diffM;
      double minusDI, plusDI, sumDX, prevADX;
      int i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      lookbackTotal = (2*optInTimePeriod) + (this.unstablePeriod[FuncUnstId.Adx.ordinal()]) - 1;
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      outBegIdx.value = today = startIdx;
      prevMinusDM = 0.0;
      prevPlusDM = 0.0;
      prevTR = 0.0;
      today = startIdx - lookbackTotal;
      prevHigh = inHigh[today];
      prevLow = inLow[today];
      prevClose = inClose[today];
      i = optInTimePeriod-1;
      while( i-- > 0 )
      {
         today++;
         tempReal = inHigh[today];
         diffP = tempReal-prevHigh;
         prevHigh = tempReal;
         tempReal = inLow[today];
         diffM = prevLow-tempReal;
         prevLow = tempReal;
         if( (diffM > 0) && (diffP < diffM) )
         {
            prevMinusDM += diffM;
         }
         else if( (diffP > 0) && (diffP > diffM) )
         {
            prevPlusDM += diffP;
         }
         { tempReal = prevHigh-prevLow; tempReal2 = Math.abs (prevHigh-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; tempReal2 = Math.abs (prevLow-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; } ;
         prevTR += tempReal;
         prevClose = inClose[today];
      }
      sumDX = 0.0;
      i = optInTimePeriod;
      while( i-- > 0 )
      {
         today++;
         tempReal = inHigh[today];
         diffP = tempReal-prevHigh;
         prevHigh = tempReal;
         tempReal = inLow[today];
         diffM = prevLow-tempReal;
         prevLow = tempReal;
         prevMinusDM -= prevMinusDM/optInTimePeriod;
         prevPlusDM -= prevPlusDM/optInTimePeriod;
         if( (diffM > 0) && (diffP < diffM) )
         {
            prevMinusDM += diffM;
         }
         else if( (diffP > 0) && (diffP > diffM) )
         {
            prevPlusDM += diffP;
         }
         { tempReal = prevHigh-prevLow; tempReal2 = Math.abs (prevHigh-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; tempReal2 = Math.abs (prevLow-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; } ;
         prevTR = prevTR - (prevTR/optInTimePeriod) + tempReal;
         prevClose = inClose[today];
         if( ! (((-0.00000001)<prevTR)&&(prevTR<0.00000001)) )
         {
            minusDI = (100.0*(prevMinusDM/prevTR)) ;
            plusDI = (100.0*(prevPlusDM/prevTR)) ;
            tempReal = minusDI+plusDI;
            if( ! (((-0.00000001)<tempReal)&&(tempReal<0.00000001)) )
               sumDX += (100.0 * ( Math.abs (minusDI-plusDI)/tempReal)) ;
         }
      }
      prevADX = (sumDX / optInTimePeriod) ;
      i = (this.unstablePeriod[FuncUnstId.Adx.ordinal()]) ;
      while( i-- > 0 )
      {
         today++;
         tempReal = inHigh[today];
         diffP = tempReal-prevHigh;
         prevHigh = tempReal;
         tempReal = inLow[today];
         diffM = prevLow-tempReal;
         prevLow = tempReal;
         prevMinusDM -= prevMinusDM/optInTimePeriod;
         prevPlusDM -= prevPlusDM/optInTimePeriod;
         if( (diffM > 0) && (diffP < diffM) )
         {
            prevMinusDM += diffM;
         }
         else if( (diffP > 0) && (diffP > diffM) )
         {
            prevPlusDM += diffP;
         }
         { tempReal = prevHigh-prevLow; tempReal2 = Math.abs (prevHigh-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; tempReal2 = Math.abs (prevLow-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; } ;
         prevTR = prevTR - (prevTR/optInTimePeriod) + tempReal;
         prevClose = inClose[today];
         if( ! (((-0.00000001)<prevTR)&&(prevTR<0.00000001)) )
         {
            minusDI = (100.0*(prevMinusDM/prevTR)) ;
            plusDI = (100.0*(prevPlusDM/prevTR)) ;
            tempReal = minusDI+plusDI;
            if( ! (((-0.00000001)<tempReal)&&(tempReal<0.00000001)) )
            {
               tempReal = (100.0*( Math.abs (minusDI-plusDI)/tempReal)) ;
               prevADX = (((prevADX*(optInTimePeriod-1))+tempReal)/optInTimePeriod) ;
            }
         }
      }
      outReal[0] = prevADX;
      outIdx = 1;
      while( today < endIdx )
      {
         today++;
         tempReal = inHigh[today];
         diffP = tempReal-prevHigh;
         prevHigh = tempReal;
         tempReal = inLow[today];
         diffM = prevLow-tempReal;
         prevLow = tempReal;
         prevMinusDM -= prevMinusDM/optInTimePeriod;
         prevPlusDM -= prevPlusDM/optInTimePeriod;
         if( (diffM > 0) && (diffP < diffM) )
         {
            prevMinusDM += diffM;
         }
         else if( (diffP > 0) && (diffP > diffM) )
         {
            prevPlusDM += diffP;
         }
         { tempReal = prevHigh-prevLow; tempReal2 = Math.abs (prevHigh-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; tempReal2 = Math.abs (prevLow-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; } ;
         prevTR = prevTR - (prevTR/optInTimePeriod) + tempReal;
         prevClose = inClose[today];
         if( ! (((-0.00000001)<prevTR)&&(prevTR<0.00000001)) )
         {
            minusDI = (100.0*(prevMinusDM/prevTR)) ;
            plusDI = (100.0*(prevPlusDM/prevTR)) ;
            tempReal = minusDI+plusDI;
            if( ! (((-0.00000001)<tempReal)&&(tempReal<0.00000001)) )
            {
               tempReal = (100.0*( Math.abs (minusDI-plusDI)/tempReal)) ;
               prevADX = (((prevADX*(optInTimePeriod-1))+tempReal)/optInTimePeriod) ;
            }
         }
         outReal[outIdx++] = prevADX;
      }
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int adxrLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      if( optInTimePeriod > 1 )
         return optInTimePeriod + adxLookback ( optInTimePeriod) - 1;
      else
         return 3;
   }
   public RetCode adxr( int startIdx,
      int endIdx,
      double inHigh[],
      double inLow[],
      double inClose[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      double []adx ;
      int adxrLookback, i, j, outIdx, nbElement;
      RetCode retCode;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      adxrLookback = adxrLookback ( optInTimePeriod );
      if( startIdx < adxrLookback )
         startIdx = adxrLookback;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      adx = new double[endIdx-startIdx+optInTimePeriod] ;
      retCode = adx ( startIdx-(optInTimePeriod-1), endIdx,
         inHigh, inLow, inClose,
         optInTimePeriod, outBegIdx, outNBElement, adx );
      if( retCode != RetCode.Success )
      {
         return retCode;
      }
      i = optInTimePeriod-1;
      j = 0;
      outIdx = 0;
      nbElement = endIdx-startIdx+2;
      while( --nbElement != 0 )
         outReal[outIdx++] = ((adx[i++]+adx[j++])/2.0) ;
      outBegIdx.value = startIdx;
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int apoLookback( int optInFastPeriod,
      int optInSlowPeriod,
      MAType optInMAType )
   {
      if( (int)optInFastPeriod == ( Integer.MIN_VALUE ) )
         optInFastPeriod = 12;
      else if( ((int)optInFastPeriod < 2) || ((int)optInFastPeriod > 100000) )
         return -1;
      if( (int)optInSlowPeriod == ( Integer.MIN_VALUE ) )
         optInSlowPeriod = 26;
      else if( ((int)optInSlowPeriod < 2) || ((int)optInSlowPeriod > 100000) )
         return -1;
      return movingAverageLookback ( (((optInSlowPeriod) > (optInFastPeriod)) ? (optInSlowPeriod) : (optInFastPeriod)) , optInMAType );
   }
   public RetCode apo( int startIdx,
      int endIdx,
      double inReal[],
      int optInFastPeriod,
      int optInSlowPeriod,
      MAType optInMAType,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      double []tempBuffer ;
      RetCode retCode;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInFastPeriod == ( Integer.MIN_VALUE ) )
         optInFastPeriod = 12;
      else if( ((int)optInFastPeriod < 2) || ((int)optInFastPeriod > 100000) )
         return RetCode.BadParam ;
      if( (int)optInSlowPeriod == ( Integer.MIN_VALUE ) )
         optInSlowPeriod = 26;
      else if( ((int)optInSlowPeriod < 2) || ((int)optInSlowPeriod > 100000) )
         return RetCode.BadParam ;
      tempBuffer = new double[(endIdx-startIdx+1)] ;
      retCode = TA_INT_PO ( startIdx, endIdx,
         inReal,
         optInFastPeriod,
         optInSlowPeriod,
         optInMAType,
         outBegIdx,
         outNBElement,
         outReal,
         tempBuffer,
         0 );
      return retCode;
   }
   RetCode TA_INT_PO( int startIdx,
      int endIdx,
      double inReal[],
      int optInFastPeriod,
      int optInSlowPeriod,
      MAType optInMethod_2,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[],
      double tempBuffer[],
      int doPercentageOutput )
   {
      RetCode retCode;
      double tempReal;
      int tempInteger;
      MInteger outBegIdx1 = new MInteger() ;
      MInteger outNbElement1 = new MInteger() ;
      MInteger outBegIdx2 = new MInteger() ;
      MInteger outNbElement2 = new MInteger() ;
      int i, j;
      if( optInSlowPeriod < optInFastPeriod )
      {
         tempInteger = optInSlowPeriod;
         optInSlowPeriod = optInFastPeriod;
         optInFastPeriod = tempInteger;
      }
      retCode = movingAverage ( startIdx, endIdx,
         inReal,
         optInFastPeriod,
         optInMethod_2,
         outBegIdx2 , outNbElement2 ,
         tempBuffer );
      if( retCode == RetCode.Success )
      {
         retCode = movingAverage ( startIdx, endIdx,
            inReal,
            optInSlowPeriod,
            optInMethod_2,
            outBegIdx1 , outNbElement1 ,
            outReal );
         if( retCode == RetCode.Success )
         {
            tempInteger = outBegIdx1.value - outBegIdx2.value ;
            if( doPercentageOutput != 0 )
            {
               for( i=0,j=tempInteger; i < outNbElement1.value ; i++, j++ )
               {
                  tempReal = outReal[i];
                  if( ! (((-0.00000001)<tempReal)&&(tempReal<0.00000001)) )
                     outReal[i] = ((tempBuffer[j]-tempReal)/tempReal)*100.0;
                  else
                     outReal[i] = 0.0;
               }
            }
            else
            {
               for( i=0,j=tempInteger; i < outNbElement1.value ; i++, j++ )
                  outReal[i] = tempBuffer[j]-outReal[i];
            }
            outBegIdx.value = outBegIdx1.value ;
            outNBElement.value = outNbElement1.value ;
         }
      }
      if( retCode != RetCode.Success )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
      }
      return retCode;
   }
   
   /* Generated */
   public int aroonLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      return optInTimePeriod;
   }
   public RetCode aroon( int startIdx,
      int endIdx,
      double inHigh[],
      double inLow[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outAroonDown[],
      double outAroonUp[] )
   {
      double lowest, highest, tmp, factor;
      int outIdx;
      int trailingIdx, lowestIdx, highestIdx, today, i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      if( startIdx < optInTimePeriod )
         startIdx = optInTimePeriod;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      today = startIdx;
      trailingIdx = startIdx-optInTimePeriod;
      lowestIdx = -1;
      highestIdx = -1;
      lowest = 0.0;
      highest = 0.0;
      factor = (double)100.0/(double)optInTimePeriod;
      while( today <= endIdx )
      {
         tmp = inLow[today];
         if( lowestIdx < trailingIdx )
         {
            lowestIdx = trailingIdx;
            lowest = inLow[lowestIdx];
            i = lowestIdx;
            while( ++i<=today )
            {
               tmp = inLow[i];
               if( tmp <= lowest )
               {
                  lowestIdx = i;
                  lowest = tmp;
               }
            }
         }
         else if( tmp <= lowest )
         {
            lowestIdx = today;
            lowest = tmp;
         }
         tmp = inHigh[today];
         if( highestIdx < trailingIdx )
         {
            highestIdx = trailingIdx;
            highest = inHigh[highestIdx];
            i = highestIdx;
            while( ++i<=today )
            {
               tmp = inHigh[i];
               if( tmp >= highest )
               {
                  highestIdx = i;
                  highest = tmp;
               }
            }
         }
         else if( tmp >= highest )
         {
            highestIdx = today;
            highest = tmp;
         }
         outAroonUp[outIdx] = factor*(optInTimePeriod-(today-highestIdx));
         outAroonDown[outIdx] = factor*(optInTimePeriod-(today-lowestIdx));
         outIdx++;
         trailingIdx++;
         today++;
      }
      outBegIdx.value = startIdx;
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int aroonOscLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      return optInTimePeriod;
   }
   public RetCode aroonOsc( int startIdx,
      int endIdx,
      double inHigh[],
      double inLow[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      double lowest, highest, tmp, factor, aroon;
      int outIdx;
      int trailingIdx, lowestIdx, highestIdx, today, i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      if( startIdx < optInTimePeriod )
         startIdx = optInTimePeriod;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      today = startIdx;
      trailingIdx = startIdx-optInTimePeriod;
      lowestIdx = -1;
      highestIdx = -1;
      lowest = 0.0;
      highest = 0.0;
      factor = (double)100.0/(double)optInTimePeriod;
      while( today <= endIdx )
      {
         tmp = inLow[today];
         if( lowestIdx < trailingIdx )
         {
            lowestIdx = trailingIdx;
            lowest = inLow[lowestIdx];
            i = lowestIdx;
            while( ++i<=today )
            {
               tmp = inLow[i];
               if( tmp <= lowest )
               {
                  lowestIdx = i;
                  lowest = tmp;
               }
            }
         }
         else if( tmp <= lowest )
         {
            lowestIdx = today;
            lowest = tmp;
         }
         tmp = inHigh[today];
         if( highestIdx < trailingIdx )
         {
            highestIdx = trailingIdx;
            highest = inHigh[highestIdx];
            i = highestIdx;
            while( ++i<=today )
            {
               tmp = inHigh[i];
               if( tmp >= highest )
               {
                  highestIdx = i;
                  highest = tmp;
               }
            }
         }
         else if( tmp >= highest )
         {
            highestIdx = today;
            highest = tmp;
         }
         aroon = factor*(highestIdx-lowestIdx);
         outReal[outIdx] = aroon;
         outIdx++;
         trailingIdx++;
         today++;
      }
      outBegIdx.value = startIdx;
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int asinLookback( )
   {
      return 0;
   }
   public RetCode asin( int startIdx,
      int endIdx,
      double inReal[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx;
      int i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      for( i=startIdx, outIdx=0; i <= endIdx; i++, outIdx++ )
      {
         outReal[outIdx] = Math.asin (inReal[i]);
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int atanLookback( )
   {
      return 0;
   }
   public RetCode atan( int startIdx,
      int endIdx,
      double inReal[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx;
      int i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      for( i=startIdx, outIdx=0; i <= endIdx; i++, outIdx++ )
      {
         outReal[outIdx] = Math.atan (inReal[i]);
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int atrLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return -1;
      return optInTimePeriod + (this.unstablePeriod[FuncUnstId.Atr.ordinal()]) ;
   }
   public RetCode atr( int startIdx,
      int endIdx,
      double inHigh[],
      double inLow[],
      double inClose[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      RetCode retCode;
      int outIdx, today, lookbackTotal;
      int nbATR;
      MInteger outBegIdx1 = new MInteger() ;
      MInteger outNbElement1 = new MInteger() ;
      double prevATR;
      double []tempBuffer ;
      double []prevATRTemp = new double[1] ;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      outBegIdx.value = 0 ;
      outNBElement.value = 0 ;
      lookbackTotal = atrLookback ( optInTimePeriod );
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
         return RetCode.Success ;
      if( optInTimePeriod <= 1 )
      {
         return trueRange ( startIdx, endIdx,
            inHigh, inLow, inClose,
            outBegIdx, outNBElement, outReal );
      }
      tempBuffer = new double[lookbackTotal+(endIdx-startIdx)+1] ;
      retCode = trueRange ( (startIdx-lookbackTotal+1), endIdx,
         inHigh, inLow, inClose,
         outBegIdx1 , outNbElement1 ,
         tempBuffer );
      if( retCode != RetCode.Success )
      {
         return retCode;
      }
      retCode = TA_INT_SMA ( optInTimePeriod-1,
         optInTimePeriod-1,
         tempBuffer, optInTimePeriod,
         outBegIdx1 , outNbElement1 ,
         prevATRTemp );
      if( retCode != RetCode.Success )
      {
         return retCode;
      }
      prevATR = prevATRTemp[0];
      today = optInTimePeriod;
      outIdx = (this.unstablePeriod[FuncUnstId.Atr.ordinal()]) ;
      while( outIdx != 0 )
      {
         prevATR *= optInTimePeriod - 1;
         prevATR += tempBuffer[today++];
         prevATR /= optInTimePeriod;
         outIdx--;
      }
      outIdx = 1;
      outReal[0] = prevATR;
      nbATR = (endIdx - startIdx)+1;
      while( --nbATR != 0 )
      {
         prevATR *= optInTimePeriod - 1;
         prevATR += tempBuffer[today++];
         prevATR /= optInTimePeriod;
         outReal[outIdx++] = prevATR;
      }
      outBegIdx.value = startIdx;
      outNBElement.value = outIdx;
      return retCode;
   }
   
   /* Generated */
   public int avgPriceLookback( )
   {
      return 0;
   }
   public RetCode avgPrice( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx, i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      outIdx = 0;
      for( i=startIdx; i <= endIdx; i++ )
      {
         outReal[outIdx++] = ( inHigh [i] +
            inLow [i] +
            inClose[i] +
            inOpen [i]) / 4;
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int bbandsLookback( int optInTimePeriod,
      double optInNbDevUp,
      double optInNbDevDn,
      MAType optInMAType )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 5;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      if( optInNbDevUp == (-4e+37) )
         optInNbDevUp = 2.000000e+0;
      else if( (optInNbDevUp < -3.000000e+37) || (optInNbDevUp > 3.000000e+37) )
         return -1;
      if( optInNbDevDn == (-4e+37) )
         optInNbDevDn = 2.000000e+0;
      else if( (optInNbDevDn < -3.000000e+37) || (optInNbDevDn > 3.000000e+37) )
         return -1;
      return movingAverageLookback ( optInTimePeriod, optInMAType );
   }
   public RetCode bbands( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      double optInNbDevUp,
      double optInNbDevDn,
      MAType optInMAType,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outRealUpperBand[],
      double outRealMiddleBand[],
      double outRealLowerBand[] )
   {
      RetCode retCode;
      int i;
      double tempReal, tempReal2;
      double []tempBuffer1 ;
      double []tempBuffer2 ;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 5;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      if( optInNbDevUp == (-4e+37) )
         optInNbDevUp = 2.000000e+0;
      else if( (optInNbDevUp < -3.000000e+37) || (optInNbDevUp > 3.000000e+37) )
         return RetCode.BadParam ;
      if( optInNbDevDn == (-4e+37) )
         optInNbDevDn = 2.000000e+0;
      else if( (optInNbDevDn < -3.000000e+37) || (optInNbDevDn > 3.000000e+37) )
         return RetCode.BadParam ;
      if( inReal == outRealUpperBand )
      {
         tempBuffer1 = outRealMiddleBand;
         tempBuffer2 = outRealLowerBand;
      }
      else if( inReal == outRealLowerBand )
      {
         tempBuffer1 = outRealMiddleBand;
         tempBuffer2 = outRealUpperBand;
      }
      else if( inReal == outRealMiddleBand )
      {
         tempBuffer1 = outRealLowerBand;
         tempBuffer2 = outRealUpperBand;
      }
      else
      {
         tempBuffer1 = outRealMiddleBand;
         tempBuffer2 = outRealUpperBand;
      }
      if( (tempBuffer1 == inReal) || (tempBuffer2 == inReal) )
         return RetCode.BadParam ;
      retCode = movingAverage ( startIdx, endIdx, inReal,
         optInTimePeriod, optInMAType,
         outBegIdx, outNBElement, tempBuffer1 );
      if( (retCode != RetCode.Success ) || ((int) outNBElement.value == 0) )
      {
         outNBElement.value = 0 ;
         return retCode;
      }
      if( optInMAType == MAType.Sma )
      {
         TA_INT_stddev_using_precalc_ma ( inReal, tempBuffer1,
            (int) outBegIdx.value , (int) outNBElement.value ,
            optInTimePeriod, tempBuffer2 );
      }
      else
      {
         retCode = stdDev ( (int) outBegIdx.value , endIdx, inReal,
            optInTimePeriod, 1.0,
            outBegIdx, outNBElement, tempBuffer2 );
         if( retCode != RetCode.Success )
         {
            outNBElement.value = 0 ;
            return retCode;
         }
      }
      if( tempBuffer1 != outRealMiddleBand )
      {
         System.arraycopy(tempBuffer1,0,outRealMiddleBand,0,outNBElement.value) ;
      }
      if( optInNbDevUp == optInNbDevDn )
      {
         if( optInNbDevUp == 1.0 )
         {
            for( i=0; i < (int) outNBElement.value ; i++ )
            {
               tempReal = tempBuffer2[i];
               tempReal2 = outRealMiddleBand[i];
               outRealUpperBand[i] = tempReal2 + tempReal;
               outRealLowerBand[i] = tempReal2 - tempReal;
            }
         }
         else
         {
            for( i=0; i < (int) outNBElement.value ; i++ )
            {
               tempReal = tempBuffer2[i] * optInNbDevUp;
               tempReal2 = outRealMiddleBand[i];
               outRealUpperBand[i] = tempReal2 + tempReal;
               outRealLowerBand[i] = tempReal2 - tempReal;
            }
         }
      }
      else if( optInNbDevUp == 1.0 )
      {
         for( i=0; i < (int) outNBElement.value ; i++ )
         {
            tempReal = tempBuffer2[i];
            tempReal2 = outRealMiddleBand[i];
            outRealUpperBand[i] = tempReal2 + tempReal;
            outRealLowerBand[i] = tempReal2 - (tempReal * optInNbDevDn);
         }
      }
      else if( optInNbDevDn == 1.0 )
      {
         for( i=0; i < (int) outNBElement.value ; i++ )
         {
            tempReal = tempBuffer2[i];
            tempReal2 = outRealMiddleBand[i];
            outRealLowerBand[i] = tempReal2 - tempReal;
            outRealUpperBand[i] = tempReal2 + (tempReal * optInNbDevUp);
         }
      }
      else
      {
         for( i=0; i < (int) outNBElement.value ; i++ )
         {
            tempReal = tempBuffer2[i];
            tempReal2 = outRealMiddleBand[i];
            outRealUpperBand[i] = tempReal2 + (tempReal * optInNbDevUp);
            outRealLowerBand[i] = tempReal2 - (tempReal * optInNbDevDn);
         }
      }
      return RetCode.Success ;
   }
   
   /* Generated */
   public int betaLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 5;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return -1;
      return optInTimePeriod;
   }
   public RetCode beta( int startIdx,
      int endIdx,
      double inReal0[],
      double inReal1[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      double S_xx = 0.0f;
      double S_xy = 0.0f;
      double S_x = 0.0f;
      double S_y = 0.0f;
      double last_price_x = 0.0f;
      double last_price_y = 0.0f;
      double trailing_last_price_x = 0.0f;
      double trailing_last_price_y = 0.0f;
      double tmp_real = 0.0f;
      double x;
      double y;
      double n = 0.0f;
      int i, outIdx;
      int trailingIdx, nbInitialElementNeeded;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 5;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      nbInitialElementNeeded = optInTimePeriod;
      if( startIdx < nbInitialElementNeeded )
         startIdx = nbInitialElementNeeded;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      trailingIdx = startIdx-nbInitialElementNeeded;
      last_price_x = trailing_last_price_x = inReal0[trailingIdx];
      last_price_y = trailing_last_price_y = inReal1[trailingIdx];
      i = ++trailingIdx;
      while( i < startIdx )
      {
         tmp_real = inReal0[i];
         if( ! (((-0.00000001)<last_price_x)&&(last_price_x<0.00000001)) )
            x = (tmp_real-last_price_x)/last_price_x;
         else
            x = 0.0;
         last_price_x = tmp_real;
         tmp_real = inReal1[i++];
         if( ! (((-0.00000001)<last_price_y)&&(last_price_y<0.00000001)) )
            y = (tmp_real-last_price_y)/last_price_y;
         else
            y = 0.0;
         last_price_y = tmp_real;
         S_xx += x*x;
         S_xy += x*y;
         S_x += x;
         S_y += y;
      }
      outIdx = 0;
      n = (double)optInTimePeriod;
      do
      {
         tmp_real = inReal0[i];
         if( ! (((-0.00000001)<last_price_x)&&(last_price_x<0.00000001)) )
            x = (tmp_real-last_price_x)/last_price_x;
         else
            x = 0.0;
         last_price_x = tmp_real;
         tmp_real = inReal1[i++];
         if( ! (((-0.00000001)<last_price_y)&&(last_price_y<0.00000001)) )
            y = (tmp_real-last_price_y)/last_price_y;
         else
            y = 0.0;
         last_price_y = tmp_real;
         S_xx += x*x;
         S_xy += x*y;
         S_x += x;
         S_y += y;
         tmp_real = inReal0[trailingIdx];
         if( ! (((-0.00000001)<trailing_last_price_x)&&(trailing_last_price_x<0.00000001)) )
            x = (tmp_real-trailing_last_price_x)/trailing_last_price_x;
         else
            x = 0.0;
         trailing_last_price_x = tmp_real;
         tmp_real = inReal1[trailingIdx++];
         if( ! (((-0.00000001)<trailing_last_price_y)&&(trailing_last_price_y<0.00000001)) )
            y = (tmp_real-trailing_last_price_y)/trailing_last_price_y;
         else
            y = 0.0;
         trailing_last_price_y = tmp_real;
         tmp_real = (n * S_xx) - (S_x * S_x);
         if( ! (((-0.00000001)<tmp_real)&&(tmp_real<0.00000001)) )
            outReal[outIdx++] = ((n * S_xy) - (S_x * S_y)) / tmp_real;
         else
            outReal[outIdx++] = 0.0;
         S_xx -= x*x;
         S_xy -= x*y;
         S_x -= x;
         S_y -= y;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int bopLookback( )
   {
      return 0;
   }
   public RetCode bop( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx, i;
      double tempReal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      outIdx = 0;
      for( i=startIdx; i <= endIdx; i++ )
      {
         tempReal = inHigh[i]-inLow[i];
         if( (tempReal<0.00000001) )
            outReal[outIdx++] = 0.0;
         else
            outReal[outIdx++] = (inClose[i] - inOpen[i])/tempReal;
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cciLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      return (optInTimePeriod-1);
   }
   public RetCode cci( int startIdx,
      int endIdx,
      double inHigh[],
      double inLow[],
      double inClose[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      double tempReal, tempReal2, theAverage, lastValue;
      int i, j, outIdx, lookbackTotal;
      int circBuffer_Idx = 0; double []circBuffer; int maxIdx_circBuffer = (30-1) ;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      lookbackTotal = (optInTimePeriod-1);
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      { if( optInTimePeriod <= 0 ) return RetCode.AllocErr ; circBuffer = new double[optInTimePeriod]; maxIdx_circBuffer = (optInTimePeriod-1); } ;
      i=startIdx-lookbackTotal;
      if( optInTimePeriod > 1 )
      {
         while( i < startIdx )
         {
            circBuffer[circBuffer_Idx] = (inHigh[i]+inLow[i]+inClose[i])/3;
            i++;
            { circBuffer_Idx ++; if( circBuffer_Idx > maxIdx_circBuffer ) circBuffer_Idx = 0; } ;
         }
      }
      outIdx = 0;
      do
      {
         lastValue = (inHigh[i]+inLow[i]+inClose[i])/3;
         circBuffer[circBuffer_Idx] = lastValue;
         theAverage = 0;
         for( j=0; j < optInTimePeriod; j++ )
            theAverage += circBuffer[j];
         theAverage /= optInTimePeriod;
         tempReal2 = 0;
         for( j=0; j < optInTimePeriod; j++ )
            tempReal2 += Math.abs (circBuffer[j]-theAverage);
         tempReal = lastValue-theAverage;
         if( (tempReal != 0.0) && (tempReal2 != 0.0) )
         {
            outReal[outIdx++] = tempReal/(0.015*(tempReal2/optInTimePeriod));
         }
         else
            outReal[outIdx++] = 0.0;
         { circBuffer_Idx ++; if( circBuffer_Idx > maxIdx_circBuffer ) circBuffer_Idx = 0; } ;
         i++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdl2CrowsLookback( )
   {
      return (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) + 2;
   }
   public RetCode cdl2Crows( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyLongPeriodTotal;
      int i, outIdx, BodyLongTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdl2CrowsLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyLongPeriodTotal = 0;
      BodyLongTrailingIdx = startIdx -2 - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      i = BodyLongTrailingIdx;
      while( i < startIdx-2 ) {
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) == 1 &&
            ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == -1 &&
            ( (((inOpen[i-1]) < (inClose[i-1])) ? (inOpen[i-1]) : (inClose[i-1])) > (((inOpen[i-2]) > (inClose[i-2])) ? (inOpen[i-2]) : (inClose[i-2])) ) &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == -1 &&
            inOpen[i] < inOpen[i-1] && inOpen[i] > inClose[i-1] &&
            inClose[i] > inOpen[i-2] && inClose[i] < inClose[i-2]
            )
            outInteger[outIdx++] = -100;
         else
            outInteger[outIdx++] = 0;
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx] - inOpen[BodyLongTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx] - inLow[BodyLongTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx] - ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inClose[BodyLongTrailingIdx] : inOpen[BodyLongTrailingIdx] ) ) + ( ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inOpen[BodyLongTrailingIdx] : inClose[BodyLongTrailingIdx] ) - inLow[BodyLongTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyLongTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdl3BlackCrowsLookback( )
   {
      return (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) + 3;
   }
   public RetCode cdl3BlackCrows( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double []ShadowVeryShortPeriodTotal = new double[3] ;
      int i, outIdx, totIdx, ShadowVeryShortTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdl3BlackCrowsLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      ShadowVeryShortPeriodTotal[2] = 0;
      ShadowVeryShortPeriodTotal[1] = 0;
      ShadowVeryShortPeriodTotal[0] = 0;
      ShadowVeryShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ;
      i = ShadowVeryShortTrailingIdx;
      while( i < startIdx ) {
         ShadowVeryShortPeriodTotal[2] += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ;
         ShadowVeryShortPeriodTotal[1] += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         ShadowVeryShortPeriodTotal[0] += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( inClose[i-3] >= inOpen[i-3] ? 1 : -1 ) == 1 &&
            ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) == -1 &&
            ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal[2] / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == -1 &&
            ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal[1] / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == -1 &&
            ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal[0] / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            inOpen[i-1] < inOpen[i-2] && inOpen[i-1] > inClose[i-2] &&
            inOpen[i] < inOpen[i-1] && inOpen[i] > inClose[i-1] &&
            inHigh[i-3] > inClose[i-2] &&
            inClose[i-2] > inClose[i-1] &&
            inClose[i-1] > inClose[i]
            )
            outInteger[outIdx++] = -100;
         else
            outInteger[outIdx++] = 0;
         for (totIdx = 2; totIdx >= 0; --totIdx)
            ShadowVeryShortPeriodTotal[totIdx] += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-totIdx] - inOpen[i-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-totIdx] - inLow[i-totIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-totIdx] - ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inClose[i-totIdx] : inOpen[i-totIdx] ) ) + ( ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inOpen[i-totIdx] : inClose[i-totIdx] ) - inLow[i-totIdx] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowVeryShortTrailingIdx-totIdx] - inOpen[ShadowVeryShortTrailingIdx-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowVeryShortTrailingIdx-totIdx] - inLow[ShadowVeryShortTrailingIdx-totIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowVeryShortTrailingIdx-totIdx] - ( inClose[ShadowVeryShortTrailingIdx-totIdx] >= inOpen[ShadowVeryShortTrailingIdx-totIdx] ? inClose[ShadowVeryShortTrailingIdx-totIdx] : inOpen[ShadowVeryShortTrailingIdx-totIdx] ) ) + ( ( inClose[ShadowVeryShortTrailingIdx-totIdx] >= inOpen[ShadowVeryShortTrailingIdx-totIdx] ? inOpen[ShadowVeryShortTrailingIdx-totIdx] : inClose[ShadowVeryShortTrailingIdx-totIdx] ) - inLow[ShadowVeryShortTrailingIdx-totIdx] ) : 0 ) ) ) ;
         i++;
         ShadowVeryShortTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdl3InsideLookback( )
   {
      return ((( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) + 2;
   }
   public RetCode cdl3Inside( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyShortPeriodTotal, BodyLongPeriodTotal;
      int i, outIdx, BodyShortTrailingIdx, BodyLongTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdl3InsideLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyLongPeriodTotal = 0;
      BodyShortPeriodTotal = 0;
      BodyLongTrailingIdx = startIdx -2 - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      BodyShortTrailingIdx = startIdx -1 - (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ;
      i = BodyLongTrailingIdx;
      while( i < startIdx-2 ) {
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = BodyShortTrailingIdx;
      while( i < startIdx-1 ) {
         BodyShortPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) <= ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) != 0.0? BodyShortPeriodTotal / (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            (((inClose[i-1]) > (inOpen[i-1])) ? (inClose[i-1]) : (inOpen[i-1])) < (((inClose[i-2]) > (inOpen[i-2])) ? (inClose[i-2]) : (inOpen[i-2])) &&
            (((inClose[i-1]) < (inOpen[i-1])) ? (inClose[i-1]) : (inOpen[i-1])) > (((inClose[i-2]) < (inOpen[i-2])) ? (inClose[i-2]) : (inOpen[i-2])) &&
            ( ( ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) == 1 && ( inClose[i] >= inOpen[i] ? 1 : -1 ) == -1 && inClose[i] < inOpen[i-2] )
            ||
            ( ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) == -1 && ( inClose[i] >= inOpen[i] ? 1 : -1 ) == 1 && inClose[i] > inOpen[i-2] )
            )
            )
            outInteger[outIdx++] = - ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) * 100;
         else
            outInteger[outIdx++] = 0;
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx] - inOpen[BodyLongTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx] - inLow[BodyLongTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx] - ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inClose[BodyLongTrailingIdx] : inOpen[BodyLongTrailingIdx] ) ) + ( ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inOpen[BodyLongTrailingIdx] : inClose[BodyLongTrailingIdx] ) - inLow[BodyLongTrailingIdx] ) : 0 ) ) ) ;
         BodyShortPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyShortTrailingIdx] - inOpen[BodyShortTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyShortTrailingIdx] - inLow[BodyShortTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyShortTrailingIdx] - ( inClose[BodyShortTrailingIdx] >= inOpen[BodyShortTrailingIdx] ? inClose[BodyShortTrailingIdx] : inOpen[BodyShortTrailingIdx] ) ) + ( ( inClose[BodyShortTrailingIdx] >= inOpen[BodyShortTrailingIdx] ? inOpen[BodyShortTrailingIdx] : inClose[BodyShortTrailingIdx] ) - inLow[BodyShortTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyLongTrailingIdx++;
         BodyShortTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdl3LineStrikeLookback( )
   {
      return (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) + 3;
   }
   public RetCode cdl3LineStrike( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double []NearPeriodTotal = new double[4] ;
      int i, outIdx, totIdx, NearTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdl3LineStrikeLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      NearPeriodTotal[3] = 0;
      NearPeriodTotal[2] = 0;
      NearTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) ;
      i = NearTrailingIdx;
      while( i < startIdx ) {
         NearPeriodTotal[3] += ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-3] - inOpen[i-3] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-3] - inLow[i-3] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-3] - ( inClose[i-3] >= inOpen[i-3] ? inClose[i-3] : inOpen[i-3] ) ) + ( ( inClose[i-3] >= inOpen[i-3] ? inOpen[i-3] : inClose[i-3] ) - inLow[i-3] ) : 0 ) ) ) ;
         NearPeriodTotal[2] += ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( inClose[i-3] >= inOpen[i-3] ? 1 : -1 ) == ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) &&
            ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) == ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == - ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) &&
            inOpen[i-2] >= (((inOpen[i-3]) < (inClose[i-3])) ? (inOpen[i-3]) : (inClose[i-3])) - ( (this.candleSettings[CandleSettingType.Near.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) != 0.0? NearPeriodTotal[3] / (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-3] - inOpen[i-3] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-3] - inLow[i-3] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-3] - ( inClose[i-3] >= inOpen[i-3] ? inClose[i-3] : inOpen[i-3] ) ) + ( ( inClose[i-3] >= inOpen[i-3] ? inOpen[i-3] : inClose[i-3] ) - inLow[i-3] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            inOpen[i-2] <= (((inOpen[i-3]) > (inClose[i-3])) ? (inOpen[i-3]) : (inClose[i-3])) + ( (this.candleSettings[CandleSettingType.Near.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) != 0.0? NearPeriodTotal[3] / (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-3] - inOpen[i-3] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-3] - inLow[i-3] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-3] - ( inClose[i-3] >= inOpen[i-3] ? inClose[i-3] : inOpen[i-3] ) ) + ( ( inClose[i-3] >= inOpen[i-3] ? inOpen[i-3] : inClose[i-3] ) - inLow[i-3] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            inOpen[i-1] >= (((inOpen[i-2]) < (inClose[i-2])) ? (inOpen[i-2]) : (inClose[i-2])) - ( (this.candleSettings[CandleSettingType.Near.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) != 0.0? NearPeriodTotal[2] / (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            inOpen[i-1] <= (((inOpen[i-2]) > (inClose[i-2])) ? (inOpen[i-2]) : (inClose[i-2])) + ( (this.candleSettings[CandleSettingType.Near.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) != 0.0? NearPeriodTotal[2] / (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            (
            (
            ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == 1 &&
            inClose[i-1] > inClose[i-2] && inClose[i-2] > inClose[i-3] &&
            inOpen[i] > inClose[i-1] &&
            inClose[i] < inOpen[i-3]
            ) ||
            (
            ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == -1 &&
            inClose[i-1] < inClose[i-2] && inClose[i-2] < inClose[i-3] &&
            inOpen[i] < inClose[i-1] &&
            inClose[i] > inOpen[i-3]
            )
            )
            )
            outInteger[outIdx++] = ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) * 100;
         else
            outInteger[outIdx++] = 0;
         for (totIdx = 3; totIdx >= 2; --totIdx)
            NearPeriodTotal[totIdx] += ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-totIdx] - inOpen[i-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-totIdx] - inLow[i-totIdx] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-totIdx] - ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inClose[i-totIdx] : inOpen[i-totIdx] ) ) + ( ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inOpen[i-totIdx] : inClose[i-totIdx] ) - inLow[i-totIdx] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[NearTrailingIdx-totIdx] - inOpen[NearTrailingIdx-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[NearTrailingIdx-totIdx] - inLow[NearTrailingIdx-totIdx] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[NearTrailingIdx-totIdx] - ( inClose[NearTrailingIdx-totIdx] >= inOpen[NearTrailingIdx-totIdx] ? inClose[NearTrailingIdx-totIdx] : inOpen[NearTrailingIdx-totIdx] ) ) + ( ( inClose[NearTrailingIdx-totIdx] >= inOpen[NearTrailingIdx-totIdx] ? inOpen[NearTrailingIdx-totIdx] : inClose[NearTrailingIdx-totIdx] ) - inLow[NearTrailingIdx-totIdx] ) : 0 ) ) ) ;
         i++;
         NearTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }

   /* Generated */
   public int cdl3OutsideLookback( )
   {
      return 3;
   }
   public RetCode cdl3Outside( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      int i, outIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdl3OutsideLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == 1 && ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) == -1 &&
            inClose[i-1] > inOpen[i-2] && inOpen[i-1] < inClose[i-2] &&
            inClose[i] > inClose[i-1]
            )
            ||
            ( ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == -1 && ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) == 1 &&
            inOpen[i-1] > inClose[i-2] && inClose[i-1] < inOpen[i-2] &&
            inClose[i] < inClose[i-1]
            )
            )
            outInteger[outIdx++] = ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) * 100;
         else
            outInteger[outIdx++] = 0;
         i++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdl3StarsInSouthLookback( )
   {
      return ((( ((( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ) > ( ((( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) )) )) ? ( ((( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ) : ( ((( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) )) )) +
         2;
   }
   public RetCode cdl3StarsInSouth( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyLongPeriodTotal, BodyShortPeriodTotal, ShadowLongPeriodTotal;
      double []ShadowVeryShortPeriodTotal = new double[2] ;
      int i, outIdx, totIdx, BodyLongTrailingIdx, BodyShortTrailingIdx, ShadowLongTrailingIdx, ShadowVeryShortTrailingIdx,
         lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdl3StarsInSouthLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyLongPeriodTotal = 0;
      BodyLongTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      ShadowLongPeriodTotal = 0;
      ShadowLongTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) ;
      ShadowVeryShortPeriodTotal[1] = 0;
      ShadowVeryShortPeriodTotal[0] = 0;
      ShadowVeryShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ;
      BodyShortPeriodTotal = 0;
      BodyShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ;
      i = BodyLongTrailingIdx;
      while( i < startIdx ) {
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ;
         i++;
      }
      i = ShadowLongTrailingIdx;
      while( i < startIdx ) {
         ShadowLongPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ;
         i++;
      }
      i = ShadowVeryShortTrailingIdx;
      while( i < startIdx ) {
         ShadowVeryShortPeriodTotal[1] += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         ShadowVeryShortPeriodTotal[0] += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = BodyShortTrailingIdx;
      while( i < startIdx ) {
         BodyShortPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) == -1 &&
            ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == -1 &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == -1 &&
            ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) > ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) != 0.0? ShadowLongPeriodTotal / (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) < ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) &&
            inOpen[i-1] > inClose[i-2] && inOpen[i-1] <= inHigh[i-2] &&
            inLow[i-1] < inClose[i-2] &&
            inLow[i-1] >= inLow[i-2] &&
            ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) > ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal[1] / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i] - inOpen[i] ) ) < ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) != 0.0? BodyShortPeriodTotal / (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal[0] / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal[0] / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            inLow[i] > inLow[i-1] && inHigh[i] < inHigh[i-1]
            )
            outInteger[outIdx++] = 100;
         else
            outInteger[outIdx++] = 0;
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx-2] - inOpen[BodyLongTrailingIdx-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx-2] - inLow[BodyLongTrailingIdx-2] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx-2] - ( inClose[BodyLongTrailingIdx-2] >= inOpen[BodyLongTrailingIdx-2] ? inClose[BodyLongTrailingIdx-2] : inOpen[BodyLongTrailingIdx-2] ) ) + ( ( inClose[BodyLongTrailingIdx-2] >= inOpen[BodyLongTrailingIdx-2] ? inOpen[BodyLongTrailingIdx-2] : inClose[BodyLongTrailingIdx-2] ) - inLow[BodyLongTrailingIdx-2] ) : 0 ) ) ) ;
         ShadowLongPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowLongTrailingIdx-2] - inOpen[ShadowLongTrailingIdx-2] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowLongTrailingIdx-2] - inLow[ShadowLongTrailingIdx-2] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowLongTrailingIdx-2] - ( inClose[ShadowLongTrailingIdx-2] >= inOpen[ShadowLongTrailingIdx-2] ? inClose[ShadowLongTrailingIdx-2] : inOpen[ShadowLongTrailingIdx-2] ) ) + ( ( inClose[ShadowLongTrailingIdx-2] >= inOpen[ShadowLongTrailingIdx-2] ? inOpen[ShadowLongTrailingIdx-2] : inClose[ShadowLongTrailingIdx-2] ) - inLow[ShadowLongTrailingIdx-2] ) : 0 ) ) ) ;
         for (totIdx = 1; totIdx >= 0; --totIdx)
            ShadowVeryShortPeriodTotal[totIdx] += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-totIdx] - inOpen[i-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-totIdx] - inLow[i-totIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-totIdx] - ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inClose[i-totIdx] : inOpen[i-totIdx] ) ) + ( ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inOpen[i-totIdx] : inClose[i-totIdx] ) - inLow[i-totIdx] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowVeryShortTrailingIdx-totIdx] - inOpen[ShadowVeryShortTrailingIdx-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowVeryShortTrailingIdx-totIdx] - inLow[ShadowVeryShortTrailingIdx-totIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowVeryShortTrailingIdx-totIdx] - ( inClose[ShadowVeryShortTrailingIdx-totIdx] >= inOpen[ShadowVeryShortTrailingIdx-totIdx] ? inClose[ShadowVeryShortTrailingIdx-totIdx] : inOpen[ShadowVeryShortTrailingIdx-totIdx] ) ) + ( ( inClose[ShadowVeryShortTrailingIdx-totIdx] >= inOpen[ShadowVeryShortTrailingIdx-totIdx] ? inOpen[ShadowVeryShortTrailingIdx-totIdx] : inClose[ShadowVeryShortTrailingIdx-totIdx] ) - inLow[ShadowVeryShortTrailingIdx-totIdx] ) : 0 ) ) ) ;
         BodyShortPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyShortTrailingIdx] - inOpen[BodyShortTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyShortTrailingIdx] - inLow[BodyShortTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyShortTrailingIdx] - ( inClose[BodyShortTrailingIdx] >= inOpen[BodyShortTrailingIdx] ? inClose[BodyShortTrailingIdx] : inOpen[BodyShortTrailingIdx] ) ) + ( ( inClose[BodyShortTrailingIdx] >= inOpen[BodyShortTrailingIdx] ? inOpen[BodyShortTrailingIdx] : inClose[BodyShortTrailingIdx] ) - inLow[BodyShortTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyLongTrailingIdx++;
         ShadowLongTrailingIdx++;
         ShadowVeryShortTrailingIdx++;
         BodyShortTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdl3WhiteSoldiersLookback( )
   {
      return ((( ((( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) )) ) > ( ((( (this.candleSettings[CandleSettingType.Far.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.Far.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) )) )) ? ( ((( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) )) ) : ( ((( (this.candleSettings[CandleSettingType.Far.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.Far.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) )) )) +
         2;
   }
   public RetCode cdl3WhiteSoldiers( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double []ShadowVeryShortPeriodTotal = new double[3] ;
      double []NearPeriodTotal = new double[3] ;
      double []FarPeriodTotal = new double[3] ;
      double BodyShortPeriodTotal;
      int i, outIdx, totIdx, ShadowVeryShortTrailingIdx, NearTrailingIdx, FarTrailingIdx, BodyShortTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdl3WhiteSoldiersLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      ShadowVeryShortPeriodTotal[2] = 0;
      ShadowVeryShortPeriodTotal[1] = 0;
      ShadowVeryShortPeriodTotal[0] = 0;
      ShadowVeryShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ;
      NearPeriodTotal[2] = 0;
      NearPeriodTotal[1] = 0;
      NearPeriodTotal[0] = 0;
      NearTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) ;
      FarPeriodTotal[2] = 0;
      FarPeriodTotal[1] = 0;
      FarPeriodTotal[0] = 0;
      FarTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.Far.ordinal()].avgPeriod) ;
      BodyShortPeriodTotal = 0;
      BodyShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ;
      i = ShadowVeryShortTrailingIdx;
      while( i < startIdx ) {
         ShadowVeryShortPeriodTotal[2] += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ;
         ShadowVeryShortPeriodTotal[1] += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         ShadowVeryShortPeriodTotal[0] += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = NearTrailingIdx;
      while( i < startIdx ) {
         NearPeriodTotal[2] += ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ;
         NearPeriodTotal[1] += ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         i++;
      }
      i = FarTrailingIdx;
      while( i < startIdx ) {
         FarPeriodTotal[2] += ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ;
         FarPeriodTotal[1] += ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         i++;
      }
      i = BodyShortTrailingIdx;
      while( i < startIdx ) {
         BodyShortPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) == 1 &&
            ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal[2] / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == 1 &&
            ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal[1] / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == 1 &&
            ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal[0] / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            inClose[i] > inClose[i-1] && inClose[i-1] > inClose[i-2] &&
            inOpen[i-1] > inOpen[i-2] &&
            inOpen[i-1] <= inClose[i-2] + ( (this.candleSettings[CandleSettingType.Near.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) != 0.0? NearPeriodTotal[2] / (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            inOpen[i] > inOpen[i-1] &&
            inOpen[i] <= inClose[i-1] + ( (this.candleSettings[CandleSettingType.Near.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) != 0.0? NearPeriodTotal[1] / (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) > ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) - ( (this.candleSettings[CandleSettingType.Far.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Far.ordinal()].avgPeriod) != 0.0? FarPeriodTotal[2] / (this.candleSettings[CandleSettingType.Far.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i] - inOpen[i] ) ) > ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) - ( (this.candleSettings[CandleSettingType.Far.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Far.ordinal()].avgPeriod) != 0.0? FarPeriodTotal[1] / (this.candleSettings[CandleSettingType.Far.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i] - inOpen[i] ) ) > ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) != 0.0? BodyShortPeriodTotal / (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            )
            outInteger[outIdx++] = 100;
         else
            outInteger[outIdx++] = 0;
         for (totIdx = 2; totIdx >= 0; --totIdx)
            ShadowVeryShortPeriodTotal[totIdx] += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-totIdx] - inOpen[i-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-totIdx] - inLow[i-totIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-totIdx] - ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inClose[i-totIdx] : inOpen[i-totIdx] ) ) + ( ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inOpen[i-totIdx] : inClose[i-totIdx] ) - inLow[i-totIdx] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowVeryShortTrailingIdx-totIdx] - inOpen[ShadowVeryShortTrailingIdx-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowVeryShortTrailingIdx-totIdx] - inLow[ShadowVeryShortTrailingIdx-totIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowVeryShortTrailingIdx-totIdx] - ( inClose[ShadowVeryShortTrailingIdx-totIdx] >= inOpen[ShadowVeryShortTrailingIdx-totIdx] ? inClose[ShadowVeryShortTrailingIdx-totIdx] : inOpen[ShadowVeryShortTrailingIdx-totIdx] ) ) + ( ( inClose[ShadowVeryShortTrailingIdx-totIdx] >= inOpen[ShadowVeryShortTrailingIdx-totIdx] ? inOpen[ShadowVeryShortTrailingIdx-totIdx] : inClose[ShadowVeryShortTrailingIdx-totIdx] ) - inLow[ShadowVeryShortTrailingIdx-totIdx] ) : 0 ) ) ) ;
         for (totIdx = 2; totIdx >= 1; --totIdx) {
            FarPeriodTotal[totIdx] += ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-totIdx] - inOpen[i-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-totIdx] - inLow[i-totIdx] ) : ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-totIdx] - ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inClose[i-totIdx] : inOpen[i-totIdx] ) ) + ( ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inOpen[i-totIdx] : inClose[i-totIdx] ) - inLow[i-totIdx] ) : 0 ) ) )
               - ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[FarTrailingIdx-totIdx] - inOpen[FarTrailingIdx-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[FarTrailingIdx-totIdx] - inLow[FarTrailingIdx-totIdx] ) : ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[FarTrailingIdx-totIdx] - ( inClose[FarTrailingIdx-totIdx] >= inOpen[FarTrailingIdx-totIdx] ? inClose[FarTrailingIdx-totIdx] : inOpen[FarTrailingIdx-totIdx] ) ) + ( ( inClose[FarTrailingIdx-totIdx] >= inOpen[FarTrailingIdx-totIdx] ? inOpen[FarTrailingIdx-totIdx] : inClose[FarTrailingIdx-totIdx] ) - inLow[FarTrailingIdx-totIdx] ) : 0 ) ) ) ;
            NearPeriodTotal[totIdx] += ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-totIdx] - inOpen[i-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-totIdx] - inLow[i-totIdx] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-totIdx] - ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inClose[i-totIdx] : inOpen[i-totIdx] ) ) + ( ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inOpen[i-totIdx] : inClose[i-totIdx] ) - inLow[i-totIdx] ) : 0 ) ) )
               - ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[NearTrailingIdx-totIdx] - inOpen[NearTrailingIdx-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[NearTrailingIdx-totIdx] - inLow[NearTrailingIdx-totIdx] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[NearTrailingIdx-totIdx] - ( inClose[NearTrailingIdx-totIdx] >= inOpen[NearTrailingIdx-totIdx] ? inClose[NearTrailingIdx-totIdx] : inOpen[NearTrailingIdx-totIdx] ) ) + ( ( inClose[NearTrailingIdx-totIdx] >= inOpen[NearTrailingIdx-totIdx] ? inOpen[NearTrailingIdx-totIdx] : inClose[NearTrailingIdx-totIdx] ) - inLow[NearTrailingIdx-totIdx] ) : 0 ) ) ) ;
         }
         BodyShortPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyShortTrailingIdx] - inOpen[BodyShortTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyShortTrailingIdx] - inLow[BodyShortTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyShortTrailingIdx] - ( inClose[BodyShortTrailingIdx] >= inOpen[BodyShortTrailingIdx] ? inClose[BodyShortTrailingIdx] : inOpen[BodyShortTrailingIdx] ) ) + ( ( inClose[BodyShortTrailingIdx] >= inOpen[BodyShortTrailingIdx] ? inOpen[BodyShortTrailingIdx] : inClose[BodyShortTrailingIdx] ) - inLow[BodyShortTrailingIdx] ) : 0 ) ) ) ;
         i++;
         ShadowVeryShortTrailingIdx++;
         NearTrailingIdx++;
         FarTrailingIdx++;
         BodyShortTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlAbandonedBabyLookback( double optInPenetration )
   {
      if( optInPenetration == (-4e+37) )
         optInPenetration = 3.000000e-1;
      else if( (optInPenetration < 0.000000e+0) || (optInPenetration > 3.000000e+37) )
         return -1;
      return ((( ((( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ) > ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) )) ? ( ((( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) )) +
         2;
   }
   public RetCode cdlAbandonedBaby( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      double optInPenetration,
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyDojiPeriodTotal, BodyLongPeriodTotal, BodyShortPeriodTotal;
      int i, outIdx, BodyDojiTrailingIdx, BodyLongTrailingIdx, BodyShortTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( optInPenetration == (-4e+37) )
         optInPenetration = 3.000000e-1;
      else if( (optInPenetration < 0.000000e+0) || (optInPenetration > 3.000000e+37) )
         return RetCode.BadParam ;
      lookbackTotal = cdlAbandonedBabyLookback (optInPenetration);
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyLongPeriodTotal = 0;
      BodyDojiPeriodTotal = 0;
      BodyShortPeriodTotal = 0;
      BodyLongTrailingIdx = startIdx -2 - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      BodyDojiTrailingIdx = startIdx -1 - (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ;
      BodyShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ;
      i = BodyLongTrailingIdx;
      while( i < startIdx-2 ) {
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = BodyDojiTrailingIdx;
      while( i < startIdx-1 ) {
         BodyDojiPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = BodyShortTrailingIdx;
      while( i < startIdx ) {
         BodyShortPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) <= ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) != 0.0? BodyDojiPeriodTotal / (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i] - inOpen[i] ) ) > ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) != 0.0? BodyShortPeriodTotal / (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( ( ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) == 1 &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == -1 &&
            inClose[i] < inClose[i-2] - ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) * optInPenetration &&
            ( inLow[i-1] > inHigh[i-2] ) &&
            ( inHigh[i] < inLow[i-1] )
            )
            ||
            (
            ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) == -1 &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == 1 &&
            inClose[i] > inClose[i-2] + ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) * optInPenetration &&
            ( inHigh[i-1] < inLow[i-2] ) &&
            ( inLow[i] > inHigh[i-1] )
            )
            )
            )
            outInteger[outIdx++] = ( inClose[i] >= inOpen[i] ? 1 : -1 ) * 100;
         else
            outInteger[outIdx++] = 0;
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx] - inOpen[BodyLongTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx] - inLow[BodyLongTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx] - ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inClose[BodyLongTrailingIdx] : inOpen[BodyLongTrailingIdx] ) ) + ( ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inOpen[BodyLongTrailingIdx] : inClose[BodyLongTrailingIdx] ) - inLow[BodyLongTrailingIdx] ) : 0 ) ) ) ;
         BodyDojiPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyDojiTrailingIdx] - inOpen[BodyDojiTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyDojiTrailingIdx] - inLow[BodyDojiTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyDojiTrailingIdx] - ( inClose[BodyDojiTrailingIdx] >= inOpen[BodyDojiTrailingIdx] ? inClose[BodyDojiTrailingIdx] : inOpen[BodyDojiTrailingIdx] ) ) + ( ( inClose[BodyDojiTrailingIdx] >= inOpen[BodyDojiTrailingIdx] ? inOpen[BodyDojiTrailingIdx] : inClose[BodyDojiTrailingIdx] ) - inLow[BodyDojiTrailingIdx] ) : 0 ) ) ) ;
         BodyShortPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyShortTrailingIdx] - inOpen[BodyShortTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyShortTrailingIdx] - inLow[BodyShortTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyShortTrailingIdx] - ( inClose[BodyShortTrailingIdx] >= inOpen[BodyShortTrailingIdx] ? inClose[BodyShortTrailingIdx] : inOpen[BodyShortTrailingIdx] ) ) + ( ( inClose[BodyShortTrailingIdx] >= inOpen[BodyShortTrailingIdx] ? inOpen[BodyShortTrailingIdx] : inClose[BodyShortTrailingIdx] ) - inLow[BodyShortTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyLongTrailingIdx++;
         BodyDojiTrailingIdx++;
         BodyShortTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlAdvanceBlockLookback( )
   {
      return ((( ((( ((( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].avgPeriod) )) ) > ( ((( (this.candleSettings[CandleSettingType.Far.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.Far.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) )) )) ? ( ((( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].avgPeriod) )) ) : ( ((( (this.candleSettings[CandleSettingType.Far.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.Far.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) )) )) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ? ( ((( ((( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].avgPeriod) )) ) > ( ((( (this.candleSettings[CandleSettingType.Far.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.Far.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) )) )) ? ( ((( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].avgPeriod) )) ) : ( ((( (this.candleSettings[CandleSettingType.Far.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.Far.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) )) )) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) +
         2;
   }
   public RetCode cdlAdvanceBlock( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double []ShadowShortPeriodTotal = new double[3] ;
      double []ShadowLongPeriodTotal = new double[2] ;
      double []NearPeriodTotal = new double[3] ;
      double []FarPeriodTotal = new double[3] ;
      double BodyLongPeriodTotal;
      int i, outIdx, totIdx, BodyLongTrailingIdx, ShadowShortTrailingIdx, ShadowLongTrailingIdx, NearTrailingIdx,
         FarTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlAdvanceBlockLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      ShadowShortPeriodTotal[2] = 0;
      ShadowShortPeriodTotal[1] = 0;
      ShadowShortPeriodTotal[0] = 0;
      ShadowShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].avgPeriod) ;
      ShadowLongPeriodTotal[1] = 0;
      ShadowLongPeriodTotal[0] = 0;
      ShadowLongTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) ;
      NearPeriodTotal[2] = 0;
      NearPeriodTotal[1] = 0;
      NearPeriodTotal[0] = 0;
      NearTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) ;
      FarPeriodTotal[2] = 0;
      FarPeriodTotal[1] = 0;
      FarPeriodTotal[0] = 0;
      FarTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.Far.ordinal()].avgPeriod) ;
      BodyLongPeriodTotal = 0;
      BodyLongTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      i = ShadowShortTrailingIdx;
      while( i < startIdx ) {
         ShadowShortPeriodTotal[2] += ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ;
         ShadowShortPeriodTotal[1] += ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         ShadowShortPeriodTotal[0] += ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = ShadowLongTrailingIdx;
      while( i < startIdx ) {
         ShadowLongPeriodTotal[1] += ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         ShadowLongPeriodTotal[0] += ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = NearTrailingIdx;
      while( i < startIdx ) {
         NearPeriodTotal[2] += ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ;
         NearPeriodTotal[1] += ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         i++;
      }
      i = FarTrailingIdx;
      while( i < startIdx ) {
         FarPeriodTotal[2] += ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ;
         FarPeriodTotal[1] += ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         i++;
      }
      i = BodyLongTrailingIdx;
      while( i < startIdx ) {
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) == 1 &&
            ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == 1 &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == 1 &&
            inClose[i] > inClose[i-1] && inClose[i-1] > inClose[i-2] &&
            inOpen[i-1] > inOpen[i-2] &&
            inOpen[i-1] <= inClose[i-2] + ( (this.candleSettings[CandleSettingType.Near.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) != 0.0? NearPeriodTotal[2] / (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            inOpen[i] > inOpen[i-1] &&
            inOpen[i] <= inClose[i-1] + ( (this.candleSettings[CandleSettingType.Near.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) != 0.0? NearPeriodTotal[1] / (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) < ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].avgPeriod) != 0.0? ShadowShortPeriodTotal[2] / (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            (
            (
            ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) < ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) - ( (this.candleSettings[CandleSettingType.Far.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Far.ordinal()].avgPeriod) != 0.0? FarPeriodTotal[2] / (this.candleSettings[CandleSettingType.Far.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i] - inOpen[i] ) ) < ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) + ( (this.candleSettings[CandleSettingType.Near.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) != 0.0? NearPeriodTotal[1] / (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            ) ||
            (
            ( Math.abs ( inClose[i] - inOpen[i] ) ) < ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) - ( (this.candleSettings[CandleSettingType.Far.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Far.ordinal()].avgPeriod) != 0.0? FarPeriodTotal[1] / (this.candleSettings[CandleSettingType.Far.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            ) ||
            (
            ( Math.abs ( inClose[i] - inOpen[i] ) ) < ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) &&
            ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) < ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) &&
            (
            ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) > ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].avgPeriod) != 0.0? ShadowShortPeriodTotal[0] / (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) ||
            ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) > ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].avgPeriod) != 0.0? ShadowShortPeriodTotal[1] / (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            )
            ) ||
            (
            ( Math.abs ( inClose[i] - inOpen[i] ) ) < ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) &&
            ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) > ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) != 0.0? ShadowLongPeriodTotal[0] / (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            )
            )
            )
            outInteger[outIdx++] = -100;
         else
            outInteger[outIdx++] = 0;
         for (totIdx = 2; totIdx >= 0; --totIdx)
            ShadowShortPeriodTotal[totIdx] += ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-totIdx] - inOpen[i-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-totIdx] - inLow[i-totIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-totIdx] - ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inClose[i-totIdx] : inOpen[i-totIdx] ) ) + ( ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inOpen[i-totIdx] : inClose[i-totIdx] ) - inLow[i-totIdx] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowShortTrailingIdx-totIdx] - inOpen[ShadowShortTrailingIdx-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowShortTrailingIdx-totIdx] - inLow[ShadowShortTrailingIdx-totIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowShortTrailingIdx-totIdx] - ( inClose[ShadowShortTrailingIdx-totIdx] >= inOpen[ShadowShortTrailingIdx-totIdx] ? inClose[ShadowShortTrailingIdx-totIdx] : inOpen[ShadowShortTrailingIdx-totIdx] ) ) + ( ( inClose[ShadowShortTrailingIdx-totIdx] >= inOpen[ShadowShortTrailingIdx-totIdx] ? inOpen[ShadowShortTrailingIdx-totIdx] : inClose[ShadowShortTrailingIdx-totIdx] ) - inLow[ShadowShortTrailingIdx-totIdx] ) : 0 ) ) ) ;
         for (totIdx = 1; totIdx >= 0; --totIdx)
            ShadowLongPeriodTotal[totIdx] += ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-totIdx] - inOpen[i-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-totIdx] - inLow[i-totIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-totIdx] - ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inClose[i-totIdx] : inOpen[i-totIdx] ) ) + ( ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inOpen[i-totIdx] : inClose[i-totIdx] ) - inLow[i-totIdx] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowLongTrailingIdx-totIdx] - inOpen[ShadowLongTrailingIdx-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowLongTrailingIdx-totIdx] - inLow[ShadowLongTrailingIdx-totIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowLongTrailingIdx-totIdx] - ( inClose[ShadowLongTrailingIdx-totIdx] >= inOpen[ShadowLongTrailingIdx-totIdx] ? inClose[ShadowLongTrailingIdx-totIdx] : inOpen[ShadowLongTrailingIdx-totIdx] ) ) + ( ( inClose[ShadowLongTrailingIdx-totIdx] >= inOpen[ShadowLongTrailingIdx-totIdx] ? inOpen[ShadowLongTrailingIdx-totIdx] : inClose[ShadowLongTrailingIdx-totIdx] ) - inLow[ShadowLongTrailingIdx-totIdx] ) : 0 ) ) ) ;
         for (totIdx = 2; totIdx >= 1; --totIdx) {
            FarPeriodTotal[totIdx] += ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-totIdx] - inOpen[i-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-totIdx] - inLow[i-totIdx] ) : ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-totIdx] - ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inClose[i-totIdx] : inOpen[i-totIdx] ) ) + ( ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inOpen[i-totIdx] : inClose[i-totIdx] ) - inLow[i-totIdx] ) : 0 ) ) )
               - ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[FarTrailingIdx-totIdx] - inOpen[FarTrailingIdx-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[FarTrailingIdx-totIdx] - inLow[FarTrailingIdx-totIdx] ) : ( (this.candleSettings[CandleSettingType.Far.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[FarTrailingIdx-totIdx] - ( inClose[FarTrailingIdx-totIdx] >= inOpen[FarTrailingIdx-totIdx] ? inClose[FarTrailingIdx-totIdx] : inOpen[FarTrailingIdx-totIdx] ) ) + ( ( inClose[FarTrailingIdx-totIdx] >= inOpen[FarTrailingIdx-totIdx] ? inOpen[FarTrailingIdx-totIdx] : inClose[FarTrailingIdx-totIdx] ) - inLow[FarTrailingIdx-totIdx] ) : 0 ) ) ) ;
            NearPeriodTotal[totIdx] += ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-totIdx] - inOpen[i-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-totIdx] - inLow[i-totIdx] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-totIdx] - ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inClose[i-totIdx] : inOpen[i-totIdx] ) ) + ( ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inOpen[i-totIdx] : inClose[i-totIdx] ) - inLow[i-totIdx] ) : 0 ) ) )
               - ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[NearTrailingIdx-totIdx] - inOpen[NearTrailingIdx-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[NearTrailingIdx-totIdx] - inLow[NearTrailingIdx-totIdx] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[NearTrailingIdx-totIdx] - ( inClose[NearTrailingIdx-totIdx] >= inOpen[NearTrailingIdx-totIdx] ? inClose[NearTrailingIdx-totIdx] : inOpen[NearTrailingIdx-totIdx] ) ) + ( ( inClose[NearTrailingIdx-totIdx] >= inOpen[NearTrailingIdx-totIdx] ? inOpen[NearTrailingIdx-totIdx] : inClose[NearTrailingIdx-totIdx] ) - inLow[NearTrailingIdx-totIdx] ) : 0 ) ) ) ;
         }
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx-2] - inOpen[BodyLongTrailingIdx-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx-2] - inLow[BodyLongTrailingIdx-2] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx-2] - ( inClose[BodyLongTrailingIdx-2] >= inOpen[BodyLongTrailingIdx-2] ? inClose[BodyLongTrailingIdx-2] : inOpen[BodyLongTrailingIdx-2] ) ) + ( ( inClose[BodyLongTrailingIdx-2] >= inOpen[BodyLongTrailingIdx-2] ? inOpen[BodyLongTrailingIdx-2] : inClose[BodyLongTrailingIdx-2] ) - inLow[BodyLongTrailingIdx-2] ) : 0 ) ) ) ;
         i++;
         ShadowShortTrailingIdx++;
         ShadowLongTrailingIdx++;
         NearTrailingIdx++;
         FarTrailingIdx++;
         BodyLongTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlBeltHoldLookback( )
   {
      return ((( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) )) ;
   }
   public RetCode cdlBeltHold( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyLongPeriodTotal, ShadowVeryShortPeriodTotal;
      int i, outIdx, BodyLongTrailingIdx, ShadowVeryShortTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlBeltHoldLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyLongPeriodTotal = 0;
      BodyLongTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      ShadowVeryShortPeriodTotal = 0;
      ShadowVeryShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ;
      i = BodyLongTrailingIdx;
      while( i < startIdx ) {
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = ShadowVeryShortTrailingIdx;
      while( i < startIdx ) {
         ShadowVeryShortPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      outIdx = 0;
      do
      {
         if( ( Math.abs ( inClose[i] - inOpen[i] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            (
            (
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == 1 &&
            ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            ) ||
            (
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == -1 &&
            ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            )
            ) )
            outInteger[outIdx++] = ( inClose[i] >= inOpen[i] ? 1 : -1 ) * 100;
         else
            outInteger[outIdx++] = 0;
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx] - inOpen[BodyLongTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx] - inLow[BodyLongTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx] - ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inClose[BodyLongTrailingIdx] : inOpen[BodyLongTrailingIdx] ) ) + ( ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inOpen[BodyLongTrailingIdx] : inClose[BodyLongTrailingIdx] ) - inLow[BodyLongTrailingIdx] ) : 0 ) ) ) ;
         ShadowVeryShortPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowVeryShortTrailingIdx] - inOpen[ShadowVeryShortTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowVeryShortTrailingIdx] - inLow[ShadowVeryShortTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowVeryShortTrailingIdx] - ( inClose[ShadowVeryShortTrailingIdx] >= inOpen[ShadowVeryShortTrailingIdx] ? inClose[ShadowVeryShortTrailingIdx] : inOpen[ShadowVeryShortTrailingIdx] ) ) + ( ( inClose[ShadowVeryShortTrailingIdx] >= inOpen[ShadowVeryShortTrailingIdx] ? inOpen[ShadowVeryShortTrailingIdx] : inClose[ShadowVeryShortTrailingIdx] ) - inLow[ShadowVeryShortTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyLongTrailingIdx++;
         ShadowVeryShortTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlBreakawayLookback( )
   {
      return (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) + 4;
   }
   public RetCode cdlBreakaway( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyLongPeriodTotal;
      int i, outIdx, BodyLongTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlBreakawayLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyLongPeriodTotal = 0;
      BodyLongTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      i = BodyLongTrailingIdx;
      while( i < startIdx ) {
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-4] - inOpen[i-4] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-4] - inLow[i-4] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-4] - ( inClose[i-4] >= inOpen[i-4] ? inClose[i-4] : inOpen[i-4] ) ) + ( ( inClose[i-4] >= inOpen[i-4] ? inOpen[i-4] : inClose[i-4] ) - inLow[i-4] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( Math.abs ( inClose[i-4] - inOpen[i-4] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-4] - inOpen[i-4] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-4] - inLow[i-4] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-4] - ( inClose[i-4] >= inOpen[i-4] ? inClose[i-4] : inOpen[i-4] ) ) + ( ( inClose[i-4] >= inOpen[i-4] ? inOpen[i-4] : inClose[i-4] ) - inLow[i-4] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inClose[i-4] >= inOpen[i-4] ? 1 : -1 ) == ( inClose[i-3] >= inOpen[i-3] ? 1 : -1 ) &&
            ( inClose[i-3] >= inOpen[i-3] ? 1 : -1 ) == ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) &&
            ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == - ( inClose[i] >= inOpen[i] ? 1 : -1 ) &&
            (
            ( ( inClose[i-4] >= inOpen[i-4] ? 1 : -1 ) == -1 &&
            ( (((inOpen[i-3]) > (inClose[i-3])) ? (inOpen[i-3]) : (inClose[i-3])) < (((inOpen[i-4]) < (inClose[i-4])) ? (inOpen[i-4]) : (inClose[i-4])) ) &&
            inHigh[i-2] < inHigh[i-3] && inLow[i-2] < inLow[i-3] &&
            inHigh[i-1] < inHigh[i-2] && inLow[i-1] < inLow[i-2] &&
            inClose[i] > inOpen[i-3] && inClose[i] < inClose[i-4]
            )
            ||
            ( ( inClose[i-4] >= inOpen[i-4] ? 1 : -1 ) == 1 &&
            ( (((inOpen[i-3]) < (inClose[i-3])) ? (inOpen[i-3]) : (inClose[i-3])) > (((inOpen[i-4]) > (inClose[i-4])) ? (inOpen[i-4]) : (inClose[i-4])) ) &&
            inHigh[i-2] > inHigh[i-3] && inLow[i-2] > inLow[i-3] &&
            inHigh[i-1] > inHigh[i-2] && inLow[i-1] > inLow[i-2] &&
            inClose[i] < inOpen[i-3] && inClose[i] > inClose[i-4]
            )
            )
            )
            outInteger[outIdx++] = ( inClose[i] >= inOpen[i] ? 1 : -1 ) * 100;
         else
            outInteger[outIdx++] = 0;
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-4] - inOpen[i-4] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-4] - inLow[i-4] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-4] - ( inClose[i-4] >= inOpen[i-4] ? inClose[i-4] : inOpen[i-4] ) ) + ( ( inClose[i-4] >= inOpen[i-4] ? inOpen[i-4] : inClose[i-4] ) - inLow[i-4] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx-4] - inOpen[BodyLongTrailingIdx-4] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx-4] - inLow[BodyLongTrailingIdx-4] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx-4] - ( inClose[BodyLongTrailingIdx-4] >= inOpen[BodyLongTrailingIdx-4] ? inClose[BodyLongTrailingIdx-4] : inOpen[BodyLongTrailingIdx-4] ) ) + ( ( inClose[BodyLongTrailingIdx-4] >= inOpen[BodyLongTrailingIdx-4] ? inOpen[BodyLongTrailingIdx-4] : inClose[BodyLongTrailingIdx-4] ) - inLow[BodyLongTrailingIdx-4] ) : 0 ) ) ) ;
         i++;
         BodyLongTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlClosingMarubozuLookback( )
   {
      return ((( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) )) ;
   }
   public RetCode cdlClosingMarubozu( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyLongPeriodTotal, ShadowVeryShortPeriodTotal;
      int i, outIdx, BodyLongTrailingIdx, ShadowVeryShortTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlClosingMarubozuLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyLongPeriodTotal = 0;
      BodyLongTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      ShadowVeryShortPeriodTotal = 0;
      ShadowVeryShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ;
      i = BodyLongTrailingIdx;
      while( i < startIdx ) {
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = ShadowVeryShortTrailingIdx;
      while( i < startIdx ) {
         ShadowVeryShortPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      outIdx = 0;
      do
      {
         if( ( Math.abs ( inClose[i] - inOpen[i] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            (
            (
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == 1 &&
            ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            ) ||
            (
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == -1 &&
            ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            )
            ) )
            outInteger[outIdx++] = ( inClose[i] >= inOpen[i] ? 1 : -1 ) * 100;
         else
            outInteger[outIdx++] = 0;
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx] - inOpen[BodyLongTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx] - inLow[BodyLongTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx] - ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inClose[BodyLongTrailingIdx] : inOpen[BodyLongTrailingIdx] ) ) + ( ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inOpen[BodyLongTrailingIdx] : inClose[BodyLongTrailingIdx] ) - inLow[BodyLongTrailingIdx] ) : 0 ) ) ) ;
         ShadowVeryShortPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowVeryShortTrailingIdx] - inOpen[ShadowVeryShortTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowVeryShortTrailingIdx] - inLow[ShadowVeryShortTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowVeryShortTrailingIdx] - ( inClose[ShadowVeryShortTrailingIdx] >= inOpen[ShadowVeryShortTrailingIdx] ? inClose[ShadowVeryShortTrailingIdx] : inOpen[ShadowVeryShortTrailingIdx] ) ) + ( ( inClose[ShadowVeryShortTrailingIdx] >= inOpen[ShadowVeryShortTrailingIdx] ? inOpen[ShadowVeryShortTrailingIdx] : inClose[ShadowVeryShortTrailingIdx] ) - inLow[ShadowVeryShortTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyLongTrailingIdx++;
         ShadowVeryShortTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlConcealBabysWallLookback( )
   {
      return (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) + 3;
   }
   public RetCode cdlConcealBabysWall( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double []ShadowVeryShortPeriodTotal = new double[4] ;
      int i, outIdx, totIdx, ShadowVeryShortTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlConcealBabysWallLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      ShadowVeryShortPeriodTotal[3] = 0;
      ShadowVeryShortPeriodTotal[2] = 0;
      ShadowVeryShortPeriodTotal[1] = 0;
      ShadowVeryShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ;
      i = ShadowVeryShortTrailingIdx;
      while( i < startIdx ) {
         ShadowVeryShortPeriodTotal[3] += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-3] - inOpen[i-3] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-3] - inLow[i-3] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-3] - ( inClose[i-3] >= inOpen[i-3] ? inClose[i-3] : inOpen[i-3] ) ) + ( ( inClose[i-3] >= inOpen[i-3] ? inOpen[i-3] : inClose[i-3] ) - inLow[i-3] ) : 0 ) ) ) ;
         ShadowVeryShortPeriodTotal[2] += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ;
         ShadowVeryShortPeriodTotal[1] += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( inClose[i-3] >= inOpen[i-3] ? 1 : -1 ) == -1 &&
            ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) == -1 &&
            ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == -1 &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == -1 &&
            ( ( inClose[i-3] >= inOpen[i-3] ? inOpen[i-3] : inClose[i-3] ) - inLow[i-3] ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal[3] / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-3] - inOpen[i-3] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-3] - inLow[i-3] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-3] - ( inClose[i-3] >= inOpen[i-3] ? inClose[i-3] : inOpen[i-3] ) ) + ( ( inClose[i-3] >= inOpen[i-3] ? inOpen[i-3] : inClose[i-3] ) - inLow[i-3] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inHigh[i-3] - ( inClose[i-3] >= inOpen[i-3] ? inClose[i-3] : inOpen[i-3] ) ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal[3] / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-3] - inOpen[i-3] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-3] - inLow[i-3] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-3] - ( inClose[i-3] >= inOpen[i-3] ? inClose[i-3] : inOpen[i-3] ) ) + ( ( inClose[i-3] >= inOpen[i-3] ? inOpen[i-3] : inClose[i-3] ) - inLow[i-3] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal[2] / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal[2] / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( (((inOpen[i-1]) > (inClose[i-1])) ? (inOpen[i-1]) : (inClose[i-1])) < (((inOpen[i-2]) < (inClose[i-2])) ? (inOpen[i-2]) : (inClose[i-2])) ) &&
            ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) > ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal[1] / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            inHigh[i-1] > inClose[i-2] &&
            inHigh[i] > inHigh[i-1] && inLow[i] < inLow[i-1]
            )
            outInteger[outIdx++] = 100;
         else
            outInteger[outIdx++] = 0;
         for (totIdx = 3; totIdx >= 1; --totIdx)
            ShadowVeryShortPeriodTotal[totIdx] += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-totIdx] - inOpen[i-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-totIdx] - inLow[i-totIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-totIdx] - ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inClose[i-totIdx] : inOpen[i-totIdx] ) ) + ( ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inOpen[i-totIdx] : inClose[i-totIdx] ) - inLow[i-totIdx] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowVeryShortTrailingIdx-totIdx] - inOpen[ShadowVeryShortTrailingIdx-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowVeryShortTrailingIdx-totIdx] - inLow[ShadowVeryShortTrailingIdx-totIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowVeryShortTrailingIdx-totIdx] - ( inClose[ShadowVeryShortTrailingIdx-totIdx] >= inOpen[ShadowVeryShortTrailingIdx-totIdx] ? inClose[ShadowVeryShortTrailingIdx-totIdx] : inOpen[ShadowVeryShortTrailingIdx-totIdx] ) ) + ( ( inClose[ShadowVeryShortTrailingIdx-totIdx] >= inOpen[ShadowVeryShortTrailingIdx-totIdx] ? inOpen[ShadowVeryShortTrailingIdx-totIdx] : inClose[ShadowVeryShortTrailingIdx-totIdx] ) - inLow[ShadowVeryShortTrailingIdx-totIdx] ) : 0 ) ) ) ;
         i++;
         ShadowVeryShortTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlCounterAttackLookback( )
   {
      return ((( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) +
         1;
   }
   public RetCode cdlCounterAttack( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double EqualPeriodTotal;
      double []BodyLongPeriodTotal = new double[2] ;
      int i, outIdx, totIdx, EqualTrailingIdx, BodyLongTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlCounterAttackLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      EqualPeriodTotal = 0;
      EqualTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) ;
      BodyLongPeriodTotal[1] = 0;
      BodyLongPeriodTotal[0] = 0;
      BodyLongTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      i = EqualTrailingIdx;
      while( i < startIdx ) {
         EqualPeriodTotal += ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         i++;
      }
      i = BodyLongTrailingIdx;
      while( i < startIdx ) {
         BodyLongPeriodTotal[1] += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         BodyLongPeriodTotal[0] += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == - ( inClose[i] >= inOpen[i] ? 1 : -1 ) &&
            ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal[1] / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i] - inOpen[i] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal[0] / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            inClose[i] <= inClose[i-1] + ( (this.candleSettings[CandleSettingType.Equal.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) != 0.0? EqualPeriodTotal / (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            inClose[i] >= inClose[i-1] - ( (this.candleSettings[CandleSettingType.Equal.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) != 0.0? EqualPeriodTotal / (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            )
            outInteger[outIdx++] = ( inClose[i] >= inOpen[i] ? 1 : -1 ) * 100;
         else
            outInteger[outIdx++] = 0;
         EqualPeriodTotal += ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[EqualTrailingIdx-1] - inOpen[EqualTrailingIdx-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[EqualTrailingIdx-1] - inLow[EqualTrailingIdx-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[EqualTrailingIdx-1] - ( inClose[EqualTrailingIdx-1] >= inOpen[EqualTrailingIdx-1] ? inClose[EqualTrailingIdx-1] : inOpen[EqualTrailingIdx-1] ) ) + ( ( inClose[EqualTrailingIdx-1] >= inOpen[EqualTrailingIdx-1] ? inOpen[EqualTrailingIdx-1] : inClose[EqualTrailingIdx-1] ) - inLow[EqualTrailingIdx-1] ) : 0 ) ) ) ;
         for (totIdx = 1; totIdx >= 0; --totIdx)
            BodyLongPeriodTotal[totIdx] += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-totIdx] - inOpen[i-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-totIdx] - inLow[i-totIdx] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-totIdx] - ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inClose[i-totIdx] : inOpen[i-totIdx] ) ) + ( ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inOpen[i-totIdx] : inClose[i-totIdx] ) - inLow[i-totIdx] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx-totIdx] - inOpen[BodyLongTrailingIdx-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx-totIdx] - inLow[BodyLongTrailingIdx-totIdx] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx-totIdx] - ( inClose[BodyLongTrailingIdx-totIdx] >= inOpen[BodyLongTrailingIdx-totIdx] ? inClose[BodyLongTrailingIdx-totIdx] : inOpen[BodyLongTrailingIdx-totIdx] ) ) + ( ( inClose[BodyLongTrailingIdx-totIdx] >= inOpen[BodyLongTrailingIdx-totIdx] ? inOpen[BodyLongTrailingIdx-totIdx] : inClose[BodyLongTrailingIdx-totIdx] ) - inLow[BodyLongTrailingIdx-totIdx] ) : 0 ) ) ) ;
         i++;
         EqualTrailingIdx++;
         BodyLongTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlDarkCloudCoverLookback( double optInPenetration )
   {
      if( optInPenetration == (-4e+37) )
         optInPenetration = 5.000000e-1;
      else if( (optInPenetration < 0.000000e+0) || (optInPenetration > 3.000000e+37) )
         return -1;
      return (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) + 1;
   }
   public RetCode cdlDarkCloudCover( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      double optInPenetration,
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyLongPeriodTotal;
      int i, outIdx, BodyLongTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( optInPenetration == (-4e+37) )
         optInPenetration = 5.000000e-1;
      else if( (optInPenetration < 0.000000e+0) || (optInPenetration > 3.000000e+37) )
         return RetCode.BadParam ;
      lookbackTotal = cdlDarkCloudCoverLookback (optInPenetration);
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyLongPeriodTotal = 0;
      BodyLongTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      i = BodyLongTrailingIdx;
      while( i < startIdx ) {
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == 1 &&
            ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == -1 &&
            inOpen[i] > inHigh[i-1] &&
            inClose[i] > inOpen[i-1] &&
            inClose[i] < inClose[i-1] - ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) * optInPenetration
            )
            outInteger[outIdx++] = -100;
         else
            outInteger[outIdx++] = 0;
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx-1] - inOpen[BodyLongTrailingIdx-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx-1] - inLow[BodyLongTrailingIdx-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx-1] - ( inClose[BodyLongTrailingIdx-1] >= inOpen[BodyLongTrailingIdx-1] ? inClose[BodyLongTrailingIdx-1] : inOpen[BodyLongTrailingIdx-1] ) ) + ( ( inClose[BodyLongTrailingIdx-1] >= inOpen[BodyLongTrailingIdx-1] ? inOpen[BodyLongTrailingIdx-1] : inClose[BodyLongTrailingIdx-1] ) - inLow[BodyLongTrailingIdx-1] ) : 0 ) ) ) ;
         i++;
         BodyLongTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlDojiLookback( )
   {
      return (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ;
   }
   public RetCode cdlDoji( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyDojiPeriodTotal;
      int i, outIdx, BodyDojiTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlDojiLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyDojiPeriodTotal = 0;
      BodyDojiTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ;
      i = BodyDojiTrailingIdx;
      while( i < startIdx ) {
         BodyDojiPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      outIdx = 0;
      do
      {
         if( ( Math.abs ( inClose[i] - inOpen[i] ) ) <= ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) != 0.0? BodyDojiPeriodTotal / (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) )
            outInteger[outIdx++] = 100;
         else
            outInteger[outIdx++] = 0;
         BodyDojiPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyDojiTrailingIdx] - inOpen[BodyDojiTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyDojiTrailingIdx] - inLow[BodyDojiTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyDojiTrailingIdx] - ( inClose[BodyDojiTrailingIdx] >= inOpen[BodyDojiTrailingIdx] ? inClose[BodyDojiTrailingIdx] : inOpen[BodyDojiTrailingIdx] ) ) + ( ( inClose[BodyDojiTrailingIdx] >= inOpen[BodyDojiTrailingIdx] ? inOpen[BodyDojiTrailingIdx] : inClose[BodyDojiTrailingIdx] ) - inLow[BodyDojiTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyDojiTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlDojiStarLookback( )
   {
      return ((( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) + 1;
   }
   public RetCode cdlDojiStar( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyDojiPeriodTotal, BodyLongPeriodTotal;
      int i, outIdx, BodyDojiTrailingIdx, BodyLongTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlDojiStarLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyLongPeriodTotal = 0;
      BodyDojiPeriodTotal = 0;
      BodyLongTrailingIdx = startIdx -1 - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      BodyDojiTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ;
      i = BodyLongTrailingIdx;
      while( i < startIdx-1 ) {
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = BodyDojiTrailingIdx;
      while( i < startIdx ) {
         BodyDojiPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      outIdx = 0;
      do
      {
         if( ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i] - inOpen[i] ) ) <= ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) != 0.0? BodyDojiPeriodTotal / (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( ( ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == 1 && ( (((inOpen[i]) < (inClose[i])) ? (inOpen[i]) : (inClose[i])) > (((inOpen[i-1]) > (inClose[i-1])) ? (inOpen[i-1]) : (inClose[i-1])) ) )
            ||
            ( ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == -1 && ( (((inOpen[i]) > (inClose[i])) ? (inOpen[i]) : (inClose[i])) < (((inOpen[i-1]) < (inClose[i-1])) ? (inOpen[i-1]) : (inClose[i-1])) ) )
            ) )
            outInteger[outIdx++] = - ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) * 100;
         else
            outInteger[outIdx++] = 0;
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx] - inOpen[BodyLongTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx] - inLow[BodyLongTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx] - ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inClose[BodyLongTrailingIdx] : inOpen[BodyLongTrailingIdx] ) ) + ( ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inOpen[BodyLongTrailingIdx] : inClose[BodyLongTrailingIdx] ) - inLow[BodyLongTrailingIdx] ) : 0 ) ) ) ;
         BodyDojiPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyDojiTrailingIdx] - inOpen[BodyDojiTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyDojiTrailingIdx] - inLow[BodyDojiTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyDojiTrailingIdx] - ( inClose[BodyDojiTrailingIdx] >= inOpen[BodyDojiTrailingIdx] ? inClose[BodyDojiTrailingIdx] : inOpen[BodyDojiTrailingIdx] ) ) + ( ( inClose[BodyDojiTrailingIdx] >= inOpen[BodyDojiTrailingIdx] ? inOpen[BodyDojiTrailingIdx] : inClose[BodyDojiTrailingIdx] ) - inLow[BodyDojiTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyLongTrailingIdx++;
         BodyDojiTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlDragonflyDojiLookback( )
   {
      return ((( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) )) ;
   }
   public RetCode cdlDragonflyDoji( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyDojiPeriodTotal, ShadowVeryShortPeriodTotal;
      int i, outIdx, BodyDojiTrailingIdx, ShadowVeryShortTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlDragonflyDojiLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyDojiPeriodTotal = 0;
      BodyDojiTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ;
      ShadowVeryShortPeriodTotal = 0;
      ShadowVeryShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ;
      i = BodyDojiTrailingIdx;
      while( i < startIdx ) {
         BodyDojiPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = ShadowVeryShortTrailingIdx;
      while( i < startIdx ) {
         ShadowVeryShortPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      outIdx = 0;
      do
      {
         if( ( Math.abs ( inClose[i] - inOpen[i] ) ) <= ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) != 0.0? BodyDojiPeriodTotal / (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) > ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            )
            outInteger[outIdx++] = 100;
         else
            outInteger[outIdx++] = 0;
         BodyDojiPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyDojiTrailingIdx] - inOpen[BodyDojiTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyDojiTrailingIdx] - inLow[BodyDojiTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyDojiTrailingIdx] - ( inClose[BodyDojiTrailingIdx] >= inOpen[BodyDojiTrailingIdx] ? inClose[BodyDojiTrailingIdx] : inOpen[BodyDojiTrailingIdx] ) ) + ( ( inClose[BodyDojiTrailingIdx] >= inOpen[BodyDojiTrailingIdx] ? inOpen[BodyDojiTrailingIdx] : inClose[BodyDojiTrailingIdx] ) - inLow[BodyDojiTrailingIdx] ) : 0 ) ) ) ;
         ShadowVeryShortPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowVeryShortTrailingIdx] - inOpen[ShadowVeryShortTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowVeryShortTrailingIdx] - inLow[ShadowVeryShortTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowVeryShortTrailingIdx] - ( inClose[ShadowVeryShortTrailingIdx] >= inOpen[ShadowVeryShortTrailingIdx] ? inClose[ShadowVeryShortTrailingIdx] : inOpen[ShadowVeryShortTrailingIdx] ) ) + ( ( inClose[ShadowVeryShortTrailingIdx] >= inOpen[ShadowVeryShortTrailingIdx] ? inOpen[ShadowVeryShortTrailingIdx] : inClose[ShadowVeryShortTrailingIdx] ) - inLow[ShadowVeryShortTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyDojiTrailingIdx++;
         ShadowVeryShortTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlEngulfingLookback( )
   {
      return 2;
   }
   public RetCode cdlEngulfing( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      int i, outIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlEngulfingLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( ( inClose[i] >= inOpen[i] ? 1 : -1 ) == 1 && ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == -1 &&
            inClose[i] > inOpen[i-1] && inOpen[i] < inClose[i-1]
            )
            ||
            ( ( inClose[i] >= inOpen[i] ? 1 : -1 ) == -1 && ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == 1 &&
            inOpen[i] > inClose[i-1] && inClose[i] < inOpen[i-1]
            )
            )
            outInteger[outIdx++] = ( inClose[i] >= inOpen[i] ? 1 : -1 ) * 100;
         else
            outInteger[outIdx++] = 0;
         i++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlEveningDojiStarLookback( double optInPenetration )
   {
      if( optInPenetration == (-4e+37) )
         optInPenetration = 3.000000e-1;
      else if( (optInPenetration < 0.000000e+0) || (optInPenetration > 3.000000e+37) )
         return -1;
      return ((( ((( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ) > ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) )) ? ( ((( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) )) +
         2;
   }
   public RetCode cdlEveningDojiStar( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      double optInPenetration,
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyDojiPeriodTotal, BodyLongPeriodTotal, BodyShortPeriodTotal;
      int i, outIdx, BodyDojiTrailingIdx, BodyLongTrailingIdx, BodyShortTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( optInPenetration == (-4e+37) )
         optInPenetration = 3.000000e-1;
      else if( (optInPenetration < 0.000000e+0) || (optInPenetration > 3.000000e+37) )
         return RetCode.BadParam ;
      lookbackTotal = cdlEveningDojiStarLookback (optInPenetration);
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyLongPeriodTotal = 0;
      BodyDojiPeriodTotal = 0;
      BodyShortPeriodTotal = 0;
      BodyLongTrailingIdx = startIdx -2 - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      BodyDojiTrailingIdx = startIdx -1 - (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ;
      BodyShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ;
      i = BodyLongTrailingIdx;
      while( i < startIdx-2 ) {
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = BodyDojiTrailingIdx;
      while( i < startIdx-1 ) {
         BodyDojiPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = BodyShortTrailingIdx;
      while( i < startIdx ) {
         BodyShortPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) == 1 &&
            ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) <= ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) != 0.0? BodyDojiPeriodTotal / (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( (((inOpen[i-1]) < (inClose[i-1])) ? (inOpen[i-1]) : (inClose[i-1])) > (((inOpen[i-2]) > (inClose[i-2])) ? (inOpen[i-2]) : (inClose[i-2])) ) &&
            ( Math.abs ( inClose[i] - inOpen[i] ) ) > ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) != 0.0? BodyShortPeriodTotal / (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == -1 &&
            inClose[i] < inClose[i-2] - ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) * optInPenetration
            )
            outInteger[outIdx++] = -100;
         else
            outInteger[outIdx++] = 0;
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx] - inOpen[BodyLongTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx] - inLow[BodyLongTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx] - ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inClose[BodyLongTrailingIdx] : inOpen[BodyLongTrailingIdx] ) ) + ( ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inOpen[BodyLongTrailingIdx] : inClose[BodyLongTrailingIdx] ) - inLow[BodyLongTrailingIdx] ) : 0 ) ) ) ;
         BodyDojiPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyDojiTrailingIdx] - inOpen[BodyDojiTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyDojiTrailingIdx] - inLow[BodyDojiTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyDojiTrailingIdx] - ( inClose[BodyDojiTrailingIdx] >= inOpen[BodyDojiTrailingIdx] ? inClose[BodyDojiTrailingIdx] : inOpen[BodyDojiTrailingIdx] ) ) + ( ( inClose[BodyDojiTrailingIdx] >= inOpen[BodyDojiTrailingIdx] ? inOpen[BodyDojiTrailingIdx] : inClose[BodyDojiTrailingIdx] ) - inLow[BodyDojiTrailingIdx] ) : 0 ) ) ) ;
         BodyShortPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyShortTrailingIdx] - inOpen[BodyShortTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyShortTrailingIdx] - inLow[BodyShortTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyShortTrailingIdx] - ( inClose[BodyShortTrailingIdx] >= inOpen[BodyShortTrailingIdx] ? inClose[BodyShortTrailingIdx] : inOpen[BodyShortTrailingIdx] ) ) + ( ( inClose[BodyShortTrailingIdx] >= inOpen[BodyShortTrailingIdx] ? inOpen[BodyShortTrailingIdx] : inClose[BodyShortTrailingIdx] ) - inLow[BodyShortTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyLongTrailingIdx++;
         BodyDojiTrailingIdx++;
         BodyShortTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlEveningStarLookback( double optInPenetration )
   {
      if( optInPenetration == (-4e+37) )
         optInPenetration = 3.000000e-1;
      else if( (optInPenetration < 0.000000e+0) || (optInPenetration > 3.000000e+37) )
         return -1;
      return ((( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) + 2;
   }
   public RetCode cdlEveningStar( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      double optInPenetration,
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyShortPeriodTotal, BodyLongPeriodTotal, BodyShortPeriodTotal2;
      int i, outIdx, BodyShortTrailingIdx, BodyLongTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( optInPenetration == (-4e+37) )
         optInPenetration = 3.000000e-1;
      else if( (optInPenetration < 0.000000e+0) || (optInPenetration > 3.000000e+37) )
         return RetCode.BadParam ;
      lookbackTotal = cdlEveningStarLookback (optInPenetration);
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyLongPeriodTotal = 0;
      BodyShortPeriodTotal = 0;
      BodyShortPeriodTotal2 = 0;
      BodyLongTrailingIdx = startIdx -2 - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      BodyShortTrailingIdx = startIdx -1 - (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ;
      i = BodyLongTrailingIdx;
      while( i < startIdx-2 ) {
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = BodyShortTrailingIdx;
      while( i < startIdx-1 ) {
         BodyShortPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         BodyShortPeriodTotal2 += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i+1] - inOpen[i+1] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i+1] - inLow[i+1] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i+1] - ( inClose[i+1] >= inOpen[i+1] ? inClose[i+1] : inOpen[i+1] ) ) + ( ( inClose[i+1] >= inOpen[i+1] ? inOpen[i+1] : inClose[i+1] ) - inLow[i+1] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) == 1 &&
            ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) <= ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) != 0.0? BodyShortPeriodTotal / (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( (((inOpen[i-1]) < (inClose[i-1])) ? (inOpen[i-1]) : (inClose[i-1])) > (((inOpen[i-2]) > (inClose[i-2])) ? (inOpen[i-2]) : (inClose[i-2])) ) &&
            ( Math.abs ( inClose[i] - inOpen[i] ) ) > ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) != 0.0? BodyShortPeriodTotal2 / (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == -1 &&
            inClose[i] < inClose[i-2] - ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) * optInPenetration
            )
            outInteger[outIdx++] = -100;
         else
            outInteger[outIdx++] = 0;
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx] - inOpen[BodyLongTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx] - inLow[BodyLongTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx] - ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inClose[BodyLongTrailingIdx] : inOpen[BodyLongTrailingIdx] ) ) + ( ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inOpen[BodyLongTrailingIdx] : inClose[BodyLongTrailingIdx] ) - inLow[BodyLongTrailingIdx] ) : 0 ) ) ) ;
         BodyShortPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyShortTrailingIdx] - inOpen[BodyShortTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyShortTrailingIdx] - inLow[BodyShortTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyShortTrailingIdx] - ( inClose[BodyShortTrailingIdx] >= inOpen[BodyShortTrailingIdx] ? inClose[BodyShortTrailingIdx] : inOpen[BodyShortTrailingIdx] ) ) + ( ( inClose[BodyShortTrailingIdx] >= inOpen[BodyShortTrailingIdx] ? inOpen[BodyShortTrailingIdx] : inClose[BodyShortTrailingIdx] ) - inLow[BodyShortTrailingIdx] ) : 0 ) ) ) ;
         BodyShortPeriodTotal2 += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyShortTrailingIdx+1] - inOpen[BodyShortTrailingIdx+1] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyShortTrailingIdx+1] - inLow[BodyShortTrailingIdx+1] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyShortTrailingIdx+1] - ( inClose[BodyShortTrailingIdx+1] >= inOpen[BodyShortTrailingIdx+1] ? inClose[BodyShortTrailingIdx+1] : inOpen[BodyShortTrailingIdx+1] ) ) + ( ( inClose[BodyShortTrailingIdx+1] >= inOpen[BodyShortTrailingIdx+1] ? inOpen[BodyShortTrailingIdx+1] : inClose[BodyShortTrailingIdx+1] ) - inLow[BodyShortTrailingIdx+1] ) : 0 ) ) ) ;
         i++;
         BodyLongTrailingIdx++;
         BodyShortTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlGapSideSideWhiteLookback( )
   {
      return ((( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) )) + 2;
   }
   public RetCode cdlGapSideSideWhite( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double NearPeriodTotal, EqualPeriodTotal;
      int i, outIdx, NearTrailingIdx, EqualTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlGapSideSideWhiteLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      NearPeriodTotal = 0;
      EqualPeriodTotal = 0;
      NearTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) ;
      EqualTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) ;
      i = NearTrailingIdx;
      while( i < startIdx ) {
         NearPeriodTotal += ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         i++;
      }
      i = EqualTrailingIdx;
      while( i < startIdx ) {
         EqualPeriodTotal += ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if(
            (
            ( ( (((inOpen[i-1]) < (inClose[i-1])) ? (inOpen[i-1]) : (inClose[i-1])) > (((inOpen[i-2]) > (inClose[i-2])) ? (inOpen[i-2]) : (inClose[i-2])) ) && ( (((inOpen[i]) < (inClose[i])) ? (inOpen[i]) : (inClose[i])) > (((inOpen[i-2]) > (inClose[i-2])) ? (inOpen[i-2]) : (inClose[i-2])) ) )
            ||
            ( ( (((inOpen[i-1]) > (inClose[i-1])) ? (inOpen[i-1]) : (inClose[i-1])) < (((inOpen[i-2]) < (inClose[i-2])) ? (inOpen[i-2]) : (inClose[i-2])) ) && ( (((inOpen[i]) > (inClose[i])) ? (inOpen[i]) : (inClose[i])) < (((inOpen[i-2]) < (inClose[i-2])) ? (inOpen[i-2]) : (inClose[i-2])) ) )
            ) &&
            ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == 1 &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == 1 &&
            ( Math.abs ( inClose[i] - inOpen[i] ) ) >= ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) - ( (this.candleSettings[CandleSettingType.Near.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) != 0.0? NearPeriodTotal / (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i] - inOpen[i] ) ) <= ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) + ( (this.candleSettings[CandleSettingType.Near.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) != 0.0? NearPeriodTotal / (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            inOpen[i] >= inOpen[i-1] - ( (this.candleSettings[CandleSettingType.Equal.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) != 0.0? EqualPeriodTotal / (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            inOpen[i] <= inOpen[i-1] + ( (this.candleSettings[CandleSettingType.Equal.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) != 0.0? EqualPeriodTotal / (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            )
            outInteger[outIdx++] = ( ( (((inOpen[i-1]) < (inClose[i-1])) ? (inOpen[i-1]) : (inClose[i-1])) > (((inOpen[i-2]) > (inClose[i-2])) ? (inOpen[i-2]) : (inClose[i-2])) ) ? 100 : -100 );
         else
            outInteger[outIdx++] = 0;
         NearPeriodTotal += ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[NearTrailingIdx-1] - inOpen[NearTrailingIdx-1] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[NearTrailingIdx-1] - inLow[NearTrailingIdx-1] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[NearTrailingIdx-1] - ( inClose[NearTrailingIdx-1] >= inOpen[NearTrailingIdx-1] ? inClose[NearTrailingIdx-1] : inOpen[NearTrailingIdx-1] ) ) + ( ( inClose[NearTrailingIdx-1] >= inOpen[NearTrailingIdx-1] ? inOpen[NearTrailingIdx-1] : inClose[NearTrailingIdx-1] ) - inLow[NearTrailingIdx-1] ) : 0 ) ) ) ;
         EqualPeriodTotal += ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[EqualTrailingIdx-1] - inOpen[EqualTrailingIdx-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[EqualTrailingIdx-1] - inLow[EqualTrailingIdx-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[EqualTrailingIdx-1] - ( inClose[EqualTrailingIdx-1] >= inOpen[EqualTrailingIdx-1] ? inClose[EqualTrailingIdx-1] : inOpen[EqualTrailingIdx-1] ) ) + ( ( inClose[EqualTrailingIdx-1] >= inOpen[EqualTrailingIdx-1] ? inOpen[EqualTrailingIdx-1] : inClose[EqualTrailingIdx-1] ) - inLow[EqualTrailingIdx-1] ) : 0 ) ) ) ;
         i++;
         NearTrailingIdx++;
         EqualTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlGravestoneDojiLookback( )
   {
      return ((( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) )) ;
   }
   public RetCode cdlGravestoneDoji( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyDojiPeriodTotal, ShadowVeryShortPeriodTotal;
      int i, outIdx, BodyDojiTrailingIdx, ShadowVeryShortTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlGravestoneDojiLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyDojiPeriodTotal = 0;
      BodyDojiTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ;
      ShadowVeryShortPeriodTotal = 0;
      ShadowVeryShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ;
      i = BodyDojiTrailingIdx;
      while( i < startIdx ) {
         BodyDojiPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = ShadowVeryShortTrailingIdx;
      while( i < startIdx ) {
         ShadowVeryShortPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      outIdx = 0;
      do
      {
         if( ( Math.abs ( inClose[i] - inOpen[i] ) ) <= ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) != 0.0? BodyDojiPeriodTotal / (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) > ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            )
            outInteger[outIdx++] = 100;
         else
            outInteger[outIdx++] = 0;
         BodyDojiPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyDojiTrailingIdx] - inOpen[BodyDojiTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyDojiTrailingIdx] - inLow[BodyDojiTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyDojiTrailingIdx] - ( inClose[BodyDojiTrailingIdx] >= inOpen[BodyDojiTrailingIdx] ? inClose[BodyDojiTrailingIdx] : inOpen[BodyDojiTrailingIdx] ) ) + ( ( inClose[BodyDojiTrailingIdx] >= inOpen[BodyDojiTrailingIdx] ? inOpen[BodyDojiTrailingIdx] : inClose[BodyDojiTrailingIdx] ) - inLow[BodyDojiTrailingIdx] ) : 0 ) ) ) ;
         ShadowVeryShortPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowVeryShortTrailingIdx] - inOpen[ShadowVeryShortTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowVeryShortTrailingIdx] - inLow[ShadowVeryShortTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowVeryShortTrailingIdx] - ( inClose[ShadowVeryShortTrailingIdx] >= inOpen[ShadowVeryShortTrailingIdx] ? inClose[ShadowVeryShortTrailingIdx] : inOpen[ShadowVeryShortTrailingIdx] ) ) + ( ( inClose[ShadowVeryShortTrailingIdx] >= inOpen[ShadowVeryShortTrailingIdx] ? inOpen[ShadowVeryShortTrailingIdx] : inClose[ShadowVeryShortTrailingIdx] ) - inLow[ShadowVeryShortTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyDojiTrailingIdx++;
         ShadowVeryShortTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlHammerLookback( )
   {
      return ((( ((( ((( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ) > ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) )) ? ( ((( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) )) ) > ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) )) ? ( ((( ((( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ) > ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) )) ? ( ((( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) )) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) )) +
         1;
   }
   public RetCode cdlHammer( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyPeriodTotal, ShadowLongPeriodTotal, ShadowVeryShortPeriodTotal, NearPeriodTotal;
      int i, outIdx, BodyTrailingIdx, ShadowLongTrailingIdx, ShadowVeryShortTrailingIdx, NearTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlHammerLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyPeriodTotal = 0;
      BodyTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ;
      ShadowLongPeriodTotal = 0;
      ShadowLongTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) ;
      ShadowVeryShortPeriodTotal = 0;
      ShadowVeryShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ;
      NearPeriodTotal = 0;
      NearTrailingIdx = startIdx -1 - (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) ;
      i = BodyTrailingIdx;
      while( i < startIdx ) {
         BodyPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = ShadowLongTrailingIdx;
      while( i < startIdx ) {
         ShadowLongPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = ShadowVeryShortTrailingIdx;
      while( i < startIdx ) {
         ShadowVeryShortPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = NearTrailingIdx;
      while( i < startIdx-1 ) {
         NearPeriodTotal += ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( Math.abs ( inClose[i] - inOpen[i] ) ) < ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) != 0.0? BodyPeriodTotal / (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) > ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) != 0.0? ShadowLongPeriodTotal / (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            (((inClose[i]) < (inOpen[i])) ? (inClose[i]) : (inOpen[i])) <= inLow[i-1] + ( (this.candleSettings[CandleSettingType.Near.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) != 0.0? NearPeriodTotal / (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            )
            outInteger[outIdx++] = 100;
         else
            outInteger[outIdx++] = 0;
         BodyPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyTrailingIdx] - inOpen[BodyTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyTrailingIdx] - inLow[BodyTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyTrailingIdx] - ( inClose[BodyTrailingIdx] >= inOpen[BodyTrailingIdx] ? inClose[BodyTrailingIdx] : inOpen[BodyTrailingIdx] ) ) + ( ( inClose[BodyTrailingIdx] >= inOpen[BodyTrailingIdx] ? inOpen[BodyTrailingIdx] : inClose[BodyTrailingIdx] ) - inLow[BodyTrailingIdx] ) : 0 ) ) ) ;
         ShadowLongPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowLongTrailingIdx] - inOpen[ShadowLongTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowLongTrailingIdx] - inLow[ShadowLongTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowLongTrailingIdx] - ( inClose[ShadowLongTrailingIdx] >= inOpen[ShadowLongTrailingIdx] ? inClose[ShadowLongTrailingIdx] : inOpen[ShadowLongTrailingIdx] ) ) + ( ( inClose[ShadowLongTrailingIdx] >= inOpen[ShadowLongTrailingIdx] ? inOpen[ShadowLongTrailingIdx] : inClose[ShadowLongTrailingIdx] ) - inLow[ShadowLongTrailingIdx] ) : 0 ) ) ) ;
         ShadowVeryShortPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowVeryShortTrailingIdx] - inOpen[ShadowVeryShortTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowVeryShortTrailingIdx] - inLow[ShadowVeryShortTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowVeryShortTrailingIdx] - ( inClose[ShadowVeryShortTrailingIdx] >= inOpen[ShadowVeryShortTrailingIdx] ? inClose[ShadowVeryShortTrailingIdx] : inOpen[ShadowVeryShortTrailingIdx] ) ) + ( ( inClose[ShadowVeryShortTrailingIdx] >= inOpen[ShadowVeryShortTrailingIdx] ? inOpen[ShadowVeryShortTrailingIdx] : inClose[ShadowVeryShortTrailingIdx] ) - inLow[ShadowVeryShortTrailingIdx] ) : 0 ) ) ) ;
         NearPeriodTotal += ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[NearTrailingIdx] - inOpen[NearTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[NearTrailingIdx] - inLow[NearTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[NearTrailingIdx] - ( inClose[NearTrailingIdx] >= inOpen[NearTrailingIdx] ? inClose[NearTrailingIdx] : inOpen[NearTrailingIdx] ) ) + ( ( inClose[NearTrailingIdx] >= inOpen[NearTrailingIdx] ? inOpen[NearTrailingIdx] : inClose[NearTrailingIdx] ) - inLow[NearTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyTrailingIdx++;
         ShadowLongTrailingIdx++;
         ShadowVeryShortTrailingIdx++;
         NearTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlHangingManLookback( )
   {
      return ((( ((( ((( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ) > ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) )) ? ( ((( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) )) ) > ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) )) ? ( ((( ((( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ) > ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) )) ? ( ((( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) )) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) )) +
         1;
   }
   public RetCode cdlHangingMan( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyPeriodTotal, ShadowLongPeriodTotal, ShadowVeryShortPeriodTotal, NearPeriodTotal;
      int i, outIdx, BodyTrailingIdx, ShadowLongTrailingIdx, ShadowVeryShortTrailingIdx, NearTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlHangingManLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyPeriodTotal = 0;
      BodyTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ;
      ShadowLongPeriodTotal = 0;
      ShadowLongTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) ;
      ShadowVeryShortPeriodTotal = 0;
      ShadowVeryShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ;
      NearPeriodTotal = 0;
      NearTrailingIdx = startIdx -1 - (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) ;
      i = BodyTrailingIdx;
      while( i < startIdx ) {
         BodyPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = ShadowLongTrailingIdx;
      while( i < startIdx ) {
         ShadowLongPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = ShadowVeryShortTrailingIdx;
      while( i < startIdx ) {
         ShadowVeryShortPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = NearTrailingIdx;
      while( i < startIdx-1 ) {
         NearPeriodTotal += ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( Math.abs ( inClose[i] - inOpen[i] ) ) < ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) != 0.0? BodyPeriodTotal / (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) > ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) != 0.0? ShadowLongPeriodTotal / (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            (((inClose[i]) < (inOpen[i])) ? (inClose[i]) : (inOpen[i])) >= inHigh[i-1] - ( (this.candleSettings[CandleSettingType.Near.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) != 0.0? NearPeriodTotal / (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            )
            outInteger[outIdx++] = -100;
         else
            outInteger[outIdx++] = 0;
         BodyPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyTrailingIdx] - inOpen[BodyTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyTrailingIdx] - inLow[BodyTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyTrailingIdx] - ( inClose[BodyTrailingIdx] >= inOpen[BodyTrailingIdx] ? inClose[BodyTrailingIdx] : inOpen[BodyTrailingIdx] ) ) + ( ( inClose[BodyTrailingIdx] >= inOpen[BodyTrailingIdx] ? inOpen[BodyTrailingIdx] : inClose[BodyTrailingIdx] ) - inLow[BodyTrailingIdx] ) : 0 ) ) ) ;
         ShadowLongPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowLongTrailingIdx] - inOpen[ShadowLongTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowLongTrailingIdx] - inLow[ShadowLongTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowLongTrailingIdx] - ( inClose[ShadowLongTrailingIdx] >= inOpen[ShadowLongTrailingIdx] ? inClose[ShadowLongTrailingIdx] : inOpen[ShadowLongTrailingIdx] ) ) + ( ( inClose[ShadowLongTrailingIdx] >= inOpen[ShadowLongTrailingIdx] ? inOpen[ShadowLongTrailingIdx] : inClose[ShadowLongTrailingIdx] ) - inLow[ShadowLongTrailingIdx] ) : 0 ) ) ) ;
         ShadowVeryShortPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowVeryShortTrailingIdx] - inOpen[ShadowVeryShortTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowVeryShortTrailingIdx] - inLow[ShadowVeryShortTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowVeryShortTrailingIdx] - ( inClose[ShadowVeryShortTrailingIdx] >= inOpen[ShadowVeryShortTrailingIdx] ? inClose[ShadowVeryShortTrailingIdx] : inOpen[ShadowVeryShortTrailingIdx] ) ) + ( ( inClose[ShadowVeryShortTrailingIdx] >= inOpen[ShadowVeryShortTrailingIdx] ? inOpen[ShadowVeryShortTrailingIdx] : inClose[ShadowVeryShortTrailingIdx] ) - inLow[ShadowVeryShortTrailingIdx] ) : 0 ) ) ) ;
         NearPeriodTotal += ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[NearTrailingIdx] - inOpen[NearTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[NearTrailingIdx] - inLow[NearTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[NearTrailingIdx] - ( inClose[NearTrailingIdx] >= inOpen[NearTrailingIdx] ? inClose[NearTrailingIdx] : inOpen[NearTrailingIdx] ) ) + ( ( inClose[NearTrailingIdx] >= inOpen[NearTrailingIdx] ? inOpen[NearTrailingIdx] : inClose[NearTrailingIdx] ) - inLow[NearTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyTrailingIdx++;
         ShadowLongTrailingIdx++;
         ShadowVeryShortTrailingIdx++;
         NearTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlHaramiLookback( )
   {
      return ((( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) + 1;
   }
   public RetCode cdlHarami( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyShortPeriodTotal, BodyLongPeriodTotal;
      int i, outIdx, BodyShortTrailingIdx, BodyLongTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlHaramiLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyLongPeriodTotal = 0;
      BodyShortPeriodTotal = 0;
      BodyLongTrailingIdx = startIdx -1 - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      BodyShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ;
      i = BodyLongTrailingIdx;
      while( i < startIdx-1 ) {
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = BodyShortTrailingIdx;
      while( i < startIdx ) {
         BodyShortPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i] - inOpen[i] ) ) <= ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) != 0.0? BodyShortPeriodTotal / (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            (((inClose[i]) > (inOpen[i])) ? (inClose[i]) : (inOpen[i])) < (((inClose[i-1]) > (inOpen[i-1])) ? (inClose[i-1]) : (inOpen[i-1])) &&
            (((inClose[i]) < (inOpen[i])) ? (inClose[i]) : (inOpen[i])) > (((inClose[i-1]) < (inOpen[i-1])) ? (inClose[i-1]) : (inOpen[i-1]))
            )
            outInteger[outIdx++] = - ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) * 100;
         else
            outInteger[outIdx++] = 0;
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx] - inOpen[BodyLongTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx] - inLow[BodyLongTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx] - ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inClose[BodyLongTrailingIdx] : inOpen[BodyLongTrailingIdx] ) ) + ( ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inOpen[BodyLongTrailingIdx] : inClose[BodyLongTrailingIdx] ) - inLow[BodyLongTrailingIdx] ) : 0 ) ) ) ;
         BodyShortPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyShortTrailingIdx] - inOpen[BodyShortTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyShortTrailingIdx] - inLow[BodyShortTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyShortTrailingIdx] - ( inClose[BodyShortTrailingIdx] >= inOpen[BodyShortTrailingIdx] ? inClose[BodyShortTrailingIdx] : inOpen[BodyShortTrailingIdx] ) ) + ( ( inClose[BodyShortTrailingIdx] >= inOpen[BodyShortTrailingIdx] ? inOpen[BodyShortTrailingIdx] : inClose[BodyShortTrailingIdx] ) - inLow[BodyShortTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyLongTrailingIdx++;
         BodyShortTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlHaramiCrossLookback( )
   {
      return ((( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) + 1;
   }
   public RetCode cdlHaramiCross( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyDojiPeriodTotal, BodyLongPeriodTotal;
      int i, outIdx, BodyDojiTrailingIdx, BodyLongTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlHaramiCrossLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyLongPeriodTotal = 0;
      BodyDojiPeriodTotal = 0;
      BodyLongTrailingIdx = startIdx -1 - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      BodyDojiTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ;
      i = BodyLongTrailingIdx;
      while( i < startIdx-1 ) {
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = BodyDojiTrailingIdx;
      while( i < startIdx ) {
         BodyDojiPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i] - inOpen[i] ) ) <= ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) != 0.0? BodyDojiPeriodTotal / (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            (((inClose[i]) > (inOpen[i])) ? (inClose[i]) : (inOpen[i])) < (((inClose[i-1]) > (inOpen[i-1])) ? (inClose[i-1]) : (inOpen[i-1])) &&
            (((inClose[i]) < (inOpen[i])) ? (inClose[i]) : (inOpen[i])) > (((inClose[i-1]) < (inOpen[i-1])) ? (inClose[i-1]) : (inOpen[i-1]))
            )
            outInteger[outIdx++] = - ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) * 100;
         else
            outInteger[outIdx++] = 0;
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx] - inOpen[BodyLongTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx] - inLow[BodyLongTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx] - ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inClose[BodyLongTrailingIdx] : inOpen[BodyLongTrailingIdx] ) ) + ( ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inOpen[BodyLongTrailingIdx] : inClose[BodyLongTrailingIdx] ) - inLow[BodyLongTrailingIdx] ) : 0 ) ) ) ;
         BodyDojiPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyDojiTrailingIdx] - inOpen[BodyDojiTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyDojiTrailingIdx] - inLow[BodyDojiTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyDojiTrailingIdx] - ( inClose[BodyDojiTrailingIdx] >= inOpen[BodyDojiTrailingIdx] ? inClose[BodyDojiTrailingIdx] : inOpen[BodyDojiTrailingIdx] ) ) + ( ( inClose[BodyDojiTrailingIdx] >= inOpen[BodyDojiTrailingIdx] ? inOpen[BodyDojiTrailingIdx] : inClose[BodyDojiTrailingIdx] ) - inLow[BodyDojiTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyLongTrailingIdx++;
         BodyDojiTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlHignWaveLookback( )
   {
      return ((( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].avgPeriod) )) ;
   }
   public RetCode cdlHignWave( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyPeriodTotal, ShadowPeriodTotal;
      int i, outIdx, BodyTrailingIdx, ShadowTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlHignWaveLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyPeriodTotal = 0;
      BodyTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ;
      ShadowPeriodTotal = 0;
      ShadowTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].avgPeriod) ;
      i = BodyTrailingIdx;
      while( i < startIdx ) {
         BodyPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = ShadowTrailingIdx;
      while( i < startIdx ) {
         ShadowPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      outIdx = 0;
      do
      {
         if( ( Math.abs ( inClose[i] - inOpen[i] ) ) < ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) != 0.0? BodyPeriodTotal / (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) > ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].avgPeriod) != 0.0? ShadowPeriodTotal / (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) > ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].avgPeriod) != 0.0? ShadowPeriodTotal / (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) )
            outInteger[outIdx++] = ( inClose[i] >= inOpen[i] ? 1 : -1 ) * 100;
         else
            outInteger[outIdx++] = 0;
         BodyPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyTrailingIdx] - inOpen[BodyTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyTrailingIdx] - inLow[BodyTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyTrailingIdx] - ( inClose[BodyTrailingIdx] >= inOpen[BodyTrailingIdx] ? inClose[BodyTrailingIdx] : inOpen[BodyTrailingIdx] ) ) + ( ( inClose[BodyTrailingIdx] >= inOpen[BodyTrailingIdx] ? inOpen[BodyTrailingIdx] : inClose[BodyTrailingIdx] ) - inLow[BodyTrailingIdx] ) : 0 ) ) ) ;
         ShadowPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowTrailingIdx] - inOpen[ShadowTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowTrailingIdx] - inLow[ShadowTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowTrailingIdx] - ( inClose[ShadowTrailingIdx] >= inOpen[ShadowTrailingIdx] ? inClose[ShadowTrailingIdx] : inOpen[ShadowTrailingIdx] ) ) + ( ( inClose[ShadowTrailingIdx] >= inOpen[ShadowTrailingIdx] ? inOpen[ShadowTrailingIdx] : inClose[ShadowTrailingIdx] ) - inLow[ShadowTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyTrailingIdx++;
         ShadowTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlHikkakeLookback( )
   {
      return 5;
   }
   public RetCode cdlHikkake( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      int i, outIdx, lookbackTotal, patternIdx, patternResult;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlHikkakeLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      patternIdx = 0;
      patternResult = 0;
      i = startIdx - 3;
      while( i < startIdx ) {
         if( inHigh[i-1] < inHigh[i-2] && inLow[i-1] > inLow[i-2] &&
            ( ( inHigh[i] < inHigh[i-1] && inLow[i] < inLow[i-1] )
            ||
            ( inHigh[i] > inHigh[i-1] && inLow[i] > inLow[i-1] )
            )
            ) {
            patternResult = 100 * ( inHigh[i] < inHigh[i-1] ? 1 : -1 );
            patternIdx = i;
         } else
            if( i <= patternIdx+3 &&
            ( ( patternResult > 0 && inClose[i] > inHigh[patternIdx-1] )
            ||
            ( patternResult < 0 && inClose[i] < inLow[patternIdx-1] )
            )
            )
            patternIdx = 0;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( inHigh[i-1] < inHigh[i-2] && inLow[i-1] > inLow[i-2] &&
            ( ( inHigh[i] < inHigh[i-1] && inLow[i] < inLow[i-1] )
            ||
            ( inHigh[i] > inHigh[i-1] && inLow[i] > inLow[i-1] )
            )
            ) {
            patternResult = 100 * ( inHigh[i] < inHigh[i-1] ? 1 : -1 );
            patternIdx = i;
            outInteger[outIdx++] = patternResult;
         } else
            if( i <= patternIdx+3 &&
            ( ( patternResult > 0 && inClose[i] > inHigh[patternIdx-1] )
            ||
            ( patternResult < 0 && inClose[i] < inLow[patternIdx-1] )
            )
            ) {
            outInteger[outIdx++] = patternResult + 100 * ( patternResult > 0 ? 1 : -1 );
            patternIdx = 0;
         } else
            outInteger[outIdx++] = 0;
         i++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlHikkakeModLookback( )
   {
      return (((1) > ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) )) ? (1) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) )) + 5;
   }
   public RetCode cdlHikkakeMod( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double NearPeriodTotal;
      int i, outIdx, NearTrailingIdx, lookbackTotal, patternIdx, patternResult;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlHikkakeModLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      NearPeriodTotal = 0;
      NearTrailingIdx = startIdx - 3 - (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) ;
      i = NearTrailingIdx;
      while( i < startIdx - 3 ) {
         NearPeriodTotal += ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ;
         i++;
      }
      patternIdx = 0;
      patternResult = 0;
      i = startIdx - 3;
      while( i < startIdx ) {
         if( inHigh[i-2] < inHigh[i-3] && inLow[i-2] > inLow[i-3] &&
            inHigh[i-1] < inHigh[i-2] && inLow[i-1] > inLow[i-2] &&
            ( ( inHigh[i] < inHigh[i-1] && inLow[i] < inLow[i-1] &&
            inClose[i-2] <= inLow[i-2] + ( (this.candleSettings[CandleSettingType.Near.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) != 0.0? NearPeriodTotal / (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            )
            ||
            ( inHigh[i] > inHigh[i-1] && inLow[i] > inLow[i-1] &&
            inClose[i-2] >= inHigh[i-2] - ( (this.candleSettings[CandleSettingType.Near.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) != 0.0? NearPeriodTotal / (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            )
            )
            ) {
            patternResult = 100 * ( inHigh[i] < inHigh[i-1] ? 1 : -1 );
            patternIdx = i;
         } else
            if( i <= patternIdx+3 &&
            ( ( patternResult > 0 && inClose[i] > inHigh[patternIdx-1] )
            ||
            ( patternResult < 0 && inClose[i] < inLow[patternIdx-1] )
            )
            )
            patternIdx = 0;
         NearPeriodTotal += ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[NearTrailingIdx-2] - inOpen[NearTrailingIdx-2] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[NearTrailingIdx-2] - inLow[NearTrailingIdx-2] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[NearTrailingIdx-2] - ( inClose[NearTrailingIdx-2] >= inOpen[NearTrailingIdx-2] ? inClose[NearTrailingIdx-2] : inOpen[NearTrailingIdx-2] ) ) + ( ( inClose[NearTrailingIdx-2] >= inOpen[NearTrailingIdx-2] ? inOpen[NearTrailingIdx-2] : inClose[NearTrailingIdx-2] ) - inLow[NearTrailingIdx-2] ) : 0 ) ) ) ;
         NearTrailingIdx++;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( inHigh[i-2] < inHigh[i-3] && inLow[i-2] > inLow[i-3] &&
            inHigh[i-1] < inHigh[i-2] && inLow[i-1] > inLow[i-2] &&
            ( ( inHigh[i] < inHigh[i-1] && inLow[i] < inLow[i-1] &&
            inClose[i-2] <= inLow[i-2] + ( (this.candleSettings[CandleSettingType.Near.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) != 0.0? NearPeriodTotal / (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            )
            ||
            ( inHigh[i] > inHigh[i-1] && inLow[i] > inLow[i-1] &&
            inClose[i-2] >= inHigh[i-2] - ( (this.candleSettings[CandleSettingType.Near.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) != 0.0? NearPeriodTotal / (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            )
            )
            ) {
            patternResult = 100 * ( inHigh[i] < inHigh[i-1] ? 1 : -1 );
            patternIdx = i;
            outInteger[outIdx++] = patternResult;
         } else
            if( i <= patternIdx+3 &&
            ( ( patternResult > 0 && inClose[i] > inHigh[patternIdx-1] )
            ||
            ( patternResult < 0 && inClose[i] < inLow[patternIdx-1] )
            )
            ) {
            outInteger[outIdx++] = patternResult + 100 * ( patternResult > 0 ? 1 : -1 );
            patternIdx = 0;
         } else
            outInteger[outIdx++] = 0;
         NearPeriodTotal += ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[NearTrailingIdx-2] - inOpen[NearTrailingIdx-2] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[NearTrailingIdx-2] - inLow[NearTrailingIdx-2] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[NearTrailingIdx-2] - ( inClose[NearTrailingIdx-2] >= inOpen[NearTrailingIdx-2] ? inClose[NearTrailingIdx-2] : inOpen[NearTrailingIdx-2] ) ) + ( ( inClose[NearTrailingIdx-2] >= inOpen[NearTrailingIdx-2] ? inOpen[NearTrailingIdx-2] : inClose[NearTrailingIdx-2] ) - inLow[NearTrailingIdx-2] ) : 0 ) ) ) ;
         NearTrailingIdx++;
         i++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlHomingPigeonLookback( )
   {
      return ((( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) + 1;
   }
   public RetCode cdlHomingPigeon( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyShortPeriodTotal, BodyLongPeriodTotal;
      int i, outIdx, BodyShortTrailingIdx, BodyLongTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlHomingPigeonLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyLongPeriodTotal = 0;
      BodyShortPeriodTotal = 0;
      BodyLongTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      BodyShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ;
      i = BodyLongTrailingIdx;
      while( i < startIdx ) {
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         i++;
      }
      i = BodyShortTrailingIdx;
      while( i < startIdx ) {
         BodyShortPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == -1 &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == -1 &&
            ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i] - inOpen[i] ) ) <= ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) != 0.0? BodyShortPeriodTotal / (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            inOpen[i] < inOpen[i-1] &&
            inClose[i] > inClose[i-1]
            )
            outInteger[outIdx++] = 100;
         else
            outInteger[outIdx++] = 0;
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx-1] - inOpen[BodyLongTrailingIdx-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx-1] - inLow[BodyLongTrailingIdx-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx-1] - ( inClose[BodyLongTrailingIdx-1] >= inOpen[BodyLongTrailingIdx-1] ? inClose[BodyLongTrailingIdx-1] : inOpen[BodyLongTrailingIdx-1] ) ) + ( ( inClose[BodyLongTrailingIdx-1] >= inOpen[BodyLongTrailingIdx-1] ? inOpen[BodyLongTrailingIdx-1] : inClose[BodyLongTrailingIdx-1] ) - inLow[BodyLongTrailingIdx-1] ) : 0 ) ) ) ;
         BodyShortPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyShortTrailingIdx] - inOpen[BodyShortTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyShortTrailingIdx] - inLow[BodyShortTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyShortTrailingIdx] - ( inClose[BodyShortTrailingIdx] >= inOpen[BodyShortTrailingIdx] ? inClose[BodyShortTrailingIdx] : inOpen[BodyShortTrailingIdx] ) ) + ( ( inClose[BodyShortTrailingIdx] >= inOpen[BodyShortTrailingIdx] ? inOpen[BodyShortTrailingIdx] : inClose[BodyShortTrailingIdx] ) - inLow[BodyShortTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyLongTrailingIdx++;
         BodyShortTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlIdentical3CrowsLookback( )
   {
      return ((( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) )) +
         2;
   }
   public RetCode cdlIdentical3Crows( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double []ShadowVeryShortPeriodTotal = new double[3] ;
      double []EqualPeriodTotal = new double[3] ;
      int i, outIdx, totIdx, ShadowVeryShortTrailingIdx, EqualTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlIdentical3CrowsLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      ShadowVeryShortPeriodTotal[2] = 0;
      ShadowVeryShortPeriodTotal[1] = 0;
      ShadowVeryShortPeriodTotal[0] = 0;
      ShadowVeryShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ;
      EqualPeriodTotal[2] = 0;
      EqualPeriodTotal[1] = 0;
      EqualPeriodTotal[0] = 0;
      EqualTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) ;
      i = ShadowVeryShortTrailingIdx;
      while( i < startIdx ) {
         ShadowVeryShortPeriodTotal[2] += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ;
         ShadowVeryShortPeriodTotal[1] += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         ShadowVeryShortPeriodTotal[0] += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = EqualTrailingIdx;
      while( i < startIdx ) {
         EqualPeriodTotal[2] += ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ;
         EqualPeriodTotal[1] += ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) == -1 &&
            ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal[2] / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == -1 &&
            ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal[1] / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == -1 &&
            ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal[0] / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            inClose[i-2] > inClose[i-1] &&
            inClose[i-1] > inClose[i] &&
            inOpen[i-1] <= inClose[i-2] + ( (this.candleSettings[CandleSettingType.Equal.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) != 0.0? EqualPeriodTotal[2] / (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            inOpen[i-1] >= inClose[i-2] - ( (this.candleSettings[CandleSettingType.Equal.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) != 0.0? EqualPeriodTotal[2] / (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            inOpen[i] <= inClose[i-1] + ( (this.candleSettings[CandleSettingType.Equal.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) != 0.0? EqualPeriodTotal[1] / (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            inOpen[i] >= inClose[i-1] - ( (this.candleSettings[CandleSettingType.Equal.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) != 0.0? EqualPeriodTotal[1] / (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            )
            outInteger[outIdx++] = -100;
         else
            outInteger[outIdx++] = 0;
         for (totIdx = 2; totIdx >= 0; --totIdx)
            ShadowVeryShortPeriodTotal[totIdx] += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-totIdx] - inOpen[i-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-totIdx] - inLow[i-totIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-totIdx] - ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inClose[i-totIdx] : inOpen[i-totIdx] ) ) + ( ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inOpen[i-totIdx] : inClose[i-totIdx] ) - inLow[i-totIdx] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowVeryShortTrailingIdx-totIdx] - inOpen[ShadowVeryShortTrailingIdx-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowVeryShortTrailingIdx-totIdx] - inLow[ShadowVeryShortTrailingIdx-totIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowVeryShortTrailingIdx-totIdx] - ( inClose[ShadowVeryShortTrailingIdx-totIdx] >= inOpen[ShadowVeryShortTrailingIdx-totIdx] ? inClose[ShadowVeryShortTrailingIdx-totIdx] : inOpen[ShadowVeryShortTrailingIdx-totIdx] ) ) + ( ( inClose[ShadowVeryShortTrailingIdx-totIdx] >= inOpen[ShadowVeryShortTrailingIdx-totIdx] ? inOpen[ShadowVeryShortTrailingIdx-totIdx] : inClose[ShadowVeryShortTrailingIdx-totIdx] ) - inLow[ShadowVeryShortTrailingIdx-totIdx] ) : 0 ) ) ) ;
         for (totIdx = 2; totIdx >= 1; --totIdx)
            EqualPeriodTotal[totIdx] += ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-totIdx] - inOpen[i-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-totIdx] - inLow[i-totIdx] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-totIdx] - ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inClose[i-totIdx] : inOpen[i-totIdx] ) ) + ( ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inOpen[i-totIdx] : inClose[i-totIdx] ) - inLow[i-totIdx] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[EqualTrailingIdx-totIdx] - inOpen[EqualTrailingIdx-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[EqualTrailingIdx-totIdx] - inLow[EqualTrailingIdx-totIdx] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[EqualTrailingIdx-totIdx] - ( inClose[EqualTrailingIdx-totIdx] >= inOpen[EqualTrailingIdx-totIdx] ? inClose[EqualTrailingIdx-totIdx] : inOpen[EqualTrailingIdx-totIdx] ) ) + ( ( inClose[EqualTrailingIdx-totIdx] >= inOpen[EqualTrailingIdx-totIdx] ? inOpen[EqualTrailingIdx-totIdx] : inClose[EqualTrailingIdx-totIdx] ) - inLow[EqualTrailingIdx-totIdx] ) : 0 ) ) ) ;
         i++;
         ShadowVeryShortTrailingIdx++;
         EqualTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlInNeckLookback( )
   {
      return ((( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) +
         1;
   }
   public RetCode cdlInNeck( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double EqualPeriodTotal, BodyLongPeriodTotal;
      int i, outIdx, EqualTrailingIdx, BodyLongTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlInNeckLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      EqualPeriodTotal = 0;
      EqualTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) ;
      BodyLongPeriodTotal = 0;
      BodyLongTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      i = EqualTrailingIdx;
      while( i < startIdx ) {
         EqualPeriodTotal += ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         i++;
      }
      i = BodyLongTrailingIdx;
      while( i < startIdx ) {
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == -1 &&
            ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == 1 &&
            inOpen[i] < inLow[i-1] &&
            inClose[i] <= inClose[i-1] + ( (this.candleSettings[CandleSettingType.Equal.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) != 0.0? EqualPeriodTotal / (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            inClose[i] >= inClose[i-1]
            )
            outInteger[outIdx++] = -100;
         else
            outInteger[outIdx++] = 0;
         EqualPeriodTotal += ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[EqualTrailingIdx-1] - inOpen[EqualTrailingIdx-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[EqualTrailingIdx-1] - inLow[EqualTrailingIdx-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[EqualTrailingIdx-1] - ( inClose[EqualTrailingIdx-1] >= inOpen[EqualTrailingIdx-1] ? inClose[EqualTrailingIdx-1] : inOpen[EqualTrailingIdx-1] ) ) + ( ( inClose[EqualTrailingIdx-1] >= inOpen[EqualTrailingIdx-1] ? inOpen[EqualTrailingIdx-1] : inClose[EqualTrailingIdx-1] ) - inLow[EqualTrailingIdx-1] ) : 0 ) ) ) ;
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx-1] - inOpen[BodyLongTrailingIdx-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx-1] - inLow[BodyLongTrailingIdx-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx-1] - ( inClose[BodyLongTrailingIdx-1] >= inOpen[BodyLongTrailingIdx-1] ? inClose[BodyLongTrailingIdx-1] : inOpen[BodyLongTrailingIdx-1] ) ) + ( ( inClose[BodyLongTrailingIdx-1] >= inOpen[BodyLongTrailingIdx-1] ? inOpen[BodyLongTrailingIdx-1] : inClose[BodyLongTrailingIdx-1] ) - inLow[BodyLongTrailingIdx-1] ) : 0 ) ) ) ;
         i++;
         EqualTrailingIdx++;
         BodyLongTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlInvertedHammerLookback( )
   {
      return ((( ((( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ) > ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) )) ? ( ((( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) )) +
         1;
   }
   public RetCode cdlInvertedHammer( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyPeriodTotal, ShadowLongPeriodTotal, ShadowVeryShortPeriodTotal;
      int i, outIdx, BodyTrailingIdx, ShadowLongTrailingIdx, ShadowVeryShortTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlInvertedHammerLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyPeriodTotal = 0;
      BodyTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ;
      ShadowLongPeriodTotal = 0;
      ShadowLongTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) ;
      ShadowVeryShortPeriodTotal = 0;
      ShadowVeryShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ;
      i = BodyTrailingIdx;
      while( i < startIdx ) {
         BodyPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = ShadowLongTrailingIdx;
      while( i < startIdx ) {
         ShadowLongPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = ShadowVeryShortTrailingIdx;
      while( i < startIdx ) {
         ShadowVeryShortPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      outIdx = 0;
      do
      {
         if( ( Math.abs ( inClose[i] - inOpen[i] ) ) < ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) != 0.0? BodyPeriodTotal / (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) > ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) != 0.0? ShadowLongPeriodTotal / (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( (((inOpen[i]) > (inClose[i])) ? (inOpen[i]) : (inClose[i])) < (((inOpen[i-1]) < (inClose[i-1])) ? (inOpen[i-1]) : (inClose[i-1])) ) )
            outInteger[outIdx++] = 100;
         else
            outInteger[outIdx++] = 0;
         BodyPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyTrailingIdx] - inOpen[BodyTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyTrailingIdx] - inLow[BodyTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyTrailingIdx] - ( inClose[BodyTrailingIdx] >= inOpen[BodyTrailingIdx] ? inClose[BodyTrailingIdx] : inOpen[BodyTrailingIdx] ) ) + ( ( inClose[BodyTrailingIdx] >= inOpen[BodyTrailingIdx] ? inOpen[BodyTrailingIdx] : inClose[BodyTrailingIdx] ) - inLow[BodyTrailingIdx] ) : 0 ) ) ) ;
         ShadowLongPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowLongTrailingIdx] - inOpen[ShadowLongTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowLongTrailingIdx] - inLow[ShadowLongTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowLongTrailingIdx] - ( inClose[ShadowLongTrailingIdx] >= inOpen[ShadowLongTrailingIdx] ? inClose[ShadowLongTrailingIdx] : inOpen[ShadowLongTrailingIdx] ) ) + ( ( inClose[ShadowLongTrailingIdx] >= inOpen[ShadowLongTrailingIdx] ? inOpen[ShadowLongTrailingIdx] : inClose[ShadowLongTrailingIdx] ) - inLow[ShadowLongTrailingIdx] ) : 0 ) ) ) ;
         ShadowVeryShortPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowVeryShortTrailingIdx] - inOpen[ShadowVeryShortTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowVeryShortTrailingIdx] - inLow[ShadowVeryShortTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowVeryShortTrailingIdx] - ( inClose[ShadowVeryShortTrailingIdx] >= inOpen[ShadowVeryShortTrailingIdx] ? inClose[ShadowVeryShortTrailingIdx] : inOpen[ShadowVeryShortTrailingIdx] ) ) + ( ( inClose[ShadowVeryShortTrailingIdx] >= inOpen[ShadowVeryShortTrailingIdx] ? inOpen[ShadowVeryShortTrailingIdx] : inClose[ShadowVeryShortTrailingIdx] ) - inLow[ShadowVeryShortTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyTrailingIdx++;
         ShadowLongTrailingIdx++;
         ShadowVeryShortTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlKickingLookback( )
   {
      return ((( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) +
         1;
   }
   public RetCode cdlKicking( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double []ShadowVeryShortPeriodTotal = new double[2] ;
      double []BodyLongPeriodTotal = new double[2] ;
      int i, outIdx, totIdx, ShadowVeryShortTrailingIdx, BodyLongTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlKickingLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      ShadowVeryShortPeriodTotal[1] = 0;
      ShadowVeryShortPeriodTotal[0] = 0;
      ShadowVeryShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ;
      BodyLongPeriodTotal[1] = 0;
      BodyLongPeriodTotal[0] = 0;
      BodyLongTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      i = ShadowVeryShortTrailingIdx;
      while( i < startIdx ) {
         ShadowVeryShortPeriodTotal[1] += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         ShadowVeryShortPeriodTotal[0] += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = BodyLongTrailingIdx;
      while( i < startIdx ) {
         BodyLongPeriodTotal[1] += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         BodyLongPeriodTotal[0] += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == - ( inClose[i] >= inOpen[i] ? 1 : -1 ) &&
            ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal[1] / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal[1] / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal[1] / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i] - inOpen[i] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal[0] / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal[0] / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal[0] / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            (
            ( ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == -1 && ( inLow[i] > inHigh[i-1] ) )
            ||
            ( ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == 1 && ( inHigh[i] < inLow[i-1] ) )
            )
            )
            outInteger[outIdx++] = ( inClose[i] >= inOpen[i] ? 1 : -1 ) * 100;
         else
            outInteger[outIdx++] = 0;
         for (totIdx = 1; totIdx >= 0; --totIdx) {
            BodyLongPeriodTotal[totIdx] += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-totIdx] - inOpen[i-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-totIdx] - inLow[i-totIdx] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-totIdx] - ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inClose[i-totIdx] : inOpen[i-totIdx] ) ) + ( ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inOpen[i-totIdx] : inClose[i-totIdx] ) - inLow[i-totIdx] ) : 0 ) ) )
               - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx-totIdx] - inOpen[BodyLongTrailingIdx-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx-totIdx] - inLow[BodyLongTrailingIdx-totIdx] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx-totIdx] - ( inClose[BodyLongTrailingIdx-totIdx] >= inOpen[BodyLongTrailingIdx-totIdx] ? inClose[BodyLongTrailingIdx-totIdx] : inOpen[BodyLongTrailingIdx-totIdx] ) ) + ( ( inClose[BodyLongTrailingIdx-totIdx] >= inOpen[BodyLongTrailingIdx-totIdx] ? inOpen[BodyLongTrailingIdx-totIdx] : inClose[BodyLongTrailingIdx-totIdx] ) - inLow[BodyLongTrailingIdx-totIdx] ) : 0 ) ) ) ;
            ShadowVeryShortPeriodTotal[totIdx] += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-totIdx] - inOpen[i-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-totIdx] - inLow[i-totIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-totIdx] - ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inClose[i-totIdx] : inOpen[i-totIdx] ) ) + ( ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inOpen[i-totIdx] : inClose[i-totIdx] ) - inLow[i-totIdx] ) : 0 ) ) )
               - ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowVeryShortTrailingIdx-totIdx] - inOpen[ShadowVeryShortTrailingIdx-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowVeryShortTrailingIdx-totIdx] - inLow[ShadowVeryShortTrailingIdx-totIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowVeryShortTrailingIdx-totIdx] - ( inClose[ShadowVeryShortTrailingIdx-totIdx] >= inOpen[ShadowVeryShortTrailingIdx-totIdx] ? inClose[ShadowVeryShortTrailingIdx-totIdx] : inOpen[ShadowVeryShortTrailingIdx-totIdx] ) ) + ( ( inClose[ShadowVeryShortTrailingIdx-totIdx] >= inOpen[ShadowVeryShortTrailingIdx-totIdx] ? inOpen[ShadowVeryShortTrailingIdx-totIdx] : inClose[ShadowVeryShortTrailingIdx-totIdx] ) - inLow[ShadowVeryShortTrailingIdx-totIdx] ) : 0 ) ) ) ;
         }
         i++;
         ShadowVeryShortTrailingIdx++;
         BodyLongTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlKickingByLengthLookback( )
   {
      return ((( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) +
         1;
   }
   public RetCode cdlKickingByLength( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double []ShadowVeryShortPeriodTotal = new double[2] ;
      double []BodyLongPeriodTotal = new double[2] ;
      int i, outIdx, totIdx, ShadowVeryShortTrailingIdx, BodyLongTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlKickingByLengthLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      ShadowVeryShortPeriodTotal[1] = 0;
      ShadowVeryShortPeriodTotal[0] = 0;
      ShadowVeryShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ;
      BodyLongPeriodTotal[1] = 0;
      BodyLongPeriodTotal[0] = 0;
      BodyLongTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      i = ShadowVeryShortTrailingIdx;
      while( i < startIdx ) {
         ShadowVeryShortPeriodTotal[1] += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         ShadowVeryShortPeriodTotal[0] += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = BodyLongTrailingIdx;
      while( i < startIdx ) {
         BodyLongPeriodTotal[1] += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         BodyLongPeriodTotal[0] += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == - ( inClose[i] >= inOpen[i] ? 1 : -1 ) &&
            ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal[1] / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal[1] / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal[1] / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i] - inOpen[i] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal[0] / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal[0] / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal[0] / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            (
            ( ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == -1 && ( inLow[i] > inHigh[i-1] ) )
            ||
            ( ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == 1 && ( inHigh[i] < inLow[i-1] ) )
            )
            )
            outInteger[outIdx++] = ( inClose[( ( Math.abs ( inClose[i] - inOpen[i] ) ) > ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) ? i : i-1 )] >= inOpen[( ( Math.abs ( inClose[i] - inOpen[i] ) ) > ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) ? i : i-1 )] ? 1 : -1 ) * 100;
         else
            outInteger[outIdx++] = 0;
         for (totIdx = 1; totIdx >= 0; --totIdx) {
            BodyLongPeriodTotal[totIdx] += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-totIdx] - inOpen[i-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-totIdx] - inLow[i-totIdx] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-totIdx] - ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inClose[i-totIdx] : inOpen[i-totIdx] ) ) + ( ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inOpen[i-totIdx] : inClose[i-totIdx] ) - inLow[i-totIdx] ) : 0 ) ) )
               - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx-totIdx] - inOpen[BodyLongTrailingIdx-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx-totIdx] - inLow[BodyLongTrailingIdx-totIdx] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx-totIdx] - ( inClose[BodyLongTrailingIdx-totIdx] >= inOpen[BodyLongTrailingIdx-totIdx] ? inClose[BodyLongTrailingIdx-totIdx] : inOpen[BodyLongTrailingIdx-totIdx] ) ) + ( ( inClose[BodyLongTrailingIdx-totIdx] >= inOpen[BodyLongTrailingIdx-totIdx] ? inOpen[BodyLongTrailingIdx-totIdx] : inClose[BodyLongTrailingIdx-totIdx] ) - inLow[BodyLongTrailingIdx-totIdx] ) : 0 ) ) ) ;
            ShadowVeryShortPeriodTotal[totIdx] += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-totIdx] - inOpen[i-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-totIdx] - inLow[i-totIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-totIdx] - ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inClose[i-totIdx] : inOpen[i-totIdx] ) ) + ( ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inOpen[i-totIdx] : inClose[i-totIdx] ) - inLow[i-totIdx] ) : 0 ) ) )
               - ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowVeryShortTrailingIdx-totIdx] - inOpen[ShadowVeryShortTrailingIdx-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowVeryShortTrailingIdx-totIdx] - inLow[ShadowVeryShortTrailingIdx-totIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowVeryShortTrailingIdx-totIdx] - ( inClose[ShadowVeryShortTrailingIdx-totIdx] >= inOpen[ShadowVeryShortTrailingIdx-totIdx] ? inClose[ShadowVeryShortTrailingIdx-totIdx] : inOpen[ShadowVeryShortTrailingIdx-totIdx] ) ) + ( ( inClose[ShadowVeryShortTrailingIdx-totIdx] >= inOpen[ShadowVeryShortTrailingIdx-totIdx] ? inOpen[ShadowVeryShortTrailingIdx-totIdx] : inClose[ShadowVeryShortTrailingIdx-totIdx] ) - inLow[ShadowVeryShortTrailingIdx-totIdx] ) : 0 ) ) ) ;
         }
         i++;
         ShadowVeryShortTrailingIdx++;
         BodyLongTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlLadderBottomLookback( )
   {
      return (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) + 4;
   }
   public RetCode cdlLadderBottom( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double ShadowVeryShortPeriodTotal;
      int i, outIdx, ShadowVeryShortTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlLadderBottomLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      ShadowVeryShortPeriodTotal = 0;
      ShadowVeryShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ;
      i = ShadowVeryShortTrailingIdx;
      while( i < startIdx ) {
         ShadowVeryShortPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if(
            ( inClose[i-4] >= inOpen[i-4] ? 1 : -1 ) == -1 && ( inClose[i-3] >= inOpen[i-3] ? 1 : -1 ) == -1 && ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) == -1 &&
            inOpen[i-4] > inOpen[i-3] && inOpen[i-3] > inOpen[i-2] &&
            inClose[i-4] > inClose[i-3] && inClose[i-3] > inClose[i-2] &&
            ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == -1 &&
            ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) > ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == 1 &&
            inOpen[i] > inOpen[i-1] &&
            inClose[i] > inHigh[i-1]
            )
            outInteger[outIdx++] = 100;
         else
            outInteger[outIdx++] = 0;
         ShadowVeryShortPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowVeryShortTrailingIdx-1] - inOpen[ShadowVeryShortTrailingIdx-1] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowVeryShortTrailingIdx-1] - inLow[ShadowVeryShortTrailingIdx-1] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowVeryShortTrailingIdx-1] - ( inClose[ShadowVeryShortTrailingIdx-1] >= inOpen[ShadowVeryShortTrailingIdx-1] ? inClose[ShadowVeryShortTrailingIdx-1] : inOpen[ShadowVeryShortTrailingIdx-1] ) ) + ( ( inClose[ShadowVeryShortTrailingIdx-1] >= inOpen[ShadowVeryShortTrailingIdx-1] ? inOpen[ShadowVeryShortTrailingIdx-1] : inClose[ShadowVeryShortTrailingIdx-1] ) - inLow[ShadowVeryShortTrailingIdx-1] ) : 0 ) ) ) ;
         i++;
         ShadowVeryShortTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlLongLeggedDojiLookback( )
   {
      return ((( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ;
   }
   public RetCode cdlLongLeggedDoji( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyDojiPeriodTotal, ShadowLongPeriodTotal;
      int i, outIdx, BodyDojiTrailingIdx, ShadowLongTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlLongLeggedDojiLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyDojiPeriodTotal = 0;
      BodyDojiTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ;
      ShadowLongPeriodTotal = 0;
      ShadowLongTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) ;
      i = BodyDojiTrailingIdx;
      while( i < startIdx ) {
         BodyDojiPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = ShadowLongTrailingIdx;
      while( i < startIdx ) {
         ShadowLongPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      outIdx = 0;
      do
      {
         if( ( Math.abs ( inClose[i] - inOpen[i] ) ) <= ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) != 0.0? BodyDojiPeriodTotal / (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) > ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) != 0.0? ShadowLongPeriodTotal / (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            ||
            ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) > ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) != 0.0? ShadowLongPeriodTotal / (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            )
            )
            outInteger[outIdx++] = 100;
         else
            outInteger[outIdx++] = 0;
         BodyDojiPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyDojiTrailingIdx] - inOpen[BodyDojiTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyDojiTrailingIdx] - inLow[BodyDojiTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyDojiTrailingIdx] - ( inClose[BodyDojiTrailingIdx] >= inOpen[BodyDojiTrailingIdx] ? inClose[BodyDojiTrailingIdx] : inOpen[BodyDojiTrailingIdx] ) ) + ( ( inClose[BodyDojiTrailingIdx] >= inOpen[BodyDojiTrailingIdx] ? inOpen[BodyDojiTrailingIdx] : inClose[BodyDojiTrailingIdx] ) - inLow[BodyDojiTrailingIdx] ) : 0 ) ) ) ;
         ShadowLongPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowLongTrailingIdx] - inOpen[ShadowLongTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowLongTrailingIdx] - inLow[ShadowLongTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowLongTrailingIdx] - ( inClose[ShadowLongTrailingIdx] >= inOpen[ShadowLongTrailingIdx] ? inClose[ShadowLongTrailingIdx] : inOpen[ShadowLongTrailingIdx] ) ) + ( ( inClose[ShadowLongTrailingIdx] >= inOpen[ShadowLongTrailingIdx] ? inOpen[ShadowLongTrailingIdx] : inClose[ShadowLongTrailingIdx] ) - inLow[ShadowLongTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyDojiTrailingIdx++;
         ShadowLongTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlLongLineLookback( )
   {
      return ((( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].avgPeriod) )) ;
   }
   public RetCode cdlLongLine( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyPeriodTotal, ShadowPeriodTotal;
      int i, outIdx, BodyTrailingIdx, ShadowTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlLongLineLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyPeriodTotal = 0;
      BodyTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      ShadowPeriodTotal = 0;
      ShadowTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].avgPeriod) ;
      i = BodyTrailingIdx;
      while( i < startIdx ) {
         BodyPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = ShadowTrailingIdx;
      while( i < startIdx ) {
         ShadowPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      outIdx = 0;
      do
      {
         if( ( Math.abs ( inClose[i] - inOpen[i] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyPeriodTotal / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) < ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].avgPeriod) != 0.0? ShadowPeriodTotal / (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) < ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].avgPeriod) != 0.0? ShadowPeriodTotal / (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) )
            outInteger[outIdx++] = ( inClose[i] >= inOpen[i] ? 1 : -1 ) * 100;
         else
            outInteger[outIdx++] = 0;
         BodyPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyTrailingIdx] - inOpen[BodyTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyTrailingIdx] - inLow[BodyTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyTrailingIdx] - ( inClose[BodyTrailingIdx] >= inOpen[BodyTrailingIdx] ? inClose[BodyTrailingIdx] : inOpen[BodyTrailingIdx] ) ) + ( ( inClose[BodyTrailingIdx] >= inOpen[BodyTrailingIdx] ? inOpen[BodyTrailingIdx] : inClose[BodyTrailingIdx] ) - inLow[BodyTrailingIdx] ) : 0 ) ) ) ;
         ShadowPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowTrailingIdx] - inOpen[ShadowTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowTrailingIdx] - inLow[ShadowTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowTrailingIdx] - ( inClose[ShadowTrailingIdx] >= inOpen[ShadowTrailingIdx] ? inClose[ShadowTrailingIdx] : inOpen[ShadowTrailingIdx] ) ) + ( ( inClose[ShadowTrailingIdx] >= inOpen[ShadowTrailingIdx] ? inOpen[ShadowTrailingIdx] : inClose[ShadowTrailingIdx] ) - inLow[ShadowTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyTrailingIdx++;
         ShadowTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlMarubozuLookback( )
   {
      return ((( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) )) ;
   }
   public RetCode cdlMarubozu( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyLongPeriodTotal, ShadowVeryShortPeriodTotal;
      int i, outIdx, BodyLongTrailingIdx, ShadowVeryShortTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlMarubozuLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyLongPeriodTotal = 0;
      BodyLongTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      ShadowVeryShortPeriodTotal = 0;
      ShadowVeryShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ;
      i = BodyLongTrailingIdx;
      while( i < startIdx ) {
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = ShadowVeryShortTrailingIdx;
      while( i < startIdx ) {
         ShadowVeryShortPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      outIdx = 0;
      do
      {
         if( ( Math.abs ( inClose[i] - inOpen[i] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) )
            outInteger[outIdx++] = ( inClose[i] >= inOpen[i] ? 1 : -1 ) * 100;
         else
            outInteger[outIdx++] = 0;
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx] - inOpen[BodyLongTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx] - inLow[BodyLongTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx] - ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inClose[BodyLongTrailingIdx] : inOpen[BodyLongTrailingIdx] ) ) + ( ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inOpen[BodyLongTrailingIdx] : inClose[BodyLongTrailingIdx] ) - inLow[BodyLongTrailingIdx] ) : 0 ) ) ) ;
         ShadowVeryShortPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowVeryShortTrailingIdx] - inOpen[ShadowVeryShortTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowVeryShortTrailingIdx] - inLow[ShadowVeryShortTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowVeryShortTrailingIdx] - ( inClose[ShadowVeryShortTrailingIdx] >= inOpen[ShadowVeryShortTrailingIdx] ? inClose[ShadowVeryShortTrailingIdx] : inOpen[ShadowVeryShortTrailingIdx] ) ) + ( ( inClose[ShadowVeryShortTrailingIdx] >= inOpen[ShadowVeryShortTrailingIdx] ? inOpen[ShadowVeryShortTrailingIdx] : inClose[ShadowVeryShortTrailingIdx] ) - inLow[ShadowVeryShortTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyLongTrailingIdx++;
         ShadowVeryShortTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlMatchingLowLookback( )
   {
      return (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) + 1;
   }
   public RetCode cdlMatchingLow( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double EqualPeriodTotal;
      int i, outIdx, EqualTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlMatchingLowLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      EqualPeriodTotal = 0;
      EqualTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) ;
      i = EqualTrailingIdx;
      while( i < startIdx ) {
         EqualPeriodTotal += ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == -1 &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == -1 &&
            inClose[i] <= inClose[i-1] + ( (this.candleSettings[CandleSettingType.Equal.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) != 0.0? EqualPeriodTotal / (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            inClose[i] >= inClose[i-1] - ( (this.candleSettings[CandleSettingType.Equal.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) != 0.0? EqualPeriodTotal / (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            )
            outInteger[outIdx++] = 100;
         else
            outInteger[outIdx++] = 0;
         EqualPeriodTotal += ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[EqualTrailingIdx-1] - inOpen[EqualTrailingIdx-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[EqualTrailingIdx-1] - inLow[EqualTrailingIdx-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[EqualTrailingIdx-1] - ( inClose[EqualTrailingIdx-1] >= inOpen[EqualTrailingIdx-1] ? inClose[EqualTrailingIdx-1] : inOpen[EqualTrailingIdx-1] ) ) + ( ( inClose[EqualTrailingIdx-1] >= inOpen[EqualTrailingIdx-1] ? inOpen[EqualTrailingIdx-1] : inClose[EqualTrailingIdx-1] ) - inLow[EqualTrailingIdx-1] ) : 0 ) ) ) ;
         i++;
         EqualTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlMatHoldLookback( double optInPenetration )
   {
      if( optInPenetration == (-4e+37) )
         optInPenetration = 5.000000e-1;
      else if( (optInPenetration < 0.000000e+0) || (optInPenetration > 3.000000e+37) )
         return -1;
      return ((( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) + 4;
   }
   public RetCode cdlMatHold( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      double optInPenetration,
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double []BodyPeriodTotal = new double[5] ;
      int i, outIdx, totIdx, BodyShortTrailingIdx, BodyLongTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( optInPenetration == (-4e+37) )
         optInPenetration = 5.000000e-1;
      else if( (optInPenetration < 0.000000e+0) || (optInPenetration > 3.000000e+37) )
         return RetCode.BadParam ;
      lookbackTotal = cdlMatHoldLookback (optInPenetration);
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyPeriodTotal[4] = 0;
      BodyPeriodTotal[3] = 0;
      BodyPeriodTotal[2] = 0;
      BodyPeriodTotal[1] = 0;
      BodyPeriodTotal[0] = 0;
      BodyShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ;
      BodyLongTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      i = BodyShortTrailingIdx;
      while( i < startIdx ) {
         BodyPeriodTotal[3] += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-3] - inOpen[i-3] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-3] - inLow[i-3] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-3] - ( inClose[i-3] >= inOpen[i-3] ? inClose[i-3] : inOpen[i-3] ) ) + ( ( inClose[i-3] >= inOpen[i-3] ? inOpen[i-3] : inClose[i-3] ) - inLow[i-3] ) : 0 ) ) ) ;
         BodyPeriodTotal[2] += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ;
         BodyPeriodTotal[1] += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         i++;
      }
      i = BodyLongTrailingIdx;
      while( i < startIdx ) {
         BodyPeriodTotal[4] += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-4] - inOpen[i-4] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-4] - inLow[i-4] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-4] - ( inClose[i-4] >= inOpen[i-4] ? inClose[i-4] : inOpen[i-4] ) ) + ( ( inClose[i-4] >= inOpen[i-4] ? inOpen[i-4] : inClose[i-4] ) - inLow[i-4] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if(
            ( Math.abs ( inClose[i-4] - inOpen[i-4] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyPeriodTotal[4] / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-4] - inOpen[i-4] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-4] - inLow[i-4] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-4] - ( inClose[i-4] >= inOpen[i-4] ? inClose[i-4] : inOpen[i-4] ) ) + ( ( inClose[i-4] >= inOpen[i-4] ? inOpen[i-4] : inClose[i-4] ) - inLow[i-4] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i-3] - inOpen[i-3] ) ) < ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) != 0.0? BodyPeriodTotal[3] / (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-3] - inOpen[i-3] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-3] - inLow[i-3] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-3] - ( inClose[i-3] >= inOpen[i-3] ? inClose[i-3] : inOpen[i-3] ) ) + ( ( inClose[i-3] >= inOpen[i-3] ? inOpen[i-3] : inClose[i-3] ) - inLow[i-3] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) < ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) != 0.0? BodyPeriodTotal[2] / (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) < ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) != 0.0? BodyPeriodTotal[1] / (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inClose[i-4] >= inOpen[i-4] ? 1 : -1 ) == 1 &&
            ( inClose[i-3] >= inOpen[i-3] ? 1 : -1 ) == -1 &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == 1 &&
            ( (((inOpen[i-3]) < (inClose[i-3])) ? (inOpen[i-3]) : (inClose[i-3])) > (((inOpen[i-4]) > (inClose[i-4])) ? (inOpen[i-4]) : (inClose[i-4])) ) &&
            (((inOpen[i-2]) < (inClose[i-2])) ? (inOpen[i-2]) : (inClose[i-2])) < inClose[i-4] &&
            (((inOpen[i-1]) < (inClose[i-1])) ? (inOpen[i-1]) : (inClose[i-1])) < inClose[i-4] &&
            (((inOpen[i-2]) < (inClose[i-2])) ? (inOpen[i-2]) : (inClose[i-2])) > inClose[i-4] - ( Math.abs ( inClose[i-4] - inOpen[i-4] ) ) * optInPenetration &&
            (((inOpen[i-1]) < (inClose[i-1])) ? (inOpen[i-1]) : (inClose[i-1])) > inClose[i-4] - ( Math.abs ( inClose[i-4] - inOpen[i-4] ) ) * optInPenetration &&
            (((inClose[i-2]) > (inOpen[i-2])) ? (inClose[i-2]) : (inOpen[i-2])) < inOpen[i-3] &&
            (((inClose[i-1]) > (inOpen[i-1])) ? (inClose[i-1]) : (inOpen[i-1])) < (((inClose[i-2]) > (inOpen[i-2])) ? (inClose[i-2]) : (inOpen[i-2])) &&
            inOpen[i] > inClose[i-1] &&
            inClose[i] > ((( (((inHigh[i-3]) > (inHigh[i-2])) ? (inHigh[i-3]) : (inHigh[i-2])) ) > (inHigh[i-1])) ? ( (((inHigh[i-3]) > (inHigh[i-2])) ? (inHigh[i-3]) : (inHigh[i-2])) ) : (inHigh[i-1]))
            )
            outInteger[outIdx++] = 100;
         else
            outInteger[outIdx++] = 0;
         BodyPeriodTotal[4] += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-4] - inOpen[i-4] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-4] - inLow[i-4] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-4] - ( inClose[i-4] >= inOpen[i-4] ? inClose[i-4] : inOpen[i-4] ) ) + ( ( inClose[i-4] >= inOpen[i-4] ? inOpen[i-4] : inClose[i-4] ) - inLow[i-4] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx-4] - inOpen[BodyLongTrailingIdx-4] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx-4] - inLow[BodyLongTrailingIdx-4] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx-4] - ( inClose[BodyLongTrailingIdx-4] >= inOpen[BodyLongTrailingIdx-4] ? inClose[BodyLongTrailingIdx-4] : inOpen[BodyLongTrailingIdx-4] ) ) + ( ( inClose[BodyLongTrailingIdx-4] >= inOpen[BodyLongTrailingIdx-4] ? inOpen[BodyLongTrailingIdx-4] : inClose[BodyLongTrailingIdx-4] ) - inLow[BodyLongTrailingIdx-4] ) : 0 ) ) ) ;
         for (totIdx = 3; totIdx >= 1; --totIdx)
            BodyPeriodTotal[totIdx] += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-totIdx] - inOpen[i-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-totIdx] - inLow[i-totIdx] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-totIdx] - ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inClose[i-totIdx] : inOpen[i-totIdx] ) ) + ( ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inOpen[i-totIdx] : inClose[i-totIdx] ) - inLow[i-totIdx] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyShortTrailingIdx-totIdx] - inOpen[BodyShortTrailingIdx-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyShortTrailingIdx-totIdx] - inLow[BodyShortTrailingIdx-totIdx] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyShortTrailingIdx-totIdx] - ( inClose[BodyShortTrailingIdx-totIdx] >= inOpen[BodyShortTrailingIdx-totIdx] ? inClose[BodyShortTrailingIdx-totIdx] : inOpen[BodyShortTrailingIdx-totIdx] ) ) + ( ( inClose[BodyShortTrailingIdx-totIdx] >= inOpen[BodyShortTrailingIdx-totIdx] ? inOpen[BodyShortTrailingIdx-totIdx] : inClose[BodyShortTrailingIdx-totIdx] ) - inLow[BodyShortTrailingIdx-totIdx] ) : 0 ) ) ) ;
         i++;
         BodyShortTrailingIdx++;
         BodyLongTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlMorningDojiStarLookback( double optInPenetration )
   {
      if( optInPenetration == (-4e+37) )
         optInPenetration = 3.000000e-1;
      else if( (optInPenetration < 0.000000e+0) || (optInPenetration > 3.000000e+37) )
         return -1;
      return ((( ((( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ) > ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) )) ? ( ((( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) )) +
         2;
   }
   public RetCode cdlMorningDojiStar( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      double optInPenetration,
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyDojiPeriodTotal, BodyLongPeriodTotal, BodyShortPeriodTotal;
      int i, outIdx, BodyDojiTrailingIdx, BodyLongTrailingIdx, BodyShortTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( optInPenetration == (-4e+37) )
         optInPenetration = 3.000000e-1;
      else if( (optInPenetration < 0.000000e+0) || (optInPenetration > 3.000000e+37) )
         return RetCode.BadParam ;
      lookbackTotal = cdlMorningDojiStarLookback (optInPenetration);
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyLongPeriodTotal = 0;
      BodyDojiPeriodTotal = 0;
      BodyShortPeriodTotal = 0;
      BodyLongTrailingIdx = startIdx -2 - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      BodyDojiTrailingIdx = startIdx -1 - (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ;
      BodyShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ;
      i = BodyLongTrailingIdx;
      while( i < startIdx-2 ) {
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = BodyDojiTrailingIdx;
      while( i < startIdx-1 ) {
         BodyDojiPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = BodyShortTrailingIdx;
      while( i < startIdx ) {
         BodyShortPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) == -1 &&
            ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) <= ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) != 0.0? BodyDojiPeriodTotal / (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( (((inOpen[i-1]) > (inClose[i-1])) ? (inOpen[i-1]) : (inClose[i-1])) < (((inOpen[i-2]) < (inClose[i-2])) ? (inOpen[i-2]) : (inClose[i-2])) ) &&
            ( Math.abs ( inClose[i] - inOpen[i] ) ) > ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) != 0.0? BodyShortPeriodTotal / (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == 1 &&
            inClose[i] > inClose[i-2] + ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) * optInPenetration
            )
            outInteger[outIdx++] = 100;
         else
            outInteger[outIdx++] = 0;
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx] - inOpen[BodyLongTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx] - inLow[BodyLongTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx] - ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inClose[BodyLongTrailingIdx] : inOpen[BodyLongTrailingIdx] ) ) + ( ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inOpen[BodyLongTrailingIdx] : inClose[BodyLongTrailingIdx] ) - inLow[BodyLongTrailingIdx] ) : 0 ) ) ) ;
         BodyDojiPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyDojiTrailingIdx] - inOpen[BodyDojiTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyDojiTrailingIdx] - inLow[BodyDojiTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyDojiTrailingIdx] - ( inClose[BodyDojiTrailingIdx] >= inOpen[BodyDojiTrailingIdx] ? inClose[BodyDojiTrailingIdx] : inOpen[BodyDojiTrailingIdx] ) ) + ( ( inClose[BodyDojiTrailingIdx] >= inOpen[BodyDojiTrailingIdx] ? inOpen[BodyDojiTrailingIdx] : inClose[BodyDojiTrailingIdx] ) - inLow[BodyDojiTrailingIdx] ) : 0 ) ) ) ;
         BodyShortPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyShortTrailingIdx] - inOpen[BodyShortTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyShortTrailingIdx] - inLow[BodyShortTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyShortTrailingIdx] - ( inClose[BodyShortTrailingIdx] >= inOpen[BodyShortTrailingIdx] ? inClose[BodyShortTrailingIdx] : inOpen[BodyShortTrailingIdx] ) ) + ( ( inClose[BodyShortTrailingIdx] >= inOpen[BodyShortTrailingIdx] ? inOpen[BodyShortTrailingIdx] : inClose[BodyShortTrailingIdx] ) - inLow[BodyShortTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyLongTrailingIdx++;
         BodyDojiTrailingIdx++;
         BodyShortTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlMorningStarLookback( double optInPenetration )
   {
      if( optInPenetration == (-4e+37) )
         optInPenetration = 3.000000e-1;
      else if( (optInPenetration < 0.000000e+0) || (optInPenetration > 3.000000e+37) )
         return -1;
      return ((( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) + 2;
   }
   public RetCode cdlMorningStar( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      double optInPenetration,
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyShortPeriodTotal, BodyLongPeriodTotal, BodyShortPeriodTotal2;
      int i, outIdx, BodyShortTrailingIdx, BodyLongTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( optInPenetration == (-4e+37) )
         optInPenetration = 3.000000e-1;
      else if( (optInPenetration < 0.000000e+0) || (optInPenetration > 3.000000e+37) )
         return RetCode.BadParam ;
      lookbackTotal = cdlMorningStarLookback (optInPenetration);
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyLongPeriodTotal = 0;
      BodyShortPeriodTotal = 0;
      BodyShortPeriodTotal2 = 0;
      BodyLongTrailingIdx = startIdx -2 - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      BodyShortTrailingIdx = startIdx -1 - (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ;
      i = BodyLongTrailingIdx;
      while( i < startIdx-2 ) {
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = BodyShortTrailingIdx;
      while( i < startIdx-1 ) {
         BodyShortPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         BodyShortPeriodTotal2 += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i+1] - inOpen[i+1] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i+1] - inLow[i+1] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i+1] - ( inClose[i+1] >= inOpen[i+1] ? inClose[i+1] : inOpen[i+1] ) ) + ( ( inClose[i+1] >= inOpen[i+1] ? inOpen[i+1] : inClose[i+1] ) - inLow[i+1] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) == -1 &&
            ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) <= ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) != 0.0? BodyShortPeriodTotal / (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( (((inOpen[i-1]) > (inClose[i-1])) ? (inOpen[i-1]) : (inClose[i-1])) < (((inOpen[i-2]) < (inClose[i-2])) ? (inOpen[i-2]) : (inClose[i-2])) ) &&
            ( Math.abs ( inClose[i] - inOpen[i] ) ) > ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) != 0.0? BodyShortPeriodTotal2 / (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == 1 &&
            inClose[i] > inClose[i-2] + ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) * optInPenetration
            )
            outInteger[outIdx++] = 100;
         else
            outInteger[outIdx++] = 0;
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx] - inOpen[BodyLongTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx] - inLow[BodyLongTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx] - ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inClose[BodyLongTrailingIdx] : inOpen[BodyLongTrailingIdx] ) ) + ( ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inOpen[BodyLongTrailingIdx] : inClose[BodyLongTrailingIdx] ) - inLow[BodyLongTrailingIdx] ) : 0 ) ) ) ;
         BodyShortPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyShortTrailingIdx] - inOpen[BodyShortTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyShortTrailingIdx] - inLow[BodyShortTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyShortTrailingIdx] - ( inClose[BodyShortTrailingIdx] >= inOpen[BodyShortTrailingIdx] ? inClose[BodyShortTrailingIdx] : inOpen[BodyShortTrailingIdx] ) ) + ( ( inClose[BodyShortTrailingIdx] >= inOpen[BodyShortTrailingIdx] ? inOpen[BodyShortTrailingIdx] : inClose[BodyShortTrailingIdx] ) - inLow[BodyShortTrailingIdx] ) : 0 ) ) ) ;
         BodyShortPeriodTotal2 += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyShortTrailingIdx+1] - inOpen[BodyShortTrailingIdx+1] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyShortTrailingIdx+1] - inLow[BodyShortTrailingIdx+1] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyShortTrailingIdx+1] - ( inClose[BodyShortTrailingIdx+1] >= inOpen[BodyShortTrailingIdx+1] ? inClose[BodyShortTrailingIdx+1] : inOpen[BodyShortTrailingIdx+1] ) ) + ( ( inClose[BodyShortTrailingIdx+1] >= inOpen[BodyShortTrailingIdx+1] ? inOpen[BodyShortTrailingIdx+1] : inClose[BodyShortTrailingIdx+1] ) - inLow[BodyShortTrailingIdx+1] ) : 0 ) ) ) ;
         i++;
         BodyLongTrailingIdx++;
         BodyShortTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlOnNeckLookback( )
   {
      return ((( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) +
         1;
   }
   public RetCode cdlOnNeck( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double EqualPeriodTotal, BodyLongPeriodTotal;
      int i, outIdx, EqualTrailingIdx, BodyLongTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlOnNeckLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      EqualPeriodTotal = 0;
      EqualTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) ;
      BodyLongPeriodTotal = 0;
      BodyLongTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      i = EqualTrailingIdx;
      while( i < startIdx ) {
         EqualPeriodTotal += ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         i++;
      }
      i = BodyLongTrailingIdx;
      while( i < startIdx ) {
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == -1 &&
            ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == 1 &&
            inOpen[i] < inLow[i-1] &&
            inClose[i] <= inLow[i-1] + ( (this.candleSettings[CandleSettingType.Equal.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) != 0.0? EqualPeriodTotal / (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            inClose[i] >= inLow[i-1] - ( (this.candleSettings[CandleSettingType.Equal.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) != 0.0? EqualPeriodTotal / (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            )
            outInteger[outIdx++] = -100;
         else
            outInteger[outIdx++] = 0;
         EqualPeriodTotal += ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[EqualTrailingIdx-1] - inOpen[EqualTrailingIdx-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[EqualTrailingIdx-1] - inLow[EqualTrailingIdx-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[EqualTrailingIdx-1] - ( inClose[EqualTrailingIdx-1] >= inOpen[EqualTrailingIdx-1] ? inClose[EqualTrailingIdx-1] : inOpen[EqualTrailingIdx-1] ) ) + ( ( inClose[EqualTrailingIdx-1] >= inOpen[EqualTrailingIdx-1] ? inOpen[EqualTrailingIdx-1] : inClose[EqualTrailingIdx-1] ) - inLow[EqualTrailingIdx-1] ) : 0 ) ) ) ;
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx-1] - inOpen[BodyLongTrailingIdx-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx-1] - inLow[BodyLongTrailingIdx-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx-1] - ( inClose[BodyLongTrailingIdx-1] >= inOpen[BodyLongTrailingIdx-1] ? inClose[BodyLongTrailingIdx-1] : inOpen[BodyLongTrailingIdx-1] ) ) + ( ( inClose[BodyLongTrailingIdx-1] >= inOpen[BodyLongTrailingIdx-1] ? inOpen[BodyLongTrailingIdx-1] : inClose[BodyLongTrailingIdx-1] ) - inLow[BodyLongTrailingIdx-1] ) : 0 ) ) ) ;
         i++;
         EqualTrailingIdx++;
         BodyLongTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlPiercingLookback( )
   {
      return (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) + 1;
   }
   public RetCode cdlPiercing( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double []BodyLongPeriodTotal = new double[2] ;
      int i, outIdx, totIdx, BodyLongTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlPiercingLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyLongPeriodTotal[1] = 0;
      BodyLongPeriodTotal[0] = 0;
      BodyLongTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      i = BodyLongTrailingIdx;
      while( i < startIdx ) {
         BodyLongPeriodTotal[1] += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         BodyLongPeriodTotal[0] += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == -1 &&
            ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal[1] / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == 1 &&
            ( Math.abs ( inClose[i] - inOpen[i] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal[0] / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            inOpen[i] < inLow[i-1] &&
            inClose[i] < inOpen[i-1] &&
            inClose[i] > inClose[i-1] + ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) * 0.5
            )
            outInteger[outIdx++] = 100;
         else
            outInteger[outIdx++] = 0;
         for (totIdx = 1; totIdx >= 0; --totIdx)
            BodyLongPeriodTotal[totIdx] += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-totIdx] - inOpen[i-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-totIdx] - inLow[i-totIdx] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-totIdx] - ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inClose[i-totIdx] : inOpen[i-totIdx] ) ) + ( ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inOpen[i-totIdx] : inClose[i-totIdx] ) - inLow[i-totIdx] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx-totIdx] - inOpen[BodyLongTrailingIdx-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx-totIdx] - inLow[BodyLongTrailingIdx-totIdx] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx-totIdx] - ( inClose[BodyLongTrailingIdx-totIdx] >= inOpen[BodyLongTrailingIdx-totIdx] ? inClose[BodyLongTrailingIdx-totIdx] : inOpen[BodyLongTrailingIdx-totIdx] ) ) + ( ( inClose[BodyLongTrailingIdx-totIdx] >= inOpen[BodyLongTrailingIdx-totIdx] ? inOpen[BodyLongTrailingIdx-totIdx] : inClose[BodyLongTrailingIdx-totIdx] ) - inLow[BodyLongTrailingIdx-totIdx] ) : 0 ) ) ) ;
         i++;
         BodyLongTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlRickshawManLookback( )
   {
      return ((( ((( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ) > ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) )) ? ( ((( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) )) ;
   }
   public RetCode cdlRickshawMan( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyDojiPeriodTotal, ShadowLongPeriodTotal, NearPeriodTotal;
      int i, outIdx, BodyDojiTrailingIdx, ShadowLongTrailingIdx, NearTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlRickshawManLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyDojiPeriodTotal = 0;
      BodyDojiTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ;
      ShadowLongPeriodTotal = 0;
      ShadowLongTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) ;
      NearPeriodTotal = 0;
      NearTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) ;
      i = BodyDojiTrailingIdx;
      while( i < startIdx ) {
         BodyDojiPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = ShadowLongTrailingIdx;
      while( i < startIdx ) {
         ShadowLongPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = NearTrailingIdx;
      while( i < startIdx ) {
         NearPeriodTotal += ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      outIdx = 0;
      do
      {
         if( ( Math.abs ( inClose[i] - inOpen[i] ) ) <= ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) != 0.0? BodyDojiPeriodTotal / (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) > ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) != 0.0? ShadowLongPeriodTotal / (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) > ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) != 0.0? ShadowLongPeriodTotal / (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            (
            (((inOpen[i]) < (inClose[i])) ? (inOpen[i]) : (inClose[i]))
            <= inLow[i] + ( inHigh[i] - inLow[i] ) / 2 + ( (this.candleSettings[CandleSettingType.Near.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) != 0.0? NearPeriodTotal / (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            &&
            (((inOpen[i]) > (inClose[i])) ? (inOpen[i]) : (inClose[i]))
            >= inLow[i] + ( inHigh[i] - inLow[i] ) / 2 - ( (this.candleSettings[CandleSettingType.Near.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) != 0.0? NearPeriodTotal / (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            )
            )
            outInteger[outIdx++] = 100;
         else
            outInteger[outIdx++] = 0;
         BodyDojiPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyDojiTrailingIdx] - inOpen[BodyDojiTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyDojiTrailingIdx] - inLow[BodyDojiTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyDojiTrailingIdx] - ( inClose[BodyDojiTrailingIdx] >= inOpen[BodyDojiTrailingIdx] ? inClose[BodyDojiTrailingIdx] : inOpen[BodyDojiTrailingIdx] ) ) + ( ( inClose[BodyDojiTrailingIdx] >= inOpen[BodyDojiTrailingIdx] ? inOpen[BodyDojiTrailingIdx] : inClose[BodyDojiTrailingIdx] ) - inLow[BodyDojiTrailingIdx] ) : 0 ) ) ) ;
         ShadowLongPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowLongTrailingIdx] - inOpen[ShadowLongTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowLongTrailingIdx] - inLow[ShadowLongTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowLongTrailingIdx] - ( inClose[ShadowLongTrailingIdx] >= inOpen[ShadowLongTrailingIdx] ? inClose[ShadowLongTrailingIdx] : inOpen[ShadowLongTrailingIdx] ) ) + ( ( inClose[ShadowLongTrailingIdx] >= inOpen[ShadowLongTrailingIdx] ? inOpen[ShadowLongTrailingIdx] : inClose[ShadowLongTrailingIdx] ) - inLow[ShadowLongTrailingIdx] ) : 0 ) ) ) ;
         NearPeriodTotal += ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[NearTrailingIdx] - inOpen[NearTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[NearTrailingIdx] - inLow[NearTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[NearTrailingIdx] - ( inClose[NearTrailingIdx] >= inOpen[NearTrailingIdx] ? inClose[NearTrailingIdx] : inOpen[NearTrailingIdx] ) ) + ( ( inClose[NearTrailingIdx] >= inOpen[NearTrailingIdx] ? inOpen[NearTrailingIdx] : inClose[NearTrailingIdx] ) - inLow[NearTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyDojiTrailingIdx++;
         ShadowLongTrailingIdx++;
         NearTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlRiseFall3MethodsLookback( )
   {
      return ((( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) + 4;
   }
   public RetCode cdlRiseFall3Methods( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double []BodyPeriodTotal = new double[5] ;
      int i, outIdx, totIdx, BodyShortTrailingIdx, BodyLongTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlRiseFall3MethodsLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyPeriodTotal[4] = 0;
      BodyPeriodTotal[3] = 0;
      BodyPeriodTotal[2] = 0;
      BodyPeriodTotal[1] = 0;
      BodyPeriodTotal[0] = 0;
      BodyShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ;
      BodyLongTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      i = BodyShortTrailingIdx;
      while( i < startIdx ) {
         BodyPeriodTotal[3] += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-3] - inOpen[i-3] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-3] - inLow[i-3] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-3] - ( inClose[i-3] >= inOpen[i-3] ? inClose[i-3] : inOpen[i-3] ) ) + ( ( inClose[i-3] >= inOpen[i-3] ? inOpen[i-3] : inClose[i-3] ) - inLow[i-3] ) : 0 ) ) ) ;
         BodyPeriodTotal[2] += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ;
         BodyPeriodTotal[1] += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         i++;
      }
      i = BodyLongTrailingIdx;
      while( i < startIdx ) {
         BodyPeriodTotal[4] += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-4] - inOpen[i-4] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-4] - inLow[i-4] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-4] - ( inClose[i-4] >= inOpen[i-4] ? inClose[i-4] : inOpen[i-4] ) ) + ( ( inClose[i-4] >= inOpen[i-4] ? inOpen[i-4] : inClose[i-4] ) - inLow[i-4] ) : 0 ) ) ) ;
         BodyPeriodTotal[0] += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if(
            ( Math.abs ( inClose[i-4] - inOpen[i-4] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyPeriodTotal[4] / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-4] - inOpen[i-4] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-4] - inLow[i-4] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-4] - ( inClose[i-4] >= inOpen[i-4] ? inClose[i-4] : inOpen[i-4] ) ) + ( ( inClose[i-4] >= inOpen[i-4] ? inOpen[i-4] : inClose[i-4] ) - inLow[i-4] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i-3] - inOpen[i-3] ) ) < ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) != 0.0? BodyPeriodTotal[3] / (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-3] - inOpen[i-3] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-3] - inLow[i-3] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-3] - ( inClose[i-3] >= inOpen[i-3] ? inClose[i-3] : inOpen[i-3] ) ) + ( ( inClose[i-3] >= inOpen[i-3] ? inOpen[i-3] : inClose[i-3] ) - inLow[i-3] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) < ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) != 0.0? BodyPeriodTotal[2] / (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) < ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) != 0.0? BodyPeriodTotal[1] / (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i] - inOpen[i] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyPeriodTotal[0] / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inClose[i-4] >= inOpen[i-4] ? 1 : -1 ) == - ( inClose[i-3] >= inOpen[i-3] ? 1 : -1 ) &&
            ( inClose[i-3] >= inOpen[i-3] ? 1 : -1 ) == ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) &&
            ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) == ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) &&
            ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == - ( inClose[i] >= inOpen[i] ? 1 : -1 ) &&
            (((inOpen[i-3]) < (inClose[i-3])) ? (inOpen[i-3]) : (inClose[i-3])) < inHigh[i-4] && (((inOpen[i-3]) > (inClose[i-3])) ? (inOpen[i-3]) : (inClose[i-3])) > inLow[i-4] &&
            (((inOpen[i-2]) < (inClose[i-2])) ? (inOpen[i-2]) : (inClose[i-2])) < inHigh[i-4] && (((inOpen[i-2]) > (inClose[i-2])) ? (inOpen[i-2]) : (inClose[i-2])) > inLow[i-4] &&
            (((inOpen[i-1]) < (inClose[i-1])) ? (inOpen[i-1]) : (inClose[i-1])) < inHigh[i-4] && (((inOpen[i-1]) > (inClose[i-1])) ? (inOpen[i-1]) : (inClose[i-1])) > inLow[i-4] &&
            inClose[i-2] * ( inClose[i-4] >= inOpen[i-4] ? 1 : -1 ) < inClose[i-3] * ( inClose[i-4] >= inOpen[i-4] ? 1 : -1 ) &&
            inClose[i-1] * ( inClose[i-4] >= inOpen[i-4] ? 1 : -1 ) < inClose[i-2] * ( inClose[i-4] >= inOpen[i-4] ? 1 : -1 ) &&
            inOpen[i] * ( inClose[i-4] >= inOpen[i-4] ? 1 : -1 ) > inClose[i-1] * ( inClose[i-4] >= inOpen[i-4] ? 1 : -1 ) &&
            inClose[i] * ( inClose[i-4] >= inOpen[i-4] ? 1 : -1 ) > inClose[i-4] * ( inClose[i-4] >= inOpen[i-4] ? 1 : -1 )
            )
            outInteger[outIdx++] = 100 * ( inClose[i-4] >= inOpen[i-4] ? 1 : -1 ) ;
         else
            outInteger[outIdx++] = 0;
         BodyPeriodTotal[4] += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-4] - inOpen[i-4] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-4] - inLow[i-4] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-4] - ( inClose[i-4] >= inOpen[i-4] ? inClose[i-4] : inOpen[i-4] ) ) + ( ( inClose[i-4] >= inOpen[i-4] ? inOpen[i-4] : inClose[i-4] ) - inLow[i-4] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx-4] - inOpen[BodyLongTrailingIdx-4] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx-4] - inLow[BodyLongTrailingIdx-4] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx-4] - ( inClose[BodyLongTrailingIdx-4] >= inOpen[BodyLongTrailingIdx-4] ? inClose[BodyLongTrailingIdx-4] : inOpen[BodyLongTrailingIdx-4] ) ) + ( ( inClose[BodyLongTrailingIdx-4] >= inOpen[BodyLongTrailingIdx-4] ? inOpen[BodyLongTrailingIdx-4] : inClose[BodyLongTrailingIdx-4] ) - inLow[BodyLongTrailingIdx-4] ) : 0 ) ) ) ;
         for (totIdx = 3; totIdx >= 1; --totIdx)
            BodyPeriodTotal[totIdx] += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-totIdx] - inOpen[i-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-totIdx] - inLow[i-totIdx] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-totIdx] - ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inClose[i-totIdx] : inOpen[i-totIdx] ) ) + ( ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inOpen[i-totIdx] : inClose[i-totIdx] ) - inLow[i-totIdx] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyShortTrailingIdx-totIdx] - inOpen[BodyShortTrailingIdx-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyShortTrailingIdx-totIdx] - inLow[BodyShortTrailingIdx-totIdx] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyShortTrailingIdx-totIdx] - ( inClose[BodyShortTrailingIdx-totIdx] >= inOpen[BodyShortTrailingIdx-totIdx] ? inClose[BodyShortTrailingIdx-totIdx] : inOpen[BodyShortTrailingIdx-totIdx] ) ) + ( ( inClose[BodyShortTrailingIdx-totIdx] >= inOpen[BodyShortTrailingIdx-totIdx] ? inOpen[BodyShortTrailingIdx-totIdx] : inClose[BodyShortTrailingIdx-totIdx] ) - inLow[BodyShortTrailingIdx-totIdx] ) : 0 ) ) ) ;
         BodyPeriodTotal[0] += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx] - inOpen[BodyLongTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx] - inLow[BodyLongTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx] - ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inClose[BodyLongTrailingIdx] : inOpen[BodyLongTrailingIdx] ) ) + ( ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inOpen[BodyLongTrailingIdx] : inClose[BodyLongTrailingIdx] ) - inLow[BodyLongTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyShortTrailingIdx++;
         BodyLongTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlSeperatingLinesLookback( )
   {
      return ((( ((( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ) > ( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) )) ? ( ((( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) )) +
         1;
   }
   public RetCode cdlSeperatingLines( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double ShadowVeryShortPeriodTotal, BodyLongPeriodTotal, EqualPeriodTotal;
      int i, outIdx, ShadowVeryShortTrailingIdx, BodyLongTrailingIdx, EqualTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlSeperatingLinesLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      ShadowVeryShortPeriodTotal = 0;
      ShadowVeryShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ;
      BodyLongPeriodTotal = 0;
      BodyLongTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      EqualPeriodTotal = 0;
      EqualTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) ;
      i = ShadowVeryShortTrailingIdx;
      while( i < startIdx ) {
         ShadowVeryShortPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = BodyLongTrailingIdx;
      while( i < startIdx ) {
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = EqualTrailingIdx;
      while( i < startIdx ) {
         EqualPeriodTotal += ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == - ( inClose[i] >= inOpen[i] ? 1 : -1 ) &&
            inOpen[i] <= inOpen[i-1] + ( (this.candleSettings[CandleSettingType.Equal.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) != 0.0? EqualPeriodTotal / (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            inOpen[i] >= inOpen[i-1] - ( (this.candleSettings[CandleSettingType.Equal.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) != 0.0? EqualPeriodTotal / (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i] - inOpen[i] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            (
            ( ( inClose[i] >= inOpen[i] ? 1 : -1 ) == 1 &&
            ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            )
            ||
            ( ( inClose[i] >= inOpen[i] ? 1 : -1 ) == -1 &&
            ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            )
            )
            )
            outInteger[outIdx++] = ( inClose[i] >= inOpen[i] ? 1 : -1 ) * 100;
         else
            outInteger[outIdx++] = 0;
         ShadowVeryShortPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowVeryShortTrailingIdx] - inOpen[ShadowVeryShortTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowVeryShortTrailingIdx] - inLow[ShadowVeryShortTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowVeryShortTrailingIdx] - ( inClose[ShadowVeryShortTrailingIdx] >= inOpen[ShadowVeryShortTrailingIdx] ? inClose[ShadowVeryShortTrailingIdx] : inOpen[ShadowVeryShortTrailingIdx] ) ) + ( ( inClose[ShadowVeryShortTrailingIdx] >= inOpen[ShadowVeryShortTrailingIdx] ? inOpen[ShadowVeryShortTrailingIdx] : inClose[ShadowVeryShortTrailingIdx] ) - inLow[ShadowVeryShortTrailingIdx] ) : 0 ) ) ) ;
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx] - inOpen[BodyLongTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx] - inLow[BodyLongTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx] - ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inClose[BodyLongTrailingIdx] : inOpen[BodyLongTrailingIdx] ) ) + ( ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inOpen[BodyLongTrailingIdx] : inClose[BodyLongTrailingIdx] ) - inLow[BodyLongTrailingIdx] ) : 0 ) ) ) ;
         EqualPeriodTotal += ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[EqualTrailingIdx-1] - inOpen[EqualTrailingIdx-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[EqualTrailingIdx-1] - inLow[EqualTrailingIdx-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[EqualTrailingIdx-1] - ( inClose[EqualTrailingIdx-1] >= inOpen[EqualTrailingIdx-1] ? inClose[EqualTrailingIdx-1] : inOpen[EqualTrailingIdx-1] ) ) + ( ( inClose[EqualTrailingIdx-1] >= inOpen[EqualTrailingIdx-1] ? inOpen[EqualTrailingIdx-1] : inClose[EqualTrailingIdx-1] ) - inLow[EqualTrailingIdx-1] ) : 0 ) ) ) ;
         i++;
         ShadowVeryShortTrailingIdx++;
         BodyLongTrailingIdx++;
         EqualTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlShootingStarLookback( )
   {
      return ((( ((( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ) > ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) )) ? ( ((( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) )) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) )) +
         1;
   }
   public RetCode cdlShootingStar( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyPeriodTotal, ShadowLongPeriodTotal, ShadowVeryShortPeriodTotal;
      int i, outIdx, BodyTrailingIdx, ShadowLongTrailingIdx, ShadowVeryShortTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlShootingStarLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyPeriodTotal = 0;
      BodyTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ;
      ShadowLongPeriodTotal = 0;
      ShadowLongTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) ;
      ShadowVeryShortPeriodTotal = 0;
      ShadowVeryShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ;
      i = BodyTrailingIdx;
      while( i < startIdx ) {
         BodyPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = ShadowLongTrailingIdx;
      while( i < startIdx ) {
         ShadowLongPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = ShadowVeryShortTrailingIdx;
      while( i < startIdx ) {
         ShadowVeryShortPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      outIdx = 0;
      do
      {
         if( ( Math.abs ( inClose[i] - inOpen[i] ) ) < ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) != 0.0? BodyPeriodTotal / (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) > ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) != 0.0? ShadowLongPeriodTotal / (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( (((inOpen[i]) < (inClose[i])) ? (inOpen[i]) : (inClose[i])) > (((inOpen[i-1]) > (inClose[i-1])) ? (inOpen[i-1]) : (inClose[i-1])) ) )
            outInteger[outIdx++] = -100;
         else
            outInteger[outIdx++] = 0;
         BodyPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyTrailingIdx] - inOpen[BodyTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyTrailingIdx] - inLow[BodyTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyTrailingIdx] - ( inClose[BodyTrailingIdx] >= inOpen[BodyTrailingIdx] ? inClose[BodyTrailingIdx] : inOpen[BodyTrailingIdx] ) ) + ( ( inClose[BodyTrailingIdx] >= inOpen[BodyTrailingIdx] ? inOpen[BodyTrailingIdx] : inClose[BodyTrailingIdx] ) - inLow[BodyTrailingIdx] ) : 0 ) ) ) ;
         ShadowLongPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowLongTrailingIdx] - inOpen[ShadowLongTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowLongTrailingIdx] - inLow[ShadowLongTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowLongTrailingIdx] - ( inClose[ShadowLongTrailingIdx] >= inOpen[ShadowLongTrailingIdx] ? inClose[ShadowLongTrailingIdx] : inOpen[ShadowLongTrailingIdx] ) ) + ( ( inClose[ShadowLongTrailingIdx] >= inOpen[ShadowLongTrailingIdx] ? inOpen[ShadowLongTrailingIdx] : inClose[ShadowLongTrailingIdx] ) - inLow[ShadowLongTrailingIdx] ) : 0 ) ) ) ;
         ShadowVeryShortPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowVeryShortTrailingIdx] - inOpen[ShadowVeryShortTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowVeryShortTrailingIdx] - inLow[ShadowVeryShortTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowVeryShortTrailingIdx] - ( inClose[ShadowVeryShortTrailingIdx] >= inOpen[ShadowVeryShortTrailingIdx] ? inClose[ShadowVeryShortTrailingIdx] : inOpen[ShadowVeryShortTrailingIdx] ) ) + ( ( inClose[ShadowVeryShortTrailingIdx] >= inOpen[ShadowVeryShortTrailingIdx] ? inOpen[ShadowVeryShortTrailingIdx] : inClose[ShadowVeryShortTrailingIdx] ) - inLow[ShadowVeryShortTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyTrailingIdx++;
         ShadowLongTrailingIdx++;
         ShadowVeryShortTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlShortLineLookback( )
   {
      return ((( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].avgPeriod) )) ;
   }
   public RetCode cdlShortLine( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyPeriodTotal, ShadowPeriodTotal;
      int i, outIdx, BodyTrailingIdx, ShadowTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlShortLineLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyPeriodTotal = 0;
      BodyTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ;
      ShadowPeriodTotal = 0;
      ShadowTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].avgPeriod) ;
      i = BodyTrailingIdx;
      while( i < startIdx ) {
         BodyPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = ShadowTrailingIdx;
      while( i < startIdx ) {
         ShadowPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      outIdx = 0;
      do
      {
         if( ( Math.abs ( inClose[i] - inOpen[i] ) ) < ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) != 0.0? BodyPeriodTotal / (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) < ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].avgPeriod) != 0.0? ShadowPeriodTotal / (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) < ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].avgPeriod) != 0.0? ShadowPeriodTotal / (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) )
            outInteger[outIdx++] = ( inClose[i] >= inOpen[i] ? 1 : -1 ) * 100;
         else
            outInteger[outIdx++] = 0;
         BodyPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyTrailingIdx] - inOpen[BodyTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyTrailingIdx] - inLow[BodyTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyTrailingIdx] - ( inClose[BodyTrailingIdx] >= inOpen[BodyTrailingIdx] ? inClose[BodyTrailingIdx] : inOpen[BodyTrailingIdx] ) ) + ( ( inClose[BodyTrailingIdx] >= inOpen[BodyTrailingIdx] ? inOpen[BodyTrailingIdx] : inClose[BodyTrailingIdx] ) - inLow[BodyTrailingIdx] ) : 0 ) ) ) ;
         ShadowPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowTrailingIdx] - inOpen[ShadowTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowTrailingIdx] - inLow[ShadowTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowTrailingIdx] - ( inClose[ShadowTrailingIdx] >= inOpen[ShadowTrailingIdx] ? inClose[ShadowTrailingIdx] : inOpen[ShadowTrailingIdx] ) ) + ( ( inClose[ShadowTrailingIdx] >= inOpen[ShadowTrailingIdx] ? inOpen[ShadowTrailingIdx] : inClose[ShadowTrailingIdx] ) - inLow[ShadowTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyTrailingIdx++;
         ShadowTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlSpinningTopLookback( )
   {
      return (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ;
   }
   public RetCode cdlSpinningTop( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyPeriodTotal;
      int i, outIdx, BodyTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlSpinningTopLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyPeriodTotal = 0;
      BodyTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ;
      i = BodyTrailingIdx;
      while( i < startIdx ) {
         BodyPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      outIdx = 0;
      do
      {
         if( ( Math.abs ( inClose[i] - inOpen[i] ) ) < ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) != 0.0? BodyPeriodTotal / (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) > ( Math.abs ( inClose[i] - inOpen[i] ) ) &&
            ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) > ( Math.abs ( inClose[i] - inOpen[i] ) )
            )
            outInteger[outIdx++] = ( inClose[i] >= inOpen[i] ? 1 : -1 ) * 100;
         else
            outInteger[outIdx++] = 0;
         BodyPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyTrailingIdx] - inOpen[BodyTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyTrailingIdx] - inLow[BodyTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyTrailingIdx] - ( inClose[BodyTrailingIdx] >= inOpen[BodyTrailingIdx] ? inClose[BodyTrailingIdx] : inOpen[BodyTrailingIdx] ) ) + ( ( inClose[BodyTrailingIdx] >= inOpen[BodyTrailingIdx] ? inOpen[BodyTrailingIdx] : inClose[BodyTrailingIdx] ) - inLow[BodyTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlStalledPatternLookback( )
   {
      return ((( ((( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) )) ) > ( ((( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) )) )) ? ( ((( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) )) ) : ( ((( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) )) )) +
         2;
   }
   public RetCode cdlStalledPattern( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double []BodyLongPeriodTotal = new double[3] ;
      double []NearPeriodTotal = new double[3] ;
      double BodyShortPeriodTotal, ShadowVeryShortPeriodTotal;
      int i, outIdx, totIdx, BodyLongTrailingIdx, BodyShortTrailingIdx, ShadowVeryShortTrailingIdx, NearTrailingIdx,
         lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlStalledPatternLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyLongPeriodTotal[2] = 0;
      BodyLongPeriodTotal[1] = 0;
      BodyLongPeriodTotal[0] = 0;
      BodyLongTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      BodyShortPeriodTotal = 0;
      BodyShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ;
      ShadowVeryShortPeriodTotal = 0;
      ShadowVeryShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ;
      NearPeriodTotal[2] = 0;
      NearPeriodTotal[1] = 0;
      NearPeriodTotal[0] = 0;
      NearTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) ;
      i = BodyLongTrailingIdx;
      while( i < startIdx ) {
         BodyLongPeriodTotal[2] += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ;
         BodyLongPeriodTotal[1] += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         i++;
      }
      i = BodyShortTrailingIdx;
      while( i < startIdx ) {
         BodyShortPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = ShadowVeryShortTrailingIdx;
      while( i < startIdx ) {
         ShadowVeryShortPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         i++;
      }
      i = NearTrailingIdx;
      while( i < startIdx ) {
         NearPeriodTotal[2] += ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ;
         NearPeriodTotal[1] += ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) == 1 &&
            ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == 1 &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == 1 &&
            inClose[i] > inClose[i-1] && inClose[i-1] > inClose[i-2] &&
            ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal[2] / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal[1] / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            inOpen[i-1] > inOpen[i-2] &&
            inOpen[i-1] <= inClose[i-2] + ( (this.candleSettings[CandleSettingType.Near.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) != 0.0? NearPeriodTotal[2] / (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i] - inOpen[i] ) ) < ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) != 0.0? BodyShortPeriodTotal / (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            inOpen[i] >= inClose[i-1] - ( Math.abs ( inClose[i] - inOpen[i] ) ) - ( (this.candleSettings[CandleSettingType.Near.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) != 0.0? NearPeriodTotal[1] / (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            )
            outInteger[outIdx++] = -100;
         else
            outInteger[outIdx++] = 0;
         for (totIdx = 2; totIdx >= 1; --totIdx) {
            BodyLongPeriodTotal[totIdx] += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-totIdx] - inOpen[i-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-totIdx] - inLow[i-totIdx] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-totIdx] - ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inClose[i-totIdx] : inOpen[i-totIdx] ) ) + ( ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inOpen[i-totIdx] : inClose[i-totIdx] ) - inLow[i-totIdx] ) : 0 ) ) )
               - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx-totIdx] - inOpen[BodyLongTrailingIdx-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx-totIdx] - inLow[BodyLongTrailingIdx-totIdx] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx-totIdx] - ( inClose[BodyLongTrailingIdx-totIdx] >= inOpen[BodyLongTrailingIdx-totIdx] ? inClose[BodyLongTrailingIdx-totIdx] : inOpen[BodyLongTrailingIdx-totIdx] ) ) + ( ( inClose[BodyLongTrailingIdx-totIdx] >= inOpen[BodyLongTrailingIdx-totIdx] ? inOpen[BodyLongTrailingIdx-totIdx] : inClose[BodyLongTrailingIdx-totIdx] ) - inLow[BodyLongTrailingIdx-totIdx] ) : 0 ) ) ) ;
            NearPeriodTotal[totIdx] += ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-totIdx] - inOpen[i-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-totIdx] - inLow[i-totIdx] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-totIdx] - ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inClose[i-totIdx] : inOpen[i-totIdx] ) ) + ( ( inClose[i-totIdx] >= inOpen[i-totIdx] ? inOpen[i-totIdx] : inClose[i-totIdx] ) - inLow[i-totIdx] ) : 0 ) ) )
               - ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[NearTrailingIdx-totIdx] - inOpen[NearTrailingIdx-totIdx] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[NearTrailingIdx-totIdx] - inLow[NearTrailingIdx-totIdx] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[NearTrailingIdx-totIdx] - ( inClose[NearTrailingIdx-totIdx] >= inOpen[NearTrailingIdx-totIdx] ? inClose[NearTrailingIdx-totIdx] : inOpen[NearTrailingIdx-totIdx] ) ) + ( ( inClose[NearTrailingIdx-totIdx] >= inOpen[NearTrailingIdx-totIdx] ? inOpen[NearTrailingIdx-totIdx] : inClose[NearTrailingIdx-totIdx] ) - inLow[NearTrailingIdx-totIdx] ) : 0 ) ) ) ;
         }
         BodyShortPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyShortTrailingIdx] - inOpen[BodyShortTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyShortTrailingIdx] - inLow[BodyShortTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyShortTrailingIdx] - ( inClose[BodyShortTrailingIdx] >= inOpen[BodyShortTrailingIdx] ? inClose[BodyShortTrailingIdx] : inOpen[BodyShortTrailingIdx] ) ) + ( ( inClose[BodyShortTrailingIdx] >= inOpen[BodyShortTrailingIdx] ? inOpen[BodyShortTrailingIdx] : inClose[BodyShortTrailingIdx] ) - inLow[BodyShortTrailingIdx] ) : 0 ) ) ) ;
         ShadowVeryShortPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowVeryShortTrailingIdx-1] - inOpen[ShadowVeryShortTrailingIdx-1] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowVeryShortTrailingIdx-1] - inLow[ShadowVeryShortTrailingIdx-1] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowVeryShortTrailingIdx-1] - ( inClose[ShadowVeryShortTrailingIdx-1] >= inOpen[ShadowVeryShortTrailingIdx-1] ? inClose[ShadowVeryShortTrailingIdx-1] : inOpen[ShadowVeryShortTrailingIdx-1] ) ) + ( ( inClose[ShadowVeryShortTrailingIdx-1] >= inOpen[ShadowVeryShortTrailingIdx-1] ? inOpen[ShadowVeryShortTrailingIdx-1] : inClose[ShadowVeryShortTrailingIdx-1] ) - inLow[ShadowVeryShortTrailingIdx-1] ) : 0 ) ) ) ;
         i++;
         BodyLongTrailingIdx++;
         BodyShortTrailingIdx++;
         ShadowVeryShortTrailingIdx++;
         NearTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlStickSandwhichLookback( )
   {
      return (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) + 2;
   }
   public RetCode cdlStickSandwhich( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double EqualPeriodTotal;
      int i, outIdx, EqualTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlStickSandwhichLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      EqualPeriodTotal = 0;
      EqualTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) ;
      i = EqualTrailingIdx;
      while( i < startIdx ) {
         EqualPeriodTotal += ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) == -1 &&
            ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == 1 &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == -1 &&
            inLow[i-1] > inClose[i-2] &&
            inClose[i] <= inClose[i-2] + ( (this.candleSettings[CandleSettingType.Equal.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) != 0.0? EqualPeriodTotal / (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            inClose[i] >= inClose[i-2] - ( (this.candleSettings[CandleSettingType.Equal.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) != 0.0? EqualPeriodTotal / (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            )
            outInteger[outIdx++] = 100;
         else
            outInteger[outIdx++] = 0;
         EqualPeriodTotal += ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[EqualTrailingIdx-2] - inOpen[EqualTrailingIdx-2] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[EqualTrailingIdx-2] - inLow[EqualTrailingIdx-2] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[EqualTrailingIdx-2] - ( inClose[EqualTrailingIdx-2] >= inOpen[EqualTrailingIdx-2] ? inClose[EqualTrailingIdx-2] : inOpen[EqualTrailingIdx-2] ) ) + ( ( inClose[EqualTrailingIdx-2] >= inOpen[EqualTrailingIdx-2] ? inOpen[EqualTrailingIdx-2] : inClose[EqualTrailingIdx-2] ) - inLow[EqualTrailingIdx-2] ) : 0 ) ) ) ;
         i++;
         EqualTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlTakuriLookback( )
   {
      return ((( ((( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) )) ) > ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].avgPeriod) )) ? ( ((( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) )) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].avgPeriod) )) ;
   }
   public RetCode cdlTakuri( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyDojiPeriodTotal, ShadowVeryShortPeriodTotal, ShadowVeryLongPeriodTotal;
      int i, outIdx, BodyDojiTrailingIdx, ShadowVeryShortTrailingIdx, ShadowVeryLongTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlTakuriLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyDojiPeriodTotal = 0;
      BodyDojiTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ;
      ShadowVeryShortPeriodTotal = 0;
      ShadowVeryShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) ;
      ShadowVeryLongPeriodTotal = 0;
      ShadowVeryLongTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].avgPeriod) ;
      i = BodyDojiTrailingIdx;
      while( i < startIdx ) {
         BodyDojiPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = ShadowVeryShortTrailingIdx;
      while( i < startIdx ) {
         ShadowVeryShortPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = ShadowVeryLongTrailingIdx;
      while( i < startIdx ) {
         ShadowVeryLongPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      outIdx = 0;
      do
      {
         if( ( Math.abs ( inClose[i] - inOpen[i] ) ) <= ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) != 0.0? BodyDojiPeriodTotal / (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) < ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) != 0.0? ShadowVeryShortPeriodTotal / (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) > ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].avgPeriod) != 0.0? ShadowVeryLongPeriodTotal / (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            )
            outInteger[outIdx++] = 100;
         else
            outInteger[outIdx++] = 0;
         BodyDojiPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyDojiTrailingIdx] - inOpen[BodyDojiTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyDojiTrailingIdx] - inLow[BodyDojiTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyDojiTrailingIdx] - ( inClose[BodyDojiTrailingIdx] >= inOpen[BodyDojiTrailingIdx] ? inClose[BodyDojiTrailingIdx] : inOpen[BodyDojiTrailingIdx] ) ) + ( ( inClose[BodyDojiTrailingIdx] >= inOpen[BodyDojiTrailingIdx] ? inOpen[BodyDojiTrailingIdx] : inClose[BodyDojiTrailingIdx] ) - inLow[BodyDojiTrailingIdx] ) : 0 ) ) ) ;
         ShadowVeryShortPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowVeryShortTrailingIdx] - inOpen[ShadowVeryShortTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowVeryShortTrailingIdx] - inLow[ShadowVeryShortTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowVeryShortTrailingIdx] - ( inClose[ShadowVeryShortTrailingIdx] >= inOpen[ShadowVeryShortTrailingIdx] ? inClose[ShadowVeryShortTrailingIdx] : inOpen[ShadowVeryShortTrailingIdx] ) ) + ( ( inClose[ShadowVeryShortTrailingIdx] >= inOpen[ShadowVeryShortTrailingIdx] ? inOpen[ShadowVeryShortTrailingIdx] : inClose[ShadowVeryShortTrailingIdx] ) - inLow[ShadowVeryShortTrailingIdx] ) : 0 ) ) ) ;
         ShadowVeryLongPeriodTotal += ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[ShadowVeryLongTrailingIdx] - inOpen[ShadowVeryLongTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[ShadowVeryLongTrailingIdx] - inLow[ShadowVeryLongTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.ShadowVeryLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[ShadowVeryLongTrailingIdx] - ( inClose[ShadowVeryLongTrailingIdx] >= inOpen[ShadowVeryLongTrailingIdx] ? inClose[ShadowVeryLongTrailingIdx] : inOpen[ShadowVeryLongTrailingIdx] ) ) + ( ( inClose[ShadowVeryLongTrailingIdx] >= inOpen[ShadowVeryLongTrailingIdx] ? inOpen[ShadowVeryLongTrailingIdx] : inClose[ShadowVeryLongTrailingIdx] ) - inLow[ShadowVeryLongTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyDojiTrailingIdx++;
         ShadowVeryShortTrailingIdx++;
         ShadowVeryLongTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlTasukiGapLookback( )
   {
      return (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) + 2;
   }
   public RetCode cdlTasukiGap( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double NearPeriodTotal;
      int i, outIdx, NearTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlTasukiGapLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      NearPeriodTotal = 0;
      NearTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) ;
      i = NearTrailingIdx;
      while( i < startIdx ) {
         NearPeriodTotal += ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if(
            (
            ( (((inOpen[i-1]) < (inClose[i-1])) ? (inOpen[i-1]) : (inClose[i-1])) > (((inOpen[i-2]) > (inClose[i-2])) ? (inOpen[i-2]) : (inClose[i-2])) ) &&
            ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == 1 &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == -1 &&
            inOpen[i] < inClose[i-1] && inOpen[i] > inOpen[i-1] &&
            inClose[i] < inOpen[i-1] &&
            inClose[i] > (((inClose[i-2]) > (inOpen[i-2])) ? (inClose[i-2]) : (inOpen[i-2])) &&
            Math.abs ( ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) - ( Math.abs ( inClose[i] - inOpen[i] ) ) ) < ( (this.candleSettings[CandleSettingType.Near.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) != 0.0? NearPeriodTotal / (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            ) ||
            (
            ( (((inOpen[i-1]) > (inClose[i-1])) ? (inOpen[i-1]) : (inClose[i-1])) < (((inOpen[i-2]) < (inClose[i-2])) ? (inOpen[i-2]) : (inClose[i-2])) ) &&
            ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == -1 &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == 1 &&
            inOpen[i] < inOpen[i-1] && inOpen[i] > inClose[i-1] &&
            inClose[i] > inOpen[i-1] &&
            inClose[i] < (((inClose[i-2]) < (inOpen[i-2])) ? (inClose[i-2]) : (inOpen[i-2])) &&
            Math.abs ( ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) - ( Math.abs ( inClose[i] - inOpen[i] ) ) ) < ( (this.candleSettings[CandleSettingType.Near.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) != 0.0? NearPeriodTotal / (this.candleSettings[CandleSettingType.Near.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) )
            )
            )
            outInteger[outIdx++] = ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) * 100;
         else
            outInteger[outIdx++] = 0;
         NearPeriodTotal += ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[NearTrailingIdx-1] - inOpen[NearTrailingIdx-1] ) ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[NearTrailingIdx-1] - inLow[NearTrailingIdx-1] ) : ( (this.candleSettings[CandleSettingType.Near.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[NearTrailingIdx-1] - ( inClose[NearTrailingIdx-1] >= inOpen[NearTrailingIdx-1] ? inClose[NearTrailingIdx-1] : inOpen[NearTrailingIdx-1] ) ) + ( ( inClose[NearTrailingIdx-1] >= inOpen[NearTrailingIdx-1] ? inOpen[NearTrailingIdx-1] : inClose[NearTrailingIdx-1] ) - inLow[NearTrailingIdx-1] ) : 0 ) ) ) ;
         i++;
         NearTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlThrustingLookback( )
   {
      return ((( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) +
         1;
   }
   public RetCode cdlThrusting( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double EqualPeriodTotal, BodyLongPeriodTotal;
      int i, outIdx, EqualTrailingIdx, BodyLongTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlThrustingLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      EqualPeriodTotal = 0;
      EqualTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) ;
      BodyLongPeriodTotal = 0;
      BodyLongTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      i = EqualTrailingIdx;
      while( i < startIdx ) {
         EqualPeriodTotal += ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         i++;
      }
      i = BodyLongTrailingIdx;
      while( i < startIdx ) {
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == -1 &&
            ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == 1 &&
            inOpen[i] < inLow[i-1] &&
            inClose[i] > inClose[i-1] + ( (this.candleSettings[CandleSettingType.Equal.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) != 0.0? EqualPeriodTotal / (this.candleSettings[CandleSettingType.Equal.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            inClose[i] <= inClose[i-1] + ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) * 0.5
            )
            outInteger[outIdx++] = -100;
         else
            outInteger[outIdx++] = 0;
         EqualPeriodTotal += ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[EqualTrailingIdx-1] - inOpen[EqualTrailingIdx-1] ) ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[EqualTrailingIdx-1] - inLow[EqualTrailingIdx-1] ) : ( (this.candleSettings[CandleSettingType.Equal.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[EqualTrailingIdx-1] - ( inClose[EqualTrailingIdx-1] >= inOpen[EqualTrailingIdx-1] ? inClose[EqualTrailingIdx-1] : inOpen[EqualTrailingIdx-1] ) ) + ( ( inClose[EqualTrailingIdx-1] >= inOpen[EqualTrailingIdx-1] ? inOpen[EqualTrailingIdx-1] : inClose[EqualTrailingIdx-1] ) - inLow[EqualTrailingIdx-1] ) : 0 ) ) ) ;
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) )
            - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx-1] - inOpen[BodyLongTrailingIdx-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx-1] - inLow[BodyLongTrailingIdx-1] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx-1] - ( inClose[BodyLongTrailingIdx-1] >= inOpen[BodyLongTrailingIdx-1] ? inClose[BodyLongTrailingIdx-1] : inOpen[BodyLongTrailingIdx-1] ) ) + ( ( inClose[BodyLongTrailingIdx-1] >= inOpen[BodyLongTrailingIdx-1] ? inOpen[BodyLongTrailingIdx-1] : inClose[BodyLongTrailingIdx-1] ) - inLow[BodyLongTrailingIdx-1] ) : 0 ) ) ) ;
         i++;
         EqualTrailingIdx++;
         BodyLongTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlTristarLookback( )
   {
      return (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) + 2;
   }
   public RetCode cdlTristar( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyPeriodTotal;
      int i, outIdx, BodyTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlTristarLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyPeriodTotal = 0;
      BodyTrailingIdx = startIdx -2 - (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) ;
      i = BodyTrailingIdx;
      while( i < startIdx-2 ) {
         BodyPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) <= ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) != 0.0? BodyPeriodTotal / (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) <= ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) != 0.0? BodyPeriodTotal / (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( Math.abs ( inClose[i] - inOpen[i] ) ) <= ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) != 0.0? BodyPeriodTotal / (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) ) {
            outInteger[outIdx] = 0;
            if ( ( (((inOpen[i-1]) < (inClose[i-1])) ? (inOpen[i-1]) : (inClose[i-1])) > (((inOpen[i-2]) > (inClose[i-2])) ? (inOpen[i-2]) : (inClose[i-2])) )
               &&
               (((inOpen[i]) > (inClose[i])) ? (inOpen[i]) : (inClose[i])) < (((inOpen[i-1]) > (inClose[i-1])) ? (inOpen[i-1]) : (inClose[i-1]))
               )
               outInteger[outIdx] = -100;
            if ( ( (((inOpen[i-1]) > (inClose[i-1])) ? (inOpen[i-1]) : (inClose[i-1])) < (((inOpen[i-2]) < (inClose[i-2])) ? (inOpen[i-2]) : (inClose[i-2])) )
               &&
               (((inOpen[i]) < (inClose[i])) ? (inOpen[i]) : (inClose[i])) > (((inOpen[i-1]) < (inClose[i-1])) ? (inOpen[i-1]) : (inClose[i-1]))
               )
               outInteger[outIdx] = +100;
            outIdx++;
         }
         else
            outInteger[outIdx++] = 0;
         BodyPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyTrailingIdx] - inOpen[BodyTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyTrailingIdx] - inLow[BodyTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyDoji.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyTrailingIdx] - ( inClose[BodyTrailingIdx] >= inOpen[BodyTrailingIdx] ? inClose[BodyTrailingIdx] : inOpen[BodyTrailingIdx] ) ) + ( ( inClose[BodyTrailingIdx] >= inOpen[BodyTrailingIdx] ? inOpen[BodyTrailingIdx] : inClose[BodyTrailingIdx] ) - inLow[BodyTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlUnique3RiverLookback( )
   {
      return ((( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) + 2;
   }
   public RetCode cdlUnique3River( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyShortPeriodTotal, BodyLongPeriodTotal;
      int i, outIdx, BodyShortTrailingIdx, BodyLongTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlUnique3RiverLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyLongPeriodTotal = 0;
      BodyShortPeriodTotal = 0;
      BodyLongTrailingIdx = startIdx -2 - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      BodyShortTrailingIdx = startIdx - (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ;
      i = BodyLongTrailingIdx;
      while( i < startIdx-2 ) {
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = BodyShortTrailingIdx;
      while( i < startIdx ) {
         BodyShortPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) == -1 &&
            ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == -1 &&
            inClose[i-1] > inClose[i-2] && inOpen[i-1] <= inOpen[i-2] &&
            inLow[i-1] < inLow[i-2] &&
            ( Math.abs ( inClose[i] - inOpen[i] ) ) < ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) != 0.0? BodyShortPeriodTotal / (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == 1 &&
            inOpen[i] > inLow[i-1]
            )
            outInteger[outIdx++] = 100;
         else
            outInteger[outIdx++] = 0;
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx] - inOpen[BodyLongTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx] - inLow[BodyLongTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx] - ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inClose[BodyLongTrailingIdx] : inOpen[BodyLongTrailingIdx] ) ) + ( ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inOpen[BodyLongTrailingIdx] : inClose[BodyLongTrailingIdx] ) - inLow[BodyLongTrailingIdx] ) : 0 ) ) ) ;
         BodyShortPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyShortTrailingIdx] - inOpen[BodyShortTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyShortTrailingIdx] - inLow[BodyShortTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyShortTrailingIdx] - ( inClose[BodyShortTrailingIdx] >= inOpen[BodyShortTrailingIdx] ? inClose[BodyShortTrailingIdx] : inOpen[BodyShortTrailingIdx] ) ) + ( ( inClose[BodyShortTrailingIdx] >= inOpen[BodyShortTrailingIdx] ? inOpen[BodyShortTrailingIdx] : inClose[BodyShortTrailingIdx] ) - inLow[BodyShortTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyLongTrailingIdx++;
         BodyShortTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlUpsideGap2CrowsLookback( )
   {
      return ((( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) ? ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) )) + 2;
   }
   public RetCode cdlUpsideGap2Crows( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double BodyShortPeriodTotal, BodyLongPeriodTotal;
      int i, outIdx, BodyShortTrailingIdx, BodyLongTrailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlUpsideGap2CrowsLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      BodyLongPeriodTotal = 0;
      BodyShortPeriodTotal = 0;
      BodyLongTrailingIdx = startIdx -2 - (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) ;
      BodyShortTrailingIdx = startIdx -1 - (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) ;
      i = BodyLongTrailingIdx;
      while( i < startIdx-2 ) {
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = BodyShortTrailingIdx;
      while( i < startIdx-1 ) {
         BodyShortPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i] - inOpen[i] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i] - inLow[i] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i] - ( inClose[i] >= inOpen[i] ? inClose[i] : inOpen[i] ) ) + ( ( inClose[i] >= inOpen[i] ? inOpen[i] : inClose[i] ) - inLow[i] ) : 0 ) ) ) ;
         i++;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) == 1 &&
            ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) > ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) != 0.0? BodyLongPeriodTotal / (this.candleSettings[CandleSettingType.BodyLong.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == -1 &&
            ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) <= ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].factor) * ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) != 0.0? BodyShortPeriodTotal / (this.candleSettings[CandleSettingType.BodyShort.ordinal()].avgPeriod) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) ) / ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? 2.0 : 1.0 ) ) &&
            ( (((inOpen[i-1]) < (inClose[i-1])) ? (inOpen[i-1]) : (inClose[i-1])) > (((inOpen[i-2]) > (inClose[i-2])) ? (inOpen[i-2]) : (inClose[i-2])) ) &&
            ( inClose[i] >= inOpen[i] ? 1 : -1 ) == -1 &&
            inOpen[i] > inOpen[i-1] && inClose[i] < inClose[i-1] &&
            inClose[i] > inClose[i-2]
            )
            outInteger[outIdx++] = -100;
         else
            outInteger[outIdx++] = 0;
         BodyLongPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-2] - inOpen[i-2] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-2] - inLow[i-2] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-2] - ( inClose[i-2] >= inOpen[i-2] ? inClose[i-2] : inOpen[i-2] ) ) + ( ( inClose[i-2] >= inOpen[i-2] ? inOpen[i-2] : inClose[i-2] ) - inLow[i-2] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyLongTrailingIdx] - inOpen[BodyLongTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyLongTrailingIdx] - inLow[BodyLongTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyLong.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyLongTrailingIdx] - ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inClose[BodyLongTrailingIdx] : inOpen[BodyLongTrailingIdx] ) ) + ( ( inClose[BodyLongTrailingIdx] >= inOpen[BodyLongTrailingIdx] ? inOpen[BodyLongTrailingIdx] : inClose[BodyLongTrailingIdx] ) - inLow[BodyLongTrailingIdx] ) : 0 ) ) ) ;
         BodyShortPeriodTotal += ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[i-1] - inOpen[i-1] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[i-1] - inLow[i-1] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[i-1] - ( inClose[i-1] >= inOpen[i-1] ? inClose[i-1] : inOpen[i-1] ) ) + ( ( inClose[i-1] >= inOpen[i-1] ? inOpen[i-1] : inClose[i-1] ) - inLow[i-1] ) : 0 ) ) ) - ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.RealBody ? ( Math.abs ( inClose[BodyShortTrailingIdx] - inOpen[BodyShortTrailingIdx] ) ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.HighLow ? ( inHigh[BodyShortTrailingIdx] - inLow[BodyShortTrailingIdx] ) : ( (this.candleSettings[CandleSettingType.BodyShort.ordinal()].rangeType) == RangeType.Shadows ? ( inHigh[BodyShortTrailingIdx] - ( inClose[BodyShortTrailingIdx] >= inOpen[BodyShortTrailingIdx] ? inClose[BodyShortTrailingIdx] : inOpen[BodyShortTrailingIdx] ) ) + ( ( inClose[BodyShortTrailingIdx] >= inOpen[BodyShortTrailingIdx] ? inOpen[BodyShortTrailingIdx] : inClose[BodyShortTrailingIdx] ) - inLow[BodyShortTrailingIdx] ) : 0 ) ) ) ;
         i++;
         BodyLongTrailingIdx++;
         BodyShortTrailingIdx++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cdlXSideGap3MethodsLookback( )
   {
      return 2;
   }
   public RetCode cdlXSideGap3Methods( int startIdx,
      int endIdx,
      double inOpen[],
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      int i, outIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      lookbackTotal = cdlXSideGap3MethodsLookback ();
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      i = startIdx;
      outIdx = 0;
      do
      {
         if( ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) == ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) &&
            ( inClose[i-1] >= inOpen[i-1] ? 1 : -1 ) == - ( inClose[i] >= inOpen[i] ? 1 : -1 ) &&
            inOpen[i] < (((inClose[i-1]) > (inOpen[i-1])) ? (inClose[i-1]) : (inOpen[i-1])) &&
            inOpen[i] > (((inClose[i-1]) < (inOpen[i-1])) ? (inClose[i-1]) : (inOpen[i-1])) &&
            inClose[i] < (((inClose[i-2]) > (inOpen[i-2])) ? (inClose[i-2]) : (inOpen[i-2])) &&
            inClose[i] > (((inClose[i-2]) < (inOpen[i-2])) ? (inClose[i-2]) : (inOpen[i-2])) &&
            ( (
            ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) == 1 &&
            ( (((inOpen[i-1]) < (inClose[i-1])) ? (inOpen[i-1]) : (inClose[i-1])) > (((inOpen[i-2]) > (inClose[i-2])) ? (inOpen[i-2]) : (inClose[i-2])) )
            ) ||
            (
            ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) == -1 &&
            ( (((inOpen[i-1]) > (inClose[i-1])) ? (inOpen[i-1]) : (inClose[i-1])) < (((inOpen[i-2]) < (inClose[i-2])) ? (inOpen[i-2]) : (inClose[i-2])) )
            )
            )
            )
            outInteger[outIdx++] = ( inClose[i-2] >= inOpen[i-2] ? 1 : -1 ) * 100;
         else
            outInteger[outIdx++] = 0;
         i++;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int ceilLookback( )
   {
      return 0;
   }
   public RetCode ceil( int startIdx,
      int endIdx,
      double inReal[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx;
      int i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      for( i=startIdx, outIdx=0; i <= endIdx; i++, outIdx++ )
      {
         outReal[outIdx] = Math.ceil (inReal[i]);
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cmoLookback( int optInTimePeriod )
   {
      int retValue;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      retValue = optInTimePeriod + (this.unstablePeriod[FuncUnstId.Cmo.ordinal()]) ;
      if( (this.compatibility) == Compatibility.Metastock )
         retValue--;
      return retValue;
   }
   public RetCode cmo( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx;
      int today, lookbackTotal, unstablePeriod, i;
      double prevGain, prevLoss, prevValue, savePrevValue;
      double tempValue1, tempValue2, tempValue3, tempValue4;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      outBegIdx.value = 0 ;
      outNBElement.value = 0 ;
      lookbackTotal = cmoLookback ( optInTimePeriod );
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
         return RetCode.Success ;
      outIdx = 0;
      if( optInTimePeriod == 1 )
      {
         outBegIdx.value = startIdx;
         i = (endIdx-startIdx)+1;
         outNBElement.value = i;
         System.arraycopy(inReal,startIdx,outReal,0,i) ;
         return RetCode.Success ;
      }
      today = startIdx-lookbackTotal;
      prevValue = inReal[today];
      unstablePeriod = (this.unstablePeriod[FuncUnstId.Cmo.ordinal()]) ;
      if( (unstablePeriod == 0) &&
         ( (this.compatibility) == Compatibility.Metastock ))
      {
         savePrevValue = prevValue;
         prevGain = 0.0;
         prevLoss = 0.0;
         for( i=optInTimePeriod; i > 0; i-- )
         {
            tempValue1 = inReal[today++];
            tempValue2 = tempValue1 - prevValue;
            prevValue = tempValue1;
            if( tempValue2 < 0 )
               prevLoss -= tempValue2;
            else
               prevGain += tempValue2;
         }
         tempValue1 = prevLoss/optInTimePeriod;
         tempValue2 = prevGain/optInTimePeriod;
         tempValue3 = tempValue2-tempValue1;
         tempValue4 = tempValue1+tempValue2;
         if( ! (((-0.00000001)<tempValue4)&&(tempValue4<0.00000001)) )
            outReal[outIdx++] = 100*(tempValue3/tempValue4);
         else
            outReal[outIdx++] = 0.0;
         if( today > endIdx )
         {
            outBegIdx.value = startIdx;
            outNBElement.value = outIdx;
            return RetCode.Success ;
         }
         today -= optInTimePeriod;
         prevValue = savePrevValue;
      }
      prevGain = 0.0;
      prevLoss = 0.0;
      today++;
      for( i=optInTimePeriod; i > 0; i-- )
      {
         tempValue1 = inReal[today++];
         tempValue2 = tempValue1 - prevValue;
         prevValue = tempValue1;
         if( tempValue2 < 0 )
            prevLoss -= tempValue2;
         else
            prevGain += tempValue2;
      }
      prevLoss /= optInTimePeriod;
      prevGain /= optInTimePeriod;
      if( today > startIdx )
      {
         tempValue1 = prevGain+prevLoss;
         if( ! (((-0.00000001)<tempValue1)&&(tempValue1<0.00000001)) )
            outReal[outIdx++] = 100.0*((prevGain-prevLoss)/tempValue1);
         else
            outReal[outIdx++] = 0.0;
      }
      else
      {
         while( today < startIdx )
         {
            tempValue1 = inReal[today];
            tempValue2 = tempValue1 - prevValue;
            prevValue = tempValue1;
            prevLoss *= (optInTimePeriod-1);
            prevGain *= (optInTimePeriod-1);
            if( tempValue2 < 0 )
               prevLoss -= tempValue2;
            else
               prevGain += tempValue2;
            prevLoss /= optInTimePeriod;
            prevGain /= optInTimePeriod;
            today++;
         }
      }
      while( today <= endIdx )
      {
         tempValue1 = inReal[today++];
         tempValue2 = tempValue1 - prevValue;
         prevValue = tempValue1;
         prevLoss *= (optInTimePeriod-1);
         prevGain *= (optInTimePeriod-1);
         if( tempValue2 < 0 )
            prevLoss -= tempValue2;
         else
            prevGain += tempValue2;
         prevLoss /= optInTimePeriod;
         prevGain /= optInTimePeriod;
         tempValue1 = prevGain+prevLoss;
         if( ! (((-0.00000001)<tempValue1)&&(tempValue1<0.00000001)) )
            outReal[outIdx++] = 100.0*((prevGain-prevLoss)/tempValue1);
         else
            outReal[outIdx++] = 0.0;
      }
      outBegIdx.value = startIdx;
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int correlLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return -1;
      return optInTimePeriod-1;
   }
   public RetCode correl( int startIdx,
      int endIdx,
      double inReal0[],
      double inReal1[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      double sumXY, sumX, sumY, sumX2, sumY2, x, y, trailingX, trailingY;
      double tempReal;
      int lookbackTotal, today, trailingIdx, outIdx;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      lookbackTotal = optInTimePeriod-1;
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outBegIdx.value = startIdx;
      trailingIdx = startIdx - lookbackTotal;
      sumXY = sumX = sumY = sumX2 = sumY2 = 0.0;
      for( today=trailingIdx; today <= startIdx; today++ )
      {
         x = inReal0[today];
         sumX += x;
         sumX2 += x*x;
         y = inReal1[today];
         sumXY += x*y;
         sumY += y;
         sumY2 += y*y;
      }
      trailingX = inReal0[trailingIdx];
      trailingY = inReal1[trailingIdx++];
      tempReal = (sumX2-((sumX*sumX)/optInTimePeriod)) * (sumY2-((sumY*sumY)/optInTimePeriod));
      if( ! (tempReal<0.00000001) )
         outReal[0] = (sumXY-((sumX*sumY)/optInTimePeriod)) / Math.sqrt (tempReal);
      else
         outReal[0] = 0.0;
      outIdx = 1;
      while( today <= endIdx )
      {
         sumX -= trailingX;
         sumX2 -= trailingX*trailingX;
         sumXY -= trailingX*trailingY;
         sumY -= trailingY;
         sumY2 -= trailingY*trailingY;
         x = inReal0[today];
         sumX += x;
         sumX2 += x*x;
         y = inReal1[today++];
         sumXY += x*y;
         sumY += y;
         sumY2 += y*y;
         trailingX = inReal0[trailingIdx];
         trailingY = inReal1[trailingIdx++];
         tempReal = (sumX2-((sumX*sumX)/optInTimePeriod)) * (sumY2-((sumY*sumY)/optInTimePeriod));
         if( ! (tempReal<0.00000001) )
            outReal[outIdx++] = (sumXY-((sumX*sumY)/optInTimePeriod)) / Math.sqrt (tempReal);
         else
            outReal[outIdx++] = 0.0;
      }
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int cosLookback( )
   {
      return 0;
   }
   public RetCode cos( int startIdx,
      int endIdx,
      double inReal[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx;
      int i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      for( i=startIdx, outIdx=0; i <= endIdx; i++, outIdx++ )
      {
         outReal[outIdx] = Math.cos (inReal[i]);
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int coshLookback( )
   {
      return 0;
   }
   public RetCode cosh( int startIdx,
      int endIdx,
      double inReal[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx;
      int i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      for( i=startIdx, outIdx=0; i <= endIdx; i++, outIdx++ )
      {
         outReal[outIdx] = Math.cosh (inReal[i]);
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int demaLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      return emaLookback ( optInTimePeriod ) * 2;
   }
   public RetCode dema( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      double []firstEMA ;
      double []secondEMA ;
      double k;
      MInteger firstEMABegIdx = new MInteger() ;
      MInteger firstEMANbElement = new MInteger() ;
      MInteger secondEMABegIdx = new MInteger() ;
      MInteger secondEMANbElement = new MInteger() ;
      int tempInt, outIdx, firstEMAIdx, lookbackTotal, lookbackEMA;
      RetCode retCode;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      outNBElement.value = 0 ;
      outBegIdx.value = 0 ;
      lookbackEMA = emaLookback ( optInTimePeriod );
      lookbackTotal = lookbackEMA * 2;
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
         return RetCode.Success ;
      if( inReal == outReal )
         firstEMA = outReal;
      else
      {
         tempInt = lookbackTotal+(endIdx-startIdx)+1;
         firstEMA = new double[tempInt] ;
      }
      k = ((double)2.0 / ((double)(optInTimePeriod + 1))) ;
      retCode = TA_INT_EMA ( startIdx-lookbackEMA, endIdx, inReal,
         optInTimePeriod, k,
         firstEMABegIdx , firstEMANbElement ,
         firstEMA );
      if( (retCode != RetCode.Success ) || ( firstEMANbElement.value == 0) )
      {
         return retCode;
      }
      secondEMA = new double[firstEMANbElement.value] ;
      retCode = TA_INT_EMA ( 0, firstEMANbElement.value -1, firstEMA,
         optInTimePeriod, k,
         secondEMABegIdx , secondEMANbElement ,
         secondEMA );
      if( (retCode != RetCode.Success ) || ( secondEMANbElement.value == 0) )
      {
         return retCode;
      }
      firstEMAIdx = secondEMABegIdx.value ;
      outIdx = 0;
      while( outIdx < secondEMANbElement.value )
      {
         outReal[outIdx] = (2.0*firstEMA[firstEMAIdx++]) - secondEMA[outIdx];
         outIdx++;
      }
      outBegIdx.value = firstEMABegIdx.value + secondEMABegIdx.value ;
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int divLookback( )
   {
      return 0;
   }
   public RetCode div( int startIdx,
      int endIdx,
      double inReal0[],
      double inReal1[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx;
      int i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      for( i=startIdx, outIdx=0; i <= endIdx; i++, outIdx++ )
      {
         outReal[outIdx] = inReal0[i]/inReal1[i];
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int dxLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      if( optInTimePeriod > 1 )
         return optInTimePeriod + (this.unstablePeriod[FuncUnstId.Dx.ordinal()]) ;
      else
         return 2;
   }
   public RetCode dx( int startIdx,
      int endIdx,
      double inHigh[],
      double inLow[],
      double inClose[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int today, lookbackTotal, outIdx;
      double prevHigh, prevLow, prevClose;
      double prevMinusDM, prevPlusDM, prevTR;
      double tempReal, tempReal2, diffP, diffM;
      double minusDI, plusDI;
      int i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      if( optInTimePeriod > 1 )
         lookbackTotal = optInTimePeriod + (this.unstablePeriod[FuncUnstId.Dx.ordinal()]) ;
      else
         lookbackTotal = 2;
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      outBegIdx.value = today = startIdx;
      prevMinusDM = 0.0;
      prevPlusDM = 0.0;
      prevTR = 0.0;
      today = startIdx - lookbackTotal;
      prevHigh = inHigh[today];
      prevLow = inLow[today];
      prevClose = inClose[today];
      i = optInTimePeriod-1;
      while( i-- > 0 )
      {
         today++;
         tempReal = inHigh[today];
         diffP = tempReal-prevHigh;
         prevHigh = tempReal;
         tempReal = inLow[today];
         diffM = prevLow-tempReal;
         prevLow = tempReal;
         if( (diffM > 0) && (diffP < diffM) )
         {
            prevMinusDM += diffM;
         }
         else if( (diffP > 0) && (diffP > diffM) )
         {
            prevPlusDM += diffP;
         }
         { tempReal = prevHigh-prevLow; tempReal2 = Math.abs (prevHigh-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; tempReal2 = Math.abs (prevLow-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; } ;
         prevTR += tempReal;
         prevClose = inClose[today];
      }
      i = (this.unstablePeriod[FuncUnstId.Dx.ordinal()]) + 1;
      while( i-- != 0 )
      {
         today++;
         tempReal = inHigh[today];
         diffP = tempReal-prevHigh;
         prevHigh = tempReal;
         tempReal = inLow[today];
         diffM = prevLow-tempReal;
         prevLow = tempReal;
         prevMinusDM -= prevMinusDM/optInTimePeriod;
         prevPlusDM -= prevPlusDM/optInTimePeriod;
         if( (diffM > 0) && (diffP < diffM) )
         {
            prevMinusDM += diffM;
         }
         else if( (diffP > 0) && (diffP > diffM) )
         {
            prevPlusDM += diffP;
         }
         { tempReal = prevHigh-prevLow; tempReal2 = Math.abs (prevHigh-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; tempReal2 = Math.abs (prevLow-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; } ;
         prevTR = prevTR - (prevTR/optInTimePeriod) + tempReal;
         prevClose = inClose[today];
      }
      if( ! (((-0.00000001)<prevTR)&&(prevTR<0.00000001)) )
      {
         minusDI = (100.0*(prevMinusDM/prevTR)) ;
         plusDI = (100.0*(prevPlusDM/prevTR)) ;
         tempReal = minusDI+plusDI;
         if( ! (((-0.00000001)<tempReal)&&(tempReal<0.00000001)) )
            outReal[0] = (100.0 * ( Math.abs (minusDI-plusDI)/tempReal)) ;
         else
            outReal[0] = 0.0;
      }
      else
         outReal[0] = 0.0;
      outIdx = 1;
      while( today < endIdx )
      {
         today++;
         tempReal = inHigh[today];
         diffP = tempReal-prevHigh;
         prevHigh = tempReal;
         tempReal = inLow[today];
         diffM = prevLow-tempReal;
         prevLow = tempReal;
         prevMinusDM -= prevMinusDM/optInTimePeriod;
         prevPlusDM -= prevPlusDM/optInTimePeriod;
         if( (diffM > 0) && (diffP < diffM) )
         {
            prevMinusDM += diffM;
         }
         else if( (diffP > 0) && (diffP > diffM) )
         {
            prevPlusDM += diffP;
         }
         { tempReal = prevHigh-prevLow; tempReal2 = Math.abs (prevHigh-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; tempReal2 = Math.abs (prevLow-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; } ;
         prevTR = prevTR - (prevTR/optInTimePeriod) + tempReal;
         prevClose = inClose[today];
         if( ! (((-0.00000001)<prevTR)&&(prevTR<0.00000001)) )
         {
            minusDI = (100.0*(prevMinusDM/prevTR)) ;
            plusDI = (100.0*(prevPlusDM/prevTR)) ;
            tempReal = minusDI+plusDI;
            if( ! (((-0.00000001)<tempReal)&&(tempReal<0.00000001)) )
               outReal[outIdx] = (100.0 * ( Math.abs (minusDI-plusDI)/tempReal)) ;
            else
               outReal[outIdx] = outReal[outIdx-1];
         }
         else
            outReal[outIdx] = outReal[outIdx-1];
         outIdx++;
      }
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int emaLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      return optInTimePeriod - 1 + (this.unstablePeriod[FuncUnstId.Ema.ordinal()]) ;
   }
   public RetCode ema( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      return TA_INT_EMA ( startIdx, endIdx, inReal,
         optInTimePeriod,
         ((double)2.0 / ((double)(optInTimePeriod + 1))) ,
         outBegIdx, outNBElement, outReal );
   }
   public RetCode TA_INT_EMA( int startIdx,
      int endIdx,
      double []inReal,
      int optInTimePeriod,
      double optInK_1,
      MInteger outBegIdx,
      MInteger outNBElement,
      double []outReal )
   {
      double tempReal, prevMA;
      int i, today, outIdx, lookbackTotal;
      lookbackTotal = emaLookback ( optInTimePeriod );
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outBegIdx.value = startIdx;
      if( (this.compatibility) == Compatibility.Default )
      {
         today = startIdx-lookbackTotal;
         i = optInTimePeriod;
         tempReal = 0.0;
         while( i-- > 0 )
            tempReal += inReal[today++];
         prevMA = tempReal / optInTimePeriod;
      }
      else
      {
         prevMA = inReal[0];
         today = 1;
      }
      while( today <= startIdx )
         prevMA = ((inReal[today++]-prevMA)*optInK_1) + prevMA;
      outReal[0] = prevMA;
      outIdx = 1;
      while( today <= endIdx )
      {
         prevMA = ((inReal[today++]-prevMA)*optInK_1) + prevMA;
         outReal[outIdx++] = prevMA;
      }
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   
   /* Generated */
   public int expLookback( )
   {
      return 0;
   }
   public RetCode exp( int startIdx,
      int endIdx,
      double inReal[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx;
      int i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      for( i=startIdx, outIdx=0; i <= endIdx; i++, outIdx++ )
      {
         outReal[outIdx] = Math.exp (inReal[i]);
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int floorLookback( )
   {
      return 0;
   }
   public RetCode floor( int startIdx,
      int endIdx,
      double inReal[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx;
      int i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      for( i=startIdx, outIdx=0; i <= endIdx; i++, outIdx++ )
      {
         outReal[outIdx] = Math.floor (inReal[i]);
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int htDcPeriodLookback( )
   {
      return 32 + (this.unstablePeriod[FuncUnstId.HtDcPeriod.ordinal()]) ;
   }
   public RetCode htDcPeriod( int startIdx,
      int endIdx,
      double inReal[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx, i;
      int lookbackTotal, today;
      double tempReal, tempReal2;
      double adjustedPrevPeriod, period;
      int trailingWMAIdx;
      double periodWMASum, periodWMASub, trailingWMAValue;
      double smoothedValue;
      final double a = 0.0962;
      final double b = 0.5769;
      double hilbertTempReal;
      int hilbertIdx;
      double []detrender_Odd = new double[3] ; double []detrender_Even = new double[3] ; double detrender; double prev_detrender_Odd ; double prev_detrender_Even ; double prev_detrender_input_Odd ; double prev_detrender_input_Even ;
      double []Q1_Odd = new double[3] ; double []Q1_Even = new double[3] ; double Q1; double prev_Q1_Odd ; double prev_Q1_Even ; double prev_Q1_input_Odd ; double prev_Q1_input_Even ;
      double []jI_Odd = new double[3] ; double []jI_Even = new double[3] ; double jI; double prev_jI_Odd ; double prev_jI_Even ; double prev_jI_input_Odd ; double prev_jI_input_Even ;
      double []jQ_Odd = new double[3] ; double []jQ_Even = new double[3] ; double jQ; double prev_jQ_Odd ; double prev_jQ_Even ; double prev_jQ_input_Odd ; double prev_jQ_input_Even ;
      double Q2, I2, prevQ2, prevI2, Re, Im;
      double I1ForOddPrev2, I1ForOddPrev3;
      double I1ForEvenPrev2, I1ForEvenPrev3;
      double rad2Deg;
      double todayValue, smoothPeriod;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      rad2Deg = 180.0 / (4.0 * Math.atan (1));
      lookbackTotal = 32 + (this.unstablePeriod[FuncUnstId.HtDcPeriod.ordinal()]) ;
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outBegIdx.value = startIdx;
      trailingWMAIdx = startIdx - lookbackTotal;
      today = trailingWMAIdx;
      tempReal = inReal[today++];
      periodWMASub = tempReal;
      periodWMASum = tempReal;
      tempReal = inReal[today++];
      periodWMASub += tempReal;
      periodWMASum += tempReal*2.0;
      tempReal = inReal[today++];
      periodWMASub += tempReal;
      periodWMASum += tempReal*3.0;
      trailingWMAValue = 0.0;
      i = 9;
      do
      {
         tempReal = inReal[today++];
         { periodWMASub += tempReal; periodWMASub -= trailingWMAValue; periodWMASum += tempReal*4.0; trailingWMAValue = inReal[trailingWMAIdx++]; smoothedValue = periodWMASum*0.1; periodWMASum -= periodWMASub; } ;
      } while( --i != 0);
      hilbertIdx = 0;
      { detrender_Odd [0] = 0.0; detrender_Odd [1] = 0.0; detrender_Odd [2] = 0.0; detrender_Even [0] = 0.0; detrender_Even [1] = 0.0; detrender_Even [2] = 0.0; detrender = 0.0; prev_detrender_Odd = 0.0; prev_detrender_Even = 0.0; prev_detrender_input_Odd = 0.0; prev_detrender_input_Even = 0.0; } ;
      { Q1_Odd [0] = 0.0; Q1_Odd [1] = 0.0; Q1_Odd [2] = 0.0; Q1_Even [0] = 0.0; Q1_Even [1] = 0.0; Q1_Even [2] = 0.0; Q1 = 0.0; prev_Q1_Odd = 0.0; prev_Q1_Even = 0.0; prev_Q1_input_Odd = 0.0; prev_Q1_input_Even = 0.0; } ;
      { jI_Odd [0] = 0.0; jI_Odd [1] = 0.0; jI_Odd [2] = 0.0; jI_Even [0] = 0.0; jI_Even [1] = 0.0; jI_Even [2] = 0.0; jI = 0.0; prev_jI_Odd = 0.0; prev_jI_Even = 0.0; prev_jI_input_Odd = 0.0; prev_jI_input_Even = 0.0; } ;
      { jQ_Odd [0] = 0.0; jQ_Odd [1] = 0.0; jQ_Odd [2] = 0.0; jQ_Even [0] = 0.0; jQ_Even [1] = 0.0; jQ_Even [2] = 0.0; jQ = 0.0; prev_jQ_Odd = 0.0; prev_jQ_Even = 0.0; prev_jQ_input_Odd = 0.0; prev_jQ_input_Even = 0.0; } ;
      period = 0.0;
      outIdx = 0;
      prevI2 = prevQ2 = 0.0;
      Re = Im = 0.0;
      I1ForOddPrev3 = I1ForEvenPrev3 = 0.0;
      I1ForOddPrev2 = I1ForEvenPrev2 = 0.0;
      smoothPeriod = 0.0;
      while( today <= endIdx )
      {
         adjustedPrevPeriod = (0.075*period)+0.54;
         todayValue = inReal[today];
         { periodWMASub += todayValue; periodWMASub -= trailingWMAValue; periodWMASum += todayValue*4.0; trailingWMAValue = inReal[trailingWMAIdx++]; smoothedValue = periodWMASum*0.1; periodWMASum -= periodWMASub; } ;
         if( (today%2) == 0 )
         {
            { hilbertTempReal = a * smoothedValue; detrender = -detrender_Even [hilbertIdx]; detrender_Even [hilbertIdx] = hilbertTempReal; detrender += hilbertTempReal; detrender -= prev_detrender_Even ; prev_detrender_Even = b * prev_detrender_input_Even ; detrender += prev_detrender_Even ; prev_detrender_input_Even = smoothedValue; detrender *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * detrender; Q1 = -Q1_Even [hilbertIdx]; Q1_Even [hilbertIdx] = hilbertTempReal; Q1 += hilbertTempReal; Q1 -= prev_Q1_Even ; prev_Q1_Even = b * prev_Q1_input_Even ; Q1 += prev_Q1_Even ; prev_Q1_input_Even = detrender; Q1 *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * I1ForEvenPrev3; jI = -jI_Even [hilbertIdx]; jI_Even [hilbertIdx] = hilbertTempReal; jI += hilbertTempReal; jI -= prev_jI_Even ; prev_jI_Even = b * prev_jI_input_Even ; jI += prev_jI_Even ; prev_jI_input_Even = I1ForEvenPrev3; jI *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * Q1; jQ = -jQ_Even [hilbertIdx]; jQ_Even [hilbertIdx] = hilbertTempReal; jQ += hilbertTempReal; jQ -= prev_jQ_Even ; prev_jQ_Even = b * prev_jQ_input_Even ; jQ += prev_jQ_Even ; prev_jQ_input_Even = Q1; jQ *= adjustedPrevPeriod; } ;
            if( ++hilbertIdx == 3 )
               hilbertIdx = 0;
            Q2 = (0.2*(Q1 + jI)) + (0.8*prevQ2);
            I2 = (0.2*(I1ForEvenPrev3 - jQ)) + (0.8*prevI2);
            I1ForOddPrev3 = I1ForOddPrev2;
            I1ForOddPrev2 = detrender;
         }
         else
         {
            { hilbertTempReal = a * smoothedValue; detrender = -detrender_Odd [hilbertIdx]; detrender_Odd [hilbertIdx] = hilbertTempReal; detrender += hilbertTempReal; detrender -= prev_detrender_Odd ; prev_detrender_Odd = b * prev_detrender_input_Odd ; detrender += prev_detrender_Odd ; prev_detrender_input_Odd = smoothedValue; detrender *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * detrender; Q1 = -Q1_Odd [hilbertIdx]; Q1_Odd [hilbertIdx] = hilbertTempReal; Q1 += hilbertTempReal; Q1 -= prev_Q1_Odd ; prev_Q1_Odd = b * prev_Q1_input_Odd ; Q1 += prev_Q1_Odd ; prev_Q1_input_Odd = detrender; Q1 *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * I1ForOddPrev3; jI = -jI_Odd [hilbertIdx]; jI_Odd [hilbertIdx] = hilbertTempReal; jI += hilbertTempReal; jI -= prev_jI_Odd ; prev_jI_Odd = b * prev_jI_input_Odd ; jI += prev_jI_Odd ; prev_jI_input_Odd = I1ForOddPrev3; jI *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * Q1; jQ = -jQ_Odd [hilbertIdx]; jQ_Odd [hilbertIdx] = hilbertTempReal; jQ += hilbertTempReal; jQ -= prev_jQ_Odd ; prev_jQ_Odd = b * prev_jQ_input_Odd ; jQ += prev_jQ_Odd ; prev_jQ_input_Odd = Q1; jQ *= adjustedPrevPeriod; } ;
            Q2 = (0.2*(Q1 + jI)) + (0.8*prevQ2);
            I2 = (0.2*(I1ForOddPrev3 - jQ)) + (0.8*prevI2);
            I1ForEvenPrev3 = I1ForEvenPrev2;
            I1ForEvenPrev2 = detrender;
         }
         Re = (0.2*((I2*prevI2)+(Q2*prevQ2)))+(0.8*Re);
         Im = (0.2*((I2*prevQ2)-(Q2*prevI2)))+(0.8*Im);
         prevQ2 = Q2;
         prevI2 = I2;
         tempReal = period;
         if( (Im != 0.0) && (Re != 0.0) )
            period = 360.0 / ( Math.atan (Im/Re)*rad2Deg);
         tempReal2 = 1.5*tempReal;
         if( period > tempReal2)
            period = tempReal2;
         tempReal2 = 0.67*tempReal;
         if( period < tempReal2 )
            period = tempReal2;
         if( period < 6 )
            period = 6;
         else if( period > 50 )
            period = 50;
         period = (0.2*period) + (0.8 * tempReal);
         smoothPeriod = (0.33*period)+(0.67*smoothPeriod);
         if( today >= startIdx )
         {
            outReal[outIdx++] = smoothPeriod;
         }
         today++;
      }
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int htDcPhaseLookback( )
   {
      return 63 + (this.unstablePeriod[FuncUnstId.HtDcPhase.ordinal()]) ;
   }
   public RetCode htDcPhase( int startIdx,
      int endIdx,
      double inReal[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx, i;
      int lookbackTotal, today;
      double tempReal, tempReal2;
      double adjustedPrevPeriod, period;
      int trailingWMAIdx;
      double periodWMASum, periodWMASub, trailingWMAValue;
      double smoothedValue;
      final double a = 0.0962;
      final double b = 0.5769;
      double hilbertTempReal;
      int hilbertIdx;
      double []detrender_Odd = new double[3] ; double []detrender_Even = new double[3] ; double detrender; double prev_detrender_Odd ; double prev_detrender_Even ; double prev_detrender_input_Odd ; double prev_detrender_input_Even ;
      double []Q1_Odd = new double[3] ; double []Q1_Even = new double[3] ; double Q1; double prev_Q1_Odd ; double prev_Q1_Even ; double prev_Q1_input_Odd ; double prev_Q1_input_Even ;
      double []jI_Odd = new double[3] ; double []jI_Even = new double[3] ; double jI; double prev_jI_Odd ; double prev_jI_Even ; double prev_jI_input_Odd ; double prev_jI_input_Even ;
      double []jQ_Odd = new double[3] ; double []jQ_Even = new double[3] ; double jQ; double prev_jQ_Odd ; double prev_jQ_Even ; double prev_jQ_input_Odd ; double prev_jQ_input_Even ;
      double Q2, I2, prevQ2, prevI2, Re, Im;
      double I1ForOddPrev2, I1ForOddPrev3;
      double I1ForEvenPrev2, I1ForEvenPrev3;
      double rad2Deg, constDeg2RadBy360;
      double todayValue, smoothPeriod;
      int smoothPrice_Idx = 0; double []smoothPrice; int maxIdx_smoothPrice = ( 50 -1) ;
      int idx;
      int DCPeriodInt;
      double DCPhase, DCPeriod, imagPart, realPart;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      { smoothPrice = new double[maxIdx_smoothPrice +1]; } ;
      tempReal = Math.atan (1);
      rad2Deg = 45.0/tempReal;
      constDeg2RadBy360 = tempReal*8.0;
      lookbackTotal = 63 + (this.unstablePeriod[FuncUnstId.HtDcPhase.ordinal()]) ;
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outBegIdx.value = startIdx;
      trailingWMAIdx = startIdx - lookbackTotal;
      today = trailingWMAIdx;
      tempReal = inReal[today++];
      periodWMASub = tempReal;
      periodWMASum = tempReal;
      tempReal = inReal[today++];
      periodWMASub += tempReal;
      periodWMASum += tempReal*2.0;
      tempReal = inReal[today++];
      periodWMASub += tempReal;
      periodWMASum += tempReal*3.0;
      trailingWMAValue = 0.0;
      i = 34;
      do
      {
         tempReal = inReal[today++];
         { periodWMASub += tempReal; periodWMASub -= trailingWMAValue; periodWMASum += tempReal*4.0; trailingWMAValue = inReal[trailingWMAIdx++]; smoothedValue = periodWMASum*0.1; periodWMASum -= periodWMASub; } ;
      } while( --i != 0);
      hilbertIdx = 0;
      { detrender_Odd [0] = 0.0; detrender_Odd [1] = 0.0; detrender_Odd [2] = 0.0; detrender_Even [0] = 0.0; detrender_Even [1] = 0.0; detrender_Even [2] = 0.0; detrender = 0.0; prev_detrender_Odd = 0.0; prev_detrender_Even = 0.0; prev_detrender_input_Odd = 0.0; prev_detrender_input_Even = 0.0; } ;
      { Q1_Odd [0] = 0.0; Q1_Odd [1] = 0.0; Q1_Odd [2] = 0.0; Q1_Even [0] = 0.0; Q1_Even [1] = 0.0; Q1_Even [2] = 0.0; Q1 = 0.0; prev_Q1_Odd = 0.0; prev_Q1_Even = 0.0; prev_Q1_input_Odd = 0.0; prev_Q1_input_Even = 0.0; } ;
      { jI_Odd [0] = 0.0; jI_Odd [1] = 0.0; jI_Odd [2] = 0.0; jI_Even [0] = 0.0; jI_Even [1] = 0.0; jI_Even [2] = 0.0; jI = 0.0; prev_jI_Odd = 0.0; prev_jI_Even = 0.0; prev_jI_input_Odd = 0.0; prev_jI_input_Even = 0.0; } ;
      { jQ_Odd [0] = 0.0; jQ_Odd [1] = 0.0; jQ_Odd [2] = 0.0; jQ_Even [0] = 0.0; jQ_Even [1] = 0.0; jQ_Even [2] = 0.0; jQ = 0.0; prev_jQ_Odd = 0.0; prev_jQ_Even = 0.0; prev_jQ_input_Odd = 0.0; prev_jQ_input_Even = 0.0; } ;
      period = 0.0;
      outIdx = 0;
      prevI2 = prevQ2 = 0.0;
      Re = Im = 0.0;
      I1ForOddPrev3 = I1ForEvenPrev3 = 0.0;
      I1ForOddPrev2 = I1ForEvenPrev2 = 0.0;
      smoothPeriod = 0.0;
      for( i=0; i < 50 ; i++ )
         smoothPrice[i] = 0.0;
      DCPhase = 0.0;
      while( today <= endIdx )
      {
         adjustedPrevPeriod = (0.075*period)+0.54;
         todayValue = inReal[today];
         { periodWMASub += todayValue; periodWMASub -= trailingWMAValue; periodWMASum += todayValue*4.0; trailingWMAValue = inReal[trailingWMAIdx++]; smoothedValue = periodWMASum*0.1; periodWMASum -= periodWMASub; } ;
         smoothPrice[smoothPrice_Idx] = smoothedValue;
         if( (today%2) == 0 )
         {
            { hilbertTempReal = a * smoothedValue; detrender = -detrender_Even [hilbertIdx]; detrender_Even [hilbertIdx] = hilbertTempReal; detrender += hilbertTempReal; detrender -= prev_detrender_Even ; prev_detrender_Even = b * prev_detrender_input_Even ; detrender += prev_detrender_Even ; prev_detrender_input_Even = smoothedValue; detrender *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * detrender; Q1 = -Q1_Even [hilbertIdx]; Q1_Even [hilbertIdx] = hilbertTempReal; Q1 += hilbertTempReal; Q1 -= prev_Q1_Even ; prev_Q1_Even = b * prev_Q1_input_Even ; Q1 += prev_Q1_Even ; prev_Q1_input_Even = detrender; Q1 *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * I1ForEvenPrev3; jI = -jI_Even [hilbertIdx]; jI_Even [hilbertIdx] = hilbertTempReal; jI += hilbertTempReal; jI -= prev_jI_Even ; prev_jI_Even = b * prev_jI_input_Even ; jI += prev_jI_Even ; prev_jI_input_Even = I1ForEvenPrev3; jI *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * Q1; jQ = -jQ_Even [hilbertIdx]; jQ_Even [hilbertIdx] = hilbertTempReal; jQ += hilbertTempReal; jQ -= prev_jQ_Even ; prev_jQ_Even = b * prev_jQ_input_Even ; jQ += prev_jQ_Even ; prev_jQ_input_Even = Q1; jQ *= adjustedPrevPeriod; } ;
            if( ++hilbertIdx == 3 )
               hilbertIdx = 0;
            Q2 = (0.2*(Q1 + jI)) + (0.8*prevQ2);
            I2 = (0.2*(I1ForEvenPrev3 - jQ)) + (0.8*prevI2);
            I1ForOddPrev3 = I1ForOddPrev2;
            I1ForOddPrev2 = detrender;
         }
         else
         {
            { hilbertTempReal = a * smoothedValue; detrender = -detrender_Odd [hilbertIdx]; detrender_Odd [hilbertIdx] = hilbertTempReal; detrender += hilbertTempReal; detrender -= prev_detrender_Odd ; prev_detrender_Odd = b * prev_detrender_input_Odd ; detrender += prev_detrender_Odd ; prev_detrender_input_Odd = smoothedValue; detrender *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * detrender; Q1 = -Q1_Odd [hilbertIdx]; Q1_Odd [hilbertIdx] = hilbertTempReal; Q1 += hilbertTempReal; Q1 -= prev_Q1_Odd ; prev_Q1_Odd = b * prev_Q1_input_Odd ; Q1 += prev_Q1_Odd ; prev_Q1_input_Odd = detrender; Q1 *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * I1ForOddPrev3; jI = -jI_Odd [hilbertIdx]; jI_Odd [hilbertIdx] = hilbertTempReal; jI += hilbertTempReal; jI -= prev_jI_Odd ; prev_jI_Odd = b * prev_jI_input_Odd ; jI += prev_jI_Odd ; prev_jI_input_Odd = I1ForOddPrev3; jI *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * Q1; jQ = -jQ_Odd [hilbertIdx]; jQ_Odd [hilbertIdx] = hilbertTempReal; jQ += hilbertTempReal; jQ -= prev_jQ_Odd ; prev_jQ_Odd = b * prev_jQ_input_Odd ; jQ += prev_jQ_Odd ; prev_jQ_input_Odd = Q1; jQ *= adjustedPrevPeriod; } ;
            Q2 = (0.2*(Q1 + jI)) + (0.8*prevQ2);
            I2 = (0.2*(I1ForOddPrev3 - jQ)) + (0.8*prevI2);
            I1ForEvenPrev3 = I1ForEvenPrev2;
            I1ForEvenPrev2 = detrender;
         }
         Re = (0.2*((I2*prevI2)+(Q2*prevQ2)))+(0.8*Re);
         Im = (0.2*((I2*prevQ2)-(Q2*prevI2)))+(0.8*Im);
         prevQ2 = Q2;
         prevI2 = I2;
         tempReal = period;
         if( (Im != 0.0) && (Re != 0.0) )
            period = 360.0 / ( Math.atan (Im/Re)*rad2Deg);
         tempReal2 = 1.5*tempReal;
         if( period > tempReal2)
            period = tempReal2;
         tempReal2 = 0.67*tempReal;
         if( period < tempReal2 )
            period = tempReal2;
         if( period < 6 )
            period = 6;
         else if( period > 50 )
            period = 50;
         period = (0.2*period) + (0.8 * tempReal);
         smoothPeriod = (0.33*period)+(0.67*smoothPeriod);
         DCPeriod = smoothPeriod+0.5;
         DCPeriodInt = (int)DCPeriod;
         realPart = 0.0;
         imagPart = 0.0;
         idx = smoothPrice_Idx;
         for( i=0; i < DCPeriodInt; i++ )
         {
            tempReal = ((double)i*constDeg2RadBy360)/(double)DCPeriodInt;
            tempReal2 = smoothPrice[idx];
            realPart += Math.sin (tempReal)*tempReal2;
            imagPart += Math.cos (tempReal)*tempReal2;
            if( idx == 0 )
               idx = 50 -1;
            else
               idx--;
         }
         tempReal = Math.abs (imagPart);
         if( tempReal > 0.0 )
            DCPhase = Math.atan (realPart/imagPart)*rad2Deg;
         else if( tempReal <= 0.01 )
         {
            if( realPart < 0.0 )
               DCPhase -= 90.0;
            else if( realPart > 0.0 )
               DCPhase += 90.0;
         }
         DCPhase += 90.0;
         DCPhase += 360.0 / smoothPeriod;
         if( imagPart < 0.0 )
            DCPhase += 180.0;
         if( DCPhase > 315.0 )
            DCPhase -= 360.0;
         if( today >= startIdx )
         {
            outReal[outIdx++] = DCPhase;
         }
         { smoothPrice_Idx ++; if( smoothPrice_Idx > maxIdx_smoothPrice ) smoothPrice_Idx = 0; } ;
         today++;
      }
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int htPhasorLookback( )
   {
      return 32 + (this.unstablePeriod[FuncUnstId.HtPhasor.ordinal()]) ;
   }
   public RetCode htPhasor( int startIdx,
      int endIdx,
      double inReal[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outInPhase[],
      double outQuadrature[] )
   {
      int outIdx, i;
      int lookbackTotal, today;
      double tempReal, tempReal2;
      double adjustedPrevPeriod, period;
      int trailingWMAIdx;
      double periodWMASum, periodWMASub, trailingWMAValue;
      double smoothedValue;
      final double a = 0.0962;
      final double b = 0.5769;
      double hilbertTempReal;
      int hilbertIdx;
      double []detrender_Odd = new double[3] ; double []detrender_Even = new double[3] ; double detrender; double prev_detrender_Odd ; double prev_detrender_Even ; double prev_detrender_input_Odd ; double prev_detrender_input_Even ;
      double []Q1_Odd = new double[3] ; double []Q1_Even = new double[3] ; double Q1; double prev_Q1_Odd ; double prev_Q1_Even ; double prev_Q1_input_Odd ; double prev_Q1_input_Even ;
      double []jI_Odd = new double[3] ; double []jI_Even = new double[3] ; double jI; double prev_jI_Odd ; double prev_jI_Even ; double prev_jI_input_Odd ; double prev_jI_input_Even ;
      double []jQ_Odd = new double[3] ; double []jQ_Even = new double[3] ; double jQ; double prev_jQ_Odd ; double prev_jQ_Even ; double prev_jQ_input_Odd ; double prev_jQ_input_Even ;
      double Q2, I2, prevQ2, prevI2, Re, Im;
      double I1ForOddPrev2, I1ForOddPrev3;
      double I1ForEvenPrev2, I1ForEvenPrev3;
      double rad2Deg;
      double todayValue;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      rad2Deg = 180.0 / (4.0 * Math.atan (1));
      lookbackTotal = 32 + (this.unstablePeriod[FuncUnstId.HtPhasor.ordinal()]) ;
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outBegIdx.value = startIdx;
      trailingWMAIdx = startIdx - lookbackTotal;
      today = trailingWMAIdx;
      tempReal = inReal[today++];
      periodWMASub = tempReal;
      periodWMASum = tempReal;
      tempReal = inReal[today++];
      periodWMASub += tempReal;
      periodWMASum += tempReal*2.0;
      tempReal = inReal[today++];
      periodWMASub += tempReal;
      periodWMASum += tempReal*3.0;
      trailingWMAValue = 0.0;
      i = 9;
      do
      {
         tempReal = inReal[today++];
         { periodWMASub += tempReal; periodWMASub -= trailingWMAValue; periodWMASum += tempReal*4.0; trailingWMAValue = inReal[trailingWMAIdx++]; smoothedValue = periodWMASum*0.1; periodWMASum -= periodWMASub; } ;
      } while( --i != 0);
      hilbertIdx = 0;
      { detrender_Odd [0] = 0.0; detrender_Odd [1] = 0.0; detrender_Odd [2] = 0.0; detrender_Even [0] = 0.0; detrender_Even [1] = 0.0; detrender_Even [2] = 0.0; detrender = 0.0; prev_detrender_Odd = 0.0; prev_detrender_Even = 0.0; prev_detrender_input_Odd = 0.0; prev_detrender_input_Even = 0.0; } ;
      { Q1_Odd [0] = 0.0; Q1_Odd [1] = 0.0; Q1_Odd [2] = 0.0; Q1_Even [0] = 0.0; Q1_Even [1] = 0.0; Q1_Even [2] = 0.0; Q1 = 0.0; prev_Q1_Odd = 0.0; prev_Q1_Even = 0.0; prev_Q1_input_Odd = 0.0; prev_Q1_input_Even = 0.0; } ;
      { jI_Odd [0] = 0.0; jI_Odd [1] = 0.0; jI_Odd [2] = 0.0; jI_Even [0] = 0.0; jI_Even [1] = 0.0; jI_Even [2] = 0.0; jI = 0.0; prev_jI_Odd = 0.0; prev_jI_Even = 0.0; prev_jI_input_Odd = 0.0; prev_jI_input_Even = 0.0; } ;
      { jQ_Odd [0] = 0.0; jQ_Odd [1] = 0.0; jQ_Odd [2] = 0.0; jQ_Even [0] = 0.0; jQ_Even [1] = 0.0; jQ_Even [2] = 0.0; jQ = 0.0; prev_jQ_Odd = 0.0; prev_jQ_Even = 0.0; prev_jQ_input_Odd = 0.0; prev_jQ_input_Even = 0.0; } ;
      period = 0.0;
      outIdx = 0;
      prevI2 = prevQ2 = 0.0;
      Re = Im = 0.0;
      I1ForOddPrev3 = I1ForEvenPrev3 = 0.0;
      I1ForOddPrev2 = I1ForEvenPrev2 = 0.0;
      while( today <= endIdx )
      {
         adjustedPrevPeriod = (0.075*period)+0.54;
         todayValue = inReal[today];
         { periodWMASub += todayValue; periodWMASub -= trailingWMAValue; periodWMASum += todayValue*4.0; trailingWMAValue = inReal[trailingWMAIdx++]; smoothedValue = periodWMASum*0.1; periodWMASum -= periodWMASub; } ;
         if( (today%2) == 0 )
         {
            { hilbertTempReal = a * smoothedValue; detrender = -detrender_Even [hilbertIdx]; detrender_Even [hilbertIdx] = hilbertTempReal; detrender += hilbertTempReal; detrender -= prev_detrender_Even ; prev_detrender_Even = b * prev_detrender_input_Even ; detrender += prev_detrender_Even ; prev_detrender_input_Even = smoothedValue; detrender *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * detrender; Q1 = -Q1_Even [hilbertIdx]; Q1_Even [hilbertIdx] = hilbertTempReal; Q1 += hilbertTempReal; Q1 -= prev_Q1_Even ; prev_Q1_Even = b * prev_Q1_input_Even ; Q1 += prev_Q1_Even ; prev_Q1_input_Even = detrender; Q1 *= adjustedPrevPeriod; } ;
            if( today >= startIdx )
            {
               outQuadrature[outIdx] = Q1;
               outInPhase[outIdx++] = I1ForEvenPrev3;
            }
            { hilbertTempReal = a * I1ForEvenPrev3; jI = -jI_Even [hilbertIdx]; jI_Even [hilbertIdx] = hilbertTempReal; jI += hilbertTempReal; jI -= prev_jI_Even ; prev_jI_Even = b * prev_jI_input_Even ; jI += prev_jI_Even ; prev_jI_input_Even = I1ForEvenPrev3; jI *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * Q1; jQ = -jQ_Even [hilbertIdx]; jQ_Even [hilbertIdx] = hilbertTempReal; jQ += hilbertTempReal; jQ -= prev_jQ_Even ; prev_jQ_Even = b * prev_jQ_input_Even ; jQ += prev_jQ_Even ; prev_jQ_input_Even = Q1; jQ *= adjustedPrevPeriod; } ;
            if( ++hilbertIdx == 3 )
               hilbertIdx = 0;
            Q2 = (0.2*(Q1 + jI)) + (0.8*prevQ2);
            I2 = (0.2*(I1ForEvenPrev3 - jQ)) + (0.8*prevI2);
            I1ForOddPrev3 = I1ForOddPrev2;
            I1ForOddPrev2 = detrender;
         }
         else
         {
            { hilbertTempReal = a * smoothedValue; detrender = -detrender_Odd [hilbertIdx]; detrender_Odd [hilbertIdx] = hilbertTempReal; detrender += hilbertTempReal; detrender -= prev_detrender_Odd ; prev_detrender_Odd = b * prev_detrender_input_Odd ; detrender += prev_detrender_Odd ; prev_detrender_input_Odd = smoothedValue; detrender *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * detrender; Q1 = -Q1_Odd [hilbertIdx]; Q1_Odd [hilbertIdx] = hilbertTempReal; Q1 += hilbertTempReal; Q1 -= prev_Q1_Odd ; prev_Q1_Odd = b * prev_Q1_input_Odd ; Q1 += prev_Q1_Odd ; prev_Q1_input_Odd = detrender; Q1 *= adjustedPrevPeriod; } ;
            if( today >= startIdx )
            {
               outQuadrature[outIdx] = Q1;
               outInPhase[outIdx++] = I1ForOddPrev3;
            }
            { hilbertTempReal = a * I1ForOddPrev3; jI = -jI_Odd [hilbertIdx]; jI_Odd [hilbertIdx] = hilbertTempReal; jI += hilbertTempReal; jI -= prev_jI_Odd ; prev_jI_Odd = b * prev_jI_input_Odd ; jI += prev_jI_Odd ; prev_jI_input_Odd = I1ForOddPrev3; jI *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * Q1; jQ = -jQ_Odd [hilbertIdx]; jQ_Odd [hilbertIdx] = hilbertTempReal; jQ += hilbertTempReal; jQ -= prev_jQ_Odd ; prev_jQ_Odd = b * prev_jQ_input_Odd ; jQ += prev_jQ_Odd ; prev_jQ_input_Odd = Q1; jQ *= adjustedPrevPeriod; } ;
            Q2 = (0.2*(Q1 + jI)) + (0.8*prevQ2);
            I2 = (0.2*(I1ForOddPrev3 - jQ)) + (0.8*prevI2);
            I1ForEvenPrev3 = I1ForEvenPrev2;
            I1ForEvenPrev2 = detrender;
         }
         Re = (0.2*((I2*prevI2)+(Q2*prevQ2)))+(0.8*Re);
         Im = (0.2*((I2*prevQ2)-(Q2*prevI2)))+(0.8*Im);
         prevQ2 = Q2;
         prevI2 = I2;
         tempReal = period;
         if( (Im != 0.0) && (Re != 0.0) )
            period = 360.0 / ( Math.atan (Im/Re)*rad2Deg);
         tempReal2 = 1.5*tempReal;
         if( period > tempReal2)
            period = tempReal2;
         tempReal2 = 0.67*tempReal;
         if( period < tempReal2 )
            period = tempReal2;
         if( period < 6 )
            period = 6;
         else if( period > 50 )
            period = 50;
         period = (0.2*period) + (0.8 * tempReal);
         today++;
      }
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int htSineLookback( )
   {
      return 63 + (this.unstablePeriod[FuncUnstId.HtSine.ordinal()]) ;
   }
   public RetCode htSine( int startIdx,
      int endIdx,
      double inReal[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outSine[],
      double outLeadSine[] )
   {
      int outIdx, i;
      int lookbackTotal, today;
      double tempReal, tempReal2;
      double adjustedPrevPeriod, period;
      int trailingWMAIdx;
      double periodWMASum, periodWMASub, trailingWMAValue;
      double smoothedValue;
      final double a = 0.0962;
      final double b = 0.5769;
      double hilbertTempReal;
      int hilbertIdx;
      double []detrender_Odd = new double[3] ; double []detrender_Even = new double[3] ; double detrender; double prev_detrender_Odd ; double prev_detrender_Even ; double prev_detrender_input_Odd ; double prev_detrender_input_Even ;
      double []Q1_Odd = new double[3] ; double []Q1_Even = new double[3] ; double Q1; double prev_Q1_Odd ; double prev_Q1_Even ; double prev_Q1_input_Odd ; double prev_Q1_input_Even ;
      double []jI_Odd = new double[3] ; double []jI_Even = new double[3] ; double jI; double prev_jI_Odd ; double prev_jI_Even ; double prev_jI_input_Odd ; double prev_jI_input_Even ;
      double []jQ_Odd = new double[3] ; double []jQ_Even = new double[3] ; double jQ; double prev_jQ_Odd ; double prev_jQ_Even ; double prev_jQ_input_Odd ; double prev_jQ_input_Even ;
      double Q2, I2, prevQ2, prevI2, Re, Im;
      double I1ForOddPrev2, I1ForOddPrev3;
      double I1ForEvenPrev2, I1ForEvenPrev3;
      double rad2Deg, deg2Rad, constDeg2RadBy360;
      double todayValue, smoothPeriod;
      int smoothPrice_Idx = 0; double []smoothPrice; int maxIdx_smoothPrice = ( 50 -1) ;
      int idx;
      int DCPeriodInt;
      double DCPhase, DCPeriod, imagPart, realPart;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      { smoothPrice = new double[maxIdx_smoothPrice +1]; } ;
      tempReal = Math.atan (1);
      rad2Deg = 45.0/tempReal;
      deg2Rad = 1.0/rad2Deg;
      constDeg2RadBy360 = tempReal*8.0;
      lookbackTotal = 63 + (this.unstablePeriod[FuncUnstId.HtSine.ordinal()]) ;
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outBegIdx.value = startIdx;
      trailingWMAIdx = startIdx - lookbackTotal;
      today = trailingWMAIdx;
      tempReal = inReal[today++];
      periodWMASub = tempReal;
      periodWMASum = tempReal;
      tempReal = inReal[today++];
      periodWMASub += tempReal;
      periodWMASum += tempReal*2.0;
      tempReal = inReal[today++];
      periodWMASub += tempReal;
      periodWMASum += tempReal*3.0;
      trailingWMAValue = 0.0;
      i = 34;
      do
      {
         tempReal = inReal[today++];
         { periodWMASub += tempReal; periodWMASub -= trailingWMAValue; periodWMASum += tempReal*4.0; trailingWMAValue = inReal[trailingWMAIdx++]; smoothedValue = periodWMASum*0.1; periodWMASum -= periodWMASub; } ;
      } while( --i != 0);
      hilbertIdx = 0;
      { detrender_Odd [0] = 0.0; detrender_Odd [1] = 0.0; detrender_Odd [2] = 0.0; detrender_Even [0] = 0.0; detrender_Even [1] = 0.0; detrender_Even [2] = 0.0; detrender = 0.0; prev_detrender_Odd = 0.0; prev_detrender_Even = 0.0; prev_detrender_input_Odd = 0.0; prev_detrender_input_Even = 0.0; } ;
      { Q1_Odd [0] = 0.0; Q1_Odd [1] = 0.0; Q1_Odd [2] = 0.0; Q1_Even [0] = 0.0; Q1_Even [1] = 0.0; Q1_Even [2] = 0.0; Q1 = 0.0; prev_Q1_Odd = 0.0; prev_Q1_Even = 0.0; prev_Q1_input_Odd = 0.0; prev_Q1_input_Even = 0.0; } ;
      { jI_Odd [0] = 0.0; jI_Odd [1] = 0.0; jI_Odd [2] = 0.0; jI_Even [0] = 0.0; jI_Even [1] = 0.0; jI_Even [2] = 0.0; jI = 0.0; prev_jI_Odd = 0.0; prev_jI_Even = 0.0; prev_jI_input_Odd = 0.0; prev_jI_input_Even = 0.0; } ;
      { jQ_Odd [0] = 0.0; jQ_Odd [1] = 0.0; jQ_Odd [2] = 0.0; jQ_Even [0] = 0.0; jQ_Even [1] = 0.0; jQ_Even [2] = 0.0; jQ = 0.0; prev_jQ_Odd = 0.0; prev_jQ_Even = 0.0; prev_jQ_input_Odd = 0.0; prev_jQ_input_Even = 0.0; } ;
      period = 0.0;
      outIdx = 0;
      prevI2 = prevQ2 = 0.0;
      Re = Im = 0.0;
      I1ForOddPrev3 = I1ForEvenPrev3 = 0.0;
      I1ForOddPrev2 = I1ForEvenPrev2 = 0.0;
      smoothPeriod = 0.0;
      for( i=0; i < 50 ; i++ )
         smoothPrice[i] = 0.0;
      DCPhase = 0.0;
      while( today <= endIdx )
      {
         adjustedPrevPeriod = (0.075*period)+0.54;
         todayValue = inReal[today];
         { periodWMASub += todayValue; periodWMASub -= trailingWMAValue; periodWMASum += todayValue*4.0; trailingWMAValue = inReal[trailingWMAIdx++]; smoothedValue = periodWMASum*0.1; periodWMASum -= periodWMASub; } ;
         smoothPrice[smoothPrice_Idx] = smoothedValue;
         if( (today%2) == 0 )
         {
            { hilbertTempReal = a * smoothedValue; detrender = -detrender_Even [hilbertIdx]; detrender_Even [hilbertIdx] = hilbertTempReal; detrender += hilbertTempReal; detrender -= prev_detrender_Even ; prev_detrender_Even = b * prev_detrender_input_Even ; detrender += prev_detrender_Even ; prev_detrender_input_Even = smoothedValue; detrender *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * detrender; Q1 = -Q1_Even [hilbertIdx]; Q1_Even [hilbertIdx] = hilbertTempReal; Q1 += hilbertTempReal; Q1 -= prev_Q1_Even ; prev_Q1_Even = b * prev_Q1_input_Even ; Q1 += prev_Q1_Even ; prev_Q1_input_Even = detrender; Q1 *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * I1ForEvenPrev3; jI = -jI_Even [hilbertIdx]; jI_Even [hilbertIdx] = hilbertTempReal; jI += hilbertTempReal; jI -= prev_jI_Even ; prev_jI_Even = b * prev_jI_input_Even ; jI += prev_jI_Even ; prev_jI_input_Even = I1ForEvenPrev3; jI *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * Q1; jQ = -jQ_Even [hilbertIdx]; jQ_Even [hilbertIdx] = hilbertTempReal; jQ += hilbertTempReal; jQ -= prev_jQ_Even ; prev_jQ_Even = b * prev_jQ_input_Even ; jQ += prev_jQ_Even ; prev_jQ_input_Even = Q1; jQ *= adjustedPrevPeriod; } ;
            if( ++hilbertIdx == 3 )
               hilbertIdx = 0;
            Q2 = (0.2*(Q1 + jI)) + (0.8*prevQ2);
            I2 = (0.2*(I1ForEvenPrev3 - jQ)) + (0.8*prevI2);
            I1ForOddPrev3 = I1ForOddPrev2;
            I1ForOddPrev2 = detrender;
         }
         else
         {
            { hilbertTempReal = a * smoothedValue; detrender = -detrender_Odd [hilbertIdx]; detrender_Odd [hilbertIdx] = hilbertTempReal; detrender += hilbertTempReal; detrender -= prev_detrender_Odd ; prev_detrender_Odd = b * prev_detrender_input_Odd ; detrender += prev_detrender_Odd ; prev_detrender_input_Odd = smoothedValue; detrender *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * detrender; Q1 = -Q1_Odd [hilbertIdx]; Q1_Odd [hilbertIdx] = hilbertTempReal; Q1 += hilbertTempReal; Q1 -= prev_Q1_Odd ; prev_Q1_Odd = b * prev_Q1_input_Odd ; Q1 += prev_Q1_Odd ; prev_Q1_input_Odd = detrender; Q1 *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * I1ForOddPrev3; jI = -jI_Odd [hilbertIdx]; jI_Odd [hilbertIdx] = hilbertTempReal; jI += hilbertTempReal; jI -= prev_jI_Odd ; prev_jI_Odd = b * prev_jI_input_Odd ; jI += prev_jI_Odd ; prev_jI_input_Odd = I1ForOddPrev3; jI *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * Q1; jQ = -jQ_Odd [hilbertIdx]; jQ_Odd [hilbertIdx] = hilbertTempReal; jQ += hilbertTempReal; jQ -= prev_jQ_Odd ; prev_jQ_Odd = b * prev_jQ_input_Odd ; jQ += prev_jQ_Odd ; prev_jQ_input_Odd = Q1; jQ *= adjustedPrevPeriod; } ;
            Q2 = (0.2*(Q1 + jI)) + (0.8*prevQ2);
            I2 = (0.2*(I1ForOddPrev3 - jQ)) + (0.8*prevI2);
            I1ForEvenPrev3 = I1ForEvenPrev2;
            I1ForEvenPrev2 = detrender;
         }
         Re = (0.2*((I2*prevI2)+(Q2*prevQ2)))+(0.8*Re);
         Im = (0.2*((I2*prevQ2)-(Q2*prevI2)))+(0.8*Im);
         prevQ2 = Q2;
         prevI2 = I2;
         tempReal = period;
         if( (Im != 0.0) && (Re != 0.0) )
            period = 360.0 / ( Math.atan (Im/Re)*rad2Deg);
         tempReal2 = 1.5*tempReal;
         if( period > tempReal2)
            period = tempReal2;
         tempReal2 = 0.67*tempReal;
         if( period < tempReal2 )
            period = tempReal2;
         if( period < 6 )
            period = 6;
         else if( period > 50 )
            period = 50;
         period = (0.2*period) + (0.8 * tempReal);
         smoothPeriod = (0.33*period)+(0.67*smoothPeriod);
         DCPeriod = smoothPeriod+0.5;
         DCPeriodInt = (int)DCPeriod;
         realPart = 0.0;
         imagPart = 0.0;
         idx = smoothPrice_Idx;
         for( i=0; i < DCPeriodInt; i++ )
         {
            tempReal = ((double)i*constDeg2RadBy360)/(double)DCPeriodInt;
            tempReal2 = smoothPrice[idx];
            realPart += Math.sin (tempReal)*tempReal2;
            imagPart += Math.cos (tempReal)*tempReal2;
            if( idx == 0 )
               idx = 50 -1;
            else
               idx--;
         }
         tempReal = Math.abs (imagPart);
         if( tempReal > 0.0 )
            DCPhase = Math.atan (realPart/imagPart)*rad2Deg;
         else if( tempReal <= 0.01 )
         {
            if( realPart < 0.0 )
               DCPhase -= 90.0;
            else if( realPart > 0.0 )
               DCPhase += 90.0;
         }
         DCPhase += 90.0;
         DCPhase += 360.0 / smoothPeriod;
         if( imagPart < 0.0 )
            DCPhase += 180.0;
         if( DCPhase > 315.0 )
            DCPhase -= 360.0;
         if( today >= startIdx )
         {
            outSine[outIdx] = Math.sin (DCPhase*deg2Rad);
            outLeadSine[outIdx++] = Math.sin ((DCPhase+45)*deg2Rad);
         }
         { smoothPrice_Idx ++; if( smoothPrice_Idx > maxIdx_smoothPrice ) smoothPrice_Idx = 0; } ;
         today++;
      }
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int htTrendlineLookback( )
   {
      return 63 + (this.unstablePeriod[FuncUnstId.HtTrendline.ordinal()]) ;
   }
   public RetCode htTrendline( int startIdx,
      int endIdx,
      double inReal[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx, i;
      int lookbackTotal, today;
      double tempReal, tempReal2;
      double adjustedPrevPeriod, period;
      int trailingWMAIdx;
      double periodWMASum, periodWMASub, trailingWMAValue;
      double smoothedValue;
      double iTrend1, iTrend2, iTrend3;
      final double a = 0.0962;
      final double b = 0.5769;
      double hilbertTempReal;
      int hilbertIdx;
      double []detrender_Odd = new double[3] ; double []detrender_Even = new double[3] ; double detrender; double prev_detrender_Odd ; double prev_detrender_Even ; double prev_detrender_input_Odd ; double prev_detrender_input_Even ;
      double []Q1_Odd = new double[3] ; double []Q1_Even = new double[3] ; double Q1; double prev_Q1_Odd ; double prev_Q1_Even ; double prev_Q1_input_Odd ; double prev_Q1_input_Even ;
      double []jI_Odd = new double[3] ; double []jI_Even = new double[3] ; double jI; double prev_jI_Odd ; double prev_jI_Even ; double prev_jI_input_Odd ; double prev_jI_input_Even ;
      double []jQ_Odd = new double[3] ; double []jQ_Even = new double[3] ; double jQ; double prev_jQ_Odd ; double prev_jQ_Even ; double prev_jQ_input_Odd ; double prev_jQ_input_Even ;
      double Q2, I2, prevQ2, prevI2, Re, Im;
      double I1ForOddPrev2, I1ForOddPrev3;
      double I1ForEvenPrev2, I1ForEvenPrev3;
      double rad2Deg;
      double todayValue, smoothPeriod;
      int smoothPrice_Idx = 0; double []smoothPrice; int maxIdx_smoothPrice = ( 50 -1) ;
      int idx;
      int DCPeriodInt;
      double DCPeriod;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      { smoothPrice = new double[maxIdx_smoothPrice +1]; } ;
      iTrend1 = iTrend2 = iTrend3 = 0.0;
      tempReal = Math.atan (1);
      rad2Deg = 45.0/tempReal;
      lookbackTotal = 63 + (this.unstablePeriod[FuncUnstId.HtTrendline.ordinal()]) ;
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outBegIdx.value = startIdx;
      trailingWMAIdx = startIdx - lookbackTotal;
      today = trailingWMAIdx;
      tempReal = inReal[today++];
      periodWMASub = tempReal;
      periodWMASum = tempReal;
      tempReal = inReal[today++];
      periodWMASub += tempReal;
      periodWMASum += tempReal*2.0;
      tempReal = inReal[today++];
      periodWMASub += tempReal;
      periodWMASum += tempReal*3.0;
      trailingWMAValue = 0.0;
      i = 34;
      do
      {
         tempReal = inReal[today++];
         { periodWMASub += tempReal; periodWMASub -= trailingWMAValue; periodWMASum += tempReal*4.0; trailingWMAValue = inReal[trailingWMAIdx++]; smoothedValue = periodWMASum*0.1; periodWMASum -= periodWMASub; } ;
      } while( --i != 0);
      hilbertIdx = 0;
      { detrender_Odd [0] = 0.0; detrender_Odd [1] = 0.0; detrender_Odd [2] = 0.0; detrender_Even [0] = 0.0; detrender_Even [1] = 0.0; detrender_Even [2] = 0.0; detrender = 0.0; prev_detrender_Odd = 0.0; prev_detrender_Even = 0.0; prev_detrender_input_Odd = 0.0; prev_detrender_input_Even = 0.0; } ;
      { Q1_Odd [0] = 0.0; Q1_Odd [1] = 0.0; Q1_Odd [2] = 0.0; Q1_Even [0] = 0.0; Q1_Even [1] = 0.0; Q1_Even [2] = 0.0; Q1 = 0.0; prev_Q1_Odd = 0.0; prev_Q1_Even = 0.0; prev_Q1_input_Odd = 0.0; prev_Q1_input_Even = 0.0; } ;
      { jI_Odd [0] = 0.0; jI_Odd [1] = 0.0; jI_Odd [2] = 0.0; jI_Even [0] = 0.0; jI_Even [1] = 0.0; jI_Even [2] = 0.0; jI = 0.0; prev_jI_Odd = 0.0; prev_jI_Even = 0.0; prev_jI_input_Odd = 0.0; prev_jI_input_Even = 0.0; } ;
      { jQ_Odd [0] = 0.0; jQ_Odd [1] = 0.0; jQ_Odd [2] = 0.0; jQ_Even [0] = 0.0; jQ_Even [1] = 0.0; jQ_Even [2] = 0.0; jQ = 0.0; prev_jQ_Odd = 0.0; prev_jQ_Even = 0.0; prev_jQ_input_Odd = 0.0; prev_jQ_input_Even = 0.0; } ;
      period = 0.0;
      outIdx = 0;
      prevI2 = prevQ2 = 0.0;
      Re = Im = 0.0;
      I1ForOddPrev3 = I1ForEvenPrev3 = 0.0;
      I1ForOddPrev2 = I1ForEvenPrev2 = 0.0;
      smoothPeriod = 0.0;
      for( i=0; i < 50 ; i++ )
         smoothPrice[i] = 0.0;
      while( today <= endIdx )
      {
         adjustedPrevPeriod = (0.075*period)+0.54;
         todayValue = inReal[today];
         { periodWMASub += todayValue; periodWMASub -= trailingWMAValue; periodWMASum += todayValue*4.0; trailingWMAValue = inReal[trailingWMAIdx++]; smoothedValue = periodWMASum*0.1; periodWMASum -= periodWMASub; } ;
         smoothPrice[smoothPrice_Idx] = smoothedValue;
         if( (today%2) == 0 )
         {
            { hilbertTempReal = a * smoothedValue; detrender = -detrender_Even [hilbertIdx]; detrender_Even [hilbertIdx] = hilbertTempReal; detrender += hilbertTempReal; detrender -= prev_detrender_Even ; prev_detrender_Even = b * prev_detrender_input_Even ; detrender += prev_detrender_Even ; prev_detrender_input_Even = smoothedValue; detrender *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * detrender; Q1 = -Q1_Even [hilbertIdx]; Q1_Even [hilbertIdx] = hilbertTempReal; Q1 += hilbertTempReal; Q1 -= prev_Q1_Even ; prev_Q1_Even = b * prev_Q1_input_Even ; Q1 += prev_Q1_Even ; prev_Q1_input_Even = detrender; Q1 *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * I1ForEvenPrev3; jI = -jI_Even [hilbertIdx]; jI_Even [hilbertIdx] = hilbertTempReal; jI += hilbertTempReal; jI -= prev_jI_Even ; prev_jI_Even = b * prev_jI_input_Even ; jI += prev_jI_Even ; prev_jI_input_Even = I1ForEvenPrev3; jI *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * Q1; jQ = -jQ_Even [hilbertIdx]; jQ_Even [hilbertIdx] = hilbertTempReal; jQ += hilbertTempReal; jQ -= prev_jQ_Even ; prev_jQ_Even = b * prev_jQ_input_Even ; jQ += prev_jQ_Even ; prev_jQ_input_Even = Q1; jQ *= adjustedPrevPeriod; } ;
            if( ++hilbertIdx == 3 )
               hilbertIdx = 0;
            Q2 = (0.2*(Q1 + jI)) + (0.8*prevQ2);
            I2 = (0.2*(I1ForEvenPrev3 - jQ)) + (0.8*prevI2);
            I1ForOddPrev3 = I1ForOddPrev2;
            I1ForOddPrev2 = detrender;
         }
         else
         {
            { hilbertTempReal = a * smoothedValue; detrender = -detrender_Odd [hilbertIdx]; detrender_Odd [hilbertIdx] = hilbertTempReal; detrender += hilbertTempReal; detrender -= prev_detrender_Odd ; prev_detrender_Odd = b * prev_detrender_input_Odd ; detrender += prev_detrender_Odd ; prev_detrender_input_Odd = smoothedValue; detrender *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * detrender; Q1 = -Q1_Odd [hilbertIdx]; Q1_Odd [hilbertIdx] = hilbertTempReal; Q1 += hilbertTempReal; Q1 -= prev_Q1_Odd ; prev_Q1_Odd = b * prev_Q1_input_Odd ; Q1 += prev_Q1_Odd ; prev_Q1_input_Odd = detrender; Q1 *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * I1ForOddPrev3; jI = -jI_Odd [hilbertIdx]; jI_Odd [hilbertIdx] = hilbertTempReal; jI += hilbertTempReal; jI -= prev_jI_Odd ; prev_jI_Odd = b * prev_jI_input_Odd ; jI += prev_jI_Odd ; prev_jI_input_Odd = I1ForOddPrev3; jI *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * Q1; jQ = -jQ_Odd [hilbertIdx]; jQ_Odd [hilbertIdx] = hilbertTempReal; jQ += hilbertTempReal; jQ -= prev_jQ_Odd ; prev_jQ_Odd = b * prev_jQ_input_Odd ; jQ += prev_jQ_Odd ; prev_jQ_input_Odd = Q1; jQ *= adjustedPrevPeriod; } ;
            Q2 = (0.2*(Q1 + jI)) + (0.8*prevQ2);
            I2 = (0.2*(I1ForOddPrev3 - jQ)) + (0.8*prevI2);
            I1ForEvenPrev3 = I1ForEvenPrev2;
            I1ForEvenPrev2 = detrender;
         }
         Re = (0.2*((I2*prevI2)+(Q2*prevQ2)))+(0.8*Re);
         Im = (0.2*((I2*prevQ2)-(Q2*prevI2)))+(0.8*Im);
         prevQ2 = Q2;
         prevI2 = I2;
         tempReal = period;
         if( (Im != 0.0) && (Re != 0.0) )
            period = 360.0 / ( Math.atan (Im/Re)*rad2Deg);
         tempReal2 = 1.5*tempReal;
         if( period > tempReal2)
            period = tempReal2;
         tempReal2 = 0.67*tempReal;
         if( period < tempReal2 )
            period = tempReal2;
         if( period < 6 )
            period = 6;
         else if( period > 50 )
            period = 50;
         period = (0.2*period) + (0.8 * tempReal);
         smoothPeriod = (0.33*period)+(0.67*smoothPeriod);
         DCPeriod = smoothPeriod+0.5;
         DCPeriodInt = (int)DCPeriod;
         idx = today;
         tempReal = 0.0;
         for( i=0; i < DCPeriodInt; i++ )
            tempReal += inReal[idx--];
         if( DCPeriodInt > 0 )
            tempReal = tempReal/(double)DCPeriodInt;
         tempReal2 = (4.0*tempReal + 3.0*iTrend1 + 2.0*iTrend2 + iTrend3) / 10.0;
         iTrend3 = iTrend2;
         iTrend2 = iTrend1;
         iTrend1 = tempReal;
         if( today >= startIdx )
         {
            outReal[outIdx++] = tempReal2;
         }
         { smoothPrice_Idx ++; if( smoothPrice_Idx > maxIdx_smoothPrice ) smoothPrice_Idx = 0; } ;
         today++;
      }
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int htTrendModeLookback( )
   {
      return 63 + (this.unstablePeriod[FuncUnstId.HtTrendMode.ordinal()]) ;
   }
   public RetCode htTrendMode( int startIdx,
      int endIdx,
      double inReal[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx, i;
      int lookbackTotal, today;
      double tempReal, tempReal2;
      double adjustedPrevPeriod, period;
      int trailingWMAIdx;
      double periodWMASum, periodWMASub, trailingWMAValue;
      double smoothedValue;
      double iTrend1, iTrend2, iTrend3;
      final double a = 0.0962;
      final double b = 0.5769;
      double hilbertTempReal;
      int hilbertIdx;
      double []detrender_Odd = new double[3] ; double []detrender_Even = new double[3] ; double detrender; double prev_detrender_Odd ; double prev_detrender_Even ; double prev_detrender_input_Odd ; double prev_detrender_input_Even ;
      double []Q1_Odd = new double[3] ; double []Q1_Even = new double[3] ; double Q1; double prev_Q1_Odd ; double prev_Q1_Even ; double prev_Q1_input_Odd ; double prev_Q1_input_Even ;
      double []jI_Odd = new double[3] ; double []jI_Even = new double[3] ; double jI; double prev_jI_Odd ; double prev_jI_Even ; double prev_jI_input_Odd ; double prev_jI_input_Even ;
      double []jQ_Odd = new double[3] ; double []jQ_Even = new double[3] ; double jQ; double prev_jQ_Odd ; double prev_jQ_Even ; double prev_jQ_input_Odd ; double prev_jQ_input_Even ;
      double Q2, I2, prevQ2, prevI2, Re, Im;
      double I1ForOddPrev2, I1ForOddPrev3;
      double I1ForEvenPrev2, I1ForEvenPrev3;
      double rad2Deg, deg2Rad, constDeg2RadBy360;
      double todayValue, smoothPeriod;
      int smoothPrice_Idx = 0; double []smoothPrice; int maxIdx_smoothPrice = ( 50 -1) ;
      int idx;
      int DCPeriodInt;
      double DCPhase, DCPeriod, imagPart, realPart;
      int daysInTrend, trend;
      double prevDCPhase, trendline;
      double prevSine, prevLeadSine, sine, leadSine;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      { smoothPrice = new double[maxIdx_smoothPrice +1]; } ;
      iTrend1 = iTrend2 = iTrend3 = 0.0;
      daysInTrend = 0;
      prevDCPhase = DCPhase = 0.0;
      prevSine = sine = 0.0;
      prevLeadSine = leadSine = 0.0;
      tempReal = Math.atan (1);
      rad2Deg = 45.0/tempReal;
      deg2Rad = 1.0/rad2Deg;
      constDeg2RadBy360 = tempReal*8.0;
      lookbackTotal = 63 + (this.unstablePeriod[FuncUnstId.HtTrendMode.ordinal()]) ;
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outBegIdx.value = startIdx;
      trailingWMAIdx = startIdx - lookbackTotal;
      today = trailingWMAIdx;
      tempReal = inReal[today++];
      periodWMASub = tempReal;
      periodWMASum = tempReal;
      tempReal = inReal[today++];
      periodWMASub += tempReal;
      periodWMASum += tempReal*2.0;
      tempReal = inReal[today++];
      periodWMASub += tempReal;
      periodWMASum += tempReal*3.0;
      trailingWMAValue = 0.0;
      i = 34;
      do
      {
         tempReal = inReal[today++];
         { periodWMASub += tempReal; periodWMASub -= trailingWMAValue; periodWMASum += tempReal*4.0; trailingWMAValue = inReal[trailingWMAIdx++]; smoothedValue = periodWMASum*0.1; periodWMASum -= periodWMASub; } ;
      } while( --i != 0);
      hilbertIdx = 0;
      { detrender_Odd [0] = 0.0; detrender_Odd [1] = 0.0; detrender_Odd [2] = 0.0; detrender_Even [0] = 0.0; detrender_Even [1] = 0.0; detrender_Even [2] = 0.0; detrender = 0.0; prev_detrender_Odd = 0.0; prev_detrender_Even = 0.0; prev_detrender_input_Odd = 0.0; prev_detrender_input_Even = 0.0; } ;
      { Q1_Odd [0] = 0.0; Q1_Odd [1] = 0.0; Q1_Odd [2] = 0.0; Q1_Even [0] = 0.0; Q1_Even [1] = 0.0; Q1_Even [2] = 0.0; Q1 = 0.0; prev_Q1_Odd = 0.0; prev_Q1_Even = 0.0; prev_Q1_input_Odd = 0.0; prev_Q1_input_Even = 0.0; } ;
      { jI_Odd [0] = 0.0; jI_Odd [1] = 0.0; jI_Odd [2] = 0.0; jI_Even [0] = 0.0; jI_Even [1] = 0.0; jI_Even [2] = 0.0; jI = 0.0; prev_jI_Odd = 0.0; prev_jI_Even = 0.0; prev_jI_input_Odd = 0.0; prev_jI_input_Even = 0.0; } ;
      { jQ_Odd [0] = 0.0; jQ_Odd [1] = 0.0; jQ_Odd [2] = 0.0; jQ_Even [0] = 0.0; jQ_Even [1] = 0.0; jQ_Even [2] = 0.0; jQ = 0.0; prev_jQ_Odd = 0.0; prev_jQ_Even = 0.0; prev_jQ_input_Odd = 0.0; prev_jQ_input_Even = 0.0; } ;
      period = 0.0;
      outIdx = 0;
      prevI2 = prevQ2 = 0.0;
      Re = Im = 0.0;
      I1ForOddPrev3 = I1ForEvenPrev3 = 0.0;
      I1ForOddPrev2 = I1ForEvenPrev2 = 0.0;
      smoothPeriod = 0.0;
      for( i=0; i < 50 ; i++ )
         smoothPrice[i] = 0.0;
      DCPhase = 0.0;
      while( today <= endIdx )
      {
         adjustedPrevPeriod = (0.075*period)+0.54;
         todayValue = inReal[today];
         { periodWMASub += todayValue; periodWMASub -= trailingWMAValue; periodWMASum += todayValue*4.0; trailingWMAValue = inReal[trailingWMAIdx++]; smoothedValue = periodWMASum*0.1; periodWMASum -= periodWMASub; } ;
         smoothPrice[smoothPrice_Idx] = smoothedValue;
         if( (today%2) == 0 )
         {
            { hilbertTempReal = a * smoothedValue; detrender = -detrender_Even [hilbertIdx]; detrender_Even [hilbertIdx] = hilbertTempReal; detrender += hilbertTempReal; detrender -= prev_detrender_Even ; prev_detrender_Even = b * prev_detrender_input_Even ; detrender += prev_detrender_Even ; prev_detrender_input_Even = smoothedValue; detrender *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * detrender; Q1 = -Q1_Even [hilbertIdx]; Q1_Even [hilbertIdx] = hilbertTempReal; Q1 += hilbertTempReal; Q1 -= prev_Q1_Even ; prev_Q1_Even = b * prev_Q1_input_Even ; Q1 += prev_Q1_Even ; prev_Q1_input_Even = detrender; Q1 *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * I1ForEvenPrev3; jI = -jI_Even [hilbertIdx]; jI_Even [hilbertIdx] = hilbertTempReal; jI += hilbertTempReal; jI -= prev_jI_Even ; prev_jI_Even = b * prev_jI_input_Even ; jI += prev_jI_Even ; prev_jI_input_Even = I1ForEvenPrev3; jI *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * Q1; jQ = -jQ_Even [hilbertIdx]; jQ_Even [hilbertIdx] = hilbertTempReal; jQ += hilbertTempReal; jQ -= prev_jQ_Even ; prev_jQ_Even = b * prev_jQ_input_Even ; jQ += prev_jQ_Even ; prev_jQ_input_Even = Q1; jQ *= adjustedPrevPeriod; } ;
            if( ++hilbertIdx == 3 )
               hilbertIdx = 0;
            Q2 = (0.2*(Q1 + jI)) + (0.8*prevQ2);
            I2 = (0.2*(I1ForEvenPrev3 - jQ)) + (0.8*prevI2);
            I1ForOddPrev3 = I1ForOddPrev2;
            I1ForOddPrev2 = detrender;
         }
         else
         {
            { hilbertTempReal = a * smoothedValue; detrender = -detrender_Odd [hilbertIdx]; detrender_Odd [hilbertIdx] = hilbertTempReal; detrender += hilbertTempReal; detrender -= prev_detrender_Odd ; prev_detrender_Odd = b * prev_detrender_input_Odd ; detrender += prev_detrender_Odd ; prev_detrender_input_Odd = smoothedValue; detrender *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * detrender; Q1 = -Q1_Odd [hilbertIdx]; Q1_Odd [hilbertIdx] = hilbertTempReal; Q1 += hilbertTempReal; Q1 -= prev_Q1_Odd ; prev_Q1_Odd = b * prev_Q1_input_Odd ; Q1 += prev_Q1_Odd ; prev_Q1_input_Odd = detrender; Q1 *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * I1ForOddPrev3; jI = -jI_Odd [hilbertIdx]; jI_Odd [hilbertIdx] = hilbertTempReal; jI += hilbertTempReal; jI -= prev_jI_Odd ; prev_jI_Odd = b * prev_jI_input_Odd ; jI += prev_jI_Odd ; prev_jI_input_Odd = I1ForOddPrev3; jI *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * Q1; jQ = -jQ_Odd [hilbertIdx]; jQ_Odd [hilbertIdx] = hilbertTempReal; jQ += hilbertTempReal; jQ -= prev_jQ_Odd ; prev_jQ_Odd = b * prev_jQ_input_Odd ; jQ += prev_jQ_Odd ; prev_jQ_input_Odd = Q1; jQ *= adjustedPrevPeriod; } ;
            Q2 = (0.2*(Q1 + jI)) + (0.8*prevQ2);
            I2 = (0.2*(I1ForOddPrev3 - jQ)) + (0.8*prevI2);
            I1ForEvenPrev3 = I1ForEvenPrev2;
            I1ForEvenPrev2 = detrender;
         }
         Re = (0.2*((I2*prevI2)+(Q2*prevQ2)))+(0.8*Re);
         Im = (0.2*((I2*prevQ2)-(Q2*prevI2)))+(0.8*Im);
         prevQ2 = Q2;
         prevI2 = I2;
         tempReal = period;
         if( (Im != 0.0) && (Re != 0.0) )
            period = 360.0 / ( Math.atan (Im/Re)*rad2Deg);
         tempReal2 = 1.5*tempReal;
         if( period > tempReal2)
            period = tempReal2;
         tempReal2 = 0.67*tempReal;
         if( period < tempReal2 )
            period = tempReal2;
         if( period < 6 )
            period = 6;
         else if( period > 50 )
            period = 50;
         period = (0.2*period) + (0.8 * tempReal);
         smoothPeriod = (0.33*period)+(0.67*smoothPeriod);
         prevDCPhase = DCPhase;
         DCPeriod = smoothPeriod+0.5;
         DCPeriodInt = (int)DCPeriod;
         realPart = 0.0;
         imagPart = 0.0;
         idx = smoothPrice_Idx;
         for( i=0; i < DCPeriodInt; i++ )
         {
            tempReal = ((double)i*constDeg2RadBy360)/(double)DCPeriodInt;
            tempReal2 = smoothPrice[idx];
            realPart += Math.sin (tempReal)*tempReal2;
            imagPart += Math.cos (tempReal)*tempReal2;
            if( idx == 0 )
               idx = 50 -1;
            else
               idx--;
         }
         tempReal = Math.abs (imagPart);
         if( tempReal > 0.0 )
            DCPhase = Math.atan (realPart/imagPart)*rad2Deg;
         else if( tempReal <= 0.01 )
         {
            if( realPart < 0.0 )
               DCPhase -= 90.0;
            else if( realPart > 0.0 )
               DCPhase += 90.0;
         }
         DCPhase += 90.0;
         DCPhase += 360.0 / smoothPeriod;
         if( imagPart < 0.0 )
            DCPhase += 180.0;
         if( DCPhase > 315.0 )
            DCPhase -= 360.0;
         prevSine = sine;
         prevLeadSine = leadSine;
         sine = Math.sin (DCPhase*deg2Rad);
         leadSine = Math.sin ((DCPhase+45)*deg2Rad);
         DCPeriod = smoothPeriod+0.5;
         DCPeriodInt = (int)DCPeriod;
         idx = today;
         tempReal = 0.0;
         for( i=0; i < DCPeriodInt; i++ )
            tempReal += inReal[idx--];
         if( DCPeriodInt > 0 )
            tempReal = tempReal/(double)DCPeriodInt;
         trendline = (4.0*tempReal + 3.0*iTrend1 + 2.0*iTrend2 + iTrend3) / 10.0;
         iTrend3 = iTrend2;
         iTrend2 = iTrend1;
         iTrend1 = tempReal;
         trend = 1;
         if( ((sine > leadSine) && (prevSine <= prevLeadSine)) ||
            ((sine < leadSine) && (prevSine >= prevLeadSine)) )
         {
            daysInTrend = 0;
            trend = 0;
         }
         daysInTrend++;
         if( daysInTrend < (0.5*smoothPeriod) )
            trend = 0;
         tempReal = DCPhase - prevDCPhase;
         if( (smoothPeriod != 0.0) &&
            ((tempReal > (0.67*360.0/smoothPeriod)) && (tempReal < (1.5*360.0/smoothPeriod))) )
         {
            trend = 0;
         }
         tempReal = smoothPrice[smoothPrice_Idx];
         if( (trendline != 0.0) && ( Math.abs ( (tempReal - trendline)/trendline ) >= 0.015) )
            trend = 1;
         if( today >= startIdx )
         {
        	 outReal[outIdx++] = trend;
         }
         { smoothPrice_Idx ++; if( smoothPrice_Idx > maxIdx_smoothPrice ) smoothPrice_Idx = 0; } ;
         today++;
      }
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int kamaLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      return optInTimePeriod + (this.unstablePeriod[FuncUnstId.Kama.ordinal()]) ;
   }
   public RetCode kama( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      final double constMax = 2.0/(30.0+1.0);
      final double constDiff = 2.0/(2.0+1.0) - constMax;
      double tempReal, tempReal2;
      double sumROC1, periodROC, prevKAMA;
      int i, today, outIdx, lookbackTotal;
      int trailingIdx;
      double trailingValue;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      outBegIdx.value = 0 ;
      outNBElement.value = 0 ;
      lookbackTotal = optInTimePeriod + (this.unstablePeriod[FuncUnstId.Kama.ordinal()]) ;
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      sumROC1 = 0.0;
      today = startIdx-lookbackTotal;
      trailingIdx = today;
      i = optInTimePeriod;
      while( i-- > 0 )
      {
         tempReal = inReal[today++];
         tempReal -= inReal[today];
         sumROC1 += Math.abs (tempReal);
      }
      prevKAMA = inReal[today-1];
      tempReal = inReal[today];
      tempReal2 = inReal[trailingIdx++];
      periodROC = tempReal-tempReal2;
      trailingValue = tempReal2;
      if( (sumROC1 <= periodROC) || (((-0.00000001)<sumROC1)&&(sumROC1<0.00000001)) )
         tempReal = 1.0;
      else
         tempReal = Math.abs (periodROC/sumROC1);
      tempReal = (tempReal*constDiff)+constMax;
      tempReal *= tempReal;
      prevKAMA = ((inReal[today++]-prevKAMA)*tempReal) + prevKAMA;
      while( today <= startIdx )
      {
         tempReal = inReal[today];
         tempReal2 = inReal[trailingIdx++];
         periodROC = tempReal-tempReal2;
         sumROC1 -= Math.abs (trailingValue-tempReal2);
         sumROC1 += Math.abs (tempReal-inReal[today-1]);
         trailingValue = tempReal2;
         if( (sumROC1 <= periodROC) || (((-0.00000001)<sumROC1)&&(sumROC1<0.00000001)) )
            tempReal = 1.0;
         else
            tempReal = Math.abs (periodROC/sumROC1);
         tempReal = (tempReal*constDiff)+constMax;
         tempReal *= tempReal;
         prevKAMA = ((inReal[today++]-prevKAMA)*tempReal) + prevKAMA;
      }
      outReal[0] = prevKAMA;
      outIdx = 1;
      outBegIdx.value = today-1;
      while( today <= endIdx )
      {
         tempReal = inReal[today];
         tempReal2 = inReal[trailingIdx++];
         periodROC = tempReal-tempReal2;
         sumROC1 -= Math.abs (trailingValue-tempReal2);
         sumROC1 += Math.abs (tempReal-inReal[today-1]);
         trailingValue = tempReal2;
         if( (sumROC1 <= periodROC) || (((-0.00000001)<sumROC1)&&(sumROC1<0.00000001)) )
            tempReal = 1.0;
         else
            tempReal = Math.abs (periodROC / sumROC1);
         tempReal = (tempReal*constDiff)+constMax;
         tempReal *= tempReal;
         prevKAMA = ((inReal[today++]-prevKAMA)*tempReal) + prevKAMA;
         outReal[outIdx++] = prevKAMA;
      }
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int linearRegLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      return optInTimePeriod-1;
   }
   public RetCode linearReg( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx;
      int today, lookbackTotal;
      double SumX, SumXY, SumY, SumXSqr, Divisor;
      double m, b;
      int i;
      double tempValue1;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      lookbackTotal = linearRegLookback ( optInTimePeriod );
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      today = startIdx;
      SumX = optInTimePeriod * ( optInTimePeriod - 1 ) * 0.5;
      SumXSqr = optInTimePeriod * ( optInTimePeriod - 1 ) * ( 2 * optInTimePeriod - 1 ) / 6;
      Divisor = SumX * SumX - optInTimePeriod * SumXSqr;
      while( today <= endIdx )
      {
         SumXY = 0;
         SumY = 0;
         for( i = optInTimePeriod; i-- != 0; )
         {
            SumY += tempValue1 = inReal[today - i];
            SumXY += (double)i * tempValue1;
         }
         m = ( optInTimePeriod * SumXY - SumX * SumY) / Divisor;
         b = ( SumY - m * SumX ) / (double)optInTimePeriod;
         outReal[outIdx++] = b + m * (double)(optInTimePeriod-1);
         today++;
      }
      outBegIdx.value = startIdx;
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int linearRegAngleLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      return optInTimePeriod-1;
   }
   public RetCode linearRegAngle( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx;
      int today, lookbackTotal;
      double SumX, SumXY, SumY, SumXSqr, Divisor;
      double m;
      int i;
      double tempValue1;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      lookbackTotal = linearRegAngleLookback ( optInTimePeriod );
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      today = startIdx;
      SumX = optInTimePeriod * ( optInTimePeriod - 1 ) * 0.5;
      SumXSqr = optInTimePeriod * ( optInTimePeriod - 1 ) * ( 2 * optInTimePeriod - 1 ) / 6;
      Divisor = SumX * SumX - optInTimePeriod * SumXSqr;
      while( today <= endIdx )
      {
         SumXY = 0;
         SumY = 0;
         for( i = optInTimePeriod; i-- != 0; )
         {
            SumY += tempValue1 = inReal[today - i];
            SumXY += (double)i * tempValue1;
         }
         m = ( optInTimePeriod * SumXY - SumX * SumY) / Divisor;
         outReal[outIdx++] = Math.atan (m) * ( 180.0 / 3.14159265358979323846 );
         today++;
      }
      outBegIdx.value = startIdx;
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int linearRegInterceptLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      return optInTimePeriod-1;
   }
   public RetCode linearRegIntercept( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx;
      int today, lookbackTotal;
      double SumX, SumXY, SumY, SumXSqr, Divisor;
      double m;
      int i;
      double tempValue1;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      lookbackTotal = linearRegInterceptLookback ( optInTimePeriod );
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      today = startIdx;
      SumX = optInTimePeriod * ( optInTimePeriod - 1 ) * 0.5;
      SumXSqr = optInTimePeriod * ( optInTimePeriod - 1 ) * ( 2 * optInTimePeriod - 1 ) / 6;
      Divisor = SumX * SumX - optInTimePeriod * SumXSqr;
      while( today <= endIdx )
      {
         SumXY = 0;
         SumY = 0;
         for( i = optInTimePeriod; i-- != 0; )
         {
            SumY += tempValue1 = inReal[today - i];
            SumXY += (double)i * tempValue1;
         }
         m = ( optInTimePeriod * SumXY - SumX * SumY) / Divisor;
         outReal[outIdx++] = ( SumY - m * SumX ) / (double)optInTimePeriod;
         today++;
      }
      outBegIdx.value = startIdx;
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int linearRegSlopeLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      return optInTimePeriod-1;
   }
   public RetCode linearRegSlope( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx;
      int today, lookbackTotal;
      double SumX, SumXY, SumY, SumXSqr, Divisor;
      int i;
      double tempValue1;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      lookbackTotal = linearRegSlopeLookback ( optInTimePeriod );
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      today = startIdx;
      SumX = optInTimePeriod * ( optInTimePeriod - 1 ) * 0.5;
      SumXSqr = optInTimePeriod * ( optInTimePeriod - 1 ) * ( 2 * optInTimePeriod - 1 ) / 6;
      Divisor = SumX * SumX - optInTimePeriod * SumXSqr;
      while( today <= endIdx )
      {
         SumXY = 0;
         SumY = 0;
         for( i = optInTimePeriod; i-- != 0; )
         {
            SumY += tempValue1 = inReal[today - i];
            SumXY += (double)i * tempValue1;
         }
         outReal[outIdx++] = ( optInTimePeriod * SumXY - SumX * SumY) / Divisor;
         today++;
      }
      outBegIdx.value = startIdx;
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int lnLookback( )
   {
      return 0;
   }
   public RetCode ln( int startIdx,
      int endIdx,
      double inReal[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx;
      int i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      for( i=startIdx, outIdx=0; i <= endIdx; i++, outIdx++ )
      {
         outReal[outIdx] = Math.log (inReal[i]);
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int log10Lookback( )
   {
      return 0;
   }
   public RetCode log10( int startIdx,
      int endIdx,
      double inReal[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx;
      int i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      for( i=startIdx, outIdx=0; i <= endIdx; i++, outIdx++ )
      {
         outReal[outIdx] = Math.log10 (inReal[i]);
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int movingAverageLookback( int optInTimePeriod,
      MAType optInMAType )
   {
      int retValue;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return -1;
      if( optInTimePeriod <= 1 )
         return 0;
      switch( optInMAType )
      {
         case Sma :
            retValue = smaLookback ( optInTimePeriod );
         break;
         case Ema :
            retValue = emaLookback ( optInTimePeriod );
         break;
         case Wma :
            retValue = wmaLookback ( optInTimePeriod );
         break;
         case Dema :
            retValue = demaLookback ( optInTimePeriod );
         break;
         case Tema :
            retValue = temaLookback ( optInTimePeriod );
         break;
         case Trima :
            retValue = trimaLookback ( optInTimePeriod );
         break;
         case Kama :
            retValue = kamaLookback ( optInTimePeriod );
         break;
         case Mama :
            retValue = mamaLookback ( 0.5, 0.05 );
         break;
         case T3 :
            retValue = t3Lookback ( optInTimePeriod, 0.7 );
         break;
         default:
            retValue = 0;
      }
      return retValue;
   }
   public RetCode movingAverage( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MAType optInMAType,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      double []dummyBuffer ;
      RetCode retCode;
      int nbElement;
      int outIdx, todayIdx;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      if( optInTimePeriod == 1 )
      {
         nbElement = endIdx-startIdx+1;
         outNBElement.value = nbElement;
         for( todayIdx=startIdx, outIdx=0; outIdx < nbElement; outIdx++, todayIdx++ )
            outReal[outIdx] = inReal[todayIdx];
         outBegIdx.value = startIdx;
         return RetCode.Success ;
      }
      switch( optInMAType )
      {
         case Sma :
            retCode = sma ( startIdx, endIdx, inReal, optInTimePeriod,
            outBegIdx, outNBElement, outReal );
         break;
         case Ema :
            retCode = ema ( startIdx, endIdx, inReal, optInTimePeriod,
            outBegIdx, outNBElement, outReal );
         break;
         case Wma :
            retCode = wma ( startIdx, endIdx, inReal, optInTimePeriod,
            outBegIdx, outNBElement, outReal );
         break;
         case Dema :
            retCode = dema ( startIdx, endIdx, inReal, optInTimePeriod,
            outBegIdx, outNBElement, outReal );
         break;
         case Tema :
            retCode = tema ( startIdx, endIdx, inReal, optInTimePeriod,
            outBegIdx, outNBElement, outReal );
         break;
         case Trima :
            retCode = trima ( startIdx, endIdx, inReal, optInTimePeriod,
            outBegIdx, outNBElement, outReal );
         break;
         case Kama :
            retCode = kama ( startIdx, endIdx, inReal, optInTimePeriod,
            outBegIdx, outNBElement, outReal );
         break;
         case Mama :
            dummyBuffer = new double[(endIdx-startIdx+1)] ;
         retCode = mama ( startIdx, endIdx, inReal, 0.5, 0.05,
            outBegIdx, outNBElement,
            outReal, dummyBuffer );
         break;
         case T3 :
            retCode = t3 ( startIdx, endIdx, inReal,
            optInTimePeriod, 0.7,
            outBegIdx, outNBElement, outReal );
         break;
         default:
            retCode = RetCode.BadParam ;
         break;
      }
      return retCode;
   }
   
   /* Generated */
   public int macdLookback( int optInFastPeriod,
      int optInSlowPeriod,
      int optInSignalPeriod )
   {
      int tempInteger;
      if( (int)optInFastPeriod == ( Integer.MIN_VALUE ) )
         optInFastPeriod = 12;
      else if( ((int)optInFastPeriod < 2) || ((int)optInFastPeriod > 100000) )
         return -1;
      if( (int)optInSlowPeriod == ( Integer.MIN_VALUE ) )
         optInSlowPeriod = 26;
      else if( ((int)optInSlowPeriod < 2) || ((int)optInSlowPeriod > 100000) )
         return -1;
      if( (int)optInSignalPeriod == ( Integer.MIN_VALUE ) )
         optInSignalPeriod = 9;
      else if( ((int)optInSignalPeriod < 1) || ((int)optInSignalPeriod > 100000) )
         return -1;
      if( optInSlowPeriod < optInFastPeriod )
      {
         tempInteger = optInSlowPeriod;
         optInSlowPeriod = optInFastPeriod;
         optInFastPeriod = tempInteger;
      }
      return emaLookback ( optInSlowPeriod )
         + emaLookback ( optInSignalPeriod );
   }
   public RetCode macd( int startIdx,
      int endIdx,
      double inReal[],
      int optInFastPeriod,
      int optInSlowPeriod,
      int optInSignalPeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outMACD[],
      double outMACDSignal[],
      double outMACDHist[] )
   {
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInFastPeriod == ( Integer.MIN_VALUE ) )
         optInFastPeriod = 12;
      else if( ((int)optInFastPeriod < 2) || ((int)optInFastPeriod > 100000) )
         return RetCode.BadParam ;
      if( (int)optInSlowPeriod == ( Integer.MIN_VALUE ) )
         optInSlowPeriod = 26;
      else if( ((int)optInSlowPeriod < 2) || ((int)optInSlowPeriod > 100000) )
         return RetCode.BadParam ;
      if( (int)optInSignalPeriod == ( Integer.MIN_VALUE ) )
         optInSignalPeriod = 9;
      else if( ((int)optInSignalPeriod < 1) || ((int)optInSignalPeriod > 100000) )
         return RetCode.BadParam ;
      return TA_INT_MACD ( startIdx, endIdx, inReal,
         optInFastPeriod,
         optInSlowPeriod,
         optInSignalPeriod,
         outBegIdx,
         outNBElement,
         outMACD,
         outMACDSignal,
         outMACDHist );
   }
   RetCode TA_INT_MACD( int startIdx,
      int endIdx,
      double inReal[],
      int optInFastPeriod,
      int optInSlowPeriod,
      int optInSignalPeriod_2,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outMACD[],
      double outMACDSignal[],
      double outMACDHist[] )
   {
      double []slowEMABuffer ;
      double []fastEMABuffer ;
      double k1, k2;
      RetCode retCode;
      int tempInteger;
      MInteger outBegIdx1 = new MInteger() ;
      MInteger outNbElement1 = new MInteger() ;
      MInteger outBegIdx2 = new MInteger() ;
      MInteger outNbElement2 = new MInteger() ;
      int lookbackTotal, lookbackSignal;
      int i;
      if( optInSlowPeriod < optInFastPeriod )
      {
         tempInteger = optInSlowPeriod;
         optInSlowPeriod = optInFastPeriod;
         optInFastPeriod = tempInteger;
      }
      if( optInSlowPeriod != 0 )
         k1 = ((double)2.0 / ((double)(optInSlowPeriod + 1))) ;
      else
      {
         optInSlowPeriod = 26;
         k1 = (double)0.075;
      }
      if( optInFastPeriod != 0 )
         k2 = ((double)2.0 / ((double)(optInFastPeriod + 1))) ;
      else
      {
         optInFastPeriod = 12;
         k2 = (double)0.15;
      }
      lookbackSignal = emaLookback ( optInSignalPeriod_2 );
      lookbackTotal = lookbackSignal;
      lookbackTotal += emaLookback ( optInSlowPeriod );
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      tempInteger = (endIdx-startIdx)+1+lookbackSignal;
      fastEMABuffer = new double[tempInteger] ;
      slowEMABuffer = new double[tempInteger] ;
      tempInteger = startIdx-lookbackSignal;
      retCode = TA_INT_EMA ( tempInteger, endIdx,
         inReal, optInSlowPeriod, k1,
         outBegIdx1 , outNbElement1 , slowEMABuffer );
      if( retCode != RetCode.Success )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return retCode;
      }
      retCode = TA_INT_EMA ( tempInteger, endIdx,
         inReal, optInFastPeriod, k2,
         outBegIdx2 , outNbElement2 , fastEMABuffer );
      if( retCode != RetCode.Success )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return retCode;
      }
      if( ( outBegIdx1.value != tempInteger) ||
         ( outBegIdx2.value != tempInteger) ||
         ( outNbElement1.value != outNbElement2.value ) ||
         ( outNbElement1.value != (endIdx-startIdx)+1+lookbackSignal) )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return (RetCode.InternalError) ;
      }
      for( i=0; i < outNbElement1.value ; i++ )
         fastEMABuffer[i] = fastEMABuffer[i] - slowEMABuffer[i];
      System.arraycopy(fastEMABuffer,lookbackSignal,outMACD,0,(endIdx-startIdx)+1) ;
      retCode = TA_INT_EMA ( 0, outNbElement1.value -1,
         fastEMABuffer, optInSignalPeriod_2, ((double)2.0 / ((double)(optInSignalPeriod_2 + 1))) ,
         outBegIdx2 , outNbElement2 , outMACDSignal );
      if( retCode != RetCode.Success )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return retCode;
      }
      for( i=0; i < outNbElement2.value ; i++ )
         outMACDHist[i] = outMACD[i]-outMACDSignal[i];
      outBegIdx.value = startIdx;
      outNBElement.value = outNbElement2.value ;
      return RetCode.Success ;
   }
   
   
   /* Generated */
   public int macdExtLookback( int optInFastPeriod,
      MAType optInFastMAType,
      int optInSlowPeriod,
      MAType optInSlowMAType,
      int optInSignalPeriod,
      MAType optInSignalMAType )
   {
      int tempInteger, lookbackLargest;
      if( (int)optInFastPeriod == ( Integer.MIN_VALUE ) )
         optInFastPeriod = 12;
      else if( ((int)optInFastPeriod < 2) || ((int)optInFastPeriod > 100000) )
         return -1;
      if( (int)optInSlowPeriod == ( Integer.MIN_VALUE ) )
         optInSlowPeriod = 26;
      else if( ((int)optInSlowPeriod < 2) || ((int)optInSlowPeriod > 100000) )
         return -1;
      if( (int)optInSignalPeriod == ( Integer.MIN_VALUE ) )
         optInSignalPeriod = 9;
      else if( ((int)optInSignalPeriod < 1) || ((int)optInSignalPeriod > 100000) )
         return -1;
      lookbackLargest = movingAverageLookback ( optInFastPeriod, optInFastMAType );
      tempInteger = movingAverageLookback ( optInSlowPeriod, optInSlowMAType );
      if( tempInteger > lookbackLargest )
         lookbackLargest = tempInteger;
      return lookbackLargest + movingAverageLookback ( optInSignalPeriod, optInSignalMAType );
   }
   public RetCode macdExt( int startIdx,
      int endIdx,
      double inReal[],
      int optInFastPeriod,
      MAType optInFastMAType,
      int optInSlowPeriod,
      MAType optInSlowMAType,
      int optInSignalPeriod,
      MAType optInSignalMAType,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outMACD[],
      double outMACDSignal[],
      double outMACDHist[] )
   {
      double []slowMABuffer ;
      double []fastMABuffer ;
      RetCode retCode;
      int tempInteger;
      MInteger outBegIdx1 = new MInteger() ;
      MInteger outNbElement1 = new MInteger() ;
      MInteger outBegIdx2 = new MInteger() ;
      MInteger outNbElement2 = new MInteger() ;
      int lookbackTotal, lookbackSignal, lookbackLargest;
      int i;
      MAType tempMAType;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInFastPeriod == ( Integer.MIN_VALUE ) )
         optInFastPeriod = 12;
      else if( ((int)optInFastPeriod < 2) || ((int)optInFastPeriod > 100000) )
         return RetCode.BadParam ;
      if( (int)optInSlowPeriod == ( Integer.MIN_VALUE ) )
         optInSlowPeriod = 26;
      else if( ((int)optInSlowPeriod < 2) || ((int)optInSlowPeriod > 100000) )
         return RetCode.BadParam ;
      if( (int)optInSignalPeriod == ( Integer.MIN_VALUE ) )
         optInSignalPeriod = 9;
      else if( ((int)optInSignalPeriod < 1) || ((int)optInSignalPeriod > 100000) )
         return RetCode.BadParam ;
      if( optInSlowPeriod < optInFastPeriod )
      {
         tempInteger = optInSlowPeriod;
         optInSlowPeriod = optInFastPeriod;
         optInFastPeriod = tempInteger;
         tempMAType = optInSlowMAType;
         optInSlowMAType = optInFastMAType;
         optInFastMAType = tempMAType;
      }
      lookbackLargest = movingAverageLookback ( optInFastPeriod, optInFastMAType );
      tempInteger = movingAverageLookback ( optInSlowPeriod, optInSlowMAType );
      if( tempInteger > lookbackLargest )
         lookbackLargest = tempInteger;
      lookbackSignal = movingAverageLookback ( optInSignalPeriod, optInSignalMAType );
      lookbackTotal = lookbackSignal+lookbackLargest;
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      tempInteger = (endIdx-startIdx)+1+lookbackSignal;
      fastMABuffer = new double[tempInteger] ;
      slowMABuffer = new double[tempInteger] ;
      tempInteger = startIdx-lookbackSignal;
      retCode = movingAverage ( tempInteger, endIdx,
         inReal, optInSlowPeriod, optInSlowMAType,
         outBegIdx1 , outNbElement1 ,
         slowMABuffer );
      if( retCode != RetCode.Success )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return retCode;
      }
      retCode = movingAverage ( tempInteger, endIdx,
         inReal, optInFastPeriod, optInFastMAType,
         outBegIdx2 , outNbElement2 ,
         fastMABuffer );
      if( retCode != RetCode.Success )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return retCode;
      }
      if( ( outBegIdx1.value != tempInteger) ||
         ( outBegIdx2.value != tempInteger) ||
         ( outNbElement1.value != outNbElement2.value ) ||
         ( outNbElement1.value != (endIdx-startIdx)+1+lookbackSignal) )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return (RetCode.InternalError) ;
      }
      for( i=0; i < outNbElement1.value ; i++ )
         fastMABuffer[i] = fastMABuffer[i] - slowMABuffer[i];
      System.arraycopy(fastMABuffer,lookbackSignal,outMACD,0,(endIdx-startIdx)+1) ;
      retCode = movingAverage ( 0, outNbElement1.value -1,
         fastMABuffer, optInSignalPeriod, optInSignalMAType,
         outBegIdx2 , outNbElement2 , outMACDSignal );
      if( retCode != RetCode.Success )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return retCode;
      }
      for( i=0; i < outNbElement2.value ; i++ )
         outMACDHist[i] = outMACD[i]-outMACDSignal[i];
      outBegIdx.value = startIdx;
      outNBElement.value = outNbElement2.value ;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int macdFixLookback( int optInSignalPeriod )
   {
      if( (int)optInSignalPeriod == ( Integer.MIN_VALUE ) )
         optInSignalPeriod = 9;
      else if( ((int)optInSignalPeriod < 1) || ((int)optInSignalPeriod > 100000) )
         return -1;
      return emaLookback ( 26 )
         + emaLookback ( optInSignalPeriod );
   }
   public RetCode macdFix( int startIdx,
      int endIdx,
      double inReal[],
      int optInSignalPeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outMACD[],
      double outMACDSignal[],
      double outMACDHist[] )
   {
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInSignalPeriod == ( Integer.MIN_VALUE ) )
         optInSignalPeriod = 9;
      else if( ((int)optInSignalPeriod < 1) || ((int)optInSignalPeriod > 100000) )
         return RetCode.BadParam ;
      return TA_INT_MACD ( startIdx, endIdx, inReal,
         0,
         0,
         optInSignalPeriod,
         outBegIdx,
         outNBElement,
         outMACD,
         outMACDSignal,
         outMACDHist );
   }
   
   /* Generated */
   public int mamaLookback( double optInFastLimit,
      double optInSlowLimit )
   {
      if( optInFastLimit == (-4e+37) )
         optInFastLimit = 5.000000e-1;
      else if( (optInFastLimit < 1.000000e-2) || (optInFastLimit > 9.900000e-1) )
         return -1;
      if( optInSlowLimit == (-4e+37) )
         optInSlowLimit = 5.000000e-2;
      else if( (optInSlowLimit < 1.000000e-2) || (optInSlowLimit > 9.900000e-1) )
         return -1;
      return 32 + (this.unstablePeriod[FuncUnstId.Mama.ordinal()]) ;
   }
   public RetCode mama( int startIdx,
      int endIdx,
      double inReal[],
      double optInFastLimit,
      double optInSlowLimit,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outMAMA[],
      double outFAMA[] )
   {
      int outIdx, i;
      int lookbackTotal, today;
      double tempReal, tempReal2;
      double adjustedPrevPeriod, period;
      int trailingWMAIdx;
      double periodWMASum, periodWMASub, trailingWMAValue;
      double smoothedValue;
      final double a = 0.0962;
      final double b = 0.5769;
      double hilbertTempReal;
      int hilbertIdx;
      double []detrender_Odd = new double[3] ; double []detrender_Even = new double[3] ; double detrender; double prev_detrender_Odd ; double prev_detrender_Even ; double prev_detrender_input_Odd ; double prev_detrender_input_Even ;
      double []Q1_Odd = new double[3] ; double []Q1_Even = new double[3] ; double Q1; double prev_Q1_Odd ; double prev_Q1_Even ; double prev_Q1_input_Odd ; double prev_Q1_input_Even ;
      double []jI_Odd = new double[3] ; double []jI_Even = new double[3] ; double jI; double prev_jI_Odd ; double prev_jI_Even ; double prev_jI_input_Odd ; double prev_jI_input_Even ;
      double []jQ_Odd = new double[3] ; double []jQ_Even = new double[3] ; double jQ; double prev_jQ_Odd ; double prev_jQ_Even ; double prev_jQ_input_Odd ; double prev_jQ_input_Even ;
      double Q2, I2, prevQ2, prevI2, Re, Im;
      double I1ForOddPrev2, I1ForOddPrev3;
      double I1ForEvenPrev2, I1ForEvenPrev3;
      double rad2Deg;
      double mama,fama,todayValue,prevPhase;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( optInFastLimit == (-4e+37) )
         optInFastLimit = 5.000000e-1;
      else if( (optInFastLimit < 1.000000e-2) || (optInFastLimit > 9.900000e-1) )
         return RetCode.BadParam ;
      if( optInSlowLimit == (-4e+37) )
         optInSlowLimit = 5.000000e-2;
      else if( (optInSlowLimit < 1.000000e-2) || (optInSlowLimit > 9.900000e-1) )
         return RetCode.BadParam ;
      rad2Deg = 180.0 / (4.0 * Math.atan (1));
      lookbackTotal = 32 + (this.unstablePeriod[FuncUnstId.Mama.ordinal()]) ;
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outBegIdx.value = startIdx;
      trailingWMAIdx = startIdx - lookbackTotal;
      today = trailingWMAIdx;
      tempReal = inReal[today++];
      periodWMASub = tempReal;
      periodWMASum = tempReal;
      tempReal = inReal[today++];
      periodWMASub += tempReal;
      periodWMASum += tempReal*2.0;
      tempReal = inReal[today++];
      periodWMASub += tempReal;
      periodWMASum += tempReal*3.0;
      trailingWMAValue = 0.0;
      i = 9;
      do
      {
         tempReal = inReal[today++];
         { periodWMASub += tempReal; periodWMASub -= trailingWMAValue; periodWMASum += tempReal*4.0; trailingWMAValue = inReal[trailingWMAIdx++]; smoothedValue = periodWMASum*0.1; periodWMASum -= periodWMASub; } ;
      } while( --i != 0);
      hilbertIdx = 0;
      { detrender_Odd [0] = 0.0; detrender_Odd [1] = 0.0; detrender_Odd [2] = 0.0; detrender_Even [0] = 0.0; detrender_Even [1] = 0.0; detrender_Even [2] = 0.0; detrender = 0.0; prev_detrender_Odd = 0.0; prev_detrender_Even = 0.0; prev_detrender_input_Odd = 0.0; prev_detrender_input_Even = 0.0; } ;
      { Q1_Odd [0] = 0.0; Q1_Odd [1] = 0.0; Q1_Odd [2] = 0.0; Q1_Even [0] = 0.0; Q1_Even [1] = 0.0; Q1_Even [2] = 0.0; Q1 = 0.0; prev_Q1_Odd = 0.0; prev_Q1_Even = 0.0; prev_Q1_input_Odd = 0.0; prev_Q1_input_Even = 0.0; } ;
      { jI_Odd [0] = 0.0; jI_Odd [1] = 0.0; jI_Odd [2] = 0.0; jI_Even [0] = 0.0; jI_Even [1] = 0.0; jI_Even [2] = 0.0; jI = 0.0; prev_jI_Odd = 0.0; prev_jI_Even = 0.0; prev_jI_input_Odd = 0.0; prev_jI_input_Even = 0.0; } ;
      { jQ_Odd [0] = 0.0; jQ_Odd [1] = 0.0; jQ_Odd [2] = 0.0; jQ_Even [0] = 0.0; jQ_Even [1] = 0.0; jQ_Even [2] = 0.0; jQ = 0.0; prev_jQ_Odd = 0.0; prev_jQ_Even = 0.0; prev_jQ_input_Odd = 0.0; prev_jQ_input_Even = 0.0; } ;
      period = 0.0;
      outIdx = 0;
      prevI2 = prevQ2 = 0.0;
      Re = Im = 0.0;
      mama = fama = 0.0;
      I1ForOddPrev3 = I1ForEvenPrev3 = 0.0;
      I1ForOddPrev2 = I1ForEvenPrev2 = 0.0;
      prevPhase = 0.0;
      while( today <= endIdx )
      {
         adjustedPrevPeriod = (0.075*period)+0.54;
         todayValue = inReal[today];
         { periodWMASub += todayValue; periodWMASub -= trailingWMAValue; periodWMASum += todayValue*4.0; trailingWMAValue = inReal[trailingWMAIdx++]; smoothedValue = periodWMASum*0.1; periodWMASum -= periodWMASub; } ;
         if( (today%2) == 0 )
         {
            { hilbertTempReal = a * smoothedValue; detrender = -detrender_Even [hilbertIdx]; detrender_Even [hilbertIdx] = hilbertTempReal; detrender += hilbertTempReal; detrender -= prev_detrender_Even ; prev_detrender_Even = b * prev_detrender_input_Even ; detrender += prev_detrender_Even ; prev_detrender_input_Even = smoothedValue; detrender *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * detrender; Q1 = -Q1_Even [hilbertIdx]; Q1_Even [hilbertIdx] = hilbertTempReal; Q1 += hilbertTempReal; Q1 -= prev_Q1_Even ; prev_Q1_Even = b * prev_Q1_input_Even ; Q1 += prev_Q1_Even ; prev_Q1_input_Even = detrender; Q1 *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * I1ForEvenPrev3; jI = -jI_Even [hilbertIdx]; jI_Even [hilbertIdx] = hilbertTempReal; jI += hilbertTempReal; jI -= prev_jI_Even ; prev_jI_Even = b * prev_jI_input_Even ; jI += prev_jI_Even ; prev_jI_input_Even = I1ForEvenPrev3; jI *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * Q1; jQ = -jQ_Even [hilbertIdx]; jQ_Even [hilbertIdx] = hilbertTempReal; jQ += hilbertTempReal; jQ -= prev_jQ_Even ; prev_jQ_Even = b * prev_jQ_input_Even ; jQ += prev_jQ_Even ; prev_jQ_input_Even = Q1; jQ *= adjustedPrevPeriod; } ;
            if( ++hilbertIdx == 3 )
               hilbertIdx = 0;
            Q2 = (0.2*(Q1 + jI)) + (0.8*prevQ2);
            I2 = (0.2*(I1ForEvenPrev3 - jQ)) + (0.8*prevI2);
            I1ForOddPrev3 = I1ForOddPrev2;
            I1ForOddPrev2 = detrender;
            if( I1ForEvenPrev3 != 0.0 )
               tempReal2 = ( Math.atan (Q1/I1ForEvenPrev3)*rad2Deg);
            else
               tempReal2 = 0.0;
         }
         else
         {
            { hilbertTempReal = a * smoothedValue; detrender = -detrender_Odd [hilbertIdx]; detrender_Odd [hilbertIdx] = hilbertTempReal; detrender += hilbertTempReal; detrender -= prev_detrender_Odd ; prev_detrender_Odd = b * prev_detrender_input_Odd ; detrender += prev_detrender_Odd ; prev_detrender_input_Odd = smoothedValue; detrender *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * detrender; Q1 = -Q1_Odd [hilbertIdx]; Q1_Odd [hilbertIdx] = hilbertTempReal; Q1 += hilbertTempReal; Q1 -= prev_Q1_Odd ; prev_Q1_Odd = b * prev_Q1_input_Odd ; Q1 += prev_Q1_Odd ; prev_Q1_input_Odd = detrender; Q1 *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * I1ForOddPrev3; jI = -jI_Odd [hilbertIdx]; jI_Odd [hilbertIdx] = hilbertTempReal; jI += hilbertTempReal; jI -= prev_jI_Odd ; prev_jI_Odd = b * prev_jI_input_Odd ; jI += prev_jI_Odd ; prev_jI_input_Odd = I1ForOddPrev3; jI *= adjustedPrevPeriod; } ;
            { hilbertTempReal = a * Q1; jQ = -jQ_Odd [hilbertIdx]; jQ_Odd [hilbertIdx] = hilbertTempReal; jQ += hilbertTempReal; jQ -= prev_jQ_Odd ; prev_jQ_Odd = b * prev_jQ_input_Odd ; jQ += prev_jQ_Odd ; prev_jQ_input_Odd = Q1; jQ *= adjustedPrevPeriod; } ;
            Q2 = (0.2*(Q1 + jI)) + (0.8*prevQ2);
            I2 = (0.2*(I1ForOddPrev3 - jQ)) + (0.8*prevI2);
            I1ForEvenPrev3 = I1ForEvenPrev2;
            I1ForEvenPrev2 = detrender;
            if( I1ForOddPrev3 != 0.0 )
               tempReal2 = ( Math.atan (Q1/I1ForOddPrev3)*rad2Deg);
            else
               tempReal2 = 0.0;
         }
         tempReal = prevPhase - tempReal2;
         prevPhase = tempReal2;
         if( tempReal < 1.0 )
            tempReal = 1.0;
         if( tempReal > 1.0 )
         {
            tempReal = optInFastLimit/tempReal;
            if( tempReal < optInSlowLimit )
               tempReal = optInSlowLimit;
         }
         else
         {
            tempReal = optInFastLimit;
         }
         mama = (tempReal*todayValue)+((1-tempReal)*mama);
         tempReal *= 0.5;
         fama = (tempReal*mama)+((1-tempReal)*fama);
         if( today >= startIdx )
         {
            outMAMA[outIdx] = mama;
            outFAMA[outIdx++] = fama;
         }
         Re = (0.2*((I2*prevI2)+(Q2*prevQ2)))+(0.8*Re);
         Im = (0.2*((I2*prevQ2)-(Q2*prevI2)))+(0.8*Im);
         prevQ2 = Q2;
         prevI2 = I2;
         tempReal = period;
         if( (Im != 0.0) && (Re != 0.0) )
            period = 360.0 / ( Math.atan (Im/Re)*rad2Deg);
         tempReal2 = 1.5*tempReal;
         if( period > tempReal2)
            period = tempReal2;
         tempReal2 = 0.67*tempReal;
         if( period < tempReal2 )
            period = tempReal2;
         if( period < 6 )
            period = 6;
         else if( period > 50 )
            period = 50;
         period = (0.2*period) + (0.8 * tempReal);
         today++;
      }
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int movingAverageVariablePeriodLookback( int optInMinPeriod,
      int optInMaxPeriod,
      MAType optInMAType )
   {
      if( (int)optInMinPeriod == ( Integer.MIN_VALUE ) )
         optInMinPeriod = 2;
      else if( ((int)optInMinPeriod < 2) || ((int)optInMinPeriod > 100000) )
         return -1;
      if( (int)optInMaxPeriod == ( Integer.MIN_VALUE ) )
         optInMaxPeriod = 30;
      else if( ((int)optInMaxPeriod < 2) || ((int)optInMaxPeriod > 100000) )
         return -1;
      return movingAverageLookback (optInMaxPeriod, optInMAType);
   }
   public RetCode movingAverageVariablePeriod( int startIdx,
      int endIdx,
      double inReal[],
      double inPeriods[],
      int optInMinPeriod,
      int optInMaxPeriod,
      MAType optInMAType,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int i, j, lookbackTotal, outputSize, tempInt, curPeriod;
      int []localPeriodArray ;
      double []localOutputArray ;
      MInteger localBegIdx = new MInteger() ;
      MInteger localNbElement = new MInteger() ;
      RetCode retCode;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInMinPeriod == ( Integer.MIN_VALUE ) )
         optInMinPeriod = 2;
      else if( ((int)optInMinPeriod < 2) || ((int)optInMinPeriod > 100000) )
         return RetCode.BadParam ;
      if( (int)optInMaxPeriod == ( Integer.MIN_VALUE ) )
         optInMaxPeriod = 30;
      else if( ((int)optInMaxPeriod < 2) || ((int)optInMaxPeriod > 100000) )
         return RetCode.BadParam ;
      lookbackTotal = movingAverageLookback (optInMaxPeriod,optInMAType);
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      if( lookbackTotal > startIdx )
         tempInt = lookbackTotal;
      else
         tempInt = startIdx;
      if( tempInt > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outputSize = endIdx - tempInt + 1;
      localOutputArray = new double[outputSize] ;
      localPeriodArray = new int[outputSize] ;
      for( i=0; i < outputSize; i++ )
      {
         tempInt = (int)(inPeriods[startIdx+i]);
         if( tempInt < optInMinPeriod )
            tempInt = optInMinPeriod;
         else if( tempInt > optInMaxPeriod )
            tempInt = optInMaxPeriod;
         localPeriodArray[i] = tempInt;
      }
      for( i=0; i < outputSize; i++ )
      {
         curPeriod = localPeriodArray[i];
         if( curPeriod != 0 )
         {
            retCode = movingAverage ( startIdx, endIdx, inReal,
               curPeriod, optInMAType,
               localBegIdx , localNbElement ,localOutputArray );
            if( retCode != RetCode.Success )
            {
               outBegIdx.value = 0 ;
               outNBElement.value = 0 ;
               return retCode;
            }
            outReal[i] = localOutputArray[i];
            for( j=i+1; j < outputSize; j++ )
            {
               if( localPeriodArray[j] == curPeriod )
               {
                  localPeriodArray[j] = 0;
                  outReal[j] = localOutputArray[j];
               }
            }
         }
      }
      outBegIdx.value = startIdx;
      outNBElement.value = outputSize;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int maxLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      return (optInTimePeriod-1);
   }
   public RetCode max( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      double highest, tmp;
      int outIdx, nbInitialElementNeeded;
      int trailingIdx, today, i, highestIdx;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      nbInitialElementNeeded = (optInTimePeriod-1);
      if( startIdx < nbInitialElementNeeded )
         startIdx = nbInitialElementNeeded;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      today = startIdx;
      trailingIdx = startIdx-nbInitialElementNeeded;
      highestIdx = -1;
      highest = 0.0;
      while( today <= endIdx )
      {
         tmp = inReal[today];
         if( highestIdx < trailingIdx )
         {
            highestIdx = trailingIdx;
            highest = inReal[highestIdx];
            i = highestIdx;
            while( ++i<=today )
            {
               tmp = inReal[i];
               if( tmp > highest )
               {
                  highestIdx = i;
                  highest = tmp;
               }
            }
         }
         else if( tmp >= highest )
         {
            highestIdx = today;
            highest = tmp;
         }
         outReal[outIdx++] = highest;
         trailingIdx++;
         today++;
      }
      outBegIdx.value = startIdx;
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int maxIndexLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      return (optInTimePeriod-1);
   }
   public RetCode maxIndex( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double highest, tmp;
      int outIdx, nbInitialElementNeeded;
      int trailingIdx, today, i, highestIdx;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      nbInitialElementNeeded = (optInTimePeriod-1);
      if( startIdx < nbInitialElementNeeded )
         startIdx = nbInitialElementNeeded;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      today = startIdx;
      trailingIdx = startIdx-nbInitialElementNeeded;
      highestIdx = -1;
      highest = 0.0;
      while( today <= endIdx )
      {
         tmp = inReal[today];
         if( highestIdx < trailingIdx )
         {
            highestIdx = trailingIdx;
            highest = inReal[highestIdx];
            i = highestIdx;
            while( ++i<=today )
            {
               tmp = inReal[i];
               if( tmp > highest )
               {
                  highestIdx = i;
                  highest = tmp;
               }
            }
         }
         else if( tmp >= highest )
         {
            highestIdx = today;
            highest = tmp;
         }
         outInteger[outIdx++] = highestIdx;
         trailingIdx++;
         today++;
      }
      outBegIdx.value = startIdx;
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int medPriceLookback( )
   {
      return 0;
   }
   public RetCode medPrice( int startIdx,
      int endIdx,
      double inHigh[],
      double inLow[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx, i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      outIdx = 0;
      for( i=startIdx; i <= endIdx; i++ )
      {
         outReal[outIdx++] = (inHigh[i]+inLow[i])/2.0;
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int mfiLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      return optInTimePeriod + (this.unstablePeriod[FuncUnstId.Mfi.ordinal()]) ;
   }
   public RetCode mfi( int startIdx,
      int endIdx,
      double inHigh[],
      double inLow[],
      double inClose[],
      double inVolume[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      double posSumMF, negSumMF, prevValue;
      double tempValue1, tempValue2;
      int lookbackTotal, outIdx, i, today;
      int mflow_Idx = 0; MoneyFlow []mflow; int maxIdx_mflow = (50-1) ;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      { if( optInTimePeriod <= 0 ) return RetCode.AllocErr ; mflow = new MoneyFlow[optInTimePeriod]; for( int _mflow_index =0; _mflow_index <mflow.length; _mflow_index ++) { mflow[_mflow_index ]=new MoneyFlow(); } maxIdx_mflow = (optInTimePeriod-1); } ;
      outBegIdx.value = 0 ;
      outNBElement.value = 0 ;
      lookbackTotal = optInTimePeriod + (this.unstablePeriod[FuncUnstId.Mfi.ordinal()]) ;
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         return RetCode.Success ;
      }
      outIdx = 0;
      today = startIdx-lookbackTotal;
      prevValue = (inHigh[today]+inLow[today]+inClose[today])/3.0;
      posSumMF = 0.0;
      negSumMF = 0.0;
      today++;
      for( i=optInTimePeriod; i > 0; i-- )
      {
         tempValue1 = (inHigh[today]+inLow[today]+inClose[today])/3.0;
         tempValue2 = tempValue1 - prevValue;
         prevValue = tempValue1;
         tempValue1 *= inVolume[today++];
         if( tempValue2 < 0 )
         {
            (mflow[mflow_Idx]). negative = tempValue1;
            negSumMF += tempValue1;
            (mflow[mflow_Idx]). positive = 0.0;
         }
         else if( tempValue2 > 0 )
         {
            (mflow[mflow_Idx]). positive = tempValue1;
            posSumMF += tempValue1;
            (mflow[mflow_Idx]). negative = 0.0;
         }
         else
         {
            (mflow[mflow_Idx]). positive = 0.0;
            (mflow[mflow_Idx]). negative = 0.0;
         }
         { mflow_Idx ++; if( mflow_Idx > maxIdx_mflow ) mflow_Idx = 0; } ;
      }
      if( today > startIdx )
      {
         tempValue1 = posSumMF+negSumMF;
         if( tempValue1 < 1.0 )
            outReal[outIdx++] = 0.0;
         else
            outReal[outIdx++] = 100.0*(posSumMF/tempValue1);
      }
      else
      {
         while( today < startIdx )
         {
            posSumMF -= (mflow[mflow_Idx]). positive;
            negSumMF -= (mflow[mflow_Idx]). negative;
            tempValue1 = (inHigh[today]+inLow[today]+inClose[today])/3.0;
            tempValue2 = tempValue1 - prevValue;
            prevValue = tempValue1;
            tempValue1 *= inVolume[today++];
            if( tempValue2 < 0 )
            {
               (mflow[mflow_Idx]). negative = tempValue1;
               negSumMF += tempValue1;
               (mflow[mflow_Idx]). positive = 0.0;
            }
            else if( tempValue2 > 0 )
            {
               (mflow[mflow_Idx]). positive = tempValue1;
               posSumMF += tempValue1;
               (mflow[mflow_Idx]). negative = 0.0;
            }
            else
            {
               (mflow[mflow_Idx]). positive = 0.0;
               (mflow[mflow_Idx]). negative = 0.0;
            }
            { mflow_Idx ++; if( mflow_Idx > maxIdx_mflow ) mflow_Idx = 0; } ;
         }
      }
      while( today <= endIdx )
      {
         posSumMF -= (mflow[mflow_Idx]). positive;
         negSumMF -= (mflow[mflow_Idx]). negative;
         tempValue1 = (inHigh[today]+inLow[today]+inClose[today])/3.0;
         tempValue2 = tempValue1 - prevValue;
         prevValue = tempValue1;
         tempValue1 *= inVolume[today++];
         if( tempValue2 < 0 )
         {
            (mflow[mflow_Idx]). negative = tempValue1;
            negSumMF += tempValue1;
            (mflow[mflow_Idx]). positive = 0.0;
         }
         else if( tempValue2 > 0 )
         {
            (mflow[mflow_Idx]). positive = tempValue1;
            posSumMF += tempValue1;
            (mflow[mflow_Idx]). negative = 0.0;
         }
         else
         {
            (mflow[mflow_Idx]). positive = 0.0;
            (mflow[mflow_Idx]). negative = 0.0;
         }
         tempValue1 = posSumMF+negSumMF;
         if( tempValue1 < 1.0 )
            outReal[outIdx++] = 0.0;
         else
            outReal[outIdx++] = 100.0*(posSumMF/tempValue1);
         { mflow_Idx ++; if( mflow_Idx > maxIdx_mflow ) mflow_Idx = 0; } ;
      }
      outBegIdx.value = startIdx;
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int midPointLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      return (optInTimePeriod-1);
   }
   public RetCode midPoint( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      double lowest, highest, tmp;
      int outIdx, nbInitialElementNeeded;
      int trailingIdx, today, i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      nbInitialElementNeeded = (optInTimePeriod-1);
      if( startIdx < nbInitialElementNeeded )
         startIdx = nbInitialElementNeeded;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      today = startIdx;
      trailingIdx = startIdx-nbInitialElementNeeded;
      while( today <= endIdx )
      {
         lowest = inReal[trailingIdx++];
         highest = lowest;
         for( i=trailingIdx; i <= today; i++ )
         {
            tmp = inReal[i];
            if( tmp < lowest ) lowest= tmp;
            else if( tmp > highest) highest = tmp;
         }
         outReal[outIdx++] = (highest+lowest)/2.0;
         today++;
      }
      outBegIdx.value = startIdx;
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int midPriceLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      return (optInTimePeriod-1);
   }
   public RetCode midPrice( int startIdx,
      int endIdx,
      double inHigh[],
      double inLow[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      double lowest, highest, tmp;
      int outIdx, nbInitialElementNeeded;
      int trailingIdx, today, i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      nbInitialElementNeeded = (optInTimePeriod-1);
      if( startIdx < nbInitialElementNeeded )
         startIdx = nbInitialElementNeeded;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      today = startIdx;
      trailingIdx = startIdx-nbInitialElementNeeded;
      while( today <= endIdx )
      {
         lowest = inLow[trailingIdx];
         highest = inHigh[trailingIdx];
         trailingIdx++;
         for( i=trailingIdx; i <= today; i++ )
         {
            tmp = inLow[i];
            if( tmp < lowest ) lowest= tmp;
            tmp = inHigh[i];
            if( tmp > highest) highest = tmp;
         }
         outReal[outIdx++] = (highest+lowest)/2.0;
         today++;
      }
      outBegIdx.value = startIdx;
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int minLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      return (optInTimePeriod-1);
   }
   public RetCode min( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      double lowest, tmp;
      int outIdx, nbInitialElementNeeded;
      int trailingIdx, lowestIdx, today, i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      nbInitialElementNeeded = (optInTimePeriod-1);
      if( startIdx < nbInitialElementNeeded )
         startIdx = nbInitialElementNeeded;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      today = startIdx;
      trailingIdx = startIdx-nbInitialElementNeeded;
      lowestIdx = -1;
      lowest = 0.0;
      while( today <= endIdx )
      {
         tmp = inReal[today];
         if( lowestIdx < trailingIdx )
         {
            lowestIdx = trailingIdx;
            lowest = inReal[lowestIdx];
            i = lowestIdx;
            while( ++i<=today )
            {
               tmp = inReal[i];
               if( tmp < lowest )
               {
                  lowestIdx = i;
                  lowest = tmp;
               }
            }
         }
         else if( tmp <= lowest )
         {
            lowestIdx = today;
            lowest = tmp;
         }
         outReal[outIdx++] = lowest;
         trailingIdx++;
         today++;
      }
      outBegIdx.value = startIdx;
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int minIndexLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      return (optInTimePeriod-1);
   }
   public RetCode minIndex( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      int outInteger[] )
   {
      double lowest, tmp;
      int outIdx, nbInitialElementNeeded;
      int trailingIdx, lowestIdx, today, i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      nbInitialElementNeeded = (optInTimePeriod-1);
      if( startIdx < nbInitialElementNeeded )
         startIdx = nbInitialElementNeeded;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      today = startIdx;
      trailingIdx = startIdx-nbInitialElementNeeded;
      lowestIdx = -1;
      lowest = 0.0;
      while( today <= endIdx )
      {
         tmp = inReal[today];
         if( lowestIdx < trailingIdx )
         {
            lowestIdx = trailingIdx;
            lowest = inReal[lowestIdx];
            i = lowestIdx;
            while( ++i<=today )
            {
               tmp = inReal[i];
               if( tmp < lowest )
               {
                  lowestIdx = i;
                  lowest = tmp;
               }
            }
         }
         else if( tmp <= lowest )
         {
            lowestIdx = today;
            lowest = tmp;
         }
         outInteger[outIdx++] = lowestIdx;
         trailingIdx++;
         today++;
      }
      outBegIdx.value = startIdx;
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int minMaxLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      return (optInTimePeriod-1);
   }
   public RetCode minMax( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outMin[],
      double outMax[] )
   {
      double highest, lowest, tmpHigh, tmpLow;
      int outIdx, nbInitialElementNeeded;
      int trailingIdx, today, i, highestIdx, lowestIdx;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      nbInitialElementNeeded = (optInTimePeriod-1);
      if( startIdx < nbInitialElementNeeded )
         startIdx = nbInitialElementNeeded;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      today = startIdx;
      trailingIdx = startIdx-nbInitialElementNeeded;
      highestIdx = -1;
      highest = 0.0;
      lowestIdx = -1;
      lowest = 0.0;
      while( today <= endIdx )
      {
         tmpLow = tmpHigh = inReal[today];
         if( highestIdx < trailingIdx )
         {
            highestIdx = trailingIdx;
            highest = inReal[highestIdx];
            i = highestIdx;
            while( ++i<=today )
            {
               tmpHigh = inReal[i];
               if( tmpHigh > highest )
               {
                  highestIdx = i;
                  highest = tmpHigh;
               }
            }
         }
         else if( tmpHigh >= highest )
         {
            highestIdx = today;
            highest = tmpHigh;
         }
         if( lowestIdx < trailingIdx )
         {
            lowestIdx = trailingIdx;
            lowest = inReal[lowestIdx];
            i = lowestIdx;
            while( ++i<=today )
            {
               tmpLow = inReal[i];
               if( tmpLow < lowest )
               {
                  lowestIdx = i;
                  lowest = tmpLow;
               }
            }
         }
         else if( tmpLow <= lowest )
         {
            lowestIdx = today;
            lowest = tmpLow;
         }
         outMax[outIdx] = highest;
         outMin[outIdx] = lowest;
         outIdx++;
         trailingIdx++;
         today++;
      }
      outBegIdx.value = startIdx;
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int minMaxIndexLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      return (optInTimePeriod-1);
   }
   public RetCode minMaxIndex( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      int outMinIdx[],
      int outMaxIdx[] )
   {
      double highest, lowest, tmpHigh, tmpLow;
      int outIdx, nbInitialElementNeeded;
      int trailingIdx, today, i, highestIdx, lowestIdx;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      nbInitialElementNeeded = (optInTimePeriod-1);
      if( startIdx < nbInitialElementNeeded )
         startIdx = nbInitialElementNeeded;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      today = startIdx;
      trailingIdx = startIdx-nbInitialElementNeeded;
      highestIdx = -1;
      highest = 0.0;
      lowestIdx = -1;
      lowest = 0.0;
      while( today <= endIdx )
      {
         tmpLow = tmpHigh = inReal[today];
         if( highestIdx < trailingIdx )
         {
            highestIdx = trailingIdx;
            highest = inReal[highestIdx];
            i = highestIdx;
            while( ++i<=today )
            {
               tmpHigh = inReal[i];
               if( tmpHigh > highest )
               {
                  highestIdx = i;
                  highest = tmpHigh;
               }
            }
         }
         else if( tmpHigh >= highest )
         {
            highestIdx = today;
            highest = tmpHigh;
         }
         if( lowestIdx < trailingIdx )
         {
            lowestIdx = trailingIdx;
            lowest = inReal[lowestIdx];
            i = lowestIdx;
            while( ++i<=today )
            {
               tmpLow = inReal[i];
               if( tmpLow < lowest )
               {
                  lowestIdx = i;
                  lowest = tmpLow;
               }
            }
         }
         else if( tmpLow <= lowest )
         {
            lowestIdx = today;
            lowest = tmpLow;
         }
         outMaxIdx[outIdx] = highestIdx;
         outMinIdx[outIdx] = lowestIdx;
         outIdx++;
         trailingIdx++;
         today++;
      }
      outBegIdx.value = startIdx;
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int minusDILookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return -1;
      if( optInTimePeriod > 1 )
         return optInTimePeriod + (this.unstablePeriod[FuncUnstId.MinusDI.ordinal()]) ;
      else
         return 1;
   }
   public RetCode minusDI( int startIdx,
      int endIdx,
      double inHigh[],
      double inLow[],
      double inClose[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int today, lookbackTotal, outIdx;
      double prevHigh, prevLow, prevClose;
      double prevMinusDM, prevTR;
      double tempReal, tempReal2, diffP, diffM;
      int i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      if( optInTimePeriod > 1 )
         lookbackTotal = optInTimePeriod + (this.unstablePeriod[FuncUnstId.MinusDI.ordinal()]) ;
      else
         lookbackTotal = 1;
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      if( optInTimePeriod <= 1 )
      {
         outBegIdx.value = startIdx;
         today = startIdx-1;
         prevHigh = inHigh[today];
         prevLow = inLow[today];
         prevClose = inClose[today];
         while( today < endIdx )
         {
            today++;
            tempReal = inHigh[today];
            diffP = tempReal-prevHigh;
            prevHigh = tempReal;
            tempReal = inLow[today];
            diffM = prevLow-tempReal;
            prevLow = tempReal;
            if( (diffM > 0) && (diffP < diffM) )
            {
               { tempReal = prevHigh-prevLow; tempReal2 = Math.abs (prevHigh-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; tempReal2 = Math.abs (prevLow-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; } ;
               if( (((-0.00000001)<tempReal)&&(tempReal<0.00000001)) )
                  outReal[outIdx++] = (double)0.0;
               else
                  outReal[outIdx++] = diffM/tempReal;
            }
            else
               outReal[outIdx++] = (double)0.0;
            prevClose = inClose[today];
         }
         outNBElement.value = outIdx;
         return RetCode.Success ;
      }
      outBegIdx.value = today = startIdx;
      prevMinusDM = 0.0;
      prevTR = 0.0;
      today = startIdx - lookbackTotal;
      prevHigh = inHigh[today];
      prevLow = inLow[today];
      prevClose = inClose[today];
      i = optInTimePeriod-1;
      while( i-- > 0 )
      {
         today++;
         tempReal = inHigh[today];
         diffP = tempReal-prevHigh;
         prevHigh = tempReal;
         tempReal = inLow[today];
         diffM = prevLow-tempReal;
         prevLow = tempReal;
         if( (diffM > 0) && (diffP < diffM) )
         {
            prevMinusDM += diffM;
         }
         { tempReal = prevHigh-prevLow; tempReal2 = Math.abs (prevHigh-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; tempReal2 = Math.abs (prevLow-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; } ;
         prevTR += tempReal;
         prevClose = inClose[today];
      }
      i = (this.unstablePeriod[FuncUnstId.MinusDI.ordinal()]) + 1;
      while( i-- != 0 )
      {
         today++;
         tempReal = inHigh[today];
         diffP = tempReal-prevHigh;
         prevHigh = tempReal;
         tempReal = inLow[today];
         diffM = prevLow-tempReal;
         prevLow = tempReal;
         if( (diffM > 0) && (diffP < diffM) )
         {
            prevMinusDM = prevMinusDM - (prevMinusDM/optInTimePeriod) + diffM;
         }
         else
         {
            prevMinusDM = prevMinusDM - (prevMinusDM/optInTimePeriod);
         }
         { tempReal = prevHigh-prevLow; tempReal2 = Math.abs (prevHigh-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; tempReal2 = Math.abs (prevLow-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; } ;
         prevTR = prevTR - (prevTR/optInTimePeriod) + tempReal;
         prevClose = inClose[today];
      }
      if( ! (((-0.00000001)<prevTR)&&(prevTR<0.00000001)) )
         outReal[0] = (100.0*(prevMinusDM/prevTR)) ;
      else
         outReal[0] = 0.0;
      outIdx = 1;
      while( today < endIdx )
      {
         today++;
         tempReal = inHigh[today];
         diffP = tempReal-prevHigh;
         prevHigh = tempReal;
         tempReal = inLow[today];
         diffM = prevLow-tempReal;
         prevLow = tempReal;
         if( (diffM > 0) && (diffP < diffM) )
         {
            prevMinusDM = prevMinusDM - (prevMinusDM/optInTimePeriod) + diffM;
         }
         else
         {
            prevMinusDM = prevMinusDM - (prevMinusDM/optInTimePeriod);
         }
         { tempReal = prevHigh-prevLow; tempReal2 = Math.abs (prevHigh-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; tempReal2 = Math.abs (prevLow-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; } ;
         prevTR = prevTR - (prevTR/optInTimePeriod) + tempReal;
         prevClose = inClose[today];
         if( ! (((-0.00000001)<prevTR)&&(prevTR<0.00000001)) )
            outReal[outIdx++] = (100.0*(prevMinusDM/prevTR)) ;
         else
            outReal[outIdx++] = 0.0;
      }
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int minusDMLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return -1;
      if( optInTimePeriod > 1 )
         return optInTimePeriod + (this.unstablePeriod[FuncUnstId.MinusDM.ordinal()]) - 1;
      else
         return 1;
   }
   public RetCode minusDM( int startIdx,
      int endIdx,
      double inHigh[],
      double inLow[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int today, lookbackTotal, outIdx;
      double prevHigh, prevLow, tempReal;
      double prevMinusDM;
      double diffP, diffM;
      int i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      if( optInTimePeriod > 1 )
         lookbackTotal = optInTimePeriod + (this.unstablePeriod[FuncUnstId.MinusDM.ordinal()]) - 1;
      else
         lookbackTotal = 1;
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      if( optInTimePeriod <= 1 )
      {
         outBegIdx.value = startIdx;
         today = startIdx-1;
         prevHigh = inHigh[today];
         prevLow = inLow[today];
         while( today < endIdx )
         {
            today++;
            tempReal = inHigh[today];
            diffP = tempReal-prevHigh;
            prevHigh = tempReal;
            tempReal = inLow[today];
            diffM = prevLow-tempReal;
            prevLow = tempReal;
            if( (diffM > 0) && (diffP < diffM) )
            {
               outReal[outIdx++] = diffM;
            }
            else
               outReal[outIdx++] = 0;
         }
         outNBElement.value = outIdx;
         return RetCode.Success ;
      }
      outBegIdx.value = startIdx;
      prevMinusDM = 0.0;
      today = startIdx - lookbackTotal;
      prevHigh = inHigh[today];
      prevLow = inLow[today];
      i = optInTimePeriod-1;
      while( i-- > 0 )
      {
         today++;
         tempReal = inHigh[today];
         diffP = tempReal-prevHigh;
         prevHigh = tempReal;
         tempReal = inLow[today];
         diffM = prevLow-tempReal;
         prevLow = tempReal;
         if( (diffM > 0) && (diffP < diffM) )
         {
            prevMinusDM += diffM;
         }
      }
      i = (this.unstablePeriod[FuncUnstId.MinusDM.ordinal()]) ;
      while( i-- != 0 )
      {
         today++;
         tempReal = inHigh[today];
         diffP = tempReal-prevHigh;
         prevHigh = tempReal;
         tempReal = inLow[today];
         diffM = prevLow-tempReal;
         prevLow = tempReal;
         if( (diffM > 0) && (diffP < diffM) )
         {
            prevMinusDM = prevMinusDM - (prevMinusDM/optInTimePeriod) + diffM;
         }
         else
         {
            prevMinusDM = prevMinusDM - (prevMinusDM/optInTimePeriod);
         }
      }
      outReal[0] = prevMinusDM;
      outIdx = 1;
      while( today < endIdx )
      {
         today++;
         tempReal = inHigh[today];
         diffP = tempReal-prevHigh;
         prevHigh = tempReal;
         tempReal = inLow[today];
         diffM = prevLow-tempReal;
         prevLow = tempReal;
         if( (diffM > 0) && (diffP < diffM) )
         {
            prevMinusDM = prevMinusDM - (prevMinusDM/optInTimePeriod) + diffM;
         }
         else
         {
            prevMinusDM = prevMinusDM - (prevMinusDM/optInTimePeriod);
         }
         outReal[outIdx++] = prevMinusDM;
      }
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int momLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 10;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return -1;
      return optInTimePeriod;
   }
   public RetCode mom( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int inIdx, outIdx, trailingIdx;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 10;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      if( startIdx < optInTimePeriod )
         startIdx = optInTimePeriod;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      inIdx = startIdx;
      trailingIdx = startIdx - optInTimePeriod;
      while( inIdx <= endIdx )
         outReal[outIdx++] = inReal[inIdx++] - inReal[trailingIdx++];
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int multLookback( )
   {
      return 0;
   }
   public RetCode mult( int startIdx,
      int endIdx,
      double inReal0[],
      double inReal1[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx;
      int i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      for( i=startIdx, outIdx=0; i <= endIdx; i++, outIdx++ )
      {
         outReal[outIdx] = inReal0[i]*inReal1[i];
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int natrLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return -1;
      return optInTimePeriod + (this.unstablePeriod[FuncUnstId.Natr.ordinal()]) ;
   }
   public RetCode natr( int startIdx,
      int endIdx,
      double inHigh[],
      double inLow[],
      double inClose[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      RetCode retCode;
      int outIdx, today, lookbackTotal;
      int nbATR;
      MInteger outBegIdx1 = new MInteger() ;
      MInteger outNbElement1 = new MInteger() ;
      double prevATR, tempValue;
      double []tempBuffer ;
      double []prevATRTemp = new double[1] ;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      outBegIdx.value = 0 ;
      outNBElement.value = 0 ;
      lookbackTotal = natrLookback ( optInTimePeriod );
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
         return RetCode.Success ;
      if( optInTimePeriod <= 1 )
      {
         return trueRange ( startIdx, endIdx,
            inHigh, inLow, inClose,
            outBegIdx, outNBElement, outReal );
      }
      tempBuffer = new double[lookbackTotal+(endIdx-startIdx)+1] ;
      retCode = trueRange ( (startIdx-lookbackTotal+1), endIdx,
         inHigh, inLow, inClose,
         outBegIdx1 , outNbElement1 ,
         tempBuffer );
      if( retCode != RetCode.Success )
      {
         return retCode;
      }
      retCode = TA_INT_SMA ( optInTimePeriod-1,
         optInTimePeriod-1,
         tempBuffer, optInTimePeriod,
         outBegIdx1 , outNbElement1 ,
         prevATRTemp );
      if( retCode != RetCode.Success )
      {
         return retCode;
      }
      prevATR = prevATRTemp[0];
      today = optInTimePeriod;
      outIdx = (this.unstablePeriod[FuncUnstId.Natr.ordinal()]) ;
      while( outIdx != 0 )
      {
         prevATR *= optInTimePeriod - 1;
         prevATR += tempBuffer[today++];
         prevATR /= optInTimePeriod;
         outIdx--;
      }
      outIdx = 1;
      tempValue = inClose[today];
      if( ! (((-0.00000001)<tempValue)&&(tempValue<0.00000001)) )
         outReal[0] = (prevATR/tempValue)*100.0;
      else
         outReal[0] = 0.0;
      nbATR = (endIdx - startIdx)+1;
      while( --nbATR != 0 )
      {
         prevATR *= optInTimePeriod - 1;
         prevATR += tempBuffer[today++];
         prevATR /= optInTimePeriod;
         tempValue = inClose[today];
         if( ! (((-0.00000001)<tempValue)&&(tempValue<0.00000001)) )
            outReal[outIdx] = (prevATR/tempValue)*100.0;
         else
            outReal[0] = 0.0;
         outIdx++;
      }
      outBegIdx.value = startIdx;
      outNBElement.value = outIdx;
      return retCode;
   }
   
   /* Generated */
   public int obvLookback( )
   {
      return 0;
   }
   public RetCode obv( int startIdx,
      int endIdx,
      double inReal[],
      double inVolume[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int i;
      int outIdx;
      double prevReal, tempReal, prevOBV;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      prevOBV = inVolume[startIdx];
      prevReal = inReal[startIdx];
      outIdx = 0;
      for(i=startIdx; i <= endIdx; i++ )
      {
         tempReal = inReal[i];
         if( tempReal > prevReal )
            prevOBV += inVolume[i];
         else if( tempReal < prevReal )
            prevOBV -= inVolume[i];
         outReal[outIdx++] = prevOBV;
         prevReal = tempReal;
      }
      outBegIdx.value = startIdx;
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int plusDILookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return -1;
      if( optInTimePeriod > 1 )
         return optInTimePeriod + (this.unstablePeriod[FuncUnstId.PlusDI.ordinal()]) ;
      else
         return 1;
   }
   public RetCode plusDI( int startIdx,
      int endIdx,
      double inHigh[],
      double inLow[],
      double inClose[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int today, lookbackTotal, outIdx;
      double prevHigh, prevLow, prevClose;
      double prevPlusDM, prevTR;
      double tempReal, tempReal2, diffP, diffM;
      int i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      if( optInTimePeriod > 1 )
         lookbackTotal = optInTimePeriod + (this.unstablePeriod[FuncUnstId.PlusDI.ordinal()]) ;
      else
         lookbackTotal = 1;
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      if( optInTimePeriod <= 1 )
      {
         outBegIdx.value = startIdx;
         today = startIdx-1;
         prevHigh = inHigh[today];
         prevLow = inLow[today];
         prevClose = inClose[today];
         while( today < endIdx )
         {
            today++;
            tempReal = inHigh[today];
            diffP = tempReal-prevHigh;
            prevHigh = tempReal;
            tempReal = inLow[today];
            diffM = prevLow-tempReal;
            prevLow = tempReal;
            if( (diffP > 0) && (diffP > diffM) )
            {
               { tempReal = prevHigh-prevLow; tempReal2 = Math.abs (prevHigh-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; tempReal2 = Math.abs (prevLow-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; } ;
               if( (((-0.00000001)<tempReal)&&(tempReal<0.00000001)) )
                  outReal[outIdx++] = (double)0.0;
               else
                  outReal[outIdx++] = diffP/tempReal;
            }
            else
               outReal[outIdx++] = (double)0.0;
            prevClose = inClose[today];
         }
         outNBElement.value = outIdx;
         return RetCode.Success ;
      }
      outBegIdx.value = today = startIdx;
      prevPlusDM = 0.0;
      prevTR = 0.0;
      today = startIdx - lookbackTotal;
      prevHigh = inHigh[today];
      prevLow = inLow[today];
      prevClose = inClose[today];
      i = optInTimePeriod-1;
      while( i-- > 0 )
      {
         today++;
         tempReal = inHigh[today];
         diffP = tempReal-prevHigh;
         prevHigh = tempReal;
         tempReal = inLow[today];
         diffM = prevLow-tempReal;
         prevLow = tempReal;
         if( (diffP > 0) && (diffP > diffM) )
         {
            prevPlusDM += diffP;
         }
         { tempReal = prevHigh-prevLow; tempReal2 = Math.abs (prevHigh-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; tempReal2 = Math.abs (prevLow-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; } ;
         prevTR += tempReal;
         prevClose = inClose[today];
      }
      i = (this.unstablePeriod[FuncUnstId.PlusDI.ordinal()]) + 1;
      while( i-- != 0 )
      {
         today++;
         tempReal = inHigh[today];
         diffP = tempReal-prevHigh;
         prevHigh = tempReal;
         tempReal = inLow[today];
         diffM = prevLow-tempReal;
         prevLow = tempReal;
         if( (diffP > 0) && (diffP > diffM) )
         {
            prevPlusDM = prevPlusDM - (prevPlusDM/optInTimePeriod) + diffP;
         }
         else
         {
            prevPlusDM = prevPlusDM - (prevPlusDM/optInTimePeriod);
         }
         { tempReal = prevHigh-prevLow; tempReal2 = Math.abs (prevHigh-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; tempReal2 = Math.abs (prevLow-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; } ;
         prevTR = prevTR - (prevTR/optInTimePeriod) + tempReal;
         prevClose = inClose[today];
      }
      if( ! (((-0.00000001)<prevTR)&&(prevTR<0.00000001)) )
         outReal[0] = (100.0*(prevPlusDM/prevTR)) ;
      else
         outReal[0] = 0.0;
      outIdx = 1;
      while( today < endIdx )
      {
         today++;
         tempReal = inHigh[today];
         diffP = tempReal-prevHigh;
         prevHigh = tempReal;
         tempReal = inLow[today];
         diffM = prevLow-tempReal;
         prevLow = tempReal;
         if( (diffP > 0) && (diffP > diffM) )
         {
            prevPlusDM = prevPlusDM - (prevPlusDM/optInTimePeriod) + diffP;
         }
         else
         {
            prevPlusDM = prevPlusDM - (prevPlusDM/optInTimePeriod);
         }
         { tempReal = prevHigh-prevLow; tempReal2 = Math.abs (prevHigh-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; tempReal2 = Math.abs (prevLow-prevClose); if( tempReal2 > tempReal ) tempReal = tempReal2; } ;
         prevTR = prevTR - (prevTR/optInTimePeriod) + tempReal;
         prevClose = inClose[today];
         if( ! (((-0.00000001)<prevTR)&&(prevTR<0.00000001)) )
            outReal[outIdx++] = (100.0*(prevPlusDM/prevTR)) ;
         else
            outReal[outIdx++] = 0.0;
      }
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int plusDMLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return -1;
      if( optInTimePeriod > 1 )
         return optInTimePeriod + (this.unstablePeriod[FuncUnstId.PlusDM.ordinal()]) - 1;
      else
         return 1;
   }
   public RetCode plusDM( int startIdx,
      int endIdx,
      double inHigh[],
      double inLow[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int today, lookbackTotal, outIdx;
      double prevHigh, prevLow, tempReal;
      double prevPlusDM;
      double diffP, diffM;
      int i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      if( optInTimePeriod > 1 )
         lookbackTotal = optInTimePeriod + (this.unstablePeriod[FuncUnstId.PlusDM.ordinal()]) - 1;
      else
         lookbackTotal = 1;
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      if( optInTimePeriod <= 1 )
      {
         outBegIdx.value = startIdx;
         today = startIdx-1;
         prevHigh = inHigh[today];
         prevLow = inLow[today];
         while( today < endIdx )
         {
            today++;
            tempReal = inHigh[today];
            diffP = tempReal-prevHigh;
            prevHigh = tempReal;
            tempReal = inLow[today];
            diffM = prevLow-tempReal;
            prevLow = tempReal;
            if( (diffP > 0) && (diffP > diffM) )
            {
               outReal[outIdx++] = diffP;
            }
            else
               outReal[outIdx++] = 0;
         }
         outNBElement.value = outIdx;
         return RetCode.Success ;
      }
      outBegIdx.value = startIdx;
      prevPlusDM = 0.0;
      today = startIdx - lookbackTotal;
      prevHigh = inHigh[today];
      prevLow = inLow[today];
      i = optInTimePeriod-1;
      while( i-- > 0 )
      {
         today++;
         tempReal = inHigh[today];
         diffP = tempReal-prevHigh;
         prevHigh = tempReal;
         tempReal = inLow[today];
         diffM = prevLow-tempReal;
         prevLow = tempReal;
         if( (diffP > 0) && (diffP > diffM) )
         {
            prevPlusDM += diffP;
         }
      }
      i = (this.unstablePeriod[FuncUnstId.PlusDM.ordinal()]) ;
      while( i-- != 0 )
      {
         today++;
         tempReal = inHigh[today];
         diffP = tempReal-prevHigh;
         prevHigh = tempReal;
         tempReal = inLow[today];
         diffM = prevLow-tempReal;
         prevLow = tempReal;
         if( (diffP > 0) && (diffP > diffM) )
         {
            prevPlusDM = prevPlusDM - (prevPlusDM/optInTimePeriod) + diffP;
         }
         else
         {
            prevPlusDM = prevPlusDM - (prevPlusDM/optInTimePeriod);
         }
      }
      outReal[0] = prevPlusDM;
      outIdx = 1;
      while( today < endIdx )
      {
         today++;
         tempReal = inHigh[today];
         diffP = tempReal-prevHigh;
         prevHigh = tempReal;
         tempReal = inLow[today];
         diffM = prevLow-tempReal;
         prevLow = tempReal;
         if( (diffP > 0) && (diffP > diffM) )
         {
            prevPlusDM = prevPlusDM - (prevPlusDM/optInTimePeriod) + diffP;
         }
         else
         {
            prevPlusDM = prevPlusDM - (prevPlusDM/optInTimePeriod);
         }
         outReal[outIdx++] = prevPlusDM;
      }
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int ppoLookback( int optInFastPeriod,
      int optInSlowPeriod,
      MAType optInMAType )
   {
      if( (int)optInFastPeriod == ( Integer.MIN_VALUE ) )
         optInFastPeriod = 12;
      else if( ((int)optInFastPeriod < 2) || ((int)optInFastPeriod > 100000) )
         return -1;
      if( (int)optInSlowPeriod == ( Integer.MIN_VALUE ) )
         optInSlowPeriod = 26;
      else if( ((int)optInSlowPeriod < 2) || ((int)optInSlowPeriod > 100000) )
         return -1;
      return movingAverageLookback ( (((optInSlowPeriod) > (optInFastPeriod)) ? (optInSlowPeriod) : (optInFastPeriod)) , optInMAType );
   }
   public RetCode ppo( int startIdx,
      int endIdx,
      double inReal[],
      int optInFastPeriod,
      int optInSlowPeriod,
      MAType optInMAType,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      double []tempBuffer ;
      RetCode retCode;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInFastPeriod == ( Integer.MIN_VALUE ) )
         optInFastPeriod = 12;
      else if( ((int)optInFastPeriod < 2) || ((int)optInFastPeriod > 100000) )
         return RetCode.BadParam ;
      if( (int)optInSlowPeriod == ( Integer.MIN_VALUE ) )
         optInSlowPeriod = 26;
      else if( ((int)optInSlowPeriod < 2) || ((int)optInSlowPeriod > 100000) )
         return RetCode.BadParam ;
      tempBuffer = new double[endIdx-startIdx+1] ;
      retCode = TA_INT_PO ( startIdx, endIdx, inReal,
         optInFastPeriod,
         optInSlowPeriod,
         optInMAType,
         outBegIdx,
         outNBElement,
         outReal,
         tempBuffer,
         1 );
      return retCode;
   }
   
   /* Generated */
   public int rocLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 10;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return -1;
      return optInTimePeriod;
   }
   public RetCode roc( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int inIdx, outIdx, trailingIdx;
      double tempReal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 10;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      if( startIdx < optInTimePeriod )
         startIdx = optInTimePeriod;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      inIdx = startIdx;
      trailingIdx = startIdx - optInTimePeriod;
      while( inIdx <= endIdx )
      {
         tempReal = inReal[trailingIdx++];
         if( tempReal != 0.0 )
            outReal[outIdx++] = ((inReal[inIdx] / tempReal)-1.0)*100.0;
         else
            outReal[outIdx++] = 0.0;
         inIdx++;
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int rocPLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 10;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return -1;
      return optInTimePeriod;
   }
   public RetCode rocP( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int inIdx, outIdx, trailingIdx;
      double tempReal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 10;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      if( startIdx < optInTimePeriod )
         startIdx = optInTimePeriod;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      inIdx = startIdx;
      trailingIdx = startIdx - optInTimePeriod;
      while( inIdx <= endIdx )
      {
         tempReal = inReal[trailingIdx++];
         if( tempReal != 0.0 )
            outReal[outIdx++] = (inReal[inIdx]-tempReal)/tempReal;
         else
            outReal[outIdx++] = 0.0;
         inIdx++;
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int rocRLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 10;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return -1;
      return optInTimePeriod;
   }
   public RetCode rocR( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int inIdx, outIdx, trailingIdx;
      double tempReal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 10;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      if( startIdx < optInTimePeriod )
         startIdx = optInTimePeriod;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      inIdx = startIdx;
      trailingIdx = startIdx - optInTimePeriod;
      while( inIdx <= endIdx )
      {
         tempReal = inReal[trailingIdx++];
         if( tempReal != 0.0 )
            outReal[outIdx++] = (inReal[inIdx] / tempReal);
         else
            outReal[outIdx++] = 0.0;
         inIdx++;
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int rocR100Lookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 10;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return -1;
      return optInTimePeriod;
   }
   public RetCode rocR100( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int inIdx, outIdx, trailingIdx;
      double tempReal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 10;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      if( startIdx < optInTimePeriod )
         startIdx = optInTimePeriod;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      inIdx = startIdx;
      trailingIdx = startIdx - optInTimePeriod;
      while( inIdx <= endIdx )
      {
         tempReal = inReal[trailingIdx++];
         if( tempReal != 0.0 )
            outReal[outIdx++] = (inReal[inIdx] / tempReal)*100.0;
         else
            outReal[outIdx++] = 0.0;
         inIdx++;
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int rsiLookback( int optInTimePeriod )
   {
      int retValue;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      retValue = optInTimePeriod + (this.unstablePeriod[FuncUnstId.Rsi.ordinal()]) ;
      if( (this.compatibility) == Compatibility.Metastock )
         retValue--;
      return retValue;
   }
   public RetCode rsi( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx;
      int today, lookbackTotal, unstablePeriod, i;
      double prevGain, prevLoss, prevValue, savePrevValue;
      double tempValue1, tempValue2;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      outBegIdx.value = 0 ;
      outNBElement.value = 0 ;
      lookbackTotal = rsiLookback ( optInTimePeriod );
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
         return RetCode.Success ;
      outIdx = 0;
      if( optInTimePeriod == 1 )
      {
         outBegIdx.value = startIdx;
         i = (endIdx-startIdx)+1;
         outNBElement.value = i;
         System.arraycopy(inReal,startIdx,outReal,0,i) ;
         return RetCode.Success ;
      }
      today = startIdx-lookbackTotal;
      prevValue = inReal[today];
      unstablePeriod = (this.unstablePeriod[FuncUnstId.Rsi.ordinal()]) ;
      if( (unstablePeriod == 0) &&
         ( (this.compatibility) == Compatibility.Metastock ))
      {
         savePrevValue = prevValue;
         prevGain = 0.0;
         prevLoss = 0.0;
         for( i=optInTimePeriod; i > 0; i-- )
         {
            tempValue1 = inReal[today++];
            tempValue2 = tempValue1 - prevValue;
            prevValue = tempValue1;
            if( tempValue2 < 0 )
               prevLoss -= tempValue2;
            else
               prevGain += tempValue2;
         }
         tempValue1 = prevLoss/optInTimePeriod;
         tempValue2 = prevGain/optInTimePeriod;
         tempValue1 = tempValue2+tempValue1;
         if( ! (((-0.00000001)<tempValue1)&&(tempValue1<0.00000001)) )
            outReal[outIdx++] = 100*(tempValue2/tempValue1);
         else
            outReal[outIdx++] = 0.0;
         if( today > endIdx )
         {
            outBegIdx.value = startIdx;
            outNBElement.value = outIdx;
            return RetCode.Success ;
         }
         today -= optInTimePeriod;
         prevValue = savePrevValue;
      }
      prevGain = 0.0;
      prevLoss = 0.0;
      today++;
      for( i=optInTimePeriod; i > 0; i-- )
      {
         tempValue1 = inReal[today++];
         tempValue2 = tempValue1 - prevValue;
         prevValue = tempValue1;
         if( tempValue2 < 0 )
            prevLoss -= tempValue2;
         else
            prevGain += tempValue2;
      }
      prevLoss /= optInTimePeriod;
      prevGain /= optInTimePeriod;
      if( today > startIdx )
      {
         tempValue1 = prevGain+prevLoss;
         if( ! (((-0.00000001)<tempValue1)&&(tempValue1<0.00000001)) )
            outReal[outIdx++] = 100.0*(prevGain/tempValue1);
         else
            outReal[outIdx++] = 0.0;
      }
      else
      {
         while( today < startIdx )
         {
            tempValue1 = inReal[today];
            tempValue2 = tempValue1 - prevValue;
            prevValue = tempValue1;
            prevLoss *= (optInTimePeriod-1);
            prevGain *= (optInTimePeriod-1);
            if( tempValue2 < 0 )
               prevLoss -= tempValue2;
            else
               prevGain += tempValue2;
            prevLoss /= optInTimePeriod;
            prevGain /= optInTimePeriod;
            today++;
         }
      }
      while( today <= endIdx )
      {
         tempValue1 = inReal[today++];
         tempValue2 = tempValue1 - prevValue;
         prevValue = tempValue1;
         prevLoss *= (optInTimePeriod-1);
         prevGain *= (optInTimePeriod-1);
         if( tempValue2 < 0 )
            prevLoss -= tempValue2;
         else
            prevGain += tempValue2;
         prevLoss /= optInTimePeriod;
         prevGain /= optInTimePeriod;
         tempValue1 = prevGain+prevLoss;
         if( ! (((-0.00000001)<tempValue1)&&(tempValue1<0.00000001)) )
            outReal[outIdx++] = 100.0*(prevGain/tempValue1);
         else
            outReal[outIdx++] = 0.0;
      }
      outBegIdx.value = startIdx;
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int sarLookback( double optInAcceleration,
      double optInMaximum )
   {
      if( optInAcceleration == (-4e+37) )
         optInAcceleration = 2.000000e-2;
      else if( (optInAcceleration < 0.000000e+0) || (optInAcceleration > 3.000000e+37) )
         return -1;
      if( optInMaximum == (-4e+37) )
         optInMaximum = 2.000000e-1;
      else if( (optInMaximum < 0.000000e+0) || (optInMaximum > 3.000000e+37) )
         return -1;
      return 1;
   }
   public RetCode sar( int startIdx,
      int endIdx,
      double inHigh[],
      double inLow[],
      double optInAcceleration,
      double optInMaximum,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      RetCode retCode;
      int isLong;
      int todayIdx, outIdx;
      MInteger tempInt = new MInteger() ;
      double newHigh, newLow, prevHigh, prevLow;
      double af, ep, sar;
      double []ep_temp = new double[1] ;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( optInAcceleration == (-4e+37) )
         optInAcceleration = 2.000000e-2;
      else if( (optInAcceleration < 0.000000e+0) || (optInAcceleration > 3.000000e+37) )
         return RetCode.BadParam ;
      if( optInMaximum == (-4e+37) )
         optInMaximum = 2.000000e-1;
      else if( (optInMaximum < 0.000000e+0) || (optInMaximum > 3.000000e+37) )
         return RetCode.BadParam ;
      if( startIdx < 1 )
         startIdx = 1;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      af = optInAcceleration;
      if( af > optInMaximum )
         af = optInAcceleration = optInMaximum;
      retCode = minusDM ( startIdx, startIdx, inHigh, inLow, 1,
         tempInt , tempInt ,
         ep_temp );
      if( ep_temp[0] > 0 )
         isLong = 0;
      else
         isLong = 1;
      if( retCode != RetCode.Success )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return retCode;
      }
      outBegIdx.value = startIdx;
      outIdx = 0;
      todayIdx = startIdx;
      newHigh = inHigh[todayIdx-1];
      newLow = inLow[todayIdx-1];
      if( isLong == 1 )
      {
         ep = inHigh[todayIdx];
         sar = newLow;
      }
      else
      {
         ep = inLow[todayIdx];
         sar = newHigh;
      }
      newLow = inLow[todayIdx];
      newHigh = inHigh[todayIdx];
      while( todayIdx <= endIdx )
      {
         prevLow = newLow;
         prevHigh = newHigh;
         newLow = inLow[todayIdx];
         newHigh = inHigh[todayIdx];
         todayIdx++;
         if( isLong == 1 )
         {
            if( newLow <= sar )
            {
               isLong = 0;
               sar = ep;
               if( sar < prevHigh )
                  sar = prevHigh;
               if( sar < newHigh )
                  sar = newHigh;
               outReal[outIdx++] = sar;
               af = optInAcceleration;
               ep = newLow;
               sar = sar + af * (ep - sar);
               if( sar < prevHigh )
                  sar = prevHigh;
               if( sar < newHigh )
                  sar = newHigh;
            }
            else
            {
               outReal[outIdx++] = sar;
               if( newHigh > ep )
               {
                  ep = newHigh;
                  af += optInAcceleration;
                  if( af > optInMaximum )
                     af = optInMaximum;
               }
               sar = sar + af * (ep - sar);
               if( sar > prevLow )
                  sar = prevLow;
               if( sar > newLow )
                  sar = newLow;
            }
         }
         else
         {
            if( newHigh >= sar )
            {
               isLong = 1;
               sar = ep;
               if( sar > prevLow )
                  sar = prevLow;
               if( sar > newLow )
                  sar = newLow;
               outReal[outIdx++] = sar;
               af = optInAcceleration;
               ep = newHigh;
               sar = sar + af * (ep - sar);
               if( sar > prevLow )
                  sar = prevLow;
               if( sar > newLow )
                  sar = newLow;
            }
            else
            {
               outReal[outIdx++] = sar;
               if( newLow < ep )
               {
                  ep = newLow;
                  af += optInAcceleration;
                  if( af > optInMaximum )
                     af = optInMaximum;
               }
               sar = sar + af * (ep - sar);
               if( sar < prevHigh )
                  sar = prevHigh;
               if( sar < newHigh )
                  sar = newHigh;
            }
         }
      }
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int sarExtLookback( double optInStartValue,
      double optInOffsetOnReverse,
      double optInAccelerationInitLong,
      double optInAccelerationLong,
      double optInAccelerationMaxLong,
      double optInAccelerationInitShort,
      double optInAccelerationShort,
      double optInAccelerationMaxShort )
   {
      if( optInStartValue == (-4e+37) )
         optInStartValue = 0.000000e+0;
      else if( (optInStartValue < -3.000000e+37) || (optInStartValue > 3.000000e+37) )
         return -1;
      if( optInOffsetOnReverse == (-4e+37) )
         optInOffsetOnReverse = 0.000000e+0;
      else if( (optInOffsetOnReverse < 0.000000e+0) || (optInOffsetOnReverse > 3.000000e+37) )
         return -1;
      if( optInAccelerationInitLong == (-4e+37) )
         optInAccelerationInitLong = 2.000000e-2;
      else if( (optInAccelerationInitLong < 0.000000e+0) || (optInAccelerationInitLong > 3.000000e+37) )
         return -1;
      if( optInAccelerationLong == (-4e+37) )
         optInAccelerationLong = 2.000000e-2;
      else if( (optInAccelerationLong < 0.000000e+0) || (optInAccelerationLong > 3.000000e+37) )
         return -1;
      if( optInAccelerationMaxLong == (-4e+37) )
         optInAccelerationMaxLong = 2.000000e-1;
      else if( (optInAccelerationMaxLong < 0.000000e+0) || (optInAccelerationMaxLong > 3.000000e+37) )
         return -1;
      if( optInAccelerationInitShort == (-4e+37) )
         optInAccelerationInitShort = 2.000000e-2;
      else if( (optInAccelerationInitShort < 0.000000e+0) || (optInAccelerationInitShort > 3.000000e+37) )
         return -1;
      if( optInAccelerationShort == (-4e+37) )
         optInAccelerationShort = 2.000000e-2;
      else if( (optInAccelerationShort < 0.000000e+0) || (optInAccelerationShort > 3.000000e+37) )
         return -1;
      if( optInAccelerationMaxShort == (-4e+37) )
         optInAccelerationMaxShort = 2.000000e-1;
      else if( (optInAccelerationMaxShort < 0.000000e+0) || (optInAccelerationMaxShort > 3.000000e+37) )
         return -1;
      return 1;
   }
   public RetCode sarExt( int startIdx,
      int endIdx,
      double inHigh[],
      double inLow[],
      double optInStartValue,
      double optInOffsetOnReverse,
      double optInAccelerationInitLong,
      double optInAccelerationLong,
      double optInAccelerationMaxLong,
      double optInAccelerationInitShort,
      double optInAccelerationShort,
      double optInAccelerationMaxShort,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      RetCode retCode;
      int isLong;
      int todayIdx, outIdx;
      MInteger tempInt = new MInteger() ;
      double newHigh, newLow, prevHigh, prevLow;
      double afLong, afShort, ep, sar;
      double []ep_temp = new double[1] ;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( optInStartValue == (-4e+37) )
         optInStartValue = 0.000000e+0;
      else if( (optInStartValue < -3.000000e+37) || (optInStartValue > 3.000000e+37) )
         return RetCode.BadParam ;
      if( optInOffsetOnReverse == (-4e+37) )
         optInOffsetOnReverse = 0.000000e+0;
      else if( (optInOffsetOnReverse < 0.000000e+0) || (optInOffsetOnReverse > 3.000000e+37) )
         return RetCode.BadParam ;
      if( optInAccelerationInitLong == (-4e+37) )
         optInAccelerationInitLong = 2.000000e-2;
      else if( (optInAccelerationInitLong < 0.000000e+0) || (optInAccelerationInitLong > 3.000000e+37) )
         return RetCode.BadParam ;
      if( optInAccelerationLong == (-4e+37) )
         optInAccelerationLong = 2.000000e-2;
      else if( (optInAccelerationLong < 0.000000e+0) || (optInAccelerationLong > 3.000000e+37) )
         return RetCode.BadParam ;
      if( optInAccelerationMaxLong == (-4e+37) )
         optInAccelerationMaxLong = 2.000000e-1;
      else if( (optInAccelerationMaxLong < 0.000000e+0) || (optInAccelerationMaxLong > 3.000000e+37) )
         return RetCode.BadParam ;
      if( optInAccelerationInitShort == (-4e+37) )
         optInAccelerationInitShort = 2.000000e-2;
      else if( (optInAccelerationInitShort < 0.000000e+0) || (optInAccelerationInitShort > 3.000000e+37) )
         return RetCode.BadParam ;
      if( optInAccelerationShort == (-4e+37) )
         optInAccelerationShort = 2.000000e-2;
      else if( (optInAccelerationShort < 0.000000e+0) || (optInAccelerationShort > 3.000000e+37) )
         return RetCode.BadParam ;
      if( optInAccelerationMaxShort == (-4e+37) )
         optInAccelerationMaxShort = 2.000000e-1;
      else if( (optInAccelerationMaxShort < 0.000000e+0) || (optInAccelerationMaxShort > 3.000000e+37) )
         return RetCode.BadParam ;
      if( startIdx < 1 )
         startIdx = 1;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      afLong = optInAccelerationInitLong;
      afShort = optInAccelerationInitShort;
      if( afLong > optInAccelerationMaxLong )
         afLong = optInAccelerationInitLong = optInAccelerationMaxLong;
      if( optInAccelerationLong > optInAccelerationMaxLong )
         optInAccelerationLong = optInAccelerationMaxLong;
      if( afShort > optInAccelerationMaxShort)
         afShort = optInAccelerationInitShort = optInAccelerationMaxShort;
      if( optInAccelerationShort > optInAccelerationMaxShort )
         optInAccelerationShort = optInAccelerationMaxShort;
      if(optInStartValue == 0)
      {
         retCode = minusDM ( startIdx, startIdx, inHigh, inLow, 1,
            tempInt , tempInt ,
            ep_temp );
         if( ep_temp[0] > 0 )
            isLong = 0;
         else
            isLong = 1;
         if( retCode != RetCode.Success )
         {
            outBegIdx.value = 0 ;
            outNBElement.value = 0 ;
            return retCode;
         }
      }
      else if( optInStartValue > 0 )
      {
         isLong = 1;
      }
      else
      {
         isLong = 0;
      }
      outBegIdx.value = startIdx;
      outIdx = 0;
      todayIdx = startIdx;
      newHigh = inHigh[todayIdx-1];
      newLow = inLow[todayIdx-1];
      if(optInStartValue == 0)
      {
         if( isLong == 1 )
         {
            ep = inHigh[todayIdx];
            sar = newLow;
         }
         else
         {
            ep = inLow[todayIdx];
            sar = newHigh;
         }
      }
      else if ( optInStartValue > 0 )
      {
         ep = inHigh[todayIdx];
         sar = optInStartValue;
      }
      else
      {
         ep = inLow[todayIdx];
         sar = Math.abs (optInStartValue);
      }
      newLow = inLow[todayIdx];
      newHigh = inHigh[todayIdx];
      while( todayIdx <= endIdx )
      {
         prevLow = newLow;
         prevHigh = newHigh;
         newLow = inLow[todayIdx];
         newHigh = inHigh[todayIdx];
         todayIdx++;
         if( isLong == 1 )
         {
            if( newLow <= sar )
            {
               isLong = 0;
               sar = ep;
               if( sar < prevHigh )
                  sar = prevHigh;
               if( sar < newHigh )
                  sar = newHigh;
               if( optInOffsetOnReverse != 0.0 )
                  sar += sar * optInOffsetOnReverse;
               outReal[outIdx++] = -sar;
               afShort = optInAccelerationInitShort;
               ep = newLow;
               sar = sar + afShort * (ep - sar);
               if( sar < prevHigh )
                  sar = prevHigh;
               if( sar < newHigh )
                  sar = newHigh;
            }
            else
            {
               outReal[outIdx++] = sar;
               if( newHigh > ep )
               {
                  ep = newHigh;
                  afLong += optInAccelerationLong;
                  if( afLong > optInAccelerationMaxLong )
                     afLong = optInAccelerationMaxLong;
               }
               sar = sar + afLong * (ep - sar);
               if( sar > prevLow )
                  sar = prevLow;
               if( sar > newLow )
                  sar = newLow;
            }
         }
         else
         {
            if( newHigh >= sar )
            {
               isLong = 1;
               sar = ep;
               if( sar > prevLow )
                  sar = prevLow;
               if( sar > newLow )
                  sar = newLow;
               if( optInOffsetOnReverse != 0.0 )
                  sar -= sar * optInOffsetOnReverse;
               outReal[outIdx++] = sar;
               afLong = optInAccelerationInitLong;
               ep = newHigh;
               sar = sar + afLong * (ep - sar);
               if( sar > prevLow )
                  sar = prevLow;
               if( sar > newLow )
                  sar = newLow;
            }
            else
            {
               outReal[outIdx++] = -sar;
               if( newLow < ep )
               {
                  ep = newLow;
                  afShort += optInAccelerationShort;
                  if( afShort > optInAccelerationMaxShort )
                     afShort = optInAccelerationMaxShort;
               }
               sar = sar + afShort * (ep - sar);
               if( sar < prevHigh )
                  sar = prevHigh;
               if( sar < newHigh )
                  sar = newHigh;
            }
         }
      }
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int sinLookback( )
   {
      return 0;
   }
   public RetCode sin( int startIdx,
      int endIdx,
      double inReal[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx;
      int i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      for( i=startIdx, outIdx=0; i <= endIdx; i++, outIdx++ )
      {
         outReal[outIdx] = Math.sin (inReal[i]);
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int sinhLookback( )
   {
      return 0;
   }
   public RetCode sinh( int startIdx,
      int endIdx,
      double inReal[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx;
      int i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      for( i=startIdx, outIdx=0; i <= endIdx; i++, outIdx++ )
      {
         outReal[outIdx] = Math.sinh (inReal[i]);
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int smaLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      return optInTimePeriod - 1;
   }
   public RetCode sma( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      return TA_INT_SMA ( startIdx, endIdx,
         inReal, optInTimePeriod,
         outBegIdx, outNBElement, outReal );
   }
   RetCode TA_INT_SMA( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      double periodTotal, tempReal;
      int i, outIdx, trailingIdx, lookbackTotal;
      lookbackTotal = (optInTimePeriod-1);
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      periodTotal = 0;
      trailingIdx = startIdx-lookbackTotal;
      i=trailingIdx;
      if( optInTimePeriod > 1 )
      {
         while( i < startIdx )
            periodTotal += inReal[i++];
      }
      outIdx = 0;
      do
      {
         periodTotal += inReal[i++];
         tempReal = periodTotal;
         periodTotal -= inReal[trailingIdx++];
         outReal[outIdx++] = tempReal / optInTimePeriod;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int sqrtLookback( )
   {
      return 0;
   }
   public RetCode sqrt( int startIdx,
      int endIdx,
      double inReal[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx;
      int i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      for( i=startIdx, outIdx=0; i <= endIdx; i++, outIdx++ )
      {
         outReal[outIdx] = Math.sqrt (inReal[i]);
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int stdDevLookback( int optInTimePeriod,
      double optInNbDev )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 5;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      if( optInNbDev == (-4e+37) )
         optInNbDev = 1.000000e+0;
      else if( (optInNbDev < -3.000000e+37) || (optInNbDev > 3.000000e+37) )
         return -1;
      return varianceLookback ( optInTimePeriod, optInNbDev );
   }
   public RetCode stdDev( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      double optInNbDev,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int i;
      RetCode retCode;
      double tempReal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 5;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      if( optInNbDev == (-4e+37) )
         optInNbDev = 1.000000e+0;
      else if( (optInNbDev < -3.000000e+37) || (optInNbDev > 3.000000e+37) )
         return RetCode.BadParam ;
      retCode = TA_INT_VAR ( startIdx, endIdx,
         inReal, optInTimePeriod,
         outBegIdx, outNBElement, outReal );
      if( retCode != RetCode.Success )
         return retCode;
      if( optInNbDev != 1.0 )
      {
         for( i=0; i < (int) outNBElement.value ; i++ )
         {
            tempReal = outReal[i];
            if( ! (tempReal<0.00000001) )
               outReal[i] = Math.sqrt (tempReal) * optInNbDev;
            else
               outReal[i] = (double)0.0;
         }
      }
      else
      {
         for( i=0; i < (int) outNBElement.value ; i++ )
         {
            tempReal = outReal[i];
            if( ! (tempReal<0.00000001) )
               outReal[i] = Math.sqrt (tempReal);
            else
               outReal[i] = (double)0.0;
         }
      }
      return RetCode.Success ;
   }
   void TA_INT_stddev_using_precalc_ma( double inReal[],
      double inMovAvg[],
      int inMovAvgBegIdx,
      int inMovAvgNbElement,
      int timePeriod,
      double output[] )
   {
      double tempReal, periodTotal2, meanValue2;
      int outIdx;
      int startSum, endSum;
      startSum = 1+inMovAvgBegIdx-timePeriod;
      endSum = inMovAvgBegIdx;
      periodTotal2 = 0;
      for( outIdx = startSum; outIdx < endSum; outIdx++)
      {
         tempReal = inReal[outIdx];
         tempReal *= tempReal;
         periodTotal2 += tempReal;
      }
      for( outIdx=0; outIdx < inMovAvgNbElement; outIdx++, startSum++, endSum++ )
      {
         tempReal = inReal[endSum];
         tempReal *= tempReal;
         periodTotal2 += tempReal;
         meanValue2 = periodTotal2/timePeriod;
         tempReal = inReal[startSum];
         tempReal *= tempReal;
         periodTotal2 -= tempReal;
         tempReal = inMovAvg[outIdx];
         tempReal *= tempReal;
         meanValue2 -= tempReal;
         if( ! (meanValue2<0.00000001) )
            output[outIdx] = Math.sqrt (meanValue2);
         else
            output[outIdx] = (double)0.0;
      }
   }
   
   
   /* Generated */
   public int stochLookback( int optInFastK_Period,
      int optInSlowK_Period,
      MAType optInSlowK_MAType,
      int optInSlowD_Period,
      MAType optInSlowD_MAType )
   {
      int retValue;
      if( (int)optInFastK_Period == ( Integer.MIN_VALUE ) )
         optInFastK_Period = 5;
      else if( ((int)optInFastK_Period < 1) || ((int)optInFastK_Period > 100000) )
         return -1;
      if( (int)optInSlowK_Period == ( Integer.MIN_VALUE ) )
         optInSlowK_Period = 3;
      else if( ((int)optInSlowK_Period < 1) || ((int)optInSlowK_Period > 100000) )
         return -1;
      if( (int)optInSlowD_Period == ( Integer.MIN_VALUE ) )
         optInSlowD_Period = 3;
      else if( ((int)optInSlowD_Period < 1) || ((int)optInSlowD_Period > 100000) )
         return -1;
      retValue = (optInFastK_Period - 1);
      retValue += movingAverageLookback ( optInSlowK_Period, optInSlowK_MAType );
      retValue += movingAverageLookback ( optInSlowD_Period, optInSlowD_MAType );
      return retValue;
   }
   public RetCode stoch( int startIdx,
      int endIdx,
      double inHigh[],
      double inLow[],
      double inClose[],
      int optInFastK_Period,
      int optInSlowK_Period,
      MAType optInSlowK_MAType,
      int optInSlowD_Period,
      MAType optInSlowD_MAType,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outSlowK[],
      double outSlowD[] )
   {
      RetCode retCode;
      double lowest, highest, tmp, diff;
      double []tempBuffer ;
      int outIdx, lowestIdx, highestIdx;
      int lookbackTotal, lookbackK, lookbackKSlow, lookbackDSlow;
      int trailingIdx, today, i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInFastK_Period == ( Integer.MIN_VALUE ) )
         optInFastK_Period = 5;
      else if( ((int)optInFastK_Period < 1) || ((int)optInFastK_Period > 100000) )
         return RetCode.BadParam ;
      if( (int)optInSlowK_Period == ( Integer.MIN_VALUE ) )
         optInSlowK_Period = 3;
      else if( ((int)optInSlowK_Period < 1) || ((int)optInSlowK_Period > 100000) )
         return RetCode.BadParam ;
      if( (int)optInSlowD_Period == ( Integer.MIN_VALUE ) )
         optInSlowD_Period = 3;
      else if( ((int)optInSlowD_Period < 1) || ((int)optInSlowD_Period > 100000) )
         return RetCode.BadParam ;
      lookbackK = optInFastK_Period-1;
      lookbackKSlow = movingAverageLookback ( optInSlowK_Period, optInSlowK_MAType );
      lookbackDSlow = movingAverageLookback ( optInSlowD_Period, optInSlowD_MAType );
      lookbackTotal = lookbackK + lookbackDSlow + lookbackKSlow;
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      trailingIdx = startIdx-lookbackTotal;
      today = trailingIdx+lookbackK;
      lowestIdx = highestIdx = -1;
      diff = highest = lowest = 0.0;
      if( (outSlowK == inHigh) ||
         (outSlowK == inLow) ||
         (outSlowK == inClose) )
      {
         tempBuffer = outSlowK;
      }
      else if( (outSlowD == inHigh) ||
         (outSlowD == inLow) ||
         (outSlowD == inClose) )
      {
         tempBuffer = outSlowD;
      }
      else
      {
         tempBuffer = new double[endIdx-today+1] ;
      }
      while( today <= endIdx )
      {
         tmp = inLow[today];
         if( lowestIdx < trailingIdx )
         {
            lowestIdx = trailingIdx;
            lowest = inLow[lowestIdx];
            i = lowestIdx;
            while( ++i<=today )
            {
               tmp = inLow[i];
               if( tmp < lowest )
               {
                  lowestIdx = i;
                  lowest = tmp;
               }
            }
            diff = (highest - lowest)/100.0;
         }
         else if( tmp <= lowest )
         {
            lowestIdx = today;
            lowest = tmp;
            diff = (highest - lowest)/100.0;
         }
         tmp = inHigh[today];
         if( highestIdx < trailingIdx )
         {
            highestIdx = trailingIdx;
            highest = inHigh[highestIdx];
            i = highestIdx;
            while( ++i<=today )
            {
               tmp = inHigh[i];
               if( tmp > highest )
               {
                  highestIdx = i;
                  highest = tmp;
               }
            }
            diff = (highest - lowest)/100.0;
         }
         else if( tmp >= highest )
         {
            highestIdx = today;
            highest = tmp;
            diff = (highest - lowest)/100.0;
         }
         if( diff != 0.0 )
            tempBuffer[outIdx++] = (inClose[today]-lowest)/diff;
         else
            tempBuffer[outIdx++] = 0.0;
         trailingIdx++;
         today++;
      }
      retCode = movingAverage ( 0, outIdx-1,
         tempBuffer, optInSlowK_Period,
         optInSlowK_MAType,
         outBegIdx, outNBElement, tempBuffer );
      if( (retCode != RetCode.Success ) || ((int) outNBElement.value == 0) )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return retCode;
      }
      retCode = movingAverage ( 0, (int) outNBElement.value -1,
         tempBuffer, optInSlowD_Period,
         optInSlowD_MAType,
         outBegIdx, outNBElement, outSlowD );
      System.arraycopy(tempBuffer,lookbackDSlow,outSlowK,0,(int)outNBElement.value) ;
      if( retCode != RetCode.Success )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return retCode;
      }
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int stochFLookback( int optInFastK_Period,
      int optInFastD_Period,
      MAType optInFastD_MAType )
   {
      int retValue;
      if( (int)optInFastK_Period == ( Integer.MIN_VALUE ) )
         optInFastK_Period = 5;
      else if( ((int)optInFastK_Period < 1) || ((int)optInFastK_Period > 100000) )
         return -1;
      if( (int)optInFastD_Period == ( Integer.MIN_VALUE ) )
         optInFastD_Period = 3;
      else if( ((int)optInFastD_Period < 1) || ((int)optInFastD_Period > 100000) )
         return -1;
      retValue = (optInFastK_Period - 1);
      retValue += movingAverageLookback ( optInFastD_Period, optInFastD_MAType );
      return retValue;
   }
   public RetCode stochF( int startIdx,
      int endIdx,
      double inHigh[],
      double inLow[],
      double inClose[],
      int optInFastK_Period,
      int optInFastD_Period,
      MAType optInFastD_MAType,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outFastK[],
      double outFastD[] )
   {
      RetCode retCode;
      double lowest, highest, tmp, diff;
      double []tempBuffer ;
      int outIdx, lowestIdx, highestIdx;
      int lookbackTotal, lookbackK, lookbackFastD;
      int trailingIdx, today, i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInFastK_Period == ( Integer.MIN_VALUE ) )
         optInFastK_Period = 5;
      else if( ((int)optInFastK_Period < 1) || ((int)optInFastK_Period > 100000) )
         return RetCode.BadParam ;
      if( (int)optInFastD_Period == ( Integer.MIN_VALUE ) )
         optInFastD_Period = 3;
      else if( ((int)optInFastD_Period < 1) || ((int)optInFastD_Period > 100000) )
         return RetCode.BadParam ;
      lookbackK = optInFastK_Period-1;
      lookbackFastD = movingAverageLookback ( optInFastD_Period, optInFastD_MAType );
      lookbackTotal = lookbackK + lookbackFastD;
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      trailingIdx = startIdx-lookbackTotal;
      today = trailingIdx+lookbackK;
      lowestIdx = highestIdx = -1;
      diff = highest = lowest = 0.0;
      if( (outFastK == inHigh) ||
         (outFastK == inLow) ||
         (outFastK == inClose) )
      {
         tempBuffer = outFastK;
      }
      else if( (outFastD == inHigh) ||
         (outFastD == inLow) ||
         (outFastD == inClose) )
      {
         tempBuffer = outFastD;
      }
      else
      {
         tempBuffer = new double[endIdx-today+1] ;
      }
      while( today <= endIdx )
      {
         tmp = inLow[today];
         if( lowestIdx < trailingIdx )
         {
            lowestIdx = trailingIdx;
            lowest = inLow[lowestIdx];
            i = lowestIdx;
            while( ++i<=today )
            {
               tmp = inLow[i];
               if( tmp < lowest )
               {
                  lowestIdx = i;
                  lowest = tmp;
               }
            }
            diff = (highest - lowest)/100.0;
         }
         else if( tmp <= lowest )
         {
            lowestIdx = today;
            lowest = tmp;
            diff = (highest - lowest)/100.0;
         }
         tmp = inHigh[today];
         if( highestIdx < trailingIdx )
         {
            highestIdx = trailingIdx;
            highest = inHigh[highestIdx];
            i = highestIdx;
            while( ++i<=today )
            {
               tmp = inHigh[i];
               if( tmp > highest )
               {
                  highestIdx = i;
                  highest = tmp;
               }
            }
            diff = (highest - lowest)/100.0;
         }
         else if( tmp >= highest )
         {
            highestIdx = today;
            highest = tmp;
            diff = (highest - lowest)/100.0;
         }
         if( diff != 0.0 )
            tempBuffer[outIdx++] = (inClose[today]-lowest)/diff;
         else
            tempBuffer[outIdx++] = 0.0;
         trailingIdx++;
         today++;
      }
      retCode = movingAverage ( 0, outIdx-1,
         tempBuffer, optInFastD_Period,
         optInFastD_MAType,
         outBegIdx, outNBElement, outFastD );
      if( (retCode != RetCode.Success ) || ((int) outNBElement.value ) == 0 )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return retCode;
      }
      System.arraycopy(tempBuffer,lookbackFastD,outFastK,0,(int)outNBElement.value) ;
      if( retCode != RetCode.Success )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return retCode;
      }
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int stochRsiLookback( int optInTimePeriod,
      int optInFastK_Period,
      int optInFastD_Period,
      MAType optInFastD_MAType )
   {
      int retValue;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      if( (int)optInFastK_Period == ( Integer.MIN_VALUE ) )
         optInFastK_Period = 5;
      else if( ((int)optInFastK_Period < 1) || ((int)optInFastK_Period > 100000) )
         return -1;
      if( (int)optInFastD_Period == ( Integer.MIN_VALUE ) )
         optInFastD_Period = 3;
      else if( ((int)optInFastD_Period < 1) || ((int)optInFastD_Period > 100000) )
         return -1;
      retValue = rsiLookback ( optInTimePeriod ) + stochFLookback ( optInFastK_Period, optInFastD_Period, optInFastD_MAType );
      return retValue;
   }
   public RetCode stochRsi( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      int optInFastK_Period,
      int optInFastD_Period,
      MAType optInFastD_MAType,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outFastK[],
      double outFastD[] )
   {
      double []tempRSIBuffer ;
      RetCode retCode;
      int lookbackTotal, lookbackSTOCHF, tempArraySize;
      MInteger outBegIdx1 = new MInteger() ;
      MInteger outBegIdx2 = new MInteger() ;
      MInteger outNbElement1 = new MInteger() ;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      if( (int)optInFastK_Period == ( Integer.MIN_VALUE ) )
         optInFastK_Period = 5;
      else if( ((int)optInFastK_Period < 1) || ((int)optInFastK_Period > 100000) )
         return RetCode.BadParam ;
      if( (int)optInFastD_Period == ( Integer.MIN_VALUE ) )
         optInFastD_Period = 3;
      else if( ((int)optInFastD_Period < 1) || ((int)optInFastD_Period > 100000) )
         return RetCode.BadParam ;
      outBegIdx.value = 0 ;
      outNBElement.value = 0 ;
      lookbackSTOCHF = stochFLookback ( optInFastK_Period, optInFastD_Period, optInFastD_MAType );
      lookbackTotal = rsiLookback ( optInTimePeriod ) + lookbackSTOCHF;
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outBegIdx.value = startIdx;
      tempArraySize = (endIdx - startIdx) + 1 + lookbackSTOCHF;
      tempRSIBuffer = new double[tempArraySize] ;
      retCode = rsi (startIdx-lookbackSTOCHF,
         endIdx,
         inReal,
         optInTimePeriod,
         outBegIdx1 ,
         outNbElement1 ,
         tempRSIBuffer);
      if( retCode != RetCode.Success || outNbElement1.value == 0 )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return retCode;
      }
      retCode = stochF (0,
         tempArraySize-1,
         tempRSIBuffer,
         tempRSIBuffer,
         tempRSIBuffer,
         optInFastK_Period,
         optInFastD_Period,
         optInFastD_MAType,
         outBegIdx2 ,
         outNBElement,
         outFastK,
         outFastD);
      if( retCode != RetCode.Success || ((int) outNBElement.value ) == 0 )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return retCode;
      }
      return RetCode.Success ;
   }
   
   /* Generated */
   public int subLookback( )
   {
      return 0;
   }
   public RetCode sub( int startIdx,
      int endIdx,
      double inReal0[],
      double inReal1[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx;
      int i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      for( i=startIdx, outIdx=0; i <= endIdx; i++, outIdx++ )
      {
         outReal[outIdx] = inReal0[i]-inReal1[i];
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int sumLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      return optInTimePeriod-1;
   }
   public RetCode sum( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      double periodTotal, tempReal;
      int i, outIdx, trailingIdx, lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      lookbackTotal = (optInTimePeriod-1);
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      periodTotal = 0;
      trailingIdx = startIdx-lookbackTotal;
      i=trailingIdx;
      if( optInTimePeriod > 1 )
      {
         while( i < startIdx )
            periodTotal += inReal[i++];
      }
      outIdx = 0;
      do
      {
         periodTotal += inReal[i++];
         tempReal = periodTotal;
         periodTotal -= inReal[trailingIdx++];
         outReal[outIdx++] = tempReal;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int t3Lookback( int optInTimePeriod,
      double optInVFactor )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 5;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      if( optInVFactor == (-4e+37) )
         optInVFactor = 7.000000e-1;
      else if( (optInVFactor < 0.000000e+0) || (optInVFactor > 1.000000e+0) )
         return -1;
      return 6 * (optInTimePeriod-1) + (this.unstablePeriod[FuncUnstId.T3.ordinal()]) ;
   }
   public RetCode t3( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      double optInVFactor,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx, lookbackTotal;
      int today, i;
      double k, one_minus_k;
      double e1, e2, e3, e4, e5, e6;
      double c1, c2, c3, c4;
      double tempReal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 5;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      if( optInVFactor == (-4e+37) )
         optInVFactor = 7.000000e-1;
      else if( (optInVFactor < 0.000000e+0) || (optInVFactor > 1.000000e+0) )
         return RetCode.BadParam ;
      lookbackTotal = 6 * (optInTimePeriod - 1) + (this.unstablePeriod[FuncUnstId.T3.ordinal()]) ;
      if( startIdx <= lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outNBElement.value = 0 ;
         outBegIdx.value = 0 ;
         return RetCode.Success ;
      }
      outBegIdx.value = startIdx;
      today = startIdx - lookbackTotal;
      k = 2.0/(optInTimePeriod+1.0);
      one_minus_k = 1.0-k;
      tempReal = inReal[today++];
      for( i=optInTimePeriod-1; i > 0 ; i-- )
         tempReal += inReal[today++];
      e1 = tempReal / optInTimePeriod;
      tempReal = e1;
      for( i=optInTimePeriod-1; i > 0 ; i-- )
      {
         e1 = (k*inReal[today++])+(one_minus_k*e1);
         tempReal += e1;
      }
      e2 = tempReal / optInTimePeriod;
      tempReal = e2;
      for( i=optInTimePeriod-1; i > 0 ; i-- )
      {
         e1 = (k*inReal[today++])+(one_minus_k*e1);
         e2 = (k*e1)+(one_minus_k*e2);
         tempReal += e2;
      }
      e3 = tempReal / optInTimePeriod;
      tempReal = e3;
      for( i=optInTimePeriod-1; i > 0 ; i-- )
      {
         e1 = (k*inReal[today++])+(one_minus_k*e1);
         e2 = (k*e1)+(one_minus_k*e2);
         e3 = (k*e2)+(one_minus_k*e3);
         tempReal += e3;
      }
      e4 = tempReal / optInTimePeriod;
      tempReal = e4;
      for( i=optInTimePeriod-1; i > 0 ; i-- )
      {
         e1 = (k*inReal[today++])+(one_minus_k*e1);
         e2 = (k*e1)+(one_minus_k*e2);
         e3 = (k*e2)+(one_minus_k*e3);
         e4 = (k*e3)+(one_minus_k*e4);
         tempReal += e4;
      }
      e5 = tempReal / optInTimePeriod;
      tempReal = e5;
      for( i=optInTimePeriod-1; i > 0 ; i-- )
      {
         e1 = (k*inReal[today++])+(one_minus_k*e1);
         e2 = (k*e1)+(one_minus_k*e2);
         e3 = (k*e2)+(one_minus_k*e3);
         e4 = (k*e3)+(one_minus_k*e4);
         e5 = (k*e4)+(one_minus_k*e5);
         tempReal += e5;
      }
      e6 = tempReal / optInTimePeriod;
      while( today <= startIdx )
      {
         e1 = (k*inReal[today++])+(one_minus_k*e1);
         e2 = (k*e1)+(one_minus_k*e2);
         e3 = (k*e2)+(one_minus_k*e3);
         e4 = (k*e3)+(one_minus_k*e4);
         e5 = (k*e4)+(one_minus_k*e5);
         e6 = (k*e5)+(one_minus_k*e6);
      }
      tempReal = optInVFactor * optInVFactor;
      c1 = -(tempReal * optInVFactor);
      c2 = 3.0 * (tempReal - c1);
      c3 = -6.0 * tempReal - 3.0 * (optInVFactor-c1);
      c4 = 1.0 + 3.0 * optInVFactor - c1 + 3.0 * tempReal;
      outIdx = 0;
      outReal[outIdx++] = c1*e6+c2*e5+c3*e4+c4*e3;
      while( today <= endIdx )
      {
         e1 = (k*inReal[today++])+(one_minus_k*e1);
         e2 = (k*e1)+(one_minus_k*e2);
         e3 = (k*e2)+(one_minus_k*e3);
         e4 = (k*e3)+(one_minus_k*e4);
         e5 = (k*e4)+(one_minus_k*e5);
         e6 = (k*e5)+(one_minus_k*e6);
         outReal[outIdx++] = c1*e6+c2*e5+c3*e4+c4*e3;
      }
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int tanLookback( )
   {
      return 0;
   }
   public RetCode tan( int startIdx,
      int endIdx,
      double inReal[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx;
      int i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      for( i=startIdx, outIdx=0; i <= endIdx; i++, outIdx++ )
      {
         outReal[outIdx] = Math.tan (inReal[i]);
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int tanhLookback( )
   {
      return 0;
   }
   public RetCode tanh( int startIdx,
      int endIdx,
      double inReal[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx;
      int i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      for( i=startIdx, outIdx=0; i <= endIdx; i++, outIdx++ )
      {
         outReal[outIdx] = Math.tanh (inReal[i]);
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int temaLookback( int optInTimePeriod )
   {
      int retValue;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      retValue = emaLookback ( optInTimePeriod );
      return retValue * 3;
   }
   public RetCode tema( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      double []firstEMA ;
      double []secondEMA ;
      double k;
      MInteger firstEMABegIdx = new MInteger() ;
      MInteger firstEMANbElement = new MInteger() ;
      MInteger secondEMABegIdx = new MInteger() ;
      MInteger secondEMANbElement = new MInteger() ;
      MInteger thirdEMABegIdx = new MInteger() ;
      MInteger thirdEMANbElement = new MInteger() ;
      int tempInt, outIdx, lookbackTotal, lookbackEMA;
      int firstEMAIdx, secondEMAIdx;
      RetCode retCode;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      outNBElement.value = 0 ;
      outBegIdx.value = 0 ;
      lookbackEMA = emaLookback ( optInTimePeriod );
      lookbackTotal = lookbackEMA * 3;
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
         return RetCode.Success ;
      tempInt = lookbackTotal+(endIdx-startIdx)+1;
      firstEMA = new double[tempInt] ;
      k = ((double)2.0 / ((double)(optInTimePeriod + 1))) ;
      retCode = TA_INT_EMA ( startIdx-(lookbackEMA*2), endIdx, inReal,
         optInTimePeriod, k,
         firstEMABegIdx , firstEMANbElement ,
         firstEMA );
      if( (retCode != RetCode.Success ) || ( firstEMANbElement.value == 0) )
      {
         return retCode;
      }
      secondEMA = new double[firstEMANbElement.value] ;
      retCode = TA_INT_EMA ( 0, firstEMANbElement.value -1, firstEMA,
         optInTimePeriod, k,
         secondEMABegIdx , secondEMANbElement ,
         secondEMA );
      if( (retCode != RetCode.Success ) || ( secondEMANbElement.value == 0) )
      {
         return retCode;
      }
      retCode = TA_INT_EMA ( 0, secondEMANbElement.value -1, secondEMA,
         optInTimePeriod, k,
         thirdEMABegIdx , thirdEMANbElement ,
         outReal );
      if( (retCode != RetCode.Success ) || ( thirdEMANbElement.value == 0) )
      {
         return retCode;
      }
      firstEMAIdx = thirdEMABegIdx.value + secondEMABegIdx.value ;
      secondEMAIdx = thirdEMABegIdx.value ;
      outBegIdx.value = firstEMAIdx + firstEMABegIdx.value ;
      outIdx = 0;
      while( outIdx < thirdEMANbElement.value )
      {
         outReal[outIdx] += (3.0*firstEMA[firstEMAIdx++]) - (3.0*secondEMA[secondEMAIdx++]);
         outIdx++;
      }
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int trueRangeLookback( )
   {
      return 1;
   }
   public RetCode trueRange( int startIdx,
      int endIdx,
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int today, outIdx;
      double val2, val3, greatest;
      double tempCY, tempLT, tempHT;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( startIdx < 1 )
         startIdx = 1;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      today = startIdx;
      while( today <= endIdx )
      {
         tempLT = inLow[today];
         tempHT = inHigh[today];
         tempCY = inClose[today-1];
         greatest = tempHT - tempLT;
         val2 = Math.abs ( tempCY - tempHT );
         if( val2 > greatest )
            greatest = val2;
         val3 = Math.abs ( tempCY - tempLT );
         if( val3 > greatest )
            greatest = val3;
         outReal[outIdx++] = greatest;
         today++;
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int trimaLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      return optInTimePeriod-1;
   }
   public RetCode trima( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int lookbackTotal;
      double numerator;
      double numeratorSub;
      double numeratorAdd;
      int i, outIdx, todayIdx, trailingIdx, middleIdx;
      double factor, tempReal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      lookbackTotal = (optInTimePeriod-1);
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      if( (optInTimePeriod % 2) == 1 )
      {
         i = (optInTimePeriod>>1);
         factor = (i+1)*(i+1);
         factor = 1.0/factor;
         trailingIdx = startIdx-lookbackTotal;
         middleIdx = trailingIdx + i;
         todayIdx = middleIdx + i;
         numerator = 0.0;
         numeratorSub = 0.0;
         for( i=middleIdx; i >= trailingIdx; i-- )
         {
            tempReal = inReal[i];
            numeratorSub += tempReal;
            numerator += numeratorSub;
         }
         numeratorAdd = 0.0;
         middleIdx++;
         for( i=middleIdx; i <= todayIdx; i++ )
         {
            tempReal = inReal[i];
            numeratorAdd += tempReal;
            numerator += numeratorAdd;
         }
         outIdx = 0;
         tempReal = inReal[trailingIdx++];
         outReal[outIdx++] = numerator * factor;
         todayIdx++;
         while( todayIdx <= endIdx )
         {
            numerator -= numeratorSub;
            numeratorSub -= tempReal;
            tempReal = inReal[middleIdx++];
            numeratorSub += tempReal;
            numerator += numeratorAdd;
            numeratorAdd -= tempReal;
            tempReal = inReal[todayIdx++];
            numeratorAdd += tempReal;
            numerator += tempReal;
            tempReal = inReal[trailingIdx++];
            outReal[outIdx++] = numerator * factor;
         }
      }
      else
      {
         i = (optInTimePeriod>>1);
         factor = i*(i+1);
         factor = 1.0/factor;
         trailingIdx = startIdx-lookbackTotal;
         middleIdx = trailingIdx + i - 1;
         todayIdx = middleIdx + i;
         numerator = 0.0;
         numeratorSub = 0.0;
         for( i=middleIdx; i >= trailingIdx; i-- )
         {
            tempReal = inReal[i];
            numeratorSub += tempReal;
            numerator += numeratorSub;
         }
         numeratorAdd = 0.0;
         middleIdx++;
         for( i=middleIdx; i <= todayIdx; i++ )
         {
            tempReal = inReal[i];
            numeratorAdd += tempReal;
            numerator += numeratorAdd;
         }
         outIdx = 0;
         tempReal = inReal[trailingIdx++];
         outReal[outIdx++] = numerator * factor;
         todayIdx++;
         while( todayIdx <= endIdx )
         {
            numerator -= numeratorSub;
            numeratorSub -= tempReal;
            tempReal = inReal[middleIdx++];
            numeratorSub += tempReal;
            numeratorAdd -= tempReal;
            numerator += numeratorAdd;
            tempReal = inReal[todayIdx++];
            numeratorAdd += tempReal;
            numerator += tempReal;
            tempReal = inReal[trailingIdx++];
            outReal[outIdx++] = numerator * factor;
         }
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int trixLookback( int optInTimePeriod )
   {
      int emaLookback;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return -1;
      emaLookback = emaLookback ( optInTimePeriod );
      return (emaLookback*3) + rocRLookback ( 1 );
   }
   public RetCode trix( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      double k;
      double []tempBuffer ;
      MInteger nbElement = new MInteger() ;
      MInteger begIdx = new MInteger() ;
      int totalLookback;
      int emaLookback, rocLookback;
      RetCode retCode;
      int nbElementToOutput;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      emaLookback = emaLookback ( optInTimePeriod );
      rocLookback = rocRLookback ( 1 );
      totalLookback = (emaLookback*3) + rocLookback;
      if( startIdx < totalLookback )
         startIdx = totalLookback;
      if( startIdx > endIdx )
      {
         outNBElement.value = 0 ;
         outBegIdx.value = 0 ;
         return RetCode.Success ;
      }
      outBegIdx.value = startIdx;
      nbElementToOutput = (endIdx-startIdx)+1+totalLookback;
      tempBuffer = new double[nbElementToOutput] ;
      k = ((double)2.0 / ((double)(optInTimePeriod + 1))) ;
      retCode = TA_INT_EMA ( (startIdx-totalLookback), endIdx, inReal,
         optInTimePeriod, k,
         begIdx , nbElement ,
         tempBuffer );
      if( (retCode != RetCode.Success ) || ( nbElement.value == 0) )
      {
         outNBElement.value = 0 ;
         outBegIdx.value = 0 ;
         return retCode;
      }
      nbElementToOutput--;
      nbElementToOutput -= emaLookback;
      retCode = TA_INT_EMA ( 0, nbElementToOutput, tempBuffer,
         optInTimePeriod, k,
         begIdx , nbElement ,
         tempBuffer );
      if( (retCode != RetCode.Success ) || ( nbElement.value == 0) )
      {
         outNBElement.value = 0 ;
         outBegIdx.value = 0 ;
         return retCode;
      }
      nbElementToOutput -= emaLookback;
      retCode = TA_INT_EMA ( 0, nbElementToOutput, tempBuffer,
         optInTimePeriod, k,
         begIdx , nbElement ,
         tempBuffer );
      if( (retCode != RetCode.Success ) || ( nbElement.value == 0) )
      {
         outNBElement.value = 0 ;
         outBegIdx.value = 0 ;
         return retCode;
      }
      nbElementToOutput -= emaLookback;
      retCode = roc ( 0, nbElementToOutput,
         tempBuffer,
         1, begIdx , outNBElement,
         outReal );
      if( (retCode != RetCode.Success ) || ((int) outNBElement.value == 0) )
      {
         outNBElement.value = 0 ;
         outBegIdx.value = 0 ;
         return retCode;
      }
      return RetCode.Success ;
   }
   
   /* Generated */
   public int tsfLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      return optInTimePeriod-1;
   }
   public RetCode tsf( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx;
      int today, lookbackTotal;
      double SumX, SumXY, SumY, SumXSqr, Divisor;
      double m, b;
      int i;
      double tempValue1;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      lookbackTotal = tsfLookback ( optInTimePeriod );
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      outIdx = 0;
      today = startIdx;
      SumX = optInTimePeriod * ( optInTimePeriod - 1 ) * 0.5;
      SumXSqr = optInTimePeriod * ( optInTimePeriod - 1 ) * ( 2 * optInTimePeriod - 1 ) / 6;
      Divisor = SumX * SumX - optInTimePeriod * SumXSqr;
      while( today <= endIdx )
      {
         SumXY = 0;
         SumY = 0;
         for( i = optInTimePeriod; i-- != 0; )
         {
            SumY += tempValue1 = inReal[today - i];
            SumXY += (double)i * tempValue1;
         }
         m = ( optInTimePeriod * SumXY - SumX * SumY) / Divisor;
         b = ( SumY - m * SumX ) / (double)optInTimePeriod;
         outReal[outIdx++] = b + m * (double)optInTimePeriod;
         today++;
      }
      outBegIdx.value = startIdx;
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int typPriceLookback( )
   {
      return 0;
   }
   public RetCode typPrice( int startIdx,
      int endIdx,
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx, i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      outIdx = 0;
      for( i= startIdx; i <= endIdx; i++ )
      {
         outReal[outIdx++] = ( inHigh [i] +
            inLow [i] +
            inClose[i] ) / 3.0;
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int ultOscLookback( int optInTimePeriod1,
      int optInTimePeriod2,
      int optInTimePeriod3 )
   {
      int maxPeriod;
      if( (int)optInTimePeriod1 == ( Integer.MIN_VALUE ) )
         optInTimePeriod1 = 7;
      else if( ((int)optInTimePeriod1 < 1) || ((int)optInTimePeriod1 > 100000) )
         return -1;
      if( (int)optInTimePeriod2 == ( Integer.MIN_VALUE ) )
         optInTimePeriod2 = 14;
      else if( ((int)optInTimePeriod2 < 1) || ((int)optInTimePeriod2 > 100000) )
         return -1;
      if( (int)optInTimePeriod3 == ( Integer.MIN_VALUE ) )
         optInTimePeriod3 = 28;
      else if( ((int)optInTimePeriod3 < 1) || ((int)optInTimePeriod3 > 100000) )
         return -1;
      maxPeriod = ((( (((optInTimePeriod1) > (optInTimePeriod2)) ? (optInTimePeriod1) : (optInTimePeriod2)) ) > (optInTimePeriod3)) ? ( (((optInTimePeriod1) > (optInTimePeriod2)) ? (optInTimePeriod1) : (optInTimePeriod2)) ) : (optInTimePeriod3)) ;
      return smaLookback ( maxPeriod ) + 1;
   }
   public RetCode ultOsc( int startIdx,
      int endIdx,
      double inHigh[],
      double inLow[],
      double inClose[],
      int optInTimePeriod1,
      int optInTimePeriod2,
      int optInTimePeriod3,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      double a1Total, a2Total, a3Total;
      double b1Total, b2Total, b3Total;
      double trueLow, trueRange, closeMinusTrueLow;
      double tempDouble, output, tempHT, tempLT, tempCY;
      int lookbackTotal;
      int longestPeriod, longestIndex;
      int i,j,today,outIdx;
      int trailingIdx1, trailingIdx2, trailingIdx3;
      int []usedFlag = new int[3] ;
      int []periods = new int[3] ;
      int []sortedPeriods = new int[3] ;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod1 == ( Integer.MIN_VALUE ) )
         optInTimePeriod1 = 7;
      else if( ((int)optInTimePeriod1 < 1) || ((int)optInTimePeriod1 > 100000) )
         return RetCode.BadParam ;
      if( (int)optInTimePeriod2 == ( Integer.MIN_VALUE ) )
         optInTimePeriod2 = 14;
      else if( ((int)optInTimePeriod2 < 1) || ((int)optInTimePeriod2 > 100000) )
         return RetCode.BadParam ;
      if( (int)optInTimePeriod3 == ( Integer.MIN_VALUE ) )
         optInTimePeriod3 = 28;
      else if( ((int)optInTimePeriod3 < 1) || ((int)optInTimePeriod3 > 100000) )
         return RetCode.BadParam ;
      outBegIdx.value = 0 ;
      outNBElement.value = 0 ;
      periods[0] = optInTimePeriod1;
      periods[1] = optInTimePeriod2;
      periods[2] = optInTimePeriod3;
      usedFlag[0] = 0;
      usedFlag[1] = 0;
      usedFlag[2] = 0;
      for ( i = 0; i < 3; ++i )
      {
         longestPeriod = 0;
         longestIndex = 0;
         for ( j = 0; j < 3; ++j )
         {
            if ( (usedFlag[j] == 0) && (periods[j] > longestPeriod) )
            {
               longestPeriod = periods[j];
               longestIndex = j;
            }
         }
         usedFlag[longestIndex] = 1;
         sortedPeriods[i] = longestPeriod;
      }
      optInTimePeriod1 = sortedPeriods[2];
      optInTimePeriod2 = sortedPeriods[1];
      optInTimePeriod3 = sortedPeriods[0];
      lookbackTotal = ultOscLookback ( optInTimePeriod1, optInTimePeriod2, optInTimePeriod3 );
      if( startIdx < lookbackTotal ) startIdx = lookbackTotal;
      if( startIdx > endIdx ) return RetCode.Success ;
      { a1Total = 0; b1Total = 0; for ( i = startIdx-optInTimePeriod1+1; i < startIdx; ++i ) { { tempLT = inLow[i]; tempHT = inHigh[i]; tempCY = inClose[i-1]; trueLow = (((tempLT) < (tempCY)) ? (tempLT) : (tempCY)) ; closeMinusTrueLow = inClose[i] - trueLow; trueRange = tempHT - tempLT; tempDouble = Math.abs ( tempCY - tempHT ); if( tempDouble > trueRange ) trueRange = tempDouble; tempDouble = Math.abs ( tempCY - tempLT ); if( tempDouble > trueRange ) trueRange = tempDouble; } ; a1Total += closeMinusTrueLow; b1Total += trueRange; } } ;
      { a2Total = 0; b2Total = 0; for ( i = startIdx-optInTimePeriod2+1; i < startIdx; ++i ) { { tempLT = inLow[i]; tempHT = inHigh[i]; tempCY = inClose[i-1]; trueLow = (((tempLT) < (tempCY)) ? (tempLT) : (tempCY)) ; closeMinusTrueLow = inClose[i] - trueLow; trueRange = tempHT - tempLT; tempDouble = Math.abs ( tempCY - tempHT ); if( tempDouble > trueRange ) trueRange = tempDouble; tempDouble = Math.abs ( tempCY - tempLT ); if( tempDouble > trueRange ) trueRange = tempDouble; } ; a2Total += closeMinusTrueLow; b2Total += trueRange; } } ;
      { a3Total = 0; b3Total = 0; for ( i = startIdx-optInTimePeriod3+1; i < startIdx; ++i ) { { tempLT = inLow[i]; tempHT = inHigh[i]; tempCY = inClose[i-1]; trueLow = (((tempLT) < (tempCY)) ? (tempLT) : (tempCY)) ; closeMinusTrueLow = inClose[i] - trueLow; trueRange = tempHT - tempLT; tempDouble = Math.abs ( tempCY - tempHT ); if( tempDouble > trueRange ) trueRange = tempDouble; tempDouble = Math.abs ( tempCY - tempLT ); if( tempDouble > trueRange ) trueRange = tempDouble; } ; a3Total += closeMinusTrueLow; b3Total += trueRange; } } ;
      today = startIdx;
      outIdx = 0;
      trailingIdx1 = today - optInTimePeriod1 + 1;
      trailingIdx2 = today - optInTimePeriod2 + 1;
      trailingIdx3 = today - optInTimePeriod3 + 1;
      while( today <= endIdx )
      {
         { tempLT = inLow[today]; tempHT = inHigh[today]; tempCY = inClose[today-1]; trueLow = (((tempLT) < (tempCY)) ? (tempLT) : (tempCY)) ; closeMinusTrueLow = inClose[today] - trueLow; trueRange = tempHT - tempLT; tempDouble = Math.abs ( tempCY - tempHT ); if( tempDouble > trueRange ) trueRange = tempDouble; tempDouble = Math.abs ( tempCY - tempLT ); if( tempDouble > trueRange ) trueRange = tempDouble; } ;
         a1Total += closeMinusTrueLow;
         a2Total += closeMinusTrueLow;
         a3Total += closeMinusTrueLow;
         b1Total += trueRange;
         b2Total += trueRange;
         b3Total += trueRange;
         output = 0.0;
         if( ! (((-0.00000001)<b1Total)&&(b1Total<0.00000001)) ) output += 4.0*(a1Total/b1Total);
         if( ! (((-0.00000001)<b2Total)&&(b2Total<0.00000001)) ) output += 2.0*(a2Total/b2Total);
         if( ! (((-0.00000001)<b3Total)&&(b3Total<0.00000001)) ) output += a3Total/b3Total;
         { tempLT = inLow[trailingIdx1]; tempHT = inHigh[trailingIdx1]; tempCY = inClose[trailingIdx1-1]; trueLow = (((tempLT) < (tempCY)) ? (tempLT) : (tempCY)) ; closeMinusTrueLow = inClose[trailingIdx1] - trueLow; trueRange = tempHT - tempLT; tempDouble = Math.abs ( tempCY - tempHT ); if( tempDouble > trueRange ) trueRange = tempDouble; tempDouble = Math.abs ( tempCY - tempLT ); if( tempDouble > trueRange ) trueRange = tempDouble; } ;
         a1Total -= closeMinusTrueLow;
         b1Total -= trueRange;
         { tempLT = inLow[trailingIdx2]; tempHT = inHigh[trailingIdx2]; tempCY = inClose[trailingIdx2-1]; trueLow = (((tempLT) < (tempCY)) ? (tempLT) : (tempCY)) ; closeMinusTrueLow = inClose[trailingIdx2] - trueLow; trueRange = tempHT - tempLT; tempDouble = Math.abs ( tempCY - tempHT ); if( tempDouble > trueRange ) trueRange = tempDouble; tempDouble = Math.abs ( tempCY - tempLT ); if( tempDouble > trueRange ) trueRange = tempDouble; } ;
         a2Total -= closeMinusTrueLow;
         b2Total -= trueRange;
         { tempLT = inLow[trailingIdx3]; tempHT = inHigh[trailingIdx3]; tempCY = inClose[trailingIdx3-1]; trueLow = (((tempLT) < (tempCY)) ? (tempLT) : (tempCY)) ; closeMinusTrueLow = inClose[trailingIdx3] - trueLow; trueRange = tempHT - tempLT; tempDouble = Math.abs ( tempCY - tempHT ); if( tempDouble > trueRange ) trueRange = tempDouble; tempDouble = Math.abs ( tempCY - tempLT ); if( tempDouble > trueRange ) trueRange = tempDouble; } ;
         a3Total -= closeMinusTrueLow;
         b3Total -= trueRange;
         outReal[outIdx] = 100.0 * (output / 7.0);
         outIdx++;
         today++;
         trailingIdx1++;
         trailingIdx2++;
         trailingIdx3++;
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int varianceLookback( int optInTimePeriod,
      double optInNbDev )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 5;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return -1;
      if( optInNbDev == (-4e+37) )
         optInNbDev = 1.000000e+0;
      else if( (optInNbDev < -3.000000e+37) || (optInNbDev > 3.000000e+37) )
         return -1;
      return optInTimePeriod-1;
   }
   public RetCode variance( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      double optInNbDev,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 5;
      else if( ((int)optInTimePeriod < 1) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      if( optInNbDev == (-4e+37) )
         optInNbDev = 1.000000e+0;
      else if( (optInNbDev < -3.000000e+37) || (optInNbDev > 3.000000e+37) )
         return RetCode.BadParam ;
      return TA_INT_VAR ( startIdx, endIdx, inReal,
         optInTimePeriod,
         outBegIdx, outNBElement, outReal );
   }
   public RetCode TA_INT_VAR( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      double tempReal, periodTotal1, periodTotal2, meanValue1, meanValue2;
      int i, outIdx, trailingIdx, nbInitialElementNeeded;
      nbInitialElementNeeded = (optInTimePeriod-1);
      if( startIdx < nbInitialElementNeeded )
         startIdx = nbInitialElementNeeded;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      periodTotal1 = 0;
      periodTotal2 = 0;
      trailingIdx = startIdx-nbInitialElementNeeded;
      i=trailingIdx;
      if( optInTimePeriod > 1 )
      {
         while( i < startIdx ) {
            tempReal = inReal[i++];
            periodTotal1 += tempReal;
            tempReal *= tempReal;
            periodTotal2 += tempReal;
         }
      }
      outIdx = 0;
      do
      {
         tempReal = inReal[i++];
         periodTotal1 += tempReal;
         tempReal *= tempReal;
         periodTotal2 += tempReal;
         meanValue1 = periodTotal1 / optInTimePeriod;
         meanValue2 = periodTotal2 / optInTimePeriod;
         tempReal = inReal[trailingIdx++];
         periodTotal1 -= tempReal;
         tempReal *= tempReal;
         periodTotal2 -= tempReal;
         outReal[outIdx++] = meanValue2-meanValue1*meanValue1;
      } while( i <= endIdx );
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   
   /* Generated */
   public int wclPriceLookback( )
   {
      return 0;
   }
   public RetCode wclPrice( int startIdx,
      int endIdx,
      double inHigh[],
      double inLow[],
      double inClose[],
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int outIdx, i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      outIdx = 0;
      for( i= startIdx; i <= endIdx; i++ )
      {
         outReal[outIdx++] = ( inHigh [i] +
            inLow [i] +
            (inClose[i]*2.0) ) / 4.0;
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int willRLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      return (optInTimePeriod-1);
   }
   public RetCode willR( int startIdx,
      int endIdx,
      double inHigh[],
      double inLow[],
      double inClose[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      double lowest, highest, tmp, diff;
      int outIdx, nbInitialElementNeeded;
      int trailingIdx, lowestIdx, highestIdx;
      int today, i;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 14;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      nbInitialElementNeeded = (optInTimePeriod-1);
      if( startIdx < nbInitialElementNeeded )
         startIdx = nbInitialElementNeeded;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      diff = 0.0;
      outIdx = 0;
      today = startIdx;
      trailingIdx = startIdx-nbInitialElementNeeded;
      lowestIdx = highestIdx = -1;
      diff = highest = lowest = 0.0;
      while( today <= endIdx )
      {
         tmp = inLow[today];
         if( lowestIdx < trailingIdx )
         {
            lowestIdx = trailingIdx;
            lowest = inLow[lowestIdx];
            i = lowestIdx;
            while( ++i<=today )
            {
               tmp = inLow[i];
               if( tmp < lowest )
               {
                  lowestIdx = i;
                  lowest = tmp;
               }
            }
            diff = (highest - lowest)/(-100.0);
         }
         else if( tmp <= lowest )
         {
            lowestIdx = today;
            lowest = tmp;
            diff = (highest - lowest)/(-100.0);
         }
         tmp = inHigh[today];
         if( highestIdx < trailingIdx )
         {
            highestIdx = trailingIdx;
            highest = inHigh[highestIdx];
            i = highestIdx;
            while( ++i<=today )
            {
               tmp = inHigh[i];
               if( tmp > highest )
               {
                  highestIdx = i;
                  highest = tmp;
               }
            }
            diff = (highest - lowest)/(-100.0);
         }
         else if( tmp >= highest )
         {
            highestIdx = today;
            highest = tmp;
            diff = (highest - lowest)/(-100.0);
         }
         if( diff != 0.0 )
            outReal[outIdx++] = (highest-inClose[today])/diff;
         else
            outReal[outIdx++] = 0.0;
         trailingIdx++;
         today++;
      }
      outBegIdx.value = startIdx;
      outNBElement.value = outIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   public int wmaLookback( int optInTimePeriod )
   {
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return -1;
      return optInTimePeriod - 1;
   }
   public RetCode wma( int startIdx,
      int endIdx,
      double inReal[],
      int optInTimePeriod,
      MInteger outBegIdx,
      MInteger outNBElement,
      double outReal[] )
   {
      int inIdx, outIdx, i, trailingIdx, divider;
      double periodSum, periodSub, tempReal, trailingValue;
      int lookbackTotal;
      if( startIdx < 0 )
         return RetCode.OutOfRangeStartIndex ;
      if( (endIdx < 0) || (endIdx < startIdx))
         return RetCode.OutOfRangeEndIndex ;
      if( (int)optInTimePeriod == ( Integer.MIN_VALUE ) )
         optInTimePeriod = 30;
      else if( ((int)optInTimePeriod < 2) || ((int)optInTimePeriod > 100000) )
         return RetCode.BadParam ;
      lookbackTotal = optInTimePeriod-1;
      if( startIdx < lookbackTotal )
         startIdx = lookbackTotal;
      if( startIdx > endIdx )
      {
         outBegIdx.value = 0 ;
         outNBElement.value = 0 ;
         return RetCode.Success ;
      }
      if( optInTimePeriod == 1 )
      {
         outBegIdx.value = startIdx;
         outNBElement.value = endIdx-startIdx+1;
         System.arraycopy(inReal,startIdx,outReal,0,(int)outNBElement.value) ;
         return RetCode.Success ;
      }
      divider = (optInTimePeriod*(optInTimePeriod+1))>>1;
      outIdx = 0;
      trailingIdx = startIdx - lookbackTotal;
      periodSum = periodSub = (double)0.0;
      inIdx=trailingIdx;
      i = 1;
      while( inIdx < startIdx )
      {
         tempReal = inReal[inIdx++];
         periodSub += tempReal;
         periodSum += tempReal*i;
         i++;
      }
      trailingValue = 0.0;
      while( inIdx <= endIdx )
      {
         tempReal = inReal[inIdx++];
         periodSub += tempReal;
         periodSub -= trailingValue;
         periodSum += tempReal*optInTimePeriod;
         trailingValue = inReal[trailingIdx++];
         outReal[outIdx++] = periodSum / divider;
         periodSum -= periodSub;
      }
      outNBElement.value = outIdx;
      outBegIdx.value = startIdx;
      return RetCode.Success ;
   }
   
   /* Generated */
   /**** END GENCODE SECTION 1 - DO NOT DELETE THIS LINE ****/
   
}
