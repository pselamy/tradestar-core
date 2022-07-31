package com.verlumen.tradestar.core.tradehistory;

import static com.google.common.base.Preconditions.checkArgument;
import static org.ta4j.core.num.DecimalNum.valueOf;

import com.verlumen.tradestar.protos.candles.Candle;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseBar;

public class BarFactory {
  public static Bar create(Duration duration, Candle candle) {
    checkArgument(candle.hasStart());
    checkArgument(candle.getOpen() > 0L);
    checkArgument(candle.getHigh() > 0L);
    checkArgument(candle.getLow() > 0L);
    checkArgument(candle.getClose() > 0L);
    checkArgument(candle.getVolume() > 0L);
    return createBar(duration, candle);
  }

  private static Bar createBar(Duration duration, Candle candle) {
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
