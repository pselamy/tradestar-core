package com.verlumen.tradestar.core.tradehistory;

import com.verlumen.tradestar.protos.candles.Granularity;

import java.time.Duration;

enum GranularitySpec {
    ONE_MINUTE(60L),
    FIVE_MINUTES(300L),
    FIFTEEN_MINUTES(900L),
    ONE_HOUR(3600L),
    SIX_HOURS(21600L),
    ONE_DAY(86400L);

    private final Duration duration;

    GranularitySpec(long seconds) {
        this.duration = Duration.ofSeconds(seconds);
    }

    static GranularitySpec fromGranularity(Granularity granularity) {
        return GranularitySpec.valueOf(granularity.name());
    }

    public Duration duration() {
        return duration;
    }
}
