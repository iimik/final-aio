import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.intellij.platform.gradle.extensions.intellijPlatform

plugins {
    alias(libs.plugins.kotlin) // Kotlin support
    alias(libs.plugins.intelliJPlatform) // IntelliJ Platform Gradle Plugin
    alias(libs.plugins.changelog) // Gradle Changelog Plugin
    alias(libs.plugins.qodana) // Gradle Qodana Plugin
    alias(libs.plugins.kover) // Gradle Kover Plugin
}

kotlin {
    jvmToolchain(21)
}

// Configure project's dependencies
repositories {
    mavenCentral()

    // IntelliJ Platform Gradle Plugin Repositories Extension - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-repositories-extension.html
    intellijPlatform {
        defaultRepositories()
    }
}


// Dependencies are managed with Gradle version catalog - read more: https://docs.gradle.org/current/userguide/platforms.html#sub:version-catalog
dependencies {
    testImplementation(libs.junit)
    // https://mvnrepository.com/artifact/org.eclipse.jgit/org.eclipse.jgit
//    implementation("org.eclipse.jgit:org.eclipse.jgit:7.2.0.202503040940-r")
    // spring boot
    implementation("org.springframework.boot:spring-boot-starter:${properties["spring.boot.version"]}")
    implementation("org.springframework.boot:spring-boot-starter-aop:${properties["spring.boot.version"]}")
    implementation("com.squareup.retrofit2:retrofit:${properties["retrofit.version"]}")
    implementation("com.squareup.retrofit2:converter-jackson:${properties["retrofit.version"]}")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.module/jackson-module-kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.19.0")
    // https://mvnrepository.com/artifact/org.apache.velocity/velocity-engine-core
//    implementation("org.apache.velocity:velocity-engine-core:2.4.1")
    // https://mvnrepository.com/artifact/org.apache.velocity.tools/velocity-tools-generic
//    implementation("org.apache.velocity.tools:velocity-tools-generic:3.1")
    testImplementation("org.springframework.boot:spring-boot-starter-web:${properties["spring.boot.version"]}")
    testImplementation("org.springframework.boot:spring-boot-starter-test:${properties["spring.boot.version"]}")
    testImplementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.2.0")
    // https://mvnrepository.com/artifact/org.mybatis/mybatis
    testImplementation("org.mybatis:mybatis:3.5.19")


    // IntelliJ Platform Gradle Plugin Dependencies Extension - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-dependencies-extension.html
    intellijPlatform {
        create(providers.gradleProperty("platformType"), providers.gradleProperty("platformVersion"))
        // Plugin Dependencies. Uses `platformBundledPlugins` property from the gradle.properties file for bundled IntelliJ Platform plugins.
        bundledPlugins(providers.gradleProperty("platformBundledPlugins").map { it.split(',') })

        // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file for plugin from JetBrains Marketplace.
        plugins(providers.gradleProperty("platformPlugins").map { it.split(',') })

        instrumentationTools()
        pluginVerifier()
        zipSigner()
        testFramework(TestFrameworkType.Platform)
    }
}


