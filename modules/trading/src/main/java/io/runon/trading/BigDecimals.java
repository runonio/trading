
package io.runon.trading;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

/**
 * @author macle
 */
public class BigDecimals {


    public final static BigDecimal DECIMAL_K = new BigDecimal(1000);
    public final static BigDecimal DECIMAL_M = new BigDecimal(1000000);
    public final static BigDecimal DECIMAL_B = new BigDecimal(1000000000);
    public final static BigDecimal DECIMAL_T = new BigDecimal("1000000000000");

    // -1
    public final static BigDecimal DECIMAL_M_1 = new BigDecimal(-1);

    public final static BigDecimal DECIMAL_2 = new BigDecimal(2);
    public final static BigDecimal DECIMAL_3 = new BigDecimal(3);
    public final static BigDecimal DECIMAL_4 = new BigDecimal(4);
    public static final BigDecimal DECIMAL_5 = new BigDecimal(5);
    public static final BigDecimal DECIMAL_6 = new BigDecimal(6);
    public static final BigDecimal DECIMAL_7 = new BigDecimal(7);
    public static final BigDecimal DECIMAL_8 = new BigDecimal(8);
    public static final BigDecimal DECIMAL_9 = new BigDecimal(9);

    public static final BigDecimal DECIMAL_10 = new BigDecimal(10);
    public static final BigDecimal DECIMAL_20 = new BigDecimal(20);
    public static final BigDecimal DECIMAL_30 = new BigDecimal(30);
    public static final BigDecimal DECIMAL_40 = new BigDecimal(40);
    public static final BigDecimal DECIMAL_50 = new BigDecimal(50);
    public static final BigDecimal DECIMAL_60 = new BigDecimal(60);
    public static final BigDecimal DECIMAL_70 = new BigDecimal(70);
    public static final BigDecimal DECIMAL_80 = new BigDecimal(80);
    public static final BigDecimal DECIMAL_90 = new BigDecimal(90);
    public final static BigDecimal DECIMAL_100 = new BigDecimal(100);
    public final static BigDecimal DECIMAL_200 = new BigDecimal(200);
    public final static BigDecimal DECIMAL_300 = new BigDecimal(300);
    public final static BigDecimal DECIMAL_400 = new BigDecimal(400);
    public final static BigDecimal DECIMAL_500 = new BigDecimal(500);
    public final static BigDecimal DECIMAL_600 = new BigDecimal(600);
    public final static BigDecimal DECIMAL_700 = new BigDecimal(700);
    public final static BigDecimal DECIMAL_800 = new BigDecimal(800);
    public final static BigDecimal DECIMAL_900 = new BigDecimal(900);

    public final static BigDecimal DECIMAL_1000 = new BigDecimal(1000);
    public final static BigDecimal DECIMAL_5000 = new BigDecimal(5000);

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


    public static final MathContext MC_10 = new MathContext(10);

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

    public static BigDecimal getNumber(String numberText){
        numberText = numberText.toUpperCase();
        numberText = numberText.replace(",","");
        if(numberText.endsWith("K")){
            String number = numberText.substring(0, numberText.length()-1);
            return new BigDecimal(number).multiply(DECIMAL_K).stripTrailingZeros();

        }else if(numberText.endsWith("M")){
            String number = numberText.substring(0, numberText.length()-1);
            return new BigDecimal(number).multiply(DECIMAL_M).stripTrailingZeros();
        }else if(numberText.endsWith("B")){
            String number = numberText.substring(0, numberText.length()-1);
            return new BigDecimal(number).multiply(DECIMAL_B).stripTrailingZeros();
        }else if(numberText.endsWith("T")){
            String number = numberText.substring(0, numberText.length()-1);
            return new BigDecimal(number).multiply(DECIMAL_T).stripTrailingZeros();
        }else{
            return new BigDecimal(numberText).stripTrailingZeros();
        }

    }


    public static BigDecimal sum(List<BigDecimal> list){

        if(list == null || list.isEmpty()){
            return  BigDecimal.ZERO;
        }

        BigDecimal sum = BigDecimal.ZERO;
        for(BigDecimal num : list){
            sum = sum.add(num);
        }

        return sum;
    }

    public static BigDecimal average(List<BigDecimal> list){
        if(list == null || list.isEmpty()){
            return  BigDecimal.ZERO;
        }

        return sum(list).divide(new BigDecimal(list.size()),MathContext.DECIMAL128);
    }

    public static BigDecimal sum(BigDecimal [] array){

        if(array == null || array.length == 0){
            return  BigDecimal.ZERO;
        }

        BigDecimal sum = BigDecimal.ZERO;
        for(BigDecimal num : array){
            sum = sum.add(num);
        }

        return sum;
    }

    public static BigDecimal average(BigDecimal [] array){
        if(array == null || array.length == 0){
            return  BigDecimal.ZERO;
        }

        return sum(array).divide(new BigDecimal(array.length),MathContext.DECIMAL128);
    }


}
