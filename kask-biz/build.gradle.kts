plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {}
    linuxX64 {}
    macosX64 {}

    sourceSets {
        val coroutinesVersion: String by project

        all { languageSettings.optIn("kotlin.RequiresOptIn") }

        @Suppress("UNUSED_VARIABLE")
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))

                implementation(project(":kask-common"))
                implementation(project(":kask-stub"))
                implementation(project(":kask-lib-cor"))
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))

                api("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
            }
        }
    }
}
