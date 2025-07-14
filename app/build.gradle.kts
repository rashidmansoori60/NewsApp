plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.kotlin.kapt)  // Use kapt as alias also
}

kapt {
    javacOptions {
        option("--add-exports", "jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED")
    }
}

android {
    namespace = "com.example.kotlinapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.kotlinapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.swiperefreshlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Hilt
    implementation("com.google.dagger:hilt-android:2.56.2")
    kapt("com.google.dagger:hilt-compiler:2.56.2")

    // CoordinatorLayout
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.3.0")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-livedata:2.9.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.9.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.1")

    // Room
    implementation("androidx.room:room-runtime:2.7.2")
    kapt("androidx.room:room-compiler:2.7.2")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    // Shimmer
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.9.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.9.1")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")

    // Retrofit & OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // ViewPager2
    implementation("androidx.viewpager2:viewpager2:1.1.0")
}
