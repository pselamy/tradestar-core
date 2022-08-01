java_library(
    name = "candle_factory",
    srcs = ["CandleFactory.java"],
    visibility = ["//visibility:public"],
    deps = [
        ":granularity_spec",
        "//:autovalue",
        "//src/main/java/com/verlumen/tradestar/core/shared:candle_sorter",
        "@maven//:com_google_guava_guava",
        "@tradestar_protos//:candles_java_proto",
        "@tradestar_protos//:instruments_java_proto",
        "@tradestar_protos//:trading_java_proto",
    ],
)

java_library(
    name = "granularity_spec",
    srcs = ["GranularitySpec.java"],
    visibility = ["//visibility:public"],
    deps = [
        "@maven//:com_google_guava_guava",
        "@tradestar_protos//:candles_java_proto",
    ],
)
