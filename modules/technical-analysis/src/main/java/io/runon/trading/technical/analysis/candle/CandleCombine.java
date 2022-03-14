package io.runon.trading.technical.analysis.candle;
/**
 * 캔들 결합
 * 최근 15분봉 (1분봉 15개 결합)등을 활용해 보기 위한 개발
 * 캔들의 이동값
 * @author macle
 */
public class CandleCombine {

    private final TradeCandle [] candles;

    private boolean isOpenTimeChange = false;
    private final long time;

    public CandleCombine(TradeCandle [] candles){
        this.candles = candles;
        this.time = candles[0].getCloseTime() - candles[0].getOpenTime();
    }

    /**
     * 마지막 캔들을 기준으로 결합
     * @param count 결합한 캔들의 수
     * @return 결합된 캔들
     */
    public TradeCandle combineLast(int count){
        if(count > candles.length){
            count = candles.length;
        }
        TradeCandle candle = new TradeCandle();
        for (int i = candles.length - count; i < candles.length; i++) {
            candle.addCandle(candles[i]);
        }
        if(isOpenTimeChange){
            candle.setOpenTime(candle.getCloseTime() - time);
        }

        return candle;
    }

    public TradeCandle [] combine(int count){
        if( count >= candles.length){
            TradeCandle [] combineCandle = new TradeCandle[1];
            combineCandle[0] = combineLast(count);
            return combineCandle;
        }

        return combine(count,candles.length - count,candles.length);
    }

    public TradeCandle [] combine(int count, int startIndex, int end){

        TradeCandle [] combineCandles = new TradeCandle[end - startIndex];
        int idx = 0;

        for (int i = startIndex; i < end; i++) {
            int combineEnd = i+1;
            int combineStart =  combineEnd - count;
            if(combineStart < 0){
                combineStart = 0;
            }
            TradeCandle candle = new TradeCandle();
            for (int j = combineStart; j < combineEnd; j++) {
                candle.addCandle(candles[i]);
            }
            if(isOpenTimeChange){
                candle.setOpenTime(candle.getCloseTime() - time);
            }
            combineCandles[idx++] = candle;
        }

        return combineCandles;
    }

    /**
     * 최근 시간으로 변경하여 캔들을 그려보기 위한 옵션
     * 최근1분봉을 받아서 최근 15분봉을 만들어서 사용하기때문에 실 매매에서는 시간이 최근값이 된다.
     * @param openTimeChange 시작시간을 변경할지에 대한 여부
     */
    public void setOpenTimeChange(boolean openTimeChange) {
        isOpenTimeChange = openTimeChange;
    }
}
