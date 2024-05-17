package io.runon.trading.data.file;

import com.seomse.commons.utils.FileUtil;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.time.ZoneId;
import java.util.List;
/**
 * csv 파일을 활용한 캔들 생성
 * @author macle
 */
public class FileLineOut {

    /**
     * 파일에 새로운 내용을 추가한다.
     */
    public static void outNewLines(TimeLine timeLine, String [] lines, String filesDirPath, TimeName.Type type, ZoneId zoneId){

        String fileSeparator = FileSystems.getDefault().getSeparator();

        if(!filesDirPath.endsWith(fileSeparator)){
            filesDirPath = filesDirPath+fileSeparator;
        }


        StringBuilder sb = new StringBuilder();
        String lastOutPath = null;

        for(String line: lines){
            long time = timeLine.getTime(line);
            String outPath = filesDirPath + TimeName.getName(time, type, zoneId);

            if(lastOutPath == null || !lastOutPath.equals(outPath)){

                if(!sb.isEmpty()){

                    if(!FileUtil.isFile(lastOutPath)){
                        FileUtil.fileOutput(sb.toString(), lastOutPath, false);
                    }else{
                        FileUtil.fileOutput("\n" + sb.toString(), lastOutPath, true);
                    }
                    sb.setLength(0);
                }

                lastOutPath = outPath;
            }

            if(sb.isEmpty()){

                sb.append(line);
            }else{
                sb.append("\n").append(line);
            }

        }

        if(!sb.isEmpty()){
            if(!FileUtil.isFile(lastOutPath)){
                FileUtil.fileOutput(sb.toString(), lastOutPath, false);
            }else{
                FileUtil.fileOutput("\n" + sb.toString(), lastOutPath, true);
            }

            sb.setLength(0);
        }

    }

    /**
     * 파일의 마지막 부분만 병경한다.
     * 관련 메소드는 마지막 파일 전체를 다시쓰는 부분이 존재한다.
     * 장중에 데이터를 호출했을떄 마지막 캔들은 계속해서 데이터가 변한다. 데이터 변화가 필요한 경우에 관련 메소드를 이용한다.
     */
    public static void outBackPartChange(PathTimeLine pathTimeLine, String [] lines, String filesDirPath, TimeName.Type type, ZoneId zoneId){
        if(lines.length == 0){
            return;
        }


        long lastOpenTime = pathTimeLine.getLastTime(filesDirPath);

        if(lastOpenTime == -1){
            outNewLines(pathTimeLine, lines, filesDirPath,type, zoneId);
            return;
        }

        String firstLine = lines[0];
        long openTime = pathTimeLine.getTime(firstLine);
        String fileSeparator = FileSystems.getDefault().getSeparator();

        if(!filesDirPath.endsWith(fileSeparator)){
            filesDirPath = filesDirPath+fileSeparator;
        }

        String outPath = filesDirPath + TimeName.getName(openTime, type, zoneId);
        if(!FileUtil.isFile(outPath)){
            outNewLines(pathTimeLine, lines, filesDirPath,type, zoneId);
            return;
        }

        List<String> lineList = FileUtil.getLineList(new File(outPath), StandardCharsets.UTF_8);

        boolean isChange = false;

        StringBuilder sb = new StringBuilder();
        for(String line : lineList){
            long time = pathTimeLine.getTime(line);
            if(time >= openTime){
                if(line.equals(firstLine)){
                    //첫라인이 변한게 없으면 두번째 라인부터 기록한다
                    if(lines.length == 1){
                        return;
                    }
                    String [] newLines = new String[lines.length-1];
                    //1번째 라인부터 복사해서 새로운 배열을 만듬
                    System.arraycopy(lines, 1, newLines, 0, newLines.length);
                    outNewLines(pathTimeLine, newLines, filesDirPath,type, zoneId);
                    return;
                }
                isChange = true;
                break;
            }
            sb.append("\n").append(line);
        }

        if(!isChange){
            outNewLines(pathTimeLine, lines, filesDirPath,type, zoneId);
            return;
        }

        if(sb.isEmpty()){
            //noinspection ResultOfMethodCallIgnored
            new File(outPath).delete();
            outNewLines(pathTimeLine, lines, filesDirPath,type, zoneId);
            return;
        }

        //마지막 라인을 제외하고 다시저장
        FileUtil.fileOutput( sb.substring(1), outPath, false);
        outNewLines(pathTimeLine, lines, filesDirPath,type, zoneId);
    }
}
