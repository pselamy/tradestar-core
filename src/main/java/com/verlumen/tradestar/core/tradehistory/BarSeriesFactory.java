package com.verlumen.tradestar.core.tradehistory;

import com.google.common.collect.ImmutableList;
import com.verlumen.tradestar.protos.candles.Candle;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.time.Instant.ofEpochSecond;

public class BarSeriesFactory {
    public static BarSeries create(ImmutableList<Candle> candles) {
        checkArgument(candlesAreConforming(candles));
        ImmutableList<Bar> bars = candles.stream()
                .map(BarFactory::create)
                .collect(toImmutableList());
        return new BaseBarSeriesBuilder()
                .withBars(bars)
                .build();
    }


    public static boolean candlesAreConforming(ImmutableList<Candle> candles) {
        return sliding(candles, 2)
                .map(subList -> new CandlePair(subList.get(0), subList.get(1)))
                .allMatch(CandlePair::isConforming);
    }

    public static <T> Stream<ImmutableList<T>> sliding(ImmutableList<T> list, int size) {
        return IntStream.rangeClosed(0, list.size() - size)
                .mapToObj(start -> list.subList(start, start + size));
    }

    private static class CandlePair {
        private final Candle first;
        private final Candle second;

        private CandlePair(Candle first,
                           Candle second) {
            this.first = first;
            this.second = second;
        }

        private boolean isConforming() {
            return first.getGranularity().equals(second.getGranularity()) &&
                    candlesAreProperlySpaced(GranularitySpec
                            .get(first.getGranularity())
                            .duration());

        }

        private boolean candlesAreProperlySpaced(Duration expectedDuration) {
            Instant instant1 = ofEpochSecond(first.getStart().getSeconds());
            Instant instant2 = ofEpochSecond(second.getStart().getSeconds());
            return Duration.between(instant1, instant2).equals(expectedDuration);
        }
    }
}
