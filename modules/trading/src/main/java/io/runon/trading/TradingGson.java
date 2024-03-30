package io.runon.trading;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author macle
 */
public class TradingGson {

    public static final Gson LOWER_CASE_WITH_UNDERSCORES_PRETTY =  new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();

    public static final Gson LOWER_CASE_WITH_UNDERSCORES =  new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

}
