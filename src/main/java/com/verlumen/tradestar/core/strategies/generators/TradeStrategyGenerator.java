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

public class TradeStrategyGenerator {
  private final ImmutableMap<StrategyOneOfCase, TradeStrategyAdapter> adapters;

  @Inject
  TradeStrategyGenerator(Set<TradeStrategyAdapter> adapters) {
    this.adapters = Maps.uniqueIndex(adapters, TradeStrategyAdapter::strategyOneOfCase);
  }

  public Stream<TradeStrategy> generate() {
    return generate(ignored -> true);
  }

  public Stream<TradeStrategy> generate(StrategyOneOfCase... oneOfCases) {
    return generate(ImmutableSet.copyOf(oneOfCases));
  }

  public Stream<TradeStrategy> generate(ImmutableSet<StrategyOneOfCase> oneOfCases) {
    return generate(adapter -> oneOfCases.contains(adapter.strategyOneOfCase()));
  }

  public Stream<TradeStrategy> generate(Predicate<TradeStrategyAdapter> adapterPredicate) {
    return adapters.values().stream()
        .filter(adapterPredicate)
        .flatMap(TradeStrategyAdapter::generate);
  }
}
