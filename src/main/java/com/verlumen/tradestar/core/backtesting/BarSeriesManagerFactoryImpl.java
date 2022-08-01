package com.verlumen.tradestar.core.backtesting;

import com.google.common.collect.ImmutableList;
import com.verlumen.tradestar.core.tradehistory.BarSeriesFactory;
import com.verlumen.tradestar.protos.candles.Candle;
import org.ta4j.core.BarSeriesManager;

class BarSeriesManagerFactoryImpl implements BarSeriesManagerFactory {
    @Override
    public BarSeriesManager create(ImmutableList<Candle> candles) {
        return new BarSeriesManager(BarSeriesFactory.create(candles));
    }
}
