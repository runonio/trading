package io.runon.trading.data;

import com.seomse.commons.utils.time.YmdUtil;
import io.runon.trading.data.file.YmdFiles;

import java.util.Arrays;

/**
 * @author macle
 */
public class YmdSet extends TextLineSet{
    protected String maxYmd = null;
    protected String minYmd = null;

    @Override
    public boolean add(String line){

        if(maxYmd == null){
            maxYmd = line;
        }else {
            maxYmd = YmdUtil.max(line, maxYmd);
        }

        if(minYmd == null){
            maxYmd = line;
        }else{
            minYmd = YmdUtil.min(line, minYmd);
        }

        return super.add(line);
    }

    public String getMaxYmd() {
        return maxYmd;
    }

    public String getMinYmd() {
        return minYmd;
    }

    public void outFile(String filePath){
        String [] arrays = lineSet.toArray(new String[0]);
        Arrays.sort(arrays);
        YmdFiles.outAppend(filePath, arrays);
    }

}
