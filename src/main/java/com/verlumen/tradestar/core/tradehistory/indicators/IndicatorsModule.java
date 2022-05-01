package com.verlumen.tradestar.core.tradehistory.indicators;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.verlumen.tradestar.core.tradehistory.indicators.IndicatorFactory.IndicatorAdapterFactory;

public class IndicatorsModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<IndicatorAdapterFactory> indicatorAdapterFactoryBinder =
                Multibinder.newSetBinder(binder(),
                        IndicatorAdapterFactory.class);
        indicatorAdapterFactoryBinder.addBinding().to(
                AdxIndicatorAdapterFactory.class);
    }
}
