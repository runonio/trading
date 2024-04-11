package io.runon.trading.closed.days;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author macle
 */
public class ClosedDaysSet implements ClosedDays{
    private final Set<String> ymdSet;

    public ClosedDaysSet(Set<String> ymdSet){
        this.ymdSet = ymdSet;
    }

    public ClosedDaysSet(){
        ymdSet = new HashSet<>();
    }

    public boolean addYmd(String ymd){
        return ymdSet.add(ymd);
    }

    public boolean removeYmd(String ymd){
        return ymdSet.remove(ymd);
    }

    @Override
    public boolean isClosedDay(String ymd) {
        return ymdSet.contains(ymd);
    }

    public void clear(){
        ymdSet.clear();
    }


    public void loadFile(String filePath){

    }

    public void outFile(String filePath){
        String [] arrays = ymdSet.toArray(new String[0]);



    }


    public static void main(String[] args) {
        String []  ymds = {"20220101","20210101"};
    }


}
