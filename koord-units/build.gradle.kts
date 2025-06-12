plugins {
    id("convention-library")
}

android {
    namespace = "nl.robor.koord.units"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {

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