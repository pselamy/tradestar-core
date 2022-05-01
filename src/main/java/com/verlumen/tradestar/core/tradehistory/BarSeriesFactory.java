package com.verlumen.tradestar.core.tradehistory;

import com.google.common.collect.ImmutableList;
import com.verlumen.tradestar.protos.candles.Candle;
import javafx.util.Pair;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;

import java.time.Duration;
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
        return candles.size() <= 1 || adjacentCandles(candles)
                .map(pair -> Duration.between(
                        ofEpochSecond(pair.getKey().getStart().getSeconds()),
                        ofEpochSecond(pair.getValue().getStart().getSeconds())))
                .distinct()
                .count() <= 1;
    }

    private static <T> Stream<Pair<T, T>> adjacentCandles(ImmutableList<T> list) {
        return IntStream.rangeClosed(0, list.size() - 2)
                .mapToObj(i -> new Pair<>(list.get(i), list.get(i + 1)));
    }
}
