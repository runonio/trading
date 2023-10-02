package io.runon.trading;

import lombok.Data;

/**
 * 문자열과 숫자 정보 클래스
 * @author macle
 */
@Data
public class YearQuarter {

    public static final YearQuarter [] EMPTY_ARRAY = new YearQuarter[0];

    int year;
    int quarter;

    public YearQuarter(){

    }

    public YearQuarter(int year, int quarter){
        this.year = year;
        this.quarter = quarter;
    }

    public int compareTo( YearQuarter y){

        if(year == y.year){
            return Integer.compare(quarter, y.quarter);
        }

        return Integer.compare(year, y.year);
    }

    public boolean equals( YearQuarter y){
        if(year != y.year){
            return false;
        }

        //noinspection RedundantIfStatement
        if(quarter != y.quarter){
            return false;
        }

        return true;
    }


    @Override
    public String toString(){
        return year + "," +quarter;
    }
}
