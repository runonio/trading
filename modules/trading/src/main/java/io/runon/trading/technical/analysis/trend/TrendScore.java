package io.runon.trading.technical.analysis.trend;

import java.math.BigDecimal;

/**
 * @author macle
 */
public class TrendScore {


    private BigDecimal upwardScore ;
    private BigDecimal downwardScore;

    public TrendScore(){
        upwardScore = new BigDecimal(101);
        downwardScore = new BigDecimal(99);
    }

    public TrendScore(BigDecimal upwardScore, BigDecimal downwardScore){
        this.upwardScore = upwardScore;
        this.downwardScore = downwardScore;
    }

    public void setUpwardScore(BigDecimal upwardScore) {
        this.upwardScore = upwardScore;
    }

    public void setDownwardScore(BigDecimal downwardScore) {
        this.downwardScore = downwardScore;
    }


    public BigDecimal getUpwardScore() {
        return upwardScore;
    }

    public BigDecimal getDownwardScore() {
        return downwardScore;
    }

    public TrendType getTrendType(BigDecimal score){
        if(score == null){
            return TrendType.NONE;
        }

        if(score.compareTo(upwardScore) >= 0){
            return TrendType.UPWARD;
        }

        if(score.compareTo(downwardScore) <= 0){
            return TrendType.DOWNWARD;
        }


        return TrendType.SIDEWAYS;
    }

}
