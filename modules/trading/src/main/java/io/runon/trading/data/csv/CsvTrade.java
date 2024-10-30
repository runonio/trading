
package io.runon.trading.data.csv;

import io.runon.trading.Trade;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * csv 파일을 활용한 Trade 생성
 * @author macle
 */
@Slf4j
public class CsvTrade {

    private static final Field TRADING_PRICE_FILED = getAmount();

    private static Field getAmount(){
        try {
            Field field = Trade.class.getDeclaredField("amount");
            field.setAccessible(true);
            return field;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

//    시간(밀리초 유닉스타임)[0],유형[1],가격[2],거래량[3],거래대금[4]
    public static String value(Trade trade){
        StringBuilder sb = new StringBuilder();
        sb.append(trade.getTime());
        sb.append(",").append(trade.getType().toString());
        CsvCommon.append(sb, trade.getPrice());
        CsvCommon.append(sb, trade.getVolume());

        //필드값이 설정되어 있으면
        try {
            Object obj = TRADING_PRICE_FILED.get(trade);
            if(obj != null) {
                CsvCommon.append(sb, trade.getAmount());
            }
        }catch (Exception ignore){}
        return sb.toString();
    }

    //    시간(밀리초 유닉스타임)[0],유형[1],가격[2],거래량[3],거래대금[4]
    public static Trade make(String csv){
        String [] values = csv.split(",");
        long time = Long.parseLong(values[0]);
        Trade.Type type = Trade.Type.valueOf(values[1]);
        Trade trade = new Trade(type, CsvCommon.getBigDecimal(values[2]), CsvCommon.getBigDecimal(values[3]), time);
        if(values.length > 4){
            trade.setAmount(CsvCommon.getBigDecimal(values[4]));
        }
        return trade;
    }


    public static void main(String[] args) {
        Trade trade = new Trade(Trade.Type.BUY, new BigDecimal(1), new BigDecimal(2), System.currentTimeMillis());
        //거래대금 추가 테스트
//        trade.setAmount(new BigDecimal(3));
        String value = value(trade);
        System.out.println(value);

        //다시 잘만들어 지는지 확인
        trade = make(value);
        System.out.println(value(trade));


    }
}
