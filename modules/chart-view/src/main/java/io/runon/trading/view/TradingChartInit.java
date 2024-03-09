package io.runon.trading.view;

import com.seomse.commons.config.Config;
import io.github.bonigarcia.wdm.WebDriverManager;
/**
 * 트레이딩 차트 실행
 * @author ccsweets
 */
public class TradingChartInit {

    private static boolean isInit = false;

    private static final Object lock = new Object();

    public static void init(){
        if(isInit){
            return;
        }

        synchronized (lock) {
            if(isInit){
                //동기화 구간에서 한번더 체크
                return;
            }

            isInit = true;
            
            if (Config.getBoolean("chrome.driver.update", true)) {
                WebDriverManager.chromedriver().setup();
            }
        }
    }
}
