version = Version.fullVersionName

tasks.register("version") {
    doLast {
        println(Version.fullVersionName)
    }
}
