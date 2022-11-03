package com.verlumen.tradestar.core.tradehistory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.verlumen.tradestar.core.candles.GranularitySpec;
import com.verlumen.tradestar.core.shared.CandleComparators;
import com.verlumen.tradestar.protos.candles.Candle;
import com.verlumen.tradestar.protos.candles.Granularity;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;

import java.time.Duration;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static com.google.common.collect.MoreCollectors.onlyElement;
import static com.verlumen.tradestar.core.shared.Constants.SUPPORTED_GRANULARITIES;

public class BarSeriesFactory {
  public static BarSeries create(ImmutableSet<Candle> candles) {
    checkArgument(candles.stream().allMatch(Candle::hasStart));
    Granularity granularity =
        candles.stream()
            .map(Candle::getGranularity)
            .filter(SUPPORTED_GRANULARITIES::contains)
            .collect(onlyElement());
    GranularitySpec granularitySpec = GranularitySpec.create(granularity);
    return create(
        granularitySpec.duration(),
        candles.stream().sorted(CandleComparators.START_TIME_ASCENDING).collect(toImmutableSet()));
  }

  private static BarSeries create(Duration duration, ImmutableSet<Candle> candles) {
    ImmutableList<Bar> bars =
        candles.stream()
            .map(candle -> BarFactory.create(duration, candle))
            .collect(toImmutableList());
    return new BaseBarSeriesBuilder().withBars(bars).build();
  }
}
