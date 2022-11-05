package com.verlumen.tradestar.core.strategies.adapters;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Strategy;

import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static com.google.common.collect.MoreCollectors.onlyElement;
import static com.google.common.collect.Sets.powerSet;
import static com.google.common.collect.Sets.union;
import static com.verlumen.tradestar.protos.strategies.TradeStrategy.*;
import static com.verlumen.tradestar.protos.strategies.TradeStrategy.Composite.Joiner;
import static java.util.EnumSet.allOf;

class CompositeTradeStrategyAdapter implements TradeStrategyAdapter {
  private static final ImmutableMap<Joiner, StrategyJoiner> JOINERS =
      ImmutableMap.of(
          Joiner.AND, StrategyJoiner.create(Strategy::and),
          Joiner.OR, StrategyJoiner.create(Strategy::or),
          Joiner.XOR,
              StrategyJoiner.create(
                  (strategy1, strategy2) ->
                      strategy1.and(strategy2).opposite().and(strategy1.or(strategy2))));

  private final Provider<Set<TradeStrategyAdapter>> adapters;
  private final StrategyNegationHandler negationHandler;

  @Inject
  CompositeTradeStrategyAdapter(
      Provider<Set<TradeStrategyAdapter>> adapters, StrategyNegationHandler negationHandler) {
    this.adapters = adapters;
    this.negationHandler = negationHandler;
  }

  @Override
  public Stream<TradeStrategy> generate() {
    ImmutableSet<TradeStrategy> baseCompositeStrategies = compositeStrategies(strategies());
    ImmutableSet<TradeStrategy> secondaryCompositeStrategies =
        compositeStrategies(baseCompositeStrategies);
    return union(baseCompositeStrategies, secondaryCompositeStrategies).stream();
  }

  @Override
  public Strategy toTa4jStrategy(TradeStrategy compositeStrategy, BarSeries barSeries) {
    checkArgument(compositeStrategy.getStrategyOneOfCase() == StrategyOneOfCase.COMPOSITE);
    checkArgument(compositeStrategy.getComposite().getJoiner() != Joiner.UNRECOGNIZED);
    checkArgument(compositeStrategy.getComposite().getJoiner() != Joiner.UNSPECIFIED);
    ImmutableList<TradeStrategy> tradeStrategies =
        ImmutableList.copyOf(compositeStrategy.getComposite().getStrategiesList());
    Joiner joiner = compositeStrategy.getComposite().getJoiner();
    ImmutableSet<Strategy> strategies =
        tradeStrategies.stream()
            .map(
                tradeStrategy -> getAdapter(tradeStrategy).toTa4jStrategy(tradeStrategy, barSeries))
            .collect(toImmutableSet());
    compositeStrategy.getComposite().getJoiner();
    Strategy strategy = getStrategyJoiner(joiner).join(strategies);
    return negationHandler.negate(strategy, compositeStrategy.getNegation());
  }

  private TradeStrategyAdapter getAdapter(TradeStrategy tradeStrategy) {
    return adapters.get().stream()
        .filter(adapter -> adapter.strategyOneOfCase().equals(tradeStrategy.getStrategyOneOfCase()))
        .collect(onlyElement());
  }

  private StrategyJoiner getStrategyJoiner(Joiner joiner) {
    return JOINERS.get(joiner);
  }

  private ImmutableSet<TradeStrategy> compositeStrategies(ImmutableSet<TradeStrategy> strategies) {
    return powerSet(strategies).stream()
        .filter(tradeStrategies -> tradeStrategies.size() > 1)
        .flatMap(
            tradeStrategies ->
                allOf(Joiner.class).stream()
                    .filter(joiner -> !joiner.equals(Joiner.UNSPECIFIED))
                    .map(
                        joiner ->
                            Composite.newBuilder()
                                .setJoiner(joiner)
                                .addAllStrategies(tradeStrategies)))
        .map(composite -> newBuilder().setComposite(composite).build())
        .collect(toImmutableSet());
  }

  private ImmutableSet<TradeStrategy> strategies() {
    ImmutableSet<TradeStrategy> strategies =
        adapters.get().stream().flatMap(TradeStrategyAdapter::generate).collect(toImmutableSet());
    ImmutableSet<TradeStrategy> negatedStrategies =
        allOf(Negation.class).stream()
            .filter(negation -> !negation.equals(Negation.UNSPECIFIED))
            .flatMap(
                negation ->
                    strategies.stream()
                        .map(strategy -> strategy.toBuilder().setNegation(negation).build()))
            .collect(toImmutableSet());
    return union(strategies, negatedStrategies).immutableCopy();
  }

  @Override
  public StrategyOneOfCase strategyOneOfCase() {
    return StrategyOneOfCase.COMPOSITE;
  }

  @AutoValue
  abstract static class StrategyJoiner {
    static StrategyJoiner create(BinaryOperator<Strategy> operator) {
      return new AutoValue_CompositeTradeStrategyAdapter_StrategyJoiner(operator);
    }

    abstract BinaryOperator<Strategy> operator();

    Strategy join(ImmutableSet<Strategy> strategies) {
      return strategies.stream().reduce(operator()).stream().collect(onlyElement());
    }
  }
}
