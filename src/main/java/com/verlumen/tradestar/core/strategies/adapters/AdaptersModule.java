package com.verlumen.tradestar.core.strategies.adapters;

import com.google.inject.AbstractModule;

import static com.google.inject.multibindings.Multibinder.newSetBinder;

public class AdaptersModule extends AbstractModule {
  @Override
  protected void configure() {
    newSetBinder(binder(), TradeStrategyAdapter.class)
        .addBinding()
        .to(AdxTradeStrategyAdapter.class);
    newSetBinder(binder(), TradeStrategyAdapter.class)
        .addBinding()
        .to(CompositeTradeStrategyAdapter.class);
  }
}
