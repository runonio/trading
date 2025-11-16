
package io.runon.trading.technical.analysis.candle;

import io.runon.commons.math.BigDecimals;
import io.runon.commons.utils.GsonUtils;
import io.runon.trading.*;
import io.runon.trading.technical.analysis.hl.HighLow;

import java.math.BigDecimal;
import java.math.MathContext;


/**
 * 캔들 일반적인 캔들 요소들만 정의
 * - 더 많은 요소 클래스는 상속받아서 구현
 * @author macle
 */
public class CandleStick implements PriceChange, Candle, PriceOpenTime, TimePrice, TimeNumber, HighLow {
    
    //데이터 병합을 위해 아이디(키)가 필요한경우
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 가격 상한가 하한가 여부
     */
    protected boolean isPriceLimit = false;
    
    public void setPriceLimit(boolean priceLimit) {
        isPriceLimit = priceLimit;
    }

    /**
     * 가격 제한선 여부 얻기
     * 상한가 하한가가 있는 한국과 같은 주식시장
     * @return 가격제한선 여부
     */
    public boolean isPriceLimit() {
        return isPriceLimit;
    }

    /**
     * 가격 변화 유형
     */
    protected PriceChangeType priceChangeType = PriceChangeType.UNDEFINED;

    /**
     * 가격변화유형 얻기
     * @return PriceChangeType 가격변화유형
     */
    public PriceChangeType getPriceChangeType() {
        return priceChangeType;
    }

    /**
     * 가격변화유형 설정
     * @param priceChangeType PriceChangeType 가격변화유형
     */
    public void setPriceChangeType(PriceChangeType priceChangeType) {
        this.priceChangeType = priceChangeType;
    }


    public void setPriceChangeType(BigDecimal shortGap, BigDecimal steadyGap) {
        if(change.abs().compareTo(steadyGap) < 0){
            priceChangeType = PriceChangeType.HOLD;
        }else{
            if(change.compareTo(BigDecimal.ZERO) > 0){
                priceChangeType = PriceChangeType.RISE;
            }else{
                priceChangeType = PriceChangeType.FALL;
            }
        };
    }

    /**
     * 시가
     */
    protected BigDecimal open;

    /**
     * 종가
     */
    protected BigDecimal close;

    /**
     * 고가
     */
    protected BigDecimal high = null;

    /**
     * 저가
     */
    protected BigDecimal low = null;

    /**
     * 변화랑
     */
    protected BigDecimal change = null;

    /**
     * 가격 변화율
     */
    protected BigDecimal changeRate = null;


    /**
     * 가격 변화율
     */
    protected BigDecimal changePercent = null;

    /**
     * 전 candle 가격
     */
    protected BigDecimal previous = null;

    /**
     * 시가 얻기
     * 설정되지않으면 -1.0
     * @return 시가
     */
    public BigDecimal getOpen() {
        return open;
    }

    /**
     * 시가 설정
     * @param open 시가
     */
    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    /**
     * 종가 얻기
     * @return 종가
     */
    public BigDecimal getClose() {
        return close;
    }

    /**
     * 종가 설정
     * @param close 종가
     */
    public void setClose(BigDecimal close) {
        this.close = close;
    }

    /**
     * 고가 얻기
     * @return  고가
     */
    public BigDecimal getHigh() {
        return high;
    }

    /**
     * 고가 설정
     * @param high 고가
     */
    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    /**
     * 저가 얻기
     * @return 저가
     */
    public BigDecimal getLow() {
        return low;
    }

    /**
     * 저가 설정
     * @param low 저가
     */
    public void setLow(BigDecimal low) {
        this.low = low;
    }


    public BigDecimal getMiddle(){
        return getHighLowClose().divide(BigDecimals.DECIMAL_3, MathContext.DECIMAL128);
    }

    /**
     * 높이 얻기 (세로길이)
     * @return 높이(세로길이)
     */
    public BigDecimal getHeight() {
        BigDecimal min = low;
        min = min.min(getPrevious());

        return high.subtract(min);
    }

    /**
     * 변동성 얻기
     * @return 변동성
     */
    public BigDecimal getVolatility(){
        return io.runon.trading.technical.analysis.hl.HighLow.getVolatility(this);
    }

    /**
     * 변동성 얻기
     * percent 가 붙으면 *100
     * @return 변동성
     */
    public BigDecimal getVolatilityPercent(){
        return io.runon.trading.technical.analysis.hl.HighLow.getVolatilityPercent(this);
    }


