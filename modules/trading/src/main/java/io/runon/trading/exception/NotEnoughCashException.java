package io.runon.trading.exception;
/**
 * @author macle
 */
public class NotEnoughCashException extends RuntimeException{

    public NotEnoughCashException(){
        super();
    }

    public NotEnoughCashException(String message){
        super(message);
    }
}
