package io.runon.trading.account;

import io.runon.trading.Trade;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 현뮬 계좌
 * @author macle
 */
public abstract class SpotAccount implements Account{

    //현금
    protected BigDecimal cash = BigDecimal.ZERO;
    //보유종목
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

    public abstract BigDecimal getBuyFee(SpotHolding holding, BigDecimal price, BigDecimal volume);
    public abstract BigDecimal getSellFee(SpotHolding holding, BigDecimal price, BigDecimal volume);


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
