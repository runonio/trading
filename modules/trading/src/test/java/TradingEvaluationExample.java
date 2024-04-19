import io.runon.trading.evaluation.ProfitLossEvaluation;

import java.math.BigDecimal;

/**
 * @author macle
 */
public class TradingEvaluationExample {
    public static void main(String[] args) {
        ProfitLossEvaluation evaluation = new ProfitLossEvaluation();

        evaluation.addProfit(new BigDecimal("100"));
        evaluation.addProfit(new BigDecimal("3"));
        evaluation.addProfit(new BigDecimal("5"));
        evaluation.addProfit(new BigDecimal("60"));
        evaluation.addProfit(new BigDecimal("70"));

        evaluation.addLoss(new BigDecimal("1"));
        evaluation.addLoss(new BigDecimal("1"));
        evaluation.addLoss(new BigDecimal("1"));
        evaluation.addLoss(new BigDecimal("1"));
        evaluation.addLoss(new BigDecimal("1"));
        evaluation.addLoss(new BigDecimal("2"));
        evaluation.addLoss(new BigDecimal("1"));
        evaluation.addLoss(new BigDecimal("3"));
        evaluation.addLoss(new BigDecimal("2"));
        evaluation.addLoss(new BigDecimal("0.5"));
        evaluation.addLoss(new BigDecimal("2"));
        evaluation.addLoss(new BigDecimal("1"));
        evaluation.addLoss(new BigDecimal("1"));
        evaluation.addLoss(new BigDecimal("1"));
        evaluation.addLoss(new BigDecimal("1"));
        evaluation.addLoss(new BigDecimal("1"));

        System.out.println(evaluation.getWinningPercent());
        System.out.println(evaluation.getProfitLossRatio());

        System.out.println(evaluation.getScore());
    }
}
