package io.runon.trading.technical.analysis.indicator.volume;

import io.runon.trading.technical.analysis.SymbolCandle;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 거래량이 급등하는 종목의 수를 지수화 결과 데이터
 *
 * @author macle
 */
@Data
public class SoaringTradingVolumeData {
    SymbolCandle [] soaringArray ;
    BigDecimal index;

    int validSymbolCount;

    public int length(){
        return soaringArray.length;
    }


}
