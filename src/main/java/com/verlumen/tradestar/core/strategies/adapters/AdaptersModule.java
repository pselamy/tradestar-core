package com.verlumen.tradestar.core.strategies.adapters;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import static com.google.inject.multibindings.Multibinder.newSetBinder;

public class AdaptersModule extends AbstractModule {
  @Override
  protected void configure() {
    bindTradeStrategyAdapters(newSetBinder(binder(), TradeStrategyAdapter.class));
  }

  private void bindTradeStrategyAdapters(Multibinder<TradeStrategyAdapter> multibinder) {
    multibinder.addBinding().to(AdxTradeStrategyAdapter.class);
  }
}
