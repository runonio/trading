package io.runon.trading.technical.analysis.indicators.volume;

import io.runon.trading.TimeNumber;

/**
 * 헤릭정산지수
 * Herrick Payoff Index
 *
 * HPI(Herrick Payoff Index)는 John Herrick이개발한 지표로 매집과 분산을 파악하는 데 유용하다.
 * 옵션 등 미결제약정이 있는 종목에서 사용되는 지표로 가격, 거래량, 미결제약정을 추적하여 추세의 유효성을 확인하고 추세 반전을 포착하는 기능을 한다.HPI는 당일 고가, 당일 저가, 거래량, 미결제 약정을 이용하며 최소 3주의 데이터가 있어야 유용한 값을 도출할 수 있다.
 *
 * mystorage1.tistory.com/578
 *
 * 블로그 내용을 보면 헤릭정산지수 방식이 책 내용이랑은 조금 변경된 부분이 있다.
 * 투자심리법칙의 수식과의 차이는 미결제약정의 최대값이 아닌 최소값으로 나눠주는 것, 구하는 수식에서 배율 인수와 100000으로 나눠주는 행위가 없어진 것이다.엘더 박사의 책에서는 HPI가 지금보다 낮은 값들을 형성하는데 그 계산과 다른 점은 예전보다 거래량이 많고 미결제약정이 커서 단위자체가 켜졌기 때문이다.
 *
 *
 * HPI = (K(1) + (K - K(1)) * S)/100000
 * K = (CM * V * (M-M(1)) * (1±(2*I)/G);
 * S = 배율 인수이동평균의 평활화와 유사하다. 배율 인수가 10이면 10기간 이동 평균과 유사하다.
 * CM = 1센트 움직이는 데 대한 가치헤릭은 상품의 1센트당 이동 가치를 100으로 권장, 은의 경우 50
 * V = 거래량 volume
 * M = 중간값 (H+L)/2
 * ± =  M > M(1) -> +
 *      M < M(1) -> -
 * I = 당일 미결제약정 - 전일 미결제약정 OI-OI(1)
 * G = 당일 미결제약정 과 전일 미결제약정 중 큰 값 max(OI,OI(1))
 *
 * 붙여넣기 수식
 * M = (H+L)/2;
 * K = (CM * V * (M-M(1)))*(if(M>M(1),1+((abs(OI-OI(1))*2)/max(OI,OI(1))),1-((abs(OI-OI(1))*2)/max(OI,OI(1)))));
 * (K(1)+(K-K(1))*s)/100000
 *
 *
 *
 * @author macle
 */
public class Hpi {

    public TimeNumber get(){



        return null;
    }


}
