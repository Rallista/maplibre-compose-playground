package com.maplibre.example.car

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.ActionStrip
import androidx.car.app.model.Template
import androidx.car.app.navigation.model.NavigationTemplate
import android.location.Location
import com.maplibre.compose.StaticLocationEngine
import com.maplibre.compose.car.CarMapViewContainer
import com.maplibre.compose.car.MapSurfaceCallback
import org.maplibre.android.location.LocationComponentActivationOptions
import org.maplibre.android.location.modes.CameraMode
import org.maplibre.android.location.modes.RenderMode

/**
 * Screen that displays a MapLibre map in Android Auto using the Navigation template.
 *
 * This demonstrates the high-performance rendering approach using VirtualDisplay and Presentation
 * as described in: https://github.com/maplibre/MapLibre-Android-Auto-Sample/pull/13
 */
class MapLibreCarScreen(carContext: CarContext) : Screen(carContext) {

  // Track current camera mode
  private var currentCameraMode = CameraMode.NONE
  
  // Create a static location engine for demo (centered on San Francisco)
  private val locationEngine = StaticLocationEngine().apply {
    lastLocation = Location("demo").apply {
      latitude = 37.7749
      longitude = -122.4194
      bearing = 45f
    }
  }
  
  // Create the map container
  private val mapContainer: CarMapViewContainer = CarMapViewContainer(
      carContext = carContext,
      styleUrl = "https://demotiles.maplibre.org/style.json",
      onMapReady = { map, style ->
        // Map is ready - center on San Francisco
        map.cameraPosition =
            org.maplibre.android.camera.CameraPosition.Builder()
                .target(org.maplibre.android.geometry.LatLng(37.7749, -122.4194))
                .zoom(12.0)
                .build()
        
        // Enable location component - MUST be done after style is loaded
        val locationActivationOptions =
            LocationComponentActivationOptions.builder(carContext, style)
                .locationEngine(locationEngine)
                .build()
        
        map.locationComponent.apply {
          activateLocationComponent(locationActivationOptions)
          isLocationComponentEnabled = true
          cameraMode = CameraMode.NONE
          renderMode = RenderMode.COMPASS
        }
      })

  // Create the surface callback that will render the map
  private val surfaceCallback: MapSurfaceCallback = MapSurfaceCallback(
      context = carContext,
      mapViewProvider = { mapContainer.setupMap() },
      onCleanup = { mapContainer.cleanUpMap() })

  init {
    // Register lifecycle observer
    lifecycle.addObserver(mapContainer)
    
    // Set the surface callback once during initialization
    carContext
        .getCarService(androidx.car.app.AppManager::class.java)
        .setSurfaceCallback(surfaceCallback)
  }

  private fun getCameraModeLabel(): String {
    return when (currentCameraMode) {
      CameraMode.NONE -> "Fixed Position"
      CameraMode.TRACKING -> "Tracking User"
      CameraMode.TRACKING_COMPASS -> "Tracking with Bearing"
      CameraMode.TRACKING_GPS -> "Tracking GPS"
      CameraMode.TRACKING_GPS_NORTH -> "Tracking GPS North"
      else -> "Unknown Mode"
    }
  }

  private fun getNextCameraMode(): Int {
    return when (currentCameraMode) {
      CameraMode.NONE -> CameraMode.TRACKING
      CameraMode.TRACKING -> CameraMode.TRACKING_COMPASS
      else -> CameraMode.NONE
    }
  }

  override fun onGetTemplate(): Template {
    return NavigationTemplate.Builder()
        .setActionStrip(
            ActionStrip.Builder()
                .addAction(
                    Action.Builder()
                        .setTitle(getCameraModeLabel())
                        .setOnClickListener {
                          mapContainer.getMapLibreMap()?.let { map ->
                            currentCameraMode = getNextCameraMode()
                            map.locationComponent.cameraMode = currentCameraMode
                            map.locationComponent.renderMode = RenderMode.COMPASS
                            // Note: We need invalidate() to update the button label,
                            // but this is called in the .also block below after the template is built
                            invalidate()
                          }
                        }
                        .build())
                .addAction(
                    Action.Builder()
                        .setTitle("Zoom In")
                        .setOnClickListener {
                          mapContainer.getMapLibreMap()?.let { map ->
                            val currentZoom = map.cameraPosition.zoom
                            val newPosition =
                                org.maplibre.android.camera.CameraPosition.Builder()
                                    .target(map.cameraPosition.target)
                                    .zoom(currentZoom + 1)
                                    .build()
                            map.animateCamera(
                                org.maplibre.android.camera.CameraUpdateFactory.newCameraPosition(
                                    newPosition),
                                300)
                          }
                        }
                        .build())
                .addAction(
                    Action.Builder()
                        .setTitle("Zoom Out")
                        .setOnClickListener {
                          mapContainer.getMapLibreMap()?.let { map ->
                            val currentZoom = map.cameraPosition.zoom
                            val newPosition =
                                org.maplibre.android.camera.CameraPosition.Builder()
                                    .target(map.cameraPosition.target)
                                    .zoom(currentZoom - 1)
                                    .build()
                            map.animateCamera(
                                org.maplibre.android.camera.CameraUpdateFactory.newCameraPosition(
                                    newPosition),
                                300)
                          }
                        }
                        .build())
                .build())
        .setMapActionStrip(ActionStrip.Builder().addAction(Action.PAN).build())
        .build()
  }
}
