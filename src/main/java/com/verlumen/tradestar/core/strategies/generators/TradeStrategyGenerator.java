package com.verlumen.tradestar.core.strategies.generators;

import com.google.common.collect.ImmutableSet;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;

import java.util.stream.Stream;

public interface TradeStrategyGenerator {
  Stream<TradeStrategy> generate();

  default Stream<TradeStrategy> generate(TradeStrategy.StrategyOneOfCase... oneOfCases) {
    return generate(ImmutableSet.copyOf(oneOfCases));
  }

  Stream<TradeStrategy> generate(ImmutableSet<TradeStrategy.StrategyOneOfCase> oneOfCases);
}
