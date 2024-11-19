package io.runon.trading.data;

import com.seomse.jdbc.annotation.Column;
import com.seomse.jdbc.annotation.Table;
import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.DateTime;
import io.runon.trading.data.jdbc.TradingJdbc;
import lombok.Data;

/**
 * @author macle
 */
@Data
@Table(name="no_date_data")
public class NoDateData {
    @PrimaryKey(seq = 1)
    @Column(name = "data_id")
    String dataId;

    @Column(name = "date_value")
    String dateValue;

    @Column(name = "date_type")
    String dateType;

    @Column(name = "name_ko")
    String nameKo;

    @Column(name = "name_en")
    String nameEn;

    @Column(name = "country")
    String country;

    @Column(name = "data_type")
    String dataType;

    @Column(name = "data_value")
    String dataValue;

    @DateTime
    @Column(name = "updated_at")
    long updatedAt = System.currentTimeMillis();


    public static void update(String dateValue, String dateType, String country, String dataType, String dataValue, String nameKo, String nameEn){
        NoDateData noDateData = new NoDateData();

        if(country == null) {
            noDateData.setDataId(dataType + "_" + dateValue);
        }else{
            noDateData.setDataId(country + "_" + dataType + "_" + dateValue);
        }

        noDateData.setDateValue(dateValue);
        noDateData.setDateType(dateType);
        noDateData.setCountry(country);
        noDateData.setDataType(dataType);
        noDateData.setDataValue(dataValue);
        noDateData.setNameKo(nameKo);
        noDateData.setNameEn(nameEn);
        TradingJdbc.updateTimeCheck(noDateData);
    }



}
