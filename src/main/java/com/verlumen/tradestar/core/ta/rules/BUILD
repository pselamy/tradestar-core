java_library(
    name = "adx_rule_factory",
    srcs = ["AdxRuleFactory.java"],
    visibility = ["//src/test/java:__subpackages__"],
    deps = [
        ":rule_factory",
        "//src/main/java/com/verlumen/tradestar/core/ta/indicators:indicator_factory",
        "//src/main/java/com/verlumen/tradestar/core/ta/signalstrength:signal_strength_spec",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
        "@maven//:com_google_mug_mug",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)

java_library(
    name = "composite_rule_factory",
    srcs = ["CompositeRuleFactory.java"],
    deps = [
        ":rule_factory",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)

java_library(
    name = "rule_factory",
    srcs = ["RuleFactory.java"],
    visibility = [
        "//src/main/java/com/verlumen/tradestar/core:__subpackages__",
        "//src/test/java:__subpackages__",
    ],
    deps = [
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)

java_library(
    name = "rules_module",
    srcs = ["RulesModule.java"],
    visibility = [
        "//src/main/java/com/verlumen/tradestar/core:__subpackages__",
        "//src/test/java:__subpackages__",
    ],
    deps = [
        ":adx_rule_factory",
        ":composite_rule_factory",
        ":rule_factory",
        "@maven//:com_google_inject_guice",
    ],
)
