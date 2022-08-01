package com.verlumen.tradestar.core.ta.rules;

import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Rule;

public interface RuleFactory {
    Rule buyRule(TradeStrategy params, BarSeries barSeries);

    Rule sellRule(TradeStrategy params, BarSeries barSeries);

    TradeStrategy.StrategyOneOfCase supportedCase();
}
