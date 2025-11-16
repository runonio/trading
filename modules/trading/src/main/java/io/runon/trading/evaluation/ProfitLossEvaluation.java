package io.runon.trading.evaluation;

import io.runon.commons.math.BigDecimals;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 성능평가
 * 관련 평가는 profit/loss ratio * Winning Ratio 의 값으로 평가 합니다.
 * 이익과 손실을 활용하여 성능을 평가한다
 *
 * @author macle
 */
@SuppressWarnings("SizeReplaceableByIsEmpty")
public class ProfitLossEvaluation {

    private final List<BigDecimal> profitList = new ArrayList<>();

    private final List<BigDecimal> lossList= new ArrayList<>();



    public void addProfit(BigDecimal profit){
        profitList.add(profit);
    }

    public void addLoss(BigDecimal loss){
        lossList.add(loss.abs());
    }

    public BigDecimal getWinningRatio(){

        if(profitList.size() == 0){
            return BigDecimal.ZERO;
        }

        if(lossList.size() == 0){
            return BigDecimals.DECIMAL_100;
        }

        return new BigDecimal(profitList.size()).divide(new BigDecimal(lossList.size()), 4, RoundingMode.HALF_UP );
    }
    public BigDecimal getWinningPercent(){

        if(profitList.size() == 0){
            return BigDecimal.ZERO;
        }

        if(lossList.size() == 0){
            return BigDecimals.DECIMAL_100;
        }

        return getWinningRatio().multiply(BigDecimals.DECIMAL_100).stripTrailingZeros();
    }



    public BigDecimal getProfitLossRatio(){

        if(profitList.size() == 0){
            return BigDecimal.ZERO;
        }

        if(lossList.size() == 0){
            return BigDecimals.DECIMAL_100;
        }



        BigDecimal profitSum = BigDecimal.ZERO;
        for(BigDecimal profit : profitList){
            profitSum = profitSum.add(profit);
        }

        BigDecimal profitAvg = profitSum.divide(new BigDecimal(profitList.size()), MathContext.DECIMAL128);

        BigDecimal lossSum = BigDecimal.ZERO;
        for(BigDecimal loss : lossList){
            lossSum = lossSum.add(loss);
        }

        BigDecimal lossAvg = lossSum.divide(new BigDecimal(lossList.size()), MathContext.DECIMAL128);
        return profitAvg.divide(lossAvg, 4, RoundingMode.HALF_UP).stripTrailingZeros();
    }

    public BigDecimal getScore(){
        return getWinningRatio().multiply(getProfitLossRatio()).setScale(4, RoundingMode.HALF_UP).stripTrailingZeros();
    }

}
