package io.runon.trading.technical.analysis.hl;

import io.runon.trading.technical.analysis.candle.CandleStick;

import java.math.BigDecimal;

/**
 * 고가저가 정보 좌측 (이전 거래내역으로 ) 검색
 * 캔들 차트 관점
 * @author macle
 */
public class HighLowCandleLeftSearch {


    public static HighLowTime getHighNextLow(CandleStick[] array, int initN, int continueN){
        return getHighNextLow(array, initN, continueN, array.length-1);
    }


    public static HighLowTime getHighNextLow(CandleStick[] array , int initN , int continueN , int index, int maxNextInit, BigDecimal high){

        int highIndex = searchHigh(array, initN , continueN,  index);

        for (int i = 1; i <maxNextInit ; i++) {
            CandleStick candleStick = array[highIndex];
            if(candleStick.getHigh().compareTo(high) > 0){
                break;
            }

            int nextInitN = initN + i*initN;
            highIndex = searchHigh(array, nextInitN , continueN,  index);
        }


        int lowIndex = searchLow(array, highIndex, index+1);
        return get(array, index, highIndex, lowIndex);
    }

    /**
     * 고가 먼저 찾고 저가 찾기
     * 피보나치에서 활용 반등폭 예측하기
     * 대세 하락이후에 반등을 줄때 활용한다.
     * @param array 배열
     * @param initN 초기검색범위
     * @param continueN 연속 신고가 검색 범위. n 이 50이면 50개 사이에 신고가가 있을경우 계속 검색
     * @param index 위치
     * @return 고가 저가 정보
     */
    public static HighLowTime getHighNextLow(CandleStick[] array , int initN , int continueN , int index){
        int highIndex = searchHigh(array, initN , continueN,  index);
        int lowIndex = searchLow(array, highIndex, index+1);
        return get(array, index, highIndex, lowIndex);
    }

    public static int searchHighIndex(CandleStick[] array, int n, int index){

        int end = index +1;
        int startIndex = end - n;

        return searchHigh(array, startIndex, end);
    }

    public static int searchHigh(CandleStick[] array , int initN , int continueN , int index){
        int highIndex = searchHighIndex(array, initN , index);
        for(;;){
            int searchIndex = searchHighIndex(array, continueN , highIndex);

            if(highIndex == searchIndex){
                break;
            }

            highIndex = searchIndex;
        }

        return highIndex;
    }

    public static int searchHigh(CandleStick[] array, int startIndex, int end){

        if(startIndex < 0){
            startIndex = 0;
        }

        if(end > array.length){
            end = array.length;
        }
        BigDecimal high = array[startIndex].getHigh();
        int highIndex = startIndex;
        for (int i = startIndex + 1; i <end ; i++) {
            BigDecimal compareHigh = array[i].getHigh();
            if(compareHigh.compareTo(high) > 0){

                high = compareHigh;
                highIndex = i;
            }
        }
        return highIndex;
    }


    public static int searchLowIndex(CandleStick[] array, int n, int index){
        int end = index +1;
        int startIndex = end - n;
        return searchLow(array, startIndex, end);
    }

    public static int searchLow(CandleStick[] array , int initN , int continueN , int index){
        int lowIndex = searchLowIndex(array, initN , index);
        for(;;){
            int searchIndex = searchLowIndex(array, continueN , lowIndex);

            if(lowIndex == searchIndex){
                break;
            }

            lowIndex = searchIndex;
        }

        return lowIndex;
    }

    public static int searchLow(CandleStick[] array, int startIndex, int end){

        if(startIndex < 0){
            startIndex = 0;
        }

        if(end > array.length){
            end = array.length;
        }

        BigDecimal low = array[startIndex].getLow();
        int lowIndex = startIndex;

        for (int i = startIndex + 1; i <end ; i++) {
            BigDecimal compareLow = array[i].getLow();
            if(compareLow.compareTo(low) < 0){

                low = compareLow;
                lowIndex = i;
            }
        }

        return lowIndex;
    }

    public static HighLowTime getLowNextHigh(CandleStick[] array , int initN , int continueN , int index, int maxNextInit, BigDecimal low){
        int lowIndex = searchLow(array, initN ,continueN, index);

        for (int i = 1; i <maxNextInit ; i++) {
            CandleStick candleStick = array[lowIndex];
            if(candleStick.getLow().compareTo(low) < 0){
                break;
            }

            int nextInitN = initN + i*initN;
            lowIndex = searchLow(array, nextInitN , continueN,  index);
        }

        int highIndex = searchHigh(array, lowIndex, index+1);

        return get(array, index, highIndex, lowIndex);
    }

    /**
     * 저가 먼저 찾고 고가 찾기
     * 피보나치에서 활용 조정폭 예상하기
     * 대세 상승 이후 조정을 줄때 활용한다.
     * @param array 배열
     * @param initN 초기검색범위
     * @param continueN 연속 신저가 검색 범위. n 이 50이면 50개 사이에 신저가가 있을경우 계속 검색
     * @param index 위치
     * @return 고가 저가 정보
     */
    public static HighLowTime getLowNextHigh(CandleStick[] array , int initN , int continueN , int index){
        int lowIndex = searchLow(array, initN ,continueN, index);

        int highIndex = searchHigh(array, lowIndex, index+1);

        return get(array, index, highIndex, lowIndex);
    }

    public static HighLowTime get(CandleStick[] array, int index, int highIndex , int lowIndex){
        CandleStick candleStick = array[index];

        CandleStick highCandle = array[highIndex];
        CandleStick lowCandle = array[lowIndex];


        HighLowTime highLow = new HighLowTime();
        highLow.time = candleStick.getTime();

        highLow.high = highCandle.getHigh();
        highLow.highIndex = highIndex;
        highLow.highTime = highCandle.getTime();

        highLow.low = lowCandle.getLow();
        highLow.lowIndex = lowIndex;
        highLow.lowTime = lowCandle.getTime();

        return highLow;
    }


}
