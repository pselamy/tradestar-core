package com.verlumen.tradestar.core.ta.strategies;

import com.google.inject.Inject;
import com.verlumen.tradestar.core.ta.rules.RuleFactory;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;

class StrategyFactoryImpl implements StrategyFactory {
  private final RuleFactory ruleFactory;

  @Inject
  StrategyFactoryImpl(RuleFactory ruleFactory) {
    this.ruleFactory = ruleFactory;
  }

  private static Strategy create(String name, Rule entryRule, Rule exitRule) {
    return new BaseStrategy(name, entryRule, exitRule);
  }

  @Override
  public Strategy create(TradeStrategy params, BarSeries barSeries) {
    String name = getStrategyName(params);
    Rule entryRule = ruleFactory.buyRule(params, barSeries);
    Rule exitRule = ruleFactory.sellRule(params, barSeries);
    return create(name, entryRule, exitRule);
  }

  private String getStrategyName(TradeStrategy params) {
    return TradeStrategyIdentifier.get(params.getStrategyOneOfCase()).name(params);
  }
}
