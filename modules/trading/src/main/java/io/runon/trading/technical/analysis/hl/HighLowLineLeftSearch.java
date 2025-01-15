package io.runon.trading.technical.analysis.hl;

import io.runon.trading.TimeNumber;

import java.math.BigDecimal;

/**
 * 고가저가 정보 좌측 (이전 거래내역으로 ) 검색
 * 라인차트 관점
 *
 * 일반 수치만 제공하는 보조지표에서 사용하기 위해 개발
 *
 * @author macle
 */
public class HighLowLineLeftSearch {

    public static HighLowTime getHighNextLow(TimeNumber[] array, int initN, int continueN){
        return getHighNextLow(array, initN, continueN, array.length-1);
    }

    /**
     * 고가 먼저 찾고 저가 찾기
     * @param array 배열
     * @param initN 초기검색범위
     * @param continueN 연속 신고가 검색 범위. n 이 50이면 50개 사이에 신고가가 있을경우 계속 검색
     * @param index 위치
     * @return 고가 저가 정보
     */
    public static HighLowTime getHighNextLow(TimeNumber [] array , int initN , int continueN , int index){
        int highIndex = searchHigh(array, initN , continueN,  index);
        int lowIndex = searchLow(array, highIndex, index+1);
        return get(array, index, highIndex, lowIndex);
    }

    public static int searchHighIndex(TimeNumber [] array, int n, int index){

        int end = index +1;
        int startIndex = end - n;

        return searchHigh(array, startIndex, end);
    }

    public static int searchHigh(TimeNumber [] array , int initN ,int continueN , int index){
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

    public static int searchHigh(TimeNumber [] array, int startIndex, int end){

        if(startIndex < 0){
            startIndex = 0;
        }

        if(end > array.length){
            end = array.length;
        }
        BigDecimal high = array[startIndex].getNumber();
        int highIndex = startIndex;
        for (int i = startIndex + 1; i <end ; i++) {
            BigDecimal compareHigh = array[i].getNumber();
            if(compareHigh.compareTo(high) > 0){

                high = compareHigh;
                highIndex = i;
            }
        }
        return highIndex;
    }


    public static int searchLowIndex(TimeNumber [] array, int n, int index){
        int end = index +1;
        int startIndex = end - n;
        return searchLow(array, startIndex, end);
    }

    public static int searchLow(TimeNumber [] array ,  int initN ,int continueN , int index){
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

    public static int searchLow(TimeNumber [] array, int startIndex, int end){

        if(startIndex < 0){
            startIndex = 0;
        }

        if(end > array.length){
            end = array.length;
        }

        BigDecimal low = array[startIndex].getNumber();
        int lowIndex = startIndex;

        for (int i = startIndex + 1; i <end ; i++) {
            BigDecimal compareLow = array[i].getNumber();
            if(compareLow.compareTo(low) < 0){

                low = compareLow;
                lowIndex = i;
            }
        }

        return lowIndex;
    }

    /**
     * 저가 먼저 찾고 고가 찾기
     * @param array 배열
     * @param initN 초기검색범위
     * @param continueN 연속 신저가 검색 범위. n 이 50이면 50개 사이에 신저가가 있을경우 계속 검색
     * @param index 위치
     * @return 고가 저가 정보
     */
    public static HighLowTime getLowNextHigh(TimeNumber [] array , int initN , int continueN , int index){
        int lowIndex = searchLow(array, initN ,continueN, index);

        int highIndex = searchHigh(array, lowIndex, index+1);

        return get(array, index, highIndex, lowIndex);
    }

    public static HighLowTime get(TimeNumber [] array, int index, int highIndex , int lowIndex){
        TimeNumber timeNumber = array[index];

        TimeNumber high = array[highIndex];
        TimeNumber low = array[lowIndex];

        HighLowTime highLow = new HighLowTime();
        highLow.time = timeNumber.getTime();

        highLow.high = high.getNumber();
        highLow.highIndex = highIndex;
        highLow.highTime = high.getTime();

        highLow.low = low.getNumber();
        highLow.lowIndex = lowIndex;
        highLow.lowTime = low.getTime();

        return highLow;
    }

}
