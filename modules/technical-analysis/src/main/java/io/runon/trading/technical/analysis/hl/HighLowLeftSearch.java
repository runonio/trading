package io.runon.trading.technical.analysis.hl;

import io.runon.trading.technical.analysis.candle.CandleStick;

import java.math.BigDecimal;

/**
 * 고가저가 정보 좌측 (이전 거래내역으로 ) 검색
 * @author macle
 */
public class HighLowLeftSearch {


    public static HighLow getHighNextLow(CandleStick [] array, int n){
        return getHighNextLow(array, n, array.length-1);
    }
    
    /**
     * 고가 먼저 찾고 저가 찾기
     * 피보나치에서 활용 반등폭 예측하기
     * 대세 하락이후에 반등을 줄때 활용한다.
     * @param array 배열
     * @param n 신고가 검색 범위. n 이 50이면 50개 사이에 신고가가 있을경우 계속 검색
     * @param index 위치
     * @return 고가 저가 정보
     */
    public static HighLow getHighNextLow(CandleStick [] array , int n , int index){
        int highIndex = index;
        for(;;){
            int searchIndex = searchHighIndex(array, n , highIndex);

            if(highIndex == searchIndex){
                break;
            }

            highIndex = searchIndex;
        }



        return null;
    }

    public static int searchHighIndex(CandleStick [] array, int n, int index){

        int end = index +1;
        int startIndex = end - n;

        return searchHigh(array, startIndex, end);
    }
    public static int searchHigh(CandleStick [] array, int startIndex, int end){

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


    public static int searchLow(CandleStick [] array, int startIndex, int end){

        if(startIndex < 0){
            startIndex = 0;
        }

        if(end > array.length){
            end = array.length;
        }

        BigDecimal low = array[startIndex].getHigh();
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

    /**
     * 저가 먼저 찾고 고가 찾기
     * 피보나치에서 활용 조정폭 예상하기
     * 대세 상승 이후 조정을 줄때 활용한다.
     * @param array 배열
     * @param n 신저가 검색 범위. n 이 50이면 50개 사이에 신고가가 있을경우 계속 검색
     * @param index 위치
     * @return 고가 저가 정보
     */
    public static HighLow getLowNextHigh(CandleStick [] array , int n , int index, boolean isExpansion){
        
        return null;
    }

}
