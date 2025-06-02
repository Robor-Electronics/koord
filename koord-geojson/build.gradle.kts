import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    id("convention-library")
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinPluginSerialization)
}

android {
    namespace = "nl.robor.koord.geojson"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(libs.kotlinxSerialization)
                api(libs.akkurate.arrow)
                api(libs.akkurate.core)
                api(libs.bundles.arrow)
            }

            // Adds the generated ksp sources
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
        }

        commonTest {
            dependencies {
                implementation(libs.bundles.testing)
                implementation(libs.akkurate.test)
            }
        }

        jvmTest {
            dependencies {
                implementation(libs.kotest.runner.junit5)
            }
        }
    }
}


val kspOutput = layout.buildDirectory.dir("generated/ksp/metadata/commonMain/kotlin")

dependencies {
    add("kspCommonMainMetadata", libs.akkurate.ksp.plugin)
}

tasks.withType<KotlinCompilationTask<*>>().configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

// Ensure generated ksp output is include in the final JS Jar
tasks
    .withType<org.gradle.jvm.tasks.Jar>()
    .matching { it.name == "sourcesJar" || it.name.endsWith("SourcesJar") }
    .configureEach {
        dependsOn("kspCommonMainKotlinMetadata")
        inputs.dir(kspOutput)
        from(kspOutput)
    }
