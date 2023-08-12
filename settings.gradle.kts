rootProject.name = "kotlin-backend-developer-professional-otus"

pluginManagement {
    val kotlinVersion: String by settings
    val openapiVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false

        id("org.openapi.generator") version openapiVersion apply false
    }
}

include("m1l1-hello")
include("kask-common")
include("kask-api-v1")
include("kask-mapping-v1")
