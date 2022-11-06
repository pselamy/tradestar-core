package com.verlumen.tradestar.core.backtesting;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.verlumen.tradestar.core.backtesting.AnalysisCriteria.Criterion;
import com.verlumen.tradestar.protos.strategies.TradeStrategyTestResult;
import com.verlumen.tradestar.protos.strategies.TradeStrategyTestResult.MaxDrawdownReport;
import com.verlumen.tradestar.protos.strategies.TradeStrategyTestResult.PositionReport;
import com.verlumen.tradestar.protos.strategies.TradeStrategyTestResult.ProfitLossReport;
import com.verlumen.tradestar.protos.strategies.TradeStrategyTestResult.ReturnReport;
import org.ta4j.core.BarSeries;
import org.ta4j.core.TradingRecord;

import java.io.Serializable;

import static java.lang.Math.max;

class TestResultFactory implements Serializable {
  public TradeStrategyTestResult create(BarSeries series, TradingRecord record) {
    return Specimen.create(series, record).testResult();
  }

  @AutoValue
  abstract static class Specimen {
    private static Specimen create(BarSeries series, TradingRecord record) {
      return new AutoValue_TestResultFactory_Specimen(series, record);
    }

    abstract BarSeries series();

    abstract TradingRecord record();

    private double doubleValue(Criterion criterion) {
      return criterion.doubleValue(series(), record());
    }

    private int intValue(Criterion criterion) {
      return criterion.intValue(series(), record());
    }

    @Memoized
    MaxDrawdownReport maxDrawdownReport() {
      return MaxDrawdownReport.newBuilder()
          .setAmount(doubleValue(Criterion.MAX_DRAWDOWN))
          .setReturn(doubleValue(Criterion.RETURN_OVER_MAX_DRAWDOWN))
          .build();
    }

    @Memoized
    PositionReport positionReport() {
      int count = intValue(Criterion.NUM_POS);
      int breakEven = intValue(Criterion.NUM_BREAK_EVEN_POS);
      int losing = intValue(Criterion.NUM_LOSING_POS);
      int winning = max(count - breakEven - losing, 0);
      return PositionReport.newBuilder()
          .setBreakEven(breakEven)
          .setConsecutiveWinning(intValue(Criterion.NUM_CONSEC_WIN_POS))
          .setCount(count)
          .setLosing(losing)
          .setLosingRatio(doubleValue(Criterion.LOSING_POS_RATIO))
          .setWinning(winning)
          .setWinningRatio(doubleValue(Criterion.WINNING_POS_RATIO))
          .build();
    }

    @Memoized
    ProfitLossReport profitLossReport() {
      return ProfitLossReport.newBuilder()
          .setAmount(doubleValue(Criterion.PROFIT_LOSS))
          .setAverageLoss(doubleValue(Criterion.AVG_LOSS))
          .setAverageProfit(doubleValue(Criterion.AVG_PROFIT))
          .setGrossLoss(doubleValue(Criterion.GROSS_LOSS))
          .setGrossProfit(doubleValue(Criterion.GROSS_PROFIT))
          .setNetLoss(doubleValue(Criterion.NET_LOSS))
          .setNetProfit(doubleValue(Criterion.NET_PROFIT))
          .setPercentage(doubleValue(Criterion.PROFIT_LOSS_PERCENTAGE))
          .setRatio(doubleValue(Criterion.PROFIT_LOSS_RATIO))
          .build();
    }

    @Memoized
    ReturnReport returnReport() {
      return ReturnReport.newBuilder()
          .setAveragePerBar(doubleValue(Criterion.AVG_RETURN_PER_BAR))
          .setBuyAndHold(doubleValue(Criterion.BUY_AND_HOLD_RETURN))
          .setExpectancy(doubleValue(Criterion.EXPECTANCY))
          .setGross(doubleValue(Criterion.GROSS_RETURN))
          .build();
    }

    @Memoized
    TradeStrategyTestResult testResult() {
      return TradeStrategyTestResult.newBuilder()
          .setMaxDrawdownReport(maxDrawdownReport())
          .setPositionReport(positionReport())
          .setProfitLossReport(profitLossReport())
          .setReturnReport(returnReport())
          .build();
    }
  }
}
