java_library(
    name = "bar_factory",
    srcs = ["BarFactory.java"],
    visibility = ["//src/test/java:__subpackages__"],
    deps = [
        ":granularity_spec",
        "@maven//:com_google_guava_guava",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:candles_java_proto",
    ],
)

java_library(
    name = "bar_series_factory",
    srcs = ["BarSeriesFactory.java"],
    visibility = ["//visibility:public"],
    deps = [
        ":bar_factory",
        ":granularity_spec",
        "@maven//:com_google_guava_guava",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:candles_java_proto",
    ],
)

java_library(
    name = "granularity_spec",
    srcs = ["GranularitySpec.java"],
    visibility = ["//visibility:public"],
    deps = [
        "@tradestar_protos//:candles_java_proto",
    ],
)
