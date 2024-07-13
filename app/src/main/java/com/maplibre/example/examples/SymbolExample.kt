package com.maplibre.example.examples

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.style.layers.Property.TEXT_ANCHOR_BOTTOM_RIGHT
import com.maplibre.compose.MapView
import com.maplibre.compose.camera.MapViewCamera
import com.maplibre.compose.rememberSaveableMapViewCamera
import com.maplibre.compose.symbols.CircleWithItem
import com.maplibre.compose.symbols.Symbol
import com.maplibre.compose.symbols.builder.SymbolText
import com.maplibre.example.R

@Composable
fun SymbolExample() {

  val mapViewCamera =
      rememberSaveableMapViewCamera(
          initialCamera =
              MapViewCamera.Centered(latitude = 1.227, longitude = 103.962, zoom = 10.0))

  Scaffold {
    Box(modifier = Modifier.padding(it)) {
      MapView(
          styleUrl = "https://demotiles.maplibre.org/style.json",
          camera = mapViewCamera,
          onTapGestureCallback = { Log.d("SymbolExample", "Tapped at ${it}") }) {
            Symbol(
                center = LatLng(1.203, 103.873),
                size = 2f,
                imageId = R.drawable.bitmap,
                imageRotation = -20f,
                onTap = { Log.d("SymbolExample", "Tapped red star") },
                onLongPress = { Log.d("SymbolExample", "Long pressed red star") })

            Symbol(
                center = LatLng(1.253, 104.019),
                imageId = R.drawable.vector,
                imageRotation = 40f,
                onTap = { Log.d("SymbolExample", "Tapped blue star") },
                onLongPress = { Log.d("SymbolExample", "Long pressed blue star") })

            CircleWithItem(
                center = LatLng(1.173, 103.969),
                radius = 10f,
                text = SymbolText.text("Default"),
                onTap = { Log.d("SymbolExample", "Tapped default circle") },
                onLongPress = { Log.d("SymbolExample", "Long pressed default circle") })

            CircleWithItem(
                center = LatLng(1.126, 103.902),
                radius = 10f,
                text =
                    SymbolText.Builder()
                        .text("Custom")
                        .textSize(20f)
                        .textColor("Red")
                        .textAnchor(TEXT_ANCHOR_BOTTOM_RIGHT)
                        .build(),
                onTap = { Log.d("SymbolExample", "Tapped custom circle") },
                onLongPress = { Log.d("SymbolExample", "Long pressed custom circle") })
          }
    }
  }
}
