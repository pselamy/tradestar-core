java_library(
    name = "analysis_criteria",
    srcs = ["AnalysisCriteria.java"],
    deps = [
        "@maven//:com_google_inject_guice",
        "@maven//:org_ta4j_ta4j_core",
    ],
)

java_library(
    name = "backtester",
    srcs = ["Backtester.java"],
    visibility = ["//visibility:public"],
    deps = [
        ":test_result_factory",
        "//:autovalue",
        "//src/main/java/com/verlumen/tradestar/core/shared:candle_comparators",
        "//src/main/java/com/verlumen/tradestar/core/shared:constants",
        "//src/main/java/com/verlumen/tradestar/core/strategies/adapters:trade_strategy_adapter",
        "//src/main/java/com/verlumen/tradestar/core/tradehistory:bar_series_factory",
        "@maven//:com_google_guava_guava",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:candles_java_proto",
        "@tradestar_protos//:strategies_java_proto",
    ],
)

java_library(
    name = "backtesting_module",
    srcs = ["BacktestingModule.java"],
    visibility = ["//visibility:public"],
    deps = [
        ":backtester",
        ":test_result_factory",
        "//src/main/java/com/verlumen/tradestar/core/strategies/adapters:adapters_module",
        "//src/main/java/com/verlumen/tradestar/core/strategies/adapters:trade_strategy_adapter",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
    ],
)

java_library(
    name = "test_result_factory",
    srcs = ["TestResultFactory.java"],
    deps = [
        ":analysis_criteria",
        "//:autovalue",
        "@maven//:com_google_inject_guice",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)
