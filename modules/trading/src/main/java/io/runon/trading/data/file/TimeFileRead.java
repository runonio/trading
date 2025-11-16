package io.runon.trading.data.file;

import io.runon.commons.exception.IORuntimeException;
import io.runon.commons.utils.FileUtils;
import io.runon.commons.validation.NumberNameFileValidation;
import io.runon.trading.data.ReadEnd;
import io.runon.trading.data.StringRead;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * 시간정보가 있는 파일
 * @author macle
 */
public class TimeFileRead {

    private boolean isRead = true;

    private ReadEnd readEnd = null;

    public void setReadEnd(ReadEnd readEnd) {
        this.readEnd = readEnd;
    }

    private Charset charset = StandardCharsets.UTF_8;

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    /**
     * 앞에서 부터 읽기
     * @param path 디렉토리 경로
     * @param read 읽기 처리 구현체
     */
    public void fromBegin(String path, StringRead read){
        isRead = true;

        File[] files = FileUtils.getInFiles(path, new NumberNameFileValidation(), FileUtils.SORT_NAME_LONG);

        outer:
        for(File f : files){
            try(BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(f.toPath()), charset))){
                String line;
                while ((line = br.readLine()) != null) {
                    if(!isRead){
                        break outer;
                    }
                    read.read(line);
                }
            }catch(IOException e){
                throw new IORuntimeException(e);
            }
        }

        if(readEnd != null){
            readEnd.endRead();
        }
    }
    /**
     * 뒤에서 부터 읽기
     * @param path 파일 또는 디렉토리 경로
     * @param read 읽기 처리 구현체
     */
    public void fromEnd(String path, StringRead read){
        //시간으로 나누어진 파일은 크기가 크지 않으므로 전체를 읽어와서 처리
        isRead = true;

        File[] files =  FileUtils.getInFiles(path, new NumberNameFileValidation(), FileUtils.SORT_NAME_LONG_DESC);

        List<String> list = new ArrayList<>();
        outer:
        for(File f : files){

            list.clear();
            try(BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(f.toPath()), charset))){
                String line;
                while ((line = br.readLine()) != null) {
                    if(!isRead){
                        break outer;
                    }
                    list.add(line);
                }

            }catch(IOException e){
                throw new IORuntimeException(e);
            }

            for (int i = list.size() -1; i > -1; i--) {
                if(!isRead){
                    break outer;
                }

                read.read(list.get(i));
            }

        }

        list.clear();

        if(readEnd != null){
            readEnd.endRead();
        }
    }


    /**
     * 읽기 종료
     */
    public void endRead(){
        isRead = false;
    }

}
