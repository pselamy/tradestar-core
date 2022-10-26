package com.verlumen.tradestar.core.strategies.adapters;

import com.google.common.collect.ImmutableList;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.google.testing.junit.testparameterinjector.TestParameterInjector;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.truth.Truth.assertThat;

@RunWith(TestParameterInjector.class)
public class AdxTradeStrategyAdapterTest {
  @Inject private AdxTradeStrategyAdapter adxTradeStrategyAdapter;

  @Before
  public void setup() {
    Guice.createInjector(BoundFieldModule.of(this)).injectMembers(this);
  }

  @Test
  public void generate_returnsExpectedTradeStrategies() {
    // Act
    ImmutableList<TradeStrategy> actual =
        adxTradeStrategyAdapter.generate().collect(toImmutableList());

    // Assert
    assertThat(actual).isNotEmpty();
  }
}
