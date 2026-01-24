import com.vanniktech.maven.publish.AndroidSingleVariantLibrary

plugins {
  alias(libs.plugins.androidLibrary)
  alias(libs.plugins.jetbrainsKotlinAndroid)
  alias(libs.plugins.ktfmt)
  alias(libs.plugins.mavenPublish)
  id("maven-publish")
  id("CommonPomConventionPlugin")
}

android {
  namespace = "com.maplibre.compose.car"
  compileSdk { version = release(36) }

  defaultConfig {
    minSdk = 29

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
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }
  kotlin { jvmToolchain(21) }
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  // Important!
  // This library MUST NOT use libs.androidx.car.app.automotive
  // if we want it to remain compatible with Android Auto.
  implementation(libs.androidx.car.app)

  implementation(platform(libs.androidx.compose.bom))
  implementation(project(":compose"))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.compose.runtime)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
}

mavenPublishing {
  publishToMavenCentral()
  signAllPublications()

  coordinates("io.github.rallista", "maplibre-compose-car-app", project.version.toString())

  configure(AndroidSingleVariantLibrary(sourcesJar = true, publishJavadocJar = true))
}

mavenPublishing {
  pom {
    name.set("Maplibre Compose Car App")
    description.set("Android Automotive and Auto extensions for Maplibre Compose")
  }
}
