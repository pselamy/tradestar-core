package com.verlumen.tradestar.core.ta.rules.adapters;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Range;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.testing.junit.testparameterinjector.TestParameter;
import com.google.testing.junit.testparameterinjector.TestParameterInjector;
import com.verlumen.tradestar.core.ta.indicators.IndicatorFactory;
import com.verlumen.tradestar.core.ta.signalstrength.SignalStrengthModule;
import com.verlumen.tradestar.protos.strategies.SignalStrength;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ta4j.core.*;
import org.ta4j.core.indicators.adx.ADXIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;

import java.time.Duration;
import java.time.ZonedDateTime;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.TruthJUnit.assume;
import static java.lang.Math.random;
import static org.junit.Assert.assertThrows;

@RunWith(TestParameterInjector.class)
public class AdxRuleAdapterTest {
  private static final ImmutableSet<SignalStrength> UNSUPPORTED_SIGNAL_STRENGTHS =
      ImmutableSet.of(SignalStrength.UNRECOGNIZED, SignalStrength.UNSPECIFIED);
  private static final ImmutableMap<SignalStrength, Range<Integer>> ADX_VALUES_BY_SIGNAl_STRENGTH =
      ImmutableMap.of(
          SignalStrength.WEAK, Range.closedOpen(0, 25),
          SignalStrength.STRONG, Range.closedOpen(25, 50),
          SignalStrength.VERY_STRONG, Range.closedOpen(50, 75),
          SignalStrength.EXTREMELY_STRONG, Range.closed(75, 100));

  private static final int BAR_COUNT = 1;
  private static final ImmutableList<Bar> BARS = ImmutableList.of(newBar(), newBar());
  private static final BarSeries BAR_SERIES = new BaseBarSeriesBuilder().withBars(BARS).build();
  private static final ADXIndicator INDICATOR = new ADXIndicator(BAR_SERIES, BAR_COUNT);
  private static final TradeStrategy.ADX ADX_TRADE_STRATEGY =
      TradeStrategy.ADX
          .newBuilder()
          .setBarCount(BAR_COUNT)
          .setBuySignalStrength(SignalStrength.VERY_STRONG)
          .setSellSignalStrength(SignalStrength.WEAK)
          .build();
  private static final TradeStrategy.ADX TRADE_STRATEGY_WITH_ZERO_BAR_COUNT =
      ADX_TRADE_STRATEGY.toBuilder().clearBarCount().build();
  private static final TradeStrategy.ADX TRADE_STRATEGY_WITH_NEGATIVE_BAR_COUNT =
      ADX_TRADE_STRATEGY.toBuilder().setBarCount(-1).build();
  private static final TradeStrategy.ADX TRADE_STRATEGY_WITH_EXCESSIVE_BAR_COUNT =
      ADX_TRADE_STRATEGY.toBuilder().setBarCount(2).build();

  @Inject private AdxRuleAdapter adapter;

  private static BaseBar newBar() {
    return BaseBar.builder()
        .timePeriod(Duration.ZERO)
        .endTime(ZonedDateTime.now())
        .closePrice(DecimalNum.valueOf(random()))
        .build();
  }

  @Before
  public void setup() {
    Guice.createInjector(new SignalStrengthModule(), new TestModule()).injectMembers(this);
  }

  @Test
  public void buyRule_withMissingArguments_throwsIllegalArgumentException(
      @TestParameter BuyRuleWithMissingArgumentsTestCase testCase) {
    assertThrows(
        IllegalArgumentException.class, () -> adapter.buyRule(testCase.tradeStrategy, BAR_SERIES));
  }

  @Test
  public void buyRule_withSupportedSignalStrength_returnsEntryRule(
      @TestParameter SignalStrength signalStrength) {
    assume().that(signalStrength).isNotIn(UNSUPPORTED_SIGNAL_STRENGTHS);
    int buyThreshold = getSignalStrengthRange(signalStrength).lowerEndpoint();
    CrossedUpIndicatorRule expected = new CrossedUpIndicatorRule(INDICATOR, buyThreshold);
    TradeStrategy.ADX adx =
        ADX_TRADE_STRATEGY.toBuilder().setBuySignalStrength(signalStrength).build();
    TradeStrategy tradeStrategy = TradeStrategy.newBuilder().setAdx(adx).build();

    CrossedUpIndicatorRule actual = adapter.buyRule(tradeStrategy, BAR_SERIES);

    assertThat(actual.getLow().toString()).isEqualTo(expected.getLow().toString());
    assertThat(actual.getUp().toString()).isEqualTo(expected.getUp().toString());
  }

