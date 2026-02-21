rootProject.name = "PrivateChest"

plugins {
    id("dev.scaffoldit") version "0.2.14"
}

hytale {
    usePatchline("release")
    useVersion("latest")

    repositories {}

    dependencies {}

    manifest {
        Name = "PrivateChest"
        Version = "1.2.3"
        Main = "com.nukku.PrivateChest"
        ServerVersion = "2026.02.19-1a311a592"
        IncludesAssetPack = false
    }


}

