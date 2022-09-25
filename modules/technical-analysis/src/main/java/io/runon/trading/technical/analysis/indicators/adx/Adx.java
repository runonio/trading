
package io.runon.trading.technical.analysis.indicators.adx;

import io.runon.trading.BigDecimals;
import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.technical.analysis.candle.CandleStick;
import io.runon.trading.technical.analysis.indicators.ma.Ema;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 *  추세 강도지표
 *  ADX (Average Directional Movement Index)
 *  DMI 정보를 활용한다.
 *
 *  docs.hsrp.kr/undefined/untitled-5/adx
 *
 * @author macle
 */
public class Adx {

    public static final AdxData [] NULL_DATA = new AdxData[0];
    private int defaultN = 14;

    public void setDefaultN(int defaultN) {
        this.defaultN = defaultN;
    }

    private int scale = 4;
    public void setScale(int scale) {
        this.scale = scale;
    }

    public AdxData [] getArray(CandleStick[] array, int resultLength) {
        return getArray(array, defaultN,array.length - resultLength, array.length);
    }

    public AdxData [] getArray(CandleStick[] array, int n, int resultLength) {
        return getArray(array,n,array.length - resultLength, array.length);
    }

    public AdxData [] getArray(CandleStick[] array, int n, int startIndex, int end) {

        if(startIndex < 1){
            startIndex = 1;

        }
        if( end > array.length){
            end = array.length;
        }

        int resultLength = end - startIndex;

        if(resultLength < 1){
            return NULL_DATA;
        }

        BigDecimal [] pdm = new BigDecimal[resultLength];
        BigDecimal [] mdm = new BigDecimal[resultLength];
        BigDecimal [] cl = new BigDecimal[resultLength];
        BigDecimal [] tr = new BigDecimal[resultLength];
        for (int i = 0; i <resultLength ; i++) {
            int index = i + startIndex;

            CandleStick candle = array[index];
            CandleStick previous = array[index-1];

            pdm[i] = candle.getHigh().subtract(previous.getHigh()).abs();
            mdm[i] = candle.getLow().subtract(previous.getLow()).abs();
            cl[i] = candle.getClose().subtract(previous.getClose()).abs();
            tr[i] = pdm[i].max(mdm[i]).max(cl[i]);
        }

        BigDecimal [] pdmn = Ema.getArray(pdm,n,pdm.length);
        BigDecimal [] mdmn = Ema.getArray(mdm,n,mdm.length);
        BigDecimal [] trn = Ema.getArray(tr,n,tr.length);

        BigDecimal [] pdi = new BigDecimal[resultLength];
        BigDecimal [] mdi = new BigDecimal[resultLength];

        BigDecimal [] dx = new BigDecimal[resultLength];
        for (int i = 0; i <resultLength ; i++) {
            if(trn[i].compareTo(BigDecimal.ZERO) == 0){
                pdi[i] = BigDecimal.ZERO;
                mdi[i] = BigDecimal.ZERO;
                dx[i] =  BigDecimal.ZERO;
                continue;
            }

            pdi[i] = pdmn[i].divide(trn[i],MathContext.DECIMAL128);
            mdi[i] = mdmn[i].divide(trn[i],MathContext.DECIMAL128);

            BigDecimal sum = mdi[i].add(pdi[i]);
            if(sum.compareTo(BigDecimal.ZERO) == 0){
                dx[i] =  BigDecimal.ZERO;
                continue;
            }
            dx[i] = pdi[i].subtract(mdi[i]).abs().multiply(BigDecimals.DECIMAL_100).divide(sum, MathContext.DECIMAL128);
        }

        BigDecimal [] adx  = Ema.getArray(dx,n,dx.length);

        AdxData [] dataArray = new AdxData[resultLength];
        for (int i = 0; i <resultLength ; i++) {
            int index = i + startIndex;

            AdxData data = new AdxData();
            data.time = array[index].getTime();
            data.pdi = pdi[i].multiply(BigDecimals.DECIMAL_100).setScale(scale, RoundingMode.HALF_UP);
            data.mdi = mdi[i].multiply(BigDecimals.DECIMAL_100).setScale(scale, RoundingMode.HALF_UP);
            data.dx = dx[i].setScale(scale, RoundingMode.HALF_UP);
            data.adx = adx[i].setScale(scale, RoundingMode.HALF_UP);
            dataArray[i] = data;
        }

        return dataArray;
    }


    public static TimeNumber [] getPdiArray(AdxData [] array){
        TimeNumber [] timeNumbers = new TimeNumber[array.length];
        for (int i = 0; i <timeNumbers.length ; i++) {
            AdxData data = array[i];
            timeNumbers[i] = new TimeNumberData(data.time, data.pdi);
        }
        return timeNumbers;
    }
    public static TimeNumber [] getMdiArray(AdxData [] array){
        TimeNumber [] timeNumbers = new TimeNumber[array.length];
        for (int i = 0; i <timeNumbers.length ; i++) {
            AdxData data = array[i];
            timeNumbers[i] = new TimeNumberData(data.time, data.mdi);
        }
        return timeNumbers;
    }

    public static TimeNumber [] getDxArray(AdxData [] array){
        TimeNumber [] timeNumbers = new TimeNumber[array.length];
        for (int i = 0; i <timeNumbers.length ; i++) {
            AdxData data = array[i];
            timeNumbers[i] = new TimeNumberData(data.time, data.dx);
        }
        return timeNumbers;
    }
}
