package com.verlumen.tradestar.core.backtesting;

import com.google.common.collect.ImmutableSet;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.verlumen.tradestar.core.strategies.adapters.AdaptersModule;
import com.verlumen.tradestar.core.strategies.adapters.TradeStrategyAdapter;

import java.util.Set;

public class BacktestingModule extends AbstractModule {
  @Override
  protected void configure() {
    install(new AdaptersModule());
  }

  @Provides
  Backtester backtester(Set<TradeStrategyAdapter> adapters, TestResultFactory testResultFactory) {
    return Backtester.create(ImmutableSet.copyOf(adapters), testResultFactory);
  }
}
