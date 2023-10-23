plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {}
    linuxX64 {}
    macosX64 {}

    sourceSets {
        val datetimeVersion: String by project

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))

                api(project(":kask-lib-logging-common"))

                api("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
            }
        }
    }
}
