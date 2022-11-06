package com.verlumen.tradestar.core.backtesting;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.verlumen.tradestar.core.candles.GranularitySpec;
import com.verlumen.tradestar.core.shared.CandleComparators;
import com.verlumen.tradestar.core.strategies.adapters.TradeStrategyAdapter;
import com.verlumen.tradestar.core.tradehistory.BarSeriesFactory;
import com.verlumen.tradestar.protos.candles.Candle;
import com.verlumen.tradestar.protos.candles.CandleDescriptor;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategyTestResult;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BarSeriesManager;
import org.ta4j.core.Strategy;
import org.ta4j.core.TradingRecord;

import java.io.Serializable;
import java.util.Set;

import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Iterables.getLast;
import static com.google.common.collect.MoreCollectors.onlyElement;
import static java.time.Instant.ofEpochSecond;

public class Backtester implements Serializable {
  private final ImmutableSet<TradeStrategyAdapter> adapters;
  private final TestResultFactory testResultFactory;

  @Inject
  Backtester(Set<TradeStrategyAdapter> adapters, TestResultFactory testResultFactory) {
    this.adapters = ImmutableSet.copyOf(adapters);
    this.testResultFactory = testResultFactory;
  }

  private static BarSeriesManager createBarSeriesManager(ImmutableSet<Candle> candles) {
    return BarSeriesManagerFactory.create(candles);
  }

  private static Strategy createStrategy(
      TradeStrategy strategy, BarSeries series, ImmutableSet<TradeStrategyAdapter> adapters) {
    return adapters.stream()
        .filter(adapter -> adapter.strategyOneOfCase().equals(strategy.getStrategyOneOfCase()))
        .collect(onlyElement())
        .toTa4jStrategy(strategy, series);
  }

  public TradeStrategyTestResult test(Params params) {
    CandleDescriptor candleDescriptor =
        params.candles().stream()
            .map(Candle::getCandleDescriptor)
            .distinct()
            .collect(onlyElement());
    ImmutableSet<Candle> candles =
        params.candles().stream()
            .sorted(CandleComparators.START_TIME_ASCENDING)
            .collect(toImmutableSet());
    BarSeriesManager seriesManager = createBarSeriesManager(candles);
    BarSeries series = seriesManager.getBarSeries();
    Strategy strategy = createStrategy(params.strategy(), series, adapters);
    TradingRecord record = seriesManager.run(strategy);
    GranularitySpec granularitySpec = GranularitySpec.create(candleDescriptor.getGranularity());
    return testResultFactory.create(
        ofEpochSecond(getStartSeconds(getFirst(candles, Candle.getDefaultInstance()))),
        ofEpochSecond(getStartSeconds(getLast(candles)) + granularitySpec.seconds()),
        candleDescriptor,
        params.strategy(),
        series,
        record);
  }

  private long getStartSeconds(Candle candle) {
    return candle.getStart().getSeconds();
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

  @AutoValue
  public abstract static class Params {
    public static Params create(ImmutableSet<Candle> candles, TradeStrategy strategy) {
      return new AutoValue_Backtester_Params(candles, strategy);
    }

    abstract ImmutableSet<Candle> candles();

    abstract TradeStrategy strategy();
  }
}
