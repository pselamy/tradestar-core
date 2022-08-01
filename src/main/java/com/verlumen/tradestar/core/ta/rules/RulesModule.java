package com.verlumen.tradestar.core.ta.rules;

import com.google.inject.AbstractModule;
import com.verlumen.tradestar.core.ta.rules.adapters.AdaptersModule;

public class RulesModule extends AbstractModule {
  @Override
  protected void configure() {
    install(new AdaptersModule());

    bind(RuleFactory.class).to(RuleFactoryImpl.class);
  }
}
