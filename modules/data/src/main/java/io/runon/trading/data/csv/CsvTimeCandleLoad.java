package io.runon.trading.data.csv;

import com.seomse.commons.exception.IORuntimeException;
import com.seomse.commons.utils.FileUtil;
import io.runon.trading.technical.analysis.candle.TimeCandle;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * csv 파일을 활용한 캔들 생성
 * @author macle
 */
@Slf4j
public abstract class CsvTimeCandleLoad {

     public void load(String path, long time){
        File [] files = FileUtil.getFiles(path);

        if(files.length == 0){
            log.debug("file length 0: " + path);
            return;
        }

        FileUtil.sortToNameLong(files, true);

        for(File file : files){
            try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))){
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if("".equals(line)){
                        continue;
                    }
                    addTimeCandle(CsvCandle.makeTimeCandle(line, time));
                }
            }catch(IOException e){
                throw new IORuntimeException(e);
            }
        }
    }

    public abstract void addTimeCandle(TimeCandle timeCandle);

}
