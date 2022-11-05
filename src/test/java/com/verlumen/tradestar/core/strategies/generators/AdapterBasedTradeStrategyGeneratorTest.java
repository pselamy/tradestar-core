package com.verlumen.tradestar.core.strategies.generators;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimaps;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.google.testing.junit.testparameterinjector.TestParameter;
import com.google.testing.junit.testparameterinjector.TestParameterInjector;
import com.verlumen.tradestar.core.strategies.adapters.TradeStrategyAdapter;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategy.StrategyOneOfCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Strategy;
import org.ta4j.core.rules.BooleanRule;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static com.google.common.truth.Truth.assertThat;

@RunWith(TestParameterInjector.class)
public class AdapterBasedTradeStrategyGeneratorTest {
  private static final Strategy ADX_STRATEGY =
      new BaseStrategy(new BooleanRule(true), new BooleanRule(true));
  private static final TradeStrategy ADX_TRADE_STRATEGY =
      TradeStrategy.newBuilder().setAdx(TradeStrategy.ADX.getDefaultInstance()).build();
  private static final Strategy COMPOSITE_STRATEGY =
      new BaseStrategy(new BooleanRule(true), new BooleanRule(false));
  private static final TradeStrategy COMPOSITE_TRADE_STRATEGY =
      TradeStrategy.newBuilder().setComposite(TradeStrategy.Composite.getDefaultInstance()).build();

  private static final TradeStrategyAdapter ADX_ADAPTER =
      FakeAdapter.create(StrategyOneOfCase.ADX, ignored -> ADX_STRATEGY, ADX_TRADE_STRATEGY);
  private static final TradeStrategyAdapter COMPOSITE_ADAPTER =
      FakeAdapter.create(
          StrategyOneOfCase.COMPOSITE, ignored -> COMPOSITE_STRATEGY, COMPOSITE_TRADE_STRATEGY);

  @Bind
  private static final Set<TradeStrategyAdapter> ADAPTERS =
      ImmutableSet.of(ADX_ADAPTER, COMPOSITE_ADAPTER);

  private static final ImmutableListMultimap<StrategyOneOfCase, TradeStrategyAdapter>
      ADAPTERS_BY_ONE_OF_CASE = Multimaps.index(ADAPTERS, TradeStrategyAdapter::strategyOneOfCase);
  @Inject private AdapterBasedTradeStrategyGenerator tradeStrategyGenerator;

  @Before
  public void setUp() {
    Guice.createInjector(BoundFieldModule.of(this)).injectMembers(this);
  }

  @Test
  public void generate_withoutParameters_generatesAllStrategies() {
    // Act
    ImmutableSet<TradeStrategy> strategies =
        tradeStrategyGenerator.generate().collect(toImmutableSet());

    // Assert
    assertThat(strategies).containsExactly(ADX_TRADE_STRATEGY, COMPOSITE_TRADE_STRATEGY);
  }

  @Test
  public void generate_withSingleParameter_generatesAppropriateStrategies(
      @TestParameter StrategyOneOfCase oneOfCase) {
    // Arrange
    ImmutableSet<TradeStrategy> expected =
        ADAPTERS_BY_ONE_OF_CASE.get(oneOfCase).stream()
            .flatMap(TradeStrategyAdapter::generate)
            .collect(toImmutableSet());

    // Act
    ImmutableSet<TradeStrategy> strategies =
        tradeStrategyGenerator.generate(oneOfCase).collect(toImmutableSet());

    // Assert
    assertThat(strategies).containsExactlyElementsIn(expected);
  }

  @AutoValue
  abstract static class FakeAdapter implements TradeStrategyAdapter {
    static FakeAdapter create(
        StrategyOneOfCase strategyOneOfCase,
        Function<TradeStrategy, Strategy> strategyFunction,
        TradeStrategy... strategies) {
      return new AutoValue_AdapterBasedTradeStrategyGeneratorTest_FakeAdapter(
          strategyOneOfCase, strategyFunction, ImmutableSet.copyOf(strategies));
    }

    abstract Function<TradeStrategy, Strategy> strategyFunction();

    abstract ImmutableSet<TradeStrategy> strategies();

    @Override
    public Stream<TradeStrategy> generate() {
      return strategies().stream();
    }

    @Override
    public Strategy toTa4jStrategy(TradeStrategy tradeStrategy, BarSeries barSeries) {
      return strategyFunction().apply(tradeStrategy);
    }
  }
}
