package com.verlumen.tradestar.core.ta.signalstrength;

import com.google.inject.AbstractModule;

import static com.google.inject.multibindings.Multibinder.newSetBinder;

public class SignalStrengthModule extends AbstractModule {
  @Override
  protected void configure() {
    newSetBinder(binder(), SignalStrengthSpec.class).addBinding().to(AdxSignalStrengthSpec.class);
  }
}
