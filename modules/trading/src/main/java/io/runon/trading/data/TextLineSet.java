package io.runon.trading.data;

import com.seomse.commons.utils.FileUtil;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
/**
 * @author macle
 */
public class TextLineSet {
    protected Set<String> lineSet = new HashSet<>();



    public void loadFile(String filePath){

        String text = FileUtil.getFileContents(new File(filePath), StandardCharsets.UTF_8);
        String [] lines = text.split("\n");
        for(String line : lines){
            line = line.trim();
            if(line.equals("")){
                continue;
            }
            if(lineSet.contains(line)){
                continue;
            }

            add(line);
        }
    }


    public boolean add(String line){
        return lineSet.add(line);
    }

    public boolean remove(String line){
        return lineSet.remove(line);
    }


    public void setLineSet(Set<String> lineSet) {
        this.lineSet = lineSet;
    }

    public String getText(){
        if(lineSet.size() == 0){
            return "";
        }

        String [] arrays = lineSet.toArray(new String[0]);
        Arrays.sort(arrays);
        StringBuilder sb = new StringBuilder();
        for(String ymd : arrays){
            sb.append("\n").append(ymd);
        }
        return sb.substring(1);
    }

    public boolean contains(String textLine){
        return lineSet.contains(textLine);
    }

    public void clear(){
        lineSet.clear();
    }
}
