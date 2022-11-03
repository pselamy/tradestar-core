package com.verlumen.tradestar.core.shared;

import com.verlumen.tradestar.protos.candles.Candle;

import java.util.Comparator;

public enum CandleComparators implements Comparator<Candle> {
  START_TIME_ASCENDING((Comparator.comparingLong(candle -> candle.getStart().getSeconds()))),
  START_TIME_DESCENDING(START_TIME_ASCENDING.reversed());

  private final Comparator<Candle> comparator;

  CandleComparators(Comparator<Candle> comparator) {
    this.comparator = comparator;
  }

  @Override
  public int compare(Candle candle1, Candle candle2) {
    return comparator.compare(candle1, candle2);
  }
}
