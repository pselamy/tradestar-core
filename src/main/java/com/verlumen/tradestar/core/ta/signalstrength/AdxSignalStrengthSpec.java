package com.verlumen.tradestar.core.ta.signalstrength;

import com.google.common.collect.Range;
import com.verlumen.tradestar.protos.strategies.SignalStrength;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;

class AdxSignalStrengthSpec implements SignalStrengthSpec {
    @Override
    public Range<Num> range(SignalStrength signalStrength) {
        return Spec.get(signalStrength).range;
    }

    @Override
    public TradeStrategy.StrategyOneOfCase supportedCase() {
        return TradeStrategy.StrategyOneOfCase.ADX;
    }

    @SuppressWarnings("unused")
    private enum Spec {
        WEAK(0, 25),
        STRONG(25, 50),
        VERY_STRONG(50, 75),
        EXTREMELY_STRONG(75, 100);

        private final Range<Num> range;

        Spec(int lower, int upper) {
            this.range = Range.closedOpen(decimalNum(lower), decimalNum(upper));
        }

        private static Spec get(SignalStrength signalStrength) {
            return Spec.valueOf(signalStrength.name());
        }

        private static Num decimalNum(Number number) {
            return DecimalNum.valueOf(number);
        }
    }
}
