package com.verlumen.tradestar.core.tradehistory.indicators;

import com.google.common.base.Joiner;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.verlumen.tradestar.protos.indicators.Indicator;
import org.ta4j.core.BarSeries;
import org.ta4j.core.num.Num;

import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Functions.identity;
import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.collect.ImmutableMap.toImmutableMap;

public class IndicatorAdapterRepository {
    private final ImmutableMap<Indicator.Params.TypeCase, IndicatorAdapterFactory>
            adapterFactories;

    @Inject
    IndicatorAdapterRepository(Set<IndicatorAdapterFactory> adapterFactories) {
        this.adapterFactories = adapterFactories
                .stream()
                .collect(toImmutableMap(
                        IndicatorAdapterFactory::typeCase, identity()));
    }

    public IndicatorAdapter get(BarSeries barSeries, Indicator.Params params) {
        return Optional.ofNullable(adapterFactories.get(params.getTypeCase()))
                .map(factory -> factory.create(barSeries, params))
                .orElseThrow(UnsupportedOperationException::new);
    }

    interface IndicatorAdapterFactory {
        IndicatorAdapter create(BarSeries barSeries, Indicator.Params params);

        Indicator.Params.TypeCase typeCase();
    }

    interface IndicatorIdSupplier extends Supplier<ImmutableList<Integer>> {
    }

    interface Ta4jIndicatorSupplier extends Supplier<org.ta4j.core.Indicator<Num>> {
    }

    public static class IndicatorAdapter {
        private static final Joiner HYPHEN_JOINER = Joiner.on("-");

        private final org.ta4j.core.Indicator<Num> driver;
        private final IndicatorIdSupplier indicatorIdSupplier;
        private final Indicator.Params params;

        private IndicatorAdapter(Ta4jIndicatorSupplier indicatorSupplier,
                                 IndicatorIdSupplier indicatorIdSupplier,
                                 Indicator.Params params) {
            this.driver = indicatorSupplier.get();
            this.indicatorIdSupplier = indicatorIdSupplier;
            this.params = params;
        }

        static IndicatorAdapter create(
                Indicator.Params params, Ta4jIndicatorSupplier indicatorSupplier,
                IndicatorIdSupplier indicatorIdSupplier) {
            return new IndicatorAdapter(indicatorSupplier, indicatorIdSupplier, params);
        }

        public Indicator indicator(int index) {
            double value = driver.getValue(index).doubleValue();
            return Indicator.newBuilder()
                    .setName(name())
                    .setValue(value)
                    .setParams(params)
                    .build();
        }

        private String name() {
            ImmutableList<?> indicatorId = firstNonNull(
                    indicatorIdSupplier.get(), ImmutableList.of());
            ImmutableList<Object> parts = ImmutableList.builder()
                    .add(params.getTypeCase().name())
                    .addAll(indicatorId)
                    .build();
            indicatorIdSupplier.get();
            return HYPHEN_JOINER.join(parts);
        }
    }
}
