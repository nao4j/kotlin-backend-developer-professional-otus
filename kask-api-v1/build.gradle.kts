plugins {
    kotlin("multiplatform")
    id("org.openapi.generator")
    kotlin("plugin.serialization")
}

kotlin {
    jvm {}
    linuxX64 {}
    macosX64 {}

    sourceSets {
        val serializationVersion: String by project

        val commonMain by getting {
            kotlin.srcDirs("$buildDir/generate-resources/main/src/commonMain/kotlin")

            dependencies {
                implementation(kotlin("stdlib-common"))

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}

openApiGenerate {
    val openapiGroup = "${rootProject.group}.kask.api.v1"
    generatorName.set("kotlin")
    packageName.set(openapiGroup)
    apiPackage.set("$openapiGroup.kask.api")
    modelPackage.set("$openapiGroup.models")
    invokerPackage.set("$openapiGroup.invoker")
    inputSpec.set("$rootDir/specs/kask-v1.yaml")
    library.set("multiplatform")

    globalProperties.apply {
        put("models", "")
        put("modelDocs", "false")
    }

    configOptions.set(
        mapOf(
            "dateLibrary" to "string",
            "enumPropertyNaming" to "UPPERCASE",
            "collectionType" to "list",
        )
    )
}

afterEvaluate {
    val openApiGenerate = tasks.getByName("openApiGenerate")
    tasks.filter { it.name.startsWith("compile") }.forEach {
        it.dependsOn(openApiGenerate)
    }
}
