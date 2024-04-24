package io.runon.trading.technical.analysis.candle;

import java.util.HashMap;
import java.util.Map;

/**
 * 메모리저장소
 * 싱글턴
 * @author macle
 */
public class IdCandleTimeStorage {
    private static class Singleton {
        private static final IdCandleTimeStorage instance = new IdCandleTimeStorage();
    }

    public static IdCandleTimeStorage getInstance() {
        return Singleton.instance;
    }

    long seq = 0;

    private final Object lock = new Object();

    private final Map<String, IdCandleTimes> map = new HashMap<>();

    public void add(IdCandleTimes symbolCandleTimes){
        String id = symbolCandleTimes.getId();
        if(id == null){
            symbolCandleTimes.setId(newId());
        }

        synchronized (lock){
            map.put(symbolCandleTimes.getId(), symbolCandleTimes);
        }

    }

    private String newId(){
        synchronized (lock){
            for(;;) {
                String id = Long.toString(seq++);
                if(map.containsKey(id)){
                    continue;
                }

                return id;
            }
        }
    }

    public IdCandleTimes remove(String id){
        synchronized (lock){
            return map.remove(id);
        }
    }

    public IdCandleTimes get(String id){
        synchronized (lock){
            return map.get(id);
        }
    }




    public IdCandleTimes[] getArray(){
        synchronized (lock){
            return map.values().toArray(new IdCandleTimes[0]);
        }
    }


}