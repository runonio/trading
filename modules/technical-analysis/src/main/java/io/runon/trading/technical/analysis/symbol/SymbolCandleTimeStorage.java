package io.runon.trading.technical.analysis.symbol;

import java.util.HashMap;
import java.util.Map;

/**
 * 메모리저장소
 * 싱글턴
 * @author macle
 */
public class SymbolCandleTimeStorage {
    private static class Singleton {
        private static final SymbolCandleTimeStorage instance = new SymbolCandleTimeStorage();
    }

    public static SymbolCandleTimeStorage getInstance() {
        return Singleton.instance;
    }

    long seq = 0;

    private final Object lock = new Object();

    private final Map<String, SymbolCandleTimes> map = new HashMap<>();

    public void add(SymbolCandleTimes symbolCandleTimes){
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

    public SymbolCandleTimes remove(String id){
        synchronized (lock){
            return map.remove(id);
        }
    }

    public SymbolCandleTimes get(String id){
        synchronized (lock){
            return map.get(id);
        }
    }




    public SymbolCandleTimes [] getArray(){
        synchronized (lock){
            return map.values().toArray(new SymbolCandleTimes[0]);
        }
    }


}