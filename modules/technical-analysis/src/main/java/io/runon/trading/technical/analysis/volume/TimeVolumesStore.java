package io.runon.trading.technical.analysis.volume;

import io.runon.trading.*;
import io.runon.trading.technical.analysis.indicators.Disparity;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

/**
 * 결합캔들 전환 포인트
 * @author macle
 */
public class TimeVolumesStore {

    private final List<TimeVolumes> list = new LinkedList<>();

    private Map<Long, List<TimePrice>> movingAverageMap;

    private long [] movingAverageTimes = null;

    public void setMovingAverageTimes(long[] movingAverageTimes) {
        Arrays.sort(movingAverageTimes);
        this.movingAverageTimes = movingAverageTimes;

        if(movingAverageMap == null){
            movingAverageMap = new HashMap<>();
        }else{
            movingAverageMap.clear();
        }

        for (long time : movingAverageTimes){
            movingAverageMap.put(time, new LinkedList<>());
        }

    }
    public List<TimePrice> getMovingAverage(long time){
        return movingAverageMap.get(time);
    }

    public TimeNumber getNearMovingAverage(){
        if(movingAverageTimes == null){
            return null;
        }

        BigDecimal close = last.getPrice();

        TimeNumber timeNumber = null;
        BigDecimal min = null;
        for(long time : movingAverageTimes){
            List<TimePrice> list = movingAverageMap.get(time);
            BigDecimal number = Disparity.get(close, list.get(list.size()-1).getClose());
            BigDecimal abs = number.subtract(BigDecimals.DECIMAL_100);

            if(abs.compareTo(BigDecimal.ZERO) < 0){
                abs = abs.multiply(BigDecimals.DECIMAL_M_1);
            }

            if(min == null){
                min = abs;
                timeNumber = new TimeNumberData(time, number);
            }else if(abs.compareTo(min) <= 0 ){
                min = abs;
                timeNumber = new TimeNumberData(time, number);
            }
        }
        return timeNumber;
    }

    public TimePrice getLastMovingAverage(long time){
        List<TimePrice> list = movingAverageMap.get(time);
        return list.get(list.size()-1);
    }

    public String getNearMovingAverageText(){
        TimeNumber timeNumber = getNearMovingAverage();
        if(timeNumber == null){
            return "";
        }
        return CandleTimes.getInterval(timeNumber.getTime()) + "/" + timeNumber.getNumber().toPlainString();

    }

    //24시간 정도 기록저장
    private int maxCount = 18000;

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    private final Object lock = new Object();

    public void add(Collection<TimeVolumes> timeVolumesCollection){
        synchronized (lock){
            list.addAll(timeVolumesCollection);
            if(list.size() > maxCount){
                int remove = list.size() - maxCount;
                //최대건수가 아니면 최대건수 만큼 줄이기
                if (remove > 0) {
                    list.subList(0, remove).clear();
                }
            }

            arrayMap.clear();
            priceGapMoveMap.clear();
        }
    }

    private final Map<Integer, TimeVolumes []> arrayMap = new HashMap<>();
    private final Map<Integer, MaxMinLast> priceGapMoveMap = new HashMap<>();

    private TimeVolumes last;

    public void add(TimeVolumes timeVolumes){
        synchronized (lock){
            last = timeVolumes;
            list.add(timeVolumes);
            if(list.size() > maxCount){
                list.remove(0);
            }
            arrayMap.clear();
            priceGapMoveMap.clear();
        }

        if(movingAverageTimes != null){

            long time = timeVolumes.getTime();

            TimeVolumes [] array = getArray();
            for(long movingAverageTime: movingAverageTimes){
                List<TimePrice> maList =  movingAverageMap.get(movingAverageTime);

                long beginTime = time - movingAverageTime;

                BigDecimal sum = BigDecimal.ZERO;
                int cnt = 0;
                for (int i = array.length-1; i > -1 ; i--) {
                    TimeVolumes tv = array[i];

                    if(tv.getTime() < beginTime){
                        break;
                    }
                    cnt++;
                    sum = sum.add(tv.getPrice());
                }

                maList.add(new TimeNumberData(time, sum.divide(new BigDecimal(cnt), MathContext.DECIMAL128)));

                if(maList.size() > maxCount){
                    maList.remove(0);
                }
            }
        }
    }

    public void clear(){
        synchronized (lock){
            list.clear();
        }
    }

    BigDecimal futuresPriceGapAverage;
    /**
     * 선물과 현물의 가격차이 평균 업데이트
     */
    public void updateFuturesPriceGapAverage(){
        TimeVolumes [] array;
        synchronized (lock){
            array = list.toArray(new TimeVolumes[0]);
        }

        BigDecimal [] gapArray = new BigDecimal[array.length];

        for (int i = 0; i <gapArray.length ; i++) {
            TimeVolumes timeVolumes = array[i];
            gapArray[i] = timeVolumes.getPriceFutures().subtract(timeVolumes.getPrice());
        }

        futuresPriceGapAverage = BigDecimals.average(gapArray, new BigDecimal("0.1"), new BigDecimal("0.1"));
        futuresPriceGapAverage = futuresPriceGapAverage.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros();
    }

    public BigDecimal getFuturesPriceGapAverage() {
        return futuresPriceGapAverage;
    }


