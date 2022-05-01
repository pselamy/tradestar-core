package com.verlumen.tradestar.core.tradehistory.indicators;

import com.google.common.collect.ImmutableSet;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.verlumen.tradestar.core.tradehistory.indicators.IndicatorFactory.IndicatorAdapterFactory;

public class IndicatorsModule extends AbstractModule {
    private static final ImmutableSet<Class<? extends IndicatorAdapterFactory>>
            ADAPTERS = ImmutableSet.of(AdxIndicatorAdapterFactory.class);

    @Override
    protected void configure() {
        Multibinder<IndicatorAdapterFactory> indicatorAdapterFactoryBinder =
                Multibinder.newSetBinder(binder(),
                        IndicatorAdapterFactory.class);
        ADAPTERS.forEach(adapter ->indicatorAdapterFactoryBinder.addBinding()
                .to(adapter));
    }
}
