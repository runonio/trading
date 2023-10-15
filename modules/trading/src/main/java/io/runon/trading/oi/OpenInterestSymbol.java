package io.runon.trading.oi;
/**
 * 미체결 약정 개별 심볼 데이터
 * @author macle
 */
public interface OpenInterestSymbol {

    /**
     * 미체결 약정 얻기
     * 시간에 가장 근접한 데이터 얻기
     * 벡테스팅용 
     * @param time 시간
     * @return 미체결 약정 정보 데이터
     */
    OpenInterest getData(long time);

    /**
     * 최근 미체결 약정 얻기
     * @return 미체결 약정 정보 데이터
     */
    OpenInterest getData();
    
}
