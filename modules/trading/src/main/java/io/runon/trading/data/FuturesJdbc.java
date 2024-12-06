package io.runon.trading.data;

import io.runon.jdbc.objects.JdbcObjects;

import java.util.List;

/**
 * @author macle
 */
public class FuturesJdbc implements FuturesData{
    @Override
    public Futures[] getListedFutures(String exchange, String ymd) {
        List<Futures> futuresList = JdbcObjects.getObjList(Futures.class, "exchange = '" + exchange +"' and listed_ymd <= "  +ymd + " and last_trading_ymd >= " + ymd);
        return futuresList.toArray(new Futures[0]);
    }

    @Override
    public Futures[] getFutures(String exchange, String ymd) {

        List<Futures> futuresList = JdbcObjects.getObjList(Futures.class, "exchange = '" + exchange +"' and listed_ymd <= "  +ymd );
        return futuresList.toArray(new Futures[0]);
    }
}
