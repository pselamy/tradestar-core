java_test(
    name = "BackTesterImplTest",
    srcs = ["BackTesterImplTest.java"],
    deps = [
        "//:autovalue",
        "//src/main/java/com/verlumen/tradestar/core/backtesting:back_tester",
        "//src/main/java/com/verlumen/tradestar/core/backtesting:back_tester_impl",
        "//src/main/java/com/verlumen/tradestar/core/backtesting:bar_series_manager_factory",
        "//src/main/java/com/verlumen/tradestar/core/strategies/adapters:trade_strategy_adapter",
        "//src/main/java/com/verlumen/tradestar/core/ta/strategies:strategy_factory",
        "@maven//:com_google_inject_extensions_guice_testlib",
        "@maven//:com_google_inject_guice",
        "@maven//:com_google_truth_truth",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:candles_java_proto",
        "@tradestar_protos//:strategies_java_proto",
    ],
)
