package com.verlumen.tradestar.core.backtesting;

import com.verlumen.tradestar.core.tradehistory.BarSeriesFactory;
import org.ta4j.core.BarSeriesManager;

class BarSeriesManagerFactoryImpl implements BarSeriesManagerFactory {
  @Override
  public BarSeriesManager create(CreateParams params) {
    return new BarSeriesManager(BarSeriesFactory.create(params.granularity(), params.candles()));
  }
}
