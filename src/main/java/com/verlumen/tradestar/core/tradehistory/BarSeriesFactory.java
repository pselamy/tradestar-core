package com.verlumen.tradestar.core.tradehistory;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.time.Instant.ofEpochSecond;

import com.google.common.collect.ImmutableList;
import com.verlumen.tradestar.core.candles.GranularitySpec;
import com.verlumen.tradestar.protos.candles.Candle;
import com.verlumen.tradestar.protos.candles.Granularity;
import java.time.Duration;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.Pair;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;

public class BarSeriesFactory {
  public static BarSeries create(Granularity granularity, ImmutableList<Candle> candles) {
    GranularitySpec granularitySpec = GranularitySpec.create(granularity);
    checkArgument(candlesAreConforming(granularitySpec, candles));
    Duration duration = granularitySpec.duration();
    return create(duration, candles);
  }

  private static BarSeries create(Duration duration, ImmutableList<Candle> candles) {
    ImmutableList<Bar> bars =
        candles.stream()
            .map(candle -> BarFactory.create(duration, candle))
            .collect(toImmutableList());
    return new BaseBarSeriesBuilder().withBars(bars).build();
  }

  public static boolean candlesAreConforming(
      GranularitySpec granularitySpec, ImmutableList<Candle> candles) {
    return candles.size() <= 1
        || adjacentCandles(candles)
            .map(
                pair ->
                    Duration.between(
                        ofEpochSecond(pair.getLeft().getStart().getSeconds()),
                        ofEpochSecond(pair.getRight().getStart().getSeconds())))
            .allMatch(duration -> duration.equals(granularitySpec.duration()));
  }

  private static <T> Stream<Pair<T, T>> adjacentCandles(ImmutableList<T> candles) {
    return IntStream.rangeClosed(0, candles.size() - 2)
        .mapToObj(i -> Pair.of(candles.get(i), candles.get(i + 1)));
  }
}
