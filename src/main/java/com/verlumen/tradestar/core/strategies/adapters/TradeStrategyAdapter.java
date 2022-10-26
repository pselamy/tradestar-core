package com.verlumen.tradestar.core.strategies.adapters;

import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Strategy;

import java.util.stream.Stream;

public interface TradeStrategyAdapter {
  Stream<TradeStrategy> generate();

  Strategy toTa4jStrategy(TradeStrategy tradeStrategy, BarSeries barSeries);

  TradeStrategy.StrategyOneOfCase strategyOneOfCase();
}
