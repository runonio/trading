package io.runon.trading.data.calendar;

import io.runon.commons.utils.GsonUtils;
import io.runon.jdbc.annotation.Column;
import io.runon.jdbc.annotation.DateTime;
import io.runon.jdbc.annotation.PrimaryKey;
import io.runon.jdbc.annotation.Table;

/**
 * @author macle
 */
@Table(name="event_calendar_item")
public class EventCalendarItem {

    @PrimaryKey(seq = 1)
    @Column(name = "event_id")
    String eventId;

    @PrimaryKey(seq = 2)
    @Column(name = "item_type")
    String itemType;

    @PrimaryKey(seq = 3)
    @Column(name = "item_id")
    String itemId;

    @DateTime
    @Column(name = "updated_at")
    Long updatedAt;

    @Override
    public String toString(){
        return GsonUtils.LOWER_CASE_WITH_UNDERSCORES_PRETTY.toJson(this);
    }
}
