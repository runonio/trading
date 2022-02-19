package io.runon.trading.account;

import io.runon.trading.AmountType;
import io.runon.trading.SymbolPrice;
import io.runon.trading.strategy.Position;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
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

    protected final String id;
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

    //기준 1달러
    protected BigDecimal minPrice = BigDecimal.ONE;
    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    protected AmountType amountType = AmountType.DECIMAL;

    public void setAmountType(AmountType amountType) {
        this.amountType = amountType;
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
     *
     * @param subtractRate 제외비율
     * @param symbol 심볼
     * @param position 롱숏 표지션
     * @param leverage 레버리지
     * @return 레버리지를 포함한 포지션, 캐시가 부족하면 null
     */
    public FuturesHolding getBuyAmount(BigDecimal subtractRate, String symbol, Position position, BigDecimal leverage){

        if(this.cash.subtract(this.cash.multiply(subtractRate)).compareTo(minPrice) < 0){
            return null;
        }

        BigDecimal price = symbolPrice.getPrice(symbol);

        BigDecimal cash = this.cash.multiply(leverage).multiply(subtractRate);
        BigDecimal amount;
        if(amountType == AmountType.INTEGER){
            amount = cash.divide(price, 0, RoundingMode.DOWN);
        }else{
            //AmountType.DECIMAL
            amount = cash.divide(price, scale, RoundingMode.DOWN);
        }

        if(price.multiply(amount).divide(leverage, priceScale, RoundingMode.DOWN).compareTo(minPrice) < 0){
            return null;
        }


        FuturesHolding futuresHolding = new FuturesHolding();
        futuresHolding.symbol = symbol;
        futuresHolding.price = price;
        futuresHolding.amount = amount;
        futuresHolding.leverage = leverage;
        futuresHolding.position = position;

//        System.out.println(futuresHolding);
        return futuresHolding;
    }

    /**
     * 종목과 방향성 추가
     * 포지션 율 변경 같은 부분은 신경쓰지않고 전량매수 전량매도로만 작업
     * 추후에 업데이트
     * @param futuresHolding 포지션
     */
    public void buy(FuturesHolding futuresHolding){
        if(futuresHolding == null){
            return;
        }

        FuturesHolding last;
        synchronized (lock){
            String key = futuresHolding.symbol + "," + futuresHolding.position;
            //기존에 다른값이 있는경우
            last = holdingMap.get(key);
            if(last != null){
                //매도 (초기에는 중복 지원안함)
//                log.error("duplication key: " + key);
                return;
            }
            holdingMap.put(key, futuresHolding);

            BigDecimal cash = futuresHolding.price.multiply(futuresHolding.amount);
            BigDecimal fee = getBuyFee(futuresHolding, futuresHolding.price, futuresHolding.amount);

            cash = cash.divide(futuresHolding.leverage, MathContext.DECIMAL128).add(fee);



            this.cash = this.cash.subtract(cash);

        }

//        if(last != null){
//            BigDecimal c = getSellPrice(futuresHolding);
//            synchronized (lock){
//                if(c.compareTo(BigDecimal.ZERO) > 0) {
//                    //청산당하지 않았으면
//                    this.cash = this.cash.add(c);
//                }
//            }
//        }
    }


    public void sell(String key){
        sell(holdingMap.get(key));
    }

    public void sell(String symbol, Position position){
        sell(holdingMap.get(symbol+ "," + position));
    }

    public void sell(FuturesHolding futuresHolding){
        if(futuresHolding == null){
            return;
        }

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

        //순이익율
        rate = rate.multiply(futuresHolding.leverage);

        //구매대금
        BigDecimal cash = futuresHolding.getAmount().multiply(futuresHolding.getPrice()).divide(futuresHolding.leverage, MathContext.DECIMAL128);

        BigDecimal change = cash.multiply(rate);
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

    public BigDecimal getCash() {
        return cash;
    }
}
