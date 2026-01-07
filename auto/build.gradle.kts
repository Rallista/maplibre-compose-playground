plugins {
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.jetbrainsKotlinAndroid)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.ktfmt)
}

android {
  namespace = "com.maplibre.auto"
  compileSdk = 36

  defaultConfig {
    applicationId = "com.maplibre.auto"
    minSdk = 29
    targetSdk = 36
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  buildFeatures { compose = true }
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)

  // Compose dependencies
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.material3)

  // MapLibre Compose
  implementation(project(":compose"))
  implementation(project(":compose-car-app"))

  // Android Auto
  implementation(libs.androidx.car.app)
  // Used for the CarAppActivity in `AndroidManifest.xml` only.
  implementation(libs.androidx.car.app.automotive)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
}
