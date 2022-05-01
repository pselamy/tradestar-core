java_library(
    name = "adx_indicator_adapter_factory",
    srcs = ["AdxIndicatorAdapterFactory.java"],
    deps = [
        ":indicator_adapter_repository",
        "@maven//:com_google_guava_guava",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:indicators_java_proto",
    ],
)

java_library(
    name = "indicator_adapter_repository",
    srcs = ["IndicatorAdapterRepository.java"],
    visibility = ["//visibility:public"],
    deps = [
        "@maven//:com_google_guava_guava",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:indicators_java_proto",
    ],
)

java_library(
    name = "indicator_adapter_repository_impl",
    srcs = ["IndicatorAdapterRepositoryImpl.java"],
    deps = [
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
        ":indicator_adapter_repository",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
    ],
)
