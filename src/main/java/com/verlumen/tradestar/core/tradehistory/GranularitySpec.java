package com.verlumen.tradestar.core.tradehistory;

import com.verlumen.tradestar.protos.candles.Granularity;

import java.time.Duration;

public enum GranularitySpec {
    ONE_MINUTE(60L),
    FIVE_MINUTES(300L),
    FIFTEEN_MINUTES(900L),
    ONE_HOUR(3600L),
    SIX_HOURS(21600L),
    ONE_DAY(86400L);

    private final Granularity granularity;
    private final Duration duration;

    GranularitySpec(long seconds) {
        this.granularity = Granularity.valueOf(name());
        this.duration = Duration.ofSeconds(seconds);
    }

    public static GranularitySpec get(Granularity granularity) {
        return GranularitySpec.valueOf(granularity.name());
    }

    public Duration duration() {
        return duration;
    }
}
