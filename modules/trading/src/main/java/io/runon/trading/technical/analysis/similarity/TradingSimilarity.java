package io.runon.trading.technical.analysis.similarity;

import io.runon.commons.data.BigDecimalArray;
import io.runon.commons.data.IdArray;
import io.runon.trading.BigDecimals;
import io.runon.trading.TimeChangePercent;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author macle
 */
public class TradingSimilarity {

    public final static Comparator<IdArray<BigDecimalArray>> SORT_SEARCH_DESC = (o1, o2) -> o2.getArray().get(1).compareTo(o1.getArray().get(1));


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

        BigDecimal score = targetChangeSum.subtract(sourceChangeSum);


        return new BigDecimalArray(similarity ,score.setScale(4,RoundingMode.HALF_UP).stripTrailingZeros(), sourceChangeSum.setScale(4, RoundingMode.HALF_UP).stripTrailingZeros(), targetChangeSum.setScale(4, RoundingMode.HALF_UP).stripTrailingZeros()
            , new BigDecimal(inCount), new BigDecimal(size)
        );
    }

    public static IdArray<BigDecimalArray> [] search(TimeChangeGet source, TimeChangeGet[] targets, BigDecimal inPercentGap, int minSize, BigDecimal minSimPercent){

        String sourceId = source.getId();

        TimeChangePercent [] sourceArray = source.getChangeArray();

        List<IdArray<BigDecimalArray>> list = new ArrayList<>();

        for(TimeChangeGet target : targets){
            String targetId = target.getId();
            if(sourceId.equals(targetId)){
                continue;
            }

            TimeChangePercent [] targetArray = target.getChangeArray();

            if(targetArray == null || targetArray.length ==0){
                continue;
            }

            BigDecimalArray simData = changeSimilarity(sourceArray, targetArray, inPercentGap);

            int size = simData.get(5).intValue();

            if(size < minSize){
                continue;
            }

            BigDecimal simPercent = simData.get(0);
            if(simPercent.compareTo(minSimPercent) <0){
                continue;
            }

            IdArray<BigDecimalArray> data = new IdArray<>(target.getId(), simData);
            list.add(data);

        }

        @SuppressWarnings("unchecked")
        IdArray<BigDecimalArray> [] array = list.toArray(new IdArray[0]);
        Arrays.sort(array,SORT_SEARCH_DESC);
        return array;
    }


    public static void main(String[] args) {

    }

}
