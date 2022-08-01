java_library(
    name = "adx_signal_strength_spec",
    srcs = ["AdxSignalStrengthSpec.java"],
    visibility = [
        "//src/main/java/com/verlumen/tradestar/core:__subpackages__",
        "//src/test/java:__subpackages__",
    ],
    deps = [
        ":signal_strength_spec",
        "@maven//:com_google_guava_guava",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)

java_library(
    name = "signal_strength_spec",
    srcs = ["SignalStrengthSpec.java"],
    visibility = ["//visibility:public"],
    deps = [
        "@maven//:com_google_guava_guava",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)

java_library(
    name = "signal_strength_module",
    srcs = ["SignalStrengthModule.java"],
    visibility = ["//visibility:public"],
    deps = [
        ":adx_signal_strength_spec",
        ":signal_strength_spec",
        "@maven//:com_google_inject_guice",
    ],
)
