package com.verlumen.tradestar.core.backtesting;

import com.google.inject.AbstractModule;

public class BacktestingModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(Backtester.class).to(BacktesterImpl.class);
  }
}