  @Test
  public void sellRule_withMissingArguments_throwsIllegalArgumentException(
      @TestParameter SellRuleWithMissingArgumentsTestCase testCase) {
    assertThrows(
        IllegalArgumentException.class, () -> adapter.sellRule(testCase.tradeStrategy, BAR_SERIES));
  }

  @Test
  public void sellRule_withSupportedSignalStrengths_returnsExitRule(
      @TestParameter SignalStrength signalStrength) {
    assume().that(signalStrength).isNotIn(UNSUPPORTED_SIGNAL_STRENGTHS);
    int sellThreshold = getSignalStrengthRange(signalStrength).upperEndpoint();
    CrossedDownIndicatorRule expected = new CrossedDownIndicatorRule(INDICATOR, sellThreshold);
    TradeStrategy.ADX adx =
        ADX_TRADE_STRATEGY.toBuilder().setSellSignalStrength(signalStrength).build();
    TradeStrategy tradeStrategy = TradeStrategy.newBuilder().setAdx(adx).build();

    CrossedDownIndicatorRule actual = adapter.sellRule(tradeStrategy, BAR_SERIES);

    assertThat(actual.getLow().toString()).isEqualTo(expected.getLow().toString());
    assertThat(actual.getUp().toString()).isEqualTo(expected.getUp().toString());
  }

  private Range<Integer> getSignalStrengthRange(SignalStrength buySignalStrength) {
    return ADX_VALUES_BY_SIGNAl_STRENGTH.get(buySignalStrength);
  }

  @SuppressWarnings("unused")
  private enum BuyRuleWithMissingArgumentsTestCase {
    EMPTY(TradeStrategy.ADX.getDefaultInstance()),
    NEGATIVE_BAR_COUNT(TRADE_STRATEGY_WITH_NEGATIVE_BAR_COUNT),
    ZERO_BAR_COUNT(TRADE_STRATEGY_WITH_ZERO_BAR_COUNT),
    HIGHER_BAR_COUNT_THAN_BAR_SERIES_COUNT(TRADE_STRATEGY_WITH_EXCESSIVE_BAR_COUNT),
    NO_BUY_SIGNAL_STRENGTH(ADX_TRADE_STRATEGY.toBuilder().clearBuySignalStrength().build());

    private final TradeStrategy tradeStrategy;

    BuyRuleWithMissingArgumentsTestCase(TradeStrategy.ADX adx) {
      this.tradeStrategy = TradeStrategy.newBuilder().setAdx(adx).build();
    }
  }

  @SuppressWarnings("unused")
  private enum SellRuleWithMissingArgumentsTestCase {
    EMPTY(TradeStrategy.ADX.getDefaultInstance()),
    NEGATIVE_BAR_COUNT(TRADE_STRATEGY_WITH_NEGATIVE_BAR_COUNT),
    ZERO_BAR_COUNT(TRADE_STRATEGY_WITH_ZERO_BAR_COUNT),
    HIGHER_BAR_COUNT_THAN_BAR_SERIES_COUNT(TRADE_STRATEGY_WITH_EXCESSIVE_BAR_COUNT),
    NO_SELL_SIGNAL_STRENGTH(ADX_TRADE_STRATEGY.toBuilder().clearSellSignalStrength().build());

    private final TradeStrategy tradeStrategy;

    SellRuleWithMissingArgumentsTestCase(TradeStrategy.ADX adx) {
      this.tradeStrategy = TradeStrategy.newBuilder().setAdx(adx).build();
    }
  }

  private static class FakeIndicatorFactory implements IndicatorFactory {
    @Override
    public Indicator<Num> create(TradeStrategy tradeStrategy, BarSeries barSeries) {
      return INDICATOR;
    }
  }

  private static class TestModule extends AbstractModule {
    @Override
    protected void configure() {
      bind(IndicatorFactory.class).to(FakeIndicatorFactory.class);
    }
  }
}
