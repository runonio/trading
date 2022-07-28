package io.runon.trading.technical.analysis.indicator.nhnl;

import io.runon.trading.technical.analysis.SymbolCandle;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 신고가 신저가 
 * 결과 데이터
 * @author macle
 */
@Data
public class NhnlData {

    int validSymbolCount = 0;
    BigDecimal index;
    SymbolCandle [] lowSymbolCandles;
    SymbolCandle [] highSymbolCandles;

}
