package io.runon.trading.backtesting.account;

import io.runon.trading.account.SymbolAccount;
import io.runon.trading.backtesting.price.symbol.SymbolPrice;
import io.runon.trading.exception.NotEnoughCashException;
import io.runon.trading.symbol.SymbolNumber;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 선물백테스팅 거래 계좌
 * 레버리지 1배고정
 * @author macle
 */
public abstract class BacktestingSymbolAccount<T extends SymbolNumber> extends SymbolAccount<T> {

    private final SymbolPrice symbolPrice ;

    public BacktestingSymbolAccount(SymbolPrice symbolPrice){
        super();
        this.symbolPrice = symbolPrice;
    }

    public BacktestingSymbolAccount(SymbolPrice symbolPrice, String id){
        super(id);
        this.symbolPrice = symbolPrice;
    }

    private int buyScale = 0;

    public void setBuyScale(int buyScale) {
        this.buyScale = buyScale;
    }

    public void buyAllCash(String symbol){
        BigDecimal price = symbolPrice.getBuyPrice(symbol);
        BigDecimal buyCount = cash.divide(price,buyScale, RoundingMode.DOWN);

        if(buyCount.compareTo(BigDecimal.ZERO) <= 0){
            return;
        }

        buy(newSymbolNumber(symbol,buyCount.stripTrailingZeros()));
    }

    public abstract T newSymbolNumber(String symbol, BigDecimal number);
    @Override
    public void buy(T symbolNumber) {
        BigDecimal buyPrice = symbolNumber.getNumber().multiply(symbolPrice.getBuyPrice(symbolNumber.getSymbol()));

        if(buyPrice.compareTo(cash) > 0 ){
            throw new NotEnoughCashException("cash: " + cash.stripTrailingZeros().toPlainString() +" buy price: " + buyPrice.stripTrailingZeros().toPlainString());
        }

        T lastSymbolNumber = symbolMap.get(symbolNumber.getSymbol());
        if(lastSymbolNumber == null){
            symbolMap.put(symbolNumber.getSymbol(), symbolNumber);
        }else{
            BigDecimal num = lastSymbolNumber.getNumber().add(symbolNumber.getNumber());
            lastSymbolNumber.setNumber(num);
        }

        cash = cash.subtract(buyPrice);
    }

    public void sellAll(String symbol){
        SymbolNumber lastSymbolNumber = symbolMap.remove(symbol);
        if(lastSymbolNumber == null){
            return;
        }

        BigDecimal sellPrice = symbolPrice.getSellPrice(symbol);
        cash = cash.add(sellPrice.multiply(lastSymbolNumber.getNumber()));
    }

    @Override
    public void sell(T symbolNumber) {
        BigDecimal sellPrice = symbolPrice.getSellPrice(symbolNumber.getSymbol());

        SymbolNumber lastSymbolNumber = symbolMap.get(symbolNumber.getSymbol());
        if(lastSymbolNumber == null){
            return;
        }

        BigDecimal sellCount = symbolNumber.getNumber();

        if(sellCount.compareTo(lastSymbolNumber.getNumber()) >= 0){
            sellAll(symbolNumber.getSymbol());
            return;
        }

        BigDecimal holdingCount = lastSymbolNumber.getNumber();
        holdingCount = holdingCount.subtract(sellCount);
        lastSymbolNumber.setNumber(holdingCount);
        cash = cash.add(sellPrice.multiply(sellCount));
    }

    @Override
    public BigDecimal getSellPrice(String symbol) {
        return symbolPrice.getSellPrice(symbol);
    }
}
