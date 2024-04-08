package io.runon.trading.technical.analysis.symbol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import lombok.Data;

/**
 * 종목 구분 기화와 캔들 구현체
 * @author macle
 */
@Data
public class SymbolCandleData implements SymbolCandle {
    private String symbol;
    private TradeCandle [] candles;

    public SymbolCandleData(){

    }

    public SymbolCandleData(String symbol, TradeCandle [] candles){
        this.symbol = symbol;
        this.candles = candles;
    }


    @Override
    public String toString(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject object = new JsonObject();
        object.addProperty("symbol", symbol);
        if(candles == null){
            object.addProperty("length", 0);
        }else{
            object.addProperty("length", candles.length);
        }

        return gson.toJson(object);
    }


}
