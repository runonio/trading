package io.runon.trading.exception;
/**
 * @author macle
 */
public class IdNotFoundException  extends RuntimeException{
    public IdNotFoundException(String id){
        super(String.format("id [%s] not found.", id));
    }
}
