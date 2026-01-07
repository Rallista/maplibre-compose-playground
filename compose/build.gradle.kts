import com.vanniktech.maven.publish.AndroidSingleVariantLibrary

plugins {
  alias(libs.plugins.androidLibrary)
  alias(libs.plugins.jetbrainsKotlinAndroid)
  alias(libs.plugins.kotlinParcelize)
  alias(libs.plugins.ktfmt)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.mavenPublish)
  id("maven-publish")
  id("CommonPomConventionPlugin")
}

android {
  namespace = "com.maplibre.compose"
  compileSdk = 36

  defaultConfig {
    minSdk = 25

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
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  buildFeatures { compose = true }
  composeOptions { kotlinCompilerExtensionVersion = "1.5.14" }
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.material3)

  api(libs.maplibre)
  api(libs.maplibre.annotation)

  testImplementation(libs.junit)
  testImplementation(libs.mockk)

  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.ui.test)
  androidTestImplementation(libs.androidx.ui.test.junit4)

  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)
}

mavenPublishing {
  publishToMavenCentral()
  signAllPublications()

  coordinates("io.github.rallista", "maplibre-compose", project.version.toString())

  configure(AndroidSingleVariantLibrary(sourcesJar = true, publishJavadocJar = true))
}

mavenPublishing {
  pom {
    name.set("Maplibre Compose")
    description.set("Composable UI wrapper for Maplibre-Native Android")
  }
}
