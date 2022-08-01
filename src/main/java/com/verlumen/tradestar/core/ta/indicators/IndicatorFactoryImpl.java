package com.verlumen.tradestar.core.ta.indicators;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategy.StrategyOneOfCase;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;

import java.util.Set;

import static com.google.common.collect.MoreCollectors.onlyElement;
import static com.google.mu.util.stream.BiStream.biStream;
import static java.util.stream.Stream.ofNullable;

class IndicatorFactoryImpl implements IndicatorFactory {
  private final ImmutableMap<StrategyOneOfCase, Adapter> adapters;

  @Inject
  IndicatorFactoryImpl(Set<Adapter> adapters) {
    this.adapters = ImmutableMap.copyOf(biStream(adapters).mapKeys(Adapter::supportedCase).toMap());
  }

  @Override
  public Indicator<Num> create(TradeStrategy strategy, BarSeries series) {
    return ofNullable(adapters.get(strategy.getStrategyOneOfCase()))
        .map(adapter -> adapter.indicator(strategy, series))
        .collect(onlyElement());
  }
}
