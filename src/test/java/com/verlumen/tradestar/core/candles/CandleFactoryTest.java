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
  private static Candle newCandle(
          double open, double high, double low, double close, double volume, Instant start) {
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

  private static ExchangeTrade newTrade(double price, double volume, int seconds) {
    ExchangeTrade.Builder builder = ExchangeTrade.newBuilder().setPrice(price).setVolume(volume);
    builder.getTimestampBuilder().setSeconds(seconds);
    return builder.build();
  }

  @Test
  public void createCandle_createsCandle(@TestParameter CreateTestCase testCase) {
    // Act
    Candle actual = CandleFactory.create(testCase.params);

    // Assert
    assertThat(actual).isEqualTo(testCase.expected);
  }

//  @Test
//  public void mergeCandles_mergesCandles(@TestParameter MergeTestCase testCase) {
//    // Act
//    Candle actual = CandleFactory.mergeCandles(testCase.params);
//
//    // Assert
//    assertThat(actual).isEqualTo(testCase.expected);
//  }

  @SuppressWarnings("unused")
  private enum CreateTestCase {
    NO_CANDLES_ONE_TRADE_ONE_MINUTE(
        Granularity.ONE_MINUTE,
        ImmutableList.of(newTrade(1, 1, 61)),
        newCandle(1.0, 1.0, 1.0, 1.0, 1.0, Instant.ofEpochSecond(60))),
    NO_CANDLES_ONE_TRADE_FIVE_MINUTES(
        Granularity.FIVE_MINUTES,
        ImmutableList.of(newTrade(2, 1, 1234)),
        newCandle(2.0, 2.0, 2.0, 2.0, 1.0, Instant.ofEpochSecond(1200))),
    NO_CANDLES_TWO_TRADES_ONE_MINUTE_SAME_MINUTE(
            Granularity.ONE_MINUTE,
            ImmutableList.of(newTrade(3, 1, 61), newTrade(2, 1, 62)),
            newCandle(3.0, 3.0, 2.0, 2.0, 2.0, Instant.ofEpochSecond(60)));

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

  @SuppressWarnings("unused")
  private enum MergeTestCase {
    ;

    private final CandleFactory.MergeParams params;
    private final Candle expected;

    MergeTestCase(CandleFactory.MergeParams params, Candle expected) {
      this.params = params;
      this.expected = expected;
    }
  }
}
