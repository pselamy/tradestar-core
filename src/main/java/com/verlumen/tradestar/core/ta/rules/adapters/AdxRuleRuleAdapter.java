package com.verlumen.tradestar.core.ta.rules.adapters;

import com.google.inject.Inject;
import com.verlumen.tradestar.core.ta.indicators.IndicatorFactory;
import com.verlumen.tradestar.core.ta.rules.RuleFactory;
import com.verlumen.tradestar.core.ta.signalstrength.SignalStrengthSpec;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategy.ADX;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;

import java.util.Set;

import static com.google.common.collect.MoreCollectors.onlyElement;
import static com.google.mu.util.stream.BiStream.biStream;

class AdxRuleRuleAdapter implements RuleFactory.RuleAdapter {
  private final IndicatorFactory indicatorFactory;
  private final SignalStrengthSpec signalStrengthSpec;

  @Inject
  AdxRuleRuleAdapter(IndicatorFactory indicatorFactory, Set<SignalStrengthSpec> signalStrengthSpecs) {
    TradeStrategy.StrategyOneOfCase supportedCase = TradeStrategy.StrategyOneOfCase.ADX;
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
    ADX adx = params.getAdx();
    Indicator<Num> indicator = indicatorFactory.create(params, barSeries);
    Num threshold = signalStrengthSpec.range(adx.getBuySignalStrength()).lowerEndpoint();
    return new CrossedUpIndicatorRule(indicator, threshold);
  }

  @Override
  public CrossedDownIndicatorRule sellRule(TradeStrategy params, BarSeries barSeries) {
    ADX adx = params.getAdx();
    Indicator<Num> indicator = indicatorFactory.create(params, barSeries);
    Num threshold = signalStrengthSpec.range(adx.getSellSignalStrength()).upperEndpoint();
    return new CrossedDownIndicatorRule(indicator, threshold);
  }

  @Override
  public TradeStrategy.StrategyOneOfCase supportedCase() {
    return TradeStrategy.StrategyOneOfCase.ADX;
  }
}
