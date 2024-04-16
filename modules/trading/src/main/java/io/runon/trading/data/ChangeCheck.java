package io.runon.trading.data;
/**
 * 데이터 변화 체크
 * 데이터가 변하면 정보를 업데이트 해야한다.
 * 데이터 변화여부 체크 (기준시간 변화등)
 * @author macle
 */
public class ChangeCheck {


    public static boolean isChange(String val1, String val2){
        if(val1 == null && val2 == null){
            return false;
        }
        if(val1 == null){
            return true;
        }
        if(val2 == null){
            return true;
        }
        return !val1.equals(val2);
    }

}
