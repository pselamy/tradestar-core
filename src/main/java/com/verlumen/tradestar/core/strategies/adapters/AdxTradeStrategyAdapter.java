package com.verlumen.tradestar.core.strategies.adapters;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Range;
import com.google.inject.Inject;
import com.verlumen.tradestar.protos.strategies.SignalStrength;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategy.StrategyOneOfCase;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.indicators.adx.ADXIndicator;

import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static com.google.common.collect.MoreCollectors.onlyElement;
import static com.google.common.collect.Sets.combinations;
import static java.util.Arrays.stream;
import static java.util.Collections.max;
import static java.util.Collections.min;

class AdxTradeStrategyAdapter implements TradeStrategyAdapter {
  private final StrategyNegationHandler negationHandler;

  @Inject
  AdxTradeStrategyAdapter(StrategyNegationHandler negationHandler) {
    this.negationHandler = negationHandler;
  }

  @Override
  public Stream<TradeStrategy> generate() {
    //noinspection UnstableApiUsage
    return Fibonacci.fibonacciRange(6, 9).stream()
        .flatMap(
            barCount ->
                combinations(AdxLevel.signalStrengths(), 2).stream()
                    .map(
                        subset ->
                            TradeStrategy.ADX
                                .newBuilder()
                                .setBarCount(barCount)
                                .setBuySignalStrength(max(subset))
                                .setSellSignalStrength(min(subset))
                                .build()))
        .map(adxParams -> TradeStrategy.newBuilder().setAdx(adxParams).build());
  }

  @Override
  public Strategy toTa4jStrategy(TradeStrategy tradeStrategy, BarSeries barSeries) {
    checkArgument(tradeStrategy.getStrategyOneOfCase() == StrategyOneOfCase.ADX);
    TradeStrategy.ADX params = tradeStrategy.getAdx();
    ADXIndicator adxIndicator = new ADXIndicator(barSeries, params.getBarCount());
    Rule buyRule =
        (i, tradingRecord) -> {
          double adxValue = adxIndicator.getValue(i).doubleValue();
          SignalStrength signalStrength = AdxLevel.of(adxValue).signalStrength;
          return signalStrength.getNumber() >= params.getBuySignalStrength().getNumber();
        };
    Rule sellRule =
        (i, tradingRecord) -> {
          double adxValue = adxIndicator.getValue(i).doubleValue();
          SignalStrength signalStrength = AdxLevel.of(adxValue).signalStrength;
          return signalStrength.getNumber() <= params.getSellSignalStrength().getNumber();
        };
    BaseStrategy strategy = new BaseStrategy(buyRule, sellRule);
    return negationHandler.negate(strategy, tradeStrategy.getNegation());
  }

  @Override
  public StrategyOneOfCase strategyOneOfCase() {
    return StrategyOneOfCase.ADX;
  }

  // https://www.investopedia.com/articles/trading/07/adx-trend-indicator.asp
  private enum AdxLevel {
    WEAK(0, 25),
    STRONG(25, 50),
    VERY_STRONG(50, 75),
    EXTREMELY_STRONG(75, 100);

    private final Range<Double> range;
    private final SignalStrength signalStrength;

    AdxLevel(double fromInclusive, double toExclusive) {
      this.range = Range.closedOpen(fromInclusive, toExclusive);
      this.signalStrength = SignalStrength.valueOf(name());
    }

    private static AdxLevel of(double value) {
      return Stream.of(values())
          .filter(level -> level.range.contains(value))
          .collect(onlyElement());
    }

    private static ImmutableSet<SignalStrength> signalStrengths() {
      return stream(values()).map(level -> level.signalStrength).collect(toImmutableSet());
    }
  }
}
