plugins {
    id(BuildPlugins.ANDROID_LIBRARY_PLUGIN)
    id(BuildPlugins.KOTLIN_ANDROID_PLUGIN)
    id(BuildPlugins.DAGGER_HILT)
    id(BuildPlugins.KOTLIN_KAPT)
}

android {
    namespace = "com.eterationcase.app.feature.cart"
    compileSdk = AppConfig.COMPILE_SDK

    defaultConfig {
        minSdk = AppConfig.MIN_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}

dependencies {

    api(project(":core:base"))
    api(project(":core:domain"))
    api(project(":core:navigation"))


    Kotlin.list.forEach(::api)
    Compose.list.forEach(::api)

    with(Di) {
        implementation(hiltAndroid)
        implementation(hiltNavigationCompose)
        kapt(hiltCompiler)
        kapt(hiltAndroidCompiler)
    }
}