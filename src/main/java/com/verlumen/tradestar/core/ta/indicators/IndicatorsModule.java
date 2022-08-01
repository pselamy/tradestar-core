package com.verlumen.tradestar.core.ta.indicators;

import com.google.inject.AbstractModule;
import com.verlumen.tradestar.core.ta.indicators.adapters.AdaptersModule;

public class IndicatorsModule extends AbstractModule {
  @Override
  protected void configure() {
    install(new AdaptersModule());

    bind(IndicatorFactory.class).to(IndicatorFactoryImpl.class);
  }
}