    public MaxMinLast getPriceGapMove(int searchCount){

        MaxMinLast maxMinLast = priceGapMoveMap.get(searchCount);
        if(maxMinLast != null){
            return maxMinLast;
        }

        TimeVolumes [] array = getArray(searchCount+1);

        if(searchCount > array.length -1){
            searchCount = array.length -1;
        }

        if(searchCount < 1){
            priceGapMoveMap.put(searchCount, MaxMinLast.ZERO);
            return MaxMinLast.ZERO;
        }

        BigDecimal [] gapArray = new BigDecimal[array.length];
        for (int i = 0; i <gapArray.length ; i++) {
            TimeVolumes timeVolumes =array[i];
            gapArray[i] = timeVolumes.getPriceFutures().subtract(timeVolumes.getPrice());
        }

        BigDecimal [] moveGaps = new BigDecimal[searchCount];
        for (int i = 0; i < moveGaps.length; i++) {
            moveGaps[i] = gapArray[i+1].subtract(gapArray[i]);
        }

        BigDecimal upSum = BigDecimal.ZERO;
        BigDecimal upMax = BigDecimal.ZERO;

        BigDecimal downSum = BigDecimal.ZERO;
        BigDecimal downMin = BigDecimal.ZERO;

        // 1상승중, -1 하락중, 0 변화없음
        int mode = 0;

        for(BigDecimal moveGap: moveGaps){


            if(moveGap.compareTo(BigDecimal.ZERO) > 0){
                if(mode < 0){
                    downMin = BigDecimals.min(downMin, downSum);
                    downSum = BigDecimal.ZERO;
                }
                upSum = upSum.add(moveGap);
                mode = 1;

            }else if(moveGap.compareTo(BigDecimal.ZERO) < 0){
                if(mode > 0){
                    upMax = BigDecimals.max(upMax, upSum);
                    upSum = BigDecimal.ZERO;
                }
                downSum = downSum.add(moveGap);
                mode = -1;
            }
        }

        //하락누적
        MaxMinLast priceGapMove = new MaxMinLast();
        priceGapMove.setLast(moveGaps[moveGaps.length-1]);
        priceGapMove.setMax(BigDecimals.max(upMax, upSum));

        priceGapMove.setMin(BigDecimals.min(downMin, downSum));
        priceGapMoveMap.put(searchCount, priceGapMove);
        return priceGapMove;
    }


    public BigDecimal getLowScore(TimeVolumes [] array, int moveCount){

        BigDecimal high = array[0].getPriceFutures();
        int highIndex = 0;
        for (int i = 1; i <array.length ; i++) {
            TimeVolumes timeVolumes = array[i];
            BigDecimal price = timeVolumes.getPriceFutures();
            if(price.compareTo(high) >= 0){
                high = price;
                highIndex = i;
            }
        }

        if(highIndex == array.length-1){
            return BigDecimal.ZERO;
        }

        int lowIndex = highIndex+1;
        BigDecimal low = array[lowIndex].getPriceFutures();

        for (int i = lowIndex+1; i <array.length ; i++) {
            TimeVolumes timeVolumes = array[i];
            BigDecimal price = timeVolumes.getPriceFutures();
            if(price.compareTo(low) <= 0){
                low = price;
            }
        }

        BigDecimal move = BigDecimal.ZERO;
        for (int i = array.length-moveCount; i < array.length ; i++) {
            move = move.add(array[i].getPriceFutures().subtract(array[i-1].getPriceFutures()));
        }



        BigDecimal price = array[array.length-1].getPriceFutures();
        return high.subtract(price).subtract(price.subtract(low)).subtract(move).divide(price, 6, RoundingMode.HALF_UP).multiply(BigDecimals.DECIMAL_10000).stripTrailingZeros();
    }

    public BigDecimal getHighScore(TimeVolumes [] array, int moveCount){

        if(moveCount > array.length - 1){
            moveCount = array.length-1;
        }

        BigDecimal low = array[0].getPriceFutures();
        int lowIndex = 0;
        for (int i = 1; i <array.length ; i++) {
            TimeVolumes timeVolumes = array[i];
            BigDecimal price = timeVolumes.getPriceFutures();
            if(price.compareTo(low) <= 0){
                low = price;
                lowIndex = i;
            }
        }

        if(lowIndex == array.length-1){
            return BigDecimal.ZERO;
        }

        int highIndex = lowIndex+1;
        BigDecimal high = array[highIndex].getPriceFutures();

        for (int i = highIndex+1; i <array.length ; i++) {
            TimeVolumes timeVolumes = array[i];
            BigDecimal price = timeVolumes.getPriceFutures();
            if(price.compareTo(low) >= 0){
                high = price;
            }
        }

        BigDecimal move = BigDecimal.ZERO;
        for (int i = array.length-moveCount; i < array.length ; i++) {
            move = move.add(array[i].getPriceFutures().subtract(array[i-1].getPriceFutures()));
        }

        BigDecimal price = array[array.length-1].getPriceFutures();
        return price.subtract(low).subtract(high.subtract(price)).add(move).divide(price, 6, RoundingMode.HALF_UP).multiply(BigDecimals.DECIMAL_10000).stripTrailingZeros();
    }


    public TimeVolumes [] getArray(){
        TimeVolumes [] array = arrayMap.get(list.size());
        if(array != null){
            return array;
        }

        array = list.toArray(new TimeVolumes[0]);
        arrayMap.put(list.size(), array);

        return array;
    }

    public TimeVolumes [] getArray(int length){

        TimeVolumes [] array = arrayMap.get(length);
        if(array != null){
            return array;
        }

        if (list.size() < length) {
            length = list.size();
        }
        array = new TimeVolumes[length];
        int size = list.size();
        int gap = size - length;
        for (int i = 0; i < length; i++) {
            array[i] = list.get(i + gap);
        }

        arrayMap.put(length, array);

        return array;
    }

    public TimeVolumes [] getArrayLock(int length){
        synchronized (lock) {
            return getArray(length);
        }
    }

    public TimeVolumes getLast(){
        return last;
    }

    public int size(){
        return list.size();
    }


    public int getMaxCount() {
        return maxCount;
    }
}
