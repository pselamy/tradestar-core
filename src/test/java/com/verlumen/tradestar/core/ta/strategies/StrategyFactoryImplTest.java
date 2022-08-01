package com.verlumen.tradestar.core.ta.strategies;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.google.testing.junit.testparameterinjector.TestParameter;
import com.google.testing.junit.testparameterinjector.TestParameterInjector;
import com.verlumen.tradestar.core.ta.rules.RuleFactory;
import com.verlumen.tradestar.protos.strategies.SignalStrength;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategy.StrategyOneOfCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ta4j.core.*;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.TruthJUnit.assume;

@RunWith(TestParameterInjector.class)
public class StrategyFactoryImplTest {
  private static final Rule BUY_RULE = (i, tradingRecord) -> false;
  private static final Rule SELL_RULE = (i, tradingRecord) -> true;

  private static final BaseBarSeries FAKE_BAR_SERIES = new BaseBarSeriesBuilder().build();
  private static final TradeStrategy ADX_TRADE_STRATEGY =
      TradeStrategy.newBuilder()
          .setAdx(
              TradeStrategy.ADX
                  .newBuilder()
                  .setBarCount(123)
                  .setBuySignalStrength(SignalStrength.STRONG)
                  .setSellSignalStrength(SignalStrength.WEAK))
          .build();

  @TestParameter private StrategyOneOfCase oneOfCase;

  @Bind(to = RuleFactory.class)
  private FakeRuleFactory ruleFactory;

  @Inject private StrategyFactoryImpl factory;

  @Before
  public void setup() {
    this.ruleFactory = FakeRuleFactory.create(oneOfCase);

    Guice.createInjector(BoundFieldModule.of(this)).injectMembers(this);
  }

  @Test
  public void create_withTradeStrategy_createsTa4jStrategy(@TestParameter CreateTestCase testCase) {
    assume().that(oneOfCase).isNotEqualTo(StrategyOneOfCase.STRATEGYONEOF_NOT_SET);
    assume().that(oneOfCase).isNotEqualTo(StrategyOneOfCase.COMPOSITE);
    String expectedName = "ADX-123-BUY-STRONG-SELL-WEAK";

    Strategy strategy = factory.create(testCase.tradeStrategy, FAKE_BAR_SERIES);

    assertThat(strategy.getName()).isEqualTo(expectedName);
    assertThat(strategy.getEntryRule()).isSameInstanceAs(BUY_RULE);
    assertThat(strategy.getExitRule()).isSameInstanceAs(SELL_RULE);
  }

  @SuppressWarnings("unused")
  private enum CreateTestCase {
    ADX(ADX_TRADE_STRATEGY);

    private final TradeStrategy tradeStrategy;

    CreateTestCase(TradeStrategy tradeStrategy) {
      this.tradeStrategy = tradeStrategy;
    }
  }

  private static class FakeRuleFactory implements RuleFactory {
    private final StrategyOneOfCase supportedCase;

    FakeRuleFactory(StrategyOneOfCase supportedCase) {
      this.supportedCase = supportedCase;
    }

    private static FakeRuleFactory create(StrategyOneOfCase oneOfCase) {
      return new FakeRuleFactory(oneOfCase);
    }

    @Override
    public Rule buyRule(TradeStrategy params, BarSeries barSeries) {
      return BUY_RULE;
    }

    @Override
    public Rule sellRule(TradeStrategy params, BarSeries barSeries) {
      return SELL_RULE;
    }

    @Override
    public StrategyOneOfCase supportedCase() {
      return supportedCase;
    }
  }
}
