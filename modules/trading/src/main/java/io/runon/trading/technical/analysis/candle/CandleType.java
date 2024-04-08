package io.runon.trading.technical.analysis.candle;
/**
 * 캔들유형
 * @author macle
 */
public enum CandleType {
    UNDEFINED // 정의되지않음
        , STEADY //보합세 캔들 시가 == 종가 or 시가종가가 비슷한 캔들
        , LONG // 긴 캔들
        , SHORT // 짧은 캔들
        , UPPER_SHADOW //위 그림자 캔들 -- 유성형 역망치형
        , LOWER_SHADOW //아래 그림자 캔들 -- 망치형(Hammer)저점 --  교수형(Hanging man)고점
        , HIGH_WAVE //위 아래에 그림자가 있는캔들 (긴거)
        , SPINNING_TOPS //위 아래에 그림자가 있는 캔들 (짧은거)
        , DOJI // 십자캔들
}
