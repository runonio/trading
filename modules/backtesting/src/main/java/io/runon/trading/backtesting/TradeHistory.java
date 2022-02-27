package io.runon.trading.backtesting;

import io.runon.trading.strategy.Position;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 트레이드 이력
 * @author ccsweets
 */
@Getter
@Setter
@AllArgsConstructor
public class TradeHistory {
   private long time;
   private Position position;
}
