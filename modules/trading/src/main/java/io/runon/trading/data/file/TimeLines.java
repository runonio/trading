package io.runon.trading.data.file;

import io.runon.commons.callback.StrCallback;
import io.runon.commons.exception.IORuntimeException;
import io.runon.commons.exception.UndefinedException;
import io.runon.commons.utils.FileUtil;
import io.runon.commons.utils.time.YmdUtil;
import io.runon.commons.validation.NumberNameFileValidation;
import io.runon.trading.data.TradingDataPath;
import io.runon.trading.exception.TradingDataException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * 데이터 구조에서 일별데이터를 ymd int 형으로 사용할때의 유틸성 매스도
 * @author macle
 */
@Slf4j
public class TimeLines {

    public static String [] load(String dirPath, TimeName timeName, TimeLine timeLine, long beginTime, int count){
        File[] files = FileUtil.getInFiles(dirPath, new NumberNameFileValidation(), FileUtil.SORT_NAME_LONG);
        if(files.length == 0){
            return new String[0];
        }

        if(count < 0){
            count = 500;
        }

        List<String> lineList = new ArrayList<>();

        String beginFileName;

        if(beginTime > 0) {
            beginFileName = timeName.getName(beginTime);
        }else{
            beginFileName = files[0].getName();
        }

        int beginFileNum = Integer.parseInt(beginFileName);

        outer:
        for(File file : files){
            int fileNum = Integer.parseInt(file.getName());

            if(fileNum < beginFileNum){
                continue;
            }
            String line = null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8))) {

                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if("".equals(line)){
                        continue;
                    }

                    long time = timeLine.getTime(line);
                    if(time < beginTime){
                        continue ;
                    }

                    lineList.add(line);

                    if(lineList.size() >= count){
                        break outer;
                    }

                }
            } catch (IOException e) {
                log.error("error file: " + file.getAbsolutePath() + "\n " + line);

                throw new IORuntimeException(e);
            }
        }

        String [] lines = lineList.toArray(new String[0]);
        lineList.clear();

        return lines;
    }

    public static void load(String dirPath, TimeName.Type timeNameType, TimeLine timeLine, StrCallback callback){
        load(dirPath, -1, -1, timeNameType, timeLine, callback);
    }


    public static String [] load(String dirPath, long beginTime, long endTime, TimeName timeName, TimeLine timeLine){

        final List<String> list = new ArrayList<>();
        StrCallback callback = list::add;
        load(dirPath, beginTime, endTime, timeName, timeLine, callback);
        String [] lines = list.toArray(new String[0]);
        list.clear();
        return lines;
    }

    public static void load(String dirPath, long beginTime, long endTime, TimeName timeName, TimeLine timeLine, StrCallback callback){
        File[] files = FileUtil.getInFiles(dirPath, new NumberNameFileValidation(), FileUtil.SORT_NAME_LONG);

        if(files.length == 0){
            return;
        }

        int beginFileNum = -1;
        int endFileNum = -1;

        if(beginTime >= 0) {
            String beginName = timeName.getName(beginTime);
            beginFileNum = Integer.parseInt(beginName);
        }

        if(endTime >= 0){
            String endName = timeName.getName(endTime);
            endFileNum = Integer.parseInt(endName);
        }
        load(files, beginFileNum, endFileNum, timeLine, beginTime, endTime, callback);

    }


    public static void load(File[] files, int beginFileNum, int endFileNum, TimeLine timeLine, long beginTime, long endTime, StrCallback callback){
        outer:
        for(File file : files){
            int fileNum = Integer.parseInt(file.getName());

            if(beginFileNum >= 0 && fileNum < beginFileNum){
                continue;
            }

            if(endFileNum >= 0 && fileNum > endFileNum){
                break;
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if("".equals(line)){
                        continue;
                    }

                    long time = timeLine.getTime(line);
                    if(beginTime >= 0 && time < beginTime){
                        continue;
                    }

                    if(endTime >= 0 && time == endTime){
                        callback.callback(line);
                        break outer;
                    }

                    if(endTime >= 0 && time > endTime){
                        break outer;
                    }
                    callback.callback(line);

                }
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }

        }
    }




    /**
     *
     * @param dirPath 경로
     * @param endTime 종료시간
     * @param timeLine 시간 파싱
     * @param callback 결과를 전달받을 callback
     */
    public static void load(String dirPath, long beginTime, long endTime, TimeName.Type timeNameType, TimeLine timeLine, StrCallback callback){

        File[] files = FileUtil.getInFiles(dirPath, new NumberNameFileValidation(), FileUtil.SORT_NAME_LONG);

        if(files.length == 0){
            return;
        }

        int beginFileNum = -1;
        int endFileNum = -1;

        if(beginTime >= 0) {
            String beginName = TimeName.getName(beginTime, timeNameType);
            beginFileNum = Integer.parseInt(beginName);
        }

        if(endTime >= 0){
            String endName = TimeName.getName(endTime , timeNameType);
            endFileNum = Integer.parseInt(endName);
        }
        load(files, beginFileNum, endFileNum, timeLine, beginTime, endTime, callback);

    }

    public static void updateCandle(String dirPath, String [] lines ){
        TimeLineLock timeLineLock = LineOutManager.getInstance().getTimeLineLock(dirPath, PathTimeLine.CSV);
        timeLineLock.update(lines);
    }


    public static String [] loadLinesLock(String dirPath,  PathTimeLine timeLine, long beginTime, int count ){
        TimeLineLock timeLineLock = LineOutManager.getInstance().getTimeLineLock(dirPath,timeLine);
        return timeLineLock.load( beginTime, count);
    }

    public static long getMaxTime(TimeLine timeLine, String [] lines){

        long maxTime = -1;
        for(String line : lines){

            long time = timeLine.getTime(line);
            maxTime = Math.max(maxTime, time);

        }

        return maxTime;
    }

    public static String getMaxYmd(TimeLine timeLine, String [] lines, ZoneId zoneId){
        long maxTime = getMaxTime(timeLine, lines);
        if(maxTime < 0){
            throw new TradingDataException("max time -1");
        }

        return YmdUtil.getYmd(maxTime, zoneId);
    }

    public static PathTimeLine getTimeLine(File file) {
        if(file.isDirectory()){
            File [] files = FileUtil.getFiles(file, new NumberNameFileValidation());
            return getTimeLine(files);
        }else{
            String line = FileUtil.getLine(file, StandardCharsets.UTF_8, 0);
            return getTimeLine(line);
        }

    }
    public static PathTimeLine getTimeLine(File [] dataFiles ) {
        String line = null;
        for(File dataFile : dataFiles){
            try {
                line  = FileUtil.getLine(dataFile, StandardCharsets.UTF_8, 0);
                if(line == null || "".equals(line)){
                    continue;
                }
                break;
            }catch (Exception ignore){}
        }

        return getTimeLine(line);
    }

    public static PathTimeLine getTimeLine(String line){

        line = line.trim();
        if(line.startsWith("{")){
            return PathTimeLine.JSON;
        }else{
            return PathTimeLine.CSV;
        }

    }

    public static TimeLineLock getTimeLineLock(JSONObject jsonObject, String line){
        PathTimeLine timeLine ;

        if(!jsonObject.isNull("time_line_type")){
            String lineType = jsonObject.getString("time_line_type").toUpperCase();
            if(lineType.equals("CSV")){
                timeLine = PathTimeLine.CSV;
            }else{
                timeLine = PathTimeLine.JSON;
            }
        }else{
            timeLine = TimeLines.getTimeLine(line);
        }

        return getTimeLineLock(jsonObject, timeLine);
    }

    //api에서 주로 사용하는 메소드, 사용할 일이 많아서 공통에 구현함
    public static TimeLineLock getTimeLineLock(JSONObject jsonObject, PathTimeLine timeLine){
        String dirPath = TradingDataPath.getAbsolutePath(jsonObject.getString("dir_path"));

        TimeName.Type timeNameType = TimeName.Type.valueOf(jsonObject.getString("time_name_type"));
        LineOutManager lineOutManager = LineOutManager.getInstance();

        return lineOutManager.get(dirPath, timeLine, timeNameType);
    }

    public static TimeLineLock getTimeLineLock(JSONObject jsonObject){
        String dirPath = TradingDataPath.getAbsolutePath(jsonObject.getString("dir_path"));

        TimeName.Type timeNameType = TimeName.Type.valueOf(jsonObject.getString("time_name_type"));
        LineOutManager lineOutManager = LineOutManager.getInstance();


        String timeLineType = jsonObject.getString("time_line_type");

        PathTimeLine timeLine;

        if(timeLineType.equals("CSV")){
            timeLine = PathTimeLine.CSV;
        }else if(timeLineType.equals("JSON")){
            timeLine = PathTimeLine.JSON;
        }else{
            throw new UndefinedException("time_line_type in(CSV, JSON) : " + timeLineType);
        }

        return lineOutManager.get(dirPath, timeLine, timeNameType);
    }

    public static String outValue(List<String> lineList){
        StringBuilder sb = new StringBuilder();
        sb.append(lineList.get(0));

        int size = lineList.size();
        for (int i = 1; i <size ; i++) {
            sb.append("\n").append(lineList.get(i));
        }

        return sb.toString();
    }

}
