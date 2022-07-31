package com.verlumen.tradestar.core.candles;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.testing.junit.testparameterinjector.TestParameter;
import com.google.testing.junit.testparameterinjector.TestParameterInjector;
import com.verlumen.tradestar.protos.candles.Candle;
import com.verlumen.tradestar.protos.candles.Granularity;
import com.verlumen.tradestar.protos.trading.ExchangeTrade;
import java.util.function.Supplier;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(TestParameterInjector.class)
public class CandleFactoryTest {
  private static Candle newCandle(
      double open, double high, double low, double close, double volume, long start) {
    Candle.Builder builder =
        Candle.newBuilder()
            .setOpen(open)
            .setHigh(high)
            .setLow(low)
            .setClose(close)
            .setVolume(volume);
    builder.getStartBuilder().setSeconds(start);
    return builder.build();
  }

  private static ExchangeTrade newTrade(double price, double volume, int seconds) {
    ExchangeTrade.Builder builder = ExchangeTrade.newBuilder().setPrice(price).setVolume(volume);
    builder.getTimestampBuilder().setSeconds(seconds);
    return builder.build();
  }

  @Test
  public void create_createsCandle(@TestParameter CreateTestCase testCase) {
    // Act
    Candle actual = CandleFactory.create(testCase.params);

    // Assert
    assertThat(actual).isEqualTo(testCase.expected);
  }

  @Test
  public void create_throwsIllegalArgumentException(
      @TestParameter CreateThrowsIllegalArgumentExceptionTestCase testCase) {
    // Act / Assert
    assertThrows(IllegalArgumentException.class, () -> CandleFactory.create(testCase.params));
  }

  @Test
  public void mergeCandles_mergesCandles(@TestParameter MergeTestCase testCase) {
    // Act
    Candle actual = CandleFactory.merge(testCase.params);

    // Assert
    assertThat(actual).isEqualTo(testCase.expected);
  }

  @Test
  public void mergeParams_throwsIllegalArgumentException(
      @TestParameter MergeThrowsIllegalArgumentExceptionTestCase testCase) {
    // Act / Assert
    assertThrows(IllegalArgumentException.class, testCase.params::get);
  }

  @SuppressWarnings("unused")
  private enum CreateTestCase {
    ONE_TRADE_ONE_MINUTE(
        Granularity.ONE_MINUTE,
        ImmutableList.of(newTrade(1, 1, 61)),
        newCandle(1.0, 1.0, 1.0, 1.0, 1.0, 60)),
    ONE_TRADE_FIVE_MINUTES(
        Granularity.FIVE_MINUTES,
        ImmutableList.of(newTrade(2, 1, 1234)),
        newCandle(2.0, 2.0, 2.0, 2.0, 1.0, 1200)),
    TWO_TRADES_ONE_MINUTE(
        Granularity.ONE_MINUTE,
        ImmutableList.of(newTrade(3, 1, 61), newTrade(2, 1, 62)),
        newCandle(3.0, 3.0, 2.0, 2.0, 2.0, 60)),
    TWO_TRADES_FIVE_MINUTES(
        Granularity.FIVE_MINUTES,
        ImmutableList.of(newTrade(2, 1, 1234)),
        newCandle(2.0, 2.0, 2.0, 2.0, 1.0, 1200))    ;

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
  private enum CreateThrowsIllegalArgumentExceptionTestCase {
    TWO_TRADES_ONE_MINUTE_DIFFERENT_MINUTES(
        Granularity.ONE_MINUTE, ImmutableList.of(newTrade(3, 1, 61), newTrade(2, 15, 122))),
    TWO_TRADES_FIVE_MINUTES_DIFFERENT_MINUTES(
        Granularity.FIVE_MINUTES, ImmutableList.of(newTrade(3, 1, 7627), newTrade(2, 15, 9999)));

    private final CandleFactory.CreateParams params;

    CreateThrowsIllegalArgumentExceptionTestCase(
        Granularity granularity, ImmutableList<ExchangeTrade> trades) {
      this.params =
          CandleFactory.CreateParams.builder()
              .setGranularity(granularity)
              .setTrades(trades)
              .build();
    }
  }

  @SuppressWarnings("unused")
  private enum MergeTestCase {
    ONE_TO_FIVE_MINUTES(
        ImmutableSet.of(
            newCandle(1.0, 1.0, 1.0, 1.0, 1.0, 60),
            newCandle(2.0, 2.0, 2.0, 2.0, 1.0, 120),
            newCandle(3.0, 3.0, 2.0, 2.0, 2.0, 180),
            newCandle(4.0, 4.0, 4.0, 4.0, 1.0, 240),
            newCandle(5.0, 5.0, 5.0, 5.0, 1.0, 300)),
        Granularity.ONE_MINUTE,
        Granularity.FIVE_MINUTES,
        newCandle(1.0, 5.0, 1.0, 5.0, 6.0, 60));

    private final CandleFactory.MergeParams params;
    private final Candle expected;

    MergeTestCase(
        ImmutableSet<Candle> candles,
        Granularity fromGranularity,
        Granularity toGranularity,
        Candle expected) {
      this.params = CandleFactory.MergeParams.create(candles, fromGranularity, toGranularity);
      this.expected = expected;
    }
  }

  @SuppressWarnings("unused")
  private enum MergeThrowsIllegalArgumentExceptionTestCase {
    ONE_TO_FIVE_MINUTES_THREE_CANDLES(
        ImmutableSet.of(
            newCandle(1.0, 1.0, 1.0, 1.0, 1.0, 60),
            newCandle(2.0, 2.0, 2.0, 2.0, 1.0, 120),
            newCandle(3.0, 3.0, 2.0, 2.0, 2.0, 180)),
        Granularity.ONE_MINUTE,
        Granularity.FIVE_MINUTES),
    ONE_TO_FIVE_MINUTES_BAD_START_TIMES(
        ImmutableSet.of(
            newCandle(1.0, 1.0, 1.0, 1.0, 1.0, 300),
            newCandle(2.0, 2.0, 2.0, 2.0, 1.0, 600),
            newCandle(3.0, 3.0, 2.0, 2.0, 2.0, 900),
            newCandle(4.0, 4.0, 4.0, 4.0, 1.0, 1200),
            newCandle(5.0, 5.0, 5.0, 5.0, 1.0, 1500)),
        Granularity.ONE_MINUTE,
        Granularity.FIVE_MINUTES);

    private final Supplier<CandleFactory.MergeParams> params;

    MergeThrowsIllegalArgumentExceptionTestCase(
        ImmutableSet<Candle> candles, Granularity fromGranularity, Granularity toGranularity) {
      this.params = () -> getParams(candles, fromGranularity, toGranularity);
    }

    private CandleFactory.MergeParams getParams(
        ImmutableSet<Candle> candles, Granularity fromGranularity, Granularity toGranularity) {
      return CandleFactory.MergeParams.create(candles, fromGranularity, toGranularity);
    }
  }
}
