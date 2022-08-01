package com.verlumen.tradestar.core.tradehistory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.verlumen.tradestar.core.candles.GranularitySpec;
import com.verlumen.tradestar.core.shared.CandleSorter;
import com.verlumen.tradestar.protos.candles.Candle;
import com.verlumen.tradestar.protos.candles.Granularity;
import org.apache.commons.lang3.tuple.Pair;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;

import java.time.Duration;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.time.Instant.ofEpochSecond;

public class BarSeriesFactory {
  public static BarSeries create(Granularity granularity, ImmutableSet<Candle> candles) {
    GranularitySpec granularitySpec = GranularitySpec.create(granularity);
    Duration duration = granularitySpec.duration();
    return create(duration, CandleSorter.sortAndValidate(candles, granularitySpec));
  }

  private static BarSeries create(Duration duration, ImmutableSet<Candle> candles) {
    ImmutableList<Bar> bars =
        candles.stream()
            .map(candle -> BarFactory.create(duration, candle))
            .collect(toImmutableList());
    return new BaseBarSeriesBuilder().withBars(bars).build();
  }

  private static <T> Stream<Pair<T, T>> adjacentCandles(ImmutableList<T> candles) {
    return IntStream.rangeClosed(0, candles.size() - 2)
        .mapToObj(i -> Pair.of(candles.get(i), candles.get(i + 1)));
  }
}
