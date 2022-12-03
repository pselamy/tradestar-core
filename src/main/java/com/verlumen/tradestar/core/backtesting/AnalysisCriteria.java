package com.verlumen.tradestar.core.backtesting;

import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.BarSeries;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.criteria.*;
import org.ta4j.core.criteria.pnl.*;
import org.ta4j.core.num.Num;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Optional;

class AnalysisCriteria implements Serializable {
  public enum Criterion {
    AVG_LOSS(AverageLossCriterion.class),
    AVG_PROFIT(AverageProfitCriterion.class),
    AVG_RETURN_PER_BAR(AverageReturnPerBarCriterion.class),
    ENTER_AND_HOLD_RETURN(EnterAndHoldReturnCriterion.class),
    EXPECTANCY(ExpectancyCriterion.class),
    GROSS_LOSS(GrossLossCriterion.class),
    GROSS_PROFIT(GrossProfitCriterion.class),
    GROSS_RETURN(GrossReturnCriterion.class),
    LOSING_POS_RATIO(LosingPositionsRatioCriterion.class),
    MAX_DRAWDOWN(MaximumDrawdownCriterion.class),
    NET_LOSS(NetLossCriterion.class),
    NET_PROFIT(NetProfitCriterion.class),
    NUM_BREAK_EVEN_POS(NumberOfBreakEvenPositionsCriterion.class),
    NUM_CONSEC_POS(NumberOfConsecutivePositionsCriterion.class),
    NUM_LOSING_POS(NumberOfLosingPositionsCriterion.class),
    NUM_POS(NumberOfPositionsCriterion.class),
    PROFIT_LOSS(ProfitLossCriterion.class),
    PROFIT_LOSS_PERCENTAGE(ProfitLossPercentageCriterion.class),
    PROFIT_LOSS_RATIO(ProfitLossRatioCriterion.class),
    RETURN_OVER_MAX_DRAWDOWN(ReturnOverMaxDrawdownCriterion.class),
    WINNING_POS_RATIO(WinningPositionsRatioCriterion.class);

    private final Class<? extends AnalysisCriterion> criterionClass;

    Criterion(Class<? extends AnalysisCriterion> criterionClass) {
      this.criterionClass = criterionClass;
    }

    private AnalysisCriterion getAnalysisCriterion() {
      try {
        Constructor<?> ctor = criterionClass.getConstructor();
        return (AnalysisCriterion) ctor.newInstance(new Object[] {});
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    }

    private Optional<Num> calculate(BarSeries series, TradingRecord tradingRecord) {
      return Optional.of(getAnalysisCriterion().calculate(series, tradingRecord))
          .filter(num -> !num.isNaN() && !num.isZero());
    }

    public Optional<Double> doubleValue(BarSeries series, TradingRecord tradingRecord) {
      return calculate(series, tradingRecord).map(Num::doubleValue);
    }

    public Optional<Integer> intValue(BarSeries series, TradingRecord tradingRecord) {
      return calculate(series, tradingRecord).map(Num::intValue);
    }
  }
}
