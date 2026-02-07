package com.maplibre.example.auto

import android.util.Log
import androidx.car.app.CarContext
import androidx.car.app.model.Action
import androidx.car.app.model.ActionStrip
import androidx.car.app.model.MessageTemplate
import androidx.car.app.model.Template
import androidx.car.app.navigation.model.MapWithContentTemplate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.maplibre.compose.MapView
import com.maplibre.compose.camera.MapViewCamera
import com.maplibre.compose.car.ComposableScreen
import com.maplibre.compose.rememberSaveableMapViewCamera
import com.maplibre.compose.symbols.Circle
import com.maplibre.compose.symbols.CircleWithItem
import com.maplibre.compose.symbols.Polyline
import com.maplibre.compose.symbols.Symbol
import com.maplibre.compose.symbols.builder.SymbolText
import com.maplibre.compose.symbols.models.SymbolOffset
import com.maplibre.example.R
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMapOptions
import org.maplibre.android.style.layers.Property.TEXT_ANCHOR_BOTTOM_RIGHT

class SymbolExampleScreen(carContext: CarContext, private val onNavigateBack: () -> Unit) :
    ComposableScreen(carContext, surfaceTag = TAG) {

  companion object {
    private const val TAG = "SymbolExampleScreen"
  }

  @Composable
  override fun content() {
    val mapViewCamera =
        rememberSaveableMapViewCamera(
            initialCamera =
                MapViewCamera.Centered(latitude = 1.227, longitude = 103.962, zoom = 10.0))

    MapView(
        modifier = Modifier.fillMaxSize(),
        styleUrl = "https://demotiles.maplibre.org/style.json",
        mapOptions = MapLibreMapOptions.createFromAttributes(carContext).pixelRatio(3f),
        camera = mapViewCamera,
        onTapGestureCallback = { Log.d(TAG, "Tapped at $it") }) {
          // Red star symbol with rotation
          Symbol(
              center = LatLng(1.203, 103.873),
              size = 2f,
              imageId = R.drawable.bitmap,
              imageRotation = -20f,
              onTap = { Log.d(TAG, "Tapped red star") },
              onLongPress = { Log.d(TAG, "Long pressed red star") })

          // Blue circle
          Circle(center = LatLng(1.253, 104.019), radius = 2f, color = "Blue")

          // Blue star symbol with offset
          Symbol(
              center = LatLng(1.253, 104.019),
              imageId = R.drawable.vector,
              imageOffset = SymbolOffset(-20f, 20f),
              onTap = { Log.d(TAG, "Tapped blue star") },
              onLongPress = { Log.d(TAG, "Long pressed blue star") })

          // Circle with default text
          CircleWithItem(
              center = LatLng(1.173, 103.969),
              radius = 10f,
              text = SymbolText.text("Default"),
              onTap = { Log.d(TAG, "Tapped default circle") },
              onLongPress = { Log.d(TAG, "Long pressed default circle") })

          // Circle with custom styled text
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
              onTap = { Log.d(TAG, "Tapped custom circle") },
              onLongPress = { Log.d(TAG, "Long pressed custom circle") })

          // Polyline with arrow pattern
          Polyline(
              points =
                  listOf(
                      LatLng(1.147, 103.813),
                      LatLng(1.259, 103.887),
                      LatLng(1.205, 103.931),
                      LatLng(1.295, 103.993)),
              lineWidth = 12f,
              linePatternId = R.drawable.arrow,
              opacity = 0.5f)
        }
  }

  override fun onGetTemplate(): Template {
    return MapWithContentTemplate.Builder()
        .setContentTemplate(MessageTemplate.Builder("Symbol Example").build())
        .setActionStrip(
            ActionStrip.Builder()
                .addAction(
                    Action.Builder()
                        .setTitle("View Camera")
                        .setOnClickListener {
                          Log.d(TAG, "Navigating back to CameraExampleScreen")
                          onNavigateBack()
                        }
                        .build())
                .build())
        .build()
  }
}
