package com.verlumen.tradestar.core.shared;

import com.google.common.collect.ImmutableSet;
import com.verlumen.tradestar.core.candles.GranularitySpec;
import com.verlumen.tradestar.protos.candles.Candle;

import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static java.util.Comparator.comparing;

public class CandleSorter {
  public static ImmutableSet<Candle> sortAndValidate(
      ImmutableSet<Candle> candles, GranularitySpec granularitySpec) {
    ImmutableSet<Candle> sortedCandles = sort(candles);
    validate(granularitySpec, sortedCandles);
    return sortedCandles;
  }

  private static ImmutableSet<Candle> sort(ImmutableSet<Candle> candles) {
    return candles.stream()
        .sorted(comparing(candle -> candle.getStart().getSeconds()))
        .collect(toImmutableSet());
  }

  private static void validate(
      GranularitySpec granularitySpec, ImmutableSet<Candle> sortedCandles) {
    ImmutableSet<Long> expectedStartTimes =
        Stream.iterate(
                sortedCandles.asList().get(0).getStart().getSeconds(),
                start -> start + granularitySpec.seconds())
            .limit(sortedCandles.size())
            .collect(toImmutableSet());
    ImmutableSet<Long> actualStartTimes =
        sortedCandles.stream()
            .map(candle -> candle.getStart().getSeconds())
            .collect(toImmutableSet());
    checkArgument(
        expectedStartTimes.equals(actualStartTimes),
        "actual: %s\nexpected: %s",
        actualStartTimes,
        expectedStartTimes);
  }
}
