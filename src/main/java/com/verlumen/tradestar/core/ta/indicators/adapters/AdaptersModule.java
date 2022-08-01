package com.verlumen.tradestar.core.ta.indicators.adapters;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.verlumen.tradestar.core.ta.indicators.IndicatorFactory;

import static com.google.inject.multibindings.Multibinder.newSetBinder;

public class AdaptersModule extends AbstractModule {
  @Override
  protected void configure() {
    Multibinder<IndicatorFactory.Adapter> adapterBinder =
        newSetBinder(binder(), IndicatorFactory.Adapter.class);
    adapterBinder.addBinding().to(AdxIndicatorAdapter.class);
  }
}
