package io.runon.trading.data;

import io.runon.commons.utils.GsonUtils;
import io.runon.jdbc.annotation.Column;
import io.runon.jdbc.annotation.DateTime;
import io.runon.jdbc.annotation.PrimaryKey;
import io.runon.jdbc.annotation.Table;
import lombok.Data;

import java.util.Comparator;

/**
 * @author macle
 */
@Data
@Table(name="daily_data")
public class DailyData {

    public static final DailyData [] EMPTY_ARRAY = new DailyData[0];
    public static final Comparator<DailyData> SORT = Comparator.comparingInt(o -> o.ymd);

    @PrimaryKey(seq = 1)
    @Column(name = "data_key")
    String dataKey;

    @PrimaryKey(seq = 2)
    @Column(name = "ymd")
    int ymd;

    @Column(name = "data_value")
    String dataValue;

    @DateTime
    @Column(name = "updated_at")
    long updatedAt = System.currentTimeMillis();


    public void setYmd(String ymd){
        this.ymd = Integer.parseInt(ymd);
    }

    public void setYmd(int ymd){
        this.ymd = ymd;
    }


    @Override
    public String toString(){
        return GsonUtils.LOWER_CASE_WITH_UNDERSCORES.toJson(this);
    }

}
