java_library(
    name = "candle_comparators",
    srcs = [
        "CandleComparators.java",
    ],
    visibility = ["//visibility:public"],
    deps = [
        "@tradestar_protos//:candles_java_proto",
    ],
)

java_library(
    name = "candle_sorter",
    srcs = ["CandleSorter.java"],
    visibility = ["//src/main/java/com/verlumen/tradestar/core:__subpackages__"],
    deps = [
        "//src/main/java/com/verlumen/tradestar/core/candles:granularity_spec",
        "@maven//:com_google_guava_guava",
        "@tradestar_protos//:candles_java_proto",
    ],
)

java_library(
    name = "constants",
    srcs = ["Constants.java"],
    visibility = ["//src/main/java/com/verlumen/tradestar/core:__subpackages__"],
    deps = [
        "@maven//:com_google_guava_guava",
        "@tradestar_protos//:candles_java_proto",
        "@tradestar_protos//:strategies_java_proto",
    ],
)
