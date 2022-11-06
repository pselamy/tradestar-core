java_test(
    name = "BacktesterTest",
    srcs = ["BacktesterTest.java"],
    deps = [
        "//:autovalue",
        "//src/main/java/com/verlumen/tradestar/core/backtesting:backtester",
        "//src/main/java/com/verlumen/tradestar/core/backtesting:test_result_factory",
        "//src/main/java/com/verlumen/tradestar/core/strategies/adapters:trade_strategy_adapter",
        "@maven//:com_google_inject_extensions_guice_testlib",
        "@maven//:com_google_inject_guice",
        "@maven//:com_google_testparameterinjector_test_parameter_injector",
        "@maven//:com_google_truth_truth",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:candles_java_proto",
        "@tradestar_protos//:strategies_java_proto",
    ],
)

java_test(
    name = "BacktestingModuleTest",
    srcs = ["BacktestingModuleTest.java"],
    deps = [
        "//src/main/java/com/verlumen/tradestar/core/backtesting:backtester",
        "//src/main/java/com/verlumen/tradestar/core/backtesting:backtesting_module",
        "@maven//:com_google_inject_guice",
    ],
)
