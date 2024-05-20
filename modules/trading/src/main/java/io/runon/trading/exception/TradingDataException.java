package io.runon.trading.exception;
/**
 * 트레이딩 데이터 관련 예외
 * @author macle
 */
public class TradingDataException extends RuntimeException{
    public TradingDataException(){
        super();
    }

    public TradingDataException(String msg){
        super(msg);
    }
}