package com.maplibre.compose.runtime.nodes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.currentComposer
import com.maplibre.compose.camera.CameraState
import com.maplibre.compose.camera.MapViewCamera
import com.maplibre.compose.camera.MapViewCameraDefaults
import com.maplibre.compose.camera.extensions.fromMapLibre
import com.maplibre.compose.camera.extensions.needsUpdate
import com.maplibre.compose.camera.extensions.toCameraMode
import com.maplibre.compose.camera.extensions.toCameraPosition
import com.maplibre.compose.camera.extensions.toMapLibre
import com.maplibre.compose.camera.models.CameraMotion
import com.maplibre.compose.camera.models.CameraPadding
import com.maplibre.compose.camera.models.CameraPitchRange
import com.maplibre.compose.ramani.MapApplier
import org.maplibre.android.camera.CameraUpdate
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.location.OnLocationCameraTransitionListener
import org.maplibre.android.location.modes.CameraMode
import org.maplibre.android.location.modes.RenderMode
import org.maplibre.android.maps.MapLibreMap

@Composable
internal fun MapCameraNode(camera: MutableState<MapViewCamera>) {

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
                  direction = mapApplier.map.cameraPosition.bearing,
                  motion = MapViewCameraDefaults.MOTION),
              pitchRange =
                  CameraPitchRange.fromMapLibre(mapApplier.map.minPitch, mapApplier.map.maxPitch),
              padding = CameraPadding.doubleArray(mapApplier.map.cameraPosition.padding)))
    }
  }

  observeIdle {
    // Getting this to update properly can be tricky. This is a good explanation of why
    // https://stackoverflow.com/questions/77012816/what-are-the-requirements-for-changes-to-a-mutablestate-to-properly-trigger-reco
    camera.value = it
  }

  MapComposeNode(camera, mapApplier, ::cameraUpdate)
}

private class CameraTransitionListener(
    val map: MapLibreMap,
    val zoom: Double?,
    val tilt: Double?,
    val padding: DoubleArray?
) : OnLocationCameraTransitionListener {
  override fun onLocationCameraTransitionFinished(cameraMode: Int) {
    // Update only if the value has changed.
    zoom?.let { zoom ->
      if (map.cameraPosition.zoom != zoom) {
        map.locationComponent.zoomWhileTracking(zoom)
      }
    }
    tilt?.let { tilt ->
      if (map.cameraPosition.tilt != tilt) {
        map.locationComponent.tiltWhileTracking(tilt)
      }
    }
    padding?.let { padding ->
      if (!map.cameraPosition.padding.contentEquals(padding)) {
        map.locationComponent.paddingWhileTracking(padding)
      }
    }
  }

  override fun onLocationCameraTransitionCanceled(cameraMode: Int) {
    // Do nothing
  }
}

private fun cameraUpdate(map: MapLibreMap, camera: MapViewCamera) {
  // This can technically fail (per the native API signature of `getCameraForLatLngBounds`),
  // so we may need to bail early.
  // The failure conditions are not documented,
  // but we assume they relate to things like invalid bounding boxes.
  val cameraPosition = camera.toCameraPosition(map) ?: return

  val cameraUpdate: CameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)

  val newPadding = camera.padding.toDoubleArray()

  // Handle values for all cases not in CameraPosition (pitchRange)
  // Pitch and pitch range are validated on their own types.
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
      when (camera.state.motion) {
        is CameraMotion.Instant -> map.moveCamera(cameraUpdate)
        is CameraMotion.Ease -> {
          map.easeCamera(cameraUpdate, camera.state.motion.animationDurationMs)
        }
        is CameraMotion.Fly -> {
          map.animateCamera(cameraUpdate, camera.state.motion.animationDurationMs)
        }
      }
    }

    // The new-updated camera transitions to a bounding box
    is CameraState.BoundingBox -> {
      // Unset any tracking modes.
      if (map.locationComponent.isLocationComponentActivated) {
        map.locationComponent.cameraMode = CameraMode.NONE
      }
      // Apply the camera update to the map using the correct motion type.
      when (camera.state.motion) {
        is CameraMotion.Instant -> map.moveCamera(cameraUpdate)
        is CameraMotion.Ease -> {
          map.easeCamera(cameraUpdate, camera.state.motion.animationDurationMs)
        }
        is CameraMotion.Fly -> {
          map.animateCamera(cameraUpdate, camera.state.motion.animationDurationMs)
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

      if (camera.state.needsUpdate(
          map.locationComponent.cameraMode, map.cameraPosition.zoom, map.cameraPosition.tilt) ||
          !map.cameraPosition.padding.contentEquals(newPadding)) {
        map.locationComponent.setCameraMode(
            camera.state.toCameraMode(),
            CameraTransitionListener(map, camera.state.zoom, camera.state.pitch, newPadding))
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

      if (camera.state.needsUpdate(
          map.locationComponent.cameraMode, map.cameraPosition.zoom, map.cameraPosition.tilt) ||
          !map.cameraPosition.padding.contentEquals(newPadding)) {
        map.locationComponent.setCameraMode(
            camera.state.toCameraMode(),
            CameraTransitionListener(map, camera.state.zoom, camera.state.pitch, newPadding))
      }
    }
  }
}
