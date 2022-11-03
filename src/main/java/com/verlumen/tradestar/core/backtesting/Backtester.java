package com.verlumen.tradestar.core.backtesting;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import com.verlumen.tradestar.protos.candles.Candle;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategyTestResult;

public interface Backtester {
  TradeStrategyTestResult test(Params params);

  @AutoValue
  abstract class Params {
    public static Params create(ImmutableSet<Candle> candles, TradeStrategy strategy) {
      return new AutoValue_Backtester_Params(candles, strategy);
    }

    abstract ImmutableSet<Candle> candles();

    abstract TradeStrategy strategy();
  }
}
