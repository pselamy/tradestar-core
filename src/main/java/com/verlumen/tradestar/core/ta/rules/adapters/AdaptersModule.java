package com.verlumen.tradestar.core.ta.rules.adapters;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.verlumen.tradestar.core.ta.rules.RuleFactory;

import static com.google.inject.multibindings.Multibinder.newSetBinder;

public class AdaptersModule extends AbstractModule {
  @Override
  protected void configure() {
    Multibinder<RuleFactory.RuleAdapter> ruleFactoryBinder =
        newSetBinder(binder(), RuleFactory.RuleAdapter.class);
    ruleFactoryBinder.addBinding().to(AdxRuleAdapter.class);
    ruleFactoryBinder.addBinding().to(CompositeRuleAdapter.class);
  }
}
