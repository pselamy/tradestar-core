package com.verlumen.tradestar.core.ta.rules;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import static com.google.inject.multibindings.Multibinder.newSetBinder;

public class RulesModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<RuleFactory> ruleFactoryBinder =
                newSetBinder(binder(), RuleFactory.class);
        ruleFactoryBinder.addBinding().to(AdxRuleFactory.class);
        ruleFactoryBinder.addBinding().to(CompositeRuleFactory.class);
    }
}
