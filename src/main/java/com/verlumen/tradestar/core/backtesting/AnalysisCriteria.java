package com.verlumen.tradestar.core.backtesting;

import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.BarSeries;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.analysis.criteria.*;
import org.ta4j.core.analysis.criteria.pnl.*;
import org.ta4j.core.num.Num;

import java.io.Serializable;
import java.lang.reflect.Constructor;

class AnalysisCriteria implements Serializable {
  public enum Criterion {
    AVG_LOSS(AverageLossCriterion.class),
    AVG_PROFIT(AverageProfitCriterion.class),
    AVG_RETURN_PER_BAR(AverageReturnPerBarCriterion.class),
    NUM_BREAK_EVEN_POS(NumberOfBreakEvenPositionsCriterion.class),
    BUY_AND_HOLD_RETURN(BuyAndHoldReturnCriterion.class),
    NUM_CONSEC_WIN_POS(NumberOfConsecutiveWinningPositionsCriterion.class),
    EXPECTANCY(ExpectancyCriterion.class),
    GROSS_LOSS(GrossLossCriterion.class),
    GROSS_PROFIT(GrossProfitCriterion.class),
    GROSS_RETURN(GrossReturnCriterion.class),
    LOSING_POS_RATIO(LosingPositionsRatioCriterion.class),
    MAX_DRAWDOWN(MaximumDrawdownCriterion.class),
    NET_LOSS(NetLossCriterion.class),
    NET_PROFIT(NetProfitCriterion.class),
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

    private Num calculate(BarSeries series, TradingRecord tradingRecord) {
      return getAnalysisCriterion().calculate(series, tradingRecord);
    }

    public double doubleValue(BarSeries series, TradingRecord tradingRecord) {
      return calculate(series, tradingRecord).doubleValue();
    }

    public int intValue(BarSeries series, TradingRecord tradingRecord) {
      return calculate(series, tradingRecord).intValue();
    }
  }
}
