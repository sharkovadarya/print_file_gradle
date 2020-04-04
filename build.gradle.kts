plugins {
    kotlin("jvm") version ("1.2.30")
    id("java-gradle-plugin")
    id("maven-publish")
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    testImplementation("org.assertj:assertj-core:3.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.1.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.1.0")

}

(tasks.findByName("test") as Test).useJUnitPlatform()

group = "ru.hse.spb.sharkova.printfile"
version = "0.0.1"
gradlePlugin {
    plugins {
        create("printfile") {
            id = "ru.hse.spb.sharkova.printfile"
            implementationClass = "ru.hse.spb.sharkova.printfile.PrintFilePlugin"
        }
    }
}