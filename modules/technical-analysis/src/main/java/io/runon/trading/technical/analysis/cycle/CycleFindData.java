package io.runon.trading.technical.analysis.cycle;

import io.runon.trading.BigDecimals;
import lombok.Data;

/**
 * 하락 사이클 진입 시점의 특징과 손실제한에서의 활용을 위하여 장기 하락사이클의 시작부터 끝 지점을  찾아주는 기능을 한다
 * 가격 하락 사이클
 *
 * @author macle
 */
@Data
public class CycleFindData {

    int priceUpdateCount;


    //가격 갱신 거리 배열
    int [] candleGaps;

    //가격 갱신 시간 배열
    long [] candleTimes;


    long beginOpenTime;
    long endOpenTime;




}
