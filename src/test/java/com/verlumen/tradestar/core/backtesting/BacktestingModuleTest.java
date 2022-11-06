package com.verlumen.tradestar.core.backtesting;

import com.google.inject.Guice;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

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

  @Test
  public void module_providesSerializableBacktester() throws IOException {
    // Arrange
    Backtester backtester = Guice.createInjector(underTest).getInstance(Backtester.class);

    // Act
    new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(backtester);
  }
}
