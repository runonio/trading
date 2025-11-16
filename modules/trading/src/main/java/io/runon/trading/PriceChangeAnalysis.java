
package io.runon.trading;

/**
 * 매수, 매도 판단할때 사용함
 * 가격변화 분석 유형
 * @author macle
 */
public interface PriceChangeAnalysis {


    /**
     * 가격변화 예측유형
     * @return PriceChangeType 가격 변화 예층 유형( 상승, 하락, 보합)
     */
    PriceChangeType getPriceChangeType();


}
