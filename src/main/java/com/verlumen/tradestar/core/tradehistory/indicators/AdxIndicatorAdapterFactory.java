package com.verlumen.tradestar.core.tradehistory.indicators;

import com.google.common.collect.ImmutableList;
import com.verlumen.tradestar.core.tradehistory.indicators.IndicatorAdapterRepository.IndicatorAdapter;
import com.verlumen.tradestar.core.tradehistory.indicators.IndicatorAdapterRepository.IndicatorAdapterFactory;
import com.verlumen.tradestar.protos.indicators.Indicator;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.adx.ADXIndicator;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Math.max;

class AdxIndicatorAdapterFactory implements IndicatorAdapterFactory {
    private static final int DEFAULT_BAR_COUNT = 14;

    @Override
    public IndicatorAdapter create(
            BarSeries barSeries, Indicator.Params params) {
        checkArgument(params.hasAdx());
        int barCount = max(params.getAdx().getBarCount(), DEFAULT_BAR_COUNT);
        int diBarCount = max(params.getAdx().getDiBarCount(), barCount);
        return IndicatorAdapter.create(params,
                () -> new ADXIndicator(barSeries, barCount, diBarCount),
                () -> ImmutableList.of(barCount, diBarCount));
    }

    @Override
    public Indicator.Params.TypeCase typeCase() {
        return Indicator.Params.TypeCase.ADX;
    }
}
