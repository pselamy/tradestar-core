java_library(
    name = "adapters_module",
    srcs = ["AdaptersModule.java"],
    visibility = [
        "//visibility:public",
    ],
    deps = [
        ":adx_trade_strategy_adapter",
        ":composite_trade_strategy_adapter",
        ":trade_strategy_adapter",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
    ],
)

java_library(
    name = "adx_trade_strategy_adapter",
    srcs = ["AdxTradeStrategyAdapter.java"],
    visibility = [
        "//src/main/java/com/verlumen/tradestar/core/strategies/generators:__subpackages__",
        "//src/test/java/com/verlumen/tradestar/core/strategies/adapters:__subpackages__",
    ],
    deps = [
        ":fibonacci",
        ":strategy_negation_handler",
        ":trade_strategy_adapter",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)

java_library(
    name = "composite_trade_strategy_adapter",
    srcs = ["CompositeTradeStrategyAdapter.java"],
    deps = [
        ":strategy_negation_handler",
        ":trade_strategy_adapter",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)

java_library(
    name = "fibonacci",
    srcs = [
        "Fibonacci.java",
    ],
    deps = [
        "@maven//:com_google_guava_guava",
    ],
)

java_library(
    name = "strategy_negation_handler",
    srcs = [
        "StrategyNegationHandler.java",
    ],
    deps = [
        "@maven//:com_google_guava_guava",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)

java_library(
    name = "trade_strategy_adapter",
    srcs = ["TradeStrategyAdapter.java"],
    visibility = [
        "//visibility:public",
    ],
    deps = [
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)
