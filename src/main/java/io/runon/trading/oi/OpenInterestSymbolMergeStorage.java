package io.runon.trading.oi;

import java.math.BigDecimal;

/**
 * 미체결 약정 2개 이상의 심볼을 합쳐서 사용할때
 * BTCUSDT, BTCBUSD 등을 같이 사용할때 활용한다
 *
 * 선물시장으로 하면 10월물 11월물등을 합치는 효과를 보고자 할때 사용한다.
 *
 * @author macle
 */
public class OpenInterestSymbolMergeStorage implements OpenInterestSymbol{

    private final OpenInterestSymbolStorage [] storages;

    public OpenInterestSymbolMergeStorage(OpenInterestSymbolStorage [] storages){
        this.storages = storages;
    }

    @Override
    public OpenInterest getData(long time) {

        OpenInterestData openInterestData = null;
        for(OpenInterestSymbolStorage storage : storages){
            OpenInterest openInterest = storage.getData(time);
            if(openInterest == null){
                continue;
            }
            if(openInterestData == null){
                openInterestData = new OpenInterestData();
                openInterestData.time = -1;
                openInterestData.openInterest = BigDecimal.ZERO;
                openInterestData.notionalValue = BigDecimal.ZERO;
            }

            OpenInterests.sum(openInterestData, openInterest);

            if(openInterestData.time < openInterest.getTime()){
                openInterestData.time = openInterest.getTime();
            }

        }

        return openInterestData;
    }

    @Override
    public OpenInterest getData() {
        OpenInterestData openInterestData = null;
        for(OpenInterestSymbolStorage storage : storages){
            OpenInterest openInterest = storage.getData();
            if(openInterest == null){
                continue;
            }
            if(openInterestData == null){
                openInterestData = new OpenInterestData();
                openInterestData.time = System.currentTimeMillis();
                openInterestData.openInterest = BigDecimal.ZERO;
                openInterestData.notionalValue = BigDecimal.ZERO;
            }

            OpenInterests.sum(openInterestData, openInterest);

            if(openInterestData.time < openInterest.getTime()){
                openInterestData.time = openInterest.getTime();
            }
        }

        return openInterestData;
    }
}
