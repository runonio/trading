package io.runon.trading;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
/**
 * 트레이딩 관련 수학도구
 * @author macle
 */
public class TradingMath {


    public static BigDecimal sum(BigDecimal[] array, int count){
        return sum(array, array.length - count, array.length);
    }

    public static BigDecimal sum(BigDecimal[] array, int startIndex, int end){
        if(startIndex < 0){
            startIndex = 0;
        }

        if(end > array.length){
            end = array.length;
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (int i = startIndex; i < end ; i++) {
            sum = sum.add(array[i]);
        }
        return sum;
    }

    public static BigDecimal sum( BigDecimal [] array){
        return add(BigDecimal.ZERO, array);
    }

    public static BigDecimal add(BigDecimal num, BigDecimal [] array){
        for(BigDecimal decimal : array){
            num = num.add(decimal);
        }
        return num;
    }


    public static BigDecimal average(BigDecimal[] array, int n, int index){
        BigDecimal sum = BigDecimal.ZERO;
        int end = index +1;
        int start = end -n;
        if(start < 0) {
            start = 0;
        }

        for (int i = start; i < end; i++) {
            sum = sum.add(array[i]);
        }
        return sum.divide(new BigDecimal(end - start), MathContext.DECIMAL128);
    }

    public static BigDecimal average(BigDecimal[] sortNumbers, String highestExclusionRate) {
        return  average(sortNumbers, new BigDecimal(highestExclusionRate));
    }

    /**
     * 평균구하기
     * @param sortNumbers 정렬된 숫자 반드시 정렬된 객체를 보내야함
     * @param highestExclusionRate 상위 제외비율 0.1 = 10%제외
     * @return 상위제외 평균
     */
    public static BigDecimal average(BigDecimal[] sortNumbers, BigDecimal highestExclusionRate) {

        int count = new BigDecimal(sortNumbers.length).multiply(BigDecimal.ONE.subtract(highestExclusionRate)).intValue();

        if(count == 0 || sortNumbers.length == 0){
            throw new IllegalArgumentException("count: " + count +" length " + sortNumbers.length);
        }

        BigDecimal sum = BigDecimal.ZERO;

        for (int i = 0; i < count; i++) {
            sum = sum.add(sortNumbers[i]);
        }

        return sum.divide(new BigDecimal(count), MathContext.DECIMAL128);
    }

    /**
     * 중간평균 구하기
     * @param sortNumbers 정렬된 숫자 반드시 정렬된 객체를 보내야함
     * @param lowestExclusionRate 하위 제외비율 0.1 = 10%제외
     * @param highestExclusionRate 상위 제외비율 0.1 = 10%제외
     * @return 하위 상위를 제외한 중간평균
     */
    public static BigDecimal average(BigDecimal[] sortNumbers, BigDecimal lowestExclusionRate, BigDecimal highestExclusionRate) {

        int end = new BigDecimal(sortNumbers.length).multiply(BigDecimal.ONE.subtract(highestExclusionRate)).intValue();
        int start = new BigDecimal(sortNumbers.length).multiply(highestExclusionRate).intValue();
        int size = end - start;

        if(size < 1){
            throw new IllegalArgumentException("start: " + start +" end " + end +" length " + sortNumbers.length);
        }

        BigDecimal sum = BigDecimal.ZERO;

        for (int i = start; i < end; i++) {
            sum = sum.add(sortNumbers[i]);
        }

        return sum.divide(new BigDecimal(size), MathContext.DECIMAL128);
    }

    public static BigDecimal average(BigDecimal[] numbers) {
        if(numbers == null || numbers.length == 0){
            return BigDecimal.ZERO;
        }

        BigDecimal sum = sum(numbers);
        return sum.divide(new BigDecimal(numbers.length), MathContext.DECIMAL128);
    }




    public static BigDecimal changePercent(BigDecimal previous , BigDecimal close){
        int compare = previous.compareTo(close);

        if(compare == 0){
            return BigDecimal.ZERO;
        }
        BigDecimal change = close.subtract(previous);
        return change.divide(previous,4, RoundingMode.HALF_UP).multiply(BigDecimals.DECIMAL_100).stripTrailingZeros();
    }

    public static BigDecimal max(BigDecimal [] array){
        BigDecimal max = array[0];
        for (int i = 1; i < array.length; i++) {
            if(max.compareTo(array[i]) < 0){
                max = array[i];
            }
        }
        return max;
    }

    public static BigDecimal min(BigDecimal [] array){
        BigDecimal min = array[0];
        for (int i = 1; i < array.length; i++) {
            if(min.compareTo(array[i]) > 0){
                min = array[i];
            }
        }

        return min;
    }

    public static BigDecimal abs( BigDecimal number){
        if(number.compareTo(BigDecimal.ZERO) < 0){
            return number.multiply(BigDecimals.DECIMAL_M_1);
        }
        return number;
    }


    public static BigDecimal sd( BigDecimal [] array){
        int index = array.length-1;
        return sd(array, average(array, array.length, index), array.length, index );
    }
    public static BigDecimal sd( BigDecimal [] array , int n){
        int index = array.length-1;
        return sd(array, average(array, n, index), n, index );
    }

    public static BigDecimal sd( BigDecimal [] array , int n, int index){
        return sd(array, average(array, n, index), n, index );
    }

    public static BigDecimal sd( BigDecimal [] array ,BigDecimal avg, int n){
        return sd(array, avg, n, array.length-1 );
    }


    /**
     * 표준편차
     * 표준 편차(標準 偏差, 영어: standard deviation, SD)는 통계집단의 분산의 정도 또는 자료의 산포도를 나타내는 수치
     */
    public static BigDecimal sd( BigDecimal [] array, BigDecimal avg , int n, int index){


        int end = index +1;
        int start = end -n;
        if(start < 0) {
            start = 0;
        }
        int length = end - start ;

        if(length < 1){
            return BigDecimal.ZERO;
        }

        if(avg == null){
            avg = average(array, n, index);
        }

        BigDecimal d = BigDecimal.ZERO;

        for (int i = start; i < end; i++) {
            d = d.add(array[i].subtract(avg).pow(2));
        }

        d = d.divide(new BigDecimal(length), MathContext.DECIMAL128);


        return d.sqrt(BigDecimals.MC_10);
    }

    public static BigDecimal min(BigDecimal num1, BigDecimal num2){
        if(num1 == null){
            return num2;
        }

        if(num2 == null){
            return num1;
        }

        if(num1.compareTo(num2) > 0){
            return num2;
        }

        return num1;
    }

    public static BigDecimal max(BigDecimal num1, BigDecimal num2){
        if(num1 == null){
            return num2;
        }

        if(num2 == null){
            return num1;
        }

        if(num1.compareTo(num2) < 0){
            return num2;
        }

        return num1;
    }


    /**
     * Maximum Drawdown, ‘최대 낙폭’ 또는 ‘최대 손실폭’을 의미함
     *
     * 일정 기간 동안 고점에서 저점으로 떨어진 비율을 계산한 수치
     *  MDD = (고점 – 저점) / 고점 X 100
     *
     * @param high 고점
     * @param low 저점
     * @return mdd
     */
    public static BigDecimal mdd(BigDecimal high, BigDecimal low){
        return high.subtract(low).multiply(BigDecimals.DECIMAL_100).divide(high,2,RoundingMode.HALF_UP).stripTrailingZeros();

    }
}
