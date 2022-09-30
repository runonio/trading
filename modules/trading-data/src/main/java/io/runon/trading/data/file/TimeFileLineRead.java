package io.runon.trading.data.file;

import com.seomse.commons.exception.IORuntimeException;
import com.seomse.commons.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * data
 * @author macle
 */
@Slf4j
public abstract class TimeFileLineRead {


    private long startName= -1;
    private long endName = -1;


    public void setStartName(long startName) {
        this.startName = startName;
    }

    public void setEndName(long endName) {
        this.endName = endName;
    }

    private boolean isEnd = false;

    public void read(String path){
        isEnd = false;

        File[] files = FileUtil.getFiles(path);

        int dirCount = 0;
        for(File checkFile : files){
            if(checkFile.isDirectory()){
                dirCount++;
            }
        }
        if(dirCount > 0) {
            int index = 0;
            File [] array = new File[files.length-dirCount];
            for(File file : files){
                if(file.isFile()) {
                    array[index++] = file;
                }
            }
            files = array;
        }

        if(files.length == 0){
            log.debug("file length 0: " + path);
            return;
        }

        FileUtil.sortToNameLong(files, true);

        for(File file : files){

            if(startName != -1 || endName != -1 ){
                long name = Long.parseLong(file.getName());
                if(startName != -1 && name < startName){
                    continue;
                }

                if(endName != -1 && name > endName){
                    continue;
                }
            }

            try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))){
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if("".equals(line)){
                        continue;
                    }
                    addLine(line);
                }
            }catch(IOException e){
                throw new IORuntimeException(e);
            }
        }
        end();

        isEnd = true;
    }
    
    //완료이벤트
    public void end(){
        
    }

    public boolean isEnd() {
        return isEnd;
    }

    public abstract void addLine(String line);
}
