package com.verlumen.tradestar.core.candles;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.math.Stats;
import com.verlumen.tradestar.protos.candles.Candle;
import com.verlumen.tradestar.protos.candles.Granularity;
import com.verlumen.tradestar.protos.trading.ExchangeTrade;

import java.time.Instant;
import java.util.stream.DoubleStream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.time.Instant.ofEpochSecond;
import static java.util.Comparator.comparing;

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
    return create(startTime, trades);
  }

  @SuppressWarnings("UnstableApiUsage")
  private static Candle create(Instant startTime, ImmutableList<ExchangeTrade> trades) {
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

  private static ImmutableList<ExchangeTrade> sortTrades(ImmutableList<ExchangeTrade> trades) {
    return trades.stream()
        .sorted(comparing(trade -> trade.getTimestamp().getSeconds()))
        .collect(toImmutableList());
  }

  private static Instant getStartTime(GranularitySpec granularitySpec, ExchangeTrade firstTrade) {
    long firstTradeSeconds = firstTrade.getTimestamp().getSeconds();
    long secondsAfterStart = firstTradeSeconds % granularitySpec.seconds();
    long startTimeSeconds = firstTradeSeconds - secondsAfterStart;
    return ofEpochSecond(startTimeSeconds);
  }

  @AutoValue
  public abstract static class CreateParams {
    public static Builder builder() {
      return new AutoValue_CandleFactory_CreateParams.Builder();
    }

    abstract Granularity granularity();

    abstract ImmutableList<ExchangeTrade> trades();

    @AutoValue.Builder
    public abstract static class Builder {
      public abstract Builder setGranularity(Granularity granularity);

      public abstract Builder setTrades(ImmutableList<ExchangeTrade> trades);

      public abstract CreateParams build();
    }
  }
}
