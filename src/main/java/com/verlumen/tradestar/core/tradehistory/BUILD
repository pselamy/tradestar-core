java_library(
    name = "bar_factory",
    srcs = ["BarFactory.java"],
    visibility = ["//src/test/java:__subpackages__"],
    deps = [
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
        "//src/main/java/com/verlumen/tradestar/core/candles:granularity_spec",
        "@maven//:com_google_guava_guava",
        "@maven//:org_apache_commons_commons_lang3",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:candles_java_proto",
    ],
)
