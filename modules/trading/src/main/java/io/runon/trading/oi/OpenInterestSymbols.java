package io.runon.trading.oi;

/**
 * 미체결 약정 저장소
 * @author macle
 */
public interface OpenInterestSymbols {

    /**
     * 미체결 약정 얻기
     * 전송한 symbol 에 한해서 얻는다.
     * 자산 시장에서 symbol = id
     * 유니크한 성격을 가진다.
     * @param symbol 종목 기호 (유니크한값)
     * @param time 시간 얻고자 하는 가장 가까운시간
     * @return 미체결약정 정보
     */
    OpenInterest getOpenInterest(String symbol, long time);

    /**
     * 미체결 약정 얻기
     * 전송한 symbols 의 합을 얻는다
     * @param symbols 종목기호 (아이디) 배열
     * @param time 시간 얻고자 하는 가장 가까운시간
     * @return 미체결약정 정보
     */
    OpenInterest getOpenInterest(String [] symbols, long time);

    /**
     * 최종 미체결 약정 얻기
     * @param symbol 종목 기호 (유니크한값)
     * @return 미체결약정 정보
     */
    OpenInterest getOpenInterest(String symbol);

}
