package io.runon.trading.data;

import io.runon.trading.data.calendar.EventCalendar;

import java.util.*;

/**
 * @author macle
 */
public class EventYmdMaps {

    final Map<String, EventYmdMap> map = new HashMap<>();

    EventYmdMap [] maps;

    public EventYmdMaps(EventYmdMap [] maps){
        this.maps = maps;
    }

    public EventYmdMaps(String [] eventIdStartWiths  ){
        maps = new EventYmdMap[eventIdStartWiths.length];
        for (int i = 0; i <maps.length ; i++) {
            maps[i] = new EventYmdMap(eventIdStartWiths[i]);
            map.put(maps[i].getEventIdStartWith(), maps[i]);
        }
    }

    public EventYmdMaps(String[] eventIdStartWiths, int beginYmd, int endYmd ){
        maps = new EventYmdMap[eventIdStartWiths.length];
        for (int i = 0; i <maps.length ; i++) {
            maps[i] = new EventYmdMap(eventIdStartWiths[i], beginYmd, endYmd);
            map.put(maps[i].getEventIdStartWith(), maps[i]);
        }
    }

    public EventCalendar [] getEvent(int ymd){
        List<EventCalendar> list = new ArrayList<>();
        for(EventYmdMap map : maps){
            EventCalendar eventCalendar = map.getEvent(ymd);
            if(eventCalendar != null){
                list.add(eventCalendar);
            }
        }
        return list.toArray(new EventCalendar[0]);
    }

    public EventYmdMap getEventMap(String eventIdStartWith){
        return map.get(eventIdStartWith);
    }

    public EventCalendar[] getAllEvent(){
        List<EventCalendar> list = new ArrayList<>();
        for(EventYmdMap map : maps){
            list.addAll(Arrays.asList(map.getEventCalendars()));
        }

        EventCalendar [] array = list.toArray(new EventCalendar[0]);
        Arrays.sort(array, EventCalendar.SORT_ASC);
        list.clear();
        return array;
    }


}
