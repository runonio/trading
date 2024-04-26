package io.runon.trading.backtesting.account;

import io.runon.trading.CashQuantity;
import io.runon.trading.HoldingQuantity;
import io.runon.trading.account.HoldingAccount;
import io.runon.trading.backtesting.price.IdPrice;
import io.runon.trading.exception.NotEnoughCashException;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 보유종목 벡테스팅용 계좌
 * 관련 구현체에선느 SymbolPrice 에서 수수료, 세금 및 슬리피지가 포함된 가격을 돌려줘야한다.
 * @author macle
 */
public abstract class BacktestingHoldingAccount<T extends HoldingQuantity> extends HoldingAccount<T> {

    protected IdPrice idPrice;

    public BacktestingHoldingAccount(){
        super();

    }

    public BacktestingHoldingAccount( String id){
        super(id);
    }

    public void setIdPrice(IdPrice idPrice) {
        this.idPrice = idPrice;
    }

    private int tradingScale = 0;

    public void setTradingScale(int tradingScale) {
        this.tradingScale = tradingScale;
    }

    public void buyAllCash(String id){
        BigDecimal price = idPrice.getBuyPrice(id);
        BigDecimal buyQuantity = cash.divide(price, tradingScale, RoundingMode.DOWN);

        if(buyQuantity.compareTo(BigDecimal.ZERO) <= 0){
            return;
        }

        buy(newHoldingQuantity(id,buyQuantity.stripTrailingZeros()));
    }

    public abstract T newHoldingQuantity(String id, BigDecimal quantity);

    public void buy(String id, BigDecimal quantity){
        buy(newHoldingQuantity(id, quantity));
    }
    public void sell(String id, BigDecimal quantity){
        sell(newHoldingQuantity(id, quantity));
    }

    @Override
    public void buy(T holdingQuantity) {
        BigDecimal buyCash = holdingQuantity.getQuantity().multiply(idPrice.getBuyPrice(holdingQuantity.getId()));

        if(buyCash.compareTo(cash) > 0 ){
            throw new NotEnoughCashException("cash: " + cash.stripTrailingZeros().toPlainString() +" buy cash: " + buyCash.stripTrailingZeros().toPlainString());
        }

        T lastHolding = HoldingMap.get(holdingQuantity.getId());
        if(lastHolding == null){
            HoldingMap.put(holdingQuantity.getId(), holdingQuantity);
        }else{
            BigDecimal num = lastHolding.getQuantity().add(holdingQuantity.getQuantity());
            lastHolding.setQuantity(num);
        }

        cash = cash.subtract(buyCash);
    }

    public void sellAll(String id){
        T lastHolding = HoldingMap.remove(id);
        if(lastHolding == null){
            return;
        }

        BigDecimal sellPrice = idPrice.getSellPrice(id);
        cash = cash.add(sellPrice.multiply(lastHolding.getQuantity()));
    }

    @Override
    public void sell(T holdingQuantity) {
        BigDecimal sellPrice = idPrice.getSellPrice(holdingQuantity.getId());

        T lastHolding = HoldingMap.get(holdingQuantity.getId());
        if(lastHolding == null){
            return;
        }

        BigDecimal sellQuantity = holdingQuantity.getQuantity();

        if(sellQuantity.compareTo(lastHolding.getQuantity()) >= 0){
            sellAll(holdingQuantity.getId());
            return;
        }

        BigDecimal quantity = lastHolding.getQuantity();
        quantity = quantity.subtract(sellQuantity);
        lastHolding.setQuantity(quantity);
        cash = cash.add(sellPrice.multiply(sellQuantity));
    }

    @Override
    public BigDecimal getSellPrice(String id) {
        return idPrice.getSellPrice(id);
    }

    //목적 수량과 현금으로 살수 잇는 최대 수량
    public CashQuantity getCashQuantity(String id, BigDecimal quantity, BigDecimal cash){
        BigDecimal price = idPrice.getBuyPrice(id);
        BigDecimal maxQuantity = cash.divide(price, tradingScale, RoundingMode.DOWN);

        if(maxQuantity.compareTo(quantity) > 0){
            return new CashQuantity(maxQuantity.multiply(price), maxQuantity);
        }

        return new CashQuantity(quantity.multiply(price), quantity);

    }
}
