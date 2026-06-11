package io.runon.trading.technical.analysis.candle;

import io.runon.commons.parallel.ParallelStatData;

import java.util.HashSet;
import java.util.Set;
/**
 * @author macle
 */
public class CandleTimeParallel implements ParallelStatData<CandlesGet> {
    Set<Long> timeSet = new HashSet<>();
    @Override
    public void workStat(CandlesGet getCandles) {
        TradeCandle[] tradeCandles = getCandles.getCandles();
        for (TradeCandle candle : tradeCandles) {
            timeSet.add(candle.getTime());
        }
    }

    @Override
    public void dataStat(ParallelStatData<CandlesGet> data) {
        CandleTimeParallel d = (CandleTimeParallel)data;
        timeSet.addAll(d.timeSet);
    }
}
