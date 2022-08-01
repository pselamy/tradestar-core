package com.verlumen.tradestar.core.backtesting;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.inject.Inject;
import com.verlumen.tradestar.protos.strategies.TradeStrategyTestResult;
import com.verlumen.tradestar.protos.strategies.TradeStrategyTestResult.MaxDrawdownReport;
import com.verlumen.tradestar.protos.strategies.TradeStrategyTestResult.PositionReport;
import com.verlumen.tradestar.protos.strategies.TradeStrategyTestResult.ProfitLossReport;
import com.verlumen.tradestar.protos.strategies.TradeStrategyTestResult.ReturnReport;
import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.BarSeries;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.num.Num;

import static java.lang.Math.max;

class TestResultFactory {
  private final AnalysisCriteria criteria;

  @Inject
  TestResultFactory(AnalysisCriteria criteria) {
    this.criteria = criteria;
  }

  public TradeStrategyTestResult create(BarSeries series, TradingRecord record) {
    return Specimen.create(criteria, series, record).testResult();
  }

  @AutoValue
  abstract static class Specimen {
    private static Specimen create(
        AnalysisCriteria criteria, BarSeries series, TradingRecord record) {
      return new AutoValue_TestResultFactory_Specimen(criteria, series, record);
    }

    abstract AnalysisCriteria criteria();

    abstract BarSeries series();

    abstract TradingRecord record();

    @Memoized
    MaxDrawdownReport maxDrawdownReport() {
      return MaxDrawdownReport.newBuilder()
          .setAmount(doubleValue(criteria().maximumDrawdown()))
          .setReturn(doubleValue(criteria().returnOverMaxDrawdown()))
          .build();
    }

    @Memoized
    PositionReport positionReport() {
      int count = intValue(criteria().numberOfPositions());
      int breakEven = intValue(criteria().numberOfBreakEvenPositions());
      int losing = intValue(criteria().numberOfLosingPositions());
      int winning = max(count - breakEven - losing, 0);
      return PositionReport.newBuilder()
          .setBreakEven(breakEven)
          .setConsecutiveWinning(intValue(criteria().numberOfConsecutiveWinningPositions()))
          .setCount(count)
          .setLosing(losing)
          .setLosingRatio(doubleValue(criteria().losingPositionsRatio()))
          .setWinning(winning)
          .setWinningRatio(doubleValue(criteria().winningPositionsRatio()))
          .build();
    }

    @Memoized
    ProfitLossReport profitLossReport() {
      return ProfitLossReport.newBuilder()
          .setAmount(doubleValue(criteria().profitLoss()))
          .setAverageLoss(doubleValue(criteria().averageLoss()))
          .setAverageProfit(doubleValue(criteria().averageProfit()))
          .setGrossLoss(doubleValue(criteria().grossLoss()))
          .setGrossProfit(doubleValue(criteria().grossProfit()))
          .setNetLoss(doubleValue(criteria().netLoss()))
          .setNetProfit(doubleValue(criteria().netProfit()))
          .setPercentage(doubleValue(criteria().profitLossPercentage()))
          .setRatio(doubleValue(criteria().profitLossRatio()))
          .build();
    }

    @Memoized
    ReturnReport returnReport() {
      return ReturnReport.newBuilder()
          .setAveragePerBar(doubleValue(criteria().averageReturnPerBar()))
          .setBuyAndHold(doubleValue(criteria().buyAndHoldReturn()))
          .setExpectancy(doubleValue(criteria().expectancy()))
          .setGross(doubleValue(criteria().grossReturn()))
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

    private double doubleValue(AnalysisCriterion criterion) {
      return getValue(criterion).doubleValue();
    }

    private int intValue(AnalysisCriterion criterion) {
      return getValue(criterion).intValue();
    }

    private Num getValue(AnalysisCriterion criterion) {
      return criterion.calculate(series(), record());
    }
  }
}
