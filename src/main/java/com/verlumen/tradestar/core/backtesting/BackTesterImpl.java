package com.verlumen.tradestar.backtesting;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.verlumen.tradestar.core.strategies.StrategyFactory;
import com.verlumen.tradestar.protos.backtesting.BackTestRequest;
import com.verlumen.tradestar.protos.backtesting.BackTestResult;
import com.verlumen.tradestar.protos.candles.Candle;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BarSeriesManager;
import org.ta4j.core.Strategy;
import org.ta4j.core.TradingRecord;

import static com.google.common.base.MoreObjects.firstNonNull;

class BackTesterImpl implements BackTester {
    private final BackTestResultFactory backTestResultFactory;
    private final BarSeriesManagerFactory barSeriesManagerFactory;
    private final StrategyFactory strategyFactory;

    @Inject
    BackTesterImpl(BackTestResultFactory backTestResultFactory,
                   BarSeriesManagerFactory barSeriesManagerFactory, StrategyFactory strategyFactory) {
        this.backTestResultFactory = backTestResultFactory;
        this.barSeriesManagerFactory = barSeriesManagerFactory;
        this.strategyFactory = strategyFactory;
    }

    @Override
    public BackTestResult run(BackTestRequest request) {
        return run(firstNonNull(request, BackTestRequest.getDefaultInstance()),
                ImmutableList.copyOf(request.getCandlesList()));
    }

    private BackTestResult run(BackTestRequest request, ImmutableList<Candle> candles) {
        BarSeriesManager seriesManager = createBarSeriesManager(candles);
        BarSeries series = seriesManager.getBarSeries();
        TradingRecord record = seriesManager.run(createStrategy(request, series));
        return backTestResultFactory.create(request, series, record);
    }

    private BarSeriesManager createBarSeriesManager(
            ImmutableList<Candle> candles) {
        return barSeriesManagerFactory.create(candles);
    }

    private Strategy createStrategy(BackTestRequest request, BarSeries series) {
        return strategyFactory.create(request.getTradeStrategy(), series);
    }
}
