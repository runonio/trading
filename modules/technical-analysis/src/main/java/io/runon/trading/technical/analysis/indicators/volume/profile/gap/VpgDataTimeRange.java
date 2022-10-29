package io.runon.trading.technical.analysis.indicators.volume.profile.gap;

import lombok.Getter;

import java.math.BigDecimal;

/**
 * 시간 범위 정보 추가
 * @author macle
 */
@Getter
public class VpgDataTimeRange {

    //기준시간 중복여부체크를 위하여 //데이터 분석벡테스팅 중에 추가분석이 필요한지 체크할때
    long longTime;
    long openTime;
    long closeTime;

    BigDecimal high;
    BigDecimal low;

    VpgData [] dataArray;

}
