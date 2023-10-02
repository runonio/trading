package io.runon.trading.exception;
/**
 * @author macle
 */
public class SymbolNotFoundException extends RuntimeException{
    public SymbolNotFoundException(String symbol){
        super(String.format("symbol [%s] not found.", symbol));
    }
}
