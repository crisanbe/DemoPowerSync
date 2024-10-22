plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.sqldelight)
    kotlin("plugin.serialization") version "1.5.31"  // Ajusta la versión a la que estás usando

}

android {
    namespace = "com.metro.demopowersyncmongo.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.metro.demopowersyncmongo.android"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "lib/arm64-v8a/libpowersync-sqlite.so"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        // Habilita el desugaring
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Añade la dependencia para desugaring
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")

    implementation("com.powersync:compose:1.0.0-BETA5")
    implementation("com.powersync:core:1.0.0-BETA5")
    implementation("co.powersync:powersync-sqlite-core:0.3.1")
    runtimeOnly("com.powersync:persistence:1.0.0-BETA5")

    // Dependencias de Ktor Client para HTTP
    implementation("io.ktor:ktor-client-core:2.3.12")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.12")
    implementation("io.ktor:ktor-client-json:2.1.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")
    implementation("io.ktor:ktor-client-okhttp:2.3.12")
    implementation(projects.shared)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    debugImplementation(libs.compose.ui.tooling)
    implementation("androidx.compose.material3:material3:1.3.0")
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.material.icons.extended)
}