package com.verlumen.tradestar.core.backtesting;

import com.google.inject.Guice;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BacktestingModuleTest {
  private BacktestingModule underTest;

  @Before
  public void setUp() {
    underTest = new BacktestingModule();
  }

  @Test
  public void createInjector_consumesModule() {
    Guice.createInjector(underTest);
  }

  @Test
  public void module_providesBacktester() {
    Guice.createInjector(underTest).getInstance(Backtester.class);
  }
}
