object Version {

    const val MAJOR: Int = 0
    const val MINOR: Int = 1
    const val PATCH: Int = 0

    /// Version name without suffix
    val versionName: String
        get() = "$MAJOR.$MINOR.$PATCH"

    /// Version code, used for versioning Android apps
    val versionCode: Int
        get() = MAJOR * 10000 + MINOR * 100 + PATCH

    /// Build number, only available in CI builds
    val buildNumber: Int?
        get() = System.getenv("CI_RUN_NUMBER")?.toIntOrNull()

    /// Represents the type of the build, usually provided through CI
    val buildType: String
        get() = System.getenv("CI_RELEASE_TYPE") ?: "local"

    val versionNameSuffix: String?
        get() {
            // If the build type is 'stable', we don't need to add a suffix
            if (buildType == "stable") return null

            // The run number is only available when the app is built through CI
            val runNumber = buildNumber?.toString() ?: ""

            // example: "beta.1", "snapshot.5", etc.
            return buildType + if (runNumber.isNotEmpty()) ".$runNumber" else ""
        }

    // The full version name is the version name with the version name suffix
    // example: "v0.0.1-beta.1", "v0.0.1-snapshot.5", etc.
    val fullVersionName: String
        get() = versionName + (versionNameSuffix?.let { "-$it" } ?: "")
}