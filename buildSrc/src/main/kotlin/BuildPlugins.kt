/**
 * Created by bedirhansaricayir on 13.08.2024
 */
object BuildPlugins {
    val androidGradlePlugin by lazy { "com.android.tools.build:gradle:${Versions.gradleVersion}" }
    val kotlinGradlePlugin by lazy { "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinGradlePluginVersion}" }
    val hiltPlugin by lazy { "com.google.dagger:hilt-android-gradle-plugin:${Versions.hiltAndroidPluginVersion}" }

    const val ANDROID_APPLICATION_PLUGIN = "com.android.application"
    const val KOTLIN_ANDROID_PLUGIN = "kotlin-android"
    const val KSP_PLUGIN = "com.google.devtools.ksp"
    const val DAGGER_HILT = "com.google.dagger.hilt.android"
    const val KOTLIN_KAPT = "kotlin-kapt"
    const val ANDROID_LIBRARY_PLUGIN = "com.android.library"
}