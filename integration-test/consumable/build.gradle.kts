plugins {
    java
}

repositories {
    mavenCentral()
}

val aggregate by configurations.creating {
    extendsFrom(configurations.implementation.get())
}

dependencies {
    implementation("org.slf4j:slf4j-api:2.0.17")
}
