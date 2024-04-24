package io.runon.trading.backtesting.account;

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

    private int buyScale = 0;

    public void setBuyScale(int buyScale) {
        this.buyScale = buyScale;
    }

    public void buyAllCash(String symbol){
        BigDecimal price = idPrice.getBuyPrice(symbol);
        BigDecimal buyQuantity = cash.divide(price,buyScale, RoundingMode.DOWN);

        if(buyQuantity.compareTo(BigDecimal.ZERO) <= 0){
            return;
        }

        buy(newHoldingQuantity(symbol,buyQuantity.stripTrailingZeros()));
    }

    public abstract T newHoldingQuantity(String id, BigDecimal quantity);
    @Override
    public void buy(T holdingQuantity) {
        BigDecimal buyPrice = holdingQuantity.getQuantity().multiply(idPrice.getBuyPrice(holdingQuantity.getId()));

        if(buyPrice.compareTo(cash) > 0 ){
            throw new NotEnoughCashException("cash: " + cash.stripTrailingZeros().toPlainString() +" buy price: " + buyPrice.stripTrailingZeros().toPlainString());
        }

        T lastHolding = HoldingMap.get(holdingQuantity.getId());
        if(lastHolding == null){
            HoldingMap.put(holdingQuantity.getId(), holdingQuantity);
        }else{
            BigDecimal num = lastHolding.getQuantity().add(holdingQuantity.getQuantity());
            lastHolding.setQuantity(num);
        }

        cash = cash.subtract(buyPrice);
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
    public BigDecimal getSellPrice(String symbol) {
        return idPrice.getSellPrice(symbol);
    }
}
