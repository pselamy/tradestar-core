package com.verlumen.tradestar.core.strategies.adapters;

import com.google.common.collect.ImmutableSet;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import static com.google.inject.multibindings.Multibinder.newSetBinder;

public class AdaptersModule extends AbstractModule {
  private static final ImmutableSet<Class<? extends TradeStrategyAdapter>> TRADE_STRATEGY_ADAPTERS =
      ImmutableSet.of(AdxTradeStrategyAdapter.class, CompositeTradeStrategyAdapter.class);

  @Override
  protected void configure() {
    Multibinder<TradeStrategyAdapter> tradeStrategyAdapterMultibinder =
        newSetBinder(binder(), TradeStrategyAdapter.class);
    TRADE_STRATEGY_ADAPTERS.forEach(t -> tradeStrategyAdapterMultibinder.addBinding().to(t));
  }
}
