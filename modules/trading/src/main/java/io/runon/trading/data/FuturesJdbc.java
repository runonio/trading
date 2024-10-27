package io.runon.trading.data;

import com.seomse.jdbc.objects.JdbcObjects;

import java.util.List;

/**
 * @author macle
 */
public class FuturesJdbc implements FuturesData{
    @Override
    public Futures[] getListedFutures(String exchange, String ymd) {

        return new Futures[0];
    }

    @Override
    public Futures[] getFutures(String exchange, String ymd) {

        List<Futures> futuresList = JdbcObjects.getObjList(Futures.class, "exchange = '" + exchange +"' and listing_ymd <= "  +ymd );
        return futuresList.toArray(new Futures[0]);
    }
}
