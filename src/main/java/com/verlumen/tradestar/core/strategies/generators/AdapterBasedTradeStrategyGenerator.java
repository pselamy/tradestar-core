package com.verlumen.tradestar.core.strategies.generators;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.verlumen.tradestar.core.strategies.adapters.CompositeTradeStrategyAdapter;
import com.verlumen.tradestar.core.strategies.adapters.StrategyNegationHandler;
import com.verlumen.tradestar.core.strategies.adapters.TradeStrategyAdapter;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategy.StrategyOneOfCase;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class AdapterBasedTradeStrategyGenerator implements TradeStrategyGenerator {
  private final ImmutableSet<TradeStrategyAdapter> adapters;
  private final StrategyNegationHandler negationHandler;

  @Inject
  AdapterBasedTradeStrategyGenerator(
      Set<TradeStrategyAdapter> adapters, StrategyNegationHandler negationHandler) {
    this.adapters = ImmutableSet.<TradeStrategyAdapter>builder().addAll(adapters).build();

    this.negationHandler = negationHandler;
  }

  @Override
  public Stream<TradeStrategy> generate(
      ImmutableSet<StrategyOneOfCase> oneOfCases, Directive directive) {
    return generate(adapter -> oneOfCases.contains(adapter.strategyOneOfCase()), directive);
  }

  private Stream<TradeStrategy> generate(
      Predicate<TradeStrategyAdapter> adapterPredicate, Directive directive) {
    ImmutableSet.Builder<TradeStrategyAdapter> adapters =
        ImmutableSet.<TradeStrategyAdapter>builder().addAll(this.adapters);

    switch (directive) {
      case INCLUDE_COMPOSITE:
        adapters.add(CompositeTradeStrategyAdapter.create(adapters.build(), negationHandler));
        break;
      case UNSPECIFIED:
      default:
        break;
    }

    return adapters.build().stream()
        .filter(adapterPredicate)
        .flatMap(TradeStrategyAdapter::generate);
  }
}
