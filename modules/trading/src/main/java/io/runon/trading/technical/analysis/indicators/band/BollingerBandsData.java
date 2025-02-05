package io.runon.trading.technical.analysis.indicators.band;

import com.google.gson.GsonBuilder;
import io.runon.trading.Time;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 볼린저밴드 데이터
 * mbb = 중심선 = 주가의 20 기간 이동평균선 = clo20
 * ubb = 상한선 = 중심선 + 주가의 20기간 표준편차 * 2
 * lbb = 하한선 = 중심선 – 주가의 20기간 표준편차 * 2
 * perb = %b = (주가 – 하한선) / (상한선 – 하한선) = (close - lbb) / (ubb - lbb)
 * bw = 밴드폭 (Bandwidth) = (상한선 – 하한선) / 중심선 = (ubb - lbb) / mbb
 * @author macle
 */
@Data
public class BollingerBandsData implements Time {

    long time = 1;
    BigDecimal mbb;
    BigDecimal ubb;
    BigDecimal lbb;
    BigDecimal perb;
    BigDecimal bw;
    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
