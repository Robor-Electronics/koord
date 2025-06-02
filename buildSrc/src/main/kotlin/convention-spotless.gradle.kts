plugins {
    id("com.diffplug.spotless")
}

spotless {
    kotlin {
        target("src/*/kotlin/**/*.kt", "src/*/java/**/*.kt")
        ktlint()
    }
}