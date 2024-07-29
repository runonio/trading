package io.runon.trading.exception;
/**
 * 트레이딩 api를 이용하다 발생하는 예외
 * 상세 예외로 상속받아 구현하는 걸 추천함.
 * @author macle
 */
public class TradingApiException  extends RuntimeException{

    /**
     * 생성자
     */
    public TradingApiException(){
        super();
    }

    /**
     * 생성자
     * @param e 예외
     */
    public TradingApiException(Exception e){
        super(e);
    }

    /**
     * 생성자
     * @param message exception message
     */
    public TradingApiException(String message){
        super(message);
    }
}
