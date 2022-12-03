package com.verlumen.tradestar.core.backtesting;

import com.google.auto.value.AutoValue;
import com.verlumen.tradestar.core.backtesting.AnalysisCriteria.Criterion;
import com.verlumen.tradestar.protos.candles.CandleDescriptor;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategyTestResult;
import com.verlumen.tradestar.protos.strategies.TradeStrategyTestResult.MaxDrawdownReport;
import com.verlumen.tradestar.protos.strategies.TradeStrategyTestResult.PositionReport;
import com.verlumen.tradestar.protos.strategies.TradeStrategyTestResult.ProfitLossReport;
import com.verlumen.tradestar.protos.strategies.TradeStrategyTestResult.ReturnReport;
import org.ta4j.core.BarSeries;
import org.ta4j.core.TradingRecord;

import java.io.Serializable;
import java.time.Instant;
import java.util.Optional;

import static java.lang.Math.max;

class TestResultFactory implements Serializable {
  public static TradeStrategyTestResult create(
      Instant startInclusive,
      Instant endExclusive,
      CandleDescriptor candleDescriptor,
      TradeStrategy tradeStrategy,
      BarSeries series,
      TradingRecord record) {
    return Specimen.create(
            startInclusive, endExclusive, candleDescriptor, tradeStrategy, series, record)
        .testResult();
  }

  @AutoValue
  abstract static class Specimen {
    private static Specimen create(
        Instant startInclusive,
        Instant endExclusive,
        CandleDescriptor candleDescriptor,
        TradeStrategy tradeStrategy,
        BarSeries series,
        TradingRecord record) {
      return new AutoValue_TestResultFactory_Specimen(
          startInclusive, endExclusive, candleDescriptor, tradeStrategy, series, record);
    }

    abstract Instant startInclusive();

    abstract Instant endExclusive();

    abstract CandleDescriptor candleDescriptor();

    abstract TradeStrategy tradeStrategy();

    abstract BarSeries series();

    abstract TradingRecord record();

    private Optional<Double> doubleValue(Criterion criterion) {
      return criterion.doubleValue(series(), record());
    }

    private Optional<Integer> intValue(Criterion criterion) {
      return criterion.intValue(series(), record());
    }

    Optional<MaxDrawdownReport> maxDrawdownReport() {
      MaxDrawdownReport.Builder builder = MaxDrawdownReport.newBuilder();
      doubleValue(Criterion.MAX_DRAWDOWN).ifPresent(builder::setAmount);
      doubleValue(Criterion.RETURN_OVER_MAX_DRAWDOWN).ifPresent(builder::setReturn);
      return Optional.of(builder.build())
          .filter(report -> !report.equals(MaxDrawdownReport.getDefaultInstance()));
    }

    Optional<PositionReport> positionReport() {
      return intValue(Criterion.NUM_POS)
          .filter(positionCount -> positionCount > 0)
          .map(PositionReport.newBuilder()::setCount)
          .flatMap(builder -> intValue(Criterion.NUM_BREAK_EVEN_POS).map(builder::setBreakEven))
          .flatMap(
              builder ->
                  intValue(Criterion.NUM_CONSEC_LOSING_POS).map(builder::setConsecutiveLosing))
          .flatMap(
              builder ->
                  intValue(Criterion.NUM_CONSEC_WINNING_POS).map(builder::setConsecutiveWinning))
          .flatMap(builder -> intValue(Criterion.NUM_LOSING_POS).map(builder::setLosing))
          .flatMap(builder -> intValue(Criterion.LOSING_POS_RATIO).map(builder::setLosingRatio))
          .flatMap(builder -> intValue(Criterion.WINNING_POS_RATIO).map(builder::setWinningRatio))
          .map(
              builder ->
                  builder.setWinning(
                      max(builder.getCount() - builder.getBreakEven() - builder.getLosing(), 0)))
          .map(PositionReport.Builder::build);
    }

    Optional<ProfitLossReport> profitLossReport() {
      ProfitLossReport.Builder builder = ProfitLossReport.newBuilder();
      doubleValue(Criterion.PROFIT_LOSS).ifPresent(builder::setAmount);
      doubleValue(Criterion.AVG_LOSS).ifPresent(builder::setAverageLoss);
      doubleValue(Criterion.AVG_PROFIT).ifPresent(builder::setAverageProfit);
      doubleValue(Criterion.GROSS_LOSS).ifPresent(builder::setGrossLoss);
      doubleValue(Criterion.GROSS_PROFIT).ifPresent(builder::setGrossProfit);
      doubleValue(Criterion.NET_LOSS).ifPresent(builder::setNetLoss);
      doubleValue(Criterion.NET_PROFIT).ifPresent(builder::setNetProfit);
      doubleValue(Criterion.NET_PROFIT).ifPresent(builder::setNetProfit);
      doubleValue(Criterion.PROFIT_LOSS_PERCENTAGE).ifPresent(builder::setPercentage);
      doubleValue(Criterion.PROFIT_LOSS_RATIO).ifPresent(builder::setRatio);
      return Optional.of(builder.build())
          .filter(report -> !report.equals(ProfitLossReport.getDefaultInstance()));
    }

    Optional<ReturnReport> returnReport() {
      ReturnReport.Builder builder = ReturnReport.newBuilder();
      doubleValue(Criterion.AVG_RETURN_PER_BAR).ifPresent(builder::setAveragePerBar);
      doubleValue(Criterion.ENTER_AND_HOLD_RETURN).ifPresent(builder::setEnterAndHold);
      doubleValue(Criterion.EXPECTANCY).ifPresent(builder::setExpectancy);
      doubleValue(Criterion.GROSS_RETURN).ifPresent(builder::setGross);
      return Optional.of(builder.build())
          .filter(report -> !report.equals(ReturnReport.getDefaultInstance()));
    }

    TradeStrategyTestResult testResult() {
      TradeStrategyTestResult.Builder builder =
          TradeStrategyTestResult.newBuilder()
              .setCandleDescriptor(candleDescriptor())
              .setStrategy(tradeStrategy());
      builder.getStartBuilder().setSeconds(startInclusive().getEpochSecond());
      builder.getEndBuilder().setSeconds(endExclusive().getEpochSecond());
      maxDrawdownReport().ifPresent(builder::setMaxDrawdownReport);
      positionReport().ifPresent(builder::setPositionReport);
      profitLossReport().ifPresent(builder::setProfitLossReport);
      returnReport().ifPresent(builder::setReturnReport);
      return builder.build();
    }
  }
}
