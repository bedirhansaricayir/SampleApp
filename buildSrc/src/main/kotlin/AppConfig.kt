/**
 * Created by bedirhansaricayir on 13.08.2024
 */

object AppConfig {
    private const val versionMajor = 1
    private const val versionMinor = 0
    private const val versionPatch = 0

    const val versionCode = versionMajor * 10000 + versionMinor * 100 + versionPatch
    const val versionName = "$versionMajor.$versionMinor.$versionPatch"


    const val COMPILE_SDK = 34
    const val MIN_SDK = 24
    const val TARGET_SDK = 34
    const val APPLICATION_ID = "com.eterationcase.app"
    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
}