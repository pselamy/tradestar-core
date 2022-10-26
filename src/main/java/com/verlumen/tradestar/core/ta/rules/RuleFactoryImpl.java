package com.verlumen.tradestar.core.ta.rules;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategy.StrategyOneOfCase;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Rule;

import java.util.Set;

import static com.google.common.collect.MoreCollectors.onlyElement;
import static java.util.stream.Stream.ofNullable;

public class RuleFactoryImpl implements RuleFactory {
  private final ImmutableMap<StrategyOneOfCase, RuleAdapter> adapters;

  @Inject
  public RuleFactoryImpl(Set<RuleAdapter> adapters) {
    this.adapters = Maps.uniqueIndex(adapters, RuleAdapter::supportedCase);
  }

  @Override
  public Rule buyRule(TradeStrategy params, BarSeries barSeries) {
    return ofNullable(adapters.get(params.getStrategyOneOfCase()))
        .map(adapter -> adapter.buyRule(params, barSeries))
        .collect(onlyElement());
  }

  @Override
  public Rule sellRule(TradeStrategy params, BarSeries barSeries) {
    return ofNullable(adapters.get(params.getStrategyOneOfCase()))
        .map(adapter -> adapter.sellRule(params, barSeries))
        .collect(onlyElement());
  }
}
