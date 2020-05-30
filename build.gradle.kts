

plugins {
    //java
    kotlin("multiplatform") version "1.3.70"
    id("org.jetbrains.kotlin.plugin.serialization") version Config.Versions.kotlin
    id ("com.github.hierynomus.license") version "0.15.0"
    `maven-publish`
    maven
    id ("com.jfrog.bintray") version "1.8.0"
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
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
    
            // evoleq
            implementation( Config.Dependencies.evoleqCore )
            implementation( Config.Dependencies.configurations )
            
            implementation("org.evoleq:mathcat-result-jvm:1.0.0")
            implementation("org.evoleq:mathcat-core-jvm:1.0.0")
            implementation("org.evoleq:mathcat-structure-jvm:1.0.0")
            implementation("org.evoleq:mathcat-structure-jvm:1.0.0")
            implementation("org.evoleq:mathcat-morphism-jvm:1.0.0")
            implementation("org.evoleq:mathcat-state-jvm:1.0.0")
            implementation("org.evoleq:ktorx-jvm:1.0.0")
            
            implementation(Config.Dependencies.kotlinSerializationRuntime)
            // kotlin serialization
            //implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime${Config.Versions.kotlinSerialization}")
    
    
            implementation("io.ktor:ktor-client-core:${Config.Versions.ktor}")
            implementation("io.ktor:ktor-client-cio:${Config.Versions.ktor}")
            //implementation("io.ktor:ktor-client-serialization${Config.Versions.ktor}")
            implementation("io.ktor:ktor-client-json:${Config.Versions.ktor}")
            
        }
    }
    // JVM-specific tests and their dependencies:
    jvm().compilations["test"].defaultSourceSet {
        dependencies {
            implementation(Config.Dependencies.kotlinSerializationRuntime)
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
            //implementation(kotlin("js"))
            implementation(kotlin("reflect"))
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.3.5")
    
            // evoleq
            implementation( Config.Dependencies.evoleqCoreJs )
            implementation( Config.Dependencies.configurationsJs )
            implementation("org.evoleq:mathcat-result-js:1.0.0")
            implementation("org.evoleq:mathcat-core-js:1.0.0")
            implementation("org.evoleq:mathcat-structure-js:1.0.0")
            implementation("org.evoleq:mathcat-structure-js:1.0.0")
            implementation("org.evoleq:mathcat-morphism-js:1.0.0")
            implementation("org.evoleq:mathcat-state-js:1.0.0")
            implementation("org.evoleq:ktorx-js:1.0.0")
            
            // kotlin serialization
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:${Config.Versions.kotlinSerialization}")
    
            // ktor
            
            implementation("io.ktor:ktor-client-core:${Config.Versions.ktor}")
            implementation("io.ktor:ktor-client-js:${Config.Versions.ktor}")
            
            
            implementation("io.ktor:ktor-client-serialization-js:${Config.Versions.ktor}")
            implementation("io.ktor:ktor-client-json:${Config.Versions.ktor}")
    
            implementation("io.ktor:ktor-websockets:${Config.Versions.ktor}")
        }
        
        /* ... */
    }
    js().compilations["test"].defaultSourceSet {/* ... */ }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(kotlin("reflect"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.5")
    
                implementation( Config.Dependencies.configurations )
                
                implementation( Config.Dependencies.kotlinSerializationRuntimeCommon )
                
                //implementation( project( ":ktorx" ))
    
                implementation("io.ktor:ktor-client-core:${Config.Versions.ktor}")
               // implementation("io.ktor:ktor-client-cio:${Config.Versions.ktor}")
               // implementation("io.ktor:ktor-client-serialization${Config.Versions.ktor}")
               // implementation("io.ktor:ktor-client-json:${Config.Versions.ktor}")
                
     
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation( Config.Dependencies.kotlinSerializationRuntimeCommon )
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