package com.maplibre.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.maplibre.example.ui.theme.MaplibreComposeTheme

// FLAG: MapLibreStyleProviding / MapLibreSystemThemeStyleProvider / mapLibreStyleUrl() from the old
// library have no equivalent in the new maplibre-compose. Using a simple CompositionLocal instead.
val LocalMapStyleUrl = compositionLocalOf<String> { error("No map style URL provided") }

@Composable
fun ProvideMapStyle(lightUrl: String, darkUrl: String, content: @Composable () -> Unit) {
  val url = if (isSystemInDarkTheme()) darkUrl else lightUrl
  CompositionLocalProvider(LocalMapStyleUrl provides url) { content() }
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    installSplashScreen()
    enableEdgeToEdge()

    // Get the API key from the gitignored resources file (res/values/api_keys.xml)
    // See README.md for more information on how to set an API key.
    val apiKey = getString(R.string.map_style_key)

    setContent {
      ProvideMapStyle(
          lightUrl = "https://tiles.stadiamaps.com/styles/alidade_smooth.json?api_key=$apiKey",
          darkUrl =
              "https://tiles.stadiamaps.com/styles/alidade_smooth_dark.json?api_key=$apiKey") {
            MaplibreComposeTheme {
              Surface(
                  modifier = Modifier.fillMaxSize(),
                  color = MaterialTheme.colorScheme.background) {
                    Main()
                  }
            }
          }
    }
  }
}
