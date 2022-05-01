package com.verlumen.tradestar.core.tradehistory.indicators;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.verlumen.tradestar.protos.indicators.Indicator;
import com.verlumen.tradestar.protos.indicators.IndicatorParams;
import org.ta4j.core.BarSeries;

import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Functions.identity;
import static com.google.common.collect.ImmutableMap.toImmutableMap;

public class IndicatorFactory {
    private final ImmutableMap<IndicatorParams.TypeCase, TypedIndicatorFactory>
            indicatorFactories;

    @Inject
    IndicatorFactory(Set<TypedIndicatorFactory> indicatorFactories) {
        this.indicatorFactories = indicatorFactories
                .stream()
                .collect(toImmutableMap(
                        TypedIndicatorFactory::supportedParams, identity()));
    }

    public Indicator create(BarSeries barSeries, IndicatorParams params) {
        return Optional.ofNullable(indicatorFactories.get(params.getTypeCase()))
                .map(factory -> factory.create(barSeries, params))
                .orElseThrow(UnsupportedOperationException::new);
    }

    public interface TypedIndicatorFactory {
        Indicator create(BarSeries barSeries, IndicatorParams params);

        IndicatorParams.TypeCase supportedParams();
    }
}
