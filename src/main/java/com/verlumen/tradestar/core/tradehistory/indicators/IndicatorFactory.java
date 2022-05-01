package com.verlumen.tradestar.core.tradehistory.indicators;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.verlumen.tradestar.protos.indicators.Indicator;
import org.ta4j.core.BarSeries;
import org.ta4j.core.num.Num;

import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Functions.identity;
import static com.google.common.collect.ImmutableMap.toImmutableMap;

public class IndicatorFactory {
    private final ImmutableMap<Indicator.Params.TypeCase, TypedIndicatorFactory>
            indicatorFactories;

    @Inject
    IndicatorFactory(Set<TypedIndicatorFactory> indicatorFactories) {
        this.indicatorFactories = indicatorFactories
                .stream()
                .collect(toImmutableMap(
                        TypedIndicatorFactory::supportedParams, identity()));
    }

    public TypedIndicator create(BarSeries barSeries, Indicator.Params params) {
        return Optional.ofNullable(indicatorFactories.get(params.getTypeCase()))
                .map(factory -> factory.create(barSeries, params))
                .orElseThrow(UnsupportedOperationException::new);
    }

    public interface TypedIndicatorFactory {
        TypedIndicator create(BarSeries barSeries, Indicator.Params params);

        Indicator.Params.TypeCase supportedParams();
    }

    public interface TypedIndicator {
        org.ta4j.core.Indicator<Num> driver();

        Indicator indicator(int index);

        default double value(int index) {
            return driver().getValue(index).doubleValue();
        }

    }
}
