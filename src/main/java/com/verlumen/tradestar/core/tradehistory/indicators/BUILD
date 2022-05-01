java_library(
    name = "adx_indicator_adapter_factory",
    srcs = ["AdxIndicatorAdapterFactory.java"],
    deps = [
        ":indicator_adapter",
        ":indicator_adapter_factory",
        ":indicator_adapter_impl",
        ":indicator_adapter_repository",
        "@maven//:com_google_guava_guava",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:indicators_java_proto",
    ],
)

java_library(
    name = "indicator_adapter",
    srcs = ["IndicatorAdapter.java"],
    visibility = ["//visibility:public"],
    deps = [
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:indicators_java_proto",
    ],
)

java_library(
    name = "indicator_adapter_factory",
    srcs = ["IndicatorAdapterFactory.java"],
    deps = [
        ":indicator_adapter",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:indicators_java_proto",
    ],
)

java_library(
    name = "indicator_adapter_impl",
    srcs = ["IndicatorAdapterImpl.java"],
    deps = [
        ":indicator_adapter",
        ":indicator_adapter_repository",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:indicators_java_proto",
    ],
)

java_library(
    name = "indicator_adapter_repository",
    srcs = ["IndicatorAdapterRepository.java"],
    visibility = ["//visibility:public"],
    deps = [
        ":indicator_adapter",
        "//src/main/java/com/verlumen/tradestar/core/tradehistory:bar_series_factory",
        "@maven//:com_google_guava_guava",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:candles_java_proto",
        "@tradestar_protos//:indicators_java_proto",
    ],
)

java_library(
    name = "indicator_adapter_repository_impl",
    srcs = ["IndicatorAdapterRepositoryImpl.java"],
    deps = [
        ":indicator_adapter",
        ":indicator_adapter_factory",
        ":indicator_adapter_repository",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:indicators_java_proto",
    ],
)

java_library(
    name = "indicators_module",
    srcs = ["IndicatorsModule.java"],
    visibility = ["//visibility:public"],
    deps = [
        ":adx_indicator_adapter_factory",
        ":indicator_adapter_factory",
        ":indicator_adapter_repository",
        ":indicator_adapter_repository_impl",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
    ],
)
