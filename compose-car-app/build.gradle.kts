plugins {
  alias(libs.plugins.androidLibrary)
  alias(libs.plugins.jetbrainsKotlinAndroid)
  alias(libs.plugins.kotlinParcelize)
  alias(libs.plugins.ktfmt)
  alias(libs.plugins.compose.compiler)
}

android {
  namespace = "com.maplibre.compose.car"
  compileSdk = 36

  defaultConfig {
    minSdk = 28

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions { jvmTarget = "1.8" }
  buildFeatures { compose = true }
  composeOptions { kotlinCompilerExtensionVersion = "1.5.14" }
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)

  // Android Auto dependency
  api(libs.androidx.car.app)

  // MapLibre Compose dependency
  api(project(":compose"))

  testImplementation(libs.junit)
  testImplementation(libs.mockk)

  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.ui.test)
  androidTestImplementation(libs.androidx.ui.test.junit4)
}
