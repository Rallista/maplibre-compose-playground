package com.maplibre.compose.ramani

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.currentComposer
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.maplibre.compose.camera.CameraMotionType
import com.maplibre.compose.camera.CameraPitch
import com.maplibre.compose.camera.CameraPosition
import com.maplibre.compose.camera.CameraTrackingMode

@Composable
internal fun MapCameraUpdater(
    cameraPosition: MutableState<CameraPosition>
) {

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
                pitch = CameraPitch.Free,
                bearing = mapApplier.map.cameraPosition.bearing,
                trackingMode = newTrackingMode,
            )
            )
        }
    }

    observeIdle {
        // Getting this to update properly can be tricky. This is a good explanation of why
        // https://stackoverflow.com/questions/77012816/what-are-the-requirements-for-changes-to-a-mutablestate-to-properly-trigger-reco
        cameraPosition.value = it
    }

    ComposeNode<MapPropertiesNode, MapApplier>(factory = {
        MapPropertiesNode(mapApplier.map, cameraPosition)
    }, update = {
        // This function is run any time the cameraPosition changes.
        // It applies an update from the parent to the Map (maintained by the MapApplier)
        update(cameraPosition.value) {
            val cameraUpdate = CameraUpdateFactory.newCameraPosition(it.toMapbox())

            when (it.trackingMode) {
                CameraTrackingMode.NONE -> {
                    if (map.locationComponent.isLocationComponentActivated) {
                        map.locationComponent.cameraMode = CameraMode.NONE
                    }

                    when (it.motionType) {
                        CameraMotionType.INSTANT -> map.moveCamera(cameraUpdate)

                        CameraMotionType.EASE -> map.easeCamera(
                            cameraUpdate,
                            it.animationDurationMs
                        )

                        CameraMotionType.FLY -> map.animateCamera(
                            cameraUpdate,
                            it.animationDurationMs
                        )
                    }
                }
                CameraTrackingMode.FOLLOW -> {
                    assert(map.locationComponent.isLocationComponentActivated)
                    map.locationComponent.cameraMode = CameraMode.TRACKING
                    map.locationComponent.renderMode = RenderMode.COMPASS
                }
                CameraTrackingMode.FOLLOW_WITH_BEARING -> {
                    assert(map.locationComponent.isLocationComponentActivated)
                    map.locationComponent.cameraMode = CameraMode.TRACKING_GPS
                    map.locationComponent.renderMode = RenderMode.COMPASS
                }
            }
        }
    })
}

private class MapPropertiesNode(
    val map: MapboxMap,
    var cameraPosition: MutableState<CameraPosition>
) : MapNode {
    override fun onAttached() {
        when (cameraPosition.value.trackingMode) {
            CameraTrackingMode.NONE -> {
                if (map.locationComponent.isLocationComponentActivated) {
                    map.locationComponent.cameraMode = CameraMode.NONE
                }
                map.cameraPosition = cameraPosition.value.toMapbox()
            }
            CameraTrackingMode.FOLLOW -> {
                assert(map.locationComponent.isLocationComponentActivated)
                map.locationComponent.cameraMode = CameraMode.TRACKING
                map.locationComponent.renderMode = RenderMode.COMPASS
            }
            CameraTrackingMode.FOLLOW_WITH_BEARING -> {
                assert(map.locationComponent.isLocationComponentActivated)
                map.locationComponent.cameraMode = CameraMode.TRACKING_GPS
                map.locationComponent.renderMode = RenderMode.COMPASS
            }
        }
    }
}