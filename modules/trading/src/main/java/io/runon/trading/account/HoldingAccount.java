package io.runon.trading.account;

import io.runon.trading.HoldingQuantity;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 현금과 종목 및 보유수량
 * @author macle
 */


public abstract class HoldingAccount<T extends HoldingQuantity> implements Account{

    protected BigDecimal cash = BigDecimal.ZERO;

    protected Map<String, T> HoldingMap = new HashMap<>();

    protected final String id;


    public HoldingAccount(String id){
        this.id = id;
    }
    public HoldingAccount(){
        this.id = "backtesting";
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public BigDecimal getAssets() {
        if(HoldingMap.isEmpty()){
            return cash;
        }

        BigDecimal sum = cash;

        Collection<T> values = HoldingMap.values();

        for(T holding : values){
            BigDecimal sellPrice = getSellPrice(holding.getId());

            sum = sum.add(sellPrice.multiply(holding.getQuantity()));
        }

        return sum.stripTrailingZeros();
    }

    @Override
    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }
    public void addCash(BigDecimal cash){
        this.cash = this.cash.add(cash);
    }


    public abstract void buy(T number);


    public abstract void sell(T number);

    public abstract BigDecimal getSellPrice(String symbol);


}
