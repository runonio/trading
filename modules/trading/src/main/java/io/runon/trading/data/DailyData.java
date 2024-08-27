package io.runon.trading.data;

import com.seomse.jdbc.annotation.Column;
import com.seomse.jdbc.annotation.DateTime;
import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.Table;
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

}
