
package io.runon.trading;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * @author macle
 */
public class BigDecimals {

    // -1
    public final static BigDecimal DECIMAL_M_1 = new BigDecimal(-1);

    public final static BigDecimal DECIMAL_2 = new BigDecimal(2);
    public final static BigDecimal DECIMAL_3 = new BigDecimal(3);
    public final static BigDecimal DECIMAL_4 = new BigDecimal(4);
    public static final BigDecimal DECIMAL_5 = new BigDecimal(5);
    public final static BigDecimal DECIMAL_100 = new BigDecimal(100);

    public final static BigDecimal DECIMAL_1000 = new BigDecimal(1000);
    public final static BigDecimal DECIMAL_10000 = new BigDecimal(10000);
    public final static BigDecimal DECIMAL_100000 = new BigDecimal(100000);
    public final static BigDecimal DECIMAL_1000000 = new BigDecimal(1000000);

    //0.1
    public final static BigDecimal DECIMAL_0_1 = new BigDecimal("0.1");


    //0.5
    public final static BigDecimal DECIMAL_0_5 = new BigDecimal("0.5");

    //1.5
    public final static BigDecimal DECIMAL_1_5 = new BigDecimal("1.5");

    //2.5
    public final static BigDecimal DECIMAL_2_5 = new BigDecimal("2.5");


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
     * BigDecimal 을 활용한 text 얻기
     * @param num BigDecimal
     * @param scale 소수점
     * @return 문자열
     */
    public static String getText(BigDecimal num, int scale){
        if( num == null){
            return "";
        }
        return num.setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
    }

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

    /**
     * 인덱스 위치를 기준으로 이전 배열의 위치를 복사해서 전달
     * @param src 원본배열
     * @param lastIndex 기준 지점 (끝지점)
     * @param length 건수
     * @return 복사된 배열
     */
    public static BigDecimal [] copy(BigDecimal [] src, int lastIndex, int length){
        if(length > lastIndex + 1){
            length = lastIndex +1;
        }
        BigDecimal [] array = new BigDecimal[length];

        System.arraycopy(src, lastIndex - length + 1, array, 0, length);
        return array;
    }


    public static BigDecimal getChangePercent(BigDecimal previous , BigDecimal close){
        int compare = previous.compareTo(close);

        if(compare == 0){
            return BigDecimal.ZERO;
        }
        BigDecimal change = close.subtract(previous);
        return change.divide(previous,4, RoundingMode.HALF_UP).multiply(DECIMAL_100).stripTrailingZeros();
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
            return number.multiply(DECIMAL_M_1);
        }
        return number;
    }


}
