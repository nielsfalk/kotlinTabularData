import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.6.21"
  application
}

group = "de.nielsfalk"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.21")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.3")
  implementation("org.junit.jupiter:junit-jupiter:5.8.2")
  testImplementation(kotlin("test"))
  testImplementation("io.strikt:strikt-core:0.34.1")
}

tasks.test {
  useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "1.8"
}

application {
  mainClass.set("MainKt")
}