    /**
     * 변화가격 설정
     * @param change  변화가격
     */
    public void setChange(BigDecimal change) {
        this.change = change;
        if(previous == null){
            getPrevious();
        }
    }

    /**
     * 전봉가격 얻기
     * @return  전 봉 가격
     */
    public BigDecimal getPrevious() {
        if(previous == null && change != null){
            previous = close.subtract(change);
        }
        return previous;
    }

    /**
     * 전봉 가격 설정
     * @param previous  전봉가격
     */
    public void setPrevious(BigDecimal previous) {
        this.previous = previous;
    }

    /**
     * 변화가격이 설정되어 있을때 전일가격을 세팅한다.
     */
    public void setPrevious() {
        if(close == null || change == null){
            return;
        }

        previous = close.subtract(change);
    }

    /**
     * 직전 가격이 설정되어 있을때 가격변화량 및 변화율을 설정한다.
     */
    public void setChange(){

        BigDecimal previous = getPrevious();

        if(close == null || previous == null){
            return;
        }



        change = close.subtract(previous);
        if(previous.compareTo( BigDecimal.ZERO) == 0){
            changeRate = BigDecimal.ZERO;
            changePercent = BigDecimal.ZERO;
        }else {
            changeRate = change.divide(previous, MathContext.DECIMAL128);
            changePercent = changeRate.multiply(BigDecimals.DECIMAL_100).stripTrailingZeros();
        }
    }

    /**
     * 가격 변화량 얻기
     *
     * @return 변화량
     */
    public BigDecimal getChange() {
        return change;
    }

    /**
     * 가격 변화율 설정
     * @param changeRate 가격변화율
     */
    public void setChangeRate(BigDecimal changeRate) {
        this.changeRate = changeRate;
    }

    /**
     * 가격 변화량 절대값 얻기
     * @return 가격 변화량 절대값
     */
    public BigDecimal changeAbs(){
        return change.abs();
    }

    /**
     * 가격 변화율 얻기
     * 백분율 아님, 백분율로 사용하려면 * 100 해서 써야 함
     * @return 가격 변화율
     */
    public BigDecimal getChangeRate(){
        if(isEndTrade && changeRate != null){
            return changeRate;
        }
        changeRate =  change.divide(getPrevious(), MathContext.DECIMAL128);
        return changeRate;
    }

    /**
     * 가격변화율 (백분율)
     * @return 가격변화율 (백분율)
     */
    public BigDecimal getChangePercent(){
        if(isEndTrade && changePercent != null){
            return changePercent;
        }

        changePercent = getChangeRate().multiply(BigDecimals.DECIMAL_100).stripTrailingZeros();
        return changePercent;
    }


    /**
     * 위꼬리 길이 얻기
     * @return 위 꼬리 길이
     */
    public BigDecimal getUpperTail(){
        return high.subtract(open.max(close));
    }

    /**
     * 아래꼬리 길이 얻기
     * @return 아래 꼬리 길이
     */
    public BigDecimal getLowerTail(){

        return open.max(close).subtract(low);
    }

    /**
     * 시작시간
     */
    protected long openTime = -1;

    /**
     * 끝시간
     */
    protected long closeTime = -1;

    /**
     * 시작시간 얻기
     * 설정되지 않으면 -1
     * @return long 시작시간
     */
    public long getOpenTime() {
        return openTime;
    }

    /**
     * 시작시간 설정
     * 설정되지 않으면 -1
     * @param openTime long 시작시간
     */
    public void setOpenTime(long openTime) {
        this.openTime = openTime;
    }

    /**
     * 끝시간 얻기
     * 설정되지 않으면 -1
     * @return long 끝시간
     */
    public long getCloseTime() {
        return closeTime;
    }

    /**
     * 끝시간 설정
     * 설정되지 않으면 -1
     * @param closeTime long 끝시간
     */
    public void setCloseTime(long closeTime) {
        this.closeTime = closeTime;
    }

    protected boolean isEndTrade = false;

    /**
     * 거래종료여부
     * @return boolean 거래종료여부
     */
    public boolean isEndTrade() {
        return isEndTrade;
    }

    /**
     * 거래종료여부 설정
     */
    public void setEndTrade() {
        isEndTrade = true;
    }

    @Override
    public long getTime() {
        return openTime;
    }

    @Override
    public BigDecimal getNumber() {
        return close;
    }


    public BigDecimal getHighLowClose(){
        return high.add(low).add(close);
    }
    @Override
    public String toString(){
        return GsonUtils.LOWER_CASE_WITH_UNDERSCORES_PRETTY.toJson(this);
    }

}

