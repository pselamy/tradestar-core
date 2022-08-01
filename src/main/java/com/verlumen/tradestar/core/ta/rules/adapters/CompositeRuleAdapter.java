package com.verlumen.tradestar.core.ta.rules.adapters;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.verlumen.tradestar.core.ta.rules.RuleFactory;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategy.StrategyOneOfCase;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Rule;

import java.util.function.BinaryOperator;

import static com.google.common.collect.MoreCollectors.onlyElement;
import static com.verlumen.tradestar.protos.strategies.TradeStrategy.Composite.Joiner;
import static java.util.stream.Stream.ofNullable;

class CompositeRuleAdapter implements RuleFactory.RuleAdapter {
  private static final ImmutableMap<Joiner, BinaryOperator<Rule>> REDUCERS =
      ImmutableMap.<Joiner, BinaryOperator<Rule>>builder()
          .put(Joiner.AND, Rule::and)
          .put(Joiner.OR, Rule::or)
          .put(Joiner.XOR, Rule::xor)
          .build();

  private final RuleFactory ruleFactory;

  @Inject
  CompositeRuleAdapter(RuleFactory ruleFactory) {
    this.ruleFactory = ruleFactory;
  }

  private static BinaryOperator<Rule> reducer(TradeStrategy params) {
    return ofNullable(REDUCERS.get(params.getComposite().getJoiner())).collect(onlyElement());
  }

  @Override
  public Rule buyRule(TradeStrategy params, BarSeries barSeries) {
    return params.getComposite().getStrategiesList().stream()
        .map(strategy -> ruleFactory.buyRule(strategy, barSeries))
        .reduce(reducer(params))
        .orElseThrow(IllegalArgumentException::new);
  }

  @Override
  public Rule sellRule(TradeStrategy params, BarSeries barSeries) {
    return params.getComposite().getStrategiesList().stream()
        .map(strategy -> ruleFactory.sellRule(strategy, barSeries))
        .reduce(reducer(params))
        .stream()
        .collect(onlyElement());
  }

  @Override
  public StrategyOneOfCase supportedCase() {
    return StrategyOneOfCase.COMPOSITE;
  }
}
