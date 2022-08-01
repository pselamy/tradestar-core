package com.verlumen.tradestar.core.backtesting;

import com.google.inject.Inject;
import org.ta4j.core.analysis.criteria.*;
import org.ta4j.core.analysis.criteria.pnl.*;

class AnalysisCriteria {
    private final AverageLossCriterion averageLoss;
    private final AverageProfitCriterion averageProfit;
    private final AverageReturnPerBarCriterion averageReturnPerBar;
    private final NumberOfBreakEvenPositionsCriterion numberOfBreakEvenPositions;
    private final BuyAndHoldReturnCriterion buyAndHoldReturn;
    private final NumberOfConsecutiveWinningPositionsCriterion numberOfConsecutiveWinningPositions;
    private final ExpectancyCriterion expectancy;
    private final GrossLossCriterion grossLoss;
    private final GrossProfitCriterion grossProfit;
    private final GrossReturnCriterion grossReturn;
    private final NumberOfLosingPositionsCriterion numberOfLosingPositions;
    private final LosingPositionsRatioCriterion losingPositionsRatio;
    private final MaximumDrawdownCriterion maximumDrawdown;
    private final NetLossCriterion netLoss;
    private final NetProfitCriterion netProfit;
    private final NumberOfPositionsCriterion numberOfPositions;
    private final ProfitLossCriterion profitLoss;
    private final ProfitLossPercentageCriterion profitLossPercentage;
    private final ProfitLossRatioCriterion profitLossRatio;
    private final ReturnOverMaxDrawdownCriterion returnOverMaxDrawdown;
    private final WinningPositionsRatioCriterion winningPositionsRatio;

    @Inject
    AnalysisCriteria(AverageLossCriterion averageLoss,
                     AverageProfitCriterion averageProfit,
                     AverageReturnPerBarCriterion averageReturnPerBar,
                     NumberOfBreakEvenPositionsCriterion numberOfBreakEvenPositions,
                     BuyAndHoldReturnCriterion buyAndHoldReturn,
                     NumberOfConsecutiveWinningPositionsCriterion numberOfConsecutiveWinningPositions,
                     ExpectancyCriterion expectancy,
                     GrossLossCriterion grossLoss,
                     GrossProfitCriterion grossProfit,
                     GrossReturnCriterion grossReturn,
                     LosingPositionsRatioCriterion losingPositionsRatio,
                     MaximumDrawdownCriterion maximumDrawdown,
                     NetLossCriterion netLoss,
                     NetProfitCriterion netProfit,
                     NumberOfLosingPositionsCriterion numberOfLosingPositions,
                     NumberOfPositionsCriterion numberOfPositions,
                     ProfitLossCriterion profitLoss,
                     ProfitLossPercentageCriterion profitLossPercentage,
                     ProfitLossRatioCriterion profitLossRatio,
                     ReturnOverMaxDrawdownCriterion returnOverMaxDrawdown,
                     WinningPositionsRatioCriterion winningPositionsRatio) {
        this.averageLoss = averageLoss;
        this.averageProfit = averageProfit;
        this.averageReturnPerBar = averageReturnPerBar;
        this.numberOfBreakEvenPositions = numberOfBreakEvenPositions;
        this.buyAndHoldReturn = buyAndHoldReturn;
        this.numberOfConsecutiveWinningPositions = numberOfConsecutiveWinningPositions;
        this.expectancy = expectancy;

        this.grossLoss = grossLoss;
        this.grossProfit = grossProfit;
        this.grossReturn = grossReturn;
        this.numberOfLosingPositions = numberOfLosingPositions;
        this.losingPositionsRatio = losingPositionsRatio;
        this.maximumDrawdown = maximumDrawdown;
        this.netLoss = netLoss;
        this.netProfit = netProfit;
        this.numberOfPositions = numberOfPositions;
        this.profitLoss = profitLoss;
        this.profitLossPercentage = profitLossPercentage;
        this.profitLossRatio = profitLossRatio;
        this.returnOverMaxDrawdown = returnOverMaxDrawdown;
        this.winningPositionsRatio = winningPositionsRatio;
    }

    AverageLossCriterion averageLoss() {
        return averageLoss;
    }

    AverageProfitCriterion averageProfit() {
        return averageProfit;
    }

    AverageReturnPerBarCriterion averageReturnPerBar() {
        return averageReturnPerBar;
    }

    NumberOfBreakEvenPositionsCriterion numberOfBreakEvenPositions() {
        return numberOfBreakEvenPositions;
    }

    BuyAndHoldReturnCriterion buyAndHoldReturn() {
        return buyAndHoldReturn;
    }

    NumberOfConsecutiveWinningPositionsCriterion numberOfConsecutiveWinningPositions() {
        return numberOfConsecutiveWinningPositions;
    }

    ExpectancyCriterion expectancy() {
        return expectancy;
    }

    GrossLossCriterion grossLoss() {
        return grossLoss;
    }

    GrossProfitCriterion grossProfit() {
        return grossProfit;
    }

    GrossReturnCriterion grossReturn() {
        return grossReturn;
    }

    NumberOfLosingPositionsCriterion numberOfLosingPositions() {
        return numberOfLosingPositions;
    }

    LosingPositionsRatioCriterion losingPositionsRatio() {
        return losingPositionsRatio;
    }

    MaximumDrawdownCriterion maximumDrawdown() {
        return maximumDrawdown;
    }

    NetLossCriterion netLoss() {
        return netLoss;
    }

    NetProfitCriterion netProfit() {
        return netProfit;
    }

    NumberOfPositionsCriterion numberOfPositions() {
        return numberOfPositions;
    }

    ProfitLossCriterion profitLoss() {
        return profitLoss;
    }

    ProfitLossPercentageCriterion profitLossPercentage() {
        return profitLossPercentage;
    }

    ProfitLossRatioCriterion profitLossRatio() {
        return profitLossRatio;
    }

    ReturnOverMaxDrawdownCriterion returnOverMaxDrawdown() {
        return returnOverMaxDrawdown;
    }


    WinningPositionsRatioCriterion winningPositionsRatio() {
        return winningPositionsRatio;
    }
}