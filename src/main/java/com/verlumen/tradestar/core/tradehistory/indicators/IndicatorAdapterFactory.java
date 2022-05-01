package com.verlumen.tradestar.core.tradehistory.indicators;

import com.verlumen.tradestar.protos.indicators.Indicator;
import org.ta4j.core.BarSeries;

interface IndicatorAdapterFactory {
    IndicatorAdapter create(BarSeries barSeries, Indicator.Params params);

    Indicator.Params.TypeCase typeCase();
}
