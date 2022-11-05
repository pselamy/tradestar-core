package com.verlumen.tradestar.core.backtesting;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.serializable.SerializableAutoValue;
import com.google.common.collect.ImmutableSet;
import com.verlumen.tradestar.core.shared.CandleComparators;
import com.verlumen.tradestar.core.strategies.adapters.TradeStrategyAdapter;
import com.verlumen.tradestar.core.tradehistory.BarSeriesFactory;
import com.verlumen.tradestar.protos.candles.Candle;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategyTestResult;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BarSeriesManager;
import org.ta4j.core.Strategy;
import org.ta4j.core.TradingRecord;

import java.io.Serializable;
import java.util.function.Function;

import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static com.google.common.collect.MoreCollectors.onlyElement;

@AutoValue
@SerializableAutoValue
public abstract class Backtester implements Serializable {
  public static Backtester create(
      ImmutableSet<TradeStrategyAdapter> adapters, TestResultFactory testResultFactory) {
    return create(params -> runTest(params, testResultFactory, adapters));
  }

  public static Backtester create(Function<Params, TradeStrategyTestResult> testResultFunction) {
    return new AutoValue_Backtester(testResultFunction);
  }

  private static TradeStrategyTestResult runTest(
      Params params,
      TestResultFactory testResultFactory,
      ImmutableSet<TradeStrategyAdapter> adapters) {
    BarSeriesManager seriesManager = createBarSeriesManager(params.candles());
    BarSeries series = seriesManager.getBarSeries();
    Strategy strategy = createStrategy(params.strategy(), series, adapters);
    TradingRecord record = seriesManager.run(strategy);
    return testResultFactory.create(series, record);
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

  abstract Function<Params, TradeStrategyTestResult> testResultFunction();

  public TradeStrategyTestResult test(Params params) {
    return testResultFunction().apply(params);
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
