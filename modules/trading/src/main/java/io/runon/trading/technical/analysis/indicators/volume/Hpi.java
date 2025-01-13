package io.runon.trading.technical.analysis.indicators.volume;

import io.runon.trading.BigDecimals;
import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.TimeNumbers;
import io.runon.trading.oi.OpenInterest;
import io.runon.trading.oi.OpenInterestSymbol;
import io.runon.trading.technical.analysis.candle.TradeCandle;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 헤릭정산지수
 * Herrick Payoff Index
 *
 * HPI(Herrick Payoff Index)는 John Herrick이개발한 지표로 매집과 분산을 파악하는 데 유용하다.
 * 옵션 등 미결제약정이 있는 종목에서 사용되는 지표로 가격, 거래량, 미결제약정을 추적하여 추세의 유효성을 확인하고 추세 반전을 포착하는 기능을 한다.HPI는 당일 고가, 당일 저가, 거래량, 미결제 약정을 이용하며 최소 3주의 데이터가 있어야 유용한 값을 도출할 수 있다.
 *
 * mystorage1.tistory.com/578
 *
 * 블로그 내용을 보면 헤릭정산지수 방식이 책 내용이랑은 조금 변경된 부분이 있다.
 * 투자심리법칙의 수식과의 차이는 미결제약정의 최대값이 아닌 최소값으로 나눠주는 것, 구하는 수식에서 배율 인수와 100000으로 나눠주는 행위가 없어진 것이다.엘더 박사의 책에서는 HPI가 지금보다 낮은 값들을 형성하는데 그 계산과 다른 점은 예전보다 거래량이 많고 미결제약정이 커서 단위자체가 켜졌기 때문이다.
 *
 *
 * HPI = (K(1) + (K - K(1)) * S)/100000
 * K = (CM * V * (M-M(1)) * (1±(2*I)/G);
 * S = 배율 인수이동평균의 평활화와 유사하다. 배율 인수가 10이면 10기간 이동 평균과 유사하다.
 * CM = 1센트 움직이는 데 대한 가치헤릭은 상품의 1센트당 이동 가치를 100으로 권장, 은의 경우 50
 * V = 거래량 volume
 * M = 중간값 (H+L)/2
 * ± =  M > M(1) -> +
 *      M < M(1) -> -
 * I = 당일 미결제약정 - 전일 미결제약정 OI-OI(1)
 * G = 당일 미결제약정 과 전일 미결제약정 중 큰 값 max(OI,OI(1))
 *
 * 붙여넣기 수식
 * M = (H+L)/2;
 * K = (CM * V * (M-M(1)))*(if(M>M(1),1+((abs(OI-OI(1))*2)/max(OI,OI(1))),1-((abs(OI-OI(1))*2)/max(OI,OI(1)))));
 * (K(1)+(K-K(1))*s)/10000
 *
 *
 *
 * @author macle
 */
public class Hpi {

    private final OpenInterestSymbol openInterestSymbol;

    protected int scale = 4;
    public void setScale(int scale) {
        this.scale = scale;
    }

    public Hpi(OpenInterestSymbol openInterestSymbol){
        this.openInterestSymbol = openInterestSymbol;
    }

    private BigDecimal s = BigDecimals.DECIMAL_10;

    private BigDecimal cm = BigDecimals.DECIMAL_100;

    public void setS(BigDecimal s) {
        this.s = s;
    }
    public void setCm(BigDecimal cm) {
        this.cm = cm;
    }

    private BigDecimal denominator = BigDecimals.DECIMAL_100000;

    public void setDenominator(BigDecimal denominator) {
        this.denominator = denominator;
    }

    public TimeNumber get(TradeCandle[] array, int index){
        BigDecimal k1 = getK(array, index -1);
        if(k1 == null){
            return null;
        }
        return get(array, k1, index);
    }

    public TimeNumber get(TradeCandle[] array, BigDecimal k1, int index){

        BigDecimal k = getK(array, index);
        if(k == null){
            return null;
        }

        BigDecimal hpi = k1.add( k.subtract(k1).multiply(s).divide(denominator, MathContext.DECIMAL128));

        TimeNumberData timeNumberData = new TimeNumberData();
        timeNumberData.setTime(array[index].getTime());
        timeNumberData.setNumber(hpi.setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros());

        return timeNumberData;
    }

    public BigDecimal getK(TradeCandle[] array, int index){
//        K = (CM * V * (M-M(1)) * (1±(2*I)/G);

        TradeCandle previousCandle = array[index - 1];
        TradeCandle candle = array[index];

        BigDecimal middle = candle.getHigh().add(candle.getLow()).divide(BigDecimals.DECIMAL_2, MathContext.DECIMAL128);
        BigDecimal previousMiddle = previousCandle.getHigh().add(previousCandle.getLow()).divide(BigDecimals.DECIMAL_2, MathContext.DECIMAL128);

        boolean isPlus =  middle.compareTo(previousMiddle) >= 0;
//        (1±(2*I)/G);

        OpenInterest openInterest =  openInterestSymbol.getData(candle.getOpenTime());
        //구할 수 없음
        if(openInterest == null){
            return null;
        }

        OpenInterest previousOpenInterest =  openInterestSymbol.getData(previousCandle.getOpenTime());
        if(previousOpenInterest == null){
            return null;
        }

        BigDecimal i = openInterest.getOpenInterest().subtract(previousOpenInterest.getOpenInterest());


        BigDecimal g = openInterest.getOpenInterest().max(previousOpenInterest.getOpenInterest());
        if(g.compareTo(BigDecimal.ZERO) == 0){
            g = BigDecimal.ONE;
        }


        BigDecimal km;
        if(isPlus){
            km = BigDecimal.ONE.add(BigDecimals.DECIMAL_2.multiply(i).divide(g, MathContext.DECIMAL128));
        }else{
            km = BigDecimal.ONE.subtract(BigDecimals.DECIMAL_2.multiply(i).divide(g, MathContext.DECIMAL128));
        }

        return cm.multiply(candle.getVolume().multiply(middle.subtract(previousMiddle))).multiply(km);
//        * HPI = (K(1) + (K - K(1)) * S)/100000
//                * K = (CM * V * (M-M(1)) * (1±(2*I)/G);
// * S = 배율 인수이동평균의 평활화와 유사하다. 배율 인수가 10이면 10기간 이동 평균과 유사하다.
// * CM = 1센트 움직이는 데 대한 가치헤릭은 상품의 1센트당 이동 가치를 100으로 권장, 은의 경우 50
//                * V = 거래량 volume
//                * M = 중간값 (H+L)/2
//                * ± =  M > M(1) -> +
//                *      M < M(1) -> -
//                * I = 당일 미결제약정 - 전일 미결제약정 OI-OI(1)
//                * G = 당일 미결제약정 과 전일 미결제약정 중 큰 값 max(OI,OI(1))

    }

    public TimeNumber [] getArray(TradeCandle [] array, int resultLength){
        return getArray(array,  array.length - resultLength, array.length);
    }
    public TimeNumber [] getArray(TradeCandle[] array, int startIndex, int end){

        if(end > array.length){
            end = array.length;
        }

        if(startIndex < 2){
            startIndex = 2;
        }

        if(startIndex >= end){
            return TimeNumbers.EMPTY_ARRAY;
        }

        TimeNumber init = null;

        for (int i = startIndex; i < end ; i++) {
            init = get(array, i);
            if(init != null){
                startIndex = i+1;
                break;
            }
        }

        if(init == null){
            return TimeNumbers.EMPTY_ARRAY;
        }

        List<TimeNumber> list = new ArrayList<>();
        list.add(init);

        BigDecimal k1 = init.getNumber().negate();

        for (int i = startIndex; i < end; i++) {
            TimeNumber hpi = get(array, k1, i);
            if(hpi == null){
                continue;
            }

            k1 = hpi.getNumber();
            list.add(hpi);
        }

        TimeNumber [] dataArray = list.toArray(new TimeNumber[0]);
        list.clear();
        return dataArray;
    }

}
