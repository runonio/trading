package io.runon.trading.data;

import com.seomse.commons.utils.string.Check;
import lombok.Setter;

import java.util.List;

/**
 * @author macle
 */
@Setter
public class NoDateDataUpdate {

    String dataType;

    String dateType;
    String nameKo;
    String nameEn;
    String country;

    public NoDateDataUpdate(String dataType){
        this.dataType = dataType;
    }

    public void update(List<StringArray> list){
        for(StringArray stringArray : list){
            if(!Check.isNumberType(stringArray.get(1))){
                continue;
            }
            if(dateType != null ){
                if(!Check.isNumber(stringArray.get(0))){
                    continue;
                }
                if(dataType.equals("month") || dataType.equals("quarter")){
                    if(stringArray.get(0).length() !=6){
                        continue;
                    }

                }else if(dataType.equals("year")){
                    if(stringArray.get(0).length() !=4){
                        continue;
                    }
                }

            }

            update(stringArray.get(0), stringArray.get(1));
        }
    }



    public void update(String date, String num){
        NoDateData.update(getDateValue(date), dateType, country, dataType, num, nameKo, nameEn);
    }

    public String getDateValue(String dateValue){
        dateValue = dateValue.replace(".","");
        dateValue = dateValue.replace(" ","");
        dateValue = dateValue.replace("\"","");
        return dateValue.trim();
    }

    public String getDateType() {
        return dateType;
    }
}
