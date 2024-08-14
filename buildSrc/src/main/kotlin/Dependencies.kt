/**
 * Created by bedirhansaricayir on 13.08.2024
 */

object Kotlin {
    private val coreKtx by lazy { "androidx.core:core-ktx:${Versions.coreKtxVersion}" }
    private val platformKotlin by lazy { "org.jetbrains.kotlin:kotlin-bom:${Versions.platformKotlinBomVersion}" }
    private val coroutinesCore by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesCoreVersion}" }
    private val coroutinesAndroid by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesAndroidVersion}" }
    private val lifecycleRuntimeKtx by lazy { "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleRuntimeKtxVersion}" }
    val ktxViewModel by lazy { "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleViewmodelKtxVersion}" }
    val kotlinSerializationJson by lazy { "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serializationVersion}" }

    val list =
        listOf(coreKtx, platformKotlin, coroutinesCore, coroutinesCore, lifecycleRuntimeKtx, coroutinesAndroid, ktxViewModel)
}

object Compose {
    private val compose by lazy { "androidx.activity:activity-compose:${Versions.activityComposeVersion}" }
    private val platformCompose by lazy { "androidx.compose:compose-bom:${Versions.platformComposeBomVersion}" }
    private val composeUi by lazy { "androidx.compose.ui:ui" }
    private val composeUiGraphics by lazy { "androidx.compose.ui:ui-graphics" }
    private val composeUiPreview by lazy { "androidx.compose.ui:ui-tooling-preview" }
    private val material3 by lazy { "androidx.compose.material3:material3:${Versions.material3Version}" }
    private val foundation by lazy { "androidx.compose.foundation:foundation:${Versions.foundationVersion}" }
    private val navigation by lazy { "androidx.navigation:navigation-compose:${Versions.composeNavigationVersion}" }
    val composeViewModel by lazy { "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.lifecycleViewmodelComposeVersion}" }
    val list = listOf(
        compose, platformCompose, composeUi, composeUiGraphics, composeUiPreview,
        material3, foundation, navigation, composeViewModel
    )
}

object Di {
    val hiltAndroid by lazy { "com.google.dagger:hilt-android:${Versions.hiltAndroidVersion}" }
    val hiltAndroidCompiler by lazy { "com.google.dagger:hilt-android-compiler:${Versions.hiltAndroidCompilerVersion}" }
    val hiltNavigationCompose by lazy { "androidx.hilt:hilt-navigation-compose:${Versions.hiltNavigationComposeVersion}" }
    val hiltCompiler by lazy { "androidx.hilt:hilt-compiler:${Versions.hiltCompilerVersion}" }
    val list = listOf(hiltAndroid, hiltAndroidCompiler, hiltNavigationCompose, hiltCompiler)
}

object Room {
    val room by lazy { "androidx.room:room-ktx:${Versions.roomVersion}" }
    val roomRuntime by lazy { "androidx.room:room-runtime:${Versions.roomVersion}" }
    val roomCompiler by lazy { "androidx.room:room-compiler:${Versions.roomVersion}" }
}

object Network {
    val retrofit by lazy { "com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}" }
    val gsonConvertor by lazy { "com.squareup.retrofit2:converter-gson:${Versions.converterGsonVersion}" }
    val okHttp by lazy { "com.squareup.okhttp3:okhttp:${Versions.okhttpVersion}" }
    val okHttpInterceptor by lazy { "com.squareup.okhttp3:logging-interceptor:${Versions.okhttpInterceptorVersion}" }
    val list = listOf(retrofit, gsonConvertor, okHttp, okHttpInterceptor)
}

object ThirdParty {
    val coil by lazy { "io.coil-kt:coil-compose:${Versions.coilVersion}" }
}