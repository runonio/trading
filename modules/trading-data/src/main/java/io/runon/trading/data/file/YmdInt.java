package io.runon.trading.data.file;
/**
 * 데이터 구조에서 일별데이터를 ymd int 형으로 사용할때의 유틸성 매스도
 * @author macle
 */
public class YmdInt {
    public static int getYmd(String ymdValue){
        String remove = ymdValue.replace(".","").replace("-","").replace(":","").replace("/","").replace(" ","")
                .replace("_","").trim();

        if(remove.length() < 8){
            throw new IllegalArgumentException(ymdValue);
        }

        if(remove.length() >8){
            remove = remove.substring(0,8);
        }

        return Integer.parseInt(remove);
    }



}
