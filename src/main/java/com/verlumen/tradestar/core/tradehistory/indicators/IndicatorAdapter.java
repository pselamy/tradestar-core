package com.verlumen.tradestar.core.tradehistory.indicators;

import com.verlumen.tradestar.protos.indicators.Indicator;
import org.ta4j.core.num.Num;

public interface IndicatorAdapter {
    Indicator indicator(int index);

    org.ta4j.core.Indicator<Num> ta4jIndicator();
}
