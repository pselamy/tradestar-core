package com.verlumen.tradestar.core.candles;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static java.time.Instant.ofEpochSecond;
import static java.util.Comparator.comparing;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.math.Stats;
import com.verlumen.tradestar.protos.candles.Candle;
import com.verlumen.tradestar.protos.candles.Granularity;
import com.verlumen.tradestar.protos.trading.ExchangeTrade;
import java.time.Instant;
import java.util.function.Function;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class CandleFactory {
  public static Candle create(CreateParams params) {
    checkArgument(!params.trades().isEmpty());
    checkArgument(GranularitySpec.isSupported(params.granularity()));

    ImmutableList<ExchangeTrade> trades = sortTrades(params.trades());
    ExchangeTrade firstTrade = trades.get(0);
    ExchangeTrade lastTrade = trades.get(trades.size() - 1);
    GranularitySpec granularitySpec = GranularitySpec.fromGranularity(params.granularity());
    Instant startTime = getStartTime(granularitySpec, firstTrade);
    Instant endTime = startTime.plus(granularitySpec.duration());
    Instant lastTradeTime = ofEpochSecond(lastTrade.getTimestamp().getSeconds());
    checkArgument(lastTradeTime.isBefore(endTime));
    return createCandle(startTime, trades);
  }

  public static Candle merge(MergeParams params) {
    ImmutableList<Candle> candles = params.candles().asList();
    Candle firstCandle = candles.get(0);
    Candle lastCandle = candles.get(candles.size() - 1);

    double volume = params.candles().stream().mapToDouble(Candle::getVolume).sum();
    //noinspection UnstableApiUsage
    return Candle.newBuilder()
        .setStart(firstCandle.getStart())
        .setOpen(firstCandle.getOpen())
        .setHigh(aggregateCandleAttribute(Candle::getHigh, Stats::max).apply(candles))
        .setLow(aggregateCandleAttribute(Candle::getLow, Stats::min).apply(candles))
        .setClose(lastCandle.getClose())
        .setVolume(volume)
        .build();
  }

  @SuppressWarnings("UnstableApiUsage")
  private static Candle createCandle(Instant startTime, ImmutableList<ExchangeTrade> trades) {
    ExchangeTrade firstTrade = trades.get(0);
    ExchangeTrade lastTrade = trades.get(trades.size() - 1);
    DoubleStream prices =
        trades.stream().map(ExchangeTrade::getPrice).mapToDouble(Double::doubleValue);

    Stats priceStats = Stats.of(prices);
    double volume = trades.stream().mapToDouble(ExchangeTrade::getVolume).sum();

    Candle.Builder builder =
        Candle.newBuilder()
            .setOpen(firstTrade.getPrice())
            .setHigh(priceStats.max())
            .setLow(priceStats.min())
            .setClose(lastTrade.getPrice())
            .setVolume(volume);
    builder.getStartBuilder().setSeconds(startTime.getEpochSecond());
    return builder.build();
  }

  private static ImmutableList<ExchangeTrade> sortTrades(ImmutableSet<ExchangeTrade> trades) {
    return trades.stream()
        .sorted(comparing(trade -> trade.getTimestamp().getSeconds()))
        .distinct()
        .collect(toImmutableList());
  }

  private static Instant getStartTime(GranularitySpec granularitySpec, ExchangeTrade firstTrade) {
    long firstTradeSeconds = firstTrade.getTimestamp().getSeconds();
    long secondsAfterStart = firstTradeSeconds % granularitySpec.seconds();
    long startTimeSeconds = firstTradeSeconds - secondsAfterStart;
    return ofEpochSecond(startTimeSeconds);
  }

  @SuppressWarnings("UnstableApiUsage")
  private static Function<ImmutableCollection<Candle>, Double> aggregateCandleAttribute(
      Function<Candle, Double> getAttribute, Function<Stats, Double> getAggregate) {
    return candles ->
        getAggregate.apply(
            Stats.of(candles.stream().map(getAttribute).mapToDouble(Double::valueOf)));
  }

  @AutoValue
  public abstract static class CreateParams {
    public static Builder builder() {
      return new AutoValue_CandleFactory_CreateParams.Builder();
    }

    abstract Granularity granularity();

    abstract ImmutableSet<ExchangeTrade> trades();

    @AutoValue.Builder
    public abstract static class Builder {
      public abstract Builder setGranularity(Granularity granularity);

      public abstract Builder setTrades(ImmutableList<ExchangeTrade> trades);

      public abstract CreateParams build();
    }
  }

  @AutoValue
  public abstract static class MergeParams {
    public static MergeParams create(
        ImmutableSet<Candle> candles, Granularity fromGranularity, Granularity toGranularity) {
      GranularitySpec fromSpec = GranularitySpec.fromGranularity(fromGranularity);
      GranularitySpec toSpec = GranularitySpec.fromGranularity(toGranularity);
      checkArgument(toSpec.minutes() % fromSpec.minutes() == 0);
      checkArgument(candles.size() == toSpec.minutes() / (fromSpec.minutes()));

      ImmutableList<Candle> sortedCandles = getSortedCandles(candles, fromSpec);
      return new AutoValue_CandleFactory_MergeParams(ImmutableSet.copyOf(sortedCandles));
    }

    private static ImmutableList<Candle> getSortedCandles(
        ImmutableSet<Candle> candles, GranularitySpec fromSpec) {
      ImmutableList<Candle> sortedCandles =
          candles.stream()
              .sorted(comparing(candle -> candle.getStart().getSeconds()))
              .collect(toImmutableList());

      ImmutableSet<Long> expectedStartTimes =
          Stream.iterate(
                  sortedCandles.get(0).getStart().getSeconds(), start -> start + fromSpec.seconds())
              .limit(sortedCandles.size())
              .collect(toImmutableSet());
      ImmutableSet<Long> actualStartTimes =
          sortedCandles.stream()
              .map(candle -> candle.getStart().getSeconds())
              .collect(toImmutableSet());
      checkArgument(
          expectedStartTimes.equals(actualStartTimes),
          "%s != %s",
          expectedStartTimes,
          actualStartTimes);
      return sortedCandles;
    }

    abstract ImmutableSet<Candle> candles();
  }
}
