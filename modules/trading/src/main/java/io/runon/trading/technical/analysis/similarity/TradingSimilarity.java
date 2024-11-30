package io.runon.trading.technical.analysis.similarity;

import com.seomse.commons.data.BigDecimalArray;
import io.runon.trading.BigDecimals;
import io.runon.trading.TimeChangePercent;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * @author macle
 */
public class TradingSimilarity {


    public static BigDecimalArray changeSimilarity(TimeChangePercent[] sourceArray, TimeChangePercent[] targetArray){
        return changeSimilarity(sourceArray, targetArray, null);
    }
    /**
     * 변화율을 활용한 유사도
     */
    public static BigDecimalArray changeSimilarity(TimeChangePercent[] sourceArray, TimeChangePercent[] targetArray, BigDecimal inPercentGap){

        int size = 0;
        int inCount = 0;

        BigDecimal sourceChangeSum = BigDecimal.ZERO;

        BigDecimal targetChangeSum = BigDecimal.ZERO;


        int lastTargetIndex = 0;
        for (TimeChangePercent source : sourceArray) {
            long sourceTime = source.getTime();


            for (int targetIndex = lastTargetIndex+1; targetIndex <targetArray.length ; targetIndex++) {
                TimeChangePercent target = targetArray[targetIndex];

                if(target.getTime() == sourceTime){

                    sourceChangeSum = sourceChangeSum.add(source.getChangePercent());
                    targetChangeSum = targetChangeSum.add(target.getChangePercent());

                    size ++;

                    boolean isIn = false;

                    if(source.getChangePercent().compareTo(BigDecimal.ZERO) > 0){
                        if(target.getChangePercent().compareTo(BigDecimal.ZERO) >= 0){
                            isIn = true;
                            inCount ++;
                        }
                    }else if(source.getChangePercent().compareTo(BigDecimal.ZERO) == 0){
                        if(target.getChangePercent().compareTo(BigDecimal.ZERO) == 0){
                            isIn = true;
                            inCount ++;
                        }
                    }else{
                        if(target.getChangePercent().compareTo(BigDecimal.ZERO) <= 0){
                            isIn = true;
                            inCount ++;
                        }
                    }

                    if(!isIn && inPercentGap != null && inPercentGap.compareTo(BigDecimal.ZERO) > 0){
                        BigDecimal gap = source.getChangePercent().subtract(target.getChangePercent());
                        gap = gap.abs();
                        if(gap.compareTo(inPercentGap) <= 0){
                            inCount ++ ;
                        }
                    }

                    lastTargetIndex = targetIndex;
                    break;
                }
            }
        }

        BigDecimal similarity;
        if(size == 0){
            similarity = BigDecimal.ZERO;
        }else{
            similarity = new BigDecimal(inCount).divide(new BigDecimal(size), MathContext.DECIMAL128).multiply(BigDecimals.DECIMAL_100).setScale(4, RoundingMode.HALF_UP).stripTrailingZeros();
        }

        return new BigDecimalArray(similarity, sourceChangeSum.setScale(4, RoundingMode.HALF_UP).stripTrailingZeros(), targetChangeSum.setScale(4, RoundingMode.HALF_UP).stripTrailingZeros()
            , new BigDecimal(inCount), new BigDecimal(size)
        );
    }


    public static SimilaritySearchData search(TimeChangeGet source, TimeChangeGet[] targets, BigDecimal inPercentGap, int minSize, BigDecimal minSimPercent){

        String sourceId = source.getId();

        TimeChangePercent [] sourceArray = source.getChangeArray();

        for(TimeChangeGet target : targets){
            String targetId = target.getId();
            if(sourceId.equals(targetId)){
                continue;
            }

            TimeChangePercent [] targetArray = target.getChangeArray();


        }




        return null;
    }

}
