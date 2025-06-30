plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("org.jetbrains.kotlin.plugin.parcelize") version "2.2.0" apply false
    alias(libs.plugins.kotlin.kapt)

}
