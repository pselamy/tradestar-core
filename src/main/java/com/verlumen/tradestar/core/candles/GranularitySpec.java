package com.verlumen.tradestar.core.candles;

import com.verlumen.tradestar.protos.candles.Granularity;

import java.time.Duration;

public enum GranularitySpec {
    ONE_MINUTE(60),
    FIVE_MINUTES(300),
    FIFTEEN_MINUTES(900),
    ONE_HOUR(3600),
    SIX_HOURS(21600),
    ONE_DAY(86400);

    private final Duration duration;

    GranularitySpec(int seconds) {
        this.duration = Duration.ofSeconds(seconds);
    }

    static GranularitySpec fromGranularity(Granularity granularity) {
        return GranularitySpec.valueOf(granularity.name());
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
}
