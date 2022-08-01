package com.verlumen.tradestar.core.backtesting;

import com.google.inject.AbstractModule;

public class BackTestingModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(BackTester.class).to(BackTesterImpl.class);
  }
}
