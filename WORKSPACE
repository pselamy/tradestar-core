workspace(name = "tradestar_core")

load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")

git_repository(
    name = "rules_proto",
    commit = "3212323502e21b819ac4fbdd455cb227ad0f6394",
    remote = "https://github.com/bazelbuild/rules_proto",
    shallow_since = "1649153521 +0200",
)

load("@rules_proto//proto:repositories.bzl", "rules_proto_dependencies", "rules_proto_toolchains")

rules_proto_dependencies()

rules_proto_toolchains()

git_repository(
    name = "tradestar_protos",
    commit = "8072191038bb4ae760a1642f362a34f8083478f8",
    remote = "https://github.com/pselamy/tradestar-protos",
    shallow_since = "1657475286 -0400",
)

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
        "com.google.inject:guice:5.1.0",
        "com.google.guava:guava:31.1-jre",
        "org.apache.commons:commons-lang3:3.12.0",
        "org.ta4j:ta4j-core:0.14",
    ],
    repositories = [
        "https://repo1.maven.org/maven2",
    ],
)
