package com.verlumen.tradestar.core.backtesting;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.verlumen.tradestar.core.shared.CandleComparators;
import com.verlumen.tradestar.core.strategies.adapters.TradeStrategyAdapter;
import com.verlumen.tradestar.core.tradehistory.BarSeriesFactory;
import com.verlumen.tradestar.protos.candles.Candle;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategy.StrategyOneOfCase;
import com.verlumen.tradestar.protos.strategies.TradeStrategyTestResult;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BarSeriesManager;
import org.ta4j.core.Strategy;
import org.ta4j.core.TradingRecord;

import java.util.Set;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

class BacktesterImpl implements Backtester {
  private final ImmutableMap<StrategyOneOfCase, TradeStrategyAdapter> adapters;
  private final TestResultFactory testResultFactory;

  @Inject
  BacktesterImpl(Set<TradeStrategyAdapter> adapters, TestResultFactory testResultFactory) {
    this.adapters = Maps.uniqueIndex(adapters, TradeStrategyAdapter::strategyOneOfCase);
    this.testResultFactory = testResultFactory;
  }

  @Override
  public TradeStrategyTestResult test(Params params) {
    BarSeriesManager seriesManager = createBarSeriesManager(params.candles());
    BarSeries series = seriesManager.getBarSeries();
    Strategy strategy = createStrategy(params.strategy(), series);
    TradingRecord record = seriesManager.run(strategy);
    return testResultFactory.create(series, record);
  }

  private BarSeriesManager createBarSeriesManager(ImmutableSet<Candle> candles) {
    return BarSeriesManagerFactory.create(candles);
  }

  private Strategy createStrategy(TradeStrategy strategy, BarSeries series) {
    return getTradeStrategyAdapter(strategy).toTa4jStrategy(strategy, series);
  }

  private TradeStrategyAdapter getTradeStrategyAdapter(TradeStrategy strategy) {
    return adapters.get(strategy.getStrategyOneOfCase());
  }

  static class BarSeriesManagerFactory {
    static BarSeriesManager create(ImmutableSet<Candle> candles) {
      return new BarSeriesManager(
          BarSeriesFactory.create(
              candles.stream()
                  .sorted(CandleComparators.START_TIME_ASCENDING)
                  .collect(toImmutableSet())));
    }
  }
}
