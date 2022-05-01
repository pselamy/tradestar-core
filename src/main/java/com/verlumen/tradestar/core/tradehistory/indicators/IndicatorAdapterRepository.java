package com.verlumen.tradestar.core.tradehistory.indicators;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.verlumen.tradestar.core.tradehistory.BarSeriesFactory;
import com.verlumen.tradestar.protos.candles.Candle;
import com.verlumen.tradestar.protos.candles.Granularity;
import com.verlumen.tradestar.protos.indicators.Indicator.Params;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;

public interface IndicatorAdapterRepository {
    default IndicatorAdapter get(Params params,
                                 ImmutableCollection<Candle> candles,
                                 Granularity granularity) {
        ImmutableList<Candle> candleList = candles.asList();
        BarSeries series = BarSeriesFactory.create(granularity, candleList);
        return get(params, series);
    }

    IndicatorAdapter get(Params params, BarSeries barSeries);

    interface IndicatorIdSupplier extends Supplier<ImmutableList<Integer>> {
    }

    interface Ta4jIndicatorSupplier extends Supplier<Indicator<Num>> {
    }
}
