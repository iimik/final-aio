plugins {
    alias(libs.plugins.kotlin) // Kotlin support
    alias(libs.plugins.intelliJPlatform) apply false // IntelliJ Platform Gradle Plugin
    alias(libs.plugins.changelog) apply false // Gradle Changelog Plugin
    alias(libs.plugins.qodana) apply false // Gradle Qodana Plugin
    alias(libs.plugins.kover) apply false // Gradle Kover Plugin
}

allprojects {
    group = providers.gradleProperty("pluginGroup").get()
    version = providers.gradleProperty("pluginVersion").get()

    // Configure project's dependencies
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    kotlin {
        jvmToolchain(21)
    }


}
tasks {
    wrapper {
        gradleVersion = providers.gradleProperty("gradleVersion").get()
    }

    test {
        jvmArgumentProviders += CommandLineArgumentProvider {
            listOf("-Didea.kotlin.plugin.use.k2=true")
        }
    }

}
