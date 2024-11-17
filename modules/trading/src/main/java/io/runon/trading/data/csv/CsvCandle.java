package io.runon.trading.data.csv;

import com.seomse.commons.exception.IORuntimeException;
import com.seomse.commons.utils.FileUtil;
import com.seomse.commons.utils.time.Times;
import com.seomse.commons.utils.time.YmdUtil;
import com.seomse.commons.validation.NumberNameFileValidation;
import io.runon.trading.PriceChangeType;
import io.runon.trading.data.TradingDataPath;
import io.runon.trading.technical.analysis.candle.TimeCandle;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.ZoneId;
import java.util.*;

/**
 * csv 파일을 활용한 캔들 생성
 * @author macle
 */
public class CsvCandle {


    public static TradeCandle [] load(String path){
        return load(new File(path), -1);
    }

    public static TradeCandle [] load(String path, int limit){
        return load(path, -1, limit);
    }

    public static TradeCandle [] load(String path, long time){
        return load(new File(path),time);
    }

    public static TradeCandle [] load(File file, long time){
        List<TradeCandle> list = new ArrayList<>();

        if(file.isFile()) {
            addFile(list, file, time);
        }else{
            File [] files = FileUtil.getInFiles(file.getAbsolutePath(), new NumberNameFileValidation(), FileUtil.SORT_NAME_LONG);
            for(File f : files){
                addFile(list, f, time);
            }
        }
        TradeCandle [] candles = list.toArray(new TradeCandle[0]);
        list.clear();
        return candles;
    }

    public static TradeCandle [] load(String path, long time, int limit){
        if(limit < 1) {
            return load(new File(path), time);
        }
        String [] lines = FileUtil.getLines(new File(path), StandardCharsets.UTF_8,new NumberNameFileValidation(), FileUtil.SORT_NAME_LONG, limit);

        TradeCandle [] candles = new TradeCandle[lines.length];
        for (int i = 0; i < lines.length; i++) {
            candles[i] = make(lines[i], time);
        }
        return candles;
    }

