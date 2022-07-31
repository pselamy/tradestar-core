package com.verlumen.tradestar.core.candles;

import com.google.common.collect.ImmutableSet;
import com.verlumen.tradestar.protos.candles.Granularity;

import java.time.Duration;
import java.util.EnumSet;

public enum GranularitySpec {
  ONE_MINUTE(60),
  FIVE_MINUTES(300),
  FIFTEEN_MINUTES(900),
  ONE_HOUR(3600),
  SIX_HOURS(21600),
  ONE_DAY(86400);

  private static final EnumSet<Granularity> UNSUPPORTED_GRANULARITIES =
      EnumSet.of(Granularity.UNRECOGNIZED, Granularity.UNSPECIFIED);
  private static final ImmutableSet<Granularity> SUPPORTED_GRANULARITIES =
      ImmutableSet.copyOf(EnumSet.complementOf(UNSUPPORTED_GRANULARITIES));
  private final Duration duration;

  GranularitySpec(int seconds) {
    this.duration = Duration.ofSeconds(seconds);
  }

  public static GranularitySpec create(Granularity granularity) {
    return GranularitySpec.valueOf(granularity.name());
  }

  public static boolean isSupported(Granularity granularity) {
    return SUPPORTED_GRANULARITIES.contains(granularity);
  }

  public Duration duration() {
    return duration;
  }

  public Granularity granularity() {
    return Granularity.valueOf(name());
  }

  public long minutes() {
    return duration.toMinutes();
  }

  public long seconds() {
    return duration.toSeconds();
  }
}
