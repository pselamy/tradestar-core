package com.verlumen.tradestar.core.backtesting;

import com.verlumen.tradestar.protos.backtesting.BackTestRequest;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategyTestResult;

public interface BackTester {
    TradeStrategyTestResult test(TestParams request);
    
    interface TestParams {
        TradeStrategy tradeStrategy();
    }
}
