package io.runon.trading.exception;
/**
 * 필수 변수가 세팅되어 있지 않을때 예외
 * @author macle
 */
public class RequiredFieldException extends RuntimeException {

    public RequiredFieldException(String message) {
        super(message);
    }

    public RequiredFieldException(Exception e){
        super(e);
    }

    public RequiredFieldException() {
        super();
    }


}
