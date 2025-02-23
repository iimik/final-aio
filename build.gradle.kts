import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import java.util.stream.Collectors

plugins {
    id("java") // Java support
    alias(libs.plugins.kotlin) // Kotlin support
    alias(libs.plugins.intelliJPlatform) // IntelliJ Platform Gradle Plugin
    alias(libs.plugins.changelog) // Gradle Changelog Plugin
    alias(libs.plugins.qodana) // Gradle Qodana Plugin
    alias(libs.plugins.kover) // Gradle Kover Plugin
}

group = providers.gradleProperty("pluginGroup").get()
version = providers.gradleProperty("pluginVersion").get()

// Set the JVM language level used to build the project.
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
    implementation("org.eclipse.jgit:org.eclipse.jgit:7.1.0.202411261347-r")
    // spring boot
    implementation("org.springframework.boot:spring-boot-starter:${properties["spring.boot.version"]}")
    implementation("org.springframework.boot:spring-boot-starter-aop:${properties["spring.boot.version"]}")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.2.0")
    testImplementation("org.springframework.boot:spring-boot-starter-web:${properties["spring.boot.version"]}")
    testImplementation("org.springframework.boot:spring-boot-starter-test:${properties["spring.boot.version"]}")
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


// Configure IntelliJ Platform Gradle Plugin - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-extension.html
intellijPlatform {
    pluginConfiguration {
        version = providers.gradleProperty("pluginVersion")
        val pluginRepositoryUrl = providers.gradleProperty("pluginRepositoryUrl")

        // Extract the <!-- Plugin description --> section from DESCRIPTION.md and provide for the plugin's manifest
        description = providers.fileContents(layout.projectDirectory.file("DESCRIPTION.md")).asText.map {
            val regex = Regex("""(#\d+)""")
            var foundMatches = regex.findAll(it)
            var formatIssues: String = it
            foundMatches.forEach { result ->
                formatIssues = formatIssues.replace(
                    "(${result.value})",
                    "([${result.value}](${pluginRepositoryUrl}/issues/${result.value.trimStart('#')}))"
                )
            }

            val mdRegex = Regex("""((/\w+)+\.md)""")
            foundMatches = mdRegex.findAll(it)
            foundMatches.forEach { result ->
                val value = "(${result.value})"
                val newValue = "(${pluginRepositoryUrl}/blob/main${result.value})"
                formatIssues = formatIssues.replace(value, newValue)
            }

            formatIssues
        }.get().let(::markdownToHTML)

        val changelog = project.changelog // local variable for configuration cache compatibility
        // Get the latest available change notes from the changelog file
        changeNotes = providers.gradleProperty("pluginVersion").map { pluginVersion ->
            with(changelog) {
                val item = getOrNull(pluginVersion) ?: getUnreleased()
                val sections = item.sections.entries.stream()
                    .collect(Collectors.toMap({ it.key }, {
                        val values = it.value
                        val regex = Regex("""(#\d+)""")
                        values.map { value ->
                            val foundMatches = regex.findAll(value)
                            var formatIssues: String = value
                            foundMatches.forEach { result ->
                                formatIssues = formatIssues.replace(
                                    "(${result.value})",
                                    "([${result.value}](${pluginRepositoryUrl}/issues/${result.value.trimStart('#')}))"
                                )
                            }
                            formatIssues
                        }.toSet()
                    }))
                val newItem = Changelog.Item(item.version, item.header, item.summary, item.isUnreleased, sections)


                renderItem(
                    newItem
                        .withHeader(false)
                        .withEmptySections(false),
                    Changelog.OutputType.HTML,
                )
            }
        }

        ideaVersion {
            sinceBuild = providers.gradleProperty("pluginSinceBuild")
            untilBuild = providers.gradleProperty("pluginUntilBuild")
        }
    }

    signing {
        certificateChain = providers.environmentVariable("CERTIFICATE_CHAIN")
        privateKey = providers.environmentVariable("PRIVATE_KEY")
        password = providers.environmentVariable("PRIVATE_KEY_PASSWORD")
    }

    publishing {
        token = providers.environmentVariable("PUBLISH_TOKEN")
        // The pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
        // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
        // https://plugins.jetbrains.com/docs/intellij/deployment.html#specifying-a-release-channel
        channels =
            providers.gradleProperty("pluginVersion").map { listOf(it.substringAfter('-', "").substringBefore('.').ifEmpty { "default" }) }
    }

    pluginVerification {
        ides {
            recommended()
        }
    }
}

// Configure Gradle Changelog Plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    groups.empty()
    repositoryUrl = providers.gradleProperty("pluginRepositoryUrl")
}

// Configure Gradle Kover Plugin - read more: https://github.com/Kotlin/kotlinx-kover#configuration
kover {
    reports {
        total {
            xml {
                onCheck = true
            }
        }
    }
}



tasks {
    wrapper {
        gradleVersion = providers.gradleProperty("gradleVersion").get()
    }

    publishPlugin {
        dependsOn(patchChangelog)
    }

    test {
        jvmArgumentProviders += CommandLineArgumentProvider {
            listOf("-Didea.kotlin.plugin.use.k2=true")
        }
    }

}

intellijPlatformTesting {
    runIde {
        register("runIdeForUiTests") {
            task {
                jvmArgumentProviders += CommandLineArgumentProvider {
                    listOf(
                        "-Drobot-server.port=8082",
                        "-Dide.mac.message.dialogs.as.sheets=false",
                        "-Djb.privacy.policy.text=<!--999.999-->",
                        "-Djb.consents.confirmation.enabled=false",
                        "-Xmx4096m",
                        "-Didea.kotlin.plugin.use.k2=true"
                    )
                }
            }

            plugins {
                robotServerPlugin()
            }
        }
    }
}
