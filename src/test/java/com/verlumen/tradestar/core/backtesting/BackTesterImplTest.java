package com.verlumen.tradestar.core.backtesting;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.verlumen.tradestar.core.backtesting.BackTester.TestParams;
import com.verlumen.tradestar.core.ta.strategies.StrategyFactory;
import com.verlumen.tradestar.protos.candles.Candle;
import com.verlumen.tradestar.protos.candles.Granularity;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategyTestResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.ta4j.core.*;
import org.ta4j.core.rules.BooleanRule;

import java.time.Duration;
import java.time.ZonedDateTime;

import static com.google.common.truth.Truth.assertThat;
import static java.lang.Math.random;
import static org.ta4j.core.num.DecimalNum.valueOf;

@RunWith(JUnit4.class)
public class BackTesterImplTest {

  private static final Strategy FAKE_STRATEGY =
      new BaseStrategy(new BooleanRule(true), new BooleanRule(false));
  private static final Bar BAR =
      BaseBar.builder()
          .closePrice(valueOf(random()))
          .endTime(ZonedDateTime.now())
          .timePeriod(Duration.ofDays(1))
          .build();
  private static final BarSeries BAR_SERIES =
      new BaseBarSeriesBuilder().withBars(ImmutableList.of(BAR)).build();
  private static final TradingRecord TRADING_RECORD = new BaseTradingRecord("fake-trading-record");
  private static final FakeBarSeriesManager FAKE_BAR_SERIES_MANAGER = new FakeBarSeriesManager();
  private static final ImmutableSet<Candle> ONE_MINUTE_CANDLES =
      ImmutableSet.of(newCandle(0), newCandle(60));
  private static final TestParams TEST_PARAMS =
      TestParams.builder()
          .setCandles(ONE_MINUTE_CANDLES)
          .setGranularity(Granularity.ONE_MINUTE)
          .setStrategy(TradeStrategy.getDefaultInstance())
          .build();

  @Bind
  private static final BarSeriesManagerFactory FAKE_BAR_SERIES_MANAGER_FACTORY =
      (candles) -> FAKE_BAR_SERIES_MANAGER;

  @Bind
  private static final StrategyFactory FAKE_STRATEGY_FACTORY =
      (tradeStrategy, barSeries) -> FAKE_STRATEGY;

  @Inject private BackTesterImpl backTester;

  private static Candle newCandle(long startTime) {
    Candle.Builder builder = Candle.newBuilder();
    builder.getStartBuilder().setSeconds(startTime);
    return builder.build();
  }

  @Before
  public void setup() {
    Guice.createInjector(BoundFieldModule.of(this)).injectMembers(this);
  }

  @Test
  public void run_withFoo_returnsBackTestResult() {
    TradeStrategyTestResult actual = backTester.test(TEST_PARAMS);

    assertThat(actual).isNotNull();
  }

  private static class FakeBarSeriesManager extends BarSeriesManager {
    @Override
    public BarSeries getBarSeries() {
      return BAR_SERIES;
    }

    @Override
    public TradingRecord run(Strategy strategy) {
      return TRADING_RECORD;
    }
  }
}
