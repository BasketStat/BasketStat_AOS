import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.daggerHilt)
    alias(libs.plugins.jetbrain.kotlin.serialization)
    alias(libs.plugins.googleKsp)
    alias(libs.plugins.google.service)
    id("kotlin-parcelize")
}

android {
    namespace = "com.dkproject.presentation"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        resValue("string","kakao_oauth_host",getApiKey("kakao_oauth_host"))
        buildConfigField("String","GOOGLE_CLIENT_ID",getApiKey("GOOGLE_CLIENT_ID"))
        buildConfigField("String","ALGOLIA_APPLICATION_ID",getApiKey("ALGOLIA_APPLICATION_ID"))
        buildConfigField("String","ALGOLIA_API_KEY",getApiKey("ALGOLIA_API_KEY"))
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
fun getApiKey(propertyKey:String):String{
    return gradleLocalProperties(rootDir,providers).getProperty(propertyKey)
}
dependencies {

    implementation(project(":Domain"))

    //icon
    implementation(libs.androidx.compose.icons)

    //hilt
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.compiler)
    ksp(libs.hilt.compiler)
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigtaion)

    //paging
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.runtime)

    //coil
    implementation(libs.coil)
    implementation(libs.coil.compose)

    //viewModel
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.viewmodel.ktx)

    //navigation
    implementation(libs.androidx.navigation.compose)

    //firebasebom
    implementation(platform(libs.firebase.bom))
    //firebaseauth
    implementation(libs.fireauth)
    //add the dependency for the Google Play services library and specify its version
    implementation(libs.gms.play.services.auth)

    //kakao-login
    implementation(libs.kakao.login)

    //algolia
    implementation(libs.algolia.android)
    implementation(libs.algolia.paging)
    implementation(libs.algolia.compose)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}