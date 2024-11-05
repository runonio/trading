package io.runon.trading.data.calendar;

import com.seomse.jdbc.annotation.Column;
import com.seomse.jdbc.annotation.DateTime;
import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.Table;
import io.runon.trading.Time;
import io.runon.trading.TradingGson;
import lombok.Data;

import java.util.Comparator;

/**
 * @author macle
 */
@Data
@Table(name="event_calendar")
public class EventCalendar implements Time {

    public final static Comparator<EventCalendar> SORT_ASC = Comparator.comparingLong(o -> o.eventTime);


    @PrimaryKey(seq = 1)
    @Column(name = "event_id")
    String eventId;

    @DateTime
    @Column(name = "event_time")
    Long eventTime;

    @Column(name = "ymd")
    Integer ymd;

    @Column(name = "event_type")
    String eventType;

    @Column(name = "name_ko")
    String nameKo;

    @Column(name = "name_en")
    String nameEn;

    @Column(name = "data_value")
    String dataValue;

    @Column(name = "country")
    String country;

    @DateTime
    @Column(name = "updated_at")
    Long updatedAt;

    @Override
    public String toString(){
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES_PRETTY.toJson(this);
    }

    @Override
    public long getTime() {
        return eventTime;
    }
}
