package com.verlumen.tradestar.core.candles;

import com.google.common.collect.ImmutableList;
import com.google.testing.junit.testparameterinjector.TestParameter;
import com.google.testing.junit.testparameterinjector.TestParameterInjector;
import com.verlumen.tradestar.protos.candles.Candle;
import com.verlumen.tradestar.protos.candles.Granularity;
import com.verlumen.tradestar.protos.trading.ExchangeTrade;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.Instant;

import static com.google.common.truth.Truth.assertThat;

@RunWith(TestParameterInjector.class)
public class CandleFactoryTest {
  private static ExchangeTrade newTrade(double price, double volume, int seconds) {
    ExchangeTrade.Builder builder = ExchangeTrade.newBuilder().setPrice(price).setVolume(volume);
    builder.getTimestampBuilder().setSeconds(seconds);
    return builder.build();
  }

  private static Candle newCandle(
      Granularity granularity,
      double open,
      double high,
      double low,
      double close,
      double volume,
      Instant start) {
    Candle.Builder builder =
        Candle.newBuilder()
            .setOpen(open)
            .setHigh(high)
            .setLow(low)
            .setClose(close)
            .setVolume(volume);
    builder.getStartBuilder().setSeconds(start.getEpochSecond());
    return builder.build();
  }

  @Test
  public void createCandle_createsCandle(@TestParameter CreateTestCase testCase) {
    // Act
    Candle actual = CandleFactory.create(testCase.params);

    // Assert
    assertThat(actual).isEqualTo(testCase.expected);
  }

  @SuppressWarnings("unused")
  private enum CreateTestCase {
    NO_CANDLES_ONE_TRADE(
        Granularity.ONE_MINUTE,
        ImmutableList.of(newTrade(1, 1, 61)),
        newCandle(Granularity.ONE_MINUTE, 1.0, 1.0, 1.0, 1.0, 1.0, Instant.ofEpochSecond(60)));

    private final CandleFactory.CreateParams params;
    private final Candle expected;

    CreateTestCase(Granularity granularity, ImmutableList<ExchangeTrade> trades, Candle expected) {
      this.params =
          CandleFactory.CreateParams.builder()
              .setGranularity(granularity)
              .setTrades(trades)
              .build();
      this.expected = expected;
    }
  }
}
