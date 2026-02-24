import kotlin.text.set

plugins {
    id ("java")
    id ("com.gradleup.shadow") version "9.3.1"
}
repositories {
    mavenCentral()
}

dependencies {
    implementation("org.slf4j:slf4j-simple:2.0.13")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("com.mysql:mysql-connector-j:9.6.0")
    implementation("org.xerial:sqlite-jdbc:3.51.2.0")
}

tasks {
    jar {
        enabled = false
    }

    shadowJar {
        archiveClassifier.set("")
        mergeServiceFiles()
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        relocate("com.zaxxer.hikari", "com.nukku.libs.hikari")
        relocate("com.mysql", "com.nukku.libs.mysql")

        dependencies {
            exclude(dependency("com.hypixel.hytale:Server"))
        }
    }

    build {
        dependsOn(shadowJar)
    }
}