plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.pasteleriamilsabores"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pasteleriamilsabores"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.7.5"
    }

    packaging.resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"

    // ✅ Forzar compatibilidad entre librerías (resuelve conflictos AAR)
    configurations.all {
        resolutionStrategy {
            force("androidx.core:core-ktx:1.13.1")
            force("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
            force("androidx.lifecycle:lifecycle-runtime-compose:2.8.6")
            force("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
            force("androidx.activity:activity-compose:1.9.3")
            force("androidx.navigation:navigation-compose:2.8.3")
            force("androidx.room:room-runtime:2.6.1")
            force("androidx.datastore:datastore-preferences:1.1.1")
            force("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
        }
    }
}

dependencies {
    // 📦 Core AndroidX
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.activity:activity-compose:1.9.3")

    // 🎨 Jetpack Compose (BOM maneja versiones internas)
    implementation(platform("androidx.compose:compose-bom:2024.10.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    // 🧭 Navegación Compose
    implementation("androidx.navigation:navigation-compose:2.8.3")

    // 🧠 ViewModel + Lifecycle Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.6")

    // 🧁 Room (Base de datos local)
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // ⚙️ DataStore (Preferencias)
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // 🔄 Corrutinas (para tareas asincrónicas)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // 🧪 Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // 🧰 Compose UI Tests y Herramientas
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.10.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
