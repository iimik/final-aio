plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "final-aio"

include("final-aio-core")
include("final-aio-tasks")
include("final-aio-spring")
include("final-aio-idea")

include("final-aio-mybatis")