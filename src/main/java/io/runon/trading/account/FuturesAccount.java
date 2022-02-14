package io.runon.trading.account;

import io.runon.trading.Position;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 선물거래 계좌
 * @author macle
 */
@Slf4j
public abstract class FuturesAccount implements Account{

    protected BigDecimal cash = BigDecimal.ZERO;

    private final String id;
    public FuturesAccount(String id){
        this.id = id;
    }
    
    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public void addCash(BigDecimal cash){
        this.cash = this.cash.add(cash);
    }

    protected final Object lock = new Object();

    protected final Map<String, FuturesHolding> holdingMap = new HashMap<>();

    protected SymbolPrice symbolPrice;

    public void setSymbolPrice(SymbolPrice symbolPrice) {
        this.symbolPrice = symbolPrice;
    }
    /**
     * 종목과 방향성 추가
     * 포지션 율 변경 같은 부분은 신경쓰지않고 전량매수 전량매도로만 작업
     * 추후에 업데이트
     * @param futuresHolding 포지션
     */
    public void buy(FuturesHolding futuresHolding){
        FuturesHolding last;
        synchronized (lock){
            String key = futuresHolding.symbol + "," + futuresHolding.position;
            //기존에 다른값이 있는경우
            last = holdingMap.get(key);
            if(last != null){
                //매도 (초기에는 중복 지원안함)
                log.error("duplication key: " + key);

            }
            holdingMap.put(key, futuresHolding);

            BigDecimal cash = futuresHolding.price.multiply(futuresHolding.amount);
            BigDecimal fee = getBuyFee(futuresHolding, futuresHolding.price, futuresHolding.price);

            cash = cash.divide(futuresHolding.leverage, MathContext.DECIMAL128).add(fee);

            this.cash = this.cash.subtract(cash);

        }

        if(last != null){
            BigDecimal c = getSellPrice(futuresHolding);
            synchronized (lock){
                if(c.compareTo(BigDecimal.ZERO) > 0) {
                    //청산당하지 않았으면
                    this.cash = this.cash.add(c);
                }
            }
        }
    }

    public void sell(FuturesHolding futuresHolding){

        BigDecimal c = getSellPrice(futuresHolding);
        synchronized (lock){
            if(c.compareTo(BigDecimal.ZERO) > 0) {
                //청산당하지 않았으면
                cash = cash.add(c);
            }
            holdingMap.remove(futuresHolding.symbol + "," + futuresHolding.position);
        }
    }

    //매각대금 계산하기
    public BigDecimal getSellPrice(FuturesHolding futuresHolding){
        BigDecimal price = symbolPrice.getPrice(futuresHolding.getSymbol());
        BigDecimal gap = price.subtract(futuresHolding.getPrice());
        BigDecimal rate;

        if(futuresHolding.position ==  Position.LONG){
            rate = gap.divide(futuresHolding.getPrice(), MathContext.DECIMAL128);

        }else if(futuresHolding.position ==  Position.SHORT){
            rate = gap.divide(futuresHolding.getPrice(), MathContext.DECIMAL128).multiply(new BigDecimal(-1));

        }else{
            throw new IllegalArgumentException("Position LONG or SHORT: " + futuresHolding.position);
        }

        BigDecimal cash = futuresHolding.getAmount().multiply(futuresHolding.getPrice());
        BigDecimal change = cash.multiply(rate);

        cash = cash.divide(futuresHolding.leverage, MathContext.DECIMAL128);

        BigDecimal fee = getSellFee(futuresHolding, price, futuresHolding.amount);
        cash = cash.add(change).subtract(fee);
        return cash;
    }

    public abstract BigDecimal getBuyFee(FuturesHolding holding, BigDecimal price, BigDecimal volume);
    public abstract BigDecimal getSellFee(FuturesHolding holding, BigDecimal price, BigDecimal volume);


    @Override
    public String getId() {
        return id;
    }

    @Override
    public BigDecimal getAssets() {
        synchronized (lock){
            BigDecimal assets = cash;
            Collection<FuturesHolding> holdings = holdingMap.values();
            for(FuturesHolding holding: holdings){
                assets = assets.add(getSellPrice(holding));
            }
            return assets;
        }
    }
}
