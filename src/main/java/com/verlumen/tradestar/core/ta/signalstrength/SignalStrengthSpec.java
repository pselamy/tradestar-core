package com.verlumen.tradestar.core.ta.signalstrength;

import com.google.common.collect.Range;
import com.verlumen.tradestar.protos.strategies.SignalStrength;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import org.ta4j.core.num.Num;

public interface SignalStrengthSpec {
  Range<Num> range(SignalStrength signalStrength);

  TradeStrategy.StrategyOneOfCase supportedCase();
}
