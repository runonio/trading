package io.runon.trading.account;

import io.runon.trading.SymbolPrice;
import io.runon.trading.Trade;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 현뮬 계좌
 * @author macle
 */
public class SpotAccount implements Account{

    //현금
    protected BigDecimal cash = BigDecimal.ZERO;
    //보유종목


    //국내 주식 2022 기준 구매수수료
    protected BigDecimal buyFee = new BigDecimal("0.00245");

    //판매수수료
    protected BigDecimal sellFee = new BigDecimal("0.00015");


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

    protected final Map<String, SpotHolding> holdingMap = new HashMap<>();

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public void addCash(BigDecimal cash){
        this.cash = this.cash.add(cash);
    }

    protected final Object lock = new Object();

    private final String id;
    public SpotAccount(String id){
        this.id = id;
    }

    public void trade(String symbol, Trade trade){
        SpotHolding holding;
        synchronized (lock){
            holding = holdingMap.get(symbol);
            if(holding == null){
                holding = new SpotHolding();
                holdingMap.put(symbol, holding);
            }
            if(trade.getType() == Trade.Type.BUY){
                //구매대금
                BigDecimal buyCash = trade.getPrice().multiply(trade.getVolume()).add(getBuyFee(holding, trade.getPrice(), trade.getVolume()));
                cash = cash.subtract(buyCash);
            }else{
                //판매대금
                BigDecimal sellCash = trade.getPrice().multiply(trade.getVolume()).subtract(getSellFee(holding, trade.getPrice(), trade.getVolume()));
                cash = cash.add(sellCash);
            }
        }
        holding.trade(trade);

        if(holding.amount.compareTo(BigDecimal.ZERO) <= 0){
            synchronized (lock){
                holdingMap.remove(symbol);
            }
        }
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
    public BigDecimal getBuyFee(SpotHolding holding, BigDecimal price, BigDecimal volume){
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
    public BigDecimal getSellFee(SpotHolding holding, BigDecimal price, BigDecimal volume){
        return  price.multiply(volume).multiply(sellFee);
    }


    protected SymbolPrice symbolPrice;

    public void setSymbolPrice(SymbolPrice symbolPrice) {
        this.symbolPrice = symbolPrice;
    }

    /**
     * 계좌 자산
     * @return cash + holdings
     */
    @Override
    public BigDecimal getAssets(){
        synchronized (lock) {
            BigDecimal assets = cash;

            Collection<SpotHolding> holdings =  holdingMap.values();
            for(SpotHolding holding: holdings){
                BigDecimal price = symbolPrice.getPrice(holding.getSymbol());
                assets = assets.add(holding.getAmount().multiply(price).subtract(getSellFee(holding, price, holding.getAmount())));
            }

            return assets;
        }
    }

    @Override
    public String getId() {
        return id;
    }

    public BigDecimal getCash() {
        return cash;
    }
}
