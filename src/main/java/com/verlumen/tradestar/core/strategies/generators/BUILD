java_library(
    name = "adapter_based_trade_strategy_generator",
    srcs = ["AdapterBasedTradeStrategyGenerator.java"],
    visibility = ["//:__subpackages__"],
    deps = [
        ":trade_strategy_generator",
        "//src/main/java/com/verlumen/tradestar/core/strategies/adapters:trade_strategy_adapter",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)

java_library(
    name = "generators_module",
    srcs = ["GeneratorsModule.java"],
    visibility = ["//visibility:public"],
    deps = [
        ":adapter_based_trade_strategy_generator",
        ":trade_strategy_generator",
        "//src/main/java/com/verlumen/tradestar/core/strategies/adapters:adapters_module",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
        "@tradestar_protos//:strategies_java_proto",
    ],
)

java_library(
    name = "trade_strategy_generator",
    srcs = ["TradeStrategyGenerator.java"],
    visibility = ["//visibility:public"],
    deps = [
        "//src/main/java/com/verlumen/tradestar/core/shared:constants",
        "@maven//:com_google_guava_guava",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)
