/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.runon.trading.technical.analysis.candle;

import io.runon.trading.PriceChange;
import io.runon.trading.PriceChangeType;

import java.math.BigDecimal;
import java.math.MathContext;


/**
 * 캔들 일반적인 캔들 요소들만 정의
 * - 더 많은 요소 클래스는 상속받아서 구현
 * @author macle
 */
public class CandleStick implements PriceChange, Candle {

    //자세한 모양은 구글시트 참조
    //https://docs.google.com/spreadsheets/d/13T8OR02ESmGTsD6uAI5alYPdRg6ekrfnVnkCdqpoAvE/edit#gid=1228683334
    public enum Type {
        UNDEFINED // 정의되지않음
        , STEADY //보합세 캔들 시가 == 종가 or 시가종가가 비슷한 캔들
        , LONG // 긴 캔들
        , SHORT // 짧은 캔들
        , UPPER_SHADOW //위 그림자 캔들 -- 유성형 역망치형
        , LOWER_SHADOW //아래 그림자 캔들 -- 망치형(Hammer)저점 --  교수형(Hanging man)고점
        , HIGH_WAVE //위 아래에 그림자가 있는캔들 (긴거)
        , SPINNING_TOPS //위 아래에 그림자가 있는 캔들 (짧은거)
        , DOJI // 십자캔들
    }
    /**
     * 캔들 유형
     */
    private Type type = Type.UNDEFINED;

    /**
     * 캔들 유형 얻기
     * @return Type 유형
     */
    public Type getType() {
        return type;
    }


    /**
     * 유형설정
     * @param shortGap  짧은 캔들 gap
     * @param steadyGap  보합세 gap
     */
    public void setType(BigDecimal shortGap, BigDecimal steadyGap){

        BigDecimal height = getHeight();
        //길이가 보합세보다 작을때
//        if(height <= steadyGap){
        if(height.compareTo(steadyGap) <= 0){
            type = Type.STEADY;
            return;
        }

        BigDecimal absChange = change.abs();

        if(absChange.compareTo(steadyGap) < 0){
            priceChangeType = PriceChangeType.HOLD;
        }else{
            if(change.compareTo(BigDecimal.ZERO) > 0){
                priceChangeType = PriceChangeType.RISE;
            }else{
                priceChangeType = PriceChangeType.FALL;
            }
        }


        BigDecimal upperShadow;
        BigDecimal lowerShadow;

        final BigDecimal decimal_2 = new BigDecimal(2);

        if(change.compareTo(BigDecimal.ZERO) < 0){
            upperShadow = high.subtract(open);
            lowerShadow = close.subtract(low);
        }else{
            upperShadow = high.subtract(close);
            lowerShadow = open.subtract(low);
        }

        //위 그림자 캔들
        if(upperShadow.compareTo(lowerShadow) > 0
                //위그림자가 아래꼬리보다 크고
        && upperShadow.compareTo(absChange) > 0
               //위그림자가 몸통보다 크고
        && upperShadow.compareTo(steadyGap) > 0
                //위그림자가 보합갭 보다 크고
        ){
            if(lowerShadow.compareTo(steadyGap) < 0){
                //아래그림자가 보합갭보다 짧으면
                type = Type.UPPER_SHADOW;
                return;
            }

            BigDecimal rate = upperShadow.divide(lowerShadow, MathContext.DECIMAL128 );
            if(rate.compareTo(decimal_2) >= 0){
                //위꼬리가 아래그림자보다 2배이상 길면
                type = Type.UPPER_SHADOW;
                return;
            }
        }


        //아래그림자 캔들
        if(lowerShadow.compareTo(upperShadow) > 0
                //아래그림자가 위꼬리보다 크고
        && lowerShadow.compareTo(absChange) > 0
                //아래그림자가 몸통보다 크고
        && lowerShadow.compareTo(steadyGap) > 0
                //아래그림자가 보합보다 크고
        ){
            if(upperShadow.compareTo(steadyGap) < 0){
                //위그림자가 보합갭보다 짧으면
                type = Type.LOWER_SHADOW;
                return;
            }


            BigDecimal rate = lowerShadow.divide(upperShadow, MathContext.DECIMAL128);
            if(rate.compareTo(decimal_2) >= 2.0){
                //아래그림자가 위꼬리보다 2배이상 길면
                type = Type.LOWER_SHADOW;
                return;
            }

        }

        //위 아래 그림자캔들
        if(
                lowerShadow.compareTo(absChange) > 0
                //아래그림자가 몸통보다 길고
                && upperShadow.compareTo(absChange) > 0
                //위그림자가 몸통보다 길고
                && lowerShadow.compareTo(steadyGap) > 0
                //아래그림자가 보합길이보다 길고
                && upperShadow.compareTo(steadyGap) > 0
                // 위그림자가 보합길이보다 길고
        ){

            if(absChange.compareTo(steadyGap) < 0){
                //몸통이 보합걸이보다 작으면
                type = Type.DOJI;
                return;
            }

            BigDecimal upperRate = upperShadow.divide(absChange, MathContext.DECIMAL128);
            BigDecimal lowerRate = lowerShadow.divide(absChange, MathContext.DECIMAL128);

            if(upperRate.compareTo(decimal_2) >= 0 && lowerRate.compareTo(decimal_2) >= 0){
                // 위아래꼬리가 몸통보다 많이길면 길면
                type = Type.HIGH_WAVE;
            }else{
                // 위아래꼬리가 길면
                type = Type.SPINNING_TOPS;
            }
            return;
        }

        //유형을 정하지 못하고 이 부분까지 온경우
        //긴캔들 짧은캔들
        if(absChange.compareTo(shortGap) <= 0){
            //몸통길이가 sortGap 짧으면 짧은캔들
            type = Type.SHORT;
        }else{
            //몸통길이가 sortGap 길면 긴캔들
            type = Type.LONG;
        }
    }

    /**
     * 가격 변화 유형
     */
    private PriceChangeType priceChangeType = PriceChangeType.UNDEFINED;

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


    /**
     * 높이 얻기 (세로길이)
     * @return 높이(세로길이)
     */
    public BigDecimal getHeight() {
        return high.subtract(low);
    }

    /**
     * 변화가격 설정
     * @param change  변화가격
     */
    public void setChange(BigDecimal change) {
        this.change = change;
    }

    /**
     * 전봉가격 얻기
     * @return  전 봉 가격
     */
    public BigDecimal getPrevious() {
        if(previous == null && change != null){
            previous = open.subtract(change);
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
        previous = close.subtract(change);
    }


    /**
     * 직전 가격이 설정되어 있을때 가격변화량 및 변화율을 설정한다.
     */
    public void setChange(){
        change = close.subtract(previous);
        changeRate = change.divide(previous, MathContext.DECIMAL128);
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
    private long openTime;

    /**
     * 끝시간
     */
    private long closeTime;


    /**
     * 시작시간 얻기
     * @return long 시작시간
     */
    public long getOpenTime() {
        return openTime;
    }

    /**
     * 시작시간 설정
     * @param openTime long 시작시간
     */
    public void setOpenTime(long openTime) {
        this.openTime = openTime;
    }

    /**
     * 끝시간 얻기
     * @return long 끝시간
     */
    public long getCloseTime() {
        return closeTime;
    }

    /**
     * 끝시간 설정
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

}

