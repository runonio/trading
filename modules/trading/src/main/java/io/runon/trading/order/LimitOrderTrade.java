package io.runon.trading.order;

/**
 * 지정가 주문 매매 정보
 * @author macle
 */
public interface LimitOrderTrade {

    //주문 아이디 얻기
    String getOrderId();


    /**
     * 주문 시간 얻기
     * @return 주문시간
     */
    long getOrderTime();



}
