java_library(
    name = "adapters_module",
    srcs = ["AdaptersModule.java"],
    visibility = ["//src/main/java/com/verlumen/tradestar/core/ta/rules:__subpackages__"],
    deps = [
        ":adx_rule_adapter",
        ":composite_rule_adapter",
        "//src/main/java/com/verlumen/tradestar/core/ta/rules:rule_factory",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
    ],
)

java_library(
    name = "adx_rule_adapter",
    srcs = ["AdxRuleRuleAdapter.java"],
    visibility = ["//src/test/java:__subpackages__"],
    deps = [
        "//src/main/java/com/verlumen/tradestar/core/ta/indicators:indicator_factory",
        "//src/main/java/com/verlumen/tradestar/core/ta/rules:rule_factory",
        "//src/main/java/com/verlumen/tradestar/core/ta/signalstrength:signal_strength_spec",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
        "@maven//:com_google_mug_mug",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)

java_library(
    name = "composite_rule_adapter",
    srcs = ["CompositeRuleAdapter.java"],
    visibility = ["//src/test/java:__subpackages__"],
    deps = [
        "//src/main/java/com/verlumen/tradestar/core/ta/rules:rule_factory",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)
