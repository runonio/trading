package io.runon.trading.data.file;

import io.runon.commons.utils.FileUtil;
import io.runon.commons.utils.time.YmdUtil;

import java.io.File;
import java.util.List;

/**
 * 데이터 구조에서 일별데이터를 ymd int 형으로 사용할때의 유틸성 매스도
 * @author macle
 */
public class YmdFiles {


    public static void outAppend(String filePath, List<String> sortYmds) {
        outAppend(filePath, sortYmds.toArray(new String[0]));
    }

    public static void outAppend(String filePath, String [] sortYmds){

        if(sortYmds.length == 0){
            return;
        }

        File file = new File(filePath);
        String lastYmd = null;
        if(file.isFile()){
            lastYmd = FileUtil.getLastTextLine(file);
            if(lastYmd.equals("")){
                lastYmd = null;
            }
        }

        StringBuilder sb = new StringBuilder();

        if(lastYmd == null){
            for(String ymd : sortYmds){
                sb.append("\n").append(ymd);
            }
            //파일이 비어있는경우
            FileUtil.fileOutput(sb.substring(1), filePath, false);
            
        }else{
            
            //라스트 내용이 있으면
            for(String ymd : sortYmds){
                if(YmdUtil.compare(ymd, lastYmd) <= 0){
                    continue;
                }

                sb.append("\n").append(ymd);
            }
            if(!sb.isEmpty()){
                FileUtil.fileOutput(sb.toString(), filePath, true);
            }
            //라스트 내용이 있으면 줄바뀜 포함
        }

        
        
        


    }
}
