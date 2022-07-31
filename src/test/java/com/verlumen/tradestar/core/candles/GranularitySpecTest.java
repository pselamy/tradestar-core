package com.verlumen.tradestar.core.candles;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.testing.junit.testparameterinjector.TestParameter;
import com.google.testing.junit.testparameterinjector.TestParameterInjector;
import com.verlumen.tradestar.protos.candles.Granularity;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.Duration;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.TruthJUnit.assume;
import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertThrows;

@RunWith(TestParameterInjector.class)
public class GranularitySpecTest {
  private static final ImmutableSet<Granularity> UNSUPPORTED_GRANULARITIES =
      ImmutableSet.of(Granularity.UNRECOGNIZED, Granularity.UNSPECIFIED);
  private static final ImmutableMap<Granularity, Duration> DURATIONS_BY_GRANULARITY =
      ImmutableMap.of(
          Granularity.ONE_MINUTE, Duration.ofMinutes(1),
          Granularity.FIVE_MINUTES, Duration.ofMinutes(5),
          Granularity.FIFTEEN_MINUTES, Duration.ofMinutes(15),
          Granularity.ONE_HOUR, Duration.ofHours(1),
          Granularity.SIX_HOURS, Duration.ofHours(6),
          Granularity.ONE_DAY, Duration.ofDays(1));

  @Test
  public void create_setsExpectedDuration(@TestParameter Granularity granularity) {
    // Arrange
    assume().that(granularity).isNotIn(UNSUPPORTED_GRANULARITIES);
    Duration expectedDuration = DURATIONS_BY_GRANULARITY.get(granularity);

    // Act
    GranularitySpec actual = GranularitySpec.create(granularity);

    // Assert
    assertThat(actual.duration()).isEqualTo(expectedDuration);
  }

  @Test
  public void create_setsExpectedGranularity(@TestParameter Granularity granularity) {
    // Arrange
    assume().that(granularity).isNotIn(UNSUPPORTED_GRANULARITIES);

    // Act
    GranularitySpec actual = GranularitySpec.create(granularity);

    // Assert
    assertThat(actual.granularity()).isEqualTo(granularity);
  }

  @Test
  public void create_setsExpectedSeconds(@TestParameter Granularity granularity) {
    // Arrange
    assume().that(granularity).isNotIn(UNSUPPORTED_GRANULARITIES);
    Duration expectedDuration = requireNonNull(DURATIONS_BY_GRANULARITY.get(granularity));
    long expectedSeconds = expectedDuration.getSeconds();

    // Act
    GranularitySpec actual = GranularitySpec.create(granularity);

    // Assert
    assertThat(actual.seconds()).isEqualTo(expectedSeconds);
  }

  @Test
  public void create_setsExpectedMinutes(@TestParameter Granularity granularity) {
    // Arrange
    assume().that(granularity).isNotIn(UNSUPPORTED_GRANULARITIES);
    Duration expectedDuration = requireNonNull(DURATIONS_BY_GRANULARITY.get(granularity));
    long expectedMinutes = expectedDuration.getSeconds() / 60;

    // Act
    GranularitySpec actual = GranularitySpec.create(granularity);

    // Assert
    assertThat(actual.minutes()).isEqualTo(expectedMinutes);
  }

  @Test
  public void create_setsNonNullDuration(@TestParameter Granularity granularity) {
    // Arrange
    assume().that(granularity).isNotIn(UNSUPPORTED_GRANULARITIES);

    // Act
    GranularitySpec actual = GranularitySpec.create(granularity);

    // Assert
    assertThat(actual.duration()).isGreaterThan(Duration.ZERO);
  }

  @Test
  public void create_withUnsupportedGranularity_throwsIllegalArgumentException(
      @TestParameter Granularity granularity) {
    // Arrange
    assume().that(granularity).isIn(UNSUPPORTED_GRANULARITIES);

    // Act / Assert
    assertThrows(IllegalArgumentException.class, () -> GranularitySpec.create(granularity));
  }
}
