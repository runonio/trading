package io.runon.trading.data.file;

import com.seomse.commons.exception.IORuntimeException;
import com.seomse.commons.utils.FileUtil;
import io.runon.trading.TradingTimes;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

/**
 * 시간 형상의 파일을 새로운경로에 새로운 파일로 이관한다.
 * @author macle
 */
@Slf4j
public abstract class TimeFilePathChange {

    protected TimeName.Type type = TimeName.Type.MONTH_1;

    public void setType(TimeName.Type type) {
        this.type = type;
    }

    protected ZoneId zoneId = TradingTimes.UTC_ZONE_ID;


    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    public void outDirs(String readPath, String outPath){

        readPath = new File(readPath).getAbsolutePath();
        outPath = new File(outPath).getAbsolutePath();

        File [] files = FileUtil.getFiles(readPath);

        for(File file : files){
            if(!file.isDirectory()){
                continue;
            }

            String next = file.getAbsolutePath().substring(readPath.length());
            outDir(file.getAbsolutePath(), outPath+next);
        }

    }

    public void outDir(String readPath, String outPath){

        File [] readFiles = TimeFiles.getFilesDir(readPath);

        if(readFiles.length == 0){
            return;
        }

        File outDir = new File(outPath);
        if(!outDir.isDirectory()){
            //noinspection ResultOfMethodCallIgnored
            outDir.mkdirs();
        }

        File [] checkFiles = TimeFiles.getFilesDir(outPath);
        if(checkFiles.length > 0){
            throw new IORuntimeException("directory is not empty: " + outPath);
        }

        String pathStart = outPath;
        if(!pathStart.endsWith("\\") && !pathStart.endsWith("/")){
            pathStart = pathStart+"/";
        }

        StringBuilder sb = new StringBuilder();
        String lastName = "";

        Set<String> overSet = new HashSet<>();
        String lastLine = "";
        for(File readFile : readFiles){

            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(readFile)))) {
                String line;
                while ((line = br.readLine()) != null) {

                    long time = getTime(line);
                    String name = TimeName.getName(time, type, zoneId);
                    if(!name.equals(lastName)){
                        if(sb.length() > 0){
                            if(!overSet.contains(lastName)){
                                FileUtil.fileOutput(sb.substring(1), pathStart+lastName, false);
                                overSet.add(lastName);
                            }else{
                                log.error(lastName + " sort error line: " + lastLine);
                            }
                            sb.setLength(0);
                        }

                        lastName = name;
                    }
                    sb.append("\n").append(line);
                    lastLine = line;
                }
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        }


        if(sb.length() > 0){
            if(!overSet.contains(lastName)){
                FileUtil.fileOutput(sb.substring(1), pathStart+lastName, false);
            }else{
                log.error(lastName + " sort error line: " + lastLine);
            }
            sb.setLength(0);
        }
        overSet.clear();
    }


    public abstract long getTime(String line);

}
