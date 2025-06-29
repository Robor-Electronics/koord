plugins {
    id("convention-library")
}

android {
    namespace = "nl.robor.koord.turf"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.koordGeojson)
                api(projects.koordUnits)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.bundles.testing)
            }
        }

        jvmTest {
            dependencies {
                implementation(libs.kotest.runner.junit5)
            }
        }
    }
}