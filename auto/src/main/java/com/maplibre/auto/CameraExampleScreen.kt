package com.maplibre.auto

import android.location.Location
import android.util.Log
import androidx.car.app.CarContext
import androidx.car.app.model.Action
import androidx.car.app.model.ActionStrip
import androidx.car.app.model.Template
import androidx.car.app.navigation.model.NavigationTemplate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.maplibre.compose.MapView
import com.maplibre.compose.StaticLocationEngine
import com.maplibre.compose.camera.CameraState
import com.maplibre.compose.camera.MapViewCamera
import com.maplibre.compose.camera.extensions.incrementZoom
import com.maplibre.compose.camera.models.CameraPadding
import com.maplibre.compose.car.ComposableScreen
import com.maplibre.compose.rememberSynchronizedMapViewCamera

class CameraExampleScreen(carContext: CarContext) : ComposableScreen(carContext) {

  val mapViewCamera = mutableStateOf(MapViewCamera.Centered(53.4106, -2.9779))

  @Composable
  override fun content() {
    val locationEngine by lazy {
      val engine = StaticLocationEngine()
      engine.lastLocation =
          Location("static").apply {
            latitude = 66.137331
            longitude = -18.529602
          }
      engine
    }

    val trackingCameraPadding =
        CameraPadding.fractionOfScreen(top = 0.6f, start = 0.55f, end = 0.05f)
    val cameraPadding =
        CameraPadding.fractionOfScreen(start = 0.1f, top = 0.1f, bottom = 0.1f, end = 0.1f)

    MapView(
        modifier = Modifier.fillMaxSize(),
        styleUrl = "https://demotiles.maplibre.org/style.json",
        camera =
            rememberSynchronizedMapViewCamera(
                mapViewCamera,
                {
                  when (it.state) {
                    is CameraState.TrackingUserLocation,
                    is CameraState.TrackingUserLocationWithBearing ->
                        it.copy(padding = trackingCameraPadding)
                    else -> it.copy(padding = cameraPadding)
                  }
                }),
        locationEngine = remember { locationEngine })
  }

  override fun onGetTemplate(): Template {
    return NavigationTemplate.Builder()
        .setActionStrip(
            ActionStrip.Builder()
                .addAction(
                    Action.Builder()
                        .setTitle("Camera")
                        .setOnClickListener {
                          mapViewCamera.value = getNextCamera(mapViewCamera.value.state)
                          Log.d("ExampleMapScreen", "Camera value ${mapViewCamera.value}")
                        }
                        .build())
                .addAction(
                    Action.Builder()
                        .setTitle("Zoom In")
                        .setOnClickListener {
                          mapViewCamera.value = mapViewCamera.value.incrementZoom(1.0)
                        }
                        .build())
                .addAction(
                    Action.Builder()
                        .setTitle("Zoom Out")
                        .setOnClickListener {
                          mapViewCamera.value = mapViewCamera.value.incrementZoom(-1.0)
                        }
                        .build())
                .build())
        .build()
  }
}
