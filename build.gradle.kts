plugins {
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.allopen") version "1.4.10"
    id("com.github.node-gradle.node") version "3.0.0-rc1"
    id("io.quarkus")
}

repositories {
    mavenLocal()
    mavenCentral()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-kotlin")
    implementation(group = "org.jetbrains.kotlin", name="kotlin-stdlib-jdk8", version = KotlinVersion.CURRENT.toString())
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-resteasy")
    implementation("io.quarkus:quarkus-resteasy-jsonb")
    testImplementation("io.quarkus:quarkus-junit5")
}

group = "org.acme"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

quarkus {
    setOutputDirectory("$projectDir/build/classes/kotlin/main")
}

tasks.withType<io.quarkus.gradle.tasks.QuarkusDev> {
    setSourceDir("$projectDir/src/main/kotlin")
}

allOpen {
    annotation("javax.ws.rs.Path")
    annotation("javax.enterprise.context.ApplicationScoped")
    annotation("io.quarkus.test.junit.QuarkusTest")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
    kotlinOptions.javaParameters = true
}

val jsDir = file("${project.projectDir}/src/main/js")

node {
    nodeProjectDir.set(jsDir)
}

tasks.register<com.github.gradle.node.npm.task.NpmTask>("npmBuild") {
    dependsOn("npmInstall")

    args.set(listOf("run-script", "build"))
    // copy to resources
    doLast {
        val targetDir = file("${project.projectDir}/src/main/resources/META-INF/resources")
        mkdir(targetDir)
        sync {
           into(targetDir)
            from(file("$jsDir/public"))
        }
    }
}

tasks {
    sourceSets.main {
       processResources {
           dependsOn("npmBuild")
       }
    }
}