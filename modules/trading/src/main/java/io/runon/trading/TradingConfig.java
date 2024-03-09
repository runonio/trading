package io.runon.trading;

import com.seomse.commons.config.Config;

import java.time.ZoneId;

/**
 * 트레이딩 기본 설정 모음
 * @author macle
 */
public class TradingConfig {


    //텍스트 표시에 사용하는 기본설정
    public static final ZoneId viewTimeZoneId = ZoneId.of(Config.getConfig("trading.view.time.zone.id","Asia/Seoul"));
}
