package io.runon.trading.technical.analysis.indicators.ma;

import io.runon.trading.TimeNumber;
import io.runon.trading.technical.analysis.indicators.NIndicators;

import java.math.BigDecimal;

/**
 * 기울기 이동평균
 * Gradient Moving Average
 * 다이버전스를 어떻게 구하는게 좋을지 연구.
 *
 * @author macle
 */
public class Gma extends NIndicators<TimeNumber> {

    int initN  = 200 ;
    int continueN = 10;

    public void setInitN(int initN) {
        this.initN = initN;
    }

    public void setContinueN(int continueN) {
        this.continueN = continueN;
    }

    @Override
    public BigDecimal get(TimeNumber[] array, int n, int index) {





        return null;
    }


}
