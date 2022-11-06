package com.verlumen.tradestar.core.backtesting;

import com.google.inject.AbstractModule;
import com.verlumen.tradestar.core.strategies.adapters.AdaptersModule;

public class BacktestingModule extends AbstractModule {
  @Override
  protected void configure() {
    install(new AdaptersModule());
  }
}
