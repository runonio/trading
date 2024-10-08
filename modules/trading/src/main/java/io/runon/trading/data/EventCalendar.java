package io.runon.trading.data;
import com.seomse.jdbc.annotation.Column;
import com.seomse.jdbc.annotation.Table;
import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.DateTime;
import lombok.Data;

/**
 * @author macle
 */
@Data
@Table(name="event_calendar")
public class EventCalendar {

    @PrimaryKey(seq = 1)
    @Column(name = "event_id")
    String eventId;

    @DateTime
    @PrimaryKey(seq = 2)
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

    @Column(name = "description")
    String description;

    @Column(name = "country")
    String country;

    @DateTime
    @Column(name = "updated_at")
    Long updatedAt;
}
