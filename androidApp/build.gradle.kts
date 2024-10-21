plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.sqldelight)
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
    //implementation("org.mongodb:mongodb-driver-kotlin-coroutine:5.2.0")
    //implementation("org.mongodb:mongodb-driver-sync:4.10.1")  // Usa una versión adecuada para MongoDB


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