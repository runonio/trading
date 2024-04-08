package io.runon.trading.technical.analysis.indicators.volume.profile.gap;

import lombok.Getter;

/**
 * 시간 범위 정보 추가
 * @author macle
 */
@Getter
public class VpgDataTimeRange {

    long openTime;
    long closeTime;

    int startIndex;
    int end;

}
