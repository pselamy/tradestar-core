package com.verlumen.tradestar.core.ta.indicators;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategy.StrategyOneOfCase;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;

import java.util.Set;

import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;

class IndicatorFactoryImpl implements IndicatorFactory {
    private final ImmutableMap<StrategyOneOfCase, Adapter> adapters;

    @Inject
    IndicatorFactoryImpl(Set<Adapter> adapters) {
        this.adapters = adapters.stream()
                .collect(toImmutableMap(Adapter::supportedCase, identity()));
    }

    @Override
    public Indicator<Num> create(TradeStrategy strategy, BarSeries series) {
        return ofNullable(adapters.get(strategy.getStrategyOneOfCase()))
                .map(adapter -> adapter.indicator(strategy, series))
                .orElseThrow(UnsupportedOperationException::new);
    }
}
