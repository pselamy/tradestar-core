package com.verlumen.tradestar.core.backtesting;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.verlumen.tradestar.core.strategies.adapters.TradeStrategyAdapter;
import com.verlumen.tradestar.protos.candles.Candle;
import com.verlumen.tradestar.protos.candles.Granularity;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategy.StrategyOneOfCase;
import com.verlumen.tradestar.protos.strategies.TradeStrategyTestResult;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BarSeriesManager;
import org.ta4j.core.Strategy;
import org.ta4j.core.TradingRecord;

import java.util.Set;

class BackTesterImpl implements BackTester {
  private final ImmutableMap<StrategyOneOfCase, TradeStrategyAdapter> adapters;
  private final BarSeriesManagerFactory barSeriesManagerFactory;
  private final TestResultFactory testResultFactory;

  @Inject
  BackTesterImpl(
      Set<TradeStrategyAdapter> adapters,
      BarSeriesManagerFactory barSeriesManagerFactory,
      TestResultFactory testResultFactory) {
    this.adapters = Maps.uniqueIndex(adapters, TradeStrategyAdapter::strategyOneOfCase);
    this.barSeriesManagerFactory = barSeriesManagerFactory;
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
    return getTradeStrategyAdapter(strategy).toTa4jStrategy(strategy, series);
  }

  private TradeStrategyAdapter getTradeStrategyAdapter(TradeStrategy strategy) {
    return adapters.get(strategy.getStrategyOneOfCase());
  }
}
