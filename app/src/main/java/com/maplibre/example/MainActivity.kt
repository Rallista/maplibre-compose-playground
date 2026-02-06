package com.maplibre.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.maplibre.compose.MapLibreStyleProviding
import com.maplibre.compose.MapLibreSystemThemeStyleProvider
import com.maplibre.example.ui.theme.MaplibreComposeTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    installSplashScreen()
    enableEdgeToEdge()

    // Get the API key from the gitignored resources file (res/values/api_keys.xml)
    // See README.md for more information on how to set an API key.
    val apiKey = getString(R.string.map_style_key)

    // Create a dynamic style provider
    val mapLibreStyleProvider =
        MapLibreSystemThemeStyleProvider(
            lightModeStyleUrl =
                "https://tiles.stadiamaps.com/styles/alidade_smooth.json?api_key=$apiKey",
            darkModeStyleUrl =
                "https://tiles.stadiamaps.com/styles/alidade_smooth_dark.json?api_key=$apiKey")

    setContent {
      MapLibreStyleProviding(mapLibreStyleProvider) {
        MaplibreComposeTheme {
          // A surface container using the 'background' color from the theme
          Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Main()
          }
        }
      }
    }
  }
}
