package com.maplibre.compose.ramani

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.currentComposer
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.location.OnLocationCameraTransitionListener
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.maplibre.compose.camera.CameraMotionType
import com.maplibre.compose.camera.CameraPitch
import com.maplibre.compose.camera.CameraPosition
import com.maplibre.compose.camera.CameraTrackingMode

@Composable
internal fun MapCameraUpdater(cameraPosition: MutableState<CameraPosition>) {

  val mapApplier = currentComposer.applier as MapApplier

  fun observeIdle(onCameraIdle: (CameraPosition) -> Unit) {
    mapApplier.map.addOnCameraIdleListener {
      // Safely calculate the new tracking mode.
      var newTrackingMode = CameraTrackingMode.NONE
      if (mapApplier.map.locationComponent.isLocationComponentActivated) {
        newTrackingMode = CameraTrackingMode.fromMapbox(mapApplier.map.locationComponent.cameraMode)
      }

      // Generate a new camera position here. This helps trigger an update to the MutableState.
      // See stack overflow link below for more info.
      onCameraIdle(
          CameraPosition(
              target = mapApplier.map.cameraPosition.target,
              zoom = mapApplier.map.cameraPosition.zoom,
              tilt = mapApplier.map.cameraPosition.tilt,
              // TODO: This should PROBABLY not be hard-coded? I'm also not really sure if pitch
              // constraints are part of position...
              pitch = CameraPitch.Free,
              bearing = mapApplier.map.cameraPosition.bearing,
              trackingMode = newTrackingMode,
          ))
    }
  }

  observeIdle {
    // Getting this to update properly can be tricky. This is a good explanation of why
    // https://stackoverflow.com/questions/77012816/what-are-the-requirements-for-changes-to-a-mutablestate-to-properly-trigger-reco
    cameraPosition.value = it
  }

  ComposeNode<MapPropertiesNode, MapApplier>(
      factory = { MapPropertiesNode(mapApplier.map, cameraPosition) },
      update = {
        // This function is run any time the cameraPosition changes.
        // It applies an update from the parent to the Map (maintained by the MapApplier)
        update(cameraPosition.value) { updatedCameraPosition ->
          cameraUpdate(map, updatedCameraPosition)
        }
      })
}

private class CameraTransitionListener(val map: MapboxMap, val zoom: Double?, val tilt: Double?) :
    OnLocationCameraTransitionListener {
  override fun onLocationCameraTransitionFinished(cameraMode: Int) {
    zoom?.let { zoom -> map.locationComponent.zoomWhileTracking(zoom) }

    tilt?.let { tilt -> map.locationComponent.tiltWhileTracking(tilt) }
  }

  override fun onLocationCameraTransitionCanceled(cameraMode: Int) {
    // Do nothing
  }
}

private class MapPropertiesNode(
    val map: MapboxMap,
    var cameraPosition: MutableState<CameraPosition>
) : MapNode {
  override fun onAttached() {
    cameraUpdate(map, cameraPosition.value)
  }
}

private fun cameraUpdate(map: MapboxMap, cameraPosition: CameraPosition) {
  val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition.toMapbox())

  when (cameraPosition.trackingMode) {
    CameraTrackingMode.NONE -> {
      if (map.locationComponent.isLocationComponentActivated) {
        map.locationComponent.cameraMode = CameraMode.NONE
      }
      when (cameraPosition.motionType) {
        CameraMotionType.INSTANT -> map.moveCamera(cameraUpdate)

        CameraMotionType.EASE -> map.easeCamera(cameraUpdate, cameraPosition.animationDurationMs)

        CameraMotionType.FLY -> map.animateCamera(cameraUpdate, cameraPosition.animationDurationMs)
      }
    }

    CameraTrackingMode.FOLLOW -> {
      assert(map.locationComponent.isLocationComponentActivated)

      map.locationComponent.renderMode = RenderMode.COMPASS
      if (map.locationComponent.cameraMode != CameraMode.TRACKING) {
        map.locationComponent.setCameraMode(
            CameraMode.TRACKING,
            CameraTransitionListener(map, cameraPosition.zoom, cameraPosition.tilt))
      }
    }

    CameraTrackingMode.FOLLOW_WITH_BEARING -> {
      assert(map.locationComponent.isLocationComponentActivated)

      map.locationComponent.renderMode = RenderMode.GPS
      if (map.locationComponent.cameraMode != CameraMode.TRACKING_GPS) {
        map.locationComponent.setCameraMode(
            CameraMode.TRACKING_GPS,
            CameraTransitionListener(map, cameraPosition.zoom, cameraPosition.tilt))
      }
    }
  }
}
