workspace(name = "tradestar_core")

load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")

############################
##### PROTOBUF SUPPORT #####
############################
git_repository(
    name = "rules_proto",
    remote = "https://github.com/bazelbuild/rules_proto",
    tag = "4.0.0-3.20.0",
)

load("@rules_proto//proto:repositories.bzl", "rules_proto_dependencies", "rules_proto_toolchains")

rules_proto_dependencies()

rules_proto_toolchains()

git_repository(
    name = "com_github_grpc_grpc",
    remote = "https://github.com/grpc/grpc",
    tag = "v1.50.1",
)

load("@com_github_grpc_grpc//bazel:grpc_deps.bzl", "grpc_deps")

grpc_deps()

######################
#### JAVA SUPPORT ####
######################
git_repository(
    name = "contrib_rules_jvm",
    commit = "f7c08ec6d73ef691b03f843e0c2c3dbe766df584",
    remote = "https://github.com/bazel-contrib/rules_jvm",
    shallow_since = "1642674503 +0000",
)

load("@contrib_rules_jvm//:repositories.bzl", "contrib_rules_jvm_deps")

contrib_rules_jvm_deps()

load("@contrib_rules_jvm//:setup.bzl", "contrib_rules_jvm_setup")

contrib_rules_jvm_setup()

git_repository(
    name = "rules_jvm_external",
    remote = "https://github.com/bazelbuild/rules_jvm_external",
    tag = "4.2",
)

load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    artifacts = [
        "com.google.auto.value:auto-value:1.9",
        "com.google.auto.value:auto-value-annotations:1.9",
        "com.google.guava:guava:31.1-jre",
        "com.google.inject:guice:5.1.0",
        "com.google.mug:mug:6.3",
        "org.apache.commons:commons-lang3:3.12.0",
        "org.ta4j:ta4j-core:0.15",
        # Unit Test Dependencies
        "com.google.inject.extensions:guice-testlib:5.1.0",
        "com.google.testparameterinjector:test-parameter-injector:1.8",
        "com.google.truth:truth:1.1.3",
        "com.google.truth.extensions:truth-java8-extension:1.1.3",
    ],
    repositories = [
        "https://repo1.maven.org/maven2",
    ],
)

##########################
##### CORE LIBRARIES #####
##########################
git_repository(
    name = "tradestar_protos",
    commit = "feffc465e9032207590cf54b412bdf3c433c3346",
    remote = "https://github.com/pselamy/tradestar-protos",
    shallow_since = "1670041544 -0500",
)
