package io.runon.trading.data.csv;

import com.seomse.commons.exception.IORuntimeException;
import com.seomse.commons.utils.FileUtil;
import com.seomse.commons.validation.NumberNameFileValidation;
import io.runon.trading.technical.analysis.candle.TimeCandle;
import io.runon.trading.technical.analysis.candle.TradeCandle;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * csv 파일을 활용한 캔들 생성
 * @author macle
 */
public class CsvCandle {


    public static TradeCandle [] load(String path, long time){
        return load(new File(path),time);
    }

    public static TradeCandle [] load(File file, long time){
        List<TradeCandle> list = new ArrayList<>();

        if(file.isFile()) {
            addFile(list, file, time);
        }else{
            File [] files = FileUtil.getFiles(file.getAbsolutePath(), new NumberNameFileValidation(), FileUtil.SORT_NAME_LONG);
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

//    public static TradeCandle [] load(String path, long time, )

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
        CsvCommon.append(sb, tradeCandle.getTradingPrice());
        sb.append(",").append(tradeCandle.getTradeCount());
        CsvCommon.append(sb, tradeCandle.getBuyVolume());
        CsvCommon.append(sb, tradeCandle.getBuyTradingPrice());
        return sb.toString();
    }

    public static long getOpenTime(String csvLine){
        int index = csvLine.indexOf(',');
        return Long.parseLong(csvLine.substring(0,index));
    }

    public static String value(long time, TradeCandle tradeCandle){
        return time + "," + value(tradeCandle);
    }

    public static TimeCandle makeTimeCandle(String csv, long time){
        int index = csv.indexOf(',');
        long initTime = Long.parseLong( csv.substring(0, index));
        return new TimeCandle(initTime, make(csv.substring(index+1), time));
    }

    /**
     *
     * @param csv 캔들시작시간(밀리초 유닉스타임)[0],종가[1],시가[2],고가[3],저가[4],직전가[5],거래량[6],거래대금[7],거래횟수[8],매수거래량[9],매수거래대금[10]
     * @param time 기준시간 일분봉등
     * @return TradeCandle
     */
    public static TradeCandle make(String csv, long time){
        String [] values = csv.split(",",-1);
        return make(values, time);
    }

    public static TradeCandle make(String [] values, long time){
        long openTime = Long.parseLong(values[0]);
//                캔들시작시간(밀리초 유닉스타임)[0],종가[1],시가[2],고가[3],저가[4],직전가[5],거래량[6],거래대금[7],거래횟수[8],매수거래량[9],매수거래대금[10]
        TradeCandle tradeCandle = new TradeCandle();
        tradeCandle.setOpenTime(openTime);
        tradeCandle.setCloseTime(openTime + time);
        tradeCandle.setClose(CsvCommon.getBigDecimal(values[1]));
        tradeCandle.setOpen(CsvCommon.getBigDecimal(values[2]));
        tradeCandle.setHigh(CsvCommon.getBigDecimal(values[3]));
        tradeCandle.setLow(CsvCommon.getBigDecimal(values[4]));
        tradeCandle.setPrevious(CsvCommon.getBigDecimal(values[5]));
        tradeCandle.setVolume(CsvCommon.getBigDecimal(values[6]));
        tradeCandle.setTradingPrice(CsvCommon.getBigDecimal(values[7]));
        if(values[8] != null && !values[8].equals("")){
            tradeCandle.setTradeCount(Integer.parseInt(values[8]));
        }

        if(tradeCandle.getPrevious() == null){
            tradeCandle.setPrevious(tradeCandle.getOpen());
        }
        if(values[9] != null && !values[9].equals("")) {
            tradeCandle.setBuyVolume(CsvCommon.getBigDecimal(values[9]));
        }
        if(values[10] != null && !values[10].equals("")) {
            tradeCandle.setBuyTradingPrice(CsvCommon.getBigDecimal(values[10]));
        }
        tradeCandle.setSellVolume();
        tradeCandle.setSellTradingPrice();
        //직전가로 변화량과 변화율 설정
        tradeCandle.setChange();
        return tradeCandle;
    }


    public static TradeCandle [] load(String path, long candleTime, long startTime, long endTime, ZoneId zoneId){

        File [] files = FileUtil.getFiles(path, new NumberNameFileValidation(), FileUtil.SORT_NAME_LONG);

        if(files.length == 0){
            return TradeCandle.EMPTY_CANDLES;
        }

        String startName = CsvTimeName.getName(startTime , candleTime, zoneId);
        String endName = CsvTimeName.getName(startTime , candleTime, zoneId);

        int startFileNum = Integer.parseInt(startName);
        int endFileNum = Integer.parseInt(endName);

        List<TradeCandle> candleList = new ArrayList<>();

        outer:
        for(File file : files){
            int fileNum = Integer.parseInt(file.getName());

            if(fileNum < startFileNum){
                continue;
            }

            if(fileNum > endFileNum){
                break;
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String [] values = line.split(",");
                    long openTime = Long.parseLong(values[0]);
                    long closeTime = openTime + candleTime;
                    if(openTime < startTime){
                        continue;
                    }

                    if(closeTime > endTime){
                        break outer;
                    }
                    candleList.add(make(values, candleTime));
                }
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        }

        TradeCandle [] candles = candleList.toArray(new TradeCandle[0]);
        candleList.clear();
        return candles;
    }
}