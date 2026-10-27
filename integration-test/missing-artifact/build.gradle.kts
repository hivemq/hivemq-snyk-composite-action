plugins {
    java
}

repositories {
    maven {
        url = uri("repository")
    }
}

dependencies {
    implementation("com.example:pom-only:1.0")
}
