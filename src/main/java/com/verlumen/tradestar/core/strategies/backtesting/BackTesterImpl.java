package com.verlumen.tradestar.core.strategies.backtesting;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.verlumen.tradestar.core.backtesting.BackTester;
import com.verlumen.tradestar.core.strategies.adapters.TradeStrategyAdapter;
import com.verlumen.tradestar.protos.candles.Candle;
import com.verlumen.tradestar.protos.candles.Granularity;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategyTestResult;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BarSeriesManager;
import org.ta4j.core.TradingRecord;

import java.util.Set;

class BackTesterImpl implements BackTester {
  private final ImmutableMap<TradeStrategy.StrategyOneOfCase, TradeStrategyAdapter> adapters;
  private final TestResultFactory testResultFactory;

  @Inject
  BackTesterImpl(Set<TradeStrategyAdapter> adapters, TestResultFactory testResultFactory) {
    this.adapters = Maps.uniqueIndex(adapters, TradeStrategyAdapter::strategyOneOfCase);
    this.testResultFactory = testResultFactory;
  }

  @Override
  public TradeStrategyTestResult test(TestParams request) {
    BarSeriesManager seriesManager =
        createBarSeriesManager(request.granularity(), request.candles());
    BarSeries series = seriesManager.getBarSeries();
    TradeStrategy strategy = request.strategy();
    TradingRecord record = seriesManager.run(getAdapter(strategy).toTa4jStrategy(strategy, series));
    return testResultFactory.create(series, record);
  }

  private BarSeriesManager createBarSeriesManager(
      Granularity granularity, ImmutableSet<Candle> candles) {
    return new BarSeriesManager(BarSeriesFactory.create(params.granularity(), params.candles()));
  }

  private TradeStrategyAdapter getAdapter(TradeStrategy strategy) {
    return adapters.get(strategy.getStrategyOneOfCase());
  }
}
