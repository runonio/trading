package io.runon.trading.closed.days;

import io.runon.trading.CountryCode;
import io.runon.trading.data.YmdSet;

/**
 * @author macle
 */
public class ClosedDaysSet extends YmdSet implements ClosedDays{


    public ClosedDaysSet(){

    }

    public ClosedDaysSet(CountryCode countryCode){
        load(countryCode);
    }

    @Override
    public boolean isClosedDay(String ymd) {
        return contains(ymd);
    }

    public void load(CountryCode countryCode){
        loadFile(ClosedDays.getCloseDaysFilePath(countryCode));
    }

}
