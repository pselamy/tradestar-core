package com.verlumen.tradestar.core.tradehistory.indicators;

import static com.google.common.base.Functions.identity;
import static com.google.common.collect.ImmutableMap.toImmutableMap;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.verlumen.tradestar.protos.indicators.Indicator;
import java.util.Optional;
import java.util.Set;
import org.ta4j.core.BarSeries;

public class IndicatorAdapterRepositoryImpl implements IndicatorAdapterRepository {
  private final ImmutableMap<Indicator.Params.TypeCase, IndicatorAdapterFactory> adapterFactories;

  @Inject
  IndicatorAdapterRepositoryImpl(Set<IndicatorAdapterFactory> adapterFactories) {
    this.adapterFactories =
        adapterFactories.stream()
            .collect(toImmutableMap(IndicatorAdapterFactory::typeCase, identity()));
  }

  @Override
  public IndicatorAdapter get(Indicator.Params params, BarSeries barSeries) {
    return Optional.ofNullable(adapterFactories.get(params.getTypeCase()))
        .map(factory -> factory.create(barSeries, params))
        .orElseThrow(UnsupportedOperationException::new);
  }
}
