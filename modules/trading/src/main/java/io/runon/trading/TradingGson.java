package io.runon.trading;

import com.google.gson.Gson;
import io.runon.commons.utils.GsonUtils;

/**
 * @author macle
 */
public class TradingGson {

    public static final Gson LOWER_CASE_WITH_UNDERSCORES_PRETTY = GsonUtils.LOWER_CASE_WITH_UNDERSCORES_PRETTY;

    public static final Gson LOWER_CASE_WITH_UNDERSCORES =  GsonUtils.LOWER_CASE_WITH_UNDERSCORES;
    public static final Gson PRETTY = GsonUtils.PRETTY;
    public static final Gson DEFAULT =GsonUtils.GSON;

}
