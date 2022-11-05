package com.verlumen.tradestar.core.strategies.generators;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.verlumen.tradestar.core.strategies.adapters.TradeStrategyAdapter;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategy.StrategyOneOfCase;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class AdapterBasedTradeStrategyGenerator implements TradeStrategyGenerator {
  private final ImmutableMap<StrategyOneOfCase, TradeStrategyAdapter> adapters;

  @Inject
  AdapterBasedTradeStrategyGenerator(Set<TradeStrategyAdapter> adapters) {
    this.adapters = Maps.uniqueIndex(adapters, TradeStrategyAdapter::strategyOneOfCase);
  }


  @Override
  public Stream<TradeStrategy> generate(ImmutableSet<StrategyOneOfCase> oneOfCases) {
    return generate(adapter -> oneOfCases.contains(adapter.strategyOneOfCase()));
  }

  private Stream<TradeStrategy> generate(Predicate<TradeStrategyAdapter> adapterPredicate) {
    return adapters.values().stream()
        .filter(adapterPredicate)
        .flatMap(TradeStrategyAdapter::generate);
  }
}
