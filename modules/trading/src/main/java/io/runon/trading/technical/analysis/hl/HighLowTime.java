package io.runon.trading.technical.analysis.hl;


import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;

import java.math.BigDecimal;
/**
 * @author macle
 */
public class HighLowTime implements HighLow {


    //고가 저가를 찾기 시작한 기준 시간
    protected long time;

    protected BigDecimal high;
    protected int highIndex;
    protected long highTime;

    protected BigDecimal low;
    protected int lowIndex;
    protected long lowTime;


    protected BigDecimal height = null;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public int getHighIndex() {
        return highIndex;
    }

    public void setHighIndex(int highIndex) {
        this.highIndex = highIndex;
    }

    public long getHighTime() {
        return highTime;
    }

    public void setHighTime(long highTime) {
        this.highTime = highTime;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public int getLowIndex() {
        return lowIndex;
    }

    public void setLowIndex(int lowIndex) {
        this.lowIndex = lowIndex;
    }

    public long getLowTime() {
        return lowTime;
    }

    public void setLowTime(long lowTime) {
        this.lowTime = lowTime;
    }


    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getHeight() {
        if(height == null){
            height = high.subtract(low);
        }

        return height;
    }

    public static TimeNumber[] getHighs(HighLowTime[] array){
        TimeNumber [] timeNumbers = new TimeNumber[array.length];
        for (int i = 0; i <timeNumbers.length ; i++) {
            HighLowTime data = array[i];
            timeNumbers[i] = new TimeNumberData(data.time, data.high);
        }

        return timeNumbers;
    }

    public static TimeNumber[] getLows(HighLowTime[] array){
        TimeNumber [] timeNumbers = new TimeNumber[array.length];
        for (int i = 0; i <timeNumbers.length ; i++) {
            HighLowTime data = array[i];
            timeNumbers[i] = new TimeNumberData(data.time, data.low);
        }

        return timeNumbers;
    }
}
