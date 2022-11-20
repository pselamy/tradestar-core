package com.verlumen.tradestar.core.tradehistory;

import com.google.testing.junit.testparameterinjector.TestParameter;
import com.google.testing.junit.testparameterinjector.TestParameterInjector;
import com.verlumen.tradestar.core.candles.GranularitySpec;
import com.verlumen.tradestar.protos.candles.Candle;
import com.verlumen.tradestar.protos.candles.Granularity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarBuilder;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.NoSuchElementException;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.TruthJUnit.assume;
import static java.lang.Math.random;
import static java.time.Instant.ofEpochSecond;
import static java.time.ZoneOffset.UTC;
import static org.junit.Assert.assertThrows;
import static org.ta4j.core.num.DecimalNum.valueOf;

@RunWith(TestParameterInjector.class)
public class BarFactoryTest {
  // Mon, 21 Feb 2022 23:21:05 GMT
  private static final long START_IN_SECONDS = 1645485664L;
  private static final Instant START_INSTANT = ofEpochSecond(START_IN_SECONDS);
  private static final double OPEN = random();
  private static final double HIGH = random();
  private static final double LOW = random();
  private static final double CLOSE = random();
  private static final double VOLUME = random();

  private static final Candle CANDLE =
      newCandle();
  private static final BaseBarBuilder baseBarBuilder = BaseBar.builder();
  
  private static Candle newCandle() {
    Candle.Builder builder =       Candle.newBuilder()
          .setOpen(OPEN)
          .setHigh(HIGH)
          .setLow(LOW)
          .setClose(CLOSE)
          .setVolume(VOLUME);
    builder.getStartBuilder().setSeconds(START_IN_SECONDS);
    return builder.build();
  }
  
  @Before
  public void setup() {
    baseBarBuilder
        .openPrice(valueOf(OPEN))
        .highPrice(valueOf(HIGH))
        .lowPrice(valueOf(LOW))
        .closePrice(valueOf(CLOSE))
        .volume(valueOf(VOLUME));
  }

  @Test
  public void create_withInvalidDuration_throwsNoSuchElementException(
      @TestParameter GranularitySpec granularitySpec) {
    assertThrows(
        NoSuchElementException.class,
        () -> BarFactory.create(granularitySpec.duration().plusSeconds(1), CANDLE));
  }

  @Test
  public void create_withMalformedCandle_throwsIllegalArgumentException(
      @TestParameter GranularitySpec granularitySpec,
      @TestParameter ThrowsIllegalArgumentExceptionTestCase testCase) {
    assertThrows(
        IllegalArgumentException.class,
        () -> BarFactory.create(granularitySpec.duration(), testCase.candle));
  }

  @Test
  public void create_returnsNewBar(@TestParameter Granularity granularity) {
    assume().that(GranularitySpec.isSupported(granularity)).isTrue();
    Duration timePeriod = GranularitySpec.create(granularity).duration();
    ZonedDateTime endTime = START_INSTANT.plus(timePeriod).atZone(UTC);
    Bar expected = baseBarBuilder.timePeriod(timePeriod).endTime(endTime).build();

    Bar actual = BarFactory.create(timePeriod, CANDLE);

    assertThat(actual).isEqualTo(expected);
  }

  @SuppressWarnings("unused")
  private enum ThrowsIllegalArgumentExceptionTestCase {
    MISSING_START(CANDLE.toBuilder().clearStart().build()),
    MISSING_OPEN(CANDLE.toBuilder().clearOpen().build()),
    MISSING_HIGH(CANDLE.toBuilder().clearHigh().build()),
    MISSING_LOW(CANDLE.toBuilder().clearLow().build()),
    MISSING_CLOSE(CANDLE.toBuilder().clearClose().build()),
    MISSING_VOLUME(CANDLE.toBuilder().clearVolume().build());

    private final Candle candle;

    ThrowsIllegalArgumentExceptionTestCase(Candle candle) {
      this.candle = candle;
    }
  }
}
