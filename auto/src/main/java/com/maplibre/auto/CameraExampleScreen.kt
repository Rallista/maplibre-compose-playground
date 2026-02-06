package com.maplibre.auto

import android.location.Location
import android.util.Log
import androidx.car.app.CarContext
import androidx.car.app.model.Action
import androidx.car.app.model.ActionStrip
import androidx.car.app.model.CarIcon
import androidx.car.app.model.MessageTemplate
import androidx.car.app.model.Template
import androidx.car.app.navigation.model.MapController
import androidx.car.app.navigation.model.MapWithContentTemplate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.graphics.drawable.IconCompat
import com.maplibre.compose.MapView
import com.maplibre.compose.StaticLocationEngine
import com.maplibre.compose.camera.CameraState
import com.maplibre.compose.camera.MapViewCamera
import com.maplibre.compose.camera.extensions.incrementZoom
import com.maplibre.compose.camera.models.CameraPadding
import com.maplibre.compose.car.ComposableScreen
import com.maplibre.compose.rememberSynchronizedMapViewCamera
import org.maplibre.android.maps.MapLibreMapOptions

class CameraExampleScreen(carContext: CarContext) :
    ComposableScreen(carContext = carContext, surfaceTag = "CameraExampleScreen") {

  companion object {
    private const val TAG = "CameraExampleScreen"
  }

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
        mapOptions = MapLibreMapOptions.createFromAttributes(carContext).pixelRatio(3f),
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
    return MapWithContentTemplate.Builder()
        .setContentTemplate(
            MessageTemplate.Builder("Camera is currently ${mapViewCamera.value.state}")
                .addAction(
                    Action.Builder()
                        .setTitle("Toggle Camera")
                        .setOnClickListener {
                          mapViewCamera.value = getNextCamera(mapViewCamera.value.state)
                          Log.d(TAG, "Camera value ${mapViewCamera.value}")
                          invalidate()
                        }
                        .build())
                .build())
        .setActionStrip(
            ActionStrip.Builder()
                .addAction(
                    Action.Builder()
                        .setTitle("View Symbols")
                        .setOnClickListener {
                          Log.d(TAG, "Navigating to SymbolExampleScreen")
                          screenManager.push(
                              SymbolExampleScreen(carContext) {
                                // Callback for navigating back
                                screenManager.pop()
                              })
                        }
                        .build())
                .build())
        .setMapController(
            MapController.Builder()
                .setMapActionStrip(
                    ActionStrip.Builder()
                        .addAction(
                            Action.Builder()
                                .setIcon(
                                    CarIcon.Builder(
                                            IconCompat.createWithResource(
                                                carContext, R.drawable.navigation_24px))
                                        .build())
                                .setOnClickListener {
                                  mapViewCamera.value = getNextCamera(mapViewCamera.value.state)
                                  invalidate()
                                }
                                .build())
                        .addAction(
                            Action.Builder()
                                .setIcon(
                                    CarIcon.Builder(
                                            IconCompat.createWithResource(
                                                carContext, R.drawable.add_24px))
                                        .build())
                                .setOnClickListener {
                                  mapViewCamera.value = mapViewCamera.value.incrementZoom(1.0)
                                  invalidate()
                                }
                                .build())
                        .addAction(
                            Action.Builder()
                                .setIcon(
                                    CarIcon.Builder(
                                            IconCompat.createWithResource(
                                                carContext, R.drawable.remove_24px))
                                        .build())
                                .setOnClickListener {
                                  mapViewCamera.value = mapViewCamera.value.incrementZoom(-1.0)
                                  invalidate()
                                }
                                .build())
                        .build())
                .build())
        .build()
  }
}
