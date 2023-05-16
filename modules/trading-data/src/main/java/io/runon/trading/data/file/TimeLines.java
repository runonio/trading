package io.runon.trading.data.file;

import com.seomse.commons.callback.StrCallback;
import com.seomse.commons.exception.IORuntimeException;
import com.seomse.commons.utils.FileUtil;
import com.seomse.commons.validation.NumberNameFileValidation;
import io.runon.trading.TradingTimes;

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
public class TimeLines {

    public static String [] load(String dirPath, TimeName.Type timeNameType, TimeLine timeLine, long beginTime, int count){
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
            beginFileName = TimeName.getName(beginTime, timeNameType, TradingTimes.UTC_ZONE_ID);
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

            try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8))) {
                String line;
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
                throw new IORuntimeException(e);
            }
        }

        String [] lines = lineList.toArray(new String[0]);
        lineList.clear();

        return lines;
    }

    public static void load(String dirPath, TimeName.Type timeNameType, TimeLine timeLine, StrCallback callback){
        load(dirPath, null, -1, -1, timeNameType, timeLine, callback);
    }

    /**
     *
     * @param dirPath 경로
     * @param zoneId 타임존 아이디
     * @param beginTime 시작오픈시간
     * @param endTime 종료시간
     * @param timeLine 시간 파싱
     * @param callback 결과를 전달받을 callback
     */
    public static void load(String dirPath, ZoneId zoneId, long beginTime, long endTime, TimeName.Type timeNameType, TimeLine timeLine, StrCallback callback){
        if(zoneId == null){
            //기본
            zoneId = TradingTimes.UTC_ZONE_ID;
        }
        File[] files = FileUtil.getInFiles(dirPath, new NumberNameFileValidation(), FileUtil.SORT_NAME_LONG);

        if(files.length == 0){
            return;
        }

        int beginFileNum = -1;
        int endFileNum = -1;

        if(beginTime >= 0) {
            String beginName = TimeName.getName(beginTime, timeNameType, zoneId);
            beginFileNum = Integer.parseInt(beginName);
        }

        if(endTime >= 0){
            String endName = TimeName.getName(endTime , timeNameType, zoneId);
            endFileNum = Integer.parseInt(endName);
        }

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

                    if(endTime >= 0 && time >= endTime){
                        break outer;
                    }

                    callback.callback(line);
                }
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }

        }

    }

    public static void updateCandle(String dirPath, ZoneId zoneId, String [] lines ){
        TimeLineOut timeLineOut = LineOutManager.getInstance().getCandleLineOut(dirPath, zoneId);
        timeLineOut.update(lines);
    }
}
