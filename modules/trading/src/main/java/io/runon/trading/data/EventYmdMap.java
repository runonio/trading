package io.runon.trading.data;

import com.seomse.jdbc.objects.JdbcObjects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author macle
 */
public class EventYmdMap {


    String eventIdStartWith;

    final Map<Integer, EventCalendar> map = new HashMap<>();
    EventCalendar [] eventCalendars;



    public EventYmdMap(String eventIdStartWith){
        this.eventIdStartWith = eventIdStartWith;
        setEvent(eventIdStartWith);
    }

    public EventYmdMap(String eventIdStartWith, int beginYmd, int endYmd){
        this.eventIdStartWith = eventIdStartWith;
        setEvent(eventIdStartWith,beginYmd,endYmd);
    }

    public EventCalendar getEvent(int ymd){
        return map.get(ymd);
    }

    public EventCalendar[] getEventCalendars() {
        return eventCalendars;
    }

    public void setEvent(String eventIdStartWith){
        List<EventCalendar> list = JdbcObjects.getObjList(EventCalendar.class, "event_id like '" + eventIdStartWith +"%'","ymd asc");
        setEventList(list);
        list.clear();
    }

    public void setEvent(String eventIdStartWith , int beginYmd, int endYmd){
        List<EventCalendar> list = JdbcObjects.getObjList(EventCalendar.class, "event_id like '" + eventIdStartWith +"%' and ymd >= " + beginYmd+ " and ymd <=" + endYmd,"ymd asc");
        setEventList(list);
        list.clear();
    }

    public void setEventList(List<EventCalendar> list){
        map.clear();
        for(EventCalendar eventCalendar : list){
            map.put(eventCalendar.getYmd(), eventCalendar);
        }
        eventCalendars = list.toArray(new EventCalendar[0]);
    }

    public String getEventIdStartWith() {
        return eventIdStartWith;
    }
}
