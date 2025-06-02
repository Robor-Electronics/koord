import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("convention-base")
    id("convention.android-library")
    kotlin("multiplatform")
}

kotlin {
    // TODO: Remove 'js' target if the following issue is resolved https://github.com/Kotlin/dokka/issues/3122
    js {
        nodejs()
    }

    jvm()
    androidTarget()

    // Use a specific Java version to make it easier to work in different environments.
    jvmToolchain(21)

    explicitApi()

    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

tasks.withType<Test>().configureEach {
    // Configure all test Gradle tasks to use JUnitPlatform.
    useJUnitPlatform()

    // Allow multiple test tasks to run in parallel
    maxParallelForks = Runtime.getRuntime().availableProcessors()

    // Log information about all test results, not only the failed ones.
    testLogging {
        setExceptionFormat("full")
        events(
            TestLogEvent.FAILED,
            TestLogEvent.PASSED,
            TestLogEvent.SKIPPED,
            TestLogEvent.STANDARD_OUT,
            TestLogEvent.STANDARD_ERROR
        )
    }
}
