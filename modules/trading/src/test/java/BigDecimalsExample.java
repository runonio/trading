import io.runon.trading.BigDecimals;
import io.runon.trading.TradingMath;

import java.math.BigDecimal;
/**
 * @author macle
 */
public class BigDecimalsExample {
    public static void main(String[] args) {
        BigDecimal t = new BigDecimal(-2);
        //pow 는 제곱, sqrt는 루트
        System.out.println("제곱과 루트 테스트: " + t.pow(4).sqrt(BigDecimals.MC_10).toPlainString());


        BigDecimal[] array = new BigDecimal[10];

        for (int i = 0; i < array.length; i++) {
            array[i] = new BigDecimal(i+1);
        }

        System.out.println("표준편차: " + TradingMath.sd(array,3));

    }
}
