import org.gradle.api.JavaVersion

object Configs {
    const val COMPILE_SDK = 35
    const val TARGET_SDK = 35
    const val MIN_SDK = 31
    const val VERSION_CODE = 1
    const val VERSION_NAME = "1.0"
    const val APPLICATION_ID = "com.github.lucascalheiros.telegramfilterapp"
    const val JVM_TARGET = "17"
    val sourceCompatibility = JavaVersion.VERSION_17
    val targetCompatibility = JavaVersion.VERSION_17
}