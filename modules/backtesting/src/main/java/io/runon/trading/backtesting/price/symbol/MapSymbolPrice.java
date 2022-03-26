package io.runon.trading.backtesting.price.symbol;

import io.runon.trading.Price;

import java.util.HashMap;
import java.util.Map;

/**
 * @author macle
 */
public abstract class MapSymbolPrice<E extends Price> implements SymbolPrice{

    protected final Map<String, E> priceMap = new HashMap<>();

    

}
