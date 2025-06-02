plugins {
    id("org.jetbrains.dokka")
}

dokka {
    dokkaSourceSets.configureEach {
        sourceLink {
            localDirectory = rootDir
//            remoteUrl("https://github.com/Robor-Electronics/Koord/blob/main")
        }
    }
}