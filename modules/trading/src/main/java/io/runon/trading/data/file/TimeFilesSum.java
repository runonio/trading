package io.runon.trading.data.file;

import io.runon.commons.utils.FileUtils;
import io.runon.commons.validation.NumberNameFileValidation;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 시간 형상의 파일을 새로운경로에 새로운 파일로 이관한다.
 * 시간정보는 반드시 맞아야한다.
 * 시간정보가 맞지 않으면 TimeFilePathChange 를 활용하여 시간정보를 맞추고 시작하여야 한다.
 * @author macle
 */
@Slf4j
public class TimeFilesSum {

    //시간정보는 반드시 맞아야한다


    private final long timeGap;
    private final TimeLine timeLine;
    public TimeFilesSum(long timeGap, TimeLine timeLine){
        this.timeGap = timeGap;
        this.timeLine = timeLine;
    }


    public void sum(String [] sumDirs, String newPath){

        File newPathCheck = new File(newPath);
        if(newPathCheck.isDirectory()){
            File [] files = newPathCheck.listFiles();
            if(files != null && files.length != 0){
                throw new RuntimeException("directory already exists: " + newPath);
            }
        }
        //noinspection ResultOfMethodCallIgnored
        new File(newPath).mkdirs();
        //중복 제거용
        Set<Long> timeFileSet = new HashSet<>();

        for(String sumDir : sumDirs){
            File []  files = FileUtils.getInFiles(sumDir , new NumberNameFileValidation(), FileUtils.SORT_NAME_LONG);

            for(File file : files){
                try {
                    timeFileSet.add(Long.parseLong(file.getName()));
                }catch (Exception ignore){}
            }
        }

        long [] timeFileNames = new long[timeFileSet.size()];
        int idx = 0;
        for(long timeFileName : timeFileSet){
            timeFileNames[idx++] = timeFileName;
        }

        Arrays.sort(timeFileNames);

        for(long timeFileName : timeFileNames){
            log.debug("start name: " + timeFileName);

            List<LineTime> lineTimeList = new ArrayList<>();

            for(String sumDir : sumDirs){
                File file = new File(sumDir+"/" + timeFileName);
                if(!file.isFile()){
                    continue;
                }

                List<String> lines = new ArrayList<>();
                FileUtils.addLine(lines, file, StandardCharsets.UTF_8);
                for(String line : lines){
                    lineTimeList.add(new LineTime(line, timeLine.getTime(line)) );
                }
                lines.clear();
            }

            LineTime [] lineTimes = lineTimeList.toArray(new LineTime[0]);
            Arrays.sort(lineTimes, LineTime.SORT);

            //새로운 라인내용 새파일에 입력할 데이터 작성
            StringBuilder sb = new StringBuilder();
            long lastTime = timeGap*-1;
            for(LineTime lineTime : lineTimes){
                if(lineTime.getTime() - timeGap >= lastTime){
                    sb.append("\n").append(lineTime.line);
                    lastTime = lineTime.getTime();
                }
            }

            if(sb.length() > 0){
                FileUtils.fileOutput(sb.substring(1), newPath +"/" + timeFileName, false);
            }

            //초기화 소스, 가비지컬렉터 성능향상용
            sb.setLength(0);
            //noinspection UnusedAssignment
            lineTimes = null;
            lineTimeList.clear();

        }
    }

    public static void main(String[] args) {

        String [] sumDirs = {
          "C:\\data\\office_1\\futures\\order_book\\BTCUSDT_new"
          , "C:\\data\\office_2\\futures\\order_book\\BTCUSDT_new"
          , "C:\\data\\office_3\\futures\\order_book\\BTCUSDT_new"
        };

        String newPath = "D:\\data\\cryptocurrency\\futures\\order_book\\BTCUSDT";

        TimeFilesSum timeFilesSum = new TimeFilesSum(700L, new JsonTimeLine());
        timeFilesSum.sum(sumDirs, newPath);
    }

}
