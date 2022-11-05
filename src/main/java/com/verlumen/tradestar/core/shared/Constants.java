package com.verlumen.tradestar.core.shared;

import com.google.common.collect.ImmutableSet;
import com.verlumen.tradestar.protos.candles.Granularity;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;

import java.util.EnumSet;

public class Constants {
  public static final ImmutableSet<Granularity> SUPPORTED_GRANULARITIES =
      ImmutableSet.of(
          Granularity.ONE_MINUTE,
          Granularity.FIVE_MINUTES,
          Granularity.FIFTEEN_MINUTES,
          Granularity.ONE_HOUR,
          Granularity.SIX_HOURS,
          Granularity.ONE_DAY);

  public static final ImmutableSet<TradeStrategy.StrategyOneOfCase>
      SUPPORTED_STRATEGY_ONE_OF_CASES =
          ImmutableSet.copyOf(
              EnumSet.complementOf(
                  EnumSet.of(TradeStrategy.StrategyOneOfCase.STRATEGYONEOF_NOT_SET)));

  private Constants() {}
}
