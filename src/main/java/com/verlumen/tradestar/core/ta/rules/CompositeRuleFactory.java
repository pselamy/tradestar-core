package com.verlumen.tradestar.core.ta.rules;

import com.google.common.collect.ImmutableMap;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategy.StrategyOneOfCase;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Rule;

import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;

import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static com.verlumen.tradestar.protos.strategies.TradeStrategy.Composite.Joiner;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;

class CompositeRuleFactory implements RuleFactory {
    private static final ImmutableMap<Joiner, BinaryOperator<Rule>> REDUCERS =
            ImmutableMap.<Joiner, BinaryOperator<Rule>>builder()
                    .put(Joiner.AND, Rule::and)
                    .put(Joiner.OR, Rule::or)
                    .put(Joiner.XOR, Rule::xor)
                    .build();
    private final ImmutableMap<StrategyOneOfCase, RuleFactory> ruleFactories;

    CompositeRuleFactory(Set<RuleFactory> ruleFactories) {
        this.ruleFactories = ruleFactories.stream()
                .collect(toImmutableMap(RuleFactory::supportedCase, identity()));
    }

    private static BinaryOperator<Rule> reducer(TradeStrategy params) {
        return ofNullable(REDUCERS.get(params.getComposite().getJoiner()))
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public Rule buyRule(TradeStrategy params, BarSeries barSeries) {
        return params.getComposite().getStrategiesList().stream()
                .map(strategy -> ruleFactory(strategy.getStrategyOneOfCase())
                        .buyRule(strategy, barSeries))
                .reduce(reducer(params))
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public Rule sellRule(TradeStrategy params, BarSeries barSeries) {
        return params.getComposite().getStrategiesList().stream()
                .map(strategy -> ruleFactory(strategy.getStrategyOneOfCase())
                        .sellRule(strategy, barSeries))
                .reduce(reducer(params))
                .orElseThrow(IllegalArgumentException::new);    }

    @Override
    public StrategyOneOfCase supportedCase() {
        return StrategyOneOfCase.COMPOSITE;
    }

    private RuleFactory ruleFactory(StrategyOneOfCase strategyOneOfCase) {
        return Optional.ofNullable(ruleFactories.get(strategyOneOfCase))
                .orElseThrow(IllegalArgumentException::new);
    }
}
