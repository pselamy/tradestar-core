package com.verlumen.tradestar.core.strategies.generators;

import com.google.common.collect.ImmutableSet;
import com.verlumen.tradestar.core.shared.Constants;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;

import java.util.stream.Stream;

public interface TradeStrategyGenerator {
  default Stream<TradeStrategy> generate() {
    return generate(Constants.SUPPORTED_STRATEGY_ONE_OF_CASES);
  }

  default Stream<TradeStrategy> generate(TradeStrategy.StrategyOneOfCase... oneOfCases) {
    return generate(ImmutableSet.copyOf(oneOfCases));
  }

  Stream<TradeStrategy> generate(ImmutableSet<TradeStrategy.StrategyOneOfCase> oneOfCases);
}
