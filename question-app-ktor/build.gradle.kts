import io.ktor.plugin.features.JreVersion.JRE_17
import org.gradle.api.file.DuplicatesStrategy.EXCLUDE
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.util.suffixIfNot

val ktorVersion: String by project
val serializationVersion: String by project
val logbackVersion: String by project

fun ktor(module: String, prefix: String = "server-", version: String? = this@Build_gradle.ktorVersion): Any =
    "io.ktor:ktor-${prefix.suffixIfNot("-")}$module:$version"

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("application")
    id("io.ktor.plugin")
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

ktor {
    docker {
        localImageName.set(rootProject.name + "-ktor")
        imageTag.set(rootProject.version.toString())
        jreVersion.set(JRE_17)
    }
}

jib {
    container.mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    jvm {
        withJava()
    }

    targets.withType<KotlinNativeTarget> {
        binaries {
            executable {
                entryPoint = "com.nao4j.otus.kask.web.main"
            }
        }
    }

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":kask-common"))
                implementation(project(":kask-api-v1"))
                implementation(project(":kask-mapping-v1"))
                implementation(project(":kask-biz"))
                implementation(project(":kask-repo-stub"))
                implementation(project(":kask-repo-in-memory"))
                implementation(project(":kask-repo-postgres"))

                implementation(kotlin("stdlib-jdk8"))
                implementation("ch.qos.logback:logback-classic:$logbackVersion")

                implementation(ktor("core"))
                implementation(ktor("netty"))
                implementation(ktor("jackson", "serialization"))
                implementation(ktor("kotlinx-json", "serialization"))
                implementation(ktor("auto-head-response"))
                implementation(ktor("config-yaml"))
                implementation(ktor("content-negotiation"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(ktor("test-host"))
                implementation(ktor("content-negotiation", prefix = "client-"))
            }
        }
    }
}

tasks.withType<Copy> {
    duplicatesStrategy = EXCLUDE
}
