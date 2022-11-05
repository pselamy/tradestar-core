package com.verlumen.tradestar.core.strategies.generators;

import com.google.inject.AbstractModule;
import com.verlumen.tradestar.core.strategies.adapters.AdaptersModule;

public class GeneratorsModule extends AbstractModule {
  @Override
  protected void configure() {
    install(new AdaptersModule());

    bind(TradeStrategyGenerator.class).to(AdapterBasedTradeStrategyGenerator.class);
  }
}
