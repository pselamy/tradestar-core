package com.verlumen.tradestar.core.candles;

import com.google.common.collect.ImmutableSet;
import com.google.testing.junit.testparameterinjector.TestParameter;
import com.google.testing.junit.testparameterinjector.TestParameterInjector;
import com.verlumen.tradestar.protos.candles.Granularity;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.TruthJUnit.assume;
import static org.junit.Assert.assertThrows;

@RunWith(TestParameterInjector.class)
public class GranularitySpecTest {
  private static final ImmutableSet<Granularity> UNSUPPORTED_GRANULARITIES =
      ImmutableSet.of(Granularity.UNRECOGNIZED, Granularity.UNSPECIFIED);

  @Test
  public void create_setsCorrectGranularity(@TestParameter Granularity granularity) {
    // Arrange
    assume().that(granularity).isNotIn(UNSUPPORTED_GRANULARITIES);

    // Act
    GranularitySpec actual = GranularitySpec.create(granularity);

    // Assert
    assertThat(actual.granularity()).isEqualTo(granularity);
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
