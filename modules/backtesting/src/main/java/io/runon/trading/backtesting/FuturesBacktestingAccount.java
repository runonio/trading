package io.runon.trading.backtesting;

import io.runon.trading.SymbolPrice;
import io.runon.trading.account.FuturesAccount;
import io.runon.trading.account.FuturesPositionData;
import io.runon.trading.strategy.Position;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 선물백테스팅 거래 계좌
 * @author macle
 */
@Slf4j
public class FuturesBacktestingAccount implements FuturesAccount {

    protected BigDecimal cash = BigDecimal.ZERO;

    protected final String id;
    public FuturesBacktestingAccount(String id){
        this.id = id;
    }

    public FuturesBacktestingAccount(){
        this.id = "backtesting";
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public void addCash(BigDecimal cash){
        this.cash = this.cash.add(cash);
    }

    protected final Object lock = new Object();

    protected final Map<String, FuturesPositionData> positionMap = new HashMap<>();
    protected SymbolPrice symbolPrice;
    public void setSymbolPrice(SymbolPrice symbolPrice) {
        this.symbolPrice = symbolPrice;
    }

    //기준 1달러
    protected BigDecimal minPrice = BigDecimal.ONE;
    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    protected int scale = 6;

    /**
     * AmountType DECIMAL 일떄만 사용
     * @param scale default 6
     */
    public void setScale(int scale) {
        this.scale = scale;
    }

    protected int priceScale = 2;

    public void setPriceScale(int priceScale) {
        this.priceScale = priceScale;
    }

    //구매수수료
    protected BigDecimal buyFee = new BigDecimal("0.0004");

    //판매수수료
    protected BigDecimal sellFee = new BigDecimal("0.0004");

    /**
     * 구매 수수료 설정
     * @param buyFee 구매수수료
     */
    public void setBuyFee(BigDecimal buyFee) {
        this.buyFee = buyFee;
    }

    /**
     * 판매수수료 설정
     * @param sellFee 판매수수료
     */
    public void setSellFee(BigDecimal sellFee) {
        this.sellFee = sellFee;
    }

    /**
     * 수수료 설정
     * 구매수수료와 판매수수료가 같은경우
     * 다른경우는 각각 설정
     * @param fee 수수료
     */
    public void setFee(BigDecimal fee){
        this.buyFee = fee;
        this.sellFee = fee;
    }

    /**
     *
     * @param subtractRate 제외비율
     * @param symbol 심볼
     * @param position 롱숏 표지션
     * @param leverage 레버리지
     * @return 레버리지를 포함한 포지션, 캐시가 부족하면 null
     */
    public FuturesPositionData getBuyQuantity(BigDecimal subtractRate, String symbol, Position position, BigDecimal leverage){

        if(this.cash.subtract(this.cash.multiply(subtractRate)).compareTo(minPrice) < 0){
            return null;
        }

        BigDecimal price = symbolPrice.getBuyPrice(symbol);

        BigDecimal cash = this.cash.multiply(leverage).multiply(subtractRate);
        BigDecimal size = cash.divide(price, scale, RoundingMode.DOWN);


        if(price.multiply(size).divide(leverage, priceScale, RoundingMode.DOWN).compareTo(minPrice) < 0){
            return null;
        }


        FuturesPositionData futuresPosition = new FuturesPositionData();

        futuresPosition.setSymbol(symbol);
        futuresPosition.setPrice(price);
        futuresPosition.setSize(size);
        futuresPosition.setLeverage(leverage);
        futuresPosition.setPosition(position);

        return futuresPosition;
    }

    /**
     * 종목과 방향성 추가
     * 포지션 율 변경 같은 부분은 신경쓰지않고 전량매수 전량매도로만 작업
     * 추후에 업데이트
     * @param futuresPosition 포지션
     */
    public void buy(FuturesPositionData futuresPosition){
        if(futuresPosition == null){
            return;
        }

        FuturesPositionData last;
        synchronized (lock){
            String key = futuresPosition.getSymbol() + "," + futuresPosition.getPosition();
            //기존에 다른값이 있는경우
            last = positionMap.get(key);
            if(last != null){
                //매도 (초기에는 중복 지원안함)
//                log.error("duplication key: " + key);
                return;
            }
            positionMap.put(key, futuresPosition);

            BigDecimal cash = futuresPosition.getPrice().multiply(futuresPosition.getSize());
            BigDecimal fee = getBuyFee(futuresPosition, futuresPosition.getPrice(), futuresPosition.getSize());

            cash = cash.divide(futuresPosition.getLeverage(), MathContext.DECIMAL128).add(fee);
            
            this.cash = this.cash.subtract(cash);

        }
    }

    public void sell(String key){
        sell(positionMap.get(key));
    }

    public void sell(String symbol, Position position){
        sell(positionMap.get(symbol+ "," + position));
    }

    public void sell(FuturesPositionData futuresPosition){
        if(futuresPosition == null){
            return;
        }

        BigDecimal c = getSellPrice(futuresPosition);
        synchronized (lock){
            if(c.compareTo(BigDecimal.ZERO) > 0) {
                //청산당하지 않았으면
                cash = cash.add(c);
            }
            positionMap.remove(futuresPosition.getSymbol() + "," + futuresPosition.getPosition());
        }
    }

    //매각대금 계산하기
    public BigDecimal getSellPrice(FuturesPositionData futuresPosition){
        BigDecimal price = symbolPrice.getSellPrice(futuresPosition.getSymbol());
        BigDecimal gap = price.subtract(futuresPosition.getPrice());
        BigDecimal rate;

        if(futuresPosition.getPosition() ==  Position.LONG){
            rate = gap.divide(futuresPosition.getPrice(), MathContext.DECIMAL128);

        }else if(futuresPosition.getPosition() ==  Position.SHORT){
            rate = gap.divide(futuresPosition.getPrice(), MathContext.DECIMAL128).multiply(new BigDecimal(-1));

        }else{
            throw new IllegalArgumentException("Position LONG or SHORT: " + futuresPosition.getPosition());
        }

        //순이익율
        rate = rate.multiply(futuresPosition.getLeverage());

        //구매대금
        BigDecimal cash = futuresPosition.getSize().multiply(futuresPosition.getPrice()).divide(futuresPosition.getLeverage(), MathContext.DECIMAL128);

        BigDecimal change = cash.multiply(rate);
        BigDecimal fee = getSellFee(futuresPosition, price, futuresPosition.getSize());

        cash = cash.add(change).subtract(fee);
        return cash;
    }

    /**
     * 매수 수수료
     * 구현된 내용은 종목별로 수수료가 다른경우는 구현되어 있지않음
     * 종목별로 수수료가 다른경우 Override 해서 구현
     * @param holding 종목별 수수료가 다른경우 아래 메소드를 오버라이딩 해서 구현 (종목 정보를 얻기위한 용도)
     * @param price 가격
     * @param volume 거래량
     * @return 수수료
     */
    public BigDecimal getBuyFee(FuturesPositionData holding, BigDecimal price, BigDecimal volume){
        return price.multiply(volume).multiply(buyFee);
    }

    /**
     * 매도 수수료
     * 구현된 내용은 종목별로 수수료가 다른경우는 구현되어 있지않음
     * 종목별로 수수료가 다른경우 Override 해서 구현
     * @param holding 종목별 수수료가 다른경우 아래 메소드를 오버라이딩 해서 구현 (종목 정보를 얻기위한 용도)
     * @param price 가격
     * @param volume 거래량
     * @return 수수료
     */
    public BigDecimal getSellFee(FuturesPositionData holding, BigDecimal price, BigDecimal volume){
        return  price.multiply(volume).multiply(sellFee);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public BigDecimal getAssets() {
        synchronized (lock){
            BigDecimal assets = cash;
            Collection<FuturesPositionData> holdings = positionMap.values();
            for(FuturesPositionData holding: holdings){
                assets = assets.add(getSellPrice(holding));
            }
            return assets;
        }
    }

    public BigDecimal getCash() {
        return cash;
    }

    @Override
    public FuturesPositionData getPosition(String symbol) {
        return positionMap.get(symbol);
    }

    @Override
    public void setLeverage(String symbol, BigDecimal leverage) {
        FuturesPositionData futuresPosition = positionMap.get(symbol);
        if(futuresPosition == null){
            return;
        }
        futuresPosition.setLeverage(leverage);

    }

    @Override
    public BigDecimal getLeverage(String symbol) {
        FuturesPositionData futuresPosition = positionMap.get(symbol);
        if(futuresPosition == null){
            return null;
        }

        return futuresPosition.getLeverage();
    }
}
