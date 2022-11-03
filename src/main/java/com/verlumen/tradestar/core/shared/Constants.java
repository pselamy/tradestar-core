package com.verlumen.tradestar.core.shared;

import com.google.common.collect.ImmutableSet;
import com.verlumen.tradestar.protos.candles.Granularity;

public class Constants {
  public static final ImmutableSet<Granularity> SUPPORTED_GRANULARITIES =
      ImmutableSet.of(
          Granularity.ONE_MINUTE,
          Granularity.FIVE_MINUTES,
          Granularity.FIFTEEN_MINUTES,
          Granularity.ONE_HOUR,
          Granularity.SIX_HOURS,
          Granularity.ONE_DAY);

  private Constants() {}
}
