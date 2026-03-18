package com.maplibre.example.examples

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maplibre.compose.MapView
import com.maplibre.compose.camera.MapViewCamera
import com.maplibre.compose.rememberSaveableMapViewCamera

private enum class MapStyleOption(val label: String, val styleUrl: String) {
  Demo(label = "MapLibre", styleUrl = "https://demotiles.maplibre.org/style.json"),
  Liberty(label = "Liberty", styleUrl = "https://tiles.openfreemap.org/styles/liberty")
}

@Composable
fun StyleSwitchingExample() {
  var selectedStyle by remember { mutableStateOf(MapStyleOption.Demo) }

  val camera =
      rememberSaveableMapViewCamera(
          initialCamera = MapViewCamera.Centered(latitude = 48.1, longitude = 11.6, zoom = 7.0))

  Box(modifier = Modifier.fillMaxSize()) {
    MapView(modifier = Modifier.fillMaxSize(), styleUrl = selectedStyle.styleUrl, camera = camera)

    SingleChoiceSegmentedButtonRow(
        modifier =
            Modifier.align(Alignment.BottomCenter).systemBarsPadding().padding(bottom = 16.dp)) {
          MapStyleOption.entries.forEachIndexed { index, option ->
            SegmentedButton(
                selected = selectedStyle == option,
                onClick = { selectedStyle = option },
                shape =
                    SegmentedButtonDefaults.itemShape(
                        index = index, count = MapStyleOption.entries.size)) {
                  Text(option.label)
                }
          }
        }
  }
}

@Preview
@Composable
fun StyleSwitchingExamplePreview() {
  StyleSwitchingExample()
}
