java_library(
    name = "adx_indicator_adapter_factory",
    srcs = ["AdxIndicatorAdapterFactory.java"],
    deps = [
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:indicators_java_proto",
    ],
)

java_library(
    name = "indicator_factory",
    srcs = ["IndicatorFactory.java"],
    visibility = ["//visibility:public"],
    deps = [
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:indicators_java_proto",
    ],
)
