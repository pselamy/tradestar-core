package com.verlumen.tradestar.core.ta.rules.adapters;

import com.google.common.collect.Range;
import com.google.inject.Inject;
import com.verlumen.tradestar.core.ta.indicators.IndicatorFactory;
import com.verlumen.tradestar.core.ta.rules.RuleFactory;
import com.verlumen.tradestar.core.ta.signalstrength.SignalStrengthSpec;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategy.StrategyOneOfCase;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;

import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.MoreCollectors.onlyElement;
import static com.google.mu.util.stream.BiStream.biStream;

class AdxRuleAdapter implements RuleFactory.RuleAdapter {
  private final IndicatorFactory indicatorFactory;
  private final SignalStrengthSpec signalStrengthSpec;

  @Inject
  AdxRuleAdapter(IndicatorFactory indicatorFactory, Set<SignalStrengthSpec> signalStrengthSpecs) {
    StrategyOneOfCase supportedCase = StrategyOneOfCase.ADX;
    this.indicatorFactory = indicatorFactory;
    this.signalStrengthSpec =
        biStream(signalStrengthSpecs)
            .mapKeys(SignalStrengthSpec::supportedCase)
            .filterKeys(supportedCase::equals)
            .values()
            .collect(onlyElement());
  }

  @Override
  public CrossedUpIndicatorRule buyRule(TradeStrategy params, BarSeries barSeries) {
    checkArgument(supportedCase().equals(params.getStrategyOneOfCase()));
    checkArgument(Range.open(0, barSeries.getBarCount()).contains(params.getAdx().getBarCount()));
    Indicator<Num> indicator = indicatorFactory.create(params, barSeries);
    Num threshold =
        signalStrengthSpec.range(params.getAdx().getBuySignalStrength()).lowerEndpoint();
    return new CrossedUpIndicatorRule(indicator, threshold);
  }

  @Override
  public CrossedDownIndicatorRule sellRule(TradeStrategy params, BarSeries barSeries) {
    checkArgument(supportedCase().equals(params.getStrategyOneOfCase()));
    checkArgument(Range.open(0, barSeries.getBarCount()).contains(params.getAdx().getBarCount()));
    Indicator<Num> indicator = indicatorFactory.create(params, barSeries);
    Num threshold =
        signalStrengthSpec.range(params.getAdx().getSellSignalStrength()).upperEndpoint();
    return new CrossedDownIndicatorRule(indicator, threshold);
  }

  @Override
  public StrategyOneOfCase supportedCase() {
    return StrategyOneOfCase.ADX;
  }
}
