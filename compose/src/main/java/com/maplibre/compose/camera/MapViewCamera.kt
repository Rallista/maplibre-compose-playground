package com.maplibre.compose.camera

import android.os.Parcelable
import com.maplibre.compose.camera.extensions.validPitch
import com.maplibre.compose.camera.extensions.validZoom
import com.maplibre.compose.camera.models.CameraMotion
import com.maplibre.compose.camera.models.CameraPadding
import com.maplibre.compose.camera.models.CameraPitchRange
import kotlinx.parcelize.Parcelize

sealed class MapViewCameraDefaults {
  companion object {
    const val ZOOM: Double = 10.0
    const val MIN_ZOOM: Double = 0.0
    const val MAX_ZOOM: Double =
        24.0 // mbgl/util/constants.hpp#L37 says 25.5, but the map goes dark above 24

    const val PITCH: Double = 0.0
    const val MIN_PITCH: Double = 0.0
    const val MAX_PITCH: Double = 60.0
    val PITCH_RANGE: CameraPitchRange = CameraPitchRange.Free
    const val DIRECTION: Double = 0.0
    val PADDING: CameraPadding = CameraPadding()
    val MOTION: CameraMotion = CameraMotion.Fly(1000)
  }
}

@Parcelize
data class MapViewCamera(
    val state: CameraState =
        CameraState.Centered(
            latitude = 0.0,
            longitude = 0.0,
            zoom = MapViewCameraDefaults.ZOOM,
            pitch = MapViewCameraDefaults.PITCH,
            direction = MapViewCameraDefaults.DIRECTION),
    val pitchRange: CameraPitchRange = MapViewCameraDefaults.PITCH_RANGE,
    val padding: CameraPadding = MapViewCameraDefaults.PADDING
    // TODO: Last change reason (See
    // https://github.com/stadiamaps/maplibre-swiftui-dsl-playground/blob/main/Sources/MapLibreSwiftUI/Models/MapCamera/MapViewCamera.swift#L4C1-L53C2)
) : Parcelable {

  companion object {
    val Default = MapViewCamera()

    fun Centered(
        latitude: Double,
        longitude: Double,
        zoom: Double = MapViewCameraDefaults.ZOOM,
        pitch: Double = MapViewCameraDefaults.PITCH,
        direction: Double = MapViewCameraDefaults.DIRECTION,
        pitchRange: CameraPitchRange = MapViewCameraDefaults.PITCH_RANGE,
        padding: CameraPadding = MapViewCameraDefaults.PADDING,
        motion: CameraMotion = MapViewCameraDefaults.MOTION
    ) =
        MapViewCamera(
            CameraState.Centered(
                latitude, longitude, validZoom(zoom), validPitch(pitch), direction, motion),
            pitchRange,
            padding)

    fun TrackingUserLocation(
        zoom: Double = MapViewCameraDefaults.ZOOM,
        pitch: Double = MapViewCameraDefaults.PITCH,
        direction: Double = MapViewCameraDefaults.DIRECTION,
        pitchRange: CameraPitchRange = MapViewCameraDefaults.PITCH_RANGE,
        padding: CameraPadding = MapViewCameraDefaults.PADDING
    ) =
        MapViewCamera(
            CameraState.TrackingUserLocation(validZoom(zoom), validPitch(pitch), direction),
            pitchRange,
            padding)

    fun TrackingUserLocationWithBearing(
        zoom: Double = MapViewCameraDefaults.ZOOM,
        pitch: Double = MapViewCameraDefaults.PITCH,
        pitchRange: CameraPitchRange = MapViewCameraDefaults.PITCH_RANGE,
        padding: CameraPadding = MapViewCameraDefaults.PADDING
    ) =
        MapViewCamera(
            CameraState.TrackingUserLocationWithBearing(validZoom(zoom), validPitch(pitch)),
            pitchRange,
            padding)
  }
}


