package io.runon.trading.order;

import com.seomse.commons.utils.time.Times;
import io.runon.trading.account.Account;
import io.runon.trading.exception.RequiredFieldException;

import java.math.BigDecimal;

/**
 * 분할매수
 * 시장가 주문이 아닌 경우
 * 원하는 수량이 채워지지 않을 수 있다.
 * 시간을 동일하게 나누어서 사는것 부터 구현한다.
 * 추상체로 여러 옵션을 받아서 구현할 계획이지만 단순 시간을 나눠서 사용하는 기본구현체 부터 제공한다.
 * 관련 클래스는 클래스명 메소드명이 버전이 바뀌면 변경될 수 있다.
 * @author macle
 */
public class SplitBuy {

    //종목코드
    protected String symbol = null;

    protected Account account;
    protected long beginTime = -1L;

    //종료시간은 반드시 설정되어야함
    protected long endTime = -1L;

    protected BigDecimal buyTotalCash = null;

    protected OrderCase orderCase = OrderCase.MARKET;

    protected long delayTime = Times.MINUTE_1;

    public void setOrderCase(OrderCase orderCase) {
        this.orderCase = orderCase;
    }

    public SplitBuy(){

    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setBuyTotalCash(BigDecimal buyTotalCash) {
        this.buyTotalCash = buyTotalCash;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    //호가창
    public void buy(){
        if(symbol == null){
            throw new RequiredFieldException("symbol null");
        }

        if(endTime < System.currentTimeMillis()){
            throw new RequiredFieldException("end time set error");
        }
        
        //시작 시간이 더 작으면 현제 시작시간으로 설정
        if(beginTime < System.currentTimeMillis()){
            beginTime = System.currentTimeMillis();
        }








    }

}
