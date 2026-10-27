plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    api("org.slf4j:slf4j-api:${property("slf4jVersion")}")
}
