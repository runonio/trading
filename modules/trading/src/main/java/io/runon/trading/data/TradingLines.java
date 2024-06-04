package io.runon.trading.data;
/**
 * 문자열 전달받기
 * @author macle
 */
public class TradingLines {

    public static String [] getLines(Object [] array){

        String [] lines = new String[array.length];

        for (int i = 0; i <array.length ; i++) {
            lines[i] = array[i].toString();
        }

        return lines;
    }
}
