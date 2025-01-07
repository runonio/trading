package io.runon.trading.evaluation;

import io.runon.commons.data.BigDecimalArray;
import io.runon.commons.utils.time.Times;
import io.runon.trading.BigDecimals;
import io.runon.trading.TimeNumber;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * 성장률 평가
 *
 * @author macle
 */
public class GrowthRateEvaluation {

    /**
     * percent, mdd, year avg percent,min, max;
     *
     */
    public static BigDecimalArray evaluation(TimeNumber[] timeNumbers, ZoneId zoneId){

        BigDecimal mdd = BigDecimal.ZERO;


        BigDecimal first = timeNumbers[0].getNumber();

        int lastYear = Times.yearInt(timeNumbers[0].getTime(), zoneId);
        List<BigDecimal> yearPercentList = new ArrayList<>();

        BigDecimal min = first;
        BigDecimal max = first;

        BigDecimal lastMax = first;
        BigDecimal lastMin = first;

        BigDecimal lastNumber = first;
        BigDecimal yearFirst = first;
        for (int i = 1; i < timeNumbers.length; i++) {
            TimeNumber timeNumber = timeNumbers[i];

            int year = Times.yearInt(timeNumbers[i].getTime(), zoneId);

            if(year > lastYear){

                lastYear = year;
                yearPercentList.add(lastNumber.subtract(yearFirst).divide(yearFirst,6,RoundingMode.HALF_UP).multiply(BigDecimals.DECIMAL_100).stripTrailingZeros());
                yearFirst = timeNumber.getNumber();
            }

            BigDecimal number = timeNumber.getNumber();
            if(number.compareTo(min) < 0){
                min = number;
            }
            if(number.compareTo(max) > 0){
                max = number;
            }

            if(number.compareTo(lastMax) > 0){
                lastMax = number;
                lastMin = number;
            }else if(number.compareTo(lastMin) < 0){
                lastMin = number;
                BigDecimal lastMdd = lastMax.subtract(lastMin).divide(lastMax, MathContext.DECIMAL128);
                mdd = mdd.max(lastMdd);
            }

            lastNumber = number;
        }

        yearPercentList.add(lastNumber.subtract(yearFirst).divide(yearFirst,6,RoundingMode.HALF_UP).multiply(BigDecimals.DECIMAL_100).stripTrailingZeros());
        BigDecimal yearPercentSum = BigDecimal.ZERO;

        for(BigDecimal yearPercent : yearPercentList){
            yearPercentSum = yearPercentSum.add(yearPercent);
        }


        BigDecimal percent = timeNumbers[timeNumbers.length - 1].getNumber().subtract(first).divide(first, 6, RoundingMode.HALF_UP).multiply(BigDecimals.DECIMAL_100).stripTrailingZeros();
        BigDecimal yearAvg = yearPercentSum.divide(new BigDecimal(yearPercentList.size()), 4, RoundingMode.HALF_UP).stripTrailingZeros();
        mdd = mdd.multiply(BigDecimals.DECIMAL_100).setScale(4, RoundingMode.HALF_UP).stripTrailingZeros();
        return new BigDecimalArray(percent, mdd, yearAvg, max, min, timeNumbers[timeNumbers.length-1].getNumber() );
    }


}