    public static void addFile(List<TradeCandle> list, File file, long time){
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(make(line, time));
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static void out(String path, TradeCandle[] candles){

        if(candles.length == 0){
            throw new IllegalArgumentException("candles length > 0");
        }

        StringBuilder sb = new StringBuilder();
        for(TradeCandle candle : candles){
            sb.append("\n").append(value(candle));
        }

        FileUtil.fileOutput(sb.substring(1), path, false);
    }

    public static String value(TradeCandle tradeCandle){
        StringBuilder sb = new StringBuilder();
        sb.append(tradeCandle.getOpenTime());
        CsvCommon.append(sb, tradeCandle.getClose());
        CsvCommon.append(sb, tradeCandle.getOpen());
        CsvCommon.append(sb, tradeCandle.getHigh());
        CsvCommon.append(sb, tradeCandle.getLow());
        CsvCommon.append(sb, tradeCandle.getPrevious());
        CsvCommon.append(sb, tradeCandle.getVolume());
        CsvCommon.append(sb, tradeCandle.getSetAmount());
        sb.append(",").append(tradeCandle.getTradeCount());
        CsvCommon.append(sb, tradeCandle.getBuyVolume());
        CsvCommon.append(sb, tradeCandle.getBuyAmount());
        if(tradeCandle.isPriceLimit()){
            sb.append(",").append("y");
        }else{
            sb.append(",").append("n");
        }

        PriceChangeType priceChangeType = tradeCandle.getPriceChangeType();
        if(priceChangeType == PriceChangeType.UNDEFINED){
            sb.append(",");
        }else{
            sb.append(",").append(priceChangeType.toString());
        }
        sb.append(",").append(tradeCandle.getCloseTime());

        Map<String,String > dataMap = tradeCandle.getDataMap();
        if(dataMap != null && !dataMap.isEmpty()){
            JSONObject object = new JSONObject();
            Set<String> keys = dataMap.keySet();
            for(String key : keys){
                object.put(key, dataMap.get(key));
            }
            //noinspection UnnecessaryToStringCall
            sb.append(",").append(object.toString());
        }

        return sb.toString();
    }

    public static long getOpenTime(String csvLine){
        return CsvTimeFile.getTime(csvLine);
    }

    public static String value(long time, TradeCandle tradeCandle){
        return time + "," + value(tradeCandle);
    }

    public static TimeCandle makeTimeCandle(String csv, long time){
        int index = csv.indexOf(',');
        long initTime = Long.parseLong( csv.substring(0, index));
        return new TimeCandle(initTime, make(csv.substring(index+1), time));
    }


    public static TradeCandle make(String csv){
        return make(csv, -1);
    }

    /**
     *
     * @param csv 캔들시작시간(밀리초 유닉스타임)[0],종가[1],시가[2],고가[3],저가[4],직전가[5],거래량[6],거래대금[7],거래횟수[8],매수거래량[9],매수거래대금[10],가격제한여부[11],가격변화유형[12],캔들종료시간[13],데이터(json)
     * @param time 기준시간 일분봉등
     * @return TradeCandle
     */
    public static TradeCandle make(String csv, long time){
        String [] values;
        if(csv.endsWith("}")){
            //json 데이터가 존재하면
            values = new String[15];
            int dataIndex = 0;
            int begin = 0;
            for(;;){

                if(dataIndex == 14){
                    values[dataIndex] = csv.substring(begin);
                    break;
                }

                int search = csv.indexOf(',',begin);
                values[dataIndex++] = csv.substring(begin, search);
                begin = search+1;
            }
        }else{
            //존재하지 않으면
            values = csv.split(",",-1);
        }
        return make(values, time);
    }

    public static TradeCandle make(String [] values, long time){
        long openTime = Long.parseLong(values[0]);
//                캔들시작시간(밀리초 유닉스타임)[0],종가[1],시가[2],고가[3],저가[4],직전가[5],거래량[6],거래대금[7],거래횟수[8],매수거래량[9],매수거래대금[10]
        TradeCandle tradeCandle = new TradeCandle();
        tradeCandle.setOpenTime(openTime);

        if(values.length > 13){
            tradeCandle.setCloseTime(Long.parseLong(values[13]));
        }else if (time > 0){
            tradeCandle.setCloseTime(openTime + time);
        }

        tradeCandle.setClose(CsvCommon.getBigDecimal(values[1]));
        if(values.length > 2)
            tradeCandle.setOpen(CsvCommon.getBigDecimal(values[2]));
        if(values.length > 3)
            tradeCandle.setHigh(CsvCommon.getBigDecimal(values[3]));
        if(values.length > 4)
            tradeCandle.setLow(CsvCommon.getBigDecimal(values[4]));
        if(values.length > 5 && values[5] != null)
            tradeCandle.setPrevious(CsvCommon.getBigDecimal(values[5]));
        if(values.length > 6)
            tradeCandle.setVolume(CsvCommon.getBigDecimal(values[6]));

        if(values.length > 7)
            tradeCandle.setAmount(CsvCommon.getBigDecimal(values[7]));

        if(values.length > 8) {
            if (values[8] != null && !values[8].isEmpty()) {
                tradeCandle.setTradeCount(Integer.parseInt(values[8]));
            }
        }

        if(tradeCandle.getPrevious() == null){
            tradeCandle.setPrevious(tradeCandle.getOpen());
        }

        if(values.length > 9) {
            if (values[9] != null && !values[9].isEmpty()) {
                tradeCandle.setBuyVolume(CsvCommon.getBigDecimal(values[9]));
                tradeCandle.setSellVolume();
            }

        }
        if(values.length > 10) {
            if (values[10] != null && !values[10].isEmpty()) {
                tradeCandle.setBuyAmount(CsvCommon.getBigDecimal(values[10]));
                tradeCandle.setSellAmount();
            }

        }

        if(values.length > 11){
            if (values[11] != null && !values[11].isEmpty()) {

                String flagValue = values[11].toLowerCase();
                if(flagValue.equals("y") || flagValue.equals("true")){
                    tradeCandle.setPriceLimit(true);
                }else{
                    tradeCandle.setPriceLimit(false);
                }
            }
        }

        if(values.length > 12){
            if (values[12] != null && !values[12].trim().isEmpty()) {
                try{
                    tradeCandle.setPriceChangeType(PriceChangeType.valueOf(values[12].trim()));
                }catch (Exception ignore){}
            }
        }

        if(values.length > 14){
            JSONObject object = new JSONObject(values[14]);
            Set<String> keys = object.keySet();
            for(String key: keys){
                tradeCandle.addData(key, object.getString(key));
            }
        }

        //직전가로 변화량과 변화율 설정
        tradeCandle.setChange();
        tradeCandle.setEndTrade();
        return tradeCandle;
    }

    public static TradeCandle [] loadDailyCandles(String path, TradeCandle [] lastCandles, int beginYmd, int endYmd, ZoneId zoneId) {
        
        //여기 체크해보기
        long beginTime = YmdUtil.getTime(beginYmd, zoneId);
        // 0시 0분 0초를 주므로 +1일을 해준다.
        long endTime = YmdUtil.getTime(endYmd, zoneId) + (Times.DAY_1 - 1L) ;

        if(lastCandles == null) {
            return load(TradingDataPath.getAbsolutePath(path), Times.DAY_1, beginTime, endTime);
        }else{
            //메모리 데이터 살리기
            return load(TradingDataPath.getAbsolutePath(path), lastCandles,  Times.DAY_1, beginTime, endTime );
        }
    }

    //하드 입출력 시간을 줄이기 위해 메모리 데이터를 살린다. (신규데이터만 불러오기)
    public static TradeCandle [] load(String path, TradeCandle [] lastCandles,long candleTime, long beginTime, long endTime) {
        if(lastCandles == null || lastCandles.length == 0){
            return load(path, candleTime, beginTime, endTime);
        }

        //라스트 1개는 다시 불러 온다. 값이 변경될 수 있다.

        List<TradeCandle> list = new ArrayList<>();
        int end = lastCandles.length-1;

        for (int i = 0; i < end ; i++) {
            TradeCandle candle = lastCandles[i];


            long openTime = candle.getOpenTime();

            if(openTime < beginTime){
                continue;
            }

            if(openTime >= endTime){
                break ;
            }

            if(candle.getCloseTime() > 0 &&  candle.getCloseTime() > endTime){
                break ;
            }

            list.add(candle);
        }

        if(list.isEmpty()){
            return load(path, candleTime, beginTime, endTime);
        }

        beginTime = lastCandles[lastCandles.length-1].getOpenTime();
        list.addAll(Arrays.asList(load(path, candleTime, beginTime, endTime)));
        return list.toArray(new TradeCandle[0]);
    }

    public static TradeCandle [] load(String path, long candleTime, long beginTime, long endTime){

        File [] files = FileUtil.getInFiles(path, new NumberNameFileValidation(), FileUtil.SORT_NAME_LONG);

        if(files.length == 0){
            return TradeCandle.EMPTY_CANDLES;
        }

        String beginName = CsvTimeName.getName(beginTime , candleTime);
        String endName = CsvTimeName.getName(endTime , candleTime);

        int beginFileNum = Integer.parseInt(beginName);
        int endFileNum = Integer.parseInt(endName);

        List<TradeCandle> candleList = new ArrayList<>();


        outer:
        for(File file : files){
            int fileNum = Integer.parseInt(file.getName());

            if(fileNum < beginFileNum){
                continue;
            }

            if(fileNum > endFileNum){
                break;
            }
            String line= null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8))) {

                while ((line = br.readLine()) != null) {

                    TradeCandle candle = make(line, candleTime);

                    long openTime = candle.getOpenTime();

                    if(openTime < beginTime){
                        continue;
                    }

                    if(openTime >= endTime){
                        break outer;
                    }

                    if(candle.getCloseTime() > 0 && candle.getCloseTime() > endTime){
                        break outer;
                    }

                    candleList.add(candle);
                }
            } catch (Exception e) {

//                System.out.println(line);
                throw new RuntimeException(line);
            }
        }

        TradeCandle [] candles = candleList.toArray(new TradeCandle[0]);
        candleList.clear();
        return candles;
    }

    public static String [] lines(TradeCandle [] candles){
        String [] lines = new String[candles.length];
        for (int i = 0; i <lines.length ; i++) {
            lines[i] = value(candles[i]);
        }
        return lines;
    }
}