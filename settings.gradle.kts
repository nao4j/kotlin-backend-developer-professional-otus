rootProject.name = "kotlin-backend-developer-professional-otus"

pluginManagement {
    val kotlinVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion apply false
    }
}

include("m1l1-hello")
