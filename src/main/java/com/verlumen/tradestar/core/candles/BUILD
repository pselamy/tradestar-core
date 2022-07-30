java_library(
    name = "candle_factory",
    srcs = ["CandleFactory.java"],
    visibility = ["//visibility:public"],
    deps = [
        ":constants",
        ":granularity_spec",
        "//:autovalue",
        "@maven//:com_google_guava_guava",
        "@tradestar_protos//:candles_java_proto",
        "@tradestar_protos//:instruments_java_proto",
        "@tradestar_protos//:trading_java_proto",
    ],
)

java_library(
    name = "constants",
    srcs = ["Constants.java"],
    visibility = ["//visibility:public"],
    deps = [
        "@maven//:com_google_guava_guava",
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
