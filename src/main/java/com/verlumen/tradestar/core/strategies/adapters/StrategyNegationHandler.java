package com.verlumen.tradestar.core.strategies.adapters;

import com.google.common.base.Strings;
import com.verlumen.tradestar.protos.strategies.TradeStrategy.Negation;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Strategy;

import java.io.Serializable;
import java.util.function.BiFunction;

import static com.google.common.base.Preconditions.checkArgument;

public class StrategyNegationHandler
    implements BiFunction<Strategy, Negation, Strategy>, Serializable {
  @Override
  public Strategy apply(Strategy strategy, Negation negation) {
    checkArgument(!Strings.isNullOrEmpty(strategy.getName()));
    String name = strategy.getName() + "-OPPOSITE_" + negation.name();

    switch (negation) {
      case ENTRY_RULE:
        return new BaseStrategy(strategy.getEntryRule().negation(), strategy.getExitRule());
      case EXIT_RULE:
        return new BaseStrategy(strategy.getEntryRule(), strategy.getExitRule().negation());
      case ALL_RULES:
        return new BaseStrategy(
            name, strategy.getEntryRule().negation(), strategy.getExitRule().negation());
      default:
        return strategy;
    }
  }
}
