java_library(
    name = "indicator_factory",
    srcs = ["IndicatorFactory.java"],
    visibility = [
        "//src/main/java/com/verlumen/tradestar/core:__subpackages__",
        "//src/test/java:__subpackages__",
    ],
    deps = [
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)

java_library(
    name = "indicator_factory_impl",
    srcs = ["IndicatorFactoryImpl.java"],
    visibility = [
        "//src/test/java/com/verlumen/tradestar/core/indicators:__subpackages__",
    ],
    deps = [
        ":indicator_factory",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
        "@maven//:com_google_mug_mug",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)

java_library(
    name = "indicators_module",
    srcs = ["IndicatorsModule.java"],
    visibility = [
        "//src/main/java/com/verlumen/tradestar/core:__subpackages__",
        "//src/test/java:__subpackages__",
    ],
    deps = [
        ":indicator_factory",
        ":indicator_factory_impl",
        "//src/main/java/com/verlumen/tradestar/core/ta/indicators/adapters:adapters_module",
        "@maven//:com_google_inject_guice",
    ],
)
