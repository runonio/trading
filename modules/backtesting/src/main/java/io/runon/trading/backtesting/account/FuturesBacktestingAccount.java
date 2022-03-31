package io.runon.trading.backtesting.account;

import io.runon.trading.account.FuturesAccount;
import io.runon.trading.account.FuturesPosition;
import io.runon.trading.account.FuturesPositionData;
import io.runon.trading.backtesting.price.symbol.SymbolPrice;
import io.runon.trading.order.Order;
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
 * 레버리지 1배고정
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

    //최소 수량
    protected final Map<String, Integer> symbolQuantityPrecision = new HashMap<>();

    protected int scale = 6;

    public void setQuantityPrecision(String symbol, Integer quantityPrecision){
        symbolQuantityPrecision.put(symbol, quantityPrecision);
    }

    public int getQuantityPrecision(String symbol){
        Integer quantityPrecision = symbolQuantityPrecision.get(symbol);
        if(quantityPrecision == null){
            symbolQuantityPrecision.put(symbol,3);
            return 3;
        }
        return quantityPrecision;
    }

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

    public void order(String symbol, Order order){
        Position position = order.getPosition();

        if(position == Position.LONG){
            BigDecimal price = symbolPrice.getBuyPrice(symbol);
            BigDecimal orderPrice = shortClose(symbol, order.getPrice(), price);
            if(orderPrice.compareTo(BigDecimal.ZERO) == 0){
                return;
            }

            if(orderPrice.compareTo(cash) > 0){
                orderPrice = cash;
            }
            buy(symbol, orderPrice.subtract(orderPrice.multiply(buyFee)), price);
        }else if (position == Position.SHORT){
            BigDecimal price = symbolPrice.getSellPrice(symbol);
            BigDecimal orderPrice = longClose(symbol, order.getPrice(), price);
            if(orderPrice.compareTo(BigDecimal.ZERO) == 0){
                return;
            }

            if(orderPrice.compareTo(cash) > 0){
                orderPrice = cash;
            }
            sell(symbol, orderPrice.subtract(orderPrice.multiply(sellFee)), price);
        }else if (position == Position.LONG_CLOSE){
            longClose(symbol, order.getPrice(), null);
        }else if (position == Position.SHORT_CLOSE){
            shortClose(symbol, order.getPrice(), null);
        }
    }

    public void buyAll(String symbol){
        //롱포지션 전체매수
        shortClose(symbol);
        buy(symbol, cash.subtract(cash.multiply(buyFee)), null);
    }

    public void buy(String symbol, BigDecimal buyCash, BigDecimal price){
        if(buyCash.compareTo(BigDecimal.ZERO) == 0){
            return;
        }

        if(price == null) {
            price = symbolPrice.getBuyPrice(symbol);
        }
        BigDecimal quantity = buyCash.divide(price, getQuantityPrecision(symbol), RoundingMode.DOWN);
        if(quantity.compareTo(BigDecimal.ZERO) == 0){
            return;
        }

        FuturesPositionData futuresPositionData = positionMap.get(symbol);
        BigDecimal tradingPrice = price.multiply(quantity);
        
        if(futuresPositionData == null){
            futuresPositionData = new FuturesPositionData();
            futuresPositionData.setSymbol(symbol);
            futuresPositionData.setPrice(price);
            futuresPositionData.setQuantity(quantity);
            futuresPositionData.setTradingPrice(tradingPrice);
            futuresPositionData.setPosition(Position.LONG);
            futuresPositionData.setLeverage(BigDecimal.ONE);
            positionMap.put(symbol, futuresPositionData);

        }else{
            //롱포지션인경우만 들어옴
            //숏포지션인경우는 들어오지 않음
            //누적수량변경
            futuresPositionData.setQuantity(futuresPositionData.getQuantity().add(quantity));
            //누적금액변경
            futuresPositionData.setTradingPrice(futuresPositionData.getTradingPrice().add(tradingPrice));
            //평단가변경
            futuresPositionData.setPrice(futuresPositionData.getTradingPrice().divide(futuresPositionData.getQuantity(),MathContext.DECIMAL128));
        }

        cash = cash.subtract(tradingPrice).subtract(tradingPrice.multiply(buyFee));

    }

    public void sellAll(String symbol){
        //숏포지션 전체매수
        longClose(symbol);
        sell(symbol, cash.subtract(cash.multiply(sellFee)), null);
    }

    public void sell(String symbol, BigDecimal sellCash, BigDecimal price){
        if(sellCash.compareTo(BigDecimal.ZERO) == 0){
            return;
        }

        if(price == null) {
            price = symbolPrice.getBuyPrice(symbol);
        }

        BigDecimal quantity = sellCash.divide(price, getQuantityPrecision(symbol), RoundingMode.DOWN);
        if(quantity.compareTo(BigDecimal.ZERO) == 0){
            return;
        }
        BigDecimal tradingPrice = price.multiply(quantity);


        FuturesPositionData futuresPositionData = positionMap.get(symbol);

        if(futuresPositionData == null){
            futuresPositionData = new FuturesPositionData();
            futuresPositionData.setSymbol(symbol);
            futuresPositionData.setPrice(price);
            futuresPositionData.setQuantity(quantity);
            futuresPositionData.setTradingPrice(tradingPrice);
            futuresPositionData.setPosition(Position.SHORT);
            futuresPositionData.setLeverage(BigDecimal.ONE);
            positionMap.put(symbol, futuresPositionData);
        }else{
            //누적수량변경
            futuresPositionData.setQuantity(futuresPositionData.getQuantity().add(quantity));
            //누적금액변경
            futuresPositionData.setTradingPrice(futuresPositionData.getTradingPrice().add(tradingPrice));
            //평단가변경
            futuresPositionData.setPrice(futuresPositionData.getTradingPrice().divide(futuresPositionData.getQuantity(),MathContext.DECIMAL128));
        }

        cash = cash.subtract(tradingPrice).subtract(tradingPrice.multiply(sellFee));
    }

    public void close(String symbol){
        FuturesPositionData futuresPositionData = positionMap.get(symbol);
        if(futuresPositionData == null){
            return ;
        }
        positionMap.remove(symbol);
        BigDecimal closePrice = closePrice(futuresPositionData);
        cash = cash.add(closePrice);
    }

    public void shortClose(String symbol){
        FuturesPositionData futuresPositionData = positionMap.get(symbol);
        if(futuresPositionData == null){
            return ;
        }
        if(futuresPositionData.getPosition() != Position.SHORT){
            return;
        }

        positionMap.remove(symbol);
        BigDecimal closePrice = closePrice(futuresPositionData);
        cash = cash.add(closePrice);
    }

    public BigDecimal shortClose(String symbol, BigDecimal orderPrice, BigDecimal price){
        FuturesPositionData futuresPositionData = positionMap.get(symbol);
        if(futuresPositionData == null){
            return orderPrice;
        }
        if(futuresPositionData.getPosition() != Position.SHORT){
            return orderPrice;
        }

        if(price == null || price.compareTo(BigDecimal.ZERO) == 0){
            price = symbolPrice.getBuyPrice(symbol);
        }

        BigDecimal quantity = orderPrice.divide(price, getQuantityPrecision(symbol), RoundingMode.DOWN);
        int compare = futuresPositionData.getQuantity().compareTo(quantity);

        if(compare == 0 || compare < 0){

            //매각대금
            positionMap.remove(symbol);
            BigDecimal closePrice = closePrice(futuresPositionData);
            cash = cash.add(closePrice);
            if(compare == 0){
                return BigDecimal.ZERO;
            }

            return orderPrice.subtract(buyPrice(price, futuresPositionData.getQuantity()));
        }else {
            //건수 만큼만 닫기
            BigDecimal positionPrice = futuresPositionData.getPrice();
            BigDecimal rate = price.subtract(positionPrice).divide(positionPrice, MathContext.DECIMAL128);
            rate = rate.multiply(new BigDecimal(-1));
            
            BigDecimal closePrice = positionPrice.multiply(quantity);
            
            futuresPositionData.setQuantity( futuresPositionData.getQuantity().subtract(quantity));
            futuresPositionData.setTradingPrice(futuresPositionData.getTradingPrice().subtract(closePrice));

            closePrice = closePrice.add(closePrice.multiply(rate));
            closePrice = closePrice.subtract(orderPrice.multiply(buyFee)); //사는것이므로 구매 수수료료
            
            cash = cash.add(closePrice);
            return BigDecimal.ZERO;
        }

    }

    public BigDecimal buyPrice(BigDecimal price, BigDecimal quantity){
        BigDecimal tradingPrice = price.multiply(quantity);
        return tradingPrice.add(tradingPrice.multiply(buyFee));
    }
    public BigDecimal sellPrice(BigDecimal price, BigDecimal quantity){
        BigDecimal tradingPrice = price.multiply(quantity);
        return tradingPrice.add(tradingPrice.multiply(sellFee));
    }

    public BigDecimal longClose(String symbol, BigDecimal orderPrice, BigDecimal price){
        FuturesPositionData futuresPositionData = positionMap.get(symbol);
        if(futuresPositionData == null){
            return orderPrice;
        }
        if(futuresPositionData.getPosition() != Position.LONG){
            return orderPrice;
        }

        if(price == null || price.compareTo(BigDecimal.ZERO) == 0){
            price = symbolPrice.getSellPrice(symbol);
        }

        BigDecimal quantity = orderPrice.divide(price, getQuantityPrecision(symbol), RoundingMode.DOWN);
        int compare = futuresPositionData.getQuantity().compareTo(quantity);

        if(compare == 0 || compare < 0){

            //매각대금
            positionMap.remove(symbol);
            BigDecimal closePrice = closePrice(futuresPositionData);
            cash = cash.add(closePrice);
            if(compare == 0){
                return BigDecimal.ZERO;
            }

            return orderPrice.subtract(sellPrice(price, futuresPositionData.getQuantity()));
        }else {
            //건수 만큼만 닫기
            BigDecimal positionPrice = futuresPositionData.getPrice();
            BigDecimal rate = price.subtract(positionPrice).divide(positionPrice, MathContext.DECIMAL128);

            BigDecimal closePrice = futuresPositionData.getPrice().multiply(quantity);

            futuresPositionData.setQuantity( futuresPositionData.getQuantity().subtract(quantity));
            futuresPositionData.setTradingPrice(futuresPositionData.getTradingPrice().subtract(closePrice));

            closePrice = closePrice.add(closePrice.multiply(rate));
            closePrice = closePrice.subtract(orderPrice.multiply(sellFee)); //사는것이므로 구매 수수료료

            cash = cash.add(closePrice);
            return BigDecimal.ZERO;
        }

    }
    public void longClose(String symbol){
        FuturesPositionData futuresPositionData = positionMap.get(symbol);
        if(futuresPositionData == null){
            return ;
        }
        if(futuresPositionData.getPosition() != Position.LONG){
            return;
        }
        positionMap.remove(symbol);
        BigDecimal closePrice = closePrice(futuresPositionData);
        cash = cash.add(closePrice);
    }

    public BigDecimal closePrice(String symbol){
        FuturesPositionData futuresPositionData = positionMap.get(symbol);
        if(futuresPositionData == null){
            return BigDecimal.ZERO;
        }
        return closePrice(futuresPositionData);
    }

    public BigDecimal closePrice(FuturesPositionData futuresPosition){
        if(futuresPosition.getPrice().compareTo(BigDecimal.ZERO) == 0){
            return BigDecimal.ZERO;
        }

        Position position = futuresPosition.getPosition();
        //평단가
        BigDecimal positionPrice = futuresPosition.getPrice();
        BigDecimal tradingPrice = futuresPosition.getTradingPrice();
        //손실율 혹은 이익율
        if(position == Position.LONG){
            
            //매매대금
            BigDecimal price = symbolPrice.getSellPrice(futuresPosition.getSymbol());
            BigDecimal rate = price.subtract(positionPrice).divide(positionPrice, MathContext.DECIMAL128);
            tradingPrice = tradingPrice.add(tradingPrice.multiply(rate));
            return tradingPrice.subtract(tradingPrice.multiply(sellFee)); //파는것이므로 판매 수수료
        }else if(position == Position.SHORT){
            BigDecimal price = symbolPrice.getBuyPrice(futuresPosition.getSymbol());
            BigDecimal rate = price.subtract(positionPrice).divide(positionPrice, MathContext.DECIMAL128);
            rate = rate.multiply(new BigDecimal(-1));
            tradingPrice = tradingPrice.add(tradingPrice.multiply(rate));
            return tradingPrice.subtract(tradingPrice.multiply(buyFee)); //사는것이므로 구매 수수료료
       }
        return BigDecimal.ZERO;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public BigDecimal getAssets() {
        BigDecimal assets = cash;
        Collection<FuturesPositionData> holdings = positionMap.values();
        for(FuturesPositionData holding: holdings){
            assets = assets.add(closePrice(holding));
        }
        return assets;
    }


    public BigDecimal getCash() {
        return cash;
    }

    @Override
    public FuturesPosition getPosition(String symbol) {
        return positionMap.get(symbol);
    }

    @Override
    public void setLeverage(String symbol, BigDecimal leverage) {
        //사용하지 않음
        FuturesPositionData futuresPosition = positionMap.get(symbol);
        if(futuresPosition == null){
            return;
        }
        futuresPosition.setLeverage(leverage);

    }

    @Override
    public BigDecimal getLeverage(String symbol) {
        //사용하지 않음
        FuturesPositionData futuresPosition = positionMap.get(symbol);
        if(futuresPosition == null){
            return BigDecimal.ZERO;
        }

        return futuresPosition.getLeverage();
    }

    @Override
    public BigDecimal getAvailableBuyPrice(String symbol) {
        BigDecimal price = cash;
        if(getSymbolPosition(symbol) == Position.SHORT){
            price = price.add(closePrice(symbol));
        }
        return price.subtract(price.multiply(buyFee));
    }

    @Override
    public BigDecimal getAvailableSellPrice(String symbol) {
        BigDecimal price = cash;
        if(getSymbolPosition(symbol) == Position.LONG){
            price = price.add(closePrice(symbol));
        }
        return price.subtract(price.multiply(buyFee));
    }

    public Position getSymbolPosition(String symbol){
        FuturesPosition futuresPosition = positionMap.get(symbol);
        if(futuresPosition == null){
            return Position.NONE;
        }

        return futuresPosition.getPosition();
    }
}