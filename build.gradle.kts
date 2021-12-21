import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.compose") version "1.0.1-rc2"
    id ("com.github.johnrengelman.shadow") version "7.1.1"
}

group = "net.demilich"
version = "1.0.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "net.demilich.tqmutator.MainKt"
    }
}

compose.desktop {
    application {
        mainClass = "net.demilich.tqmutator.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Exe, TargetFormat.Deb)
            packageName = "titanquest_mutator"
            description = "Titan Quest AE savegame editor"
            packageVersion = "$version"
        }
    }
}