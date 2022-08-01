package com.verlumen.tradestar.core.ta.strategies;

import com.google.common.collect.ImmutableList;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;

import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.MoreCollectors.onlyElement;

enum TradeStrategyIdentifier {
  ADX(
      params ->
          String.format(
              "ADX-%d-BUY-%s-SELL-%s",
              params.getAdx().getBarCount(),
              params.getAdx().getBuySignalStrength(),
              params.getAdx().getSellSignalStrength()),
      ImmutableList.of(
          params -> params.getAdx().getBarCount() > 0,
          params -> params.getAdx().getBuySignalStrengthValue() > 0,
          params -> params.getAdx().getBuySignalStrengthValue() > 0));

  private final Function<TradeStrategy, String> nameFunction;
  private final Predicate<TradeStrategy> validator;

  TradeStrategyIdentifier(
      Function<TradeStrategy, String> nameFunction,
      ImmutableList<Predicate<TradeStrategy>> validators) {
    this.nameFunction = nameFunction;
    this.validator = validators.stream().reduce(Predicate::and).stream().collect(onlyElement());
  }

  static TradeStrategyIdentifier get(TradeStrategy.StrategyOneOfCase supportedCase) {
    return TradeStrategyIdentifier.valueOf(supportedCase.name());
  }

  String name(TradeStrategy tradeStrategy) {
    checkArgument(validator.test(tradeStrategy));
    return nameFunction.apply(tradeStrategy);
  }

  TradeStrategy.StrategyOneOfCase supportedCase() {
    return TradeStrategy.StrategyOneOfCase.valueOf(name());
  }
}
