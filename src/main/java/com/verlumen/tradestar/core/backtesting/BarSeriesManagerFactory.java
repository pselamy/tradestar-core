package com.verlumen.tradestar.core.backtesting;

import com.google.common.collect.ImmutableSet;
import com.verlumen.tradestar.protos.candles.Candle;
import com.verlumen.tradestar.protos.candles.Granularity;
import org.ta4j.core.BarSeriesManager;

interface BarSeriesManagerFactory {
    BarSeriesManager create(CreateParams params);
    
    @AutoValue
    abstract static class CreateParams {
        static Builder builder() {
            return new AutoValue_BarSeriesManager_CreateParams.Builder();   
        }
        
        ImmutableSet<Candle> candles();
        Granularity granularity();

        @AutoValue.Builder
        abstract static class Builder() {
            abstract Builder setCandles(ImmutableSet<Candle> candles);
            abstract Builder setGranularity(Granularity granularity);
            abstract CreateParams build();
        }
    }
}
