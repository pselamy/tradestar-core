package com.verlumen.tradestar.core.backtesting;

import com.google.common.collect.ImmutableList;
import com.verlumen.tradestar.protos.candles.Candle;
import org.ta4j.core.BarSeriesManager;

interface BarSeriesManagerFactory {
    BarSeriesManager create(ImmutableList<Candle> candles);
}
