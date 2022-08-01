java_library(
    name = "strategies_module",
    srcs = ["StrategiesModule.java"],
    visibility = ["//visibility:public"],
    deps = [
        ":strategy_factory",
        ":strategy_factory_impl",
        "//src/main/java/com/verlumen/tradestar/core/ta/indicators:indicators_module",
        "//src/main/java/com/verlumen/tradestar/core/ta/rules:rules_module",
        "//src/main/java/com/verlumen/tradestar/core/ta/signalstrength:signal_strength_module",
        "@maven//:com_google_inject_guice",
    ],
)

java_library(
    name = "strategy_factory",
    srcs = ["StrategyFactory.java"],
    visibility = ["//visibility:public"],
    deps = [
        "trade_strategy_id",
        "//src/main/java/com/verlumen/tradestar/core/ta/rules:rule_factory",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)

java_library(
    name = "strategy_factory_impl",
    srcs = ["StrategyFactoryImpl.java"],
    visibility = ["//src/test/java:__subpackages__"],
    deps = [
        "trade_strategy_id",
        ":strategy_factory",
        "//src/main/java/com/verlumen/tradestar/core/ta/rules:rule_factory",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)

java_library(
    name = "trade_strategy_id",
    srcs = ["TradeStrategyIdentifier.java"],
    visibility = ["//src/test/java:__subpackages__"],
    deps = [
        "@maven//:com_google_guava_guava",
        "@tradestar_protos//:strategies_java_proto",
    ],
)
