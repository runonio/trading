package io.runon.trading.technical.analysis.similarity;

import io.runon.trading.TimeChangePercent;

/**
 * @author macle
 */
public interface TimeChangeGet {
    String getId();

    TimeChangePercent[] getChangeArray();
}
