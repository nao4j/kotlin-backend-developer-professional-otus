plugins {
    java
    kotlin("jvm")
    id ("com.bmuschko.docker-java-application")
}

dependencies {
    val coroutinesVersion: String by project
    val logbackVersion: String by project
    val rabbitVersion: String by project
    val testContainersVersion: String by project

    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    implementation("com.rabbitmq:amqp-client:$rabbitVersion")

    implementation(project(":kask-common"))
    implementation(project(":kask-api-v1"))
    implementation(project(":kask-mapping-v1"))
    implementation(project(":kask-biz"))

    testImplementation(kotlin("test"))
    testImplementation("org.testcontainers:rabbitmq:$testContainersVersion")
    testImplementation(project(":kask-stub"))
}

docker {
    javaApplication {
        baseImage.set("openjdk:17")
        ports.set(listOf(8080, 5672))
        images.set(setOf("${project.name}:latest"))
        jvmArgs.set(listOf("-XX:+UseContainerSupport"))
    }
}
