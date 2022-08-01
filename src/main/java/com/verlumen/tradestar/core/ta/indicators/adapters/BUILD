java_library(
    name = "adapters_module",
    srcs = ["AdaptersModule.java"],
    visibility = [
        "//src/main/java/com/verlumen/tradestar/core:__subpackages__",
        "//src/test/java/com/verlumen/tradestar/core:__subpackages__",
    ],
    deps = [
        ":adx_indicator_adapter",
        "//src/main/java/com/verlumen/tradestar/core/ta/indicators:indicator_factory",
        "@maven//:com_google_inject_guice",
        "@maven//:org_ta4j_ta4j_core",
    ],
)

java_library(
    name = "adx_indicator_adapter",
    srcs = ["AdxIndicatorAdapter.java"],
    deps = [
        "//src/main/java/com/verlumen/tradestar/core/ta/indicators:indicator_factory",
        "@maven//:com_google_guava_guava",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)
