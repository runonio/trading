package io.runon.trading.exception;
/**
 * 캔들 시간 관련 오류
 * @author macle
 */
public class CandleTimeException extends RuntimeException{
    public CandleTimeException(){
        super();
    }

    public CandleTimeException(String msg){
        super(msg);
    }
}
