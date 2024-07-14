package com.maplibre.compose.ramani

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.currentComposer
import com.maplibre.compose.camera.CameraState
import com.maplibre.compose.camera.MapViewCamera
import com.maplibre.compose.camera.extensions.needsUpdate
import com.maplibre.compose.camera.extensions.toCameraMode
import com.maplibre.compose.camera.extensions.toCameraPosition
import com.maplibre.compose.camera.extensions.toMapLibre
import com.maplibre.compose.camera.models.CameraMotion
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.location.OnLocationCameraTransitionListener
import org.maplibre.android.location.modes.CameraMode
import org.maplibre.android.location.modes.RenderMode
import org.maplibre.android.maps.MapLibreMap

@Composable
internal fun MapCameraUpdater(camera: MutableState<MapViewCamera>) {

  val mapApplier = currentComposer.applier as MapApplier

  fun observeIdle(onCameraIdle: (MapViewCamera) -> Unit) {
    mapApplier.map.addOnCameraIdleListener {
      // Safely calculate the new tracking mode.
      var newCameraMode = CameraMode.NONE
      if (mapApplier.map.locationComponent.isLocationComponentActivated) {
        newCameraMode = mapApplier.map.locationComponent.cameraMode
      }

      // Exit early if the tracking mode is set to follow the user. We don't need to propagate
      // the camera position up the tree in this case. Once the user pans the map, they'll exit
      // tracking and we'll need to update the camera position.
      if (newCameraMode != CameraMode.NONE) {
        return@addOnCameraIdleListener
      }

      // Exit early if the camera target is null. This will result in a NullPointerException
      // on the next applied MapLibre CameraPosition.
      // TODO: Should we log/throw an error here?
      val target = mapApplier.map.cameraPosition.target ?: return@addOnCameraIdleListener

      // Generate a new camera position here. This helps trigger an update to the MutableState.
      // See stack overflow link below for more info.
      onCameraIdle(
          MapViewCamera(
              CameraState.Centered(
                  latitude = target.latitude,
                  longitude = target.longitude,
                  zoom = mapApplier.map.cameraPosition.zoom,
                  pitch = mapApplier.map.cameraPosition.tilt,
                  direction = mapApplier.map.cameraPosition.bearing)))
    }
  }

  observeIdle {
    // Getting this to update properly can be tricky. This is a good explanation of why
    // https://stackoverflow.com/questions/77012816/what-are-the-requirements-for-changes-to-a-mutablestate-to-properly-trigger-reco
    camera.value = it
  }

  ComposeNode<MapPropertiesNode, MapApplier>(
      factory = { MapPropertiesNode(mapApplier.map, camera) },
      update = {
        // This function is run any time the cameraPosition changes.
        // It applies an update from the parent to the Map (maintained by the MapApplier)
        update(camera.value) { updatedCameraPosition -> cameraUpdate(map, updatedCameraPosition) }
      })
}

private class CameraTransitionListener(val map: MapLibreMap, val zoom: Double?, val tilt: Double?) :
    OnLocationCameraTransitionListener {
  override fun onLocationCameraTransitionFinished(cameraMode: Int) {
    zoom?.let { zoom -> map.locationComponent.zoomWhileTracking(zoom) }
    tilt?.let { tilt -> map.locationComponent.tiltWhileTracking(tilt) }
  }

  override fun onLocationCameraTransitionCanceled(cameraMode: Int) {
    // Do nothing
  }
}

private class MapPropertiesNode(val map: MapLibreMap, var camera: MutableState<MapViewCamera>) :
    MapNode {
  override fun onAttached() {
    cameraUpdate(map, camera.value)
  }
}

private fun cameraUpdate(map: MapLibreMap, camera: MapViewCamera) {
  val cameraUpdate = CameraUpdateFactory.newCameraPosition(camera.toCameraPosition())

  // Handle values for all cases not in CameraPosition (pitchRange)
  // TODO: Test how an invalid pitch value is handled when pitchRange is more restrictive.
  camera.pitchRange.toMapLibre().let {
    map.setMinPitchPreference(it.first)
    map.setMaxPitchPreference(it.second)
  }

  when (camera.state) {
    // The new-updated camera is centered on a specific location.
    is CameraState.Centered -> {
      // Unset any tracking modes.
      if (map.locationComponent.isLocationComponentActivated) {
        map.locationComponent.cameraMode = CameraMode.NONE
      }
      // Apply the camera update to the map using the correct motion type.
      val value = camera.state as CameraState.Centered
      when (camera.motion) {
        is CameraMotion.Instant -> map.moveCamera(cameraUpdate)
        is CameraMotion.Ease -> {
          val motion = camera.motion as CameraMotion.Ease
          map.easeCamera(cameraUpdate, motion.animationDurationMs)
        }
        is CameraMotion.Fly -> {
          val motion = camera.motion as CameraMotion.Fly
          map.animateCamera(cameraUpdate, motion.animationDurationMs)
        }
      }
    }

    // The new-updated camera is tracking the user's location.
    is CameraState.TrackingUserLocation -> {
      // Ensure the location component is activated before we manipulate any locationComponent
      // values.
      assert(map.locationComponent.isLocationComponentActivated)
      // Set the render mode to compass, this is the style of the user location icon. Not the
      // which camera mode.
      map.locationComponent.renderMode = RenderMode.COMPASS

      val value = camera.state as CameraState.TrackingUserLocation
      if (value.needsUpdate(
          map.locationComponent.cameraMode, map.cameraPosition.zoom, map.cameraPosition.tilt)) {
        map.locationComponent.setCameraMode(
            value.toCameraMode(), CameraTransitionListener(map, value.zoom, value.pitch))
      }
    }

    // The new-updated camera is tracking the user's location with bearing.
    is CameraState.TrackingUserLocationWithBearing -> {
      // Ensure the location component is activated before we manipulate any locationComponent
      // values.
      assert(map.locationComponent.isLocationComponentActivated)
      // Set the render mode to compass, this is the style of the user location icon. Not the
      // which camera mode.
      map.locationComponent.renderMode = RenderMode.GPS

      val value = camera.state as CameraState.TrackingUserLocationWithBearing
      if (value.needsUpdate(
          map.locationComponent.cameraMode, map.cameraPosition.zoom, map.cameraPosition.tilt)) {
        map.locationComponent.setCameraMode(
            value.toCameraMode(), CameraTransitionListener(map, value.zoom, value.pitch))
      }
    }
  }
}
