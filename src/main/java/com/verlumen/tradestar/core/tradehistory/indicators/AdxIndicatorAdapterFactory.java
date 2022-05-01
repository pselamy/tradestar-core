package com.verlumen.tradestar.core.tradehistory.indicators;

import com.verlumen.tradestar.protos.indicators.Indicator;
import org.ta4j.core.BarSeries;

import static com.google.common.base.Preconditions.checkArgument;

class AdxIndicatorAdapterFactory
        implements IndicatorFactory.IndicatorAdapterFactory {
    @Override
    public IndicatorFactory.IndicatorAdapter create(
            BarSeries barSeries, Indicator.Params params) {
        checkArgument(params.hasAdx());
        int barCount = Math.max(params.getAdx().getBarCount(), 14);
        int diBarCount = Math.max(params.getAdx().getDiBarCount(), 14);
        org.ta4j.core.indicators.adx.ADXIndicator driver =
                new org.ta4j.core.indicators.adx.ADXIndicator(
                        barSeries, barCount, diBarCount);
        return IndicatorFactory.IndicatorAdapter.create(
                driver, Indicator.Type.ADX);
    }

    @Override
    public Indicator.Params.TypeCase supportedParams() {
        return Indicator.Params.TypeCase.ADX;
    }
}
