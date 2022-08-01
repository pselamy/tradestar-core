package com.verlumen.tradestar.core.backtesting;

import com.verlumen.tradestar.protos.backtesting.BackTestRequest;
import com.verlumen.tradestar.protos.backtesting.BackTestResult;

public interface BackTester {
    BackTestResult run(BackTestRequest request);
}
