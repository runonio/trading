package io.runon.trading.data;

import com.seomse.jdbc.annotation.Column;
import com.seomse.jdbc.annotation.Table;
import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.DateTime;
import lombok.Data;

import java.util.Comparator;


/**
 * @author macle
 */
@Data
@Table(name="time_text")
public class TimeText {

    public static final Comparator<TimeText> SORT = Comparator.comparingLong(o -> o.time);

    @PrimaryKey(seq = 1)
    @Column(name = "data_key")
    String dataKey;

    @PrimaryKey(seq = 2)
    @Column(name = "time_long")
    long time;

    @Column(name = "data_value")
    String dataValue;

    @DateTime
    @Column(name = "updated_at")
    long updatedAt;

}
