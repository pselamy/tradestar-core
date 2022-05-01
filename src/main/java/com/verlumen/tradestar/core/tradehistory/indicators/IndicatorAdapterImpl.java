package com.verlumen.tradestar.core.tradehistory.indicators;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.verlumen.tradestar.core.tradehistory.indicators.IndicatorAdapterRepository.IndicatorIdSupplier;
import com.verlumen.tradestar.core.tradehistory.indicators.IndicatorAdapterRepository.Ta4jIndicatorSupplier;
import com.verlumen.tradestar.protos.indicators.Indicator;
import org.ta4j.core.num.Num;

import static com.google.common.base.MoreObjects.firstNonNull;

public class IndicatorAdapterImpl implements IndicatorAdapter {
    private static final Joiner HYPHEN_JOINER = Joiner.on("-");

    private final IndicatorIdSupplier indicatorIdSupplier;
    private final Ta4jIndicatorSupplier indicatorSupplier;
    private final Indicator.Params params;

    private IndicatorAdapterImpl(Ta4jIndicatorSupplier indicatorSupplier,
                                 IndicatorIdSupplier indicatorIdSupplier,
                                 Indicator.Params params) {
        this.indicatorIdSupplier = indicatorIdSupplier;
        this.indicatorSupplier = indicatorSupplier;
        this.params = params;
    }

    static IndicatorAdapter create(
            Indicator.Params params, Ta4jIndicatorSupplier indicatorSupplier,
            IndicatorIdSupplier indicatorIdSupplier) {
        return new IndicatorAdapterImpl(indicatorSupplier, indicatorIdSupplier, params);
    }

    @Override
    public Indicator indicator(int index) {
        double value = ta4jIndicator().getValue(index).doubleValue();
        return Indicator.newBuilder()
                .setName(name())
                .setValue(value)
                .setParams(params)
                .build();
    }

    @Override
    public org.ta4j.core.Indicator<Num> ta4jIndicator() {
        return indicatorSupplier.get();
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
