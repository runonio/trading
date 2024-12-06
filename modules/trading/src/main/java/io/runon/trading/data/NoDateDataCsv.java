package io.runon.trading.data;

import io.runon.commons.utils.FileUtil;
import lombok.Setter;

/**
 * @author macle
 */
@Setter
public class NoDateDataCsv {


    String dataType;

    String dateType;
    String nameKo;
    String nameEn;

    String columnSplitRegex = "\t";

    String country;

    public NoDateDataCsv(String dataType){
        this.dataType = dataType;
    }


    public void load(String path){
        String content = FileUtil.getFileContents(path);

        String [] rows = content.split("\n");
        for(String row : rows){
            String [] values = row.split(columnSplitRegex);
            NoDateData.update(getDateValue(values[0]), dateType, country, dataType, values[1], nameKo, nameEn);
        }
    }


    public String getDateValue(String dateValue){
        dateValue = dateValue.replace(".","");
        dateValue = dateValue.replace(" ","");
        dateValue = dateValue.replace("\"","");
        return dateValue.trim();
    }

}
