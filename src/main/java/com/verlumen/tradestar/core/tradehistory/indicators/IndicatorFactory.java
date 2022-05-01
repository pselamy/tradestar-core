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
    private final ImmutableMap<Indicator.Params.TypeCase, IndicatorAdapterFactory>
            indicatorFactories;

    @Inject
    IndicatorFactory(Set<IndicatorAdapterFactory> indicatorFactories) {
        this.indicatorFactories = indicatorFactories
                .stream()
                .collect(toImmutableMap(
                        IndicatorAdapterFactory::supportedParams, identity()));
    }

    public IndicatorAdapter create(BarSeries barSeries, Indicator.Params params) {
        return Optional.ofNullable(indicatorFactories.get(params.getTypeCase()))
                .map(factory -> factory.create(barSeries, params))
                .orElseThrow(UnsupportedOperationException::new);
    }

    interface IndicatorAdapterFactory {
        IndicatorAdapter create(BarSeries barSeries, Indicator.Params params);

        Indicator.Params.TypeCase supportedParams();
    }

    public static class IndicatorAdapter {
        private final org.ta4j.core.Indicator<Num> driver;
        private final Indicator.Params params;

        private IndicatorAdapter(org.ta4j.core.Indicator<Num> driver,
                                 Indicator.Params params) {
            this.driver = driver;
            this.params = params;
        }

        public static IndicatorAdapter create(org.ta4j.core.Indicator<Num> driver,
                                              Indicator.Params params) {
            return new IndicatorAdapter(driver, params);
        }

        Indicator indicator(int index) {
            double value = driver.getValue(index).doubleValue();
            return Indicator.newBuilder()
                    .setParams(params)
                    .setValue(value)
                    .build();
        }
    }
}
