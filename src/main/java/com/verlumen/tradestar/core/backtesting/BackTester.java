package com.verlumen.tradestar.core.backtesting;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import com.verlumen.tradestar.protos.candles.Candle;
import com.verlumen.tradestar.protos.candles.Granularity;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategyTestResult;

public interface BackTester {
  TradeStrategyTestResult test(TestParams request);

  @AutoValue
  abstract class TestParams {
    static Builder builder() {
      return new AutoValue_BackTester_TestParams.Builder();
    }

    abstract ImmutableSet<Candle> candles();

    abstract Granularity granularity();

    abstract TradeStrategy strategy();

    @AutoValue.Builder
    abstract static class Builder {
      abstract Builder setCandles(ImmutableSet<Candle> candles);

      abstract Builder setGranularity(Granularity granularity);

      abstract Builder setStrategy(TradeStrategy strategy);

      abstract TestParams build();
    }
  }
}
