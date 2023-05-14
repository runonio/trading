package io.runon.trading.data.file;

import com.seomse.commons.exception.IORuntimeException;
import com.seomse.commons.utils.FileUtil;
import com.seomse.commons.validation.NumberNameFileValidation;
import io.runon.trading.TradingTimes;

import java.io.*;
import java.time.ZoneId;

/**
 * 시간파일 다시 정의
 * 시간범위 (1시간 1일등으로 파일을 다시나눔)
 * 타임존 변경
 * @author macle
 */
public class TimeFileOverride implements Runnable{

    private final String dirPath;
    private final TimeLine timeLine;
    private final TimeName.Type timeNameType;

    private boolean isStop =false;

    public TimeFileOverride(String dirPath
            , TimeLine timeLine
            , TimeName.Type timeNameType){
        this.dirPath = dirPath;
        this.timeLine = timeLine;
        this.timeNameType = timeNameType;
    }

    private ZoneId zoneId = TradingTimes.UTC_ZONE_ID;

    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    @Override
    public void run() {

        String moveDir = dirPath +"/temp_time_files";
        if(new File(moveDir).isDirectory()) {
            FileUtil.delete(moveDir);
        }
        TimeFiles.moveDir(dirPath, moveDir);

        File[] files = FileUtil.getInFiles(moveDir, new NumberNameFileValidation(), FileUtil.SORT_NAME_LONG);

        String lastName = null;
        StringBuilder sb = new StringBuilder();

        outer:
        for(File file : files){
            try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))){
                String line;
                while ((line = br.readLine()) != null) {
                    if(isStop){
                        break outer;
                    }

                    line = line.trim();
                    if("".equals(line)){
                        continue;
                    }

                    long time = timeLine.getTime(line);
                    String fileName = TimeName.getName(time , timeNameType, zoneId);

                    if(lastName == null){
                        lastName = fileName;
                        sb.append(line);
                        continue;
                    }

                    if(fileName.equals(lastName)){
                        sb.append('\n').append(line);
                    }else {
                        FileUtil.fileOutput(sb.toString(), dirPath + "/" + lastName, false);
                        sb.setLength(0);
                        lastName = fileName;
                        sb.append(line);
                    }
                }
            }catch(IOException e){
                throw new IORuntimeException(e);
            }
        }

        if(sb.length() > 0){
            FileUtil.fileOutput(sb.toString(), dirPath + "/" + lastName, false);
            sb.setLength(0);
        }

    }

    public void setStop(boolean stop) {
        isStop = stop;
    }

}
