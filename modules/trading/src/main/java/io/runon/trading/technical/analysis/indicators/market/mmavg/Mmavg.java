package io.runon.trading.technical.analysis.indicators.market.mmavg;

import io.runon.commons.data.GetNumber;
import io.runon.commons.math.BigDecimalMath;
import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.technical.analysis.candle.*;
import io.runon.trading.technical.analysis.indicators.market.MarketIndicatorsTimeNumber;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


/**
 * 시장 중간 평균 등락률
 *
 * 시총이 높은 주식들로만 보이는 지수로는 전체시장이 좋은지는 알 수없음.
 * 전체시장이 좋은지를 판단하고자 만든지표.
 * market middle average
 *
 * 이 클래스는 지수값을 만들지 않고 날짜별 평균 등락률만 산출한다.
 * base 부터 누적해서 지수화하는 작업은 외부 집계 클래스에서 처리한다.
 *
 * - 그날의 생존편향 제거: 상장폐지 종목까지 포함된 IdCandlesGet 배열을 입력으로 사용한다.
 * - 정리매매 주식 왜곡 제거: excludeFilter 를 이용해 외부에서 제거한다.
 * - 그날의 하위 거래대금 5% 제거
 * - 상승 상위 10%, 하락 하위 10% 제거 옵션
 *
 *
 * @author macle
 */
public class Mmavg extends MarketIndicatorsTimeNumber {

    /**
     * 거래대금 하위 제거 비율
     */
    private BigDecimal lowAmountExclusionRate = new BigDecimal("0.05");


    /**
     * 정리매매, 거래정지, 관리종목, 신규상장 등 외부 조건 제거용 필터
     */

    public Mmavg(IdCandlesGet[] idCandles) {
        super(idCandles);
        scale = -1;
    }

    public Mmavg(IdCandleTimes idCandleTimes) {
        super(idCandleTimes);
        scale = -1;
    }

    public void setLowAmountExclusionRate(BigDecimal lowAmountExclusionRate) {
        this.lowAmountExclusionRate = lowAmountExclusionRate;
    }


    /**
     * 해당 날짜의 시장 중간 평균 등락률
     *
     * @param index times index
     * @return 평균 등락률. 0.01 = 1%, -0.01 = -1%
     */
    @Override
    public TimeNumber getData(int index) {
        long time = times[index];

        BigDecimal averageChangeRate = getAverageChangeRate(index);
        if (averageChangeRate == null) {
            averageChangeRate = BigDecimal.ZERO;
        }

        if(scale > 0) {

            return new TimeNumberData(time, averageChangeRate.setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros());
        }else{
            return new TimeNumberData(time, averageChangeRate.stripTrailingZeros());
        }
    }

    /**
     * 해당 날짜의 시장 중간 평균 등락률
     *
     * @param index times index
     * @return 평균 등락률. 0.01 = 1%, -0.01 = -1%
     */
    public BigDecimal getAverageChangeRate(int index) {
        if (index < 1) {
            return BigDecimal.ZERO;
        }

        long time = times[index];

        List<TradeCandle> candleList = new ArrayList<>();

        int searchLength = searchIndex(index);

        for (IdCandlesGet idCandle : idCandles) {
            TradeCandle[] candles = idCandle.getCandles();

            int openTimeIndex = Candles.getOpenTimeIndex(candles, time, searchLength);
            if (openTimeIndex < 1) {
                continue;
            }

            TradeCandle candle = candles[openTimeIndex];

            //정리매매 파악하기

            BigDecimal amount = candle.getAmount();
            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            if (minAmount != null && amount.compareTo(minAmount) < 0) {
                continue;
            }

            BigDecimal close = candle.getClose();
            if (close == null || close.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            candleList.add(candle);

        }

        if (candleList.isEmpty()) {
            return BigDecimal.ZERO;
        }

        //하위 제거
        TradeCandleAmount [] amounts = TradeCandleAmount.newArray(candleList);

        Arrays.sort(amounts, GetNumber.SORT_ASC);

        int start = new BigDecimal(amounts.length).multiply(lowAmountExclusionRate).intValue();


        BigDecimal sum = BigDecimal.ZERO;

        for (int i = start; i < amounts.length; i++) {
            TradeCandle candle = amounts[i].getCandle();
            sum = sum.add(candle.getChangeRate());
        }

        return sum.divide(new BigDecimal( (amounts.length - start)),  MathContext.DECIMAL128);
    }

}