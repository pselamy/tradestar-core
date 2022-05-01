package com.verlumen.tradestar.core.tradehistory.indicators;

import com.verlumen.tradestar.core.tradehistory.indicators.IndicatorFactory.IndicatorAdapter;
import com.verlumen.tradestar.core.tradehistory.indicators.IndicatorFactory.IndicatorAdapterFactory;
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
        ADXIndicator driver = driver(barSeries, params);
        return IndicatorAdapter.create(driver, params);
    }

    private ADXIndicator driver(BarSeries barSeries, Indicator.Params params) {
        checkArgument(params.hasAdx());
        int barCount = max(params.getAdx().getBarCount(), DEFAULT_BAR_COUNT);
        int diBarCount = max(params.getAdx().getDiBarCount(), DEFAULT_BAR_COUNT);
        return new ADXIndicator(barSeries, barCount, diBarCount);
    }

    @Override
    public Indicator.Params.TypeCase supportedParams() {
        return Indicator.Params.TypeCase.ADX;
    }
}
