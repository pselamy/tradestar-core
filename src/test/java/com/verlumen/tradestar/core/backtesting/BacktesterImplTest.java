package com.verlumen.tradestar.core.backtesting;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.verlumen.tradestar.core.backtesting.Backtester.Params;
import com.verlumen.tradestar.core.strategies.adapters.TradeStrategyAdapter;
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
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.google.common.truth.Truth.assertThat;
import static java.lang.Math.random;
import static org.ta4j.core.num.DecimalNum.valueOf;

@RunWith(JUnit4.class)
public class BacktesterImplTest {
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
      ImmutableSet.of(newCandle(Granularity.ONE_MINUTE, 0), newCandle(Granularity.ONE_MINUTE, 60));

  private static final Strategy ADX_STRATEGY =
      new BaseStrategy(new BooleanRule(true), new BooleanRule(true));
  private static final TradeStrategy ADX_TRADE_STRATEGY =
      TradeStrategy.newBuilder().setAdx(TradeStrategy.ADX.getDefaultInstance()).build();
  private static final Strategy COMPOSITE_STRATEGY =
      new BaseStrategy(new BooleanRule(true), new BooleanRule(false));
  private static final TradeStrategy COMPOSITE_TRADE_STRATEGY =
      TradeStrategy.newBuilder().setComposite(TradeStrategy.Composite.getDefaultInstance()).build();

  private static final TradeStrategyAdapter ADX_ADAPTER =
      FakeAdapter.create(
          TradeStrategy.StrategyOneOfCase.ADX, ignored -> ADX_STRATEGY, ADX_TRADE_STRATEGY);
  private static final TradeStrategyAdapter COMPOSITE_ADAPTER =
      FakeAdapter.create(
          TradeStrategy.StrategyOneOfCase.COMPOSITE,
          ignored -> COMPOSITE_STRATEGY,
          COMPOSITE_TRADE_STRATEGY);

  @Bind
  private static final Set<TradeStrategyAdapter> ADAPTERS =
      ImmutableSet.of(ADX_ADAPTER, COMPOSITE_ADAPTER);

  private static final Params TEST_PARAMS = Params.create(ONE_MINUTE_CANDLES, ADX_TRADE_STRATEGY);
  @Inject private BacktesterImpl backTester;

  private static Candle newCandle(Granularity granularity, long startTime) {
    Candle.Builder builder =
        Candle.newBuilder().setOpen(25).setHigh(50).setLow(5).setClose(23).setVolume(10323);
    builder.getCandleDescriptorBuilder().setGranularity(granularity);
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

  @AutoValue
  abstract static class FakeAdapter implements TradeStrategyAdapter {
    static FakeAdapter create(
        TradeStrategy.StrategyOneOfCase strategyOneOfCase,
        Function<TradeStrategy, Strategy> strategyFunction,
        TradeStrategy... strategies) {
      return new AutoValue_BacktesterImplTest_FakeAdapter(
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
