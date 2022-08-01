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
        abstract ImmutableSet<Candle> candles();

        abstract Granularity granularity();

        abstract TradeStrategy strategy();
    }
}
