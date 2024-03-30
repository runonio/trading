package io.runon.trading;
/**
 * 관련 유형은 정의만하고 아직 사용하지않음.
 * 다양한 케이스가 필요할것으로 예상
 * 락의 종류에 대한 정의가 아직 확실치 않음
 * 한국투자증권은 아래와 같이 총 7가지를 정의함. 다른 증권사 까지 연동되어야 정의가 가능해보임. 한국투자증권만 이용할때는 아래 코드를 활용함
 * 01 : 권리락
 * 02 : 배당락
 * 03 : 분배락
 * 04 : 권배락
 * 05 : 중간(분기)배당락
 * 06 : 권리중간배당락
 * 07 : 권리분기배당락
 * @author macle
 */
public enum LockType {
    NONE // 락 없음
    , RIGHTS_LOCK //권리락 01
    , DIVIDEND_LOCK //배당락 02 05
    , DISTRIBUTION_LOCK //분배락 03
    , RIGHTS_DIVIDEND_LOCK //권배락 (권리배당 동시발생) 04 06 07

}
