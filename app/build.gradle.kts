import org.gradle.internal.classpath.Instrumented.systemProperty

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {

    namespace = "io.github.citate_of_day"
    compileSdk = 35


    defaultConfig {
        applicationId = "io.github.citate_of_day"
        minSdk = 26
        targetSdk = 35
        versionCode = 7
        versionName = "7"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {

        debug {
            isMinifyEnabled = false
            isShrinkResources = false
        }

        release {
            isMinifyEnabled = true

            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    testOptions {
        unitTests.all {
            it.jvmArgs("-Xmx2g")
            // Отключаем R8 для тестов
            it.ignoreFailures = false
            systemProperty("disableR8", "true")  // Важно!
            systemProperty("dexmaker.dexcache", layout.buildDirectory.dir("tmp").get().asFile.absolutePath)
            systemProperty("disableR8", "true")
            systemProperty("disableMinification", "true")
        }

        unitTests{
            isReturnDefaultValues = true
        }
    }
}



dependencies {
    implementation (libs.androidx.lifecycle.viewmodel.compose)
    implementation("androidx.compose.animation:animation:1.6.1")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation (libs.kotlinx.coroutines.core)
    implementation( libs.kotlinx.coroutines.android)

    implementation( libs.androidx.lifecycle.viewmodel.ktx)
    implementation( libs.androidx.lifecycle.runtime.ktx.v270)
    testImplementation("app.cash.turbine:turbine:1.0.0")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.7.0")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

