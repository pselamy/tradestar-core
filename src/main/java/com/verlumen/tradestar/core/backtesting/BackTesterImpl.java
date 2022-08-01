package com.verlumen.tradestar.core.backtesting;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.verlumen.tradestar.core.ta.strategies.StrategyFactory;
import com.verlumen.tradestar.protos.candles.Candle;
import com.verlumen.tradestar.protos.candles.Granularity;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategyTestResult;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BarSeriesManager;
import org.ta4j.core.Strategy;
import org.ta4j.core.TradingRecord;

class BackTesterImpl implements BackTester {
  private final BarSeriesManagerFactory barSeriesManagerFactory;
  private final StrategyFactory strategyFactory;
  private final TestResultFactory testResultFactory;

  @Inject
  BackTesterImpl(
      BarSeriesManagerFactory barSeriesManagerFactory,
      StrategyFactory strategyFactory,
      TestResultFactory testResultFactory) {
    this.barSeriesManagerFactory = barSeriesManagerFactory;
    this.strategyFactory = strategyFactory;
    this.testResultFactory = testResultFactory;
  }

  @Override
  public TradeStrategyTestResult test(TestParams request) {
    BarSeriesManager seriesManager =
        createBarSeriesManager(request.granularity(), request.candles());
    BarSeries series = seriesManager.getBarSeries();
    TradingRecord record = seriesManager.run(createStrategy(request.strategy(), series));
    return testResultFactory.create(series, record);
  }

  private BarSeriesManager createBarSeriesManager(
      Granularity granularity, ImmutableSet<Candle> candles) {
    return barSeriesManagerFactory.create(
        BarSeriesManagerFactory.CreateParams.builder()
            .setCandles(candles)
            .setGranularity(granularity)
            .build());
  }

  private Strategy createStrategy(TradeStrategy strategy, BarSeries series) {
    return strategyFactory.create(strategy, series);
  }
}
