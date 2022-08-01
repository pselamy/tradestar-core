package com.verlumen.tradestar.core.ta.indicators.adapters;

import com.google.common.collect.Range;
import com.verlumen.tradestar.core.ta.indicators.IndicatorFactory;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategy.StrategyOneOfCase;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.adx.ADXIndicator;
import org.ta4j.core.num.Num;

import static com.google.common.base.Preconditions.checkArgument;

class AdxIndicatorAdapter implements IndicatorFactory.Adapter {
  @Override
  public Indicator<Num> indicator(TradeStrategy strategy, BarSeries series) {
    checkArgument(
        StrategyOneOfCase.ADX.equals(strategy.getStrategyOneOfCase()),
        "StrategyOneOfCase must be ADX");
    checkArgument(Range.open(0, series.getBarCount()).contains(strategy.getAdx().getBarCount()));
    return new ADXIndicator(series, strategy.getAdx().getBarCount());
  }

  @Override
  public StrategyOneOfCase supportedCase() {
    return StrategyOneOfCase.ADX;
  }
}
