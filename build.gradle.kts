

plugins {
    //java
    kotlin("multiplatform") version Config.Versions.kotlinMppPlugin
    id("org.jetbrains.kotlin.plugin.serialization") version Config.Versions.kotlinSerializationPlugin
    id ("com.github.hierynomus.license") version "0.15.0"
    `maven-publish`
    maven
    //id ("com.jfrog.bintray") version "1.8.0"
    id("org.jetbrains.dokka") version "0.9.17"
}

group = Config.Projects.KtorxResponse.group
version = Config.Projects.KtorxResponse.version//+"-SNAPSHOT"

buildscript{
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
        maven ( "https://dl.bintray.com/kotlin/ktor" )
        maven  ("https://dl.bintray.com/kotlin/kotlinx")
        maven { url = uri ("https://plugins.gradle.org/m2/") }
    }
    
    dependencies{
        classpath( Config.Dependencies.kotlinGradlePlugin )
        //classpath( Config.Dependencies.shadow )
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    maven ( "https://dl.bintray.com/kotlin/ktor" )
    maven  ("https://dl.bintray.com/kotlin/kotlinx")
    maven { url = uri ("https://plugins.gradle.org/m2/") }
}

kotlin {
    /* Targets configuration omitted.
    *  To find out how to configure the targets, please follow the link:
    *  https://kotlinlang.org/docs/reference/building-mpp-with-gradle.html#setting-up-targets */
    jvm().compilations["main"].defaultSourceSet {
        dependencies {
            implementation(kotlin("stdlib-jdk8"))
            implementation(kotlin("reflect"))
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Config.Versions.coroutines}")

            // evoleq/configurations
            implementation( Config.Dependencies.configurations )

            // evoleq/mathcat
            implementation("org.evoleq:mathcat-result-jvm:${Config.Versions.mathcat}")
            implementation("org.evoleq:mathcat-core-jvm:${Config.Versions.mathcat}")
            implementation("org.evoleq:mathcat-structure-jvm:${Config.Versions.mathcat}")
            implementation("org.evoleq:mathcat-structure-jvm:${Config.Versions.mathcat}")
            implementation("org.evoleq:mathcat-morphism-jvm:${Config.Versions.mathcat}")
            implementation("org.evoleq:mathcat-state-jvm:${Config.Versions.mathcat}")

            // evoleq/ktorx
            implementation("org.evoleq:ktorx-jvm:${Config.Projects.Ktorx.version}")

            // kotlin serialization
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Config.Versions.kotlinSerialization}")
    
            // ktor
            implementation("io.ktor:ktor-client-core:${Config.Versions.ktor}")
            implementation("io.ktor:ktor-client-cio:${Config.Versions.ktor}")
            implementation("io.ktor:ktor-client-serialization-jvm:${Config.Versions.ktor}")
            implementation("io.ktor:ktor-client-json:${Config.Versions.ktor}")
            
        }
    }
    // JVM-specific tests and their dependencies:
    jvm().compilations["test"].defaultSourceSet {
        dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Config.Versions.kotlinSerialization}")
            implementation(kotlin("test-junit"))
        }
    }
    /*
    js(){
        browser {
            dceTask {
                keep("ktor-ktor-io.\$\$importsForInline\$\$.ktor-ktor-io.io.ktor.utils.io")
            }
        }
        
    }
    
     */
    js().compilations["main"].defaultSourceSet  {
        dependencies {
            implementation(kotlin("reflect"))
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:${Config.Versions.coroutines}")

            // evoleq/configurations
            implementation( Config.Dependencies.configurationsJs )

            // evoleq/mathcat
            implementation("org.evoleq:mathcat-result-js:${Config.Versions.mathcat}")
            implementation("org.evoleq:mathcat-core-js:${Config.Versions.mathcat}")
            implementation("org.evoleq:mathcat-structure-js:${Config.Versions.mathcat}")
            implementation("org.evoleq:mathcat-structure-js:${Config.Versions.mathcat}")
            implementation("org.evoleq:mathcat-morphism-js:${Config.Versions.mathcat}")
            implementation("org.evoleq:mathcat-state-js:${Config.Versions.mathcat}")

            // evoleq/ktorx
            implementation("org.evoleq:ktorx-js:${Config.Projects.Ktorx.version}")

            // ktor
            implementation("io.ktor:ktor-client-core:${Config.Versions.ktor}")
            implementation("io.ktor:ktor-client-js:${Config.Versions.ktor}")
            implementation("io.ktor:ktor-client-serialization-js:${Config.Versions.ktor}")
            implementation("io.ktor:ktor-client-json:${Config.Versions.ktor}")
            implementation("io.ktor:ktor-client-websockets-js:${Config.Versions.ktor}")
        }
    }
    js().compilations["test"].defaultSourceSet {/* ... */ }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(kotlin("reflect"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Config.Versions.coroutines}")

                //implementation( Config.Dependencies.configurations )

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Config.Versions.kotlinSerialization}")

                implementation("io.ktor:ktor-client-core:${Config.Versions.ktor}")
    
                // evoleq/mathcat
                implementation("org.evoleq:mathcat-result:${Config.Versions.mathcat}")
                implementation("org.evoleq:mathcat-core:${Config.Versions.mathcat}")
                implementation("org.evoleq:mathcat-structure:${Config.Versions.mathcat}")
                implementation("org.evoleq:mathcat-structure:${Config.Versions.mathcat}")
                implementation("org.evoleq:mathcat-morphism:${Config.Versions.mathcat}")
                implementation("org.evoleq:mathcat-state:${Config.Versions.mathcat}")
    
                // evoleq/ktorx
                implementation("org.evoleq:ktorx:${Config.Projects.Ktorx.version}")
    
                implementation("org.evoleq:configurations:${Config.Versions.configurations}")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Config.Versions.kotlinSerialization}")
            }
        }
    }
}

tasks{
    val licenseFormatJsMain by creating(com.hierynomus.gradle.license.tasks.LicenseFormat::class) {
        source = fileTree("$projectDir/src/jsMain/kotlin") {
        }
        group = "license"
    }
    val licenseFormatJvmMain by creating(com.hierynomus.gradle.license.tasks.LicenseFormat::class) {
        source = fileTree("$projectDir/src/jvmMain/kotlin") {
        }
        group = "license"
    }
    val licenseFormatCommonMain by creating(com.hierynomus.gradle.license.tasks.LicenseFormat::class) {
        source = fileTree("$projectDir/src/commonMain/kotlin") {
        }
        group = "license"
    }
    licenseFormat {
        finalizedBy(licenseFormatJsMain, licenseFormatCommonMain, licenseFormatJvmMain)
    }
}

apply(from = "../publish.mpp.gradle.kts")