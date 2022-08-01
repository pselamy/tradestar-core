java_library(
    name = "back_tester",
    srcs = ["BackTester.java"],
    deps = [
        "@tradestar_protos//:strategies_java_proto",
    ],
)

java_library(
    name = "bar_series_manager_factory",
    srcs = ["BarSeriesManagerFactory.java"],
    visibility = ["//src/test/java:__subpackages__"],
    deps = [
        "@maven//:com_google_guava_guava",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:candles_java_proto",
        "//:autovalue",
    ],
)


java_library(
    name = "bar_series_manager_factory_impl",
    srcs = ["BarSeriesManagerFactoryImpl.java"],
    deps = [
        ":bar_series_manager_factory",
        "@maven//:com_google_guava_guava",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:candles_java_proto",
        "//src/main/java/com/verlumen/tradestar/core/tradehistory:bar_series_factory",
    ],
)
