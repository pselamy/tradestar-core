package com.verlumen.tradestar.core.tradehistory;

import com.verlumen.tradestar.protos.candles.Candle;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseBar;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static com.google.common.base.Preconditions.checkArgument;
import static org.ta4j.core.num.DecimalNum.valueOf;

class BarFactory {
    static Bar create(Candle candle) {
        checkArgument(candle.hasGranularity());
        checkArgument(candle.hasStart());
        checkArgument(candle.hasOpen());
        checkArgument(candle.hasHigh());
        checkArgument(candle.hasLow());
        checkArgument(candle.hasClose());
        checkArgument(candle.hasVolume());
        return createBar(candle);
    }

    private static Bar createBar(Candle candle) {
        Duration duration = GranularitySpec.get(candle.getGranularity()).duration();
        Instant start = Instant.ofEpochSecond((candle.getStart()).getSeconds());
        ZonedDateTime end = start.plus(duration).atZone(ZoneOffset.UTC);
        return BaseBar.builder()
                .timePeriod(duration)
                .endTime(end)
                .openPrice(valueOf(candle.getOpen()))
                .highPrice(valueOf(candle.getHigh()))
                .lowPrice(valueOf(candle.getLow()))
                .closePrice(valueOf(candle.getClose()))
                .volume(valueOf(candle.getVolume()))
                .build();
    }

}
