package io.runon.trading.account;

import io.runon.trading.symbol.SymbolNumber;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 현금과 종목 및 보유수량
 * @author macle
 */


public abstract class SymbolAccount<T extends SymbolNumber> implements Account{

    protected BigDecimal cash = BigDecimal.ZERO;

    protected Map<String, T> symbolMap = new HashMap<>();

    protected final String id;


    public SymbolAccount(String id){
        this.id = id;
    }
    public SymbolAccount(){
        this.id = "backtesting";
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public BigDecimal getAssets() {
        if(symbolMap.isEmpty()){
            return cash;
        }

        BigDecimal sum = cash;

        Collection<T> values = symbolMap.values();

        for(SymbolNumber holding : values){
            BigDecimal sellPrice = getSellPrice(holding.getSymbol());

            sum = sum.add(sellPrice.multiply(holding.getNumber()));
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
