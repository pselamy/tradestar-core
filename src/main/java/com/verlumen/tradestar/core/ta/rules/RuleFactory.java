package com.verlumen.tradestar.core.ta.rules;

import com.google.common.collect.ImmutableSet;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategy.StrategyOneOfCase;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Rule;

public interface RuleFactory {
  Rule buyRule(TradeStrategy params, BarSeries barSeries);

  Rule sellRule(TradeStrategy params, BarSeries barSeries);

  default StrategyOneOfCase supportedCase() {
    return StrategyOneOfCase.STRATEGYONEOF_NOT_SET;
  }

  interface RuleAdapter {
    Rule buyRule(TradeStrategy params, BarSeries barSeries);

    Rule sellRule(TradeStrategy params, BarSeries barSeries);

    StrategyOneOfCase supportedCase();
  }
}
