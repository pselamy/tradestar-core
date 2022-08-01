java_library(
    name = "analysis_criteria",
    srcs = ["AnalysisCriteria.java"],
    deps = [
        "@maven//:com_google_inject_guice",
        "@maven//:org_ta4j_ta4j_core",
    ],
)

java_library(
    name = "back_tester",
    srcs = ["BackTester.java"],
    visibility = ["//visibility:public"],
    deps = [
        "//:autovalue",
        "@maven//:com_google_guava_guava",
        "@tradestar_protos//:candles_java_proto",
        "@tradestar_protos//:strategies_java_proto",
    ],
)

java_library(
    name = "back_tester_impl",
    srcs = ["BackTesterImpl.java"],
    visibility = ["//src/test/java:__subpackages__"],
    deps = [
        ":back_tester",
        ":bar_series_manager_factory",
        ":test_result_factory",
        "//src/main/java/com/verlumen/tradestar/core/ta/strategies:strategy_factory",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:candles_java_proto",
        "@tradestar_protos//:strategies_java_proto",
    ],
)

java_library(
    name = "back_testing_module",
    srcs = ["BackTestingModule.java"],
    visibility = ["//src/test/java:__subpackages__"],
    deps = [
        ":back_tester",
        ":back_tester_impl",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
    ],
)

java_library(
    name = "bar_series_manager_factory",
    srcs = ["BarSeriesManagerFactory.java"],
    visibility = ["//src/test/java:__subpackages__"],
    deps = [
        "//:autovalue",
        "@maven//:com_google_guava_guava",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:candles_java_proto",
    ],
)

java_library(
    name = "bar_series_manager_factory_impl",
    srcs = ["BarSeriesManagerFactoryImpl.java"],
    deps = [
        ":bar_series_manager_factory",
        "//src/main/java/com/verlumen/tradestar/core/tradehistory:bar_series_factory",
        "@maven//:org_ta4j_ta4j_core",
    ],
)

java_library(
    name = "test_result_factory",
    srcs = ["TestResultFactory.java"],
    deps = [
        ":analysis_criteria",
        "//:autovalue",
        "@maven//:com_google_inject_guice",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)
