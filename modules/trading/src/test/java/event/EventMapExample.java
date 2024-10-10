package event;

import io.runon.trading.data.EventCalendar;
import io.runon.trading.data.EventYmdMap;
/**
 * @author macle
 */
public class EventMapExample {
    public static void main(String[] args) {
        //한국 선물 만기일 얻기 KOR-futures-expiration 선물, KOR-option-expiration 옵션
        EventYmdMap eventYmdMap = new EventYmdMap("KOR-option-expiration", 20200101, 20241231);
        EventCalendar[] array = eventYmdMap.getEventCalendars();

        for(EventCalendar event : array){
            System.out.println(event);
        }

        System.out.println(array.length);
    }
}
